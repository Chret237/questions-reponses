package com.alibaba.fastjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator.class */
public abstract class JSONValidator implements Cloneable {

    /* renamed from: ch */
    protected char f63ch;
    protected boolean eof;
    protected Type type;
    protected int pos = -1;
    protected int count = 0;
    protected boolean supportMultiValue = true;

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator$ReaderValidator.class */
    static class ReaderValidator extends JSONValidator {
        private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
        private char[] buf;

        /* renamed from: r */
        final Reader f64r;
        private int end = -1;
        private int readCount = 0;

        ReaderValidator(Reader reader) throws IOException {
            this.f64r = reader;
            char[] cArr = bufLocal.get();
            this.buf = cArr;
            if (cArr != null) {
                bufLocal.set(null);
            } else {
                this.buf = new char[8192];
            }
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        public void close() throws IOException {
            bufLocal.set(this.buf);
            this.f64r.close();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void error() {
            throw new JSONException("error, readCount " + this.readCount + ", valueCount : " + this.count + ", pos " + this.pos);
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() throws IOException {
            if (this.pos < this.end) {
                char[] cArr = this.buf;
                int i = this.pos + 1;
                this.pos = i;
                this.f63ch = cArr[i];
                return;
            }
            if (this.eof) {
                return;
            }
            try {
                int i2 = this.f64r.read(this.buf, 0, this.buf.length);
                this.readCount++;
                if (i2 > 0) {
                    this.f63ch = this.buf[0];
                    this.pos = 0;
                    this.end = i2 - 1;
                } else {
                    if (i2 == -1) {
                        this.pos = 0;
                        this.end = 0;
                        this.buf = null;
                        this.f63ch = (char) 0;
                        this.eof = true;
                        return;
                    }
                    this.pos = 0;
                    this.end = 0;
                    this.buf = null;
                    this.f63ch = (char) 0;
                    this.eof = true;
                    throw new JSONException("read error");
                }
            } catch (IOException e) {
                throw new JSONException("read error");
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator$Type.class */
    public enum Type {
        Object,
        Array,
        Value
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator$UTF16Validator.class */
    static class UTF16Validator extends JSONValidator {
        private final String str;

        public UTF16Validator(String str) {
            this.str = str;
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            this.pos++;
            if (this.pos < this.str.length()) {
                this.f63ch = this.str.charAt(this.pos);
            } else {
                this.f63ch = (char) 0;
                this.eof = true;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator$UTF8InputStreamValidator.class */
    static class UTF8InputStreamValidator extends JSONValidator {
        private static final ThreadLocal<byte[]> bufLocal = new ThreadLocal<>();
        private byte[] buf;

        /* renamed from: is */
        private final InputStream f65is;
        private int end = -1;
        private int readCount = 0;

        public UTF8InputStreamValidator(InputStream inputStream) throws IOException {
            this.f65is = inputStream;
            byte[] bArr = bufLocal.get();
            this.buf = bArr;
            if (bArr != null) {
                bufLocal.set(null);
            } else {
                this.buf = new byte[8192];
            }
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        public void close() throws IOException {
            bufLocal.set(this.buf);
            this.f65is.close();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void error() {
            throw new JSONException("error, readCount " + this.readCount + ", valueCount : " + this.count + ", pos " + this.pos);
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() throws IOException {
            if (this.pos < this.end) {
                byte[] bArr = this.buf;
                int i = this.pos + 1;
                this.pos = i;
                this.f63ch = (char) bArr[i];
                return;
            }
            if (this.eof) {
                return;
            }
            try {
                int i2 = this.f65is.read(this.buf, 0, this.buf.length);
                this.readCount++;
                if (i2 > 0) {
                    this.f63ch = (char) this.buf[0];
                    this.pos = 0;
                    this.end = i2 - 1;
                } else {
                    if (i2 == -1) {
                        this.pos = 0;
                        this.end = 0;
                        this.buf = null;
                        this.f63ch = (char) 0;
                        this.eof = true;
                        return;
                    }
                    this.pos = 0;
                    this.end = 0;
                    this.buf = null;
                    this.f63ch = (char) 0;
                    this.eof = true;
                    throw new JSONException("read error");
                }
            } catch (IOException e) {
                throw new JSONException("read error");
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONValidator$UTF8Validator.class */
    static class UTF8Validator extends JSONValidator {
        private final byte[] bytes;

        public UTF8Validator(byte[] bArr) {
            this.bytes = bArr;
            next();
            skipWhiteSpace();
        }

        @Override // com.alibaba.fastjson.JSONValidator
        void next() {
            this.pos++;
            int i = this.pos;
            byte[] bArr = this.bytes;
            if (i < bArr.length) {
                this.f63ch = (char) bArr[this.pos];
            } else {
                this.f63ch = (char) 0;
                this.eof = true;
            }
        }
    }

    public static JSONValidator from(Reader reader) {
        return new ReaderValidator(reader);
    }

    public static JSONValidator from(String str) {
        return new UTF16Validator(str);
    }

    public static JSONValidator fromUtf8(InputStream inputStream) {
        return new UTF8InputStreamValidator(inputStream);
    }

    public static JSONValidator fromUtf8(byte[] bArr) {
        return new UTF8Validator(bArr);
    }

    static final boolean isWhiteSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f' || c == '\b';
    }

    /* JADX WARN: Removed duplicated region for block: B:140:0x02e8  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x035f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01fd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void any() {
        /*
            Method dump skipped, instructions count: 954
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONValidator.any():void");
    }

    public void close() throws IOException {
    }

    void error() {
        throw new JSONException("error : " + this.pos);
    }

    protected void fieldName() {
        next();
        while (true) {
            char c = this.f63ch;
            if (c == '\\') {
                next();
                if (this.f63ch == 'u') {
                    next();
                    next();
                    next();
                    next();
                    next();
                } else {
                    next();
                }
            } else {
                if (c == '\"') {
                    next();
                    return;
                }
                next();
            }
        }
    }

    public Type getType() {
        return this.type;
    }

    abstract void next();

    void skipWhiteSpace() {
        while (isWhiteSpace(this.f63ch)) {
            next();
        }
    }

    public boolean validate() {
        do {
            any();
            this.count++;
            if (!this.supportMultiValue || this.eof) {
                return true;
            }
            skipWhiteSpace();
        } while (!this.eof);
        return true;
    }
}
