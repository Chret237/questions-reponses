package android.support.v4.os;

import android.os.Build;
import android.support.v7.widget.ActivityChooserView;
import com.github.clans.fab.BuildConfig;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/LocaleListHelper.class */
final class LocaleListHelper {
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    private final Locale[] mList;
    private final String mStringRepresentation;
    private static final Locale[] sEmptyList = new Locale[0];
    private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
    private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
    private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
    private static final Locale EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
    private static final Object sLock = new Object();
    private static LocaleListHelper sLastExplicitlySetLocaleList = null;
    private static LocaleListHelper sDefaultLocaleList = null;
    private static LocaleListHelper sDefaultAdjustedLocaleList = null;
    private static Locale sLastDefaultLocale = null;

    LocaleListHelper(Locale locale, LocaleListHelper localeListHelper) {
        if (locale == null) {
            throw new NullPointerException("topLocale is null");
        }
        int length = localeListHelper == null ? 0 : localeListHelper.mList.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (locale.equals(localeListHelper.mList[i])) {
                break;
            } else {
                i++;
            }
        }
        int i2 = (i == -1 ? 1 : 0) + length;
        Locale[] localeArr = new Locale[i2];
        localeArr[0] = (Locale) locale.clone();
        if (i != -1) {
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 >= i) {
                    break;
                }
                int i5 = i4 + 1;
                localeArr[i5] = (Locale) localeListHelper.mList[i4].clone();
                i3 = i5;
            }
            while (true) {
                i++;
                if (i >= length) {
                    break;
                } else {
                    localeArr[i] = (Locale) localeListHelper.mList[i].clone();
                }
            }
        } else {
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 >= length) {
                    break;
                }
                int i8 = i7 + 1;
                localeArr[i8] = (Locale) localeListHelper.mList[i7].clone();
                i6 = i8;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i9 = 0; i9 < i2; i9++) {
            sb.append(LocaleHelper.toLanguageTag(localeArr[i9]));
            if (i9 < i2 - 1) {
                sb.append(',');
            }
        }
        this.mList = localeArr;
        this.mStringRepresentation = sb.toString();
    }

    LocaleListHelper(Locale... localeArr) {
        if (localeArr.length == 0) {
            this.mList = sEmptyList;
            this.mStringRepresentation = BuildConfig.FLAVOR;
            return;
        }
        Locale[] localeArr2 = new Locale[localeArr.length];
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < localeArr.length; i++) {
            Locale locale = localeArr[i];
            if (locale == null) {
                throw new NullPointerException("list[" + i + "] is null");
            }
            if (hashSet.contains(locale)) {
                throw new IllegalArgumentException("list[" + i + "] is a repetition");
            }
            Locale locale2 = (Locale) locale.clone();
            localeArr2[i] = locale2;
            sb.append(LocaleHelper.toLanguageTag(locale2));
            if (i < localeArr.length - 1) {
                sb.append(',');
            }
            hashSet.add(locale2);
        }
        this.mList = localeArr2;
        this.mStringRepresentation = sb.toString();
    }

    private Locale computeFirstMatch(Collection<String> collection, boolean z) {
        int iComputeFirstMatchIndex = computeFirstMatchIndex(collection, z);
        return iComputeFirstMatchIndex == -1 ? null : this.mList[iComputeFirstMatchIndex];
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int computeFirstMatchIndex(java.util.Collection<java.lang.String> r4, boolean r5) {
        /*
            r3 = this;
            r0 = r3
            java.util.Locale[] r0 = r0.mList
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r1 = 1
            if (r0 != r1) goto Lf
            r0 = 0
            return r0
        Lf:
            r0 = r8
            int r0 = r0.length
            if (r0 != 0) goto L17
            r0 = -1
            return r0
        L17:
            r0 = r5
            if (r0 == 0) goto L32
            r0 = r3
            java.util.Locale r1 = android.support.v4.os.LocaleListHelper.EN_LATN
            int r0 = r0.findFirstMatchIndex(r1)
            r6 = r0
            r0 = r6
            if (r0 != 0) goto L29
            r0 = 0
            return r0
        L29:
            r0 = r6
            r1 = 2147483647(0x7fffffff, float:NaN)
            if (r0 >= r1) goto L32
            goto L35
        L32:
            r0 = 2147483647(0x7fffffff, float:NaN)
            r6 = r0
        L35:
            r0 = r4
            java.util.Iterator r0 = r0.iterator()
            r4 = r0
        L3c:
            r0 = r4
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L6a
            r0 = r3
            r1 = r4
            java.lang.Object r1 = r1.next()
            java.lang.String r1 = (java.lang.String) r1
            java.util.Locale r1 = android.support.v4.os.LocaleHelper.forLanguageTag(r1)
            int r0 = r0.findFirstMatchIndex(r1)
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L5e
            r0 = 0
            return r0
        L5e:
            r0 = r7
            r1 = r6
            if (r0 >= r1) goto L3c
            r0 = r7
            r6 = r0
            goto L3c
        L6a:
            r0 = r6
            r1 = 2147483647(0x7fffffff, float:NaN)
            if (r0 != r1) goto L72
            r0 = 0
            return r0
        L72:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.LocaleListHelper.computeFirstMatchIndex(java.util.Collection, boolean):int");
    }

    private int findFirstMatchIndex(Locale locale) {
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            }
            if (matchScore(locale, localeArr[i]) > 0) {
                return i;
            }
            i++;
        }
    }

    static LocaleListHelper forLanguageTags(String str) {
        if (str == null || str.isEmpty()) {
            return getEmptyLocaleList();
        }
        String[] strArrSplit = str.split(",", -1);
        int length = strArrSplit.length;
        Locale[] localeArr = new Locale[length];
        for (int i = 0; i < length; i++) {
            localeArr[i] = LocaleHelper.forLanguageTag(strArrSplit[i]);
        }
        return new LocaleListHelper(localeArr);
    }

    static LocaleListHelper getAdjustedDefault() {
        LocaleListHelper localeListHelper;
        getDefault();
        synchronized (sLock) {
            localeListHelper = sDefaultAdjustedLocaleList;
        }
        return localeListHelper;
    }

    static LocaleListHelper getDefault() {
        Locale locale = Locale.getDefault();
        synchronized (sLock) {
            if (!locale.equals(sLastDefaultLocale)) {
                sLastDefaultLocale = locale;
                if (sDefaultLocaleList != null && locale.equals(sDefaultLocaleList.get(0))) {
                    return sDefaultLocaleList;
                }
                LocaleListHelper localeListHelper = new LocaleListHelper(locale, sLastExplicitlySetLocaleList);
                sDefaultLocaleList = localeListHelper;
                sDefaultAdjustedLocaleList = localeListHelper;
            }
            return sDefaultLocaleList;
        }
    }

    static LocaleListHelper getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    private static String getLikelyScript(Locale locale) {
        if (Build.VERSION.SDK_INT < 21) {
            return BuildConfig.FLAVOR;
        }
        String script = locale.getScript();
        return !script.isEmpty() ? script : BuildConfig.FLAVOR;
    }

    private static boolean isPseudoLocale(String str) {
        return STRING_EN_XA.equals(str) || STRING_AR_XB.equals(str);
    }

    private static boolean isPseudoLocale(Locale locale) {
        return LOCALE_EN_XA.equals(locale) || LOCALE_AR_XB.equals(locale);
    }

    static boolean isPseudoLocalesOnly(String[] strArr) {
        if (strArr == null) {
            return true;
        }
        if (strArr.length > 3) {
            return false;
        }
        for (String str : strArr) {
            if (!str.isEmpty() && !isPseudoLocale(str)) {
                return false;
            }
        }
        return true;
    }

    private static int matchScore(Locale locale, Locale locale2) {
        if (locale.equals(locale2)) {
            return 1;
        }
        if (!locale.getLanguage().equals(locale2.getLanguage()) || isPseudoLocale(locale) || isPseudoLocale(locale2)) {
            return 0;
        }
        String likelyScript = getLikelyScript(locale);
        if (!likelyScript.isEmpty()) {
            return likelyScript.equals(getLikelyScript(locale2)) ? 1 : 0;
        }
        String country = locale.getCountry();
        int i = 1;
        if (!country.isEmpty()) {
            i = country.equals(locale2.getCountry()) ? 1 : 0;
        }
        return i;
    }

    static void setDefault(LocaleListHelper localeListHelper) {
        setDefault(localeListHelper, 0);
    }

    static void setDefault(LocaleListHelper localeListHelper, int i) {
        if (localeListHelper == null) {
            throw new NullPointerException("locales is null");
        }
        if (localeListHelper.isEmpty()) {
            throw new IllegalArgumentException("locales is empty");
        }
        synchronized (sLock) {
            Locale locale = localeListHelper.get(i);
            sLastDefaultLocale = locale;
            Locale.setDefault(locale);
            sLastExplicitlySetLocaleList = localeListHelper;
            sDefaultLocaleList = localeListHelper;
            if (i == 0) {
                sDefaultAdjustedLocaleList = localeListHelper;
            } else {
                sDefaultAdjustedLocaleList = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LocaleListHelper)) {
            return false;
        }
        Locale[] localeArr = ((LocaleListHelper) obj).mList;
        if (this.mList.length != localeArr.length) {
            return false;
        }
        int i = 0;
        while (true) {
            Locale[] localeArr2 = this.mList;
            if (i >= localeArr2.length) {
                return true;
            }
            if (!localeArr2[i].equals(localeArr[i])) {
                return false;
            }
            i++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.util.Locale get(int r4) {
        /*
            r3 = this;
            r0 = r4
            if (r0 < 0) goto L16
            r0 = r3
            java.util.Locale[] r0 = r0.mList
            r5 = r0
            r0 = r4
            r1 = r5
            int r1 = r1.length
            if (r0 >= r1) goto L16
            r0 = r5
            r1 = r4
            r0 = r0[r1]
            r5 = r0
            goto L18
        L16:
            r0 = 0
            r5 = r0
        L18:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.os.LocaleListHelper.get(int):java.util.Locale");
    }

    Locale getFirstMatch(String[] strArr) {
        return computeFirstMatch(Arrays.asList(strArr), false);
    }

    int getFirstMatchIndex(String[] strArr) {
        return computeFirstMatchIndex(Arrays.asList(strArr), false);
    }

    int getFirstMatchIndexWithEnglishSupported(Collection<String> collection) {
        return computeFirstMatchIndex(collection, true);
    }

    int getFirstMatchIndexWithEnglishSupported(String[] strArr) {
        return getFirstMatchIndexWithEnglishSupported(Arrays.asList(strArr));
    }

    Locale getFirstMatchWithEnglishSupported(String[] strArr) {
        return computeFirstMatch(Arrays.asList(strArr), true);
    }

    public int hashCode() {
        int iHashCode = 1;
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return iHashCode;
            }
            iHashCode = (iHashCode * 31) + localeArr[i].hashCode();
            i++;
        }
    }

    int indexOf(Locale locale) {
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return -1;
            }
            if (localeArr[i].equals(locale)) {
                return i;
            }
            i++;
        }
    }

    boolean isEmpty() {
        return this.mList.length == 0;
    }

    int size() {
        return this.mList.length;
    }

    String toLanguageTags() {
        return this.mStringRepresentation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                sb.append("]");
                return sb.toString();
            }
            sb.append(localeArr[i]);
            if (i < this.mList.length - 1) {
                sb.append(',');
            }
            i++;
        }
    }
}
