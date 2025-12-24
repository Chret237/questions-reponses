package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.FitWindowsViewGroup;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/FitWindowsLinearLayout.class */
public class FitWindowsLinearLayout extends LinearLayout implements FitWindowsViewGroup {
    private FitWindowsViewGroup.OnFitSystemWindowsListener mListener;

    public FitWindowsLinearLayout(Context context) {
        super(context);
    }

    public FitWindowsLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        FitWindowsViewGroup.OnFitSystemWindowsListener onFitSystemWindowsListener = this.mListener;
        if (onFitSystemWindowsListener != null) {
            onFitSystemWindowsListener.onFitSystemWindows(rect);
        }
        return super.fitSystemWindows(rect);
    }

    @Override // android.support.v7.widget.FitWindowsViewGroup
    public void setOnFitSystemWindowsListener(FitWindowsViewGroup.OnFitSystemWindowsListener onFitSystemWindowsListener) {
        this.mListener = onFitSystemWindowsListener;
    }
}
