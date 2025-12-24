package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/JSONReaderScanner.class */
public final class JSONReaderScanner extends JSONLexerBase {
    private static final ThreadLocal<char[]> BUF_LOCAL = new ThreadLocal<>();
    private char[] buf;
    private int bufLength;
    private Reader reader;

    public JSONReaderScanner(Reader reader) {
        this(reader, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader, int i) throws IOException {
        super(i);
        this.reader = reader;
        char[] cArr = BUF_LOCAL.get();
        this.buf = cArr;
        if (cArr != null) {
            BUF_LOCAL.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[16384];
        }
        try {
            this.bufLength = reader.read(this.buf);
            this.f68bp = -1;
            next();
            if (this.f69ch == 65279) {
                next();
            }
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public JSONReaderScanner(String str) {
        this(str, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(String str, int i) {
        this(new StringReader(str), i);
    }

    public JSONReaderScanner(char[] cArr, int i) {
        this(cArr, i, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(char[] cArr, int i, int i2) {
        this(new CharArrayReader(cArr, 0, i), i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String addSymbol(int i, int i2, int i3, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.buf, i, i2, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void arrayCopy(int i, char[] cArr, int i2, int i3) {
        System.arraycopy(this.buf, i, cArr, i2, i3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public byte[] bytesValue() {
        if (this.token != 26) {
            return IOUtils.decodeBase64(this.buf, this.f70np + 1, this.f71sp);
        }
        throw new JSONException("TODO");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final boolean charArrayCompare(char[] cArr) {
        for (int i = 0; i < cArr.length; i++) {
            if (charAt(this.f68bp + i) != cArr[i]) {
                return false;
            }
        }
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char charAt(int i) throws IOException {
        int i2 = this.bufLength;
        int i3 = i;
        if (i >= i2) {
            if (i2 == -1) {
                if (i < this.f71sp) {
                    return this.buf[i];
                }
                return (char) 26;
            }
            if (this.f68bp == 0) {
                char[] cArr = this.buf;
                int length = (cArr.length * 3) / 2;
                char[] cArr2 = new char[length];
                System.arraycopy(cArr, this.f68bp, cArr2, 0, this.bufLength);
                int i4 = this.bufLength;
                try {
                    this.bufLength += this.reader.read(cArr2, i4, length - i4);
                    this.buf = cArr2;
                    i3 = i;
                } catch (IOException e) {
                    throw new JSONException(e.getMessage(), e);
                }
            } else {
                int i5 = this.bufLength - this.f68bp;
                if (i5 > 0) {
                    System.arraycopy(this.buf, this.f68bp, this.buf, 0, i5);
                }
                try {
                    int i6 = this.reader.read(this.buf, i5, this.buf.length - i5);
                    this.bufLength = i6;
                    if (i6 == 0) {
                        throw new JSONException("illegal state, textLength is zero");
                    }
                    if (i6 == -1) {
                        return (char) 26;
                    }
                    this.bufLength = i6 + i5;
                    i3 = i - this.f68bp;
                    this.f70np -= this.f68bp;
                    this.f68bp = 0;
                } catch (IOException e2) {
                    throw new JSONException(e2.getMessage(), e2);
                }
            }
        }
        return this.buf[i3];
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        char[] cArr = this.buf;
        if (cArr.length <= 65536) {
            BUF_LOCAL.set(cArr);
        }
        this.buf = null;
        IOUtils.close(this.reader);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void copyTo(int i, int i2, char[] cArr) {
        System.arraycopy(this.buf, i, cArr, 0, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0046  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.math.BigDecimal decimalValue() throws java.io.IOException {
        /*
            r6 = this;
            r0 = r6
            int r0 = r0.f70np
            r8 = r0
            r0 = r8
            r7 = r0
            r0 = r8
            r1 = -1
            if (r0 != r1) goto Le
            r0 = 0
            r7 = r0
        Le:
            r0 = r6
            r1 = r6
            int r1 = r1.f71sp
            r2 = r7
            int r1 = r1 + r2
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r10 = r0
            r0 = r6
            int r0 = r0.f71sp
            r9 = r0
            r0 = r10
            r1 = 76
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 83
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 66
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 70
            if (r0 == r1) goto L46
            r0 = r9
            r8 = r0
            r0 = r10
            r1 = 68
            if (r0 != r1) goto L4a
        L46:
            r0 = r9
            r1 = 1
            int r0 = r0 - r1
            r8 = r0
        L4a:
            java.math.BigDecimal r0 = new java.math.BigDecimal
            r1 = r0
            r2 = r6
            char[] r2 = r2.buf
            r3 = r7
            r4 = r8
            r1.<init>(r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONReaderScanner.decimalValue():java.math.BigDecimal");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final int indexOf(char c, int i) throws IOException {
        int i2 = i - this.f68bp;
        while (true) {
            char cCharAt = charAt(this.f68bp + i2);
            if (c == cCharAt) {
                return i2 + this.f68bp;
            }
            if (cCharAt == 26) {
                return -1;
            }
            i2++;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char c = this.buf[i];
            if (c == 26) {
                this.token = 20;
                return true;
            }
            if (!isWhitespace(c)) {
                return false;
            }
            i++;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean isEOF() {
        boolean z = true;
        if (this.bufLength != -1) {
            z = true;
            if (this.f68bp != this.buf.length) {
                z = this.f69ch == 26 && this.f68bp + 1 >= this.buf.length;
            }
        }
        return z;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final char next() throws IOException {
        int i = this.f68bp + 1;
        this.f68bp = i;
        int i2 = this.bufLength;
        int i3 = i;
        if (i >= i2) {
            if (i2 == -1) {
                return (char) 26;
            }
            if (this.f71sp > 0) {
                int i4 = this.bufLength - this.f71sp;
                int i5 = i4;
                if (this.f69ch == '\"') {
                    i5 = i4;
                    if (i4 > 0) {
                        i5 = i4 - 1;
                    }
                }
                char[] cArr = this.buf;
                System.arraycopy(cArr, i5, cArr, 0, this.f71sp);
            }
            this.f70np = -1;
            int i6 = this.f71sp;
            this.f68bp = i6;
            try {
                int i7 = this.f68bp;
                int length = this.buf.length - i7;
                int length2 = length;
                if (length == 0) {
                    char[] cArr2 = new char[this.buf.length * 2];
                    System.arraycopy(this.buf, 0, cArr2, 0, this.buf.length);
                    this.buf = cArr2;
                    length2 = cArr2.length - i7;
                }
                int i8 = this.reader.read(this.buf, this.f68bp, length2);
                this.bufLength = i8;
                if (i8 == 0) {
                    throw new JSONException("illegal stat, textLength is zero");
                }
                if (i8 == -1) {
                    this.f69ch = (char) 26;
                    return (char) 26;
                }
                this.bufLength = i8 + this.f68bp;
                i3 = i6;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
        char c = this.buf[i3];
        this.f69ch = c;
        return c;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0046  */
    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String numberString() throws java.io.IOException {
        /*
            r6 = this;
            r0 = r6
            int r0 = r0.f70np
            r8 = r0
            r0 = r8
            r7 = r0
            r0 = r8
            r1 = -1
            if (r0 != r1) goto Le
            r0 = 0
            r7 = r0
        Le:
            r0 = r6
            r1 = r6
            int r1 = r1.f71sp
            r2 = r7
            int r1 = r1 + r2
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r10 = r0
            r0 = r6
            int r0 = r0.f71sp
            r9 = r0
            r0 = r10
            r1 = 76
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 83
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 66
            if (r0 == r1) goto L46
            r0 = r10
            r1 = 70
            if (r0 == r1) goto L46
            r0 = r9
            r8 = r0
            r0 = r10
            r1 = 68
            if (r0 != r1) goto L4a
        L46:
            r0 = r9
            r1 = 1
            int r0 = r0 - r1
            r8 = r0
        L4a:
            java.lang.String r0 = new java.lang.String
            r1 = r0
            r2 = r6
            char[] r2 = r2.buf
            r3 = r7
            r4 = r8
            r1.<init>(r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONReaderScanner.numberString():java.lang.String");
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.f71sp);
        }
        int i = this.f70np + 1;
        if (i < 0) {
            throw new IllegalStateException();
        }
        if (i <= this.buf.length - this.f71sp) {
            return new String(this.buf, i, this.f71sp);
        }
        throw new IllegalStateException();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String subString(int i, int i2) {
        if (i2 >= 0) {
            return new String(this.buf, i, i2);
        }
        throw new StringIndexOutOfBoundsException(i2);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char[] sub_chars(int i, int i2) {
        if (i2 < 0) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i == 0) {
            return this.buf;
        }
        char[] cArr = new char[i2];
        System.arraycopy(this.buf, i, cArr, 0, i2);
        return cArr;
    }
}
