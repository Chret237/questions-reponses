package android.support.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.transition.TransitionUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/support/transition/ChangeImageTransform.class */
public class ChangeImageTransform extends Transition {
    private static final String PROPNAME_MATRIX = "android:changeImageTransform:matrix";
    private static final String PROPNAME_BOUNDS = "android:changeImageTransform:bounds";
    private static final String[] sTransitionProperties = {PROPNAME_MATRIX, PROPNAME_BOUNDS};
    private static final TypeEvaluator<Matrix> NULL_MATRIX_EVALUATOR = new TypeEvaluator<Matrix>() { // from class: android.support.transition.ChangeImageTransform.1
        @Override // android.animation.TypeEvaluator
        public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
            return null;
        }
    };
    private static final Property<ImageView, Matrix> ANIMATED_TRANSFORM_PROPERTY = new Property<ImageView, Matrix>(Matrix.class, "animatedTransform") { // from class: android.support.transition.ChangeImageTransform.2
        @Override // android.util.Property
        public Matrix get(ImageView imageView) {
            return null;
        }

        @Override // android.util.Property
        public void set(ImageView imageView, Matrix matrix) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            ImageViewUtils.animateTransform(imageView, matrix);
        }
    };

    /* renamed from: android.support.transition.ChangeImageTransform$3 */
    /* loaded from: classes-dex2jar.jar:android/support/transition/ChangeImageTransform$3.class */
    static /* synthetic */ class C01373 {
        static final int[] $SwitchMap$android$widget$ImageView$ScaleType;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:7:0x0020
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.widget.ImageView$ScaleType[] r0 = android.widget.ImageView.ScaleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.support.transition.ChangeImageTransform.C01373.$SwitchMap$android$widget$ImageView$ScaleType = r0
                r0 = r4
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_XY     // Catch: java.lang.NoSuchFieldError -> L20
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L20
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L20
            L14:
                int[] r0 = android.support.transition.ChangeImageTransform.C01373.$SwitchMap$android$widget$ImageView$ScaleType     // Catch: java.lang.NoSuchFieldError -> L20 java.lang.NoSuchFieldError -> L24
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_CROP     // Catch: java.lang.NoSuchFieldError -> L24
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L24
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L24
            L1f:
                return
            L20:
                r4 = move-exception
                goto L14
            L24:
                r4 = move-exception
                goto L1f
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.transition.ChangeImageTransform.C01373.m110clinit():void");
        }
    }

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if ((view instanceof ImageView) && view.getVisibility() == 0) {
            ImageView imageView = (ImageView) view;
            if (imageView.getDrawable() == null) {
                return;
            }
            Map<String, Object> map = transitionValues.values;
            map.put(PROPNAME_BOUNDS, new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            map.put(PROPNAME_MATRIX, copyImageMatrix(imageView));
        }
    }

    private static Matrix centerCropMatrix(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        float width = imageView.getWidth();
        float f = intrinsicWidth;
        float f2 = width / f;
        int intrinsicHeight = drawable.getIntrinsicHeight();
        float height = imageView.getHeight();
        float f3 = intrinsicHeight;
        float fMax = Math.max(f2, height / f3);
        int iRound = Math.round((width - (f * fMax)) / 2.0f);
        int iRound2 = Math.round((height - (f3 * fMax)) / 2.0f);
        Matrix matrix = new Matrix();
        matrix.postScale(fMax, fMax);
        matrix.postTranslate(iRound, iRound2);
        return matrix;
    }

    private static Matrix copyImageMatrix(ImageView imageView) {
        int i = C01373.$SwitchMap$android$widget$ImageView$ScaleType[imageView.getScaleType().ordinal()];
        return i != 1 ? i != 2 ? new Matrix(imageView.getImageMatrix()) : centerCropMatrix(imageView) : fitXYMatrix(imageView);
    }

    private ObjectAnimator createMatrixAnimator(ImageView imageView, Matrix matrix, Matrix matrix2) {
        return ObjectAnimator.ofObject(imageView, (Property<ImageView, V>) ANIMATED_TRANSFORM_PROPERTY, (TypeEvaluator) new TransitionUtils.MatrixEvaluator(), (Object[]) new Matrix[]{matrix, matrix2});
    }

    private ObjectAnimator createNullAnimator(ImageView imageView) {
        return ObjectAnimator.ofObject(imageView, (Property<ImageView, V>) ANIMATED_TRANSFORM_PROPERTY, (TypeEvaluator) NULL_MATRIX_EVALUATOR, (Object[]) new Matrix[]{null, null});
    }

    private static Matrix fitXYMatrix(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Matrix matrix = new Matrix();
        matrix.postScale(imageView.getWidth() / drawable.getIntrinsicWidth(), imageView.getHeight() / drawable.getIntrinsicHeight());
        return matrix;
    }

    @Override // android.support.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ObjectAnimator objectAnimatorCreateNullAnimator;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues.values.get(PROPNAME_BOUNDS);
        Rect rect2 = (Rect) transitionValues2.values.get(PROPNAME_BOUNDS);
        if (rect == null || rect2 == null) {
            return null;
        }
        Matrix matrix = (Matrix) transitionValues.values.get(PROPNAME_MATRIX);
        Matrix matrix2 = (Matrix) transitionValues2.values.get(PROPNAME_MATRIX);
        boolean z = (matrix == null && matrix2 == null) || (matrix != null && matrix.equals(matrix2));
        if (rect.equals(rect2) && z) {
            return null;
        }
        ImageView imageView = (ImageView) transitionValues2.view;
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        ImageViewUtils.startAnimateTransform(imageView);
        if (intrinsicWidth == 0 || intrinsicHeight == 0) {
            objectAnimatorCreateNullAnimator = createNullAnimator(imageView);
        } else {
            Matrix matrix3 = matrix;
            if (matrix == null) {
                matrix3 = MatrixUtils.IDENTITY_MATRIX;
            }
            Matrix matrix4 = matrix2;
            if (matrix2 == null) {
                matrix4 = MatrixUtils.IDENTITY_MATRIX;
            }
            ANIMATED_TRANSFORM_PROPERTY.set(imageView, matrix3);
            objectAnimatorCreateNullAnimator = createMatrixAnimator(imageView, matrix3, matrix4);
        }
        ImageViewUtils.reserveEndAnimateTransform(imageView, objectAnimatorCreateNullAnimator);
        return objectAnimatorCreateNullAnimator;
    }

    @Override // android.support.transition.Transition
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}
