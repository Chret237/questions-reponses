package com.github.kiulian.downloader.downloader;

import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.Response;
import java.io.File;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/Downloader.class */
public interface Downloader {
    Response<File> downloadVideoAsFile(RequestVideoFileDownload requestVideoFileDownload);

    Response<Void> downloadVideoAsStream(RequestVideoStreamDownload requestVideoStreamDownload);

    Response<String> downloadWebpage(RequestWebpage requestWebpage);
}
