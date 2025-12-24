package android.support.v4.widget;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/* loaded from: classes-dex2jar.jar:android/support/v4/widget/ViewGroupUtils.class */
public class ViewGroupUtils {
    private static final ThreadLocal<Matrix> sMatrix = new ThreadLocal<>();
    private static final ThreadLocal<RectF> sRectF = new ThreadLocal<>();

    private ViewGroupUtils() {
    }

    public static void getDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        rect.set(0, 0, view.getWidth(), view.getHeight());
        offsetDescendantRect(viewGroup, view, rect);
    }

    private static void offsetDescendantMatrix(ViewParent viewParent, View view, Matrix matrix) {
        Object parent = view.getParent();
        if ((parent instanceof View) && parent != viewParent) {
            offsetDescendantMatrix(viewParent, (View) parent, matrix);
            matrix.preTranslate(-r0.getScrollX(), -r0.getScrollY());
        }
        matrix.preTranslate(view.getLeft(), view.getTop());
        if (view.getMatrix().isIdentity()) {
            return;
        }
        matrix.preConcat(view.getMatrix());
    }

    static void offsetDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        Matrix matrix = sMatrix.get();
        if (matrix == null) {
            matrix = new Matrix();
            sMatrix.set(matrix);
        } else {
            matrix.reset();
        }
        offsetDescendantMatrix(viewGroup, view, matrix);
        RectF rectF = sRectF.get();
        RectF rectF2 = rectF;
        if (rectF == null) {
            rectF2 = new RectF();
            sRectF.set(rectF2);
        }
        rectF2.set(rect);
        matrix.mapRect(rectF2);
        rect.set((int) (rectF2.left + 0.5f), (int) (rectF2.top + 0.5f), (int) (rectF2.right + 0.5f), (int) (rectF2.bottom + 0.5f));
    }
}
