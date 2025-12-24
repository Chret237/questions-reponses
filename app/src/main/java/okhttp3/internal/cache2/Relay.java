package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/cache2/Relay.class */
final class Relay {
    private static final long FILE_HEADER_SIZE = 32;
    static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
    static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    final long bufferMaxSize;
    boolean complete;
    RandomAccessFile file;
    private final ByteString metadata;
    int sourceCount;
    Source upstream;
    long upstreamPos;
    Thread upstreamReader;
    final Buffer upstreamBuffer = new Buffer();
    final Buffer buffer = new Buffer();

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/cache2/Relay$RelaySource.class */
    class RelaySource implements Source {
        private FileOperator fileOperator;
        private long sourcePos;
        final Relay this$0;
        private final Timeout timeout = new Timeout();

        RelaySource(Relay relay) {
            this.this$0 = relay;
            this.fileOperator = new FileOperator(this.this$0.file.getChannel());
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.fileOperator == null) {
                return;
            }
            RandomAccessFile randomAccessFile = null;
            this.fileOperator = null;
            synchronized (this.this$0) {
                this.this$0.sourceCount--;
                if (this.this$0.sourceCount == 0) {
                    randomAccessFile = this.this$0.file;
                    this.this$0.file = null;
                }
            }
            if (randomAccessFile != null) {
                Util.closeQuietly(randomAccessFile);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x0085, code lost:
        
            if (r11 != 2) goto L101;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x0088, code lost:
        
            r0 = java.lang.Math.min(r9, r0 - r7.sourcePos);
            r7.fileOperator.read(r7.sourcePos + okhttp3.internal.cache2.Relay.FILE_HEADER_SIZE, r8, r0);
            r7.sourcePos += r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b0, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00b1, code lost:
        
            r0 = r7.this$0.upstream.read(r7.this$0.upstreamBuffer, r7.this$0.bufferMaxSize);
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00d3, code lost:
        
            if (r0 != (-1)) goto L48;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00d6, code lost:
        
            r7.this$0.commit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00e0, code lost:
        
            r0 = r7.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00e7, code lost:
        
            monitor-enter(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00e8, code lost:
        
            r7.this$0.upstreamReader = null;
            r7.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00f9, code lost:
        
            monitor-exit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x00fa, code lost:
        
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x0104, code lost:
        
            r0 = java.lang.Math.min(r0, r9);
            r7.this$0.upstreamBuffer.copyTo(r8, 0, r0);
            r7.sourcePos += r0;
            r7.fileOperator.write(r0 + okhttp3.internal.cache2.Relay.FILE_HEADER_SIZE, r7.this$0.upstreamBuffer.clone(), r0);
            r0 = r7.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0142, code lost:
        
            monitor-enter(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x0144, code lost:
        
            r7.this$0.buffer.write(r7.this$0.upstreamBuffer, r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0168, code lost:
        
            if (r7.this$0.buffer.size() <= r7.this$0.bufferMaxSize) goto L54;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x016b, code lost:
        
            r7.this$0.buffer.skip(r7.this$0.buffer.size() - r7.this$0.bufferMaxSize);
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x0188, code lost:
        
            r7.this$0.upstreamPos += r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x019b, code lost:
        
            monitor-exit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x019d, code lost:
        
            r0 = r7.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:59:0x01a2, code lost:
        
            monitor-enter(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x01a3, code lost:
        
            r7.this$0.upstreamReader = null;
            r7.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:61:0x01b3, code lost:
        
            monitor-exit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x01b5, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:74:0x01c4, code lost:
        
            r16 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:76:0x01cc, code lost:
        
            monitor-enter(r7.this$0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x01cd, code lost:
        
            r7.this$0.upstreamReader = null;
            r7.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:80:0x01e0, code lost:
        
            throw r16;
         */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // okio.Source
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public long read(okio.Buffer r8, long r9) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 559
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.Relay.RelaySource.read(okio.Buffer, long):long");
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.timeout;
        }
    }

    private Relay(RandomAccessFile randomAccessFile, Source source, long j, ByteString byteString, long j2) {
        this.file = randomAccessFile;
        this.upstream = source;
        this.complete = source == null;
        this.upstreamPos = j;
        this.metadata = byteString;
        this.bufferMaxSize = j2;
    }

    public static Relay edit(File file, Source source, ByteString byteString, long j) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        Relay relay = new Relay(randomAccessFile, source, 0L, byteString, j);
        randomAccessFile.setLength(0L);
        relay.writeHeader(PREFIX_DIRTY, -1L, -1L);
        return relay;
    }

    public static Relay read(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
        Buffer buffer = new Buffer();
        fileOperator.read(0L, buffer, FILE_HEADER_SIZE);
        if (!buffer.readByteString(PREFIX_CLEAN.size()).equals(PREFIX_CLEAN)) {
            throw new IOException("unreadable cache file");
        }
        long j = buffer.readLong();
        long j2 = buffer.readLong();
        Buffer buffer2 = new Buffer();
        fileOperator.read(j + FILE_HEADER_SIZE, buffer2, j2);
        return new Relay(randomAccessFile, null, j, buffer2.readByteString(), 0L);
    }

    private void writeHeader(ByteString byteString, long j, long j2) throws IOException {
        Buffer buffer = new Buffer();
        buffer.write(byteString);
        buffer.writeLong(j);
        buffer.writeLong(j2);
        if (buffer.size() != FILE_HEADER_SIZE) {
            throw new IllegalArgumentException();
        }
        new FileOperator(this.file.getChannel()).write(0L, buffer, FILE_HEADER_SIZE);
    }

    private void writeMetadata(long j) throws IOException {
        Buffer buffer = new Buffer();
        buffer.write(this.metadata);
        new FileOperator(this.file.getChannel()).write(FILE_HEADER_SIZE + j, buffer, this.metadata.size());
    }

    void commit(long j) throws IOException {
        writeMetadata(j);
        this.file.getChannel().force(false);
        writeHeader(PREFIX_CLEAN, j, this.metadata.size());
        this.file.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
        }
        Util.closeQuietly(this.upstream);
        this.upstream = null;
    }

    boolean isClosed() {
        return this.file == null;
    }

    public ByteString metadata() {
        return this.metadata;
    }

    public Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            this.sourceCount++;
            return new RelaySource(this);
        }
    }
}
