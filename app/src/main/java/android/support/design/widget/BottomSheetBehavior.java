package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.C0029R;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomSheetBehavior.class */
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HALF_EXPANDED = 6;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int activePointerId;
    private BottomSheetCallback callback;
    int collapsedOffset;
    private final ViewDragHelper.Callback dragCallback;
    private boolean fitToContents;
    int fitToContentsOffset;
    int halfExpandedOffset;
    boolean hideable;
    private boolean ignoreEvents;
    private Map<View, Integer> importantForAccessibilityMap;
    private int initialY;
    private int lastNestedScrollDy;
    private int lastPeekHeight;
    private float maximumVelocity;
    private boolean nestedScrolled;
    WeakReference<View> nestedScrollingChildRef;
    int parentHeight;
    private int peekHeight;
    private boolean peekHeightAuto;
    private int peekHeightMin;
    private boolean skipCollapsed;
    int state;
    boolean touchingScrollingChild;
    private VelocityTracker velocityTracker;
    ViewDragHelper viewDragHelper;
    WeakReference<V> viewRef;

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomSheetBehavior$BottomSheetCallback.class */
    public static abstract class BottomSheetCallback {
        public abstract void onSlide(View view, float f);

        public abstract void onStateChanged(View view, int i);
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomSheetBehavior$SavedState.class */
    protected static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.design.widget.BottomSheetBehavior.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
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
        final int state;

        public SavedState(Parcel parcel) {
            this(parcel, (ClassLoader) null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }

        public SavedState(Parcelable parcelable, int i) {
            super(parcelable);
            this.state = i;
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.state);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomSheetBehavior$SettleRunnable.class */
    private class SettleRunnable implements Runnable {
        private final int targetState;
        final BottomSheetBehavior this$0;
        private final View view;

        SettleRunnable(BottomSheetBehavior bottomSheetBehavior, View view, int i) {
            this.this$0 = bottomSheetBehavior;
            this.view = view;
            this.targetState = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.viewDragHelper == null || !this.this$0.viewDragHelper.continueSettling(true)) {
                this.this$0.setStateInternal(this.targetState);
            } else {
                ViewCompat.postOnAnimation(this.view, this);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomSheetBehavior$State.class */
    public @interface State {
    }

    public BottomSheetBehavior() {
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback(this) { // from class: android.support.design.widget.BottomSheetBehavior.2
            final BottomSheetBehavior this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int clampViewPositionHorizontal(View view, int i, int i2) {
                return view.getLeft();
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int clampViewPositionVertical(View view, int i, int i2) {
                return android.support.v4.math.MathUtils.clamp(i, this.this$0.getExpandedOffset(), this.this$0.hideable ? this.this$0.parentHeight : this.this$0.collapsedOffset);
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int getViewVerticalDragRange(View view) {
                return this.this$0.hideable ? this.this$0.parentHeight : this.this$0.collapsedOffset;
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public void onViewDragStateChanged(int i) {
                if (i == 1) {
                    this.this$0.setStateInternal(1);
                }
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
                this.this$0.dispatchOnSlide(i2);
            }

            /* JADX WARN: Removed duplicated region for block: B:46:0x0177  */
            /* JADX WARN: Removed duplicated region for block: B:47:0x0194  */
            @Override // android.support.v4.widget.ViewDragHelper.Callback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onViewReleased(android.view.View r8, float r9, float r10) {
                /*
                    Method dump skipped, instructions count: 414
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.BottomSheetBehavior.C00802.onViewReleased(android.view.View, float, float):void");
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public boolean tryCaptureView(View view, int i) {
                View view2;
                boolean z = true;
                if (this.this$0.state == 1 || this.this$0.touchingScrollingChild) {
                    return false;
                }
                if (this.this$0.state == 3 && this.this$0.activePointerId == i && (view2 = this.this$0.nestedScrollingChildRef.get()) != null && view2.canScrollVertically(-1)) {
                    return false;
                }
                if (this.this$0.viewRef == null || this.this$0.viewRef.get() != view) {
                    z = false;
                }
                return z;
            }
        };
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.fitToContents = true;
        this.state = 4;
        this.dragCallback = new ViewDragHelper.Callback(this) { // from class: android.support.design.widget.BottomSheetBehavior.2
            final BottomSheetBehavior this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int clampViewPositionHorizontal(View view, int i, int i2) {
                return view.getLeft();
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int clampViewPositionVertical(View view, int i, int i2) {
                return android.support.v4.math.MathUtils.clamp(i, this.this$0.getExpandedOffset(), this.this$0.hideable ? this.this$0.parentHeight : this.this$0.collapsedOffset);
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public int getViewVerticalDragRange(View view) {
                return this.this$0.hideable ? this.this$0.parentHeight : this.this$0.collapsedOffset;
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public void onViewDragStateChanged(int i) {
                if (i == 1) {
                    this.this$0.setStateInternal(1);
                }
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
                this.this$0.dispatchOnSlide(i2);
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public void onViewReleased(View v, float v2, float v3) {
                /*
                    Method dump skipped, instructions count: 414
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.BottomSheetBehavior.C00802.onViewReleased(android.view.View, float, float):void");
            }

            @Override // android.support.v4.widget.ViewDragHelper.Callback
            public boolean tryCaptureView(View view, int i) {
                View view2;
                boolean z = true;
                if (this.this$0.state == 1 || this.this$0.touchingScrollingChild) {
                    return false;
                }
                if (this.this$0.state == 3 && this.this$0.activePointerId == i && (view2 = this.this$0.nestedScrollingChildRef.get()) != null && view2.canScrollVertically(-1)) {
                    return false;
                }
                if (this.this$0.viewRef == null || this.this$0.viewRef.get() != view) {
                    z = false;
                }
                return z;
            }
        };
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0029R.styleable.BottomSheetBehavior_Layout);
        TypedValue typedValuePeekValue = typedArrayObtainStyledAttributes.peekValue(C0029R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (typedValuePeekValue == null || typedValuePeekValue.data != -1) {
            setPeekHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        } else {
            setPeekHeight(typedValuePeekValue.data);
        }
        setHideable(typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        setFitToContents(typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.BottomSheetBehavior_Layout_behavior_fitToContents, true));
        setSkipCollapsed(typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        typedArrayObtainStyledAttributes.recycle();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    private void calculateCollapsedOffset() {
        if (this.fitToContents) {
            this.collapsedOffset = Math.max(this.parentHeight - this.lastPeekHeight, this.fitToContentsOffset);
        } else {
            this.collapsedOffset = this.parentHeight - this.lastPeekHeight;
        }
    }

    public static <V extends View> BottomSheetBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior) behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getExpandedOffset() {
        return this.fitToContents ? this.fitToContentsOffset : 0;
    }

    private float getYVelocity() {
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.maximumVelocity);
        return this.velocityTracker.getYVelocity(this.activePointerId);
    }

    private void reset() {
        this.activePointerId = -1;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
    }

    private void updateImportantForAccessibility(boolean z) {
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (Build.VERSION.SDK_INT >= 16 && z) {
                if (this.importantForAccessibilityMap != null) {
                    return;
                } else {
                    this.importantForAccessibilityMap = new HashMap(childCount);
                }
            }
            for (int i = 0; i < childCount; i++) {
                View childAt = coordinatorLayout.getChildAt(i);
                if (childAt != this.viewRef.get()) {
                    if (z) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            this.importantForAccessibilityMap.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        }
                        ViewCompat.setImportantForAccessibility(childAt, 4);
                    } else {
                        Map<View, Integer> map = this.importantForAccessibilityMap;
                        if (map != null && map.containsKey(childAt)) {
                            ViewCompat.setImportantForAccessibility(childAt, this.importantForAccessibilityMap.get(childAt).intValue());
                        }
                    }
                }
            }
            if (z) {
                return;
            }
            this.importantForAccessibilityMap = null;
        }
    }

    void dispatchOnSlide(int i) {
        BottomSheetCallback bottomSheetCallback;
        V v = this.viewRef.get();
        if (v == null || (bottomSheetCallback = this.callback) == null) {
            return;
        }
        if (i > this.collapsedOffset) {
            bottomSheetCallback.onSlide(v, (r0 - i) / (this.parentHeight - r0));
        } else {
            bottomSheetCallback.onSlide(v, (r0 - i) / (r0 - getExpandedOffset()));
        }
    }

    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View viewFindScrollingChild = findScrollingChild(viewGroup.getChildAt(i));
            if (viewFindScrollingChild != null) {
                return viewFindScrollingChild;
            }
        }
        return null;
    }

    public final int getPeekHeight() {
        return this.peekHeightAuto ? -1 : this.peekHeight;
    }

    int getPeekHeightMin() {
        return this.peekHeightMin;
    }

    public boolean getSkipCollapsed() {
        return this.skipCollapsed;
    }

    public final int getState() {
        return this.state;
    }

    public boolean isFitToContents() {
        return this.fitToContents;
    }

    public boolean isHideable() {
        return this.hideable;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper;
        if (!v.isShown()) {
            this.ignoreEvents = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 0) {
            int x = (int) motionEvent.getX();
            this.initialY = (int) motionEvent.getY();
            WeakReference<View> weakReference = this.nestedScrollingChildRef;
            View view = weakReference != null ? weakReference.get() : null;
            if (view != null && coordinatorLayout.isPointInChildBounds(view, x, this.initialY)) {
                this.activePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                this.touchingScrollingChild = true;
            }
            this.ignoreEvents = this.activePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v, x, this.initialY);
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.touchingScrollingChild = false;
            this.activePointerId = -1;
            if (this.ignoreEvents) {
                this.ignoreEvents = false;
                return false;
            }
        }
        if (!this.ignoreEvents && (viewDragHelper = this.viewDragHelper) != null && viewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        WeakReference<View> weakReference2 = this.nestedScrollingChildRef;
        View view2 = null;
        if (weakReference2 != null) {
            view2 = weakReference2.get();
        }
        boolean z = false;
        if (actionMasked == 2) {
            z = false;
            if (view2 != null) {
                z = false;
                if (!this.ignoreEvents) {
                    z = false;
                    if (this.state != 1) {
                        z = false;
                        if (!coordinatorLayout.isPointInChildBounds(view2, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                            z = false;
                            if (this.viewDragHelper != null) {
                                z = false;
                                if (Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
                                    z = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int i) {
        if (ViewCompat.getFitsSystemWindows(coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v)) {
            v.setFitsSystemWindows(true);
        }
        int top = v.getTop();
        coordinatorLayout.onLayoutChild(v, i);
        this.parentHeight = coordinatorLayout.getHeight();
        if (this.peekHeightAuto) {
            if (this.peekHeightMin == 0) {
                this.peekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(C0029R.dimen.design_bottom_sheet_peek_height_min);
            }
            this.lastPeekHeight = Math.max(this.peekHeightMin, this.parentHeight - ((coordinatorLayout.getWidth() * 9) / 16));
        } else {
            this.lastPeekHeight = this.peekHeight;
        }
        this.fitToContentsOffset = Math.max(0, this.parentHeight - v.getHeight());
        this.halfExpandedOffset = this.parentHeight / 2;
        calculateCollapsedOffset();
        int i2 = this.state;
        if (i2 == 3) {
            ViewCompat.offsetTopAndBottom(v, getExpandedOffset());
        } else if (i2 == 6) {
            ViewCompat.offsetTopAndBottom(v, this.halfExpandedOffset);
        } else if (this.hideable && i2 == 5) {
            ViewCompat.offsetTopAndBottom(v, this.parentHeight);
        } else {
            int i3 = this.state;
            if (i3 == 4) {
                ViewCompat.offsetTopAndBottom(v, this.collapsedOffset);
            } else if (i3 == 1 || i3 == 2) {
                ViewCompat.offsetTopAndBottom(v, top - v.getTop());
            }
        }
        if (this.viewDragHelper == null) {
            this.viewDragHelper = ViewDragHelper.create(coordinatorLayout, this.dragCallback);
        }
        this.viewRef = new WeakReference<>(v);
        this.nestedScrollingChildRef = new WeakReference<>(findScrollingChild(v));
        return true;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
        return view == this.nestedScrollingChildRef.get() && (this.state != 3 || super.onNestedPreFling(coordinatorLayout, v, view, f, f2));
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
        if (i3 != 1 && view == this.nestedScrollingChildRef.get()) {
            int top = v.getTop();
            int i4 = top - i2;
            if (i2 > 0) {
                if (i4 < getExpandedOffset()) {
                    iArr[1] = top - getExpandedOffset();
                    ViewCompat.offsetTopAndBottom(v, -iArr[1]);
                    setStateInternal(3);
                } else {
                    iArr[1] = i2;
                    ViewCompat.offsetTopAndBottom(v, -i2);
                    setStateInternal(1);
                }
            } else if (i2 < 0 && !view.canScrollVertically(-1)) {
                int i5 = this.collapsedOffset;
                if (i4 <= i5 || this.hideable) {
                    iArr[1] = i2;
                    ViewCompat.offsetTopAndBottom(v, -i2);
                    setStateInternal(1);
                } else {
                    iArr[1] = top - i5;
                    ViewCompat.offsetTopAndBottom(v, -iArr[1]);
                    setStateInternal(4);
                }
            }
            dispatchOnSlide(v.getTop());
            this.lastNestedScrollDy = i2;
            this.nestedScrolled = true;
        }
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, savedState.getSuperState());
        if (savedState.state == 1 || savedState.state == 2) {
            this.state = 4;
        } else {
            this.state = savedState.state;
        }
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.state);
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i, int i2) {
        boolean z = false;
        this.lastNestedScrollDy = 0;
        this.nestedScrolled = false;
        if ((i & 2) != 0) {
            z = true;
        }
        return z;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i) {
        int expandedOffset;
        int i2 = 3;
        if (v.getTop() == getExpandedOffset()) {
            setStateInternal(3);
            return;
        }
        if (view == this.nestedScrollingChildRef.get() && this.nestedScrolled) {
            if (this.lastNestedScrollDy > 0) {
                expandedOffset = getExpandedOffset();
            } else if (this.hideable && shouldHide(v, getYVelocity())) {
                expandedOffset = this.parentHeight;
                i2 = 5;
            } else {
                if (this.lastNestedScrollDy == 0) {
                    int top = v.getTop();
                    if (!this.fitToContents) {
                        int i3 = this.halfExpandedOffset;
                        if (top < i3) {
                            if (top < Math.abs(top - this.collapsedOffset)) {
                                expandedOffset = 0;
                            } else {
                                expandedOffset = this.halfExpandedOffset;
                            }
                        } else if (Math.abs(top - i3) < Math.abs(top - this.collapsedOffset)) {
                            expandedOffset = this.halfExpandedOffset;
                        } else {
                            expandedOffset = this.collapsedOffset;
                        }
                        i2 = 6;
                    } else if (Math.abs(top - this.fitToContentsOffset) < Math.abs(top - this.collapsedOffset)) {
                        expandedOffset = this.fitToContentsOffset;
                    } else {
                        expandedOffset = this.collapsedOffset;
                    }
                } else {
                    expandedOffset = this.collapsedOffset;
                }
                i2 = 4;
            }
            if (this.viewDragHelper.smoothSlideViewTo(v, v.getLeft(), expandedOffset)) {
                setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable(this, v, i2));
            } else {
                setStateInternal(i2);
            }
            this.nestedScrolled = false;
        }
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (this.state == 1 && actionMasked == 0) {
            return true;
        }
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper != null) {
            viewDragHelper.processTouchEvent(motionEvent);
        }
        if (actionMasked == 0) {
            reset();
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.ignoreEvents && Math.abs(this.initialY - motionEvent.getY()) > this.viewDragHelper.getTouchSlop()) {
            this.viewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return !this.ignoreEvents;
    }

    public void setBottomSheetCallback(BottomSheetCallback bottomSheetCallback) {
        this.callback = bottomSheetCallback;
    }

    public void setFitToContents(boolean z) {
        if (this.fitToContents == z) {
            return;
        }
        this.fitToContents = z;
        if (this.viewRef != null) {
            calculateCollapsedOffset();
        }
        setStateInternal((this.fitToContents && this.state == 6) ? 3 : this.state);
    }

    public void setHideable(boolean z) {
        this.hideable = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void setPeekHeight(int r5) {
        /*
            r4 = this;
            r0 = 1
            r6 = r0
            r0 = r5
            r1 = -1
            if (r0 != r1) goto L18
            r0 = r4
            boolean r0 = r0.peekHeightAuto
            if (r0 != 0) goto L2a
            r0 = r4
            r1 = 1
            r0.peekHeightAuto = r1
            r0 = r6
            r5 = r0
            goto L49
        L18:
            r0 = r4
            boolean r0 = r0.peekHeightAuto
            if (r0 != 0) goto L2f
            r0 = r4
            int r0 = r0.peekHeight
            r1 = r5
            if (r0 == r1) goto L2a
            goto L2f
        L2a:
            r0 = 0
            r5 = r0
            goto L49
        L2f:
            r0 = r4
            r1 = 0
            r0.peekHeightAuto = r1
            r0 = r4
            r1 = 0
            r2 = r5
            int r1 = java.lang.Math.max(r1, r2)
            r0.peekHeight = r1
            r0 = r4
            r1 = r4
            int r1 = r1.parentHeight
            r2 = r5
            int r1 = r1 - r2
            r0.collapsedOffset = r1
            r0 = r6
            r5 = r0
        L49:
            r0 = r5
            if (r0 == 0) goto L6e
            r0 = r4
            int r0 = r0.state
            r1 = 4
            if (r0 != r1) goto L6e
            r0 = r4
            java.lang.ref.WeakReference<V extends android.view.View> r0 = r0.viewRef
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L6e
            r0 = r7
            java.lang.Object r0 = r0.get()
            android.view.View r0 = (android.view.View) r0
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L6e
            r0 = r7
            r0.requestLayout()
        L6e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.BottomSheetBehavior.setPeekHeight(int):void");
    }

    public void setSkipCollapsed(boolean z) {
        this.skipCollapsed = z;
    }

    public final void setState(int i) {
        if (i == this.state) {
            return;
        }
        WeakReference<V> weakReference = this.viewRef;
        if (weakReference == null) {
            if (i == 4 || i == 3 || i == 6 || (this.hideable && i == 5)) {
                this.state = i;
                return;
            }
            return;
        }
        V v = weakReference.get();
        if (v == null) {
            return;
        }
        ViewParent parent = v.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(v)) {
            v.post(new Runnable(this, v, i) { // from class: android.support.design.widget.BottomSheetBehavior.1
                final BottomSheetBehavior this$0;
                final View val$child;
                final int val$finalState;

                {
                    this.this$0 = this;
                    this.val$child = v;
                    this.val$finalState = i;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.this$0.startSettlingAnimation(this.val$child, this.val$finalState);
                }
            });
        } else {
            startSettlingAnimation(v, i);
        }
    }

    void setStateInternal(int i) {
        BottomSheetCallback bottomSheetCallback;
        if (this.state == i) {
            return;
        }
        this.state = i;
        if (i == 6 || i == 3) {
            updateImportantForAccessibility(true);
        } else if (i == 5 || i == 4) {
            updateImportantForAccessibility(false);
        }
        V v = this.viewRef.get();
        if (v == null || (bottomSheetCallback = this.callback) == null) {
            return;
        }
        bottomSheetCallback.onStateChanged(v, i);
    }

    boolean shouldHide(View view, float f) {
        boolean z = true;
        if (this.skipCollapsed) {
            return true;
        }
        if (view.getTop() < this.collapsedOffset) {
            return false;
        }
        if (Math.abs((view.getTop() + (f * HIDE_FRICTION)) - this.collapsedOffset) / this.peekHeight <= HIDE_THRESHOLD) {
            z = false;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void startSettlingAnimation(android.view.View r8, int r9) {
        /*
            r7 = this;
            r0 = r9
            r1 = 4
            if (r0 != r1) goto Ld
            r0 = r7
            int r0 = r0.collapsedOffset
            r10 = r0
            goto L54
        Ld:
            r0 = r9
            r1 = 6
            if (r0 != r1) goto L36
            r0 = r7
            int r0 = r0.halfExpandedOffset
            r11 = r0
            r0 = r7
            boolean r0 = r0.fitToContents
            if (r0 == 0) goto L30
            r0 = r7
            int r0 = r0.fitToContentsOffset
            r10 = r0
            r0 = r11
            r1 = r10
            if (r0 > r1) goto L30
            r0 = 3
            r9 = r0
            goto L54
        L30:
            r0 = r11
            r10 = r0
            goto L54
        L36:
            r0 = r9
            r1 = 3
            if (r0 != r1) goto L43
            r0 = r7
            int r0 = r0.getExpandedOffset()
            r10 = r0
            goto L54
        L43:
            r0 = r7
            boolean r0 = r0.hideable
            if (r0 == 0) goto L80
            r0 = r9
            r1 = 5
            if (r0 != r1) goto L80
            r0 = r7
            int r0 = r0.parentHeight
            r10 = r0
        L54:
            r0 = r7
            android.support.v4.widget.ViewDragHelper r0 = r0.viewDragHelper
            r1 = r8
            r2 = r8
            int r2 = r2.getLeft()
            r3 = r10
            boolean r0 = r0.smoothSlideViewTo(r1, r2, r3)
            if (r0 == 0) goto L7a
            r0 = r7
            r1 = 2
            r0.setStateInternal(r1)
            r0 = r8
            android.support.design.widget.BottomSheetBehavior$SettleRunnable r1 = new android.support.design.widget.BottomSheetBehavior$SettleRunnable
            r2 = r1
            r3 = r7
            r4 = r8
            r5 = r9
            r2.<init>(r3, r4, r5)
            android.support.v4.view.ViewCompat.postOnAnimation(r0, r1)
            goto L7f
        L7a:
            r0 = r7
            r1 = r9
            r0.setStateInternal(r1)
        L7f:
            return
        L80:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r8 = r0
            r0 = r8
            java.lang.String r1 = "Illegal state argument: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r8
            r1 = r9
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            r2 = r8
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.BottomSheetBehavior.startSettlingAnimation(android.view.View, int):void");
    }
}
