package com.example.music_xiehailong;

import android.content.Context;
import androidx.annotation.NonNull;
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
}
