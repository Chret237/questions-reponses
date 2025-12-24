package android.support.v4.content.p001pm;

import android.content.pm.PermissionInfo;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes-dex2jar.jar:android/support/v4/content/pm/PermissionInfoCompat.class */
public final class PermissionInfoCompat {

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/v4/content/pm/PermissionInfoCompat$Protection.class */
    public @interface Protection {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/v4/content/pm/PermissionInfoCompat$ProtectionFlags.class */
    public @interface ProtectionFlags {
    }

    private PermissionInfoCompat() {
    }

    public static int getProtection(PermissionInfo permissionInfo) {
        return Build.VERSION.SDK_INT >= 28 ? permissionInfo.getProtection() : permissionInfo.protectionLevel & 15;
    }

    public static int getProtectionFlags(PermissionInfo permissionInfo) {
        return Build.VERSION.SDK_INT >= 28 ? permissionInfo.getProtectionFlags() : permissionInfo.protectionLevel & (-16);
    }
}
