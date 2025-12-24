package android.support.v4.content.p001pm;

import android.content.pm.PackageInfo;
import android.os.Build;

/* loaded from: classes-dex2jar.jar:android/support/v4/content/pm/PackageInfoCompat.class */
public final class PackageInfoCompat {
    private PackageInfoCompat() {
    }

    public static long getLongVersionCode(PackageInfo packageInfo) {
        return Build.VERSION.SDK_INT >= 28 ? packageInfo.getLongVersionCode() : packageInfo.versionCode;
    }
}
