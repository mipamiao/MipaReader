package com.example.mipareader.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.mipareader.DATA.Chapter;
import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.DataSet;
import com.example.mipareader.DATA.Dir;
import com.example.mipareader.UI.event.NovelViewTouchListener;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.UI.popwindow.My_PopupWindow;
import com.example.mipareader.R;
import com.example.mipareader.Utils.TimeRead;
import com.example.mipareader.UI.popwindow.setting_popwindow;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.RandomAccessFile;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;

import java.util.Map;
import java.util.regex.Matcher;

public class NovelWindow extends AppCompatActivity {
//1.权限的请求4美化(设置界面，多字体添加，书籍图片的自添加)5.背景的自定义6.书签7.上一页的优化8.目录popupwindow项数少美化9.语言朗读功能
    public String TAG = "栗山未来说 ";
    public String EncodeType = "utf-8";
    Map<String , Typeface> FontMap ;
    TextView tv,tv_pre,tv_next, cheaptername;
    LinearLayout mid,pre,next;
    public  int Index;
    public Data DT;
    public DataSet DS;
    public IndirectClass IC ;
    //ArrayList<Cheapter> CheapterList;
    Dir dir;

    public int OnePageRows ;
    RandomAccessFile RAF ;
    private int tvWidth,tvHeight;
    public float tv_font_extra_space = 0.2f;

    TimeRead TR;
    TextView ReadProgressTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_window);
        InitialParama();
        InitialFonts();
        try {
            RAF = new RandomAccessFile(DT.getNovelFilePath(),"r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        cheaptername = findViewById(R.id.CheapterName);
        SetCheapterName(DT.getLastReadChapterName());
        tv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tv.getViewTreeObserver().removeOnPreDrawListener(this);
                tvWidth = tv.getWidth();
                tvHeight = tv.getHeight();
                UpdataOnePageRows();
                ReShowChapter(DT.getLastReadPos());
                return true;}
            });
        EventAdd();
        FullScreen();
        InitialBatteryShow();
        StartTime();
    }
    public void InitialParama(){
        IC = new IndirectClass(this,null,this);
        Intent thisIntent = getIntent();
        Index = thisIntent.getIntExtra("Index",-1);
        DT = MainActivity.DS.getAllBookData().get(Index);
        //DT = (Data) thisIntent.getSerializableExtra("NovelData");
        DS = MainActivity.DS;
        dir = DT.getNovelDir();
        EncodeType = DT.getEncodeType();
        tv = findViewById(R.id.ShowArea_mid);
        tv_pre = findViewById(R.id.ShowArea_pre);
        tv_next = findViewById(R.id.ShowArea_next);
        mid = findViewById(R.id.mid_layout);
        pre = findViewById(R.id.pre_layout);
        next = findViewById(R.id.next_layout);
        ReadProgressTV = findViewById(R.id.ReadProgress);
        ToTop(mid,pre,next);
    }
    public void ToTop(LinearLayout tv1,LinearLayout tv2,LinearLayout tv3){
        ViewCompat.setZ(tv1 , 2);
        ViewCompat.setZ(tv2 , 1);
        ViewCompat.setZ(tv3 , 0);
    }
    public void InitialFonts(){
        FontMap = new HashMap<String, Typeface>() ;
        //tv.setTypeface(Typeface.createFromAsset(getAssets(),getResources().getString(R.string.hanyiqinzhoupos)));
        FontMap.put("默认字体",tv.getTypeface());
        FontMap.put("方正悠黑 ",Typeface.createFromAsset(getAssets(),getResources().getString(R.string.fangzhengyouheipos)));
        //FontMap.put("白桃乌龙 ",Typeface.createFromAsset(getAssets(),getResources().getString(R.string.baitaowulongpos)));
        ChangeType(DT.getFontType() , false);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,DT.getFontSize());
        tv_pre.setTextSize(TypedValue.COMPLEX_UNIT_PX,DT.getFontSize());
        tv_next.setTextSize(TypedValue.COMPLEX_UNIT_PX,DT.getFontSize());
    }
    @SuppressLint("ClickableViewAccessibility")
    public void EventAdd(){
        RelativeLayout ShowAreaRL = findViewById(R.id.show_area);
        tv.setOnTouchListener(new NovelViewTouchListener(new NovelViewTouchListener.OnTouchActionListener() {
            @Override public void onLastPage(View v) { LastPage(v); }
            @Override public void onNextPage(View v) { NextPage(v); }
            @Override public void onClick(View v, MotionEvent event) { Click(v, event); }
        }));
    }
    public void Click(View v,MotionEvent event){
        float part = v.getWidth()/3;
        switch ((int)(event.getX()/part)){
            case 0:LastPage(v);break;
            case 1:OpenSetting(v);break;
            case 2:NextPage(v);break;
            default:break;
        }
    }
    public void SetCheapterName(String str){
        DT.setLastReadChapterName(str);
        cheaptername.setText(str);
    }

    public int GetTVHieght(){
        return tvHeight;
    }
    public int GetTVWidth(){
        return tvWidth;
    }
    public void ShowPage(long pos){
        String text = GetShowText(pos);
        tv.setText(text);
        UpdateReadProgres();
    }
    public String GetShowText( long pos ){
        Log.e(TAG, "ShowPage: " + pos );
        DT.setLastReadPos(pos);
        int spaceline = 0;
        TransResult tr;
        int remainCount = OnePageRows;
        try {
            RAF.seek(pos);
            int totalheight = 0;
            String text = "";
            while (true){
                pos = RAF.getFilePointer();
                String line = RAF.readLine();
                if(line == null){
                    if(totalheight!=0)return text;
                    return null;
                }
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1),EncodeType);
                if(isCheapterName(line)){
                    if(remainCount == OnePageRows)SetCheapterName(line);
                    else {
                        RAF.seek(pos);
                        return text;
                    }
                }
                if(line .equals("")){
                    spaceline++;
                    if(spaceline>1)continue;
                }else spaceline=0;
                tr = getLineCount(line, remainCount);
                text +=  tr.getResultStr();
                //Log.e(TAG, "GetShowText: " + tr.getResultStr());
                if(tr.getRestLineCount()==0){
                    pos = pos + tr.getByteCount() ;
                    RAF.seek(pos);
                    break;
                }
                remainCount = tr.getRestLineCount();
            }
            Log.e(TAG, "GetShowText: " + totalheight + ":"  + tv.getHeight() + ":" + tr.getRestStr());
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void UpdataOnePageRows(){
        Paint.FontMetrics pf = tv.getPaint().getFontMetrics();
        float size1 = pf.bottom - pf.top,size2 = pf.descent - pf.ascent;
        if(size1 < size2)size1 = size2;
        float lineSpacingExtra = size1 * tv_font_extra_space;
        tv.setLineSpacing(lineSpacingExtra,1.0f);
        tv_next.setLineSpacing(lineSpacingExtra,1.0f);
        tv_pre.setLineSpacing(lineSpacingExtra,1.0f);
        int one_col_height = (int) Math.ceil( size1 * (1 + tv_font_extra_space));
        Log.e("lishanweilai", "UpdataOnePageRows: "+one_col_height+" extra" +tv.getLineSpacingExtra() + " hangjianju:" + tv.getLineSpacingMultiplier());
        OnePageRows = (int)(GetTVHieght()/tv.getLineHeight())-2;
        Log.e(TAG, "UpdataOnePageRows: " + OnePageRows );
        Log.e(TAG,"UpdataOnePageRows" +   ":" + tv.getHeight());
        Log.e(TAG,"UpdataOnePageRows" + tv.getHeight()/tv.getLineHeight()+ ":" + one_col_height);
    }

    public TransResult getLineCount(String text, int remainLineCount){
        float maxWidth = tv.getWidth() - tv.getPaddingStart() - tv.getPaddingEnd();
        try {
            return transToShow(text, remainLineCount, maxWidth);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void GetTvHeight(View view) {
        Log.e(TAG, "GetTvHeight:  print tv hieght " + tv.getHeight() + "lineheight "+tv.getLineHeight());
    }
    public void ReShowChapter(long pos){
        //long pos = DT .LastReadPos;
        long InitialPos = 0;
        if(pos == 0)return ;
        else if(pos < dir.get(0).getPosition())InitialPos = 0;
        else {
            for(int i = dir.getSize()- 1;i>=0;i--)
                if(dir.get(i).getPosition() < pos){
                    InitialPos = dir.get(i).getPosition();break;
                }
        }
        try {
            RAF.seek(InitialPos);
            String text="";
            do text = GetShowText(RAF.getFilePointer());
            while(pos >= RAF.getFilePointer());
            tv.setText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UpdateReadProgres();
    }

    public void NextPage(View view) {
        if(My_PopupWindow.HavePopup()){My_PopupWindow.ClosePopup();return;}
        try {
            if(RAF.getFilePointer() == RAF.length())return;
            String text = GetShowText(RAF.getFilePointer());
            pre.setTranslationX(0);
            tv_pre.setText(tv.getText());
            ToTop(pre,mid,next);
            tv.setText(text);
            TranslationX(pre,0,-tvWidth,300);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //DataSet.getInstance(null).save();
        UpdateReadProgres();
    }
    public void TranslationX(View myview , float start , float end ,int duration){
        ValueAnimator animator = ValueAnimator.ofFloat(start, end); // 在 X 轴方向上平移 100 个像素
        animator.setDuration(duration); // 持续1秒
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 设置加速减速插值器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前动画的值，并更新视图的属性
                float value = (float) animation.getAnimatedValue();
                myview.setTranslationX(value); // 更新视图在 X 轴上的平移位置
            }
        });
        animator.start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(My_PopupWindow.HavePopup())return super.onKeyDown(keyCode, event);
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            LastPage(null);return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            NextPage(null);return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void LastPage(View view){
        if(My_PopupWindow.HavePopup()){My_PopupWindow.ClosePopup();return;}
        cheaptername.setText("");
        long pos = DT.getLastReadPos();
        long InitialPos = 0;
        if(pos == 0)return ;
        else if(pos < dir.get(0).getPosition())InitialPos = 0;
        else {
            int index = GetCheapterIndex();
            if(dir.get(index).getPosition() == pos&&index!=0)index--;
            InitialPos = dir.get(index).getPosition();
        }
        try {
            RAF.seek(InitialPos);
            String text = "";
            do text = GetShowText(RAF.getFilePointer());
            while(pos != RAF.getFilePointer());
            pre.setTranslationX(0);
            tv_pre.setText(tv.getText());
            mid.setTranslationX(-tvWidth);
            ToTop(mid , pre,next);
            tv.setText(text);
            TranslationX(mid,-tvWidth,0,300);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //DataSet.getInstance(null).save();
        UpdateReadProgres();
    }

    public  boolean isCheapterName(String str){
        Matcher matcher = Chapter.getChapterPattern().matcher(str);
        return matcher.find();
    }
    public void AddFontSize(){
        DT.setFontSize(tv.getTextSize() + 1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        tv_pre.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        tv_next.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        UpdataOnePageRows();
        //ShowPage(DT.LastReadPos);
        ReShowChapter(DT.getLastReadPos());
    }
    public void SubFontSize(){
        DT.setFontSize(tv.getTextSize() - 1)  ;
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        tv_pre.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        tv_next.setTextSize(TypedValue.COMPLEX_UNIT_PX ,DT.getFontSize());
        UpdataOnePageRows();
        //ShowPage(DT.LastReadPos);
        ReShowChapter(DT.getLastReadPos());
    }
    public void ChangeType(String typename, boolean needflush){
        Typeface tf = FontMap.getOrDefault(typename,tv.getTypeface());
        DT.setFontType(typename);
        tv.setTypeface(tf);
        tv_pre.setTypeface(tf);
        tv_next.setTypeface(tf);
        if(needflush){
            UpdataOnePageRows();
            //ShowPage(DT.LastReadPos);
            ReShowChapter(DT.getLastReadPos());
        }
    }
    public void OpenSetting(View view) {
        if(My_PopupWindow.HavePopup()){My_PopupWindow.ClosePopup();return;}
        new setting_popwindow(new IndirectClass(this,null,this));
    }

    @Override
    public void onBackPressed() {
        try {
            RAF.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Save();
        Intent intent = new Intent();
        intent.putExtra("AllNovelData",DS);
        setResult(Activity.RESULT_OK , intent);
        My_PopupWindow.ClosePopup();
        finish();
    }
    public void Save(){
        DS.getAllBookData().set(Index , DT);
        //DataSet.getInstance(null).save();
    }
    @Override
    public void onDestroy(){
        StopTime();
        super.onDestroy();
    }
    @Override
    public void onPause() {
        Save();
        StopTime();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        StartTime();
    }
    public int GetCheapterIndex(){
        long pos = DT.getLastReadPos();
        return dir.getIndex(pos);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        FullScreen();
    }
    public void FullScreen(){
        Window w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    public void InitialBatteryShow(){
        TextView battery = findViewById(R.id.battery_show);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level * 100 / (float) scale;
                // 将电池电量显示在TextView中
                battery.setText(level + "/" + scale);

            }
        };
        registerReceiver(batteryReceiver, filter);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE||newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            tvWidth = tv.getWidth();
            tvHeight = tv.getHeight();
            Log.e(TAG, "onConfigurationChanged: " +tvWidth + ":" + tvHeight);

        }
        Log.e(TAG, "onConfigurationChanged: " +tvWidth + ":" + tvHeight);
    }
    public void StartTime(){
        if(TR!=null&&!TR.isTerminated())return;
        TR =new TimeRead(DT,findViewById(R.id.ReadTime),this);
        TR.start();
    }
    public void StopTime(){
        try {
            TR.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void UpdateReadProgres()  {
        try {
            DT.setReadProgress((float) RAF.getFilePointer() / RAF.length()*100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ReadProgressTV.setText(String.format("%.2f%%",DT.getReadProgress()));
    }
    public void AddBookmark(){
        DT.getNovelBookmark().addBookmark((String) tv.getText().subSequence(0,30),DT.getLastReadPos());
    }

    public TextView getTV(){
        return  tv;
    }

    public Dir getDir(){
        return dir;
    }
    public Map<String , Typeface> getFontMap(){
        return FontMap;
    }
    public TransResult transToShow(String str, int maxLineCount, float maxWidth) throws UnsupportedEncodingException {
        str = str.replaceAll("\r\n","").replaceAll("\n","");
        String rStr = "";
        int byteCount = str.getBytes(EncodeType).length;
        while(str.length()>0&&maxLineCount>0){
            int charCount = (int)tv.getPaint().breakText(str, true, maxWidth, null);
            charCount = Math.min(charCount, str.length());
            rStr+=str.substring(0,charCount)+"\n";
            str = str.substring(charCount);
            maxLineCount--;
        }
        TransResult tr  = new TransResult(rStr, str, maxLineCount);
        tr.setByteCount(byteCount - str.getBytes(EncodeType).length);
        return tr;
    }
    private class TransResult{

        public TransResult(String resultStr, String restStr, int restLineCount){
            this.resultStr = resultStr;
            this.restStr = restStr;
            this.restLineCount = restLineCount;
        }

        public void setByteCount(int byteCount) {
            this.byteCount = byteCount;
        }

        private String resultStr;
        private String restStr;

        public int getByteCount() {
            return byteCount;
        }

        private int byteCount;

        private int restLineCount;

        public String getResultStr() {
            return resultStr;
        }

        public void setResultStr(String resultStr) {
            this.resultStr = resultStr;
        }

        public String getRestStr() {
            return restStr;
        }

        public void setRestStr(String restStr) {
            this.restStr = restStr;
        }

        public int getRestLineCount() {
            return restLineCount;
        }

        public void setRestLineCount(int restLineCount) {
            this.restLineCount = restLineCount;
        }


    }
}