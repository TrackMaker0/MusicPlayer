package com.example.music_xiehailong;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.lang.ref.WeakReference;

public class MusicPlayerService extends Service {

    private MyMediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder(this);

    public MusicPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MyMediaPlayer(MusicPlayerService.this);
    }

    public static class MusicBinder extends Binder {
        private final WeakReference<MusicPlayerService> service;

        public MusicBinder(MusicPlayerService service) {
            this.service = new WeakReference<>(service);
        }

        public MusicPlayerService getService() {
            return service.get();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean isPrepared() {
        return mediaPlayer.isPrepared();
    }

    public void setPlayAllowed(boolean playAllowed) {
        mediaPlayer.setPlayAllowed(playAllowed);
    }

    public boolean isPlayAllowed() {
        return mediaPlayer.isPlayAllowed();
    }

    public void setCurrentSongIndex(int index) {
        mediaPlayer.setCurrentSongIndex(index);
    }

    public int getCurrentSongIndex() {
        return mediaPlayer.getCurrentSongIndex();
    }

    public void notifyItemDeleted(int position) {
        mediaPlayer.notifyItemDeleted(position);
    }

    public int getNumLoopState() {
        return 3;
    }

    public MyMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playMusic() {
        mediaPlayer.playMusic();
    }

    public void pauseMusic() {
        mediaPlayer.pauseMusic();
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

    public int getLoopState() {
        return mediaPlayer.getLoopState();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        mediaPlayer.setOnPreparedListener(listener);
    }

    public void setPrepared(boolean prepared) {
        mediaPlayer.setPrepared(prepared);
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
        super.onDestroy();
        // 释放媒体播放器资源
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
