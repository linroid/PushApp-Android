package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Device;


import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by linroid on 7/24/15.
 */
public interface DeviceService {
    @POST("/device/bind")
    void bindDevice(@Body Device device, Callback<Device> callback);
}
