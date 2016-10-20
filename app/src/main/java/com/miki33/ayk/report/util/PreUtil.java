package com.miki33.ayk.report.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by guerdun on 16/10/19 019.
 */

public class PreUtil {

    private static final String NAME = "com.miki33.ayk.report";
    private static final String MODE = "DAY_NiGHT_MODE";
    private SharedPreferences mpreferences;

    public PreUtil(Context context) {
        this.mpreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存模式设置
     *
     * @param mode
     * @return
     */
    public boolean setMODE(Day_Night mode) {
        SharedPreferences.Editor editor = mpreferences.edit();
        editor.putString(MODE, mode.getName());
        return editor.commit();
    }

    /**
     * 夜间模式
     *
     * @return
     */
    public boolean isNight() {
        String mode = mpreferences.getString(MODE, Day_Night.DAY.getName());
        return Day_Night.NIGHT.getName().equals(mode);
    }


    /**
     * 日间模式
     *
     * @return
     */
    public boolean isDay() {
        String mode = mpreferences.getString(MODE, Day_Night.DAY.getName());
        return Day_Night.DAY.getName().equals(mode);
    }
}
