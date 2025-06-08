package com.example.mipareader.Contract;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.example.mipareader.DATA.Bookmarks;
import com.example.mipareader.DATA.Dir;
import com.example.mipareader.Utils.ShowAreaType;
import com.example.mipareader.Utils.TypefaceSet;

public interface NovelContract {
    interface View{
        int getShowAreaLineCount();
        float getShowAreaAvailWidth();
        Paint getShowAreaPaint();
        String getShowAreaContent();

        float getShowAreaFontSize();
        void setChapterName(String chapterName);

        void setShowAreaContent(String content);
        void setReadProgress(String readProgress);
        void setShowAreaTypeface(Typeface typeface);
        void setShowAreaFontSize(float fontSize);
        void setReadTime(String readTime);

        void addShowAreaFontSize();
        void subShowAreaFontSize();

        void startLastPageAnimation();
        void startNextPageAnimation();

    }
    interface Presenter{

         void loadAndShow();
         void showPage(long pos);
         void reshowChapter(long pos);

         void showNowPage();
         void lastPage();
         void nextPage();
         void openDir();
         void jumpToChapter(long pos);

         void addFont();
         void subFont();
        void changeTypeface(String typefaceName, boolean needflush);

         void addBookmark();

         void onViewTouched();


         void onBackPressed();

         void onDestroy();

         void onPause() ;

         void onResume() ;

         Dir getDir();

         int getChapterIndex();
        public TypefaceSet getTypefaceSet();


        public Bookmarks getBookmark();
    }
}
