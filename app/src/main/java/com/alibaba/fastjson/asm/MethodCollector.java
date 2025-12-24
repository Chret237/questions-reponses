package com.alibaba.fastjson.asm;

import com.github.clans.fab.BuildConfig;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/asm/MethodCollector.class */
public class MethodCollector {
    protected boolean debugInfoPresent;
    private final int ignoreCount;
    private final int paramCount;
    private final StringBuilder result = new StringBuilder();
    private int currentParameter = 0;

    protected MethodCollector(int i, int i2) {
        this.ignoreCount = i;
        this.paramCount = i2;
        boolean z = false;
        this.debugInfoPresent = i2 == 0 ? true : z;
    }

    protected String getResult() {
        return this.result.length() != 0 ? this.result.substring(1) : BuildConfig.FLAVOR;
    }

    protected void visitLocalVariable(String str, int i) {
        int i2 = this.ignoreCount;
        if (i < i2 || i >= i2 + this.paramCount) {
            return;
        }
        if (!str.equals("arg" + this.currentParameter)) {
            this.debugInfoPresent = true;
        }
        this.result.append(',');
        this.result.append(str);
        this.currentParameter++;
    }
}
