package com.example.guerdun.news.about;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.guerdun.news.R;
import com.example.guerdun.news.activity.MainActivity;
import com.example.guerdun.news.SystemLoad.GetVsion;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class AboutDeveloper {
    public static void show(Context context){
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(((MainActivity)context).getTitle());
        dialog.setIcon(R.mipmap.ic_launcher);

        dialog.setMessage("Version " + GetVsion.load(context) + "\n" + "by 朱先生");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
}
