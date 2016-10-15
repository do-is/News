package com.example.guerdun.news.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class SnackBar {
    public static void show(RecyclerView recyclerView, String string) {
        Snackbar.make(recyclerView, string, Snackbar.LENGTH_LONG).show();
    }

    public static void showSet(final Context context, RecyclerView recyclerView, String string, String stringtwo) {
        Snackbar.make(recyclerView, string, Snackbar.LENGTH_LONG)
                .setAction(stringtwo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }
}
