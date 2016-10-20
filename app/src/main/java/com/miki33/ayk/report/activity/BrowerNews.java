package com.miki33.ayk.report.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.miki33.ayk.report.Db.FavortieDb;
import com.miki33.ayk.report.R;
import com.miki33.ayk.report.enity.Detail;
import com.miki33.ayk.report.enity.News;
import com.miki33.ayk.report.util.PreUtil;
import com.miki33.ayk.report.util.SnackBar;

public class BrowerNews extends AppCompatActivity {

    private News.question news;

    private PreUtil preUtil;
    private WebView webView;
    private ImageView imageView;
    private TextView title;
    private TextView ImageSource;
    private Detail detail = new Detail();
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarlayout;

    private String url = "http://news-at.zhihu.com/api/4/news/";
    private boolean isfavorite = false;

    public static void startNewDetail(Context context, News.question question) {
        Intent intent = new Intent(context, BrowerNews.class);
        intent.putExtra("News", question);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initdata();
        inittheme();
//        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brower_news);

        initview();
        news = (News.question) getIntent().getSerializableExtra("News");
        isfavorite = FavortieDb.getInstance(this).isFavorite(news);
        int id = news.getId();
        StringRequest request = new StringRequest(url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getgsondata(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SnackBar.showError(webView, "没有网络");
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void inittheme() {
        if (preUtil.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    private void initdata() {
        preUtil = new PreUtil(this);
    }

    private void initview() {
        webView = (WebView) findViewById(R.id.detail_webview);
        imageView = (ImageView) findViewById(R.id.detail_image);
        title = (TextView) findViewById(R.id.detail_title);
        ImageSource = (TextView) findViewById(R.id.detail_source);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbarlayout = (CollapsingToolbarLayout) findViewById(R.id.coll_tool_bar);

        toolbarlayout.setTitle("  ");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getgsondata(String response) {
        Gson gson = new Gson();
        detail = gson.fromJson(response, Detail.class);

        Glide.with(this)
                .load(detail.getImage())
                .centerCrop()
                .into(imageView);
        title.setText(detail.getTitle());
        ImageSource.setText(detail.getImage_source());

        String html = gethtml(detail.getBody());
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
    }

    private String gethtml(String body) {
        body = body.replace("<div class=\"headline\">", "");
        body = body.replace("<div class=\"img-place-holder\"></div>", "");

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if (preUtil.isNight()) {
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(body)
                .append("</body></html>");

        return builder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        if (isfavorite) {
            menu.findItem(R.id.contentfavorite).setIcon(R.drawable.ic_star_yellow_48px);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.share:
                Intent sendmg = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String message = detail.getTitle() + " " + detail.getShare_url() + "\t\t\t" + "来自" + getTitle() + "应用";
                sendmg.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sendmg, "分享到"));
                break;
            case R.id.contentfavorite:

                if (isfavorite) {
                    FavortieDb.getInstance(this).deleteFavorite(news);
                    item.setIcon(R.drawable.ic_star_white_48px);
                    isfavorite = false;
                } else {
                    FavortieDb.getInstance(this).savedata(news);
                    SnackBar.showShorTime(webView, "已加收藏");
                    item.setIcon(R.drawable.ic_star_yellow_48px);
                    isfavorite = true;
                }

                break;
            default:
                break;
        }
        return true;
    }
}
