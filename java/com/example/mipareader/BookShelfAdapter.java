package com.example.mipareader;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookShelfAdapter extends RecyclerView.Adapter<BookShelfAdapter.ViewHolder> {
    private final ArrayList<Data> AllBook ;
    Book_OP clickListener ;
    IndirectClass IC;
    public BookShelfAdapter(ArrayList<Data> ac , IndirectClass ic){
        this.AllBook = ac;IC = ic;
    }
    public Data getItem(int index){
        if(AllBook == null)return null;
        return AllBook.get(index);
    }
    @NonNull
    @Override
    public BookShelfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookshelf_item, parent, false);
        return new BookShelfAdapter.ViewHolder(view , clickListener,IC);
    }

    @Override
    public void onBindViewHolder(@NonNull BookShelfAdapter.ViewHolder holder, int position) {
        //holder.img.setImageResource(R.drawable.mipadefault);
        holder.textView.setText(AllBook.get(position).NovelName);
        holder.but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("lishanweilai", "onClick: "+position+":" + holder.getAdapterPosition() );
                AllBook.remove(holder.getAdapterPosition() );
                BookShelfAdapter.super.notifyItemRemoved(holder.getAdapterPosition() );
            }
        });
    }

    @Override
    public int getItemCount() {
        return AllBook.size();
    }
    public void  SetOnClickListener(Book_OP listener){
        clickListener = listener ;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView img ;
        ImageView but;
        Button changenamebut;
        public LinearLayout linear;
        Book_OP clicklistener;
        public ViewHolder(@NonNull View itemView , Book_OP listener,IndirectClass IC)  {
            super(itemView);
            textView = itemView.findViewById(R.id.NovelName);
            img = itemView.findViewById(R.id.NovelImg);
            but = itemView.findViewById(R.id.del_but);
            changenamebut  = (Button) itemView.findViewById(R.id.changename);
            linear = itemView.findViewById(R.id.ItemLinear);
            clicklistener = listener;
            itemView.setOnTouchListener(new View.OnTouchListener() {
                boolean onemove = false;
                float startX = 0,startY = 0;
                boolean have_this_slide = false;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float deltaX = 0 ;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            onemove = true;
                            startX = event.getX();
                            startY = event.getY();
                            have_this_slide = false;
                            Log.e("TAG", "onTouch: down" + "x:"+event.getX()+"y:"+event.getY());
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(have_this_slide) break;
                            if(event.getX() == startX&&event.getY()==startY)
                                clicklistener.onItemClick(getAdapterPosition());
                            deltaX = event.getX() - startX;
                            int left_termine = GetItemWidth()-itemView.getWidth();
                            int x = itemView.getScrollX();
                            if(deltaX<0&&x==0)
                                clicklistener.onItemLeftSlide((int) deltaX,GetItemWidth(),itemView,event);
                            else if(deltaX>0&&x==left_termine)
                                clicklistener.onItemRightSlide((int) deltaX,GetItemWidth(),itemView,event);
                            have_this_slide = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            if(have_this_slide) break;
                            if(event.getX() == startX&&event.getY()==startY)
                                clicklistener.onItemClick(getAdapterPosition());
                            Log.e("TAG", "onTouch: up" + "x:"+event.getX()+"y:"+event.getY());
                           break;
                    } return true;
                }
            });
            changenamebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ChangeBookName_popwindow(IC,getAdapterPosition());
                }
            });
        }
        public int GetItemWidth(){
            return  img.getWidth()+but.getWidth()+textView.getWidth()+changenamebut.getWidth();
        }
    }
}
