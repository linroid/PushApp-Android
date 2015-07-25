package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Device;
import com.squareup.okhttp.Call;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by linroid on 7/24/15.
 */
public interface DeviceService {
    @POST("/device/bind")
    void bindDevice(@Body Device device, Callback<Device> callback);
    @GET("/device/check")
    void checkToken(@Query("token") String token, @Query("device_id") String deviceId, Callback<Device> callback);
}
