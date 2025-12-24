package okhttp3.internal.http2;

import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import okhttp3.internal.Util;
import okio.ByteString;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2.class */
public final class Http2 {
    static final byte FLAG_ACK = 1;
    static final byte FLAG_COMPRESSED = 32;
    static final byte FLAG_END_HEADERS = 4;
    static final byte FLAG_END_PUSH_PROMISE = 4;
    static final byte FLAG_END_STREAM = 1;
    static final byte FLAG_NONE = 0;
    static final byte FLAG_PADDED = 8;
    static final byte FLAG_PRIORITY = 32;
    static final int INITIAL_MAX_FRAME_SIZE = 16384;
    static final byte TYPE_CONTINUATION = 9;
    static final byte TYPE_DATA = 0;
    static final byte TYPE_GOAWAY = 7;
    static final byte TYPE_HEADERS = 1;
    static final byte TYPE_PING = 6;
    static final byte TYPE_PRIORITY = 2;
    static final byte TYPE_PUSH_PROMISE = 5;
    static final byte TYPE_RST_STREAM = 3;
    static final byte TYPE_SETTINGS = 4;
    static final byte TYPE_WINDOW_UPDATE = 8;
    static final ByteString CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
    private static final String[] FRAME_NAMES = {"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
    static final String[] FLAGS = new String[64];
    static final String[] BINARY = new String[256];

    static {
        int i;
        int i2 = 0;
        while (true) {
            String[] strArr = BINARY;
            if (i2 >= strArr.length) {
                break;
            }
            strArr[i2] = Util.format("%8s", Integer.toBinaryString(i2)).replace(' ', '0');
            i2++;
        }
        String[] strArr2 = FLAGS;
        strArr2[0] = BuildConfig.FLAVOR;
        strArr2[1] = "END_STREAM";
        int[] iArr = {1};
        strArr2[8] = "PADDED";
        for (int i3 = 0; i3 < 1; i3++) {
            int i4 = iArr[i3];
            FLAGS[i4 | 8] = FLAGS[i4] + "|PADDED";
        }
        String[] strArr3 = FLAGS;
        strArr3[4] = "END_HEADERS";
        strArr3[32] = "PRIORITY";
        strArr3[36] = "END_HEADERS|PRIORITY";
        int i5 = 0;
        while (true) {
            i = 0;
            if (i5 >= 3) {
                break;
            }
            int i6 = new int[]{4, 32, 36}[i5];
            for (int i7 = 0; i7 < 1; i7++) {
                int i8 = iArr[i7];
                int i9 = i8 | i6;
                FLAGS[i9] = FLAGS[i8] + '|' + FLAGS[i6];
                FLAGS[i9 | 8] = FLAGS[i8] + '|' + FLAGS[i6] + "|PADDED";
            }
            i5++;
        }
        while (true) {
            String[] strArr4 = FLAGS;
            if (i >= strArr4.length) {
                return;
            }
            if (strArr4[i] == null) {
                strArr4[i] = BINARY[i];
            }
            i++;
        }
    }

    private Http2() {
    }

    static String formatFlags(byte b, byte b2) {
        if (b2 == 0) {
            return BuildConfig.FLAVOR;
        }
        if (b != 2 && b != 3) {
            if (b == 4 || b == 6) {
                return b2 == 1 ? "ACK" : BINARY[b2];
            }
            if (b != 7 && b != 8) {
                String[] strArr = FLAGS;
                String str = b2 < strArr.length ? strArr[b2] : BINARY[b2];
                return (b != 5 || (b2 & 4) == 0) ? (b != 0 || (b2 & 32) == 0) ? str : str.replace("PRIORITY", "COMPRESSED") : str.replace("HEADERS", "PUSH_PROMISE");
            }
        }
        return BINARY[b2];
    }

    static String frameLog(boolean z, int i, int i2, byte b, byte b2) {
        String[] strArr = FRAME_NAMES;
        return Util.format("%s 0x%08x %5d %-13s %s", z ? "<<" : ">>", Integer.valueOf(i), Integer.valueOf(i2), b < strArr.length ? strArr[b] : Util.format("0x%02x", Byte.valueOf(b)), formatFlags(b, b2));
    }

    static IllegalArgumentException illegalArgument(String str, Object... objArr) {
        throw new IllegalArgumentException(Util.format(str, objArr));
    }

    static IOException ioException(String str, Object... objArr) throws IOException {
        throw new IOException(Util.format(str, objArr));
    }
}
