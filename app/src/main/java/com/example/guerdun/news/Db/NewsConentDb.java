package com.example.guerdun.news.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.guerdun.news.enity.Detail;

/**
 * Created by guerdun on 16/10/16 016.
 */

public class NewsConentDb {

    private static final String DBNAME = "NewsContent";
    private static final int VERSION = 1;
    private DbHelp dbHelp;
    private SQLiteDatabase db;
    private static NewsConentDb newsConentDb;

    public NewsConentDb(Context context) {
        dbHelp = new DbHelp(context, DBNAME, null, VERSION);
        db = dbHelp.getWritableDatabase();
    }

    public static NewsConentDb getInstance(Context context) {
        if (newsConentDb != null) {
            newsConentDb = new NewsConentDb(context);
        }
        return newsConentDb;
    }

    public void saveNewsContent(Detail newsdetail, int newsid) {
        if (newsdetail != null) {
            ContentValues values = new ContentValues();
            values.put("id",newsid);
            values.put("title", newsdetail.getTitle());
            values.put("iamge_source", newsdetail.getImage_source());
            values.put("image", newsdetail.getImage());
            values.put("body", newsdetail.getBody());
            db.insert(DBNAME, null, values);
        }

    }


    public boolean isExist(Detail detail) {
        Cursor cursor = db.query(DBNAME, null, "title = ?", new String[]{detail.getTitle()}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {

            return false;
        }
    }
}
