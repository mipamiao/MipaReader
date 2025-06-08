package com.example.mipareader.Utils;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {
    public static MultipartBody.Part createFilePart(File file, String OnlyCode) {
        RequestBody requestFile = RequestBody.create(
                file,
                okhttp3.MediaType.parse("multipart/form-data")
        );
        return MultipartBody.Part.createFormData(OnlyCode, file.getName(), requestFile);
    }
}
