package com.alibaba.fastjson.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/asm/ClassReader.class */
public class ClassReader {

    /* renamed from: b */
    public final byte[] f66b;
    public final int header;
    private final int[] items;
    private final int maxStringLength;
    private boolean readAnnotations;
    private final String[] strings;

    public ClassReader(InputStream inputStream, boolean z) throws IOException {
        int i;
        int i2;
        int i3;
        int i4;
        this.readAnnotations = z;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int i5 = inputStream.read(bArr);
            i = 0;
            if (i5 == -1) {
                break;
            } else if (i5 > 0) {
                byteArrayOutputStream.write(bArr, 0, i5);
            }
        }
        inputStream.close();
        this.f66b = byteArrayOutputStream.toByteArray();
        int[] iArr = new int[readUnsignedShort(8)];
        this.items = iArr;
        int length = iArr.length;
        this.strings = new String[length];
        int i6 = 10;
        int i7 = 1;
        while (i7 < length) {
            int i8 = i6 + 1;
            this.items[i7] = i8;
            byte b = this.f66b[i6];
            if (b == 1) {
                int unsignedShort = readUnsignedShort(i8) + 3;
                i2 = i7;
                i3 = i;
                i4 = unsignedShort;
                if (unsignedShort > i) {
                    i3 = unsignedShort;
                    i4 = unsignedShort;
                    i2 = i7;
                }
            } else if (b != 15) {
                i2 = i7;
                i3 = i;
                i4 = 5;
                if (b != 18) {
                    i2 = i7;
                    i3 = i;
                    i4 = 5;
                    if (b != 3) {
                        i2 = i7;
                        i3 = i;
                        i4 = 5;
                        if (b != 4) {
                            if (b != 5 && b != 6) {
                                i2 = i7;
                                i3 = i;
                                i4 = 5;
                                switch (b) {
                                    case 9:
                                    case 10:
                                    case 11:
                                    case 12:
                                        break;
                                    default:
                                        i4 = 3;
                                        i2 = i7;
                                        i3 = i;
                                        break;
                                }
                            } else {
                                i4 = 9;
                                i2 = i7 + 1;
                                i3 = i;
                            }
                        }
                    }
                }
            } else {
                i4 = 4;
                i2 = i7;
                i3 = i;
            }
            i6 += i4;
            i7 = i2 + 1;
            i = i3;
        }
        this.maxStringLength = i;
        this.header = i6;
    }

    private int getAttributes() {
        int i = this.header;
        int unsignedShort = i + 8 + (readUnsignedShort(i + 6) * 2);
        for (int unsignedShort2 = readUnsignedShort(unsignedShort); unsignedShort2 > 0; unsignedShort2--) {
            for (int unsignedShort3 = readUnsignedShort(unsignedShort + 8); unsignedShort3 > 0; unsignedShort3--) {
                unsignedShort += readInt(unsignedShort + 12) + 6;
            }
            unsignedShort += 8;
        }
        int i2 = unsignedShort + 2;
        for (int unsignedShort4 = readUnsignedShort(i2); unsignedShort4 > 0; unsignedShort4--) {
            for (int unsignedShort5 = readUnsignedShort(i2 + 8); unsignedShort5 > 0; unsignedShort5--) {
                i2 += readInt(i2 + 12) + 6;
            }
            i2 += 8;
        }
        return i2 + 2;
    }

    private int readInt(int i) {
        byte[] bArr = this.f66b;
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    private int readMethod(TypeCollector typeCollector, char[] cArr, int i) {
        int i2;
        int unsignedShort = readUnsignedShort(i);
        String utf8 = readUTF8(i + 2, cArr);
        String utf82 = readUTF8(i + 4, cArr);
        int i3 = i + 8;
        int i4 = 0;
        int i5 = 0;
        for (int unsignedShort2 = readUnsignedShort(i + 6); unsignedShort2 > 0; unsignedShort2--) {
            String utf83 = readUTF8(i3, cArr);
            int i6 = readInt(i3 + 2);
            int i7 = i3 + 6;
            if (utf83.equals("Code")) {
                i5 = i7;
            }
            i3 = i7 + i6;
        }
        MethodCollector methodCollectorVisitMethod = typeCollector.visitMethod(unsignedShort, utf8, utf82);
        if (methodCollectorVisitMethod != null && i5 != 0) {
            int i8 = i5 + 8 + readInt(i5 + 4);
            int i9 = i8 + 2;
            for (int unsignedShort3 = readUnsignedShort(i8); unsignedShort3 > 0; unsignedShort3--) {
                i9 += 8;
            }
            int unsignedShort4 = readUnsignedShort(i9);
            int i10 = i9 + 2;
            int i11 = 0;
            while (true) {
                i2 = i4;
                if (unsignedShort4 <= 0) {
                    break;
                }
                String utf84 = readUTF8(i10, cArr);
                if (utf84.equals("LocalVariableTable")) {
                    i4 = i10 + 6;
                } else {
                    i4 = i2;
                    if (utf84.equals("LocalVariableTypeTable")) {
                        i11 = i10 + 6;
                        i4 = i2;
                    }
                }
                i10 += readInt(i10 + 2) + 6;
                unsignedShort4--;
            }
            if (i2 != 0) {
                if (i11 != 0) {
                    int unsignedShort5 = readUnsignedShort(i11) * 3;
                    int i12 = i11 + 2;
                    int[] iArr = new int[unsignedShort5];
                    while (unsignedShort5 > 0) {
                        int i13 = unsignedShort5 - 1;
                        iArr[i13] = i12 + 6;
                        int i14 = i13 - 1;
                        iArr[i14] = readUnsignedShort(i12 + 8);
                        unsignedShort5 = i14 - 1;
                        iArr[unsignedShort5] = readUnsignedShort(i12);
                        i12 += 10;
                    }
                }
                int i15 = i2 + 2;
                for (int unsignedShort6 = readUnsignedShort(i2); unsignedShort6 > 0; unsignedShort6--) {
                    methodCollectorVisitMethod.visitLocalVariable(readUTF8(i15 + 4, cArr), readUnsignedShort(i15 + 8));
                    i15 += 10;
                }
            }
        }
        return i3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v25 */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r0v34 */
    /* JADX WARN: Type inference failed for: r0v35 */
    /* JADX WARN: Type inference failed for: r0v39 */
    private String readUTF(int i, int i2, char[] cArr) {
        boolean z;
        int i3;
        byte[] bArr = this.f66b;
        int i4 = 0;
        boolean z2 = false;
        char c = 0;
        int i5 = i;
        while (true) {
            int i6 = i5;
            if (i6 >= i2 + i) {
                return new String(cArr, 0, i4);
            }
            byte b = bArr[i6];
            if (!z2) {
                int i7 = b & 255;
                if (i7 < 128) {
                    cArr[i4] = (char) i7;
                    i4++;
                    z = z2;
                } else if (i7 >= 224 || i7 <= 191) {
                    c = (char) (i7 & 15);
                    z = 2;
                } else {
                    i3 = i7 & 31;
                    c = (char) i3;
                    z = 1;
                }
            } else if (z2) {
                cArr[i4] = (char) ((b & 63) | (c << 6));
                i4++;
                z = 0;
            } else if (z2 != 2) {
                z = z2;
            } else {
                i3 = (b & 63) | (c << 6);
                c = (char) i3;
                z = 1;
            }
            z2 = z;
            i5 = i6 + 1;
        }
    }

    private String readUTF8(int i, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        String[] strArr = this.strings;
        String str = strArr[unsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.items[unsignedShort];
        String utf = readUTF(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[unsignedShort] = utf;
        return utf;
    }

    private int readUnsignedShort(int i) {
        byte[] bArr = this.f66b;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }

    public void accept(TypeCollector typeCollector) {
        int i;
        char[] cArr = new char[this.maxStringLength];
        if (this.readAnnotations) {
            int attributes = getAttributes();
            for (int unsignedShort = readUnsignedShort(attributes); unsignedShort > 0; unsignedShort--) {
                if ("RuntimeVisibleAnnotations".equals(readUTF8(attributes + 2, cArr))) {
                    i = attributes + 8;
                    break;
                }
                attributes += readInt(attributes + 4) + 6;
            }
            i = 0;
        } else {
            i = 0;
        }
        int i2 = this.header;
        int i3 = this.items[readUnsignedShort(i2 + 4)];
        int i4 = i2 + 8;
        for (int i5 = 0; i5 < readUnsignedShort(i2 + 6); i5++) {
            i4 += 2;
        }
        int i6 = i4 + 2;
        int i7 = i6;
        for (int unsignedShort2 = readUnsignedShort(i4); unsignedShort2 > 0; unsignedShort2--) {
            i7 += 8;
            for (int unsignedShort3 = readUnsignedShort(i7 + 6); unsignedShort3 > 0; unsignedShort3--) {
                i7 += readInt(i7 + 2) + 6;
            }
        }
        int i8 = i7 + 2;
        for (int unsignedShort4 = readUnsignedShort(i7); unsignedShort4 > 0; unsignedShort4--) {
            i8 += 8;
            for (int unsignedShort5 = readUnsignedShort(i8 + 6); unsignedShort5 > 0; unsignedShort5--) {
                i8 += readInt(i8 + 2) + 6;
            }
        }
        int i9 = i8 + 2;
        for (int unsignedShort6 = readUnsignedShort(i8); unsignedShort6 > 0; unsignedShort6--) {
            i9 += readInt(i9 + 2) + 6;
        }
        if (i != 0) {
            for (int unsignedShort7 = readUnsignedShort(i); unsignedShort7 > 0; unsignedShort7--) {
                typeCollector.visitAnnotation(readUTF8(i + 2, cArr));
            }
        }
        int i10 = i6;
        for (int unsignedShort8 = readUnsignedShort(i4); unsignedShort8 > 0; unsignedShort8--) {
            i10 += 8;
            for (int unsignedShort9 = readUnsignedShort(i10 + 6); unsignedShort9 > 0; unsignedShort9--) {
                i10 += readInt(i10 + 2) + 6;
            }
        }
        int method = i10 + 2;
        for (int unsignedShort10 = readUnsignedShort(i10); unsignedShort10 > 0; unsignedShort10--) {
            method = readMethod(typeCollector, cArr, method);
        }
    }
}
