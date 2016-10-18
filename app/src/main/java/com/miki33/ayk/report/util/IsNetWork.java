package com.miki33.ayk.report.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class IsNetWork {
    public static boolean connect(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return ni.isAvailable();
        }
        return false;
    }
}
