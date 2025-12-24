package com.github.kiulian.downloader.model;

import android.support.v4.os.EnvironmentCompat;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/Extension.class */
public class Extension {
    private final String value;
    public static final Extension MPEG4 = new Extension("mp4");
    public static final Extension WEBM = new Extension("webm");
    public static final Extension _3GP = new Extension("3gp");
    public static final Extension FLV = new Extension("flv");
    public static final Extension M4A = new Extension("m4a");
    public static final Extension WEBA = new Extension("weba");
    public static final Extension JSON3 = new Extension("json3");
    public static final Extension SUBRIP = new Extension("srt");
    public static final Extension TRANSCRIPT_V1 = new Extension("srv1");
    public static final Extension TRANSCRIPT_V2 = new Extension("srv2");
    public static final Extension TRANSCRIPT_V3 = new Extension("srv3");
    public static final Extension TTML = new Extension("ttml");
    public static final Extension WEBVTT = new Extension("vtt");
    public static final Extension UNKNOWN = new Extension(EnvironmentCompat.MEDIA_UNKNOWN);

    private Extension(String str) {
        this.value = str;
    }

    public boolean isAudio() {
        return equals(M4A) || equals(WEBM);
    }

    public boolean isSubtitle() {
        return equals(SUBRIP) || equals(TRANSCRIPT_V1) || equals(TRANSCRIPT_V2) || equals(TRANSCRIPT_V3) || equals(TTML) || equals(WEBVTT) || equals(JSON3);
    }

    public boolean isVideo() {
        return equals(MPEG4) || equals(WEBM) || equals(_3GP) || equals(FLV);
    }

    public String value() {
        return this.value;
    }
}
