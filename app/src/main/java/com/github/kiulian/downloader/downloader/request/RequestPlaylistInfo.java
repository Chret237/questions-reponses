package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.playlist.PlaylistInfo;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestPlaylistInfo.class */
public class RequestPlaylistInfo extends Request<RequestPlaylistInfo, PlaylistInfo> {
    private final String playlistId;

    public RequestPlaylistInfo(String str) {
        this.playlistId = str;
    }

    public String getPlaylistId() {
        return this.playlistId;
    }
}
