package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.videos.VideoInfo;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestVideoInfo.class */
public class RequestVideoInfo extends Request<RequestVideoInfo, VideoInfo> {
    private final String videoId;

    public RequestVideoInfo(String str) {
        this.videoId = str;
    }

    public String getVideoId() {
        return this.videoId;
    }
}
