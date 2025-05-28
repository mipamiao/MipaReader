package com.example.mipareader.UI.popwindow;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mipareader.DATA.Data;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.R;

public class ChangeBookName_popwindow {
    public  ChangeBookName_popwindow(IndirectClass ic, int pos){
        Data book = ic.getActivity().DS.getAllBookData().get(pos);
        View changebooknameview = LayoutInflater.from(ic.getContext()).inflate(R.layout.change_book_name_popupwindow,null);
        PopupWindow changebookpopwindow = new PopupWindow(changebooknameview, ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        changebookpopwindow.showAtLocation(ic.getActivity().BookShelf, Gravity.CENTER,0,0);
        TextView bookname = changebooknameview.findViewById(R.id.BookName);
        Button ok_book_name = changebooknameview.findViewById(R.id.ok_book_name);
        bookname.setText(book.getNovelName());
        ok_book_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setNovelName( bookname.getText().toString());
                ic.getActivity().DS.getAllBookData().set(pos,book);
                ic.getActivity().BookShelf.getAdapter().notifyItemChanged(pos);
                changebookpopwindow.dismiss();
            }
        });
    }
}
