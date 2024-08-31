package com.example.music_xiehailong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<HomeItem> homeItems;
    public static final String TAG = "MyHomeAdapter";

    public HomeAdapter(Context context, List<HomeItem> homeItems) {
        this.context = context;
        this.homeItems = homeItems;
    }

    @Override
    public int getItemViewType(int position) {
        return homeItems.get(position).getModuleType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case HomeItem.TYPE_BANNER:
                view = inflater.inflate(R.layout.item_banner, parent, false);
                return new BannerViewHolder(view);
            case HomeItem.TYPE_HORIZONTAL_CARD:
                view = inflater.inflate(R.layout.item_exclusive_song, parent, false);
                return new ExclusiveViewHolder(view);
            case HomeItem.TYPE_SINGLE_COLUMN:
                view = inflater.inflate(R.layout.item_daily_recommend, parent, false);
                return new RecommendViewHolder(view); // Use SingleColumnViewHolder
            case HomeItem.TYPE_DOUBLE_COLUMN:
                view = inflater.inflate(R.layout.item_hot_music, parent, false);
                return new HotSongViewHolder(view); // Use DoubleColumnViewHolder
            default:
                throw new IllegalArgumentException("Unsupported view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeItem homeItem = homeItems.get(position);
        List<MusicInfo> musicInfoList = homeItem.getContentItems(); // Assuming you always get the first item

        switch (homeItem.getModuleType()) {
            case HomeItem.TYPE_BANNER:
                BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
                bannerViewHolder.bind(context, musicInfoList);
                break;
            case HomeItem.TYPE_HORIZONTAL_CARD:
                ExclusiveViewHolder exclusiveViewHolder = (ExclusiveViewHolder) holder;
                exclusiveViewHolder.bind(context, musicInfoList);
                break;
            case HomeItem.TYPE_SINGLE_COLUMN:
                RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
                recommendViewHolder.bind(context, holder.itemView, musicInfoList.get(0));
                break;
            case HomeItem.TYPE_DOUBLE_COLUMN:
                HotSongViewHolder hotSongViewHolder = (HotSongViewHolder) holder;
                hotSongViewHolder.bind(context, holder.itemView, musicInfoList.get(0), musicInfoList.get(1));
                break;
            default:
                throw new IllegalArgumentException("Unsupported view holder type");
        }
    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }
}
