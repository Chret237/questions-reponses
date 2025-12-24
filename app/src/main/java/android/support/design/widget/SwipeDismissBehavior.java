package android.support.design.widget;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/SwipeDismissBehavior.class */
public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    private boolean interceptingEvents;
    OnDismissListener listener;
    private boolean sensitivitySet;
    ViewDragHelper viewDragHelper;
    private float sensitivity = 0.0f;
    int swipeDirection = 2;
    float dragDismissThreshold = 0.5f;
    float alphaStartSwipeDistance = 0.0f;
    float alphaEndSwipeDistance = 0.5f;
    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback(this) { // from class: android.support.design.widget.SwipeDismissBehavior.1
        private static final int INVALID_POINTER_ID = -1;
        private int activePointerId = -1;
        private int originalCapturedViewLeft;
        final SwipeDismissBehavior this$0;

        {
            this.this$0 = this;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x0050  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0080  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private boolean shouldDismiss(android.view.View r4, float r5) {
            /*
                r3 = this;
                r0 = 0
                r10 = r0
                r0 = 0
                r9 = r0
                r0 = 0
                r11 = r0
                r0 = r5
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 == 0) goto L86
                r0 = r4
                int r0 = android.support.v4.view.ViewCompat.getLayoutDirection(r0)
                r1 = 1
                if (r0 != r1) goto L1c
                r0 = 1
                r6 = r0
                goto L1e
            L1c:
                r0 = 0
                r6 = r0
            L1e:
                r0 = r3
                android.support.design.widget.SwipeDismissBehavior r0 = r0.this$0
                int r0 = r0.swipeDirection
                r1 = 2
                if (r0 != r1) goto L2b
                r0 = 1
                return r0
            L2b:
                r0 = r3
                android.support.design.widget.SwipeDismissBehavior r0 = r0.this$0
                int r0 = r0.swipeDirection
                if (r0 != 0) goto L56
                r0 = r6
                if (r0 == 0) goto L46
                r0 = r11
                r9 = r0
                r0 = r5
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 >= 0) goto L53
                goto L50
            L46:
                r0 = r11
                r9 = r0
                r0 = r5
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto L53
            L50:
                r0 = 1
                r9 = r0
            L53:
                r0 = r9
                return r0
            L56:
                r0 = r10
                r9 = r0
                r0 = r3
                android.support.design.widget.SwipeDismissBehavior r0 = r0.this$0
                int r0 = r0.swipeDirection
                r1 = 1
                if (r0 != r1) goto L83
                r0 = r6
                if (r0 == 0) goto L76
                r0 = r10
                r9 = r0
                r0 = r5
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto L83
                goto L80
            L76:
                r0 = r10
                r9 = r0
                r0 = r5
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 >= 0) goto L83
            L80:
                r0 = 1
                r9 = r0
            L83:
                r0 = r9
                return r0
            L86:
                r0 = r4
                int r0 = r0.getLeft()
                r7 = r0
                r0 = r3
                int r0 = r0.originalCapturedViewLeft
                r6 = r0
                r0 = r4
                int r0 = r0.getWidth()
                float r0 = (float) r0
                r1 = r3
                android.support.design.widget.SwipeDismissBehavior r1 = r1.this$0
                float r1 = r1.dragDismissThreshold
                float r0 = r0 * r1
                int r0 = java.lang.Math.round(r0)
                r8 = r0
                r0 = r7
                r1 = r6
                int r0 = r0 - r1
                int r0 = java.lang.Math.abs(r0)
                r1 = r8
                if (r0 < r1) goto Lb2
                r0 = 1
                r9 = r0
            Lb2:
                r0 = r9
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.SwipeDismissBehavior.C01021.shouldDismiss(android.view.View, float):boolean");
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i, int i2) {
            int width;
            int width2;
            int width3;
            boolean z = ViewCompat.getLayoutDirection(view) == 1;
            if (this.this$0.swipeDirection == 0) {
                if (z) {
                    width = this.originalCapturedViewLeft - view.getWidth();
                    width2 = this.originalCapturedViewLeft;
                } else {
                    width = this.originalCapturedViewLeft;
                    width3 = view.getWidth();
                    width2 = width3 + width;
                }
            } else if (this.this$0.swipeDirection != 1) {
                width = this.originalCapturedViewLeft - view.getWidth();
                width2 = view.getWidth() + this.originalCapturedViewLeft;
            } else if (z) {
                width = this.originalCapturedViewLeft;
                width3 = view.getWidth();
                width2 = width3 + width;
            } else {
                width = this.originalCapturedViewLeft - view.getWidth();
                width2 = this.originalCapturedViewLeft;
            }
            return SwipeDismissBehavior.clamp(width, i, width2);
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i, int i2) {
            return view.getTop();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return view.getWidth();
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewCaptured(View view, int i) {
            this.activePointerId = i;
            this.originalCapturedViewLeft = view.getLeft();
            ViewParent parent = view.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewDragStateChanged(int i) {
            if (this.this$0.listener != null) {
                this.this$0.listener.onDragStateChanged(i);
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            float width = this.originalCapturedViewLeft + (view.getWidth() * this.this$0.alphaStartSwipeDistance);
            float width2 = this.originalCapturedViewLeft + (view.getWidth() * this.this$0.alphaEndSwipeDistance);
            float f = i;
            if (f <= width) {
                view.setAlpha(1.0f);
            } else if (f >= width2) {
                view.setAlpha(0.0f);
            } else {
                view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(width, width2, f), 1.0f));
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public void onViewReleased(View view, float f, float f2) {
            int i;
            boolean z;
            this.activePointerId = -1;
            int width = view.getWidth();
            if (shouldDismiss(view, f)) {
                int left = view.getLeft();
                int i2 = this.originalCapturedViewLeft;
                i = left < i2 ? i2 - width : i2 + width;
                z = true;
            } else {
                i = this.originalCapturedViewLeft;
                z = false;
            }
            if (this.this$0.viewDragHelper.settleCapturedViewAt(i, view.getTop())) {
                ViewCompat.postOnAnimation(view, new SettleRunnable(this.this$0, view, z));
            } else {
                if (!z || this.this$0.listener == null) {
                    return;
                }
                this.this$0.listener.onDismiss(view);
            }
        }

        @Override // android.support.v4.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i) {
            return this.activePointerId == -1 && this.this$0.canSwipeDismissView(view);
        }
    };

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/SwipeDismissBehavior$OnDismissListener.class */
    public interface OnDismissListener {
        void onDismiss(View view);

        void onDragStateChanged(int i);
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/SwipeDismissBehavior$SettleRunnable.class */
    private class SettleRunnable implements Runnable {
        private final boolean dismiss;
        final SwipeDismissBehavior this$0;
        private final View view;

        SettleRunnable(SwipeDismissBehavior swipeDismissBehavior, View view, boolean z) {
            this.this$0 = swipeDismissBehavior;
            this.view = view;
            this.dismiss = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.viewDragHelper != null && this.this$0.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            } else {
                if (!this.dismiss || this.this$0.listener == null) {
                    return;
                }
                this.this$0.listener.onDismiss(this.view);
            }
        }
    }

    static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    static int clamp(int i, int i2, int i3) {
        return Math.min(Math.max(i, i2), i3);
    }

    private void ensureViewDragHelper(ViewGroup viewGroup) {
        if (this.viewDragHelper == null) {
            this.viewDragHelper = this.sensitivitySet ? ViewDragHelper.create(viewGroup, this.sensitivity, this.dragCallback) : ViewDragHelper.create(viewGroup, this.dragCallback);
        }
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public boolean canSwipeDismissView(View view) {
        return true;
    }

    public int getDragState() {
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        return viewDragHelper != null ? viewDragHelper.getViewDragState() : 0;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        boolean zIsPointInChildBounds = this.interceptingEvents;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            zIsPointInChildBounds = coordinatorLayout.isPointInChildBounds(v, (int) motionEvent.getX(), (int) motionEvent.getY());
            this.interceptingEvents = zIsPointInChildBounds;
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.interceptingEvents = false;
        }
        if (!zIsPointInChildBounds) {
            return false;
        }
        ensureViewDragHelper(coordinatorLayout);
        return this.viewDragHelper.shouldInterceptTouchEvent(motionEvent);
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        ViewDragHelper viewDragHelper = this.viewDragHelper;
        if (viewDragHelper == null) {
            return false;
        }
        viewDragHelper.processTouchEvent(motionEvent);
        return true;
    }

    public void setDragDismissDistance(float f) {
        this.dragDismissThreshold = clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.alphaEndSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.listener = onDismissListener;
    }

    public void setSensitivity(float f) {
        this.sensitivity = f;
        this.sensitivitySet = true;
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.alphaStartSwipeDistance = clamp(0.0f, f, 1.0f);
    }

    public void setSwipeDirection(int i) {
        this.swipeDirection = i;
    }
}
