package com.example.mipareader.Presenter;

import android.graphics.Typeface;
import android.util.Log;

import com.example.mipareader.Contract.NovelContract;
import com.example.mipareader.DATA.Bookmarks;
import com.example.mipareader.DATA.Chapter;
import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.Dir;
import com.example.mipareader.DATA.Repository.BookRepository;
import com.example.mipareader.Utils.TimeRead;
import com.example.mipareader.Utils.TimeReadShow;
import com.example.mipareader.Utils.TransResult;
import com.example.mipareader.Utils.TypefaceSet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class NovelPresenter implements NovelContract.Presenter {

    private NovelContract.View view;
    private BookRepository bookRepo;

    private long nowPos;

    private static final String TAG = "NovelPresenter";

    private RandomAccessFile RAF;

    private int bookId;
    private Data book;

    private TypefaceSet typefaceSet;

    private TimeRead TR;

    public NovelPresenter(NovelContract.View view, int bookId){
        this.view = view;
        this.bookRepo = BookRepository.getInstance();
        this.bookId =bookId;

        //showPage(book.getLastReadPos());
    }
    @Override
    public void loadAndShow(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                book = bookRepo.loadBook(bookId);
                try {
                    RAF = new RandomAccessFile(book.getNovelFilePath(),"r");
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "run: " +"FileNotFoundException :"  + book.getNovelFilePath()) ;
                    throw new RuntimeException(e);
                }
                typefaceSet = new TypefaceSet();
                view.setShowAreaTypeface(typefaceSet.getTypeface(book.getFontType()));
                view.setShowAreaFontSize(book.getFontSize());
                view.setReadTime(TimeRead.minutesToString(book.getReadTimeMinutes()));
                showNowPage();
                startTime();
            }
        });
        thread.start();
    }

    public String computerShowText(long pos){
        Log.e(TAG, "ShowPage: " + pos );
        book.setLastReadPos(pos);
        int spaceline = 0;
        TransResult tr;
        int totalCount = view.getShowAreaLineCount();
        int remainCount = totalCount;
        try {
            RAF.seek(pos);
            int totalheight = 0;
            String text = "";
            while (true){
                pos = RAF.getFilePointer();
                String line = RAF.readLine();
                if(line == null){
                    if(totalheight!=0)return text;
                    return null;
                }
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1),book.getEncodeType());
                if(isCheapterName(line)){
                    if(remainCount == totalCount)SetCheapterName(line);
                    else {
                        RAF.seek(pos);
                        return text;
                    }
                }
                if(line .equals("")){
                    spaceline++;
                    if(spaceline>1)continue;
                }else spaceline=0;
                tr = getLineCount(line, remainCount);
                text +=  tr.getResultStr();
                //Log.e(TAG, "GetShowText: " + tr.getResultStr());
                if(tr.getRestLineCount()==0){
                    pos = pos + tr.getByteCount() ;
                    RAF.seek(pos);
                    break;
                }
                remainCount = tr.getRestLineCount();
            }
            Log.e(TAG, "computerShowText: " + totalheight + ":"  + view.getShowAreaLineCount() + ":" + tr.getRestStr());
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public TransResult getLineCount(String text, int remainLineCount){
        float maxWidth = view.getShowAreaAvailWidth();
        try {
            return transToShow(text, remainLineCount, maxWidth);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public TransResult transToShow(String str, int maxLineCount, float maxWidth) throws UnsupportedEncodingException {
        str = str.replaceAll("\r\n","").replaceAll("\n","");
        String rStr = "";
        int byteCount = str.getBytes(book.getEncodeType()).length;
        while(str.length()>0&&maxLineCount>0){
            int charCount = (int)view.getShowAreaPaint().breakText(str, true, maxWidth, null);
            charCount = Math.min(charCount, str.length());
            rStr+=str.substring(0,charCount)+"\n";
            str = str.substring(charCount);
            maxLineCount--;
        }
        TransResult tr  = new TransResult(rStr, str, maxLineCount);
        tr.setByteCount(byteCount - str.getBytes(book.getEncodeType()).length);
        return tr;
    }
    public  boolean isCheapterName(String str){
        Matcher matcher = Chapter.getChapterPattern().matcher(str);
        return matcher.find();
    }
    public void SetCheapterName(String str){
        book.setLastReadChapterName(str);
        view.setChapterName(str);
    }
    @Override
    public void lastPage() {
        view.setChapterName("");
        long pos = book.getLastReadPos();
        long InitialPos = 0;
        Dir dir = book.getNovelDir();
        int index = dir.getIndexLess(pos);
        if(index == -1)return;
        InitialPos = dir.get(index).getPosition();
        try {
            RAF.seek(InitialPos);
            String text = "";
            do text = computerShowText(RAF.getFilePointer());
            while(pos != RAF.getFilePointer());
            view.setShowAreaContent(text);
            view.startLastPageAnimation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void nextPage() {
        long pos = book.getLastReadPos();
        try {
            if(RAF.getFilePointer() == RAF.length())return;
            String content = computerShowText(RAF.getFilePointer());
            view.setShowAreaContent(content);
            view.startNextPageAnimation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openDir() {

    }

    @Override
    public void jumpToChapter(long pos) {
        showPage(pos);
    }

    public void showPage(long pos){
        String text = computerShowText(pos);
        view.setShowAreaContent(text);
        updateReadProgres();
    }
    public void updateReadProgres()  {
        try {
            book.setReadProgress((float) RAF.getFilePointer() / RAF.length()*100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.setReadProgress(String.format("%.2f%%",book.getReadProgress()));
    }
    @Override
    public void addFont() {
        view.addShowAreaFontSize();
        reshowChapter(book.getLastReadPos());
        book.setFontSize(view.getShowAreaFontSize());
    }

    @Override
    public void subFont() {
        view.subShowAreaFontSize();
        reshowChapter(book.getLastReadPos());
        book.setFontSize(view.getShowAreaFontSize());
    }

    public void changeTypeface(String typefaceName, boolean needflush){
        Typeface tf = typefaceSet.getTypeface(typefaceName);

        book.setFontType(typefaceName);
        view.setShowAreaTypeface(tf);
        if(needflush){
            reshowChapter(book.getLastReadPos());
        }
    }
    public void reshowChapter(long pos){
        //long pos = DT .LastReadPos;
        Dir dir = book.getNovelDir();
        long InitialPos = 0;
        int index = dir.getIndexLess(pos);
        if(index == -1 )return;
        InitialPos = dir.get(index).getPosition();
        try {
            RAF.seek(InitialPos);
            String text="";
            do text = computerShowText(RAF.getFilePointer());
            while(pos >= RAF.getFilePointer());
            view.setShowAreaContent(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateReadProgres();
    }
    @Override
    public void showNowPage(){
        reshowChapter(book.getLastReadPos());
    }

    @Override
    public void addBookmark() {
        String content =  view.getShowAreaContent();
        book.getNovelBookmark().addBookmark((String)content.subSequence(0,Math.min(30, content.length())),book.getLastReadPos());
    }

    @Override
    public void onViewTouched() {

    }

    @Override
    public void onBackPressed() {
        try {
            RAF.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        stopTime();
        if(view!=null)view = null;
    }

    @Override
    public void onPause() {
        stopTime();
    }

    @Override
    public void onResume() {
        startTime();
    }

    public void startTime(){
        if(book == null)return;
        if(TR!=null&&!TR.isTerminated())return;
        TR = new TimeRead(book, new TimeReadShow() {
            @Override
            public void showReadTimeOnUIAsyn(String readTime) {
                view.setReadTime(readTime);
            }
        });
        TR.start();
    }
    public void stopTime(){
        try {
            TR.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Dir getDir(){
        return book.getNovelDir();
    }

    @Override
    public int getChapterIndex(){
        return book.getNovelDir().getIndexLessEqual(book.getLastReadPos());
    }

    @Override
    public TypefaceSet getTypefaceSet(){
        return typefaceSet;
    }

    @Override
    public Bookmarks getBookmark(){
        return book.getNovelBookmark();
    }
}
