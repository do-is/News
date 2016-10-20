package com.miki33.ayk.report.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class SnackBar {
    public static void showShorTime(View view, String string) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show();
    }


    public static void showError(View view, String string) {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG).show();
    }

    public static void showErrorSet(final Context context, View view, String string, String stringtwo) {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG)
                .setAction(stringtwo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }
}
