package android.support.v7.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/AlertDialogLayout.class */
public class AlertDialogLayout extends LinearLayoutCompat {
    public AlertDialogLayout(Context context) {
        super(context);
    }

    public AlertDialogLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void forceUniformWidth(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) childAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = childAt.getMeasuredHeight();
                    measureChildWithMargins(childAt, iMakeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    private static int resolveMinimumHeight(View view) throws NoSuchFieldException {
        int minimumHeight = ViewCompat.getMinimumHeight(view);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (!(view instanceof ViewGroup)) {
            return 0;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (viewGroup.getChildCount() == 1) {
            return resolveMinimumHeight(viewGroup.getChildAt(0));
        }
        return 0;
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    private boolean tryOnMeasure(int i, int i2) throws NoSuchFieldException {
        int iCombineMeasuredStates;
        int iResolveMinimumHeight;
        int measuredHeight;
        int measuredHeight2;
        int i3;
        int childCount = getChildCount();
        View view = null;
        View view2 = null;
        View view3 = null;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                int id = childAt.getId();
                if (id == C0287R.id.topPanel) {
                    view = childAt;
                } else if (id == C0287R.id.buttonPanel) {
                    view2 = childAt;
                } else {
                    if ((id != C0287R.id.contentPanel && id != C0287R.id.customPanel) || view3 != null) {
                        return false;
                    }
                    view3 = childAt;
                }
            }
        }
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        int mode2 = View.MeasureSpec.getMode(i);
        int paddingTop = getPaddingTop() + getPaddingBottom();
        if (view != null) {
            view.measure(i, 0);
            paddingTop += view.getMeasuredHeight();
            iCombineMeasuredStates = View.combineMeasuredStates(0, view.getMeasuredState());
        } else {
            iCombineMeasuredStates = 0;
        }
        if (view2 != null) {
            view2.measure(i, 0);
            iResolveMinimumHeight = resolveMinimumHeight(view2);
            measuredHeight = view2.getMeasuredHeight() - iResolveMinimumHeight;
            paddingTop += iResolveMinimumHeight;
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, view2.getMeasuredState());
        } else {
            iResolveMinimumHeight = 0;
            measuredHeight = 0;
        }
        if (view3 != null) {
            view3.measure(i, mode == 0 ? 0 : View.MeasureSpec.makeMeasureSpec(Math.max(0, size - paddingTop), mode));
            measuredHeight2 = view3.getMeasuredHeight();
            paddingTop += measuredHeight2;
            iCombineMeasuredStates = View.combineMeasuredStates(iCombineMeasuredStates, view3.getMeasuredState());
        } else {
            measuredHeight2 = 0;
        }
        int i5 = size - paddingTop;
        int iCombineMeasuredStates2 = iCombineMeasuredStates;
        int i6 = i5;
        int measuredHeight3 = paddingTop;
        if (view2 != null) {
            int iMin = Math.min(i5, measuredHeight);
            int i7 = i5;
            int i8 = iResolveMinimumHeight;
            if (iMin > 0) {
                i7 = i5 - iMin;
                i8 = iResolveMinimumHeight + iMin;
            }
            view2.measure(i, View.MeasureSpec.makeMeasureSpec(i8, 1073741824));
            measuredHeight3 = (paddingTop - iResolveMinimumHeight) + view2.getMeasuredHeight();
            i6 = i7;
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates, view2.getMeasuredState());
        }
        int iCombineMeasuredStates3 = iCombineMeasuredStates2;
        int measuredHeight4 = measuredHeight3;
        if (view3 != null) {
            iCombineMeasuredStates3 = iCombineMeasuredStates2;
            measuredHeight4 = measuredHeight3;
            if (i6 > 0) {
                view3.measure(i, View.MeasureSpec.makeMeasureSpec(measuredHeight2 + i6, mode));
                measuredHeight4 = (measuredHeight3 - measuredHeight2) + view3.getMeasuredHeight();
                iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates2, view3.getMeasuredState());
            }
        }
        int i9 = 0;
        int i10 = 0;
        while (true) {
            i3 = i10;
            if (i9 >= childCount) {
                break;
            }
            View childAt2 = getChildAt(i9);
            int iMax = i3;
            if (childAt2.getVisibility() != 8) {
                iMax = Math.max(i3, childAt2.getMeasuredWidth());
            }
            i9++;
            i10 = iMax;
        }
        setMeasuredDimension(View.resolveSizeAndState(i3 + getPaddingLeft() + getPaddingRight(), i, iCombineMeasuredStates3), View.resolveSizeAndState(measuredHeight4, i2, 0));
        if (mode2 == 1073741824) {
            return true;
        }
        forceUniformWidth(childCount, i2);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x013c  */
    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onLayout(boolean r8, int r9, int r10, int r11, int r12) {
        /*
            Method dump skipped, instructions count: 365
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AlertDialogLayout.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.View
    protected void onMeasure(int i, int i2) {
        if (tryOnMeasure(i, i2)) {
            return;
        }
        super.onMeasure(i, i2);
    }
}
