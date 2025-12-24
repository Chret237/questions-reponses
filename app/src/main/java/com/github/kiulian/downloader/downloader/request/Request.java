package com.github.kiulian.downloader.downloader.request;

import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.proxy.ProxyAuthenticator;
import com.github.kiulian.downloader.downloader.proxy.ProxyCredentialsImpl;
import com.github.kiulian.downloader.downloader.request.Request;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/request/Request.class */
public abstract class Request<T extends Request<T, S>, S> {
    private boolean async;
    private YoutubeCallback<S> callback;
    protected Map<String, String> headers;
    private Integer maxRetries;
    private Proxy proxy;

    public T async() {
        this.async = true;
        return this;
    }

    public T callback(YoutubeCallback<S> youtubeCallback) {
        this.callback = youtubeCallback;
        return this;
    }

    public YoutubeCallback<S> getCallback() {
        return this.callback;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Integer getMaxRetries() {
        return this.maxRetries;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public T header(String str, String str2) {
        if (this.headers == null) {
            this.headers = new HashMap();
        }
        this.headers.put(str, str2);
        return this;
    }

    public boolean isAsync() {
        return this.async;
    }

    public T maxRetries(int i) {
        this.maxRetries = Integer.valueOf(i);
        return this;
    }

    public T proxy(String str, int i) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        return this;
    }

    public T proxy(String str, int i, String str2, String str3) {
        if (ProxyAuthenticator.getDefault() == null) {
            ProxyAuthenticator.setDefault(new ProxyAuthenticator(new ProxyCredentialsImpl()));
        }
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        ProxyAuthenticator.addAuthentication(str, i, str2, str3);
        return this;
    }
}
