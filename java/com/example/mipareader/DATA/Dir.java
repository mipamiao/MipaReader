package com.example.mipareader.DATA;

import com.example.mipareader.DATA.Repository.Section;
import com.example.mipareader.Utils.IndirectClass;

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
import java.util.List;
import java.util.stream.Collectors;

public class Dir implements Serializable {
    private ArrayList<Chapter> chapterList;
    private String filePath;
    private String encodeType;
    private float progress;
    private static final String TAG = "Dir Class";

    private int book_id;

    public int getBookId(){return book_id;}
    public void setBookId(int book_id){this.book_id = book_id;}

    public Dir(String completePath, String encodeType, IndirectClass ic) {
        this.chapterList = new ArrayList<>();
        this.filePath = completePath;
        this.encodeType = encodeType;
    }

    // Getter and Setter methods
    public ArrayList<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(ArrayList<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean initialDir() {
        File file = new File(filePath);
        if (!file.canRead()) return false;

        chapterList.add(new Chapter(0, "开始"));
        progress = 0;

        Thread dirThread = new Thread(this::initialChildThread);
        dirThread.start();
        return true;
    }
    public Dir(List<Section> sections, String filePath, String encodeType){
       ArrayList<Chapter> chpaters =  sections.stream()
                        .map(Chapter::fromSection)
                         .collect(Collectors.toCollection(ArrayList::new));
        setChapterList(chpaters);
        this.filePath = filePath;this.encodeType = encodeType;
    }

    private void initialChildThread() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                FileChannel channel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
                initialDirByFileChannel(channel);
                channel.close();
            } else {
                FileInputStream fis = new FileInputStream(filePath);
                initialDirByInputStream(fis);
                fis.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialDirByFileChannel(FileChannel channel) throws IOException {
        long pos = 0;
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        while (true) {
            pos = channel.position();
            int bytesRead = channel.read(buffer);
            if (bytesRead <= 0) break;

            buffer.flip();
            byte[] data = new byte[bytesRead];
            buffer.get(data);

            int remain = parseStringsFromBytesAndAddToChapterList(data, pos);
            channel.position(channel.position() - remain);
            buffer.clear();
            progress = channel.position() * 1.0f / channel.size();
        }
        progress = 1;
    }

    private void initialDirByInputStream(FileInputStream fis) throws IOException {
        byte[] data = new byte[8192];
        while (true) {
            int byteRead = fis.read(data);
            if (byteRead == -1) break;

            long pos = fis.getChannel().position();
            int remain = parseStringsFromBytesAndAddToChapterList(data, pos);
            fis.getChannel().position(fis.getChannel().position() - remain);
            progress = fis.getChannel().position() * 1.0f / fis.getChannel().size();
        }
        progress = 1;
    }

    private int parseStringsFromBytesAndAddToChapterList(byte[] data, long startPos) throws UnsupportedEncodingException {
        int byteCount = data.length;
        int start = 0, end = 0;

        while (end < byteCount) {
            if (data[end] == '\n') {
                if (end + 1 < byteCount && data[end + 1] == '\r') end++;
                judgeAndAddToChapterList(data, start, end - start + 1, startPos + start);
                start = end + 1;
            } else if (data[end] == '\r') {
                judgeAndAddToChapterList(data, start, end - start + 1, startPos + start);
                start = end + 1;
            }
            end++;
        }

        if (start == 0) {
            judgeAndAddToChapterList(data, 0, byteCount, startPos);
            start = byteCount;
        }
        return byteCount - start;
    }

    private boolean judgeAndAddToChapterList(byte[] data, int start, int length, long pos) throws UnsupportedEncodingException {
        String line = new String(data, start, length, encodeType);
        if (Chapter.isChapter(line)) {
            chapterList.add(new Chapter(pos, line));
            return true;
        }
        return false;
    }

    public Chapter get(int index) {
        return chapterList.get(index);
    }

    public int getSize() {
        return chapterList.size();
    }

    public float getDirGenProgress() {
        return progress;
    }

    public int getIndex(long pos) {
        return halfSearch(0, chapterList.size() - 1, pos);
    }

    private int halfSearch(int low, int high, long pos) {
        int mid = (low + high) / 2;
        int r = isOk(mid, pos);
        if (r == 0) return mid;
        else if (r == 1) return halfSearch(mid + 1, high, pos);
        else return halfSearch(low, mid - 1, pos);
    }

    private int isOk(int index, long pos) {
        if (index == chapterList.size() - 1) return 0;
        else if (chapterList.get(index).getPosition() <= pos && chapterList.get(index + 1).getPosition() > pos) return 0;
        else if (chapterList.get(index).getPosition() < pos) return 1;
        else return -1;
    }
    public static List<Section> toSections(Dir dir){
        return dir.getChapterList().stream().map(Chapter::toSection).collect(Collectors.toList());
    }
}