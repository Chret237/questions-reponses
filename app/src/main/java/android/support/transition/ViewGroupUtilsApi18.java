package android.support.transition;

import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/transition/ViewGroupUtilsApi18.class */
class ViewGroupUtilsApi18 {
    private static final String TAG = "ViewUtilsApi18";
    private static Method sSuppressLayoutMethod;
    private static boolean sSuppressLayoutMethodFetched;

    private ViewGroupUtilsApi18() {
    }

    private static void fetchSuppressLayoutMethod() throws NoSuchMethodException, SecurityException {
        if (sSuppressLayoutMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE);
            sSuppressLayoutMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve suppressLayout method", e);
        }
        sSuppressLayoutMethodFetched = true;
    }

    static void suppressLayout(ViewGroup viewGroup, boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        fetchSuppressLayoutMethod();
        Method method = sSuppressLayoutMethod;
        if (method != null) {
            try {
                method.invoke(viewGroup, Boolean.valueOf(z));
            } catch (IllegalAccessException e) {
                Log.i(TAG, "Failed to invoke suppressLayout method", e);
            } catch (InvocationTargetException e2) {
                Log.i(TAG, "Error invoking suppressLayout method", e2);
            }
        }
    }
}
