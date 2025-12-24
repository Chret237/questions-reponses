package com.github.kiulian.downloader.downloader.request;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/RequestWebpage.class */
public class RequestWebpage extends RequestRaw<RequestWebpage> {
    private final String body;
    private final String method;
    protected final String url;

    public RequestWebpage(String str) {
        this(str, "GET", null);
    }

    public RequestWebpage(String str, String str2, String str3) {
        this.url = str;
        this.method = str2;
        this.body = str3;
    }

    public String getBody() {
        return this.body;
    }

    @Override // com.github.kiulian.downloader.downloader.request.RequestRaw
    public String getDownloadUrl() {
        return this.url;
    }

    public String getMethod() {
        return this.method;
    }
}
