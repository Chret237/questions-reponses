package android.support.v7.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionBarContextView.class */
public class ActionBarContextView extends AbsActionBarView {
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0287R.attr.actionModeStyle);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0287R.styleable.ActionMode, i, 0);
        ViewCompat.setBackground(this, tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.ActionMode_background));
        this.mTitleStyleRes = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = tintTypedArrayObtainStyledAttributes.getLayoutDimension(C0287R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.ActionMode_closeItemLayout, C0287R.layout.abc_action_mode_close_item_material);
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    private void initTitle() {
        if (this.mTitleLayout == null) {
            LayoutInflater.from(getContext()).inflate(C0287R.layout.abc_action_bar_title_item, this);
            LinearLayout linearLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.mTitleLayout = linearLayout;
            this.mTitleView = (TextView) linearLayout.findViewById(C0287R.id.action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(C0287R.id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        boolean zIsEmpty = TextUtils.isEmpty(this.mTitle);
        boolean z = !TextUtils.isEmpty(this.mSubtitle);
        this.mSubtitleView.setVisibility(z ? 0 : 8);
        LinearLayout linearLayout2 = this.mTitleLayout;
        int i = 0;
        if (!(!zIsEmpty)) {
            i = z ? 0 : 8;
        }
        linearLayout2.setVisibility(i);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void animateToVisibility(int i) {
        super.animateToVisibility(i);
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean canShowOverflowMenu() {
        return super.canShowOverflowMenu();
    }

    public void closeMode() {
        if (this.mClose == null) {
            killMode();
        }
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void dismissPopupMenus() {
        super.dismissPopupMenus();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    public void initForMode(ActionMode actionMode) {
        View view = this.mClose;
        if (view == null) {
            View viewInflate = LayoutInflater.from(getContext()).inflate(this.mCloseItemLayout, (ViewGroup) this, false);
            this.mClose = viewInflate;
            addView(viewInflate);
        } else if (view.getParent() == null) {
            addView(this.mClose);
        }
        this.mClose.findViewById(C0287R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener(this, actionMode) { // from class: android.support.v7.widget.ActionBarContextView.1
            final ActionBarContextView this$0;
            final ActionMode val$mode;

            {
                this.this$0 = this;
                this.val$mode = actionMode;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                this.val$mode.finish();
            }
        });
        MenuBuilder menuBuilder = (MenuBuilder) actionMode.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        this.mActionMenuPresenter = new ActionMenuPresenter(getContext());
        this.mActionMenuPresenter.setReserveOverflow(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
        menuBuilder.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
        ViewCompat.setBackground(this.mMenuView, null);
        addView(this.mMenuView, layoutParams);
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isOverflowMenuShowPending() {
        return super.isOverflowMenuShowPending();
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isOverflowReserved() {
        return super.isOverflowReserved();
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }

    public void killMode() {
        removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    @Override // android.support.v7.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() != 32) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            return;
        }
        accessibilityEvent.setSource(this);
        accessibilityEvent.setClassName(getClass().getName());
        accessibilityEvent.setPackageName(getContext().getPackageName());
        accessibilityEvent.setContentDescription(this.mTitle);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingRight = zIsLayoutRtl ? (i3 - i) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingTop2 = ((i4 - i2) - getPaddingTop()) - getPaddingBottom();
        View view = this.mClose;
        int next = paddingRight;
        if (view != null) {
            next = paddingRight;
            if (view.getVisibility() != 8) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mClose.getLayoutParams();
                int i5 = zIsLayoutRtl ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
                int i6 = zIsLayoutRtl ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
                int next2 = next(paddingRight, i5, zIsLayoutRtl);
                next = next(next2 + positionChild(this.mClose, next2, paddingTop, paddingTop2, zIsLayoutRtl), i6, zIsLayoutRtl);
            }
        }
        LinearLayout linearLayout = this.mTitleLayout;
        int iPositionChild = next;
        if (linearLayout != null) {
            iPositionChild = next;
            if (this.mCustomView == null) {
                iPositionChild = next;
                if (linearLayout.getVisibility() != 8) {
                    iPositionChild = next + positionChild(this.mTitleLayout, next, paddingTop, paddingTop2, zIsLayoutRtl);
                }
            }
        }
        View view2 = this.mCustomView;
        if (view2 != null) {
            positionChild(view2, iPositionChild, paddingTop, paddingTop2, zIsLayoutRtl);
        }
        int paddingLeft = zIsLayoutRtl ? getPaddingLeft() : (i3 - i) - getPaddingRight();
        if (this.mMenuView != null) {
            positionChild(this.mMenuView, paddingLeft, paddingTop, paddingTop2, !zIsLayoutRtl);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (View.MeasureSpec.getMode(i) != 1073741824) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        if (View.MeasureSpec.getMode(i2) == 0) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with android:layout_height=\"wrap_content\"");
        }
        int size = View.MeasureSpec.getSize(i);
        int size2 = this.mContentHeight > 0 ? this.mContentHeight : View.MeasureSpec.getSize(i2);
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int i3 = size2 - paddingTop;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE);
        View view = this.mClose;
        int i4 = paddingLeft;
        if (view != null) {
            int iMeasureChildView = measureChildView(view, paddingLeft, iMakeMeasureSpec, 0);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mClose.getLayoutParams();
            i4 = iMeasureChildView - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
        }
        int iMeasureChildView2 = i4;
        if (this.mMenuView != null) {
            iMeasureChildView2 = i4;
            if (this.mMenuView.getParent() == this) {
                iMeasureChildView2 = measureChildView(this.mMenuView, i4, iMakeMeasureSpec, 0);
            }
        }
        LinearLayout linearLayout = this.mTitleLayout;
        int iMeasureChildView3 = iMeasureChildView2;
        if (linearLayout != null) {
            iMeasureChildView3 = iMeasureChildView2;
            if (this.mCustomView == null) {
                if (this.mTitleOptional) {
                    this.mTitleLayout.measure(View.MeasureSpec.makeMeasureSpec(0, 0), iMakeMeasureSpec);
                    int measuredWidth = this.mTitleLayout.getMeasuredWidth();
                    boolean z = measuredWidth <= iMeasureChildView2;
                    iMeasureChildView3 = iMeasureChildView2;
                    if (z) {
                        iMeasureChildView3 = iMeasureChildView2 - measuredWidth;
                    }
                    this.mTitleLayout.setVisibility(z ? 0 : 8);
                } else {
                    iMeasureChildView3 = measureChildView(linearLayout, iMeasureChildView2, iMakeMeasureSpec, 0);
                }
            }
        }
        View view2 = this.mCustomView;
        if (view2 != null) {
            ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
            int i5 = layoutParams.width != -2 ? 1073741824 : Integer.MIN_VALUE;
            int iMin = iMeasureChildView3;
            if (layoutParams.width >= 0) {
                iMin = Math.min(layoutParams.width, iMeasureChildView3);
            }
            int i6 = layoutParams.height != -2 ? 1073741824 : Integer.MIN_VALUE;
            int iMin2 = i3;
            if (layoutParams.height >= 0) {
                iMin2 = Math.min(layoutParams.height, i3);
            }
            this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec(iMin, i5), View.MeasureSpec.makeMeasureSpec(iMin2, i6));
        }
        if (this.mContentHeight > 0) {
            setMeasuredDimension(size, size2);
            return;
        }
        int childCount = getChildCount();
        int i7 = 0;
        int i8 = 0;
        while (i8 < childCount) {
            int measuredHeight = getChildAt(i8).getMeasuredHeight() + paddingTop;
            int i9 = i7;
            if (measuredHeight > i7) {
                i9 = measuredHeight;
            }
            i8++;
            i7 = i9;
        }
        setMeasuredDimension(size, i7);
    }

    @Override // android.support.v7.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public void setContentHeight(int i) {
        this.mContentHeight = i;
    }

    public void setCustomView(View view) {
        LinearLayout linearLayout;
        View view2 = this.mCustomView;
        if (view2 != null) {
            removeView(view2);
        }
        this.mCustomView = view;
        if (view != null && (linearLayout = this.mTitleLayout) != null) {
            removeView(linearLayout);
            this.mTitleLayout = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        initTitle();
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        initTitle();
    }

    public void setTitleOptional(boolean z) {
        if (z != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = z;
    }

    @Override // android.support.v7.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ViewPropertyAnimatorCompat setupAnimatorToVisibility(int i, long j) {
        return super.setupAnimatorToVisibility(i, j);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override // android.support.v7.widget.AbsActionBarView
    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }
}
