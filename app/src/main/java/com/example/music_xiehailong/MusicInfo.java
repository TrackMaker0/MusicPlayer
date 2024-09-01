package com.example.music_xiehailong;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

// MusicInfo 对象
public class MusicInfo implements Parcelable {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj instanceof MusicInfo) return false;
        MusicInfo musicInfo = (MusicInfo) obj;
        return super.equals(musicInfo.id);
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public MusicInfo() {
        // Default constructor
    }

    public MusicInfo(Parcel in) {
        id = in.readInt();
        musicName = in.readString();
        author = in.readString();
        coverUrl = in.readString();
        musicUrl = in.readString();
        lyricUrl = in.readString();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(musicName);
        dest.writeString(author);
        dest.writeString(coverUrl);
        dest.writeString(musicUrl);
        dest.writeString(lyricUrl);
    }
}
