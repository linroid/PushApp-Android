package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.model.Token;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by linroid on 7/24/15.
 */
public interface DeviceService {
    @GET("/device")
    Observable<Pagination<Device>> listDevice(@Query("page") int page);

    @FormUrlEncoded
    @PUT("/device/{id}")
    void updateDevice(@Path("id") int id, @FieldMap Map<String, String> params, Callback<Device> callback);

    @GET("/device/token")
    Observable<Token> token();
}
