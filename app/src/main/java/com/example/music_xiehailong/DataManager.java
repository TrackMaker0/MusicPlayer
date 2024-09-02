package com.example.music_xiehailong;

import android.content.Context;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static List<MusicInfo> musicInfoList = new ArrayList<>();
    private static HashMap<Integer, Boolean> likeStatusMap = new HashMap<>();
    private static MusicPlayerService musicPlayerService;
    private static final String PREFS_NAME = "LikeStatusPrefs";
    private static final String LIKE_STATUS_KEY = "LikeStatus";

    public static OnListEmptyListener onListEmptyListener;

    public static interface OnListEmptyListener {
        void OnListEmpty();
    }

    public static List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    public static void setMusicInfoList(List<MusicInfo> musicInfoList) {
        DataManager.musicInfoList = musicInfoList;
    }

    public static void setCurrentMusic(MusicInfo musicInfo) {
        if (musicPlayerService == null) return;
        if (!musicInfoList.contains(musicInfo)) return;
        musicPlayerService.setCurrentSongIndex(musicInfoList.indexOf(musicInfo));
    }

    public static void setOnListEmptyListener(OnListEmptyListener onListEmptyListener) {
        DataManager.onListEmptyListener = onListEmptyListener;
    }

    public static MusicPlayerService getMusicPlayerService() {
        return musicPlayerService;
    }

    public static void setMusicPlayerService(MusicPlayerService musicPlayerService) {
        if (musicPlayerService == null) return;
        DataManager.musicPlayerService = musicPlayerService;
    }

    public static MusicInfo getCurrentMusic() {
        if (musicPlayerService == null) return null;
        return DataManager.musicPlayerService.getCurrentMusicInfo();
    }

    public static void addItem(MusicInfo musicInfo) {
        if (musicInfoList.contains(musicInfo)) return;
        musicInfoList.add(musicInfo);
        if (musicInfoList.size() == 1)
            EventBus.getDefault().postSticky(new MusicListEmptyEvent(false));
    }

    public static void addItem(int index, MusicInfo musicInfo) {
        if (musicInfoList.contains(musicInfo)) return;
        musicInfoList.add(index, musicInfo);
        if (musicInfoList.size() == 1)
            EventBus.getDefault().postSticky(new MusicListEmptyEvent(false));
    }

    public static void remove(MusicInfo musicInfo) {
        if (!musicInfoList.contains(musicInfo)) return;
        int position = musicInfoList.indexOf(musicInfo);
        musicInfoList.remove(musicInfo);
        musicPlayerService.notifyItemDeleted(position);
        if (musicInfoList.isEmpty()) {
            EventBus.getDefault().postSticky(new MusicListEmptyEvent(true));
            if (onListEmptyListener != null) onListEmptyListener.OnListEmpty();
        }
    }

    public static boolean getLikeStatus(MusicInfo currentMusicInfo) {
        return Boolean.TRUE.equals(likeStatusMap.getOrDefault(currentMusicInfo.getId(), false));
    }

    public static void setLikeStatus(MusicInfo currentMusicInfo, boolean isLike) {
        likeStatusMap.put(currentMusicInfo.getId(), isLike);
    }

    // Save the HashMap to SharedPreferences
    public static void saveLikeStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert HashMap to JSONObject
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<Integer, Boolean> entry : likeStatusMap.entrySet()) {
            try {
                jsonObject.put(String.valueOf(entry.getKey()), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String jsonString = jsonObject.toString();

        // Save JSON string to SharedPreferences
        editor.putString(LIKE_STATUS_KEY, jsonString);
        editor.apply();  // apply() writes asynchronously
    }

    // Retrieve the HashMap from SharedPreferences
    public static void loadLikeStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonString = prefs.getString(LIKE_STATUS_KEY, "{}");

        HashMap<Integer, Boolean> likeStatusMap = new HashMap<>();
        try {
            // Convert JSON string back to JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Convert JSONObject to HashMap
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Boolean value = jsonObject.getBoolean(key);
                likeStatusMap.put(Integer.parseInt(key), value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataManager.likeStatusMap = likeStatusMap;
    }

    public static void addAll(List<MusicInfo> randomMusic) {
        randomMusic.forEach(DataManager::addItem);
    }

    public static void nextMusic() {
        if (musicPlayerService == null) return;
        musicPlayerService.nextMusic();
    }

    public static int getCount() {
        return musicInfoList.size();
    }
}
