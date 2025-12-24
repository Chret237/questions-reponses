package com.alibaba.fastjson.util;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/RyuFloat.class */
public final class RyuFloat {
    private static final int[][] POW5_SPLIT = {new int[]{536870912, 0}, new int[]{671088640, 0}, new int[]{838860800, 0}, new int[]{1048576000, 0}, new int[]{655360000, 0}, new int[]{819200000, 0}, new int[]{1024000000, 0}, new int[]{640000000, 0}, new int[]{800000000, 0}, new int[]{1000000000, 0}, new int[]{625000000, 0}, new int[]{781250000, 0}, new int[]{976562500, 0}, new int[]{610351562, 1073741824}, new int[]{762939453, 268435456}, new int[]{953674316, 872415232}, new int[]{596046447, 1619001344}, new int[]{745058059, 1486880768}, new int[]{931322574, 1321730048}, new int[]{582076609, 289210368}, new int[]{727595761, 898383872}, new int[]{909494701, 1659850752}, new int[]{568434188, 1305842176}, new int[]{710542735, 1632302720}, new int[]{888178419, 1503507488}, new int[]{555111512, 671256724}, new int[]{693889390, 839070905}, new int[]{867361737, 2122580455}, new int[]{542101086, 521306416}, new int[]{677626357, 1725374844}, new int[]{847032947, 546105819}, new int[]{1058791184, 145761362}, new int[]{661744490, 91100851}, new int[]{827180612, 1187617888}, new int[]{1033975765, 1484522360}, new int[]{646234853, 1196261931}, new int[]{807793566, 2032198326}, new int[]{1009741958, 1466506084}, new int[]{631088724, 379695390}, new int[]{788860905, 474619238}, new int[]{986076131, 1130144959}, new int[]{616297582, 437905143}, new int[]{770371977, 1621123253}, new int[]{962964972, 415791331}, new int[]{601853107, 1333611405}, new int[]{752316384, 1130143345}, new int[]{940395480, 1412679181}};
    private static final int[][] POW5_INV_SPLIT = {new int[]{268435456, 1}, new int[]{214748364, 1717986919}, new int[]{171798691, 1803886265}, new int[]{137438953, 1013612282}, new int[]{219902325, 1192282922}, new int[]{175921860, 953826338}, new int[]{140737488, 763061070}, new int[]{225179981, 791400982}, new int[]{180143985, 203624056}, new int[]{144115188, 162899245}, new int[]{230584300, 1978625710}, new int[]{184467440, 1582900568}, new int[]{147573952, 1266320455}, new int[]{236118324, 308125809}, new int[]{188894659, 675997377}, new int[]{151115727, 970294631}, new int[]{241785163, 1981968139}, new int[]{193428131, 297084323}, new int[]{154742504, 1955654377}, new int[]{247588007, 1840556814}, new int[]{198070406, 613451992}, new int[]{158456325, 61264864}, new int[]{253530120, 98023782}, new int[]{202824096, 78419026}, new int[]{162259276, 1780722139}, new int[]{259614842, 1990161963}, new int[]{207691874, 733136111}, new int[]{166153499, 1016005619}, new int[]{265845599, 337118801}, new int[]{212676479, 699191770}, new int[]{170141183, 988850146}};

    public static int toString(float f, char[] cArr, int i) {
        int i2;
        int i3;
        boolean z;
        int i4;
        int i5;
        int i6;
        boolean z2;
        boolean z3;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        if (Float.isNaN(f)) {
            int i12 = i + 1;
            cArr[i] = 'N';
            int i13 = i12 + 1;
            cArr[i12] = 'a';
            i11 = i13 + 1;
            cArr[i13] = 'N';
        } else {
            if (f == Float.POSITIVE_INFINITY) {
                int i14 = i + 1;
                cArr[i] = 'I';
                int i15 = i14 + 1;
                cArr[i14] = 'n';
                int i16 = i15 + 1;
                cArr[i15] = 'f';
                int i17 = i16 + 1;
                cArr[i16] = 'i';
                int i18 = i17 + 1;
                cArr[i17] = 'n';
                int i19 = i18 + 1;
                cArr[i18] = 'i';
                int i20 = i19 + 1;
                cArr[i19] = 't';
                cArr[i20] = 'y';
                return (i20 + 1) - i;
            }
            if (f == Float.NEGATIVE_INFINITY) {
                int i21 = i + 1;
                cArr[i] = '-';
                int i22 = i21 + 1;
                cArr[i21] = 'I';
                int i23 = i22 + 1;
                cArr[i22] = 'n';
                int i24 = i23 + 1;
                cArr[i23] = 'f';
                int i25 = i24 + 1;
                cArr[i24] = 'i';
                int i26 = i25 + 1;
                cArr[i25] = 'n';
                int i27 = i26 + 1;
                cArr[i26] = 'i';
                int i28 = i27 + 1;
                cArr[i27] = 't';
                i11 = i28 + 1;
                cArr[i28] = 'y';
            } else {
                int iFloatToIntBits = Float.floatToIntBits(f);
                if (iFloatToIntBits != 0) {
                    if (iFloatToIntBits == Integer.MIN_VALUE) {
                        int i29 = i + 1;
                        cArr[i] = '-';
                        int i30 = i29 + 1;
                        cArr[i29] = '0';
                        int i31 = i30 + 1;
                        cArr[i30] = '.';
                        cArr[i31] = '0';
                        return (i31 + 1) - i;
                    }
                    int i32 = (iFloatToIntBits >> 23) & 255;
                    int i33 = 8388607 & iFloatToIntBits;
                    if (i32 == 0) {
                        i2 = -149;
                    } else {
                        i2 = (i32 - 127) - 23;
                        i33 |= 8388608;
                    }
                    boolean z4 = iFloatToIntBits < 0;
                    boolean z5 = (i33 & 1) == 0;
                    int i34 = i33 * 4;
                    int i35 = i34 + 2;
                    int i36 = i34 - ((((long) i33) != 8388608 || i32 <= 1) ? 2 : 1);
                    int i37 = i2 - 2;
                    if (i37 >= 0) {
                        int i38 = (int) ((i37 * 3010299) / 10000000);
                        int i39 = i38 == 0 ? 1 : (int) ((((i38 * 23219280) + 10000000) - 1) / 10000000);
                        int i40 = (-i37) + i38;
                        int[][] iArr = POW5_INV_SPLIT;
                        long j = iArr[i38][0];
                        long j2 = iArr[i38][1];
                        long j3 = i34;
                        int i41 = (((i39 + 59) - 1) + i40) - 31;
                        int i42 = (int) (((j3 * j) + ((j3 * j2) >> 31)) >> i41);
                        long j4 = i35;
                        int i43 = (int) (((j4 * j) + ((j4 * j2) >> 31)) >> i41);
                        long j5 = i36;
                        int i44 = (int) (((j * j5) + ((j5 * j2) >> 31)) >> i41);
                        if (i38 == 0 || (i43 - 1) / 10 > i44 / 10) {
                            i3 = 0;
                        } else {
                            int i45 = i38 - 1;
                            int i46 = i45 == 0 ? 1 : (int) ((((i45 * 23219280) + 10000000) - 1) / 10000000);
                            int[][] iArr2 = POW5_INV_SPLIT;
                            i3 = (int) ((((iArr2[i45][0] * j3) + ((iArr2[i45][1] * j3) >> 31)) >> (((i40 - 1) + ((i46 + 59) - 1)) - 31)) % 10);
                        }
                        int i47 = 0;
                        int i48 = i35;
                        while (i48 > 0 && i48 % 5 == 0) {
                            i48 /= 5;
                            i47++;
                        }
                        int i49 = i34;
                        int i50 = 0;
                        while (i49 > 0 && i49 % 5 == 0) {
                            i49 /= 5;
                            i50++;
                        }
                        int i51 = 0;
                        int i52 = i36;
                        while (i52 > 0 && i52 % 5 == 0) {
                            i52 /= 5;
                            i51++;
                        }
                        boolean z6 = i47 >= i38;
                        boolean z7 = i50 >= i38;
                        if (i51 >= i38) {
                            i6 = i44;
                            z = z6;
                            i4 = i42;
                            i7 = i38;
                            i5 = i43;
                            z2 = z7;
                            z3 = true;
                        } else {
                            i6 = i44;
                            z = z6;
                            i4 = i42;
                            i7 = i38;
                            i5 = i43;
                            z2 = z7;
                            z3 = false;
                        }
                    } else {
                        int i53 = -i37;
                        int i54 = (int) ((i53 * 6989700) / 10000000);
                        int i55 = i53 - i54;
                        int i56 = i55 == 0 ? 1 : (int) ((((i55 * 23219280) + 10000000) - 1) / 10000000);
                        int[][] iArr3 = POW5_SPLIT;
                        long j6 = iArr3[i55][0];
                        long j7 = iArr3[i55][1];
                        int i57 = (i54 - (i56 - 61)) - 31;
                        long j8 = i34;
                        int i58 = (int) (((j8 * j6) + ((j8 * j7) >> 31)) >> i57);
                        long j9 = i35;
                        int i59 = (int) (((j9 * j6) + ((j9 * j7) >> 31)) >> i57);
                        long j10 = i36;
                        int i60 = (int) (((j10 * j6) + ((j10 * j7) >> 31)) >> i57);
                        if (i54 == 0 || (i59 - 1) / 10 > i60 / 10) {
                            i3 = 0;
                        } else {
                            int i61 = i55 + 1;
                            int i62 = i61 == 0 ? 1 : (int) ((((i61 * 23219280) + 10000000) - 1) / 10000000);
                            int[][] iArr4 = POW5_SPLIT;
                            i3 = (int) ((((iArr4[i61][0] * j8) + ((j8 * iArr4[i61][1]) >> 31)) >> (((i54 - 1) - (i62 - 61)) - 31)) % 10);
                        }
                        int i63 = i54 + i37;
                        boolean z8 = 1 >= i54;
                        z = z8;
                        i4 = i58;
                        i5 = i59;
                        i6 = i60;
                        z2 = i54 < 23 && (i34 & ((1 << (i54 - 1)) - 1)) == 0;
                        z3 = (i36 % 2 == 1 ? 0 : 1) >= i54;
                        i7 = i63;
                    }
                    int i64 = 1000000000;
                    int i65 = 10;
                    while (i65 > 0 && i5 < i64) {
                        i64 /= 10;
                        i65--;
                    }
                    int i66 = (i7 + i65) - 1;
                    boolean z9 = i66 < -3 || i66 >= 7;
                    int i67 = i5;
                    if (z) {
                        i67 = i5;
                        if (!z5) {
                            i67 = i5 - 1;
                        }
                    }
                    int i68 = 0;
                    boolean z10 = z3;
                    int i69 = i4;
                    while (true) {
                        int i70 = i67 / 10;
                        int i71 = i6 / 10;
                        if (i70 <= i71 || (i67 < 100 && z9)) {
                            break;
                        }
                        z10 &= i6 % 10 == 0;
                        i3 = i69 % 10;
                        i69 /= 10;
                        i68++;
                        i67 = i70;
                        i6 = i71;
                    }
                    int i72 = i3;
                    int i73 = i6;
                    int i74 = i69;
                    int i75 = i68;
                    if (z10) {
                        i72 = i3;
                        i73 = i6;
                        i74 = i69;
                        i75 = i68;
                        if (z5) {
                            while (true) {
                                i72 = i3;
                                i73 = i6;
                                i74 = i69;
                                i75 = i68;
                                if (i6 % 10 != 0) {
                                    break;
                                }
                                if (i67 < 100 && z9) {
                                    i72 = i3;
                                    i73 = i6;
                                    i74 = i69;
                                    i75 = i68;
                                    break;
                                }
                                i67 /= 10;
                                i3 = i69 % 10;
                                i69 /= 10;
                                i6 /= 10;
                                i68++;
                            }
                        }
                    }
                    int i76 = i72;
                    if (z2) {
                        i76 = i72;
                        if (i72 == 5) {
                            i76 = i72;
                            if (i74 % 2 == 0) {
                                i76 = 4;
                            }
                        }
                    }
                    int i77 = i74 + (((i74 != i73 || (z10 && z5)) && i76 < 5) ? 0 : 1);
                    int i78 = i65 - i75;
                    if (z4) {
                        i8 = i + 1;
                        cArr[i] = '-';
                    } else {
                        i8 = i;
                    }
                    if (z9) {
                        int i79 = 0;
                        while (true) {
                            i10 = i77;
                            if (i79 >= i78 - 1) {
                                break;
                            }
                            i77 = i10 / 10;
                            cArr[(i8 + i78) - i79] = (char) ((i10 % 10) + 48);
                            i79++;
                        }
                        cArr[i8] = (char) ((i10 % 10) + 48);
                        cArr[i8 + 1] = '.';
                        int i80 = i8 + i78 + 1;
                        int i81 = i80;
                        if (i78 == 1) {
                            cArr[i80] = '0';
                            i81 = i80 + 1;
                        }
                        int i82 = i81 + 1;
                        cArr[i81] = 'E';
                        int i83 = i82;
                        int i84 = i66;
                        if (i66 < 0) {
                            cArr[i82] = '-';
                            i84 = -i66;
                            i83 = i82 + 1;
                        }
                        if (i84 >= 10) {
                            cArr[i83] = (char) ((i84 / 10) + 48);
                            i83++;
                        }
                        cArr[i83] = (char) ((i84 % 10) + 48);
                        i9 = i83 + 1;
                    } else if (i66 < 0) {
                        int i85 = i8 + 1;
                        cArr[i8] = '0';
                        int i86 = i85 + 1;
                        cArr[i85] = '.';
                        int i87 = -1;
                        while (i87 > i66) {
                            cArr[i86] = '0';
                            i87--;
                            i86++;
                        }
                        int i88 = i77;
                        i9 = i86;
                        for (int i89 = 0; i89 < i78; i89++) {
                            cArr[((i86 + i78) - i89) - 1] = (char) ((i88 % 10) + 48);
                            i88 /= 10;
                            i9++;
                        }
                    } else {
                        int i90 = i66 + 1;
                        if (i90 >= i78) {
                            for (int i91 = 0; i91 < i78; i91++) {
                                cArr[((i8 + i78) - i91) - 1] = (char) ((i77 % 10) + 48);
                                i77 /= 10;
                            }
                            int i92 = i8 + i78;
                            int i93 = i78;
                            while (i93 < i90) {
                                cArr[i92] = '0';
                                i93++;
                                i92++;
                            }
                            int i94 = i92 + 1;
                            cArr[i92] = '.';
                            i9 = i94 + 1;
                            cArr[i94] = '0';
                        } else {
                            int i95 = i8 + 1;
                            for (int i96 = 0; i96 < i78; i96++) {
                                if ((i78 - i96) - 1 == i66) {
                                    cArr[((i95 + i78) - i96) - 1] = '.';
                                    i95--;
                                }
                                cArr[((i95 + i78) - i96) - 1] = (char) ((i77 % 10) + 48);
                                i77 /= 10;
                            }
                            i9 = i8 + i78 + 1;
                        }
                    }
                    return i9 - i;
                }
                int i97 = i + 1;
                cArr[i] = '0';
                int i98 = i97 + 1;
                cArr[i97] = '.';
                i11 = i98 + 1;
                cArr[i98] = '0';
            }
        }
        return i11 - i;
    }

    public static String toString(float f) {
        char[] cArr = new char[15];
        return new String(cArr, 0, toString(f, cArr, 0));
    }
}
