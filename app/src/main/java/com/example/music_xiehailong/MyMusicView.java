package com.example.music_xiehailong;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

public class MyMusicView extends androidx.constraintlayout.widget.ConstraintLayout {

    private ImageView coverView;
    private TextView musicNameView;
    private TextView authorView;
    private View addBtn;

    public MyMusicView(@NonNull Context context) {
        super(context);
    }

    public MyMusicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_music, this);

        coverView = (ImageView) findViewById(R.id.cover);
        musicNameView = (TextView) findViewById(R.id.musicName);
        authorView = (TextView) findViewById(R.id.author);
        addBtn = (View) findViewById(R.id.addItem);
    }

    public static void bind(MyMusicView myMusicView, MusicInfo musicInfo, Context context, View view) {
        if (musicInfo != null){
            Glide.with(view)
                    .load(musicInfo.getCoverUrl())
                    .placeholder(R.drawable.placeholder) // 设置加载中的占位图
                    .error(R.drawable.error) // 设置加载失败的占位图
                    .into(myMusicView.getCoverView());
            myMusicView.getMusicNameView().setText(musicInfo.getMusicName());
            myMusicView.getAuthorView().setText(musicInfo.getAuthor());
            myMusicView.getAddBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "将" + musicInfo.getMusicName() + "添加到音乐列表", Toast.LENGTH_SHORT).show();
                }
            });
            myMusicView.getCoverView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, musicInfo.getMusicName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public ImageView getCoverView() {
        return coverView;
    }

    public void setCoverView(ImageView coverView) {
        this.coverView = coverView;
    }

    public TextView getMusicNameView() {
        return musicNameView;
    }

    public void setMusicNameView(TextView musicNameView) {
        this.musicNameView = musicNameView;
    }

    public TextView getAuthorView() {
        return authorView;
    }

    public void setAuthorView(TextView authorView) {
        this.authorView = authorView;
    }

    public View getAddBtn() {
        return addBtn;
    }

    public void setAddBtn(View addBtn) {
        this.addBtn = addBtn;
    }
}
