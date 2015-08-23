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

package com.linroid.pushapp.util;


import android.support.v4.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import retrofit.mime.TypedFile;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * 为Retrofit的文件上传提供进度回调
 * Created by linroid on 8/15/15.
 */
public class CountingTypedFile extends TypedFile {
    private static final int BUFFER_SIZE = 4096;
    /**
     * 文件大小
     **/
    private final Long fileSize;
    /**
     * 已上传的大小
     **/
    private long uploadedBytes = 0;

    PublishSubject<Pair<Long, Long>> subject;

    /**
     * Constructs a new typed file.
     *
     * @param file 要上传的文件
     * @throws NullPointerException if file or mimeType is null
     */
    public CountingTypedFile(File file) {
        super(AndroidUtil.getMimeType(file.getAbsolutePath()), file);
        this.fileSize = file.length();
        subject = PublishSubject.create();
    }

    /**
     * 订阅进度
     *
     * @param subscriber
     * @return
     */
    public Subscription subscribe(Subscriber<Pair<Long, Long>> subscriber) {
        return subject.debounce(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream in = new FileInputStream(super.file());
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                uploadedBytes += read;
                out.write(buffer, 0, read);
                subject.onNext(new Pair<>(fileSize, uploadedBytes));
            }
        } finally {
            in.close();
            subject.onCompleted();
        }
    }

}
