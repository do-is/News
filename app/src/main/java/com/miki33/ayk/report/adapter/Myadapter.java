package com.miki33.ayk.report.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miki33.ayk.report.R;
import com.miki33.ayk.report.enity.News;
import com.miki33.ayk.report.util.FormatData;
import com.miki33.ayk.report.util.Itemonclicklistener;

import java.util.List;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {


    private Context context;
    private List<News.question> newses;
    private Itemonclicklistener mlistener;

    private static final int NewsList = 0;
    private static final int NewsList_Date = 1;

    public Myadapter(Context context, List<News.question> newses) {
        this.context = context;
        this.newses = newses;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return NewsList;
        }
        String nowdate = newses.get(position).getDate();
        int beforedate = position - 1;
        boolean isdifferent = !newses.get(beforedate).getDate().equals(nowdate);
        return isdifferent ? NewsList_Date : NewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NewsList) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
            return new MyViewHolder(view, mlistener);
        } else {
            View view = LayoutInflater.from(context).
                    inflate(R.layout.card_view_date, parent, false);
            return new DateViewHolder(view,mlistener);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        News.question news = newses.get(position);

        if (holder instanceof DateViewHolder){
            String date = FormatData.date(news.getDate());
            ((DateViewHolder) holder).date_text.setText(date);
            BindHolder(holder,position,news);
        }
        BindHolder(holder,position,news);
    }

    private void BindHolder(MyViewHolder holder, int position, News.question news) {
        holder.Title.setText(news.getTitle());

        String url = news.getImages().get(0);
        if (url != null){
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(holder.images);
        }
    }


    @Override
    public int getItemCount() {
        return newses.size();
    }

    public void setOnClickListener(Itemonclicklistener listener) {
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Title;
        final ImageView images;
        private Itemonclicklistener listener;
        private CardView cardview;

        public MyViewHolder(View itemView, Itemonclicklistener listener) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.card_title);
            images = (ImageView) itemView.findViewById(R.id.card_image);
            cardview = (CardView) itemView.findViewById(R.id.card_view);
            this.listener = listener;
            cardview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(v, getLayoutPosition());
        }
    }

    public class DateViewHolder extends MyViewHolder {

        private TextView date_text;

        public DateViewHolder(View itemView, Itemonclicklistener listener) {
            super(itemView, listener);

            date_text = (TextView) itemView.findViewById(R.id.date_text);
        }
    }


}
