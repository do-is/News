//package com.example.guerdun.news.SystemLoad;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
///**
// * Created by guerdun on 16/10/15 015.
// */
//
//public class RecyviewLoadmore extends RecyclerView.OnScrollListener {
//
//    boolean isSlidingtoLast = false;
//
//    @Override
//    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//            int last = manager.findLastCompletelyVisibleItemPosition();
//            int count = manager.getItemCount();
//
//            if (last == (count - 1) && isSlidingtoLast) {
//                Calendar c = Calendar.getInstance();
//                c.set(mYEAR, mMONTH, mDAY--);
//                Date date = new Date(c.getTimeInMillis());
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//
//                System.out.println(formatter.format(date));
//
//                loadjsondata(beforeurl + formatter.format(date));
//            }
//        }
//
//        super.onScrollStateChanged(recyclerView, newState);
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//    }
//
//    /*
//     boolean isSlidingtoLast = false;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int last = manager.findLastCompletelyVisibleItemPosition();
//                    int count = manager.getItemCount();
//
//                    if (last == (count - 1) && isSlidingtoLast) {
//                        Calendar c = Calendar.getInstance();
//                        c.set(mYEAR, mMONTH, mDAY--);
//                        Date date = new Date(c.getTimeInMillis());
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//
//                        System.out.println(formatter.format(date));
//
//                        loadjsondata(beforeurl + formatter.format(date));
//                    }
//                }
//
//
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                isSlidingtoLast = dy > 0;
//            }
//     */
//}
