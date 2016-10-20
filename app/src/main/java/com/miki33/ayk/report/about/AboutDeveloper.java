package com.miki33.ayk.report.about;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.miki33.ayk.report.R;
import com.miki33.ayk.report.SystemLoad.GetVsion;
import com.miki33.ayk.report.activity.MainActivity;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class AboutDeveloper {
    public static void show(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(((MainActivity) context).getTitle());
        dialog.setIcon(R.drawable.news_icon);

        dialog.setMessage("Version " + GetVsion.load(context) + "\n" + "by Miki33");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
