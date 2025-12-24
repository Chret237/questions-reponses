package com.github.kiulian.downloader.cipher;

import com.github.kiulian.downloader.YoutubeException;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/CipherFactory.class */
public interface CipherFactory {
    void addFunctionEquivalent(String str, CipherFunction cipherFunction);

    void addInitialFunctionPattern(int i, String str);

    Cipher createCipher(String str) throws YoutubeException;
}
