package com.github.kiulian.downloader.downloader.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/proxy/ProxyAuthenticator.class */
public class ProxyAuthenticator extends Authenticator {
    private static volatile ProxyAuthenticator instance;
    private final ProxyCredentials proxyCredentials;

    public ProxyAuthenticator(ProxyCredentials proxyCredentials) {
        this.proxyCredentials = proxyCredentials;
    }

    public static void addAuthentication(String str, int i, String str2, String str3) {
        if (instance == null) {
            throw new NullPointerException("ProxyAuthenticator instance is null. Use ProxyAuthenticator.setDefault() to init");
        }
        instance.proxyCredentials.addAuthentication(str, i, str2, str3);
    }

    public static ProxyAuthenticator getDefault() {
        ProxyAuthenticator proxyAuthenticator;
        synchronized (ProxyAuthenticator.class) {
            try {
                proxyAuthenticator = instance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return proxyAuthenticator;
    }

    public static void setDefault(ProxyAuthenticator proxyAuthenticator) {
        synchronized (ProxyAuthenticator.class) {
            try {
                instance = proxyAuthenticator;
                Authenticator.setDefault(instance);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // java.net.Authenticator
    public PasswordAuthentication getPasswordAuthentication() {
        return this.proxyCredentials.getAuthentication(getRequestingHost(), getRequestingPort());
    }
}
