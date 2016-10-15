package com.example.guerdun.news.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.guerdun.news.R;
import com.example.guerdun.news.about.AboutDeveloper;
import com.example.guerdun.news.util.IsNetWork;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.guerdun.news.adapter.Myadapter;
import com.example.guerdun.news.enity.News;
import com.example.guerdun.news.util.Itemonclicklistener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;
//    private DbHelp dbHelp;
//    private SQLiteDatabase db;
    private List<News.question> list = new ArrayList<News.question>();

    private static final String uri = "http://news-at.zhihu.com/api/4/news/";
    private static final String beforeurl = "http://news.at.zhihu.com/api/4/news/before/";
    private int mYEAR = Calendar.getInstance().get(Calendar.YEAR);
    private int mMONTH = Calendar.getInstance().get(Calendar.MONTH);
    private int mDAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
        loadjsondata(uri + "latest");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshload();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingtoLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();


                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int last = manager.findLastCompletelyVisibleItemPosition();
                    int count = manager.getItemCount();

                    if (last == (count - 1) && isSlidingtoLast) {
                        Calendar c = Calendar.getInstance();
                        c.set(mYEAR, mMONTH, mDAY--);
                        Date date = new Date(c.getTimeInMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

                        System.out.println(formatter.format(date));

                        loadjsondata(beforeurl + formatter.format(date));
                    }
                }


                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingtoLast = dy > 0;
            }
        });
    }

    private void initview() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        dbHelp = new DbHelp(this, "Thisismydb", null, 1);
//        db = dbHelp.getWritableDatabase();

        refreshLayout.setDistanceToTriggerSync(300);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_red_light,
                android.R.color.holo_orange_dark);

    }

    public void loadjsondata(String parseurl) {
        if (IsNetWork.connect(this)) {
            StringRequest request = new StringRequest(parseurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    getGson(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(recyclerView, "网络错误", Snackbar.LENGTH_LONG).show();
                }
            });

            Volley.newRequestQueue(this).add(request);

        } else {
//            Gson gsson = new Gson();
//            Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                do {
//                    News.question question = gsson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), News.question.class);
//                    list.add(question);
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
            showresults();
            Snackbar.make(recyclerView, "网络出错，请检查网络", Snackbar.LENGTH_LONG)
                    .setAction("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }).show();
        }

    }

    private void getGson(String response) {
        Gson gson = new Gson();
        News news = gson.fromJson(response, News.class);

        for (News.question question : news.getStories()) {
            list.add(question);

//            if (!dbishere(question.getId())) {
//
//                db.beginTransaction();
//                values.put("zhihu_id", question.getId());
//                values.put("zhihu_news", gson.toJson(question));
//                values.put("zhihu_time", news.getDate());
//                System.out.println(news.getDate());
//                values.put("zhihu_content", "");
//                db.insert("Zhihu", null, values);
//                values.clear();
//                db.setTransactionSuccessful();
//                db.endTransaction();
//
//
//            }

//            Cachecontent(question.getId());

        }

        showresults();
    }

    public void showresults() {

        if (myadapter == null) {
            myadapter = new Myadapter(this, list);
            myadapter.setOnClickListener(new Itemonclicklistener() {
                @Override
                public void OnItemClick(View v, int position) {
                    News.question news = list.get(position);
                    BrowerNews.startNewDetail(MainActivity.this, news);
                }
            });
            recyclerView.setAdapter(myadapter);
        } else {
            myadapter.notifyDataSetChanged();
        }

        refreshstop();
    }

    private void refreshload() {

        list.clear();
        loadjsondata(uri + "latest");

    }


    private void refreshstop() {

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

//    private boolean dbishere(int id) {
//        Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))) {
//                    return true;
//                }
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return false;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.item1:
//                Snackbar.make(recyclerView, "我是item1", Snackbar.LENGTH_LONG).show();
//                break;
            case R.id.about:
                AboutDeveloper.show(this);
                break;
        }
        return true;
    }
}