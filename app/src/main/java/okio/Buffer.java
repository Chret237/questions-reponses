package okio;

import com.alibaba.fastjson.asm.Opcodes;
import com.github.clans.fab.BuildConfig;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes-dex2jar.jar:okio/Buffer.class */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    private static final byte[] DIGITS = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    static final int REPLACEMENT_CHARACTER = 65533;

    @Nullable
    Segment head;
    long size;

    /* loaded from: classes-dex2jar.jar:okio/Buffer$UnsafeCursor.class */
    public static final class UnsafeCursor implements Closeable {
        public Buffer buffer;
        public byte[] data;
        public boolean readWrite;
        private Segment segment;
        public long offset = -1;
        public int start = -1;
        public int end = -1;

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            this.buffer = null;
            this.segment = null;
            this.offset = -1L;
            this.data = null;
            this.start = -1;
            this.end = -1;
        }

        public long expandBuffer(int i) {
            if (i <= 0) {
                throw new IllegalArgumentException("minByteCount <= 0: " + i);
            }
            if (i > 8192) {
                throw new IllegalArgumentException("minByteCount > Segment.SIZE: " + i);
            }
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            if (!this.readWrite) {
                throw new IllegalStateException("expandBuffer() only permitted for read/write buffers");
            }
            long j = buffer.size;
            Segment segmentWritableSegment = this.buffer.writableSegment(i);
            int i2 = 8192 - segmentWritableSegment.limit;
            segmentWritableSegment.limit = 8192;
            long j2 = i2;
            this.buffer.size = j + j2;
            this.segment = segmentWritableSegment;
            this.offset = j;
            this.data = segmentWritableSegment.data;
            this.start = 8192 - i2;
            this.end = 8192;
            return j2;
        }

        public int next() {
            if (this.offset == this.buffer.size) {
                throw new IllegalStateException();
            }
            long j = this.offset;
            return j == -1 ? seek(0L) : seek(j + (this.end - this.start));
        }

        public long resizeBuffer(long j) {
            Buffer buffer = this.buffer;
            if (buffer == null) {
                throw new IllegalStateException("not attached to a buffer");
            }
            if (!this.readWrite) {
                throw new IllegalStateException("resizeBuffer() only permitted for read/write buffers");
            }
            long j2 = buffer.size;
            if (j <= j2) {
                if (j < 0) {
                    throw new IllegalArgumentException("newSize < 0: " + j);
                }
                long j3 = j2;
                long j4 = j;
                while (true) {
                    long j5 = j3 - j4;
                    if (j5 <= 0) {
                        break;
                    }
                    Segment segment = this.buffer.head.prev;
                    long j6 = segment.limit - segment.pos;
                    if (j6 > j5) {
                        segment.limit = (int) (segment.limit - j5);
                        break;
                    }
                    this.buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                    j3 = j5;
                    j4 = j6;
                }
                this.segment = null;
                this.offset = j;
                this.data = null;
                this.start = -1;
                this.end = -1;
            } else if (j > j2) {
                long j7 = j - j2;
                boolean z = true;
                while (j7 > 0) {
                    Segment segmentWritableSegment = this.buffer.writableSegment(1);
                    int iMin = (int) Math.min(j7, 8192 - segmentWritableSegment.limit);
                    segmentWritableSegment.limit += iMin;
                    long j8 = j7 - iMin;
                    j7 = j8;
                    if (z) {
                        this.segment = segmentWritableSegment;
                        this.offset = j2;
                        this.data = segmentWritableSegment.data;
                        this.start = segmentWritableSegment.limit - iMin;
                        this.end = segmentWritableSegment.limit;
                        z = false;
                        j7 = j8;
                    }
                }
            }
            this.buffer.size = j;
            return j2;
        }

        public int seek(long j) {
            long j2;
            Segment segment;
            if (j < -1 || j > this.buffer.size) {
                throw new ArrayIndexOutOfBoundsException(String.format("offset=%s > size=%s", Long.valueOf(j), Long.valueOf(this.buffer.size)));
            }
            if (j == -1 || j == this.buffer.size) {
                this.segment = null;
                this.offset = j;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return -1;
            }
            long j3 = this.buffer.size;
            Segment segment2 = this.buffer.head;
            Segment segment3 = this.buffer.head;
            long j4 = 0;
            long j5 = j3;
            Segment segment4 = segment2;
            Segment segment5 = segment3;
            if (this.segment != null) {
                j4 = this.offset - (this.start - r0.pos);
                if (j4 > j) {
                    segment5 = this.segment;
                    j5 = j4;
                    j4 = 0;
                    segment4 = segment2;
                } else {
                    segment4 = this.segment;
                    segment5 = segment3;
                    j5 = j3;
                }
            }
            long j6 = j5;
            if (j5 - j > j - j4) {
                Segment segment6 = segment4;
                while (true) {
                    Segment segment7 = segment6;
                    j2 = j4;
                    segment = segment7;
                    if (j < (segment7.limit - segment7.pos) + j4) {
                        break;
                    }
                    j4 += segment7.limit - segment7.pos;
                    segment6 = segment7.next;
                }
            } else {
                while (j6 > j) {
                    segment5 = segment5.prev;
                    j6 -= segment5.limit - segment5.pos;
                }
                j2 = j6;
                segment = segment5;
            }
            Segment segmentPush = segment;
            if (this.readWrite) {
                segmentPush = segment;
                if (segment.shared) {
                    Segment segmentUnsharedCopy = segment.unsharedCopy();
                    if (this.buffer.head == segment) {
                        this.buffer.head = segmentUnsharedCopy;
                    }
                    segmentPush = segment.push(segmentUnsharedCopy);
                    segmentPush.prev.pop();
                }
            }
            this.segment = segmentPush;
            this.offset = j;
            this.data = segmentPush.data;
            this.start = segmentPush.pos + ((int) (j - j2));
            int i = segmentPush.limit;
            this.end = i;
            return i - this.start;
        }
    }

    private ByteString digest(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            if (this.head != null) {
                messageDigest.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                Segment segment = this.head;
                while (true) {
                    segment = segment.next;
                    if (segment == this.head) {
                        break;
                    }
                    messageDigest.update(segment.data, segment.pos, segment.limit - segment.pos);
                }
            }
            return ByteString.m19of(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    private ByteString hmac(String str, ByteString byteString) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            Mac mac = Mac.getInstance(str);
            mac.init(new SecretKeySpec(byteString.toByteArray(), str));
            if (this.head != null) {
                mac.update(this.head.data, this.head.pos, this.head.limit - this.head.pos);
                Segment segment = this.head;
                while (true) {
                    segment = segment.next;
                    if (segment == this.head) {
                        break;
                    }
                    mac.update(segment.data, segment.pos, segment.limit - segment.pos);
                }
            }
            return ByteString.m19of(mac.doFinal());
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError();
        }
    }

    private boolean rangeEquals(Segment segment, int i, ByteString byteString, int i2, int i3) {
        int i4 = segment.limit;
        byte[] bArr = segment.data;
        while (i2 < i3) {
            int i5 = i4;
            Segment segment2 = segment;
            int i6 = i;
            if (i == i4) {
                segment2 = segment.next;
                byte[] bArr2 = segment2.data;
                i6 = segment2.pos;
                i5 = segment2.limit;
                bArr = bArr2;
            }
            if (bArr[i6] != byteString.getByte(i2)) {
                return false;
            }
            i = i6 + 1;
            i2++;
            i4 = i5;
            segment = segment2;
        }
        return true;
    }

    private void readFrom(InputStream inputStream, long j, boolean z) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        while (true) {
            if (j <= 0 && !z) {
                return;
            }
            Segment segmentWritableSegment = writableSegment(1);
            int i = inputStream.read(segmentWritableSegment.data, segmentWritableSegment.limit, (int) Math.min(j, 8192 - segmentWritableSegment.limit));
            if (i == -1) {
                if (!z) {
                    throw new EOFException();
                }
                return;
            } else {
                segmentWritableSegment.limit += i;
                long j2 = i;
                this.size += j2;
                j -= j2;
            }
        }
    }

    @Override // okio.BufferedSource, okio.BufferedSink
    public Buffer buffer() {
        return this;
    }

    public void clear() {
        try {
            skip(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public Buffer clone() {
        Buffer buffer = new Buffer();
        if (this.size == 0) {
            return buffer;
        }
        Segment segmentSharedCopy = this.head.sharedCopy();
        buffer.head = segmentSharedCopy;
        segmentSharedCopy.prev = segmentSharedCopy;
        segmentSharedCopy.next = segmentSharedCopy;
        Segment segment = this.head;
        while (true) {
            segment = segment.next;
            if (segment == this.head) {
                buffer.size = this.size;
                return buffer;
            }
            buffer.head.prev.push(segment.sharedCopy());
        }
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public long completeSegmentByteCount() {
        long j = this.size;
        if (j == 0) {
            return 0L;
        }
        Segment segment = this.head.prev;
        long j2 = j;
        if (segment.limit < 8192) {
            j2 = j;
            if (segment.owner) {
                j2 = j - (segment.limit - segment.pos);
            }
        }
        return j2;
    }

    public Buffer copyTo(OutputStream outputStream) throws IOException {
        return copyTo(outputStream, 0L, this.size);
    }

    public Buffer copyTo(OutputStream outputStream, long j, long j2) throws IOException {
        Segment segment;
        long j3;
        long j4;
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        Segment segment2 = this.head;
        while (true) {
            Segment segment3 = segment2;
            segment = segment3;
            j3 = j;
            j4 = j2;
            if (j < segment3.limit - segment3.pos) {
                break;
            }
            j -= segment3.limit - segment3.pos;
            segment2 = segment3.next;
        }
        while (j4 > 0) {
            int iMin = (int) Math.min(segment.limit - r0, j4);
            outputStream.write(segment.data, (int) (segment.pos + j3), iMin);
            j4 -= iMin;
            segment = segment.next;
            j3 = 0;
        }
        return this;
    }

    public Buffer copyTo(Buffer buffer, long j, long j2) {
        Segment segment;
        long j3;
        long j4;
        if (buffer == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, j, j2);
        if (j2 == 0) {
            return this;
        }
        buffer.size += j2;
        Segment segment2 = this.head;
        while (true) {
            Segment segment3 = segment2;
            segment = segment3;
            j3 = j;
            j4 = j2;
            if (j < segment3.limit - segment3.pos) {
                break;
            }
            j -= segment3.limit - segment3.pos;
            segment2 = segment3.next;
        }
        while (j4 > 0) {
            Segment segmentSharedCopy = segment.sharedCopy();
            segmentSharedCopy.pos = (int) (segmentSharedCopy.pos + j3);
            segmentSharedCopy.limit = Math.min(segmentSharedCopy.pos + ((int) j4), segmentSharedCopy.limit);
            Segment segment4 = buffer.head;
            if (segment4 == null) {
                segmentSharedCopy.prev = segmentSharedCopy;
                segmentSharedCopy.next = segmentSharedCopy;
                buffer.head = segmentSharedCopy;
            } else {
                segment4.prev.push(segmentSharedCopy);
            }
            j4 -= segmentSharedCopy.limit - segmentSharedCopy.pos;
            segment = segment.next;
            j3 = 0;
        }
        return this;
    }

    @Override // okio.BufferedSink
    public BufferedSink emit() {
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer emitCompleteSegments() {
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Buffer)) {
            return false;
        }
        Buffer buffer = (Buffer) obj;
        long j = this.size;
        if (j != buffer.size) {
            return false;
        }
        long j2 = 0;
        if (j == 0) {
            return true;
        }
        Segment segment = this.head;
        Segment segment2 = buffer.head;
        int i = segment.pos;
        int i2 = segment2.pos;
        while (j2 < this.size) {
            long jMin = Math.min(segment.limit - i, segment2.limit - i2);
            int i3 = 0;
            while (i3 < jMin) {
                if (segment.data[i] != segment2.data[i2]) {
                    return false;
                }
                i3++;
                i++;
                i2++;
            }
            Segment segment3 = segment;
            int i4 = i;
            if (i == segment.limit) {
                segment3 = segment.next;
                i4 = segment3.pos;
            }
            int i5 = i2;
            Segment segment4 = segment2;
            if (i2 == segment2.limit) {
                segment4 = segment2.next;
                i5 = segment4.pos;
            }
            j2 += jMin;
            segment = segment3;
            i = i4;
            i2 = i5;
            segment2 = segment4;
        }
        return true;
    }

    @Override // okio.BufferedSource
    public boolean exhausted() {
        return this.size == 0;
    }

    @Override // okio.BufferedSink, okio.Sink, java.io.Flushable
    public void flush() {
    }

    public byte getByte(long j) {
        Segment segment;
        long j2;
        Util.checkOffsetAndCount(this.size, j, 1L);
        long j3 = this.size;
        if (j3 - j <= j) {
            long j4 = j - j3;
            Segment segment2 = this.head;
            do {
                segment = segment2.prev;
                j2 = j4 + (segment.limit - segment.pos);
                segment2 = segment;
                j4 = j2;
            } while (j2 < 0);
            return segment.data[segment.pos + ((int) j2)];
        }
        Segment segment3 = this.head;
        while (true) {
            Segment segment4 = segment3;
            long j5 = segment4.limit - segment4.pos;
            if (j < j5) {
                return segment4.data[segment4.pos + ((int) j)];
            }
            j -= j5;
            segment3 = segment4.next;
        }
    }

    public int hashCode() {
        int i;
        Segment segment;
        Segment segment2 = this.head;
        if (segment2 == null) {
            return 0;
        }
        int i2 = 1;
        do {
            int i3 = segment2.limit;
            i = i2;
            for (int i4 = segment2.pos; i4 < i3; i4++) {
                i = (i * 31) + segment2.data[i4];
            }
            segment = segment2.next;
            segment2 = segment;
            i2 = i;
        } while (segment != this.head);
        return i;
    }

    public ByteString hmacSha1(ByteString byteString) {
        return hmac("HmacSHA1", byteString);
    }

    public ByteString hmacSha256(ByteString byteString) {
        return hmac("HmacSHA256", byteString);
    }

    public ByteString hmacSha512(ByteString byteString) {
        return hmac("HmacSHA512", byteString);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b) {
        return indexOf(b, 0L, Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b, long j) {
        return indexOf(b, j, Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b, long j, long j2) {
        if (j < 0 || j2 < j) {
            throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", Long.valueOf(this.size), Long.valueOf(j), Long.valueOf(j2)));
        }
        long j3 = this.size;
        if (j2 <= j3) {
            j3 = j2;
        }
        if (j == j3) {
            return -1L;
        }
        Segment segment = this.head;
        if (segment == null) {
            return -1L;
        }
        long j4 = this.size;
        long j5 = 0;
        Segment segment2 = segment;
        if (j4 - j >= j) {
            while (true) {
                long j6 = (segment2.limit - segment2.pos) + j5;
                if (j6 >= j) {
                    break;
                }
                segment2 = segment2.next;
                j5 = j6;
            }
        } else {
            long j7 = j4;
            while (true) {
                long j8 = j7;
                segment2 = segment;
                j5 = j8;
                if (j8 <= j) {
                    break;
                }
                segment = segment.prev;
                j7 = j8 - (segment.limit - segment.pos);
            }
        }
        long j9 = j5;
        long j10 = j;
        while (j9 < j3) {
            byte[] bArr = segment2.data;
            int iMin = (int) Math.min(segment2.limit, (segment2.pos + j3) - j9);
            for (int i = (int) ((segment2.pos + j10) - j9); i < iMin; i++) {
                if (bArr[i] == b) {
                    return (i - segment2.pos) + j9;
                }
            }
            long j11 = j9 + (segment2.limit - segment2.pos);
            segment2 = segment2.next;
            j10 = j11;
            j9 = j11;
        }
        return -1L;
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString byteString) throws IOException {
        return indexOf(byteString, 0L);
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString byteString, long j) throws IOException {
        if (byteString.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        }
        long j2 = 0;
        if (j < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            return -1L;
        }
        long j3 = this.size;
        Segment segment2 = segment;
        if (j3 - j >= j) {
            while (true) {
                long j4 = (segment2.limit - segment2.pos) + j2;
                if (j4 >= j) {
                    break;
                }
                segment2 = segment2.next;
                j2 = j4;
            }
        } else {
            while (true) {
                segment2 = segment;
                j2 = j3;
                if (j3 <= j) {
                    break;
                }
                segment = segment.prev;
                j3 -= segment.limit - segment.pos;
            }
        }
        byte b = byteString.getByte(0);
        int size = byteString.size();
        long j5 = 1 + (this.size - size);
        Segment segment3 = segment2;
        long j6 = j2;
        long j7 = j;
        while (j6 < j5) {
            byte[] bArr = segment3.data;
            int iMin = (int) Math.min(segment3.limit, (segment3.pos + j5) - j6);
            for (int i = (int) ((segment3.pos + j7) - j6); i < iMin; i++) {
                if (bArr[i] == b && rangeEquals(segment3, i + 1, byteString, 1, size)) {
                    return (i - segment3.pos) + j6;
                }
            }
            long j8 = j6 + (segment3.limit - segment3.pos);
            segment3 = segment3.next;
            j7 = j8;
            j6 = j8;
        }
        return -1L;
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0L);
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString byteString, long j) {
        int i;
        int i2;
        long j2 = 0;
        if (j < 0) {
            throw new IllegalArgumentException("fromIndex < 0");
        }
        Segment segment = this.head;
        if (segment == null) {
            return -1L;
        }
        long j3 = this.size;
        Segment segment2 = segment;
        if (j3 - j >= j) {
            while (true) {
                long j4 = (segment2.limit - segment2.pos) + j2;
                if (j4 >= j) {
                    break;
                }
                segment2 = segment2.next;
                j2 = j4;
            }
        } else {
            while (true) {
                segment2 = segment;
                j2 = j3;
                if (j3 <= j) {
                    break;
                }
                segment = segment.prev;
                j3 -= segment.limit - segment.pos;
            }
        }
        if (byteString.size() == 2) {
            byte b = byteString.getByte(0);
            byte b2 = byteString.getByte(1);
            while (j2 < this.size) {
                byte[] bArr = segment2.data;
                i = (int) ((segment2.pos + j) - j2);
                int i3 = segment2.limit;
                while (i < i3) {
                    byte b3 = bArr[i];
                    if (b3 == b || b3 == b2) {
                        i2 = segment2.pos;
                    } else {
                        i++;
                    }
                }
                j2 += segment2.limit - segment2.pos;
                segment2 = segment2.next;
                j = j2;
            }
            return -1L;
        }
        byte[] bArrInternalArray = byteString.internalArray();
        while (j2 < this.size) {
            byte[] bArr2 = segment2.data;
            i = (int) ((segment2.pos + j) - j2);
            int i4 = segment2.limit;
            while (i < i4) {
                byte b4 = bArr2[i];
                for (byte b5 : bArrInternalArray) {
                    if (b4 == b5) {
                        i2 = segment2.pos;
                    }
                }
                i++;
            }
            j2 += segment2.limit - segment2.pos;
            segment2 = segment2.next;
            j = j2;
        }
        return -1L;
        return (i - i2) + j2;
    }

    @Override // okio.BufferedSource
    public InputStream inputStream() {
        return new InputStream(this) { // from class: okio.Buffer.2
            final Buffer this$0;

            {
                this.this$0 = this;
            }

            @Override // java.io.InputStream
            public int available() {
                return (int) Math.min(this.this$0.size, 2147483647L);
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            @Override // java.io.InputStream
            public int read() {
                if (this.this$0.size > 0) {
                    return this.this$0.readByte() & 255;
                }
                return -1;
            }

            @Override // java.io.InputStream
            public int read(byte[] bArr, int i, int i2) {
                return this.this$0.read(bArr, i, i2);
            }

            public String toString() {
                return this.this$0 + ".inputStream()";
            }
        };
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return true;
    }

    public ByteString md5() {
        return digest("MD5");
    }

    @Override // okio.BufferedSink
    public OutputStream outputStream() {
        return new OutputStream(this) { // from class: okio.Buffer.1
            final Buffer this$0;

            {
                this.this$0 = this;
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }

            @Override // java.io.OutputStream, java.io.Flushable
            public void flush() {
            }

            public String toString() {
                return this.this$0 + ".outputStream()";
            }

            @Override // java.io.OutputStream
            public void write(int i) {
                this.this$0.writeByte((int) ((byte) i));
            }

            @Override // java.io.OutputStream
            public void write(byte[] bArr, int i, int i2) {
                this.this$0.write(bArr, i, i2);
            }
        };
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long j, ByteString byteString) {
        return rangeEquals(j, byteString, 0, byteString.size());
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long j, ByteString byteString, int i, int i2) {
        if (j < 0 || i < 0 || i2 < 0 || this.size - j < i2 || byteString.size() - i < i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (getByte(i3 + j) != byteString.getByte(i + i3)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int iMin = Math.min(byteBuffer.remaining(), segment.limit - segment.pos);
        byteBuffer.put(segment.data, segment.pos, iMin);
        segment.pos += iMin;
        this.size -= iMin;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return iMin;
    }

    @Override // okio.BufferedSource
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // okio.BufferedSource
    public int read(byte[] bArr, int i, int i2) {
        Util.checkOffsetAndCount(bArr.length, i, i2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int iMin = Math.min(i2, segment.limit - segment.pos);
        System.arraycopy(segment.data, segment.pos, bArr, i, iMin);
        segment.pos += iMin;
        this.size -= iMin;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return iMin;
    }

    @Override // okio.Source
    public long read(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        }
        long j2 = this.size;
        if (j2 == 0) {
            return -1L;
        }
        long j3 = j;
        if (j > j2) {
            j3 = j2;
        }
        buffer.write(this, j3);
        return j3;
    }

    @Override // okio.BufferedSource
    public long readAll(Sink sink) throws IOException {
        long j = this.size;
        if (j > 0) {
            sink.write(this, j);
        }
        return j;
    }

    public UnsafeCursor readAndWriteUnsafe() {
        return readAndWriteUnsafe(new UnsafeCursor());
    }

    public UnsafeCursor readAndWriteUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = true;
        return unsafeCursor;
    }

    @Override // okio.BufferedSource
    public byte readByte() {
        if (this.size == 0) {
            throw new IllegalStateException("size == 0");
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        byte[] bArr = segment.data;
        int i3 = i + 1;
        byte b = bArr[i];
        this.size--;
        if (i3 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i3;
        }
        return b;
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray() {
        try {
            return readByteArray(this.size);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray(long j) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, j);
        if (j <= 2147483647L) {
            byte[] bArr = new byte[(int) j];
            readFully(bArr);
            return bArr;
        }
        throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + j);
    }

    @Override // okio.BufferedSource
    public ByteString readByteString() {
        return new ByteString(readByteArray());
    }

    @Override // okio.BufferedSource
    public ByteString readByteString(long j) throws EOFException {
        return new ByteString(readByteArray(j));
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0087, code lost:
    
        r0 = new okio.Buffer().writeDecimalLong(r14).writeByte((int) r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x009c, code lost:
    
        if (r10 != false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x009f, code lost:
    
        r0.readByte();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ce, code lost:
    
        throw new java.lang.NumberFormatException("Number too large: " + r0.readUtf8());
     */
    @Override // okio.BufferedSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long readDecimalLong() {
        /*
            Method dump skipped, instructions count: 386
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readDecimalLong():long");
    }

    public Buffer readFrom(InputStream inputStream) throws IOException {
        readFrom(inputStream, Long.MAX_VALUE, true);
        return this;
    }

    public Buffer readFrom(InputStream inputStream, long j) throws IOException {
        if (j >= 0) {
            readFrom(inputStream, j, false);
            return this;
        }
        throw new IllegalArgumentException("byteCount < 0: " + j);
    }

    @Override // okio.BufferedSource
    public void readFully(Buffer buffer, long j) throws EOFException {
        long j2 = this.size;
        if (j2 >= j) {
            buffer.write(this, j);
        } else {
            buffer.write(this, j2);
            throw new EOFException();
        }
    }

    @Override // okio.BufferedSource
    public void readFully(byte[] bArr) throws EOFException {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= bArr.length) {
                return;
            }
            int i3 = read(bArr, i2, bArr.length - i2);
            if (i3 == -1) {
                throw new EOFException();
            }
            i = i2 + i3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e8, code lost:
    
        if (r8 == 0) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00eb, code lost:
    
        r11 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x011a, code lost:
    
        throw new java.lang.NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + java.lang.Integer.toHexString(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x011e, code lost:
    
        if (r9 != r0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0121, code lost:
    
        r6.head = r0.pop();
        okio.SegmentPool.recycle(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0132, code lost:
    
        r0.pos = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x013a, code lost:
    
        if (r11 != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x013d, code lost:
    
        r10 = r8;
        r7 = r11;
        r15 = r13;
     */
    @Override // okio.BufferedSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long readHexadecimalUnsignedLong() {
        /*
            Method dump skipped, instructions count: 369
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.readHexadecimalUnsignedLong():long");
    }

    @Override // okio.BufferedSource
    public int readInt() {
        if (this.size < 4) {
            throw new IllegalStateException("size < 4: " + this.size);
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 4) {
            return ((readByte() & 255) << 24) | ((readByte() & 255) << 16) | ((readByte() & 255) << 8) | (readByte() & 255);
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        byte b = bArr[i];
        int i4 = i3 + 1;
        byte b2 = bArr[i3];
        int i5 = i4 + 1;
        byte b3 = bArr[i4];
        int i6 = i5 + 1;
        byte b4 = bArr[i5];
        this.size -= 4;
        if (i6 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i6;
        }
        return ((b & 255) << 24) | ((b2 & 255) << 16) | ((b3 & 255) << 8) | (b4 & 255);
    }

    @Override // okio.BufferedSource
    public int readIntLe() {
        return Util.reverseBytesInt(readInt());
    }

    @Override // okio.BufferedSource
    public long readLong() {
        if (this.size < 8) {
            throw new IllegalStateException("size < 8: " + this.size);
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 8) {
            return ((readInt() & 4294967295L) << 32) | (4294967295L & readInt());
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        long j = bArr[i];
        int i4 = i3 + 1;
        long j2 = bArr[i3];
        int i5 = i4 + 1;
        long j3 = bArr[i4];
        int i6 = i5 + 1;
        long j4 = bArr[i5];
        int i7 = i6 + 1;
        long j5 = bArr[i6];
        int i8 = i7 + 1;
        long j6 = bArr[i7];
        int i9 = i8 + 1;
        long j7 = bArr[i8];
        int i10 = i9 + 1;
        long j8 = bArr[i9];
        this.size -= 8;
        if (i10 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i10;
        }
        return (j8 & 255) | ((j7 & 255) << 8) | ((j & 255) << 56) | ((j2 & 255) << 48) | ((j3 & 255) << 40) | ((j4 & 255) << 32) | ((j5 & 255) << 24) | ((j6 & 255) << 16);
    }

    @Override // okio.BufferedSource
    public long readLongLe() {
        return Util.reverseBytesLong(readLong());
    }

    @Override // okio.BufferedSource
    public short readShort() {
        if (this.size < 2) {
            throw new IllegalStateException("size < 2: " + this.size);
        }
        Segment segment = this.head;
        int i = segment.pos;
        int i2 = segment.limit;
        if (i2 - i < 2) {
            return (short) (((readByte() & 255) << 8) | (readByte() & 255));
        }
        byte[] bArr = segment.data;
        int i3 = i + 1;
        byte b = bArr[i];
        int i4 = i3 + 1;
        byte b2 = bArr[i3];
        this.size -= 2;
        if (i4 == i2) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        } else {
            segment.pos = i4;
        }
        return (short) (((b & 255) << 8) | (b2 & 255));
    }

    @Override // okio.BufferedSource
    public short readShortLe() {
        return Util.reverseBytesShort(readShort());
    }

    @Override // okio.BufferedSource
    public String readString(long j, Charset charset) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, j);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        if (j > 2147483647L) {
            throw new IllegalArgumentException("byteCount > Integer.MAX_VALUE: " + j);
        }
        if (j == 0) {
            return BuildConfig.FLAVOR;
        }
        Segment segment = this.head;
        if (segment.pos + j > segment.limit) {
            return new String(readByteArray(j), charset);
        }
        String str = new String(segment.data, segment.pos, (int) j, charset);
        segment.pos = (int) (segment.pos + j);
        this.size -= j;
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return str;
    }

    @Override // okio.BufferedSource
    public String readString(Charset charset) {
        try {
            return readString(this.size, charset);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    public UnsafeCursor readUnsafe() {
        return readUnsafe(new UnsafeCursor());
    }

    public UnsafeCursor readUnsafe(UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer != null) {
            throw new IllegalStateException("already attached to a buffer");
        }
        unsafeCursor.buffer = this;
        unsafeCursor.readWrite = false;
        return unsafeCursor;
    }

    @Override // okio.BufferedSource
    public String readUtf8() {
        try {
            return readString(this.size, Util.UTF_8);
        } catch (EOFException e) {
            throw new AssertionError(e);
        }
    }

    @Override // okio.BufferedSource
    public String readUtf8(long j) throws EOFException {
        return readString(j, Util.UTF_8);
    }

    @Override // okio.BufferedSource
    public int readUtf8CodePoint() throws EOFException {
        int i;
        int i2;
        int i3;
        if (this.size == 0) {
            throw new EOFException();
        }
        byte b = getByte(0L);
        if ((b & 128) == 0) {
            i = b & 127;
            i2 = 1;
            i3 = 0;
        } else if ((b & 224) == 192) {
            i = b & 31;
            i2 = 2;
            i3 = 128;
        } else if ((b & 240) == 224) {
            i = b & 15;
            i2 = 3;
            i3 = 2048;
        } else {
            if ((b & 248) != 240) {
                skip(1L);
                return REPLACEMENT_CHARACTER;
            }
            i = b & 7;
            i2 = 4;
            i3 = 65536;
        }
        long j = i2;
        if (this.size < j) {
            throw new EOFException("size < " + i2 + ": " + this.size + " (to read code point prefixed 0x" + Integer.toHexString(b) + ")");
        }
        for (int i4 = 1; i4 < i2; i4++) {
            long j2 = i4;
            byte b2 = getByte(j2);
            if ((b2 & 192) != 128) {
                skip(j2);
                return REPLACEMENT_CHARACTER;
            }
            i = (i << 6) | (b2 & 63);
        }
        skip(j);
        return i > 1114111 ? REPLACEMENT_CHARACTER : ((i < 55296 || i > 57343) && i >= i3) ? i : REPLACEMENT_CHARACTER;
    }

    @Override // okio.BufferedSource
    @Nullable
    public String readUtf8Line() throws EOFException {
        long jIndexOf = indexOf((byte) 10);
        if (jIndexOf != -1) {
            return readUtf8Line(jIndexOf);
        }
        long j = this.size;
        return j != 0 ? readUtf8(j) : null;
    }

    String readUtf8Line(long j) throws EOFException {
        if (j > 0) {
            long j2 = j - 1;
            if (getByte(j2) == 13) {
                String utf8 = readUtf8(j2);
                skip(2L);
                return utf8;
            }
        }
        String utf82 = readUtf8(j);
        skip(1L);
        return utf82;
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict() throws EOFException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict(long j) throws EOFException {
        if (j < 0) {
            throw new IllegalArgumentException("limit < 0: " + j);
        }
        long j2 = Long.MAX_VALUE;
        if (j != Long.MAX_VALUE) {
            j2 = j + 1;
        }
        long jIndexOf = indexOf((byte) 10, 0L, j2);
        if (jIndexOf != -1) {
            return readUtf8Line(jIndexOf);
        }
        if (j2 < size() && getByte(j2 - 1) == 13 && getByte(j2) == 10) {
            return readUtf8Line(j2);
        }
        Buffer buffer = new Buffer();
        copyTo(buffer, 0L, Math.min(32L, size()));
        throw new EOFException("\\n not found: limit=" + Math.min(size(), j) + " content=" + buffer.readByteString().hex() + (char) 8230);
    }

    @Override // okio.BufferedSource
    public boolean request(long j) {
        return this.size >= j;
    }

    @Override // okio.BufferedSource
    public void require(long j) throws EOFException {
        if (this.size < j) {
            throw new EOFException();
        }
    }

    List<Integer> segmentSizes() {
        if (this.head == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(this.head.limit - this.head.pos));
        Segment segment = this.head;
        while (true) {
            segment = segment.next;
            if (segment == this.head) {
                return arrayList;
            }
            arrayList.add(Integer.valueOf(segment.limit - segment.pos));
        }
    }

    @Override // okio.BufferedSource
    public int select(Options options) {
        Segment segment = this.head;
        if (segment == null) {
            return options.indexOf(ByteString.EMPTY);
        }
        ByteString[] byteStringArr = options.byteStrings;
        int length = byteStringArr.length;
        for (int i = 0; i < length; i++) {
            ByteString byteString = byteStringArr[i];
            if (this.size >= byteString.size() && rangeEquals(segment, segment.pos, byteString, 0, byteString.size())) {
                try {
                    skip(byteString.size());
                    return i;
                } catch (EOFException e) {
                    throw new AssertionError(e);
                }
            }
        }
        return -1;
    }

    int selectPrefix(Options options) {
        Segment segment = this.head;
        ByteString[] byteStringArr = options.byteStrings;
        int length = byteStringArr.length;
        for (int i = 0; i < length; i++) {
            ByteString byteString = byteStringArr[i];
            int iMin = (int) Math.min(this.size, byteString.size());
            if (iMin == 0 || rangeEquals(segment, segment.pos, byteString, 0, iMin)) {
                return i;
            }
        }
        return -1;
    }

    public ByteString sha1() {
        return digest("SHA-1");
    }

    public ByteString sha256() {
        return digest("SHA-256");
    }

    public ByteString sha512() {
        return digest("SHA-512");
    }

    public long size() {
        return this.size;
    }

    @Override // okio.BufferedSource
    public void skip(long j) throws EOFException {
        while (j > 0) {
            if (this.head == null) {
                throw new EOFException();
            }
            int iMin = (int) Math.min(j, r0.limit - this.head.pos);
            long j2 = iMin;
            this.size -= j2;
            long j3 = j - j2;
            this.head.pos += iMin;
            j = j3;
            if (this.head.pos == this.head.limit) {
                Segment segment = this.head;
                this.head = segment.pop();
                SegmentPool.recycle(segment);
                j = j3;
            }
        }
    }

    public ByteString snapshot() {
        long j = this.size;
        if (j <= 2147483647L) {
            return snapshot((int) j);
        }
        throw new IllegalArgumentException("size > Integer.MAX_VALUE: " + this.size);
    }

    public ByteString snapshot(int i) {
        return i == 0 ? ByteString.EMPTY : new SegmentedByteString(this, i);
    }

    @Override // okio.Source
    public Timeout timeout() {
        return Timeout.NONE;
    }

    public String toString() {
        return snapshot().toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    okio.Segment writableSegment(int r4) {
        /*
            r3 = this;
            r0 = r4
            r1 = 1
            if (r0 < r1) goto L4e
            r0 = r4
            r1 = 8192(0x2000, float:1.148E-41)
            if (r0 > r1) goto L4e
            r0 = r3
            okio.Segment r0 = r0.head
            r5 = r0
            r0 = r5
            if (r0 != 0) goto L2a
            okio.Segment r0 = okio.SegmentPool.take()
            r5 = r0
            r0 = r3
            r1 = r5
            r0.head = r1
            r0 = r5
            r1 = r5
            r0.prev = r1
            r0 = r5
            r1 = r5
            r0.next = r1
            r0 = r5
            return r0
        L2a:
            r0 = r5
            okio.Segment r0 = r0.prev
            r6 = r0
            r0 = r6
            int r0 = r0.limit
            r1 = r4
            int r0 = r0 + r1
            r1 = 8192(0x2000, float:1.148E-41)
            if (r0 > r1) goto L44
            r0 = r6
            r5 = r0
            r0 = r6
            boolean r0 = r0.owner
            if (r0 != 0) goto L4c
        L44:
            r0 = r6
            okio.Segment r1 = okio.SegmentPool.take()
            okio.Segment r0 = r0.push(r1)
            r5 = r0
        L4c:
            r0 = r5
            return r0
        L4e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            r1.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.writableSegment(int):okio.Segment");
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            throw new IllegalArgumentException("source == null");
        }
        int iRemaining = byteBuffer.remaining();
        int i = iRemaining;
        while (i > 0) {
            Segment segmentWritableSegment = writableSegment(1);
            int iMin = Math.min(i, 8192 - segmentWritableSegment.limit);
            byteBuffer.get(segmentWritableSegment.data, segmentWritableSegment.limit, iMin);
            i -= iMin;
            segmentWritableSegment.limit += iMin;
        }
        this.size += iRemaining;
        return iRemaining;
    }

    @Override // okio.BufferedSink
    public Buffer write(ByteString byteString) {
        if (byteString == null) {
            throw new IllegalArgumentException("byteString == null");
        }
        byteString.write(this);
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer write(byte[] bArr) {
        if (bArr != null) {
            return write(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("source == null");
    }

    @Override // okio.BufferedSink
    public Buffer write(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = i2;
        Util.checkOffsetAndCount(bArr.length, i, j);
        int i3 = i2 + i;
        while (i < i3) {
            Segment segmentWritableSegment = writableSegment(1);
            int iMin = Math.min(i3 - i, 8192 - segmentWritableSegment.limit);
            System.arraycopy(bArr, i, segmentWritableSegment.data, segmentWritableSegment.limit, iMin);
            i += iMin;
            segmentWritableSegment.limit += iMin;
        }
        this.size += j;
        return this;
    }

    @Override // okio.BufferedSink
    public BufferedSink write(Source source, long j) throws IOException {
        while (j > 0) {
            long j2 = source.read(this, j);
            if (j2 == -1) {
                throw new EOFException();
            }
            j -= j2;
        }
        return this;
    }

    @Override // okio.Sink
    public void write(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (buffer == this) {
            throw new IllegalArgumentException("source == this");
        }
        Util.checkOffsetAndCount(buffer.size, 0L, j);
        while (j > 0) {
            if (j < buffer.head.limit - buffer.head.pos) {
                Segment segment = this.head;
                Segment segment2 = segment != null ? segment.prev : null;
                if (segment2 != null && segment2.owner) {
                    if ((segment2.limit + j) - (segment2.shared ? 0 : segment2.pos) <= 8192) {
                        buffer.head.writeTo(segment2, (int) j);
                        buffer.size -= j;
                        this.size += j;
                        return;
                    }
                }
                buffer.head = buffer.head.split((int) j);
            }
            Segment segment3 = buffer.head;
            long j2 = segment3.limit - segment3.pos;
            buffer.head = segment3.pop();
            Segment segment4 = this.head;
            if (segment4 == null) {
                this.head = segment3;
                segment3.prev = segment3;
                segment3.next = segment3;
            } else {
                segment4.prev.push(segment3).compact();
            }
            buffer.size -= j2;
            this.size += j2;
            j -= j2;
        }
    }

    @Override // okio.BufferedSink
    public long writeAll(Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = 0;
        while (true) {
            long j2 = j;
            long j3 = source.read(this, 8192L);
            if (j3 == -1) {
                return j2;
            }
            j = j2 + j3;
        }
    }

    @Override // okio.BufferedSink
    public Buffer writeByte(int i) {
        Segment segmentWritableSegment = writableSegment(1);
        byte[] bArr = segmentWritableSegment.data;
        int i2 = segmentWritableSegment.limit;
        segmentWritableSegment.limit = i2 + 1;
        bArr[i2] = (byte) i;
        this.size++;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeDecimalLong(long j) {
        if (j == 0) {
            return writeByte(48);
        }
        boolean z = false;
        int i = 1;
        long j2 = j;
        if (j < 0) {
            j2 = -j;
            if (j2 < 0) {
                return writeUtf8("-9223372036854775808");
            }
            z = true;
        }
        if (j2 >= 100000000) {
            i = j2 < 1000000000000L ? j2 < 10000000000L ? j2 < 1000000000 ? 9 : 10 : j2 < 100000000000L ? 11 : 12 : j2 < 1000000000000000L ? j2 < 10000000000000L ? 13 : j2 < 100000000000000L ? 14 : 15 : j2 < 100000000000000000L ? j2 < 10000000000000000L ? 16 : 17 : j2 < 1000000000000000000L ? 18 : 19;
        } else if (j2 >= 10000) {
            i = j2 < 1000000 ? j2 < 100000 ? 5 : 6 : j2 < 10000000 ? 7 : 8;
        } else if (j2 >= 100) {
            i = j2 < 1000 ? 3 : 4;
        } else if (j2 >= 10) {
            i = 2;
        }
        int i2 = i;
        if (z) {
            i2 = i + 1;
        }
        Segment segmentWritableSegment = writableSegment(i2);
        byte[] bArr = segmentWritableSegment.data;
        int i3 = segmentWritableSegment.limit + i2;
        while (j2 != 0) {
            i3--;
            bArr[i3] = DIGITS[(int) (j2 % 10)];
            j2 /= 10;
        }
        if (z) {
            bArr[i3 - 1] = 45;
        }
        segmentWritableSegment.limit += i2;
        this.size += i2;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeHexadecimalUnsignedLong(long j) {
        if (j == 0) {
            return writeByte(48);
        }
        int iNumberOfTrailingZeros = (Long.numberOfTrailingZeros(Long.highestOneBit(j)) / 4) + 1;
        Segment segmentWritableSegment = writableSegment(iNumberOfTrailingZeros);
        byte[] bArr = segmentWritableSegment.data;
        int i = segmentWritableSegment.limit;
        for (int i2 = (segmentWritableSegment.limit + iNumberOfTrailingZeros) - 1; i2 >= i; i2--) {
            bArr[i2] = DIGITS[(int) (15 & j)];
            j >>>= 4;
        }
        segmentWritableSegment.limit += iNumberOfTrailingZeros;
        this.size += iNumberOfTrailingZeros;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeInt(int i) {
        Segment segmentWritableSegment = writableSegment(4);
        byte[] bArr = segmentWritableSegment.data;
        int i2 = segmentWritableSegment.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        segmentWritableSegment.limit = i5 + 1;
        this.size += 4;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeIntLe(int i) {
        return writeInt(Util.reverseBytesInt(i));
    }

    @Override // okio.BufferedSink
    public Buffer writeLong(long j) {
        Segment segmentWritableSegment = writableSegment(8);
        byte[] bArr = segmentWritableSegment.data;
        int i = segmentWritableSegment.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((j >>> 56) & 255);
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((j >>> 48) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((j >>> 40) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((j >>> 32) & 255);
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((j >>> 24) & 255);
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((j >>> 16) & 255);
        int i8 = i7 + 1;
        bArr[i7] = (byte) ((j >>> 8) & 255);
        bArr[i8] = (byte) (j & 255);
        segmentWritableSegment.limit = i8 + 1;
        this.size += 8;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeLongLe(long j) {
        return writeLong(Util.reverseBytesLong(j));
    }

    @Override // okio.BufferedSink
    public Buffer writeShort(int i) {
        Segment segmentWritableSegment = writableSegment(2);
        byte[] bArr = segmentWritableSegment.data;
        int i2 = segmentWritableSegment.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        bArr[i3] = (byte) (i & 255);
        segmentWritableSegment.limit = i3 + 1;
        this.size += 2;
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeShortLe(int i) {
        return writeShort((int) Util.reverseBytesShort((short) i));
    }

    @Override // okio.BufferedSink
    public Buffer writeString(String str, int i, int i2, Charset charset) {
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalAccessError("beginIndex < 0: " + i);
        }
        if (i2 < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
        }
        if (i2 <= str.length()) {
            if (charset == null) {
                throw new IllegalArgumentException("charset == null");
            }
            if (charset.equals(Util.UTF_8)) {
                return writeUtf8(str, i, i2);
            }
            byte[] bytes = str.substring(i, i2).getBytes(charset);
            return write(bytes, 0, bytes.length);
        }
        throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
    }

    @Override // okio.BufferedSink
    public Buffer writeString(String str, Charset charset) {
        return writeString(str, 0, str.length(), charset);
    }

    public Buffer writeTo(OutputStream outputStream) throws IOException {
        return writeTo(outputStream, this.size);
    }

    public Buffer writeTo(OutputStream outputStream, long j) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, 0L, j);
        Segment segment = this.head;
        while (j > 0) {
            int iMin = (int) Math.min(j, segment.limit - segment.pos);
            outputStream.write(segment.data, segment.pos, iMin);
            segment.pos += iMin;
            long j2 = iMin;
            this.size -= j2;
            long j3 = j - j2;
            j = j3;
            if (segment.pos == segment.limit) {
                Segment segmentPop = segment.pop();
                this.head = segmentPop;
                SegmentPool.recycle(segment);
                segment = segmentPop;
                j = j3;
            }
        }
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String str) {
        return writeUtf8(str, 0, str.length());
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String str, int i, int i2) {
        char cCharAt;
        if (str == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + i);
        }
        if (i2 < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + i2 + " < " + i);
        }
        if (i2 > str.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + i2 + " > " + str.length());
        }
        while (i < i2) {
            char cCharAt2 = str.charAt(i);
            if (cCharAt2 < 128) {
                Segment segmentWritableSegment = writableSegment(1);
                byte[] bArr = segmentWritableSegment.data;
                int i3 = segmentWritableSegment.limit - i;
                int iMin = Math.min(i2, 8192 - i3);
                bArr[i + i3] = (byte) cCharAt2;
                i++;
                while (i < iMin && (cCharAt = str.charAt(i)) < 128) {
                    bArr[i + i3] = (byte) cCharAt;
                    i++;
                }
                int i4 = (i3 + i) - segmentWritableSegment.limit;
                segmentWritableSegment.limit += i4;
                this.size += i4;
            } else {
                if (cCharAt2 < 2048) {
                    writeByte((cCharAt2 >> 6) | Opcodes.CHECKCAST);
                    writeByte((cCharAt2 & '?') | 128);
                } else if (cCharAt2 < 55296 || cCharAt2 > 57343) {
                    writeByte((cCharAt2 >> '\f') | 224);
                    writeByte(((cCharAt2 >> 6) & 63) | 128);
                    writeByte((cCharAt2 & '?') | 128);
                } else {
                    int i5 = i + 1;
                    char cCharAt3 = i5 < i2 ? str.charAt(i5) : (char) 0;
                    if (cCharAt2 > 56319 || cCharAt3 < 56320 || cCharAt3 > 57343) {
                        writeByte(63);
                        i = i5;
                    } else {
                        int i6 = (((cCharAt2 & 10239) << 10) | (9215 & cCharAt3)) + 65536;
                        writeByte((i6 >> 18) | 240);
                        writeByte(((i6 >> 12) & 63) | 128);
                        writeByte(((i6 >> 6) & 63) | 128);
                        writeByte((i6 & 63) | 128);
                        i += 2;
                    }
                }
                i++;
            }
        }
        return this;
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8CodePoint(int i) {
        if (i < 128) {
            writeByte(i);
        } else if (i < 2048) {
            writeByte((i >> 6) | Opcodes.CHECKCAST);
            writeByte((i & 63) | 128);
        } else if (i < 65536) {
            if (i < 55296 || i > 57343) {
                writeByte((i >> 12) | 224);
                writeByte(((i >> 6) & 63) | 128);
                writeByte((i & 63) | 128);
            } else {
                writeByte(63);
            }
        } else {
            if (i > 1114111) {
                throw new IllegalArgumentException("Unexpected code point: " + Integer.toHexString(i));
            }
            writeByte((i >> 18) | 240);
            writeByte(((i >> 12) & 63) | 128);
            writeByte(((i >> 6) & 63) | 128);
            writeByte((i & 63) | 128);
        }
        return this;
    }
}
