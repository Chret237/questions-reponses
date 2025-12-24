package okhttp3.internal.http2;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Stream.class */
public final class Http2Stream {
    static final boolean $assertionsDisabled = false;
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    private boolean hasResponseHeaders;

    /* renamed from: id */
    final int f84id;
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final FramingSink sink;
    private final FramingSource source;
    long unacknowledgedBytesRead = 0;
    final StreamTimeout readTimeout = new StreamTimeout(this);
    final StreamTimeout writeTimeout = new StreamTimeout(this);
    ErrorCode errorCode = null;

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Stream$FramingSink.class */
    final class FramingSink implements Sink {
        static final boolean $assertionsDisabled = false;
        private static final long EMIT_BUFFER_SIZE = 16384;
        boolean closed;
        boolean finished;
        private final Buffer sendBuffer = new Buffer();
        final Http2Stream this$0;

        FramingSink(Http2Stream http2Stream) {
            this.this$0 = http2Stream;
        }

        private void emitFrame(boolean z) throws IOException {
            long jMin;
            synchronized (this.this$0) {
                try {
                    this.this$0.writeTimeout.enter();
                    while (this.this$0.bytesLeftInWriteWindow <= 0 && !this.finished && !this.closed && this.this$0.errorCode == null) {
                        try {
                            this.this$0.waitForIo();
                        } finally {
                        }
                    }
                    this.this$0.writeTimeout.exitAndThrowIfTimedOut();
                    this.this$0.checkOutNotClosed();
                    jMin = Math.min(this.this$0.bytesLeftInWriteWindow, this.sendBuffer.size());
                    this.this$0.bytesLeftInWriteWindow -= jMin;
                } catch (Throwable th) {
                    throw th;
                }
            }
            this.this$0.writeTimeout.enter();
            try {
                this.this$0.connection.writeData(this.this$0.f84id, z && jMin == this.sendBuffer.size(), this.sendBuffer, jMin);
            } finally {
            }
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this.this$0) {
                try {
                    if (this.closed) {
                        return;
                    }
                    if (!this.this$0.sink.finished) {
                        if (this.sendBuffer.size() > 0) {
                            while (this.sendBuffer.size() > 0) {
                                emitFrame(true);
                            }
                        } else {
                            this.this$0.connection.writeData(this.this$0.f84id, true, null, 0L);
                        }
                    }
                    synchronized (this.this$0) {
                        this.closed = true;
                    }
                    this.this$0.connection.flush();
                    this.this$0.cancelStreamIfNecessary();
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            synchronized (this.this$0) {
                try {
                    this.this$0.checkOutNotClosed();
                } catch (Throwable th) {
                    throw th;
                }
            }
            while (this.sendBuffer.size() > 0) {
                emitFrame(false);
                this.this$0.connection.flush();
            }
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return this.this$0.writeTimeout;
        }

        @Override // okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            this.sendBuffer.write(buffer, j);
            while (this.sendBuffer.size() >= EMIT_BUFFER_SIZE) {
                emitFrame(false);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Stream$FramingSource.class */
    private final class FramingSource implements Source {
        static final boolean $assertionsDisabled = false;
        boolean closed;
        boolean finished;
        private final long maxByteCount;
        final Http2Stream this$0;
        private final Buffer receiveBuffer = new Buffer();
        private final Buffer readBuffer = new Buffer();

        FramingSource(Http2Stream http2Stream, long j) {
            this.this$0 = http2Stream;
            this.maxByteCount = j;
        }

        private void checkNotClosed() throws IOException {
            if (this.closed) {
                throw new IOException("stream closed");
            }
            if (this.this$0.errorCode != null) {
                throw new StreamResetException(this.this$0.errorCode);
            }
        }

        private void waitUntilReadable() throws IOException {
            this.this$0.readTimeout.enter();
            while (this.readBuffer.size() == 0 && !this.finished && !this.closed && this.this$0.errorCode == null) {
                try {
                    this.this$0.waitForIo();
                } finally {
                    this.this$0.readTimeout.exitAndThrowIfTimedOut();
                }
            }
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this.this$0) {
                this.closed = true;
                this.readBuffer.clear();
                this.this$0.notifyAll();
            }
            this.this$0.cancelStreamIfNecessary();
        }

        @Override // okio.Source
        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            synchronized (this.this$0) {
                waitUntilReadable();
                checkNotClosed();
                if (this.readBuffer.size() == 0) {
                    return -1L;
                }
                long j2 = this.readBuffer.read(buffer, Math.min(j, this.readBuffer.size()));
                this.this$0.unacknowledgedBytesRead += j2;
                if (this.this$0.unacknowledgedBytesRead >= this.this$0.connection.okHttpSettings.getInitialWindowSize() / 2) {
                    this.this$0.connection.writeWindowUpdateLater(this.this$0.f84id, this.this$0.unacknowledgedBytesRead);
                    this.this$0.unacknowledgedBytesRead = 0L;
                }
                synchronized (this.this$0.connection) {
                    this.this$0.connection.unacknowledgedBytesRead += j2;
                    if (this.this$0.connection.unacknowledgedBytesRead >= this.this$0.connection.okHttpSettings.getInitialWindowSize() / 2) {
                        this.this$0.connection.writeWindowUpdateLater(0, this.this$0.connection.unacknowledgedBytesRead);
                        this.this$0.connection.unacknowledgedBytesRead = 0L;
                    }
                }
                return j2;
            }
        }

        void receive(BufferedSource bufferedSource, long j) throws IOException {
            boolean z;
            boolean z2;
            while (j > 0) {
                synchronized (this.this$0) {
                    z = this.finished;
                    z2 = this.readBuffer.size() + j > this.maxByteCount;
                }
                if (z2) {
                    bufferedSource.skip(j);
                    this.this$0.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (z) {
                    bufferedSource.skip(j);
                    return;
                }
                long j2 = bufferedSource.read(this.receiveBuffer, j);
                if (j2 == -1) {
                    throw new EOFException();
                }
                j -= j2;
                synchronized (this.this$0) {
                    boolean z3 = this.readBuffer.size() == 0;
                    this.readBuffer.writeAll(this.receiveBuffer);
                    if (z3) {
                        this.this$0.notifyAll();
                    }
                }
            }
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.this$0.readTimeout;
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Stream$StreamTimeout.class */
    class StreamTimeout extends AsyncTimeout {
        final Http2Stream this$0;

        StreamTimeout(Http2Stream http2Stream) {
            this.this$0 = http2Stream;
        }

        public void exitAndThrowIfTimedOut() throws IOException {
            if (exit()) {
                throw newTimeoutException(null);
            }
        }

        @Override // okio.AsyncTimeout
        protected IOException newTimeoutException(IOException iOException) {
            SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        @Override // okio.AsyncTimeout
        protected void timedOut() {
            this.this$0.closeLater(ErrorCode.CANCEL);
        }
    }

    Http2Stream(int i, Http2Connection http2Connection, boolean z, boolean z2, List<Header> list) {
        if (http2Connection == null) {
            throw new NullPointerException("connection == null");
        }
        if (list == null) {
            throw new NullPointerException("requestHeaders == null");
        }
        this.f84id = i;
        this.connection = http2Connection;
        this.bytesLeftInWriteWindow = http2Connection.peerSettings.getInitialWindowSize();
        this.source = new FramingSource(this, http2Connection.okHttpSettings.getInitialWindowSize());
        this.sink = new FramingSink(this);
        this.source.finished = z2;
        this.sink.finished = z;
        this.requestHeaders = list;
    }

    private boolean closeInternal(ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
            this.errorCode = errorCode;
            notifyAll();
            this.connection.removeStream(this.f84id);
            return true;
        }
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void cancelStreamIfNecessary() throws IOException {
        boolean z;
        boolean zIsOpen;
        synchronized (this) {
            z = !this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed);
            zIsOpen = isOpen();
        }
        if (z) {
            close(ErrorCode.CANCEL);
        } else {
            if (zIsOpen) {
                return;
            }
            this.connection.removeStream(this.f84id);
        }
    }

    void checkOutNotClosed() throws IOException {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        }
        if (this.sink.finished) {
            throw new IOException("stream finished");
        }
        if (this.errorCode != null) {
            throw new StreamResetException(this.errorCode);
        }
    }

    public void close(ErrorCode errorCode) throws IOException {
        if (closeInternal(errorCode)) {
            this.connection.writeSynReset(this.f84id, errorCode);
        }
    }

    public void closeLater(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynResetLater(this.f84id, errorCode);
        }
    }

    public Http2Connection getConnection() {
        return this.connection;
    }

    public ErrorCode getErrorCode() {
        ErrorCode errorCode;
        synchronized (this) {
            errorCode = this.errorCode;
        }
        return errorCode;
    }

    public int getId() {
        return this.f84id;
    }

    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }

    public Sink getSink() {
        synchronized (this) {
            if (!this.hasResponseHeaders && !isLocallyInitiated()) {
                throw new IllegalStateException("reply before requesting the sink");
            }
        }
        return this.sink;
    }

    public Source getSource() {
        return this.source;
    }

    public boolean isLocallyInitiated() {
        return this.connection.client == ((this.f84id & 1) == 1);
    }

    public boolean isOpen() {
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if ((this.source.finished || this.source.closed) && (this.sink.finished || this.sink.closed)) {
                if (this.hasResponseHeaders) {
                    return false;
                }
            }
            return true;
        }
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    void receiveData(BufferedSource bufferedSource, int i) throws IOException {
        this.source.receive(bufferedSource, i);
    }

    void receiveFin() {
        boolean zIsOpen;
        synchronized (this) {
            this.source.finished = true;
            zIsOpen = isOpen();
            notifyAll();
        }
        if (zIsOpen) {
            return;
        }
        this.connection.removeStream(this.f84id);
    }

    void receiveHeaders(List<Header> list) {
        boolean zIsOpen;
        synchronized (this) {
            zIsOpen = true;
            this.hasResponseHeaders = true;
            if (this.responseHeaders == null) {
                this.responseHeaders = list;
                zIsOpen = isOpen();
                notifyAll();
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.responseHeaders);
                arrayList.add(null);
                arrayList.addAll(list);
                this.responseHeaders = arrayList;
            }
        }
        if (zIsOpen) {
            return;
        }
        this.connection.removeStream(this.f84id);
    }

    void receiveRstStream(ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode == null) {
                this.errorCode = errorCode;
                notifyAll();
            }
        }
    }

    public void sendResponseHeaders(List<Header> list, boolean z) throws IOException {
        if (list == null) {
            throw new NullPointerException("responseHeaders == null");
        }
        boolean z2 = false;
        synchronized (this) {
            this.hasResponseHeaders = true;
            if (!z) {
                this.sink.finished = true;
                z2 = true;
            }
        }
        this.connection.writeSynReply(this.f84id, z2, list);
        if (z2) {
            this.connection.flush();
        }
    }

    public List<Header> takeResponseHeaders() throws IOException {
        List<Header> list;
        synchronized (this) {
            if (!isLocallyInitiated()) {
                throw new IllegalStateException("servers cannot read response headers");
            }
            this.readTimeout.enter();
            while (this.responseHeaders == null && this.errorCode == null) {
                try {
                    waitForIo();
                } catch (Throwable th) {
                    this.readTimeout.exitAndThrowIfTimedOut();
                    throw th;
                }
            }
            this.readTimeout.exitAndThrowIfTimedOut();
            list = this.responseHeaders;
            if (list == null) {
                throw new StreamResetException(this.errorCode);
            }
            this.responseHeaders = null;
        }
        return list;
    }

    void waitForIo() throws InterruptedException, InterruptedIOException {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }
}
