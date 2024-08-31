package com.example.music_xiehailong;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoubleColumnViewHolder extends RecyclerView.ViewHolder {
    private MyMusicView myMusicView1;
    private MyMusicView myMusicView2;

    public DoubleColumnViewHolder(@NonNull View itemView) {
        super(itemView);
        myMusicView1 = itemView.findViewById(R.id.myMusicView1);
        myMusicView2 = itemView.findViewById(R.id.myMusicView2);
    }

    public void bind(Context context, View view, MusicInfo item1, MusicInfo item2) {
        MyMusicView.bind(myMusicView1, item1, context, view);
        MyMusicView.bind(myMusicView2, item2, context, view);
    }
}
