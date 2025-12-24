package com.github.kiulian.downloader;

import com.github.kiulian.downloader.downloader.proxy.ProxyAuthenticator;
import com.github.kiulian.downloader.downloader.proxy.ProxyCredentials;
import com.github.kiulian.downloader.downloader.proxy.ProxyCredentialsImpl;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/Config.class */
public class Config {
    private static final String DEFAULT_ACCEPT_LANG = "en-US,en;";
    private static final int DEFAULT_RETRY_ON_FAILURE = 0;
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
    private static final ThreadFactory threadFactory = new ThreadFactory() { // from class: com.github.kiulian.downloader.Config.1
        private static final String NAME_PREFIX = "yt-downloader-";
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            SecurityManager securityManager = System.getSecurityManager();
            Thread thread = new Thread(securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup(), runnable, NAME_PREFIX + this.threadNumber.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    };
    private ExecutorService executorService;
    private Map<String, String> headers;
    private int maxRetries;
    private Proxy proxy;

    /* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/Config$Builder.class */
    public static class Builder {
        private ExecutorService executorService;
        private Map<String, String> headers = new HashMap();
        private int maxRetries = 0;
        private Proxy proxy;

        public Config build() {
            if (this.executorService == null) {
                this.executorService = Executors.newCachedThreadPool(Config.threadFactory);
            }
            return new Config(this);
        }

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public Builder header(String str, String str2) {
            this.headers.put(str, str2);
            return this;
        }

        public Builder maxRetries(int i) {
            this.maxRetries = i;
            return this;
        }

        public Builder proxy(String str, int i) {
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
            return this;
        }

        public Builder proxy(String str, int i, String str2, String str3) {
            if (ProxyAuthenticator.getDefault() == null) {
                ProxyAuthenticator.setDefault(new ProxyAuthenticator(new ProxyCredentialsImpl()));
            }
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
            ProxyAuthenticator.addAuthentication(str, i, str2, str3);
            return this;
        }

        public Builder proxyCredentialsManager(ProxyCredentials proxyCredentials) {
            ProxyAuthenticator.setDefault(new ProxyAuthenticator(proxyCredentials));
            return this;
        }
    }

    private Config() {
        this.headers = new HashMap();
        this.maxRetries = 0;
        this.executorService = Executors.newCachedThreadPool(threadFactory);
        setHeader("User-Agent", DEFAULT_USER_AGENT);
        setHeader("Accept-language", DEFAULT_ACCEPT_LANG);
    }

    private Config(Builder builder) {
        this.headers = builder.headers;
        this.maxRetries = builder.maxRetries;
        this.executorService = builder.executorService;
        this.proxy = builder.proxy;
    }

    static Config buildDefault() {
        return new Config();
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setHeader(String str, String str2) {
        this.headers.put(str, str2);
    }

    public void setMaxRetries(int i) {
        this.maxRetries = i;
    }

    public void setProxy(String str, int i) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
    }

    public void setProxy(String str, int i, String str2, String str3) {
        if (ProxyAuthenticator.getDefault() == null) {
            ProxyAuthenticator.setDefault(new ProxyAuthenticator(new ProxyCredentialsImpl()));
        }
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str, i));
        ProxyAuthenticator.addAuthentication(str, i, str2, str3);
    }

    public void setProxyAuthenticator(ProxyCredentials proxyCredentials) {
        ProxyAuthenticator.setDefault(new ProxyAuthenticator(proxyCredentials));
    }
}
