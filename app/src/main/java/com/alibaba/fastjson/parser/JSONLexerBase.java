package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.github.clans.fab.BuildConfig;
import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/JSONLexerBase.class */
public abstract class JSONLexerBase implements JSONLexer, Closeable {
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;

    /* renamed from: bp */
    protected int f68bp;

    /* renamed from: ch */
    protected char f69ch;
    protected int eofPos;
    protected int features;
    protected boolean hasSpecial;

    /* renamed from: np */
    protected int f70np;
    protected int pos;
    protected char[] sbuf;

    /* renamed from: sp */
    protected int f71sp;
    protected String stringDefaultValue;
    protected int token;
    private static final ThreadLocal<char[]> SBUF_LOCAL = new ThreadLocal<>();
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected static final int[] digits = new int[103];
    protected Calendar calendar = null;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected Locale locale = JSON.defaultLocale;
    public int matchStat = 0;

    static {
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    public JSONLexerBase(int i) {
        this.stringDefaultValue = null;
        this.features = i;
        if ((i & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = BuildConfig.FLAVOR;
        }
        char[] cArr = SBUF_LOCAL.get();
        this.sbuf = cArr;
        if (cArr == null) {
            this.sbuf = new char[512];
        }
    }

    public static boolean isWhitespace(char c) {
        return c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b');
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0276  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String readString(char[] r10, int r11) {
        /*
            Method dump skipped, instructions count: 706
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.readString(char[], int):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x0204  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0214  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void scanStringSingleQuote() {
        /*
            Method dump skipped, instructions count: 756
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanStringSingleQuote():void");
    }

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract byte[] bytesValue();

    protected abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    @Override // com.alibaba.fastjson.parser.JSONLexer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        char[] cArr = this.sbuf;
        if (cArr.length <= 8192) {
            SBUF_LOCAL.set(cArr);
        }
        this.sbuf = null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void config(Feature feature, boolean z) {
        int iConfig = Feature.config(this.features, feature, z);
        this.features = iConfig;
        if ((iConfig & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = BuildConfig.FLAVOR;
        }
    }

    protected abstract void copyTo(int i, int i2, char[] cArr);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number decimalValue(boolean z) {
        char cCharAt = charAt((this.f70np + this.f71sp) - 1);
        try {
            return cCharAt == 'F' ? Float.valueOf(Float.parseFloat(numberString())) : cCharAt == 'D' ? Double.valueOf(Double.parseDouble(numberString())) : z ? decimalValue() : Double.valueOf(doubleValue());
        } catch (NumberFormatException e) {
            throw new JSONException(e.getMessage() + ", " + info());
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract BigDecimal decimalValue();

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public float floatValue() throws NumberFormatException {
        char cCharAt;
        String strNumberString = numberString();
        float f = Float.parseFloat(strNumberString);
        if ((f != 0.0f && f != Float.POSITIVE_INFINITY) || (cCharAt = strNumberString.charAt(0)) <= '0' || cCharAt > '9') {
            return f;
        }
        throw new JSONException("float overflow : " + strNumberString);
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final char getCurrent() {
        return this.f69ch;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int getFeatures() {
        return this.features;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Locale getLocale() {
        return this.locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public abstract int indexOf(char c, int i);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String info() {
        return BuildConfig.FLAVOR;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00b7, code lost:
    
        if (r9 == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c1, code lost:
    
        if (r8 <= (r5.f70np + 1)) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00c5, code lost:
    
        return r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00d1, code lost:
    
        throw new java.lang.NumberFormatException(numberString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d4, code lost:
    
        return -r7;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final int intValue() {
        /*
            Method dump skipped, instructions count: 213
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.intValue():int");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number integerValue() throws NumberFormatException {
        long j;
        long j2;
        boolean z = false;
        if (this.f70np == -1) {
            this.f70np = 0;
        }
        int i = this.f70np;
        int i2 = this.f71sp + i;
        boolean z2 = 32;
        char cCharAt = charAt(i2 - 1);
        if (cCharAt == 'B') {
            i2--;
            z2 = 66;
        } else if (cCharAt == 'L') {
            i2--;
            z2 = 76;
        } else if (cCharAt == 'S') {
            i2--;
            z2 = 83;
        }
        if (charAt(this.f70np) == '-') {
            j = Long.MIN_VALUE;
            i++;
            z = true;
        } else {
            j = -9223372036854775807L;
        }
        if (i < i2) {
            j2 = -(charAt(i) - '0');
            i++;
        } else {
            j2 = 0;
        }
        while (i < i2) {
            char cCharAt2 = charAt(i);
            if (j2 < MULTMIN_RADIX_TEN) {
                return new BigInteger(numberString());
            }
            long j3 = j2 * 10;
            long j4 = cCharAt2 - '0';
            if (j3 < j + j4) {
                return new BigInteger(numberString());
            }
            j2 = j3 - j4;
            i++;
        }
        if (!z) {
            long j5 = -j2;
            return (j5 > 2147483647L || z2 == 76) ? Long.valueOf(j5) : z2 == 83 ? Short.valueOf((short) j5) : z2 == 66 ? Byte.valueOf((byte) j5) : Integer.valueOf((int) j5);
        }
        if (i > this.f70np + 1) {
            return (j2 < -2147483648L || z2 == 76) ? Long.valueOf(j2) : z2 == 83 ? Short.valueOf((short) j2) : z2 == 66 ? Byte.valueOf((byte) j2) : Integer.valueOf((int) j2);
        }
        throw new NumberFormatException(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean isBlankInput() {
        int i = 0;
        while (true) {
            char cCharAt = charAt(i);
            if (cCharAt == 26) {
                this.token = 20;
                return true;
            }
            if (!isWhitespace(cCharAt)) {
                return false;
            }
            i++;
        }
    }

    public abstract boolean isEOF();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(int i) {
        return (i & this.features) != 0;
    }

    public final boolean isEnabled(int i, int i2) {
        return ((this.features & i2) == 0 && (i & i2) == 0) ? false : true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(Feature feature) {
        return isEnabled(feature.mask);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isRef() {
        if (this.f71sp != 4) {
            return false;
        }
        boolean z = false;
        if (charAt(this.f70np + 1) == '$') {
            z = false;
            if (charAt(this.f70np + 2) == 'r') {
                z = false;
                if (charAt(this.f70np + 3) == 'e') {
                    z = false;
                    if (charAt(this.f70np + 4) == 'f') {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    protected void lexError(String str, Object... objArr) {
        this.token = 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c7, code lost:
    
        if (r8 == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d1, code lost:
    
        if (r10 <= (r7.f70np + 1)) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00d6, code lost:
    
        return r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00e2, code lost:
    
        throw new java.lang.NumberFormatException(numberString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00e6, code lost:
    
        return -r14;
     */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final long longValue() throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 231
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.longValue():long");
    }

    public int matchField(long j) {
        throw new UnsupportedOperationException();
    }

    public final boolean matchField(char[] cArr) {
        while (!charArrayCompare(cArr)) {
            if (!isWhitespace(this.f69ch)) {
                return false;
            }
            next();
        }
        int length = this.f68bp + cArr.length;
        this.f68bp = length;
        char cCharAt = charAt(length);
        this.f69ch = cCharAt;
        if (cCharAt == '{') {
            next();
            this.token = 12;
            return true;
        }
        if (cCharAt == '[') {
            next();
            this.token = 14;
            return true;
        }
        if (cCharAt != 'S' || charAt(this.f68bp + 1) != 'e' || charAt(this.f68bp + 2) != 't' || charAt(this.f68bp + 3) != '[') {
            nextToken();
            return true;
        }
        int i = this.f68bp + 3;
        this.f68bp = i;
        this.f69ch = charAt(i);
        this.token = 21;
        return true;
    }

    public boolean matchField2(char[] cArr) {
        throw new UnsupportedOperationException();
    }

    public final int matchStat() {
        return this.matchStat;
    }

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

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract char next();

    public final void nextIdent() {
        while (isWhitespace(this.f69ch)) {
            next();
        }
        char c = this.f69ch;
        if (c == '_' || c == '$' || Character.isLetter(c)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken() {
        this.f71sp = 0;
        while (true) {
            this.pos = this.f68bp;
            char c = this.f69ch;
            if (c == '/') {
                skipComment();
            } else {
                if (c == '\"') {
                    scanString();
                    return;
                }
                if (c == ',') {
                    next();
                    this.token = 16;
                    return;
                }
                if (c >= '0' && c <= '9') {
                    scanNumber();
                    return;
                }
                char c2 = this.f69ch;
                if (c2 != '-') {
                    switch (c2) {
                        case '\b':
                        case '\t':
                        case '\n':
                        case '\f':
                        case '\r':
                        case ' ':
                            next();
                            break;
                        case '\'':
                            if (!isEnabled(Feature.AllowSingleQuotes)) {
                                throw new JSONException("Feature.AllowSingleQuotes is false");
                            }
                            scanStringSingleQuote();
                            return;
                        case '(':
                            next();
                            this.token = 10;
                            return;
                        case ')':
                            next();
                            this.token = 11;
                            return;
                        case '+':
                            next();
                            scanNumber();
                            return;
                        case '.':
                            next();
                            this.token = 25;
                            return;
                        case ':':
                            next();
                            this.token = 17;
                            return;
                        case ';':
                            next();
                            this.token = 24;
                            return;
                        case 'N':
                        case 'S':
                        case 'T':
                        case 'u':
                            scanIdent();
                            return;
                        case '[':
                            next();
                            this.token = 14;
                            return;
                        case ']':
                            next();
                            this.token = 15;
                            return;
                        case 'f':
                            scanFalse();
                            return;
                        case 'n':
                            scanNullOrNew();
                            return;
                        case 't':
                            scanTrue();
                            return;
                        case 'x':
                            scanHex();
                            return;
                        case CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE /* 123 */:
                            next();
                            this.token = 12;
                            return;
                        case NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE /* 125 */:
                            next();
                            this.token = 13;
                            return;
                        default:
                            if (isEOF()) {
                                if (this.token == 20) {
                                    throw new JSONException("EOF error");
                                }
                                this.token = 20;
                                int i = this.f68bp;
                                this.pos = i;
                                this.eofPos = i;
                                return;
                            }
                            char c3 = this.f69ch;
                            if (c3 > 31 && c3 != 127) {
                                lexError("illegal.char", String.valueOf((int) c3));
                                next();
                                return;
                            } else {
                                next();
                                break;
                            }
                            break;
                    }
                } else {
                    scanNumber();
                    return;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:123:0x00da A[SYNTHETIC] */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void nextToken(int r4) {
        /*
            Method dump skipped, instructions count: 508
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.nextToken(int):void");
    }

    public final void nextTokenWithChar(char c) {
        this.f71sp = 0;
        while (true) {
            char c2 = this.f69ch;
            if (c2 == c) {
                next();
                nextToken();
                return;
            }
            if (c2 != ' ' && c2 != '\n' && c2 != '\r' && c2 != '\t' && c2 != '\f' && c2 != '\b') {
                throw new JSONException("not match " + c + " - " + this.f69ch + ", info : " + info());
            }
            next();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon(int i) {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String numberString();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int pos() {
        return this.pos;
    }

    protected final void putChar(char c) {
        int i = this.f71sp;
        char[] cArr = this.sbuf;
        if (i == cArr.length) {
            char[] cArr2 = new char[cArr.length * 2];
            System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
            this.sbuf = cArr2;
        }
        char[] cArr3 = this.sbuf;
        int i2 = this.f71sp;
        this.f71sp = i2 + 1;
        cArr3[i2] = c;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void resetStringPosition() {
        this.f71sp = 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public boolean scanBoolean(char c) {
        boolean z = false;
        this.matchStat = 0;
        char cCharAt = charAt(this.f68bp + 0);
        int i = 5;
        if (cCharAt == 't') {
            if (charAt(this.f68bp + 1) != 'r' || charAt(this.f68bp + 1 + 1) != 'u' || charAt(this.f68bp + 1 + 2) != 'e') {
                this.matchStat = -1;
                return false;
            }
            cCharAt = charAt(this.f68bp + 4);
            z = true;
        } else if (cCharAt != 'f') {
            if (cCharAt == '1') {
                cCharAt = charAt(this.f68bp + 1);
                z = true;
            } else if (cCharAt == '0') {
                cCharAt = charAt(this.f68bp + 1);
            } else {
                i = 1;
                z = false;
            }
            i = 2;
        } else {
            if (charAt(this.f68bp + 1) != 'a' || charAt(this.f68bp + 1 + 1) != 'l' || charAt(this.f68bp + 1 + 2) != 's' || charAt(this.f68bp + 1 + 3) != 'e') {
                this.matchStat = -1;
                return false;
            }
            cCharAt = charAt(this.f68bp + 5);
            i = 6;
            z = false;
        }
        while (cCharAt != c) {
            if (!isWhitespace(cCharAt)) {
                this.matchStat = -1;
                return z;
            }
            cCharAt = charAt(this.f68bp + i);
            i++;
        }
        int i2 = this.f68bp + i;
        this.f68bp = i2;
        this.f69ch = charAt(i2);
        this.matchStat = 3;
        return z;
    }

    public Date scanDate(char c) {
        int i;
        long j;
        Date date;
        boolean z = false;
        this.matchStat = 0;
        char cCharAt = charAt(this.f68bp + 0);
        if (cCharAt == '\"') {
            int iIndexOf = indexOf('\"', this.f68bp + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i2 = this.f68bp + 1;
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
                int i6 = this.f68bp;
                int i7 = i3 - (i6 + 1);
                string = readString(sub_chars(i6 + 1, i7), i7);
            }
            int i8 = this.f68bp;
            int i9 = (i3 - (i8 + 1)) + 1 + 1;
            cCharAt = charAt(i8 + i9);
            JSONScanner jSONScanner = new JSONScanner(string);
            try {
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    this.matchStat = -1;
                    jSONScanner.close();
                    return null;
                }
                date = jSONScanner.getCalendar().getTime();
                jSONScanner.close();
                i = i9 + 1;
            } catch (Throwable th) {
                jSONScanner.close();
                throw th;
            }
        } else {
            i = 2;
            if (cCharAt == '-' || (cCharAt >= '0' && cCharAt <= '9')) {
                if (cCharAt == '-') {
                    cCharAt = charAt(this.f68bp + 1);
                    z = true;
                } else {
                    i = 1;
                }
                if (cCharAt >= '0' && cCharAt <= '9') {
                    long j2 = cCharAt - '0';
                    while (true) {
                        int i10 = i + 1;
                        char cCharAt2 = charAt(this.f68bp + i);
                        cCharAt = cCharAt2;
                        j = j2;
                        i = i10;
                        if (cCharAt2 < '0') {
                            break;
                        }
                        cCharAt = cCharAt2;
                        j = j2;
                        i = i10;
                        if (cCharAt2 > '9') {
                            break;
                        }
                        j2 = (j2 * 10) + (cCharAt2 - '0');
                        i = i10;
                    }
                } else {
                    j = 0;
                }
                if (j < 0) {
                    this.matchStat = -1;
                    return null;
                }
                long j3 = j;
                if (z) {
                    j3 = -j;
                }
                date = new Date(j3);
            } else {
                if (cCharAt != 'n' || charAt(this.f68bp + 1) != 'u' || charAt(this.f68bp + 1 + 1) != 'l' || charAt(this.f68bp + 1 + 2) != 'l') {
                    this.matchStat = -1;
                    return null;
                }
                this.matchStat = 5;
                cCharAt = charAt(this.f68bp + 4);
                date = null;
                i = 5;
            }
        }
        if (cCharAt == ',') {
            int i11 = this.f68bp + i;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
            this.matchStat = 3;
            this.token = 16;
            return date;
        }
        if (cCharAt != ']') {
            this.matchStat = -1;
            return null;
        }
        int i12 = i + 1;
        char cCharAt3 = charAt(this.f68bp + i);
        if (cCharAt3 == ',') {
            this.token = 16;
            int i13 = this.f68bp + i12;
            this.f68bp = i13;
            this.f69ch = charAt(i13);
        } else if (cCharAt3 == ']') {
            this.token = 15;
            int i14 = this.f68bp + i12;
            this.f68bp = i14;
            this.f69ch = charAt(i14);
        } else if (cCharAt3 == '}') {
            this.token = 13;
            int i15 = this.f68bp + i12;
            this.f68bp = i15;
            this.f69ch = charAt(i15);
        } else {
            if (cCharAt3 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.f68bp += i12 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return date;
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x01b3  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x01c5 -> B:57:0x017c). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.math.BigDecimal scanDecimal(char r7) {
        /*
            Method dump skipped, instructions count: 1075
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanDecimal(char):java.math.BigDecimal");
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x022e  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:67:0x0251 -> B:61:0x01d5). Please report as a decompilation issue!!! */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double scanDouble(char r6) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 1097
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanDouble(char):double");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Enum<?> scanEnum(Class<?> cls, SymbolTable symbolTable, char c) {
        String strScanSymbolWithSeperator = scanSymbolWithSeperator(symbolTable, c);
        if (strScanSymbolWithSeperator == null) {
            return null;
        }
        return Enum.valueOf(cls, strScanSymbolWithSeperator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v82, types: [int] */
    public long scanEnumSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.f68bp + length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = -3750763034362895579L;
        while (true) {
            int i2 = i + 1;
            char cCharAt = charAt(this.f68bp + i);
            if (cCharAt == '\"') {
                int i3 = i2 + 1;
                char cCharAt2 = charAt(this.f68bp + i2);
                if (cCharAt2 == ',') {
                    int i4 = this.f68bp + i3;
                    this.f68bp = i4;
                    this.f69ch = charAt(i4);
                    this.matchStat = 3;
                    return j;
                }
                if (cCharAt2 != '}') {
                    this.matchStat = -1;
                    return 0L;
                }
                int i5 = i3 + 1;
                char cCharAt3 = charAt(this.f68bp + i3);
                if (cCharAt3 == ',') {
                    this.token = 16;
                    int i6 = this.f68bp + i5;
                    this.f68bp = i6;
                    this.f69ch = charAt(i6);
                } else if (cCharAt3 == ']') {
                    this.token = 15;
                    int i7 = this.f68bp + i5;
                    this.f68bp = i7;
                    this.f69ch = charAt(i7);
                } else if (cCharAt3 == '}') {
                    this.token = 13;
                    int i8 = this.f68bp + i5;
                    this.f68bp = i8;
                    this.f69ch = charAt(i8);
                } else {
                    if (cCharAt3 != 26) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.token = 20;
                    this.f68bp += i5 - 1;
                    this.f69ch = (char) 26;
                }
                this.matchStat = 4;
                return j;
            }
            j = (j ^ ((cCharAt < 'A' || cCharAt > 'Z') ? cCharAt : cCharAt + ' ')) * 1099511628211L;
            if (cCharAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i2;
        }
    }

    public final void scanFalse() {
        if (this.f69ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.f69ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.f69ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.f69ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.f69ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        char c = this.f69ch;
        if (c != ' ' && c != ',' && c != '}' && c != ']' && c != '\n' && c != '\r' && c != '\t' && c != 26 && c != '\f' && c != '\b' && c != ':' && c != '/') {
            throw new JSONException("scan false error");
        }
        this.token = 7;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00f3, code lost:
    
        r10 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.math.BigInteger scanFieldBigInteger(char[] r7) {
        /*
            Method dump skipped, instructions count: 925
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldBigInteger(char[]):java.math.BigInteger");
    }

    public boolean scanFieldBoolean(char[] cArr) {
        int i;
        boolean z;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return false;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt = charAt(this.f68bp + length);
        if (cCharAt == 't') {
            int i3 = i2 + 1;
            if (charAt(this.f68bp + i2) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int i4 = i3 + 1;
            if (charAt(this.f68bp + i3) != 'u') {
                this.matchStat = -1;
                return false;
            }
            i = i4 + 1;
            if (charAt(this.f68bp + i4) != 'e') {
                this.matchStat = -1;
                return false;
            }
            z = true;
        } else {
            if (cCharAt != 'f') {
                this.matchStat = -1;
                return false;
            }
            int i5 = i2 + 1;
            if (charAt(this.f68bp + i2) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int i6 = i5 + 1;
            if (charAt(this.f68bp + i5) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int i7 = i6 + 1;
            if (charAt(this.f68bp + i6) != 's') {
                this.matchStat = -1;
                return false;
            }
            if (charAt(this.f68bp + i7) != 'e') {
                this.matchStat = -1;
                return false;
            }
            i = i7 + 1;
            z = false;
        }
        int i8 = i + 1;
        char cCharAt2 = charAt(this.f68bp + i);
        if (cCharAt2 == ',') {
            int i9 = this.f68bp + i8;
            this.f68bp = i9;
            this.f69ch = charAt(i9);
            this.matchStat = 3;
            this.token = 16;
            return z;
        }
        if (cCharAt2 != '}') {
            this.matchStat = -1;
            return false;
        }
        int i10 = i8 + 1;
        char cCharAt3 = charAt(this.f68bp + i8);
        if (cCharAt3 == ',') {
            this.token = 16;
            int i11 = this.f68bp + i10;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
        } else if (cCharAt3 == ']') {
            this.token = 15;
            int i12 = this.f68bp + i10;
            this.f68bp = i12;
            this.f69ch = charAt(i12);
        } else if (cCharAt3 == '}') {
            this.token = 13;
            int i13 = this.f68bp + i10;
            this.f68bp = i13;
            this.f69ch = charAt(i13);
        } else {
            if (cCharAt3 != 26) {
                this.matchStat = -1;
                return false;
            }
            this.token = 20;
            this.f68bp += i10 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return z;
    }

    public Date scanFieldDate(char[] cArr) {
        char cCharAt;
        int i;
        long j;
        Date date;
        boolean z = false;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt2 = charAt(this.f68bp + length);
        if (cCharAt2 == '\"') {
            int iIndexOf = indexOf('\"', this.f68bp + cArr.length + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int length2 = this.f68bp + cArr.length + 1;
            String strSubString = subString(length2, iIndexOf - length2);
            int i3 = iIndexOf;
            String string = strSubString;
            if (strSubString.indexOf(92) != -1) {
                while (true) {
                    int i4 = 0;
                    for (int i5 = iIndexOf - 1; i5 >= 0 && charAt(i5) == '\\'; i5--) {
                        i4++;
                    }
                    if (i4 % 2 == 0) {
                        break;
                    }
                    iIndexOf = indexOf('\"', iIndexOf + 1);
                }
                int i6 = this.f68bp;
                int length3 = iIndexOf - ((cArr.length + i6) + 1);
                string = readString(sub_chars(i6 + cArr.length + 1, length3), length3);
                i3 = iIndexOf;
            }
            int i7 = this.f68bp;
            int length4 = i2 + (i3 - ((cArr.length + i7) + 1)) + 1;
            i = length4 + 1;
            cCharAt = charAt(i7 + length4);
            JSONScanner jSONScanner = new JSONScanner(string);
            try {
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    this.matchStat = -1;
                    jSONScanner.close();
                    return null;
                }
                date = jSONScanner.getCalendar().getTime();
            } finally {
                jSONScanner.close();
            }
        } else {
            if (cCharAt2 != '-' && (cCharAt2 < '0' || cCharAt2 > '9')) {
                this.matchStat = -1;
                return null;
            }
            cCharAt = cCharAt2;
            i = i2;
            if (cCharAt2 == '-') {
                cCharAt = charAt(this.f68bp + i2);
                i = i2 + 1;
                z = true;
            }
            if (cCharAt < '0' || cCharAt > '9') {
                j = 0;
            } else {
                long j2 = cCharAt - '0';
                while (true) {
                    j = j2;
                    int i8 = i;
                    i = i8 + 1;
                    cCharAt = charAt(this.f68bp + i8);
                    if (cCharAt < '0' || cCharAt > '9') {
                        break;
                    }
                    j2 = (j * 10) + (cCharAt - '0');
                }
            }
            if (j < 0) {
                this.matchStat = -1;
                return null;
            }
            long j3 = j;
            if (z) {
                j3 = -j;
            }
            date = new Date(j3);
        }
        if (cCharAt == ',') {
            int i9 = this.f68bp + i;
            this.f68bp = i9;
            this.f69ch = charAt(i9);
            this.matchStat = 3;
            return date;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return null;
        }
        int i10 = i + 1;
        char cCharAt3 = charAt(this.f68bp + i);
        if (cCharAt3 == ',') {
            this.token = 16;
            int i11 = this.f68bp + i10;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
        } else if (cCharAt3 == ']') {
            this.token = 15;
            int i12 = this.f68bp + i10;
            this.f68bp = i12;
            this.f69ch = charAt(i12);
        } else if (cCharAt3 == '}') {
            this.token = 13;
            int i13 = this.f68bp + i10;
            this.f68bp = i13;
            this.f69ch = charAt(i13);
        } else {
            if (cCharAt3 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.f68bp += i10 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return date;
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x01db  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x01ee -> B:60:0x01a2). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.math.BigDecimal scanFieldDecimal(char[] r7) {
        /*
            Method dump skipped, instructions count: 1156
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldDecimal(char[]):java.math.BigDecimal");
    }

    /* JADX WARN: Removed duplicated region for block: B:66:0x024c  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x0267 -> B:62:0x0203). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final double scanFieldDouble(char[] r6) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 1342
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldDouble(char[]):double");
    }

    public final float scanFieldFloat(char[] cArr) throws NumberFormatException {
        int i;
        char cCharAt;
        long j;
        int length;
        int i2;
        float f;
        int i3;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0.0f;
        }
        int length2 = cArr.length;
        int i4 = length2 + 1;
        char cCharAt2 = charAt(this.f68bp + length2);
        boolean z = cCharAt2 == '\"';
        int i5 = i4;
        if (z) {
            cCharAt2 = charAt(this.f68bp + i4);
            i5 = i4 + 1;
        }
        boolean z2 = cCharAt2 == '-';
        char cCharAt3 = cCharAt2;
        int i6 = i5;
        if (z2) {
            cCharAt3 = charAt(this.f68bp + i5);
            i6 = i5 + 1;
        }
        if (cCharAt3 < '0' || cCharAt3 > '9') {
            if (cCharAt3 != 'n' || charAt(this.f68bp + i6) != 'u' || charAt(this.f68bp + i6 + 1) != 'l' || charAt(this.f68bp + i6 + 2) != 'l') {
                this.matchStat = -1;
                return 0.0f;
            }
            this.matchStat = 5;
            int i7 = i6 + 3;
            int i8 = i7 + 1;
            char cCharAt4 = charAt(this.f68bp + i7);
            char cCharAt5 = cCharAt4;
            int i9 = i8;
            if (z) {
                cCharAt5 = cCharAt4;
                i9 = i8;
                if (cCharAt4 == '\"') {
                    cCharAt5 = charAt(this.f68bp + i8);
                    i9 = i8 + 1;
                }
            }
            while (cCharAt5 != ',') {
                if (cCharAt5 == '}') {
                    int i10 = this.f68bp + i9;
                    this.f68bp = i10;
                    this.f69ch = charAt(i10);
                    this.matchStat = 5;
                    this.token = 13;
                    return 0.0f;
                }
                if (!isWhitespace(cCharAt5)) {
                    this.matchStat = -1;
                    return 0.0f;
                }
                cCharAt5 = charAt(this.f68bp + i9);
                i9++;
            }
            int i11 = this.f68bp + i9;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
            this.matchStat = 5;
            this.token = 16;
            return 0.0f;
        }
        long j2 = cCharAt3 - '0';
        while (true) {
            i = i6 + 1;
            cCharAt = charAt(this.f68bp + i6);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j2 = (j2 * 10) + (cCharAt - '0');
            i6 = i;
        }
        if (cCharAt == '.') {
            int i12 = i + 1;
            char cCharAt6 = charAt(this.f68bp + i);
            if (cCharAt6 >= '0' && cCharAt6 <= '9') {
                long j3 = (j2 * 10) + (cCharAt6 - '0');
                long j4 = 10;
                while (true) {
                    int i13 = i12 + 1;
                    char cCharAt7 = charAt(this.f68bp + i12);
                    cCharAt = cCharAt7;
                    j = j4;
                    j2 = j3;
                    i = i13;
                    if (cCharAt7 < '0') {
                        break;
                    }
                    cCharAt = cCharAt7;
                    j = j4;
                    j2 = j3;
                    i = i13;
                    if (cCharAt7 > '9') {
                        break;
                    }
                    j3 = (j3 * 10) + (cCharAt7 - '0');
                    j4 *= 10;
                    i12 = i13;
                }
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        } else {
            j = 1;
        }
        boolean z3 = cCharAt == 'e' || cCharAt == 'E';
        long j5 = j;
        boolean z4 = z2;
        long j6 = j2;
        boolean z5 = z3;
        int i14 = i;
        if (z3) {
            int i15 = i + 1;
            char cCharAt8 = charAt(this.f68bp + i);
            if (cCharAt8 == '+' || cCharAt8 == '-') {
                int i16 = i15 + 1;
                cCharAt8 = charAt(this.f68bp + i15);
                i3 = i16;
            } else {
                i3 = i15;
            }
            while (true) {
                cCharAt = cCharAt8;
                j5 = j;
                z4 = z2;
                j6 = j2;
                z5 = z3;
                i14 = i3;
                if (cCharAt8 < '0') {
                    break;
                }
                cCharAt = cCharAt8;
                j5 = j;
                z4 = z2;
                j6 = j2;
                z5 = z3;
                i14 = i3;
                if (cCharAt8 > '9') {
                    break;
                }
                int i17 = i3 + 1;
                cCharAt8 = charAt(this.f68bp + i3);
                i3 = i17;
            }
        }
        if (!z) {
            int i18 = this.f68bp;
            length = cArr.length + i18;
            i2 = ((i18 + i14) - length) - 1;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0.0f;
            }
            int i19 = i14 + 1;
            cCharAt = charAt(this.f68bp + i14);
            int i20 = this.f68bp;
            int length3 = cArr.length + i20 + 1;
            i2 = ((i20 + i19) - length3) - 2;
            i14 = i19;
            length = length3;
        }
        if (z5 || i2 >= 17) {
            f = Float.parseFloat(subString(length, i2));
        } else {
            double d = j6;
            double d2 = j5;
            Double.isNaN(d);
            Double.isNaN(d2);
            float f2 = (float) (d / d2);
            f = f2;
            if (z4) {
                f = -f2;
            }
        }
        if (cCharAt == ',') {
            int i21 = this.f68bp + i14;
            this.f68bp = i21;
            this.f69ch = charAt(i21);
            this.matchStat = 3;
            this.token = 16;
            return f;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return 0.0f;
        }
        int i22 = i14 + 1;
        char cCharAt9 = charAt(this.f68bp + i14);
        if (cCharAt9 == ',') {
            this.token = 16;
            int i23 = this.f68bp + i22;
            this.f68bp = i23;
            this.f69ch = charAt(i23);
        } else if (cCharAt9 == ']') {
            this.token = 15;
            int i24 = this.f68bp + i22;
            this.f68bp = i24;
            this.f69ch = charAt(i24);
        } else if (cCharAt9 == '}') {
            this.token = 13;
            int i25 = this.f68bp + i22;
            this.f68bp = i25;
            this.f69ch = charAt(i25);
        } else {
            if (cCharAt9 != 26) {
                this.matchStat = -1;
                return 0.0f;
            }
            this.f68bp += i22 - 1;
            this.token = 20;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:115:0x040f, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0415, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0175, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x017b, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final float[] scanFieldFloatArray(char[] r7) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 1046
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray(char[]):float[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0419, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0422, code lost:
    
        return (float[][]) null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01b2, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01bb, code lost:
    
        return (float[][]) null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0352, code lost:
    
        r0 = r16 + 1;
        r11 = charAt(r6.f68bp + r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x036d, code lost:
    
        if (r10 == r7.length) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0370, code lost:
    
        r27 = new float[r10];
        java.lang.System.arraycopy(r7, 0, r27, 0, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0383, code lost:
    
        r27 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0386, code lost:
    
        r7 = r28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x038e, code lost:
    
        if (r20 < r28.length) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0391, code lost:
    
        r7 = new float[(r28.length * 3) / 2];
        java.lang.System.arraycopy(r27, 0, r7, 0, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x03a6, code lost:
    
        r0 = r20 + 1;
        r7[r20] = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x03b6, code lost:
    
        if (r11 != ',') goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x03b9, code lost:
    
        r11 = charAt(r6.f68bp + r0);
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x03d3, code lost:
    
        if (r11 != ']') goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x03d6, code lost:
    
        r13 = charAt(r6.f68bp + r0);
        r14 = r0;
        r12 = r0 + 1;
        r27 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x03fb, code lost:
    
        r0 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v150 */
    /* JADX WARN: Type inference failed for: r0v58, types: [float[][]] */
    /* JADX WARN: Type inference failed for: r0v68, types: [float[][]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final float[][] scanFieldFloatArray2(char[] r7) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 1315
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray2(char[]):float[][]");
    }

    public int scanFieldInt(char[] cArr) {
        int i;
        char cCharAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt2 = charAt(this.f68bp + length);
        boolean z = cCharAt2 == '-';
        int i3 = i2;
        if (z) {
            cCharAt2 = charAt(this.f68bp + i2);
            i3 = i2 + 1;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int i4 = cCharAt2 - '0';
        int i5 = i3;
        int i6 = i4;
        while (true) {
            i = i5 + 1;
            cCharAt = charAt(this.f68bp + i5);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            i6 = (i6 * 10) + (cCharAt - '0');
            i5 = i;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0;
        }
        if ((i6 < 0 || i > cArr.length + 14) && !(i6 == Integer.MIN_VALUE && i == 17 && z)) {
            this.matchStat = -1;
            return 0;
        }
        if (cCharAt == ',') {
            int i7 = this.f68bp + i;
            this.f68bp = i7;
            this.f69ch = charAt(i7);
            this.matchStat = 3;
            this.token = 16;
            int i8 = i6;
            if (z) {
                i8 = -i6;
            }
            return i8;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return 0;
        }
        int i9 = i + 1;
        char cCharAt3 = charAt(this.f68bp + i);
        if (cCharAt3 == ',') {
            this.token = 16;
            int i10 = this.f68bp + i9;
            this.f68bp = i10;
            this.f69ch = charAt(i10);
        } else if (cCharAt3 == ']') {
            this.token = 15;
            int i11 = this.f68bp + i9;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
        } else if (cCharAt3 == '}') {
            this.token = 13;
            int i12 = this.f68bp + i9;
            this.f68bp = i12;
            this.f69ch = charAt(i12);
        } else {
            if (cCharAt3 != 26) {
                this.matchStat = -1;
                return 0;
            }
            this.token = 20;
            this.f68bp += i9 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        int i13 = i6;
        if (z) {
            i13 = -i6;
        }
        return i13;
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x0241, code lost:
    
        r6.matchStat = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0247, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final int[] scanFieldIntArray(char[] r7) {
        /*
            Method dump skipped, instructions count: 584
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldIntArray(char[]):int[]");
    }

    public long scanFieldLong(char[] cArr) {
        boolean z;
        int i;
        char cCharAt;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i2 = length + 1;
        char cCharAt2 = charAt(this.f68bp + length);
        if (cCharAt2 == '-') {
            cCharAt2 = charAt(this.f68bp + i2);
            i2++;
            z = true;
        } else {
            z = false;
        }
        if (cCharAt2 < '0' || cCharAt2 > '9') {
            this.matchStat = -1;
            return 0L;
        }
        long j = cCharAt2 - '0';
        while (true) {
            i = i2 + 1;
            cCharAt = charAt(this.f68bp + i2);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j = (j * 10) + (cCharAt - '0');
            i2 = i;
        }
        if (cCharAt == '.') {
            this.matchStat = -1;
            return 0L;
        }
        if (!(i - cArr.length < 21 && (j >= 0 || (j == Long.MIN_VALUE && z)))) {
            this.matchStat = -1;
            return 0L;
        }
        if (cCharAt == ',') {
            int i3 = this.f68bp + i;
            this.f68bp = i3;
            this.f69ch = charAt(i3);
            this.matchStat = 3;
            this.token = 16;
            long j2 = j;
            if (z) {
                j2 = -j;
            }
            return j2;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return 0L;
        }
        int i4 = i + 1;
        char cCharAt3 = charAt(this.f68bp + i);
        if (cCharAt3 == ',') {
            this.token = 16;
            int i5 = this.f68bp + i4;
            this.f68bp = i5;
            this.f69ch = charAt(i5);
        } else if (cCharAt3 == ']') {
            this.token = 15;
            int i6 = this.f68bp + i4;
            this.f68bp = i6;
            this.f69ch = charAt(i6);
        } else if (cCharAt3 == '}') {
            this.token = 13;
            int i7 = this.f68bp + i4;
            this.f68bp = i7;
            this.f69ch = charAt(i7);
        } else {
            if (cCharAt3 != 26) {
                this.matchStat = -1;
                return 0L;
            }
            this.token = 20;
            this.f68bp += i4 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        long j3 = j;
        if (z) {
            j3 = -j;
        }
        return j3;
    }

    public String scanFieldString(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int length = cArr.length;
        if (charAt(this.f68bp + length) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int iIndexOf = indexOf('\"', this.f68bp + cArr.length + 1);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        int length2 = this.f68bp + cArr.length + 1;
        String strSubString = subString(length2, iIndexOf - length2);
        int i = iIndexOf;
        String string = strSubString;
        if (strSubString.indexOf(92) != -1) {
            int iIndexOf2 = iIndexOf;
            while (true) {
                i = iIndexOf2;
                int i2 = 0;
                for (int i3 = i - 1; i3 >= 0 && charAt(i3) == '\\'; i3--) {
                    i2++;
                }
                if (i2 % 2 == 0) {
                    break;
                }
                iIndexOf2 = indexOf('\"', i + 1);
            }
            int i4 = this.f68bp;
            int length3 = i - ((cArr.length + i4) + 1);
            string = readString(sub_chars(i4 + cArr.length + 1, length3), length3);
        }
        int i5 = this.f68bp;
        int length4 = length + 1 + (i - ((cArr.length + i5) + 1)) + 1;
        int i6 = length4 + 1;
        char cCharAt = charAt(i5 + length4);
        if (cCharAt == ',') {
            int i7 = this.f68bp + i6;
            this.f68bp = i7;
            this.f69ch = charAt(i7);
            this.matchStat = 3;
            return string;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int i8 = i6 + 1;
        char cCharAt2 = charAt(this.f68bp + i6);
        if (cCharAt2 == ',') {
            this.token = 16;
            int i9 = this.f68bp + i8;
            this.f68bp = i9;
            this.f69ch = charAt(i9);
        } else if (cCharAt2 == ']') {
            this.token = 15;
            int i10 = this.f68bp + i8;
            this.f68bp = i10;
            this.f69ch = charAt(i10);
        } else if (cCharAt2 == '}') {
            this.token = 13;
            int i11 = this.f68bp + i8;
            this.f68bp = i11;
            this.f69ch = charAt(i11);
        } else {
            if (cCharAt2 != 26) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.token = 20;
            this.f68bp += i8 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return string;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x01cb, code lost:
    
        if (r8 != ']') goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01d5, code lost:
    
        if (r0.size() != 0) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x01d8, code lost:
    
        r0 = r9 + 1;
        r9 = charAt(r5.f68bp + r9);
        r8 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x02e2, code lost:
    
        throw new com.alibaba.fastjson.JSONException("illega str");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r6, java.lang.Class<?> r7) {
        /*
            Method dump skipped, instructions count: 742
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldStringArray(char[], java.lang.Class):java.util.Collection");
    }

    public String[] scanFieldStringArray(char[] cArr, int i, SymbolTable symbolTable) {
        throw new UnsupportedOperationException();
    }

    public long scanFieldSymbol(char[] cArr) {
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return 0L;
        }
        int length = cArr.length;
        int i = length + 1;
        if (charAt(this.f68bp + length) != '\"') {
            this.matchStat = -1;
            return 0L;
        }
        long j = -3750763034362895579L;
        while (true) {
            int i2 = i + 1;
            char cCharAt = charAt(this.f68bp + i);
            if (cCharAt == '\"') {
                int i3 = i2 + 1;
                char cCharAt2 = charAt(this.f68bp + i2);
                if (cCharAt2 == ',') {
                    int i4 = this.f68bp + i3;
                    this.f68bp = i4;
                    this.f69ch = charAt(i4);
                    this.matchStat = 3;
                    return j;
                }
                if (cCharAt2 != '}') {
                    this.matchStat = -1;
                    return 0L;
                }
                int i5 = i3 + 1;
                char cCharAt3 = charAt(this.f68bp + i3);
                if (cCharAt3 == ',') {
                    this.token = 16;
                    int i6 = this.f68bp + i5;
                    this.f68bp = i6;
                    this.f69ch = charAt(i6);
                } else if (cCharAt3 == ']') {
                    this.token = 15;
                    int i7 = this.f68bp + i5;
                    this.f68bp = i7;
                    this.f69ch = charAt(i7);
                } else if (cCharAt3 == '}') {
                    this.token = 13;
                    int i8 = this.f68bp + i5;
                    this.f68bp = i8;
                    this.f69ch = charAt(i8);
                } else {
                    if (cCharAt3 != 26) {
                        this.matchStat = -1;
                        return 0L;
                    }
                    this.token = 20;
                    this.f68bp += i5 - 1;
                    this.f69ch = (char) 26;
                }
                this.matchStat = 4;
                return j;
            }
            j = (j ^ cCharAt) * 1099511628211L;
            if (cCharAt == '\\') {
                this.matchStat = -1;
                return 0L;
            }
            i = i2;
        }
    }

    public UUID scanFieldUUID(char[] cArr) {
        char cCharAt;
        int i;
        UUID uuid;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        this.matchStat = 0;
        if (!charArrayCompare(cArr)) {
            this.matchStat = -2;
            return null;
        }
        int length = cArr.length;
        int i15 = length + 1;
        char cCharAt2 = charAt(this.f68bp + length);
        if (cCharAt2 != '\"') {
            if (cCharAt2 == 'n') {
                int i16 = i15 + 1;
                if (charAt(this.f68bp + i15) == 'u') {
                    int i17 = i16 + 1;
                    if (charAt(this.f68bp + i16) == 'l') {
                        int i18 = i17 + 1;
                        if (charAt(this.f68bp + i17) == 'l') {
                            cCharAt = charAt(this.f68bp + i18);
                            i = i18 + 1;
                            uuid = null;
                        }
                    }
                }
            }
            this.matchStat = -1;
            return null;
        }
        int iIndexOf = indexOf('\"', this.f68bp + cArr.length + 1);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        int length2 = this.f68bp + cArr.length + 1;
        int i19 = iIndexOf - length2;
        if (i19 == 36) {
            long j = 0;
            for (int i20 = 0; i20 < 8; i20++) {
                char cCharAt3 = charAt(length2 + i20);
                if (cCharAt3 < '0' || cCharAt3 > '9') {
                    if (cCharAt3 >= 'a' && cCharAt3 <= 'f') {
                        i13 = cCharAt3 - 'a';
                    } else {
                        if (cCharAt3 < 'A' || cCharAt3 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i13 = cCharAt3 - 'A';
                    }
                    i14 = i13 + 10;
                } else {
                    i14 = cCharAt3 - '0';
                }
                j = (j << 4) | i14;
            }
            for (int i21 = 9; i21 < 13; i21++) {
                char cCharAt4 = charAt(length2 + i21);
                if (cCharAt4 < '0' || cCharAt4 > '9') {
                    if (cCharAt4 >= 'a' && cCharAt4 <= 'f') {
                        i11 = cCharAt4 - 'a';
                    } else {
                        if (cCharAt4 < 'A' || cCharAt4 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i11 = cCharAt4 - 'A';
                    }
                    i12 = i11 + 10;
                } else {
                    i12 = cCharAt4 - '0';
                }
                j = (j << 4) | i12;
            }
            long j2 = j;
            for (int i22 = 14; i22 < 18; i22++) {
                char cCharAt5 = charAt(length2 + i22);
                if (cCharAt5 < '0' || cCharAt5 > '9') {
                    if (cCharAt5 >= 'a' && cCharAt5 <= 'f') {
                        i9 = cCharAt5 - 'a';
                    } else {
                        if (cCharAt5 < 'A' || cCharAt5 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i9 = cCharAt5 - 'A';
                    }
                    i10 = i9 + 10;
                } else {
                    i10 = cCharAt5 - '0';
                }
                j2 = (j2 << 4) | i10;
            }
            int i23 = 19;
            long j3 = 0;
            while (true) {
                long j4 = j3;
                if (i23 < 23) {
                    char cCharAt6 = charAt(length2 + i23);
                    if (cCharAt6 < '0' || cCharAt6 > '9') {
                        if (cCharAt6 >= 'a' && cCharAt6 <= 'f') {
                            i7 = cCharAt6 - 'a';
                        } else {
                            if (cCharAt6 < 'A' || cCharAt6 > 'F') {
                                break;
                            }
                            i7 = cCharAt6 - 'A';
                        }
                        i8 = i7 + 10;
                    } else {
                        i8 = cCharAt6 - '0';
                    }
                    i23++;
                    j3 = (j4 << 4) | i8;
                } else {
                    for (int i24 = 24; i24 < 36; i24++) {
                        char cCharAt7 = charAt(length2 + i24);
                        if (cCharAt7 < '0' || cCharAt7 > '9') {
                            if (cCharAt7 >= 'a' && cCharAt7 <= 'f') {
                                i5 = cCharAt7 - 'a';
                            } else {
                                if (cCharAt7 < 'A' || cCharAt7 > 'F') {
                                    this.matchStat = -2;
                                    return null;
                                }
                                i5 = cCharAt7 - 'A';
                            }
                            i6 = i5 + 10;
                        } else {
                            i6 = cCharAt7 - '0';
                        }
                        j4 = (j4 << 4) | i6;
                    }
                    UUID uuid2 = new UUID(j2, j4);
                    int i25 = this.f68bp;
                    int length3 = i15 + (iIndexOf - ((cArr.length + i25) + 1)) + 1;
                    i = length3 + 1;
                    cCharAt = charAt(i25 + length3);
                    uuid = uuid2;
                }
            }
            this.matchStat = -2;
            return null;
        }
        if (i19 != 32) {
            this.matchStat = -1;
            return null;
        }
        long j5 = 0;
        for (int i26 = 0; i26 < 16; i26++) {
            char cCharAt8 = charAt(length2 + i26);
            if (cCharAt8 < '0' || cCharAt8 > '9') {
                if (cCharAt8 >= 'a' && cCharAt8 <= 'f') {
                    i3 = cCharAt8 - 'a';
                } else {
                    if (cCharAt8 < 'A' || cCharAt8 > 'F') {
                        this.matchStat = -2;
                        return null;
                    }
                    i3 = cCharAt8 - 'A';
                }
                i4 = i3 + 10;
            } else {
                i4 = cCharAt8 - '0';
            }
            j5 = (j5 << 4) | i4;
        }
        long j6 = 0;
        for (int i27 = 16; i27 < 32; i27++) {
            char cCharAt9 = charAt(length2 + i27);
            if (cCharAt9 >= '0' && cCharAt9 <= '9') {
                i2 = cCharAt9 - '0';
            } else if (cCharAt9 >= 'a' && cCharAt9 <= 'f') {
                i2 = (cCharAt9 - 'a') + 10;
            } else {
                if (cCharAt9 < 'A' || cCharAt9 > 'F') {
                    this.matchStat = -2;
                    return null;
                }
                i2 = (cCharAt9 - 'A') + 10;
            }
            j6 = (j6 << 4) | i2;
        }
        UUID uuid3 = new UUID(j5, j6);
        int i28 = this.f68bp;
        int length4 = i15 + (iIndexOf - ((cArr.length + i28) + 1)) + 1;
        i = length4 + 1;
        cCharAt = charAt(i28 + length4);
        uuid = uuid3;
        if (cCharAt == ',') {
            int i29 = this.f68bp + i;
            this.f68bp = i29;
            this.f69ch = charAt(i29);
            this.matchStat = 3;
            return uuid;
        }
        if (cCharAt != '}') {
            this.matchStat = -1;
            return null;
        }
        int i30 = i + 1;
        char cCharAt10 = charAt(this.f68bp + i);
        if (cCharAt10 == ',') {
            this.token = 16;
            int i31 = this.f68bp + i30;
            this.f68bp = i31;
            this.f69ch = charAt(i31);
        } else if (cCharAt10 == ']') {
            this.token = 15;
            int i32 = this.f68bp + i30;
            this.f68bp = i32;
            this.f69ch = charAt(i32);
        } else if (cCharAt10 == '}') {
            this.token = 13;
            int i33 = this.f68bp + i30;
            this.f68bp = i33;
            this.f69ch = charAt(i33);
        } else {
            if (cCharAt10 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.f68bp += i30 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return uuid;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final float scanFloat(char c) throws NumberFormatException {
        int i;
        int i2;
        char cCharAt;
        int i3;
        int i4;
        int i5;
        float f;
        int i6;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.f68bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.f68bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        char cCharAt3 = cCharAt2;
        int i7 = i;
        if (z2) {
            cCharAt3 = charAt(this.f68bp + i);
            i7 = i + 1;
        }
        if (cCharAt3 < '0' || cCharAt3 > '9') {
            if (cCharAt3 != 'n' || charAt(this.f68bp + i7) != 'u' || charAt(this.f68bp + i7 + 1) != 'l' || charAt(this.f68bp + i7 + 2) != 'l') {
                this.matchStat = -1;
                return 0.0f;
            }
            this.matchStat = 5;
            int i8 = i7 + 3;
            int i9 = i8 + 1;
            char cCharAt4 = charAt(this.f68bp + i8);
            char cCharAt5 = cCharAt4;
            int i10 = i9;
            if (z) {
                cCharAt5 = cCharAt4;
                i10 = i9;
                if (cCharAt4 == '\"') {
                    cCharAt5 = charAt(this.f68bp + i9);
                    i10 = i9 + 1;
                }
            }
            while (cCharAt5 != ',') {
                if (cCharAt5 == ']') {
                    int i11 = this.f68bp + i10;
                    this.f68bp = i11;
                    this.f69ch = charAt(i11);
                    this.matchStat = 5;
                    this.token = 15;
                    return 0.0f;
                }
                if (!isWhitespace(cCharAt5)) {
                    this.matchStat = -1;
                    return 0.0f;
                }
                cCharAt5 = charAt(this.f68bp + i10);
                i10++;
            }
            int i12 = this.f68bp + i10;
            this.f68bp = i12;
            this.f69ch = charAt(i12);
            this.matchStat = 5;
            this.token = 16;
            return 0.0f;
        }
        long j = cCharAt3 - '0';
        while (true) {
            i2 = i7 + 1;
            cCharAt = charAt(this.f68bp + i7);
            if (cCharAt < '0' || cCharAt > '9') {
                break;
            }
            j = (j * 10) + (cCharAt - '0');
            i7 = i2;
        }
        long j2 = 1;
        long j3 = j;
        int i13 = i2;
        if (cCharAt == '.') {
            int i14 = i2 + 1;
            char cCharAt6 = charAt(this.f68bp + i2);
            if (cCharAt6 >= '0' && cCharAt6 <= '9') {
                long j4 = (j * 10) + (cCharAt6 - '0');
                long j5 = 10;
                while (true) {
                    int i15 = i14 + 1;
                    char cCharAt7 = charAt(this.f68bp + i14);
                    j3 = j4;
                    cCharAt = cCharAt7;
                    i13 = i15;
                    j2 = j5;
                    if (cCharAt7 < '0') {
                        break;
                    }
                    j3 = j4;
                    cCharAt = cCharAt7;
                    i13 = i15;
                    j2 = j5;
                    if (cCharAt7 > '9') {
                        break;
                    }
                    j4 = (j4 * 10) + (cCharAt7 - '0');
                    j5 *= 10;
                    i14 = i15;
                }
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        }
        boolean z3 = cCharAt == 'e' || cCharAt == 'E';
        long j6 = j3;
        boolean z4 = z;
        boolean z5 = z2;
        boolean z6 = z3;
        int i16 = i13;
        if (z3) {
            int i17 = i13 + 1;
            char cCharAt8 = charAt(this.f68bp + i13);
            if (cCharAt8 == '+' || cCharAt8 == '-') {
                i6 = i17 + 1;
                cCharAt8 = charAt(this.f68bp + i17);
            } else {
                i6 = i17;
            }
            while (true) {
                j6 = j3;
                z4 = z;
                cCharAt = cCharAt8;
                z5 = z2;
                z6 = z3;
                i16 = i6;
                if (cCharAt8 < '0') {
                    break;
                }
                j6 = j3;
                z4 = z;
                cCharAt = cCharAt8;
                z5 = z2;
                z6 = z3;
                i16 = i6;
                if (cCharAt8 > '9') {
                    break;
                }
                cCharAt8 = charAt(this.f68bp + i6);
                i6++;
            }
        }
        if (!z4) {
            i3 = this.f68bp;
            i4 = ((i3 + i16) - i3) - 1;
            i5 = i16;
        } else {
            if (cCharAt != '\"') {
                this.matchStat = -1;
                return 0.0f;
            }
            i5 = i16 + 1;
            cCharAt = charAt(this.f68bp + i16);
            int i18 = this.f68bp;
            i3 = i18 + 1;
            i4 = ((i18 + i5) - i3) - 2;
        }
        if (z6 || i4 >= 17) {
            f = Float.parseFloat(subString(i3, i4));
        } else {
            double d = j6;
            double d2 = j2;
            Double.isNaN(d);
            Double.isNaN(d2);
            float f2 = (float) (d / d2);
            f = f2;
            if (z5) {
                f = -f2;
            }
        }
        if (cCharAt != c) {
            this.matchStat = -1;
            return f;
        }
        int i19 = this.f68bp + i5;
        this.f68bp = i19;
        this.f69ch = charAt(i19);
        this.matchStat = 3;
        this.token = 16;
        return f;
    }

    public final void scanHex() {
        char next;
        if (this.f69ch != 'x') {
            throw new JSONException("illegal state. " + this.f69ch);
        }
        next();
        if (this.f69ch != '\'') {
            throw new JSONException("illegal state. " + this.f69ch);
        }
        this.f70np = this.f68bp;
        next();
        if (this.f69ch == '\'') {
            next();
            this.token = 26;
            return;
        }
        while (true) {
            next = next();
            if ((next < '0' || next > '9') && (next < 'A' || next > 'F')) {
                break;
            } else {
                this.f71sp++;
            }
        }
        if (next == '\'') {
            this.f71sp++;
            next();
            this.token = 26;
        } else {
            throw new JSONException("illegal state. " + next);
        }
    }

    public final void scanIdent() {
        this.f70np = this.f68bp - 1;
        this.hasSpecial = false;
        do {
            this.f71sp++;
            next();
        } while (Character.isLetterOrDigit(this.f69ch));
        String strStringVal = stringVal();
        if ("null".equalsIgnoreCase(strStringVal)) {
            this.token = 8;
            return;
        }
        if ("new".equals(strStringVal)) {
            this.token = 9;
            return;
        }
        if ("true".equals(strStringVal)) {
            this.token = 6;
            return;
        }
        if ("false".equals(strStringVal)) {
            this.token = 7;
            return;
        }
        if ("undefined".equals(strStringVal)) {
            this.token = 23;
            return;
        }
        if ("Set".equals(strStringVal)) {
            this.token = 21;
        } else if ("TreeSet".equals(strStringVal)) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int scanInt(char c) {
        int i;
        int i2;
        char cCharAt;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.f68bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.f68bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        char cCharAt3 = cCharAt2;
        int i3 = i;
        if (z2) {
            cCharAt3 = charAt(this.f68bp + i);
            i3 = i + 1;
        }
        if (cCharAt3 >= '0' && cCharAt3 <= '9') {
            int i4 = cCharAt3 - '0';
            while (true) {
                i2 = i3 + 1;
                cCharAt = charAt(this.f68bp + i3);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                i4 = (i4 * 10) + (cCharAt - '0');
                i3 = i2;
            }
            if (cCharAt == '.') {
                this.matchStat = -1;
                return 0;
            }
            if (i4 < 0) {
                this.matchStat = -1;
                return 0;
            }
            while (cCharAt != c) {
                if (!isWhitespace(cCharAt)) {
                    this.matchStat = -1;
                    int i5 = i4;
                    if (z2) {
                        i5 = -i4;
                    }
                    return i5;
                }
                cCharAt = charAt(this.f68bp + i2);
                i2++;
            }
            int i6 = this.f68bp + i2;
            this.f68bp = i6;
            this.f69ch = charAt(i6);
            this.matchStat = 3;
            this.token = 16;
            int i7 = i4;
            if (z2) {
                i7 = -i4;
            }
            return i7;
        }
        if (cCharAt3 != 'n' || charAt(this.f68bp + i3) != 'u' || charAt(this.f68bp + i3 + 1) != 'l' || charAt(this.f68bp + i3 + 2) != 'l') {
            this.matchStat = -1;
            return 0;
        }
        this.matchStat = 5;
        int i8 = i3 + 3;
        int i9 = i8 + 1;
        char cCharAt4 = charAt(this.f68bp + i8);
        char cCharAt5 = cCharAt4;
        int i10 = i9;
        if (z) {
            cCharAt5 = cCharAt4;
            i10 = i9;
            if (cCharAt4 == '\"') {
                i10 = i9 + 1;
                cCharAt5 = charAt(this.f68bp + i9);
            }
        }
        while (cCharAt5 != ',') {
            if (cCharAt5 == ']') {
                int i11 = this.f68bp + i10;
                this.f68bp = i11;
                this.f69ch = charAt(i11);
                this.matchStat = 5;
                this.token = 15;
                return 0;
            }
            if (!isWhitespace(cCharAt5)) {
                this.matchStat = -1;
                return 0;
            }
            cCharAt5 = charAt(this.f68bp + i10);
            i10++;
        }
        int i12 = this.f68bp + i10;
        this.f68bp = i12;
        this.f69ch = charAt(i12);
        this.matchStat = 5;
        this.token = 16;
        return 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public long scanLong(char c) {
        int i;
        int i2;
        char cCharAt;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.f68bp + 0);
        boolean z = cCharAt2 == '\"';
        if (z) {
            cCharAt2 = charAt(this.f68bp + 1);
            i = 2;
        } else {
            i = 1;
        }
        boolean z2 = cCharAt2 == '-';
        char cCharAt3 = cCharAt2;
        int i3 = i;
        if (z2) {
            cCharAt3 = charAt(this.f68bp + i);
            i3 = i + 1;
        }
        if (cCharAt3 >= '0' && cCharAt3 <= '9') {
            long j = cCharAt3 - '0';
            while (true) {
                i2 = i3 + 1;
                cCharAt = charAt(this.f68bp + i3);
                if (cCharAt < '0' || cCharAt > '9') {
                    break;
                }
                j = (j * 10) + (cCharAt - '0');
                i3 = i2;
            }
            if (cCharAt == '.') {
                this.matchStat = -1;
                return 0L;
            }
            if (!(j >= 0 || (j == Long.MIN_VALUE && z2))) {
                throw new NumberFormatException(subString(this.f68bp, i2 - 1));
            }
            if (z) {
                if (cCharAt != '\"') {
                    this.matchStat = -1;
                    return 0L;
                }
                cCharAt = charAt(this.f68bp + i2);
                i2++;
            }
            while (cCharAt != c) {
                if (!isWhitespace(cCharAt)) {
                    this.matchStat = -1;
                    return j;
                }
                cCharAt = charAt(this.f68bp + i2);
                i2++;
            }
            int i4 = this.f68bp + i2;
            this.f68bp = i4;
            this.f69ch = charAt(i4);
            this.matchStat = 3;
            this.token = 16;
            long j2 = j;
            if (z2) {
                j2 = -j;
            }
            return j2;
        }
        if (cCharAt3 != 'n' || charAt(this.f68bp + i3) != 'u' || charAt(this.f68bp + i3 + 1) != 'l' || charAt(this.f68bp + i3 + 2) != 'l') {
            this.matchStat = -1;
            return 0L;
        }
        this.matchStat = 5;
        int i5 = i3 + 3;
        int i6 = i5 + 1;
        char cCharAt4 = charAt(this.f68bp + i5);
        char cCharAt5 = cCharAt4;
        int i7 = i6;
        if (z) {
            cCharAt5 = cCharAt4;
            i7 = i6;
            if (cCharAt4 == '\"') {
                i7 = i6 + 1;
                cCharAt5 = charAt(this.f68bp + i6);
            }
        }
        while (cCharAt5 != ',') {
            if (cCharAt5 == ']') {
                int i8 = this.f68bp + i7;
                this.f68bp = i8;
                this.f69ch = charAt(i8);
                this.matchStat = 5;
                this.token = 15;
                return 0L;
            }
            if (!isWhitespace(cCharAt5)) {
                this.matchStat = -1;
                return 0L;
            }
            cCharAt5 = charAt(this.f68bp + i7);
            i7++;
        }
        int i9 = this.f68bp + i7;
        this.f68bp = i9;
        this.f69ch = charAt(i9);
        this.matchStat = 5;
        this.token = 16;
        return 0L;
    }

    public final void scanNullOrNew() {
        scanNullOrNew(true);
    }

    public final void scanNullOrNew(boolean z) {
        char c;
        if (this.f69ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        char c2 = this.f69ch;
        if (c2 != 'u') {
            if (c2 != 'e') {
                throw new JSONException("error parse new");
            }
            next();
            if (this.f69ch != 'w') {
                throw new JSONException("error parse new");
            }
            next();
            char c3 = this.f69ch;
            if (c3 != ' ' && c3 != ',' && c3 != '}' && c3 != ']' && c3 != '\n' && c3 != '\r' && c3 != '\t' && c3 != 26 && c3 != '\f' && c3 != '\b') {
                throw new JSONException("scan new error");
            }
            this.token = 9;
            return;
        }
        next();
        if (this.f69ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        if (this.f69ch != 'l') {
            throw new JSONException("error parse null");
        }
        next();
        char c4 = this.f69ch;
        if (c4 != ' ' && c4 != ',' && c4 != '}' && c4 != ']' && c4 != '\n' && c4 != '\r' && c4 != '\t' && c4 != 26 && ((c4 != ':' || !z) && (c = this.f69ch) != '\f' && c != '\b')) {
            throw new JSONException("scan null error");
        }
        this.token = 8;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x017e  */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void scanNumber() {
        /*
            Method dump skipped, instructions count: 417
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanNumber():void");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanString(char c) {
        this.matchStat = 0;
        char cCharAt = charAt(this.f68bp + 0);
        if (cCharAt == 'n') {
            if (charAt(this.f68bp + 1) != 'u' || charAt(this.f68bp + 1 + 1) != 'l' || charAt(this.f68bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            }
            if (charAt(this.f68bp + 4) != c) {
                this.matchStat = -1;
                return null;
            }
            int i = this.f68bp + 5;
            this.f68bp = i;
            this.f69ch = charAt(i);
            this.matchStat = 3;
            return null;
        }
        int i2 = 1;
        while (cCharAt != '\"') {
            if (!isWhitespace(cCharAt)) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            cCharAt = charAt(this.f68bp + i2);
            i2++;
        }
        int i3 = this.f68bp + i2;
        int iIndexOf = indexOf('\"', i3);
        if (iIndexOf == -1) {
            throw new JSONException("unclosed str");
        }
        String strSubString = subString(this.f68bp + i2, iIndexOf - i3);
        int i4 = iIndexOf;
        String string = strSubString;
        if (strSubString.indexOf(92) != -1) {
            while (true) {
                int i5 = 0;
                for (int i6 = iIndexOf - 1; i6 >= 0 && charAt(i6) == '\\'; i6--) {
                    i5++;
                }
                if (i5 % 2 == 0) {
                    break;
                }
                iIndexOf = indexOf('\"', iIndexOf + 1);
            }
            int i7 = iIndexOf - i3;
            string = readString(sub_chars(this.f68bp + 1, i7), i7);
            i4 = iIndexOf;
        }
        int i8 = i2 + (i4 - i3) + 1;
        int i9 = i8 + 1;
        char cCharAt2 = charAt(this.f68bp + i8);
        while (cCharAt2 != c) {
            if (!isWhitespace(cCharAt2)) {
                this.matchStat = -1;
                return string;
            }
            cCharAt2 = charAt(this.f68bp + i9);
            i9++;
        }
        int i10 = this.f68bp + i9;
        this.f68bp = i10;
        this.f69ch = charAt(i10);
        this.matchStat = 3;
        this.token = 16;
        return string;
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x0258  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0268  */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void scanString() {
        /*
            Method dump skipped, instructions count: 840
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanString():void");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void scanStringArray(Collection<String> collection, char c) {
        int i;
        char cCharAt;
        int i2;
        char cCharAt2;
        this.matchStat = 0;
        char cCharAt3 = charAt(this.f68bp + 0);
        if (cCharAt3 == 'n' && charAt(this.f68bp + 1) == 'u' && charAt(this.f68bp + 1 + 1) == 'l' && charAt(this.f68bp + 1 + 2) == 'l' && charAt(this.f68bp + 1 + 3) == c) {
            int i3 = this.f68bp + 5;
            this.f68bp = i3;
            this.f69ch = charAt(i3);
            this.matchStat = 5;
            return;
        }
        if (cCharAt3 != '[') {
            this.matchStat = -1;
            return;
        }
        char cCharAt4 = charAt(this.f68bp + 1);
        int i4 = 2;
        while (true) {
            if (cCharAt4 == 'n' && charAt(this.f68bp + i4) == 'u' && charAt(this.f68bp + i4 + 1) == 'l' && charAt(this.f68bp + i4 + 2) == 'l') {
                int i5 = i4 + 3;
                i = i5 + 1;
                cCharAt = charAt(this.f68bp + i5);
                collection.add(null);
            } else {
                if (cCharAt4 == ']' && collection.size() == 0) {
                    i2 = i4 + 1;
                    cCharAt2 = charAt(this.f68bp + i4);
                    break;
                }
                if (cCharAt4 != '\"') {
                    this.matchStat = -1;
                    return;
                }
                int i6 = this.f68bp + i4;
                int iIndexOf = indexOf('\"', i6);
                if (iIndexOf == -1) {
                    throw new JSONException("unclosed str");
                }
                String strSubString = subString(this.f68bp + i4, iIndexOf - i6);
                String string = strSubString;
                int i7 = iIndexOf;
                if (strSubString.indexOf(92) != -1) {
                    int iIndexOf2 = iIndexOf;
                    while (true) {
                        i7 = iIndexOf2;
                        int i8 = 0;
                        for (int i9 = i7 - 1; i9 >= 0 && charAt(i9) == '\\'; i9--) {
                            i8++;
                        }
                        if (i8 % 2 == 0) {
                            break;
                        } else {
                            iIndexOf2 = indexOf('\"', i7 + 1);
                        }
                    }
                    int i10 = i7 - i6;
                    string = readString(sub_chars(this.f68bp + i4, i10), i10);
                }
                int i11 = this.f68bp;
                int i12 = i4 + (i7 - (i11 + i4)) + 1;
                i = i12 + 1;
                cCharAt = charAt(i11 + i12);
                collection.add(string);
            }
            if (cCharAt == ',') {
                i4 = i + 1;
                cCharAt4 = charAt(this.f68bp + i);
            } else {
                if (cCharAt != ']') {
                    this.matchStat = -1;
                    return;
                }
                char cCharAt5 = charAt(this.f68bp + i);
                i2 = i + 1;
                cCharAt2 = cCharAt5;
            }
        }
        if (cCharAt2 != c) {
            this.matchStat = -1;
            return;
        }
        int i13 = this.f68bp + i2;
        this.f68bp = i13;
        this.f69ch = charAt(i13);
        this.matchStat = 3;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        char c = this.f69ch;
        if (c == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (c == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        }
        if (c == '}') {
            next();
            this.token = 13;
            return null;
        }
        if (c == ',') {
            next();
            this.token = 16;
            return null;
        }
        if (c == 26) {
            this.token = 20;
            return null;
        }
        if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        }
        throw new JSONException("syntax error");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable, char c) throws NumberFormatException {
        String strAddSymbol;
        this.f70np = this.f68bp;
        this.f71sp = 0;
        boolean z = false;
        int i = 0;
        while (true) {
            char next = next();
            if (next == c) {
                this.token = 4;
                if (z) {
                    strAddSymbol = symbolTable.addSymbol(this.sbuf, 0, this.f71sp, i);
                } else {
                    int i2 = this.f70np;
                    strAddSymbol = addSymbol(i2 == -1 ? 0 : i2 + 1, this.f71sp, i, symbolTable);
                }
                this.f71sp = 0;
                next();
                return strAddSymbol;
            }
            if (next == 26) {
                throw new JSONException("unclosed.str");
            }
            if (next == '\\') {
                boolean z2 = z;
                if (!z) {
                    int i3 = this.f71sp;
                    char[] cArr = this.sbuf;
                    if (i3 >= cArr.length) {
                        int length = cArr.length * 2;
                        if (i3 > length) {
                            length = i3;
                        }
                        char[] cArr2 = new char[length];
                        char[] cArr3 = this.sbuf;
                        System.arraycopy(cArr3, 0, cArr2, 0, cArr3.length);
                        this.sbuf = cArr2;
                    }
                    arrayCopy(this.f70np + 1, this.sbuf, 0, this.f71sp);
                    z2 = true;
                }
                char next2 = next();
                if (next2 == '\"') {
                    i = (i * 31) + 34;
                    putChar('\"');
                    z = z2;
                } else if (next2 != '\'') {
                    if (next2 != 'F') {
                        if (next2 == '\\') {
                            i = (i * 31) + 92;
                            putChar('\\');
                            z = z2;
                        } else if (next2 == 'b') {
                            i = (i * 31) + 8;
                            putChar('\b');
                            z = z2;
                        } else if (next2 != 'f') {
                            if (next2 == 'n') {
                                i = (i * 31) + 10;
                                putChar('\n');
                                z = z2;
                            } else if (next2 == 'r') {
                                i = (i * 31) + 13;
                                putChar('\r');
                                z = z2;
                            } else if (next2 != 'x') {
                                switch (next2) {
                                    case '/':
                                        i = (i * 31) + 47;
                                        putChar('/');
                                        z = z2;
                                        break;
                                    case '0':
                                        i = (i * 31) + next2;
                                        putChar((char) 0);
                                        z = z2;
                                        break;
                                    case '1':
                                        i = (i * 31) + next2;
                                        putChar((char) 1);
                                        z = z2;
                                        break;
                                    case '2':
                                        i = (i * 31) + next2;
                                        putChar((char) 2);
                                        z = z2;
                                        break;
                                    case '3':
                                        i = (i * 31) + next2;
                                        putChar((char) 3);
                                        z = z2;
                                        break;
                                    case '4':
                                        i = (i * 31) + next2;
                                        putChar((char) 4);
                                        z = z2;
                                        break;
                                    case '5':
                                        i = (i * 31) + next2;
                                        putChar((char) 5);
                                        z = z2;
                                        break;
                                    case '6':
                                        i = (i * 31) + next2;
                                        putChar((char) 6);
                                        z = z2;
                                        break;
                                    case '7':
                                        i = (i * 31) + next2;
                                        putChar((char) 7);
                                        z = z2;
                                        break;
                                    default:
                                        switch (next2) {
                                            case 't':
                                                i = (i * 31) + 9;
                                                putChar('\t');
                                                z = z2;
                                                break;
                                            case 'u':
                                                int i4 = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                                                i = (i * 31) + i4;
                                                putChar((char) i4);
                                                z = z2;
                                                break;
                                            case 'v':
                                                i = (i * 31) + 11;
                                                putChar((char) 11);
                                                z = z2;
                                                break;
                                            default:
                                                this.f69ch = next2;
                                                throw new JSONException("unclosed.str.lit");
                                        }
                                }
                            } else {
                                char next3 = next();
                                this.f69ch = next3;
                                char next4 = next();
                                this.f69ch = next4;
                                int[] iArr = digits;
                                char c2 = (char) ((iArr[next3] * 16) + iArr[next4]);
                                i = (i * 31) + c2;
                                putChar(c2);
                                z = z2;
                            }
                        }
                    }
                    i = (i * 31) + 12;
                    putChar('\f');
                    z = z2;
                } else {
                    i = (i * 31) + 39;
                    putChar('\'');
                    z = z2;
                }
            } else {
                i = (i * 31) + next;
                if (z) {
                    int i5 = this.f71sp;
                    char[] cArr4 = this.sbuf;
                    if (i5 == cArr4.length) {
                        putChar(next);
                    } else {
                        this.f71sp = i5 + 1;
                        cArr4[i5] = next;
                    }
                } else {
                    this.f71sp++;
                }
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean z = false;
        if (this.token == 1 && this.pos == 0 && this.f68bp == 1) {
            this.f68bp = 0;
        }
        boolean[] zArr = IOUtils.firstIdentifierFlags;
        char c = this.f69ch;
        if (c >= zArr.length || zArr[c]) {
            z = true;
        }
        if (!z) {
            throw new JSONException("illegal identifier : " + this.f69ch + info());
        }
        boolean[] zArr2 = IOUtils.identifierFlags;
        this.f70np = this.f68bp;
        this.f71sp = 1;
        int i = c;
        while (true) {
            char next = next();
            if (next < zArr2.length && !zArr2[next]) {
                break;
            }
            i = (i * 31) + next;
            this.f71sp++;
        }
        this.f69ch = charAt(this.f68bp);
        this.token = 18;
        if (this.f71sp == 4 && i == 3392903 && charAt(this.f70np) == 'n' && charAt(this.f70np + 1) == 'u' && charAt(this.f70np + 2) == 'l' && charAt(this.f70np + 3) == 'l') {
            return null;
        }
        return symbolTable == null ? subString(this.f70np, this.f71sp) : addSymbol(this.f70np, this.f71sp, i, symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanSymbolWithSeperator(SymbolTable symbolTable, char c) {
        int i = 0;
        this.matchStat = 0;
        char cCharAt = charAt(this.f68bp + 0);
        if (cCharAt == 'n') {
            if (charAt(this.f68bp + 1) != 'u' || charAt(this.f68bp + 1 + 1) != 'l' || charAt(this.f68bp + 1 + 2) != 'l') {
                this.matchStat = -1;
                return null;
            }
            if (charAt(this.f68bp + 4) != c) {
                this.matchStat = -1;
                return null;
            }
            int i2 = this.f68bp + 5;
            this.f68bp = i2;
            this.f69ch = charAt(i2);
            this.matchStat = 3;
            return null;
        }
        if (cCharAt != '\"') {
            this.matchStat = -1;
            return null;
        }
        int i3 = 1;
        while (true) {
            int i4 = i3;
            int i5 = i4 + 1;
            char cCharAt2 = charAt(this.f68bp + i4);
            if (cCharAt2 == '\"') {
                int i6 = this.f68bp;
                int i7 = i6 + 0 + 1;
                String strAddSymbol = addSymbol(i7, ((i6 + i5) - i7) - 1, i, symbolTable);
                int i8 = i5 + 1;
                char cCharAt3 = charAt(this.f68bp + i5);
                while (cCharAt3 != c) {
                    if (!isWhitespace(cCharAt3)) {
                        this.matchStat = -1;
                        return strAddSymbol;
                    }
                    cCharAt3 = charAt(this.f68bp + i8);
                    i8++;
                }
                int i9 = this.f68bp + i8;
                this.f68bp = i9;
                this.f69ch = charAt(i9);
                this.matchStat = 3;
                return strAddSymbol;
            }
            i = (i * 31) + cCharAt2;
            if (cCharAt2 == '\\') {
                this.matchStat = -1;
                return null;
            }
            i3 = i5;
        }
    }

    public final void scanTrue() {
        if (this.f69ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.f69ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.f69ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.f69ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        char c = this.f69ch;
        if (c != ' ' && c != ',' && c != '}' && c != ']' && c != '\n' && c != '\r' && c != '\t' && c != 26 && c != '\f' && c != '\b' && c != ':' && c != '/') {
            throw new JSONException("scan true error");
        }
        this.token = 6;
    }

    public final int scanType(String str) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int length = this.f68bp + typeFieldName.length;
        int length2 = str.length();
        for (int i = 0; i < length2; i++) {
            if (str.charAt(i) != charAt(length + i)) {
                return -1;
            }
        }
        int i2 = length + length2;
        if (charAt(i2) != '\"') {
            return -1;
        }
        int i3 = i2 + 1;
        char cCharAt = charAt(i3);
        this.f69ch = cCharAt;
        if (cCharAt == ',') {
            int i4 = i3 + 1;
            this.f69ch = charAt(i4);
            this.f68bp = i4;
            this.token = 16;
            return 3;
        }
        int i5 = i3;
        if (cCharAt == '}') {
            i5 = i3 + 1;
            char cCharAt2 = charAt(i5);
            this.f69ch = cCharAt2;
            if (cCharAt2 == ',') {
                this.token = 16;
                i5++;
                this.f69ch = charAt(i5);
            } else if (cCharAt2 == ']') {
                this.token = 15;
                i5++;
                this.f69ch = charAt(i5);
            } else if (cCharAt2 == '}') {
                this.token = 13;
                i5++;
                this.f69ch = charAt(i5);
            } else {
                if (cCharAt2 != 26) {
                    return -1;
                }
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.f68bp = i5;
        return this.matchStat;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanTypeName(SymbolTable symbolTable) {
        return null;
    }

    public UUID scanUUID(char c) {
        int i;
        char cCharAt;
        UUID uuid;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        this.matchStat = 0;
        char cCharAt2 = charAt(this.f68bp + 0);
        if (cCharAt2 == '\"') {
            int iIndexOf = indexOf('\"', this.f68bp + 1);
            if (iIndexOf == -1) {
                throw new JSONException("unclosed str");
            }
            int i15 = this.f68bp + 1;
            int i16 = iIndexOf - i15;
            if (i16 == 36) {
                long j = 0;
                for (int i17 = 0; i17 < 8; i17++) {
                    char cCharAt3 = charAt(i15 + i17);
                    if (cCharAt3 < '0' || cCharAt3 > '9') {
                        if (cCharAt3 >= 'a' && cCharAt3 <= 'f') {
                            i13 = cCharAt3 - 'a';
                        } else {
                            if (cCharAt3 < 'A' || cCharAt3 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i13 = cCharAt3 - 'A';
                        }
                        i14 = i13 + 10;
                    } else {
                        i14 = cCharAt3 - '0';
                    }
                    j = (j << 4) | i14;
                }
                for (int i18 = 9; i18 < 13; i18++) {
                    char cCharAt4 = charAt(i15 + i18);
                    if (cCharAt4 < '0' || cCharAt4 > '9') {
                        if (cCharAt4 >= 'a' && cCharAt4 <= 'f') {
                            i11 = cCharAt4 - 'a';
                        } else {
                            if (cCharAt4 < 'A' || cCharAt4 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i11 = cCharAt4 - 'A';
                        }
                        i12 = i11 + 10;
                    } else {
                        i12 = cCharAt4 - '0';
                    }
                    j = (j << 4) | i12;
                }
                long j2 = j;
                for (int i19 = 14; i19 < 18; i19++) {
                    char cCharAt5 = charAt(i15 + i19);
                    if (cCharAt5 < '0' || cCharAt5 > '9') {
                        if (cCharAt5 >= 'a' && cCharAt5 <= 'f') {
                            i9 = cCharAt5 - 'a';
                        } else {
                            if (cCharAt5 < 'A' || cCharAt5 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i9 = cCharAt5 - 'A';
                        }
                        i10 = i9 + 10;
                    } else {
                        i10 = cCharAt5 - '0';
                    }
                    j2 = (j2 << 4) | i10;
                }
                long j3 = 0;
                for (int i20 = 19; i20 < 23; i20++) {
                    char cCharAt6 = charAt(i15 + i20);
                    if (cCharAt6 < '0' || cCharAt6 > '9') {
                        if (cCharAt6 >= 'a' && cCharAt6 <= 'f') {
                            i7 = cCharAt6 - 'a';
                        } else {
                            if (cCharAt6 < 'A' || cCharAt6 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i7 = cCharAt6 - 'A';
                        }
                        i8 = i7 + 10;
                    } else {
                        i8 = cCharAt6 - '0';
                    }
                    j3 = (j3 << 4) | i8;
                }
                for (int i21 = 24; i21 < 36; i21++) {
                    char cCharAt7 = charAt(i15 + i21);
                    if (cCharAt7 < '0' || cCharAt7 > '9') {
                        if (cCharAt7 >= 'a' && cCharAt7 <= 'f') {
                            i5 = cCharAt7 - 'a';
                        } else {
                            if (cCharAt7 < 'A' || cCharAt7 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i5 = cCharAt7 - 'A';
                        }
                        i6 = i5 + 10;
                    } else {
                        i6 = cCharAt7 - '0';
                    }
                    j3 = (j3 << 4) | i6;
                }
                uuid = new UUID(j2, j3);
                int i22 = this.f68bp;
                int i23 = 1 + (iIndexOf - (i22 + 1)) + 1;
                i = i23 + 1;
                cCharAt = charAt(i22 + i23);
            } else {
                if (i16 != 32) {
                    this.matchStat = -1;
                    return null;
                }
                long j4 = 0;
                for (int i24 = 0; i24 < 16; i24++) {
                    char cCharAt8 = charAt(i15 + i24);
                    if (cCharAt8 < '0' || cCharAt8 > '9') {
                        if (cCharAt8 >= 'a' && cCharAt8 <= 'f') {
                            i3 = cCharAt8 - 'a';
                        } else {
                            if (cCharAt8 < 'A' || cCharAt8 > 'F') {
                                this.matchStat = -2;
                                return null;
                            }
                            i3 = cCharAt8 - 'A';
                        }
                        i4 = i3 + 10;
                    } else {
                        i4 = cCharAt8 - '0';
                    }
                    j4 = (j4 << 4) | i4;
                }
                long j5 = 0;
                for (int i25 = 16; i25 < 32; i25++) {
                    char cCharAt9 = charAt(i15 + i25);
                    if (cCharAt9 >= '0' && cCharAt9 <= '9') {
                        i2 = cCharAt9 - '0';
                    } else if (cCharAt9 >= 'a' && cCharAt9 <= 'f') {
                        i2 = (cCharAt9 - 'a') + 10;
                    } else {
                        if (cCharAt9 < 'A' || cCharAt9 > 'F') {
                            this.matchStat = -2;
                            return null;
                        }
                        i2 = (cCharAt9 - 'A') + 10;
                    }
                    j5 = (j5 << 4) | i2;
                }
                uuid = new UUID(j4, j5);
                int i26 = this.f68bp;
                int i27 = 1 + (iIndexOf - (i26 + 1)) + 1;
                i = i27 + 1;
                cCharAt = charAt(i26 + i27);
            }
        } else {
            if (cCharAt2 != 'n' || charAt(this.f68bp + 1) != 'u' || charAt(this.f68bp + 2) != 'l' || charAt(this.f68bp + 3) != 'l') {
                this.matchStat = -1;
                return null;
            }
            i = 5;
            cCharAt = charAt(this.f68bp + 4);
            uuid = null;
        }
        if (cCharAt == ',') {
            int i28 = this.f68bp + i;
            this.f68bp = i28;
            this.f69ch = charAt(i28);
            this.matchStat = 3;
            return uuid;
        }
        if (cCharAt != ']') {
            this.matchStat = -1;
            return null;
        }
        int i29 = i + 1;
        char cCharAt10 = charAt(this.f68bp + i);
        if (cCharAt10 == ',') {
            this.token = 16;
            int i30 = this.f68bp + i29;
            this.f68bp = i30;
            this.f69ch = charAt(i30);
        } else if (cCharAt10 == ']') {
            this.token = 15;
            int i31 = this.f68bp + i29;
            this.f68bp = i31;
            this.f69ch = charAt(i31);
        } else if (cCharAt10 == '}') {
            this.token = 13;
            int i32 = this.f68bp + i29;
            this.f68bp = i32;
            this.f69ch = charAt(i32);
        } else {
            if (cCharAt10 != 26) {
                this.matchStat = -1;
                return null;
            }
            this.token = 20;
            this.f68bp += i29 - 1;
            this.f69ch = (char) 26;
        }
        this.matchStat = 4;
        return uuid;
    }

    public boolean seekArrayToItem(int i) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long j, boolean z) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToField(long[] jArr) {
        throw new UnsupportedOperationException();
    }

    public int seekObjectToFieldDeepScan(long j) {
        throw new UnsupportedOperationException();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setToken(int i) {
        this.token = i;
    }

    public void skipArray() {
        throw new UnsupportedOperationException();
    }

    protected void skipComment() {
        char c;
        next();
        char c2 = this.f69ch;
        if (c2 == '/') {
            do {
                next();
                c = this.f69ch;
                if (c == '\n') {
                    next();
                    return;
                }
            } while (c != 26);
            return;
        }
        if (c2 != '*') {
            throw new JSONException("invalid comment");
        }
        next();
        while (true) {
            char c3 = this.f69ch;
            if (c3 == 26) {
                return;
            }
            if (c3 == '*') {
                next();
                if (this.f69ch == '/') {
                    next();
                    return;
                }
            } else {
                next();
            }
        }
    }

    public void skipObject() {
        throw new UnsupportedOperationException();
    }

    public void skipObject(boolean z) {
        throw new UnsupportedOperationException();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void skipWhitespace() {
        while (true) {
            char c = this.f69ch;
            if (c > '/') {
                return;
            }
            if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f' || c == '\b') {
                next();
            } else if (c != '/') {
                return;
            } else {
                skipComment();
            }
        }
    }

    public final String stringDefaultValue() {
        return this.stringDefaultValue;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    protected abstract char[] sub_chars(int i, int i2);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int token() {
        return this.token;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String tokenName() {
        return JSONToken.name(this.token);
    }
}
