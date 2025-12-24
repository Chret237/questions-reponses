package android.support.v7.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.view.PointerIconCompat;
import android.support.v7.appcompat.C0287R;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/TooltipPopup.class */
class TooltipPopup {
    private static final String TAG = "TooltipPopup";
    private final View mContentView;
    private final Context mContext;
    private final TextView mMessageView;
    private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private final Rect mTmpDisplayFrame = new Rect();
    private final int[] mTmpAnchorPos = new int[2];
    private final int[] mTmpAppPos = new int[2];

    TooltipPopup(Context context) {
        this.mContext = context;
        View viewInflate = LayoutInflater.from(context).inflate(C0287R.layout.abc_tooltip, (ViewGroup) null);
        this.mContentView = viewInflate;
        this.mMessageView = (TextView) viewInflate.findViewById(C0287R.id.message);
        this.mLayoutParams.setTitle(getClass().getSimpleName());
        this.mLayoutParams.packageName = this.mContext.getPackageName();
        this.mLayoutParams.type = PointerIconCompat.TYPE_HAND;
        this.mLayoutParams.width = -2;
        this.mLayoutParams.height = -2;
        this.mLayoutParams.format = -3;
        this.mLayoutParams.windowAnimations = C0287R.style.Animation_AppCompat_Tooltip;
        this.mLayoutParams.flags = 24;
    }

    private void computePosition(View view, int i, int i2, boolean z, WindowManager.LayoutParams layoutParams) throws Resources.NotFoundException {
        int height;
        int i3;
        layoutParams.token = view.getApplicationWindowToken();
        int dimensionPixelOffset = this.mContext.getResources().getDimensionPixelOffset(C0287R.dimen.tooltip_precise_anchor_threshold);
        if (view.getWidth() < dimensionPixelOffset) {
            i = view.getWidth() / 2;
        }
        if (view.getHeight() >= dimensionPixelOffset) {
            int dimensionPixelOffset2 = this.mContext.getResources().getDimensionPixelOffset(C0287R.dimen.tooltip_precise_anchor_extra_offset);
            int i4 = i2 + dimensionPixelOffset2;
            int i5 = i2 - dimensionPixelOffset2;
            height = i4;
            i3 = i5;
        } else {
            height = view.getHeight();
            i3 = 0;
        }
        layoutParams.gravity = 49;
        int dimensionPixelOffset3 = this.mContext.getResources().getDimensionPixelOffset(z ? C0287R.dimen.tooltip_y_offset_touch : C0287R.dimen.tooltip_y_offset_non_touch);
        View appRootView = getAppRootView(view);
        if (appRootView == null) {
            Log.e(TAG, "Cannot find app view");
            return;
        }
        appRootView.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            Resources resources = this.mContext.getResources();
            int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
            int dimensionPixelSize = identifier != 0 ? resources.getDimensionPixelSize(identifier) : 0;
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, dimensionPixelSize, displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        appRootView.getLocationOnScreen(this.mTmpAppPos);
        view.getLocationOnScreen(this.mTmpAnchorPos);
        int[] iArr = this.mTmpAnchorPos;
        int i6 = iArr[0];
        int[] iArr2 = this.mTmpAppPos;
        iArr[0] = i6 - iArr2[0];
        iArr[1] = iArr[1] - iArr2[1];
        layoutParams.x = (iArr[0] + i) - (appRootView.getWidth() / 2);
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mContentView.measure(iMakeMeasureSpec, iMakeMeasureSpec);
        int measuredHeight = this.mContentView.getMeasuredHeight();
        int[] iArr3 = this.mTmpAnchorPos;
        int i7 = ((iArr3[1] + i3) - dimensionPixelOffset3) - measuredHeight;
        int i8 = iArr3[1] + height + dimensionPixelOffset3;
        if (z) {
            if (i7 >= 0) {
                layoutParams.y = i7;
                return;
            } else {
                layoutParams.y = i8;
                return;
            }
        }
        if (measuredHeight + i8 <= this.mTmpDisplayFrame.height()) {
            layoutParams.y = i8;
        } else {
            layoutParams.y = i7;
        }
    }

    private static View getAppRootView(View view) {
        View rootView = view.getRootView();
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        if ((layoutParams instanceof WindowManager.LayoutParams) && ((WindowManager.LayoutParams) layoutParams).type == 2) {
            return rootView;
        }
        Context context = view.getContext();
        while (true) {
            Context context2 = context;
            if (!(context2 instanceof ContextWrapper)) {
                return rootView;
            }
            if (context2 instanceof Activity) {
                return ((Activity) context2).getWindow().getDecorView();
            }
            context = ((ContextWrapper) context2).getBaseContext();
        }
    }

    void hide() {
        if (isShowing()) {
            ((WindowManager) this.mContext.getSystemService("window")).removeView(this.mContentView);
        }
    }

    boolean isShowing() {
        return this.mContentView.getParent() != null;
    }

    void show(View view, int i, int i2, boolean z, CharSequence charSequence) throws Resources.NotFoundException {
        if (isShowing()) {
            hide();
        }
        this.mMessageView.setText(charSequence);
        computePosition(view, i, i2, z, this.mLayoutParams);
        ((WindowManager) this.mContext.getSystemService("window")).addView(this.mContentView, this.mLayoutParams);
    }
}
