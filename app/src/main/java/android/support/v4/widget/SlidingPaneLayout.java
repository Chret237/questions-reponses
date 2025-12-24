package android.support.v4.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout.class */
public class SlidingPaneLayout extends ViewGroup {
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private boolean mDisplayListReflectionLoaded;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private Method mGetDisplayList;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables;
    boolean mPreservedOpenState;
    private Field mRecreateDisplayList;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$AccessibilityDelegate.class */
    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();
        final SlidingPaneLayout this$0;

        AccessibilityDelegate(SlidingPaneLayout slidingPaneLayout) {
            this.this$0 = slidingPaneLayout;
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
            accessibilityNodeInfoCompat.setMovementGranularities(accessibilityNodeInfoCompat2.getMovementGranularities());
        }

        public boolean filter(View view) {
            return this.this$0.isDimmed(view);
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityNodeInfoCompat accessibilityNodeInfoCompatObtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompatObtain);
            copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompatObtain);
            accessibilityNodeInfoCompatObtain.recycle();
            accessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(view);
            Object parentForAccessibility = ViewCompat.getParentForAccessibility(view);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat.setParent((View) parentForAccessibility);
            }
            int childCount = this.this$0.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.this$0.getChildAt(i);
                if (!filter(childAt) && childAt.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(childAt, 1);
                    accessibilityNodeInfoCompat.addChild(childAt);
                }
            }
        }

        @Override // android.support.v4.view.AccessibilityDelegateCompat
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (filter(view)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$DisableLayerRunnable.class */
    private class DisableLayerRunnable implements Runnable {
        final View mChildView;
        final SlidingPaneLayout this$0;

        DisableLayerRunnable(SlidingPaneLayout slidingPaneLayout, View view) {
            this.this$0 = slidingPaneLayout;
            this.mChildView = view;
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
            if (this.mChildView.getParent() == this.this$0) {
                this.mChildView.setLayerType(0, null);
                this.this$0.invalidateChildRegion(this.mChildView);
            }
            this.this$0.mPostedRunnables.remove(this);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$DragHelperCallback.class */
    private class DragHelperCallback extends ViewDragHelper.Callback {
        final SlidingPaneLayout this$0;

        DragHelperCallback(SlidingPaneLayout slidingPaneLayout) {
            this.this$0 = slidingPaneLayout;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i, int i2) {
            int iMin;
            LayoutParams layoutParams = (LayoutParams) this.this$0.mSlideableView.getLayoutParams();
            if (this.this$0.isLayoutRtlSupport()) {
                int width = this.this$0.getWidth() - ((this.this$0.getPaddingRight() + layoutParams.rightMargin) + this.this$0.mSlideableView.getWidth());
                iMin = Math.max(Math.min(i, width), width - this.this$0.mSlideRange);
            } else {
                int paddingLeft = this.this$0.getPaddingLeft() + layoutParams.leftMargin;
                iMin = Math.min(Math.max(i, paddingLeft), this.this$0.mSlideRange + paddingLeft);
            }
            return iMin;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i, int i2) {
            return view.getTop();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return this.this$0.mSlideRange;
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onEdgeDragStarted(int i, int i2) {
            this.this$0.mDragHelper.captureChildView(this.this$0.mSlideableView, i2);
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewCaptured(View view, int i) {
            this.this$0.setAllChildrenVisible();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int i) {
            if (this.this$0.mDragHelper.getViewDragState() == 0) {
                if (this.this$0.mSlideOffset != 0.0f) {
                    SlidingPaneLayout slidingPaneLayout = this.this$0;
                    slidingPaneLayout.dispatchOnPanelOpened(slidingPaneLayout.mSlideableView);
                    this.this$0.mPreservedOpenState = true;
                } else {
                    SlidingPaneLayout slidingPaneLayout2 = this.this$0;
                    slidingPaneLayout2.updateObscuredViewsVisibility(slidingPaneLayout2.mSlideableView);
                    SlidingPaneLayout slidingPaneLayout3 = this.this$0;
                    slidingPaneLayout3.dispatchOnPanelClosed(slidingPaneLayout3.mSlideableView);
                    this.this$0.mPreservedOpenState = false;
                }
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
            this.this$0.onPanelDragged(i);
            this.this$0.invalidate();
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0043  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x00a1  */
        @Override // android.support.v4.widget.ViewDragHelper.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onViewReleased(android.view.View r5, float r6, float r7) {
            /*
                r4 = this;
                r0 = r5
                android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
                android.support.v4.widget.SlidingPaneLayout$LayoutParams r0 = (android.support.v4.widget.SlidingPaneLayout.LayoutParams) r0
                r10 = r0
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                boolean r0 = r0.isLayoutRtlSupport()
                if (r0 == 0) goto L6d
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                int r0 = r0.getPaddingRight()
                r1 = r10
                int r1 = r1.rightMargin
                int r0 = r0 + r1
                r9 = r0
                r0 = r6
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 < 0) goto L43
                r0 = r9
                r8 = r0
                r0 = r6
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 != 0) goto L4f
                r0 = r9
                r8 = r0
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                float r0 = r0.mSlideOffset
                r1 = 1056964608(0x3f000000, float:0.5)
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto L4f
            L43:
                r0 = r9
                r1 = r4
                android.support.v4.widget.SlidingPaneLayout r1 = r1.this$0
                int r1 = r1.mSlideRange
                int r0 = r0 + r1
                r8 = r0
            L4f:
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                android.view.View r0 = r0.mSlideableView
                int r0 = r0.getWidth()
                r9 = r0
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                int r0 = r0.getWidth()
                r1 = r8
                int r0 = r0 - r1
                r1 = r9
                int r0 = r0 - r1
                r8 = r0
                goto Lad
            L6d:
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                int r0 = r0.getPaddingLeft()
                r8 = r0
                r0 = r10
                int r0 = r0.leftMargin
                r1 = r8
                int r0 = r0 + r1
                r9 = r0
                r0 = r6
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 > 0) goto La1
                r0 = r9
                r8 = r0
                r0 = r6
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 != 0) goto Lad
                r0 = r9
                r8 = r0
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                float r0 = r0.mSlideOffset
                r1 = 1056964608(0x3f000000, float:0.5)
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto Lad
            La1:
                r0 = r9
                r1 = r4
                android.support.v4.widget.SlidingPaneLayout r1 = r1.this$0
                int r1 = r1.mSlideRange
                int r0 = r0 + r1
                r8 = r0
            Lad:
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                android.support.v4.widget.ViewDragHelper r0 = r0.mDragHelper
                r1 = r8
                r2 = r5
                int r2 = r2.getTop()
                boolean r0 = r0.settleCapturedViewAt(r1, r2)
                r0 = r4
                android.support.v4.widget.SlidingPaneLayout r0 = r0.this$0
                r0.invalidate()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.DragHelperCallback.onViewReleased(android.view.View, float, float):void");
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i) {
            if (this.this$0.mIsUnableToDrag) {
                return false;
            }
            return ((LayoutParams) view.getLayoutParams()).slideable;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = {R.attr.layout_weight};
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;

        public LayoutParams() {
            super(-1, -1);
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.weight = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.weight = 0.0f;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ATTRS);
            this.weight = typedArrayObtainStyledAttributes.getFloat(0, 0.0f);
            typedArrayObtainStyledAttributes.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.weight = 0.0f;
            this.weight = layoutParams.weight;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.weight = 0.0f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.weight = 0.0f;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$PanelSlideListener.class */
    public interface PanelSlideListener {
        void onPanelClosed(View view);

        void onPanelOpened(View view);

        void onPanelSlide(View view, float f);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$SavedState.class */
    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v4.widget.SlidingPaneLayout.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean isOpen;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isOpen = parcel.readInt() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isOpen ? 1 : 0);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/SlidingPaneLayout$SimplePanelSlideListener.class */
    public static class SimplePanelSlideListener implements PanelSlideListener {
        @Override // android.support.v4.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelClosed(View view) {
        }

        @Override // android.support.v4.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelOpened(View view) {
        }

        @Override // android.support.v4.widget.SlidingPaneLayout.PanelSlideListener
        public void onPanelSlide(View view, float f) {
        }
    }

    public SlidingPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSliderFadeColor = DEFAULT_FADE_COLOR;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList<>();
        float f = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int) ((32.0f * f) + 0.5f);
        setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate(this));
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewDragHelper viewDragHelperCreate = ViewDragHelper.create(this, 0.5f, new DragHelperCallback(this));
        this.mDragHelper = viewDragHelperCreate;
        viewDragHelperCreate.setMinVelocity(f * 400.0f);
    }

    private boolean closePane(View view, int i) {
        if (!this.mFirstLayout && !smoothSlideTo(0.0f, i)) {
            return false;
        }
        this.mPreservedOpenState = false;
        return true;
    }

    private void dimChildView(View view, float f, int i) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (f <= 0.0f || i == 0) {
            if (view.getLayerType() != 0) {
                if (layoutParams.dimPaint != null) {
                    layoutParams.dimPaint.setColorFilter(null);
                }
                DisableLayerRunnable disableLayerRunnable = new DisableLayerRunnable(this, view);
                this.mPostedRunnables.add(disableLayerRunnable);
                ViewCompat.postOnAnimation(this, disableLayerRunnable);
                return;
            }
            return;
        }
        int i2 = (int) ((((-16777216) & i) >>> 24) * f);
        if (layoutParams.dimPaint == null) {
            layoutParams.dimPaint = new Paint();
        }
        layoutParams.dimPaint.setColorFilter(new PorterDuffColorFilter((i2 << 24) | (i & ViewCompat.MEASURED_SIZE_MASK), PorterDuff.Mode.SRC_OVER));
        if (view.getLayerType() != 2) {
            view.setLayerType(2, layoutParams.dimPaint);
        }
        invalidateChildRegion(view);
    }

    private boolean openPane(View view, int i) {
        if (!this.mFirstLayout && !smoothSlideTo(1.0f, i)) {
            return false;
        }
        this.mPreservedOpenState = true;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parallaxOtherViews(float r6) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 200
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.parallaxOtherViews(float):void");
    }

    private static boolean viewIsOpaque(View view) {
        Drawable background;
        boolean z = true;
        if (view.isOpaque()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 18 || (background = view.getBackground()) == null) {
            return false;
        }
        if (background.getOpacity() != -1) {
            z = false;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean canScroll(android.view.View r9, boolean r10, int r11, int r12, int r13) {
        /*
            r8 = this;
            r0 = r9
            boolean r0 = r0 instanceof android.view.ViewGroup
            r20 = r0
            r0 = 1
            r19 = r0
            r0 = r20
            if (r0 == 0) goto L90
            r0 = r9
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            r22 = r0
            r0 = r9
            int r0 = r0.getScrollX()
            r16 = r0
            r0 = r9
            int r0 = r0.getScrollY()
            r15 = r0
            r0 = r22
            int r0 = r0.getChildCount()
            r1 = 1
            int r0 = r0 - r1
            r14 = r0
        L29:
            r0 = r14
            if (r0 < 0) goto L90
            r0 = r22
            r1 = r14
            android.view.View r0 = r0.getChildAt(r1)
            r21 = r0
            r0 = r12
            r1 = r16
            int r0 = r0 + r1
            r18 = r0
            r0 = r18
            r1 = r21
            int r1 = r1.getLeft()
            if (r0 < r1) goto L8a
            r0 = r18
            r1 = r21
            int r1 = r1.getRight()
            if (r0 >= r1) goto L8a
            r0 = r13
            r1 = r15
            int r0 = r0 + r1
            r17 = r0
            r0 = r17
            r1 = r21
            int r1 = r1.getTop()
            if (r0 < r1) goto L8a
            r0 = r17
            r1 = r21
            int r1 = r1.getBottom()
            if (r0 >= r1) goto L8a
            r0 = r8
            r1 = r21
            r2 = 1
            r3 = r11
            r4 = r18
            r5 = r21
            int r5 = r5.getLeft()
            int r4 = r4 - r5
            r5 = r17
            r6 = r21
            int r6 = r6.getTop()
            int r5 = r5 - r6
            boolean r0 = r0.canScroll(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L8a
            r0 = 1
            return r0
        L8a:
            int r14 = r14 + (-1)
            goto L29
        L90:
            r0 = r10
            if (r0 == 0) goto Laf
            r0 = r8
            boolean r0 = r0.isLayoutRtlSupport()
            if (r0 == 0) goto L9e
            goto La1
        L9e:
            r0 = r11
            int r0 = -r0
            r11 = r0
        La1:
            r0 = r9
            r1 = r11
            boolean r0 = r0.canScrollHorizontally(r1)
            if (r0 == 0) goto Laf
            r0 = r19
            r10 = r0
            goto Lb1
        Laf:
            r0 = 0
            r10 = r0
        Lb1:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.canScroll(android.view.View, boolean, int, int, int):boolean");
    }

    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    public boolean closePane() {
        return closePane(this.mSlideableView, 0);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mDragHelper.continueSettling(true)) {
            if (this.mCanSlide) {
                ViewCompat.postInvalidateOnAnimation(this);
            } else {
                this.mDragHelper.abort();
            }
        }
    }

    void dispatchOnPanelClosed(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelClosed(view);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelOpened(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelOpened(view);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelSlide(View view) {
        PanelSlideListener panelSlideListener = this.mPanelSlideListener;
        if (panelSlideListener != null) {
            panelSlideListener.onPanelSlide(view, this.mSlideOffset);
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int i;
        int right;
        super.draw(canvas);
        Drawable drawable = isLayoutRtlSupport() ? this.mShadowDrawableRight : this.mShadowDrawableLeft;
        View childAt = getChildCount() > 1 ? getChildAt(1) : null;
        if (childAt == null || drawable == null) {
            return;
        }
        int top = childAt.getTop();
        int bottom = childAt.getBottom();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (isLayoutRtlSupport()) {
            right = childAt.getRight();
            i = intrinsicWidth + right;
        } else {
            int left = childAt.getLeft();
            i = left;
            right = left - intrinsicWidth;
        }
        drawable.setBounds(right, top, i, bottom);
        drawable.draw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int iSave = canvas.save();
        if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (isLayoutRtlSupport()) {
                Rect rect = this.mTmpRect;
                rect.left = Math.max(rect.left, this.mSlideableView.getRight());
            } else {
                Rect rect2 = this.mTmpRect;
                rect2.right = Math.min(rect2.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        boolean zDrawChild = super.drawChild(canvas, view, j);
        canvas.restoreToCount(iSave);
        return zDrawChild;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public int getParallaxDistance() {
        return this.mParallaxBy;
    }

    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }

    void invalidateChildRegion(View view) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        Field field;
        if (Build.VERSION.SDK_INT >= 17) {
            ViewCompat.setLayerPaint(view, ((LayoutParams) view.getLayoutParams()).dimPaint);
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            if (!this.mDisplayListReflectionLoaded) {
                try {
                    this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
                }
                try {
                    Field declaredField = View.class.getDeclaredField("mRecreateDisplayList");
                    this.mRecreateDisplayList = declaredField;
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e2) {
                    Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e2);
                }
                this.mDisplayListReflectionLoaded = true;
            }
            if (this.mGetDisplayList == null || (field = this.mRecreateDisplayList) == null) {
                view.invalidate();
                return;
            }
            try {
                field.setBoolean(view, true);
                this.mGetDisplayList.invoke(view, (Object[]) null);
            } catch (Exception e3) {
                Log.e(TAG, "Error refreshing display list state", e3);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    boolean isDimmed(View view) {
        if (view == null) {
            return false;
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        boolean z = false;
        if (this.mCanSlide) {
            z = false;
            if (layoutParams.dimWhenOffset) {
                z = false;
                if (this.mSlideOffset > 0.0f) {
                    z = true;
                }
            }
        }
        return z;
    }

    boolean isLayoutRtlSupport() {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        return z;
    }

    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 1.0f;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        int size = this.mPostedRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mPostedRunnables.get(i).run();
        }
        this.mPostedRunnables.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0106  */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r7) {
        /*
            Method dump skipped, instructions count: 302
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        int i5;
        int i6;
        int i7;
        int i8;
        boolean zIsLayoutRtlSupport = isLayoutRtlSupport();
        if (zIsLayoutRtlSupport) {
            this.mDragHelper.setEdgeTrackingEnabled(2);
        } else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        int i9 = i3 - i;
        int paddingRight = zIsLayoutRtlSupport ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = zIsLayoutRtlSupport ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        if (this.mFirstLayout) {
            this.mSlideOffset = (this.mCanSlide && this.mPreservedOpenState) ? 1.0f : 0.0f;
        }
        int i10 = paddingRight;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                if (layoutParams.slideable) {
                    int i12 = layoutParams.leftMargin;
                    int i13 = layoutParams.rightMargin;
                    int i14 = i9 - paddingLeft;
                    int iMin = (Math.min(paddingRight, i14 - this.mOverhangSize) - i10) - (i12 + i13);
                    this.mSlideRange = iMin;
                    int i15 = zIsLayoutRtlSupport ? layoutParams.rightMargin : layoutParams.leftMargin;
                    layoutParams.dimWhenOffset = ((i10 + i15) + iMin) + (measuredWidth / 2) > i14;
                    int i16 = (int) (iMin * this.mSlideOffset);
                    i10 += i15 + i16;
                    this.mSlideOffset = i16 / this.mSlideRange;
                    i5 = 0;
                } else if (!this.mCanSlide || (i6 = this.mParallaxBy) == 0) {
                    i10 = paddingRight;
                    i5 = 0;
                } else {
                    i5 = (int) ((1.0f - this.mSlideOffset) * i6);
                    i10 = paddingRight;
                }
                if (zIsLayoutRtlSupport) {
                    i8 = (i9 - i10) + i5;
                    i7 = i8 - measuredWidth;
                } else {
                    i7 = i10 - i5;
                    i8 = i7 + measuredWidth;
                }
                childAt.layout(i7, paddingTop, i8, childAt.getMeasuredHeight() + paddingTop);
                paddingRight += childAt.getWidth();
            }
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams) this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            } else {
                for (int i17 = 0; i17 < childCount; i17++) {
                    dimChildView(getChildAt(i17), 0.0f, this.mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0162 A[PHI: r8
  0x0162: PHI (r8v1 float) = (r8v0 float), (r8v2 float) binds: [B:40:0x0147, B:42:0x015c] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onMeasure(int r6, int r7) {
        /*
            Method dump skipped, instructions count: 1083
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.SlidingPaneLayout.onMeasure(int, int):void");
    }

    void onPanelDragged(int i) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
            return;
        }
        boolean zIsLayoutRtlSupport = isLayoutRtlSupport();
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        int width = this.mSlideableView.getWidth();
        int width2 = i;
        if (zIsLayoutRtlSupport) {
            width2 = (getWidth() - i) - width;
        }
        float paddingRight = (width2 - ((zIsLayoutRtlSupport ? getPaddingRight() : getPaddingLeft()) + (zIsLayoutRtlSupport ? layoutParams.rightMargin : layoutParams.leftMargin))) / this.mSlideRange;
        this.mSlideOffset = paddingRight;
        if (this.mParallaxBy != 0) {
            parallaxOtherViews(paddingRight);
        }
        if (layoutParams.dimWhenOffset) {
            dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
        }
        dispatchOnPanelSlide(this.mSlideableView);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.isOpen) {
            openPane();
        } else {
            closePane();
        }
        this.mPreservedOpenState = savedState.isOpen;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isOpen = isSlideable() ? isOpen() : this.mPreservedOpenState;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            this.mFirstLayout = true;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mCanSlide) {
            return super.onTouchEvent(motionEvent);
        }
        this.mDragHelper.processTouchEvent(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            this.mInitialMotionX = x;
            this.mInitialMotionY = y;
            return true;
        }
        if (actionMasked != 1 || !isDimmed(this.mSlideableView)) {
            return true;
        }
        float x2 = motionEvent.getX();
        float y2 = motionEvent.getY();
        float f = x2 - this.mInitialMotionX;
        float f2 = y2 - this.mInitialMotionY;
        int touchSlop = this.mDragHelper.getTouchSlop();
        if ((f * f) + (f2 * f2) >= touchSlop * touchSlop || !this.mDragHelper.isViewUnder(this.mSlideableView, (int) x2, (int) y2)) {
            return true;
        }
        closePane(this.mSlideableView, 0);
        return true;
    }

    public boolean openPane() {
        return openPane(this.mSlideableView, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        if (isInTouchMode() || this.mCanSlide) {
            return;
        }
        this.mPreservedOpenState = view == this.mSlideableView;
    }

    void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 4) {
                childAt.setVisibility(0);
            }
        }
    }

    public void setCoveredFadeColor(int i) {
        this.mCoveredFadeColor = i;
    }

    public void setPanelSlideListener(PanelSlideListener panelSlideListener) {
        this.mPanelSlideListener = panelSlideListener;
    }

    public void setParallaxDistance(int i) {
        this.mParallaxBy = i;
        requestLayout();
    }

    @Deprecated
    public void setShadowDrawable(Drawable drawable) {
        setShadowDrawableLeft(drawable);
    }

    public void setShadowDrawableLeft(Drawable drawable) {
        this.mShadowDrawableLeft = drawable;
    }

    public void setShadowDrawableRight(Drawable drawable) {
        this.mShadowDrawableRight = drawable;
    }

    @Deprecated
    public void setShadowResource(int i) {
        setShadowDrawable(getResources().getDrawable(i));
    }

    public void setShadowResourceLeft(int i) {
        setShadowDrawableLeft(ContextCompat.getDrawable(getContext(), i));
    }

    public void setShadowResourceRight(int i) {
        setShadowDrawableRight(ContextCompat.getDrawable(getContext(), i));
    }

    public void setSliderFadeColor(int i) {
        this.mSliderFadeColor = i;
    }

    @Deprecated
    public void smoothSlideClosed() {
        closePane();
    }

    @Deprecated
    public void smoothSlideOpen() {
        openPane();
    }

    boolean smoothSlideTo(float f, int i) {
        int paddingLeft;
        if (!this.mCanSlide) {
            return false;
        }
        boolean zIsLayoutRtlSupport = isLayoutRtlSupport();
        LayoutParams layoutParams = (LayoutParams) this.mSlideableView.getLayoutParams();
        if (zIsLayoutRtlSupport) {
            paddingLeft = (int) (getWidth() - (((getPaddingRight() + layoutParams.rightMargin) + (f * this.mSlideRange)) + this.mSlideableView.getWidth()));
        } else {
            paddingLeft = (int) (getPaddingLeft() + layoutParams.leftMargin + (f * this.mSlideRange));
        }
        ViewDragHelper viewDragHelper = this.mDragHelper;
        View view = this.mSlideableView;
        if (!viewDragHelper.smoothSlideViewTo(view, paddingLeft, view.getTop())) {
            return false;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    void updateObscuredViewsVisibility(View view) {
        int left;
        int right;
        int top;
        int bottom;
        View childAt;
        boolean zIsLayoutRtlSupport = isLayoutRtlSupport();
        int width = zIsLayoutRtlSupport ? getWidth() - getPaddingRight() : getPaddingLeft();
        int paddingLeft = zIsLayoutRtlSupport ? getPaddingLeft() : getWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int height = getHeight();
        int paddingBottom = getPaddingBottom();
        if (view == null || !viewIsOpaque(view)) {
            left = 0;
            right = 0;
            top = 0;
            bottom = 0;
        } else {
            left = view.getLeft();
            right = view.getRight();
            top = view.getTop();
            bottom = view.getBottom();
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount && (childAt = getChildAt(i)) != view; i++) {
            if (childAt.getVisibility() != 8) {
                childAt.setVisibility((Math.max(zIsLayoutRtlSupport ? paddingLeft : width, childAt.getLeft()) < left || Math.max(paddingTop, childAt.getTop()) < top || Math.min(zIsLayoutRtlSupport ? width : paddingLeft, childAt.getRight()) > right || Math.min(height - paddingBottom, childAt.getBottom()) > bottom) ? 0 : 4);
            }
        }
    }
}
