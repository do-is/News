package com.example.guerdun.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.guerdun.news.Db.FavortieDb;
import com.example.guerdun.news.R;
import com.example.guerdun.news.enity.News;
import com.example.guerdun.news.util.SnackBar;
import com.google.gson.Gson;

import com.example.guerdun.news.enity.Detail;

public class BrowerNews extends AppCompatActivity {

    private News.question news;

    private WebView webView;
    private ImageView imageView;
    private TextView title;
    private TextView ImageSource;
    private Detail detail = new Detail();
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarlayout;

    private String url = "http://news-at.zhihu.com/api/4/news/";
    private boolean isfavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                SnackBar.showError(webView,"没有网络");
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void initview() {
        webView = (WebView) findViewById(R.id.detail_webview);
        imageView = (ImageView) findViewById(R.id.detail_image);
        title = (TextView) findViewById(R.id.detail_title);
        ImageSource = (TextView) findViewById(R.id.detail_source);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbarlayout = (CollapsingToolbarLayout) findViewById(R.id.coll_tool_bar);

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
        toolbarlayout.setTitle(detail.getTitle());

        String html = gethtml(detail.getBody());
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", null);
    }

    private String gethtml(String body) {
        body = body.replace("<div class=\"headline\">", "");
        body = body.replace("<div class=\"img-place-holder\"></div>", "");

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
//        if (App.getThemeValue() == Theme.NIGHT_THEME){
//            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
//        }
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
        if (isfavorite){
            menu.findItem(R.id.contentfavorite).setIcon(R.drawable.ic_star_white_48px);
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
                String message = detail.getTitle()+" " + detail.getShare_url() + "\t\t\t" + "来自News应用";
                sendmg.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sendmg, "分享到"));
                break;
            case R.id.contentfavorite:

                if (isfavorite){
                    FavortieDb.getInstance(this).deleteFavorite(news);
                    item.setIcon(R.drawable.ic_star_border_white_48px);
                    isfavorite = false;
                } else {
                    FavortieDb.getInstance(this).savedata(news);
                    item.setIcon(R.drawable.ic_star_white_48px);
                    isfavorite = true;
                }
//                System.out.println(isfavorite);

                break;
            default:
                break;
        }
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        System.out.println("记动了menu");
//        if(isfavorite){
//            menu.findItem(R.id.contentfavorite).setIcon(R.drawable.ic_star_white_48px);
//            isfavorite = false;
//        } else{
//            menu.findItem(R.id.contentfavorite).setIcon(R.drawable.ic_star_border_white_48px);
//            isfavorite = true;
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

    public static void startNewDetail(Context context, News.question question){
        Intent intent = new Intent(context, BrowerNews.class);
        intent.putExtra("News", question);
        context.startActivity(intent);
    }
}
