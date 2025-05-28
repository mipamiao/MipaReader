package com.example.mipareader.DATA.Repository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AllBooksForNet {
    private List<BookWithSectionsAndBookmarks> allBookData;

    public List<BookWithSectionsAndBookmarks> getAllBookData() {
        return allBookData;
    }

    public void setAllBookData(List<BookWithSectionsAndBookmarks> allBookData) {
        this.allBookData = allBookData;
    }
}
