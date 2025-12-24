package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;

/* loaded from: classes-dex2jar.jar:okhttp3/Address.class */
public final class Address {

    @Nullable
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final Dns dns;

    @Nullable
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;

    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;

    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final HttpUrl url;

    public Address(String str, int i, Dns dns, SocketFactory socketFactory, @Nullable SSLSocketFactory sSLSocketFactory, @Nullable HostnameVerifier hostnameVerifier, @Nullable CertificatePinner certificatePinner, Authenticator authenticator, @Nullable Proxy proxy, List<Protocol> list, List<ConnectionSpec> list2, ProxySelector proxySelector) {
        this.url = new HttpUrl.Builder().scheme(sSLSocketFactory != null ? "https" : "http").host(str).port(i).build();
        if (dns == null) {
            throw new NullPointerException("dns == null");
        }
        this.dns = dns;
        if (socketFactory == null) {
            throw new NullPointerException("socketFactory == null");
        }
        this.socketFactory = socketFactory;
        if (authenticator == null) {
            throw new NullPointerException("proxyAuthenticator == null");
        }
        this.proxyAuthenticator = authenticator;
        if (list == null) {
            throw new NullPointerException("protocols == null");
        }
        this.protocols = Util.immutableList(list);
        if (list2 == null) {
            throw new NullPointerException("connectionSpecs == null");
        }
        this.connectionSpecs = Util.immutableList(list2);
        if (proxySelector == null) {
            throw new NullPointerException("proxySelector == null");
        }
        this.proxySelector = proxySelector;
        this.proxy = proxy;
        this.sslSocketFactory = sSLSocketFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.certificatePinner = certificatePinner;
    }

    @Nullable
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public Dns dns() {
        return this.dns;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(@javax.annotation.Nullable java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            boolean r0 = r0 instanceof okhttp3.Address
            if (r0 == 0) goto L29
            r0 = r3
            okhttp3.HttpUrl r0 = r0.url
            r6 = r0
            r0 = r4
            okhttp3.Address r0 = (okhttp3.Address) r0
            r4 = r0
            r0 = r6
            r1 = r4
            okhttp3.HttpUrl r1 = r1.url
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L29
            r0 = r3
            r1 = r4
            boolean r0 = r0.equalsNonHost(r1)
            if (r0 == 0) goto L29
            r0 = 1
            r5 = r0
            goto L2b
        L29:
            r0 = 0
            r5 = r0
        L2b:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Address.equals(java.lang.Object):boolean");
    }

    boolean equalsNonHost(Address address) {
        return this.dns.equals(address.dns) && this.proxyAuthenticator.equals(address.proxyAuthenticator) && this.protocols.equals(address.protocols) && this.connectionSpecs.equals(address.connectionSpecs) && this.proxySelector.equals(address.proxySelector) && Util.equal(this.proxy, address.proxy) && Util.equal(this.sslSocketFactory, address.sslSocketFactory) && Util.equal(this.hostnameVerifier, address.hostnameVerifier) && Util.equal(this.certificatePinner, address.certificatePinner) && url().port() == address.url().port();
    }

    public int hashCode() {
        int iHashCode = this.url.hashCode();
        int iHashCode2 = this.dns.hashCode();
        int iHashCode3 = this.proxyAuthenticator.hashCode();
        int iHashCode4 = this.protocols.hashCode();
        int iHashCode5 = this.connectionSpecs.hashCode();
        int iHashCode6 = this.proxySelector.hashCode();
        Proxy proxy = this.proxy;
        int iHashCode7 = 0;
        int iHashCode8 = proxy != null ? proxy.hashCode() : 0;
        SSLSocketFactory sSLSocketFactory = this.sslSocketFactory;
        int iHashCode9 = sSLSocketFactory != null ? sSLSocketFactory.hashCode() : 0;
        HostnameVerifier hostnameVerifier = this.hostnameVerifier;
        int iHashCode10 = hostnameVerifier != null ? hostnameVerifier.hashCode() : 0;
        CertificatePinner certificatePinner = this.certificatePinner;
        if (certificatePinner != null) {
            iHashCode7 = certificatePinner.hashCode();
        }
        return ((((((((((((((((((527 + iHashCode) * 31) + iHashCode2) * 31) + iHashCode3) * 31) + iHashCode4) * 31) + iHashCode5) * 31) + iHashCode6) * 31) + iHashCode8) * 31) + iHashCode9) * 31) + iHashCode10) * 31) + iHashCode7;
    }

    @Nullable
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    public List<Protocol> protocols() {
        return this.protocols;
    }

    @Nullable
    public Proxy proxy() {
        return this.proxy;
    }

    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public ProxySelector proxySelector() {
        return this.proxySelector;
    }

    public SocketFactory socketFactory() {
        return this.socketFactory;
    }

    @Nullable
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Address{");
        sb.append(this.url.host());
        sb.append(":");
        sb.append(this.url.port());
        if (this.proxy != null) {
            sb.append(", proxy=");
            sb.append(this.proxy);
        } else {
            sb.append(", proxySelector=");
            sb.append(this.proxySelector);
        }
        sb.append("}");
        return sb.toString();
    }

    public HttpUrl url() {
        return this.url;
    }
}
