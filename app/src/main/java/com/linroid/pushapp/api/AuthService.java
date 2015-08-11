package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Authorization;
import com.linroid.pushapp.model.Device;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by linroid on 8/12/15.
 */
public interface AuthService {
    @POST("/auth/bind")
    void bindDevice(@Query("token") String token, @Body Device device, Callback<Authorization> callback);

    @GET("/auth/check")
    void checkToken(@Query("token") String token, @Query("device_id") String deviceId, Callback<Device> callback);

}
