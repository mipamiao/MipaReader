package com.example.mipareader;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
public class ChangeBookName_popwindow {
    public  ChangeBookName_popwindow(IndirectClass ic,int pos){
        Data book = ic.getActivity().DS.AllBookData.get(pos);
        View changebooknameview = LayoutInflater.from(ic.getContxt()).inflate(R.layout.change_book_name_popupwindow,null);
        PopupWindow changebookpopwindow = new PopupWindow(changebooknameview, ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        changebookpopwindow.showAtLocation(ic.getActivity().BookShelf, Gravity.CENTER,0,0);
        TextView bookname = changebooknameview.findViewById(R.id.BookName);
        Button ok_book_name = changebooknameview.findViewById(R.id.ok_book_name);
        bookname.setText(book.NovelName);
        ok_book_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.NovelName = bookname.getText().toString();
                ic.getActivity().DS.AllBookData.set(pos,book);
                ic.getActivity().BookShelf.getAdapter().notifyItemChanged(pos);
                changebookpopwindow.dismiss();
            }
        });
    }
}
