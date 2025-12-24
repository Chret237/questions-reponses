package okhttp3.internal.http2;

import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.ActivityChooserView;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Connection.class */
public final class Http2Connection implements Closeable {
    static final boolean $assertionsDisabled = false;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    private static final ExecutorService listenerExecutor = new ThreadPoolExecutor(0, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    private boolean awaitingPong;
    long bytesLeftInWriteWindow;
    final boolean client;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    int nextStreamId;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean shutdown;
    final Socket socket;
    final Http2Writer writer;
    private final ScheduledExecutorService writerExecutor;
    final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    Settings okHttpSettings = new Settings();
    final Settings peerSettings = new Settings();
    boolean receivedInitialPeerSettings = false;
    final Set<Integer> currentPushRequests = new LinkedHashSet();

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Connection$Builder.class */
    public static class Builder {
        boolean client;
        String hostname;
        int pingIntervalMillis;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;
        Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        PushObserver pushObserver = PushObserver.CANCEL;

        public Builder(boolean z) {
            this.client = z;
        }

        public Http2Connection build() {
            return new Http2Connection(this);
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder pingIntervalMillis(int i) {
            this.pingIntervalMillis = i;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }

        public Builder socket(Socket socket) throws IOException {
            return socket(socket, ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }

        public Builder socket(Socket socket, String str, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.socket = socket;
            this.hostname = str;
            this.source = bufferedSource;
            this.sink = bufferedSink;
            return this;
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Connection$Listener.class */
    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() { // from class: okhttp3.internal.http2.Http2Connection.Listener.1
            @Override // okhttp3.internal.http2.Http2Connection.Listener
            public void onStream(Http2Stream http2Stream) throws IOException {
                http2Stream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public void onSettings(Http2Connection http2Connection) {
        }

        public abstract void onStream(Http2Stream http2Stream) throws IOException;
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Connection$PingRunnable.class */
    final class PingRunnable extends NamedRunnable {
        final int payload1;
        final int payload2;
        final boolean reply;
        final Http2Connection this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        PingRunnable(Http2Connection http2Connection, boolean z, int i, int i2) {
            super("OkHttp %s ping %08x%08x", http2Connection.hostname, Integer.valueOf(i), Integer.valueOf(i2));
            this.this$0 = http2Connection;
            this.reply = z;
            this.payload1 = i;
            this.payload2 = i2;
        }

        @Override // okhttp3.internal.NamedRunnable
        public void execute() {
            this.this$0.writePing(this.reply, this.payload1, this.payload2);
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Connection$ReaderRunnable.class */
    class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
        final Http2Reader reader;
        final Http2Connection this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ReaderRunnable(Http2Connection http2Connection, Http2Reader http2Reader) {
            super("OkHttp %s", http2Connection.hostname);
            this.this$0 = http2Connection;
            this.reader = http2Reader;
        }

        private void applyAndAckSettings(Settings settings) {
            try {
                this.this$0.writerExecutor.execute(new NamedRunnable(this, "OkHttp %s ACK Settings", new Object[]{this.this$0.hostname}, settings) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.3
                    final ReaderRunnable this$1;
                    final Settings val$peerSettings;

                    {
                        this.this$1 = this;
                        this.val$peerSettings = settings;
                    }

                    @Override // okhttp3.internal.NamedRunnable
                    public void execute() {
                        try {
                            this.this$1.this$0.writer.applyAndAckSettings(this.val$peerSettings);
                        } catch (IOException e) {
                            this.this$1.this$0.failConnection();
                        }
                    }
                });
            } catch (RejectedExecutionException e) {
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ackSettings() {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void alternateService(int i, String str, ByteString byteString, String str2, int i2, long j) {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void data(boolean z, int i, BufferedSource bufferedSource, int i2) throws IOException {
            if (this.this$0.pushedStream(i)) {
                this.this$0.pushDataLater(i, bufferedSource, i2, z);
                return;
            }
            Http2Stream stream = this.this$0.getStream(i);
            if (stream == null) {
                this.this$0.writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                bufferedSource.skip(i2);
            } else {
                stream.receiveData(bufferedSource, i2);
                if (z) {
                    stream.receiveFin();
                }
            }
        }

        @Override // okhttp3.internal.NamedRunnable
        protected void execute() throws IOException {
            ErrorCode errorCode;
            ErrorCode errorCode2;
            Http2Connection http2Connection;
            ErrorCode errorCode3 = ErrorCode.INTERNAL_ERROR;
            ErrorCode errorCode4 = ErrorCode.INTERNAL_ERROR;
            ErrorCode errorCode5 = errorCode3;
            ErrorCode errorCode6 = errorCode3;
            try {
                try {
                    try {
                        this.reader.readConnectionPreface(this);
                        do {
                        } while (this.reader.nextFrame(false, this));
                        ErrorCode errorCode7 = ErrorCode.NO_ERROR;
                        errorCode6 = errorCode7;
                        ErrorCode errorCode8 = ErrorCode.CANCEL;
                        http2Connection = this.this$0;
                        errorCode = errorCode7;
                        errorCode2 = errorCode8;
                    } catch (IOException e) {
                        errorCode = ErrorCode.PROTOCOL_ERROR;
                        errorCode5 = errorCode;
                        errorCode2 = ErrorCode.PROTOCOL_ERROR;
                        http2Connection = this.this$0;
                    }
                    http2Connection.close(errorCode, errorCode2);
                } catch (IOException e2) {
                }
                Util.closeQuietly(this.reader);
            } catch (Throwable th) {
                try {
                    this.this$0.close(errorCode5, errorCode4);
                } catch (IOException e3) {
                }
                Util.closeQuietly(this.reader);
                throw th;
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void goAway(int i, ErrorCode errorCode, ByteString byteString) {
            Http2Stream[] http2StreamArr;
            byteString.size();
            synchronized (this.this$0) {
                try {
                    http2StreamArr = (Http2Stream[]) this.this$0.streams.values().toArray(new Http2Stream[this.this$0.streams.size()]);
                    this.this$0.shutdown = true;
                } catch (Throwable th) {
                    throw th;
                }
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                if (http2Stream.getId() > i && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    this.this$0.removeStream(http2Stream.getId());
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void headers(boolean z, int i, int i2, List<Header> list) {
            if (this.this$0.pushedStream(i)) {
                this.this$0.pushHeadersLater(i, list, z);
                return;
            }
            synchronized (this.this$0) {
                Http2Stream stream = this.this$0.getStream(i);
                if (stream != null) {
                    stream.receiveHeaders(list);
                    if (z) {
                        stream.receiveFin();
                        return;
                    }
                    return;
                }
                if (this.this$0.shutdown) {
                    return;
                }
                if (i <= this.this$0.lastGoodStreamId) {
                    return;
                }
                if (i % 2 == this.this$0.nextStreamId % 2) {
                    return;
                }
                Http2Stream http2Stream = new Http2Stream(i, this.this$0, false, z, list);
                this.this$0.lastGoodStreamId = i;
                this.this$0.streams.put(Integer.valueOf(i), http2Stream);
                Http2Connection.listenerExecutor.execute(new NamedRunnable(this, "OkHttp %s stream %d", new Object[]{this.this$0.hostname, Integer.valueOf(i)}, http2Stream) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.1
                    final ReaderRunnable this$1;
                    final Http2Stream val$newStream;

                    {
                        this.this$1 = this;
                        this.val$newStream = http2Stream;
                    }

                    @Override // okhttp3.internal.NamedRunnable
                    public void execute() {
                        try {
                            this.this$1.this$0.listener.onStream(this.val$newStream);
                        } catch (IOException e) {
                            Platform.get().log(4, "Http2Connection.Listener failure for " + this.this$1.this$0.hostname, e);
                            try {
                                this.val$newStream.close(ErrorCode.PROTOCOL_ERROR);
                            } catch (IOException e2) {
                            }
                        }
                    }
                });
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ping(boolean z, int i, int i2) {
            if (!z) {
                try {
                    this.this$0.writerExecutor.execute(new PingRunnable(this.this$0, true, i, i2));
                } catch (RejectedExecutionException e) {
                }
            } else {
                synchronized (this.this$0) {
                    this.this$0.awaitingPong = false;
                    this.this$0.notifyAll();
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void priority(int i, int i2, int i3, boolean z) {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void pushPromise(int i, int i2, List<Header> list) {
            this.this$0.pushRequestLater(i2, list);
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void rstStream(int i, ErrorCode errorCode) {
            if (this.this$0.pushedStream(i)) {
                this.this$0.pushResetLater(i, errorCode);
                return;
            }
            Http2Stream http2StreamRemoveStream = this.this$0.removeStream(i);
            if (http2StreamRemoveStream != null) {
                http2StreamRemoveStream.receiveRstStream(errorCode);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void settings(boolean z, Settings settings) {
            Http2Stream[] http2StreamArr;
            long j;
            int i;
            synchronized (this.this$0) {
                try {
                    int initialWindowSize = this.this$0.peerSettings.getInitialWindowSize();
                    if (z) {
                        this.this$0.peerSettings.clear();
                    }
                    this.this$0.peerSettings.merge(settings);
                    applyAndAckSettings(settings);
                    int initialWindowSize2 = this.this$0.peerSettings.getInitialWindowSize();
                    http2StreamArr = null;
                    if (initialWindowSize2 == -1 || initialWindowSize2 == initialWindowSize) {
                        j = 0;
                    } else {
                        long j2 = initialWindowSize2 - initialWindowSize;
                        if (!this.this$0.receivedInitialPeerSettings) {
                            this.this$0.addBytesToWriteWindow(j2);
                            this.this$0.receivedInitialPeerSettings = true;
                        }
                        j = j2;
                        if (!this.this$0.streams.isEmpty()) {
                            http2StreamArr = (Http2Stream[]) this.this$0.streams.values().toArray(new Http2Stream[this.this$0.streams.size()]);
                            j = j2;
                        }
                    }
                    Http2Connection.listenerExecutor.execute(new NamedRunnable(this, "OkHttp %s settings", this.this$0.hostname) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.2
                        final ReaderRunnable this$1;

                        {
                            this.this$1 = this;
                        }

                        @Override // okhttp3.internal.NamedRunnable
                        public void execute() {
                            this.this$1.this$0.listener.onSettings(this.this$1.this$0);
                        }
                    });
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (http2StreamArr == null || j == 0) {
                return;
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                synchronized (http2Stream) {
                    http2Stream.addBytesToWriteWindow(j);
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void windowUpdate(int i, long j) {
            if (i == 0) {
                synchronized (this.this$0) {
                    this.this$0.bytesLeftInWriteWindow += j;
                    this.this$0.notifyAll();
                }
                return;
            }
            Http2Stream stream = this.this$0.getStream(i);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(j);
                }
            }
        }
    }

    Http2Connection(Builder builder) {
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.writerExecutor = new ScheduledThreadPoolExecutor(1, Util.threadFactory(Util.format("OkHttp %s Writer", this.hostname), false));
        if (builder.pingIntervalMillis != 0) {
            this.writerExecutor.scheduleAtFixedRate(new PingRunnable(this, false, 0, 0), builder.pingIntervalMillis, builder.pingIntervalMillis, TimeUnit.MILLISECONDS);
        }
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, SupportMenu.USER_MASK);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(this, new Http2Reader(builder.source, this.client));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failConnection() {
        try {
            close(ErrorCode.PROTOCOL_ERROR, ErrorCode.PROTOCOL_ERROR);
        } catch (IOException e) {
        }
    }

    private Http2Stream newStream(int i, List<Header> list, boolean z) throws IOException {
        int i2;
        Http2Stream http2Stream;
        boolean z2;
        boolean z3 = !z;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.nextStreamId > 1073741823) {
                    shutdown(ErrorCode.REFUSED_STREAM);
                }
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                i2 = this.nextStreamId;
                this.nextStreamId += 2;
                http2Stream = new Http2Stream(i2, this, z3, false, list);
                z2 = !z || this.bytesLeftInWriteWindow == 0 || http2Stream.bytesLeftInWriteWindow == 0;
                if (http2Stream.isOpen()) {
                    this.streams.put(Integer.valueOf(i2), http2Stream);
                }
            }
            if (i == 0) {
                this.writer.synStream(z3, i2, i, list);
            } else {
                if (this.client) {
                    throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                }
                this.writer.pushPromise(i, i2, list);
            }
        }
        if (z2) {
            this.writer.flush();
        }
        return http2Stream;
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void awaitPong() throws InterruptedException, IOException {
        synchronized (this) {
            while (this.awaitingPong) {
                wait();
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:0|2|(3:55|3|4)|6|f|13|14|(8:16|(2:17|(6:19|53|20|21|58|26)(0))|27|28|49|33|35|(1:37)(2:38|39))(0)|51|27|28|49|33|35|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0093, code lost:
    
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0094, code lost:
    
        r4 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0099, code lost:
    
        if (r8 == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x009c, code lost:
    
        r4 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a8, code lost:
    
        r4 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00bf A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00c0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void close(okhttp3.internal.http2.ErrorCode r4, okhttp3.internal.http2.ErrorCode r5) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 205
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.close(okhttp3.internal.http2.ErrorCode, okhttp3.internal.http2.ErrorCode):void");
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    Http2Stream getStream(int i) {
        Http2Stream http2Stream;
        synchronized (this) {
            http2Stream = this.streams.get(Integer.valueOf(i));
        }
        return http2Stream;
    }

    public boolean isShutdown() {
        boolean z;
        synchronized (this) {
            z = this.shutdown;
        }
        return z;
    }

    public int maxConcurrentStreams() {
        int maxConcurrentStreams;
        synchronized (this) {
            maxConcurrentStreams = this.peerSettings.getMaxConcurrentStreams(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        }
        return maxConcurrentStreams;
    }

    public Http2Stream newStream(List<Header> list, boolean z) throws IOException {
        return newStream(0, list, z);
    }

    public int openStreamCount() {
        int size;
        synchronized (this) {
            size = this.streams.size();
        }
        return size;
    }

    void pushDataLater(int i, BufferedSource bufferedSource, int i2, boolean z) throws IOException {
        Buffer buffer = new Buffer();
        long j = i2;
        bufferedSource.require(j);
        bufferedSource.read(buffer, j);
        if (buffer.size() == j) {
            this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, buffer, i2, z) { // from class: okhttp3.internal.http2.Http2Connection.5
                final Http2Connection this$0;
                final Buffer val$buffer;
                final int val$byteCount;
                final boolean val$inFinished;
                final int val$streamId;

                {
                    this.this$0 = this;
                    this.val$streamId = i;
                    this.val$buffer = buffer;
                    this.val$byteCount = i2;
                    this.val$inFinished = z;
                }

                @Override // okhttp3.internal.NamedRunnable
                public void execute() {
                    try {
                        boolean zOnData = this.this$0.pushObserver.onData(this.val$streamId, this.val$buffer, this.val$byteCount, this.val$inFinished);
                        if (zOnData) {
                            this.this$0.writer.rstStream(this.val$streamId, ErrorCode.CANCEL);
                        }
                        if (zOnData || this.val$inFinished) {
                            synchronized (this.this$0) {
                                this.this$0.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                            }
                        }
                    } catch (IOException e) {
                    }
                }
            });
            return;
        }
        throw new IOException(buffer.size() + " != " + i2);
    }

    void pushHeadersLater(int i, List<Header> list, boolean z) {
        try {
            this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, list, z) { // from class: okhttp3.internal.http2.Http2Connection.4
                final Http2Connection this$0;
                final boolean val$inFinished;
                final List val$requestHeaders;
                final int val$streamId;

                {
                    this.this$0 = this;
                    this.val$streamId = i;
                    this.val$requestHeaders = list;
                    this.val$inFinished = z;
                }

                @Override // okhttp3.internal.NamedRunnable
                public void execute() {
                    boolean zOnHeaders = this.this$0.pushObserver.onHeaders(this.val$streamId, this.val$requestHeaders, this.val$inFinished);
                    if (zOnHeaders) {
                        try {
                            this.this$0.writer.rstStream(this.val$streamId, ErrorCode.CANCEL);
                        } catch (IOException e) {
                            return;
                        }
                    }
                    if (zOnHeaders || this.val$inFinished) {
                        synchronized (this.this$0) {
                            this.this$0.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }

    void pushRequestLater(int i, List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(i))) {
                writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(i));
            try {
                this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Request[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, list) { // from class: okhttp3.internal.http2.Http2Connection.3
                    final Http2Connection this$0;
                    final List val$requestHeaders;
                    final int val$streamId;

                    {
                        this.this$0 = this;
                        this.val$streamId = i;
                        this.val$requestHeaders = list;
                    }

                    @Override // okhttp3.internal.NamedRunnable
                    public void execute() {
                        if (this.this$0.pushObserver.onRequest(this.val$streamId, this.val$requestHeaders)) {
                            try {
                                this.this$0.writer.rstStream(this.val$streamId, ErrorCode.CANCEL);
                                synchronized (this.this$0) {
                                    this.this$0.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
                });
            } catch (RejectedExecutionException e) {
            }
        }
    }

    void pushResetLater(int i, ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable(this, "OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(i)}, i, errorCode) { // from class: okhttp3.internal.http2.Http2Connection.6
            final Http2Connection this$0;
            final ErrorCode val$errorCode;
            final int val$streamId;

            {
                this.this$0 = this;
                this.val$streamId = i;
                this.val$errorCode = errorCode;
            }

            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                this.this$0.pushObserver.onReset(this.val$streamId, this.val$errorCode);
                synchronized (this.this$0) {
                    this.this$0.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                }
            }
        });
    }

    public Http2Stream pushStream(int i, List<Header> list, boolean z) throws IOException {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        return newStream(i, list, z);
    }

    boolean pushedStream(int i) {
        boolean z = true;
        if (i == 0 || (i & 1) != 0) {
            z = false;
        }
        return z;
    }

    Http2Stream removeStream(int i) {
        Http2Stream http2StreamRemove;
        synchronized (this) {
            http2StreamRemove = this.streams.remove(Integer.valueOf(i));
            notifyAll();
        }
        return http2StreamRemove;
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
            }
            this.writer.settings(settings);
        }
    }

    public void shutdown(ErrorCode errorCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                this.writer.goAway(this.lastGoodStreamId, errorCode, Util.EMPTY_BYTE_ARRAY);
            }
        }
    }

    public void start() throws IOException {
        start(true);
    }

    void start(boolean z) throws IOException {
        if (z) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            if (this.okHttpSettings.getInitialWindowSize() != 65535) {
                this.writer.windowUpdate(0, r0 - SupportMenu.USER_MASK);
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public void writeData(int i, boolean z, Buffer buffer, long j) throws IOException {
        int iMin;
        long j2;
        long j3 = j;
        if (j == 0) {
            this.writer.data(z, i, buffer, 0);
            return;
        }
        while (j3 > 0) {
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(i))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                iMin = Math.min((int) Math.min(j3, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                j2 = iMin;
                this.bytesLeftInWriteWindow -= j2;
            }
            j3 -= j2;
            this.writer.data(z && j3 == 0, i, buffer, iMin);
        }
    }

    void writePing(boolean z, int i, int i2) {
        boolean z2;
        if (!z) {
            synchronized (this) {
                z2 = this.awaitingPong;
                this.awaitingPong = true;
            }
            if (z2) {
                failConnection();
                return;
            }
        }
        try {
            this.writer.ping(z, i, i2);
        } catch (IOException e) {
            failConnection();
        }
    }

    void writePingAndAwaitPong() throws InterruptedException, IOException {
        writePing(false, 1330343787, -257978967);
        awaitPong();
    }

    void writeSynReply(int i, boolean z, List<Header> list) throws IOException {
        this.writer.synReply(z, i, list);
    }

    void writeSynReset(int i, ErrorCode errorCode) throws IOException {
        this.writer.rstStream(i, errorCode);
    }

    void writeSynResetLater(int i, ErrorCode errorCode) {
        try {
            this.writerExecutor.execute(new NamedRunnable(this, "OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}, i, errorCode) { // from class: okhttp3.internal.http2.Http2Connection.1
                final Http2Connection this$0;
                final ErrorCode val$errorCode;
                final int val$streamId;

                {
                    this.this$0 = this;
                    this.val$streamId = i;
                    this.val$errorCode = errorCode;
                }

                @Override // okhttp3.internal.NamedRunnable
                public void execute() {
                    try {
                        this.this$0.writeSynReset(this.val$streamId, this.val$errorCode);
                    } catch (IOException e) {
                        this.this$0.failConnection();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }

    void writeWindowUpdateLater(int i, long j) {
        try {
            this.writerExecutor.execute(new NamedRunnable(this, "OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}, i, j) { // from class: okhttp3.internal.http2.Http2Connection.2
                final Http2Connection this$0;
                final int val$streamId;
                final long val$unacknowledgedBytesRead;

                {
                    this.this$0 = this;
                    this.val$streamId = i;
                    this.val$unacknowledgedBytesRead = j;
                }

                @Override // okhttp3.internal.NamedRunnable
                public void execute() {
                    try {
                        this.this$0.writer.windowUpdate(this.val$streamId, this.val$unacknowledgedBytesRead);
                    } catch (IOException e) {
                        this.this$0.failConnection();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
        }
    }
}
