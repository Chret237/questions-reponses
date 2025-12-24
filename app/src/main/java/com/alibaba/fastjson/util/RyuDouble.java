package com.alibaba.fastjson.util;

import com.github.clans.fab.BuildConfig;
import java.math.BigInteger;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/RyuDouble.class */
public final class RyuDouble {
    private static final int[][] POW5_SPLIT = new int[326][4];
    private static final int[][] POW5_INV_SPLIT = new int[291][4];

    static {
        BigInteger bigIntegerSubtract = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        BigInteger bigIntegerSubtract2 = BigInteger.ONE.shiftLeft(31).subtract(BigInteger.ONE);
        int i = 0;
        while (i < 326) {
            BigInteger bigIntegerPow = BigInteger.valueOf(5L).pow(i);
            int iBitLength = bigIntegerPow.bitLength();
            int i2 = i == 0 ? 1 : (int) ((((i * 23219280) + 10000000) - 1) / 10000000);
            if (i2 != iBitLength) {
                throw new IllegalStateException(iBitLength + " != " + i2);
            }
            if (i < POW5_SPLIT.length) {
                for (int i3 = 0; i3 < 4; i3++) {
                    POW5_SPLIT[i][i3] = bigIntegerPow.shiftRight((iBitLength - 121) + ((3 - i3) * 31)).and(bigIntegerSubtract).intValue();
                }
            }
            if (i < POW5_INV_SPLIT.length) {
                BigInteger bigIntegerAdd = BigInteger.ONE.shiftLeft(iBitLength + 121).divide(bigIntegerPow).add(BigInteger.ONE);
                for (int i4 = 0; i4 < 4; i4++) {
                    if (i4 == 0) {
                        POW5_INV_SPLIT[i][i4] = bigIntegerAdd.shiftRight((3 - i4) * 31).intValue();
                    } else {
                        POW5_INV_SPLIT[i][i4] = bigIntegerAdd.shiftRight((3 - i4) * 31).and(bigIntegerSubtract2).intValue();
                    }
                }
            }
            i++;
        }
    }

    public static int toString(double d, char[] cArr, int i) {
        int i2;
        long j;
        long j2;
        long j3;
        int iMax;
        boolean z;
        boolean z2;
        int i3;
        long j4;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        boolean z3;
        int i9;
        int i10;
        int i11;
        int i12;
        if (!Double.isNaN(d)) {
            if (d == Double.POSITIVE_INFINITY) {
                int i13 = i + 1;
                cArr[i] = 'I';
                int i14 = i13 + 1;
                cArr[i13] = 'n';
                int i15 = i14 + 1;
                cArr[i14] = 'f';
                int i16 = i15 + 1;
                cArr[i15] = 'i';
                int i17 = i16 + 1;
                cArr[i16] = 'n';
                int i18 = i17 + 1;
                cArr[i17] = 'i';
                int i19 = i18 + 1;
                cArr[i18] = 't';
                i8 = i19 + 1;
                cArr[i19] = 'y';
            } else if (d == Double.NEGATIVE_INFINITY) {
                int i20 = i + 1;
                cArr[i] = '-';
                int i21 = i20 + 1;
                cArr[i20] = 'I';
                int i22 = i21 + 1;
                cArr[i21] = 'n';
                int i23 = i22 + 1;
                cArr[i22] = 'f';
                int i24 = i23 + 1;
                cArr[i23] = 'i';
                int i25 = i24 + 1;
                cArr[i24] = 'n';
                int i26 = i25 + 1;
                cArr[i25] = 'i';
                int i27 = i26 + 1;
                cArr[i26] = 't';
                i12 = i27 + 1;
                cArr[i27] = 'y';
            } else {
                long jDoubleToLongBits = Double.doubleToLongBits(d);
                if (jDoubleToLongBits == 0) {
                    int i28 = i + 1;
                    cArr[i] = '0';
                    int i29 = i28 + 1;
                    cArr[i28] = '.';
                    i12 = i29 + 1;
                    cArr[i29] = '0';
                } else if (jDoubleToLongBits == Long.MIN_VALUE) {
                    int i30 = i + 1;
                    cArr[i] = '-';
                    int i31 = i30 + 1;
                    cArr[i30] = '0';
                    int i32 = i31 + 1;
                    cArr[i31] = '.';
                    i8 = i32 + 1;
                    cArr[i32] = '0';
                } else {
                    int i33 = (int) ((jDoubleToLongBits >>> 52) & 2047);
                    long j5 = 4503599627370495L & jDoubleToLongBits;
                    if (i33 == 0) {
                        i2 = -1074;
                    } else {
                        i2 = (i33 - 1023) - 52;
                        j5 |= 4503599627370496L;
                    }
                    boolean z4 = jDoubleToLongBits < 0;
                    boolean z5 = (j5 & 1) == 0;
                    long j6 = 4 * j5;
                    long j7 = j6 + 2;
                    int i34 = (j5 != 4503599627370496L || i33 <= 1) ? 1 : 0;
                    long j8 = (j6 - 1) - i34;
                    int i35 = i2 - 2;
                    if (i35 >= 0) {
                        iMax = Math.max(0, ((int) ((i35 * 3010299) / 10000000)) - 1);
                        int i36 = ((((-i35) + iMax) + (((iMax == 0 ? 1 : (int) ((((iMax * 23219280) + 10000000) - 1) / 10000000)) + 122) - 1)) - 93) - 21;
                        if (i36 < 0) {
                            throw new IllegalArgumentException(BuildConfig.FLAVOR + i36);
                        }
                        int[] iArr = POW5_INV_SPLIT[iMax];
                        long j9 = j6 >>> 31;
                        long j10 = j6 & 2147483647L;
                        long j11 = iArr[0];
                        long j12 = iArr[0];
                        long j13 = iArr[1];
                        long j14 = iArr[1];
                        long j15 = iArr[2];
                        long j16 = iArr[2];
                        long j17 = iArr[3];
                        long j18 = iArr[3];
                        long j19 = j7 >>> 31;
                        long j20 = j7 & 2147483647L;
                        long j21 = iArr[0];
                        long j22 = iArr[0];
                        long j23 = iArr[1];
                        long j24 = iArr[1];
                        long j25 = iArr[2];
                        long j26 = iArr[2];
                        long j27 = iArr[3];
                        long j28 = iArr[3];
                        long j29 = j8 >>> 31;
                        long j30 = j8 & 2147483647L;
                        j2 = ((((((((((((j28 * j20) >>> 31) + (j26 * j20)) + (j19 * j27)) >>> 31) + (j24 * j20)) + (j25 * j19)) >>> 31) + (j22 * j20)) + (j23 * j19)) >>> 21) + ((j21 * j19) << 10)) >>> i36;
                        long j31 = ((((((((((((j30 * iArr[3]) >>> 31) + (iArr[2] * j30)) + (j29 * iArr[3])) >>> 31) + (iArr[1] * j30)) + (iArr[2] * j29)) >>> 31) + (iArr[0] * j30)) + (iArr[1] * j29)) >>> 21) + ((iArr[0] * j29) << 10)) >>> i36;
                        long j32 = j2;
                        if (iMax <= 21) {
                            long j33 = j6 % 5;
                            if (j33 == 0) {
                                if (j33 != 0) {
                                    i11 = 0;
                                } else if (j6 % 25 != 0) {
                                    i11 = 1;
                                } else if (j6 % 125 != 0) {
                                    i11 = 2;
                                } else if (j6 % 625 != 0) {
                                    i11 = 3;
                                } else {
                                    long j34 = j6 / 625;
                                    int i37 = 4;
                                    while (true) {
                                        i11 = i37;
                                        if (j34 <= 0) {
                                            break;
                                        }
                                        if (j34 % 5 != 0) {
                                            i11 = i37;
                                            break;
                                        }
                                        j34 /= 5;
                                        i37++;
                                    }
                                }
                                z3 = i11 >= iMax;
                                z = false;
                                j3 = j31;
                                z2 = z3;
                                j = ((((((((((((j10 * j18) >>> 31) + (j16 * j10)) + (j17 * j9)) >>> 31) + (j14 * j10)) + (j15 * j9)) >>> 31) + (j12 * j10)) + (j13 * j9)) >>> 21) + ((j11 * j9) << 10)) >>> i36;
                            } else {
                                if (z5) {
                                    if (j8 % 5 != 0) {
                                        i10 = 0;
                                    } else if (j8 % 25 != 0) {
                                        i10 = 1;
                                    } else if (j8 % 125 != 0) {
                                        i10 = 2;
                                    } else if (j8 % 625 != 0) {
                                        i10 = 3;
                                    } else {
                                        long j35 = j8 / 625;
                                        int i38 = 4;
                                        while (true) {
                                            i10 = i38;
                                            if (j35 <= 0) {
                                                break;
                                            }
                                            if (j35 % 5 != 0) {
                                                i10 = i38;
                                                break;
                                            }
                                            j35 /= 5;
                                            i38++;
                                        }
                                    }
                                    j32 = j2;
                                    z = i10 >= iMax;
                                    z3 = false;
                                    j3 = j31;
                                    z2 = z3;
                                    j = ((((((((((((j10 * j18) >>> 31) + (j16 * j10)) + (j17 * j9)) >>> 31) + (j14 * j10)) + (j15 * j9)) >>> 31) + (j12 * j10)) + (j13 * j9)) >>> 21) + ((j11 * j9) << 10)) >>> i36;
                                } else {
                                    if (j7 % 5 != 0) {
                                        i9 = 0;
                                    } else if (j7 % 25 != 0) {
                                        i9 = 1;
                                    } else if (j7 % 125 != 0) {
                                        i9 = 2;
                                    } else if (j7 % 625 != 0) {
                                        i9 = 3;
                                    } else {
                                        long j36 = j7 / 625;
                                        int i39 = 4;
                                        while (true) {
                                            i9 = i39;
                                            if (j36 <= 0) {
                                                break;
                                            }
                                            if (j36 % 5 != 0) {
                                                i9 = i39;
                                                break;
                                            }
                                            j36 /= 5;
                                            i39++;
                                        }
                                    }
                                    j32 = j2;
                                    if (i9 >= iMax) {
                                        j32 = j2 - 1;
                                    }
                                }
                                j2 = j32;
                                z3 = false;
                                j3 = j31;
                                z2 = z3;
                                j = ((((((((((((j10 * j18) >>> 31) + (j16 * j10)) + (j17 * j9)) >>> 31) + (j14 * j10)) + (j15 * j9)) >>> 31) + (j12 * j10)) + (j13 * j9)) >>> 21) + ((j11 * j9) << 10)) >>> i36;
                            }
                        } else {
                            j2 = j32;
                            z3 = false;
                            j3 = j31;
                            z2 = z3;
                            j = ((((((((((((j10 * j18) >>> 31) + (j16 * j10)) + (j17 * j9)) >>> 31) + (j14 * j10)) + (j15 * j9)) >>> 31) + (j12 * j10)) + (j13 * j9)) >>> 21) + ((j11 * j9) << 10)) >>> i36;
                        }
                    } else {
                        int i40 = -i35;
                        int iMax2 = Math.max(0, ((int) ((i40 * 6989700) / 10000000)) - 1);
                        int i41 = i40 - iMax2;
                        int i42 = ((iMax2 - ((i41 == 0 ? 1 : (int) ((((i41 * 23219280) + 10000000) - 1) / 10000000)) - 121)) - 93) - 21;
                        if (i42 < 0) {
                            throw new IllegalArgumentException(BuildConfig.FLAVOR + i42);
                        }
                        int[] iArr2 = POW5_SPLIT[i41];
                        long j37 = j6 >>> 31;
                        long j38 = j6 & 2147483647L;
                        long j39 = iArr2[0];
                        long j40 = iArr2[0];
                        long j41 = iArr2[1];
                        long j42 = iArr2[1];
                        long j43 = iArr2[2];
                        long j44 = iArr2[2];
                        long j45 = iArr2[3];
                        long j46 = iArr2[3];
                        long j47 = j7 >>> 31;
                        long j48 = j7 & 2147483647L;
                        j = ((((((((((((j38 * j46) >>> 31) + (j44 * j38)) + (j37 * j45)) >>> 31) + (j42 * j38)) + (j43 * j37)) >>> 31) + (j40 * j38)) + (j41 * j37)) >>> 21) + ((j39 * j37) << 10)) >>> i42;
                        j2 = ((((((((((((iArr2[3] * j48) >>> 31) + (iArr2[2] * j48)) + (j47 * iArr2[3])) >>> 31) + (iArr2[1] * j48)) + (iArr2[2] * j47)) >>> 31) + (iArr2[0] * j48)) + (iArr2[1] * j47)) >>> 21) + ((iArr2[0] * j47) << 10)) >>> i42;
                        long j49 = j8 >>> 31;
                        long j50 = j8 & 2147483647L;
                        j3 = ((((((((((((j50 * iArr2[3]) >>> 31) + (iArr2[2] * j50)) + (j49 * iArr2[3])) >>> 31) + (iArr2[1] * j50)) + (iArr2[2] * j49)) >>> 31) + (iArr2[0] * j50)) + (iArr2[1] * j49)) >>> 21) + ((iArr2[0] * j49) << 10)) >>> i42;
                        iMax = iMax2 + i35;
                        if (iMax2 <= 1) {
                            if (z5) {
                                z = i34 == 1;
                            } else {
                                j2--;
                                z = false;
                            }
                            z2 = true;
                        } else if (iMax2 < 63) {
                            z = false;
                            z2 = (j6 & ((1 << (iMax2 - 1)) - 1)) == 0;
                        } else {
                            z = false;
                            z2 = false;
                        }
                    }
                    int i43 = j2 >= 1000000000000000000L ? 19 : j2 >= 100000000000000000L ? 18 : j2 >= 10000000000000000L ? 17 : j2 >= 1000000000000000L ? 16 : j2 >= 100000000000000L ? 15 : j2 >= 10000000000000L ? 14 : j2 >= 1000000000000L ? 13 : j2 >= 100000000000L ? 12 : j2 >= 10000000000L ? 11 : j2 >= 1000000000 ? 10 : j2 >= 100000000 ? 9 : j2 >= 10000000 ? 8 : j2 >= 1000000 ? 7 : j2 >= 100000 ? 6 : j2 >= 10000 ? 5 : j2 >= 1000 ? 4 : j2 >= 100 ? 3 : j2 >= 10 ? 2 : 1;
                    int i44 = (iMax + i43) - 1;
                    boolean z6 = i44 < -3 || i44 >= 7;
                    if (z || z2) {
                        int i45 = 0;
                        int i46 = 0;
                        long j51 = j2;
                        long j52 = j;
                        boolean z7 = z;
                        while (true) {
                            long j53 = j51 / 10;
                            long j54 = j3 / 10;
                            if (j53 <= j54 || (j51 < 100 && z6)) {
                                break;
                            }
                            z7 &= j3 % 10 == 0;
                            z2 &= i45 == 0;
                            i45 = (int) (j52 % 10);
                            j52 /= 10;
                            i46++;
                            j51 = j53;
                            j3 = j54;
                        }
                        long j55 = j3;
                        boolean z8 = z2;
                        int i47 = i45;
                        i3 = i46;
                        long j56 = j52;
                        if (z7) {
                            j55 = j3;
                            z8 = z2;
                            i47 = i45;
                            i3 = i46;
                            j56 = j52;
                            if (z5) {
                                while (true) {
                                    j55 = j3;
                                    z8 = z2;
                                    i47 = i45;
                                    i3 = i46;
                                    j56 = j52;
                                    if (j3 % 10 != 0) {
                                        break;
                                    }
                                    if (j51 < 100 && z6) {
                                        j55 = j3;
                                        z8 = z2;
                                        i47 = i45;
                                        i3 = i46;
                                        j56 = j52;
                                        break;
                                    }
                                    z2 &= i45 == 0;
                                    i45 = (int) (j52 % 10);
                                    j51 /= 10;
                                    j52 /= 10;
                                    j3 /= 10;
                                    i46++;
                                }
                            }
                        }
                        int i48 = i47;
                        if (z8) {
                            i48 = i47;
                            if (i47 == 5) {
                                i48 = i47;
                                if (j56 % 2 == 0) {
                                    i48 = 4;
                                }
                            }
                        }
                        j4 = j56 + (((j56 != j55 || (z7 && z5)) && i48 < 5) ? 0 : 1);
                    } else {
                        i3 = 0;
                        int i49 = 0;
                        while (true) {
                            long j57 = j2 / 10;
                            long j58 = j3 / 10;
                            if (j57 <= j58 || (j2 < 100 && z6)) {
                                break;
                            }
                            i49 = (int) (j % 10);
                            j /= 10;
                            i3++;
                            j2 = j57;
                            j3 = j58;
                        }
                        j4 = j + ((j == j3 || i49 >= 5) ? 1 : 0);
                    }
                    int i50 = i43 - i3;
                    if (z4) {
                        i4 = i + 1;
                        cArr[i] = '-';
                    } else {
                        i4 = i;
                    }
                    if (!z6) {
                        if (i44 < 0) {
                            int i51 = i4 + 1;
                            cArr[i4] = '0';
                            int i52 = i51 + 1;
                            cArr[i51] = '.';
                            int i53 = -1;
                            while (i53 > i44) {
                                cArr[i52] = '0';
                                i53--;
                                i52++;
                            }
                            int i54 = i52;
                            int i55 = 0;
                            while (true) {
                                i5 = i54;
                                if (i55 >= i50) {
                                    break;
                                }
                                cArr[((i52 + i50) - i55) - 1] = (char) ((j4 % 10) + 48);
                                j4 /= 10;
                                i54++;
                                i55++;
                            }
                        } else {
                            int i56 = i44 + 1;
                            if (i56 >= i50) {
                                for (int i57 = 0; i57 < i50; i57++) {
                                    cArr[((i4 + i50) - i57) - 1] = (char) ((j4 % 10) + 48);
                                    j4 /= 10;
                                }
                                int i58 = i4 + i50;
                                int i59 = i50;
                                while (i59 < i56) {
                                    cArr[i58] = '0';
                                    i59++;
                                    i58++;
                                }
                                int i60 = i58 + 1;
                                cArr[i58] = '.';
                                i5 = i60 + 1;
                                cArr[i60] = '0';
                            } else {
                                int i61 = i4 + 1;
                                for (int i62 = 0; i62 < i50; i62++) {
                                    if ((i50 - i62) - 1 == i44) {
                                        cArr[((i61 + i50) - i62) - 1] = '.';
                                        i61--;
                                    }
                                    cArr[((i61 + i50) - i62) - 1] = (char) ((j4 % 10) + 48);
                                    j4 /= 10;
                                }
                                i5 = i4 + i50 + 1;
                            }
                        }
                        return i5 - i;
                    }
                    for (int i63 = 0; i63 < i50 - 1; i63++) {
                        int i64 = (int) (j4 % 10);
                        j4 /= 10;
                        cArr[(i4 + i50) - i63] = (char) (i64 + 48);
                    }
                    cArr[i4] = (char) ((j4 % 10) + 48);
                    cArr[i4 + 1] = '.';
                    int i65 = i4 + i50 + 1;
                    int i66 = i65;
                    if (i50 == 1) {
                        cArr[i65] = '0';
                        i66 = i65 + 1;
                    }
                    int i67 = i66 + 1;
                    cArr[i66] = 'E';
                    int i68 = i67;
                    int i69 = i44;
                    if (i44 < 0) {
                        cArr[i67] = '-';
                        i69 = -i44;
                        i68 = i67 + 1;
                    }
                    if (i69 >= 100) {
                        int i70 = i68 + 1;
                        cArr[i68] = (char) ((i69 / 100) + 48);
                        i7 = i69 % 100;
                        i6 = i70 + 1;
                        cArr[i70] = (char) ((i7 / 10) + 48);
                    } else {
                        i6 = i68;
                        i7 = i69;
                        if (i69 >= 10) {
                            cArr[i68] = (char) ((i69 / 10) + 48);
                            i6 = i68 + 1;
                            i7 = i69;
                        }
                    }
                    i8 = i6 + 1;
                    cArr[i6] = (char) ((i7 % 10) + 48);
                }
            }
            return i8 - i;
        }
        int i71 = i + 1;
        cArr[i] = 'N';
        int i72 = i71 + 1;
        cArr[i71] = 'a';
        i12 = i72 + 1;
        cArr[i72] = 'N';
        return i12 - i;
    }

    public static String toString(double d) {
        char[] cArr = new char[24];
        return new String(cArr, 0, toString(d, cArr, 0));
    }
}
