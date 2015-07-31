package com.linroid.pushapp.api;

import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.model.Pagination;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by linroid on 7/30/15.
 */
public interface PackageService {
    @GET("/package")
    void listReceieved(@Query("page") int page, Callback<Pagination<InstallPackage>> callback);
}
