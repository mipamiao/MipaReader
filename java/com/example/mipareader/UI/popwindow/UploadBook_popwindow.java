package com.example.mipareader.UI.popwindow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.Repository.Book;
import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.MyApp;
import com.example.mipareader.R;
import com.example.mipareader.Utils.IndirectClass;

public class UploadBook_popwindow extends PopupWindow{

    Data book ;
    public UploadBook_popwindow(IndirectClass ic, int pos){
        Data book = ic.getActivity().DS.getAllBookData().get(pos);
        View upLoadPopView = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.upload_download_popwindow,null);
        EditText et = upLoadPopView.findViewById(R.id.only_code_et);
        PopupWindow upLoadPop = new PopupWindow(upLoadPopView , ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        upLoadPop.setAnimationStyle(R.style.pop_animation);
        upLoadPop.showAtLocation(ic.getActivity().BookShelf, Gravity.CENTER,0,0);
        upLoadPopView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlyCode = String.valueOf(et.getText());
                BookRepository.getInstance().uploadBookFile(book, onlyCode);
                BookRepository.getInstance().uploadBookInf(book, onlyCode);
                upLoadPop.dismiss();
            }
        });
        upLoadPopView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadPop.dismiss();
            }
        });
    }

}
