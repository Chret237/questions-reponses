package okhttp3.internal.http2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import okio.BufferedSink;
import okio.ByteString;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Huffman.class */
class Huffman {
    private static final int[] CODES = $d2j$hex$f88a9d59$decode_I("f81f0000d8ff7f00e2ffff0fe3ffff0fe4ffff0fe5ffff0fe6ffff0fe7ffff0fe8ffff0feaffff00fcffff3fe9ffff0feaffff0ffdffff3febffff0fecffff0fedffff0feeffff0fefffff0ff0ffff0ff1ffff0ff2ffff0ffeffff3ff3ffff0ff4ffff0ff5ffff0ff6ffff0ff7ffff0ff8ffff0ff9ffff0ffaffff0ffbffff0f14000000f8030000f9030000fa0f0000f91f000015000000f8000000fa070000fa030000fb030000f9000000fb070000fa000000160000001700000018000000000000000100000002000000190000001a0000001b0000001c0000001d0000001e0000001f0000005c000000fb000000fc7f000020000000fb0f0000fc030000fa1f0000210000005d0000005e0000005f000000600000006100000062000000630000006400000065000000660000006700000068000000690000006a0000006b0000006c0000006d0000006e0000006f000000700000007100000072000000fc00000073000000fd000000fb1f0000f0ff0700fc1f0000fc3f000022000000fd7f0000030000002300000004000000240000000500000025000000260000002700000006000000740000007500000028000000290000002a000000070000002b000000760000002c00000008000000090000002d0000007700000078000000790000007a0000007b000000fe7f0000fc070000fd3f0000fd1f0000fcffff0fe6ff0f00d2ff3f00e7ff0f00e8ff0f00d3ff3f00d4ff3f00d5ff3f00d9ff7f00d6ff3f00daff7f00dbff7f00dcff7f00ddff7f00deff7f00ebffff00dfff7f00ecffff00edffff00d7ff3f00e0ff7f00eeffff00e1ff7f00e2ff7f00e3ff7f00e4ff7f00dcff1f00d8ff3f00e5ff7f00d9ff3f00e6ff7f00e7ff7f00efffff00daff3f00ddff1f00e9ff0f00dbff3f00dcff3f00e8ff7f00e9ff7f00deff1f00eaff7f00ddff3f00deff3f00f0ffff00dfff1f00dfff3f00ebff7f00ecff7f00e0ff1f00e1ff1f00e0ff3f00e2ff1f00edff7f00e1ff3f00eeff7f00efff7f00eaff0f00e2ff3f00e3ff3f00e4ff3f00f0ff7f00e5ff3f00e6ff3f00f1ff7f00e0ffff03e1ffff03ebff0f00f1ff0700e7ff3f00f2ff7f00e8ff3f00ecffff01e2ffff03e3ffff03e4ffff03deffff07dfffff07e5ffff03f1ffff00edffff01f2ff0700e3ff1f00e6ffff03e0ffff07e1ffff07e7ffff03e2ffff07f2ffff00e4ff1f00e5ff1f00e8ffff03e9ffff03fdffff0fe3ffff07e4ffff07e5ffff07ecff0f00f3ffff00edff0f00e6ff1f00e9ff3f00e7ff1f00e8ff1f00f3ff7f00eaff3f00ebff3f00eeffff01efffff01f4ffff00f5ffff00eaffff03f4ff7f00ebffff03e6ffff07ecffff03edffff03e7ffff07e8ffff07e9ffff07eaffff07ebffff07feffff0fecffff07edffff07eeffff07efffff07f0ffff07eeffff03");
    private static final byte[] CODE_LENGTHS = {13, 23, 28, 28, 28, 28, 28, 28, 28, 24, 30, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 30, 28, 28, 28, 28, 28, 28, 28, 28, 28, 6, 10, 10, 12, 13, 6, 8, 11, 10, 10, 8, 11, 8, 6, 6, 6, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 7, 8, 15, 6, 12, 10, 13, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 7, 8, 13, 19, 13, 14, 6, 15, 5, 6, 5, 6, 5, 6, 6, 6, 5, 7, 7, 6, 6, 6, 5, 6, 7, 6, 5, 5, 6, 7, 7, 7, 7, 7, 15, 11, 14, 13, 28, 20, 22, 20, 20, 22, 22, 22, 23, 22, 23, 23, 23, 23, 23, 24, 23, 24, 24, 22, 23, 24, 23, 23, 23, 23, 21, 22, 23, 22, 23, 23, 24, 22, 21, 20, 22, 22, 23, 23, 21, 23, 22, 22, 24, 21, 22, 23, 23, 21, 21, 22, 21, 23, 22, 23, 23, 20, 22, 22, 22, 23, 22, 22, 23, 26, 26, 20, 19, 22, 23, 22, 25, 26, 26, 26, 27, 27, 26, 24, 25, 19, 21, 26, 27, 27, 26, 27, 24, 21, 21, 26, 26, 28, 27, 27, 27, 20, 24, 20, 21, 22, 21, 21, 23, 22, 22, 25, 25, 24, 24, 26, 23, 26, 27, 26, 26, 27, 27, 27, 27, 27, 28, 27, 27, 27, 27, 27, 26};
    private static final Huffman INSTANCE = new Huffman();
    private final Node root = new Node();

    /* loaded from: classes-dex2jar.jar:okhttp3/internal/http2/Huffman$Node.class */
    private static final class Node {
        final Node[] children;
        final int symbol;
        final int terminalBits;

        Node() {
            this.children = new Node[256];
            this.symbol = 0;
            this.terminalBits = 0;
        }

        Node(int i, int i2) {
            this.children = null;
            this.symbol = i;
            int i3 = i2 & 7;
            this.terminalBits = i3 == 0 ? 8 : i3;
        }
    }

    private Huffman() {
        buildTree();
    }

    private void addCode(int i, int i2, byte b) {
        Node node = new Node(i, b);
        Node node2 = this.root;
        while (true) {
            Node node3 = node2;
            if (b <= 8) {
                int i3 = 8 - b;
                int i4 = (i2 << i3) & 255;
                for (int i5 = i4; i5 < i4 + (1 << i3); i5++) {
                    node3.children[i5] = node;
                }
                return;
            }
            b = (byte) (b - 8);
            int i6 = (i2 >>> b) & 255;
            if (node3.children == null) {
                throw new IllegalStateException("invalid dictionary: prefix not unique");
            }
            if (node3.children[i6] == null) {
                node3.children[i6] = new Node();
            }
            node2 = node3.children[i6];
        }
    }

    private void buildTree() {
        int i = 0;
        while (true) {
            byte[] bArr = CODE_LENGTHS;
            if (i >= bArr.length) {
                return;
            }
            addCode(i, CODES[i], bArr[i]);
            i++;
        }
    }

    public static Huffman get() {
        return INSTANCE;
    }

    byte[] decode(byte[] bArr) {
        Node node;
        int i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Node node2 = this.root;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            node = node2;
            i = i4;
            if (i2 >= bArr.length) {
                break;
            }
            i3 = (i3 << 8) | (bArr[i2] & 255);
            i4 += 8;
            while (i4 >= 8) {
                node2 = node2.children[(i3 >>> (i4 - 8)) & 255];
                if (node2.children == null) {
                    byteArrayOutputStream.write(node2.symbol);
                    i4 -= node2.terminalBits;
                    node2 = this.root;
                } else {
                    i4 -= 8;
                }
            }
            i2++;
        }
        while (i > 0) {
            Node node3 = node.children[(i3 << (8 - i)) & 255];
            if (node3.children != null || node3.terminalBits > i) {
                break;
            }
            byteArrayOutputStream.write(node3.symbol);
            i -= node3.terminalBits;
            node = this.root;
        }
        return byteArrayOutputStream.toByteArray();
    }

    void encode(ByteString byteString, BufferedSink bufferedSink) throws IOException {
        long j = 0;
        int i = 0;
        for (int i2 = 0; i2 < byteString.size(); i2++) {
            int i3 = byteString.getByte(i2) & 255;
            int i4 = CODES[i3];
            byte b = CODE_LENGTHS[i3];
            j = (j << b) | i4;
            i += b;
            while (i >= 8) {
                i -= 8;
                bufferedSink.writeByte((int) (j >> i));
            }
        }
        if (i > 0) {
            bufferedSink.writeByte((int) ((255 >>> i) | (j << (8 - i))));
        }
    }

    int encodedLength(ByteString byteString) {
        long j = 0;
        for (int i = 0; i < byteString.size(); i++) {
            j += CODE_LENGTHS[byteString.getByte(i) & 255];
        }
        return (int) ((j + 7) >> 3);
    }

    private static long[] $d2j$hex$f88a9d59$decode_J(String src) {
        byte[] d = $d2j$hex$f88a9d59$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer s = b.asLongBuffer();
        long[] data = new long[d.length / 8];
        s.get(data);
        return data;
    }

    private static int[] $d2j$hex$f88a9d59$decode_I(String src) {
        byte[] d = $d2j$hex$f88a9d59$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer s = b.asIntBuffer();
        int[] data = new int[d.length / 4];
        s.get(data);
        return data;
    }

    private static short[] $d2j$hex$f88a9d59$decode_S(String src) {
        byte[] d = $d2j$hex$f88a9d59$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer s = b.asShortBuffer();
        short[] data = new short[d.length / 2];
        s.get(data);
        return data;
    }

    private static byte[] $d2j$hex$f88a9d59$decode_B(String src) {
        int hh;
        int i;
        char[] d = src.toCharArray();
        byte[] ret = new byte[src.length() / 2];
        for (int i2 = 0; i2 < ret.length; i2++) {
            char h = d[2 * i2];
            char l = d[(2 * i2) + 1];
            if (h >= '0' && h <= '9') {
                hh = h - '0';
            } else if (h >= 'a' && h <= 'f') {
                hh = (h - 'a') + 10;
            } else if (h >= 'A' && h <= 'F') {
                hh = (h - 'A') + 10;
            } else {
                throw new RuntimeException();
            }
            if (l >= '0' && l <= '9') {
                i = l - '0';
            } else if (l >= 'a' && l <= 'f') {
                i = (l - 'a') + 10;
            } else if (l >= 'A' && l <= 'F') {
                i = (l - 'A') + 10;
            } else {
                throw new RuntimeException();
            }
            int ll = i;
            ret[i2] = (byte) ((hh << 4) | ll);
        }
        return ret;
    }
}
