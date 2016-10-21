package com.miki33.ayk.report.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.miki33.ayk.report.Db.NewsListDb;
import com.miki33.ayk.report.R;
import com.miki33.ayk.report.about.AboutDeveloper;
import com.miki33.ayk.report.adapter.Myadapter;
import com.miki33.ayk.report.enity.News;
import com.miki33.ayk.report.util.Day_Night;
import com.miki33.ayk.report.util.FormatData;
import com.miki33.ayk.report.util.IsNetWork;
import com.miki33.ayk.report.util.Itemonclicklistener;
import com.miki33.ayk.report.util.PreUtil;
import com.miki33.ayk.report.util.SnackBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String uri = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String beforeurl = "http://news.at.zhihu.com/api/4/news/before/";
    private Toolbar toolbar;
    private PreUtil preUtil;
    private RecyclerView mrecyclerView;
    private Myadapter myadapter;
    private SwipeRefreshLayout refreshLayout;
    private List<News.question> list = new ArrayList<News.question>();
    private int mYEAR = Calendar.getInstance().get(Calendar.YEAR);
    private int mMONTH = Calendar.getInstance().get(Calendar.MONTH);
    private int mDAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        initTheme();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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

    private void initTheme() {
        if (preUtil.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    private void initData() {
        preUtil = new PreUtil(this);
    }

    private void initview() {
        toolbar = (Toolbar) findViewById(R.id.re_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5);
        }
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mrecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setDistanceToTriggerSync(300);
        refreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_red_light,
                android.R.color.holo_orange_dark);
        if (preUtil.isNight())
            refreshLayout.setProgressBackgroundColorSchemeResource(R.color.backgroundnight);
        mrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    SnackBar.showError(mrecyclerView, "请求出错");
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

            SnackBar.showErrorSet(this, mrecyclerView, "网络出错，请检查网络", "设置");
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
                    BrowerNews.startNewDetail(MainActivity.this, news);

//                    Intent intent = new Intent(MainActivity.this, BrowerNews.class);
//                    intent.putExtra("id", news.getId());
//                    startActivity(intent);
                }
            });
            mrecyclerView.setAdapter(myadapter);
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
            SnackBar.showErrorSet(this, mrecyclerView, "网络出错，请检查网络", "设置");
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
        if (preUtil.isNight()){
            menu.findItem(R.id.change).setTitle("日间模式");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change:
                changeThemeByZhiHu();
                if (preUtil.isDay()){
                    item.setTitle("夜间模式");
                } else{
                    item.setTitle("日间模式");
                }
                break;
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

    //--------------------更改主题-------------------//
    private void changeThemeByZhiHu() {
        showAnimation();
        toggleThemeSetting();
        refreshUI();
    }

    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorview = getWindow().getDecorView();
        Bitmap cahceBitmap = getCacheBitmapfromView(decorview);
        if (decorview instanceof ViewGroup && cahceBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cahceBitmap));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorview).addView(view, layoutParams);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorview).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapfromView(View view) {
        boolean drawingCacheEnable = true;
        view.setDrawingCacheEnabled(drawingCacheEnable);
        view.buildDrawingCache(drawingCacheEnable);
        Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }


    private void toggleThemeSetting() {
        if (preUtil.isDay()) {
            preUtil.setMODE(Day_Night.NIGHT);
            setTheme(R.style.NightTheme);
        } else {
            preUtil.setMODE(Day_Night.DAY);
            setTheme(R.style.DayTheme);
        }
    }

    private void refreshUI() {
        TypedValue backvalue = new TypedValue();
        TypedValue textvalue = new TypedValue();
        TypedValue cardvalue = new TypedValue();
        TypedValue barcolor = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.backgroundCustom, backvalue, true);
        theme.resolveAttribute(R.attr.TextColorCustom, textvalue, true);
        theme.resolveAttribute(R.attr.cardviewCustom, cardvalue, true);
        theme.resolveAttribute(R.attr.colorPrimary, barcolor, true);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.recycler_linear_layout);
        linearLayout.setBackgroundResource(backvalue.resourceId);

        refreshLayout.setProgressBackgroundColorSchemeResource(backvalue.resourceId);
        toolbar.setBackgroundResource(barcolor.resourceId);
        Resources resources = getResources();

        int childcount = mrecyclerView.getChildCount();
        for (int childindex = 0; childindex < childcount; childindex++) {
            ViewGroup childview = (ViewGroup) mrecyclerView.getChildAt(childindex);
            View infolayout = childview.findViewById(R.id.card_relative_layout);
            infolayout.setBackgroundResource(cardvalue.resourceId);

            TextView cardtitle = (TextView) childview.findViewById(R.id.card_title);
            cardtitle.setTextColor(resources.getColor(textvalue.resourceId));

        }


        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName())
                    .getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mrecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mrecyclerView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        refreshStatusBar();
    }

    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue value = new TypedValue();
            TypedValue ncolor = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, value, true);
            theme.resolveAttribute(R.attr.colorPrimary, ncolor, true);
            getWindow().setStatusBarColor(theme.getResources().getColor(value.resourceId));
            getWindow().setNavigationBarColor(theme.getResources().getColor(ncolor.resourceId));
        }
    }


}