package com.example.mipareader.DATA.Net;

import com.example.mipareader.DATA.Data;
import com.example.mipareader.DATA.Repository.AllBooksForNet;
import com.example.mipareader.DATA.Repository.BookWithSectionsAndBookmarks;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ApiService {
    @GET("books/{id}")
    Call<AllBooksForNet> getBooks(@Path("id") int userId);

    @POST("books/")
    Call<Void> postBooks(@Body AllBooksForNet allBooksForNet);

    @Multipart
    @POST("data/upload")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file,
            @Part("OnlyCode") RequestBody OnlyCode
    );
    @Streaming
    @GET("data/{filename}")
    Call<ResponseBody> downloadFile(@Path("filename") String filename);

    @POST("inf/upload")
    Call<ResponseBody> uploadInf(@Body Data book);

    @GET("inf/{onlycode}")
    Call<Data> downloadInf(@Path("onlycode") String onlyCode);
}
