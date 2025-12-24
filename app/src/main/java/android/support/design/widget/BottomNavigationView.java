package android.support.design.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.C0029R;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.design.internal.ThemeEnforcement;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomNavigationView.class */
public class BottomNavigationView extends FrameLayout {
    private static final int MENU_PRESENTER_ID = 1;
    private final MenuBuilder menu;
    private MenuInflater menuInflater;
    private final BottomNavigationMenuView menuView;
    private final BottomNavigationPresenter presenter;
    private OnNavigationItemReselectedListener reselectedListener;
    private OnNavigationItemSelectedListener selectedListener;

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomNavigationView$OnNavigationItemReselectedListener.class */
    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(MenuItem menuItem);
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomNavigationView$OnNavigationItemSelectedListener.class */
    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/BottomNavigationView$SavedState.class */
    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.design.widget.BottomNavigationView.SavedState.1
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
        Bundle menuPresenterState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            readFromParcel(parcel, classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void readFromParcel(Parcel parcel, ClassLoader classLoader) {
            this.menuPresenterState = parcel.readBundle(classLoader);
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuPresenterState);
        }
    }

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.presenter = new BottomNavigationPresenter();
        this.menu = new BottomNavigationMenu(context);
        this.menuView = new BottomNavigationMenuView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        this.menuView.setLayoutParams(layoutParams);
        this.presenter.setBottomNavigationMenuView(this.menuView);
        this.presenter.setId(1);
        this.menuView.setPresenter(this.presenter);
        this.menu.addMenuPresenter(this.presenter);
        this.presenter.initForMenu(getContext(), this.menu);
        TintTypedArray tintTypedArrayObtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context, attributeSet, C0029R.styleable.BottomNavigationView, i, C0029R.style.Widget_Design_BottomNavigationView, C0029R.styleable.BottomNavigationView_itemTextAppearanceInactive, C0029R.styleable.BottomNavigationView_itemTextAppearanceActive);
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_itemIconTint)) {
            this.menuView.setIconTintList(tintTypedArrayObtainTintedStyledAttributes.getColorStateList(C0029R.styleable.BottomNavigationView_itemIconTint));
        } else {
            BottomNavigationMenuView bottomNavigationMenuView = this.menuView;
            bottomNavigationMenuView.setIconTintList(bottomNavigationMenuView.createDefaultColorStateList(R.attr.textColorSecondary));
        }
        setItemIconSize(tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.BottomNavigationView_itemIconSize, getResources().getDimensionPixelSize(C0029R.dimen.design_bottom_navigation_icon_size)));
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_itemTextAppearanceInactive)) {
            setItemTextAppearanceInactive(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
        }
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_itemTextAppearanceActive)) {
            setItemTextAppearanceActive(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.BottomNavigationView_itemTextAppearanceActive, 0));
        }
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_itemTextColor)) {
            setItemTextColor(tintTypedArrayObtainTintedStyledAttributes.getColorStateList(C0029R.styleable.BottomNavigationView_itemTextColor));
        }
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation(this, tintTypedArrayObtainTintedStyledAttributes.getDimensionPixelSize(C0029R.styleable.BottomNavigationView_elevation, 0));
        }
        setLabelVisibilityMode(tintTypedArrayObtainTintedStyledAttributes.getInteger(C0029R.styleable.BottomNavigationView_labelVisibilityMode, -1));
        setItemHorizontalTranslationEnabled(tintTypedArrayObtainTintedStyledAttributes.getBoolean(C0029R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        this.menuView.setItemBackgroundRes(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.BottomNavigationView_itemBackground, 0));
        if (tintTypedArrayObtainTintedStyledAttributes.hasValue(C0029R.styleable.BottomNavigationView_menu)) {
            inflateMenu(tintTypedArrayObtainTintedStyledAttributes.getResourceId(C0029R.styleable.BottomNavigationView_menu, 0));
        }
        tintTypedArrayObtainTintedStyledAttributes.recycle();
        addView(this.menuView, layoutParams);
        if (Build.VERSION.SDK_INT < 21) {
            addCompatibilityTopDivider(context);
        }
        this.menu.setCallback(new MenuBuilder.Callback(this) { // from class: android.support.design.widget.BottomNavigationView.1
            final BottomNavigationView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                boolean z = true;
                if (this.this$0.reselectedListener != null && menuItem.getItemId() == this.this$0.getSelectedItemId()) {
                    this.this$0.reselectedListener.onNavigationItemReselected(menuItem);
                    return true;
                }
                if (this.this$0.selectedListener == null || this.this$0.selectedListener.onNavigationItemSelected(menuItem)) {
                    z = false;
                }
                return z;
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }
        });
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, C0029R.color.design_bottom_navigation_shadow_color));
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(C0029R.dimen.design_bottom_navigation_shadow_height)));
        addView(view);
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public int getMaxItemCount() {
        return 5;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void inflateMenu(int i) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.menuView.isItemHorizontalTranslationEnabled();
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.menu.restorePresenterStates(savedState.menuPresenterState);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    public void setItemBackground(Drawable drawable) {
        this.menuView.setItemBackground(drawable);
    }

    public void setItemBackgroundResource(int i) {
        this.menuView.setItemBackgroundRes(i);
    }

    public void setItemHorizontalTranslationEnabled(boolean z) {
        if (this.menuView.isItemHorizontalTranslationEnabled() != z) {
            this.menuView.setItemHorizontalTranslationEnabled(z);
            this.presenter.updateMenuView(false);
        }
    }

    public void setItemIconSize(int i) {
        this.menuView.setItemIconSize(i);
    }

    public void setItemIconSizeRes(int i) {
        setItemIconSize(getResources().getDimensionPixelSize(i));
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.menuView.setIconTintList(colorStateList);
    }

    public void setItemTextAppearanceActive(int i) {
        this.menuView.setItemTextAppearanceActive(i);
    }

    public void setItemTextAppearanceInactive(int i) {
        this.menuView.setItemTextAppearanceInactive(i);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.menuView.setItemTextColor(colorStateList);
    }

    public void setLabelVisibilityMode(int i) {
        if (this.menuView.getLabelVisibilityMode() != i) {
            this.menuView.setLabelVisibilityMode(i);
            this.presenter.updateMenuView(false);
        }
    }

    public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener onNavigationItemReselectedListener) {
        this.reselectedListener = onNavigationItemReselectedListener;
    }

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.selectedListener = onNavigationItemSelectedListener;
    }

    public void setSelectedItemId(int i) {
        MenuItem menuItemFindItem = this.menu.findItem(i);
        if (menuItemFindItem == null || this.menu.performItemAction(menuItemFindItem, this.presenter, 0)) {
            return;
        }
        menuItemFindItem.setChecked(true);
    }
}
