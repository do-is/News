package com.example.guerdun.news.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.guerdun.news.enity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guerdun on 16/10/16 016.
 */

public class FavortieDb {

    private static FavortieDb favortieDb;

    private static final String DBNAME = "Favorite";
    private static final int VERSION = 1;

    private DbHelp dbHelp;
    private SQLiteDatabase db;

    public FavortieDb(Context context) {
        dbHelp = new DbHelp(context, DBNAME, null, VERSION);
        db = dbHelp.getWritableDatabase();
    }

    public synchronized static FavortieDb getInstance(Context context) {
        if (favortieDb == null) {
            favortieDb = new FavortieDb(context);
        }
        return favortieDb;
    }

    public void savedata(News.question news) {

        if (news != null){

            ContentValues values = new ContentValues();
            values.put("id", news.getId());
            values.put("title", news.getTitle());
            values.put("image", news.getImages().get(0));
            db.insert(DBNAME, null, values);
        }

    }


    public List<News.question> loaddata() {
        List<News.question> favoritelist = new ArrayList<News.question>();
        Cursor cursor = db.query(DBNAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                News.question news = new News().new question();
                ArrayList<String> image = new ArrayList<String>();
                news.setId(cursor.getInt(cursor.getColumnIndex("id")));
                news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                image.add(cursor.getString(cursor.getColumnIndex("image")));
                news.setImages(image);
                favoritelist.add(news);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoritelist;
    }

    public boolean isFavorite(News.question news) {
        Cursor cursor = db.query(DBNAME, null, "id = ?", new String[]{news.getId() + ""}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public void deleteFavorite(News.question news) {
        db.delete(DBNAME, "title = ?", new String[]{news.getTitle()});
    }
}
