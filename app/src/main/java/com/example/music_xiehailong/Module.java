package com.example.music_xiehailong;


import com.google.gson.annotations.SerializedName;

import java.util.List;

// Module 对象
public class Module {
    @SerializedName("moduleConfigId")
    private int moduleConfigId;

    @SerializedName("moduleName")
    private String moduleName;

    @SerializedName("style")
    private int style;

    @SerializedName("musicInfoList")
    private List<MusicInfo> musicInfoList;

    public int getModuleConfigId() {
        return moduleConfigId;
    }

    public void setModuleConfigId(int moduleConfigId) {
        this.moduleConfigId = moduleConfigId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public List<MusicInfo> getMusicInfoList() {
        return musicInfoList;
    }

    public void setMusicInfoList(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
    }
}
