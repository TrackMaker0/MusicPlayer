package com.example.music_xiehailong;

import android.annotation.SuppressLint;
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

    public SongListAdapter(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
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
//        if (position >= musicInfoList.size()) {
//            holder.itemView.setVisibility(View.INVISIBLE);
//            return;
//        }
        MusicInfo musicInfo = musicInfoList.get(position);
        holder.musicNameView.setText(musicInfo.getMusicName());
        holder.authorView.setText(musicInfo.getAuthor());
        holder.removeView.setOnClickListener(v -> {
            DataManager.remove(musicInfo);
            notifyDataSetChanged();
        });
        holder.filledView.setOnClickListener(v -> {
            DataManager.setCurrentMusic(musicInfo);
        });
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

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            musicNameView = itemView.findViewById(R.id.musicName);
            authorView = itemView.findViewById(R.id.author);
            removeView = itemView.findViewById(R.id.remove);
            filledView = itemView.findViewById(R.id.fillView);
        }
    }
}

