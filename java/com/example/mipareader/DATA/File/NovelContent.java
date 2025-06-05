package com.example.mipareader.DATA.File;

import com.example.mipareader.Utils.MyBytes;
import com.example.mipareader.Utils.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class NovelContent {

    private String filePath;
    private long lastReadPos, fileSize;

    private String charset;
    private BlockingQueue<Paragraph> que;
    private MyBytes bytes;

    private int mapSize = 4096;

    private boolean EOF = false;

    public NovelContent(String filePath, int lastReadPos, String charset) {
        this.filePath = filePath;
        this.lastReadPos = lastReadPos;
        this.charset = charset;
        que = new LinkedBlockingQueue<>(100);
        bytes = new MyBytes();
    }

    public Paragraph getParagraph() throws InterruptedException {
        return que.take();
    }

    private void fillQue() {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r");
             FileChannel channel = raf.getChannel()) {
            fileSize = channel.size();
            if(channel.size()<=lastReadPos + mapSize){
                mapSize = (int) (channel.size() - lastReadPos);
                EOF = true;
            }
            // 内存映射文件
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, lastReadPos, mapSize);
            lastReadPos += mapSize;
            int start = 0;
            int end = -1; // 换行符结束位置（不包含）

            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                boolean isEnd = false;
                if (b == '\n') {
                    end = buffer.position() - 1; // \n 前面位置
                    isEnd = true;
                } else if (b == '\r') {
                    end = buffer.position() - 1;
                    // 查看下一个字节是否是 \n，如果是则跳过
                    if (buffer.hasRemaining()) {
                        byte next = buffer.get();
                        if (next != '\n') {
                            buffer.position(buffer.position() - 1); // 不是\n回退
                        }
                    }
                    isEnd = true;
                }
                if (isEnd) {
                    if(start != end) {
                        byte[] lineBytes = new byte[end - start];
                        buffer.position(start);
                        buffer.get(lineBytes);
                        bytes.addBytes(lineBytes);
                        if(start == 0){
                            que.put(new Paragraph(lastReadPos + start, new String(bytes.getBytes(),0,bytes.getSize(), charset)));
                            bytes.clear();
                        }else que.put(new Paragraph(lastReadPos + start, new String(lineBytes,charset)));
                    }
                    start = buffer.position();
                    end = -1;
                    isEnd = false;
                }
            }
            if (end == -1) {
                bytes.addBytes(new byte[mapSize - start]);
            }
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }
    public void startFillQue(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!EOF){
                    fillQue();
                }
                try {
                    que.put(new Paragraph(lastReadPos,"\n\n\n"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    public long getLastReadPos() {
        return lastReadPos;
    }
    public long getFileSize(){
        return fileSize;
    }

}