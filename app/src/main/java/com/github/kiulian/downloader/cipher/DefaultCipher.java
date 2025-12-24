package com.github.kiulian.downloader.cipher;

import java.util.List;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/DefaultCipher.class */
public class DefaultCipher implements Cipher {
    private final List<JsFunction> functions;
    private final Map<String, CipherFunction> functionsMap;

    public DefaultCipher(List<JsFunction> list, Map<String, CipherFunction> map) {
        this.functionsMap = map;
        this.functions = list;
    }

    @Override // com.github.kiulian.downloader.cipher.Cipher
    public String getSignature(String str) {
        char[] charArray = str.toCharArray();
        for (JsFunction jsFunction : this.functions) {
            charArray = this.functionsMap.get(jsFunction.getName()).apply(charArray, jsFunction.getArgument());
        }
        return String.valueOf(charArray);
    }
}
