package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/* loaded from: classes-dex2jar.jar:okio/AsyncTimeout.class */
public class AsyncTimeout extends Timeout {
    private static final long IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60);
    private static final long IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
    private static final int TIMEOUT_WRITE_SIZE = 65536;

    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;

    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;

    /* loaded from: classes-dex2jar.jar:okio/AsyncTimeout$Watchdog.class */
    private static final class Watchdog extends Thread {
        Watchdog() {
            super("Okio Watchdog");
            setDaemon(true);
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0024, code lost:
        
            r0.timedOut();
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r3 = this;
            L0:
                java.lang.Class<okio.AsyncTimeout> r0 = okio.AsyncTimeout.class
                monitor-enter(r0)     // Catch: java.lang.InterruptedException -> L36
                okio.AsyncTimeout r0 = okio.AsyncTimeout.awaitTimeout()     // Catch: java.lang.Throwable -> L2a java.lang.InterruptedException -> L36
                r4 = r0
                r0 = r4
                if (r0 != 0) goto L11
                java.lang.Class<okio.AsyncTimeout> r0 = okio.AsyncTimeout.class
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2a
                goto L0
            L11:
                r0 = r4
                okio.AsyncTimeout r1 = okio.AsyncTimeout.head     // Catch: java.lang.Throwable -> L2a
                if (r0 != r1) goto L20
                r0 = 0
                okio.AsyncTimeout.head = r0     // Catch: java.lang.Throwable -> L2a
                java.lang.Class<okio.AsyncTimeout> r0 = okio.AsyncTimeout.class
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2a
                return
            L20:
                java.lang.Class<okio.AsyncTimeout> r0 = okio.AsyncTimeout.class
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2a
                r0 = r4
                r0.timedOut()     // Catch: java.lang.InterruptedException -> L36
                goto L0
            L2a:
                r4 = move-exception
                java.lang.Class<okio.AsyncTimeout> r0 = okio.AsyncTimeout.class
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2a
                goto L33
            L31:
                r0 = r4
                throw r0
            L33:
                goto L31
            L36:
                r4 = move-exception
                goto L0
            */
            throw new UnsupportedOperationException("Method not decompiled: okio.AsyncTimeout.Watchdog.run():void");
        }
    }

    @Nullable
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        AsyncTimeout asyncTimeout = head.next;
        if (asyncTimeout == null) {
            long jNanoTime = System.nanoTime();
            AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
            AsyncTimeout asyncTimeout2 = null;
            if (head.next == null) {
                asyncTimeout2 = null;
                if (System.nanoTime() - jNanoTime >= IDLE_TIMEOUT_NANOS) {
                    asyncTimeout2 = head;
                }
            }
            return asyncTimeout2;
        }
        long jRemainingNanos = asyncTimeout.remainingNanos(System.nanoTime());
        if (jRemainingNanos > 0) {
            long j = jRemainingNanos / 1000000;
            AsyncTimeout.class.wait(j, (int) (jRemainingNanos - (1000000 * j)));
            return null;
        }
        head.next = asyncTimeout.next;
        asyncTimeout.next = null;
        return asyncTimeout;
    }

    private static boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            try {
                for (AsyncTimeout asyncTimeout2 = head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                    if (asyncTimeout2.next == asyncTimeout) {
                        asyncTimeout2.next = asyncTimeout.next;
                        asyncTimeout.next = null;
                        return false;
                    }
                }
                return true;
            } finally {
            }
        }
    }

    private long remainingNanos(long j) {
        return this.timeoutAt - j;
    }

    private static void scheduleTimeout(AsyncTimeout asyncTimeout, long j, boolean z) {
        synchronized (AsyncTimeout.class) {
            try {
                if (head == null) {
                    head = new AsyncTimeout();
                    new Watchdog().start();
                }
                long jNanoTime = System.nanoTime();
                if (j != 0 && z) {
                    asyncTimeout.timeoutAt = Math.min(j, asyncTimeout.deadlineNanoTime() - jNanoTime) + jNanoTime;
                } else if (j != 0) {
                    asyncTimeout.timeoutAt = j + jNanoTime;
                } else {
                    if (!z) {
                        throw new AssertionError();
                    }
                    asyncTimeout.timeoutAt = asyncTimeout.deadlineNanoTime();
                }
                long jRemainingNanos = asyncTimeout.remainingNanos(jNanoTime);
                AsyncTimeout asyncTimeout2 = head;
                while (asyncTimeout2.next != null && jRemainingNanos >= asyncTimeout2.next.remainingNanos(jNanoTime)) {
                    asyncTimeout2 = asyncTimeout2.next;
                }
                asyncTimeout.next = asyncTimeout2.next;
                asyncTimeout2.next = asyncTimeout;
                if (asyncTimeout2 == head) {
                    AsyncTimeout.class.notify();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        long jTimeoutNanos = timeoutNanos();
        boolean zHasDeadline = hasDeadline();
        if (jTimeoutNanos != 0 || zHasDeadline) {
            this.inQueue = true;
            scheduleTimeout(this, jTimeoutNanos, zHasDeadline);
        }
    }

    final IOException exit(IOException iOException) throws IOException {
        return !exit() ? iOException : newTimeoutException(iOException);
    }

    final void exit(boolean z) throws IOException {
        if (exit() && z) {
            throw newTimeoutException(null);
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }

    protected IOException newTimeoutException(@Nullable IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final Sink sink(Sink sink) {
        return new Sink(this, sink) { // from class: okio.AsyncTimeout.1
            final AsyncTimeout this$0;
            final Sink val$sink;

            {
                this.this$0 = this;
                this.val$sink = sink;
            }

            @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                this.this$0.enter();
                try {
                    try {
                        this.val$sink.close();
                        this.this$0.exit(true);
                    } catch (IOException e) {
                        throw this.this$0.exit(e);
                    }
                } catch (Throwable th) {
                    this.this$0.exit(false);
                    throw th;
                }
            }

            @Override // okio.Sink, java.io.Flushable
            public void flush() throws IOException {
                this.this$0.enter();
                try {
                    try {
                        this.val$sink.flush();
                        this.this$0.exit(true);
                    } catch (IOException e) {
                        throw this.this$0.exit(e);
                    }
                } catch (Throwable th) {
                    this.this$0.exit(false);
                    throw th;
                }
            }

            @Override // okio.Sink
            public Timeout timeout() {
                return this.this$0;
            }

            public String toString() {
                return "AsyncTimeout.sink(" + this.val$sink + ")";
            }

            @Override // okio.Sink
            public void write(Buffer buffer, long j) throws IOException {
                long j2;
                Util.checkOffsetAndCount(buffer.size, 0L, j);
                while (true) {
                    long j3 = 0;
                    if (j <= 0) {
                        return;
                    }
                    Segment segment = buffer.head;
                    while (true) {
                        Segment segment2 = segment;
                        j2 = j3;
                        if (j3 >= 65536) {
                            break;
                        }
                        j3 += segment2.limit - segment2.pos;
                        if (j3 >= j) {
                            j2 = j;
                            break;
                        }
                        segment = segment2.next;
                    }
                    this.this$0.enter();
                    try {
                        try {
                            this.val$sink.write(buffer, j2);
                            j -= j2;
                            this.this$0.exit(true);
                        } catch (IOException e) {
                            throw this.this$0.exit(e);
                        }
                    } catch (Throwable th) {
                        this.this$0.exit(false);
                        throw th;
                    }
                }
            }
        };
    }

    public final Source source(Source source) {
        return new Source(this, source) { // from class: okio.AsyncTimeout.2
            final AsyncTimeout this$0;
            final Source val$source;

            {
                this.this$0 = this;
                this.val$source = source;
            }

            @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                try {
                    try {
                        this.val$source.close();
                        this.this$0.exit(true);
                    } catch (IOException e) {
                        throw this.this$0.exit(e);
                    }
                } catch (Throwable th) {
                    this.this$0.exit(false);
                    throw th;
                }
            }

            @Override // okio.Source
            public long read(Buffer buffer, long j) throws IOException {
                this.this$0.enter();
                try {
                    try {
                        long j2 = this.val$source.read(buffer, j);
                        this.this$0.exit(true);
                        return j2;
                    } catch (IOException e) {
                        throw this.this$0.exit(e);
                    }
                } catch (Throwable th) {
                    this.this$0.exit(false);
                    throw th;
                }
            }

            @Override // okio.Source
            public Timeout timeout() {
                return this.this$0;
            }

            public String toString() {
                return "AsyncTimeout.source(" + this.val$source + ")";
            }
        };
    }

    protected void timedOut() {
    }
}
