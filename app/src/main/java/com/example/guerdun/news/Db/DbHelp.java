package com.example.guerdun.news.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guerdun on 16/10/11 011.
 */

public class DbHelp extends SQLiteOpenHelper {

    private static final String NewsList = ("create table if not exists NewsList(" +
            "id integer not null," +
            "title text," +
            "image text)");

    private static final String NewsContent = ("create table if not exists NewsContent(" +
            "id integer not null," +
            "title text," +
            "image text," +
            "iamge_source text" +
            "body text)");

    private static final String Favorite = ("create table if not exists Favorite(" +
            "id integer not null," +
            "title text," +
            "image text)");

    public DbHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsList);
        db.execSQL(NewsContent);
        db.execSQL(Favorite);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
