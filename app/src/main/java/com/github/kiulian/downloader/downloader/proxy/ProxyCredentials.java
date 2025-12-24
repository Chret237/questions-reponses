package com.github.kiulian.downloader.downloader.proxy;

import java.net.PasswordAuthentication;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/proxy/ProxyCredentials.class */
public interface ProxyCredentials {
    void addAuthentication(String str, int i, String str2, String str3);

    PasswordAuthentication getAuthentication(String str, int i);
}
