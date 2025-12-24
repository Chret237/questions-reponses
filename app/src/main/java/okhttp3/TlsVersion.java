package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes-dex2jar.jar:okhttp3/TlsVersion.class */
public enum TlsVersion {
    TLS_1_3("TLSv1.3"),
    TLS_1_2("TLSv1.2"),
    TLS_1_1("TLSv1.1"),
    TLS_1_0("TLSv1"),
    SSL_3_0("SSLv3");

    final String javaName;

    TlsVersion(String str) {
        this.javaName = str;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static okhttp3.TlsVersion forJavaName(java.lang.String r4) {
        /*
            r0 = r4
            int r0 = r0.hashCode()
            r5 = r0
            r0 = r5
            r1 = 79201641(0x4b88569, float:4.338071E-36)
            if (r0 == r1) goto L67
            r0 = r5
            r1 = 79923350(0x4c38896, float:4.5969714E-36)
            if (r0 == r1) goto L59
            r0 = r5
            switch(r0) {
                case -503070503: goto L4b;
                case -503070502: goto L3d;
                case -503070501: goto L2f;
                default: goto L2c;
            }
        L2c:
            goto L75
        L2f:
            r0 = r4
            java.lang.String r1 = "TLSv1.3"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L75
            r0 = 0
            r5 = r0
            goto L77
        L3d:
            r0 = r4
            java.lang.String r1 = "TLSv1.2"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L75
            r0 = 1
            r5 = r0
            goto L77
        L4b:
            r0 = r4
            java.lang.String r1 = "TLSv1.1"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L75
            r0 = 2
            r5 = r0
            goto L77
        L59:
            r0 = r4
            java.lang.String r1 = "TLSv1"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L75
            r0 = 3
            r5 = r0
            goto L77
        L67:
            r0 = r4
            java.lang.String r1 = "SSLv3"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L75
            r0 = 4
            r5 = r0
            goto L77
        L75:
            r0 = -1
            r5 = r0
        L77:
            r0 = r5
            if (r0 == 0) goto Lc0
            r0 = r5
            r1 = 1
            if (r0 == r1) goto Lbc
            r0 = r5
            r1 = 2
            if (r0 == r1) goto Lb8
            r0 = r5
            r1 = 3
            if (r0 == r1) goto Lb4
            r0 = r5
            r1 = 4
            if (r0 != r1) goto L93
            okhttp3.TlsVersion r0 = okhttp3.TlsVersion.SSL_3_0
            return r0
        L93:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = r6
            java.lang.String r1 = "Unexpected TLS version: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r6
            r1 = r4
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            r2 = r6
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        Lb4:
            okhttp3.TlsVersion r0 = okhttp3.TlsVersion.TLS_1_0
            return r0
        Lb8:
            okhttp3.TlsVersion r0 = okhttp3.TlsVersion.TLS_1_1
            return r0
        Lbc:
            okhttp3.TlsVersion r0 = okhttp3.TlsVersion.TLS_1_2
            return r0
        Lc0:
            okhttp3.TlsVersion r0 = okhttp3.TlsVersion.TLS_1_3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.TlsVersion.forJavaName(java.lang.String):okhttp3.TlsVersion");
    }

    static List<TlsVersion> forJavaNames(String... strArr) {
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(forJavaName(str));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public String javaName() {
        return this.javaName;
    }
}
