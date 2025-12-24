package okio;

/* loaded from: classes-dex2jar.jar:okio/Utf8.class */
public final class Utf8 {
    private Utf8() {
    }

    public static long size(String str) {
        return size(str, 0, str.length());
    }

    public static long size(String str, int i, int i2) {
        long j;
        long j2;
        long j3;
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + i);
        }
        if (i2 < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
        }
        if (i2 > str.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
        }
        long j4 = 0;
        while (i < i2) {
            char cCharAt = str.charAt(i);
            if (cCharAt < 128) {
                j = j4;
                j2 = 1;
            } else {
                if (cCharAt < 2048) {
                    j3 = 2;
                } else if (cCharAt < 55296 || cCharAt > 57343) {
                    j3 = 3;
                } else {
                    int i3 = i + 1;
                    char cCharAt2 = i3 < i2 ? str.charAt(i3) : (char) 0;
                    if (cCharAt > 56319 || cCharAt2 < 56320 || cCharAt2 > 57343) {
                        j4++;
                        i = i3;
                    } else {
                        j4 += 4;
                        i += 2;
                    }
                }
                j = j4;
                j2 = j3;
            }
            j4 = j + j2;
            i++;
        }
        return j4;
    }
}
