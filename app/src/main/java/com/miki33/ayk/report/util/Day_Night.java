package com.miki33.ayk.report.util;

/**
 * Created by guerdun on 16/10/19 019.
 */

public enum Day_Night {

    DAY("DAY", 0),
    NIGHT("NIGHT", 1);

    private String name;
    private int code;

    private Day_Night(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
