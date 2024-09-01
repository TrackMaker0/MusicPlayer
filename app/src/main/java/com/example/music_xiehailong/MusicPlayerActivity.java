package com.example.music_xiehailong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView coverView, loopView, prevView, playView, nextView, listView, likeView, leaveView;
    private TextView musicNameView, authorView, currentTimeView, totalTimeView;
    private SeekBar seekBar;
    private View backgroundView;
    private RecyclerView lyricsRecyclerView;
    private LyricsAdapter lyricsAdapter;
    private LinearLayoutManager layoutManager;
    private ObjectAnimator rotateAnimator;
    private MusicService musicService;
    private boolean isBound = false;

    public static final String TAG = "MyMusicPlayerActivity";
    private int loopState = 0;//循环状态  0-顺序播放 1-单曲循环 2-随机播放
    private final int LOOP_ORDER = 0;
    private final int LOOP_SINGLE = 1;
    private final int LOOP_RANDOM = 2;
    private final int EMPTY_LINE = 10;//歌词前后空白行
    private int numLoopState = 3;//循环状态数
    private int UserNotScrollTime = 0;// 用户停止滑动时间
    private boolean statList = true;//播放列表可见, 初始默认可见
    private boolean isUserSeeking = false;//是否在调整进度条
    private boolean isUserScrolling = false;//是否在滚动歌词
    private boolean FistPrepare = true;//是否在调整进度条

    List<LrcParser.LrcLine> lrcLines = new ArrayList<>();
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // 初始化控件和默认参数
        Init();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lyricsRecyclerView.setLayoutManager(layoutManager);
        lyricsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isUserScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
                if (isUserScrolling) UserNotScrollTime = 0;
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;
            // 初始化MyMediaPlayer
            setupMediaPlayer();
            // 绑定点击事件
            bindOnClickListener();
            // 开始进度条更新时间
            timer = new Timer();
            timer.schedule(new TimerTask() {  //匿名类
                public void run() { //定时器函数体
                    runOnUiThread(() -> updateTime());
                }
            }, 0, 1000);
            // 手动刷新UI
            try {
                updateMusicInfo();
            } catch (IOException e) {
                Log.e(TAG, "Error updating music info", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private void Init() {
        coverView = findViewById(R.id.cover);
        loopView = findViewById(R.id.loop);
        prevView = findViewById(R.id.prev);
        playView = findViewById(R.id.play);
        nextView = findViewById(R.id.next);
        listView = findViewById(R.id.list);
        likeView = findViewById(R.id.like);
        leaveView = findViewById(R.id.leave);
        musicNameView = findViewById(R.id.musicName);
        authorView = findViewById(R.id.author);
        currentTimeView = findViewById(R.id.currentTime);
        totalTimeView = findViewById(R.id.totalTime);
        seekBar = findViewById(R.id.seekBar);
        backgroundView = findViewById(R.id.backgroundView);
        lyricsRecyclerView = findViewById(R.id.lyrics_recycler_view);
    }

    public void bindOnClickListener() {

        coverView.setOnClickListener(v -> {
            coverView.setVisibility(View.GONE);
            lyricsRecyclerView.setVisibility(View.VISIBLE);
        });

        // 播放控制绑定
        playView.setOnClickListener(v -> {
            if (!musicService.isPrepared()) return;
            if (musicService.isPlaying()) {
                musicService.pauseMusic();
                playView.setImageResource(R.drawable.ic_play);  // 切换为播放图标
                // 停止动画
                float currentRotation = coverView.getRotation();
                setCurrentRotationAngle(currentRotation);
                rotateAnimator.cancel();
            } else {
                musicService.playMusic();
                playView.setImageResource(R.drawable.ic_pause);  // 切换为暂停图标
                // 开始动画
                if (rotateAnimator != null && rotateAnimator.isRunning()) rotateAnimator.cancel();
                float currentRotation = getCurrentRotationAngle();
                startRotationAnimation(currentRotation);
            }
        });
        nextView.setOnClickListener(v -> musicService.nextMusic());
        prevView.setOnClickListener(v -> musicService.prevMusic());
        loopView.setOnClickListener(v -> {
            loopState = (loopState + 1) % numLoopState;
            musicService.setLoopState(loopState);
            if (loopState == LOOP_ORDER) loopView.setImageResource(R.drawable.ic_ordered);
            if (loopState == LOOP_SINGLE) loopView.setImageResource(R.drawable.ic_loop);
            if (loopState == LOOP_RANDOM) loopView.setImageResource(R.drawable.ic_random);
        });

        // 进度条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                musicService.seekTo(progress);
                isUserSeeking = false;
            }
        });

        // 退出函数绑定
        leaveView.setOnClickListener(v -> finish());

        // 歌曲列表
        listView.setOnClickListener(v -> {
            SongListBottomSheetFragment bottomSheet = new SongListBottomSheetFragment();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });
    }

    private void setupMediaPlayer() {
        // 设置OnPrepared事件
        musicService.setOnPreparedListener(mp -> {
            musicService.setPrepared(true);
            if (!FistPrepare) musicService.start();
            FistPrepare = false;
            try {
                updateMusicInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // 设置播放结束事件
        musicService.setOnCompletionListener(mp -> {
            // 音乐播放完毕后切换到下一首
            if (!musicService.isLooping()) {
                musicService.nextMusic();
            }
        });
    }

    private void startRotationAnimation(float startAngle) {
        rotateAnimator = ObjectAnimator.ofFloat(coverView, "rotation", startAngle, startAngle + 360f);
        rotateAnimator.setDuration(10000); // Duration of one full rotation
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimator.start();
    }

    private float getCurrentRotationAngle() {
        Object tag = coverView.getTag();
        if (tag instanceof Float) {
            return (Float) tag;
        } else {
            return 0f;
        }
    }

    private void setCurrentRotationAngle(float angle) {
        coverView.setTag(angle); // Save the current angle
    }

    private void updateMusicInfo() throws IOException {
        MusicInfo musicInfo = musicService.getCurrentMusicInfo();
        if (musicInfo == null) return;
        // 更新歌曲信息，背景颜色
        Glide.with(this)
                .asBitmap()
                .load(musicInfo.getCoverUrl())
                .placeholder(R.drawable.placeholder) // 设置加载中的占位图
                .error(R.drawable.error) // 设置加载失败的占位图
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(palette -> {
                            if (palette != null) {
                                int defaultColor = Color.GRAY; // 默认颜色，避免提取失败
                                int dominantColor = palette.getDominantColor(Color.WHITE);
                                int vibrantColor = palette.getVibrantColor(Color.WHITE);

                                // 调整颜色以避免太淡
                                int finalDominantColor = adjustColorIfTooLight(dominantColor);
                                int finalVibrantColor = adjustColorIfTooLight(vibrantColor);

                                runOnUiThread(() -> {
                                    backgroundView.setBackgroundColor(finalDominantColor);
                                    getWindow().setStatusBarColor(finalDominantColor);
                                });
                            }
                        });
                        coverView.setImageBitmap(resource);
                    }

                    // 检查并调整颜色的亮度和饱和度
                    private int adjustColorIfTooLight(int color) {
                        float[] hsv = new float[3];
                        Color.colorToHSV(color, hsv);

                        // 检查亮度 (hsv[2])，范围是0-1，低于0.5认为颜色较深
                        if (hsv[2] > 0.8f) {
                            hsv[2] = 0.7f; // 降低亮度，避免颜色太淡
                        }

                        // 检查饱和度 (hsv[1])，范围是0-1，低于0.3认为颜色较灰
                        if (hsv[1] < 0.3f) {
                            hsv[1] = 0.3f; // 增加饱和度，避免颜色太灰
                        }

                        return Color.HSVToColor(hsv);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        musicNameView.setText(musicInfo.getMusicName());
        authorView.setText(musicInfo.getAuthor());
        // 更新进度条设置
        seekBar.setMax(musicService.getDuration());
        totalTimeView.setText(formatTime(musicService.getDuration()));

        // 获取歌词
        getLyricFromUrl();

        // 更新播放状态控件
        updateTime();
    }

    private void getLyricFromUrl() {
        MusicInfo musicInfo = musicService.getCurrentMusicInfo();
        if (musicInfo == null) return;

        String lyricUrl = musicInfo.getLyricUrl();
        new LrcParser().parseLrcFromUrl(lyricUrl, new LrcParser.LrcCallback() {
            @Override
            public void onSuccess(List<LrcParser.LrcLine> lrcLinesResult) {
                runOnUiThread(() -> {
                    lrcLines = lrcLinesResult;

                    // 为歌词前后添加空白行，方便歌词显示
                    for (int i = 0; i < EMPTY_LINE; i++)
                        lrcLines.add(0, new LrcParser.LrcLine(0, ""));
                    for (int i = 0; i < EMPTY_LINE; i++)
                        lrcLines.add(new LrcParser.LrcLine(0, ""));

                    // 实现 OnItemClickListener 接口
                    LyricsAdapter.OnItemClickListener onItemClickListener = new LyricsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            lyricsRecyclerView.setVisibility(View.GONE);
                            coverView.setVisibility(View.VISIBLE);
                        }
                    };
                    lyricsAdapter = new LyricsAdapter(lrcLines, onItemClickListener);
                    lyricsRecyclerView.setAdapter(lyricsAdapter);
                });
            }

            @Override
            public void onError(IOException e) {
                runOnUiThread(() -> {
                    // Handle error case, e.g., show a toast or log the error
                    Log.e("LrcParser", "Failed to load lyrics", e);
                });
            }
        });
    }

    public void updateTime() {
        updateCurTime();//更新时间控件
        updatePlayTime();//更新进度条
        updateLyrics();//更新歌词进度
    }

    private void updateCurTime() {
        currentTimeView.setText(formatTime(musicService.getCurrentPosition()));
    }

    private void updatePlayTime() {
        seekBar.setProgress(musicService.getCurrentPosition());
    }

    private void updateLyrics() {
        if (lrcLines.isEmpty()) return;
        if (lyricsAdapter == null) return;

        int currentMillis = musicService.getCurrentPosition();
        int currentLineIndex = findCurrentLineIndex(currentMillis);

        if (!isUserScrolling) {
            UserNotScrollTime++;
            // 如果用户有一会儿没滑动歌词，那么歌词自动滚到当前行
            if (++UserNotScrollTime > 2) {
                if (lyricsRecyclerView.getChildAt(0) == null) return;
                int recyclerViewHeight = lyricsRecyclerView.getHeight();
                int itemHeight = lyricsRecyclerView.getChildAt(0).getHeight();
                int offset = (recyclerViewHeight / 2) - (itemHeight / 2);
                // 滚动到指定位置，并将当前行居中
                layoutManager.scrollToPositionWithOffset(currentLineIndex, offset);
            }
        }

        // 高亮当前歌词行
        lyricsAdapter.updateCurrentLine(currentLineIndex);
    }

    private int findCurrentLineIndex(int currentMillis) {
        for (int i = 0; i < lrcLines.size(); i++) {
            if (i == lrcLines.size() - 1 ||
                    (currentMillis >= lrcLines.get(i).timeInMillis &&
                            currentMillis < lrcLines.get(i + 1).timeInMillis)) {
                return i;
            }
        }
        return 0;
    }

    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateAnimator != null && rotateAnimator.isRunning()) rotateAnimator.cancel();
        // 关闭定时器
        if (timer != null) timer.cancel();
        // 解绑Service
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_in, R.anim.slide_down_out);
    }
}