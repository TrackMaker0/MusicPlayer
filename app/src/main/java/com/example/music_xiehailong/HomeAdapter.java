package com.example.music_xiehailong;

import android.content.Context;
import android.util.Log;
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
                Log.d(TAG, "onCreateViewHolder: ");
                return null;
            case HomeItem.TYPE_HORIZONTAL_CARD:
                Log.d(TAG, "onCreateViewHolder: ");
                return null;
            case HomeItem.TYPE_SINGLE_COLUMN:
                view = inflater.inflate(R.layout.fragment_daily_recommend, parent, false);
                return new SingleColumnViewHolder(view); // Use SingleColumnViewHolder
            case HomeItem.TYPE_DOUBLE_COLUMN:
                view = inflater.inflate(R.layout.fragment_hot_music, parent, false);
                return new DoubleColumnViewHolder(view); // Use DoubleColumnViewHolder
            default:
                throw new IllegalArgumentException("Unsupported view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeItem homeItem = homeItems.get(position);
        MusicInfo musicInfo = homeItem.getContentItems().get(0); // Assuming you always get the first item

        switch (homeItem.getModuleType()) {
            case HomeItem.TYPE_BANNER:
                Log.d(TAG, "onBindViewHolder: ");
                break;
            case HomeItem.TYPE_HORIZONTAL_CARD:
                Log.d(TAG, "onBindViewHolder: ");
                break;
            case HomeItem.TYPE_SINGLE_COLUMN:
                SingleColumnViewHolder singleColumnViewHolder = (SingleColumnViewHolder) holder;
                singleColumnViewHolder.bind(context, holder.itemView, musicInfo);
                break;
            case HomeItem.TYPE_DOUBLE_COLUMN:
                DoubleColumnViewHolder doubleColumnViewHolder = (DoubleColumnViewHolder) holder;
                doubleColumnViewHolder.bind(context, holder.itemView, musicInfo);
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
