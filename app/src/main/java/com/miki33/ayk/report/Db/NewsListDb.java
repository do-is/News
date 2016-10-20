package com.miki33.ayk.report.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miki33.ayk.report.enity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guerdun on 16/10/16 016.
 */

public class NewsListDb {

    private static final String DB_NAME = "NewsList";
    private static final int VERSION = 1;
    private static NewsListDb newsListDb;
    private Context context;
    private DbHelp dbHelp;
    private SQLiteDatabase db;

    private NewsListDb(Context context) {
        dbHelp = new DbHelp(context, DB_NAME, null, VERSION);
        db = dbHelp.getWritableDatabase();
    }

    public synchronized static NewsListDb getInstance(Context context) {
        if (newsListDb == null) {
            newsListDb = new NewsListDb(context);
        }
        return newsListDb;
    }

    public void saveNewsList(News.question news) {
        if (news != null) {
            ContentValues values = new ContentValues();
            values.put("id", news.getId());
            values.put("title", news.getTitle());
            values.put("image", news.getImages().get(0));
            values.put("date", news.getDate());
            db.insert(DB_NAME, null, values);
        }
    }

    public List<News.question> loadNewsList() {
        List<News.question> list = new ArrayList<News.question>();
        Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> strings = new ArrayList<String>();
                News.question news = new News().new question();
                news.setId(cursor.getInt(0));
                news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                strings.add(cursor.getString(2));
                news.setImages(strings);
                news.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(news);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void DeleteCache() {
        db.delete(DB_NAME, null, null);
    }


    public boolean isExist(News.question news) {
        Cursor cursor = db.query(DB_NAME, null, "id = ?", new String[]{news.getId() + ""}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }

        return false;
    }


}
