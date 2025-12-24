package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/Util.class */
public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final RequestBody EMPTY_REQUEST;
    public static final ResponseBody EMPTY_RESPONSE;
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final ByteString UTF_8_BOM = ByteString.decodeHex("efbbbf");
    private static final ByteString UTF_16_BE_BOM = ByteString.decodeHex("feff");
    private static final ByteString UTF_16_LE_BOM = ByteString.decodeHex("fffe");
    private static final ByteString UTF_32_BE_BOM = ByteString.decodeHex("0000ffff");
    private static final ByteString UTF_32_LE_BOM = ByteString.decodeHex("ffff0000");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final Charset UTF_16_BE = Charset.forName("UTF-16BE");
    private static final Charset UTF_16_LE = Charset.forName("UTF-16LE");
    private static final Charset UTF_32_BE = Charset.forName("UTF-32BE");
    private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");
    public static final TimeZone UTC = TimeZone.getTimeZone("GMT");
    public static final Comparator<String> NATURAL_ORDER = new Comparator<String>() { // from class: okhttp3.internal.Util.1
        @Override // java.util.Comparator
        public int compare(String str, String str2) {
            return str.compareTo(str2);
        }
    };
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");

    static {
        byte[] bArr = new byte[0];
        EMPTY_BYTE_ARRAY = bArr;
        EMPTY_RESPONSE = ResponseBody.create((MediaType) null, bArr);
        EMPTY_REQUEST = RequestBody.create((MediaType) null, EMPTY_BYTE_ARRAY);
    }

    private Util() {
    }

    public static AssertionError assertionError(String str, Exception exc) {
        AssertionError assertionError = new AssertionError(str);
        try {
            assertionError.initCause(exc);
        } catch (IllegalStateException e) {
        }
        return assertionError;
    }

    public static Charset bomAwareCharset(BufferedSource bufferedSource, Charset charset) throws IOException {
        if (bufferedSource.rangeEquals(0L, UTF_8_BOM)) {
            bufferedSource.skip(UTF_8_BOM.size());
            return UTF_8;
        }
        if (bufferedSource.rangeEquals(0L, UTF_16_BE_BOM)) {
            bufferedSource.skip(UTF_16_BE_BOM.size());
            return UTF_16_BE;
        }
        if (bufferedSource.rangeEquals(0L, UTF_16_LE_BOM)) {
            bufferedSource.skip(UTF_16_LE_BOM.size());
            return UTF_16_LE;
        }
        if (bufferedSource.rangeEquals(0L, UTF_32_BE_BOM)) {
            bufferedSource.skip(UTF_32_BE_BOM.size());
            return UTF_32_BE;
        }
        if (!bufferedSource.rangeEquals(0L, UTF_32_LE_BOM)) {
            return charset;
        }
        bufferedSource.skip(UTF_32_LE_BOM.size());
        return UTF_32_LE;
    }

    public static String canonicalizeHost(String str) {
        if (!str.contains(":")) {
            try {
                String lowerCase = IDN.toASCII(str).toLowerCase(Locale.US);
                if (lowerCase.isEmpty()) {
                    return null;
                }
                if (containsInvalidHostnameAsciiCodes(lowerCase)) {
                    return null;
                }
                return lowerCase;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        InetAddress inetAddressDecodeIpv6 = (str.startsWith("[") && str.endsWith("]")) ? decodeIpv6(str, 1, str.length() - 1) : decodeIpv6(str, 0, str.length());
        if (inetAddressDecodeIpv6 == null) {
            return null;
        }
        byte[] address = inetAddressDecodeIpv6.getAddress();
        if (address.length == 16) {
            return inet6AddressToAscii(address);
        }
        throw new AssertionError("Invalid IPv6 address: '" + str + "'");
    }

    public static int checkDuration(String str, long j, TimeUnit timeUnit) {
        if (j < 0) {
            throw new IllegalArgumentException(str + " < 0");
        }
        if (timeUnit == null) {
            throw new NullPointerException("unit == null");
        }
        long millis = timeUnit.toMillis(j);
        if (millis > 2147483647L) {
            throw new IllegalArgumentException(str + " too large.");
        }
        if (millis != 0 || j <= 0) {
            return (int) millis;
        }
        throw new IllegalArgumentException(str + " too small.");
    }

    public static void checkOffsetAndCount(long j, long j2, long j3) {
        if ((j2 | j3) < 0 || j2 > j || j - j2 < j3) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static void closeQuietly(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) throws IOException {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(Socket socket) throws IOException {
        if (socket != null) {
            try {
                socket.close();
            } catch (AssertionError e) {
                if (!isAndroidGetsocknameError(e)) {
                    throw e;
                }
            } catch (RuntimeException e2) {
                throw e2;
            } catch (Exception e3) {
            }
        }
    }

    public static String[] concat(String[] strArr, String str) {
        int length = strArr.length + 1;
        String[] strArr2 = new String[length];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        strArr2[length - 1] = str;
        return strArr2;
    }

    private static boolean containsInvalidHostnameAsciiCodes(String str) {
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt <= 31 || cCharAt >= 127 || " #%/:?@[\\]".indexOf(cCharAt) != -1) {
                return true;
            }
        }
        return false;
    }

    public static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if (c < 'a' || c > 'f') {
            c2 = 'A';
            if (c < 'A' || c > 'F') {
                return -1;
            }
        }
        return (c - c2) + 10;
    }

    private static boolean decodeIpv4Suffix(String str, int i, int i2, byte[] bArr, int i3) {
        char cCharAt;
        int i4 = i3;
        int i5 = i;
        while (i5 < i2) {
            if (i4 == bArr.length) {
                return false;
            }
            int i6 = i5;
            if (i4 != i3) {
                if (str.charAt(i5) != '.') {
                    return false;
                }
                i6 = i5 + 1;
            }
            i5 = i6;
            int i7 = 0;
            while (i5 < i2 && (cCharAt = str.charAt(i5)) >= '0' && cCharAt <= '9') {
                if (i7 == 0 && i6 != i5) {
                    return false;
                }
                i7 = ((i7 * 10) + cCharAt) - 48;
                if (i7 > 255) {
                    return false;
                }
                i5++;
            }
            if (i5 - i6 == 0) {
                return false;
            }
            bArr[i4] = (byte) i7;
            i4++;
        }
        return i4 == i3 + 4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0126, code lost:
    
        if (r12 == 16) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x012c, code lost:
    
        if (r14 != (-1)) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x012f, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0131, code lost:
    
        r0 = r12 - r14;
        java.lang.System.arraycopy(r0, r14, r0, 16 - r0, r0);
        java.util.Arrays.fill(r0, r14, (16 - r12) + r14, (byte) 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x015c, code lost:
    
        return java.net.InetAddress.getByAddress(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x016a, code lost:
    
        throw new java.lang.AssertionError();
     */
    @javax.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.net.InetAddress decodeIpv6(java.lang.String r7, int r8, int r9) {
        /*
            Method dump skipped, instructions count: 366
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.Util.decodeIpv6(java.lang.String, int, int):java.net.InetAddress");
    }

    public static int delimiterOffset(String str, int i, int i2, char c) {
        while (i < i2) {
            if (str.charAt(i) == c) {
                return i;
            }
            i++;
        }
        return i2;
    }

    public static int delimiterOffset(String str, int i, int i2, String str2) {
        while (i < i2) {
            if (str2.indexOf(str.charAt(i)) != -1) {
                return i;
            }
            i++;
        }
        return i2;
    }

    public static boolean discard(Source source, int i, TimeUnit timeUnit) {
        try {
            return skipAll(source, i, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static String format(String str, Object... objArr) {
        return String.format(Locale.US, str, objArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String hostHeader(okhttp3.HttpUrl r3, boolean r4) {
        /*
            r0 = r3
            java.lang.String r0 = r0.host()
            java.lang.String r1 = ":"
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L33
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r5 = r0
            r0 = r5
            java.lang.String r1 = "["
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r5
            r1 = r3
            java.lang.String r1 = r1.host()
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r5
            java.lang.String r1 = "]"
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r5
            java.lang.String r0 = r0.toString()
            r5 = r0
            goto L38
        L33:
            r0 = r3
            java.lang.String r0 = r0.host()
            r5 = r0
        L38:
            r0 = r4
            if (r0 != 0) goto L4c
            r0 = r5
            r6 = r0
            r0 = r3
            int r0 = r0.port()
            r1 = r3
            java.lang.String r1 = r1.scheme()
            int r1 = okhttp3.HttpUrl.defaultPort(r1)
            if (r0 == r1) goto L6f
        L4c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = r6
            r1 = r5
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r6
            java.lang.String r1 = ":"
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r6
            r1 = r3
            int r1 = r1.port()
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r6
            java.lang.String r0 = r0.toString()
            r6 = r0
        L6f:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.Util.hostHeader(okhttp3.HttpUrl, boolean):java.lang.String");
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList(list));
    }

    public static <T> List<T> immutableList(T... tArr) {
        return Collections.unmodifiableList(Arrays.asList((Object[]) tArr.clone()));
    }

    public static int indexOf(Comparator<String> comparator, String[] strArr, String str) {
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            if (comparator.compare(strArr[i], str) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfControlOrNonAscii(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt <= 31 || cCharAt >= 127) {
                return i;
            }
        }
        return -1;
    }

    private static String inet6AddressToAscii(byte[] bArr) {
        int i;
        int i2 = -1;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = i4;
            if (i3 >= bArr.length) {
                break;
            }
            int i5 = i3;
            while (i5 < 16 && bArr[i5] == 0 && bArr[i5 + 1] == 0) {
                i5 += 2;
            }
            int i6 = i5 - i3;
            int i7 = i2;
            int i8 = i;
            if (i6 > i) {
                i7 = i2;
                i8 = i;
                if (i6 >= 4) {
                    i8 = i6;
                    i7 = i3;
                }
            }
            i3 = i5 + 2;
            i2 = i7;
            i4 = i8;
        }
        Buffer buffer = new Buffer();
        int i9 = 0;
        while (i9 < bArr.length) {
            if (i9 == i2) {
                buffer.writeByte(58);
                int i10 = i9 + i;
                i9 = i10;
                if (i10 == 16) {
                    buffer.writeByte(58);
                    i9 = i10;
                }
            } else {
                if (i9 > 0) {
                    buffer.writeByte(58);
                }
                buffer.writeHexadecimalUnsignedLong(((bArr[i9] & 255) << 8) | (bArr[i9 + 1] & 255));
                i9 += 2;
            }
        }
        return buffer.readUtf8();
    }

    public static String[] intersect(Comparator<? super String> comparator, String[] strArr, String[] strArr2) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            int length = strArr2.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (comparator.compare(str, strArr2[i]) == 0) {
                    arrayList.add(str);
                    break;
                }
                i++;
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        return (assertionError.getCause() == null || assertionError.getMessage() == null || !assertionError.getMessage().contains("getsockname failed")) ? false : true;
    }

    public static boolean nonEmptyIntersection(Comparator<String> comparator, String[] strArr, String[] strArr2) {
        if (strArr == null || strArr2 == null || strArr.length == 0 || strArr2.length == 0) {
            return false;
        }
        for (String str : strArr) {
            for (String str2 : strArr2) {
                if (comparator.compare(str, str2) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean skipAll(Source source, int i, TimeUnit timeUnit) throws IOException {
        long jNanoTime = System.nanoTime();
        long jDeadlineNanoTime = source.timeout().hasDeadline() ? source.timeout().deadlineNanoTime() - jNanoTime : Long.MAX_VALUE;
        source.timeout().deadlineNanoTime(Math.min(jDeadlineNanoTime, timeUnit.toNanos(i)) + jNanoTime);
        try {
            Buffer buffer = new Buffer();
            while (source.read(buffer, 8192L) != -1) {
                buffer.clear();
            }
            if (jDeadlineNanoTime == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                return true;
            }
            source.timeout().deadlineNanoTime(jNanoTime + jDeadlineNanoTime);
            return true;
        } catch (InterruptedIOException e) {
            if (jDeadlineNanoTime == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                return false;
            }
            source.timeout().deadlineNanoTime(jNanoTime + jDeadlineNanoTime);
            return false;
        } catch (Throwable th) {
            if (jDeadlineNanoTime == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
            } else {
                source.timeout().deadlineNanoTime(jNanoTime + jDeadlineNanoTime);
            }
            throw th;
        }
    }

    public static int skipLeadingAsciiWhitespace(String str, int i, int i2) {
        while (i < i2) {
            char cCharAt = str.charAt(i);
            if (cCharAt != '\t' && cCharAt != '\n' && cCharAt != '\f' && cCharAt != '\r' && cCharAt != ' ') {
                return i;
            }
            i++;
        }
        return i2;
    }

    public static int skipTrailingAsciiWhitespace(String str, int i, int i2) {
        while (true) {
            i2--;
            if (i2 < i) {
                return i;
            }
            char cCharAt = str.charAt(i2);
            if (cCharAt != '\t' && cCharAt != '\n' && cCharAt != '\f' && cCharAt != '\r' && cCharAt != ' ') {
                return i2 + 1;
            }
        }
    }

    public static ThreadFactory threadFactory(String str, boolean z) {
        return new ThreadFactory(str, z) { // from class: okhttp3.internal.Util.2
            final boolean val$daemon;
            final String val$name;

            {
                this.val$name = str;
                this.val$daemon = z;
            }

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, this.val$name);
                thread.setDaemon(this.val$daemon);
                return thread;
            }
        };
    }

    public static String trimSubstring(String str, int i, int i2) {
        int iSkipLeadingAsciiWhitespace = skipLeadingAsciiWhitespace(str, i, i2);
        return str.substring(iSkipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace(str, iSkipLeadingAsciiWhitespace, i2));
    }

    public static boolean verifyAsIpAddress(String str) {
        return VERIFY_AS_IP_ADDRESS.matcher(str).matches();
    }
}
