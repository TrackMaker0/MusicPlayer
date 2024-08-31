package com.example.music_xiehailong;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class BannerViewHolder extends RecyclerView.ViewHolder {

    private ViewPager2 viewPager;
    private MusicAdapter adapter;
    private Runnable autoScrollRunnable;
    private final int LooperTime = 10 * 1000;

    public BannerViewHolder(@NonNull View itemView) {
        super(itemView);
        viewPager = itemView.findViewById(R.id.banner_viewpager);
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
                viewPager.setCurrentItem(nextItem, true);
                viewPager.postDelayed(this, LooperTime); // 每3秒自动轮播
            }
        };
    }

    public void bind(Context context, List<MusicInfo> musicInfoList) {
        adapter = new MusicAdapter(context, musicInfoList);
        viewPager.setAdapter(adapter);
        viewPager.postDelayed(autoScrollRunnable, LooperTime);
    }
}
