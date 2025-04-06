package com.example.mipareader;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class FontTypeAdapter extends RecyclerView.Adapter<FontTypeAdapter.ViewHolder> {
    public Map<String, Typeface> ST_map ;
    public ArrayList<String>StringList;
    OnDirItemClickListener clickListener ;
    public FontTypeAdapter(Map<String,Typeface> map){
        ST_map = map;
        StringList = new ArrayList<>(map.keySet());
    }
    public Typeface GetTypeface(int index){
        return ST_map.get(StringList.get(index));
    }
    public String getItem(int index){
        return StringList.get(index);
    }
    @NonNull
    @Override
    public FontTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dir_item, parent, false);
        return new FontTypeAdapter.ViewHolder(view , clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FontTypeAdapter.ViewHolder holder, int position) {
        holder.textView.setText(StringList.get(position));
    }

    @Override
    public int getItemCount() {
        return StringList.size();
    }
    public void  SetOnClickListener(OnDirItemClickListener listener){
        clickListener = listener ;
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
