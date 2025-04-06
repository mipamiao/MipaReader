package com.example.mipareader;

import android.os.Environment;
import android.util.Log;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Data implements Serializable {
    public long LastReadPos;
    public  String LastReadCheapterName;
    public String NovleFilePath;
    public String NovelName;
    public String FontType;
    public Dir NovelDir;
    public Bookmark NovelBookmark;
    public float FontSize;
    public String EncodeType;
    public float ReadTimeMinutes;
    public float ReadProgress;
    public Data(){
        NovelName = "测试";
        LastReadPos = 0;
        LastReadCheapterName = " ";
        NovleFilePath = Environment.getExternalStorageDirectory() + "/"+ "mytxt.txt";
        FontType  = "默认字体";
        FontSize = 39;
        ReadTimeMinutes = 0;
        ReadProgress = 0;
        NovelBookmark = new Bookmark();
    }
    public void StartLoad(){
        GetCharset();
        NovelDir = new Dir(NovleFilePath , EncodeType,null);
        NovelDir.InitialDir();
    }
    public float GetLoadProgress(){
        return NovelDir.GetDirGenProgress();
    }
    public void GetCharset(){
        byte[] buf = new byte[1024];
        try {
            RandomAccessFile RAF = new RandomAccessFile(NovleFilePath,"r");
            RAF.read(buf);
            RAF.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CharsetDetector detector = new CharsetDetector();
        detector.setText(buf);
        CharsetMatch match = detector.detect();
        // 获取探测到的文件编码
        String detectedCharset = match.getName();
        EncodeType = detectedCharset;
        Log.e("抹茶味胖次", "Detected Encoding: " + detectedCharset);
    }
}

