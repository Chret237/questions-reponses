package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

/* loaded from: classes-dex2jar.jar:okhttp3/CertificatePinner.class */
public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new Builder().build();

    @Nullable
    private final CertificateChainCleaner certificateChainCleaner;
    private final Set<Pin> pins;

    /* loaded from: classes-dex2jar.jar:okhttp3/CertificatePinner$Builder.class */
    public static final class Builder {
        private final List<Pin> pins = new ArrayList();

        public Builder add(String str, String... strArr) {
            if (str == null) {
                throw new NullPointerException("pattern == null");
            }
            for (String str2 : strArr) {
                this.pins.add(new Pin(str, str2));
            }
            return this;
        }

        public CertificatePinner build() {
            return new CertificatePinner(new LinkedHashSet(this.pins), null);
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/CertificatePinner$Pin.class */
    static final class Pin {
        private static final String WILDCARD = "*.";
        final String canonicalHostname;
        final ByteString hash;
        final String hashAlgorithm;
        final String pattern;

        Pin(String str, String str2) {
            String strHost;
            this.pattern = str;
            if (str.startsWith(WILDCARD)) {
                strHost = HttpUrl.parse("http://" + str.substring(2)).host();
            } else {
                strHost = HttpUrl.parse("http://" + str).host();
            }
            this.canonicalHostname = strHost;
            if (str2.startsWith("sha1/")) {
                this.hashAlgorithm = "sha1/";
                this.hash = ByteString.decodeBase64(str2.substring(5));
            } else {
                if (!str2.startsWith("sha256/")) {
                    throw new IllegalArgumentException("pins must start with 'sha256/' or 'sha1/': " + str2);
                }
                this.hashAlgorithm = "sha256/";
                this.hash = ByteString.decodeBase64(str2.substring(7));
            }
            if (this.hash != null) {
                return;
            }
            throw new IllegalArgumentException("pins must be base64: " + str2);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x003d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean equals(java.lang.Object r4) {
            /*
                r3 = this;
                r0 = r4
                boolean r0 = r0 instanceof okhttp3.CertificatePinner.Pin
                if (r0 == 0) goto L3d
                r0 = r3
                java.lang.String r0 = r0.pattern
                r6 = r0
                r0 = r4
                okhttp3.CertificatePinner$Pin r0 = (okhttp3.CertificatePinner.Pin) r0
                r4 = r0
                r0 = r6
                r1 = r4
                java.lang.String r1 = r1.pattern
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L3d
                r0 = r3
                java.lang.String r0 = r0.hashAlgorithm
                r1 = r4
                java.lang.String r1 = r1.hashAlgorithm
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L3d
                r0 = r3
                okio.ByteString r0 = r0.hash
                r1 = r4
                okio.ByteString r1 = r1.hash
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L3d
                r0 = 1
                r5 = r0
                goto L3f
            L3d:
                r0 = 0
                r5 = r0
            L3f:
                r0 = r5
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.CertificatePinner.Pin.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            return ((((527 + this.pattern.hashCode()) * 31) + this.hashAlgorithm.hashCode()) * 31) + this.hash.hashCode();
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0046  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        boolean matches(java.lang.String r8) {
            /*
                r7 = this;
                r0 = r7
                java.lang.String r0 = r0.pattern
                java.lang.String r1 = "*."
                boolean r0 = r0.startsWith(r1)
                if (r0 == 0) goto L4c
                r0 = r8
                r1 = 46
                int r0 = r0.indexOf(r1)
                r10 = r0
                r0 = r8
                int r0 = r0.length()
                r9 = r0
                r0 = 1
                r11 = r0
                r0 = r9
                r1 = r10
                int r0 = r0 - r1
                r1 = 1
                int r0 = r0 - r1
                r1 = r7
                java.lang.String r1 = r1.canonicalHostname
                int r1 = r1.length()
                if (r0 != r1) goto L46
                r0 = r7
                java.lang.String r0 = r0.canonicalHostname
                r12 = r0
                r0 = r8
                r1 = 0
                r2 = r10
                r3 = 1
                int r2 = r2 + r3
                r3 = r12
                r4 = 0
                r5 = r12
                int r5 = r5.length()
                boolean r0 = r0.regionMatches(r1, r2, r3, r4, r5)
                if (r0 == 0) goto L46
                goto L49
            L46:
                r0 = 0
                r11 = r0
            L49:
                r0 = r11
                return r0
            L4c:
                r0 = r8
                r1 = r7
                java.lang.String r1 = r1.canonicalHostname
                boolean r0 = r0.equals(r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.CertificatePinner.Pin.matches(java.lang.String):boolean");
        }

        public String toString() {
            return this.hashAlgorithm + this.hash.base64();
        }
    }

    CertificatePinner(Set<Pin> set, @Nullable CertificateChainCleaner certificateChainCleaner) {
        this.pins = set;
        this.certificateChainCleaner = certificateChainCleaner;
    }

    public static String pin(Certificate certificate) {
        if (!(certificate instanceof X509Certificate)) {
            throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
        }
        return "sha256/" + sha256((X509Certificate) certificate).base64();
    }

    static ByteString sha1(X509Certificate x509Certificate) {
        return ByteString.m19of(x509Certificate.getPublicKey().getEncoded()).sha1();
    }

    static ByteString sha256(X509Certificate x509Certificate) {
        return ByteString.m19of(x509Certificate.getPublicKey().getEncoded()).sha256();
    }

    public void check(String str, List<Certificate> list) throws SSLPeerUnverifiedException {
        List<Pin> listFindMatchingPins = findMatchingPins(str);
        if (listFindMatchingPins.isEmpty()) {
            return;
        }
        CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
        List<Certificate> listClean = list;
        if (certificateChainCleaner != null) {
            listClean = certificateChainCleaner.clean(list, str);
        }
        int size = listClean.size();
        for (int i = 0; i < size; i++) {
            X509Certificate x509Certificate = (X509Certificate) listClean.get(i);
            int size2 = listFindMatchingPins.size();
            ByteString byteString = null;
            ByteString byteString2 = null;
            for (int i2 = 0; i2 < size2; i2++) {
                Pin pin = listFindMatchingPins.get(i2);
                if (pin.hashAlgorithm.equals("sha256/")) {
                    ByteString byteStringSha256 = byteString;
                    if (byteString == null) {
                        byteStringSha256 = sha256(x509Certificate);
                    }
                    byteString = byteStringSha256;
                    if (pin.hash.equals(byteStringSha256)) {
                        return;
                    }
                } else {
                    if (!pin.hashAlgorithm.equals("sha1/")) {
                        throw new AssertionError("unsupported hashAlgorithm: " + pin.hashAlgorithm);
                    }
                    ByteString byteStringSha1 = byteString2;
                    if (byteString2 == null) {
                        byteStringSha1 = sha1(x509Certificate);
                    }
                    byteString2 = byteStringSha1;
                    if (pin.hash.equals(byteStringSha1)) {
                        return;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Certificate pinning failure!");
        sb.append("\n  Peer certificate chain:");
        int size3 = listClean.size();
        for (int i3 = 0; i3 < size3; i3++) {
            X509Certificate x509Certificate2 = (X509Certificate) listClean.get(i3);
            sb.append("\n    ");
            sb.append(pin(x509Certificate2));
            sb.append(": ");
            sb.append(x509Certificate2.getSubjectDN().getName());
        }
        sb.append("\n  Pinned certificates for ");
        sb.append(str);
        sb.append(":");
        int size4 = listFindMatchingPins.size();
        for (int i4 = 0; i4 < size4; i4++) {
            Pin pin2 = listFindMatchingPins.get(i4);
            sb.append("\n    ");
            sb.append(pin2);
        }
        throw new SSLPeerUnverifiedException(sb.toString());
    }

    public void check(String str, Certificate... certificateArr) throws SSLPeerUnverifiedException {
        check(str, Arrays.asList(certificateArr));
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0038  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(@javax.annotation.Nullable java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 1
            r5 = r0
            r0 = r4
            r1 = r3
            if (r0 != r1) goto L9
            r0 = 1
            return r0
        L9:
            r0 = r4
            boolean r0 = r0 instanceof okhttp3.CertificatePinner
            if (r0 == 0) goto L38
            r0 = r3
            okhttp3.internal.tls.CertificateChainCleaner r0 = r0.certificateChainCleaner
            r6 = r0
            r0 = r4
            okhttp3.CertificatePinner r0 = (okhttp3.CertificatePinner) r0
            r4 = r0
            r0 = r6
            r1 = r4
            okhttp3.internal.tls.CertificateChainCleaner r1 = r1.certificateChainCleaner
            boolean r0 = okhttp3.internal.Util.equal(r0, r1)
            if (r0 == 0) goto L38
            r0 = r3
            java.util.Set<okhttp3.CertificatePinner$Pin> r0 = r0.pins
            r1 = r4
            java.util.Set<okhttp3.CertificatePinner$Pin> r1 = r1.pins
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L38
            goto L3a
        L38:
            r0 = 0
            r5 = r0
        L3a:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.CertificatePinner.equals(java.lang.Object):boolean");
    }

    List<Pin> findMatchingPins(String str) {
        List<Pin> listEmptyList = Collections.emptyList();
        for (Pin pin : this.pins) {
            if (pin.matches(str)) {
                List<Pin> arrayList = listEmptyList;
                if (listEmptyList.isEmpty()) {
                    arrayList = new ArrayList();
                }
                arrayList.add(pin);
                listEmptyList = arrayList;
            }
        }
        return listEmptyList;
    }

    public int hashCode() {
        CertificateChainCleaner certificateChainCleaner = this.certificateChainCleaner;
        return ((certificateChainCleaner != null ? certificateChainCleaner.hashCode() : 0) * 31) + this.pins.hashCode();
    }

    CertificatePinner withCertificateChainCleaner(@Nullable CertificateChainCleaner certificateChainCleaner) {
        return Util.equal(this.certificateChainCleaner, certificateChainCleaner) ? this : new CertificatePinner(this.pins, certificateChainCleaner);
    }
}
