package com.github.kiulian.downloader.model.subtitles;

import com.github.kiulian.downloader.model.Extension;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/subtitles/Subtitles.class */
public class Subtitles {
    private Extension format;
    private final boolean fromCaptions;
    private String translationLanguage;
    private final String url;

    Subtitles(String str, boolean z) {
        this.url = str;
        this.fromCaptions = z;
    }

    public Subtitles formatTo(Extension extension) {
        this.format = extension;
        return this;
    }

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

    public Subtitles translateTo(String str) {
        if (this.fromCaptions) {
            this.translationLanguage = str;
        }
        return this;
    }
}
