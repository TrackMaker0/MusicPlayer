package com.example.music_xiehailong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class BannerFragment extends Fragment {

    private static final String ARG_MUSIC_LIST = "music_list";
    private ViewPager2 viewPager;
    private MusicAdapter adapter;
    private List<MusicInfo> bannerItems;
    private Runnable autoScrollRunnable;
    private final int LooperTime = 10 * 1000;

    public static BannerFragment newInstance(List<MusicInfo> musicList) {
        BannerFragment fragment = new BannerFragment();
        Bundle args = new Bundle();

        Gson gson = new Gson();
        String musicListJson = gson.toJson(musicList);
        args.putString(ARG_MUSIC_LIST, musicListJson);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String musicListJson = getArguments().getString(ARG_MUSIC_LIST);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MusicInfo>>() {}.getType();
            bannerItems = gson.fromJson(musicListJson, listType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.banner_viewpager);

        adapter = new MusicAdapter(getContext(), bannerItems);
        viewPager.setAdapter(adapter);

//        int startPosition = adapter.getItemCount() / 2;
//        viewPager.setCurrentItem(startPosition, false);

        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
                viewPager.setCurrentItem(nextItem, true);
                viewPager.postDelayed(this, LooperTime); // 每3秒自动轮播
            }
        };

//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                int totalCount = adapter.getItemCount();
//                if (position == 0) {
//                    // 滑到第一个时，设置到最后一个
//                    viewPager.setCurrentItem(totalCount - 2, false);
//                } else if (position == totalCount - 1) {
//                    // 滑到最后一个时，设置到第一个
//                    viewPager.setCurrentItem(1, false);
//                }
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始自动轮播
        viewPager.postDelayed(autoScrollRunnable, LooperTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止自动轮播
        viewPager.removeCallbacks(autoScrollRunnable);
    }
}
