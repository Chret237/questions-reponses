package android.support.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/transition/GhostViewApi21.class */
class GhostViewApi21 implements GhostViewImpl {
    private static final String TAG = "GhostViewApi21";
    private static Method sAddGhostMethod;
    private static boolean sAddGhostMethodFetched;
    private static Class<?> sGhostViewClass;
    private static boolean sGhostViewClassFetched;
    private static Method sRemoveGhostMethod;
    private static boolean sRemoveGhostMethodFetched;
    private final View mGhostView;

    private GhostViewApi21(View view) {
        this.mGhostView = view;
    }

    static GhostViewImpl addGhost(View view, ViewGroup viewGroup, Matrix matrix) throws NoSuchMethodException, SecurityException {
        fetchAddGhostMethod();
        Method method = sAddGhostMethod;
        if (method == null) {
            return null;
        }
        try {
            return new GhostViewApi21((View) method.invoke(null, view, viewGroup, matrix));
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2.getCause());
        }
    }

    private static void fetchAddGhostMethod() throws NoSuchMethodException, SecurityException {
        if (sAddGhostMethodFetched) {
            return;
        }
        try {
            fetchGhostViewClass();
            Method declaredMethod = sGhostViewClass.getDeclaredMethod("addGhost", View.class, ViewGroup.class, Matrix.class);
            sAddGhostMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve addGhost method", e);
        }
        sAddGhostMethodFetched = true;
    }

    private static void fetchGhostViewClass() {
        if (sGhostViewClassFetched) {
            return;
        }
        try {
            sGhostViewClass = Class.forName("android.view.GhostView");
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "Failed to retrieve GhostView class", e);
        }
        sGhostViewClassFetched = true;
    }

    private static void fetchRemoveGhostMethod() throws NoSuchMethodException, SecurityException {
        if (sRemoveGhostMethodFetched) {
            return;
        }
        try {
            fetchGhostViewClass();
            Method declaredMethod = sGhostViewClass.getDeclaredMethod("removeGhost", View.class);
            sRemoveGhostMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve removeGhost method", e);
        }
        sRemoveGhostMethodFetched = true;
    }

    static void removeGhost(View view) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        fetchRemoveGhostMethod();
        Method method = sRemoveGhostMethod;
        if (method != null) {
            try {
                method.invoke(null, view);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }

    @Override // android.support.transition.GhostViewImpl
    public void reserveEndViewTransition(ViewGroup viewGroup, View view) {
    }

    @Override // android.support.transition.GhostViewImpl
    public void setVisibility(int i) {
        this.mGhostView.setVisibility(i);
    }
}
