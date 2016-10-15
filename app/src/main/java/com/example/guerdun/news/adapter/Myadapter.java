package com.example.guerdun.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.guerdun.news.R;

import java.util.List;

import com.example.guerdun.news.enity.News;
import com.example.guerdun.news.util.Itemonclicklistener;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {


    private Context context;
    private List<News.question> newses;
    private Itemonclicklistener mlistener;

    public Myadapter(Context context, List<News.question> newses) {
        this.context = context;
        this.newses = newses;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view,mlistener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        News.question news = newses.get(position);
        holder.Title.setText(news.getTitle());

        String url = news.getImages().get(0);

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.images);

    }


    @Override
    public int getItemCount() {
        return newses.size();
    }

    public void setOnClickListener(Itemonclicklistener listener){
        this.mlistener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Title;
        final ImageView images;
        private Itemonclicklistener listener;

        public MyViewHolder(View itemView, Itemonclicklistener listener) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.card_title);
            images = (ImageView) itemView.findViewById(R.id.card_image);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(v,getLayoutPosition());
        }
    }


}
