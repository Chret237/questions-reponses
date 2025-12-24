package com.github.kiulian.downloader.downloader;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/YoutubeCallback.class */
public interface YoutubeCallback<T> {
    void onError(Throwable th);

    void onFinished(T t);
}
