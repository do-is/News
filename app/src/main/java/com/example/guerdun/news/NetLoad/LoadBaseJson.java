package com.example.guerdun.news.NetLoad;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.guerdun.news.enity.News;
import com.example.guerdun.news.util.IsNetWork;
import com.example.guerdun.news.util.SnackBar;

import java.util.ArrayList;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class LoadBaseJson {

    private static final String News = "http://news-at.zhihu.com/api/4/news/";
    private static final String LASTER_NEWS = "http://news-at.zhihu.com/api/4/news/latest";

    public static String load(final Context context, String LASTER_NEWS){
        final String[] urll = new String[1];
        StringRequest request = new StringRequest(LASTER_NEWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                urll[0] =  response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "3232", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(request);
        return urll[0];
    }

    public static String getNewsList(Context context){
        return load(context, LASTER_NEWS);
    }
}
