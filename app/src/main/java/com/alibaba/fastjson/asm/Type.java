package com.alibaba.fastjson.asm;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/asm/Type.class */
public class Type {
    private final char[] buf;
    private final int len;
    private final int off;
    protected final int sort;
    public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);

    private Type(int i, char[] cArr, int i2, int i3) {
        this.sort = i;
        this.buf = cArr;
        this.off = i2;
        this.len = i3;
    }

    static Type[] getArgumentTypes(String str) {
        char[] charArray = str.toCharArray();
        int i = 1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            char c = charArray[i];
            if (c == ')') {
                break;
            }
            if (c == 'L') {
                while (true) {
                    i = i3 + 1;
                    if (charArray[i3] == ';') {
                        break;
                    }
                    i3 = i;
                }
                i2++;
            } else {
                int i4 = i2;
                if (c != '[') {
                    i4 = i2 + 1;
                }
                i = i3;
                i2 = i4;
            }
        }
        Type[] typeArr = new Type[i2];
        int i5 = 0;
        int i6 = 1;
        while (charArray[i6] != ')') {
            typeArr[i5] = getType(charArray, i6);
            i6 += typeArr[i5].len + (typeArr[i5].sort == 10 ? 2 : 0);
            i5++;
        }
        return typeArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getArgumentsAndReturnSizes(java.lang.String r3) {
        /*
            r0 = 1
            r7 = r0
            r0 = 1
            r5 = r0
            r0 = 1
            r4 = r0
        L7:
            r0 = r5
            r1 = 1
            int r0 = r0 + r1
            r6 = r0
            r0 = r3
            r1 = r5
            char r0 = r0.charAt(r1)
            r5 = r0
            r0 = r5
            r1 = 41
            if (r0 != r1) goto L3f
            r0 = r3
            r1 = r6
            char r0 = r0.charAt(r1)
            r6 = r0
            r0 = r6
            r1 = 86
            if (r0 != r1) goto L28
            r0 = 0
            r5 = r0
            goto L39
        L28:
            r0 = r6
            r1 = 68
            if (r0 == r1) goto L37
            r0 = r7
            r5 = r0
            r0 = r6
            r1 = 74
            if (r0 != r1) goto L39
        L37:
            r0 = 2
            r5 = r0
        L39:
            r0 = r4
            r1 = 2
            int r0 = r0 << r1
            r1 = r5
            r0 = r0 | r1
            return r0
        L3f:
            r0 = r5
            r1 = 76
            if (r0 != r1) goto L5e
        L45:
            r0 = r6
            r1 = 1
            int r0 = r0 + r1
            r5 = r0
            r0 = r3
            r1 = r6
            char r0 = r0.charAt(r1)
            r1 = 59
            if (r0 == r1) goto L58
            r0 = r5
            r6 = r0
            goto L45
        L58:
            int r4 = r4 + 1
            goto L7
        L5e:
            r0 = r5
            r1 = 68
            if (r0 == r1) goto L73
            r0 = r5
            r1 = 74
            if (r0 != r1) goto L6d
            goto L73
        L6d:
            int r4 = r4 + 1
            goto L76
        L73:
            int r4 = r4 + 2
        L76:
            r0 = r6
            r5 = r0
            goto L7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.asm.Type.getArgumentsAndReturnSizes(java.lang.String):int");
    }

    private int getDimensions() {
        int i = 1;
        while (this.buf[this.off + i] == '[') {
            i++;
        }
        return i;
    }

    public static Type getType(String str) {
        return getType(str.toCharArray(), 0);
    }

    private static Type getType(char[] cArr, int i) {
        int i2;
        char c = cArr[i];
        if (c == 'F') {
            return FLOAT_TYPE;
        }
        if (c == 'S') {
            return SHORT_TYPE;
        }
        if (c == 'V') {
            return VOID_TYPE;
        }
        if (c == 'I') {
            return INT_TYPE;
        }
        if (c == 'J') {
            return LONG_TYPE;
        }
        if (c == 'Z') {
            return BOOLEAN_TYPE;
        }
        if (c != '[') {
            switch (c) {
                case 'B':
                    return BYTE_TYPE;
                case 'C':
                    return CHAR_TYPE;
                case 'D':
                    return DOUBLE_TYPE;
                default:
                    int i3 = 1;
                    while (cArr[i + i3] != ';') {
                        i3++;
                    }
                    return new Type(10, cArr, i + 1, i3 - 1);
            }
        }
        int i4 = 1;
        while (true) {
            i2 = i + i4;
            if (cArr[i2] != '[') {
                break;
            }
            i4++;
        }
        int i5 = i4;
        if (cArr[i2] == 'L') {
            do {
                i4++;
                i5 = i4;
            } while (cArr[i + i4] != ';');
        }
        return new Type(9, cArr, i, i5 + 1);
    }

    protected String getClassName() {
        switch (this.sort) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuilder sb = new StringBuilder(getType(this.buf, this.off + getDimensions()).getClassName());
                for (int dimensions = getDimensions(); dimensions > 0; dimensions--) {
                    sb.append("[]");
                }
                return sb.toString();
            default:
                return new String(this.buf, this.off, this.len).replace('/', '.');
        }
    }

    String getDescriptor() {
        return new String(this.buf, this.off, this.len);
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }
}
