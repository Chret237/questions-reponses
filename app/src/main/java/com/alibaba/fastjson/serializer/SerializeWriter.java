package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.RyuDouble;
import com.alibaba.fastjson.util.RyuFloat;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/SerializeWriter.class */
public final class SerializeWriter extends Writer {
    private static int BUFFER_THRESHOLD;
    private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
    private static final ThreadLocal<byte[]> bytesBufLocal = new ThreadLocal<>();
    static final int nonDirectFeatures;
    protected boolean beanToArray;
    protected boolean browserSecure;
    protected char[] buf;
    protected int count;
    protected boolean disableCircularReferenceDetect;
    protected int features;
    protected char keySeperator;
    protected int maxBufSize;
    protected boolean notWriteDefaultValue;
    protected boolean quoteFieldNames;
    protected long sepcialBits;
    protected boolean sortField;
    protected boolean useSingleQuotes;
    protected boolean writeDirect;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected boolean writeNonStringValueAsString;
    private final Writer writer;

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:16:0x0087
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    static {
        /*
            java.lang.ThreadLocal r0 = new java.lang.ThreadLocal
            r1 = r0
            r1.<init>()
            com.alibaba.fastjson.serializer.SerializeWriter.bufLocal = r0
            java.lang.ThreadLocal r0 = new java.lang.ThreadLocal
            r1 = r0
            r1.<init>()
            com.alibaba.fastjson.serializer.SerializeWriter.bytesBufLocal = r0
            r0 = 131072(0x20000, float:1.83671E-40)
            com.alibaba.fastjson.serializer.SerializeWriter.BUFFER_THRESHOLD = r0
            java.lang.String r0 = "fastjson.serializer_buffer_threshold"
            java.lang.String r0 = com.alibaba.fastjson.util.IOUtils.getStringProperty(r0)     // Catch: java.lang.Throwable -> L87
            r4 = r0
            r0 = r4
            if (r0 == 0) goto L43
            r0 = r4
            int r0 = r0.length()     // Catch: java.lang.Throwable -> L87
            if (r0 <= 0) goto L43
            r0 = r4
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Throwable -> L87
            r3 = r0
            r0 = r3
            r1 = 64
            if (r0 < r1) goto L43
            r0 = r3
            r1 = 65536(0x10000, float:9.1835E-41)
            if (r0 > r1) goto L43
            r0 = r3
            r1 = 1024(0x400, float:1.435E-42)
            int r0 = r0 * r1
            com.alibaba.fastjson.serializer.SerializeWriter.BUFFER_THRESHOLD = r0     // Catch: java.lang.Throwable -> L87
        L43:
            com.alibaba.fastjson.serializer.SerializerFeature r0 = com.alibaba.fastjson.serializer.SerializerFeature.UseSingleQuotes     // Catch: java.lang.Throwable -> L87
            int r0 = r0.mask
            r1 = 0
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteEnumUsingToString
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringValueAsString
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.IgnoreErrorGetter
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializerFeature r1 = com.alibaba.fastjson.serializer.SerializerFeature.NotWriteDefaultValue
            int r1 = r1.mask
            r0 = r0 | r1
            com.alibaba.fastjson.serializer.SerializeWriter.nonDirectFeatures = r0
            return
        L87:
            r4 = move-exception
            goto L43
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.m348clinit():void");
    }

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(int i) {
        this((Writer) null, i);
    }

    public SerializeWriter(Writer writer) {
        this(writer, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
    }

    public SerializeWriter(Writer writer, int i) {
        this.maxBufSize = -1;
        this.writer = writer;
        if (i > 0) {
            this.buf = new char[i];
            computeFeatures();
        } else {
            throw new IllegalArgumentException("Negative initial size: " + i);
        }
    }

    public SerializeWriter(Writer writer, int i, SerializerFeature... serializerFeatureArr) {
        this.maxBufSize = -1;
        this.writer = writer;
        char[] cArr = bufLocal.get();
        this.buf = cArr;
        if (cArr != null) {
            bufLocal.set(null);
        } else {
            this.buf = new char[2048];
        }
        int mask = i;
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            mask |= serializerFeature.getMask();
        }
        this.features = mask;
        computeFeatures();
    }

    public SerializeWriter(Writer writer, SerializerFeature... serializerFeatureArr) {
        this(writer, 0, serializerFeatureArr);
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this((Writer) null, serializerFeatureArr);
    }

    private int encodeToUTF8(OutputStream outputStream) throws IOException {
        double d = this.count;
        Double.isNaN(d);
        int i = (int) (d * 3.0d);
        byte[] bArr = bytesBufLocal.get();
        byte[] bArr2 = bArr;
        if (bArr == null) {
            bArr2 = new byte[8192];
            bytesBufLocal.set(bArr2);
        }
        byte[] bArr3 = bArr2;
        if (bArr2.length < i) {
            bArr3 = new byte[i];
        }
        int iEncodeUTF8 = IOUtils.encodeUTF8(this.buf, 0, this.count, bArr3);
        outputStream.write(bArr3, 0, iEncodeUTF8);
        return iEncodeUTF8;
    }

    private byte[] encodeToUTF8Bytes() {
        double d = this.count;
        Double.isNaN(d);
        int i = (int) (d * 3.0d);
        byte[] bArr = bytesBufLocal.get();
        byte[] bArr2 = bArr;
        if (bArr == null) {
            bArr2 = new byte[8192];
            bytesBufLocal.set(bArr2);
        }
        byte[] bArr3 = bArr2;
        if (bArr2.length < i) {
            bArr3 = new byte[i];
        }
        int iEncodeUTF8 = IOUtils.encodeUTF8(this.buf, 0, this.count, bArr3);
        byte[] bArr4 = new byte[iEncodeUTF8];
        System.arraycopy(bArr3, 0, bArr4, 0, iEncodeUTF8);
        return bArr4;
    }

    private void writeEnumFieldValue(char c, String str, String str2) {
        if (this.useSingleQuotes) {
            writeFieldValue(c, str, str2);
        } else {
            writeFieldValueStringWithDoubleQuote(c, str, str2);
        }
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String str) {
        boolean z;
        byte[] bArr = IOUtils.specicalFlags_singleQuotes;
        int length = str.length();
        int i = this.count + length + 1;
        if (i > this.buf.length) {
            if (this.writer != null) {
                if (length == 0) {
                    write(39);
                    write(39);
                    write(58);
                    return;
                }
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        z = false;
                        break;
                    }
                    char cCharAt = str.charAt(i2);
                    if (cCharAt < bArr.length && bArr[cCharAt] != 0) {
                        z = true;
                        break;
                    }
                    i2++;
                }
                int i3 = 0;
                if (z) {
                    write(39);
                    i3 = 0;
                }
                while (i3 < length) {
                    char cCharAt2 = str.charAt(i3);
                    if (cCharAt2 >= bArr.length || bArr[cCharAt2] == 0) {
                        write(cCharAt2);
                    } else {
                        write(92);
                        write(IOUtils.replaceChars[cCharAt2]);
                    }
                    i3++;
                }
                if (z) {
                    write(39);
                }
                write(58);
                return;
            }
            expandCapacity(i);
        }
        if (length == 0) {
            int i4 = this.count;
            if (i4 + 3 > this.buf.length) {
                expandCapacity(i4 + 3);
            }
            char[] cArr = this.buf;
            int i5 = this.count;
            int i6 = i5 + 1;
            this.count = i6;
            cArr[i5] = '\'';
            int i7 = i6 + 1;
            this.count = i7;
            cArr[i6] = '\'';
            this.count = i7 + 1;
            cArr[i7] = ':';
            return;
        }
        int i8 = this.count;
        int i9 = i8 + length;
        str.getChars(0, length, this.buf, i8);
        this.count = i;
        int i10 = i8;
        boolean z2 = false;
        while (i10 < i9) {
            char[] cArr2 = this.buf;
            char c = cArr2[i10];
            int i11 = i10;
            boolean z3 = z2;
            int i12 = i;
            int i13 = i9;
            if (c < bArr.length) {
                i11 = i10;
                z3 = z2;
                i12 = i;
                i13 = i9;
                if (bArr[c] != 0) {
                    if (z2) {
                        i12 = i + 1;
                        if (i12 > cArr2.length) {
                            expandCapacity(i12);
                        }
                        this.count = i12;
                        char[] cArr3 = this.buf;
                        i11 = i10 + 1;
                        System.arraycopy(cArr3, i11, cArr3, i10 + 2, i9 - i10);
                        char[] cArr4 = this.buf;
                        cArr4[i10] = '\\';
                        cArr4[i11] = IOUtils.replaceChars[c];
                        i13 = i9 + 1;
                        z3 = z2;
                    } else {
                        i12 = i + 3;
                        if (i12 > cArr2.length) {
                            expandCapacity(i12);
                        }
                        this.count = i12;
                        char[] cArr5 = this.buf;
                        int i14 = i10 + 1;
                        System.arraycopy(cArr5, i14, cArr5, i10 + 3, (i9 - i10) - 1);
                        char[] cArr6 = this.buf;
                        System.arraycopy(cArr6, 0, cArr6, 1, i10);
                        char[] cArr7 = this.buf;
                        cArr7[i8] = '\'';
                        cArr7[i14] = '\\';
                        i11 = i14 + 1;
                        cArr7[i11] = IOUtils.replaceChars[c];
                        i13 = i9 + 2;
                        this.buf[this.count - 2] = '\'';
                        z3 = true;
                    }
                }
            }
            i10 = i11 + 1;
            z2 = z3;
            i = i12;
            i9 = i13;
        }
        this.buf[i - 1] = ':';
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(char c) {
        write(c);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(CharSequence charSequence) throws IOException {
        String string = charSequence == null ? "null" : charSequence.toString();
        write(string, 0, string.length());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(CharSequence charSequence, int i, int i2) throws IOException {
        CharSequence charSequence2 = charSequence;
        if (charSequence == null) {
            charSequence2 = "null";
        }
        String string = charSequence2.subSequence(i, i2).toString();
        write(string, 0, string.length());
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        char[] cArr = this.buf;
        if (cArr.length <= BUFFER_THRESHOLD) {
            bufLocal.set(cArr);
        }
        this.buf = null;
    }

    protected void computeFeatures() {
        this.quoteFieldNames = (this.features & SerializerFeature.QuoteFieldNames.mask) != 0;
        this.useSingleQuotes = (this.features & SerializerFeature.UseSingleQuotes.mask) != 0;
        this.sortField = (this.features & SerializerFeature.SortField.mask) != 0;
        this.disableCircularReferenceDetect = (this.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0;
        this.beanToArray = (this.features & SerializerFeature.BeanToArray.mask) != 0;
        this.writeNonStringValueAsString = (this.features & SerializerFeature.WriteNonStringValueAsString.mask) != 0;
        this.notWriteDefaultValue = (this.features & SerializerFeature.NotWriteDefaultValue.mask) != 0;
        this.writeEnumUsingName = (this.features & SerializerFeature.WriteEnumUsingName.mask) != 0;
        this.writeEnumUsingToString = (this.features & SerializerFeature.WriteEnumUsingToString.mask) != 0;
        this.writeDirect = this.quoteFieldNames && (this.features & nonDirectFeatures) == 0 && (this.beanToArray || this.writeEnumUsingName);
        this.keySeperator = this.useSingleQuotes ? '\'' : '\"';
        boolean z = (this.features & SerializerFeature.BrowserSecure.mask) != 0;
        this.browserSecure = z;
        this.sepcialBits = z ? 5764610843043954687L : (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0 ? 140758963191807L : 21474836479L;
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (z) {
            this.features |= serializerFeature.getMask();
            if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                this.features &= SerializerFeature.WriteEnumUsingName.getMask() ^ (-1);
            } else if (serializerFeature == SerializerFeature.WriteEnumUsingName) {
                this.features &= SerializerFeature.WriteEnumUsingToString.getMask() ^ (-1);
            }
        } else {
            this.features = (serializerFeature.getMask() ^ (-1)) & this.features;
        }
        computeFeatures();
    }

    public void expandCapacity(int i) {
        char[] cArr;
        int i2 = this.maxBufSize;
        if (i2 != -1 && i >= i2) {
            throw new JSONException("serialize exceeded MAX_OUTPUT_LENGTH=" + this.maxBufSize + ", minimumCapacity=" + i);
        }
        char[] cArr2 = this.buf;
        int length = cArr2.length + (cArr2.length >> 1) + 1;
        if (length >= i) {
            i = length;
        }
        char[] cArr3 = new char[i];
        System.arraycopy(this.buf, 0, cArr3, 0, this.count);
        if (this.buf.length < BUFFER_THRESHOLD && ((cArr = bufLocal.get()) == null || cArr.length < this.buf.length)) {
            bufLocal.set(this.buf);
        }
        this.buf = cArr3;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        Writer writer = this.writer;
        if (writer == null) {
            return;
        }
        try {
            writer.write(this.buf, 0, this.count);
            this.writer.flush();
            this.count = 0;
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public int getMaxBufSize() {
        return this.maxBufSize;
    }

    public boolean isEnabled(int i) {
        return (i & this.features) != 0;
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return (serializerFeature.mask & this.features) != 0;
    }

    public boolean isNotWriteDefaultValue() {
        return this.notWriteDefaultValue;
    }

    public boolean isSortField() {
        return this.sortField;
    }

    public void setMaxBufSize(int i) {
        if (i >= this.buf.length) {
            this.maxBufSize = i;
            return;
        }
        throw new JSONException("must > " + this.buf.length);
    }

    public int size() {
        return this.count;
    }

    public byte[] toBytes(String str) {
        return toBytes((str == null || "UTF-8".equals(str)) ? IOUtils.UTF8 : Charset.forName(str));
    }

    public byte[] toBytes(Charset charset) {
        if (this.writer == null) {
            return charset == IOUtils.UTF8 ? encodeToUTF8Bytes() : new String(this.buf, 0, this.count).getBytes(charset);
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        int i = this.count;
        char[] cArr = new char[i];
        System.arraycopy(this.buf, 0, cArr, 0, i);
        return cArr;
    }

    public char[] toCharArrayForSpringWebSocket() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        int i = this.count;
        char[] cArr = new char[i - 2];
        System.arraycopy(this.buf, 1, cArr, 0, i - 2);
        return cArr;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    @Override // java.io.Writer
    public void write(int i) {
        int i2 = 1;
        int i3 = this.count + 1;
        if (i3 <= this.buf.length) {
            i2 = i3;
        } else if (this.writer == null) {
            expandCapacity(i3);
            i2 = i3;
        } else {
            flush();
        }
        this.buf[this.count] = (char) i;
        this.count = i2;
    }

    @Override // java.io.Writer
    public void write(String str) {
        if (str == null) {
            writeNull();
        } else {
            write(str, 0, str.length());
        }
    }

    @Override // java.io.Writer
    public void write(String str, int i, int i2) throws IOException {
        int i3;
        int i4;
        int i5 = this.count + i2;
        int i6 = i5;
        int i7 = i;
        int i8 = i2;
        if (i5 > this.buf.length) {
            int i9 = i;
            int i10 = i2;
            if (this.writer == null) {
                expandCapacity(i5);
                i6 = i5;
                i7 = i;
                i8 = i2;
            } else {
                while (true) {
                    char[] cArr = this.buf;
                    int length = cArr.length;
                    int i11 = this.count;
                    int i12 = length - i11;
                    i3 = i9 + i12;
                    str.getChars(i9, i3, cArr, i11);
                    this.count = this.buf.length;
                    flush();
                    i4 = i10 - i12;
                    if (i4 <= this.buf.length) {
                        break;
                    }
                    i9 = i3;
                    i10 = i4;
                }
                i6 = i4;
                i7 = i3;
                i8 = i4;
            }
        }
        str.getChars(i7, i8 + i7, this.buf, this.count);
        this.count = i6;
    }

    public void write(List<String> list) {
        boolean z;
        int i;
        if (list.isEmpty()) {
            write("[]");
            return;
        }
        int i2 = this.count;
        int size = list.size();
        int i3 = i2;
        int i4 = 0;
        while (i4 < size) {
            String str = list.get(i4);
            if (str == null) {
                z = true;
            } else {
                int length = str.length();
                z = false;
                for (int i5 = 0; i5 < length; i5++) {
                    char cCharAt = str.charAt(i5);
                    z = cCharAt < ' ' || cCharAt > '~' || cCharAt == '\"' || cCharAt == '\\';
                    if (z) {
                        break;
                    }
                }
            }
            if (z) {
                this.count = i2;
                write(91);
                for (int i6 = 0; i6 < list.size(); i6++) {
                    String str2 = list.get(i6);
                    if (i6 != 0) {
                        write(44);
                    }
                    if (str2 == null) {
                        write("null");
                    } else {
                        writeStringWithDoubleQuote(str2, (char) 0);
                    }
                }
                write(93);
                return;
            }
            int length2 = str.length() + i3 + 3;
            int i7 = length2;
            if (i4 == list.size() - 1) {
                i7 = length2 + 1;
            }
            if (i7 > this.buf.length) {
                this.count = i3;
                expandCapacity(i7);
            }
            if (i4 == 0) {
                i = i3 + 1;
                this.buf[i3] = '[';
            } else {
                i = i3 + 1;
                this.buf[i3] = ',';
            }
            int i8 = i + 1;
            this.buf[i] = '\"';
            str.getChars(0, str.length(), this.buf, i8);
            int length3 = i8 + str.length();
            this.buf[length3] = '\"';
            i4++;
            i3 = length3 + 1;
        }
        this.buf[i3] = ']';
        this.count = i3 + 1;
    }

    public void write(boolean z) {
        if (z) {
            write("true");
        } else {
            write("false");
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i, int i2) throws IOException {
        int i3;
        int i4;
        int i5;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return;
        }
        int i6 = this.count + i2;
        int i7 = i6;
        int i8 = i;
        int i9 = i2;
        if (i6 > this.buf.length) {
            int i10 = i;
            int i11 = i2;
            if (this.writer == null) {
                expandCapacity(i6);
                i7 = i6;
                i8 = i;
                i9 = i2;
            } else {
                do {
                    char[] cArr2 = this.buf;
                    int length = cArr2.length;
                    int i12 = this.count;
                    int i13 = length - i12;
                    System.arraycopy(cArr, i10, cArr2, i12, i13);
                    this.count = this.buf.length;
                    flush();
                    i4 = i11 - i13;
                    i5 = i10 + i13;
                    i10 = i5;
                    i11 = i4;
                } while (i4 > this.buf.length);
                i7 = i4;
                i9 = i4;
                i8 = i5;
            }
        }
        System.arraycopy(cArr, i8, this.buf, this.count, i9);
        this.count = i7;
    }

    public void writeByteArray(byte[] bArr) throws IOException {
        if (isEnabled(SerializerFeature.WriteClassName.mask)) {
            writeHex(bArr);
            return;
        }
        int length = bArr.length;
        char c = this.useSingleQuotes ? '\'' : '\"';
        if (length == 0) {
            write(this.useSingleQuotes ? "''" : "\"\"");
            return;
        }
        char[] cArr = IOUtils.f74CA;
        int i = (length / 3) * 3;
        int i2 = length - 1;
        int i3 = i2 / 3;
        int i4 = this.count;
        int i5 = ((i3 + 1) << 2) + i4 + 2;
        if (i5 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                int i6 = 0;
                while (i6 < i) {
                    int i7 = i6 + 1;
                    int i8 = i7 + 1;
                    int i9 = ((bArr[i6] & 255) << 16) | ((bArr[i7] & 255) << 8) | (bArr[i8] & 255);
                    write(cArr[(i9 >>> 18) & 63]);
                    write(cArr[(i9 >>> 12) & 63]);
                    write(cArr[(i9 >>> 6) & 63]);
                    write(cArr[i9 & 63]);
                    i6 = i8 + 1;
                }
                int i10 = length - i;
                if (i10 > 0) {
                    byte b = bArr[i];
                    int i11 = 0;
                    if (i10 == 2) {
                        i11 = (bArr[i2] & 255) << 2;
                    }
                    int i12 = ((b & 255) << 10) | i11;
                    write(cArr[i12 >> 12]);
                    write(cArr[(i12 >>> 6) & 63]);
                    write(i10 == 2 ? cArr[i12 & 63] : '=');
                    write(61);
                }
                write(c);
                return;
            }
            expandCapacity(i5);
        }
        this.count = i5;
        int i13 = i4 + 1;
        this.buf[i4] = c;
        int i14 = 0;
        while (i14 < i) {
            int i15 = i14 + 1;
            int i16 = i15 + 1;
            int i17 = ((bArr[i14] & 255) << 16) | ((bArr[i15] & 255) << 8) | (bArr[i16] & 255);
            char[] cArr2 = this.buf;
            int i18 = i13 + 1;
            cArr2[i13] = cArr[(i17 >>> 18) & 63];
            int i19 = i18 + 1;
            cArr2[i18] = cArr[(i17 >>> 12) & 63];
            int i20 = i19 + 1;
            cArr2[i19] = cArr[(i17 >>> 6) & 63];
            i13 = i20 + 1;
            cArr2[i20] = cArr[i17 & 63];
            i14 = i16 + 1;
        }
        int i21 = length - i;
        if (i21 > 0) {
            byte b2 = bArr[i];
            int i22 = 0;
            if (i21 == 2) {
                i22 = (bArr[i2] & 255) << 2;
            }
            int i23 = ((b2 & 255) << 10) | i22;
            char[] cArr3 = this.buf;
            cArr3[i5 - 5] = cArr[i23 >> 12];
            cArr3[i5 - 4] = cArr[(i23 >>> 6) & 63];
            cArr3[i5 - 3] = i21 == 2 ? cArr[i23 & 63] : '=';
            this.buf[i5 - 2] = '=';
        }
        this.buf[i5 - 1] = c;
    }

    public void writeDouble(double d, boolean z) throws IOException {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            writeNull();
            return;
        }
        int i = this.count + 24;
        if (i > this.buf.length) {
            if (this.writer != null) {
                String string = RyuDouble.toString(d);
                write(string, 0, string.length());
                if (z && isEnabled(SerializerFeature.WriteClassName)) {
                    write(68);
                    return;
                }
                return;
            }
            expandCapacity(i);
        }
        this.count += RyuDouble.toString(d, this.buf, this.count);
        if (z && isEnabled(SerializerFeature.WriteClassName)) {
            write(68);
        }
    }

    public void writeEnum(Enum<?> r4) throws IOException {
        if (r4 == null) {
            writeNull();
            return;
        }
        String string = null;
        if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            string = r4.name();
        } else if (this.writeEnumUsingToString) {
            string = r4.toString();
        }
        if (string == null) {
            writeInt(r4.ordinal());
            return;
        }
        int i = isEnabled(SerializerFeature.UseSingleQuotes) ? 39 : 34;
        write(i);
        write(string);
        write(i);
    }

    public void writeFieldName(String str) {
        writeFieldName(str, false);
    }

    public void writeFieldName(String str, boolean z) {
        if (str == null) {
            write("null:");
            return;
        }
        if (this.useSingleQuotes) {
            if (!this.quoteFieldNames) {
                writeKeyWithSingleQuoteIfHasSpecial(str);
                return;
            } else {
                writeStringWithSingleQuote(str);
                write(58);
                return;
            }
        }
        if (this.quoteFieldNames) {
            writeStringWithDoubleQuote(str, ':');
            return;
        }
        boolean z2 = str.length() == 0;
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                break;
            }
            char cCharAt = str.charAt(i);
            if ((cCharAt < '@' && (this.sepcialBits & (1 << cCharAt)) != 0) || cCharAt == '\\') {
                z2 = true;
                break;
            }
            i++;
        }
        if (z2) {
            writeStringWithDoubleQuote(str, ':');
        } else {
            write(str);
            write(58);
        }
    }

    public void writeFieldNameDirect(String str) {
        int length = str.length();
        int i = this.count + length + 3;
        if (i > this.buf.length) {
            expandCapacity(i);
        }
        int i2 = this.count;
        char[] cArr = this.buf;
        cArr[i2] = '\"';
        str.getChars(0, length, cArr, i2 + 1);
        this.count = i;
        char[] cArr2 = this.buf;
        cArr2[i - 2] = '\"';
        cArr2[i - 1] = ':';
    }

    public void writeFieldValue(char c, String str, char c2) {
        write(c);
        writeFieldName(str);
        if (c2 == 0) {
            writeString("��");
        } else {
            writeString(Character.toString(c2));
        }
    }

    public void writeFieldValue(char c, String str, double d) throws IOException {
        write(c);
        writeFieldName(str);
        writeDouble(d, false);
    }

    public void writeFieldValue(char c, String str, float f) throws IOException {
        write(c);
        writeFieldName(str);
        writeFloat(f, false);
    }

    public void writeFieldValue(char c, String str, int i) throws IOException {
        if (i == Integer.MIN_VALUE || !this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            writeInt(i);
            return;
        }
        int iStringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int length = str.length();
        int i2 = this.count + length + 4 + iStringSize;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeFieldName(str);
                writeInt(i);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        char[] cArr = this.buf;
        cArr[i3] = c;
        int i4 = i3 + length + 1;
        cArr[i3 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i3 + 2);
        char[] cArr2 = this.buf;
        cArr2[i4 + 1] = this.keySeperator;
        cArr2[i4 + 2] = ':';
        IOUtils.getChars(i, this.count, cArr2);
    }

    public void writeFieldValue(char c, String str, long j) throws IOException {
        if (j == Long.MIN_VALUE || !this.quoteFieldNames || isEnabled(SerializerFeature.BrowserCompatible.mask)) {
            write(c);
            writeFieldName(str);
            writeLong(j);
            return;
        }
        int iStringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int length = str.length();
        int i = this.count + length + 4 + iStringSize;
        if (i > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeFieldName(str);
                writeLong(j);
                return;
            }
            expandCapacity(i);
        }
        int i2 = this.count;
        this.count = i;
        char[] cArr = this.buf;
        cArr[i2] = c;
        int i3 = i2 + length + 1;
        cArr[i2 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i2 + 2);
        char[] cArr2 = this.buf;
        cArr2[i3 + 1] = this.keySeperator;
        cArr2[i3 + 2] = ':';
        IOUtils.getChars(j, this.count, cArr2);
    }

    public void writeFieldValue(char c, String str, Enum<?> r8) throws IOException {
        if (r8 == null) {
            write(c);
            writeFieldName(str);
            writeNull();
        } else if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, r8.name());
        } else if (this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, r8.toString());
        } else {
            writeFieldValue(c, str, r8.ordinal());
        }
    }

    public void writeFieldValue(char c, String str, String str2) {
        if (!this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
                return;
            } else {
                writeString(str2);
                return;
            }
        }
        if (this.useSingleQuotes) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
                return;
            } else {
                writeString(str2);
                return;
            }
        }
        if (!isEnabled(SerializerFeature.BrowserCompatible)) {
            writeFieldValueStringWithDoubleQuoteCheck(c, str, str2);
            return;
        }
        write(c);
        writeStringWithDoubleQuote(str, ':');
        writeStringWithDoubleQuote(str2, (char) 0);
    }

    public void writeFieldValue(char c, String str, BigDecimal bigDecimal) {
        write(c);
        writeFieldName(str);
        if (bigDecimal == null) {
            writeNull();
        } else {
            int iScale = bigDecimal.scale();
            write((!isEnabled(SerializerFeature.WriteBigDecimalAsPlain) || iScale < -100 || iScale >= 100) ? bigDecimal.toString() : bigDecimal.toPlainString());
        }
    }

    public void writeFieldValue(char c, String str, boolean z) {
        if (!this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            write(z);
            return;
        }
        int i = z ? 4 : 5;
        int length = str.length();
        int i2 = this.count + length + 4 + i;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeString(str);
                write(58);
                write(z);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        char[] cArr = this.buf;
        cArr[i3] = c;
        int i4 = i3 + length + 1;
        cArr[i3 + 1] = this.keySeperator;
        str.getChars(0, length, cArr, i3 + 2);
        this.buf[i4 + 1] = this.keySeperator;
        if (z) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, i4 + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, i4 + 2, 6);
        }
    }

    public void writeFieldValueStringWithDoubleQuote(char c, String str, String str2) {
        int length = str.length();
        int i = this.count;
        int length2 = str2.length();
        int i2 = i + length + length2 + 6;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeStringWithDoubleQuote(str, ':');
                writeStringWithDoubleQuote(str2, (char) 0);
                return;
            }
            expandCapacity(i2);
        }
        char[] cArr = this.buf;
        int i3 = this.count;
        cArr[i3] = c;
        int i4 = i3 + 2;
        int i5 = i4 + length;
        cArr[i3 + 1] = '\"';
        str.getChars(0, length, cArr, i4);
        this.count = i2;
        char[] cArr2 = this.buf;
        cArr2[i5] = '\"';
        int i6 = i5 + 1;
        int i7 = i6 + 1;
        cArr2[i6] = ':';
        cArr2[i7] = '\"';
        str2.getChars(0, length2, cArr2, i7 + 1);
        this.buf[this.count - 1] = '\"';
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0197 A[PHI: r14 r15 r16 r17
  0x0197: PHI (r14v4 int) = (r14v3 int), (r14v6 int) binds: [B:60:0x023a, B:33:0x0183] A[DONT_GENERATE, DONT_INLINE]
  0x0197: PHI (r15v5 int) = (r15v2 int), (r15v10 int) binds: [B:60:0x023a, B:33:0x0183] A[DONT_GENERATE, DONT_INLINE]
  0x0197: PHI (r16v4 int) = (r16v3 int), (r16v6 int) binds: [B:60:0x023a, B:33:0x0183] A[DONT_GENERATE, DONT_INLINE]
  0x0197: PHI (r17v4 int) = (r17v1 int), (r17v5 int) binds: [B:60:0x023a, B:33:0x0183] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x021e  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x023d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeFieldValueStringWithDoubleQuoteCheck(char r8, java.lang.String r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 1825
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeFieldValueStringWithDoubleQuoteCheck(char, java.lang.String, java.lang.String):void");
    }

    public void writeFloat(float f, boolean z) throws IOException {
        if (f != f || f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            writeNull();
            return;
        }
        int i = this.count + 15;
        if (i > this.buf.length) {
            if (this.writer != null) {
                String string = RyuFloat.toString(f);
                write(string, 0, string.length());
                if (z && isEnabled(SerializerFeature.WriteClassName)) {
                    write(70);
                    return;
                }
                return;
            }
            expandCapacity(i);
        }
        this.count += RyuFloat.toString(f, this.buf, this.count);
        if (z && isEnabled(SerializerFeature.WriteClassName)) {
            write(70);
        }
    }

    public void writeHex(byte[] bArr) throws IOException {
        int i = 2;
        int length = this.count + (bArr.length * 2) + 3;
        if (length > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[(bArr.length * 2) + 3];
                cArr[0] = 'x';
                cArr[1] = '\'';
                int i2 = 0;
                while (i2 < bArr.length) {
                    int i3 = bArr[i2] & 255;
                    int i4 = i3 >> 4;
                    int i5 = i3 & 15;
                    int i6 = i + 1;
                    cArr[i] = (char) (i4 + (i4 < 10 ? 48 : 55));
                    int i7 = i6 + 1;
                    cArr[i6] = (char) (i5 + (i5 < 10 ? 48 : 55));
                    i2++;
                    i = i7;
                }
                cArr[i] = '\'';
                try {
                    this.writer.write(cArr);
                    return;
                } catch (IOException e) {
                    throw new JSONException("writeBytes error.", e);
                }
            }
            expandCapacity(length);
        }
        char[] cArr2 = this.buf;
        int i8 = this.count;
        int i9 = i8 + 1;
        this.count = i9;
        cArr2[i8] = 'x';
        this.count = i9 + 1;
        cArr2[i9] = '\'';
        for (byte b : bArr) {
            int i10 = b & 255;
            int i11 = i10 >> 4;
            int i12 = i10 & 15;
            char[] cArr3 = this.buf;
            int i13 = this.count;
            this.count = i13 + 1;
            cArr3[i13] = (char) (i11 + (i11 < 10 ? 48 : 55));
            char[] cArr4 = this.buf;
            int i14 = this.count;
            this.count = i14 + 1;
            cArr4[i14] = (char) (i12 + (i12 < 10 ? 48 : 55));
        }
        char[] cArr5 = this.buf;
        int i15 = this.count;
        this.count = i15 + 1;
        cArr5[i15] = '\'';
    }

    public void writeInt(int i) throws IOException {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int iStringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int i2 = this.count + iStringSize;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[iStringSize];
                IOUtils.getChars(i, iStringSize, cArr);
                write(cArr, 0, iStringSize);
                return;
            }
            expandCapacity(i2);
        }
        IOUtils.getChars(i, i2, this.buf);
        this.count = i2;
    }

    public void writeLong(long j) throws IOException {
        boolean z = isEnabled(SerializerFeature.BrowserCompatible) && !isEnabled(SerializerFeature.WriteClassName) && (j > 9007199254740991L || j < -9007199254740991L);
        if (j == Long.MIN_VALUE) {
            if (z) {
                write("\"-9223372036854775808\"");
                return;
            } else {
                write("-9223372036854775808");
                return;
            }
        }
        int iStringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int i = this.count + iStringSize;
        int i2 = i;
        if (z) {
            i2 = i + 2;
        }
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[iStringSize];
                IOUtils.getChars(j, iStringSize, cArr);
                if (!z) {
                    write(cArr, 0, iStringSize);
                    return;
                }
                write(34);
                write(cArr, 0, iStringSize);
                write(34);
                return;
            }
            expandCapacity(i2);
        }
        if (z) {
            char[] cArr2 = this.buf;
            cArr2[this.count] = '\"';
            int i3 = i2 - 1;
            IOUtils.getChars(j, i3, cArr2);
            this.buf[i3] = '\"';
        } else {
            IOUtils.getChars(j, i2, this.buf);
        }
        this.count = i2;
    }

    public void writeLongAndChar(long j, char c) throws IOException {
        writeLong(j);
        write(c);
    }

    public void writeNull() {
        write("null");
    }

    public void writeNull(int i, int i2) {
        if ((i & i2) == 0 && (this.features & i2) == 0) {
            writeNull();
            return;
        }
        if (i2 == SerializerFeature.WriteNullListAsEmpty.mask) {
            write("[]");
            return;
        }
        if (i2 == SerializerFeature.WriteNullStringAsEmpty.mask) {
            writeString(BuildConfig.FLAVOR);
            return;
        }
        if (i2 == SerializerFeature.WriteNullBooleanAsFalse.mask) {
            write("false");
        } else if (i2 == SerializerFeature.WriteNullNumberAsZero.mask) {
            write(48);
        } else {
            writeNull();
        }
    }

    public void writeNull(SerializerFeature serializerFeature) {
        writeNull(0, serializerFeature.mask);
    }

    public void writeString(String str) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(str);
        } else {
            writeStringWithDoubleQuote(str, (char) 0);
        }
    }

    public void writeString(String str, char c) {
        if (!this.useSingleQuotes) {
            writeStringWithDoubleQuote(str, c);
        } else {
            writeStringWithSingleQuote(str);
            write(c);
        }
    }

    public void writeString(char[] cArr) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(cArr);
        } else {
            writeStringWithDoubleQuote(new String(cArr), (char) 0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:163:0x05d7 A[PHI: r14 r15 r16 r17
  0x05d7: PHI (r14v4 int) = (r14v3 int), (r14v6 int) binds: [B:189:0x0676, B:162:0x05c3] A[DONT_GENERATE, DONT_INLINE]
  0x05d7: PHI (r15v5 int) = (r15v2 int), (r15v8 int) binds: [B:189:0x0676, B:162:0x05c3] A[DONT_GENERATE, DONT_INLINE]
  0x05d7: PHI (r16v5 int) = (r16v2 int), (r16v8 int) binds: [B:189:0x0676, B:162:0x05c3] A[DONT_GENERATE, DONT_INLINE]
  0x05d7: PHI (r17v3 int) = (r17v2 int), (r17v5 int) binds: [B:189:0x0676, B:162:0x05c3] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x065a  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0679  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x023e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeStringWithDoubleQuote(java.lang.String r8, char r9) {
        /*
            Method dump skipped, instructions count: 2973
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuote(java.lang.String, char):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:187:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0600  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0238  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeStringWithDoubleQuote(char[] r8, char r9) {
        /*
            Method dump skipped, instructions count: 2800
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuote(char[], char):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0212  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void writeStringWithSingleQuote(java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 603
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithSingleQuote(java.lang.String):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x020d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void writeStringWithSingleQuote(char[] r8) {
        /*
            Method dump skipped, instructions count: 598
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithSingleQuote(char[]):void");
    }

    public void writeTo(OutputStream outputStream, String str) throws IOException {
        writeTo(outputStream, Charset.forName(str));
    }

    public void writeTo(OutputStream outputStream, Charset charset) throws IOException {
        writeToEx(outputStream, charset);
    }

    public void writeTo(Writer writer) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        writer.write(this.buf, 0, this.count);
    }

    public int writeToEx(OutputStream outputStream, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        if (charset == IOUtils.UTF8) {
            return encodeToUTF8(outputStream);
        }
        byte[] bytes = new String(this.buf, 0, this.count).getBytes(charset);
        outputStream.write(bytes);
        return bytes.length;
    }
}
