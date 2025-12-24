package com.alibaba.fastjson.parser;

import android.support.v4.internal.view.SupportMenu;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.TimeZone;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/JSONScanner.class */
public final class JSONScanner extends JSONLexerBase {
    private final int len;
    private final String text;

    public JSONScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String str, int i) {
        super(i);
        this.text = str;
        this.len = str.length();
        this.f68bp = -1;
        next();
        if (this.f69ch == 65279) {
            next();
        }
    }

    public JSONScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] cArr, int i, int i2) {
        this(new String(cArr, 0, i), i2);
    }

    static boolean charArrayCompare(String str, int i, char[] cArr) {
        int length = cArr.length;
        if (length + i > str.length()) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != str.charAt(i + i2)) {
                return false;
            }
        }
        return true;
    }

    static boolean checkDate(char c, char c2, char c3, char c4, char c5, char c6, int i, int i2) {
        if (c < '0' || c > '9' || c2 < '0' || c2 > '9' || c3 < '0' || c3 > '9' || c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 == '0') {
            if (c6 < '1' || c6 > '9') {
                return false;
            }
        } else {
            if (c5 != '1') {
                return false;
            }
            if (c6 != '0' && c6 != '1' && c6 != '2') {
                return false;
            }
        }
        if (i == 48) {
            return i2 >= 49 && i2 <= 57;
        }
        if (i == 49 || i == 50) {
            return i2 >= 48 && i2 <= 57;
        }
        if (i == 51) {
            return i2 == 48 || i2 == 49;
        }
        return false;
    }

    private boolean checkTime(char c, char c2, char c3, char c4, char c5, char c6) {
        if (c == '0') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c == '1') {
            if (c2 < '0' || c2 > '9') {
                return false;
            }
        } else if (c != '2' || c2 < '0' || c2 > '4') {
            return false;
        }
        if (c3 < '0' || c3 > '5') {
            if (c3 != '6' || c4 != '0') {
                return false;
            }
        } else if (c4 < '0' || c4 > '9') {
            return false;
        }
        return (c5 < '0' || c5 > '5') ? c5 == '6' && c6 == '0' : c6 >= '0' && c6 <= '9';
    }

    /* JADX WARN: Removed duplicated region for block: B:146:0x03bc A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x03be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean scanISO8601DateIfMatch(boolean r11, int r12) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 3151
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanISO8601DateIfMatch(boolean, int):boolean");
    }

    private void setCalendar(char c, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((c - '0') * 1000) + ((c2 - '0') * 100) + ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(2, (((c5 - '0') * 10) + (c6 - '0')) - 1);
        this.calendar.set(5, ((c7 - '0') * 10) + (c8 - '0'));
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, i, i2, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        this.text.getChars(i, i3 + i, cArr, i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public byte[] bytesValue() {
        if (this.token != 26) {
            return !this.hasSpecial ? IOUtils.decodeBase64(this.text, this.f70np + 1, this.f71sp) : IOUtils.decodeBase64(new String(this.sbuf, 0, this.f71sp));
        }
        int i = this.f70np;
        int i2 = this.f71sp;
        if (i2 % 2 != 0) {
            throw new JSONException("illegal state. " + i2);
        }
        int i3 = i2 / 2;
        byte[] bArr = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (i4 * 2) + i + 1;
            char cCharAt = this.text.charAt(i5);
            char cCharAt2 = this.text.charAt(i5 + 1);
            char c = '0';
            char c2 = cCharAt <= '9' ? '0' : '7';
            if (cCharAt2 > '9') {
                c = '7';
            }
            bArr[i4] = (byte) (((cCharAt - c2) << 4) | (cCharAt2 - c));
        }
        return bArr;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final boolean charArrayCompare(char[] cArr) {
        return charArrayCompare(this.text, this.f68bp, cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char charAt(int i) {
        if (i >= this.len) {
            return (char) 26;
        }
        return this.text.charAt(i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void copyTo(int i, int i2, char[] cArr) {
        this.text.getChars(i, i2 + i, cArr, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0035  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.math.BigDecimal decimalValue() {
        /*
            r6 = this;
            r0 = r6
            r1 = r6
            int r1 = r1.f70np
            r2 = r6
            int r2 = r2.f71sp
            int r1 = r1 + r2
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r9 = r0
            r0 = r6
            int r0 = r0.f71sp
            r8 = r0
            r0 = r9
            r1 = 76
            if (r0 == r1) goto L35
            r0 = r9
            r1 = 83
            if (r0 == r1) goto L35
            r0 = r9
            r1 = 66
            if (r0 == r1) goto L35
            r0 = r9
            r1 = 70
            if (r0 == r1) goto L35
            r0 = r8
            r7 = r0
            r0 = r9
            r1 = 68
            if (r0 != r1) goto L39
        L35:
            r0 = r8
            r1 = 1
            int r0 = r0 - r1
            r7 = r0
        L39:
            r0 = r6
            int r0 = r0.f70np
            r8 = r0
            r0 = r7
            r1 = r6
            char[] r1 = r1.sbuf
            int r1 = r1.length
            if (r0 >= r1) goto L65
            r0 = r6
            java.lang.String r0 = r0.text
            r1 = r8
            r2 = r8
            r3 = r7
            int r2 = r2 + r3
            r3 = r6
            char[] r3 = r3.sbuf
            r4 = 0
            r0.getChars(r1, r2, r3, r4)
            java.math.BigDecimal r0 = new java.math.BigDecimal
            r1 = r0
            r2 = r6
            char[] r2 = r2.sbuf
            r3 = 0
            r4 = r7
            r1.<init>(r2, r3, r4)
            return r0
        L65:
            r0 = r7
            char[] r0 = new char[r0]
            r10 = r0
            r0 = r6
            java.lang.String r0 = r0.text
            r1 = r8
            r2 = r7
            r3 = r8
            int r2 = r2 + r3
            r3 = r10
            r4 = 0
            r0.getChars(r1, r2, r3, r4)
            java.math.BigDecimal r0 = new java.math.BigDecimal
            r1 = r0
            r2 = r10
            r1.<init>(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.decimalValue():java.math.BigDecimal");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final int indexOf(char c, int i) {
        return this.text.indexOf(c, i);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int i2 = 1;
        int i3 = 1;
        while (i < this.f68bp) {
            int i4 = i2;
            if (this.text.charAt(i) == '\n') {
                i4 = i2 + 1;
                i3 = 1;
            }
            i++;
            i3++;
            i2 = i4;
        }
        sb.append("pos ");
        sb.append(this.f68bp);
        sb.append(", line ");
        sb.append(i2);
        sb.append(", column ");
        sb.append(i3);
        if (this.text.length() < 65535) {
            sb.append(this.text);
        } else {
            sb.append(this.text.substring(0, SupportMenu.USER_MASK));
        }
        return sb.toString();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean isEOF() {
        boolean z = true;
        if (this.f68bp != this.len) {
            z = this.f69ch == 26 && this.f68bp + 1 >= this.len;
        }
        return z;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean matchField2(char[] cArr) {
        while (isWhitespace(this.f69ch)) {
            next();
        }
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = this.f68bp + cArr.length;
        int i = length + 1;
        char cCharAt = this.text.charAt(length);
        while (isWhitespace(cCharAt)) {
            cCharAt = this.text.charAt(i);
            i++;
        }
        if (cCharAt != ':') {
            this.matchStat = -2;
            return false;
        }
        this.f68bp = i;
        this.f69ch = charAt(this.f68bp);
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public Collection<String> newCollectionByType(Class<?> cls) {
        if (cls.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (cls.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        try {
            return (Collection) cls.newInstance();
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final char next() {
        int i = this.f68bp + 1;
        this.f68bp = i;
        char cCharAt = i >= this.len ? (char) 26 : this.text.charAt(i);
        this.f69ch = cCharAt;
        return cCharAt;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0035  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String numberString() {
        /*
            r4 = this;
            r0 = r4
            r1 = r4
            int r1 = r1.f70np
            r2 = r4
            int r2 = r2.f71sp
            int r1 = r1 + r2
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r7 = r0
            r0 = r4
            int r0 = r0.f71sp
            r6 = r0
            r0 = r7
            r1 = 76
            if (r0 == r1) goto L35
            r0 = r7
            r1 = 83
            if (r0 == r1) goto L35
            r0 = r7
            r1 = 66
            if (r0 == r1) goto L35
            r0 = r7
            r1 = 70
            if (r0 == r1) goto L35
            r0 = r6
            r5 = r0
            r0 = r7
            r1 = 68
            if (r0 != r1) goto L39
        L35:
            r0 = r6
            r1 = 1
            int r0 = r0 - r1
            r5 = r0
        L39:
            r0 = r4
            r1 = r4
            int r1 = r1.f70np
            r2 = r5
            java.lang.String r0 = r0.subString(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.numberString():java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x01b2  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Date scanDate(char r6) {
        /*
            Method dump skipped, instructions count: 713
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanDate(char):java.util.Date");
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x0209  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x022c -> B:60:0x01b5). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double scanDouble(char r6) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 995
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanDouble(char):double");
    }

    /* JADX WARN: Code restructure failed: missing block: B:103:0x0298, code lost:
    
        return r12;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x01de A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0201  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean scanFieldBoolean(char[] r5) {
        /*
            Method dump skipped, instructions count: 757
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldBoolean(char[]):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0176  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Date scanFieldDate(char[] r6) {
        /*
            Method dump skipped, instructions count: 658
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldDate(char[]):java.util.Date");
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00eb, code lost:
    
        if (r0 != '.') goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ee, code lost:
    
        r4.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00f4, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00f7, code lost:
    
        if (r11 >= 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00fa, code lost:
    
        r4.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0100, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0101, code lost:
    
        r10 = r11;
        r13 = r9;
        r15 = r0;
        r6 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0111, code lost:
    
        if (r12 == false) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0117, code lost:
    
        if (r0 == '\"') goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x011a, code lost:
    
        r4.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0120, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0121, code lost:
    
        r0 = r0 + 1;
        r6 = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x012e, code lost:
    
        r15 = r0;
        r13 = r9;
        r10 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x013d, code lost:
    
        if (r6 == ',') goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0143, code lost:
    
        if (r6 != '}') goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x014d, code lost:
    
        if (isWhitespace(r6) == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0150, code lost:
    
        r0 = r15 + 1;
        r6 = charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x016c, code lost:
    
        r4.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0172, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0173, code lost:
    
        r0 = r15 - 1;
        r4.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0182, code lost:
    
        if (r6 != ',') goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0185, code lost:
    
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r4.f69ch = charAt(r0);
        r4.matchStat = 3;
        r4.token = 16;
        r9 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01ae, code lost:
    
        if (r13 == false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01b1, code lost:
    
        r9 = -r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01b8, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01bc, code lost:
    
        if (r6 != '}') goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01bf, code lost:
    
        r4.f68bp = r0;
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r0 = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01da, code lost:
    
        r6 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01dd, code lost:
    
        if (r6 != ',') goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01e0, code lost:
    
        r4.token = 16;
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r4.f69ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0204, code lost:
    
        if (r6 != ']') goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0207, code lost:
    
        r4.token = 15;
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r4.f69ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x022b, code lost:
    
        if (r6 != '}') goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x022e, code lost:
    
        r4.token = 13;
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r4.f69ch = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0252, code lost:
    
        if (r6 != 26) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0255, code lost:
    
        r4.token = 20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x025b, code lost:
    
        r4.matchStat = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0267, code lost:
    
        if (isWhitespace(r6) == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x026a, code lost:
    
        r0 = r4.f68bp + 1;
        r4.f68bp = r0;
        r0 = charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0282, code lost:
    
        r4.f68bp = r0;
        r4.f69ch = r0;
        r4.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0294, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0295, code lost:
    
        r9 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x029b, code lost:
    
        if (r13 == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x029e, code lost:
    
        r9 = -r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02a5, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x012e, code lost:
    
        r15 = r0;
        r13 = r13;
        r10 = r10;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int scanFieldInt(char[] r5) {
        /*
            Method dump skipped, instructions count: 685
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldInt(char[]):int");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldLong(char[] cArr) {
        boolean z;
        int i;
        char cCharAt;
        this.matchStat = 0;
        int i2 = this.f68bp;
        char c = this.f69ch;
        if (!charArrayCompare(this.text, this.f68bp, cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = this.f68bp + cArr.length;
        int i3 = length + 1;
        char cCharAt2 = charAt(length);
        boolean z2 = cCharAt2 == '\"';
        int i4 = i3;
        if (z2) {
            cCharAt2 = charAt(i3);
            i4 = i3 + 1;
        }
        if (cCharAt2 == '-') {
            cCharAt2 = charAt(i4);
            z = true;
            i4++;
        } else {
            z = false;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            this.f68bp = i2;
            this.f69ch = c;
            this.matchStat = -1;
            return 0L;
        }
        long j = cCharAt2 - '0';
        while (true) {
            i = i4 + 1;
            cCharAt = charAt(i4);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j = (j * 10) + (cCharAt - '0');
            i4 = i;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        char cCharAt3 = cCharAt;
        int i5 = i;
        if (z2) {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0L;
            }
            cCharAt3 = charAt(i);
            i5 = i + 1;
        }
        if (cCharAt3 == ',' || cCharAt3 == '}') {
            this.f68bp = i5 - 1;
        }
        if (!(j >= 0 || (j == Long.MIN_VALUE && z))) {
            this.f68bp = i2;
            this.f69ch = c;
            this.matchStat = -1;
            return 0L;
        }
        while (cCharAt3 != ',') {
            if (cCharAt3 == '}') {
                int i6 = this.f68bp + 1;
                this.f68bp = i6;
                char cCharAt4 = charAt(i6);
                while (true) {
                    char c2 = cCharAt4;
                    if (c2 == ',') {
                        this.token = 16;
                        int i7 = this.f68bp + 1;
                        this.f68bp = i7;
                        this.f69ch = charAt(i7);
                        break;
                    }
                    if (c2 == ']') {
                        this.token = 15;
                        int i8 = this.f68bp + 1;
                        this.f68bp = i8;
                        this.f69ch = charAt(i8);
                        break;
                    }
                    if (c2 == '}') {
                        this.token = 13;
                        int i9 = this.f68bp + 1;
                        this.f68bp = i9;
                        this.f69ch = charAt(i9);
                        break;
                    }
                    if (c2 == 26) {
                        this.token = 20;
                        break;
                    }
                    if (!isWhitespace(c2)) {
                        this.f68bp = i2;
                        this.f69ch = c;
                        this.matchStat = -1;
                        return 0L;
                    }
                    int i10 = this.f68bp + 1;
                    this.f68bp = i10;
                    cCharAt4 = charAt(i10);
                }
                this.matchStat = 4;
                long j2 = j;
                if (z) {
                    j2 = -j;
                }
                return j2;
            }
            if (!isWhitespace(cCharAt3)) {
                this.matchStat = -1;
                return 0L;
            }
            this.f68bp = i5;
            cCharAt3 = charAt(i5);
            i5++;
        }
        int i11 = this.f68bp + 1;
        this.f68bp = i11;
        this.f69ch = charAt(i11);
        this.matchStat = 3;
        this.token = 16;
        long j3 = j;
        if (z) {
            j3 = -j;
        }
        return j3;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String scanFieldString(char[] cArr) {
        char c;
        this.matchStat = 0;
        int i = this.f68bp;
        char c2 = this.f69ch;
        while (!charArrayCompare(this.text, this.f68bp, cArr)) {
            if (!isWhitespace(this.f69ch)) {
                this.matchStat = -2;
                return stringDefaultValue();
            }
            next();
        }
        int length = this.f68bp + cArr.length;
        int i2 = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int iIndexOf = indexOf('\"', i2);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        String strSubString = subString(i2, iIndexOf - i2);
        int i3 = iIndexOf;
        String string = strSubString;
        if (strSubString.indexOf(92) != -1) {
            int iIndexOf2 = iIndexOf;
            while (true) {
                i3 = iIndexOf2;
                int i4 = 0;
                for (int i5 = i3 - 1; i5 >= 0 && charAt(i5) == '\\'; i5--) {
                    i4++;
                }
                if (i4 % 2 == 0) {
                    break;
                }
                iIndexOf2 = indexOf('\"', i3 + 1);
            }
            int length2 = i3 - ((this.f68bp + cArr.length) + 1);
            string = readString(sub_chars(this.f68bp + cArr.length + 1, length2), length2);
        }
        char cCharAt = charAt(i3 + 1);
        while (true) {
            c = cCharAt;
            if (c == ',' || c == '}') {
                break;
            }
            if (!isWhitespace(c)) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            i3++;
            cCharAt = charAt(i3 + 1);
        }
        this.f68bp = i3 + 1;
        this.f69ch = c;
        if (c == ',') {
            int i6 = this.f68bp + 1;
            this.f68bp = i6;
            this.f69ch = charAt(i6);
            this.matchStat = 3;
            return string;
        }
        int i7 = this.f68bp + 1;
        this.f68bp = i7;
        char cCharAt2 = charAt(i7);
        if (cCharAt2 == ',') {
            this.token = 16;
            int i8 = this.f68bp + 1;
            this.f68bp = i8;
            this.f69ch = charAt(i8);
        } else if (cCharAt2 == ']') {
            this.token = 15;
            int i9 = this.f68bp + 1;
            this.f68bp = i9;
            this.f69ch = charAt(i9);
        } else if (cCharAt2 == '}') {
            this.token = 13;
            int i10 = this.f68bp + 1;
            this.f68bp = i10;
            this.f69ch = charAt(i10);
        } else {
            if (cCharAt2 != 26) {
                this.f68bp = i;
                this.f69ch = c2;
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.token = 20;
        }
        this.matchStat = 4;
        return string;
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x01a0, code lost:
    
        if (r10 != ']') goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01a9, code lost:
    
        if (r0.size() != 0) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01ac, code lost:
    
        r10 = r11 + 1;
        r12 = charAt(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01bf, code lost:
    
        r5.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01c5, code lost:
    
        return null;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r6, java.lang.Class<?> r7) {
        /*
            Method dump skipped, instructions count: 812
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        int i2;
        char c;
        int i3 = this.f68bp;
        char c2 = this.f69ch;
        while (isWhitespace(this.f69ch)) {
            next();
        }
        if (cArr != null) {
            this.matchStat = 0;
            if (!charArrayCompare(cArr)) {
                this.matchStat = -2;
                return null;
            }
            int length = this.f68bp + cArr.length;
            int i4 = length + 1;
            char cCharAt = this.text.charAt(length);
            while (isWhitespace(cCharAt)) {
                cCharAt = this.text.charAt(i4);
                i4++;
            }
            if (cCharAt != ':') {
                this.matchStat = -1;
                return null;
            }
            char cCharAt2 = this.text.charAt(i4);
            int i5 = i4 + 1;
            while (true) {
                i2 = i5;
                c = cCharAt2;
                if (!isWhitespace(cCharAt2)) {
                    break;
                }
                cCharAt2 = this.text.charAt(i5);
                i5++;
            }
        } else {
            i2 = this.f68bp + 1;
            c = this.f69ch;
        }
        if (c != '[') {
            if (c != 'n' || !this.text.startsWith("ull", this.f68bp + 1)) {
                this.matchStat = -1;
                return null;
            }
            this.f68bp += 4;
            this.f69ch = this.text.charAt(this.f68bp);
            return null;
        }
        this.f68bp = i2;
        this.f69ch = this.text.charAt(this.f68bp);
        String[] strArr = i >= 0 ? new String[i] : new String[4];
        int i6 = 0;
        String[] strArr2 = strArr;
        while (true) {
            if (isWhitespace(this.f69ch)) {
                next();
            } else {
                if (this.f69ch != '\"') {
                    this.f68bp = i3;
                    this.f69ch = c2;
                    this.matchStat = -1;
                    return null;
                }
                String strScanSymbol = scanSymbol(symbolTable, '\"');
                String[] strArr3 = strArr2;
                if (i6 == strArr2.length) {
                    strArr3 = new String[strArr2.length + (strArr2.length >> 1) + 1];
                    System.arraycopy(strArr2, 0, strArr3, 0, strArr2.length);
                }
                int i7 = i6 + 1;
                strArr3[i6] = strScanSymbol;
                while (isWhitespace(this.f69ch)) {
                    next();
                }
                if (this.f69ch != ',') {
                    String[] strArr4 = strArr3;
                    if (strArr3.length != i7) {
                        strArr4 = new String[i7];
                        System.arraycopy(strArr3, 0, strArr4, 0, i7);
                    }
                    while (isWhitespace(this.f69ch)) {
                        next();
                    }
                    if (this.f69ch == ']') {
                        next();
                        return strArr4;
                    }
                    this.f68bp = i3;
                    this.f69ch = c2;
                    this.matchStat = -1;
                    return null;
                }
                next();
                i6 = i7;
                strArr2 = strArr3;
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.f68bp, cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = this.f68bp + cArr.length;
        int i = length + 1;
        if (charAt(length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = -3750763034362895579L;
        while (true) {
            int i2 = i + 1;
            char cCharAt = charAt(i);
            if (cCharAt == '\"') {
                this.f68bp = i2;
                char cCharAt2 = charAt(this.f68bp);
                this.f69ch = cCharAt2;
                while (cCharAt2 != ',') {
                    if (cCharAt2 == '}') {
                        next();
                        skipWhitespace();
                        char current = getCurrent();
                        if (current == ',') {
                            this.token = 16;
                            int i3 = this.f68bp + 1;
                            this.f68bp = i3;
                            this.f69ch = charAt(i3);
                        } else if (current == ']') {
                            this.token = 15;
                            int i4 = this.f68bp + 1;
                            this.f68bp = i4;
                            this.f69ch = charAt(i4);
                        } else if (current == '}') {
                            this.token = 13;
                            int i5 = this.f68bp + 1;
                            this.f68bp = i5;
                            this.f69ch = charAt(i5);
                        } else {
                            if (current != 26) {
                                this.matchStat = -1;
                                return 0L;
                            }
                            this.token = 20;
                        }
                        this.matchStat = 4;
                        return j;
                    }
                    if (!isWhitespace(cCharAt2)) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    int i6 = this.f68bp + 1;
                    this.f68bp = i6;
                    cCharAt2 = charAt(i6);
                }
                int i7 = this.f68bp + 1;
                this.f68bp = i7;
                this.f69ch = charAt(i7);
                this.matchStat = 3;
                return j;
            }
            if (i2 > this.len) {
                this.matchStat = -1;
                return 0L;
            }
            j = (j ^ cCharAt) * 1099511628211L;
            i = i2;
        }
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean z) {
        return scanISO8601DateIfMatch(z, this.len - this.f68bp);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0108, code lost:
    
        if (r0 != '.') goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x010b, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0111, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0112, code lost:
    
        r8 = r0;
        r11 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x011a, code lost:
    
        if (r12 == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0120, code lost:
    
        if (r0 == '\"') goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0123, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0129, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x012a, code lost:
    
        r8 = charAt(r0);
        r11 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0139, code lost:
    
        if (r10 >= 0) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x013c, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0142, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0145, code lost:
    
        if (r8 != r7) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0148, code lost:
    
        r6.f68bp = r11;
        r6.f69ch = charAt(r6.f68bp);
        r6.matchStat = 3;
        r6.token = 16;
        r7 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x016a, code lost:
    
        if (r13 == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x016d, code lost:
    
        r7 = -r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0172, code lost:
    
        return r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0177, code lost:
    
        if (isWhitespace(r8) == false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x017a, code lost:
    
        r8 = charAt(r11);
        r11 = r11 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0187, code lost:
    
        r6.matchStat = -1;
        r7 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0191, code lost:
    
        if (r13 == false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0194, code lost:
    
        r7 = -r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0199, code lost:
    
        return r7;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final int scanInt(char r7) {
        /*
            Method dump skipped, instructions count: 623
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanInt(char):int");
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x0105  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long scanLong(char r6) {
        /*
            Method dump skipped, instructions count: 568
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanLong(char):long");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public String scanTypeName(SymbolTable symbolTable) {
        int iIndexOf;
        if (!this.text.startsWith("\"@type\":\"", this.f68bp) || (iIndexOf = this.text.indexOf(34, this.f68bp + 9)) == -1) {
            return null;
        }
        this.f68bp += 9;
        int iCharAt = 0;
        for (int i = this.f68bp; i < iIndexOf; i++) {
            iCharAt = (iCharAt * 31) + this.text.charAt(i);
        }
        String strAddSymbol = addSymbol(this.f68bp, iIndexOf - this.f68bp, iCharAt, symbolTable);
        char cCharAt = this.text.charAt(iIndexOf + 1);
        if (cCharAt != ',' && cCharAt != ']') {
            return null;
        }
        this.f68bp = iIndexOf + 2;
        this.f69ch = this.text.charAt(this.f68bp);
        return strAddSymbol;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f4, code lost:
    
        if (r7 == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0104, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illegal json.");
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean seekArrayToItem(int r5) {
        /*
            Method dump skipped, instructions count: 380
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.seekArrayToItem(int):boolean");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public int seekObjectToField(long j, boolean z) {
        if (this.token == 20) {
            return -1;
        }
        if (this.token == 13 || this.token == 15) {
            nextToken();
            return -1;
        }
        if (this.token != 12 && this.token != 16) {
            throw new UnsupportedOperationException(JSONToken.name(this.token));
        }
        while (this.f69ch != '}') {
            if (this.f69ch == 26) {
                return -1;
            }
            if (this.f69ch != '\"') {
                skipWhitespace();
            }
            if (this.f69ch != '\"') {
                throw new UnsupportedOperationException();
            }
            long j2 = -3750763034362895579L;
            int i = this.f68bp;
            while (true) {
                int i2 = i + 1;
                if (i2 >= this.text.length()) {
                    break;
                }
                char cCharAt = this.text.charAt(i2);
                int i3 = i2;
                char cCharAt2 = cCharAt;
                if (cCharAt == '\\') {
                    i3 = i2 + 1;
                    if (i3 == this.text.length()) {
                        throw new JSONException("unclosed str, " + info());
                    }
                    cCharAt2 = this.text.charAt(i3);
                }
                if (cCharAt2 == '\"') {
                    this.f68bp = i3 + 1;
                    this.f69ch = this.f68bp >= this.text.length() ? (char) 26 : this.text.charAt(this.f68bp);
                } else {
                    j2 = (j2 ^ cCharAt2) * 1099511628211L;
                    i = i3;
                }
            }
            if (j2 == j) {
                if (this.f69ch != ':') {
                    skipWhitespace();
                }
                if (this.f69ch != ':') {
                    return 3;
                }
                int i4 = this.f68bp + 1;
                this.f68bp = i4;
                this.f69ch = i4 >= this.text.length() ? (char) 26 : this.text.charAt(i4);
                if (this.f69ch == ',') {
                    int i5 = this.f68bp + 1;
                    this.f68bp = i5;
                    this.f69ch = i5 >= this.text.length() ? (char) 26 : this.text.charAt(i5);
                    this.token = 16;
                    return 3;
                }
                if (this.f69ch == ']') {
                    int i6 = this.f68bp + 1;
                    this.f68bp = i6;
                    this.f69ch = i6 >= this.text.length() ? (char) 26 : this.text.charAt(i6);
                    this.token = 15;
                    return 3;
                }
                if (this.f69ch == '}') {
                    int i7 = this.f68bp + 1;
                    this.f68bp = i7;
                    this.f69ch = i7 >= this.text.length() ? (char) 26 : this.text.charAt(i7);
                    this.token = 13;
                    return 3;
                }
                if (this.f69ch < '0' || this.f69ch > '9') {
                    nextToken(2);
                    return 3;
                }
                this.f71sp = 0;
                this.pos = this.f68bp;
                scanNumber();
                return 3;
            }
            if (this.f69ch != ':') {
                skipWhitespace();
            }
            if (this.f69ch != ':') {
                throw new JSONException("illegal json, " + info());
            }
            int i8 = this.f68bp + 1;
            this.f68bp = i8;
            this.f69ch = i8 >= this.text.length() ? (char) 26 : this.text.charAt(i8);
            if (this.f69ch != '\"' && this.f69ch != '\'' && this.f69ch != '{' && this.f69ch != '[' && this.f69ch != '0' && this.f69ch != '1' && this.f69ch != '2' && this.f69ch != '3' && this.f69ch != '4' && this.f69ch != '5' && this.f69ch != '6' && this.f69ch != '7' && this.f69ch != '8' && this.f69ch != '9' && this.f69ch != '+' && this.f69ch != '-') {
                skipWhitespace();
            }
            if (this.f69ch == '-' || this.f69ch == '+' || (this.f69ch >= '0' && this.f69ch <= '9')) {
                next();
                while (this.f69ch >= '0' && this.f69ch <= '9') {
                    next();
                }
                if (this.f69ch == '.') {
                    next();
                    while (this.f69ch >= '0' && this.f69ch <= '9') {
                        next();
                    }
                }
                if (this.f69ch == 'E' || this.f69ch == 'e') {
                    next();
                    if (this.f69ch == '-' || this.f69ch == '+') {
                        next();
                    }
                    while (this.f69ch >= '0' && this.f69ch <= '9') {
                        next();
                    }
                }
                if (this.f69ch != ',') {
                    skipWhitespace();
                }
                if (this.f69ch == ',') {
                    next();
                }
            } else if (this.f69ch == '\"') {
                skipString();
                if (this.f69ch != ',' && this.f69ch != '}') {
                    skipWhitespace();
                }
                if (this.f69ch == ',') {
                    next();
                }
            } else if (this.f69ch == 't') {
                next();
                if (this.f69ch == 'r') {
                    next();
                    if (this.f69ch == 'u') {
                        next();
                        if (this.f69ch == 'e') {
                            next();
                        }
                    }
                }
                if (this.f69ch != ',' && this.f69ch != '}') {
                    skipWhitespace();
                }
                if (this.f69ch == ',') {
                    next();
                }
            } else if (this.f69ch == 'n') {
                next();
                if (this.f69ch == 'u') {
                    next();
                    if (this.f69ch == 'l') {
                        next();
                        if (this.f69ch == 'l') {
                            next();
                        }
                    }
                }
                if (this.f69ch != ',' && this.f69ch != '}') {
                    skipWhitespace();
                }
                if (this.f69ch == ',') {
                    next();
                }
            } else if (this.f69ch == 'f') {
                next();
                if (this.f69ch == 'a') {
                    next();
                    if (this.f69ch == 'l') {
                        next();
                        if (this.f69ch == 's') {
                            next();
                            if (this.f69ch == 'e') {
                                next();
                            }
                        }
                    }
                }
                if (this.f69ch != ',' && this.f69ch != '}') {
                    skipWhitespace();
                }
                if (this.f69ch == ',') {
                    next();
                }
            } else if (this.f69ch == '{') {
                int i9 = this.f68bp + 1;
                this.f68bp = i9;
                this.f69ch = i9 >= this.text.length() ? (char) 26 : this.text.charAt(i9);
                if (z) {
                    this.token = 12;
                    return 1;
                }
                skipObject(false);
                if (this.token == 13) {
                    return -1;
                }
            } else {
                if (this.f69ch != '[') {
                    throw new UnsupportedOperationException();
                }
                next();
                if (z) {
                    this.token = 14;
                    return 2;
                }
                skipArray(false);
                if (this.token == 13) {
                    return -1;
                }
            }
        }
        next();
        nextToken();
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x02d1, code lost:
    
        if (r5.f69ch == '\'') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x02da, code lost:
    
        if (r5.f69ch == '{') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x02e3, code lost:
    
        if (r5.f69ch == '[') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x02ec, code lost:
    
        if (r5.f69ch == '0') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x02f5, code lost:
    
        if (r5.f69ch == '1') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x02fe, code lost:
    
        if (r5.f69ch == '2') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0307, code lost:
    
        if (r5.f69ch == '3') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0310, code lost:
    
        if (r5.f69ch == '4') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0319, code lost:
    
        if (r5.f69ch == '5') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0322, code lost:
    
        if (r5.f69ch == '6') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x032b, code lost:
    
        if (r5.f69ch == '7') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0334, code lost:
    
        if (r5.f69ch == '8') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x033d, code lost:
    
        if (r5.f69ch == '9') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0346, code lost:
    
        if (r5.f69ch == '+') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x034f, code lost:
    
        if (r5.f69ch == '-') goto L130;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0352, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x035c, code lost:
    
        if (r5.f69ch == '-') goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0365, code lost:
    
        if (r5.f69ch == '+') goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x036e, code lost:
    
        if (r5.f69ch < '0') goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0377, code lost:
    
        if (r5.f69ch > '9') goto L213;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0383, code lost:
    
        if (r5.f69ch != '\"') goto L205;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0386, code lost:
    
        skipString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0390, code lost:
    
        if (r5.f69ch == ',') goto L146;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0399, code lost:
    
        if (r5.f69ch == '}') goto L146;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x039c, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x03a6, code lost:
    
        if (r5.f69ch != ',') goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x03a9, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x03b7, code lost:
    
        if (r5.f69ch != '{') goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x03ba, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x03d1, code lost:
    
        if (r0 < r5.text.length()) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x03d7, code lost:
    
        r7 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x03e1, code lost:
    
        r5.f69ch = r7;
        skipObject(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x03f4, code lost:
    
        if (r5.f69ch != '[') goto L218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x03f7, code lost:
    
        next();
        skipArray(false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x040b, code lost:
    
        throw new java.lang.UnsupportedOperationException();
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x040c, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0417, code lost:
    
        if (r5.f69ch < '0') goto L231;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0420, code lost:
    
        if (r5.f69ch > '9') goto L232;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0423, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0431, code lost:
    
        if (r5.f69ch != '.') goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0434, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x043f, code lost:
    
        if (r5.f69ch < '0') goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0448, code lost:
    
        if (r5.f69ch > '9') goto L234;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x044b, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0459, code lost:
    
        if (r5.f69ch == 'E') goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0462, code lost:
    
        if (r5.f69ch != 'e') goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0465, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0470, code lost:
    
        if (r5.f69ch == '-') goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0479, code lost:
    
        if (r5.f69ch != '+') goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x047c, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x0487, code lost:
    
        if (r5.f69ch < '0') goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0490, code lost:
    
        if (r5.f69ch > '9') goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0493, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x04a1, code lost:
    
        if (r5.f69ch == ',') goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x04a4, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x04ae, code lost:
    
        if (r5.f69ch != ',') goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x04b1, code lost:
    
        next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x04dd, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illegal json, " + info());
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0125, code lost:
    
        r9 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x012c, code lost:
    
        if (r9 >= r6.length) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0136, code lost:
    
        if (r13 != r6[r9]) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x013c, code lost:
    
        r9 = r9 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0142, code lost:
    
        r9 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0148, code lost:
    
        if (r9 == (-1)) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0151, code lost:
    
        if (r5.f69ch == ':') goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0154, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x015e, code lost:
    
        if (r5.f69ch != ':') goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0161, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0178, code lost:
    
        if (r0 < r5.text.length()) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x017b, code lost:
    
        r8 = 26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0181, code lost:
    
        r8 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x018b, code lost:
    
        r5.f69ch = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0196, code lost:
    
        if (r5.f69ch != ',') goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0199, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01b0, code lost:
    
        if (r0 < r5.text.length()) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01b6, code lost:
    
        r7 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01c0, code lost:
    
        r5.f69ch = r7;
        r5.token = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01d4, code lost:
    
        if (r5.f69ch != ']') goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01d7, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01ee, code lost:
    
        if (r0 < r5.text.length()) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01f4, code lost:
    
        r7 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01fe, code lost:
    
        r5.f69ch = r7;
        r5.token = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0212, code lost:
    
        if (r5.f69ch != '}') goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0215, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x022c, code lost:
    
        if (r0 < r5.text.length()) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0232, code lost:
    
        r7 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x023c, code lost:
    
        r5.f69ch = r7;
        r5.token = 13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0250, code lost:
    
        if (r5.f69ch < '0') goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0259, code lost:
    
        if (r5.f69ch > '9') goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x025c, code lost:
    
        r5.f71sp = 0;
        r5.pos = r5.f68bp;
        scanNumber();
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0270, code lost:
    
        nextToken(2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0275, code lost:
    
        r5.matchStat = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x027c, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0283, code lost:
    
        if (r5.f69ch == ':') goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0286, code lost:
    
        skipWhitespace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0290, code lost:
    
        if (r5.f69ch != ':') goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0293, code lost:
    
        r0 = r5.f68bp + 1;
        r5.f68bp = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x02aa, code lost:
    
        if (r0 < r5.text.length()) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x02ad, code lost:
    
        r8 = 26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x02b3, code lost:
    
        r8 = r5.text.charAt(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x02bd, code lost:
    
        r5.f69ch = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x02c8, code lost:
    
        if (r5.f69ch == '\"') goto L130;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int seekObjectToField(long[] r6) {
        /*
            Method dump skipped, instructions count: 1262
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.seekObjectToField(long[]):int");
    }

    protected void setTime(char c, char c2, char c3, char c4, char c5, char c6) {
        this.calendar.set(11, ((c - '0') * 10) + (c2 - '0'));
        this.calendar.set(12, ((c3 - '0') * 10) + (c4 - '0'));
        this.calendar.set(13, ((c5 - '0') * 10) + (c6 - '0'));
    }

    protected void setTimeZone(char c, char c2, char c3) {
        setTimeZone(c, c2, c3, '0', '0');
    }

    protected void setTimeZone(char c, char c2, char c3, char c4, char c5) {
        int i = ((((c2 - '0') * 10) + (c3 - '0')) * 3600 * 1000) + ((((c4 - '0') * 10) + (c5 - '0')) * 60 * 1000);
        int i2 = i;
        if (c == '-') {
            i2 = -i;
        }
        if (this.calendar.getTimeZone().getRawOffset() != i2) {
            String[] availableIDs = TimeZone.getAvailableIDs(i2);
            if (availableIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(availableIDs[0]));
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipArray() {
        skipArray(false);
    }

    public final void skipArray(boolean z) {
        int i;
        boolean z2;
        int i2;
        int i3 = this.f68bp;
        boolean z3 = false;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i3 >= this.text.length()) {
                if (i3 != this.text.length()) {
                    return;
                }
                throw new JSONException("illegal str, " + info());
            }
            char cCharAt = this.text.charAt(i3);
            if (cCharAt == '\\') {
                if (i3 >= this.len - 1) {
                    this.f69ch = cCharAt;
                    this.f68bp = i3;
                    throw new JSONException("illegal str, " + info());
                }
                i = i3 + 1;
                z2 = z3;
                i2 = i5;
            } else if (cCharAt == '\"') {
                z2 = !z3;
                i = i3;
                i2 = i5;
            } else if (cCharAt != '[') {
                char cCharAt2 = 26;
                if (cCharAt == '{' && z) {
                    int i6 = this.f68bp + 1;
                    this.f68bp = i6;
                    if (i6 < this.text.length()) {
                        cCharAt2 = this.text.charAt(i6);
                    }
                    this.f69ch = cCharAt2;
                    skipObject(z);
                    i = i3;
                    z2 = z3;
                    i2 = i5;
                } else {
                    i = i3;
                    z2 = z3;
                    i2 = i5;
                    if (cCharAt != ']') {
                        continue;
                    } else if (z3) {
                        i = i3;
                        z2 = z3;
                        i2 = i5;
                    } else {
                        int i7 = i5 - 1;
                        i = i3;
                        z2 = z3;
                        i2 = i7;
                        if (i7 == -1) {
                            this.f68bp = i3 + 1;
                            if (this.f68bp == this.text.length()) {
                                this.f69ch = (char) 26;
                                this.token = 20;
                                return;
                            } else {
                                this.f69ch = this.text.charAt(this.f68bp);
                                nextToken(16);
                                return;
                            }
                        }
                    }
                }
            } else if (z3) {
                i = i3;
                z2 = z3;
                i2 = i5;
            } else {
                i2 = i5 + 1;
                i = i3;
                z2 = z3;
            }
            i3 = i + 1;
            z3 = z2;
            i4 = i2;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipObject() {
        skipObject(false);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final void skipObject(boolean z) {
        boolean z2;
        int i;
        int i2;
        int i3 = this.f68bp;
        boolean z3 = false;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i3 >= this.text.length()) {
                if (i3 != this.text.length()) {
                    return;
                }
                throw new JSONException("illegal str, " + info());
            }
            char cCharAt = this.text.charAt(i3);
            if (cCharAt == '\\') {
                if (i3 >= this.len - 1) {
                    this.f69ch = cCharAt;
                    this.f68bp = i3;
                    throw new JSONException("illegal str, " + info());
                }
                i2 = i3 + 1;
                z2 = z3;
                i = i5;
            } else if (cCharAt == '\"') {
                z2 = !z3;
                i = i5;
                i2 = i3;
            } else if (cCharAt != '{') {
                z2 = z3;
                i = i5;
                i2 = i3;
                if (cCharAt != '}') {
                    continue;
                } else if (z3) {
                    z2 = z3;
                    i = i5;
                    i2 = i3;
                } else {
                    int i6 = i5 - 1;
                    z2 = z3;
                    i = i6;
                    i2 = i3;
                    if (i6 == -1) {
                        this.f68bp = i3 + 1;
                        char cCharAt2 = 26;
                        if (this.f68bp == this.text.length()) {
                            this.f69ch = (char) 26;
                            this.token = 20;
                            return;
                        }
                        this.f69ch = this.text.charAt(this.f68bp);
                        if (this.f69ch == ',') {
                            this.token = 16;
                            int i7 = this.f68bp + 1;
                            this.f68bp = i7;
                            if (i7 < this.text.length()) {
                                cCharAt2 = this.text.charAt(i7);
                            }
                            this.f69ch = cCharAt2;
                            return;
                        }
                        if (this.f69ch == '}') {
                            this.token = 13;
                            next();
                            return;
                        } else if (this.f69ch != ']') {
                            nextToken(16);
                            return;
                        } else {
                            this.token = 15;
                            next();
                            return;
                        }
                    }
                }
            } else if (z3) {
                z2 = z3;
                i = i5;
                i2 = i3;
            } else {
                i = i5 + 1;
                z2 = z3;
                i2 = i3;
            }
            i3 = i2 + 1;
            z3 = z2;
            i4 = i;
        }
    }

    public final void skipString() {
        if (this.f69ch != '\"') {
            throw new UnsupportedOperationException();
        }
        int i = this.f68bp;
        while (true) {
            int i2 = i + 1;
            if (i2 >= this.text.length()) {
                throw new JSONException("unclosed str");
            }
            char cCharAt = this.text.charAt(i2);
            if (cCharAt == '\\') {
                i = i2;
                if (i2 < this.len - 1) {
                    i = i2 + 1;
                }
            } else {
                i = i2;
                if (cCharAt == '\"') {
                    String str = this.text;
                    int i3 = i2 + 1;
                    this.f68bp = i3;
                    this.f69ch = str.charAt(i3);
                    return;
                }
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String stringVal() {
        return !this.hasSpecial ? subString(this.f70np + 1, this.f71sp) : new String(this.sbuf, 0, this.f71sp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String subString(int i, int i2) {
        if (!ASMUtils.IS_ANDROID) {
            return this.text.substring(i, i2 + i);
        }
        if (i2 < this.sbuf.length) {
            this.text.getChars(i, i + i2, this.sbuf, 0);
            return new String(this.sbuf, 0, i2);
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return new String(cArr);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char[] sub_chars(int i, int i2) {
        if (ASMUtils.IS_ANDROID && i2 < this.sbuf.length) {
            this.text.getChars(i, i2 + i, this.sbuf, 0);
            return this.sbuf;
        }
        char[] cArr = new char[i2];
        this.text.getChars(i, i2 + i, cArr, 0);
        return cArr;
    }
}
