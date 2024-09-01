package com.example.music_xiehailong;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyMediaPlayer extends MediaPlayer {
    private final Context context;
    private boolean isPrepared = false;
    private int currentSongIndex = 0;

    private List<MusicInfo> playlist;

    private int loopState = 0;//循环状态  0-顺序播放 1-单曲循环 2-随机播放
    private final int LOOP_ORDER = 0;
    private final int LOOP_SINGLE = 1;
    private final int LOOP_RANDOM = 2;

    public MyMediaPlayer(Context context) {
        this.context = context;
        this.playlist = DataManager.musicInfoList;
        setCurrentSongIndex(0);
    }

    public void setPlaylist(List<MusicInfo> playlist) {
        this.playlist = playlist;
    }

    public void addPlayList(MusicInfo musicInfo) {
        DataManager.addItem(musicInfo);
    }

    public void addPlayList(int index, MusicInfo musicInfo) {
        DataManager.addItem(index, musicInfo);
    }

    public void playMusic() {
        if (isPrepared) {
            start();
        } else {
            prepareAndPlay();
        }
    }

    private void prepareAndPlay() {
        try {
            reset();
            setDataSource(context, Uri.parse(playlist.get(currentSongIndex).getMusicUrl()));
            prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLoopState() {
        return loopState;
    }

    public void setLoopState(int loopState) {
        this.loopState = loopState;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        isPrepared = false;
        this.currentSongIndex = currentSongIndex;
        prepareAndPlay();
    }

    public MusicInfo getCurrentMusicInfo() {
        if (playlist == null) return null;
        return playlist.get(getCurrentSongIndex());
    }

    public void pauseMusic() {
        if (isPlaying()) {
            pause();
        }
    }

    public void stopMusic() {
        stop();
        isPrepared = false;
    }

    public int getTotalSeconds() {
        return getDuration() / 1000;
    }

    public int getCurrentSeconds() {
        return getCurrentPosition() / 1000;
    }

    public void nextMusic() {
        if (loopState == LOOP_ORDER) nextMusic(1);
        if (loopState == LOOP_SINGLE) nextMusic(0);
        if (loopState == LOOP_RANDOM) nextMusic(new Random().nextInt(playlist.size()));
    }

    public void prevMusic() {
        nextMusic(-1);
    }

    public void nextMusic(int dist) {
        isPrepared = false;
        currentSongIndex = (currentSongIndex + dist) % playlist.size();
        prepareAndPlay();
    }

    public boolean isPrepared() {
        return this.isPrepared;
    }

    public void setPrepared(boolean prepared) {
        this.isPrepared = prepared;
    }

    public int getCurrentPosition() {
        return isPrepared ? super.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return isPrepared ? super.getDuration() : 0;
    }

}

