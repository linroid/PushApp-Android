package com.linroid.pushapp.ui;

import android.os.Bundle;

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
}
