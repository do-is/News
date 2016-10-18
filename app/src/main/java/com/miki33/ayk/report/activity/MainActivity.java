package com.miki33.ayk.report.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.miki33.ayk.report.Db.NewsListDb;
import com.miki33.ayk.report.R;
import com.miki33.ayk.report.about.AboutDeveloper;
import com.miki33.ayk.report.adapter.Myadapter;
import com.miki33.ayk.report.enity.News;
import com.miki33.ayk.report.util.FormatData;
import com.miki33.ayk.report.util.IsNetWork;
import com.miki33.ayk.report.util.Itemonclicklistener;
import com.miki33.ayk.report.util.SnackBar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;
    private List<News.question> list = new ArrayList<News.question>();

    private static final String uri = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String beforeurl = "http://news.at.zhihu.com/api/4/news/before/";
    private int mYEAR = Calendar.getInstance().get(Calendar.YEAR);
    private int mMONTH = Calendar.getInstance().get(Calendar.MONTH);
    private int mDAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
        loadjsondata(uri);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshload();
            }
        });
    }

    private void initview() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setDistanceToTriggerSync(300);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_red_light,
                android.R.color.holo_orange_dark);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingtoLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();


                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int last = manager.findLastCompletelyVisibleItemPosition();
                    int count = manager.getItemCount();

                    if (last == (count - 1) && isSlidingtoLast) {
                        loadjsondata(beforeurl + FormatData.format(mYEAR, mMONTH, mDAY--));

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

    public void loadjsondata(String parseurl) {
        if (IsNetWork.connect(this)) {
            StringRequest request = new StringRequest(parseurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getGson(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SnackBar.showError(recyclerView, "请求出错");
                }
            });

            Volley.newRequestQueue(this).add(request);

        } else {
            new AsyncTask<Void, Void, List<News.question>>() {

                @Override
                protected List<News.question> doInBackground(Void... params) {
                    list = NewsListDb.getInstance(getApplicationContext()).loadNewsList();
                    return list;
                }

                @Override
                protected void onPostExecute(List<News.question> questions) {
                    showresults();
                    super.onPostExecute(questions);
                }
            }.execute();

            SnackBar.showErrorSet(this, recyclerView, "网络出错，请检查网络", "设置");
        }

    }

    public void getGson(String response) {
        Gson gson = new Gson();
        News news = gson.fromJson(response, News.class);

        for (final News.question question : news.getStories()) {
            list.add(question);
            question.setDate(news.getDate());

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    if (!NewsListDb.getInstance(getApplicationContext()).isExist(question)) {
                        NewsListDb.getInstance(getApplicationContext()).saveNewsList(question);
                    }
                    return null;
                }
            }.execute();

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
                    BrowerNews.startNewDetail(MainActivity.this,news);

//                    Intent intent = new Intent(MainActivity.this, BrowerNews.class);
//                    intent.putExtra("id", news.getId());
//                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(myadapter);
        } else {
            myadapter.notifyDataSetChanged();
        }

        refreshstop();
    }

    private void refreshload() {

        if (IsNetWork.connect(this)) {
            list.clear();
            loadjsondata(uri);
        } else {
            SnackBar.showErrorSet(this, recyclerView, "网络出错，请检查网络", "设置");
            refreshstop();
        }


    }

    private void refreshstop() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

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
            case R.id.clear:
                NewsListDb.getInstance(this).DeleteCache();
                break;
            case R.id.myfavorite:
                startActivity(new Intent(MainActivity.this, Favorite.class));
                break;
//            case R.id.download:
//                refreshload();
//                getDetailGson();
//                break;
        }
        return true;
    }


}