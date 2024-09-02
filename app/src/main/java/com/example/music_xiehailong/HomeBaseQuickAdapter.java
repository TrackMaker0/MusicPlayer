package com.example.music_xiehailong;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class HomeBaseQuickAdapter extends BaseMultiItemQuickAdapter<HomeItem, BaseViewHolder>
        implements LoadMoreModule {

    private Context context;

    public HomeBaseQuickAdapter(Context context, List<HomeItem> homeItemList) {
        super(homeItemList);
        this.context = context;
        addItemType(HomeItem.TYPE_BANNER, R.layout.item_banner);
        addItemType(HomeItem.TYPE_HORIZONTAL_CARD, R.layout.item_exclusive_song);
        addItemType(HomeItem.TYPE_SINGLE_COLUMN, R.layout.item_daily_recommend);
        addItemType(HomeItem.TYPE_DOUBLE_COLUMN, R.layout.item_hot_music);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, HomeItem homeItem) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HomeItem.TYPE_BANNER:
                BannerViewHolder bannerViewHolder = new BannerViewHolder(holder.itemView);
                bannerViewHolder.bind(context, homeItem.getMusicInfoList());
                break;
            case HomeItem.TYPE_HORIZONTAL_CARD:
                ExclusiveViewHolder exclusiveViewHolder = new ExclusiveViewHolder(holder.itemView);
                exclusiveViewHolder.bind(context, homeItem.getMusicInfoList());
                break;
            case HomeItem.TYPE_SINGLE_COLUMN:
                RecommendViewHolder recommendViewHolder = new RecommendViewHolder(holder.itemView);
                recommendViewHolder.bind(context, holder.itemView, homeItem.getMusicInfoList().get(0));
                break;
            case HomeItem.TYPE_DOUBLE_COLUMN:
                HotSongViewHolder hotSongViewHolder = new HotSongViewHolder(holder.itemView);
                hotSongViewHolder.bind(context, holder.itemView, homeItem.getMusicInfoList().get(0), homeItem.getMusicInfoList().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unsupported view holder type");
        }
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private ViewPager2 viewPager;
        private BannerAdapter adapter;
        private Runnable autoScrollRunnable;
        private final int LooperTime = 6 * 1000;

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
            adapter = new BannerAdapter(context, musicInfoList);
            viewPager.setAdapter(adapter);
            viewPager.postDelayed(autoScrollRunnable, LooperTime);
        }
    }

    public static class ExclusiveViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private ExclusiveAdapter adapter;

        public ExclusiveViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.exclusive_recycler);
        }

        public void bind(Context context, List<MusicInfo> musicInfoList) {
            adapter = new ExclusiveAdapter(context, musicInfoList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

    public static class RecommendViewHolder extends RecyclerView.ViewHolder {
        private MyMusicView myMusicView;

        public RecommendViewHolder(@NonNull View itemView) {
            super(itemView);
            myMusicView = itemView.findViewById(R.id.myRecommendMusicView);
        }

        public void bind(Context context, View view, MusicInfo musicInfo) {
            MyMusicView.bind(myMusicView, musicInfo, context, view);
        }
    }

    public static class HotSongViewHolder extends RecyclerView.ViewHolder {
        private MyMusicView myMusicView1;
        private MyMusicView myMusicView2;

        public HotSongViewHolder(@NonNull View itemView) {
            super(itemView);
            myMusicView1 = itemView.findViewById(R.id.myMusicView1);
            myMusicView2 = itemView.findViewById(R.id.myMusicView2);
        }

        public void bind(Context context, View view, MusicInfo item1, MusicInfo item2) {
            MyMusicView.bind(myMusicView1, item1, context, view);
            MyMusicView.bind(myMusicView2, item2, context, view);
        }
    }
}
