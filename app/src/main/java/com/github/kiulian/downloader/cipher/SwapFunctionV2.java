package com.github.kiulian.downloader.cipher;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/SwapFunctionV2.class */
class SwapFunctionV2 implements CipherFunction {
    SwapFunctionV2() {
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFunction
    public char[] apply(char[] cArr, String str) throws NumberFormatException {
        int i = Integer.parseInt(str);
        char c = cArr[0];
        cArr[0] = cArr[i % cArr.length];
        cArr[i % cArr.length] = c;
        return cArr;
    }
}
