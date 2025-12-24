package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/Toolbar.class */
public class Toolbar extends ViewGroup {
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/Toolbar$ExpandedActionViewMenuPresenter.class */
    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;
        final Toolbar this$0;

        ExpandedActionViewMenuPresenter(Toolbar toolbar) {
            this.this$0 = toolbar;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            if (this.this$0.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) this.this$0.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar toolbar = this.this$0;
            toolbar.removeView(toolbar.mExpandedActionView);
            Toolbar toolbar2 = this.this$0;
            toolbar2.removeView(toolbar2.mCollapseButtonView);
            this.this$0.mExpandedActionView = null;
            this.this$0.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            this.this$0.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
            this.this$0.ensureCollapseButtonView();
            ViewParent parent = this.this$0.mCollapseButtonView.getParent();
            Toolbar toolbar = this.this$0;
            if (parent != toolbar) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(toolbar.mCollapseButtonView);
                }
                Toolbar toolbar2 = this.this$0;
                toolbar2.addView(toolbar2.mCollapseButtonView);
            }
            this.this$0.mExpandedActionView = menuItemImpl.getActionView();
            this.mCurrentExpandedItem = menuItemImpl;
            ViewParent parent2 = this.this$0.mExpandedActionView.getParent();
            Toolbar toolbar3 = this.this$0;
            if (parent2 != toolbar3) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(toolbar3.mExpandedActionView);
                }
                LayoutParams layoutParamsGenerateDefaultLayoutParams = this.this$0.generateDefaultLayoutParams();
                layoutParamsGenerateDefaultLayoutParams.gravity = 8388611 | (this.this$0.mButtonGravity & 112);
                layoutParamsGenerateDefaultLayoutParams.mViewType = 2;
                this.this$0.mExpandedActionView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
                Toolbar toolbar4 = this.this$0;
                toolbar4.addView(toolbar4.mExpandedActionView);
            }
            this.this$0.removeChildrenForExpandedActionView();
            this.this$0.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            if (!(this.this$0.mExpandedActionView instanceof CollapsibleActionView)) {
                return true;
            }
            ((CollapsibleActionView) this.this$0.mExpandedActionView).onActionViewExpanded();
            return true;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public boolean flagActionItems() {
            return false;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public int getId() {
            return 0;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public void initForMenu(Context context, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder2 = this.mMenu;
            if (menuBuilder2 != null && (menuItemImpl = this.mCurrentExpandedItem) != null) {
                menuBuilder2.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menuBuilder;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public void setCallback(MenuPresenter.Callback callback) {
        }

        @Override // android.support.v7.view.menu.MenuPresenter
        public void updateMenuView(boolean z) {
            if (this.mCurrentExpandedItem != null) {
                MenuBuilder menuBuilder = this.mMenu;
                boolean z2 = false;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    int i = 0;
                    while (true) {
                        z2 = false;
                        if (i >= size) {
                            break;
                        }
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            z2 = true;
                            break;
                        }
                        i++;
                    }
                }
                if (z2) {
                    return;
                }
                collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/Toolbar$LayoutParams.class */
    public static class LayoutParams extends ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(int i) {
            this(-2, -1, i);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2);
            this.mViewType = 0;
            this.gravity = i3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mViewType = 0;
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ActionBar.LayoutParams) layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.mViewType = 0;
            copyMarginsFromCompat(marginLayoutParams);
        }

        void copyMarginsFromCompat(ViewGroup.MarginLayoutParams marginLayoutParams) {
            this.leftMargin = marginLayoutParams.leftMargin;
            this.topMargin = marginLayoutParams.topMargin;
            this.rightMargin = marginLayoutParams.rightMargin;
            this.bottomMargin = marginLayoutParams.bottomMargin;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/Toolbar$OnMenuItemClickListener.class */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/Toolbar$SavedState.class */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v7.widget.Toolbar.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int expandedMenuItemId;
        boolean isOverflowOpen;

        public SavedState(Parcel parcel) {
            this(parcel, null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = parcel.readInt() != 0;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0287R.attr.toolbarStyle);
    }

    public Toolbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<>();
        this.mHiddenViews = new ArrayList<>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener(this) { // from class: android.support.v7.widget.Toolbar.1
            final Toolbar this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.ActionMenuView.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (this.this$0.mOnMenuItemClickListener != null) {
                    return this.this$0.mOnMenuItemClickListener.onMenuItemClick(menuItem);
                }
                return false;
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable(this) { // from class: android.support.v7.widget.Toolbar.2
            final Toolbar this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.this$0.showOverflowMenu();
            }
        };
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getContext(), attributeSet, C0287R.styleable.Toolbar, i, 0);
        this.mTitleTextAppearance = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = tintTypedArrayObtainStyledAttributes.getInteger(C0287R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = tintTypedArrayObtainStyledAttributes.getInteger(C0287R.styleable.Toolbar_buttonGravity, 48);
        int dimensionPixelOffset = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMargin, 0);
        int dimensionPixelOffset2 = tintTypedArrayObtainStyledAttributes.hasValue(C0287R.styleable.Toolbar_titleMargins) ? tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMargins, dimensionPixelOffset) : dimensionPixelOffset;
        this.mTitleMarginBottom = dimensionPixelOffset2;
        this.mTitleMarginTop = dimensionPixelOffset2;
        this.mTitleMarginEnd = dimensionPixelOffset2;
        this.mTitleMarginStart = dimensionPixelOffset2;
        int dimensionPixelOffset3 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMarginStart, -1);
        if (dimensionPixelOffset3 >= 0) {
            this.mTitleMarginStart = dimensionPixelOffset3;
        }
        int dimensionPixelOffset4 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMarginEnd, -1);
        if (dimensionPixelOffset4 >= 0) {
            this.mTitleMarginEnd = dimensionPixelOffset4;
        }
        int dimensionPixelOffset5 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMarginTop, -1);
        if (dimensionPixelOffset5 >= 0) {
            this.mTitleMarginTop = dimensionPixelOffset5;
        }
        int dimensionPixelOffset6 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_titleMarginBottom, -1);
        if (dimensionPixelOffset6 >= 0) {
            this.mTitleMarginBottom = dimensionPixelOffset6;
        }
        this.mMaxButtonHeight = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.Toolbar_maxButtonHeight, -1);
        int dimensionPixelOffset7 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int dimensionPixelOffset8 = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int dimensionPixelSize = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.Toolbar_contentInsetLeft, 0);
        int dimensionPixelSize2 = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelSize, dimensionPixelSize2);
        if (dimensionPixelOffset7 != Integer.MIN_VALUE || dimensionPixelOffset8 != Integer.MIN_VALUE) {
            this.mContentInsets.setRelative(dimensionPixelOffset7, dimensionPixelOffset8);
        }
        this.mContentInsetStartWithNavigation = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = tintTypedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.Toolbar_collapseContentDescription);
        CharSequence text = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        CharSequence text2 = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text2)) {
            setSubtitle(text2);
        }
        this.mPopupContext = getContext();
        setPopupTheme(tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.Toolbar_popupTheme, 0));
        Drawable drawable = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        CharSequence text3 = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            setNavigationContentDescription(text3);
        }
        Drawable drawable2 = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.Toolbar_logo);
        if (drawable2 != null) {
            setLogo(drawable2);
        }
        CharSequence text4 = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text4)) {
            setLogoDescription(text4);
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(C0287R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(tintTypedArrayObtainStyledAttributes.getColor(C0287R.styleable.Toolbar_titleTextColor, -1));
        }
        if (tintTypedArrayObtainStyledAttributes.hasValue(C0287R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(tintTypedArrayObtainStyledAttributes.getColor(C0287R.styleable.Toolbar_subtitleTextColor, -1));
        }
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    private void addCustomViewsWithGravity(List<View> list, int i) {
        boolean z = ViewCompat.getLayoutDirection(this) == 1;
        int childCount = getChildCount();
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this));
        list.clear();
        if (!z) {
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i3 = childCount - 1; i3 >= 0; i3--) {
            View childAt2 = getChildAt(i3);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.mViewType == 0 && shouldLayout(childAt2) && getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                list.add(childAt2);
            }
        }
    }

    private void addSystemView(View view, boolean z) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        LayoutParams layoutParamsGenerateDefaultLayoutParams = layoutParams == null ? generateDefaultLayoutParams() : !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : (LayoutParams) layoutParams;
        layoutParamsGenerateDefaultLayoutParams.mViewType = 1;
        if (!z || this.mExpandedActionView == null) {
            addView(view, layoutParamsGenerateDefaultLayoutParams);
        } else {
            view.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            this.mHiddenViews.add(view);
        }
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this);
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            ActionMenuView actionMenuView = new ActionMenuView(getContext());
            this.mMenuView = actionMenuView;
            actionMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.gravity = 8388613 | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            addSystemView(this.mMenuView, false);
        }
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), null, C0287R.attr.toolbarNavigationButtonStyle);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
        }
    }

    private int getChildHorizontalGravity(int i) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i, layoutDirection) & 7;
        if (absoluteGravity != 1) {
            int i2 = 3;
            if (absoluteGravity != 3 && absoluteGravity != 5) {
                if (layoutDirection == 1) {
                    i2 = 5;
                }
                return i2;
            }
        }
        return absoluteGravity;
    }

    private int getChildTop(View view, int i) {
        int iMax;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int measuredHeight = view.getMeasuredHeight();
        int i2 = i > 0 ? (measuredHeight - i) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(layoutParams.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - i2;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - measuredHeight) - layoutParams.bottomMargin) - i2;
        }
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int i3 = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
        if (i3 < layoutParams.topMargin) {
            iMax = layoutParams.topMargin;
        } else {
            int i4 = (((height - paddingBottom) - measuredHeight) - i3) - paddingTop;
            iMax = i3;
            if (i4 < layoutParams.bottomMargin) {
                iMax = Math.max(0, i3 - (layoutParams.bottomMargin - i4));
            }
        }
        return paddingTop + iMax;
    }

    private int getChildVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = i2;
        if (i2 != 16) {
            i3 = i2;
            if (i2 != 48) {
                i3 = i2;
                if (i2 != 80) {
                    i3 = this.mGravity & 112;
                }
            }
        }
        return i3;
    }

    private int getHorizontalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    private int getVerticalMargins(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int getViewListMeasuredWidth(List<View> list, int[] iArr) {
        int iMax = iArr[0];
        int iMax2 = iArr[1];
        int size = list.size();
        int measuredWidth = 0;
        for (int i = 0; i < size; i++) {
            View view = list.get(i);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i2 = layoutParams.leftMargin - iMax;
            int i3 = layoutParams.rightMargin - iMax2;
            int iMax3 = Math.max(0, i2);
            int iMax4 = Math.max(0, i3);
            iMax = Math.max(0, -i2);
            iMax2 = Math.max(0, -i3);
            measuredWidth += iMax3 + view.getMeasuredWidth() + iMax4;
        }
        return measuredWidth;
    }

    private boolean isChildOrHidden(View view) {
        return view.getParent() == this || this.mHiddenViews.contains(view);
    }

    private static boolean isCustomView(View view) {
        return ((LayoutParams) view.getLayoutParams()).mViewType == 0;
    }

    private int layoutChildLeft(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = layoutParams.leftMargin - iArr[0];
        int iMax = i + Math.max(0, i3);
        iArr[0] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(iMax, childTop, iMax + measuredWidth, view.getMeasuredHeight() + childTop);
        return iMax + measuredWidth + layoutParams.rightMargin;
    }

    private int layoutChildRight(View view, int i, int[] iArr, int i2) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i3 = layoutParams.rightMargin - iArr[1];
        int iMax = i - Math.max(0, i3);
        iArr[1] = Math.max(0, -i3);
        int childTop = getChildTop(view, i2);
        int measuredWidth = view.getMeasuredWidth();
        view.layout(iMax - measuredWidth, childTop, iMax, view.getMeasuredHeight() + childTop);
        return iMax - (measuredWidth + layoutParams.leftMargin);
    }

    private int measureChildCollapseMargins(View view, int i, int i2, int i3, int i4, int[] iArr) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i5 = marginLayoutParams.leftMargin - iArr[0];
        int i6 = marginLayoutParams.rightMargin - iArr[1];
        int iMax = Math.max(0, i5) + Math.max(0, i6);
        iArr[0] = Math.max(0, -i5);
        iArr[1] = Math.max(0, -i6);
        view.measure(getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + iMax + i2, marginLayoutParams.width), getChildMeasureSpec(i3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i4, marginLayoutParams.height));
        return view.getMeasuredWidth() + iMax;
    }

    private void measureChildConstrained(View view, int i, int i2, int i3, int i4, int i5) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int childMeasureSpec = getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + i2, marginLayoutParams.width);
        int childMeasureSpec2 = getChildMeasureSpec(i3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i4, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        int iMakeMeasureSpec = childMeasureSpec2;
        if (mode != 1073741824) {
            iMakeMeasureSpec = childMeasureSpec2;
            if (i5 >= 0) {
                int iMin = i5;
                if (mode != 0) {
                    iMin = Math.min(View.MeasureSpec.getSize(childMeasureSpec2), i5);
                }
                iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(iMin, 1073741824);
            }
        }
        view.measure(childMeasureSpec, iMakeMeasureSpec);
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    void addChildrenForExpandedActionView() {
        for (int size = this.mHiddenViews.size() - 1; size >= 0; size--) {
            addView(this.mHiddenViews.get(size));
        }
        this.mHiddenViews.clear();
    }

    public boolean canShowOverflowMenu() {
        ActionMenuView actionMenuView;
        return getVisibility() == 0 && (actionMenuView = this.mMenuView) != null && actionMenuView.isOverflowReserved();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.checkLayoutParams(layoutParams) && (layoutParams instanceof LayoutParams);
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            AppCompatImageButton appCompatImageButton = new AppCompatImageButton(getContext(), null, C0287R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView = appCompatImageButton;
            appCompatImageButton.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams layoutParamsGenerateDefaultLayoutParams = generateDefaultLayoutParams();
            layoutParamsGenerateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            layoutParamsGenerateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(layoutParamsGenerateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener(new View.OnClickListener(this) { // from class: android.support.v7.widget.Toolbar.3
                final Toolbar this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.collapseActionView();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : layoutParams instanceof ActionBar.LayoutParams ? new LayoutParams((ActionBar.LayoutParams) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getEnd() : 0;
    }

    public int getContentInsetEndWithActions() {
        int contentInsetEnd = this.mContentInsetEndWithActions;
        if (contentInsetEnd == Integer.MIN_VALUE) {
            contentInsetEnd = getContentInsetEnd();
        }
        return contentInsetEnd;
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getLeft() : 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getRight() : 0;
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getStart() : 0;
    }

    public int getContentInsetStartWithNavigation() {
        int contentInsetStart = this.mContentInsetStartWithNavigation;
        if (contentInsetStart == Integer.MIN_VALUE) {
            contentInsetStart = getContentInsetStart();
        }
        return contentInsetStart;
    }

    public int getCurrentContentInsetEnd() {
        MenuBuilder menuBuilderPeekMenu;
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && (menuBuilderPeekMenu = actionMenuView.peekMenu()) != null && menuBuilderPeekMenu.hasVisibleItems() ? Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0)) : getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        return ViewCompat.getLayoutDirection(this) == 1 ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        return ViewCompat.getLayoutDirection(this) == 1 ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
    }

    public int getCurrentContentInsetStart() {
        return getNavigationIcon() != null ? Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0)) : getContentInsetStart();
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getDrawable() : null;
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getContentDescription() : null;
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    Context getPopupContext() {
        return this.mPopupContext;
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    public void inflateMenu(int i) {
        getMenuInflater().inflate(i, getMenu());
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    public boolean isTitleTruncated() {
        Layout layout;
        TextView textView = this.mTitleTextView;
        if (textView == null || (layout = textView.getLayout()) == null) {
            return false;
        }
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (layout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !zOnHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked != 10 && actionMasked != 3) {
            return true;
        }
        this.mEatingHover = false;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x043e  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0543 A[LOOP:0: B:119:0x053d->B:121:0x0543, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0578 A[LOOP:1: B:123:0x0572->B:125:0x0578, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x05cb  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x05f1 A[LOOP:2: B:134:0x05eb->B:136:0x05f1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0215  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x024f  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0270  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0346  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x035b  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onLayout(boolean r8, int r9, int r10, int r11, int r12) throws java.lang.NoSuchFieldException {
        /*
            Method dump skipped, instructions count: 1556
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        Object[] objArr;
        Object[] objArr2;
        int measuredWidth;
        int iMax;
        int iCombineMeasuredStates;
        int measuredWidth2;
        int iMax2;
        int measuredHeight;
        int[] iArr = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            objArr = true;
            objArr2 = false;
        } else {
            objArr = false;
            objArr2 = true;
        }
        if (shouldLayout(this.mNavButtonView)) {
            measureChildConstrained(this.mNavButtonView, i, 0, i2, 0, this.mMaxButtonHeight);
            measuredWidth = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            iMax = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            iCombineMeasuredStates = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        } else {
            measuredWidth = 0;
            iMax = 0;
            iCombineMeasuredStates = 0;
        }
        int measuredWidth3 = measuredWidth;
        int iMax3 = iMax;
        int iCombineMeasuredStates2 = iCombineMeasuredStates;
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, i, 0, i2, 0, this.mMaxButtonHeight);
            measuredWidth3 = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            iMax3 = Math.max(iMax, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates, this.mCollapseButtonView.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int iMax4 = 0 + Math.max(currentContentInsetStart, measuredWidth3);
        iArr[objArr == true ? 1 : 0] = Math.max(0, currentContentInsetStart - measuredWidth3);
        if (shouldLayout(this.mMenuView)) {
            measureChildConstrained(this.mMenuView, i, iMax4, i2, 0, this.mMaxButtonHeight);
            measuredWidth2 = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            iMax3 = Math.max(iMax3, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates2, this.mMenuView.getMeasuredState());
        } else {
            measuredWidth2 = 0;
        }
        int currentContentInsetEnd = getCurrentContentInsetEnd();
        int iMax5 = iMax4 + Math.max(currentContentInsetEnd, measuredWidth2);
        iArr[objArr2 == true ? 1 : 0] = Math.max(0, currentContentInsetEnd - measuredWidth2);
        int iMax6 = iMax3;
        int iCombineMeasuredStates3 = iCombineMeasuredStates2;
        int iMeasureChildCollapseMargins = iMax5;
        if (shouldLayout(this.mExpandedActionView)) {
            iMeasureChildCollapseMargins = iMax5 + measureChildCollapseMargins(this.mExpandedActionView, i, iMax5, i2, 0, iArr);
            iMax6 = Math.max(iMax3, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            iCombineMeasuredStates3 = View.combineMeasuredStates(iCombineMeasuredStates2, this.mExpandedActionView.getMeasuredState());
        }
        int iMax7 = iMax6;
        int iCombineMeasuredStates4 = iCombineMeasuredStates3;
        int iMeasureChildCollapseMargins2 = iMeasureChildCollapseMargins;
        if (shouldLayout(this.mLogoView)) {
            iMeasureChildCollapseMargins2 = iMeasureChildCollapseMargins + measureChildCollapseMargins(this.mLogoView, i, iMeasureChildCollapseMargins, i2, 0, iArr);
            iMax7 = Math.max(iMax6, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            iCombineMeasuredStates4 = View.combineMeasuredStates(iCombineMeasuredStates3, this.mLogoView.getMeasuredState());
        }
        int childCount = getChildCount();
        int i3 = iMeasureChildCollapseMargins2;
        int iCombineMeasuredStates5 = iCombineMeasuredStates4;
        int i4 = iMax7;
        int i5 = 0;
        while (i5 < childCount) {
            View childAt = getChildAt(i5);
            int iMax8 = i4;
            int iCombineMeasuredStates6 = iCombineMeasuredStates5;
            int iMeasureChildCollapseMargins3 = i3;
            if (((LayoutParams) childAt.getLayoutParams()).mViewType == 0) {
                if (shouldLayout(childAt)) {
                    iMeasureChildCollapseMargins3 = i3 + measureChildCollapseMargins(childAt, i, i3, i2, 0, iArr);
                    iMax8 = Math.max(i4, childAt.getMeasuredHeight() + getVerticalMargins(childAt));
                    iCombineMeasuredStates6 = View.combineMeasuredStates(iCombineMeasuredStates5, childAt.getMeasuredState());
                } else {
                    iMax8 = i4;
                    iCombineMeasuredStates6 = iCombineMeasuredStates5;
                    iMeasureChildCollapseMargins3 = i3;
                }
            }
            i5++;
            i4 = iMax8;
            iCombineMeasuredStates5 = iCombineMeasuredStates6;
            i3 = iMeasureChildCollapseMargins3;
        }
        int i6 = this.mTitleMarginTop + this.mTitleMarginBottom;
        int i7 = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (shouldLayout(this.mTitleTextView)) {
            measureChildCollapseMargins(this.mTitleTextView, i, i3 + i7, i2, i6, iArr);
            int measuredWidth4 = this.mTitleTextView.getMeasuredWidth();
            int horizontalMargins = getHorizontalMargins(this.mTitleTextView);
            int measuredHeight2 = this.mTitleTextView.getMeasuredHeight();
            int verticalMargins = getVerticalMargins(this.mTitleTextView);
            iCombineMeasuredStates5 = View.combineMeasuredStates(iCombineMeasuredStates5, this.mTitleTextView.getMeasuredState());
            measuredHeight = measuredHeight2 + verticalMargins;
            iMax2 = measuredWidth4 + horizontalMargins;
        } else {
            iMax2 = 0;
            measuredHeight = 0;
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            iMax2 = Math.max(iMax2, measureChildCollapseMargins(this.mSubtitleTextView, i, i3 + i7, i2, measuredHeight + i6, iArr));
            measuredHeight += this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            iCombineMeasuredStates5 = View.combineMeasuredStates(iCombineMeasuredStates5, this.mSubtitleTextView.getMeasuredState());
        }
        int iMax9 = Math.max(i4, measuredHeight);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int iResolveSizeAndState = View.resolveSizeAndState(Math.max(i3 + iMax2 + paddingLeft + paddingRight, getSuggestedMinimumWidth()), i, (-16777216) & iCombineMeasuredStates5);
        int iResolveSizeAndState2 = View.resolveSizeAndState(Math.max(iMax9 + paddingTop + paddingBottom, getSuggestedMinimumHeight()), i2, iCombineMeasuredStates5 << 16);
        if (shouldCollapse()) {
            iResolveSizeAndState2 = 0;
        }
        setMeasuredDimension(iResolveSizeAndState, iResolveSizeAndState2);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        MenuItem menuItemFindItem;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        ActionMenuView actionMenuView = this.mMenuView;
        MenuBuilder menuBuilderPeekMenu = actionMenuView != null ? actionMenuView.peekMenu() : null;
        if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && menuBuilderPeekMenu != null && (menuItemFindItem = menuBuilderPeekMenu.findItem(savedState.expandedMenuItemId)) != null) {
            menuItemFindItem.expandActionView();
        }
        if (savedState.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(i);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        return savedState;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean zOnTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !zOnTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked != 1 && actionMasked != 3) {
            return true;
        }
        this.mEatingTouch = false;
        return true;
    }

    void removeChildrenForExpandedActionView() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (((LayoutParams) childAt.getLayoutParams()).mViewType != 2 && childAt != this.mMenuView) {
                removeViewAt(childCount);
                this.mHiddenViews.add(childAt);
            }
        }
    }

    public void setCollapsible(boolean z) {
        this.mCollapsible = z;
        requestLayout();
    }

    public void setContentInsetEndWithActions(int i) {
        int i2 = i;
        if (i < 0) {
            i2 = Integer.MIN_VALUE;
        }
        if (i2 != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = i2;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetStartWithNavigation(int i) {
        int i2 = i;
        if (i < 0) {
            i2 = Integer.MIN_VALUE;
        }
        if (i2 != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = i2;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetsAbsolute(int i, int i2) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(i, i2);
    }

    public void setContentInsetsRelative(int i, int i2) {
        ensureContentInsets();
        this.mContentInsets.setRelative(i, i2);
    }

    public void setLogo(int i) {
        setLogo(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            ImageView imageView = this.mLogoView;
            if (imageView != null && isChildOrHidden(imageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        ImageView imageView2 = this.mLogoView;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    public void setLogoDescription(int i) {
        setLogoDescription(getContext().getText(i));
    }

    public void setLogoDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public void setMenu(MenuBuilder menuBuilder, ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder == null && this.mMenuView == null) {
            return;
        }
        ensureMenuView();
        MenuBuilder menuBuilderPeekMenu = this.mMenuView.peekMenu();
        if (menuBuilderPeekMenu == menuBuilder) {
            return;
        }
        if (menuBuilderPeekMenu != null) {
            menuBuilderPeekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
            menuBuilderPeekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this);
        }
        actionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        } else {
            actionMenuPresenter.initForMenu(this.mPopupContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
            actionMenuPresenter.updateMenuView(true);
            this.mExpandedMenuPresenter.updateMenuView(true);
        }
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(actionMenuPresenter);
        this.mOuterActionMenuPresenter = actionMenuPresenter;
    }

    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(callback, callback2);
        }
    }

    public void setNavigationContentDescription(int i) {
        setNavigationContentDescription(i != 0 ? getContext().getText(i) : null);
    }

    public void setNavigationContentDescription(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setNavigationIcon(int i) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setNavigationIcon(Drawable drawable) {
        if (drawable != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            ImageButton imageButton = this.mNavButtonView;
            if (imageButton != null && isChildOrHidden(imageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        ImageButton imageButton2 = this.mNavButtonView;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(drawable);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(Drawable drawable) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(drawable);
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

    public void setSubtitle(int i) {
        setSubtitle(getContext().getText(i));
    }

    public void setSubtitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.mSubtitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mSubtitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mSubtitleTextColor;
                if (i2 != 0) {
                    this.mSubtitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    public void setSubtitleTextAppearance(Context context, int i) {
        this.mSubtitleTextAppearance = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setSubtitleTextColor(int i) {
        this.mSubtitleTextColor = i;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public void setTitle(int i) {
        setTitle(getContext().getText(i));
    }

    public void setTitle(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            TextView textView = this.mTitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        } else {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mTitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                int i2 = this.mTitleTextColor;
                if (i2 != 0) {
                    this.mTitleTextView.setTextColor(i2);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        }
        TextView textView2 = this.mTitleTextView;
        if (textView2 != null) {
            textView2.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }

    public void setTitleMargin(int i, int i2, int i3, int i4) {
        this.mTitleMarginStart = i;
        this.mTitleMarginTop = i2;
        this.mTitleMarginEnd = i3;
        this.mTitleMarginBottom = i4;
        requestLayout();
    }

    public void setTitleMarginBottom(int i) {
        this.mTitleMarginBottom = i;
        requestLayout();
    }

    public void setTitleMarginEnd(int i) {
        this.mTitleMarginEnd = i;
        requestLayout();
    }

    public void setTitleMarginStart(int i) {
        this.mTitleMarginStart = i;
        requestLayout();
    }

    public void setTitleMarginTop(int i) {
        this.mTitleMarginTop = i;
        requestLayout();
    }

    public void setTitleTextAppearance(Context context, int i) {
        this.mTitleTextAppearance = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, i);
        }
    }

    public void setTitleTextColor(int i) {
        this.mTitleTextColor = i;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }
}
