package retrofit2;

import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

/* loaded from: classes-dex2jar.jar:retrofit2/RequestBuilder.class */
final class RequestBuilder {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#";
    private final HttpUrl baseUrl;

    @Nullable
    private RequestBody body;

    @Nullable
    private MediaType contentType;

    @Nullable
    private FormBody.Builder formBuilder;
    private final boolean hasBody;
    private final String method;

    @Nullable
    private MultipartBody.Builder multipartBuilder;

    @Nullable
    private String relativeUrl;
    private final Request.Builder requestBuilder;

    @Nullable
    private HttpUrl.Builder urlBuilder;

    /* loaded from: classes-dex2jar.jar:retrofit2/RequestBuilder$ContentTypeOverridingRequestBody.class */
    private static class ContentTypeOverridingRequestBody extends RequestBody {
        private final MediaType contentType;
        private final RequestBody delegate;

        ContentTypeOverridingRequestBody(RequestBody requestBody, MediaType mediaType) {
            this.delegate = requestBody;
            this.contentType = mediaType;
        }

        @Override // okhttp3.RequestBody
        public long contentLength() throws IOException {
            return this.delegate.contentLength();
        }

        @Override // okhttp3.RequestBody
        public MediaType contentType() {
            return this.contentType;
        }

        @Override // okhttp3.RequestBody
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            this.delegate.writeTo(bufferedSink);
        }
    }

    RequestBuilder(String str, HttpUrl httpUrl, @Nullable String str2, @Nullable Headers headers, @Nullable MediaType mediaType, boolean z, boolean z2, boolean z3) {
        this.method = str;
        this.baseUrl = httpUrl;
        this.relativeUrl = str2;
        Request.Builder builder = new Request.Builder();
        this.requestBuilder = builder;
        this.contentType = mediaType;
        this.hasBody = z;
        if (headers != null) {
            builder.headers(headers);
        }
        if (z2) {
            this.formBuilder = new FormBody.Builder();
        } else if (z3) {
            MultipartBody.Builder builder2 = new MultipartBody.Builder();
            this.multipartBuilder = builder2;
            builder2.setType(MultipartBody.FORM);
        }
    }

    private static String canonicalizeForPath(String str, boolean z) {
        int i;
        String utf8;
        int length = str.length();
        int iCharCount = 0;
        while (true) {
            i = iCharCount;
            utf8 = str;
            if (i >= length) {
                break;
            }
            int iCodePointAt = str.codePointAt(i);
            if (iCodePointAt < 32 || iCodePointAt >= 127 || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(iCodePointAt) != -1 || (!z && (iCodePointAt == 47 || iCodePointAt == 37))) {
                break;
            }
            iCharCount = i + Character.charCount(iCodePointAt);
        }
        Buffer buffer = new Buffer();
        buffer.writeUtf8(str, 0, i);
        canonicalizeForPath(buffer, str, i, length, z);
        utf8 = buffer.readUtf8();
        return utf8;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void canonicalizeForPath(okio.Buffer r5, java.lang.String r6, int r7, int r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 233
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: retrofit2.RequestBuilder.canonicalizeForPath(okio.Buffer, java.lang.String, int, int, boolean):void");
    }

    void addFormField(String str, String str2, boolean z) {
        if (z) {
            this.formBuilder.addEncoded(str, str2);
        } else {
            this.formBuilder.add(str, str2);
        }
    }

    void addHeader(String str, String str2) {
        if (!"Content-Type".equalsIgnoreCase(str)) {
            this.requestBuilder.addHeader(str, str2);
            return;
        }
        MediaType mediaType = MediaType.parse(str2);
        if (mediaType != null) {
            this.contentType = mediaType;
            return;
        }
        throw new IllegalArgumentException("Malformed content type: " + str2);
    }

    void addPart(Headers headers, RequestBody requestBody) {
        this.multipartBuilder.addPart(headers, requestBody);
    }

    void addPart(MultipartBody.Part part) {
        this.multipartBuilder.addPart(part);
    }

    void addPathParam(String str, String str2, boolean z) {
        String str3 = this.relativeUrl;
        if (str3 == null) {
            throw new AssertionError();
        }
        this.relativeUrl = str3.replace("{" + str + "}", canonicalizeForPath(str2, z));
    }

    void addQueryParam(String str, @Nullable String str2, boolean z) {
        String str3 = this.relativeUrl;
        if (str3 != null) {
            HttpUrl.Builder builderNewBuilder = this.baseUrl.newBuilder(str3);
            this.urlBuilder = builderNewBuilder;
            if (builderNewBuilder == null) {
                throw new IllegalArgumentException("Malformed URL. Base: " + this.baseUrl + ", Relative: " + this.relativeUrl);
            }
            this.relativeUrl = null;
        }
        if (z) {
            this.urlBuilder.addEncodedQueryParameter(str, str2);
        } else {
            this.urlBuilder.addQueryParameter(str, str2);
        }
    }

    Request build() {
        HttpUrl httpUrlResolve;
        HttpUrl.Builder builder = this.urlBuilder;
        if (builder != null) {
            httpUrlResolve = builder.build();
        } else {
            httpUrlResolve = this.baseUrl.resolve(this.relativeUrl);
            if (httpUrlResolve == null) {
                throw new IllegalArgumentException("Malformed URL. Base: " + this.baseUrl + ", Relative: " + this.relativeUrl);
            }
        }
        RequestBody requestBody = this.body;
        RequestBody requestBodyCreate = requestBody;
        if (requestBody == null) {
            FormBody.Builder builder2 = this.formBuilder;
            if (builder2 != null) {
                requestBodyCreate = builder2.build();
            } else {
                MultipartBody.Builder builder3 = this.multipartBuilder;
                if (builder3 != null) {
                    requestBodyCreate = builder3.build();
                } else {
                    requestBodyCreate = requestBody;
                    if (this.hasBody) {
                        requestBodyCreate = RequestBody.create((MediaType) null, new byte[0]);
                    }
                }
            }
        }
        MediaType mediaType = this.contentType;
        RequestBody contentTypeOverridingRequestBody = requestBodyCreate;
        if (mediaType != null) {
            if (requestBodyCreate != null) {
                contentTypeOverridingRequestBody = new ContentTypeOverridingRequestBody(requestBodyCreate, mediaType);
            } else {
                this.requestBuilder.addHeader("Content-Type", mediaType.toString());
                contentTypeOverridingRequestBody = requestBodyCreate;
            }
        }
        return this.requestBuilder.url(httpUrlResolve).method(this.method, contentTypeOverridingRequestBody).build();
    }

    void setBody(RequestBody requestBody) {
        this.body = requestBody;
    }

    void setRelativeUrl(Object obj) {
        this.relativeUrl = obj.toString();
    }
}
