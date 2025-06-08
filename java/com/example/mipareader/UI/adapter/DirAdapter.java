package com.example.mipareader.UI.adapter;


import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipareader.DATA.Chapter;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.UI.event.OnDirItemClickListener;
import com.example.mipareader.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class DirAdapter extends RecyclerView.Adapter<DirAdapter.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter{
    private final ArrayList<Chapter> ChapterList ;
    OnDirItemClickListener clickListener ;
    public DirAdapter(ArrayList<Chapter> ac ){
        this.ChapterList = ac;
    }
    public Chapter getItem(int index){
        if(ChapterList == null)return null;
        return ChapterList.get(index);
    }
    @NonNull
    @Override
    public DirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dir_item, parent, false);
        return new ViewHolder(view , clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DirAdapter.ViewHolder holder, int position) {
        holder.textView.setText(ChapterList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ChapterList.size();
    }
    public void  SetOnClickListener(OnDirItemClickListener listener){
        clickListener = listener ;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return ChapterList.get(position).getName();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        OnDirItemClickListener clicklistener;
        public ViewHolder(@NonNull View itemView , OnDirItemClickListener listener)  {
            super(itemView);
            textView = itemView.findViewById(R.id.DirItemTV);
             clicklistener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(clicklistener != null && pos != RecyclerView.NO_POSITION){
                        clicklistener.onItemClick(pos);
                    }
                }
            });
        }
    }
}
