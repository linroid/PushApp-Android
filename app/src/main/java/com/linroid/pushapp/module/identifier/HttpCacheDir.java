package com.linroid.pushapp.module.identifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by linroid on 7/26/15.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpCacheDir {
}
