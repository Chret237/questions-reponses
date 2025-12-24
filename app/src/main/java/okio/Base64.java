package okio;

import java.io.UnsupportedEncodingException;

/* loaded from: classes-dex2jar.jar:okio/Base64.class */
final class Base64 {
    private static final byte[] MAP = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_MAP = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

    private Base64() {
    }

    public static byte[] decode(String str) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        char cCharAt;
        int length = str.length();
        while (length > 0 && ((cCharAt = str.charAt(length - 1)) == '=' || cCharAt == '\n' || cCharAt == '\r' || cCharAt == ' ' || cCharAt == '\t')) {
            length--;
        }
        int i6 = (int) ((length * 6) / 8);
        byte[] bArr = new byte[i6];
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (true) {
            int i11 = i10;
            if (i7 >= length) {
                int i12 = i8 % 4;
                if (i12 == 1) {
                    return null;
                }
                if (i12 == 2) {
                    bArr[i11] = (byte) ((i9 << 12) >> 16);
                    i = i11 + 1;
                } else {
                    i = i11;
                    if (i12 == 3) {
                        int i13 = i9 << 6;
                        int i14 = i11 + 1;
                        bArr[i11] = (byte) (i13 >> 16);
                        i = i14 + 1;
                        bArr[i14] = (byte) (i13 >> 8);
                    }
                }
                if (i == i6) {
                    return bArr;
                }
                byte[] bArr2 = new byte[i];
                System.arraycopy(bArr, 0, bArr2, 0, i);
                return bArr2;
            }
            char cCharAt2 = str.charAt(i7);
            if (cCharAt2 >= 'A' && cCharAt2 <= 'Z') {
                i2 = cCharAt2 - 'A';
            } else if (cCharAt2 >= 'a' && cCharAt2 <= 'z') {
                i2 = cCharAt2 - 'G';
            } else if (cCharAt2 >= '0' && cCharAt2 <= '9') {
                i2 = cCharAt2 + 4;
            } else if (cCharAt2 == '+' || cCharAt2 == '-') {
                i2 = 62;
            } else if (cCharAt2 == '/' || cCharAt2 == '_') {
                i2 = 63;
            } else {
                i3 = i8;
                i4 = i9;
                i5 = i11;
                if (cCharAt2 != '\n') {
                    i3 = i8;
                    i4 = i9;
                    i5 = i11;
                    if (cCharAt2 != '\r') {
                        i3 = i8;
                        i4 = i9;
                        i5 = i11;
                        if (cCharAt2 == ' ') {
                            continue;
                        } else {
                            if (cCharAt2 != '\t') {
                                return null;
                            }
                            i3 = i8;
                            i4 = i9;
                            i5 = i11;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                i7++;
                i8 = i3;
                i9 = i4;
                i10 = i5;
            }
            int i15 = (i9 << 6) | ((byte) i2);
            int i16 = i8 + 1;
            i3 = i16;
            i4 = i15;
            i5 = i11;
            if (i16 % 4 == 0) {
                int i17 = i11 + 1;
                bArr[i11] = (byte) (i15 >> 16);
                int i18 = i17 + 1;
                bArr[i17] = (byte) (i15 >> 8);
                bArr[i18] = (byte) i15;
                i5 = i18 + 1;
                i4 = i15;
                i3 = i16;
            }
            i7++;
            i8 = i3;
            i9 = i4;
            i10 = i5;
        }
    }

    public static String encode(byte[] bArr) {
        return encode(bArr, MAP);
    }

    private static String encode(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[((bArr.length + 2) / 3) * 4];
        int length = bArr.length - (bArr.length % 3);
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int i3 = i + 1;
            bArr3[i] = bArr2[(bArr[i2] & 255) >> 2];
            int i4 = i3 + 1;
            int i5 = i2 + 1;
            bArr3[i3] = bArr2[((bArr[i2] & 3) << 4) | ((bArr[i5] & 255) >> 4)];
            int i6 = i4 + 1;
            byte b = bArr[i5];
            int i7 = i2 + 2;
            bArr3[i4] = bArr2[((b & 15) << 2) | ((bArr[i7] & 255) >> 6)];
            i = i6 + 1;
            bArr3[i6] = bArr2[bArr[i7] & 63];
        }
        int length2 = bArr.length % 3;
        if (length2 == 1) {
            int i8 = i + 1;
            bArr3[i] = bArr2[(bArr[length] & 255) >> 2];
            int i9 = i8 + 1;
            bArr3[i8] = bArr2[(bArr[length] & 3) << 4];
            bArr3[i9] = 61;
            bArr3[i9 + 1] = 61;
        } else if (length2 == 2) {
            int i10 = i + 1;
            bArr3[i] = bArr2[(bArr[length] & 255) >> 2];
            int i11 = i10 + 1;
            int i12 = length + 1;
            bArr3[i10] = bArr2[((bArr[i12] & 255) >> 4) | ((bArr[length] & 3) << 4)];
            bArr3[i11] = bArr2[(bArr[i12] & 15) << 2];
            bArr3[i11 + 1] = 61;
        }
        try {
            return new String(bArr3, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeUrl(byte[] bArr) {
        return encode(bArr, URL_MAP);
    }
}
