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

import com.linroid.pushapp.model.Push;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by linroid on 8/14/15.
 */
public interface PushService {
    @POST("/push")
    @FormUrlEncoded
    void installPackage(@Field("devices") String deviceIds, @Field("package") int packId, Callback<Push> callback);
    @Multipart
    @POST("/push")
    void installLocal(@Part("devices") TypedString deviceIds, @Part("file") TypedFile apk, Callback<Push> callback);
}
