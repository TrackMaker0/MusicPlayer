package com.example.music_xiehailong;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_xiehailong.R;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private List<MusicInfo> musicInfoList; // Or any data model representing a song

    public SongListAdapter(List<MusicInfo> musicInfoList, OnDataSetChangeListener listener) {
        this.musicInfoList = musicInfoList;
        this.onDataSetChangeListener = listener;
    }

    private SongListAdapter.OnDataSetChangeListener onDataSetChangeListener;

    public interface OnDataSetChangeListener {
        void onDataSetChange();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfoList.get(position);
        holder.musicNameView.setText(musicInfo.getMusicName());
        holder.authorView.setText(musicInfo.getAuthor());
        holder.removeView.setOnClickListener(v -> {
            DataManager.remove(musicInfo);
            notifyDataSetChanged();
            if (onDataSetChangeListener != null) onDataSetChangeListener.onDataSetChange();
        });

        holder.filledView.setOnClickListener(v -> {
            DataManager.setCurrentMusic(musicInfo);
            notifyDataSetChanged();
            if (onDataSetChangeListener != null) onDataSetChangeListener.onDataSetChange();
        });

        MusicInfo currentMusicInfo = DataManager.getCurrentMusic();
        if (currentMusicInfo != null && currentMusicInfo.equals(musicInfo)) {
            holder.musicNameView.setTextColor(Color.argb(255, 51, 37, 205));
            holder.authorView.setTextColor(Color.argb(255, 51, 37, 205));
            holder.backgroundView.setBackground(new ColorDrawable(Color.argb(8,0,0,0)));
        } else {
            holder.musicNameView.setTextColor(Color.BLACK);
            holder.authorView.setTextColor(Color.BLACK);
            holder.backgroundView.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView musicNameView;
        TextView authorView;
        ImageView removeView;
        View filledView;
        View backgroundView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            musicNameView = itemView.findViewById(R.id.musicName);
            authorView = itemView.findViewById(R.id.author);
            removeView = itemView.findViewById(R.id.remove);
            filledView = itemView.findViewById(R.id.fillView);
            backgroundView = itemView.findViewById(R.id.backgroundView);
        }
    }
}

