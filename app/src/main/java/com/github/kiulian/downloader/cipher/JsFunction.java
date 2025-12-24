package com.github.kiulian.downloader.cipher;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/JsFunction.class */
public class JsFunction {
    private final String argument;
    private final String name;
    private final String var;

    public JsFunction(String str, String str2, String str3) {
        this.var = str;
        this.name = str2;
        this.argument = str3;
    }

    public String getArgument() {
        return this.argument;
    }

    public String getName() {
        return this.name;
    }

    public String getVar() {
        return this.var;
    }
}
