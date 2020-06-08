package com.example.imitationqqmusic.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.imitationqqmusic.model.GetDataModel;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SongItem implements Parcelable {

    @SerializedName(value = "songName", alternate = {"title"})
    private String name;

    @SerializedName(value = "singer", alternate = {"artist_name"})
    private String singer;

    @SerializedName(value = "songMid", alternate = {"song_id"})
    private String songMid;

    @SerializedName(value = "album", alternate = {"album_id"})
    private long albumId;

    @SerializedName(value = "albumPath", alternate = {"pic_huge"})
    private Object albumPath;

    private boolean isFromInternet;

    private GetDataModel.ApiKind kind;

    private String path;

    public SongItem(){}

    protected SongItem(Parcel in) {
        name = in.readString();
        singer = in.readString();
        songMid = in.readString();
        albumId = in.readLong();
        albumPath = Objects.requireNonNull(in.readArray(Object.class.getClassLoader()))[0];
        isFromInternet = in.readByte() != 0;
    }

    public static final Creator<SongItem> CREATOR = new Creator<SongItem>() {
        @Override
        public SongItem createFromParcel(Parcel in) {
            return new SongItem(in);
        }

        @Override
        public SongItem[] newArray(int size) {
            return new SongItem[size];
        }
    };

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

    public GetDataModel.ApiKind getKind() {
        return kind;
    }

    public void setKind(GetDataModel.ApiKind kind) {
        this.kind = kind;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SongItem{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", songMid='" + songMid + '\'' +
                ", albumId=" + albumId +
                ", albumPath=" + albumPath +
                ", isFromInternet=" + isFromInternet +
                ", kind=" + kind +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(singer);
        dest.writeString(songMid);
        dest.writeLong(albumId);
        if (albumPath != null){
            dest.writeArray(new Object[]{albumPath});
//            if (albumPath instanceof Integer){
//                dest.writeInt((Integer) albumPath);
//            }else {
//                dest.writeString((String) albumPath);
//            }
        }
        dest.writeByte((byte) (isFromInternet ? 1 : 0));
    }
}

