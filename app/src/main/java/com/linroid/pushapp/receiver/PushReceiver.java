package com.linroid.pushapp.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.model.*;
import com.linroid.pushapp.service.DownloadService;
import com.linroid.pushapp.util.AndroidUtil;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/28/15.
 */
public class PushReceiver extends BroadcastReceiver {
    private NotificationManager nm;
    private Context context;
    @Inject
    Gson gson;

    @Override
    @DebugLog
    public void onReceive(Context context, Intent intent) {
        App.from(context).component().inject(this);
        this.context = context;
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Timber.d("onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.sprintBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Timber.d("JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Timber.d("接受到推送下来的自定义消息");
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Timber.d("接受到推送下来的通知");
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Timber.d("用户点击打开了通知");
        } else {
            Timber.d("Unhandled intent - " + intent.getAction());
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        switch (type) {
            case Constants.PUSH_TYPE_PACKAGE:
                Pack pack = gson.fromJson(message, Pack.class);
//                Push push =  Push.PushBuilder.aPush()
//                        .withPackageId(pack.getId())
//                        .withIsOk(true)
//                        .withMsgId(msgId)
//                        .withPackage(pack)
//                        .build();
                onReceivePackagePush(pack);
                break;
            default:
                break;
        }
    }

    private void onReceivePackagePush(Pack pack) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.EXTRA_PACKAGE, pack);
        context.startService(intent);
    }
}
