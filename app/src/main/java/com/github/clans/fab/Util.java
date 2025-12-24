package com.github.clans.fab;

import android.content.Context;
import android.os.Build;

/* loaded from: classes-dex2jar.jar:com/github/clans/fab/Util.class */
final class Util {
    private Util() {
    }

    static int dpToPx(Context context, float f) {
        return Math.round(f * context.getResources().getDisplayMetrics().density);
    }

    static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }
}
