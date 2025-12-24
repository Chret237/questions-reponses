package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.model.Extension;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestSubtitlesDownload.class */
public class RequestSubtitlesDownload extends RequestWebpage {
    private Extension format;
    private boolean fromCaptions;
    private String translationLanguage;

    public RequestSubtitlesDownload(SubtitlesInfo subtitlesInfo) {
        super(subtitlesInfo.getUrl());
        this.fromCaptions = subtitlesInfo.isFromCaptions();
    }

    public RequestSubtitlesDownload formatTo(Extension extension) {
        this.format = extension;
        return this;
    }

    @Override // com.github.kiulian.downloader.downloader.request.RequestWebpage, com.github.kiulian.downloader.downloader.request.RequestRaw
    public String getDownloadUrl() {
        String str = this.url;
        Extension extension = this.format;
        String str2 = str;
        if (extension != null) {
            str2 = str;
            if (extension.isSubtitle()) {
                str2 = str + "&fmt=" + this.format.value();
            }
        }
        String str3 = this.translationLanguage;
        String str4 = str2;
        if (str3 != null) {
            str4 = str2;
            if (!str3.isEmpty()) {
                str4 = str2 + "&tlang=" + this.translationLanguage;
            }
        }
        return str4;
    }

    public RequestSubtitlesDownload translateTo(String str) {
        if (this.fromCaptions) {
            this.translationLanguage = str;
        }
        return this;
    }
}
