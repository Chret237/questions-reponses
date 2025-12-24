package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes-dex2jar.jar:okio/HashingSource.class */
public final class HashingSource extends ForwardingSource {
    private final Mac mac;
    private final MessageDigest messageDigest;

    private HashingSource(Source source, String str) {
        super(source);
        try {
            this.messageDigest = MessageDigest.getInstance(str);
            this.mac = null;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    private HashingSource(Source source, ByteString byteString, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        super(source);
        try {
            Mac mac = Mac.getInstance(str);
            this.mac = mac;
            mac.init(new SecretKeySpec(byteString.toByteArray(), str));
            this.messageDigest = null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError();
        }
    }

    public static HashingSource hmacSha1(Source source, ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA1");
    }

    public static HashingSource hmacSha256(Source source, ByteString byteString) {
        return new HashingSource(source, byteString, "HmacSHA256");
    }

    public static HashingSource md5(Source source) {
        return new HashingSource(source, "MD5");
    }

    public static HashingSource sha1(Source source) {
        return new HashingSource(source, "SHA-1");
    }

    public static HashingSource sha256(Source source) {
        return new HashingSource(source, "SHA-256");
    }

    public ByteString hash() {
        MessageDigest messageDigest = this.messageDigest;
        return ByteString.m19of(messageDigest != null ? messageDigest.digest() : this.mac.doFinal());
    }

    @Override // okio.ForwardingSource, okio.Source
    public long read(Buffer buffer, long j) throws IllegalStateException, IOException {
        long j2;
        long j3;
        Segment segment;
        long j4 = super.read(buffer, j);
        if (j4 != -1) {
            long j5 = buffer.size - j4;
            long j6 = buffer.size;
            Segment segment2 = buffer.head;
            while (true) {
                j2 = j5;
                j3 = j6;
                segment = segment2;
                if (j6 <= j5) {
                    break;
                }
                segment2 = segment2.prev;
                j6 -= segment2.limit - segment2.pos;
            }
            while (j3 < buffer.size) {
                int i = (int) ((segment.pos + j2) - j3);
                MessageDigest messageDigest = this.messageDigest;
                if (messageDigest != null) {
                    messageDigest.update(segment.data, i, segment.limit - i);
                } else {
                    this.mac.update(segment.data, i, segment.limit - i);
                }
                j3 += segment.limit - segment.pos;
                segment = segment.next;
                j2 = j3;
            }
        }
        return j4;
    }
}
