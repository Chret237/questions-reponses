package com.google.gson.internal.bind.util;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/bind/util/ISO8601Utils.class */
public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(UTC_ID);

    private static boolean checkOffset(String str, int i, char c) {
        return i < str.length() && str.charAt(i) == c;
    }

    public static String format(Date date) {
        return format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean z) {
        return format(date, z, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean z, TimeZone timeZone) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        StringBuilder sb = new StringBuilder(19 + (z ? 4 : 0) + (timeZone.getRawOffset() == 0 ? 1 : 6));
        padInt(sb, gregorianCalendar.get(1), 4);
        char c = '-';
        sb.append('-');
        padInt(sb, gregorianCalendar.get(2) + 1, 2);
        sb.append('-');
        padInt(sb, gregorianCalendar.get(5), 2);
        sb.append('T');
        padInt(sb, gregorianCalendar.get(11), 2);
        sb.append(':');
        padInt(sb, gregorianCalendar.get(12), 2);
        sb.append(':');
        padInt(sb, gregorianCalendar.get(13), 2);
        if (z) {
            sb.append('.');
            padInt(sb, gregorianCalendar.get(14), 3);
        }
        int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            int i = offset / 60000;
            int iAbs = Math.abs(i / 60);
            int iAbs2 = Math.abs(i % 60);
            if (offset >= 0) {
                c = '+';
            }
            sb.append(c);
            padInt(sb, iAbs, 2);
            sb.append(':');
            padInt(sb, iAbs2, 2);
        } else {
            sb.append('Z');
        }
        return sb.toString();
    }

    private static int indexOfNonDigit(String str, int i) {
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            if (cCharAt < '0' || cCharAt > '9') {
                return i;
            }
            i++;
        }
        return str.length();
    }

    private static void padInt(StringBuilder sb, int i, int i2) {
        String string = Integer.toString(i);
        for (int length = i2 - string.length(); length > 0; length--) {
            sb.append('0');
        }
        sb.append(string);
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x0355 A[Catch: IllegalArgumentException -> 0x0364, IllegalArgumentException -> 0x0364, NumberFormatException | IllegalArgumentException | IndexOutOfBoundsException -> 0x0369, NumberFormatException | IllegalArgumentException | IndexOutOfBoundsException -> 0x0369, IndexOutOfBoundsException -> 0x036e, IndexOutOfBoundsException -> 0x036e, TRY_ENTER, TRY_LEAVE, TryCatch #2 {NumberFormatException | IllegalArgumentException | IndexOutOfBoundsException -> 0x0369, blocks: (B:2:0x0000, B:4:0x000a, B:6:0x0016, B:11:0x002b, B:13:0x0037, B:18:0x004c, B:20:0x0064, B:22:0x006d, B:27:0x009a, B:29:0x00a6, B:34:0x00bb, B:36:0x00c7, B:40:0x00d7, B:42:0x00df, B:50:0x0100, B:56:0x011e, B:60:0x012f, B:73:0x01aa, B:75:0x01b3, B:78:0x01c0, B:107:0x02fa, B:107:0x02fa, B:108:0x02fd, B:85:0x01dd, B:86:0x020f, B:88:0x0211, B:93:0x0247, B:95:0x0259, B:98:0x0266, B:100:0x029f, B:103:0x02b3, B:104:0x02f1, B:106:0x02f5, B:91:0x0224, B:110:0x0355, B:110:0x0355, B:111:0x0358, B:112:0x0363), top: B:129:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01b3 A[Catch: IllegalArgumentException -> 0x0364, NumberFormatException | IllegalArgumentException | IndexOutOfBoundsException -> 0x0369, IndexOutOfBoundsException -> 0x036e, TRY_LEAVE, TryCatch #2 {NumberFormatException | IllegalArgumentException | IndexOutOfBoundsException -> 0x0369, blocks: (B:2:0x0000, B:4:0x000a, B:6:0x0016, B:11:0x002b, B:13:0x0037, B:18:0x004c, B:20:0x0064, B:22:0x006d, B:27:0x009a, B:29:0x00a6, B:34:0x00bb, B:36:0x00c7, B:40:0x00d7, B:42:0x00df, B:50:0x0100, B:56:0x011e, B:60:0x012f, B:73:0x01aa, B:75:0x01b3, B:78:0x01c0, B:107:0x02fa, B:107:0x02fa, B:108:0x02fd, B:85:0x01dd, B:86:0x020f, B:88:0x0211, B:93:0x0247, B:95:0x0259, B:98:0x0266, B:100:0x029f, B:103:0x02b3, B:104:0x02f1, B:106:0x02f5, B:91:0x0224, B:110:0x0355, B:110:0x0355, B:111:0x0358, B:112:0x0363), top: B:129:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.Date parse(java.lang.String r5, java.text.ParsePosition r6) throws java.text.ParseException {
        /*
            Method dump skipped, instructions count: 1063
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.util.ISO8601Utils.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    private static int parseInt(String str, int i, int i2) throws NumberFormatException {
        int i3;
        int i4;
        if (i < 0 || i2 > str.length() || i > i2) {
            throw new NumberFormatException(str);
        }
        if (i < i2) {
            i3 = i + 1;
            int iDigit = Character.digit(str.charAt(i), 10);
            if (iDigit < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i, i2));
            }
            i4 = -iDigit;
        } else {
            i3 = i;
            i4 = 0;
        }
        while (i3 < i2) {
            int iDigit2 = Character.digit(str.charAt(i3), 10);
            if (iDigit2 < 0) {
                throw new NumberFormatException("Invalid number: " + str.substring(i, i2));
            }
            i4 = (i4 * 10) - iDigit2;
            i3++;
        }
        return -i4;
    }
}
