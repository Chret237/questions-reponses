package com.alibaba.fastjson.asm;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.ASMUtils;
import com.github.clans.fab.BuildConfig;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/asm/TypeCollector.class */
public class TypeCollector {
    private static String JSONType = ASMUtils.desc((Class<?>) JSONType.class);
    private static final Map<String, String> primitives = new HashMap<String, String>() { // from class: com.alibaba.fastjson.asm.TypeCollector.1
        {
            put("int", "I");
            put("boolean", "Z");
            put("byte", "B");
            put("char", "C");
            put("short", "S");
            put("float", "F");
            put("long", "J");
            put("double", "D");
        }
    };
    protected MethodCollector collector = null;
    protected boolean jsonType;
    private final String methodName;
    private final Class<?>[] parameterTypes;

    public TypeCollector(String str, Class<?>[] clsArr) {
        this.methodName = str;
        this.parameterTypes = clsArr;
    }

    private boolean correctTypeName(Type type, String str) {
        String className = type.getClassName();
        String str2 = BuildConfig.FLAVOR;
        while (className.endsWith("[]")) {
            str2 = str2 + "[";
            className = className.substring(0, className.length() - 2);
        }
        String str3 = className;
        if (!str2.equals(BuildConfig.FLAVOR)) {
            if (primitives.containsKey(className)) {
                str3 = str2 + primitives.get(className);
            } else {
                str3 = str2 + "L" + className + ";";
            }
        }
        return str3.equals(str);
    }

    public String[] getParameterNamesForMethod() {
        MethodCollector methodCollector = this.collector;
        return (methodCollector == null || !methodCollector.debugInfoPresent) ? new String[0] : this.collector.getResult().split(",");
    }

    public boolean hasJsonType() {
        return this.jsonType;
    }

    public boolean matched() {
        return this.collector != null;
    }

    public void visitAnnotation(String str) {
        if (JSONType.equals(str)) {
            this.jsonType = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.alibaba.fastjson.asm.MethodCollector visitMethod(int r7, java.lang.String r8, java.lang.String r9) {
        /*
            r6 = this;
            r0 = r6
            com.alibaba.fastjson.asm.MethodCollector r0 = r0.collector
            if (r0 == 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r8
            r1 = r6
            java.lang.String r1 = r1.methodName
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L16
            r0 = 0
            return r0
        L16:
            r0 = r9
            com.alibaba.fastjson.asm.Type[] r0 = com.alibaba.fastjson.asm.Type.getArgumentTypes(r0)
            r9 = r0
            r0 = r9
            int r0 = r0.length
            r14 = r0
            r0 = 0
            r13 = r0
            r0 = 0
            r12 = r0
            r0 = 0
            r10 = r0
        L28:
            r0 = r12
            r1 = r14
            if (r0 >= r1) goto L5d
            r0 = r9
            r1 = r12
            r0 = r0[r1]
            java.lang.String r0 = r0.getClassName()
            r8 = r0
            r0 = r8
            java.lang.String r1 = "long"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L4d
            r0 = r10
            r11 = r0
            r0 = r8
            java.lang.String r1 = "double"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L53
        L4d:
            r0 = r10
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
        L53:
            int r12 = r12 + 1
            r0 = r11
            r10 = r0
            goto L28
        L5d:
            r0 = r13
            r11 = r0
            r0 = r9
            int r0 = r0.length
            r1 = r6
            java.lang.Class<?>[] r1 = r1.parameterTypes
            int r1 = r1.length
            if (r0 == r1) goto L6d
            r0 = 0
            return r0
        L6d:
            r0 = r11
            r1 = r9
            int r1 = r1.length
            if (r0 >= r1) goto L91
            r0 = r6
            r1 = r9
            r2 = r11
            r1 = r1[r2]
            r2 = r6
            java.lang.Class<?>[] r2 = r2.parameterTypes
            r3 = r11
            r2 = r2[r3]
            java.lang.String r2 = r2.getName()
            boolean r0 = r0.correctTypeName(r1, r2)
            if (r0 != 0) goto L8b
            r0 = 0
            return r0
        L8b:
            int r11 = r11 + 1
            goto L6d
        L91:
            com.alibaba.fastjson.asm.MethodCollector r0 = new com.alibaba.fastjson.asm.MethodCollector
            r1 = r0
            r2 = r7
            boolean r2 = java.lang.reflect.Modifier.isStatic(r2)
            r3 = 1
            r2 = r2 ^ r3
            r3 = r9
            int r3 = r3.length
            r4 = r10
            int r3 = r3 + r4
            r1.<init>(r2, r3)
            r8 = r0
            r0 = r6
            r1 = r8
            r0.collector = r1
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.asm.TypeCollector.visitMethod(int, java.lang.String, java.lang.String):com.alibaba.fastjson.asm.MethodCollector");
    }
}
