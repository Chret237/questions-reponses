package okio;

import java.io.IOException;

/* loaded from: classes-dex2jar.jar:okio/Pipe.class */
public final class Pipe {
    final long maxBufferSize;
    boolean sinkClosed;
    boolean sourceClosed;
    final Buffer buffer = new Buffer();
    private final Sink sink = new PipeSink(this);
    private final Source source = new PipeSource(this);

    /* loaded from: classes-dex2jar.jar:okio/Pipe$PipeSink.class */
    final class PipeSink implements Sink {
        final Pipe this$0;
        final Timeout timeout = new Timeout();

        PipeSink(Pipe pipe) {
            this.this$0 = pipe;
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this.this$0.buffer) {
                if (this.this$0.sinkClosed) {
                    return;
                }
                if (this.this$0.sourceClosed && this.this$0.buffer.size() > 0) {
                    throw new IOException("source is closed");
                }
                this.this$0.sinkClosed = true;
                this.this$0.buffer.notifyAll();
            }
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            synchronized (this.this$0.buffer) {
                if (this.this$0.sinkClosed) {
                    throw new IllegalStateException("closed");
                }
                if (this.this$0.sourceClosed && this.this$0.buffer.size() > 0) {
                    throw new IOException("source is closed");
                }
            }
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return this.timeout;
        }

        @Override // okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            synchronized (this.this$0.buffer) {
                try {
                    if (this.this$0.sinkClosed) {
                        throw new IllegalStateException("closed");
                    }
                    while (j > 0) {
                        if (this.this$0.sourceClosed) {
                            throw new IOException("source is closed");
                        }
                        long size = this.this$0.maxBufferSize - this.this$0.buffer.size();
                        if (size == 0) {
                            this.timeout.waitUntilNotified(this.this$0.buffer);
                        } else {
                            long jMin = Math.min(size, j);
                            this.this$0.buffer.write(buffer, jMin);
                            j -= jMin;
                            this.this$0.buffer.notifyAll();
                        }
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:okio/Pipe$PipeSource.class */
    final class PipeSource implements Source {
        final Pipe this$0;
        final Timeout timeout = new Timeout();

        PipeSource(Pipe pipe) {
            this.this$0 = pipe;
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this.this$0.buffer) {
                this.this$0.sourceClosed = true;
                this.this$0.buffer.notifyAll();
            }
        }

        @Override // okio.Source
        public long read(Buffer buffer, long j) throws IOException {
            synchronized (this.this$0.buffer) {
                try {
                    if (this.this$0.sourceClosed) {
                        throw new IllegalStateException("closed");
                    }
                    while (this.this$0.buffer.size() == 0) {
                        if (this.this$0.sinkClosed) {
                            return -1L;
                        }
                        this.timeout.waitUntilNotified(this.this$0.buffer);
                    }
                    long j2 = this.this$0.buffer.read(buffer, j);
                    this.this$0.buffer.notifyAll();
                    return j2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.timeout;
        }
    }

    public Pipe(long j) {
        if (j >= 1) {
            this.maxBufferSize = j;
            return;
        }
        throw new IllegalArgumentException("maxBufferSize < 1: " + j);
    }

    public Sink sink() {
        return this.sink;
    }

    public Source source() {
        return this.source;
    }
}
