package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Authorization;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pagination;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.PartMap;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by linroid on 7/24/15.
 */
public interface DeviceService {
    @POST("/device/bind")
    void bindDevice(@Body Device device, Callback<Authorization> callback);

    @GET("/device/check")
    void checkToken(@Query("token") String token, @Query("device_id") String deviceId, Callback<Device> callback);

    @GET("/device")
    Observable<Pagination<Device>> listDevice(@Query("page") int page);

    @FormUrlEncoded
    @PUT("/device")
    void updateDevice(@FieldMap Map<String, String> params, Callback<Device> callback);
}
