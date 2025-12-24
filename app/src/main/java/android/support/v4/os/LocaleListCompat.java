package android.support.v4.os;

import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/LocaleListCompat.class */
public final class LocaleListCompat {
    static final LocaleListInterface IMPL;
    private static final LocaleListCompat sEmptyLocaleList = new LocaleListCompat();

    /* loaded from: classes-dex2jar.jar:android/support/v4/os/LocaleListCompat$LocaleListCompatApi24Impl.class */
    static class LocaleListCompatApi24Impl implements LocaleListInterface {
        private LocaleList mLocaleList = new LocaleList(new Locale[0]);

        LocaleListCompatApi24Impl() {
        }

        @Override // android.support.v4.os.LocaleListInterface
        public boolean equals(Object obj) {
            return this.mLocaleList.equals(((LocaleListCompat) obj).unwrap());
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Locale get(int i) {
            return this.mLocaleList.get(i);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Locale getFirstMatch(String[] strArr) {
            LocaleList localeList = this.mLocaleList;
            if (localeList != null) {
                return localeList.getFirstMatch(strArr);
            }
            return null;
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Object getLocaleList() {
            return this.mLocaleList;
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int indexOf(Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public void setLocaleList(Locale... localeArr) {
            this.mLocaleList = new LocaleList(localeArr);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int size() {
            return this.mLocaleList.size();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public String toString() {
            return this.mLocaleList.toString();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/os/LocaleListCompat$LocaleListCompatBaseImpl.class */
    static class LocaleListCompatBaseImpl implements LocaleListInterface {
        private LocaleListHelper mLocaleList = new LocaleListHelper(new Locale[0]);

        LocaleListCompatBaseImpl() {
        }

        @Override // android.support.v4.os.LocaleListInterface
        public boolean equals(Object obj) {
            return this.mLocaleList.equals(((LocaleListCompat) obj).unwrap());
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Locale get(int i) {
            return this.mLocaleList.get(i);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Locale getFirstMatch(String[] strArr) {
            LocaleListHelper localeListHelper = this.mLocaleList;
            if (localeListHelper != null) {
                return localeListHelper.getFirstMatch(strArr);
            }
            return null;
        }

        @Override // android.support.v4.os.LocaleListInterface
        public Object getLocaleList() {
            return this.mLocaleList;
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int indexOf(Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public void setLocaleList(Locale... localeArr) {
            this.mLocaleList = new LocaleListHelper(localeArr);
        }

        @Override // android.support.v4.os.LocaleListInterface
        public int size() {
            return this.mLocaleList.size();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }

        @Override // android.support.v4.os.LocaleListInterface
        public String toString() {
            return this.mLocaleList.toString();
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 24) {
            IMPL = new LocaleListCompatApi24Impl();
        } else {
            IMPL = new LocaleListCompatBaseImpl();
        }
    }

    private LocaleListCompat() {
    }

    public static LocaleListCompat create(Locale... localeArr) {
        LocaleListCompat localeListCompat = new LocaleListCompat();
        localeListCompat.setLocaleListArray(localeArr);
        return localeListCompat;
    }

    public static LocaleListCompat forLanguageTags(String str) {
        if (str == null || str.isEmpty()) {
            return getEmptyLocaleList();
        }
        String[] strArrSplit = str.split(",", -1);
        int length = strArrSplit.length;
        Locale[] localeArr = new Locale[length];
        for (int i = 0; i < length; i++) {
            localeArr[i] = Build.VERSION.SDK_INT >= 21 ? Locale.forLanguageTag(strArrSplit[i]) : LocaleHelper.forLanguageTag(strArrSplit[i]);
        }
        LocaleListCompat localeListCompat = new LocaleListCompat();
        localeListCompat.setLocaleListArray(localeArr);
        return localeListCompat;
    }

    public static LocaleListCompat getAdjustedDefault() {
        return Build.VERSION.SDK_INT >= 24 ? wrap(LocaleList.getAdjustedDefault()) : create(Locale.getDefault());
    }

    public static LocaleListCompat getDefault() {
        return Build.VERSION.SDK_INT >= 24 ? wrap(LocaleList.getDefault()) : create(Locale.getDefault());
    }

    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    private void setLocaleList(LocaleList localeList) {
        int size = localeList.size();
        if (size > 0) {
            Locale[] localeArr = new Locale[size];
            for (int i = 0; i < size; i++) {
                localeArr[i] = localeList.get(i);
            }
            IMPL.setLocaleList(localeArr);
        }
    }

    private void setLocaleListArray(Locale... localeArr) {
        IMPL.setLocaleList(localeArr);
    }

    public static LocaleListCompat wrap(Object obj) {
        LocaleListCompat localeListCompat = new LocaleListCompat();
        if (obj instanceof LocaleList) {
            localeListCompat.setLocaleList((LocaleList) obj);
        }
        return localeListCompat;
    }

    public boolean equals(Object obj) {
        return IMPL.equals(obj);
    }

    public Locale get(int i) {
        return IMPL.get(i);
    }

    public Locale getFirstMatch(String[] strArr) {
        return IMPL.getFirstMatch(strArr);
    }

    public int hashCode() {
        return IMPL.hashCode();
    }

    public int indexOf(Locale locale) {
        return IMPL.indexOf(locale);
    }

    public boolean isEmpty() {
        return IMPL.isEmpty();
    }

    public int size() {
        return IMPL.size();
    }

    public String toLanguageTags() {
        return IMPL.toLanguageTags();
    }

    public String toString() {
        return IMPL.toString();
    }

    public Object unwrap() {
        return IMPL.getLocaleList();
    }
}
