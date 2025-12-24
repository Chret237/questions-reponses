package okhttp3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

/* loaded from: classes-dex2jar.jar:okhttp3/RequestBody.class */
public abstract class RequestBody {
    public static RequestBody create(@Nullable MediaType mediaType, File file) {
        if (file != null) {
            return new RequestBody(mediaType, file) { // from class: okhttp3.RequestBody.3
                final MediaType val$contentType;
                final File val$file;

                {
                    this.val$contentType = mediaType;
                    this.val$file = file;
                }

                @Override // okhttp3.RequestBody
                public long contentLength() {
                    return this.val$file.length();
                }

                @Override // okhttp3.RequestBody
                @Nullable
                public MediaType contentType() {
                    return this.val$contentType;
                }

                @Override // okhttp3.RequestBody
                public void writeTo(BufferedSink bufferedSink) throws IOException {
                    Source source = null;
                    try {
                        Source source2 = Okio.source(this.val$file);
                        source = source2;
                        bufferedSink.writeAll(source2);
                        Util.closeQuietly(source2);
                    } catch (Throwable th) {
                        Util.closeQuietly(source);
                        throw th;
                    }
                }
            };
        }
        throw new NullPointerException("content == null");
    }

    public static RequestBody create(@Nullable MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        MediaType mediaType2 = mediaType;
        if (mediaType != null) {
            Charset charset2 = mediaType.charset();
            charset = charset2;
            mediaType2 = mediaType;
            if (charset2 == null) {
                charset = Util.UTF_8;
                mediaType2 = MediaType.parse(mediaType + "; charset=utf-8");
            }
        }
        return create(mediaType2, str.getBytes(charset));
    }

    public static RequestBody create(@Nullable MediaType mediaType, ByteString byteString) {
        return new RequestBody(mediaType, byteString) { // from class: okhttp3.RequestBody.1
            final ByteString val$content;
            final MediaType val$contentType;

            {
                this.val$contentType = mediaType;
                this.val$content = byteString;
            }

            @Override // okhttp3.RequestBody
            public long contentLength() throws IOException {
                return this.val$content.size();
            }

            @Override // okhttp3.RequestBody
            @Nullable
            public MediaType contentType() {
                return this.val$contentType;
            }

            @Override // okhttp3.RequestBody
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(this.val$content);
            }
        };
    }

    public static RequestBody create(@Nullable MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr, 0, bArr.length);
    }

    public static RequestBody create(@Nullable MediaType mediaType, byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(bArr.length, i, i2);
        return new RequestBody(mediaType, i2, bArr, i) { // from class: okhttp3.RequestBody.2
            final int val$byteCount;
            final byte[] val$content;
            final MediaType val$contentType;
            final int val$offset;

            {
                this.val$contentType = mediaType;
                this.val$byteCount = i2;
                this.val$content = bArr;
                this.val$offset = i;
            }

            @Override // okhttp3.RequestBody
            public long contentLength() {
                return this.val$byteCount;
            }

            @Override // okhttp3.RequestBody
            @Nullable
            public MediaType contentType() {
                return this.val$contentType;
            }

            @Override // okhttp3.RequestBody
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(this.val$content, this.val$offset, this.val$byteCount);
            }
        };
    }

    public long contentLength() throws IOException {
        return -1L;
    }

    @Nullable
    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink) throws IOException;
}
