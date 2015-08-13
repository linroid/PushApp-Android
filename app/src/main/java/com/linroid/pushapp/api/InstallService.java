package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Push;
import com.linroid.pushapp.model.User;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by linroid on 8/14/15.
 */
public interface InstallService {
    @POST("/install")
    @FormUrlEncoded
    void pushPackage(@Field("devices") String deviceIds, Callback<Push> callback);
    @Multipart
    @PUT("/install")
    User pushInstalledApplication( @Part("devices") TypedString deviceIds, @Part("file") TypedFile apk, Callback<Push> callback);
}
