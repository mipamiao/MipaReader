package MyFilePicker;


import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.UI.event.OnDirItemClickListener;
import com.example.mipareader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class File_Adapter extends RecyclerView.Adapter<File_Adapter.ViewHolder> {
    private final ArrayList<File> FileList ;
    OnDirItemClickListener clickListener;
    OnFileClick file_click;
    File now_file ;
    public File_Adapter( IndirectClass ic){
        FileList = new ArrayList<File>();
        String InitialPath = Environment.getExternalStorageDirectory().getPath();
        File filefolder = new File(InitialPath);
        //FileList.addAll(Arrays.asList(filefolder.listFiles()));
        OpenFolder(filefolder);
        clickListener = new OnDirItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(FileList.get(position).isDirectory())OpenFolder(FileList.get(position));
                else file_click.onFileClick(FileList.get(position));
            }
        };
    }


    @NonNull
    @Override
    public File_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_picker_item, parent, false);
        return new File_Adapter.ViewHolder(view ,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull File_Adapter.ViewHolder holder, int position) {
        holder.textView.setText(FileList.get(position).getName());
        if(FileList.get(position).isDirectory())holder.imageView.setImageResource(R.drawable.folder);
        else holder.imageView.setImageResource(R.drawable.file);
    }

    @Override
    public int getItemCount() {
        return FileList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        OnDirItemClickListener clickListener;
        public ViewHolder(@NonNull View itemView ,OnDirItemClickListener click)  {
            super(itemView);
            textView = itemView.findViewById(R.id.FilePickerItem);
            imageView = itemView.findViewById(R.id.fileImg);
            clickListener = click;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION && clickListener != null)
                        clickListener.onItemClick(pos);
                }
            });
        }

    }
    public void setOnFileClick(OnFileClick click){
        file_click = click;
    }
    public void OpenFolder(File filefolder){
        if(filefolder.isDirectory()){
            File[] files = filefolder.listFiles();
            if(files ==null)return;
            FileList.clear();
            FileList.addAll(Arrays.asList(files));
            FileList.sort(new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                   return  compare_filename(o1,o2);
                }
            });
           super.notifyDataSetChanged();
           now_file = filefolder;
        }
    }
    public void PreFolder(){
        if(now_file.getPath().equals(Environment.getExternalStorageDirectory().getPath()))return;
        File pre_file = now_file.getParentFile();
        if(pre_file!=null){
            OpenFolder(pre_file);
        }
    }
    public int compare_filename(File a,File b){
        if(a.isDirectory()&&b.isFile())return -1;
        else if(a.isFile()&&b.isDirectory())return 1;
        else return a.getName().compareTo(b.getName());
    }
}
