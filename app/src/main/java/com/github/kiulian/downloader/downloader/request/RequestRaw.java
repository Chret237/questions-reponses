package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.downloader.request.RequestRaw;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestRaw.class */
public abstract class RequestRaw<T extends RequestRaw<T>> extends Request<T, String> {
    public abstract String getDownloadUrl();
}
