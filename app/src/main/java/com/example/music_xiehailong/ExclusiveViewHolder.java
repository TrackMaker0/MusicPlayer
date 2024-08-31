package com.example.music_xiehailong;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExclusiveViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;
    private MusicAdapter adapter;

    public ExclusiveViewHolder(@NonNull View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.exclusive_recycler);
    }

    public void bind(Context context, List<MusicInfo> musicInfoList) {
        adapter = new MusicAdapter(context, musicInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }
}
