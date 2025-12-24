package okhttp3.internal.http2;

import android.support.v7.widget.ActivityChooserView;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Hpack;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Reader.class */
final class Http2Reader implements Closeable {
    static final Logger logger = Logger.getLogger(Http2.class.getName());
    private final boolean client;
    private final ContinuationSource continuation;
    final Hpack.Reader hpackReader;
    private final BufferedSource source;

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Reader$ContinuationSource.class */
    static final class ContinuationSource implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        ContinuationSource(BufferedSource bufferedSource) {
            this.source = bufferedSource;
        }

        private void readContinuationHeader() throws IOException {
            int i = this.streamId;
            int medium = Http2Reader.readMedium(this.source);
            this.left = medium;
            this.length = medium;
            byte b = (byte) (this.source.readByte() & 255);
            this.flags = (byte) (this.source.readByte() & 255);
            if (Http2Reader.logger.isLoggable(Level.FINE)) {
                Http2Reader.logger.fine(Http2.frameLog(true, this.streamId, this.length, b, this.flags));
            }
            int i2 = this.source.readInt() & ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            this.streamId = i2;
            if (b != 9) {
                throw Http2.ioException("%s != TYPE_CONTINUATION", Byte.valueOf(b));
            }
            if (i2 != i) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        @Override // okio.Source
        public long read(Buffer buffer, long j) throws IOException {
            while (true) {
                int i = this.left;
                if (i != 0) {
                    long j2 = this.source.read(buffer, Math.min(j, i));
                    if (j2 == -1) {
                        return -1L;
                    }
                    this.left = (int) (this.left - j2);
                    return j2;
                }
                this.source.skip(this.padding);
                this.padding = (short) 0;
                if ((this.flags & 4) != 0) {
                    return -1L;
                }
                readContinuationHeader();
            }
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.source.timeout();
        }
    }

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Reader$Handler.class */
    interface Handler {
        void ackSettings();

        void alternateService(int i, String str, ByteString byteString, String str2, int i2, long j);

        void data(boolean z, int i, BufferedSource bufferedSource, int i2) throws IOException;

        void goAway(int i, ErrorCode errorCode, ByteString byteString);

        void headers(boolean z, int i, int i2, List<Header> list);

        void ping(boolean z, int i, int i2);

        void priority(int i, int i2, int i3, boolean z);

        void pushPromise(int i, int i2, List<Header> list) throws IOException;

        void rstStream(int i, ErrorCode errorCode);

        void settings(boolean z, Settings settings);

        void windowUpdate(int i, long j);
    }

    Http2Reader(BufferedSource bufferedSource, boolean z) {
        this.source = bufferedSource;
        this.client = z;
        ContinuationSource continuationSource = new ContinuationSource(bufferedSource);
        this.continuation = continuationSource;
        this.hpackReader = new Hpack.Reader(4096, continuationSource);
    }

    static int lengthWithoutPadding(int i, byte b, short s) throws IOException {
        int i2 = i;
        if ((b & 8) != 0) {
            i2 = i - 1;
        }
        if (s <= i2) {
            return (short) (i2 - s);
        }
        throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", Short.valueOf(s), Integer.valueOf(i2));
    }

    private void readData(Handler handler, int i, byte b, int i2) throws IOException {
        short s = 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_DATA streamId == 0", new Object[0]);
        }
        boolean z = true;
        boolean z2 = (b & 1) != 0;
        if ((b & 32) == 0) {
            z = false;
        }
        if (z) {
            throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
        }
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        handler.data(z2, i2, this.source, lengthWithoutPadding(i, b, s));
        this.source.skip(s);
    }

    private void readGoAway(Handler handler, int i, byte b, int i2) throws IOException {
        if (i < 8) {
            throw Http2.ioException("TYPE_GOAWAY length < 8: %s", Integer.valueOf(i));
        }
        if (i2 != 0) {
            throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
        }
        int i3 = this.source.readInt();
        int i4 = this.source.readInt();
        int i5 = i - 8;
        ErrorCode errorCodeFromHttp2 = ErrorCode.fromHttp2(i4);
        if (errorCodeFromHttp2 == null) {
            throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", Integer.valueOf(i4));
        }
        ByteString byteString = ByteString.EMPTY;
        if (i5 > 0) {
            byteString = this.source.readByteString(i5);
        }
        handler.goAway(i3, errorCodeFromHttp2, byteString);
    }

    private List<Header> readHeaderBlock(int i, short s, byte b, int i2) throws IOException {
        ContinuationSource continuationSource = this.continuation;
        continuationSource.left = i;
        continuationSource.length = i;
        this.continuation.padding = s;
        this.continuation.flags = b;
        this.continuation.streamId = i2;
        this.hpackReader.readHeaders();
        return this.hpackReader.getAndResetHeaderList();
    }

    private void readHeaders(Handler handler, int i, byte b, int i2) throws IOException {
        short s = 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
        }
        boolean z = (b & 1) != 0;
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        int i3 = i;
        if ((b & 32) != 0) {
            readPriority(handler, i2);
            i3 = i - 5;
        }
        handler.headers(z, i2, -1, readHeaderBlock(lengthWithoutPadding(i3, b, s), s, b, i2));
    }

    static int readMedium(BufferedSource bufferedSource) throws IOException {
        return (bufferedSource.readByte() & 255) | ((bufferedSource.readByte() & 255) << 16) | ((bufferedSource.readByte() & 255) << 8);
    }

    private void readPing(Handler handler, int i, byte b, int i2) throws IOException {
        boolean z = false;
        if (i != 8) {
            throw Http2.ioException("TYPE_PING length != 8: %s", Integer.valueOf(i));
        }
        if (i2 != 0) {
            throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
        }
        int i3 = this.source.readInt();
        int i4 = this.source.readInt();
        if ((b & 1) != 0) {
            z = true;
        }
        handler.ping(z, i3, i4);
    }

    private void readPriority(Handler handler, int i) throws IOException {
        int i2 = this.source.readInt();
        handler.priority(i, i2 & ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, (this.source.readByte() & 255) + 1, (Integer.MIN_VALUE & i2) != 0);
    }

    private void readPriority(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 5) {
            throw Http2.ioException("TYPE_PRIORITY length: %d != 5", Integer.valueOf(i));
        }
        if (i2 == 0) {
            throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
        }
        readPriority(handler, i2);
    }

    private void readPushPromise(Handler handler, int i, byte b, int i2) throws IOException {
        short s = 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
        }
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        handler.pushPromise(i2, this.source.readInt() & ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, readHeaderBlock(lengthWithoutPadding(i - 4, b, s), s, b, i2));
    }

    private void readRstStream(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", Integer.valueOf(i));
        }
        if (i2 == 0) {
            throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
        }
        int i3 = this.source.readInt();
        ErrorCode errorCodeFromHttp2 = ErrorCode.fromHttp2(i3);
        if (errorCodeFromHttp2 == null) {
            throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", Integer.valueOf(i3));
        }
        handler.rstStream(i2, errorCodeFromHttp2);
    }

    private void readSettings(Handler handler, int i, byte b, int i2) throws IOException {
        int i3;
        if (i2 != 0) {
            throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
        }
        if ((b & 1) != 0) {
            if (i != 0) {
                throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
            }
            handler.ackSettings();
            return;
        }
        if (i % 6 != 0) {
            throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", Integer.valueOf(i));
        }
        Settings settings = new Settings();
        for (int i4 = 0; i4 < i; i4 += 6) {
            int i5 = this.source.readShort() & 65535;
            int i6 = this.source.readInt();
            if (i5 == 2) {
                i3 = i5;
                if (i6 == 0) {
                    continue;
                } else {
                    if (i6 != 1) {
                        throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                    }
                    i3 = i5;
                }
            } else if (i5 == 3) {
                i3 = 4;
            } else if (i5 == 4) {
                i3 = 7;
                if (i6 < 0) {
                    throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                }
            } else if (i5 != 5) {
                i3 = i5;
            } else {
                if (i6 < 16384 || i6 > 16777215) {
                    throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", Integer.valueOf(i6));
                }
                i3 = i5;
            }
            settings.set(i3, i6);
        }
        handler.settings(false, settings);
    }

    private void readWindowUpdate(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", Integer.valueOf(i));
        }
        long j = this.source.readInt() & 2147483647L;
        if (j == 0) {
            throw Http2.ioException("windowSizeIncrement was 0", Long.valueOf(j));
        }
        handler.windowUpdate(i2, j);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.source.close();
    }

    public boolean nextFrame(boolean z, Handler handler) throws IOException {
        try {
            this.source.require(9L);
            int medium = readMedium(this.source);
            if (medium < 0 || medium > 16384) {
                throw Http2.ioException("FRAME_SIZE_ERROR: %s", Integer.valueOf(medium));
            }
            byte b = (byte) (this.source.readByte() & 255);
            if (z && b != 4) {
                throw Http2.ioException("Expected a SETTINGS frame but was %s", Byte.valueOf(b));
            }
            byte b2 = (byte) (this.source.readByte() & 255);
            int i = this.source.readInt() & ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Http2.frameLog(true, i, medium, b, b2));
            }
            switch (b) {
                case 0:
                    readData(handler, medium, b2, i);
                    return true;
                case 1:
                    readHeaders(handler, medium, b2, i);
                    return true;
                case 2:
                    readPriority(handler, medium, b2, i);
                    return true;
                case 3:
                    readRstStream(handler, medium, b2, i);
                    return true;
                case 4:
                    readSettings(handler, medium, b2, i);
                    return true;
                case 5:
                    readPushPromise(handler, medium, b2, i);
                    return true;
                case 6:
                    readPing(handler, medium, b2, i);
                    return true;
                case 7:
                    readGoAway(handler, medium, b2, i);
                    return true;
                case 8:
                    readWindowUpdate(handler, medium, b2, i);
                    return true;
                default:
                    this.source.skip(medium);
                    return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public void readConnectionPreface(Handler handler) throws IOException {
        if (this.client) {
            if (!nextFrame(true, handler)) {
                throw Http2.ioException("Required SETTINGS preface not received", new Object[0]);
            }
            return;
        }
        ByteString byteString = this.source.readByteString(Http2.CONNECTION_PREFACE.size());
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(Util.format("<< CONNECTION %s", byteString.hex()));
        }
        if (!Http2.CONNECTION_PREFACE.equals(byteString)) {
            throw Http2.ioException("Expected a connection header but was %s", byteString.utf8());
        }
    }
}
