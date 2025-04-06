package com.example.mipareader;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Dir implements Serializable {
    ArrayList<Cheapter> CheapterList;
    public String FilePath , EncodeType , TAG = "Dir Class";
    //IndirectClass IC;
    float progress;
    public Dir(String CompletePath ,String EncodeType, IndirectClass ic){
        CheapterList = new ArrayList<Cheapter>();
        FilePath = CompletePath;
        //IC = ic;
        this.EncodeType = EncodeType;
    }
    public boolean InitialDir(){
        File file = new File(FilePath);
        if(!file.canRead())return false;
        CheapterList.add(new Cheapter(0,"开始"));
        progress = 0;
        Thread dir_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initial_child_thread();
            }
        });
        dir_thread.start();
        return  true;
    }
    void initial_child_thread(){
        try{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                FileChannel channel = FileChannel.open(Paths.get(FilePath), StandardOpenOption.READ);
                InitialDirByFileChannel(channel);
                channel.close();
            }else{
                FileInputStream fis  = new FileInputStream(FilePath);
                InitialDirByInputStream(fis);
                fis.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void InitialDirByFileChannel(FileChannel channel) throws IOException {
        long pos = 0;
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        while (true) {
            pos = channel.position();
            int bytesRead = channel.read(buffer);
            if (bytesRead <= 0) break;
            buffer.flip();
            byte[] data = new byte[bytesRead];
            buffer.get(data);
            int remain = ParseStringsFromBytes__AddToCheapterList(data,pos);
            channel.position(channel.position() - remain);
            buffer.clear();
            progress = channel.position() *1.0f/channel.size();
        }
        progress = 1;
    }
    public void InitialDirByInputStream( FileInputStream fis) throws IOException {
        byte []data = new byte[8192];
        while(true){
            int byteRead = fis.read(data);
            if(byteRead == -1)break;
            long pos = fis.getChannel().position();
            int remain = ParseStringsFromBytes__AddToCheapterList(data ,pos);
            fis.getChannel().position(fis.getChannel().position() - remain);
            progress =  fis.getChannel().position()*1.0f /fis.getChannel().size();
        }
        progress = 1;
    }
    public int ParseStringsFromBytes__AddToCheapterList(byte []data  , long StartPos ) throws UnsupportedEncodingException {//返回剩余多少字节
        int ByteCount = data.length;
        int start,end;
        start = end = 0;
        while(end < ByteCount) {
            if(data[end]=='\n') {
                if(end + 1 < ByteCount && data[end+1] == '\r')end++;//可能一个\r在下一个数组里，导致多一行
                JudgeAndAddToCheapterList(data,start,end - start + 1,StartPos + start);
                start = end + 1;
            }
            else if (data[end] == '\r') {
                JudgeAndAddToCheapterList(data,start,end - start + 1,StartPos + start);
                start = end + 1;
            }
            end++;
        }
        if(start == 0) {
            JudgeAndAddToCheapterList(data,0,ByteCount,StartPos );
            start = ByteCount;
        }
        return ByteCount - start  ;
    }
    public boolean JudgeAndAddToCheapterList(byte[] data,int start , int length , long  pos) throws UnsupportedEncodingException {
        String line = new String(data,start,length,EncodeType);
        if (Cheapter.isCheapter(line)) {
            CheapterList.add(new Cheapter(pos, line));return true;
        }
        return false;
    }
    public Cheapter Get(int index){
        return CheapterList.get(index);
    }
    public int GetSize(){
        return CheapterList.size();
    }
    public float GetDirGenProgress(){
        return progress;
    }
    public int GetIndex(long pos){
        return half_search(0,CheapterList.size()-1,pos);
    }
    private int half_search(int low,int high,long pos){
        int mid = (low + high) / 2;
        int r = isOk(mid , pos);
        if(r==0)return mid;
        else if(r==1)return half_search(mid + 1,high,pos);
        else return half_search(low,mid-1,pos);
    }
    private int isOk(int index, long pos){
        if(index == CheapterList.size() - 1)return 0;
        else if(CheapterList.get(index).pos <= pos&&CheapterList.get(index + 1).pos>pos )return 0;
        else if(CheapterList.get(index).pos<pos)return 1;
        else return -1;
    }
}
