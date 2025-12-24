package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:okio/SegmentedByteString.class */
final class SegmentedByteString extends ByteString {
    final transient int[] directory;
    final transient byte[][] segments;

    /* JADX WARN: Type inference failed for: r1v4, types: [byte[], byte[][]] */
    SegmentedByteString(Buffer buffer, int i) {
        super(null);
        Util.checkOffsetAndCount(buffer.size, 0L, i);
        Segment segment = buffer.head;
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            if (segment.limit == segment.pos) {
                throw new AssertionError("s.limit == s.pos");
            }
            i2 += segment.limit - segment.pos;
            i3++;
            segment = segment.next;
        }
        this.segments = new byte[i3];
        this.directory = new int[i3 * 2];
        Segment segment2 = buffer.head;
        int i4 = 0;
        int i5 = 0;
        while (i5 < i) {
            this.segments[i4] = segment2.data;
            int i6 = i5 + (segment2.limit - segment2.pos);
            i5 = i6;
            if (i6 > i) {
                i5 = i;
            }
            int[] iArr = this.directory;
            iArr[i4] = i5;
            iArr[this.segments.length + i4] = segment2.pos;
            segment2.shared = true;
            i4++;
            segment2 = segment2.next;
        }
    }

    private int segment(int i) {
        int iBinarySearch = Arrays.binarySearch(this.directory, 0, this.segments.length, i + 1);
        if (iBinarySearch < 0) {
            iBinarySearch ^= -1;
        }
        return iBinarySearch;
    }

    private ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    private Object writeReplace() {
        return toByteString();
    }

    @Override // okio.ByteString
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
    }

    @Override // okio.ByteString
    public String base64() {
        return toByteString().base64();
    }

    @Override // okio.ByteString
    public String base64Url() {
        return toByteString().base64Url();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0031  */
    @Override // okio.ByteString
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            r8 = r0
            r0 = r7
            r1 = r6
            if (r0 != r1) goto L9
            r0 = 1
            return r0
        L9:
            r0 = r7
            boolean r0 = r0 instanceof okio.ByteString
            if (r0 == 0) goto L31
            r0 = r7
            okio.ByteString r0 = (okio.ByteString) r0
            r7 = r0
            r0 = r7
            int r0 = r0.size()
            r1 = r6
            int r1 = r1.size()
            if (r0 != r1) goto L31
            r0 = r6
            r1 = 0
            r2 = r7
            r3 = 0
            r4 = r6
            int r4 = r4.size()
            boolean r0 = r0.rangeEquals(r1, r2, r3, r4)
            if (r0 == 0) goto L31
            goto L33
        L31:
            r0 = 0
            r8 = r0
        L33:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.SegmentedByteString.equals(java.lang.Object):boolean");
    }

    @Override // okio.ByteString
    public byte getByte(int i) {
        Util.checkOffsetAndCount(this.directory[this.segments.length - 1], i, 1L);
        int iSegment = segment(i);
        int i2 = iSegment == 0 ? 0 : this.directory[iSegment - 1];
        int[] iArr = this.directory;
        byte[][] bArr = this.segments;
        return bArr[iSegment][(i - i2) + iArr[bArr.length + iSegment]];
    }

    @Override // okio.ByteString
    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        int length = this.segments.length;
        int i2 = 0;
        int i3 = 0;
        int i4 = 1;
        while (i2 < length) {
            byte[] bArr = this.segments[i2];
            int[] iArr = this.directory;
            int i5 = iArr[length + i2];
            int i6 = iArr[i2];
            for (int i7 = i5; i7 < (i6 - i3) + i5; i7++) {
                i4 = (i4 * 31) + bArr[i7];
            }
            i2++;
            i3 = i6;
        }
        this.hashCode = i4;
        return i4;
    }

    @Override // okio.ByteString
    public String hex() {
        return toByteString().hex();
    }

    @Override // okio.ByteString
    public ByteString hmacSha1(ByteString byteString) {
        return toByteString().hmacSha1(byteString);
    }

    @Override // okio.ByteString
    public ByteString hmacSha256(ByteString byteString) {
        return toByteString().hmacSha256(byteString);
    }

    @Override // okio.ByteString
    public int indexOf(byte[] bArr, int i) {
        return toByteString().indexOf(bArr, i);
    }

    @Override // okio.ByteString
    byte[] internalArray() {
        return toByteArray();
    }

    @Override // okio.ByteString
    public int lastIndexOf(byte[] bArr, int i) {
        return toByteString().lastIndexOf(bArr, i);
    }

    @Override // okio.ByteString
    public ByteString md5() {
        return toByteString().md5();
    }

    @Override // okio.ByteString
    public boolean rangeEquals(int i, ByteString byteString, int i2, int i3) {
        if (i < 0 || i > size() - i3) {
            return false;
        }
        int i4 = i;
        int iSegment = segment(i);
        while (i3 > 0) {
            int i5 = iSegment == 0 ? 0 : this.directory[iSegment - 1];
            int iMin = Math.min(i3, ((this.directory[iSegment] - i5) + i5) - i4);
            int[] iArr = this.directory;
            byte[][] bArr = this.segments;
            if (!byteString.rangeEquals(i2, bArr[iSegment], (i4 - i5) + iArr[bArr.length + iSegment], iMin)) {
                return false;
            }
            i4 += iMin;
            i2 += iMin;
            i3 -= iMin;
            iSegment++;
        }
        return true;
    }

    @Override // okio.ByteString
    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        if (i < 0 || i > size() - i3 || i2 < 0 || i2 > bArr.length - i3) {
            return false;
        }
        int i4 = i;
        int iSegment = segment(i);
        while (i3 > 0) {
            int i5 = iSegment == 0 ? 0 : this.directory[iSegment - 1];
            int iMin = Math.min(i3, ((this.directory[iSegment] - i5) + i5) - i4);
            int[] iArr = this.directory;
            byte[][] bArr2 = this.segments;
            if (!Util.arrayRangeEquals(bArr2[iSegment], (i4 - i5) + iArr[bArr2.length + iSegment], bArr, i2, iMin)) {
                return false;
            }
            i4 += iMin;
            i2 += iMin;
            i3 -= iMin;
            iSegment++;
        }
        return true;
    }

    @Override // okio.ByteString
    public ByteString sha1() {
        return toByteString().sha1();
    }

    @Override // okio.ByteString
    public ByteString sha256() {
        return toByteString().sha256();
    }

    @Override // okio.ByteString
    public int size() {
        return this.directory[this.segments.length - 1];
    }

    @Override // okio.ByteString
    public String string(Charset charset) {
        return toByteString().string(charset);
    }

    @Override // okio.ByteString
    public ByteString substring(int i) {
        return toByteString().substring(i);
    }

    @Override // okio.ByteString
    public ByteString substring(int i, int i2) {
        return toByteString().substring(i, i2);
    }

    @Override // okio.ByteString
    public ByteString toAsciiLowercase() {
        return toByteString().toAsciiLowercase();
    }

    @Override // okio.ByteString
    public ByteString toAsciiUppercase() {
        return toByteString().toAsciiUppercase();
    }

    @Override // okio.ByteString
    public byte[] toByteArray() {
        int[] iArr = this.directory;
        byte[][] bArr = this.segments;
        byte[] bArr2 = new byte[iArr[bArr.length - 1]];
        int length = bArr.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= length) {
                return bArr2;
            }
            int[] iArr2 = this.directory;
            int i4 = iArr2[length + i];
            int i5 = iArr2[i];
            System.arraycopy(this.segments[i], i4, bArr2, i3, i5 - i3);
            i++;
            i2 = i5;
        }
    }

    @Override // okio.ByteString
    public String toString() {
        return toByteString().toString();
    }

    @Override // okio.ByteString
    public String utf8() {
        return toByteString().utf8();
    }

    @Override // okio.ByteString
    public void write(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        int length = this.segments.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= length) {
                return;
            }
            int[] iArr = this.directory;
            int i4 = iArr[length + i];
            int i5 = iArr[i];
            outputStream.write(this.segments[i], i4, i5 - i3);
            i++;
            i2 = i5;
        }
    }

    @Override // okio.ByteString
    void write(Buffer buffer) {
        int length = this.segments.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= length) {
                buffer.size += i3;
                return;
            }
            int[] iArr = this.directory;
            int i4 = iArr[length + i];
            int i5 = iArr[i];
            Segment segment = new Segment(this.segments[i], i4, (i4 + i5) - i3, true, false);
            if (buffer.head == null) {
                segment.prev = segment;
                segment.next = segment;
                buffer.head = segment;
            } else {
                buffer.head.prev.push(segment);
            }
            i++;
            i2 = i5;
        }
    }
}
