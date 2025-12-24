package android.support.v4.os;

import java.util.Locale;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/LocaleListInterface.class */
interface LocaleListInterface {
    boolean equals(Object obj);

    Locale get(int i);

    Locale getFirstMatch(String[] strArr);

    Object getLocaleList();

    int hashCode();

    int indexOf(Locale locale);

    boolean isEmpty();

    void setLocaleList(Locale... localeArr);

    int size();

    String toLanguageTags();

    String toString();
}
