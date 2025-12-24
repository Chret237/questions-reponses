package com.github.kiulian.downloader.cipher;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/SwapFunctionV1.class */
class SwapFunctionV1 implements CipherFunction {
    SwapFunctionV1() {
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFunction
    public char[] apply(char[] cArr, String str) throws NumberFormatException {
        int i = Integer.parseInt(str);
        char c = cArr[0];
        cArr[0] = cArr[i % cArr.length];
        cArr[i] = c;
        return cArr;
    }
}
