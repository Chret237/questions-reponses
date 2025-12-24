package com.github.kiulian.downloader.cipher;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/ReverseFunction.class */
class ReverseFunction implements CipherFunction {
    ReverseFunction() {
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFunction
    public char[] apply(char[] cArr, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(cArr);
        return sb.reverse().toString().toCharArray();
    }
}
