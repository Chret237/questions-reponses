package com.alibaba.fastjson.util;

import com.alibaba.fastjson.asm.Opcodes;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/UTF8Decoder.class */
public class UTF8Decoder extends CharsetDecoder {
    private static final Charset charset = Charset.forName("UTF-8");

    public UTF8Decoder() {
        super(charset, 1.0f, 1.0f);
    }

    private CoderResult decodeArrayLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        byte[] bArrArray = byteBuffer.array();
        int iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
        int iArrayOffset2 = byteBuffer.arrayOffset() + byteBuffer.limit();
        char[] cArrArray = charBuffer.array();
        int iArrayOffset3 = charBuffer.arrayOffset() + charBuffer.position();
        int iArrayOffset4 = charBuffer.arrayOffset() + charBuffer.limit();
        int iMin = Math.min(iArrayOffset2 - iArrayOffset, iArrayOffset4 - iArrayOffset3);
        int i = iArrayOffset3;
        while (i < iMin + iArrayOffset3 && bArrArray[iArrayOffset] >= 0) {
            cArrArray[i] = (char) bArrArray[iArrayOffset];
            i++;
            iArrayOffset++;
        }
        while (iArrayOffset < iArrayOffset2) {
            byte b = bArrArray[iArrayOffset];
            if (b >= 0) {
                if (i >= iArrayOffset4) {
                    return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, i, 1);
                }
                cArrArray[i] = (char) b;
                iArrayOffset++;
                i++;
            } else if ((b >> 5) == -2) {
                if (iArrayOffset2 - iArrayOffset < 2 || i >= iArrayOffset4) {
                    return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, i, 2);
                }
                byte b2 = bArrArray[iArrayOffset + 1];
                if (isMalformed2(b, b2)) {
                    return malformed(byteBuffer, iArrayOffset, charBuffer, i, 2);
                }
                cArrArray[i] = (char) (((b << 6) ^ b2) ^ 3968);
                iArrayOffset += 2;
                i++;
            } else if ((b >> 4) == -2) {
                if (iArrayOffset2 - iArrayOffset < 3 || i >= iArrayOffset4) {
                    return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, i, 3);
                }
                byte b3 = bArrArray[iArrayOffset + 1];
                byte b4 = bArrArray[iArrayOffset + 2];
                if (isMalformed3(b, b3, b4)) {
                    return malformed(byteBuffer, iArrayOffset, charBuffer, i, 3);
                }
                cArrArray[i] = (char) ((((b << 12) ^ (b3 << 6)) ^ b4) ^ 8064);
                iArrayOffset += 3;
                i++;
            } else {
                if ((b >> 3) != -2) {
                    return malformed(byteBuffer, iArrayOffset, charBuffer, i, 1);
                }
                if (iArrayOffset2 - iArrayOffset < 4 || iArrayOffset4 - i < 2) {
                    return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, i, 4);
                }
                byte b5 = bArrArray[iArrayOffset + 1];
                byte b6 = bArrArray[iArrayOffset + 2];
                byte b7 = bArrArray[iArrayOffset + 3];
                int i2 = ((b & 7) << 18) | ((b5 & 63) << 12) | ((b6 & 63) << 6) | (b7 & 63);
                if (isMalformed4(b5, b6, b7) || i2 < 65536 || i2 > 1114111) {
                    return malformed(byteBuffer, iArrayOffset, charBuffer, i, 4);
                }
                int i3 = i + 1;
                int i4 = i2 - 65536;
                cArrArray[i] = (char) (((i4 >> 10) & 1023) | 55296);
                i = i3 + 1;
                cArrArray[i3] = (char) ((i4 & 1023) | 56320);
                iArrayOffset += 4;
            }
        }
        return xflow(byteBuffer, iArrayOffset, iArrayOffset2, charBuffer, i, 0);
    }

    private static boolean isMalformed2(int i, int i2) {
        return (i & 30) == 0 || (i2 & Opcodes.CHECKCAST) != 128;
    }

    private static boolean isMalformed3(int i, int i2, int i3) {
        return ((i != -32 || (i2 & 224) != 128) && (i2 & Opcodes.CHECKCAST) == 128 && (i3 & Opcodes.CHECKCAST) == 128) ? false : true;
    }

    private static boolean isMalformed4(int i, int i2, int i3) {
        return ((i & Opcodes.CHECKCAST) == 128 && (i2 & Opcodes.CHECKCAST) == 128 && (i3 & Opcodes.CHECKCAST) == 128) ? false : true;
    }

    private static boolean isNotContinuation(int i) {
        return (i & Opcodes.CHECKCAST) != 128;
    }

    private static CoderResult lookupN(ByteBuffer byteBuffer, int i) {
        for (int i2 = 1; i2 < i; i2++) {
            if (isNotContinuation(byteBuffer.get())) {
                return CoderResult.malformedForLength(i2);
            }
        }
        return CoderResult.malformedForLength(i);
    }

    private static CoderResult malformed(ByteBuffer byteBuffer, int i, CharBuffer charBuffer, int i2, int i3) {
        byteBuffer.position(i - byteBuffer.arrayOffset());
        CoderResult coderResultMalformedN = malformedN(byteBuffer, i3);
        byteBuffer.position(i);
        charBuffer.position(i2);
        return coderResultMalformedN;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.nio.charset.CoderResult malformedN(java.nio.ByteBuffer r3, int r4) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.UTF8Decoder.malformedN(java.nio.ByteBuffer, int):java.nio.charset.CoderResult");
    }

    private static CoderResult xflow(Buffer buffer, int i, int i2, Buffer buffer2, int i3, int i4) {
        buffer.position(i);
        buffer2.position(i3);
        return (i4 == 0 || i2 - i < i4) ? CoderResult.UNDERFLOW : CoderResult.OVERFLOW;
    }

    @Override // java.nio.charset.CharsetDecoder
    protected CoderResult decodeLoop(ByteBuffer byteBuffer, CharBuffer charBuffer) {
        return decodeArrayLoop(byteBuffer, charBuffer);
    }
}
