package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestSubtitlesInfo.class */
public class RequestSubtitlesInfo extends Request<RequestSubtitlesInfo, List<SubtitlesInfo>> {
    private final String videoId;

    public RequestSubtitlesInfo(String str) {
        this.videoId = str;
    }

    public String getVideoId() {
        return this.videoId;
    }
}
