package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes-dex2jar.jar:android/support/transition/ChangeTransform.class */
public class ChangeTransform extends Transition {
    private static final String PROPNAME_INTERMEDIATE_MATRIX = "android:changeTransform:intermediateMatrix";
    private static final String PROPNAME_INTERMEDIATE_PARENT_MATRIX = "android:changeTransform:intermediateParentMatrix";
    private static final String PROPNAME_PARENT = "android:changeTransform:parent";
    private static final boolean SUPPORTS_VIEW_REMOVAL_SUPPRESSION;
    private boolean mReparent;
    private Matrix mTempMatrix;
    boolean mUseOverlay;
    private static final String PROPNAME_MATRIX = "android:changeTransform:matrix";
    private static final String PROPNAME_TRANSFORMS = "android:changeTransform:transforms";
    private static final String PROPNAME_PARENT_MATRIX = "android:changeTransform:parentMatrix";
    private static final String[] sTransitionProperties = {PROPNAME_MATRIX, PROPNAME_TRANSFORMS, PROPNAME_PARENT_MATRIX};
    private static final Property<PathAnimatorMatrix, float[]> NON_TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, float[]>(float[].class, "nonTranslations") { // from class: android.support.transition.ChangeTransform.1
        @Override // android.util.Property
        public float[] get(PathAnimatorMatrix pathAnimatorMatrix) {
            return null;
        }

        @Override // android.util.Property
        public void set(PathAnimatorMatrix pathAnimatorMatrix, float[] fArr) {
            pathAnimatorMatrix.setValues(fArr);
        }
    };
    private static final Property<PathAnimatorMatrix, PointF> TRANSLATIONS_PROPERTY = new Property<PathAnimatorMatrix, PointF>(PointF.class, "translations") { // from class: android.support.transition.ChangeTransform.2
        @Override // android.util.Property
        public PointF get(PathAnimatorMatrix pathAnimatorMatrix) {
            return null;
        }

        @Override // android.util.Property
        public void set(PathAnimatorMatrix pathAnimatorMatrix, PointF pointF) {
            pathAnimatorMatrix.setTranslation(pointF);
        }
    };

    /* loaded from: classes-dex2jar.jar:android/support/transition/ChangeTransform$GhostListener.class */
    private static class GhostListener extends TransitionListenerAdapter {
        private GhostViewImpl mGhostView;
        private View mView;

        GhostListener(View view, GhostViewImpl ghostViewImpl) {
            this.mView = view;
            this.mGhostView = ghostViewImpl;
        }

        @Override // android.support.transition.TransitionListenerAdapter, android.support.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            transition.removeListener(this);
            GhostViewUtils.removeGhost(this.mView);
            this.mView.setTag(C0149R.id.transition_transform, null);
            this.mView.setTag(C0149R.id.parent_matrix, null);
        }

        @Override // android.support.transition.TransitionListenerAdapter, android.support.transition.Transition.TransitionListener
        public void onTransitionPause(Transition transition) {
            this.mGhostView.setVisibility(4);
        }

        @Override // android.support.transition.TransitionListenerAdapter, android.support.transition.Transition.TransitionListener
        public void onTransitionResume(Transition transition) {
            this.mGhostView.setVisibility(0);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/ChangeTransform$PathAnimatorMatrix.class */
    private static class PathAnimatorMatrix {
        private final Matrix mMatrix = new Matrix();
        private float mTranslationX;
        private float mTranslationY;
        private final float[] mValues;
        private final View mView;

        PathAnimatorMatrix(View view, float[] fArr) {
            this.mView = view;
            float[] fArr2 = (float[]) fArr.clone();
            this.mValues = fArr2;
            this.mTranslationX = fArr2[2];
            this.mTranslationY = fArr2[5];
            setAnimationMatrix();
        }

        private void setAnimationMatrix() {
            float[] fArr = this.mValues;
            fArr[2] = this.mTranslationX;
            fArr[5] = this.mTranslationY;
            this.mMatrix.setValues(fArr);
            ViewUtils.setAnimationMatrix(this.mView, this.mMatrix);
        }

        Matrix getMatrix() {
            return this.mMatrix;
        }

        void setTranslation(PointF pointF) {
            this.mTranslationX = pointF.x;
            this.mTranslationY = pointF.y;
            setAnimationMatrix();
        }

        void setValues(float[] fArr) {
            System.arraycopy(fArr, 0, this.mValues, 0, fArr.length);
            setAnimationMatrix();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/ChangeTransform$Transforms.class */
    private static class Transforms {
        final float mRotationX;
        final float mRotationY;
        final float mRotationZ;
        final float mScaleX;
        final float mScaleY;
        final float mTranslationX;
        final float mTranslationY;
        final float mTranslationZ;

        Transforms(View view) {
            this.mTranslationX = view.getTranslationX();
            this.mTranslationY = view.getTranslationY();
            this.mTranslationZ = ViewCompat.getTranslationZ(view);
            this.mScaleX = view.getScaleX();
            this.mScaleY = view.getScaleY();
            this.mRotationX = view.getRotationX();
            this.mRotationY = view.getRotationY();
            this.mRotationZ = view.getRotation();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Transforms)) {
                return false;
            }
            Transforms transforms = (Transforms) obj;
            boolean z = false;
            if (transforms.mTranslationX == this.mTranslationX) {
                z = false;
                if (transforms.mTranslationY == this.mTranslationY) {
                    z = false;
                    if (transforms.mTranslationZ == this.mTranslationZ) {
                        z = false;
                        if (transforms.mScaleX == this.mScaleX) {
                            z = false;
                            if (transforms.mScaleY == this.mScaleY) {
                                z = false;
                                if (transforms.mRotationX == this.mRotationX) {
                                    z = false;
                                    if (transforms.mRotationY == this.mRotationY) {
                                        z = false;
                                        if (transforms.mRotationZ == this.mRotationZ) {
                                            z = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return z;
        }

        public int hashCode() {
            float f = this.mTranslationX;
            int iFloatToIntBits = 0;
            int iFloatToIntBits2 = f != 0.0f ? Float.floatToIntBits(f) : 0;
            float f2 = this.mTranslationY;
            int iFloatToIntBits3 = f2 != 0.0f ? Float.floatToIntBits(f2) : 0;
            float f3 = this.mTranslationZ;
            int iFloatToIntBits4 = f3 != 0.0f ? Float.floatToIntBits(f3) : 0;
            float f4 = this.mScaleX;
            int iFloatToIntBits5 = f4 != 0.0f ? Float.floatToIntBits(f4) : 0;
            float f5 = this.mScaleY;
            int iFloatToIntBits6 = f5 != 0.0f ? Float.floatToIntBits(f5) : 0;
            float f6 = this.mRotationX;
            int iFloatToIntBits7 = f6 != 0.0f ? Float.floatToIntBits(f6) : 0;
            float f7 = this.mRotationY;
            int iFloatToIntBits8 = f7 != 0.0f ? Float.floatToIntBits(f7) : 0;
            float f8 = this.mRotationZ;
            if (f8 != 0.0f) {
                iFloatToIntBits = Float.floatToIntBits(f8);
            }
            return (((((((((((((iFloatToIntBits2 * 31) + iFloatToIntBits3) * 31) + iFloatToIntBits4) * 31) + iFloatToIntBits5) * 31) + iFloatToIntBits6) * 31) + iFloatToIntBits7) * 31) + iFloatToIntBits8) * 31) + iFloatToIntBits;
        }

        public void restore(View view) {
            ChangeTransform.setTransforms(view, this.mTranslationX, this.mTranslationY, this.mTranslationZ, this.mScaleX, this.mScaleY, this.mRotationX, this.mRotationY, this.mRotationZ);
        }
    }

    static {
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 21) {
            z = true;
        }
        SUPPORTS_VIEW_REMOVAL_SUPPRESSION = z;
    }

    public ChangeTransform() {
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
    }

    public ChangeTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUseOverlay = true;
        this.mReparent = true;
        this.mTempMatrix = new Matrix();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.CHANGE_TRANSFORM);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        this.mUseOverlay = TypedArrayUtils.getNamedBoolean(typedArrayObtainStyledAttributes, xmlPullParser, "reparentWithOverlay", 1, true);
        this.mReparent = TypedArrayUtils.getNamedBoolean(typedArrayObtainStyledAttributes, xmlPullParser, "reparent", 0, true);
        typedArrayObtainStyledAttributes.recycle();
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view.getVisibility() == 8) {
            return;
        }
        transitionValues.values.put(PROPNAME_PARENT, view.getParent());
        transitionValues.values.put(PROPNAME_TRANSFORMS, new Transforms(view));
        Matrix matrix = view.getMatrix();
        transitionValues.values.put(PROPNAME_MATRIX, (matrix == null || matrix.isIdentity()) ? null : new Matrix(matrix));
        if (this.mReparent) {
            Matrix matrix2 = new Matrix();
            ViewUtils.transformMatrixToGlobal((ViewGroup) view.getParent(), matrix2);
            matrix2.preTranslate(-r0.getScrollX(), -r0.getScrollY());
            transitionValues.values.put(PROPNAME_PARENT_MATRIX, matrix2);
            transitionValues.values.put(PROPNAME_INTERMEDIATE_MATRIX, view.getTag(C0149R.id.transition_transform));
            transitionValues.values.put(PROPNAME_INTERMEDIATE_PARENT_MATRIX, view.getTag(C0149R.id.parent_matrix));
        }
    }

    private void createGhostView(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        Transition transition;
        View view = transitionValues2.view;
        Matrix matrix = new Matrix((Matrix) transitionValues2.values.get(PROPNAME_PARENT_MATRIX));
        ViewUtils.transformMatrixToLocal(viewGroup, matrix);
        GhostViewImpl ghostViewImplAddGhost = GhostViewUtils.addGhost(view, viewGroup, matrix);
        if (ghostViewImplAddGhost == null) {
            return;
        }
        ghostViewImplAddGhost.reserveEndViewTransition((ViewGroup) transitionValues.values.get(PROPNAME_PARENT), transitionValues.view);
        Transition transition2 = this;
        while (true) {
            transition = transition2;
            if (transition.mParent == null) {
                break;
            } else {
                transition2 = transition.mParent;
            }
        }
        transition.addListener(new GhostListener(view, ghostViewImplAddGhost));
        if (SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            if (transitionValues.view != transitionValues2.view) {
                ViewUtils.setTransitionAlpha(transitionValues.view, 0.0f);
            }
            ViewUtils.setTransitionAlpha(view, 1.0f);
        }
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [float[], java.lang.Object[]] */
    private ObjectAnimator createTransformAnimator(TransitionValues transitionValues, TransitionValues transitionValues2, boolean z) {
        Matrix matrix = (Matrix) transitionValues.values.get(PROPNAME_MATRIX);
        Matrix matrix2 = (Matrix) transitionValues2.values.get(PROPNAME_MATRIX);
        Matrix matrix3 = matrix;
        if (matrix == null) {
            matrix3 = MatrixUtils.IDENTITY_MATRIX;
        }
        Matrix matrix4 = matrix2;
        if (matrix2 == null) {
            matrix4 = MatrixUtils.IDENTITY_MATRIX;
        }
        if (matrix3.equals(matrix4)) {
            return null;
        }
        Transforms transforms = (Transforms) transitionValues2.values.get(PROPNAME_TRANSFORMS);
        View view = transitionValues2.view;
        setIdentityTransforms(view);
        float[] fArr = new float[9];
        matrix3.getValues(fArr);
        float[] fArr2 = new float[9];
        matrix4.getValues(fArr2);
        PathAnimatorMatrix pathAnimatorMatrix = new PathAnimatorMatrix(view, fArr);
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(pathAnimatorMatrix, PropertyValuesHolder.ofObject(NON_TRANSLATIONS_PROPERTY, new FloatArrayEvaluator(new float[9]), (Object[]) new float[]{fArr, fArr2}), PropertyValuesHolderUtils.ofPointF(TRANSLATIONS_PROPERTY, getPathMotion().getPath(fArr[2], fArr[5], fArr2[2], fArr2[5])));
        AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter(this, z, matrix4, view, transforms, pathAnimatorMatrix) { // from class: android.support.transition.ChangeTransform.3
            private boolean mIsCanceled;
            private Matrix mTempMatrix = new Matrix();
            final ChangeTransform this$0;
            final Matrix val$finalEndMatrix;
            final boolean val$handleParentChange;
            final PathAnimatorMatrix val$pathAnimatorMatrix;
            final Transforms val$transforms;
            final View val$view;

            {
                this.this$0 = this;
                this.val$handleParentChange = z;
                this.val$finalEndMatrix = matrix4;
                this.val$view = view;
                this.val$transforms = transforms;
                this.val$pathAnimatorMatrix = pathAnimatorMatrix;
            }

            private void setCurrentMatrix(Matrix matrix5) {
                this.mTempMatrix.set(matrix5);
                this.val$view.setTag(C0149R.id.transition_transform, this.mTempMatrix);
                this.val$transforms.restore(this.val$view);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mIsCanceled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mIsCanceled) {
                    if (this.val$handleParentChange && this.this$0.mUseOverlay) {
                        setCurrentMatrix(this.val$finalEndMatrix);
                    } else {
                        this.val$view.setTag(C0149R.id.transition_transform, null);
                        this.val$view.setTag(C0149R.id.parent_matrix, null);
                    }
                }
                ViewUtils.setAnimationMatrix(this.val$view, null);
                this.val$transforms.restore(this.val$view);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
            public void onAnimationPause(Animator animator) {
                setCurrentMatrix(this.val$pathAnimatorMatrix.getMatrix());
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
            public void onAnimationResume(Animator animator) {
                ChangeTransform.setIdentityTransforms(this.val$view);
            }
        };
        objectAnimatorOfPropertyValuesHolder.addListener(animatorListenerAdapter);
        AnimatorUtils.addPauseListener(objectAnimatorOfPropertyValuesHolder, animatorListenerAdapter);
        return objectAnimatorOfPropertyValuesHolder;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean parentsMatch(android.view.ViewGroup r5, android.view.ViewGroup r6) {
        /*
            r4 = this;
            r0 = r4
            r1 = r5
            boolean r0 = r0.isValidTarget(r1)
            r9 = r0
            r0 = 1
            r8 = r0
            r0 = 0
            r7 = r0
            r0 = r9
            if (r0 == 0) goto L35
            r0 = r4
            r1 = r6
            boolean r0 = r0.isValidTarget(r1)
            if (r0 != 0) goto L1c
            goto L35
        L1c:
            r0 = r4
            r1 = r5
            r2 = 1
            android.support.transition.TransitionValues r0 = r0.getMatchedTransitionValues(r1, r2)
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L42
            r0 = r6
            r1 = r5
            android.view.View r1 = r1.view
            if (r0 != r1) goto L40
            r0 = r8
            r7 = r0
            goto L42
        L35:
            r0 = r5
            r1 = r6
            if (r0 != r1) goto L40
            r0 = r8
            r7 = r0
            goto L42
        L40:
            r0 = 0
            r7 = r0
        L42:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.ChangeTransform.parentsMatch(android.view.ViewGroup, android.view.ViewGroup):boolean");
    }

    static void setIdentityTransforms(View view) {
        setTransforms(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }

    private void setMatricesForParent(TransitionValues transitionValues, TransitionValues transitionValues2) {
        Matrix matrix = (Matrix) transitionValues2.values.get(PROPNAME_PARENT_MATRIX);
        transitionValues2.view.setTag(C0149R.id.parent_matrix, matrix);
        Matrix matrix2 = this.mTempMatrix;
        matrix2.reset();
        matrix.invert(matrix2);
        Matrix matrix3 = (Matrix) transitionValues.values.get(PROPNAME_MATRIX);
        Matrix matrix4 = matrix3;
        if (matrix3 == null) {
            matrix4 = new Matrix();
            transitionValues.values.put(PROPNAME_MATRIX, matrix4);
        }
        matrix4.postConcat((Matrix) transitionValues.values.get(PROPNAME_PARENT_MATRIX));
        matrix4.postConcat(matrix2);
    }

    static void setTransforms(View view, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        view.setTranslationX(f);
        view.setTranslationY(f2);
        ViewCompat.setTranslationZ(view, f3);
        view.setScaleX(f4);
        view.setScaleY(f5);
        view.setRotationX(f6);
        view.setRotationY(f7);
        view.setRotation(f8);
    }

    @Override // android.support.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        if (SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            return;
        }
        ((ViewGroup) transitionValues.view.getParent()).startViewTransition(transitionValues.view);
    }

    @Override // android.support.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null || !transitionValues.values.containsKey(PROPNAME_PARENT) || !transitionValues2.values.containsKey(PROPNAME_PARENT)) {
            return null;
        }
        ViewGroup viewGroup2 = (ViewGroup) transitionValues.values.get(PROPNAME_PARENT);
        boolean z = this.mReparent && !parentsMatch(viewGroup2, (ViewGroup) transitionValues2.values.get(PROPNAME_PARENT));
        Matrix matrix = (Matrix) transitionValues.values.get(PROPNAME_INTERMEDIATE_MATRIX);
        if (matrix != null) {
            transitionValues.values.put(PROPNAME_MATRIX, matrix);
        }
        Matrix matrix2 = (Matrix) transitionValues.values.get(PROPNAME_INTERMEDIATE_PARENT_MATRIX);
        if (matrix2 != null) {
            transitionValues.values.put(PROPNAME_PARENT_MATRIX, matrix2);
        }
        if (z) {
            setMatricesForParent(transitionValues, transitionValues2);
        }
        ObjectAnimator objectAnimatorCreateTransformAnimator = createTransformAnimator(transitionValues, transitionValues2, z);
        if (z && objectAnimatorCreateTransformAnimator != null && this.mUseOverlay) {
            createGhostView(viewGroup, transitionValues, transitionValues2);
        } else if (!SUPPORTS_VIEW_REMOVAL_SUPPRESSION) {
            viewGroup2.endViewTransition(transitionValues.view);
        }
        return objectAnimatorCreateTransformAnimator;
    }

    public boolean getReparent() {
        return this.mReparent;
    }

    public boolean getReparentWithOverlay() {
        return this.mUseOverlay;
    }

    @Override // android.support.transition.Transition
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    public void setReparent(boolean z) {
        this.mReparent = z;
    }

    public void setReparentWithOverlay(boolean z) {
        this.mUseOverlay = z;
    }
}
