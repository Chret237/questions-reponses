package com.github.kiulian.downloader.downloader.response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/response/Response.class */
public interface Response<T> {
    boolean cancel();

    T data();

    T data(long j, TimeUnit timeUnit) throws TimeoutException;

    Throwable error();

    /* renamed from: ok */
    boolean mo13ok();

    ResponseStatus status();
}
