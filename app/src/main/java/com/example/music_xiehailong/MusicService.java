package com.example.music_xiehailong;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

    private MyMediaPlayer mediaPlayer;

    public MusicService() {
    }

    public boolean isPrepared() {
        return mediaPlayer.isPrepared();
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MyMediaPlayer(MusicService.this);
    }

    public void playMusic() {
        mediaPlayer.playMusic();
    }

    public void pauseMusic() {
        mediaPlayer.pauseMusic();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void nextMusic() {
        mediaPlayer.nextMusic();
    }

    public void prevMusic() {
        mediaPlayer.prevMusic();
    }

    public void setLoopState(int loopState) {
        mediaPlayer.setLoopState(loopState);
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        mediaPlayer.setOnPreparedListener(listener);
    }

    public void setPrepared(boolean b) {
        mediaPlayer.setPrepared(b);
    }

    public void start() {
        mediaPlayer.start();
    }

    public void setOnCompletionListener(MyMediaPlayer.OnCompletionListener listener) {
        mediaPlayer.setOnCompletionListener(listener);
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public MusicInfo getCurrentMusicInfo() {
        return mediaPlayer.getCurrentMusicInfo();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
