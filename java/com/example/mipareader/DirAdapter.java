package com.example.mipareader;


import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class DirAdapter extends RecyclerView.Adapter<DirAdapter.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter{
    private final ArrayList<Cheapter> CheapterList ;
    OnDirItemClickListener clickListener ;
    public DirAdapter(ArrayList<Cheapter> ac , IndirectClass ic){
        this.CheapterList = ac;
    }
    public Cheapter getItem(int index){
        if(CheapterList == null)return null;
        return CheapterList.get(index);
    }
    @NonNull
    @Override
    public DirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dir_item, parent, false);
        return new ViewHolder(view , clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DirAdapter.ViewHolder holder, int position) {
        holder.textView.setText(CheapterList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return CheapterList.size();
    }
    public void  SetOnClickListener(OnDirItemClickListener listener){
        clickListener = listener ;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return CheapterList.get(position).name;
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
