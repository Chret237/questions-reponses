package com.github.kiulian.downloader;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/YoutubeException.class */
public abstract class YoutubeException extends Exception {

    /* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/YoutubeException$BadPageException.class */
    public static class BadPageException extends YoutubeException {
        public BadPageException(String str) {
            super(str);
        }
    }

    /* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/YoutubeException$CipherException.class */
    public static class CipherException extends YoutubeException {
        public CipherException(String str) {
            super(str);
        }
    }

    /* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/YoutubeException$DownloadException.class */
    public static class DownloadException extends YoutubeException {
        public DownloadException(String str) {
            super(str);
        }
    }

    private YoutubeException(String str) {
        super(str);
    }
}
