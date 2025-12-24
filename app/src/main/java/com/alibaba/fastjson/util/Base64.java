package com.alibaba.fastjson.util;

import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/Base64.class */
public class Base64 {

    /* renamed from: CA */
    public static final char[] f72CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /* renamed from: IA */
    public static final int[] f73IA;

    static {
        int[] iArr = new int[256];
        f73IA = iArr;
        Arrays.fill(iArr, -1);
        int length = f72CA.length;
        for (int i = 0; i < length; i++) {
            f73IA[f72CA[i]] = i;
        }
        f73IA[61] = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0177 A[PHI: r8
  0x0177: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x015e, B:38:0x016b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeFast(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 470
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.Base64.decodeFast(java.lang.String):byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0178 A[PHI: r8
  0x0178: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x0162, B:38:0x016d] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeFast(java.lang.String r6, int r7, int r8) {
        /*
            Method dump skipped, instructions count: 467
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.Base64.decodeFast(java.lang.String, int, int):byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0157 A[PHI: r8
  0x0157: PHI (r8v6 int) = (r8v5 int), (r8v8 int) binds: [B:36:0x0141, B:38:0x014c] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decodeFast(char[] r6, int r7, int r8) {
        /*
            Method dump skipped, instructions count: 432
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.Base64.decodeFast(char[], int, int):byte[]");
    }
}
