package android.support.transition;

import android.os.Build;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:android/support/transition/ViewGroupUtils.class */
class ViewGroupUtils {
    private ViewGroupUtils() {
    }

    static ViewGroupOverlayImpl getOverlay(ViewGroup viewGroup) {
        return Build.VERSION.SDK_INT >= 18 ? new ViewGroupOverlayApi18(viewGroup) : ViewGroupOverlayApi14.createFrom(viewGroup);
    }

    static void suppressLayout(ViewGroup viewGroup, boolean z) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (Build.VERSION.SDK_INT >= 18) {
            ViewGroupUtilsApi18.suppressLayout(viewGroup, z);
        } else {
            ViewGroupUtilsApi14.suppressLayout(viewGroup, z);
        }
    }
}
