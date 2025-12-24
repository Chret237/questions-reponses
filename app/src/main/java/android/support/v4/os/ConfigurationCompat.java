package android.support.v4.os;

import android.content.res.Configuration;
import android.os.Build;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/ConfigurationCompat.class */
public final class ConfigurationCompat {
    private ConfigurationCompat() {
    }

    public static LocaleListCompat getLocales(Configuration configuration) {
        return Build.VERSION.SDK_INT >= 24 ? LocaleListCompat.wrap(configuration.getLocales()) : LocaleListCompat.create(configuration.locale);
    }
}
