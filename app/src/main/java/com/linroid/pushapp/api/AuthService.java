package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Binding;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pagination;
import com.squareup.okhttp.Call;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by linroid on 8/12/15.
 */
public interface AuthService {
    @POST("/auth/bind")
    void bindDevice(@Query("token") String token, @Body Device device, Callback<Binding> callback);

    @GET("/auth/check")
    void checkToken(@Query("token") String token, @Query("device_id") String deviceId, Callback<Device> callback);

    @POST("/auth")
    void authUser(@Query("token") String token, Callback<Auth> callback);

    @GET("/auth")
    Observable<Pagination<Auth>> listAuth(@Query("page") int page);

    @DELETE("/auth/{id}")
    void revoke(@Path("id") int id, Callback<Void> callback);
}
