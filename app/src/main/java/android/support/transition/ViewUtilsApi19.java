package android.support.transition;

import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/transition/ViewUtilsApi19.class */
class ViewUtilsApi19 extends ViewUtilsBase {
    private static final String TAG = "ViewUtilsApi19";
    private static Method sGetTransitionAlphaMethod;
    private static boolean sGetTransitionAlphaMethodFetched;
    private static Method sSetTransitionAlphaMethod;
    private static boolean sSetTransitionAlphaMethodFetched;

    ViewUtilsApi19() {
    }

    private void fetchGetTransitionAlphaMethod() throws NoSuchMethodException, SecurityException {
        if (sGetTransitionAlphaMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = View.class.getDeclaredMethod("getTransitionAlpha", new Class[0]);
            sGetTransitionAlphaMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve getTransitionAlpha method", e);
        }
        sGetTransitionAlphaMethodFetched = true;
    }

    private void fetchSetTransitionAlphaMethod() throws NoSuchMethodException, SecurityException {
        if (sSetTransitionAlphaMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = View.class.getDeclaredMethod("setTransitionAlpha", Float.TYPE);
            sSetTransitionAlphaMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve setTransitionAlpha method", e);
        }
        sSetTransitionAlphaMethodFetched = true;
    }

    @Override // android.support.transition.ViewUtilsBase
    public void clearNonTransitionAlpha(View view) {
    }

    @Override // android.support.transition.ViewUtilsBase
    public float getTransitionAlpha(View view) throws NoSuchMethodException, SecurityException {
        fetchGetTransitionAlphaMethod();
        Method method = sGetTransitionAlphaMethod;
        if (method != null) {
            try {
                return ((Float) method.invoke(view, new Object[0])).floatValue();
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
        return super.getTransitionAlpha(view);
    }

    @Override // android.support.transition.ViewUtilsBase
    public void saveNonTransitionAlpha(View view) {
    }

    @Override // android.support.transition.ViewUtilsBase
    public void setTransitionAlpha(View view, float f) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        fetchSetTransitionAlphaMethod();
        Method method = sSetTransitionAlphaMethod;
        if (method == null) {
            view.setAlpha(f);
            return;
        }
        try {
            method.invoke(view, Float.valueOf(f));
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2.getCause());
        }
    }
}
