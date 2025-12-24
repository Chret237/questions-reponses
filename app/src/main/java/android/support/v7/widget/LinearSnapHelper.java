package android.support.v7.widget;

import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/LinearSnapHelper.class */
public class LinearSnapHelper extends SnapHelper {
    private static final float INVALID_DISTANCE = 1.0f;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int i;
        View view;
        int i2;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return INVALID_DISTANCE;
        }
        int i3 = 0;
        View view2 = null;
        View view3 = null;
        int i4 = Integer.MAX_VALUE;
        int i5 = Integer.MIN_VALUE;
        while (true) {
            i = i5;
            if (i3 >= childCount) {
                break;
            }
            View childAt = layoutManager.getChildAt(i3);
            int position = layoutManager.getPosition(childAt);
            if (position == -1) {
                view = view2;
                i2 = i;
            } else {
                int i6 = i4;
                if (position < i4) {
                    view2 = childAt;
                    i6 = position;
                }
                view = view2;
                i4 = i6;
                i2 = i;
                if (position > i) {
                    i2 = position;
                    i4 = i6;
                    view3 = childAt;
                    view = view2;
                }
            }
            i3++;
            view2 = view;
            i5 = i2;
        }
        if (view2 == null || view3 == null) {
            return INVALID_DISTANCE;
        }
        int iMax = Math.max(orientationHelper.getDecoratedEnd(view2), orientationHelper.getDecoratedEnd(view3)) - Math.min(orientationHelper.getDecoratedStart(view2), orientationHelper.getDecoratedStart(view3));
        return iMax == 0 ? INVALID_DISTANCE : (iMax * INVALID_DISTANCE) / ((i - i4) + 1);
    }

    private int distanceToCenter(RecyclerView.LayoutManager layoutManager, View view, OrientationHelper orientationHelper) {
        return (orientationHelper.getDecoratedStart(view) + (orientationHelper.getDecoratedMeasurement(view) / 2)) - (layoutManager.getClipToPadding() ? orientationHelper.getStartAfterPadding() + (orientationHelper.getTotalSpace() / 2) : orientationHelper.getEnd() / 2);
    }

    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper, int i, int i2) {
        int[] iArrCalculateScrollDistance = calculateScrollDistance(i, i2);
        float fComputeDistancePerChild = computeDistancePerChild(layoutManager, orientationHelper);
        if (fComputeDistancePerChild <= 0.0f) {
            return 0;
        }
        return Math.round((Math.abs(iArrCalculateScrollDistance[0]) > Math.abs(iArrCalculateScrollDistance[1]) ? iArrCalculateScrollDistance[0] : iArrCalculateScrollDistance[1]) / fComputeDistancePerChild);
    }

    private View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper orientationHelper) {
        int childCount = layoutManager.getChildCount();
        View view = null;
        if (childCount == 0) {
            return null;
        }
        int startAfterPadding = layoutManager.getClipToPadding() ? orientationHelper.getStartAfterPadding() + (orientationHelper.getTotalSpace() / 2) : orientationHelper.getEnd() / 2;
        int i = Integer.MAX_VALUE;
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = layoutManager.getChildAt(i2);
            int iAbs = Math.abs((orientationHelper.getDecoratedStart(childAt) + (orientationHelper.getDecoratedMeasurement(childAt) / 2)) - startAfterPadding);
            int i3 = i;
            if (iAbs < i) {
                view = childAt;
                i3 = iAbs;
            }
            i2++;
            i = i3;
        }
        return view;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mHorizontalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mVerticalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }

    @Override // android.support.v7.widget.SnapHelper
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View view) {
        int[] iArr = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            iArr[0] = distanceToCenter(layoutManager, view, getHorizontalHelper(layoutManager));
        } else {
            iArr[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            iArr[1] = distanceToCenter(layoutManager, view, getVerticalHelper(layoutManager));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    @Override // android.support.v7.widget.SnapHelper
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.v7.widget.SnapHelper
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int i, int i2) {
        int itemCount;
        View viewFindSnapView;
        int position;
        int i3;
        PointF pointFComputeScrollVectorForPosition;
        int i4;
        int i5;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (itemCount = layoutManager.getItemCount()) == 0 || (viewFindSnapView = findSnapView(layoutManager)) == null || (position = layoutManager.getPosition(viewFindSnapView)) == -1 || (pointFComputeScrollVectorForPosition = ((RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager).computeScrollVectorForPosition((i3 = itemCount - 1))) == null) {
            return -1;
        }
        if (layoutManager.canScrollHorizontally()) {
            int iEstimateNextPositionDiffForFling = estimateNextPositionDiffForFling(layoutManager, getHorizontalHelper(layoutManager), i, 0);
            i4 = iEstimateNextPositionDiffForFling;
            if (pointFComputeScrollVectorForPosition.x < 0.0f) {
                i4 = -iEstimateNextPositionDiffForFling;
            }
        } else {
            i4 = 0;
        }
        if (layoutManager.canScrollVertically()) {
            int iEstimateNextPositionDiffForFling2 = estimateNextPositionDiffForFling(layoutManager, getVerticalHelper(layoutManager), 0, i2);
            i5 = iEstimateNextPositionDiffForFling2;
            if (pointFComputeScrollVectorForPosition.y < 0.0f) {
                i5 = -iEstimateNextPositionDiffForFling2;
            }
        } else {
            i5 = 0;
        }
        if (layoutManager.canScrollVertically()) {
            i4 = i5;
        }
        if (i4 == 0) {
            return -1;
        }
        int i6 = position + i4;
        if (i6 < 0) {
            i6 = 0;
        }
        if (i6 >= itemCount) {
            i6 = i3;
        }
        return i6;
    }
}
