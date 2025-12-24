package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.playlist.PlaylistInfo;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestChannelUploads.class */
public class RequestChannelUploads extends Request<RequestPlaylistInfo, PlaylistInfo> {
    private final String channelId;

    public RequestChannelUploads(String str) {
        this.channelId = str;
    }

    public String getChannelId() {
        return this.channelId;
    }
}
