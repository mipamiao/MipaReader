package com.example.mipareader.Utils;

import android.graphics.Typeface;

import com.example.mipareader.MyApp;
import com.example.mipareader.R;

import java.util.HashMap;
import java.util.Map;

public class TypefaceSet {
    Map<String , Typeface> FontMap ;

    public TypefaceSet(){
        FontMap = new HashMap<>();
        FontMap.put("默认字体",Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        FontMap.put("方正悠黑 ",Typeface.createFromAsset(MyApp.getInstance().getAssets(),MyApp.getInstance().getResources().getString(R.string.fangzhengyouheipos)));
        //FontMap.put("白桃乌龙 ",Typeface.createFromAsset(MyApp.getInstance().getAssets(),MyApp.getInstance().getResources().getString(R.string.baitaowulongpos)));
    }
    public Typeface getTypeface(String name){
        return FontMap.get(name);
    }

    public Map<String , Typeface> getFontMap(){
        return FontMap;
    }
}
