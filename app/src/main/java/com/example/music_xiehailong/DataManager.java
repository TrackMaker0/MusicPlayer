package com.example.music_xiehailong;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public static List<MusicInfo> musicInfoList = new ArrayList<>();
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
}
