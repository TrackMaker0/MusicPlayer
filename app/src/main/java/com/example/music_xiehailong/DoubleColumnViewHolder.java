package com.example.music_xiehailong;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class DoubleColumnViewHolder extends RecyclerView.ViewHolder {
    private ImageView coverView;
    private TextView musicNameView;
    private TextView authorView;
    private View addBtn;

    public DoubleColumnViewHolder(@NonNull View itemView) {
        super(itemView);
        coverView = itemView.findViewById(R.id.cover);
        musicNameView = itemView.findViewById(R.id.musicName);
        authorView = itemView.findViewById(R.id.author);
        addBtn = itemView.findViewById(R.id.addItem);
    }

    public void bind(Context context, View view, MusicInfo item) {
        if (item == null) {
            throw new IllegalArgumentException("MusicInfo item cannot be null");
        }
        Glide.with(view)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.placeholder) // 设置加载中的占位图
                .error(R.drawable.error) // 设置加载失败的占位图
                .into(coverView);
        musicNameView.setText(item.getMusicName());
        authorView.setText(item.getAuthor());
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getMusicName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
