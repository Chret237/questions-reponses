package okhttp3;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;

/* loaded from: classes-dex2jar.jar:okhttp3/HttpUrl.class */
public final class HttpUrl {
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";

    @Nullable
    private final String fragment;
    final String host;
    private final String password;
    private final List<String> pathSegments;
    final int port;

    @Nullable
    private final List<String> queryNamesAndValues;
    final String scheme;
    private final String url;
    private final String username;

    /* renamed from: okhttp3.HttpUrl$1 */
    /* loaded from: classes-dex2jar.jar:okhttp3/HttpUrl$1.class */
    static /* synthetic */ class C05351 {
        static final int[] $SwitchMap$okhttp3$HttpUrl$Builder$ParseResult;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:19:0x004d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                okhttp3.HttpUrl$Builder$ParseResult[] r0 = okhttp3.HttpUrl.Builder.ParseResult.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                okhttp3.HttpUrl.C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult = r0
                r0 = r4
                okhttp3.HttpUrl$Builder$ParseResult r1 = okhttp3.HttpUrl.Builder.ParseResult.SUCCESS     // Catch: java.lang.NoSuchFieldError -> L41
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L41
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L41
            L14:
                int[] r0 = okhttp3.HttpUrl.C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult     // Catch: java.lang.NoSuchFieldError -> L41 java.lang.NoSuchFieldError -> L45
                okhttp3.HttpUrl$Builder$ParseResult r1 = okhttp3.HttpUrl.Builder.ParseResult.INVALID_HOST     // Catch: java.lang.NoSuchFieldError -> L45
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L45
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L45
            L1f:
                int[] r0 = okhttp3.HttpUrl.C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult     // Catch: java.lang.NoSuchFieldError -> L45 java.lang.NoSuchFieldError -> L49
                okhttp3.HttpUrl$Builder$ParseResult r1 = okhttp3.HttpUrl.Builder.ParseResult.UNSUPPORTED_SCHEME     // Catch: java.lang.NoSuchFieldError -> L49
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L49
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L49
            L2a:
                int[] r0 = okhttp3.HttpUrl.C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult     // Catch: java.lang.NoSuchFieldError -> L49 java.lang.NoSuchFieldError -> L4d
                okhttp3.HttpUrl$Builder$ParseResult r1 = okhttp3.HttpUrl.Builder.ParseResult.MISSING_SCHEME     // Catch: java.lang.NoSuchFieldError -> L4d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L4d
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L4d
            L35:
                int[] r0 = okhttp3.HttpUrl.C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult     // Catch: java.lang.NoSuchFieldError -> L4d java.lang.NoSuchFieldError -> L51
                okhttp3.HttpUrl$Builder$ParseResult r1 = okhttp3.HttpUrl.Builder.ParseResult.INVALID_PORT     // Catch: java.lang.NoSuchFieldError -> L51
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L51
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L51
            L40:
                return
            L41:
                r4 = move-exception
                goto L14
            L45:
                r4 = move-exception
                goto L1f
            L49:
                r4 = move-exception
                goto L2a
            L4d:
                r4 = move-exception
                goto L35
            L51:
                r4 = move-exception
                goto L40
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.C05351.m446clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/HttpUrl$Builder.class */
    public static final class Builder {

        @Nullable
        String encodedFragment;
        final List<String> encodedPathSegments;

        @Nullable
        List<String> encodedQueryNamesAndValues;

        @Nullable
        String host;

        @Nullable
        String scheme;
        String encodedUsername = "";
        String encodedPassword = "";
        int port = -1;

        /* loaded from: classes-dex2jar.jar:okhttp3/HttpUrl$Builder$ParseResult.class */
        enum ParseResult {
            SUCCESS,
            MISSING_SCHEME,
            UNSUPPORTED_SCHEME,
            INVALID_PORT,
            INVALID_HOST
        }

        public Builder() {
            ArrayList arrayList = new ArrayList();
            this.encodedPathSegments = arrayList;
            arrayList.add("");
        }

        private Builder addPathSegments(String str, boolean z) {
            int i;
            int i2 = 0;
            do {
                int iDelimiterOffset = Util.delimiterOffset(str, i2, str.length(), "/\\");
                push(str, i2, iDelimiterOffset, iDelimiterOffset < str.length(), z);
                i = iDelimiterOffset + 1;
                i2 = i;
            } while (i <= str.length());
            return this;
        }

        private static String canonicalizeHost(String str, int i, int i2) {
            return Util.canonicalizeHost(HttpUrl.percentDecode(str, i, i2, false));
        }

        private boolean isDot(String str) {
            return str.equals(".") || str.equalsIgnoreCase("%2e");
        }

        private boolean isDotDot(String str) {
            return str.equals("..") || str.equalsIgnoreCase("%2e.") || str.equalsIgnoreCase(".%2e") || str.equalsIgnoreCase("%2e%2e");
        }

        private static int parsePort(String str, int i, int i2) throws NumberFormatException {
            try {
                int i3 = Integer.parseInt(HttpUrl.canonicalize(str, i, i2, "", false, false, false, true, null));
                if (i3 <= 0 || i3 > 65535) {
                    return -1;
                }
                return i3;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private void pop() {
            List<String> list = this.encodedPathSegments;
            if (!list.remove(list.size() - 1).isEmpty() || this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add("");
            } else {
                List<String> list2 = this.encodedPathSegments;
                list2.set(list2.size() - 1, "");
            }
        }

        private static int portColonOffset(String str, int i, int i2) {
            int i3;
            while (i < i2) {
                char cCharAt = str.charAt(i);
                if (cCharAt == ':') {
                    return i;
                }
                int i4 = i;
                if (cCharAt != '[') {
                    i3 = i;
                } else {
                    while (true) {
                        int i5 = i4 + 1;
                        i3 = i5;
                        if (i5 < i2) {
                            i4 = i5;
                            if (str.charAt(i5) == ']') {
                                i3 = i5;
                                break;
                            }
                        }
                    }
                }
                i = i3 + 1;
            }
            return i2;
        }

        private void push(String str, int i, int i2, boolean z, boolean z2) {
            String strCanonicalize = HttpUrl.canonicalize(str, i, i2, HttpUrl.PATH_SEGMENT_ENCODE_SET, z2, false, false, true, null);
            if (isDot(strCanonicalize)) {
                return;
            }
            if (isDotDot(strCanonicalize)) {
                pop();
                return;
            }
            List<String> list = this.encodedPathSegments;
            if (list.get(list.size() - 1).isEmpty()) {
                List<String> list2 = this.encodedPathSegments;
                list2.set(list2.size() - 1, strCanonicalize);
            } else {
                this.encodedPathSegments.add(strCanonicalize);
            }
            if (z) {
                this.encodedPathSegments.add("");
            }
        }

        private void removeAllCanonicalQueryParameters(String str) {
            for (int size = this.encodedQueryNamesAndValues.size() - 2; size >= 0; size -= 2) {
                if (str.equals(this.encodedQueryNamesAndValues.get(size))) {
                    this.encodedQueryNamesAndValues.remove(size + 1);
                    this.encodedQueryNamesAndValues.remove(size);
                    if (this.encodedQueryNamesAndValues.isEmpty()) {
                        this.encodedQueryNamesAndValues = null;
                        return;
                    }
                }
            }
        }

        private void resolvePath(String str, int i, int i2) {
            if (i == i2) {
                return;
            }
            char cCharAt = str.charAt(i);
            if (cCharAt == '/' || cCharAt == '\\') {
                this.encodedPathSegments.clear();
                this.encodedPathSegments.add("");
                i++;
            } else {
                List<String> list = this.encodedPathSegments;
                list.set(list.size() - 1, "");
            }
            while (i < i2) {
                int iDelimiterOffset = Util.delimiterOffset(str, i, i2, "/\\");
                boolean z = iDelimiterOffset < i2;
                push(str, i, iDelimiterOffset, z, true);
                i = iDelimiterOffset;
                if (z) {
                    i = iDelimiterOffset + 1;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0020  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static int schemeDelimiterOffset(java.lang.String r3, int r4, int r5) {
            /*
                r0 = r5
                r1 = r4
                int r0 = r0 - r1
                r1 = 2
                if (r0 >= r1) goto L9
                r0 = -1
                return r0
            L9:
                r0 = r3
                r1 = r4
                char r0 = r0.charAt(r1)
                r7 = r0
                r0 = r7
                r1 = 97
                if (r0 < r1) goto L20
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 122(0x7a, float:1.71E-43)
                if (r0 <= r1) goto L33
            L20:
                r0 = r7
                r1 = 65
                if (r0 < r1) goto L9a
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 90
                if (r0 <= r1) goto L33
                goto L9a
            L33:
                r0 = r6
                r1 = 1
                int r0 = r0 + r1
                r4 = r0
                r0 = r4
                r1 = r5
                if (r0 >= r1) goto L9a
                r0 = r3
                r1 = r4
                char r0 = r0.charAt(r1)
                r7 = r0
                r0 = r7
                r1 = 97
                if (r0 < r1) goto L53
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 122(0x7a, float:1.71E-43)
                if (r0 <= r1) goto L33
            L53:
                r0 = r7
                r1 = 65
                if (r0 < r1) goto L63
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 90
                if (r0 <= r1) goto L33
            L63:
                r0 = r7
                r1 = 48
                if (r0 < r1) goto L73
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 57
                if (r0 <= r1) goto L33
            L73:
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 43
                if (r0 == r1) goto L33
                r0 = r4
                r6 = r0
                r0 = r7
                r1 = 45
                if (r0 == r1) goto L33
                r0 = r7
                r1 = 46
                if (r0 != r1) goto L91
                r0 = r4
                r6 = r0
                goto L33
            L91:
                r0 = r7
                r1 = 58
                if (r0 != r1) goto L9a
                r0 = r4
                return r0
            L9a:
                r0 = -1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.schemeDelimiterOffset(java.lang.String, int, int):int");
        }

        private static int slashCount(String str, int i, int i2) {
            char cCharAt;
            int i3 = 0;
            while (i < i2 && ((cCharAt = str.charAt(i)) == '\\' || cCharAt == '/')) {
                i3++;
                i++;
            }
            return i3;
        }

        public Builder addEncodedPathSegment(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            push(str, 0, str.length(), false, true);
            return this;
        }

        public Builder addEncodedPathSegments(String str) {
            if (str != null) {
                return addPathSegments(str, true);
            }
            throw new NullPointerException("encodedPathSegments == null");
        }

        public Builder addEncodedQueryParameter(String str, @Nullable String str2) {
            if (str == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, true));
            this.encodedQueryNamesAndValues.add(str2 != null ? HttpUrl.canonicalize(str2, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, true) : null);
            return this;
        }

        public Builder addPathSegment(String str) {
            if (str == null) {
                throw new NullPointerException("pathSegment == null");
            }
            push(str, 0, str.length(), false, false);
            return this;
        }

        public Builder addPathSegments(String str) {
            if (str != null) {
                return addPathSegments(str, false);
            }
            throw new NullPointerException("pathSegments == null");
        }

        public Builder addQueryParameter(String str, @Nullable String str2) {
            if (str == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                this.encodedQueryNamesAndValues = new ArrayList();
            }
            this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
            this.encodedQueryNamesAndValues.add(str2 != null ? HttpUrl.canonicalize(str2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true) : null);
            return this;
        }

        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            }
            if (this.host != null) {
                return new HttpUrl(this);
            }
            throw new IllegalStateException("host == null");
        }

        int effectivePort() {
            int iDefaultPort = this.port;
            if (iDefaultPort == -1) {
                iDefaultPort = HttpUrl.defaultPort(this.scheme);
            }
            return iDefaultPort;
        }

        public Builder encodedFragment(@Nullable String str) {
            this.encodedFragment = str != null ? HttpUrl.canonicalize(str, "", true, false, false, false) : null;
            return this;
        }

        public Builder encodedPassword(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPassword == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }

        public Builder encodedPath(String str) {
            if (str == null) {
                throw new NullPointerException("encodedPath == null");
            }
            if (str.startsWith("/")) {
                resolvePath(str, 0, str.length());
                return this;
            }
            throw new IllegalArgumentException("unexpected encodedPath: " + str);
        }

        public Builder encodedQuery(@Nullable String str) {
            this.encodedQueryNamesAndValues = str != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str, HttpUrl.QUERY_ENCODE_SET, true, false, true, true)) : null;
            return this;
        }

        public Builder encodedUsername(String str) {
            if (str == null) {
                throw new NullPointerException("encodedUsername == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
            return this;
        }

        public Builder fragment(@Nullable String str) {
            this.encodedFragment = str != null ? HttpUrl.canonicalize(str, "", false, false, false, false) : null;
            return this;
        }

        public Builder host(String str) {
            if (str == null) {
                throw new NullPointerException("host == null");
            }
            String strCanonicalizeHost = canonicalizeHost(str, 0, str.length());
            if (strCanonicalizeHost != null) {
                this.host = strCanonicalizeHost;
                return this;
            }
            throw new IllegalArgumentException("unexpected host: " + str);
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x00d5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        okhttp3.HttpUrl.Builder.ParseResult parse(@javax.annotation.Nullable okhttp3.HttpUrl r12, java.lang.String r13) throws java.lang.NumberFormatException {
            /*
                Method dump skipped, instructions count: 715
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.parse(okhttp3.HttpUrl, java.lang.String):okhttp3.HttpUrl$Builder$ParseResult");
        }

        public Builder password(String str) {
            if (str == null) {
                throw new NullPointerException("password == null");
            }
            this.encodedPassword = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }

        public Builder port(int i) {
            if (i > 0 && i <= 65535) {
                this.port = i;
                return this;
            }
            throw new IllegalArgumentException("unexpected port: " + i);
        }

        public Builder query(@Nullable String str) {
            this.encodedQueryNamesAndValues = str != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(str, HttpUrl.QUERY_ENCODE_SET, false, false, true, true)) : null;
            return this;
        }

        Builder reencodeForUri() {
            int size = this.encodedPathSegments.size();
            for (int i = 0; i < size; i++) {
                this.encodedPathSegments.set(i, HttpUrl.canonicalize(this.encodedPathSegments.get(i), HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, true, true, false, true));
            }
            List<String> list = this.encodedQueryNamesAndValues;
            if (list != null) {
                int size2 = list.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    String str = this.encodedQueryNamesAndValues.get(i2);
                    if (str != null) {
                        this.encodedQueryNamesAndValues.set(i2, HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, true));
                    }
                }
            }
            String str2 = this.encodedFragment;
            if (str2 != null) {
                this.encodedFragment = HttpUrl.canonicalize(str2, HttpUrl.FRAGMENT_ENCODE_SET_URI, true, true, false, false);
            }
            return this;
        }

        public Builder removeAllEncodedQueryParameters(String str) {
            if (str == null) {
                throw new NullPointerException("encodedName == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            removeAllCanonicalQueryParameters(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, true));
            return this;
        }

        public Builder removeAllQueryParameters(String str) {
            if (str == null) {
                throw new NullPointerException("name == null");
            }
            if (this.encodedQueryNamesAndValues == null) {
                return this;
            }
            removeAllCanonicalQueryParameters(HttpUrl.canonicalize(str, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
            return this;
        }

        public Builder removePathSegment(int i) {
            this.encodedPathSegments.remove(i);
            if (this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add("");
            }
            return this;
        }

        public Builder scheme(String str) {
            if (str == null) {
                throw new NullPointerException("scheme == null");
            }
            if (str.equalsIgnoreCase("http")) {
                this.scheme = "http";
            } else {
                if (!str.equalsIgnoreCase("https")) {
                    throw new IllegalArgumentException("unexpected scheme: " + str);
                }
                this.scheme = "https";
            }
            return this;
        }

        public Builder setEncodedPathSegment(int i, String str) {
            if (str == null) {
                throw new NullPointerException("encodedPathSegment == null");
            }
            String strCanonicalize = HttpUrl.canonicalize(str, 0, str.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false, false, true, null);
            this.encodedPathSegments.set(i, strCanonicalize);
            if (!isDot(strCanonicalize) && !isDotDot(strCanonicalize)) {
                return this;
            }
            throw new IllegalArgumentException("unexpected path segment: " + str);
        }

        public Builder setEncodedQueryParameter(String str, @Nullable String str2) {
            removeAllEncodedQueryParameters(str);
            addEncodedQueryParameter(str, str2);
            return this;
        }

        public Builder setPathSegment(int i, String str) {
            if (str == null) {
                throw new NullPointerException("pathSegment == null");
            }
            String strCanonicalize = HttpUrl.canonicalize(str, 0, str.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false, false, true, null);
            if (!isDot(strCanonicalize) && !isDotDot(strCanonicalize)) {
                this.encodedPathSegments.set(i, strCanonicalize);
                return this;
            }
            throw new IllegalArgumentException("unexpected path segment: " + str);
        }

        public Builder setQueryParameter(String str, @Nullable String str2) {
            removeAllQueryParameters(str);
            addQueryParameter(str, str2);
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.scheme);
            sb.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                sb.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    sb.append(':');
                    sb.append(this.encodedPassword);
                }
                sb.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                sb.append('[');
                sb.append(this.host);
                sb.append(']');
            } else {
                sb.append(this.host);
            }
            int iEffectivePort = effectivePort();
            if (iEffectivePort != HttpUrl.defaultPort(this.scheme)) {
                sb.append(':');
                sb.append(iEffectivePort);
            }
            HttpUrl.pathSegmentsToString(sb, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                sb.append('?');
                HttpUrl.namesAndValuesToQueryString(sb, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                sb.append('#');
                sb.append(this.encodedFragment);
            }
            return sb.toString();
        }

        public Builder username(String str) {
            if (str == null) {
                throw new NullPointerException("username == null");
            }
            this.encodedUsername = HttpUrl.canonicalize(str, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
            return this;
        }
    }

    HttpUrl(Builder builder) {
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = percentDecode(builder.encodedPathSegments, false);
        this.queryNamesAndValues = builder.encodedQueryNamesAndValues != null ? percentDecode(builder.encodedQueryNamesAndValues, true) : null;
        this.fragment = builder.encodedFragment != null ? percentDecode(builder.encodedFragment, false) : null;
        this.url = builder.toString();
    }

    static String canonicalize(String str, int i, int i2, String str2, boolean z, boolean z2, boolean z3, boolean z4, Charset charset) {
        int i3;
        int iCharCount = i;
        while (true) {
            i3 = iCharCount;
            if (i3 >= i2) {
                return str.substring(i, i2);
            }
            int iCodePointAt = str.codePointAt(i3);
            if (iCodePointAt < 32 || iCodePointAt == 127 || ((iCodePointAt >= 128 && z4) || str2.indexOf(iCodePointAt) != -1 || ((iCodePointAt == 37 && (!z || (z2 && !percentEncoded(str, i3, i2)))) || (iCodePointAt == 43 && z3)))) {
                break;
            }
            iCharCount = i3 + Character.charCount(iCodePointAt);
        }
        Buffer buffer = new Buffer();
        buffer.writeUtf8(str, i, i3);
        canonicalize(buffer, str, i3, i2, str2, z, z2, z3, z4, charset);
        return buffer.readUtf8();
    }

    static String canonicalize(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4) {
        return canonicalize(str, 0, str.length(), str2, z, z2, z3, z4, null);
    }

    static String canonicalize(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, Charset charset) {
        return canonicalize(str, 0, str.length(), str2, z, z2, z3, z4, charset);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void canonicalize(okio.Buffer r6, java.lang.String r7, int r8, int r9, java.lang.String r10, boolean r11, boolean r12, boolean r13, boolean r14, java.nio.charset.Charset r15) {
        /*
            Method dump skipped, instructions count: 334
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.canonicalize(okio.Buffer, java.lang.String, int, int, java.lang.String, boolean, boolean, boolean, boolean, java.nio.charset.Charset):void");
    }

    public static int defaultPort(String str) {
        if (str.equals("http")) {
            return 80;
        }
        return str.equals("https") ? 443 : -1;
    }

    @Nullable
    public static HttpUrl get(URI uri) {
        return parse(uri.toString());
    }

    @Nullable
    public static HttpUrl get(URL url) {
        return parse(url.toString());
    }

    static HttpUrl getChecked(String str) throws MalformedURLException, NumberFormatException, UnknownHostException {
        Builder builder = new Builder();
        Builder.ParseResult parseResult = builder.parse(null, str);
        int i = C05351.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[parseResult.ordinal()];
        if (i == 1) {
            return builder.build();
        }
        if (i == 2) {
            throw new UnknownHostException("Invalid host: " + str);
        }
        throw new MalformedURLException("Invalid URL: " + parseResult + " for " + str);
    }

    static void namesAndValuesToQueryString(StringBuilder sb, List<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i += 2) {
            String str = list.get(i);
            String str2 = list.get(i + 1);
            if (i > 0) {
                sb.append('&');
            }
            sb.append(str);
            if (str2 != null) {
                sb.append('=');
                sb.append(str2);
            }
        }
    }

    @Nullable
    public static HttpUrl parse(String str) {
        Builder builder = new Builder();
        HttpUrl httpUrlBuild = null;
        if (builder.parse(null, str) == Builder.ParseResult.SUCCESS) {
            httpUrlBuild = builder.build();
        }
        return httpUrlBuild;
    }

    static void pathSegmentsToString(StringBuilder sb, List<String> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            sb.append('/');
            sb.append(list.get(i));
        }
    }

    static String percentDecode(String str, int i, int i2, boolean z) {
        for (int i3 = i; i3 < i2; i3++) {
            char cCharAt = str.charAt(i3);
            if (cCharAt == '%' || (cCharAt == '+' && z)) {
                Buffer buffer = new Buffer();
                buffer.writeUtf8(str, i, i3);
                percentDecode(buffer, str, i3, i2, z);
                return buffer.readUtf8();
            }
        }
        return str.substring(i, i2);
    }

    static String percentDecode(String str, boolean z) {
        return percentDecode(str, 0, str.length(), z);
    }

    private List<String> percentDecode(List<String> list, boolean z) {
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            String str = list.get(i);
            arrayList.add(str != null ? percentDecode(str, z) : null);
        }
        return Collections.unmodifiableList(arrayList);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void percentDecode(okio.Buffer r4, java.lang.String r5, int r6, int r7, boolean r8) {
        /*
        L0:
            r0 = r6
            r1 = r7
            if (r0 >= r1) goto L7b
            r0 = r5
            r1 = r6
            int r0 = r0.codePointAt(r1)
            r10 = r0
            r0 = r10
            r1 = 37
            if (r0 != r1) goto L53
            r0 = r6
            r1 = 2
            int r0 = r0 + r1
            r9 = r0
            r0 = r9
            r1 = r7
            if (r0 >= r1) goto L53
            r0 = r5
            r1 = r6
            r2 = 1
            int r1 = r1 + r2
            char r0 = r0.charAt(r1)
            int r0 = okhttp3.internal.Util.decodeHexDigit(r0)
            r11 = r0
            r0 = r5
            r1 = r9
            char r0 = r0.charAt(r1)
            int r0 = okhttp3.internal.Util.decodeHexDigit(r0)
            r12 = r0
            r0 = r11
            r1 = -1
            if (r0 == r1) goto L69
            r0 = r12
            r1 = -1
            if (r0 == r1) goto L69
            r0 = r4
            r1 = r11
            r2 = 4
            int r1 = r1 << r2
            r2 = r12
            int r1 = r1 + r2
            okio.Buffer r0 = r0.writeByte(r1)
            r0 = r9
            r6 = r0
            goto L70
        L53:
            r0 = r10
            r1 = 43
            if (r0 != r1) goto L69
            r0 = r8
            if (r0 == 0) goto L69
            r0 = r4
            r1 = 32
            okio.Buffer r0 = r0.writeByte(r1)
            goto L70
        L69:
            r0 = r4
            r1 = r10
            okio.Buffer r0 = r0.writeUtf8CodePoint(r1)
        L70:
            r0 = r6
            r1 = r10
            int r1 = java.lang.Character.charCount(r1)
            int r0 = r0 + r1
            r6 = r0
            goto L0
        L7b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.percentDecode(okio.Buffer, java.lang.String, int, int, boolean):void");
    }

    static boolean percentEncoded(String str, int i, int i2) {
        int i3 = i + 2;
        boolean z = true;
        if (i3 >= i2 || str.charAt(i) != '%' || Util.decodeHexDigit(str.charAt(i + 1)) == -1 || Util.decodeHexDigit(str.charAt(i3)) == -1) {
            z = false;
        }
        return z;
    }

    static List<String> queryStringToNamesAndValues(String str) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 > str.length()) {
                return arrayList;
            }
            int iIndexOf = str.indexOf(38, i2);
            int length = iIndexOf;
            if (iIndexOf == -1) {
                length = str.length();
            }
            int iIndexOf2 = str.indexOf(61, i2);
            if (iIndexOf2 == -1 || iIndexOf2 > length) {
                arrayList.add(str.substring(i2, length));
                arrayList.add(null);
            } else {
                arrayList.add(str.substring(i2, iIndexOf2));
                arrayList.add(str.substring(iIndexOf2 + 1, length));
            }
            i = length + 1;
        }
    }

    @Nullable
    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        return this.url.substring(this.url.indexOf(35) + 1);
    }

    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return "";
        }
        int iIndexOf = this.url.indexOf(58, this.scheme.length() + 3);
        return this.url.substring(iIndexOf + 1, this.url.indexOf(64));
    }

    public String encodedPath() {
        int iIndexOf = this.url.indexOf(47, this.scheme.length() + 3);
        String str = this.url;
        return this.url.substring(iIndexOf, Util.delimiterOffset(str, iIndexOf, str.length(), "?#"));
    }

    public List<String> encodedPathSegments() {
        int iIndexOf = this.url.indexOf(47, this.scheme.length() + 3);
        String str = this.url;
        int iDelimiterOffset = Util.delimiterOffset(str, iIndexOf, str.length(), "?#");
        ArrayList arrayList = new ArrayList();
        while (iIndexOf < iDelimiterOffset) {
            int i = iIndexOf + 1;
            iIndexOf = Util.delimiterOffset(this.url, i, iDelimiterOffset, '/');
            arrayList.add(this.url.substring(i, iIndexOf));
        }
        return arrayList;
    }

    @Nullable
    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int iIndexOf = this.url.indexOf(63) + 1;
        String str = this.url;
        return this.url.substring(iIndexOf, Util.delimiterOffset(str, iIndexOf, str.length(), '#'));
    }

    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return "";
        }
        int length = this.scheme.length() + 3;
        String str = this.url;
        return this.url.substring(length, Util.delimiterOffset(str, length, str.length(), ":@"));
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof HttpUrl) && ((HttpUrl) obj).url.equals(this.url);
    }

    @Nullable
    public String fragment() {
        return this.fragment;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public String host() {
        return this.host;
    }

    public boolean isHttps() {
        return this.scheme.equals("https");
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = encodedUsername();
        builder.encodedPassword = encodedPassword();
        builder.host = this.host;
        builder.port = this.port != defaultPort(this.scheme) ? this.port : -1;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(encodedPathSegments());
        builder.encodedQuery(encodedQuery());
        builder.encodedFragment = encodedFragment();
        return builder;
    }

    @Nullable
    public Builder newBuilder(String str) {
        Builder builder = new Builder();
        return builder.parse(this, str) == Builder.ParseResult.SUCCESS ? builder : null;
    }

    public String password() {
        return this.password;
    }

    public List<String> pathSegments() {
        return this.pathSegments;
    }

    public int pathSize() {
        return this.pathSegments.size();
    }

    public int port() {
        return this.port;
    }

    @Nullable
    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        namesAndValuesToQueryString(sb, this.queryNamesAndValues);
        return sb.toString();
    }

    @Nullable
    public String queryParameter(String str) {
        List<String> list = this.queryNamesAndValues;
        if (list == null) {
            return null;
        }
        int size = list.size();
        for (int i = 0; i < size; i += 2) {
            if (str.equals(this.queryNamesAndValues.get(i))) {
                return this.queryNamesAndValues.get(i + 1);
            }
        }
        return null;
    }

    public String queryParameterName(int i) {
        List<String> list = this.queryNamesAndValues;
        if (list != null) {
            return list.get(i * 2);
        }
        throw new IndexOutOfBoundsException();
    }

    public Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return Collections.emptySet();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int size = this.queryNamesAndValues.size();
        for (int i = 0; i < size; i += 2) {
            linkedHashSet.add(this.queryNamesAndValues.get(i));
        }
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public String queryParameterValue(int i) {
        List<String> list = this.queryNamesAndValues;
        if (list != null) {
            return list.get((i * 2) + 1);
        }
        throw new IndexOutOfBoundsException();
    }

    public List<String> queryParameterValues(String str) {
        if (this.queryNamesAndValues == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        int size = this.queryNamesAndValues.size();
        for (int i = 0; i < size; i += 2) {
            if (str.equals(this.queryNamesAndValues.get(i))) {
                arrayList.add(this.queryNamesAndValues.get(i + 1));
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public int querySize() {
        List<String> list = this.queryNamesAndValues;
        return list != null ? list.size() / 2 : 0;
    }

    public String redact() {
        return newBuilder("/...").username("").password("").build().toString();
    }

    @Nullable
    public HttpUrl resolve(String str) {
        Builder builderNewBuilder = newBuilder(str);
        return builderNewBuilder != null ? builderNewBuilder.build() : null;
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.url;
    }

    @Nullable
    public String topPrivateDomain() {
        if (Util.verifyAsIpAddress(this.host)) {
            return null;
        }
        return PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
    }

    public URI uri() {
        String string = newBuilder().reencodeForUri().toString();
        try {
            return new URI(string);
        } catch (URISyntaxException e) {
            try {
                return URI.create(string.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
            } catch (Exception e2) {
                throw new RuntimeException(e);
            }
        }
    }

    public URL url() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String username() {
        return this.username;
    }
}
