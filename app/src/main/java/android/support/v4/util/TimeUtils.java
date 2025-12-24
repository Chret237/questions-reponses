package android.support.v4.util;

import java.io.PrintWriter;

/* loaded from: classes-dex2jar.jar:android/support/v4/util/TimeUtils.class */
public final class TimeUtils {
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final Object sFormatSync = new Object();
    private static char[] sFormatStr = new char[24];

    private TimeUtils() {
    }

    private static int accumField(int i, int i2, boolean z, int i3) {
        if (i > 99 || (z && i3 >= 3)) {
            return i2 + 3;
        }
        if (i > 9 || (z && i3 >= 2)) {
            return i2 + 2;
        }
        if (z || i > 0) {
            return i2 + 1;
        }
        return 0;
    }

    public static void formatDuration(long j, long j2, PrintWriter printWriter) {
        if (j == 0) {
            printWriter.print("--");
        } else {
            formatDuration(j - j2, printWriter, 0);
        }
    }

    public static void formatDuration(long j, PrintWriter printWriter) {
        formatDuration(j, printWriter, 0);
    }

    public static void formatDuration(long j, PrintWriter printWriter, int i) {
        synchronized (sFormatSync) {
            printWriter.print(new String(sFormatStr, 0, formatDurationLocked(j, i)));
        }
    }

    public static void formatDuration(long j, StringBuilder sb) {
        synchronized (sFormatSync) {
            sb.append(sFormatStr, 0, formatDurationLocked(j, 0));
        }
    }

    private static int formatDurationLocked(long j, int i) {
        char c;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (sFormatStr.length < i) {
            sFormatStr = new char[i];
        }
        char[] cArr = sFormatStr;
        if (j == 0) {
            while (i - 1 > 0) {
                cArr[0] = ' ';
            }
            cArr[0] = '0';
            return 1;
        }
        if (j > 0) {
            c = '+';
        } else {
            c = '-';
            j = -j;
        }
        int i7 = (int) (j % 1000);
        int iFloor = (int) Math.floor(j / 1000);
        if (iFloor > SECONDS_PER_DAY) {
            i2 = iFloor / SECONDS_PER_DAY;
            iFloor -= SECONDS_PER_DAY * i2;
        } else {
            i2 = 0;
        }
        if (iFloor > SECONDS_PER_HOUR) {
            i3 = iFloor / SECONDS_PER_HOUR;
            iFloor -= i3 * SECONDS_PER_HOUR;
        } else {
            i3 = 0;
        }
        if (iFloor > 60) {
            i4 = iFloor / 60;
            i5 = iFloor - (i4 * 60);
        } else {
            i4 = 0;
            i5 = iFloor;
        }
        if (i != 0) {
            int iAccumField = accumField(i2, 1, false, 0);
            int iAccumField2 = iAccumField + accumField(i3, 1, iAccumField > 0, 2);
            int iAccumField3 = iAccumField2 + accumField(i4, 1, iAccumField2 > 0, 2);
            int iAccumField4 = iAccumField3 + accumField(i5, 1, iAccumField3 > 0, 2);
            int iAccumField5 = iAccumField4 + accumField(i7, 2, true, iAccumField4 > 0 ? 3 : 0) + 1;
            int i8 = 0;
            while (true) {
                i6 = i8;
                if (iAccumField5 >= i) {
                    break;
                }
                cArr[i8] = ' ';
                i8++;
                iAccumField5++;
            }
        } else {
            i6 = 0;
        }
        cArr[i6] = c;
        int i9 = i6 + 1;
        boolean z = i != 0;
        int iPrintField = printField(cArr, i2, 'd', i9, false, 0);
        int iPrintField2 = printField(cArr, i3, 'h', iPrintField, iPrintField != i9, z ? 2 : 0);
        int iPrintField3 = printField(cArr, i4, 'm', iPrintField2, iPrintField2 != i9, z ? 2 : 0);
        int iPrintField4 = printField(cArr, i5, 's', iPrintField3, iPrintField3 != i9, z ? 2 : 0);
        int iPrintField5 = printField(cArr, i7, 'm', iPrintField4, true, (!z || iPrintField4 == i9) ? 0 : 3);
        cArr[iPrintField5] = 's';
        return iPrintField5 + 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x000c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int printField(char[] r5, int r6, char r7, int r8, boolean r9, int r10) {
        /*
            r0 = r9
            if (r0 != 0) goto Lc
            r0 = r8
            r11 = r0
            r0 = r6
            if (r0 <= 0) goto L91
        Lc:
            r0 = r9
            if (r0 == 0) goto L17
            r0 = r10
            r1 = 3
            if (r0 >= r1) goto L1d
        L17:
            r0 = r6
            r1 = 99
            if (r0 <= r1) goto L3c
        L1d:
            r0 = r6
            r1 = 100
            int r0 = r0 / r1
            r12 = r0
            r0 = r5
            r1 = r8
            r2 = r12
            r3 = 48
            int r2 = r2 + r3
            char r2 = (char) r2
            r0[r1] = r2
            r0 = r8
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
            r0 = r6
            r1 = r12
            r2 = 100
            int r1 = r1 * r2
            int r0 = r0 - r1
            r6 = r0
            goto L3f
        L3c:
            r0 = r8
            r11 = r0
        L3f:
            r0 = r9
            if (r0 == 0) goto L4a
            r0 = r10
            r1 = 2
            if (r0 >= r1) goto L5d
        L4a:
            r0 = r6
            r1 = 9
            if (r0 > r1) goto L5d
            r0 = r11
            r12 = r0
            r0 = r6
            r10 = r0
            r0 = r8
            r1 = r11
            if (r0 == r1) goto L79
        L5d:
            r0 = r6
            r1 = 10
            int r0 = r0 / r1
            r8 = r0
            r0 = r5
            r1 = r11
            r2 = r8
            r3 = 48
            int r2 = r2 + r3
            char r2 = (char) r2
            r0[r1] = r2
            r0 = r11
            r1 = 1
            int r0 = r0 + r1
            r12 = r0
            r0 = r6
            r1 = r8
            r2 = 10
            int r1 = r1 * r2
            int r0 = r0 - r1
            r10 = r0
        L79:
            r0 = r5
            r1 = r12
            r2 = r10
            r3 = 48
            int r2 = r2 + r3
            char r2 = (char) r2
            r0[r1] = r2
            r0 = r12
            r1 = 1
            int r0 = r0 + r1
            r6 = r0
            r0 = r5
            r1 = r6
            r2 = r7
            r0[r1] = r2
            r0 = r6
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
        L91:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.TimeUtils.printField(char[], int, char, int, boolean, int):int");
    }
}
