package android.support.v7.widget;

import android.graphics.Rect;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/FitWindowsViewGroup.class */
public interface FitWindowsViewGroup {

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/FitWindowsViewGroup$OnFitSystemWindowsListener.class */
    public interface OnFitSystemWindowsListener {
        void onFitSystemWindows(Rect rect);
    }

    void setOnFitSystemWindowsListener(OnFitSystemWindowsListener onFitSystemWindowsListener);
}
