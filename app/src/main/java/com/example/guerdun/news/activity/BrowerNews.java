package com.example.guerdun.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.guerdun.news.R;
import com.example.guerdun.news.enity.News;
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

    private String url = "http://news-at.zhihu.com/api/4/news/";
//    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brower_news);

        webView = (WebView) findViewById(R.id.detail_webview);
        imageView = (ImageView) findViewById(R.id.detail_image);
        title = (TextView) findViewById(R.id.detail_title);
        ImageSource = (TextView) findViewById(R.id.detail_source);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        news = (News.question) getIntent().getSerializableExtra("News");

        StringRequest request = new StringRequest(url + news.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getgsondata(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BrowerNews.this, "请求出错", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);

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
                String message = detail.getTitle()+" " + detail.getShare_url() + "\t\t\t" + "来自帅气的朱总";
                sendmg.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sendmg, "分享到"));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startNewDetail(Context context, News.question question){
        Intent intent = new Intent(context, BrowerNews.class);
        intent.putExtra("News", question);
        context.startActivity(intent);
    }
}
