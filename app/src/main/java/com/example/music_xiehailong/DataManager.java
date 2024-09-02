package com.example.music_xiehailong;

import android.content.Context;
import android.content.SharedPreferences;

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

    public static MusicPlayerService getMusicPlayerService() {
        return musicPlayerService;
    }

    public static void setMusicPlayerService(MusicPlayerService musicPlayerService) {
        DataManager.musicPlayerService = musicPlayerService;
    }

    public static void addItem(MusicInfo musicInfo) {
        if (musicInfoList.contains(musicInfo)) return;
        musicInfoList.add(musicInfo);
    }

    public static void addItem(int index, MusicInfo musicInfo) {
        if (musicInfoList.contains(musicInfo)) return;
        musicInfoList.add(index, musicInfo);
    }

    public static void remove(MusicInfo musicInfo) {
        musicInfoList.remove(musicInfo);
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
}
