package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

/* loaded from: classes-dex2jar.jar:okhttp3/Handshake.class */
public final class Handshake {
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;

    private Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = list;
        this.localCertificates = list2;
    }

    public static Handshake get(SSLSession sSLSession) throws SSLPeerUnverifiedException {
        Certificate[] peerCertificates;
        String cipherSuite = sSLSession.getCipherSuite();
        if (cipherSuite == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        CipherSuite cipherSuiteForJavaName = CipherSuite.forJavaName(cipherSuite);
        String protocol = sSLSession.getProtocol();
        if (protocol == null) {
            throw new IllegalStateException("tlsVersion == null");
        }
        TlsVersion tlsVersionForJavaName = TlsVersion.forJavaName(protocol);
        try {
            peerCertificates = sSLSession.getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            peerCertificates = null;
        }
        List listImmutableList = peerCertificates != null ? Util.immutableList(peerCertificates) : Collections.emptyList();
        Certificate[] localCertificates = sSLSession.getLocalCertificates();
        return new Handshake(tlsVersionForJavaName, cipherSuiteForJavaName, listImmutableList, localCertificates != null ? Util.immutableList(localCertificates) : Collections.emptyList());
    }

    public static Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        if (tlsVersion == null) {
            throw new NullPointerException("tlsVersion == null");
        }
        if (cipherSuite != null) {
            return new Handshake(tlsVersion, cipherSuite, Util.immutableList(list), Util.immutableList(list2));
        }
        throw new NullPointerException("cipherSuite == null");
    }

    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Handshake)) {
            return false;
        }
        Handshake handshake = (Handshake) obj;
        boolean z = false;
        if (this.tlsVersion.equals(handshake.tlsVersion)) {
            z = false;
            if (this.cipherSuite.equals(handshake.cipherSuite)) {
                z = false;
                if (this.peerCertificates.equals(handshake.peerCertificates)) {
                    z = false;
                    if (this.localCertificates.equals(handshake.localCertificates)) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    public int hashCode() {
        return ((((((527 + this.tlsVersion.hashCode()) * 31) + this.cipherSuite.hashCode()) * 31) + this.peerCertificates.hashCode()) * 31) + this.localCertificates.hashCode();
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    @Nullable
    public Principal localPrincipal() {
        return !this.localCertificates.isEmpty() ? ((X509Certificate) this.localCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    @Nullable
    public Principal peerPrincipal() {
        return !this.peerCertificates.isEmpty() ? ((X509Certificate) this.peerCertificates.get(0)).getSubjectX500Principal() : null;
    }

    public TlsVersion tlsVersion() {
        return this.tlsVersion;
    }
}
