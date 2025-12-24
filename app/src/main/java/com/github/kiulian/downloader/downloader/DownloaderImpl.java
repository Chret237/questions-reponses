package com.github.kiulian.downloader.downloader;

import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.request.Request;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.ResponseImpl;
import com.github.kiulian.downloader.model.Utils;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/DownloaderImpl.class */
public class DownloaderImpl implements Downloader {
    private static final int BUFFER_SIZE = 4096;
    private static final int PART_LENGTH = 2097152;
    private final Config config;

    public DownloaderImpl(Config config) {
        this.config = config;
    }

    private static long copyAndCloseInput(InputStream inputStream, OutputStream outputStream, byte[] bArr) throws IOException {
        long j = 0;
        while (true) {
            try {
                long j2 = j;
                int i = inputStream.read(bArr);
                if (i == -1) {
                    return j2;
                }
                if (Thread.interrupted()) {
                    throw new CancellationException();
                }
                outputStream.write(bArr, 0, i);
                j = j2 + i;
            } finally {
                Utils.closeSilently(inputStream);
            }
        }
    }

    private static long copyAndCloseInput(InputStream inputStream, OutputStream outputStream, byte[] bArr, long j, long j2, YoutubeCallback<?> youtubeCallback) throws Throwable {
        long j3;
        if (j == 0) {
            j3 = 0;
        } else {
            try {
                j3 = (j * 100) / j2;
            } catch (Throwable th) {
                th = th;
                Utils.closeSilently(inputStream);
                throw th;
            }
        }
        long j4 = j3;
        long j5 = 0;
        while (true) {
            try {
                int i = inputStream.read(bArr);
                if (i == -1) {
                    Utils.closeSilently(inputStream);
                    return j5;
                }
                if (Thread.interrupted()) {
                    throw new CancellationException();
                }
                outputStream.write(bArr, 0, i);
                long j6 = j5 + i;
                long j7 = ((j + j6) * 100) / j2;
                j5 = j6;
                if (j7 > j4) {
                    if (youtubeCallback instanceof YoutubeProgressCallback) {
                        ((YoutubeProgressCallback) youtubeCallback).onDownloading((int) j7);
                    }
                    j4 = j7;
                    j5 = j6;
                }
            } catch (Throwable th2) {
                th = th2;
                Utils.closeSilently(inputStream);
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: download, reason: merged with bridge method [inline-methods] */
    public File lambda$downloadVideoAsFile$1$DownloaderImpl(RequestVideoFileDownload requestVideoFileDownload) throws IOException {
        Format format = requestVideoFileDownload.getFormat();
        File outputFile = requestVideoFileDownload.getOutputFile();
        YoutubeCallback<File> callback = requestVideoFileDownload.getCallback();
        download(requestVideoFileDownload, format, new FileOutputStream(outputFile));
        if (callback != null) {
            callback.onFinished(outputFile);
        }
        return outputFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: download, reason: merged with bridge method [inline-methods] */
    public String lambda$downloadWebpage$0$DownloaderImpl(RequestWebpage requestWebpage) throws Throwable {
        IOException e;
        int i;
        HttpURLConnection httpURLConnectionOpenConnection;
        int responseCode;
        BufferedReader bufferedReader;
        String downloadUrl = requestWebpage.getDownloadUrl();
        Map<String, String> headers = requestWebpage.getHeaders();
        YoutubeCallback<String> callback = requestWebpage.getCallback();
        int iIntValue = requestWebpage.getMaxRetries() != null ? requestWebpage.getMaxRetries().intValue() : this.config.getMaxRetries();
        Proxy proxy = requestWebpage.getProxy();
        StringBuilder sb = new StringBuilder();
        do {
            e = null;
            try {
                httpURLConnectionOpenConnection = openConnection(downloadUrl, headers, proxy);
                httpURLConnectionOpenConnection.setRequestMethod(requestWebpage.getMethod());
                if (requestWebpage.getBody() != null) {
                    httpURLConnectionOpenConnection.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnectionOpenConnection.getOutputStream(), "UTF-8");
                    try {
                        outputStreamWriter.write(requestWebpage.getBody());
                        outputStreamWriter.flush();
                        outputStreamWriter.close();
                    } finally {
                    }
                }
                responseCode = httpURLConnectionOpenConnection.getResponseCode();
            } catch (IOException e2) {
                e = e2;
                i = iIntValue - 1;
            }
            if (responseCode != 200) {
                YoutubeException.DownloadException downloadException = new YoutubeException.DownloadException("Failed to download: HTTP " + responseCode);
                if (callback != null) {
                    callback.onError(downloadException);
                }
                throw downloadException;
            }
            if (httpURLConnectionOpenConnection.getContentLength() == 0) {
                YoutubeException.DownloadException downloadException2 = new YoutubeException.DownloadException("Failed to download: Response is empty");
                if (callback != null) {
                    callback.onError(downloadException2);
                }
                throw downloadException2;
            }
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnectionOpenConnection.getInputStream(), "UTF-8"));
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                        sb.append('\n');
                    } catch (Throwable th) {
                        th = th;
                        Utils.closeSilently(bufferedReader);
                        throw th;
                    }
                }
                Utils.closeSilently(bufferedReader);
                i = iIntValue;
                if (e == null) {
                    break;
                }
                iIntValue = i;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader = null;
            }
        } while (i > 0);
        if (e != null) {
            if (callback != null) {
                callback.onError(e);
            }
            throw e;
        }
        String string = sb.toString();
        if (callback != null) {
            callback.onFinished(string);
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: download, reason: merged with bridge method [inline-methods] */
    public Void lambda$downloadVideoAsStream$2$DownloaderImpl(RequestVideoStreamDownload requestVideoStreamDownload) throws IOException {
        Format format = requestVideoStreamDownload.getFormat();
        YoutubeCallback<Void> callback = requestVideoStreamDownload.getCallback();
        download(requestVideoStreamDownload, format, requestVideoStreamDownload.getOutputStream());
        if (callback == null) {
            return null;
        }
        callback.onFinished(null);
        return null;
    }

    private void download(Request<?, ?> request, Format format, OutputStream outputStream) throws IOException {
        Map<String, String> headers = request.getHeaders();
        YoutubeCallback<?> callback = request.getCallback();
        int iIntValue = request.getMaxRetries() != null ? request.getMaxRetries().intValue() : this.config.getMaxRetries();
        Proxy proxy = request.getProxy();
        do {
            try {
                if (!format.isAdaptive() || format.contentLength() == null) {
                    downloadStraight(format, outputStream, headers, proxy, callback);
                } else {
                    downloadByPart(format, outputStream, headers, proxy, callback);
                }
                e = null;
            } catch (IOException e) {
                e = e;
            } catch (Throwable th) {
                Utils.closeSilently(outputStream);
                throw th;
            }
            Utils.closeSilently(outputStream);
            if (e == null) {
                break;
            }
        } while (iIntValue > 0);
        if (e != null) {
            if (callback != null) {
                callback.onError(e);
            }
            throw e;
        }
    }

    private void downloadByPart(Format format, OutputStream outputStream, Map<String, String> map, Proxy proxy, YoutubeCallback<?> youtubeCallback) throws IOException {
        String str = "&cver=" + format.clientVersion() + "&range=";
        long jLongValue = format.contentLength().longValue();
        byte[] bArr = new byte[4096];
        int i = 0;
        long jCopyAndCloseInput = 0;
        while (true) {
            long j = jCopyAndCloseInput;
            if (j >= jLongValue) {
                return;
            }
            long j2 = 2097152;
            if (j + 2097152 > jLongValue) {
                j2 = (int) (jLongValue - j);
            }
            i++;
            HttpURLConnection httpURLConnectionOpenConnection = openConnection(format.url() + str + j + "-" + ((j2 + j) - 1) + "&rn=" + i, map, proxy);
            int responseCode = httpURLConnectionOpenConnection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Failed to download: HTTP " + responseCode);
            }
            InputStream inputStream = httpURLConnectionOpenConnection.getInputStream();
            jCopyAndCloseInput = j + (youtubeCallback == null ? copyAndCloseInput(inputStream, outputStream, bArr) : copyAndCloseInput(inputStream, outputStream, bArr, j, jLongValue, youtubeCallback));
        }
    }

    private void downloadStraight(Format format, OutputStream outputStream, Map<String, String> map, Proxy proxy, YoutubeCallback<?> youtubeCallback) throws Throwable {
        HttpURLConnection httpURLConnectionOpenConnection = openConnection(format.url(), map, proxy);
        int responseCode = httpURLConnectionOpenConnection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to download: HTTP " + responseCode);
        }
        int contentLength = httpURLConnectionOpenConnection.getContentLength();
        InputStream inputStream = httpURLConnectionOpenConnection.getInputStream();
        byte[] bArr = new byte[4096];
        if (youtubeCallback == null) {
            copyAndCloseInput(inputStream, outputStream, bArr);
        } else {
            copyAndCloseInput(inputStream, outputStream, bArr, 0L, contentLength, youtubeCallback);
        }
    }

    private HttpURLConnection openConnection(String str, Map<String, String> map, Proxy proxy) throws IOException {
        URL url = new URL(str);
        HttpURLConnection httpURLConnection = proxy != null ? (HttpURLConnection) url.openConnection(proxy) : this.config.getProxy() != null ? (HttpURLConnection) url.openConnection(this.config.getProxy()) : (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> entry : this.config.getHeaders().entrySet()) {
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (map != null) {
            for (Map.Entry<String, String> entry2 : map.entrySet()) {
                httpURLConnection.setRequestProperty(entry2.getKey(), entry2.getValue());
            }
        }
        return httpURLConnection;
    }

    @Override // com.github.kiulian.downloader.downloader.Downloader
    public ResponseImpl<File> downloadVideoAsFile(final RequestVideoFileDownload requestVideoFileDownload) {
        if (requestVideoFileDownload.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestVideoFileDownload) { // from class: com.github.kiulian.downloader.downloader._$$Lambda$DownloaderImpl$8MDs5n3DI_8a0La9tlhK2ubuh5M
                public final DownloaderImpl f$0;
                public final RequestVideoFileDownload f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestVideoFileDownload;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$downloadVideoAsFile$1$DownloaderImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(lambda$downloadVideoAsFile$1$DownloaderImpl(requestVideoFileDownload));
        } catch (IOException e) {
            return ResponseImpl.error(e);
        }
    }

    @Override // com.github.kiulian.downloader.downloader.Downloader
    public ResponseImpl<Void> downloadVideoAsStream(final RequestVideoStreamDownload requestVideoStreamDownload) {
        if (requestVideoStreamDownload.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestVideoStreamDownload) { // from class: com.github.kiulian.downloader.downloader._$$Lambda$DownloaderImpl$ZXbti450VVBY_2gqiX_EeWczdwQ
                public final DownloaderImpl f$0;
                public final RequestVideoStreamDownload f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestVideoStreamDownload;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$downloadVideoAsStream$2$DownloaderImpl(this.f$1);
                }
            }));
        }
        try {
            lambda$downloadVideoAsStream$2$DownloaderImpl(requestVideoStreamDownload);
            return ResponseImpl.from(null);
        } catch (IOException e) {
            return ResponseImpl.error(e);
        }
    }

    @Override // com.github.kiulian.downloader.downloader.Downloader
    public ResponseImpl<String> downloadWebpage(final RequestWebpage requestWebpage) {
        if (requestWebpage.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestWebpage) { // from class: com.github.kiulian.downloader.downloader._$$Lambda$DownloaderImpl$_i28ymT_xdt0xfrthaAyonaM5UM
                public final DownloaderImpl f$0;
                public final RequestWebpage f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestWebpage;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$downloadWebpage$0$DownloaderImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(lambda$downloadWebpage$0$DownloaderImpl(requestWebpage));
        } catch (YoutubeException | IOException e) {
            return ResponseImpl.error(e);
        }
    }
}
