package com.example.mipareader.DATA;

import android.os.Environment;
import android.util.Log;

import com.example.mipareader.DATA.Repository.Book;
import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.DATA.Repository.BookWithSectionsAndBookmarks;
import com.example.mipareader.DATA.Repository.Bookmark;
import com.example.mipareader.DATA.Repository.Section;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private int id;
    private long lastReadPos;
    private String lastReadChapterName;

    private String novelFilePath;
    private String novelName;
    private String fontType;
    private Dir novelDir;
    private Bookmarks novelBookmarks;
    private float fontSize;
    private String encodeType;
    private float readTimeMinutes;
    private float readProgress;

    public Data() {
        this.novelName = "测试";
        this.lastReadPos = 0;
        this.lastReadChapterName = " ";
        this.novelFilePath = Environment.getExternalStorageDirectory() + "/" + "mytxt.txt";
        this.fontType = "默认字体";
        this.fontSize = 39;
        this.readTimeMinutes = 0;
        this.readProgress = 0;
        this.novelBookmarks = new Bookmarks();
    }


    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public long getLastReadPos() {
        return lastReadPos;
    }

    public void setLastReadPos(long lastReadPos) {
        this.lastReadPos = lastReadPos;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public String getLastReadChapterName() {
        return lastReadChapterName;
    }

    public void setLastReadChapterName(String lastReadChapterName) {
        this.lastReadChapterName = lastReadChapterName;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public String getNovelFilePath() {
        return novelFilePath;
    }

    public void setNovelFilePath(String novelFilePath) {
        this.novelFilePath = novelFilePath;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public Dir getNovelDir() {
        return novelDir;
    }

    public void setNovelDir(Dir novelDir) {
        this.novelDir = novelDir;
    }

    public Bookmarks getNovelBookmark() {
        return novelBookmarks;
    }

    public void setNovelBookmark(Bookmarks novelBookmarks) {
        this.novelBookmarks = novelBookmarks;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public float getReadTimeMinutes() {
        return readTimeMinutes;
    }

    public void setReadTimeMinutes(float readTimeMinutes) {
        this.readTimeMinutes = readTimeMinutes;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public float getReadProgress() {
        return readProgress;
    }

    public void setReadProgress(float readProgress) {
        this.readProgress = readProgress;
        BookRepository.getInstance().updataBookExceptDirAndBookmark(this);
    }

    public void startLoad() {
        getCharset();
        this.novelDir = new Dir(novelFilePath, encodeType, null);
        this.novelDir.initialDir();
    }

    public float getLoadProgress() {
        return novelDir.getDirGenProgress();
    }

    private void getCharset() {
        byte[] buf = new byte[1024];
        try {
            RandomAccessFile raf = new RandomAccessFile(novelFilePath, "r");
            raf.read(buf);
            raf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CharsetDetector detector = new CharsetDetector();
        detector.setText(buf);
        CharsetMatch match = detector.detect();
        String detectedCharset = match.getName();
        this.encodeType = detectedCharset;
        Log.e("7878", "Detected Encoding: " + detectedCharset);
    }

    public static Data fromBook(Book book, List<Section>sections, List<Bookmark> bookmarks){
        Data data = new Data();
        data.setLastReadPos(book.getLastReadPos());
        data.setLastReadChapterName(book.getLastReadChapterName());
        data.setNovelFilePath(book.getNovelFilePath());
        data.setNovelName(book.getNovelName());
        data.setFontType(book.getFontType());
        data.setFontSize(book.getFontSize());
        data.setEncodeType(book.getEncodeType());
        data.setReadTimeMinutes(book.getReadTimeMinutes());
        data.setReadProgress(book.getReadProgress());
        data.setId(book.getId());


        Dir bookDir = new Dir(sections,book.getNovelFilePath(),book.getEncodeType());
        bookDir.setBookId(book.getId());


        Bookmarks bookmarkss = new Bookmarks(bookmarks);
        bookmarkss.setBookId(book.getId());

        data.setNovelDir(bookDir);
        data.setNovelBookmark(bookmarkss);
        return data;
    }
    public static Data fromBookAndInf(BookWithSectionsAndBookmarks bookAndInf){
        return fromBook(bookAndInf.book, bookAndInf.sections, bookAndInf.bookmarks);
    }

    public static Book toBook(Data data){
        Book book = new Book();
        book.setEncodeType(data.getEncodeType());
        book.setLastReadPos(data.getLastReadPos());
        book.setLastReadChapterName(data.getLastReadChapterName());
        book.setNovelFilePath(data.getNovelFilePath());
        book.setNovelName(data.getNovelName());
        book.setFontType(data.getFontType());
        book.setFontSize(data.getFontSize());
        book.setReadTimeMinutes(data.getReadTimeMinutes());
        book.setReadProgress(data.getReadProgress());
        book.setId(data.getId());

        return book;
    }
    public static BookWithSectionsAndBookmarks toBookAndInf(Data data){
        Book book = new Book();
        book.setEncodeType(data.getEncodeType());
        book.setLastReadPos(data.getLastReadPos());
        book.setLastReadChapterName(data.getLastReadChapterName());
        book.setNovelFilePath(data.getNovelFilePath());
        book.setNovelName(data.getNovelName());
        book.setFontType(data.getFontType());
        book.setFontSize(data.getFontSize());
        book.setReadTimeMinutes(data.getReadTimeMinutes());
        book.setReadProgress(data.getReadProgress());
        book.setId(data.getId());

        BookWithSectionsAndBookmarks bookAndInf = new BookWithSectionsAndBookmarks();
        bookAndInf.book = book;
        bookAndInf.sections = Dir.toSections(data.getNovelDir());
        bookAndInf.bookmarks = Bookmarks.toBookmarks(data.getNovelBookmark());

        return bookAndInf;
    }
}