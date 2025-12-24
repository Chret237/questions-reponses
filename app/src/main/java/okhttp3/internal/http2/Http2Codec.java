package okhttp3.internal.http2;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.ByteString;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Codec.class */
public final class Http2Codec implements HttpCodec {
    private static final List<ByteString> HTTP_2_SKIPPED_REQUEST_HEADERS;
    private static final List<ByteString> HTTP_2_SKIPPED_RESPONSE_HEADERS;
    private static final ByteString UPGRADE;
    private final Interceptor.Chain chain;
    private final OkHttpClient client;
    private final Http2Connection connection;
    private Http2Stream stream;
    final StreamAllocation streamAllocation;
    private static final ByteString CONNECTION = ByteString.encodeUtf8("connection");
    private static final ByteString HOST = ByteString.encodeUtf8("host");
    private static final ByteString KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
    private static final ByteString PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
    private static final ByteString TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");

    /* renamed from: TE */
    private static final ByteString f83TE = ByteString.encodeUtf8("te");
    private static final ByteString ENCODING = ByteString.encodeUtf8("encoding");

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Http2Codec$StreamFinishingSource.class */
    class StreamFinishingSource extends ForwardingSource {
        long bytesRead;
        boolean completed;
        final Http2Codec this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        StreamFinishingSource(Http2Codec http2Codec, Source source) {
            super(source);
            this.this$0 = http2Codec;
            this.completed = false;
            this.bytesRead = 0L;
        }

        private void endOfInput(IOException iOException) {
            if (this.completed) {
                return;
            }
            this.completed = true;
            this.this$0.streamAllocation.streamFinished(false, this.this$0, this.bytesRead, iOException);
        }

        @Override // okio.ForwardingSource, okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            endOfInput(null);
        }

        @Override // okio.ForwardingSource, okio.Source
        public long read(Buffer buffer, long j) throws IOException {
            try {
                long j2 = delegate().read(buffer, j);
                if (j2 > 0) {
                    this.bytesRead += j2;
                }
                return j2;
            } catch (IOException e) {
                endOfInput(e);
                throw e;
            }
        }
    }

    static {
        ByteString byteStringEncodeUtf8 = ByteString.encodeUtf8("upgrade");
        UPGRADE = byteStringEncodeUtf8;
        HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, f83TE, TRANSFER_ENCODING, ENCODING, byteStringEncodeUtf8, Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY);
        HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList(CONNECTION, HOST, KEEP_ALIVE, PROXY_CONNECTION, f83TE, TRANSFER_ENCODING, ENCODING, UPGRADE);
    }

    public Http2Codec(OkHttpClient okHttpClient, Interceptor.Chain chain, StreamAllocation streamAllocation, Http2Connection http2Connection) {
        this.client = okHttpClient;
        this.chain = chain;
        this.streamAllocation = streamAllocation;
        this.connection = http2Connection;
    }

    public static List<Header> http2HeadersList(Request request) {
        Headers headers = request.headers();
        ArrayList arrayList = new ArrayList(headers.size() + 4);
        arrayList.add(new Header(Header.TARGET_METHOD, request.method()));
        arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(request.url())));
        String strHeader = request.header("Host");
        if (strHeader != null) {
            arrayList.add(new Header(Header.TARGET_AUTHORITY, strHeader));
        }
        arrayList.add(new Header(Header.TARGET_SCHEME, request.url().scheme()));
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            ByteString byteStringEncodeUtf8 = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(byteStringEncodeUtf8)) {
                arrayList.add(new Header(byteStringEncodeUtf8, headers.value(i)));
            }
        }
        return arrayList;
    }

    public static Response.Builder readHttp2HeadersList(List<Header> list) throws NumberFormatException, IOException {
        StatusLine statusLine;
        Headers.Builder builder;
        StatusLine statusLine2;
        Headers.Builder builder2 = new Headers.Builder();
        int size = list.size();
        int i = 0;
        StatusLine statusLine3 = null;
        while (true) {
            statusLine = statusLine3;
            if (i >= size) {
                break;
            }
            Header header = list.get(i);
            if (header == null) {
                builder = builder2;
                statusLine2 = statusLine;
                if (statusLine != null) {
                    builder = builder2;
                    statusLine2 = statusLine;
                    if (statusLine.code == 100) {
                        builder = new Headers.Builder();
                        statusLine2 = null;
                    }
                }
            } else {
                ByteString byteString = header.name;
                String strUtf8 = header.value.utf8();
                if (byteString.equals(Header.RESPONSE_STATUS)) {
                    statusLine2 = StatusLine.parse("HTTP/1.1 " + strUtf8);
                    builder = builder2;
                } else {
                    builder = builder2;
                    statusLine2 = statusLine;
                    if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(byteString)) {
                        Internal.instance.addLenient(builder2, byteString.utf8(), strUtf8);
                        statusLine2 = statusLine;
                        builder = builder2;
                    }
                }
            }
            i++;
            builder2 = builder;
            statusLine3 = statusLine2;
        }
        if (statusLine != null) {
            return new Response.Builder().protocol(Protocol.HTTP_2).code(statusLine.code).message(statusLine.message).headers(builder2.build());
        }
        throw new ProtocolException("Expected ':status' header not present");
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void cancel() {
        Http2Stream http2Stream = this.stream;
        if (http2Stream != null) {
            http2Stream.closeLater(ErrorCode.CANCEL);
        }
    }

    @Override // okhttp3.internal.http.HttpCodec
    public Sink createRequestBody(Request request, long j) {
        return this.stream.getSink();
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void finishRequest() throws IOException {
        this.stream.getSink().close();
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void flushRequest() throws IOException {
        this.connection.flush();
    }

    @Override // okhttp3.internal.http.HttpCodec
    public ResponseBody openResponseBody(Response response) throws IOException {
        this.streamAllocation.eventListener.responseBodyStart(this.streamAllocation.call);
        return new RealResponseBody(response.header("Content-Type"), HttpHeaders.contentLength(response), Okio.buffer(new StreamFinishingSource(this, this.stream.getSource())));
    }

    @Override // okhttp3.internal.http.HttpCodec
    public Response.Builder readResponseHeaders(boolean z) throws NumberFormatException, IOException {
        Response.Builder http2HeadersList = readHttp2HeadersList(this.stream.takeResponseHeaders());
        if (z && Internal.instance.code(http2HeadersList) == 100) {
            return null;
        }
        return http2HeadersList;
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void writeRequestHeaders(Request request) throws IOException {
        if (this.stream != null) {
            return;
        }
        Http2Stream http2StreamNewStream = this.connection.newStream(http2HeadersList(request), request.body() != null);
        this.stream = http2StreamNewStream;
        http2StreamNewStream.readTimeout().timeout(this.chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.stream.writeTimeout().timeout(this.chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
    }
}
