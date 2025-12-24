package com.alibaba.fastjson.util;

import java.lang.ref.SoftReference;
import java.nio.charset.CharsetDecoder;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/ThreadLocalCache.class */
public class ThreadLocalCache {
    static final boolean $assertionsDisabled = false;
    public static final int BYTES_CACH_INIT_SIZE = 1024;
    public static final int BYTES_CACH_INIT_SIZE_EXP = 10;
    public static final int BYTES_CACH_MAX_SIZE = 131072;
    public static final int BYTES_CACH_MAX_SIZE_EXP = 17;
    public static final int CHARS_CACH_INIT_SIZE = 1024;
    public static final int CHARS_CACH_INIT_SIZE_EXP = 10;
    public static final int CHARS_CACH_MAX_SIZE = 131072;
    public static final int CHARS_CACH_MAX_SIZE_EXP = 17;
    private static final ThreadLocal<SoftReference<char[]>> charsBufLocal = new ThreadLocal<>();
    private static final ThreadLocal<CharsetDecoder> decoderLocal = new ThreadLocal<>();
    private static final ThreadLocal<SoftReference<byte[]>> bytesBufLocal = new ThreadLocal<>();

    private static char[] allocate(int i) {
        if (i > 131072) {
            return new char[i];
        }
        char[] cArr = new char[getAllocateLengthExp(10, 17, i)];
        charsBufLocal.set(new SoftReference<>(cArr));
        return cArr;
    }

    private static byte[] allocateBytes(int i) {
        if (i > 131072) {
            return new byte[i];
        }
        byte[] bArr = new byte[getAllocateLengthExp(10, 17, i)];
        bytesBufLocal.set(new SoftReference<>(bArr));
        return bArr;
    }

    public static void clearBytes() {
        bytesBufLocal.set(null);
    }

    public static void clearChars() {
        charsBufLocal.set(null);
    }

    private static int getAllocateLengthExp(int i, int i2, int i3) {
        return (i3 >>> i) <= 0 ? 1 << i : 1 << (32 - Integer.numberOfLeadingZeros(i3 - 1));
    }

    public static byte[] getBytes(int i) {
        byte[] bArr;
        SoftReference<byte[]> softReference = bytesBufLocal.get();
        if (softReference != null && (bArr = softReference.get()) != null) {
            byte[] bArrAllocateBytes = bArr;
            if (bArr.length < i) {
                bArrAllocateBytes = allocateBytes(i);
            }
            return bArrAllocateBytes;
        }
        return allocateBytes(i);
    }

    public static char[] getChars(int i) {
        char[] cArr;
        SoftReference<char[]> softReference = charsBufLocal.get();
        if (softReference != null && (cArr = softReference.get()) != null) {
            char[] cArrAllocate = cArr;
            if (cArr.length < i) {
                cArrAllocate = allocate(i);
            }
            return cArrAllocate;
        }
        return allocate(i);
    }

    public static CharsetDecoder getUTF8Decoder() {
        CharsetDecoder charsetDecoder = decoderLocal.get();
        CharsetDecoder uTF8Decoder = charsetDecoder;
        if (charsetDecoder == null) {
            uTF8Decoder = new UTF8Decoder();
            decoderLocal.set(uTF8Decoder);
        }
        return uTF8Decoder;
    }
}
