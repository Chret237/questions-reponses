package com.alibaba.fastjson;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/PropertyNamingStrategy.class */
public enum PropertyNamingStrategy {
    CamelCase,
    PascalCase,
    SnakeCase,
    KebabCase;

    /* renamed from: com.alibaba.fastjson.PropertyNamingStrategy$1 */
    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/PropertyNamingStrategy$1.class */
    static /* synthetic */ class C03961 {
        static final int[] $SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:15:0x003e
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                com.alibaba.fastjson.PropertyNamingStrategy[] r0 = com.alibaba.fastjson.PropertyNamingStrategy.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                com.alibaba.fastjson.PropertyNamingStrategy.C03961.$SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy = r0
                r0 = r4
                com.alibaba.fastjson.PropertyNamingStrategy r1 = com.alibaba.fastjson.PropertyNamingStrategy.SnakeCase     // Catch: java.lang.NoSuchFieldError -> L36
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L36
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L36
            L14:
                int[] r0 = com.alibaba.fastjson.PropertyNamingStrategy.C03961.$SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy     // Catch: java.lang.NoSuchFieldError -> L36 java.lang.NoSuchFieldError -> L3a
                com.alibaba.fastjson.PropertyNamingStrategy r1 = com.alibaba.fastjson.PropertyNamingStrategy.KebabCase     // Catch: java.lang.NoSuchFieldError -> L3a
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L3a
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L3a
            L1f:
                int[] r0 = com.alibaba.fastjson.PropertyNamingStrategy.C03961.$SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy     // Catch: java.lang.NoSuchFieldError -> L3a java.lang.NoSuchFieldError -> L3e
                com.alibaba.fastjson.PropertyNamingStrategy r1 = com.alibaba.fastjson.PropertyNamingStrategy.PascalCase     // Catch: java.lang.NoSuchFieldError -> L3e
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L3e
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L3e
            L2a:
                int[] r0 = com.alibaba.fastjson.PropertyNamingStrategy.C03961.$SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy     // Catch: java.lang.NoSuchFieldError -> L3e java.lang.NoSuchFieldError -> L42
                com.alibaba.fastjson.PropertyNamingStrategy r1 = com.alibaba.fastjson.PropertyNamingStrategy.CamelCase     // Catch: java.lang.NoSuchFieldError -> L42
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L42
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L42
            L35:
                return
            L36:
                r4 = move-exception
                goto L14
            L3a:
                r4 = move-exception
                goto L1f
            L3e:
                r4 = move-exception
                goto L2a
            L42:
                r4 = move-exception
                goto L35
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.PropertyNamingStrategy.C03961.m293clinit():void");
        }
    }

    public String translate(String str) {
        int i = C03961.$SwitchMap$com$alibaba$fastjson$PropertyNamingStrategy[ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < str.length(); i2++) {
                char cCharAt = str.charAt(i2);
                if (cCharAt < 'A' || cCharAt > 'Z') {
                    sb.append(cCharAt);
                } else {
                    char c = (char) (cCharAt + ' ');
                    if (i2 > 0) {
                        sb.append('_');
                    }
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            for (int i3 = 0; i3 < str.length(); i3++) {
                char cCharAt2 = str.charAt(i3);
                if (cCharAt2 < 'A' || cCharAt2 > 'Z') {
                    sb2.append(cCharAt2);
                } else {
                    char c2 = (char) (cCharAt2 + ' ');
                    if (i3 > 0) {
                        sb2.append('-');
                    }
                    sb2.append(c2);
                }
            }
            return sb2.toString();
        }
        if (i == 3) {
            char cCharAt3 = str.charAt(0);
            if (cCharAt3 < 'a' || cCharAt3 > 'z') {
                return str;
            }
            char[] charArray = str.toCharArray();
            charArray[0] = (char) (charArray[0] - ' ');
            return new String(charArray);
        }
        if (i != 4) {
            return str;
        }
        char cCharAt4 = str.charAt(0);
        if (cCharAt4 < 'A' || cCharAt4 > 'Z') {
            return str;
        }
        char[] charArray2 = str.toCharArray();
        charArray2[0] = (char) (charArray2[0] + ' ');
        return new String(charArray2);
    }
}
