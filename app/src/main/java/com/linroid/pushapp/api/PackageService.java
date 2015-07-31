package com.linroid.pushapp.api;

import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.model.Pagination;


import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by linroid on 7/30/15.
 */
public interface PackageService {
    @GET("/package")
    Observable<Pagination<Pack>> listReceived(@Query("page") int page);
}
