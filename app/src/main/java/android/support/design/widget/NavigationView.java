package android.support.design.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.C0029R;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.internal.ThemeEnforcement;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/NavigationView.class */
public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private final NavigationMenuPresenter presenter;

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/NavigationView$OnNavigationItemSelectedListener.class */
    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/NavigationView$SavedState.class */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.design.widget.NavigationView.SavedState.1
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
        public Bundle menuState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuState);
        }
    }

    public NavigationView(Context context) {
        this(context, null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.navigationViewStyle);
    }

    public NavigationView(Context context, AttributeSet attributeSet, int i) throws Resources.NotFoundException {
        int resourceId;
        boolean z;
        super(context, attributeSet, i);
        this.presenter = new NavigationMenuPresenter();
        this.menu = new NavigationMenu(context);
        TintTypedArray tintTypedArrayObtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context, attributeSet, C0029R.styleable.NavigationView, i, C0029R.style.Widget_Design_NavigationView, new int[0]);
        ViewCompat.setBackground(this, tintTypedArrayObtainTintedStyledAttributes.getDrawable(C0029R.styleable.NavigationView_android_background));
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation(this, tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows(this, tintTypedArrayObtainTintedStyledAttributes.getBoolean(C0029R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.maxWidth = tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.NavigationView_android_maxWidth, 0);
        ColorStateList colorStateList = tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_itemIconTint) ? tintTypedArrayObtainTintedStyledAttributes.getColorStateList(C0029R.styleable.NavigationView_itemIconTint) : createDefaultColorStateList(R.attr.textColorSecondary);
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_itemTextAppearance)) {
            resourceId = tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.NavigationView_itemTextAppearance, 0);
            z = true;
        } else {
            resourceId = 0;
            z = false;
        }
        ColorStateList colorStateList2 = tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_itemTextColor) ? tintTypedArrayObtainTintedStyledAttributes.getColorStateList(C0029R.styleable.NavigationView_itemTextColor) : null;
        ColorStateList colorStateListCreateDefaultColorStateList = colorStateList2;
        if (!z) {
            colorStateListCreateDefaultColorStateList = colorStateList2;
            if (colorStateList2 == null) {
                colorStateListCreateDefaultColorStateList = createDefaultColorStateList(R.attr.textColorPrimary);
            }
        }
        Drawable drawable = tintTypedArrayObtainTintedStyledAttributes.getDrawable(C0029R.styleable.NavigationView_itemBackground);
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_itemHorizontalPadding)) {
            this.presenter.setItemHorizontalPadding(tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.NavigationView_itemHorizontalPadding, 0));
        }
        int dimensionPixelSize = tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.NavigationView_itemIconPadding, 0);
        this.menu.setCallback(new MenuBuilder.Callback(this) { // from class: android.support.design.widget.NavigationView.1
            final NavigationView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                return this.this$0.listener != null && this.this$0.listener.onNavigationItemSelected(menuItem);
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
        this.presenter.setId(1);
        this.presenter.initForMenu(context, this.menu);
        this.presenter.setItemIconTintList(colorStateList);
        if (z) {
            this.presenter.setItemTextAppearance(resourceId);
        }
        this.presenter.setItemTextColor(colorStateListCreateDefaultColorStateList);
        this.presenter.setItemBackground(drawable);
        this.presenter.setItemIconPadding(dimensionPixelSize);
        this.menu.addMenuPresenter(this.presenter);
        addView((View) this.presenter.getMenuView(this));
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_menu)) {
            inflateMenu(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.NavigationView_menu, 0));
        }
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.NavigationView_headerLayout)) {
            inflateHeaderView(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.NavigationView_headerLayout, 0));
        }
        tintTypedArrayObtainTintedStyledAttributes.recycle();
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [int[], int[][]] */
    private ColorStateList createDefaultColorStateList(int i) throws Resources.NotFoundException {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0287R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        return new ColorStateList(new int[]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(DISABLED_STATE_SET, defaultColor), i2, defaultColor});
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    public void addHeaderView(View view) {
        this.presenter.addHeaderView(view);
    }

    public MenuItem getCheckedItem() {
        return this.presenter.getCheckedItem();
    }

    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }

    public View getHeaderView(int i) {
        return this.presenter.getHeaderView(i);
    }

    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }

    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }

    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }

    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }

    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }

    public Menu getMenu() {
        return this.menu;
    }

    public View inflateHeaderView(int i) {
        return this.presenter.inflateHeaderView(i);
    }

    public void inflateMenu(int i) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }

    @Override // android.support.design.internal.ScrimInsetsFrameLayout
    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.presenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            i = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i), this.maxWidth), 1073741824);
        } else if (mode == 0) {
            i = View.MeasureSpec.makeMeasureSpec(this.maxWidth, 1073741824);
        }
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.menu.restorePresenterStates(savedState.menuState);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    public void removeHeaderView(View view) {
        this.presenter.removeHeaderView(view);
    }

    public void setCheckedItem(int i) {
        MenuItem menuItemFindItem = this.menu.findItem(i);
        if (menuItemFindItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl) menuItemFindItem);
        }
    }

    public void setCheckedItem(MenuItem menuItem) {
        MenuItem menuItemFindItem = this.menu.findItem(menuItem.getItemId());
        if (menuItemFindItem == null) {
            throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
        }
        this.presenter.setCheckedItem((MenuItemImpl) menuItemFindItem);
    }

    public void setItemBackground(Drawable drawable) {
        this.presenter.setItemBackground(drawable);
    }

    public void setItemBackgroundResource(int i) {
        setItemBackground(ContextCompat.getDrawable(getContext(), i));
    }

    public void setItemHorizontalPadding(int i) {
        this.presenter.setItemHorizontalPadding(i);
    }

    public void setItemHorizontalPaddingResource(int i) {
        this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(i));
    }

    public void setItemIconPadding(int i) {
        this.presenter.setItemIconPadding(i);
    }

    public void setItemIconPaddingResource(int i) {
        this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(i));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.presenter.setItemIconTintList(colorStateList);
    }

    public void setItemTextAppearance(int i) {
        this.presenter.setItemTextAppearance(i);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.presenter.setItemTextColor(colorStateList);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.listener = onNavigationItemSelectedListener;
    }
}
