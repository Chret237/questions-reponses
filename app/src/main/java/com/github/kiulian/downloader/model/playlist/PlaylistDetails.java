package com.github.kiulian.downloader.model.playlist;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/playlist/PlaylistDetails.class */
public class PlaylistDetails {
    private String author;
    private String playlistId;
    private String title;
    private int videoCount;
    private int viewCount;

    public PlaylistDetails(String str, String str2, String str3, int i, int i2) {
        this.playlistId = str;
        this.title = str2;
        this.author = str3;
        this.videoCount = i;
        this.viewCount = i2;
    }

    public String author() {
        return this.author;
    }

    public String playlistId() {
        return this.playlistId;
    }

    public String title() {
        return this.title;
    }

    public int videoCount() {
        return this.videoCount;
    }

    public int viewCount() {
        return this.viewCount;
    }
}
