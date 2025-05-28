package com.example.mipareader.DATA.Net;

import com.example.mipareader.DATA.Repository.AllBooksForNet;
import com.example.mipareader.DATA.Repository.BookWithSectionsAndBookmarks;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("books/{id}")
    Call<AllBooksForNet> getBooks(@Path("id") int userId);

    @POST("books/")
    Call<Void> postBooks(@Body AllBooksForNet allBooksForNet);
}
