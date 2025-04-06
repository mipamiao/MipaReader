package com.example.mipareader;

import android.view.View;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class My_PopupWindow extends PopupWindow {
    public static boolean havePopup;
    public static PopupWindow popup = null;
    public static ArrayList<PopupWindow> popups = new ArrayList<>();
    public  static void ClosePopup(){
        if(!popups.isEmpty()){
            for (int i = 0; i < popups.size(); i++) {
                popups.get(i).dismiss();
            }
        }
        havePopup = false;
        popups.clear();
    }
    public void SetPopup(PopupWindow Popup){
        havePopup = Popup != null;
        popups.add(Popup);
    }
    public void AddPopup(PopupWindow Popup){
        popups.add(Popup);
    }
    public static boolean HavePopup(){return havePopup;}

    public My_PopupWindow(View contentView, int width, int height, boolean focusable){
        super(contentView,width,height,focusable);
        ClosePopup();
        SetPopup(this);
    }
    public void request_dismiss(){
        ClosePopup();
    }
}
