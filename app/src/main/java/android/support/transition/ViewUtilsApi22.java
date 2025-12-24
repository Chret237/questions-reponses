package android.support.transition;

import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/transition/ViewUtilsApi22.class */
class ViewUtilsApi22 extends ViewUtilsApi21 {
    private static final String TAG = "ViewUtilsApi22";
    private static Method sSetLeftTopRightBottomMethod;
    private static boolean sSetLeftTopRightBottomMethodFetched;

    ViewUtilsApi22() {
    }

    private void fetchSetLeftTopRightBottomMethod() throws NoSuchMethodException, SecurityException {
        if (sSetLeftTopRightBottomMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = View.class.getDeclaredMethod("setLeftTopRightBottom", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            sSetLeftTopRightBottomMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve setLeftTopRightBottom method", e);
        }
        sSetLeftTopRightBottomMethodFetched = true;
    }

    @Override // android.support.transition.ViewUtilsBase
    public void setLeftTopRightBottom(View view, int i, int i2, int i3, int i4) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        fetchSetLeftTopRightBottomMethod();
        Method method = sSetLeftTopRightBottomMethod;
        if (method != null) {
            try {
                method.invoke(view, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }
}
