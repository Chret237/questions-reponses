package okhttp3;

import java.nio.charset.Charset;
import okhttp3.internal.Util;

/* loaded from: classes-dex2jar.jar:okhttp3/Challenge.class */
public final class Challenge {
    private final Charset charset;
    private final String realm;
    private final String scheme;

    public Challenge(String str, String str2) {
        this(str, str2, Util.ISO_8859_1);
    }

    private Challenge(String str, String str2, Charset charset) {
        if (str == null) {
            throw new NullPointerException("scheme == null");
        }
        if (str2 == null) {
            throw new NullPointerException("realm == null");
        }
        if (charset == null) {
            throw new NullPointerException("charset == null");
        }
        this.scheme = str;
        this.realm = str2;
        this.charset = charset;
    }

    public Charset charset() {
        return this.charset;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(@javax.annotation.Nullable java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            boolean r0 = r0 instanceof okhttp3.Challenge
            if (r0 == 0) goto L3b
            r0 = r4
            okhttp3.Challenge r0 = (okhttp3.Challenge) r0
            r4 = r0
            r0 = r4
            java.lang.String r0 = r0.scheme
            r1 = r3
            java.lang.String r1 = r1.scheme
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = r4
            java.lang.String r0 = r0.realm
            r1 = r3
            java.lang.String r1 = r1.realm
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = r4
            java.nio.charset.Charset r0 = r0.charset
            r1 = r3
            java.nio.charset.Charset r1 = r1.charset
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = 1
            r5 = r0
            goto L3d
        L3b:
            r0 = 0
            r5 = r0
        L3d:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Challenge.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return ((((899 + this.realm.hashCode()) * 31) + this.scheme.hashCode()) * 31) + this.charset.hashCode();
    }

    public String realm() {
        return this.realm;
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.scheme + " realm=\"" + this.realm + "\" charset=\"" + this.charset + "\"";
    }

    public Challenge withCharset(Charset charset) {
        return new Challenge(this.scheme, this.realm, charset);
    }
}
