package com.example.music_xiehailong;

import com.google.gson.annotations.SerializedName;

// MusicInfo 对象
public class MusicInfo {
    @SerializedName("id")
    private int id;

    @SerializedName("musicName")
    private String musicName;

    @SerializedName("author")
    private String author;

    @SerializedName("coverUrl")
    private String coverUrl;

    @SerializedName("musicUrl")
    private String musicUrl;

    @SerializedName("lyricUrl")
    private String lyricUrl;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMusicName() { return musicName; }
    public void setMusicName(String musicName) { this.musicName = musicName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getMusicUrl() { return musicUrl; }
    public void setMusicUrl(String musicUrl) { this.musicUrl = musicUrl; }

    public String getLyricUrl() { return lyricUrl; }
    public void setLyricUrl(String lyricUrl) { this.lyricUrl = lyricUrl; }
}