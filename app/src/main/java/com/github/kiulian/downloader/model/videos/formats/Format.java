package com.github.kiulian.downloader.model.videos.formats;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.model.Extension;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/formats/Format.class */
public abstract class Format {
    public static final String AUDIO = "audio";
    public static final String AUDIO_VIDEO = "audio/video";
    public static final String VIDEO = "video";
    protected final Long approxDurationMs;
    protected final Integer bitrate;
    protected final String clientVersion;
    protected final Long contentLength;
    protected final Extension extension;
    private final boolean isAdaptive;
    protected final Itag itag;
    protected final Long lastModified;
    protected final String mimeType;
    protected final String url;

    protected Format(JSONObject jSONObject, boolean z, String str) {
        Itag itagValueOf;
        this.isAdaptive = z;
        this.clientVersion = str;
        try {
            itagValueOf = Itag.valueOf("i" + jSONObject.getInteger("itag"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            itagValueOf = Itag.unknown;
            itagValueOf.setId(jSONObject.getIntValue("itag"));
        }
        this.itag = itagValueOf;
        this.url = jSONObject.getString("url").replace("\\u0026", "&");
        this.mimeType = jSONObject.getString("mimeType");
        this.bitrate = jSONObject.getInteger("bitrate");
        this.contentLength = jSONObject.getLong("contentLength");
        this.lastModified = jSONObject.getLong("lastModified");
        this.approxDurationMs = jSONObject.getLong("approxDurationMs");
        String str2 = this.mimeType;
        if (str2 == null || str2.isEmpty()) {
            this.extension = Extension.UNKNOWN;
            return;
        }
        if (this.mimeType.contains(Extension.MPEG4.value())) {
            if (this instanceof AudioFormat) {
                this.extension = Extension.M4A;
                return;
            } else {
                this.extension = Extension.MPEG4;
                return;
            }
        }
        if (this.mimeType.contains(Extension.WEBM.value())) {
            if (this instanceof AudioFormat) {
                this.extension = Extension.WEBA;
                return;
            } else {
                this.extension = Extension.WEBM;
                return;
            }
        }
        if (this.mimeType.contains(Extension.FLV.value())) {
            this.extension = Extension.FLV;
        } else if (this.mimeType.contains(Extension._3GP.value())) {
            this.extension = Extension._3GP;
        } else {
            this.extension = Extension.UNKNOWN;
        }
    }

    public Integer bitrate() {
        return this.bitrate;
    }

    public String clientVersion() {
        return this.clientVersion;
    }

    public Long contentLength() {
        return this.contentLength;
    }

    public Long duration() {
        return this.approxDurationMs;
    }

    public Extension extension() {
        return this.extension;
    }

    public boolean isAdaptive() {
        return this.isAdaptive;
    }

    public Itag itag() {
        return this.itag;
    }

    public long lastModified() {
        return this.lastModified.longValue();
    }

    public String mimeType() {
        return this.mimeType;
    }

    public abstract String type();

    public String url() {
        return this.url;
    }
}
