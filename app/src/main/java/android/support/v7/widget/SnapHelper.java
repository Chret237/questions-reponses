package android.support.v7.widget;

import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/SnapHelper.class */
public abstract class SnapHelper extends RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100.0f;
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener(this) { // from class: android.support.v7.widget.SnapHelper.1
        boolean mScrolled = false;
        final SnapHelper this$0;

        {
            this.this$0 = this;
        }

        @Override // android.support.v7.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i == 0 && this.mScrolled) {
                this.mScrolled = false;
                this.this$0.snapToTargetExistingView();
            }
        }

        @Override // android.support.v7.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (i == 0 && i2 == 0) {
                return;
            }
            this.mScrolled = true;
        }
    };

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        this.mRecyclerView.addOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(this);
    }

    private boolean snapFromFling(RecyclerView.LayoutManager layoutManager, int i, int i2) {
        RecyclerView.SmoothScroller smoothScrollerCreateScroller;
        int iFindTargetSnapPosition;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (smoothScrollerCreateScroller = createScroller(layoutManager)) == null || (iFindTargetSnapPosition = findTargetSnapPosition(layoutManager, i, i2)) == -1) {
            return false;
        }
        smoothScrollerCreateScroller.setTargetPosition(iFindTargetSnapPosition);
        layoutManager.startSmoothScroll(smoothScrollerCreateScroller);
        return true;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (recyclerView != null) {
            setupCallbacks();
            this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
            snapToTargetExistingView();
        }
    }

    public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View view);

    public int[] calculateScrollDistance(int i, int i2) {
        this.mGravityScroller.fling(0, 0, i, i2, Integer.MIN_VALUE, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, Integer.MIN_VALUE, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        return new int[]{this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY()};
    }

    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        return createSnapScroller(layoutManager);
    }

    @Deprecated
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return new LinearSmoothScroller(this, this.mRecyclerView.getContext()) { // from class: android.support.v7.widget.SnapHelper.2
                final SnapHelper this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.support.v7.widget.LinearSmoothScroller
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return SnapHelper.MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }

                @Override // android.support.v7.widget.LinearSmoothScroller, android.support.v7.widget.RecyclerView.SmoothScroller
                protected void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
                    if (this.this$0.mRecyclerView == null) {
                        return;
                    }
                    SnapHelper snapHelper = this.this$0;
                    int[] iArrCalculateDistanceToFinalSnap = snapHelper.calculateDistanceToFinalSnap(snapHelper.mRecyclerView.getLayoutManager(), view);
                    int i = iArrCalculateDistanceToFinalSnap[0];
                    int i2 = iArrCalculateDistanceToFinalSnap[1];
                    int iCalculateTimeForDeceleration = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(i2)));
                    if (iCalculateTimeForDeceleration > 0) {
                        action.update(i, i2, iCalculateTimeForDeceleration, this.mDecelerateInterpolator);
                    }
                }
            };
        }
        return null;
    }

    public abstract View findSnapView(RecyclerView.LayoutManager layoutManager);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int i, int i2);

    /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
    @Override // android.support.v7.widget.RecyclerView.OnFlingListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onFling(int r6, int r7) {
        /*
            r5 = this;
            r0 = r5
            android.support.v7.widget.RecyclerView r0 = r0.mRecyclerView
            android.support.v7.widget.RecyclerView$LayoutManager r0 = r0.getLayoutManager()
            r11 = r0
            r0 = 0
            r10 = r0
            r0 = r11
            if (r0 != 0) goto L13
            r0 = 0
            return r0
        L13:
            r0 = r5
            android.support.v7.widget.RecyclerView r0 = r0.mRecyclerView
            android.support.v7.widget.RecyclerView$Adapter r0 = r0.getAdapter()
            if (r0 != 0) goto L1f
            r0 = 0
            return r0
        L1f:
            r0 = r5
            android.support.v7.widget.RecyclerView r0 = r0.mRecyclerView
            int r0 = r0.getMinFlingVelocity()
            r8 = r0
            r0 = r7
            int r0 = java.lang.Math.abs(r0)
            r1 = r8
            if (r0 > r1) goto L3b
            r0 = r10
            r9 = r0
            r0 = r6
            int r0 = java.lang.Math.abs(r0)
            r1 = r8
            if (r0 <= r1) goto L4d
        L3b:
            r0 = r10
            r9 = r0
            r0 = r5
            r1 = r11
            r2 = r6
            r3 = r7
            boolean r0 = r0.snapFromFling(r1, r2, r3)
            if (r0 == 0) goto L4d
            r0 = 1
            r9 = r0
        L4d:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.SnapHelper.onFling(int, int):boolean");
    }

    void snapToTargetExistingView() {
        RecyclerView.LayoutManager layoutManager;
        View viewFindSnapView;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView == null || (layoutManager = recyclerView.getLayoutManager()) == null || (viewFindSnapView = findSnapView(layoutManager)) == null) {
            return;
        }
        int[] iArrCalculateDistanceToFinalSnap = calculateDistanceToFinalSnap(layoutManager, viewFindSnapView);
        if (iArrCalculateDistanceToFinalSnap[0] == 0 && iArrCalculateDistanceToFinalSnap[1] == 0) {
            return;
        }
        this.mRecyclerView.smoothScrollBy(iArrCalculateDistanceToFinalSnap[0], iArrCalculateDistanceToFinalSnap[1]);
    }
}
