package com.example.music_xiehailong;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class HomeItem implements MultiItemEntity {
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_HORIZONTAL_CARD = 2;
    public static final int TYPE_SINGLE_COLUMN = 3;
    public static final int TYPE_DOUBLE_COLUMN = 4;

    private int moduleType;
    private String moduleName;
    private List<MusicInfo> contentItems;

    public HomeItem(int moduleType, String moduleName, List<MusicInfo> contentItems) {
        this.moduleType = moduleType;
        this.moduleName = moduleName;
        this.contentItems = contentItems;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<MusicInfo> getContentItems() {
        return contentItems;
    }

    public void setContentItems(List<MusicInfo> contentItems) {
        this.contentItems = contentItems;
    }

    public List<MusicInfo> getMusicInfoList() {
        return contentItems;
    }

    public void setMusicInfoList(List<MusicInfo> contentItems) {
        this.contentItems = contentItems;
    }

    @Override
    public int getItemType() {
        return moduleType;
    }
}

