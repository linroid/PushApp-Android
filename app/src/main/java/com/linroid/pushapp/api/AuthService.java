/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pagination;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by linroid on 8/12/15.
 */
public interface AuthService {
    @POST("/auth/bind")
    void bindDevice(@Query("token") String token, @Body Device device, Callback<Account> callback);

    @GET("/auth/check")
    void checkToken(@Query("token") String token, @Query("device_id") String deviceId, Callback<Device> callback);

    @FormUrlEncoded
    @POST("/auth")
    void authUser(@Field("token") String token, Callback<Auth> callback);

    @GET("/auth")
    Observable<Pagination<Auth>> listAuth(@Query("page") int page);

    @DELETE("/auth/{id}")
    Observable<Void> revoke(@Path("id") int id);

    @PATCH("/auth/{id}/recall")
    void recallRevoke(@Path("id") int id, Callback<Auth> auth);
}
