package okhttp3.internal.cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/CacheStrategy.class */
public final class CacheStrategy {

    @Nullable
    public final Response cacheResponse;

    @Nullable
    public final Request networkRequest;

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/cache/CacheStrategy$Factory.class */
    public static class Factory {
        private int ageSeconds;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;

        public Factory(long j, Request request, Response response) {
            this.ageSeconds = -1;
            this.nowMillis = j;
            this.request = request;
            this.cacheResponse = response;
            if (response != null) {
                this.sentRequestMillis = response.sentRequestAtMillis();
                this.receivedResponseMillis = response.receivedResponseAtMillis();
                Headers headers = response.headers();
                int size = headers.size();
                for (int i = 0; i < size; i++) {
                    String strName = headers.name(i);
                    String strValue = headers.value(i);
                    if ("Date".equalsIgnoreCase(strName)) {
                        this.servedDate = HttpDate.parse(strValue);
                        this.servedDateString = strValue;
                    } else if ("Expires".equalsIgnoreCase(strName)) {
                        this.expires = HttpDate.parse(strValue);
                    } else if ("Last-Modified".equalsIgnoreCase(strName)) {
                        this.lastModified = HttpDate.parse(strValue);
                        this.lastModifiedString = strValue;
                    } else if ("ETag".equalsIgnoreCase(strName)) {
                        this.etag = strValue;
                    } else if ("Age".equalsIgnoreCase(strName)) {
                        this.ageSeconds = HttpHeaders.parseSeconds(strValue, -1);
                    }
                }
            }
        }

        private long cacheResponseAge() {
            Date date = this.servedDate;
            long jMax = 0;
            if (date != null) {
                jMax = Math.max(0L, this.receivedResponseMillis - date.getTime());
            }
            long jMax2 = jMax;
            if (this.ageSeconds != -1) {
                jMax2 = Math.max(jMax, TimeUnit.SECONDS.toMillis(this.ageSeconds));
            }
            long j = this.receivedResponseMillis;
            return jMax2 + (j - this.sentRequestMillis) + (this.nowMillis - j);
        }

        private long computeFreshnessLifetime() {
            if (this.cacheResponse.cacheControl().maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis(r0.maxAgeSeconds());
            }
            long j = 0;
            if (this.expires != null) {
                Date date = this.servedDate;
                long time = this.expires.getTime() - (date != null ? date.getTime() : this.receivedResponseMillis);
                if (time > 0) {
                    j = time;
                }
                return j;
            }
            long j2 = 0;
            if (this.lastModified != null) {
                j2 = 0;
                if (this.cacheResponse.request().url().query() == null) {
                    Date date2 = this.servedDate;
                    long time2 = (date2 != null ? date2.getTime() : this.sentRequestMillis) - this.lastModified.getTime();
                    j2 = 0;
                    if (time2 > 0) {
                        j2 = time2 / 10;
                    }
                }
            }
            return j2;
        }

        private CacheStrategy getCandidate() {
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null);
            }
            if ((!this.request.isHttps() || this.cacheResponse.handshake() != null) && CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                CacheControl cacheControl = this.request.cacheControl();
                if (cacheControl.noCache() || hasConditions(this.request)) {
                    return new CacheStrategy(this.request, null);
                }
                CacheControl cacheControl2 = this.cacheResponse.cacheControl();
                if (cacheControl2.immutable()) {
                    return new CacheStrategy(null, this.cacheResponse);
                }
                long jCacheResponseAge = cacheResponseAge();
                long jComputeFreshnessLifetime = computeFreshnessLifetime();
                long jMin = jComputeFreshnessLifetime;
                if (cacheControl.maxAgeSeconds() != -1) {
                    jMin = Math.min(jComputeFreshnessLifetime, TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds()));
                }
                long millis = cacheControl.minFreshSeconds() != -1 ? TimeUnit.SECONDS.toMillis(cacheControl.minFreshSeconds()) : 0L;
                long millis2 = 0;
                if (!cacheControl2.mustRevalidate()) {
                    millis2 = 0;
                    if (cacheControl.maxStaleSeconds() != -1) {
                        millis2 = TimeUnit.SECONDS.toMillis(cacheControl.maxStaleSeconds());
                    }
                }
                if (!cacheControl2.noCache()) {
                    long j = millis + jCacheResponseAge;
                    if (j < millis2 + jMin) {
                        Response.Builder builderNewBuilder = this.cacheResponse.newBuilder();
                        if (j >= jMin) {
                            builderNewBuilder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                        }
                        if (jCacheResponseAge > 86400000 && isFreshnessLifetimeHeuristic()) {
                            builderNewBuilder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                        }
                        return new CacheStrategy(null, builderNewBuilder.build());
                    }
                }
                String str = this.etag;
                String str2 = "If-Modified-Since";
                if (str != null) {
                    str2 = "If-None-Match";
                } else if (this.lastModified != null) {
                    str = this.lastModifiedString;
                } else {
                    if (this.servedDate == null) {
                        return new CacheStrategy(this.request, null);
                    }
                    str = this.servedDateString;
                }
                Headers.Builder builderNewBuilder2 = this.request.headers().newBuilder();
                Internal.instance.addLenient(builderNewBuilder2, str2, str);
                return new CacheStrategy(this.request.newBuilder().headers(builderNewBuilder2.build()).build(), this.cacheResponse);
            }
            return new CacheStrategy(this.request, null);
        }

        private static boolean hasConditions(Request request) {
            return (request.header("If-Modified-Since") == null && request.header("If-None-Match") == null) ? false : true;
        }

        private boolean isFreshnessLifetimeHeuristic() {
            return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }

        public CacheStrategy get() {
            CacheStrategy candidate = getCandidate();
            CacheStrategy cacheStrategy = candidate;
            if (candidate.networkRequest != null) {
                cacheStrategy = candidate;
                if (this.request.cacheControl().onlyIfCached()) {
                    cacheStrategy = new CacheStrategy(null, null);
                }
            }
            return cacheStrategy;
        }
    }

    CacheStrategy(Request request, Response response) {
        this.networkRequest = request;
        this.cacheResponse = response;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isCacheable(okhttp3.Response r3, okhttp3.Request r4) {
        /*
            r0 = r3
            int r0 = r0.code()
            r5 = r0
            r0 = 0
            r7 = r0
            r0 = r5
            r1 = 200(0xc8, float:2.8E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 410(0x19a, float:5.75E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 414(0x19e, float:5.8E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 501(0x1f5, float:7.02E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 203(0xcb, float:2.84E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 204(0xcc, float:2.86E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 307(0x133, float:4.3E-43)
            if (r0 == r1) goto L6b
            r0 = r5
            r1 = 308(0x134, float:4.32E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 404(0x194, float:5.66E-43)
            if (r0 == r1) goto L98
            r0 = r5
            r1 = 405(0x195, float:5.68E-43)
            if (r0 == r1) goto L98
            r0 = r5
            switch(r0) {
                case 300: goto L98;
                case 301: goto L98;
                case 302: goto L6b;
                default: goto L68;
            }
        L68:
            goto L96
        L6b:
            r0 = r3
            java.lang.String r1 = "Expires"
            java.lang.String r0 = r0.header(r1)
            if (r0 != 0) goto L98
            r0 = r3
            okhttp3.CacheControl r0 = r0.cacheControl()
            int r0 = r0.maxAgeSeconds()
            r1 = -1
            if (r0 != r1) goto L98
            r0 = r3
            okhttp3.CacheControl r0 = r0.cacheControl()
            boolean r0 = r0.isPublic()
            if (r0 != 0) goto L98
            r0 = r3
            okhttp3.CacheControl r0 = r0.cacheControl()
            boolean r0 = r0.isPrivate()
            if (r0 == 0) goto L96
            goto L98
        L96:
            r0 = 0
            return r0
        L98:
            r0 = r7
            r6 = r0
            r0 = r3
            okhttp3.CacheControl r0 = r0.cacheControl()
            boolean r0 = r0.noStore()
            if (r0 != 0) goto Lb4
            r0 = r7
            r6 = r0
            r0 = r4
            okhttp3.CacheControl r0 = r0.cacheControl()
            boolean r0 = r0.noStore()
            if (r0 != 0) goto Lb4
            r0 = 1
            r6 = r0
        Lb4:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.CacheStrategy.isCacheable(okhttp3.Response, okhttp3.Request):boolean");
    }
}
