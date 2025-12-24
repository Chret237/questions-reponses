package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0287R;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionBarContainer.class */
public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewCompat.setBackground(this, new ActionBarBackgroundDrawable(this));
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0287R.styleable.ActionBar);
        this.mBackground = typedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.ActionBar_background);
        this.mStackedBackground = typedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.ActionBar_backgroundStacked);
        this.mHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.ActionBar_height, -1);
        boolean z = true;
        if (getId() == C0287R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = typedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.ActionBar_backgroundSplit);
        }
        typedArrayObtainStyledAttributes.recycle();
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 == null || !drawable3.isStateful()) {
            return;
        }
        this.mSplitBackground.setState(getDrawableState());
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(C0287R.id.action_bar);
        this.mContextView = findViewById(C0287R.id.action_context_bar);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2;
        Drawable drawable;
        super.onLayout(z, i, i2, i3, i4);
        View view = this.mTabContainer;
        boolean z3 = (view == null || view.getVisibility() == 8) ? false : true;
        if (view != null && view.getVisibility() != 8) {
            int measuredHeight = getMeasuredHeight();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            view.layout(i, (measuredHeight - view.getMeasuredHeight()) - layoutParams.bottomMargin, i3, measuredHeight - layoutParams.bottomMargin);
        }
        if (this.mIsSplit) {
            Drawable drawable2 = this.mSplitBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                z2 = true;
            } else {
                z2 = false;
            }
        } else {
            z2 = false;
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else {
                    View view2 = this.mContextView;
                    if (view2 == null || view2.getVisibility() != 0) {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    } else {
                        this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
                    }
                }
                z2 = true;
            }
            this.mIsStacked = z3;
            if (z3 && (drawable = this.mStackedBackground) != null) {
                drawable.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                z2 = true;
            }
        }
        if (z2) {
            invalidate();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int iMakeMeasureSpec = i2;
        if (this.mActionBarView == null) {
            iMakeMeasureSpec = i2;
            if (View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
                int i3 = this.mHeight;
                iMakeMeasureSpec = i2;
                if (i3 >= 0) {
                    iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(i3, View.MeasureSpec.getSize(i2)), Integer.MIN_VALUE);
                }
            }
        }
        super.onMeasure(i, iMakeMeasureSpec);
        if (this.mActionBarView == null) {
            return;
        }
        int mode = View.MeasureSpec.getMode(iMakeMeasureSpec);
        View view = this.mTabContainer;
        if (view == null || view.getVisibility() == 8 || mode == 1073741824) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth(), Math.min((!isCollapsed(this.mActionBarView) ? getMeasuredHeightWithMargins(this.mActionBarView) : !isCollapsed(this.mContextView) ? getMeasuredHeightWithMargins(this.mContextView) : 0) + getMeasuredHeightWithMargins(this.mTabContainer), mode == Integer.MIN_VALUE ? View.MeasureSpec.getSize(iMakeMeasureSpec) : Integer.MAX_VALUE));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void setPrimaryBackground(Drawable drawable) {
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            View view = this.mActionBarView;
            if (view != null) {
                this.mBackground.setBounds(view.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = true;
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setSplitBackground(android.graphics.drawable.Drawable r7) {
        /*
            r6 = this;
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mSplitBackground
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L19
            r0 = r10
            r1 = 0
            r0.setCallback(r1)
            r0 = r6
            r1 = r6
            android.graphics.drawable.Drawable r1 = r1.mSplitBackground
            r0.unscheduleDrawable(r1)
        L19:
            r0 = r6
            r1 = r7
            r0.mSplitBackground = r1
            r0 = 0
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L47
            r0 = r7
            r1 = r6
            r0.setCallback(r1)
            r0 = r6
            boolean r0 = r0.mIsSplit
            if (r0 == 0) goto L47
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mSplitBackground
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L47
            r0 = r7
            r1 = 0
            r2 = 0
            r3 = r6
            int r3 = r3.getMeasuredWidth()
            r4 = r6
            int r4 = r4.getMeasuredHeight()
            r0.setBounds(r1, r2, r3, r4)
        L47:
            r0 = r6
            boolean r0 = r0.mIsSplit
            if (r0 == 0) goto L5c
            r0 = r9
            r8 = r0
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mSplitBackground
            if (r0 != 0) goto L71
        L57:
            r0 = 1
            r8 = r0
            goto L71
        L5c:
            r0 = r9
            r8 = r0
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mBackground
            if (r0 != 0) goto L71
            r0 = r9
            r8 = r0
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mStackedBackground
            if (r0 != 0) goto L71
            goto L57
        L71:
            r0 = r6
            r1 = r8
            r0.setWillNotDraw(r1)
            r0 = r6
            r0.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.setSplitBackground(android.graphics.drawable.Drawable):void");
    }

    public void setStackedBackground(Drawable drawable) {
        Drawable drawable2;
        Drawable drawable3 = this.mStackedBackground;
        if (drawable3 != null) {
            drawable3.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mIsStacked && (drawable2 = this.mStackedBackground) != null) {
                drawable2.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean z = true;
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        this.mTabContainer = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    public void setTransitioning(boolean z) {
        this.mIsTransitioning = z;
        setDescendantFocusability(z ? 393216 : 262144);
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i) {
        if (i != 0) {
            return super.startActionModeForChild(view, callback, i);
        }
        return null;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || ((drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable));
    }
}
