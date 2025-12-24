package com.github.kiulian.downloader.downloader.proxy;

import java.net.PasswordAuthentication;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/proxy/ProxyCredentialsImpl.class */
public class ProxyCredentialsImpl implements ProxyCredentials {
    private final Map<String, PasswordAuthentication> credentials = new ConcurrentHashMap();

    @Override // com.github.kiulian.downloader.downloader.proxy.ProxyCredentials
    public void addAuthentication(String str, int i, String str2, String str3) {
        this.credentials.put(str + ":" + i, new PasswordAuthentication(str2, str3.toCharArray()));
    }

    @Override // com.github.kiulian.downloader.downloader.proxy.ProxyCredentials
    public PasswordAuthentication getAuthentication(String str, int i) {
        return this.credentials.get(str + ":" + i);
    }
}
