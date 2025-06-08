package com.example.mipareader.UI;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.Manifest;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.PopupWindow;

import android.widget.Toast;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;

import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.MyApp;
import com.example.mipareader.UI.adapter.BookShelfAdapter;
import com.example.mipareader.UI.event.BookTouchAndClick;
import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.DataSet;
import com.example.mipareader.Utils.DatabaseExecutor;
import com.example.mipareader.Utils.IndirectClass;
import com.example.mipareader.UI.DIYview.MyCircleProgress;
import com.example.mipareader.R;
import com.example.mipareader.UI.event.VerticalSpaceItemDecoration;

import MyFilePicker.File_Picker_Main;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    public static DataSet DS ;
    public IndirectClass IC;
    public Data now_book;
    public int now_book_index;
    public boolean HaveReadPermission;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    ActivityResultLauncher<Intent> filePickerLauncher;
    public Map<Button, Integer> But_Index_Map;
    public RecyclerView BookShelf;
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialMe();
    }

    private void InitialMe() {
        IC=new IndirectClass(this,MainActivity.this);

//        DS = new DataSet(IC);
//        DS.AllBookData.add(new Data()) ;//测试
        But_Index_Map = new HashMap<>();
        PermissInital();
        RegistCallBack();
        DatabaseExecutor.getInstance().getDiskIOExecutor().execute(new Runnable() {
            @Override
            public void run() {
                DS = DataSet.getInstance(IC.getContext().getFilesDir().getPath());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InitialBookshelf();
                    }
                });
            }
        });
    }
    public void RegistCallBack(){
        intentActivityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //此处是跳转的result回调方法
                        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                            //DS = (DataSet) result.getData().getSerializableExtra("AllNovelData");
                            DS.moveBookToFirst(now_book_index);
                            UpdateBookshelf(now_book_index,0);
                        } else {
                            Toast.makeText(getApplicationContext(), "not good result", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                            String path = result.getData().getStringExtra("filepath");
                            String name = result.getData().getStringExtra("filename");
                            if(!path.equals("")){
                                Log.e("1212", "name:"+name  + " path:" +path );
                                AddBook(name,path);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "not good result", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    public void AddBook(String bookname , String bookpath){
        DS.addBook(bookname,bookpath);
        View view = LayoutInflater.from(IC.getContext()).inflate(R.layout.load_progress_popwindow,null);
        PopupWindow popup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,false);
        MyCircleProgress mcp = view.findViewById(R.id.circle_progress);
        popup.showAtLocation(findViewById(R.id.mainpage),Gravity.CENTER,0,0);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while(progress != 100){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    progress = (int) (DS.getLoadProgress()*100);
                    Log.e(TAG, "run: progress : " + progress );
                    int finalProgress = progress;
                    runOnUiThread(()->mcp.SetCurrent(finalProgress));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateBookshelf();
                        UpdateBookshelf(DS.getAllBookData().size()-1,0);
                        popup.dismiss();
                    }
                });

            }
        });
        thread.start();
    }
    public void InitialBookshelf(){
        BookShelfAdapter bsa = new BookShelfAdapter(DS.getAllBookData(),IC);
        bsa.SetOnClickListener(new BookTouchAndClick(IC));
        BookShelf = findViewById(R.id.bookshelf);
        BookShelf.setAdapter(bsa);
        BookShelf.setLayoutManager(new LinearLayoutManager(this));
        int verticalSpaceHeight = getResources().getDimensionPixelSize(R.dimen.vertical_space_height);
        VerticalSpaceItemDecoration deco = new VerticalSpaceItemDecoration(this,verticalSpaceHeight);
        BookShelf.addItemDecoration(deco);
    }
    public void UpdateBookshelf(){
        RecyclerView rv = findViewById(R.id.bookshelf);
        rv.getAdapter().notifyDataSetChanged();
    }
    public void UpdateBookshelf(int src_pos , int dst_pos){
        RecyclerView rv = findViewById(R.id.bookshelf);
        rv.getAdapter().notifyItemMoved(src_pos , dst_pos);
    }
    private String getFilePath(DocumentFile documentFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String filePath = "";
            Cursor cursor = this.getContentResolver().query(documentFile.getUri(), null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
            return filePath;  // 在 Android 10 及以上版本，使用 Uri 来表示文件路径
        } else {
            // 在 Android 9 以下版本，使用旧的方式来获取文件路径
            String basePath = Environment.getExternalStorageDirectory().getPath();
            String relativePath = documentFile.getUri().getPath().substring("/document/primary:".length());
            return basePath + "/" + relativePath;
        }
    }

    public void OpenNovelWindow(int index) {
        if(!HaveReadPermission){
            PermissInital();return;
        }
        now_book_index = index;
        now_book = DS.getAllBookData().get(index);
        Intent switchIntent = new Intent(MainActivity.this, NovelWindow.class);
        switchIntent.putExtra("Index",DS.getAllBookData().get(index).getId());
        //switchIntent.putExtra("NovelData", DS.AllBookData.get(index));
        //switchIntent.putExtra("AllNovelData",DS);
        intentActivityResultLauncher.launch(switchIntent);
    }
    public boolean PermissInital(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)return PermissReq_14();
        else return PermissReq_less14();
    }
    public boolean PermissReq_less14(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            HaveReadPermission = true;
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public boolean PermissReq_14(){
        if (Environment.isExternalStorageManager()) {
            HaveReadPermission = true;
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && IsAllTrue(grantResults)) {
                HaveReadPermission = true;
            } else {
                Intent intent2 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                //Uri uri = Uri.fromParts("package", getPackageName(), null);
                //intent.setData(uri);
                startActivity(intent);
            }
        }
    }
    public boolean IsAllTrue(int[] grantResults){
        for(int i = 0 ;i < grantResults.length ;i++)
            if(grantResults[i]==PackageManager.PERMISSION_DENIED)return false;
        return true;
    }

    @Override
    protected void onStop() {
        //DataSet.getInstance(null).save();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        //DataSet.getInstance(null).save();
        super.onDestroy();

    }

    public void LoadBook(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.e(TAG, "LoadBook: 1\t" +
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) );
            Log.e(TAG, "LoadBook: 2\t" +
                    PermissionChecker.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) );
        }
        filePickerLauncher.launch(new Intent(MainActivity.this, File_Picker_Main.class));
    }

    public void downloadBook(View view){
        View downLoadPopView = LayoutInflater.from(MyApp.getInstance().getApplicationContext()).inflate(R.layout.upload_download_popwindow,null);
        EditText et = downLoadPopView.findViewById(R.id.only_code_et);
        PopupWindow downLoadPop = new PopupWindow(downLoadPopView , ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        downLoadPop.setAnimationStyle(R.style.pop_animation);
        downLoadPop.showAtLocation(BookShelf, Gravity.CENTER,0,0);
        downLoadPopView.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlyCode = String.valueOf(et.getText());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File exFile = Environment.getExternalStorageDirectory();
                        Data book = BookRepository.getInstance().downloadInf(onlyCode);
                        BookRepository.getInstance().downloadBookFile(onlyCode,exFile.getPath() + "/" + book.getNovelName());
                        book.setNovelFilePath(exFile.getPath() + "/" + book.getNovelName());
                        DatabaseExecutor.getInstance().getDiskIOExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                BookRepository.getInstance().addBook(book);
                                DS.getAllBookData().add(book);
                                RecyclerView rv = findViewById(R.id.bookshelf);
                                rv.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                });
                thread.start();

                downLoadPop.dismiss();
            }
        });
        downLoadPopView.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadPop.dismiss();
            }
        });
    }


}