package com.example.music_xiehailong;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.List;

public class MultipleItem implements MultiItemEntity {

    public static final int TYPE_BANNER = 1;
    public static final int TYPE_HORIZONTAL_CARD = 2;
    public static final int TYPE_SINGLE_COLUMN = 3;
    public static final int TYPE_DOUBLE_COLUMN = 4;

    private int itemType;
    private String moduleName;
    private List<MusicInfo> musicInfoList;

    public MultipleItem(int itemType, String moduleName, List<MusicInfo> musicInfoList) {
        this.itemType = itemType;
        this.moduleName = moduleName;
        this.musicInfoList = musicInfoList;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    public void setMusicInfoList(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }
}
