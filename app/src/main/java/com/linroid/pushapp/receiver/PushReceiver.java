package com.linroid.pushapp.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.gson.Gson;
import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.service.DownloadService;
import com.linroid.pushapp.util.AndroidUtil;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/28/15.
 */
public class PushReceiver extends BroadcastReceiver {
    private NotificationManager nm;
    public static final String ACTION_NEW_DEVICE = "com.linroid.pushapp.action.new_device";
    @Inject
    Gson gson;
    @Inject
    BriteDatabase db;

    @Override
    @DebugLog
    public void onReceive(Context context, Intent intent) {
        App.from(context).component().inject(this);
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
                Intent intent = DownloadService.createNewDownloadIntent(context, pack);
                context.startService(intent);
                break;
            case Constants.PUSH_TYPE_DEVICE:
                Device device = gson.fromJson(message, Device.class);
                saveNewDevice(device);
                break;
            default:
                break;
        }
    }

    private void saveNewDevice(Device device) {
        Cursor cursor = db.query(Device.DB.SQL_ITEM_QUERY, String.valueOf(device.getId()));
        if (cursor.moveToNext()) {
            Timber.d("[%s]设备状态变更", device.getAlias());
            db.update(Device.DB.TABLE_NAME, device.toContentValues(), Pack.DB.WHERE_ID, String.valueOf(device.getId()));
        } else {
            Timber.d("[%s]设备新增", device.getAlias());
            db.insert(Device.DB.TABLE_NAME, device.toContentValues());
        }
    }

}
