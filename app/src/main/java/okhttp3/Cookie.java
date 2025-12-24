package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

/* loaded from: classes-dex2jar.jar:okhttp3/Cookie.class */
public final class Cookie {
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");

    /* loaded from: classes-dex2jar.jar:okhttp3/Cookie$Builder.class */
    public static final class Builder {
        String domain;
        boolean hostOnly;
        boolean httpOnly;
        String name;
        boolean persistent;
        boolean secure;
        String value;
        long expiresAt = HttpDate.MAX_DATE;
        String path = "/";

        private Builder domain(String str, boolean z) {
            if (str == null) {
                throw new NullPointerException("domain == null");
            }
            String strCanonicalizeHost = Util.canonicalizeHost(str);
            if (strCanonicalizeHost != null) {
                this.domain = strCanonicalizeHost;
                this.hostOnly = z;
                return this;
            }
            throw new IllegalArgumentException("unexpected domain: " + str);
        }

        public Cookie build() {
            return new Cookie(this);
        }

        public Builder domain(String str) {
            return domain(str, false);
        }

        public Builder expiresAt(long j) {
            long j2 = j;
            if (j <= 0) {
                j2 = Long.MIN_VALUE;
            }
            long j3 = j2;
            if (j2 > HttpDate.MAX_DATE) {
                j3 = 253402300799999L;
            }
            this.expiresAt = j3;
            this.persistent = true;
            return this;
        }

        public Builder hostOnlyDomain(String str) {
            return domain(str, true);
        }

        public Builder httpOnly() {
            this.httpOnly = true;
            return this;
        }

        public Builder name(String str) {
            if (str == null) {
                throw new NullPointerException("name == null");
            }
            if (!str.trim().equals(str)) {
                throw new IllegalArgumentException("name is not trimmed");
            }
            this.name = str;
            return this;
        }

        public Builder path(String str) {
            if (!str.startsWith("/")) {
                throw new IllegalArgumentException("path must start with '/'");
            }
            this.path = str;
            return this;
        }

        public Builder secure() {
            this.secure = true;
            return this;
        }

        public Builder value(String str) {
            if (str == null) {
                throw new NullPointerException("value == null");
            }
            if (!str.trim().equals(str)) {
                throw new IllegalArgumentException("value is not trimmed");
            }
            this.value = str;
            return this;
        }
    }

    private Cookie(String str, String str2, long j, String str3, String str4, boolean z, boolean z2, boolean z3, boolean z4) {
        this.name = str;
        this.value = str2;
        this.expiresAt = j;
        this.domain = str3;
        this.path = str4;
        this.secure = z;
        this.httpOnly = z2;
        this.hostOnly = z3;
        this.persistent = z4;
    }

    Cookie(Builder builder) {
        if (builder.name == null) {
            throw new NullPointerException("builder.name == null");
        }
        if (builder.value == null) {
            throw new NullPointerException("builder.value == null");
        }
        if (builder.domain == null) {
            throw new NullPointerException("builder.domain == null");
        }
        this.name = builder.name;
        this.value = builder.value;
        this.expiresAt = builder.expiresAt;
        this.domain = builder.domain;
        this.path = builder.path;
        this.secure = builder.secure;
        this.httpOnly = builder.httpOnly;
        this.persistent = builder.persistent;
        this.hostOnly = builder.hostOnly;
    }

    private static int dateCharacterOffset(String str, int i, int i2, boolean z) {
        while (i < i2) {
            char cCharAt = str.charAt(i);
            if (((cCharAt < ' ' && cCharAt != '\t') || cCharAt >= 127 || (cCharAt >= '0' && cCharAt <= '9') || ((cCharAt >= 'a' && cCharAt <= 'z') || ((cCharAt >= 'A' && cCharAt <= 'Z') || cCharAt == ':'))) == (!z)) {
                return i;
            }
            i++;
        }
        return i2;
    }

    private static boolean domainMatch(String str, String str2) {
        if (str.equals(str2)) {
            return true;
        }
        return str.endsWith(str2) && str.charAt((str.length() - str2.length()) - 1) == '.' && !Util.verifyAsIpAddress(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x023d  */
    @javax.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static okhttp3.Cookie parse(long r13, okhttp3.HttpUrl r15, java.lang.String r16) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 758
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cookie.parse(long, okhttp3.HttpUrl, java.lang.String):okhttp3.Cookie");
    }

    @Nullable
    public static Cookie parse(HttpUrl httpUrl, String str) {
        return parse(System.currentTimeMillis(), httpUrl, str);
    }

    public static List<Cookie> parseAll(HttpUrl httpUrl, Headers headers) {
        List<String> listValues = headers.values("Set-Cookie");
        int size = listValues.size();
        ArrayList arrayList = null;
        for (int i = 0; i < size; i++) {
            Cookie cookie = parse(httpUrl, listValues.get(i));
            if (cookie != null) {
                ArrayList arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList();
                }
                arrayList2.add(cookie);
                arrayList = arrayList2;
            }
        }
        return arrayList != null ? Collections.unmodifiableList(arrayList) : Collections.emptyList();
    }

    private static String parseDomain(String str) {
        if (str.endsWith(".")) {
            throw new IllegalArgumentException();
        }
        String strSubstring = str;
        if (str.startsWith(".")) {
            strSubstring = str.substring(1);
        }
        String strCanonicalizeHost = Util.canonicalizeHost(strSubstring);
        if (strCanonicalizeHost != null) {
            return strCanonicalizeHost;
        }
        throw new IllegalArgumentException();
    }

    private static long parseExpires(String str, int i, int i2) throws NumberFormatException {
        int i3;
        int i4;
        int i5;
        int iIndexOf;
        int i6;
        int i7;
        int iDateCharacterOffset = dateCharacterOffset(str, i, i2, false);
        Matcher matcher = TIME_PATTERN.matcher(str);
        int i8 = -1;
        int i9 = -1;
        int i10 = -1;
        int i11 = -1;
        int i12 = -1;
        int i13 = -1;
        while (iDateCharacterOffset < i2) {
            int iDateCharacterOffset2 = dateCharacterOffset(str, iDateCharacterOffset + 1, i2, true);
            matcher.region(iDateCharacterOffset, iDateCharacterOffset2);
            if (i9 == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
                i4 = Integer.parseInt(matcher.group(1));
                i6 = Integer.parseInt(matcher.group(2));
                i7 = Integer.parseInt(matcher.group(3));
                i3 = i8;
                i5 = i10;
                iIndexOf = i11;
            } else if (i10 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
                i5 = Integer.parseInt(matcher.group(1));
                i3 = i8;
                i4 = i9;
                iIndexOf = i11;
                i6 = i12;
                i7 = i13;
            } else if (i11 == -1 && matcher.usePattern(MONTH_PATTERN).matches()) {
                iIndexOf = MONTH_PATTERN.pattern().indexOf(matcher.group(1).toLowerCase(Locale.US)) / 4;
                i3 = i8;
                i4 = i9;
                i5 = i10;
                i6 = i12;
                i7 = i13;
            } else {
                i3 = i8;
                i4 = i9;
                i5 = i10;
                iIndexOf = i11;
                i6 = i12;
                i7 = i13;
                if (i8 == -1) {
                    i3 = i8;
                    i4 = i9;
                    i5 = i10;
                    iIndexOf = i11;
                    i6 = i12;
                    i7 = i13;
                    if (matcher.usePattern(YEAR_PATTERN).matches()) {
                        i3 = Integer.parseInt(matcher.group(1));
                        i7 = i13;
                        i6 = i12;
                        iIndexOf = i11;
                        i5 = i10;
                        i4 = i9;
                    }
                }
            }
            i8 = i3;
            i9 = i4;
            i10 = i5;
            i11 = iIndexOf;
            i12 = i6;
            i13 = i7;
            iDateCharacterOffset = dateCharacterOffset(str, iDateCharacterOffset2 + 1, i2, false);
        }
        int i14 = i8;
        if (i8 >= 70) {
            i14 = i8;
            if (i8 <= 99) {
                i14 = i8 + 1900;
            }
        }
        int i15 = i14;
        if (i14 >= 0) {
            i15 = i14;
            if (i14 <= 69) {
                i15 = i14 + 2000;
            }
        }
        if (i15 < 1601) {
            throw new IllegalArgumentException();
        }
        if (i11 == -1) {
            throw new IllegalArgumentException();
        }
        if (i10 < 1 || i10 > 31) {
            throw new IllegalArgumentException();
        }
        if (i9 < 0 || i9 > 23) {
            throw new IllegalArgumentException();
        }
        if (i12 < 0 || i12 > 59) {
            throw new IllegalArgumentException();
        }
        if (i13 < 0 || i13 > 59) {
            throw new IllegalArgumentException();
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
        gregorianCalendar.setLenient(false);
        gregorianCalendar.set(1, i15);
        gregorianCalendar.set(2, i11 - 1);
        gregorianCalendar.set(5, i10);
        gregorianCalendar.set(11, i9);
        gregorianCalendar.set(12, i12);
        gregorianCalendar.set(13, i13);
        gregorianCalendar.set(14, 0);
        return gregorianCalendar.getTimeInMillis();
    }

    private static long parseMaxAge(String str) throws NumberFormatException {
        long j = Long.MIN_VALUE;
        try {
            long j2 = Long.parseLong(str);
            if (j2 > 0) {
                j = j2;
            }
            return j;
        } catch (NumberFormatException e) {
            if (!str.matches("-?\\d+")) {
                throw e;
            }
            if (!str.startsWith("-")) {
                j = Long.MAX_VALUE;
            }
            return j;
        }
    }

    private static boolean pathMatch(HttpUrl httpUrl, String str) {
        String strEncodedPath = httpUrl.encodedPath();
        if (strEncodedPath.equals(str)) {
            return true;
        }
        if (strEncodedPath.startsWith(str)) {
            return str.endsWith("/") || strEncodedPath.charAt(str.length()) == '/';
        }
        return false;
    }

    public String domain() {
        return this.domain;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Cookie)) {
            return false;
        }
        Cookie cookie = (Cookie) obj;
        boolean z = false;
        if (cookie.name.equals(this.name)) {
            z = false;
            if (cookie.value.equals(this.value)) {
                z = false;
                if (cookie.domain.equals(this.domain)) {
                    z = false;
                    if (cookie.path.equals(this.path)) {
                        z = false;
                        if (cookie.expiresAt == this.expiresAt) {
                            z = false;
                            if (cookie.secure == this.secure) {
                                z = false;
                                if (cookie.httpOnly == this.httpOnly) {
                                    z = false;
                                    if (cookie.persistent == this.persistent) {
                                        z = false;
                                        if (cookie.hostOnly == this.hostOnly) {
                                            z = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    public long expiresAt() {
        return this.expiresAt;
    }

    public int hashCode() {
        int iHashCode = this.name.hashCode();
        int iHashCode2 = this.value.hashCode();
        int iHashCode3 = this.domain.hashCode();
        int iHashCode4 = this.path.hashCode();
        long j = this.expiresAt;
        return ((((((((((((((((527 + iHashCode) * 31) + iHashCode2) * 31) + iHashCode3) * 31) + iHashCode4) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + (!this.secure ? 1 : 0)) * 31) + (!this.httpOnly ? 1 : 0)) * 31) + (!this.persistent ? 1 : 0)) * 31) + (!this.hostOnly ? 1 : 0);
    }

    public boolean hostOnly() {
        return this.hostOnly;
    }

    public boolean httpOnly() {
        return this.httpOnly;
    }

    public boolean matches(HttpUrl httpUrl) {
        if ((this.hostOnly ? httpUrl.host().equals(this.domain) : domainMatch(httpUrl.host(), this.domain)) && pathMatch(httpUrl, this.path)) {
            return !this.secure || httpUrl.isHttps();
        }
        return false;
    }

    public String name() {
        return this.name;
    }

    public String path() {
        return this.path;
    }

    public boolean persistent() {
        return this.persistent;
    }

    public boolean secure() {
        return this.secure;
    }

    public String toString() {
        return toString(false);
    }

    String toString(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('=');
        sb.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                sb.append("; max-age=0");
            } else {
                sb.append("; expires=");
                sb.append(HttpDate.format(new Date(this.expiresAt)));
            }
        }
        if (!this.hostOnly) {
            sb.append("; domain=");
            if (z) {
                sb.append(".");
            }
            sb.append(this.domain);
        }
        sb.append("; path=");
        sb.append(this.path);
        if (this.secure) {
            sb.append("; secure");
        }
        if (this.httpOnly) {
            sb.append("; httponly");
        }
        return sb.toString();
    }

    public String value() {
        return this.value;
    }
}
