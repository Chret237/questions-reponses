package android.support.design.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.design.C0029R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.design.animation.ImageMatrixProperty;
import android.support.design.animation.MatrixEvaluator;
import android.support.design.animation.MotionSpec;
import android.support.design.ripple.RippleUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl.class */
class FloatingActionButtonImpl {
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final long ELEVATION_ANIM_DELAY = 100;
    static final long ELEVATION_ANIM_DURATION = 100;
    private static final float HIDE_ICON_SCALE = 0.0f;
    private static final float HIDE_OPACITY = 0.0f;
    private static final float HIDE_SCALE = 0.0f;
    private static final float SHOW_ICON_SCALE = 1.0f;
    private static final float SHOW_OPACITY = 1.0f;
    private static final float SHOW_SCALE = 1.0f;
    CircularBorderDrawable borderDrawable;
    Drawable contentBackground;
    Animator currentAnimator;
    private MotionSpec defaultHideMotionSpec;
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    private ArrayList<Animator.AnimatorListener> hideListeners;
    MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    int maxImageSize;
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    ShadowDrawableWrapper shadowDrawable;
    final ShadowViewDelegate shadowViewDelegate;
    Drawable shapeDrawable;
    private ArrayList<Animator.AnimatorListener> showListeners;
    MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    final VisibilityAwareImageButton view;
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] PRESSED_ENABLED_STATE_SET = {R.attr.state_pressed, R.attr.state_enabled};
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET = {R.attr.state_hovered, R.attr.state_focused, R.attr.state_enabled};
    static final int[] FOCUSED_ENABLED_STATE_SET = {R.attr.state_focused, R.attr.state_enabled};
    static final int[] HOVERED_ENABLED_STATE_SET = {R.attr.state_hovered, R.attr.state_enabled};
    static final int[] ENABLED_STATE_SET = {R.attr.state_enabled};
    static final int[] EMPTY_STATE_SET = new int[0];
    int animState = 0;
    float imageMatrixScale = 1.0f;
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    private final Matrix tmpMatrix = new Matrix();

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$DisabledElevationAnimation.class */
    private class DisabledElevationAnimation extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        DisabledElevationAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            super();
            this.this$0 = floatingActionButtonImpl;
        }

        @Override // android.support.design.widget.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$ElevateToHoveredFocusedTranslationZAnimation.class */
    private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ElevateToHoveredFocusedTranslationZAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            super();
            this.this$0 = floatingActionButtonImpl;
        }

        @Override // android.support.design.widget.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return this.this$0.elevation + this.this$0.hoveredFocusedTranslationZ;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$ElevateToPressedTranslationZAnimation.class */
    private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ElevateToPressedTranslationZAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            super();
            this.this$0 = floatingActionButtonImpl;
        }

        @Override // android.support.design.widget.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return this.this$0.elevation + this.this$0.pressedTranslationZ;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$InternalVisibilityChangedListener.class */
    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$ResetElevationAnimation.class */
    private class ResetElevationAnimation extends ShadowAnimatorImpl {
        final FloatingActionButtonImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ResetElevationAnimation(FloatingActionButtonImpl floatingActionButtonImpl) {
            super();
            this.this$0 = floatingActionButtonImpl;
        }

        @Override // android.support.design.widget.FloatingActionButtonImpl.ShadowAnimatorImpl
        protected float getTargetShadowSize() {
            return this.this$0.elevation;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/FloatingActionButtonImpl$ShadowAnimatorImpl.class */
    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        final FloatingActionButtonImpl this$0;
        private boolean validValues;

        private ShadowAnimatorImpl(FloatingActionButtonImpl floatingActionButtonImpl) {
            this.this$0 = floatingActionButtonImpl;
        }

        protected abstract float getTargetShadowSize();

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.this$0.shadowDrawable.setShadowSize(this.shadowSizeEnd);
            this.validValues = false;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.validValues) {
                this.shadowSizeStart = this.this$0.shadowDrawable.getShadowSize();
                this.shadowSizeEnd = getTargetShadowSize();
                this.validValues = true;
            }
            ShadowDrawableWrapper shadowDrawableWrapper = this.this$0.shadowDrawable;
            float f = this.shadowSizeStart;
            shadowDrawableWrapper.setShadowSize(f + ((this.shadowSizeEnd - f) * valueAnimator.getAnimatedFraction()));
        }
    }

    FloatingActionButtonImpl(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        this.view = visibilityAwareImageButton;
        this.shadowViewDelegate = shadowViewDelegate;
        StateListAnimator stateListAnimator = new StateListAnimator();
        this.stateListAnimator = stateListAnimator;
        stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation(this)));
        this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation(this)));
        this.stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation(this)));
        this.stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation(this)));
        this.rotation = this.view.getRotation();
    }

    private void calculateImageMatrixFromScale(float f, Matrix matrix) {
        matrix.reset();
        if (this.view.getDrawable() == null || this.maxImageSize == 0) {
            return;
        }
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        rectF.set(0.0f, 0.0f, r0.getIntrinsicWidth(), r0.getIntrinsicHeight());
        int i = this.maxImageSize;
        rectF2.set(0.0f, 0.0f, i, i);
        matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        int i2 = this.maxImageSize;
        matrix.postScale(f, f, i2 / 2.0f, i2 / 2.0f);
    }

    private AnimatorSet createAnimator(MotionSpec motionSpec, float f, float f2, float f3) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.view, (Property<VisibilityAwareImageButton, Float>) View.ALPHA, f);
        motionSpec.getTiming("opacity").apply(objectAnimatorOfFloat);
        arrayList.add(objectAnimatorOfFloat);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(this.view, (Property<VisibilityAwareImageButton, Float>) View.SCALE_X, f2);
        motionSpec.getTiming("scale").apply(objectAnimatorOfFloat2);
        arrayList.add(objectAnimatorOfFloat2);
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(this.view, (Property<VisibilityAwareImageButton, Float>) View.SCALE_Y, f2);
        motionSpec.getTiming("scale").apply(objectAnimatorOfFloat3);
        arrayList.add(objectAnimatorOfFloat3);
        calculateImageMatrixFromScale(f3, this.tmpMatrix);
        ObjectAnimator objectAnimatorOfObject = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator(), new Matrix(this.tmpMatrix));
        motionSpec.getTiming("iconScale").apply(objectAnimatorOfObject);
        arrayList.add(objectAnimatorOfObject);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        return animatorSet;
    }

    private ValueAnimator createElevationAnimator(ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener(shadowAnimatorImpl);
        valueAnimator.addUpdateListener(shadowAnimatorImpl);
        valueAnimator.setFloatValues(0.0f, 1.0f);
        return valueAnimator;
    }

    private void ensurePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener(this) { // from class: android.support.design.widget.FloatingActionButtonImpl.3
                final FloatingActionButtonImpl this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    this.this$0.onPreDraw();
                    return true;
                }
            };
        }
    }

    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), C0029R.animator.design_fab_hide_motion_spec);
        }
        return this.defaultHideMotionSpec;
    }

    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), C0029R.animator.design_fab_show_motion_spec);
        }
        return this.defaultShowMotionSpec;
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode();
    }

    private void updateFromViewRotation() {
        if (Build.VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, null);
            }
        }
        ShadowDrawableWrapper shadowDrawableWrapper = this.shadowDrawable;
        if (shadowDrawableWrapper != null) {
            shadowDrawableWrapper.setRotation(-this.rotation);
        }
        CircularBorderDrawable circularBorderDrawable = this.borderDrawable;
        if (circularBorderDrawable != null) {
            circularBorderDrawable.setRotation(-this.rotation);
        }
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList<>();
        }
        this.hideListeners.add(animatorListener);
    }

    void addOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList<>();
        }
        this.showListeners.add(animatorListener);
    }

    CircularBorderDrawable createBorderDrawable(int i, ColorStateList colorStateList) {
        Context context = this.view.getContext();
        CircularBorderDrawable circularBorderDrawableNewCircularDrawable = newCircularDrawable();
        circularBorderDrawableNewCircularDrawable.setGradientColors(ContextCompat.getColor(context, C0029R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, C0029R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, C0029R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, C0029R.color.design_fab_stroke_end_outer_color));
        circularBorderDrawableNewCircularDrawable.setBorderWidth(i);
        circularBorderDrawableNewCircularDrawable.setBorderTint(colorStateList);
        return circularBorderDrawableNewCircularDrawable;
    }

    GradientDrawable createShapeDrawable() {
        GradientDrawable gradientDrawableNewGradientDrawableForShape = newGradientDrawableForShape();
        gradientDrawableNewGradientDrawableForShape.setShape(1);
        gradientDrawableNewGradientDrawableForShape.setColor(-1);
        return gradientDrawableNewGradientDrawableForShape;
    }

    final Drawable getContentBackground() {
        return this.contentBackground;
    }

    float getElevation() {
        return this.elevation;
    }

    final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    void getPadding(Rect rect) {
        this.shadowDrawable.getPadding(rect);
    }

    float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    void hide(InternalVisibilityChangedListener internalVisibilityChangedListener, boolean z) {
        if (isOrWillBeHidden()) {
            return;
        }
        Animator animator = this.currentAnimator;
        if (animator != null) {
            animator.cancel();
        }
        if (!shouldAnimateVisibilityChange()) {
            this.view.internalSetVisibility(z ? 8 : 4, z);
            if (internalVisibilityChangedListener != null) {
                internalVisibilityChangedListener.onHidden();
                return;
            }
            return;
        }
        MotionSpec defaultHideMotionSpec = this.hideMotionSpec;
        if (defaultHideMotionSpec == null) {
            defaultHideMotionSpec = getDefaultHideMotionSpec();
        }
        AnimatorSet animatorSetCreateAnimator = createAnimator(defaultHideMotionSpec, 0.0f, 0.0f, 0.0f);
        animatorSetCreateAnimator.addListener(new AnimatorListenerAdapter(this, z, internalVisibilityChangedListener) { // from class: android.support.design.widget.FloatingActionButtonImpl.1
            private boolean cancelled;
            final FloatingActionButtonImpl this$0;
            final boolean val$fromUser;
            final InternalVisibilityChangedListener val$listener;

            {
                this.this$0 = this;
                this.val$fromUser = z;
                this.val$listener = internalVisibilityChangedListener;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator2) {
                this.cancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                this.this$0.animState = 0;
                this.this$0.currentAnimator = null;
                if (this.cancelled) {
                    return;
                }
                this.this$0.view.internalSetVisibility(this.val$fromUser ? 8 : 4, this.val$fromUser);
                InternalVisibilityChangedListener internalVisibilityChangedListener2 = this.val$listener;
                if (internalVisibilityChangedListener2 != null) {
                    internalVisibilityChangedListener2.onHidden();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator2) {
                this.this$0.view.internalSetVisibility(0, this.val$fromUser);
                this.this$0.animState = 1;
                this.this$0.currentAnimator = animator2;
                this.cancelled = false;
            }
        });
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList != null) {
            Iterator<Animator.AnimatorListener> it = arrayList.iterator();
            while (it.hasNext()) {
                animatorSetCreateAnimator.addListener(it.next());
            }
        }
        animatorSetCreateAnimator.start();
    }

    boolean isOrWillBeHidden() {
        boolean z = false;
        if (this.view.getVisibility() == 0) {
            if (this.animState == 1) {
                z = true;
            }
            return z;
        }
        boolean z2 = false;
        if (this.animState != 2) {
            z2 = true;
        }
        return z2;
    }

    boolean isOrWillBeShown() {
        boolean z = false;
        if (this.view.getVisibility() == 0) {
            if (this.animState != 1) {
                z = true;
            }
            return z;
        }
        boolean z2 = false;
        if (this.animState == 2) {
            z2 = true;
        }
        return z2;
    }

    void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }

    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }

    GradientDrawable newGradientDrawableForShape() {
        return new GradientDrawable();
    }

    void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
        }
    }

    void onCompatShadowChanged() {
    }

    void onDetachedFromWindow() {
        if (this.preDrawListener != null) {
            this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
            this.preDrawListener = null;
        }
    }

    void onDrawableStateChanged(int[] iArr) {
        this.stateListAnimator.setState(iArr);
    }

    void onElevationsChanged(float f, float f2, float f3) {
        ShadowDrawableWrapper shadowDrawableWrapper = this.shadowDrawable;
        if (shadowDrawableWrapper != null) {
            shadowDrawableWrapper.setShadowSize(f, this.pressedTranslationZ + f);
            updatePadding();
        }
    }

    void onPaddingUpdated(Rect rect) {
    }

    void onPreDraw() {
        float rotation = this.view.getRotation();
        if (this.rotation != rotation) {
            this.rotation = rotation;
            updateFromViewRotation();
        }
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(animatorListener);
    }

    void removeOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(animatorListener);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void setBackgroundDrawable(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i) {
        Drawable[] drawableArr;
        Drawable drawableWrap = DrawableCompat.wrap(createShapeDrawable());
        this.shapeDrawable = drawableWrap;
        DrawableCompat.setTintList(drawableWrap, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.shapeDrawable, mode);
        }
        Drawable drawableWrap2 = DrawableCompat.wrap(createShapeDrawable());
        this.rippleDrawable = drawableWrap2;
        DrawableCompat.setTintList(drawableWrap2, RippleUtils.convertToRippleDrawableColor(colorStateList2));
        if (i > 0) {
            CircularBorderDrawable circularBorderDrawableCreateBorderDrawable = createBorderDrawable(i, colorStateList);
            this.borderDrawable = circularBorderDrawableCreateBorderDrawable;
            drawableArr = new Drawable[]{circularBorderDrawableCreateBorderDrawable, this.shapeDrawable, this.rippleDrawable};
        } else {
            this.borderDrawable = null;
            drawableArr = new Drawable[]{this.shapeDrawable, this.rippleDrawable};
        }
        this.contentBackground = new LayerDrawable(drawableArr);
        Context context = this.view.getContext();
        Drawable drawable = this.contentBackground;
        float radius = this.shadowViewDelegate.getRadius();
        float f = this.elevation;
        ShadowDrawableWrapper shadowDrawableWrapper = new ShadowDrawableWrapper(context, drawable, radius, f, f + this.pressedTranslationZ);
        this.shadowDrawable = shadowDrawableWrapper;
        shadowDrawableWrapper.setAddPaddingForCorners(false);
        this.shadowViewDelegate.setBackgroundDrawable(this.shadowDrawable);
    }

    void setBackgroundTintList(ColorStateList colorStateList) {
        Drawable drawable = this.shapeDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, colorStateList);
        }
        CircularBorderDrawable circularBorderDrawable = this.borderDrawable;
        if (circularBorderDrawable != null) {
            circularBorderDrawable.setBorderTint(colorStateList);
        }
    }

    void setBackgroundTintMode(PorterDuff.Mode mode) {
        Drawable drawable = this.shapeDrawable;
        if (drawable != null) {
            DrawableCompat.setTintMode(drawable, mode);
        }
    }

    final void setElevation(float f) {
        if (this.elevation != f) {
            this.elevation = f;
            onElevationsChanged(f, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    final void setHideMotionSpec(MotionSpec motionSpec) {
        this.hideMotionSpec = motionSpec;
    }

    final void setHoveredFocusedTranslationZ(float f) {
        if (this.hoveredFocusedTranslationZ != f) {
            this.hoveredFocusedTranslationZ = f;
            onElevationsChanged(this.elevation, f, this.pressedTranslationZ);
        }
    }

    final void setImageMatrixScale(float f) {
        this.imageMatrixScale = f;
        Matrix matrix = this.tmpMatrix;
        calculateImageMatrixFromScale(f, matrix);
        this.view.setImageMatrix(matrix);
    }

    final void setMaxImageSize(int i) {
        if (this.maxImageSize != i) {
            this.maxImageSize = i;
            updateImageMatrixScale();
        }
    }

    final void setPressedTranslationZ(float f) {
        if (this.pressedTranslationZ != f) {
            this.pressedTranslationZ = f;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, f);
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, RippleUtils.convertToRippleDrawableColor(colorStateList));
        }
    }

    final void setShowMotionSpec(MotionSpec motionSpec) {
        this.showMotionSpec = motionSpec;
    }

    void show(InternalVisibilityChangedListener internalVisibilityChangedListener, boolean z) {
        if (isOrWillBeShown()) {
            return;
        }
        Animator animator = this.currentAnimator;
        if (animator != null) {
            animator.cancel();
        }
        if (!shouldAnimateVisibilityChange()) {
            this.view.internalSetVisibility(0, z);
            this.view.setAlpha(1.0f);
            this.view.setScaleY(1.0f);
            this.view.setScaleX(1.0f);
            setImageMatrixScale(1.0f);
            if (internalVisibilityChangedListener != null) {
                internalVisibilityChangedListener.onShown();
                return;
            }
            return;
        }
        if (this.view.getVisibility() != 0) {
            this.view.setAlpha(0.0f);
            this.view.setScaleY(0.0f);
            this.view.setScaleX(0.0f);
            setImageMatrixScale(0.0f);
        }
        MotionSpec defaultShowMotionSpec = this.showMotionSpec;
        if (defaultShowMotionSpec == null) {
            defaultShowMotionSpec = getDefaultShowMotionSpec();
        }
        AnimatorSet animatorSetCreateAnimator = createAnimator(defaultShowMotionSpec, 1.0f, 1.0f, 1.0f);
        animatorSetCreateAnimator.addListener(new AnimatorListenerAdapter(this, z, internalVisibilityChangedListener) { // from class: android.support.design.widget.FloatingActionButtonImpl.2
            final FloatingActionButtonImpl this$0;
            final boolean val$fromUser;
            final InternalVisibilityChangedListener val$listener;

            {
                this.this$0 = this;
                this.val$fromUser = z;
                this.val$listener = internalVisibilityChangedListener;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                this.this$0.animState = 0;
                this.this$0.currentAnimator = null;
                InternalVisibilityChangedListener internalVisibilityChangedListener2 = this.val$listener;
                if (internalVisibilityChangedListener2 != null) {
                    internalVisibilityChangedListener2.onShown();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator2) {
                this.this$0.view.internalSetVisibility(0, this.val$fromUser);
                this.this$0.animState = 2;
                this.this$0.currentAnimator = animator2;
            }
        });
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList != null) {
            Iterator<Animator.AnimatorListener> it = arrayList.iterator();
            while (it.hasNext()) {
                animatorSetCreateAnimator.addListener(it.next());
            }
        }
        animatorSetCreateAnimator.start();
    }

    final void updateImageMatrixScale() {
        setImageMatrixScale(this.imageMatrixScale);
    }

    final void updatePadding() {
        Rect rect = this.tmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
}
