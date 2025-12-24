package android.support.v4.content.p001pm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import java.util.Iterator;

/* loaded from: classes-dex2jar.jar:android/support/v4/content/pm/ShortcutManagerCompat.class */
public class ShortcutManagerCompat {
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";

    private ShortcutManagerCompat() {
    }

    public static Intent createShortcutResultIntent(Context context, ShortcutInfoCompat shortcutInfoCompat) {
        Intent intentCreateShortcutResultIntent = Build.VERSION.SDK_INT >= 26 ? ((ShortcutManager) context.getSystemService(ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo()) : null;
        Intent intent = intentCreateShortcutResultIntent;
        if (intentCreateShortcutResultIntent == null) {
            intent = new Intent();
        }
        return shortcutInfoCompat.addToIntent(intent);
    }

    public static boolean isRequestPinShortcutSupported(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager) context.getSystemService(ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        if (ContextCompat.checkSelfPermission(context, INSTALL_SHORTCUT_PERMISSION) != 0) {
            return false;
        }
        Iterator<ResolveInfo> it = context.getPackageManager().queryBroadcastReceivers(new Intent(ACTION_INSTALL_SHORTCUT), 0).iterator();
        while (it.hasNext()) {
            String str = it.next().activityInfo.permission;
            if (TextUtils.isEmpty(str) || INSTALL_SHORTCUT_PERMISSION.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean requestPinShortcut(Context context, ShortcutInfoCompat shortcutInfoCompat, IntentSender intentSender) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 26) {
            return ((ShortcutManager) context.getSystemService(ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        Intent intentAddToIntent = shortcutInfoCompat.addToIntent(new Intent(ACTION_INSTALL_SHORTCUT));
        if (intentSender == null) {
            context.sendBroadcast(intentAddToIntent);
            return true;
        }
        context.sendOrderedBroadcast(intentAddToIntent, null, new BroadcastReceiver(intentSender) { // from class: android.support.v4.content.pm.ShortcutManagerCompat.1
            final IntentSender val$callback;

            {
                this.val$callback = intentSender;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) throws IntentSender.SendIntentException {
                try {
                    this.val$callback.sendIntent(context2, 0, null, null, null);
                } catch (IntentSender.SendIntentException e) {
                }
            }
        }, null, -1, null, null);
        return true;
    }
}
