package okhttp3.internal.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/CacheInterceptor.class */
public final class CacheInterceptor implements Interceptor {
    final InternalCache cache;

    public CacheInterceptor(InternalCache internalCache) {
        this.cache = internalCache;
    }

    private Response cacheWritingResponse(CacheRequest cacheRequest, Response response) throws IOException {
        Sink sinkBody;
        if (cacheRequest != null && (sinkBody = cacheRequest.body()) != null) {
            return response.newBuilder().body(new RealResponseBody(response.header("Content-Type"), response.body().contentLength(), Okio.buffer(new Source(this, response.body().source(), cacheRequest, Okio.buffer(sinkBody)) { // from class: okhttp3.internal.cache.CacheInterceptor.1
                boolean cacheRequestClosed;
                final CacheInterceptor this$0;
                final BufferedSink val$cacheBody;
                final CacheRequest val$cacheRequest;
                final BufferedSource val$source;

                {
                    this.this$0 = this;
                    this.val$source = bufferedSource;
                    this.val$cacheRequest = cacheRequest;
                    this.val$cacheBody = bufferedSink;
                }

                @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                        this.cacheRequestClosed = true;
                        this.val$cacheRequest.abort();
                    }
                    this.val$source.close();
                }

                @Override // okio.Source
                public long read(Buffer buffer, long j) throws IOException {
                    try {
                        long j2 = this.val$source.read(buffer, j);
                        if (j2 != -1) {
                            buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - j2, j2);
                            this.val$cacheBody.emitCompleteSegments();
                            return j2;
                        }
                        if (this.cacheRequestClosed) {
                            return -1L;
                        }
                        this.cacheRequestClosed = true;
                        this.val$cacheBody.close();
                        return -1L;
                    } catch (IOException e) {
                        if (!this.cacheRequestClosed) {
                            this.cacheRequestClosed = true;
                            this.val$cacheRequest.abort();
                        }
                        throw e;
                    }
                }

                @Override // okio.Source
                public Timeout timeout() {
                    return this.val$source.timeout();
                }
            }))).build();
        }
        return response;
    }

    private static Headers combine(Headers headers, Headers headers2) {
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String strName = headers.name(i);
            String strValue = headers.value(i);
            if ((!"Warning".equalsIgnoreCase(strName) || !strValue.startsWith("1")) && (isContentSpecificHeader(strName) || !isEndToEnd(strName) || headers2.get(strName) == null)) {
                Internal.instance.addLenient(builder, strName, strValue);
            }
        }
        int size2 = headers2.size();
        for (int i2 = 0; i2 < size2; i2++) {
            String strName2 = headers2.name(i2);
            if (!isContentSpecificHeader(strName2) && isEndToEnd(strName2)) {
                Internal.instance.addLenient(builder, strName2, headers2.value(i2));
            }
        }
        return builder.build();
    }

    static boolean isContentSpecificHeader(String str) {
        return "Content-Length".equalsIgnoreCase(str) || "Content-Encoding".equalsIgnoreCase(str) || "Content-Type".equalsIgnoreCase(str);
    }

    static boolean isEndToEnd(String str) {
        return ("Connection".equalsIgnoreCase(str) || "Keep-Alive".equalsIgnoreCase(str) || "Proxy-Authenticate".equalsIgnoreCase(str) || "Proxy-Authorization".equalsIgnoreCase(str) || "TE".equalsIgnoreCase(str) || "Trailers".equalsIgnoreCase(str) || "Transfer-Encoding".equalsIgnoreCase(str) || "Upgrade".equalsIgnoreCase(str)) ? false : true;
    }

    private static Response stripBody(Response response) {
        Response responseBuild = response;
        if (response != null) {
            responseBuild = response;
            if (response.body() != null) {
                responseBuild = response.newBuilder().body(null).build();
            }
        }
        return responseBuild;
    }

    /* JADX WARN: Finally extract failed */
    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        InternalCache internalCache = this.cache;
        Response response = internalCache != null ? internalCache.get(chain.request()) : null;
        CacheStrategy cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), response).get();
        Request request = cacheStrategy.networkRequest;
        Response response2 = cacheStrategy.cacheResponse;
        InternalCache internalCache2 = this.cache;
        if (internalCache2 != null) {
            internalCache2.trackResponse(cacheStrategy);
        }
        if (response != null && response2 == null) {
            Util.closeQuietly(response.body());
        }
        if (request == null && response2 == null) {
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        if (request == null) {
            return response2.newBuilder().cacheResponse(stripBody(response2)).build();
        }
        try {
            Response responseProceed = chain.proceed(request);
            if (responseProceed == null && response != null) {
                Util.closeQuietly(response.body());
            }
            if (response2 != null) {
                if (responseProceed.code() == 304) {
                    Response responseBuild = response2.newBuilder().headers(combine(response2.headers(), responseProceed.headers())).sentRequestAtMillis(responseProceed.sentRequestAtMillis()).receivedResponseAtMillis(responseProceed.receivedResponseAtMillis()).cacheResponse(stripBody(response2)).networkResponse(stripBody(responseProceed)).build();
                    responseProceed.body().close();
                    this.cache.trackConditionalCacheHit();
                    this.cache.update(response2, responseBuild);
                    return responseBuild;
                }
                Util.closeQuietly(response2.body());
            }
            Response responseBuild2 = responseProceed.newBuilder().cacheResponse(stripBody(response2)).networkResponse(stripBody(responseProceed)).build();
            if (this.cache != null) {
                if (HttpHeaders.hasBody(responseBuild2) && CacheStrategy.isCacheable(responseBuild2, request)) {
                    return cacheWritingResponse(this.cache.put(responseBuild2), responseBuild2);
                }
                if (HttpMethod.invalidatesCache(request.method())) {
                    try {
                        this.cache.remove(request);
                    } catch (IOException e) {
                    }
                }
            }
            return responseBuild2;
        } catch (Throwable th) {
            if (response != null) {
                Util.closeQuietly(response.body());
            }
            throw th;
        }
    }
}
