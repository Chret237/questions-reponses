package okio;

import java.io.IOException;
import java.util.zip.Deflater;

/* loaded from: classes-dex2jar.jar:okio/DeflaterSink.class */
public final class DeflaterSink implements Sink {
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;

    DeflaterSink(BufferedSink bufferedSink, Deflater deflater) {
        if (bufferedSink == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.sink = bufferedSink;
        this.deflater = deflater;
    }

    public DeflaterSink(Sink sink, Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }

    private void deflate(boolean z) throws IOException {
        Segment segmentWritableSegment;
        Buffer buffer = this.sink.buffer();
        while (true) {
            segmentWritableSegment = buffer.writableSegment(1);
            int iDeflate = z ? this.deflater.deflate(segmentWritableSegment.data, segmentWritableSegment.limit, 8192 - segmentWritableSegment.limit, 2) : this.deflater.deflate(segmentWritableSegment.data, segmentWritableSegment.limit, 8192 - segmentWritableSegment.limit);
            if (iDeflate > 0) {
                segmentWritableSegment.limit += iDeflate;
                buffer.size += iDeflate;
                this.sink.emitCompleteSegments();
            } else if (this.deflater.needsInput()) {
                break;
            }
        }
        if (segmentWritableSegment.pos == segmentWritableSegment.limit) {
            buffer.head = segmentWritableSegment.pop();
            SegmentPool.recycle(segmentWritableSegment);
        }
    }

    @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        Throwable th;
        Throwable th2;
        if (this.closed) {
            return;
        }
        Throwable th3 = null;
        try {
            finishDeflate();
        } catch (Throwable th4) {
            th3 = th4;
        }
        try {
            this.deflater.end();
            th = th3;
        } catch (Throwable th5) {
            th = th3;
            if (th3 == null) {
                th = th5;
            }
        }
        try {
            this.sink.close();
            th2 = th;
        } catch (Throwable th6) {
            th2 = th;
            if (th == null) {
                th2 = th6;
            }
        }
        this.closed = true;
        if (th2 != null) {
            Util.sneakyRethrow(th2);
        }
    }

    void finishDeflate() throws IOException {
        this.deflater.finish();
        deflate(false);
    }

    @Override // okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        deflate(true);
        this.sink.flush();
    }

    @Override // okio.Sink
    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }

    @Override // okio.Sink
    public void write(Buffer buffer, long j) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0L, j);
        while (j > 0) {
            Segment segment = buffer.head;
            int iMin = (int) Math.min(j, segment.limit - segment.pos);
            this.deflater.setInput(segment.data, segment.pos, iMin);
            deflate(false);
            long j2 = iMin;
            buffer.size -= j2;
            segment.pos += iMin;
            if (segment.pos == segment.limit) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            j -= j2;
        }
    }
}
