package com.miki33.ayk.report.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miki33.ayk.report.Db.FavortieDb;
import com.miki33.ayk.report.R;
import com.miki33.ayk.report.adapter.FEAdapter;
import com.miki33.ayk.report.enity.News;
import com.miki33.ayk.report.util.Itemonclicklistener;
import com.miki33.ayk.report.util.PreUtil;

import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity {

    private PreUtil preUtil;
    private RecyclerView recyclerView;
    private FEAdapter feadapter;
    private List<News.question> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDate();
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
    }

    private void initTheme() {
        if (preUtil.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    private void initDate() {
        preUtil = new PreUtil(this);
    }

    private void initview() {
        list = new ArrayList<News.question>();
        recyclerView = (RecyclerView) findViewById(R.id.myfavorite_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        feadapter = new FEAdapter(this, R.layout.card_view, list = FavortieDb.getInstance(this).loaddata());
        feadapter.OnClickListneenr(new Itemonclicklistener() {
            @Override
            public void OnItemClick(View v, int position) {
                BrowerNews.startNewDetail(Favorite.this, list.get(position));
            }
        });
        recyclerView.setAdapter(feadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
