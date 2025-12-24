package android.support.design.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.C0029R;
import android.support.design.animation.AnimationUtils;
import android.support.design.behavior.HideBottomViewOnScrollBehavior;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.resources.MaterialResources;
import android.support.design.shape.MaterialShapeDrawable;
import android.support.design.shape.ShapePathModel;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/design/bottomappbar/BottomAppBar.class */
public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
    private static final long ANIMATION_DURATION = 300;
    public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
    public static final int FAB_ALIGNMENT_MODE_END = 1;
    private Animator attachAnimator;
    private int fabAlignmentMode;
    AnimatorListenerAdapter fabAnimationListener;
    private boolean fabAttached;
    private final int fabOffsetEndMode;
    private boolean hideOnScroll;
    private final MaterialShapeDrawable materialShapeDrawable;
    private Animator menuAnimator;
    private Animator modeAnimator;
    private final BottomAppBarTopEdgeTreatment topEdgeTreatment;

    /* loaded from: classes-dex2jar.jar:android/support/design/bottomappbar/BottomAppBar$Behavior.class */
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
        private final Rect fabContentRect;

        public Behavior() {
            this.fabContentRect = new Rect();
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.fabContentRect = new Rect();
        }

        private boolean updateFabPositionAndVisibility(FloatingActionButton floatingActionButton, BottomAppBar bottomAppBar) {
            ((CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams()).anchorGravity = 17;
            bottomAppBar.addFabAnimationListeners(floatingActionButton);
            return true;
        }

        @Override // android.support.design.behavior.HideBottomViewOnScrollBehavior, android.support.design.widget.CoordinatorLayout.Behavior
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, int i) {
            FloatingActionButton floatingActionButtonFindDependentFab = bottomAppBar.findDependentFab();
            if (floatingActionButtonFindDependentFab != null) {
                updateFabPositionAndVisibility(floatingActionButtonFindDependentFab, bottomAppBar);
                floatingActionButtonFindDependentFab.getMeasuredContentRect(this.fabContentRect);
                bottomAppBar.setFabDiameter(this.fabContentRect.height());
            }
            if (!bottomAppBar.isAnimationRunning()) {
                bottomAppBar.setCutoutState();
            }
            coordinatorLayout.onLayoutChild(bottomAppBar, i);
            return super.onLayoutChild(coordinatorLayout, (CoordinatorLayout) bottomAppBar, i);
        }

        @Override // android.support.design.widget.CoordinatorLayout.Behavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, View view, View view2, int i, int i2) {
            return bottomAppBar.getHideOnScroll() && super.onStartNestedScroll(coordinatorLayout, (CoordinatorLayout) bottomAppBar, view, view2, i, i2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.design.behavior.HideBottomViewOnScrollBehavior
        public void slideDown(BottomAppBar bottomAppBar) {
            super.slideDown((Behavior) bottomAppBar);
            FloatingActionButton floatingActionButtonFindDependentFab = bottomAppBar.findDependentFab();
            if (floatingActionButtonFindDependentFab != null) {
                floatingActionButtonFindDependentFab.getContentRect(this.fabContentRect);
                float measuredHeight = floatingActionButtonFindDependentFab.getMeasuredHeight() - this.fabContentRect.height();
                floatingActionButtonFindDependentFab.clearAnimation();
                floatingActionButtonFindDependentFab.animate().translationY((-floatingActionButtonFindDependentFab.getPaddingBottom()) + measuredHeight).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setDuration(175L);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.design.behavior.HideBottomViewOnScrollBehavior
        public void slideUp(BottomAppBar bottomAppBar) {
            super.slideUp((Behavior) bottomAppBar);
            FloatingActionButton floatingActionButtonFindDependentFab = bottomAppBar.findDependentFab();
            if (floatingActionButtonFindDependentFab != null) {
                floatingActionButtonFindDependentFab.clearAnimation();
                floatingActionButtonFindDependentFab.animate().translationY(bottomAppBar.getFabTranslationY()).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setDuration(225L);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/bottomappbar/BottomAppBar$FabAlignmentMode.class */
    public @interface FabAlignmentMode {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/bottomappbar/BottomAppBar$SavedState.class */
    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.design.bottomappbar.BottomAppBar.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int fabAlignmentMode;
        boolean fabAttached;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.fabAlignmentMode = parcel.readInt();
            this.fabAttached = parcel.readInt() != 0;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.fabAlignmentMode);
            parcel.writeInt(this.fabAttached ? 1 : 0);
        }
    }

    public BottomAppBar(Context context) {
        this(context, null, 0);
    }

    public BottomAppBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.bottomAppBarStyle);
    }

    public BottomAppBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.fabAttached = true;
        this.fabAnimationListener = new AnimatorListenerAdapter(this) { // from class: android.support.design.bottomappbar.BottomAppBar.7
            final BottomAppBar this$0;

            {
                this.this$0 = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                BottomAppBar bottomAppBar = this.this$0;
                bottomAppBar.maybeAnimateAttachChange(bottomAppBar.fabAttached);
                BottomAppBar bottomAppBar2 = this.this$0;
                bottomAppBar2.maybeAnimateMenuView(bottomAppBar2.fabAlignmentMode, this.this$0.fabAttached);
            }
        };
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0029R.styleable.BottomAppBar, i, C0029R.style.Widget_MaterialComponents_BottomAppBar, new int[0]);
        ColorStateList colorStateList = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.BottomAppBar_backgroundTint);
        float dimensionPixelOffset = typedArrayObtainStyledAttributes.getDimensionPixelOffset(C0029R.styleable.BottomAppBar_fabCradleMargin, 0);
        float dimensionPixelOffset2 = typedArrayObtainStyledAttributes.getDimensionPixelOffset(C0029R.styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
        float dimensionPixelOffset3 = typedArrayObtainStyledAttributes.getDimensionPixelOffset(C0029R.styleable.BottomAppBar_fabCradleVerticalOffset, 0);
        this.fabAlignmentMode = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.BottomAppBar_fabAlignmentMode, 0);
        this.hideOnScroll = typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.BottomAppBar_hideOnScroll, false);
        typedArrayObtainStyledAttributes.recycle();
        this.fabOffsetEndMode = getResources().getDimensionPixelOffset(C0029R.dimen.mtrl_bottomappbar_fabOffsetEndMode);
        this.topEdgeTreatment = new BottomAppBarTopEdgeTreatment(dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        ShapePathModel shapePathModel = new ShapePathModel();
        shapePathModel.setTopEdge(this.topEdgeTreatment);
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapePathModel);
        this.materialShapeDrawable = materialShapeDrawable;
        materialShapeDrawable.setShadowEnabled(true);
        this.materialShapeDrawable.setPaintStyle(Paint.Style.FILL);
        DrawableCompat.setTintList(this.materialShapeDrawable, colorStateList);
        ViewCompat.setBackground(this, this.materialShapeDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addFabAnimationListeners(FloatingActionButton floatingActionButton) {
        removeFabAnimationListeners(floatingActionButton);
        floatingActionButton.addOnHideAnimationListener(this.fabAnimationListener);
        floatingActionButton.addOnShowAnimationListener(this.fabAnimationListener);
    }

    private void cancelAnimations() {
        Animator animator = this.attachAnimator;
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = this.menuAnimator;
        if (animator2 != null) {
            animator2.cancel();
        }
        Animator animator3 = this.modeAnimator;
        if (animator3 != null) {
            animator3.cancel();
        }
    }

    private void createCradleShapeAnimation(boolean z, List<Animator> list) {
        if (z) {
            this.topEdgeTreatment.setHorizontalOffset(getFabTranslationX());
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.materialShapeDrawable.getInterpolation(), z ? 1.0f : 0.0f);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: android.support.design.bottomappbar.BottomAppBar.6
            final BottomAppBar this$0;

            {
                this.this$0 = this;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.materialShapeDrawable.setInterpolation(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        valueAnimatorOfFloat.setDuration(ANIMATION_DURATION);
        list.add(valueAnimatorOfFloat);
    }

    private void createCradleTranslationAnimation(int i, List<Animator> list) {
        if (this.fabAttached) {
            ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.topEdgeTreatment.getHorizontalOffset(), getFabTranslationX(i));
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: android.support.design.bottomappbar.BottomAppBar.2
                final BottomAppBar this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.topEdgeTreatment.setHorizontalOffset(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    this.this$0.materialShapeDrawable.invalidateSelf();
                }
            });
            valueAnimatorOfFloat.setDuration(ANIMATION_DURATION);
            list.add(valueAnimatorOfFloat);
        }
    }

    private void createFabTranslationXAnimation(int i, List<Animator> list) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(findDependentFab(), "translationX", getFabTranslationX(i));
        objectAnimatorOfFloat.setDuration(ANIMATION_DURATION);
        list.add(objectAnimatorOfFloat);
    }

    private void createFabTranslationYAnimation(boolean z, List<Animator> list) {
        FloatingActionButton floatingActionButtonFindDependentFab = findDependentFab();
        if (floatingActionButtonFindDependentFab == null) {
            return;
        }
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(floatingActionButtonFindDependentFab, "translationY", getFabTranslationY(z));
        objectAnimatorOfFloat.setDuration(ANIMATION_DURATION);
        list.add(objectAnimatorOfFloat);
    }

    private void createMenuViewTranslationAnimation(int i, boolean z, List<Animator> list) {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView == null) {
            return;
        }
        Animator animatorOfFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", 1.0f);
        if ((!this.fabAttached && (!z || !isVisibleFab())) || (this.fabAlignmentMode != 1 && i != 1)) {
            if (actionMenuView.getAlpha() < 1.0f) {
                list.add(animatorOfFloat);
            }
        } else {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", 0.0f);
            objectAnimatorOfFloat.addListener(new AnimatorListenerAdapter(this, actionMenuView, i, z) { // from class: android.support.design.bottomappbar.BottomAppBar.4
                public boolean cancelled;
                final BottomAppBar this$0;
                final ActionMenuView val$actionMenuView;
                final boolean val$targetAttached;
                final int val$targetMode;

                {
                    this.this$0 = this;
                    this.val$actionMenuView = actionMenuView;
                    this.val$targetMode = i;
                    this.val$targetAttached = z;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.cancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (this.cancelled) {
                        return;
                    }
                    this.this$0.translateActionMenuView(this.val$actionMenuView, this.val$targetMode, this.val$targetAttached);
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(150L);
            animatorSet.playSequentially(objectAnimatorOfFloat, animatorOfFloat);
            list.add(animatorSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FloatingActionButton findDependentFab() {
        if (!(getParent() instanceof CoordinatorLayout)) {
            return null;
        }
        for (View view : ((CoordinatorLayout) getParent()).getDependents(this)) {
            if (view instanceof FloatingActionButton) {
                return (FloatingActionButton) view;
            }
        }
        return null;
    }

    private ActionMenuView getActionMenuView() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ActionMenuView) {
                return (ActionMenuView) childAt;
            }
        }
        return null;
    }

    private float getFabTranslationX() {
        return getFabTranslationX(this.fabAlignmentMode);
    }

    private int getFabTranslationX(int i) {
        int i2 = 0;
        boolean z = ViewCompat.getLayoutDirection(this) == 1;
        if (i == 1) {
            int measuredWidth = getMeasuredWidth() / 2;
            int i3 = this.fabOffsetEndMode;
            int i4 = 1;
            if (z) {
                i4 = -1;
            }
            i2 = (measuredWidth - i3) * i4;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getFabTranslationY() {
        return getFabTranslationY(this.fabAttached);
    }

    private float getFabTranslationY(boolean z) {
        FloatingActionButton floatingActionButtonFindDependentFab = findDependentFab();
        if (floatingActionButtonFindDependentFab == null) {
            return 0.0f;
        }
        Rect rect = new Rect();
        floatingActionButtonFindDependentFab.getContentRect(rect);
        float fHeight = rect.height();
        float measuredHeight = fHeight;
        if (fHeight == 0.0f) {
            measuredHeight = floatingActionButtonFindDependentFab.getMeasuredHeight();
        }
        float height = floatingActionButtonFindDependentFab.getHeight() - rect.bottom;
        float height2 = floatingActionButtonFindDependentFab.getHeight() - rect.height();
        float f = (-getCradleVerticalOffset()) + (measuredHeight / 2.0f) + height;
        float paddingBottom = floatingActionButtonFindDependentFab.getPaddingBottom();
        float f2 = -getMeasuredHeight();
        if (!z) {
            f = height2 - paddingBottom;
        }
        return f2 + f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAnimationRunning() {
        Animator animator;
        Animator animator2;
        Animator animator3 = this.attachAnimator;
        return (animator3 != null && animator3.isRunning()) || ((animator = this.menuAnimator) != null && animator.isRunning()) || ((animator2 = this.modeAnimator) != null && animator2.isRunning());
    }

    private boolean isVisibleFab() {
        FloatingActionButton floatingActionButtonFindDependentFab = findDependentFab();
        return floatingActionButtonFindDependentFab != null && floatingActionButtonFindDependentFab.isOrWillBeShown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAnimateAttachChange(boolean z) {
        if (ViewCompat.isLaidOut(this)) {
            Animator animator = this.attachAnimator;
            if (animator != null) {
                animator.cancel();
            }
            ArrayList arrayList = new ArrayList();
            createCradleShapeAnimation(z && isVisibleFab(), arrayList);
            createFabTranslationYAnimation(z, arrayList);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            this.attachAnimator = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter(this) { // from class: android.support.design.bottomappbar.BottomAppBar.5
                final BottomAppBar this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    this.this$0.attachAnimator = null;
                }
            });
            this.attachAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeAnimateMenuView(int i, boolean z) {
        if (ViewCompat.isLaidOut(this)) {
            Animator animator = this.menuAnimator;
            if (animator != null) {
                animator.cancel();
            }
            ArrayList arrayList = new ArrayList();
            if (!isVisibleFab()) {
                i = 0;
                z = false;
            }
            createMenuViewTranslationAnimation(i, z, arrayList);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            this.menuAnimator = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter(this) { // from class: android.support.design.bottomappbar.BottomAppBar.3
                final BottomAppBar this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    this.this$0.menuAnimator = null;
                }
            });
            this.menuAnimator.start();
        }
    }

    private void maybeAnimateModeChange(int i) {
        if (this.fabAlignmentMode == i || !ViewCompat.isLaidOut(this)) {
            return;
        }
        Animator animator = this.modeAnimator;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        createCradleTranslationAnimation(i, arrayList);
        createFabTranslationXAnimation(i, arrayList);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.modeAnimator = animatorSet;
        animatorSet.addListener(new AnimatorListenerAdapter(this) { // from class: android.support.design.bottomappbar.BottomAppBar.1
            final BottomAppBar this$0;

            {
                this.this$0 = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                this.this$0.modeAnimator = null;
            }
        });
        this.modeAnimator.start();
    }

    private void removeFabAnimationListeners(FloatingActionButton floatingActionButton) {
        floatingActionButton.removeOnHideAnimationListener(this.fabAnimationListener);
        floatingActionButton.removeOnShowAnimationListener(this.fabAnimationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCutoutState() {
        this.topEdgeTreatment.setHorizontalOffset(getFabTranslationX());
        FloatingActionButton floatingActionButtonFindDependentFab = findDependentFab();
        this.materialShapeDrawable.setInterpolation((this.fabAttached && isVisibleFab()) ? 1.0f : 0.0f);
        if (floatingActionButtonFindDependentFab != null) {
            floatingActionButtonFindDependentFab.setTranslationY(getFabTranslationY());
            floatingActionButtonFindDependentFab.setTranslationX(getFabTranslationX());
        }
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView != null) {
            actionMenuView.setAlpha(1.0f);
            if (isVisibleFab()) {
                translateActionMenuView(actionMenuView, this.fabAlignmentMode, this.fabAttached);
            } else {
                translateActionMenuView(actionMenuView, 0, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void translateActionMenuView(ActionMenuView actionMenuView, int i, boolean z) {
        int i2;
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i2 = i4;
            if (i3 >= getChildCount()) {
                break;
            }
            View childAt = getChildAt(i3);
            int iMax = i2;
            if ((childAt.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt.getLayoutParams()).gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 8388611) {
                iMax = Math.max(i2, z2 ? childAt.getLeft() : childAt.getRight());
            }
            i3++;
            i4 = iMax;
        }
        actionMenuView.setTranslationX((i == 1 && z) ? i2 - (z2 ? actionMenuView.getRight() : actionMenuView.getLeft()) : 0.0f);
    }

    public ColorStateList getBackgroundTint() {
        return this.materialShapeDrawable.getTintList();
    }

    @Override // android.support.design.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior<BottomAppBar> getBehavior() {
        return new Behavior();
    }

    public float getCradleVerticalOffset() {
        return this.topEdgeTreatment.getCradleVerticalOffset();
    }

    public int getFabAlignmentMode() {
        return this.fabAlignmentMode;
    }

    public float getFabCradleMargin() {
        return this.topEdgeTreatment.getFabCradleMargin();
    }

    public float getFabCradleRoundedCornerRadius() {
        return this.topEdgeTreatment.getFabCradleRoundedCornerRadius();
    }

    public boolean getHideOnScroll() {
        return this.hideOnScroll;
    }

    @Override // android.support.v7.widget.Toolbar, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) throws NoSuchFieldException {
        super.onLayout(z, i, i2, i3, i4);
        cancelAnimations();
        setCutoutState();
    }

    @Override // android.support.v7.widget.Toolbar, android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.fabAlignmentMode = savedState.fabAlignmentMode;
        this.fabAttached = savedState.fabAttached;
    }

    @Override // android.support.v7.widget.Toolbar, android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return savedState;
    }

    public void replaceMenu(int i) {
        getMenu().clear();
        inflateMenu(i);
    }

    public void setBackgroundTint(ColorStateList colorStateList) {
        DrawableCompat.setTintList(this.materialShapeDrawable, colorStateList);
    }

    public void setCradleVerticalOffset(float f) {
        if (f != getCradleVerticalOffset()) {
            this.topEdgeTreatment.setCradleVerticalOffset(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabAlignmentMode(int i) {
        maybeAnimateModeChange(i);
        maybeAnimateMenuView(i, this.fabAttached);
        this.fabAlignmentMode = i;
    }

    public void setFabCradleMargin(float f) {
        if (f != getFabCradleMargin()) {
            this.topEdgeTreatment.setFabCradleMargin(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabCradleRoundedCornerRadius(float f) {
        if (f != getFabCradleRoundedCornerRadius()) {
            this.topEdgeTreatment.setFabCradleRoundedCornerRadius(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    void setFabDiameter(int i) {
        float f = i;
        if (f != this.topEdgeTreatment.getFabDiameter()) {
            this.topEdgeTreatment.setFabDiameter(f);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setHideOnScroll(boolean z) {
        this.hideOnScroll = z;
    }

    @Override // android.support.v7.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
    }

    @Override // android.support.v7.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
    }
}
