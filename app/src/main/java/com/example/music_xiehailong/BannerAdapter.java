package com.example.music_xiehailong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<MusicInfo> musicInfoList;
    private Context context;

    public BannerAdapter(Context context, List<MusicInfo> musicInfoList) {
        this.context = context;
        this.musicInfoList = musicInfoList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfoList.get(position);
        holder.bind(context, holder.itemView, musicInfo);
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private MyMusicView myMusicView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            myMusicView = itemView.findViewById(R.id.myBannerMusicView);
        }

        public void bind(Context context, View view, MusicInfo musicInfo) {
            MyMusicView.bind(myMusicView, musicInfo, context, view);
        }
    }
}
