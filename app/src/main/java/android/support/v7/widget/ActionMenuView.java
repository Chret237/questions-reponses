package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView.class */
public class ActionMenuView extends LinearLayoutCompat implements MenuBuilder.ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView$ActionMenuChildView.class */
    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        boolean needsDividerBefore();
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView$ActionMenuPresenterCallback.class */
    private static class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            return false;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView$LayoutParams.class */
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams {

        @ViewDebug.ExportedProperty
        public int cellsUsed;

        @ViewDebug.ExportedProperty
        public boolean expandable;
        boolean expanded;

        @ViewDebug.ExportedProperty
        public int extraPixels;

        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;

        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.isOverflowButton = false;
        }

        LayoutParams(int i, int i2, boolean z) {
            super(i, i2);
            this.isOverflowButton = z;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams) layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView$MenuBuilderCallback.class */
    private class MenuBuilderCallback implements MenuBuilder.Callback {
        final ActionMenuView this$0;

        MenuBuilderCallback(ActionMenuView actionMenuView) {
            this.this$0 = actionMenuView;
        }

        @Override // android.support.v7.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return this.this$0.mOnMenuItemClickListener != null && this.this$0.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }

        @Override // android.support.v7.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.this$0.mMenuBuilderCallback != null) {
                this.this$0.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActionMenuView$OnMenuItemClickListener.class */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * f);
        this.mGeneratedItemPadding = (int) (f * 4.0f);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    static int measureChildForCells(View view, int i, int i2, int i3, int i4) {
        int i5;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i3) - i4, View.MeasureSpec.getMode(i3));
        ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean z = true;
        boolean z2 = actionMenuItemView != null && actionMenuItemView.hasText();
        if (i2 <= 0 || (z2 && i2 < 2)) {
            i5 = 0;
        } else {
            view.measure(View.MeasureSpec.makeMeasureSpec(i2 * i, Integer.MIN_VALUE), iMakeMeasureSpec);
            int measuredWidth = view.getMeasuredWidth();
            int i6 = measuredWidth / i;
            i5 = i6;
            if (measuredWidth % i != 0) {
                i5 = i6 + 1;
            }
            if (z2 && i5 < 2) {
                i5 = 2;
            }
        }
        if (layoutParams.isOverflowButton || !z2) {
            z = false;
        }
        layoutParams.expandable = z;
        layoutParams.cellsUsed = i5;
        view.measure(View.MeasureSpec.makeMeasureSpec(i * i5, 1073741824), iMakeMeasureSpec);
        return i5;
    }

    private void onMeasureExactFormat(int i, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        long j;
        int i3;
        int i4;
        long j2;
        int i5;
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int childMeasureSpec = getChildMeasureSpec(i2, paddingTop, -2);
        int i6 = size - (paddingLeft + paddingRight);
        int i7 = this.mMinCellSize;
        int i8 = i6 / i7;
        if (i8 == 0) {
            setMeasuredDimension(i6, 0);
            return;
        }
        int i9 = i7 + ((i6 % i7) / i8);
        int childCount = getChildCount();
        int iMax = 0;
        int i10 = 0;
        boolean z4 = false;
        int i11 = 0;
        int iMax2 = 0;
        int i12 = 0;
        long j3 = 0;
        while (i10 < childCount) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() == 8) {
                i5 = i12;
            } else {
                boolean z5 = childAt instanceof ActionMenuItemView;
                i11++;
                if (z5) {
                    int i13 = this.mGeneratedItemPadding;
                    childAt.setPadding(i13, 0, i13, 0);
                }
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = z5 && ((ActionMenuItemView) childAt).hasText();
                int iMeasureChildForCells = measureChildForCells(childAt, i9, layoutParams.isOverflowButton ? 1 : i8, childMeasureSpec, paddingTop);
                iMax2 = Math.max(iMax2, iMeasureChildForCells);
                i5 = i12;
                if (layoutParams.expandable) {
                    i5 = i12 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    z4 = true;
                }
                i8 -= iMeasureChildForCells;
                iMax = Math.max(iMax, childAt.getMeasuredHeight());
                if (iMeasureChildForCells == 1) {
                    j3 |= 1 << i10;
                }
            }
            i10++;
            i12 = i5;
        }
        boolean z6 = false;
        int i14 = i8;
        boolean z7 = z4 && i11 == 2;
        while (i12 > 0 && i14 > 0) {
            int i15 = 0;
            int i16 = 0;
            int i17 = Integer.MAX_VALUE;
            long j4 = 0;
            while (true) {
                j = j4;
                if (i16 >= childCount) {
                    break;
                }
                LayoutParams layoutParams2 = (LayoutParams) getChildAt(i16).getLayoutParams();
                if (!layoutParams2.expandable) {
                    i3 = i15;
                    i4 = i17;
                    j2 = j;
                } else if (layoutParams2.cellsUsed < i17) {
                    i4 = layoutParams2.cellsUsed;
                    j2 = 1 << i16;
                    i3 = 1;
                } else {
                    i3 = i15;
                    i4 = i17;
                    j2 = j;
                    if (layoutParams2.cellsUsed == i17) {
                        i3 = i15 + 1;
                        j2 = j | (1 << i16);
                        i4 = i17;
                    }
                }
                i16++;
                i15 = i3;
                i17 = i4;
                j4 = j2;
            }
            z = z6;
            j3 |= j;
            if (i15 > i14) {
                break;
            }
            for (int i18 = 0; i18 < childCount; i18++) {
                View childAt2 = getChildAt(i18);
                LayoutParams layoutParams3 = (LayoutParams) childAt2.getLayoutParams();
                long j5 = 1 << i18;
                if ((j & j5) == 0) {
                    long j6 = j3;
                    if (layoutParams3.cellsUsed == i17 + 1) {
                        j6 = j3 | j5;
                    }
                    j3 = j6;
                } else {
                    if (z7 && layoutParams3.preventEdgeOffset && i14 == 1) {
                        int i19 = this.mGeneratedItemPadding;
                        childAt2.setPadding(i19 + i9, 0, i19, 0);
                    }
                    layoutParams3.cellsUsed++;
                    layoutParams3.expanded = true;
                    i14--;
                }
            }
            z6 = true;
        }
        z = z6;
        boolean z8 = !z4 && i11 == 1;
        if (i14 > 0 && j3 != 0 && (i14 < i11 - 1 || z8 || iMax2 > 1)) {
            float fBitCount = Long.bitCount(j3);
            if (!z8) {
                float f = fBitCount;
                if ((j3 & 1) != 0) {
                    f = fBitCount;
                    if (!((LayoutParams) getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        f = fBitCount - 0.5f;
                    }
                }
                int i20 = childCount - 1;
                fBitCount = f;
                if ((j3 & (1 << i20)) != 0) {
                    fBitCount = f;
                    if (!((LayoutParams) getChildAt(i20).getLayoutParams()).preventEdgeOffset) {
                        fBitCount = f - 0.5f;
                    }
                }
            }
            int i21 = fBitCount > 0.0f ? (int) ((i14 * i9) / fBitCount) : 0;
            int i22 = 0;
            while (true) {
                z2 = z;
                if (i22 >= childCount) {
                    break;
                }
                if ((j3 & (1 << i22)) == 0) {
                    z3 = z;
                } else {
                    View childAt3 = getChildAt(i22);
                    LayoutParams layoutParams4 = (LayoutParams) childAt3.getLayoutParams();
                    if (childAt3 instanceof ActionMenuItemView) {
                        layoutParams4.extraPixels = i21;
                        layoutParams4.expanded = true;
                        if (i22 == 0 && !layoutParams4.preventEdgeOffset) {
                            layoutParams4.leftMargin = (-i21) / 2;
                        }
                    } else if (layoutParams4.isOverflowButton) {
                        layoutParams4.extraPixels = i21;
                        layoutParams4.expanded = true;
                        layoutParams4.rightMargin = (-i21) / 2;
                    } else {
                        if (i22 != 0) {
                            layoutParams4.leftMargin = i21 / 2;
                        }
                        z3 = z;
                        if (i22 != childCount - 1) {
                            layoutParams4.rightMargin = i21 / 2;
                            z3 = z;
                        }
                    }
                    z3 = true;
                }
                i22++;
                z = z3;
            }
        } else {
            z2 = z;
        }
        if (z2) {
            for (int i23 = 0; i23 < childCount; i23++) {
                View childAt4 = getChildAt(i23);
                LayoutParams layoutParams5 = (LayoutParams) childAt4.getLayoutParams();
                if (layoutParams5.expanded) {
                    childAt4.measure(View.MeasureSpec.makeMeasureSpec((layoutParams5.cellsUsed * i9) + layoutParams5.extraPixels, 1073741824), childMeasureSpec);
                }
            }
        }
        if (mode == 1073741824) {
            iMax = size2;
        }
        setMeasuredDimension(i6, iMax);
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams != null && (layoutParams instanceof LayoutParams);
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams == null) {
            return generateDefaultLayoutParams();
        }
        LayoutParams layoutParams2 = layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : new LayoutParams(layoutParams);
        if (layoutParams2.gravity <= 0) {
            layoutParams2.gravity = 16;
        }
        return layoutParams2;
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
        layoutParamsGenerateDefaultLayoutParams.isOverflowButton = true;
        return layoutParamsGenerateDefaultLayoutParams;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            MenuBuilder menuBuilder = new MenuBuilder(context);
            this.mMenu = menuBuilder;
            menuBuilder.setCallback(new MenuBuilderCallback(this));
            ActionMenuPresenter actionMenuPresenter = new ActionMenuPresenter(context);
            this.mPresenter = actionMenuPresenter;
            actionMenuPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter2 = this.mPresenter;
            MenuPresenter.Callback actionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            if (actionMenuPresenterCallback == null) {
                actionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter2.setCallback(actionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @Override // android.support.v7.view.menu.MenuView
    public int getWindowAnimations() {
        return 0;
    }

    protected boolean hasSupportDividerBeforeChildAt(int i) {
        if (i == 0) {
            return false;
        }
        KeyEvent.Callback childAt = getChildAt(i - 1);
        KeyEvent.Callback childAt2 = getChildAt(i);
        boolean zNeedsDividerAfter = false;
        if (i < getChildCount()) {
            zNeedsDividerAfter = false;
            if (childAt instanceof ActionMenuChildView) {
                zNeedsDividerAfter = false | ((ActionMenuChildView) childAt).needsDividerAfter();
            }
        }
        boolean zNeedsDividerBefore = zNeedsDividerAfter;
        if (i > 0) {
            zNeedsDividerBefore = zNeedsDividerAfter;
            if (childAt2 instanceof ActionMenuChildView) {
                zNeedsDividerBefore = zNeedsDividerAfter | ((ActionMenuChildView) childAt2).needsDividerBefore();
            }
        }
        return zNeedsDividerBefore;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu();
    }

    @Override // android.support.v7.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @Override // android.support.v7.view.menu.MenuBuilder.ItemInvoker
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width;
        int paddingLeft;
        if (!this.mFormatItems) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int childCount = getChildCount();
        int i5 = (i4 - i2) / 2;
        int dividerWidth = getDividerWidth();
        int i6 = i3 - i;
        int paddingRight = (i6 - getPaddingRight()) - getPaddingLeft();
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int i10 = measuredWidth;
                    if (hasSupportDividerBeforeChildAt(i9)) {
                        i10 = measuredWidth + dividerWidth;
                    }
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (zIsLayoutRtl) {
                        paddingLeft = getPaddingLeft() + layoutParams.leftMargin;
                        width = paddingLeft + i10;
                    } else {
                        width = (getWidth() - getPaddingRight()) - layoutParams.rightMargin;
                        paddingLeft = width - i10;
                    }
                    int i11 = i5 - (measuredHeight / 2);
                    childAt.layout(paddingLeft, i11, width, measuredHeight + i11);
                    paddingRight -= i10;
                    i7 = 1;
                } else {
                    paddingRight -= (childAt.getMeasuredWidth() + layoutParams.leftMargin) + layoutParams.rightMargin;
                    hasSupportDividerBeforeChildAt(i9);
                    i8++;
                }
            }
        }
        if (childCount == 1 && i7 == 0) {
            View childAt2 = getChildAt(0);
            int measuredWidth2 = childAt2.getMeasuredWidth();
            int measuredHeight2 = childAt2.getMeasuredHeight();
            int i12 = (i6 / 2) - (measuredWidth2 / 2);
            int i13 = i5 - (measuredHeight2 / 2);
            childAt2.layout(i12, i13, measuredWidth2 + i12, measuredHeight2 + i13);
            return;
        }
        int i14 = i8 - (i7 ^ 1);
        int iMax = Math.max(0, i14 > 0 ? paddingRight / i14 : 0);
        if (zIsLayoutRtl) {
            int width2 = getWidth() - getPaddingRight();
            int i15 = 0;
            while (i15 < childCount) {
                View childAt3 = getChildAt(i15);
                LayoutParams layoutParams2 = (LayoutParams) childAt3.getLayoutParams();
                int i16 = width2;
                if (childAt3.getVisibility() != 8) {
                    if (layoutParams2.isOverflowButton) {
                        i16 = width2;
                    } else {
                        int i17 = width2 - layoutParams2.rightMargin;
                        int measuredWidth3 = childAt3.getMeasuredWidth();
                        int measuredHeight3 = childAt3.getMeasuredHeight();
                        int i18 = i5 - (measuredHeight3 / 2);
                        childAt3.layout(i17 - measuredWidth3, i18, i17, measuredHeight3 + i18);
                        i16 = i17 - ((measuredWidth3 + layoutParams2.leftMargin) + iMax);
                    }
                }
                i15++;
                width2 = i16;
            }
            return;
        }
        int paddingLeft2 = getPaddingLeft();
        int i19 = 0;
        while (i19 < childCount) {
            View childAt4 = getChildAt(i19);
            LayoutParams layoutParams3 = (LayoutParams) childAt4.getLayoutParams();
            int i20 = paddingLeft2;
            if (childAt4.getVisibility() != 8) {
                if (layoutParams3.isOverflowButton) {
                    i20 = paddingLeft2;
                } else {
                    int i21 = paddingLeft2 + layoutParams3.leftMargin;
                    int measuredWidth4 = childAt4.getMeasuredWidth();
                    int measuredHeight4 = childAt4.getMeasuredHeight();
                    int i22 = i5 - (measuredHeight4 / 2);
                    childAt4.layout(i21, i22, i21 + measuredWidth4, measuredHeight4 + i22);
                    i20 = i21 + measuredWidth4 + layoutParams3.rightMargin + iMax;
                }
            }
            i19++;
            paddingLeft2 = i20;
        }
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.View
    protected void onMeasure(int i, int i2) {
        MenuBuilder menuBuilder;
        boolean z = this.mFormatItems;
        boolean z2 = View.MeasureSpec.getMode(i) == 1073741824;
        this.mFormatItems = z2;
        if (z != z2) {
            this.mFormatItemsWidth = 0;
        }
        int size = View.MeasureSpec.getSize(i);
        if (this.mFormatItems && (menuBuilder = this.mMenu) != null && size != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = size;
            menuBuilder.onItemsChanged(true);
        }
        int childCount = getChildCount();
        if (this.mFormatItems && childCount > 0) {
            onMeasureExactFormat(i, i2);
            return;
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i3).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
        }
        super.onMeasure(i, i2);
    }

    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public void setExpandedActionViewsExclusive(boolean z) {
        this.mPresenter.setExpandedActionViewsExclusive(z);
    }

    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(Drawable drawable) {
        getMenu();
        this.mPresenter.setOverflowIcon(drawable);
    }

    public void setOverflowReserved(boolean z) {
        this.mReserveOverflow = z;
    }

    public void setPopupTheme(int i) {
        if (this.mPopupTheme != i) {
            this.mPopupTheme = i;
            if (i == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), i);
            }
        }
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        actionMenuPresenter.setMenuView(this);
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }
}
