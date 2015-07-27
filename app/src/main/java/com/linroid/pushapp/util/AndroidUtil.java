package com.linroid.pushapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.linroid.pushapp.model.InstallPackage;

import java.io.File;

/**
 * Created by linroid on 7/26/15.
 */
public class AndroidUtil {
    public static String sprintBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key: bundle.keySet()){
            sb.append(key);
            sb.append(" => ");
            sb.append(bundle.get(key));
            sb.append("\n");
        }
        return sb.toString();
    }
    public static void installPackage(Context context, InstallPackage pack) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(pack.getPath())),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
