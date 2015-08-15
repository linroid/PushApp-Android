package com.linroid.pushapp.util;


import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import retrofit.mime.TypedFile;

/**
 * 为Retrofit的文件上传提供进度回调
 * Created by linroid on 8/15/15.
 */
public class CountingTypedFile extends TypedFile implements Runnable {
    private static final int BUFFER_SIZE = 4096;
    /** 文件大小 **/
    private final long fileSize;
    /** 已上传的大小 **/
    private long uploadedBytes = 0;

    private final ProgressListener listener;
    private final Handler handler;
    /**
     * Constructs a new typed file.
     *
     * @param mimeType 文件的mimeType
     * @param file 要上传的文件
     * @param listener 进度监听
     * @throws NullPointerException if file or mimeType is null
     */
    public CountingTypedFile(String mimeType, File file, ProgressListener listener) {
        super(mimeType, file);
        this.listener = listener;
        this.fileSize = file.length();
        this.handler = new Handler(Looper.getMainLooper());
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
                this.handler.post(this);
            }
        } finally {
            in.close();
        }
    }

    @Override
    public void run() {
        CountingTypedFile.this.listener.onProgress(fileSize, uploadedBytes);
    }

    public interface ProgressListener {
        void onProgress(long total, long uploaded);
    }
}
