package com.example.music_xiehailong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExclusiveAdapter extends RecyclerView.Adapter<ExclusiveAdapter.ExclusiveItemViewHolder> {

    private List<MusicInfo> musicInfoList;
    private Context context;

    public ExclusiveAdapter(Context context, List<MusicInfo> musicInfoList) {
        this.context = context;
        this.musicInfoList = musicInfoList;
    }

    @NonNull
    @Override
    public ExclusiveItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_exclusive, parent, false);
        return new ExclusiveItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExclusiveItemViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfoList.get(position);
        holder.bind(context, holder.itemView, musicInfo);
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public static class ExclusiveItemViewHolder extends RecyclerView.ViewHolder {
        private MyMusicView myMusicView;

        public ExclusiveItemViewHolder(@NonNull View itemView) {
            super(itemView);
            myMusicView = itemView.findViewById(R.id.myExclusiveMusicView);
        }

        public void bind(Context context, View view, MusicInfo musicInfo) {
            MyMusicView.bind(myMusicView, musicInfo, context, view);
        }
    }
}
