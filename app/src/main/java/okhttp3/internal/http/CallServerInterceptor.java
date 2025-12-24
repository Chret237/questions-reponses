package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http/CallServerInterceptor.class */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http/CallServerInterceptor$CountingSink.class */
    static final class CountingSink extends ForwardingSink {
        long successfulCount;

        CountingSink(Sink sink) {
            super(sink);
        }

        @Override // okio.ForwardingSink, okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            super.write(buffer, j);
            this.successfulCount += j;
        }
    }

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpCodec httpCodecHttpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        Request request = realInterceptorChain.request();
        long jCurrentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpCodecHttpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        Response.Builder responseHeaders = null;
        Response.Builder builder = null;
        if (HttpMethod.permitsRequestBody(request.method())) {
            builder = null;
            if (request.body() != null) {
                if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                    httpCodecHttpStream.flushRequest();
                    realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                    responseHeaders = httpCodecHttpStream.readResponseHeaders(true);
                }
                if (responseHeaders == null) {
                    realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                    CountingSink countingSink = new CountingSink(httpCodecHttpStream.createRequestBody(request, request.body().contentLength()));
                    BufferedSink bufferedSinkBuffer = Okio.buffer(countingSink);
                    request.body().writeTo(bufferedSinkBuffer);
                    bufferedSinkBuffer.close();
                    realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
                    builder = responseHeaders;
                } else {
                    builder = responseHeaders;
                    if (!realConnection.isMultiplexed()) {
                        streamAllocation.noNewStreams();
                        builder = responseHeaders;
                    }
                }
            }
        }
        httpCodecHttpStream.finishRequest();
        Response.Builder responseHeaders2 = builder;
        if (builder == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            responseHeaders2 = httpCodecHttpStream.readResponseHeaders(false);
        }
        Response responseBuild = responseHeaders2.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int iCode = responseBuild.code();
        int iCode2 = iCode;
        if (iCode == 100) {
            responseBuild = httpCodecHttpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            iCode2 = responseBuild.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), responseBuild);
        Response responseBuild2 = (this.forWebSocket && iCode2 == 101) ? responseBuild.newBuilder().body(Util.EMPTY_RESPONSE).build() : responseBuild.newBuilder().body(httpCodecHttpStream.openResponseBody(responseBuild)).build();
        if ("close".equalsIgnoreCase(responseBuild2.request().header("Connection")) || "close".equalsIgnoreCase(responseBuild2.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((iCode2 != 204 && iCode2 != 205) || responseBuild2.body().contentLength() <= 0) {
            return responseBuild2;
        }
        throw new ProtocolException("HTTP " + iCode2 + " had non-zero Content-Length: " + responseBuild2.body().contentLength());
    }
}
