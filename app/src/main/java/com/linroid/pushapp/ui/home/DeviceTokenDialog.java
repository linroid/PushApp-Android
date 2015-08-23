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

package com.linroid.pushapp.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.linroid.pushapp.App;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.model.Token;

import java.util.EnumMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by linroid on 8/20/15.
 */
public class DeviceTokenDialog extends DialogFragment {
    public static final String STATE_TOKEN = "token";
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.qrcode_iv)
    ImageView qrcodeIV;
    @Inject
    DeviceService deviceApi;
    Subscription subscription;
    Token token;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<Token> observable;
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_TOKEN)) {
            Token token = savedInstanceState.getParcelable(STATE_TOKEN);
            observable = Observable.just(token);
        } else {
            observable = deviceApi.token();
        }
        subscription = observable.observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Token, Bitmap>() {
                    @Override
                    public Bitmap call(Token token) {
                        DeviceTokenDialog.this.token = token;
                        String url = getString(R.string.txt_device_token_url, BuildConfig.HOST_URL, token.getValue());
                        return generateQrcode(url, qrcodeIV.getMeasuredHeight());
                    }
                })
                .subscribe(new Subscriber<Bitmap>() {
                               @Override
                               public void onCompleted() {
                                   progressBar.setVisibility(View.GONE);
                                   subscription = null;
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Toast.makeText(getActivity(), R.string.toast_get_device_token_failed, Toast.LENGTH_SHORT).show();
                                   getDialog().dismiss();
                               }

                               @Override
                               public void onNext(Bitmap qrcode) {
                                   qrcodeIV.setImageBitmap(qrcode);
                               }
                           }

                );
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (token!=null) {
            outState.putParcelable(STATE_TOKEN, token);
        }
    }

    private Bitmap generateQrcode(String content, int size) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            EnumMap<EncodeHintType, Object> hint = new EnumMap<>(EncodeHintType.class);
            hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hint);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    // pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000
                    // : 0xFFFFFFFF;
                    pixels[offset + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialog_device_token, container, false);
        ButterKnife.bind(this, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(R.string.title_device_qrcode);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
