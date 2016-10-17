package com.example.guerdun.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guerdun.news.R;
import com.example.guerdun.news.enity.News;
import com.example.guerdun.news.util.Itemonclicklistener;

import java.util.List;

/**
 * Created by guerdun on 16/10/16 016.
 */

public class FEAdapter extends RecyclerView.Adapter<FEAdapter.MyViewHolder> {

    private Context context;
    private int resourceid;
    private LayoutInflater inflater;
    private List<News.question> list;
    private Itemonclicklistener listener;

    public FEAdapter(Context context,int resourceid) {
        this.context = context;
        this.resourceid = resourceid;
        this.inflater = LayoutInflater.from(context);
    }

    public FEAdapter(Context context, int resourceid, List<News.question> list) {
        this.context = context;
        this.resourceid = resourceid;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(resourceid,parent,false);
        return new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        News.question question = list.get(position);
        holder.cardtitle.setText(question.getTitle());
        Glide.with(context)
                .load(question.getImages().get(0))
                .centerCrop()
                .into(holder.cardimage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnClickListneenr(Itemonclicklistener listener){
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cardtitle;
        private ImageView cardimage;
        private Itemonclicklistener listener;

        public MyViewHolder(View itemView, Itemonclicklistener listener) {
            super(itemView);

            cardtitle = (TextView) itemView.findViewById(R.id.card_title);
            cardimage = (ImageView) itemView.findViewById(R.id.card_image);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.OnItemClick(v,getLayoutPosition());
        }
    }
}
