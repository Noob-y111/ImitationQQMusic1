package com.example.imitationqqmusic.model.bean;

import androidx.annotation.NonNull;

public class SongItem {

    private String name;
    private String singer;
    private String songMid;
    private long albumId;
    private Object albumPath;
    private boolean isFromInternet;

    public SongItem(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongMid() {
        return songMid;
    }

    public void setSongMid(String songMid) {
        this.songMid = songMid;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Object getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(Object albumPath) {
        this.albumPath = albumPath;
    }

    public boolean isFromInternet() {
        return isFromInternet;
    }

    public void setFromInternet(boolean fromInternet) {
        isFromInternet = fromInternet;
    }

    @NonNull
    @Override
    public String toString() {
        return "SongItem{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", songMid='" + songMid + '\'' +
                ", albumId=" + albumId +
                ", albumPath=" + albumPath +
                ", isFromInternet=" + isFromInternet +
                '}';
    }
}
