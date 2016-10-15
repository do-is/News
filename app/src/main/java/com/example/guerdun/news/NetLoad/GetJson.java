package com.example.guerdun.news.NetLoad;

import com.example.guerdun.news.enity.News;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by guerdun on 16/10/15 015.
 */

public class GetJson {
    public static ArrayList<News.question> loadnewslist(String response, ArrayList<News.question> list) {
        Gson gson = new Gson();
        News news = gson.fromJson(response, News.class);
        for (News.question question : news.getStories()){
            list.add(question);
        }
        return list;
    }



}