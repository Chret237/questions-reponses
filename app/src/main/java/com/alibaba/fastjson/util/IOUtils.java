package com.alibaba.fastjson.util;

import android.support.v7.widget.ActivityChooserView;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.asm.Opcodes;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Properties;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/IOUtils.class */
public class IOUtils {
    public static final char[] ASCII_CHARS;

    /* renamed from: CA */
    public static final char[] f74CA;
    static final char[] DigitOnes;
    static final char[] DigitTens;
    public static final String FASTJSON_COMPATIBLEWITHFIELDNAME = "fastjson.compatibleWithFieldName";
    public static final String FASTJSON_COMPATIBLEWITHJAVABEAN = "fastjson.compatibleWithJavaBean";
    public static final String FASTJSON_PROPERTIES = "fastjson.properties";

    /* renamed from: IA */
    public static final int[] f75IA;
    static final char[] digits;
    public static final char[] replaceChars;
    static final int[] sizeTable;
    public static final byte[] specicalFlags_doubleQuotes;
    public static final boolean[] specicalFlags_doubleQuotesFlags;
    public static final byte[] specicalFlags_singleQuotes;
    public static final boolean[] specicalFlags_singleQuotesFlags;
    public static final Properties DEFAULT_PROPERTIES = new Properties();
    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final boolean[] firstIdentifierFlags = new boolean[256];
    public static final boolean[] identifierFlags = new boolean[256];

    static {
        char c = 0;
        while (true) {
            char c2 = c;
            boolean[] zArr = firstIdentifierFlags;
            if (c2 >= zArr.length) {
                break;
            }
            if (c2 >= 'A' && c2 <= 'Z') {
                zArr[c2] = true;
            } else if (c2 >= 'a' && c2 <= 'z') {
                firstIdentifierFlags[c2] = true;
            } else if (c2 == '_' || c2 == '$') {
                firstIdentifierFlags[c2] = true;
            }
            c = (char) (c2 + 1);
        }
        char c3 = 0;
        while (true) {
            char c4 = c3;
            boolean[] zArr2 = identifierFlags;
            if (c4 < zArr2.length) {
                if (c4 >= 'A' && c4 <= 'Z') {
                    zArr2[c4] = true;
                } else if (c4 >= 'a' && c4 <= 'z') {
                    identifierFlags[c4] = true;
                } else if (c4 == '_') {
                    identifierFlags[c4] = true;
                } else if (c4 >= '0' && c4 <= '9') {
                    identifierFlags[c4] = true;
                }
                c3 = (char) (c4 + 1);
            } else {
                try {
                    break;
                } catch (Throwable th) {
                }
            }
        }
        loadPropertiesFromFile();
        byte[] bArr = new byte[Opcodes.IF_ICMPLT];
        specicalFlags_doubleQuotes = bArr;
        byte[] bArr2 = new byte[Opcodes.IF_ICMPLT];
        specicalFlags_singleQuotes = bArr2;
        specicalFlags_doubleQuotesFlags = new boolean[Opcodes.IF_ICMPLT];
        specicalFlags_singleQuotesFlags = new boolean[Opcodes.IF_ICMPLT];
        replaceChars = new char[93];
        bArr[0] = 4;
        bArr[1] = 4;
        bArr[2] = 4;
        bArr[3] = 4;
        bArr[4] = 4;
        bArr[5] = 4;
        bArr[6] = 4;
        bArr[7] = 4;
        bArr[8] = 1;
        bArr[9] = 1;
        bArr[10] = 1;
        bArr[11] = 4;
        bArr[12] = 1;
        bArr[13] = 1;
        bArr[34] = 1;
        bArr[92] = 1;
        bArr2[0] = 4;
        bArr2[1] = 4;
        bArr2[2] = 4;
        bArr2[3] = 4;
        bArr2[4] = 4;
        bArr2[5] = 4;
        bArr2[6] = 4;
        bArr2[7] = 4;
        bArr2[8] = 1;
        bArr2[9] = 1;
        bArr2[10] = 1;
        bArr2[11] = 4;
        bArr2[12] = 1;
        bArr2[13] = 1;
        bArr2[92] = 1;
        bArr2[39] = 1;
        for (int i = 14; i <= 31; i++) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }
        for (int i2 = 127; i2 < 160; i2++) {
            specicalFlags_doubleQuotes[i2] = 4;
            specicalFlags_singleQuotes[i2] = 4;
        }
        for (int i3 = 0; i3 < 161; i3++) {
            specicalFlags_doubleQuotesFlags[i3] = specicalFlags_doubleQuotes[i3] != 0;
            specicalFlags_singleQuotesFlags[i3] = specicalFlags_singleQuotes[i3] != 0;
        }
        char[] cArr = replaceChars;
        cArr[0] = '0';
        cArr[1] = '1';
        cArr[2] = '2';
        cArr[3] = '3';
        cArr[4] = '4';
        cArr[5] = '5';
        cArr[6] = '6';
        cArr[7] = '7';
        cArr[8] = 'b';
        cArr[9] = 't';
        cArr[10] = 'n';
        cArr[11] = 'v';
        cArr[12] = 'f';
        cArr[13] = 'r';
        cArr[34] = '\"';
        cArr[39] = '\'';
        cArr[47] = '/';
        cArr[92] = '\\';
        ASCII_CHARS = new char[]{'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F'};
        digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        DigitTens = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        DigitOnes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        sizeTable = new int[]{9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        f74CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
        int[] iArr = new int[256];
        f75IA = iArr;
        Arrays.fill(iArr, -1);
        int length = f74CA.length;
        for (int i4 = 0; i4 < length; i4++) {
            f75IA[f74CA[i4]] = i4;
        }
        f75IA[61] = 0;
    }

    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static void decode(CharsetDecoder charsetDecoder, ByteBuffer byteBuffer, CharBuffer charBuffer) throws CharacterCodingException {
        try {
            CoderResult coderResultDecode = charsetDecoder.decode(byteBuffer, charBuffer, true);
            if (!coderResultDecode.isUnderflow()) {
                coderResultDecode.throwException();
            }
            CoderResult coderResultFlush = charsetDecoder.flush(charBuffer);
            if (coderResultFlush.isUnderflow()) {
                return;
            }
            coderResultFlush.throwException();
        } catch (CharacterCodingException e) {
            throw new JSONException("utf8 decode error, " + e.getMessage(), e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0177 A[PHI: r8
  0x0177: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x015e, B:38:0x016b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeBase64(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 470
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.decodeBase64(java.lang.String):byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0178 A[PHI: r8
  0x0178: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x0162, B:38:0x016d] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeBase64(java.lang.String r6, int r7, int r8) {
        /*
            Method dump skipped, instructions count: 467
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.decodeBase64(java.lang.String, int, int):byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0157 A[PHI: r8
  0x0157: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x0141, B:38:0x014c] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeBase64(char[] r6, int r7, int r8) {
        /*
            Method dump skipped, instructions count: 432
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.decodeBase64(char[], int, int):byte[]");
    }

    public static int decodeUTF8(byte[] bArr, int i, int i2, char[] cArr) {
        int i3;
        int i4;
        int i5 = i + i2;
        int iMin = Math.min(i2, cArr.length);
        int i6 = 0;
        int i7 = i;
        while (true) {
            i3 = i6;
            i4 = i7;
            if (i6 >= iMin) {
                break;
            }
            i3 = i6;
            i4 = i7;
            if (bArr[i7] < 0) {
                break;
            }
            cArr[i6] = (char) bArr[i7];
            i6++;
            i7++;
        }
        while (i4 < i5) {
            int i8 = i4 + 1;
            byte b = bArr[i4];
            if (b >= 0) {
                cArr[i3] = (char) b;
                i4 = i8;
                i3++;
            } else if ((b >> 5) != -2 || (b & 30) == 0) {
                if ((b >> 4) == -2) {
                    int i9 = i8 + 1;
                    if (i9 >= i5) {
                        return -1;
                    }
                    byte b2 = bArr[i8];
                    byte b3 = bArr[i9];
                    if ((b == -32 && (b2 & 224) == 128) || (b2 & 192) != 128 || (b3 & 192) != 128) {
                        return -1;
                    }
                    char c = (char) (((b << 12) ^ (b2 << 6)) ^ ((-123008) ^ b3));
                    if (c >= 55296 && c < 57344) {
                        return -1;
                    }
                    cArr[i3] = c;
                    i3++;
                    i4 = i9 + 1;
                } else {
                    if ((b >> 3) != -2 || i8 + 2 >= i5) {
                        return -1;
                    }
                    int i10 = i8 + 1;
                    byte b4 = bArr[i8];
                    int i11 = i10 + 1;
                    byte b5 = bArr[i10];
                    byte b6 = bArr[i11];
                    int i12 = (((b << 18) ^ (b4 << 12)) ^ (b5 << 6)) ^ (3678080 ^ b6);
                    if ((b4 & 192) != 128 || (b5 & 192) != 128 || (b6 & 192) != 128 || i12 < 65536 || i12 >= 1114112) {
                        return -1;
                    }
                    int i13 = i3 + 1;
                    cArr[i3] = (char) ((i12 >>> 10) + 55232);
                    i3 = i13 + 1;
                    cArr[i13] = (char) ((i12 & 1023) + 56320);
                    i4 = i11 + 1;
                }
            } else {
                if (i8 >= i5) {
                    return -1;
                }
                byte b7 = bArr[i8];
                if ((b7 & 192) != 128) {
                    return -1;
                }
                cArr[i3] = (char) (((b << 6) ^ b7) ^ 3968);
                i4 = i8 + 1;
                i3++;
            }
        }
        return i3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:40:0x012e A[PHI: r10
  0x012e: PHI (r10v13 char) = (r10v10 char), (r10v11 char), (r10v17 char), (r10v18 char) binds: [B:35:0x010d, B:37:0x0118, B:32:0x00e5, B:27:0x00c9] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0143  */
    /* JADX WARN: Type inference failed for: r0v82, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int encodeUTF8(char[] r6, int r7, int r8, byte[] r9) {
        /*
            Method dump skipped, instructions count: 474
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IOUtils.encodeUTF8(char[], int, int, byte[]):int");
    }

    public static boolean firstIdentifier(char c) {
        boolean[] zArr = firstIdentifierFlags;
        return c < zArr.length && zArr[c];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [int] */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    public static void getChars(byte b, int i, char[] cArr) {
        char c;
        if (b < 0) {
            c = '-';
            b = -b;
        } else {
            c = 0;
        }
        while (true) {
            ?? r0 = (52429 * b) >>> 19;
            i--;
            cArr[i] = digits[b - ((r0 << 3) + (r0 << 1))];
            if (r0 == 0) {
                break;
            } else {
                b = r0;
            }
        }
        if (c != 0) {
            cArr[i - 1] = c;
        }
    }

    public static void getChars(int i, int i2, char[] cArr) {
        char c;
        int i3;
        int i4;
        if (i < 0) {
            c = '-';
            i = -i;
        } else {
            c = 0;
        }
        while (true) {
            i3 = i;
            i4 = i2;
            if (i < 65536) {
                break;
            }
            int i5 = i / 100;
            int i6 = i - (((i5 << 6) + (i5 << 5)) + (i5 << 2));
            int i7 = i2 - 1;
            cArr[i7] = DigitOnes[i6];
            i2 = i7 - 1;
            cArr[i2] = DigitTens[i6];
            i = i5;
        }
        while (true) {
            int i8 = (52429 * i3) >>> 19;
            i4--;
            cArr[i4] = digits[i3 - ((i8 << 3) + (i8 << 1))];
            if (i8 == 0) {
                break;
            } else {
                i3 = i8;
            }
        }
        if (c != 0) {
            cArr[i4 - 1] = c;
        }
    }

    public static void getChars(long j, int i, char[] cArr) {
        char c;
        int i2;
        int i3;
        if (j < 0) {
            c = '-';
            j = -j;
        } else {
            c = 0;
        }
        while (j > 2147483647L) {
            long j2 = j / 100;
            int i4 = (int) (j - (((j2 << 6) + (j2 << 5)) + (j2 << 2)));
            int i5 = i - 1;
            cArr[i5] = DigitOnes[i4];
            i = i5 - 1;
            cArr[i] = DigitTens[i4];
            j = j2;
        }
        int i6 = (int) j;
        while (true) {
            int i7 = i6;
            i2 = i7;
            i3 = i;
            if (i7 < 65536) {
                break;
            }
            int i8 = i7 / 100;
            int i9 = i7 - (((i8 << 6) + (i8 << 5)) + (i8 << 2));
            int i10 = i - 1;
            cArr[i10] = DigitOnes[i9];
            i = i10 - 1;
            cArr[i] = DigitTens[i9];
            i6 = i8;
        }
        while (true) {
            int i11 = (52429 * i2) >>> 19;
            i3--;
            cArr[i3] = digits[i2 - ((i11 << 3) + (i11 << 1))];
            if (i11 == 0) {
                break;
            } else {
                i2 = i11;
            }
        }
        if (c != 0) {
            cArr[i3 - 1] = c;
        }
    }

    public static String getStringProperty(String str) {
        String property;
        try {
            property = System.getProperty(str);
        } catch (SecurityException e) {
            property = null;
        }
        String property2 = property;
        if (property == null) {
            property2 = DEFAULT_PROPERTIES.getProperty(str);
        }
        return property2;
    }

    public static boolean isIdent(char c) {
        boolean[] zArr = identifierFlags;
        return c < zArr.length && zArr[c];
    }

    public static boolean isValidJsonpQueryParam(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt != '.' && !isIdent(cCharAt)) {
                return false;
            }
        }
        return true;
    }

    public static void loadPropertiesFromFile() throws IOException {
        InputStream inputStream = (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: com.alibaba.fastjson.util.IOUtils.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public InputStream run() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                return contextClassLoader != null ? contextClassLoader.getResourceAsStream(IOUtils.FASTJSON_PROPERTIES) : ClassLoader.getSystemResourceAsStream(IOUtils.FASTJSON_PROPERTIES);
            }
        });
        if (inputStream != null) {
            try {
                DEFAULT_PROPERTIES.load(inputStream);
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static String readAll(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            char[] cArr = new char[2048];
            while (true) {
                int i = reader.read(cArr, 0, 2048);
                if (i < 0) {
                    return sb.toString();
                }
                sb.append(cArr, 0, i);
            }
        } catch (Exception e) {
            throw new JSONException("read string from reader error", e);
        }
    }

    public static int stringSize(int i) {
        int i2 = 0;
        while (i > sizeTable[i2]) {
            i2++;
        }
        return i2 + 1;
    }

    public static int stringSize(long j) {
        long j2 = 10;
        for (int i = 1; i < 19; i++) {
            if (j < j2) {
                return i;
            }
            j2 *= 10;
        }
        return 19;
    }
}
