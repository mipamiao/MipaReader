package MyFilePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mipareader.IndirectClass;
import com.example.mipareader.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;

public class File_Picker_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker_main);
        FastScrollRecyclerView fsr = findViewById(R.id.filepickerlist);
        File_Adapter adapter = new File_Adapter(new IndirectClass(this,null,null));
        fsr.setAdapter(adapter);
        fsr.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.file_pick_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.PreFolder();
            }
        });
        findViewById(R.id.file_pick_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        adapter.setOnFileClick(new OnFileClick() {
            @Override
            public void onFileClick(File file) {
                Intent intent = new Intent();
                intent.putExtra("filepath",file.getPath());
                intent.putExtra("filename",file.getName());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("filepath","");
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}