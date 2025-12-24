package okhttp3.internal.p003ws;

import com.alibaba.fastjson.asm.Opcodes;
import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/ws/WebSocketWriter.class */
final class WebSocketWriter {
    boolean activeWriter;
    final Buffer buffer = new Buffer();
    final FrameSink frameSink = new FrameSink(this);
    final boolean isClient;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    final Random random;
    final BufferedSink sink;
    final Buffer sinkBuffer;
    boolean writerClosed;

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/ws/WebSocketWriter$FrameSink.class */
    final class FrameSink implements Sink {
        boolean closed;
        long contentLength;
        int formatOpcode;
        boolean isFirstFrame;
        final WebSocketWriter this$0;

        FrameSink(WebSocketWriter webSocketWriter) {
            this.this$0 = webSocketWriter;
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter webSocketWriter = this.this$0;
            webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.size(), this.isFirstFrame, true);
            this.closed = true;
            this.this$0.activeWriter = false;
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter webSocketWriter = this.this$0;
            webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.size(), this.isFirstFrame, false);
            this.isFirstFrame = false;
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return this.this$0.sink.timeout();
        }

        @Override // okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            this.this$0.buffer.write(buffer, j);
            boolean z = this.isFirstFrame && this.contentLength != -1 && this.this$0.buffer.size() > this.contentLength - 8192;
            long jCompleteSegmentByteCount = this.this$0.buffer.completeSegmentByteCount();
            if (jCompleteSegmentByteCount <= 0 || z) {
                return;
            }
            this.this$0.writeMessageFrame(this.formatOpcode, jCompleteSegmentByteCount, this.isFirstFrame, false);
            this.isFirstFrame = false;
        }
    }

    WebSocketWriter(boolean z, BufferedSink bufferedSink, Random random) {
        if (bufferedSink == null) {
            throw new NullPointerException("sink == null");
        }
        if (random == null) {
            throw new NullPointerException("random == null");
        }
        this.isClient = z;
        this.sink = bufferedSink;
        this.sinkBuffer = bufferedSink.buffer();
        this.random = random;
        this.maskKey = z ? new byte[4] : null;
        this.maskCursor = z ? new Buffer.UnsafeCursor() : null;
    }

    private void writeControlFrame(int i, ByteString byteString) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int size = byteString.size();
        if (size > 125) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        this.sinkBuffer.writeByte(i | 128);
        if (this.isClient) {
            this.sinkBuffer.writeByte(size | 128);
            this.random.nextBytes(this.maskKey);
            this.sinkBuffer.write(this.maskKey);
            if (size > 0) {
                long size2 = this.sinkBuffer.size();
                this.sinkBuffer.write(byteString);
                this.sinkBuffer.readAndWriteUnsafe(this.maskCursor);
                this.maskCursor.seek(size2);
                WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        } else {
            this.sinkBuffer.writeByte(size);
            this.sinkBuffer.write(byteString);
        }
        this.sink.flush();
    }

    Sink newMessageSink(int i, long j) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        this.frameSink.formatOpcode = i;
        this.frameSink.contentLength = j;
        this.frameSink.isFirstFrame = true;
        this.frameSink.closed = false;
        return this.frameSink;
    }

    void writeClose(int i, ByteString byteString) throws IOException {
        ByteString byteString2 = ByteString.EMPTY;
        if (i != 0 || byteString != null) {
            if (i != 0) {
                WebSocketProtocol.validateCloseCode(i);
            }
            Buffer buffer = new Buffer();
            buffer.writeShort(i);
            if (byteString != null) {
                buffer.write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        try {
            writeControlFrame(8, byteString2);
        } finally {
            this.writerClosed = true;
        }
    }

    void writeMessageFrame(int i, long j, boolean z, boolean z2) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        if (!z) {
            i = 0;
        }
        int i2 = i;
        if (z2) {
            i2 = i | 128;
        }
        this.sinkBuffer.writeByte(i2);
        int i3 = 0;
        if (this.isClient) {
            i3 = 128;
        }
        if (j <= 125) {
            this.sinkBuffer.writeByte(((int) j) | i3);
        } else if (j <= 65535) {
            this.sinkBuffer.writeByte(i3 | Opcodes.IAND);
            this.sinkBuffer.writeShort((int) j);
        } else {
            this.sinkBuffer.writeByte(i3 | 127);
            this.sinkBuffer.writeLong(j);
        }
        if (this.isClient) {
            this.random.nextBytes(this.maskKey);
            this.sinkBuffer.write(this.maskKey);
            if (j > 0) {
                long size = this.sinkBuffer.size();
                this.sinkBuffer.write(this.buffer, j);
                this.sinkBuffer.readAndWriteUnsafe(this.maskCursor);
                this.maskCursor.seek(size);
                WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        } else {
            this.sinkBuffer.write(this.buffer, j);
        }
        this.sink.emit();
    }

    void writePing(ByteString byteString) throws IOException {
        writeControlFrame(9, byteString);
    }

    void writePong(ByteString byteString) throws IOException {
        writeControlFrame(10, byteString);
    }
}
