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

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.mipareader.Contract.NovelContract;
import com.example.mipareader.Presenter.NovelPresenter;
import com.example.mipareader.UI.DIYview.ReadView;
import com.example.mipareader.UI.event.NovelViewTouchListener;
import com.example.mipareader.UI.popwindow.My_PopupWindow;
import com.example.mipareader.R;
import com.example.mipareader.UI.popwindow.setting_popwindow;

import java.util.ArrayList;

public class NovelWindow extends AppCompatActivity implements NovelContract.View {
    //1.权限的请求4美化(设置界面，多字体添加，书籍图片的自添加)5.背景的自定义6.书签7.上一页的优化8.目录popupwindow项数少美化9.语言朗读功能
    public String TAG = "栗山未来说 ";

    TextView  cheaptername;
    ReadView tv,tv_pre, tv_next;
    LinearLayout mid,pre,next;

    TextView ReadProgressTV,readTimeTV;

    private NovelPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_window);
        initialParama();
        cheaptername = findViewById(R.id.CheapterName);
        addEvent();
        FullScreen();
        InitialBatteryShow();

    }
    public void initialParama(){
        //IC = new IndirectClass(this,null,this);
        Intent thisIntent = getIntent();
        int index  = thisIntent.getIntExtra("Index",-1);
        tv = findViewById(R.id.ShowArea_mid);
        tv_pre = findViewById(R.id.ShowArea_pre);
        tv_next = findViewById(R.id.ShowArea_next);
        mid = findViewById(R.id.mid_layout);
        pre = findViewById(R.id.pre_layout);
        next = findViewById(R.id.next_layout);
        ReadProgressTV = findViewById(R.id.ReadProgress);
        readTimeTV = findViewById(R.id.ReadTime);
        toTop(mid,pre,next);
        NovelContract.View view = this;
        presenter = new NovelPresenter(view,index);
        tv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tv.getViewTreeObserver().removeOnPreDrawListener(this);
                presenter.loadAndShow();
                return true;}
        });
    }
    public void toTop(LinearLayout tv1,LinearLayout tv2,LinearLayout tv3){
        ViewCompat.setZ(tv1 , 2);
        ViewCompat.setZ(tv2 , 1);
        ViewCompat.setZ(tv3 , 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addEvent(){
        RelativeLayout ShowAreaRL = findViewById(R.id.show_area);
        tv.setOnTouchListener(new NovelViewTouchListener(new NovelViewTouchListener.OnTouchActionListener() {
            @Override public void onLastPage(View v) { lastPage(v); }
            @Override public void onNextPage(View v) { nextPage(v); }
            @Override public void onClick(View v, MotionEvent event) { Click(v, event); }
        }));
    }
    public void Click(View v,MotionEvent event){
        float part = v.getWidth()/3;
        switch ((int)(event.getX()/part)){
            case 0:lastPage(v);break;
            case 1:OpenSetting(v);break;
            case 2:nextPage(v);break;
            default:break;
        }
    }
    private void lastPage(View view){
        tv_pre.setParagraph(tv.getParagraph());
        presenter.lastPage();
    }
    private void nextPage(View view){
        tv_pre.setParagraph(tv.getParagraph());
        presenter.nextPage();
    }


    public void translationX(View myview , float start , float end ,int duration){
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
            lastPage(null);return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            nextPage(null);return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void OpenSetting(View view) {
        if(My_PopupWindow.HavePopup()){My_PopupWindow.ClosePopup();return;}
        new setting_popwindow(this);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("AllNovelData",0);
        setResult(Activity.RESULT_OK , intent);
        My_PopupWindow.ClosePopup();
        finish();
    }
    @Override
    public void onDestroy(){
        presenter.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE||newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            tvWidth = tv.getWidth();
//            tvHeight = tv.getHeight();
//            Log.e(TAG, "onConfigurationChanged: " +tvWidth + ":" + tvHeight);
//
//        }
//        Log.e(TAG, "onConfigurationChanged: " +tvWidth + ":" + tvHeight);
//    }


    @Override
    public int getShowAreaLineCount() {
        return tv.getLincCount();
    }

    @Override
    public float getShowAreaAvailWidth() {
        return tv.getAvailWidth();
    }

    @Override
    public Paint getShowAreaPaint() {
        return tv.getPaint();
    }

    @Override
    public String getShowAreaContent() {
        if(tv.getParagraph().size()==0)return " ";
        return tv.getParagraph().get(0);
    }

    @Override
    public float getShowAreaFontSize() {
        return tv.getFontSize();
    }

    @Override
    public void setChapterName(String chapterName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cheaptername.setText(chapterName);
            }
        });
    }

    @Override
    public void setShowAreaContent(String content) {
        String [] strs = content.split("\n");
        ArrayList<String> arrayList = new ArrayList<>();
        for(String str:strs)arrayList.add(str);
        tv.setParagraph(arrayList);
    }

    @Override
    public void setReadProgress(String readProgress) {
        ReadProgressTV.setText(readProgress);
    }

    @Override
    public void setShowAreaTypeface(Typeface typeface) {
        tv.setFont(typeface);
        tv_pre.setFont(typeface);
    }

    @Override
    public void setShowAreaFontSize(float fontSize) {
        tv.setFontSize(fontSize);
        tv_pre.setFontSize(fontSize);
    }

    @Override
    public void setReadTime(String readTime) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readTimeTV.setText(readTime);
            }
        });
    }

    @Override
    public void addShowAreaFontSize() {
        tv.setFontSize(tv.getFontSize()+1);
        tv_pre.setFontSize((tv.getFontSize()+1));
    }

    @Override
    public void subShowAreaFontSize() {
        tv.setFontSize(tv.getFontSize()-1);
        tv_pre.setFontSize((tv.getFontSize()-1));
    }

    @Override
    public void startLastPageAnimation() {
        pre.setTranslationX(0);
        mid.setTranslationX(-mid.getWidth());
        toTop(mid , pre, next);
        translationX(mid, -mid.getWidth(), 0, 300);
    }

    @Override
    public void startNextPageAnimation() {
        tv_pre.setTranslationX(0);
        toTop(pre , mid, next);
        translationX(pre, 0, -mid.getWidth(), 300);
    }

    public ReadView getTV() {
        return tv;
    }
    public NovelPresenter getPresenter(){
        return presenter;
    }
}