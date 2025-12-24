package com.github.kiulian.downloader.cipher;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/SpliceFunction.class */
class SpliceFunction implements CipherFunction {
    SpliceFunction() {
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFunction
    public char[] apply(char[] cArr, String str) throws NumberFormatException {
        int i = Integer.parseInt(str);
        int length = cArr.length - i;
        char[] cArr2 = new char[length];
        System.arraycopy(cArr, 0, cArr2, 0, i);
        System.arraycopy(cArr, i * 2, cArr2, i, length - i);
        return cArr2;
    }
}
