package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.C0029R;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter.class */
public class NavigationMenuPresenter implements MenuPresenter {
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HEADER = "android:menu:header";
    private static final String STATE_HIERARCHY = "android:menu:list";
    NavigationMenuAdapter adapter;
    private MenuPresenter.Callback callback;
    LinearLayout headerLayout;
    ColorStateList iconTintList;

    /* renamed from: id */
    private int f18id;
    Drawable itemBackground;
    int itemHorizontalPadding;
    int itemIconPadding;
    LayoutInflater layoutInflater;
    MenuBuilder menu;
    private NavigationMenuView menuView;
    final View.OnClickListener onClickListener = new View.OnClickListener(this) { // from class: android.support.design.internal.NavigationMenuPresenter.1
        final NavigationMenuPresenter this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.this$0.setUpdateSuspended(true);
            MenuItemImpl itemData = ((NavigationMenuItemView) view).getItemData();
            boolean zPerformItemAction = this.this$0.menu.performItemAction(itemData, this.this$0, 0);
            if (itemData != null && itemData.isCheckable() && zPerformItemAction) {
                this.this$0.adapter.setCheckedItem(itemData);
            }
            this.this$0.setUpdateSuspended(false);
            this.this$0.updateMenuView(false);
        }
    };
    int paddingSeparator;
    private int paddingTopDefault;
    int textAppearance;
    boolean textAppearanceSet;
    ColorStateList textColor;

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$HeaderViewHolder.class */
    private static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NavigationMenuAdapter.class */
    private class NavigationMenuAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl checkedItem;
        private final ArrayList<NavigationMenuItem> items = new ArrayList<>();
        final NavigationMenuPresenter this$0;
        private boolean updateSuspended;

        NavigationMenuAdapter(NavigationMenuPresenter navigationMenuPresenter) {
            this.this$0 = navigationMenuPresenter;
            prepareMenuItems();
        }

        private void appendTransparentIconIfMissing(int i, int i2) {
            while (i < i2) {
                ((NavigationMenuTextItem) this.items.get(i)).needsEmptyIcon = true;
                i++;
            }
        }

        private void prepareMenuItems() {
            int groupId;
            boolean z;
            int i;
            int i2;
            boolean z2;
            if (this.updateSuspended) {
                return;
            }
            this.updateSuspended = true;
            this.items.clear();
            this.items.add(new NavigationMenuHeaderItem());
            int i3 = -1;
            int size = this.this$0.menu.getVisibleItems().size();
            int i4 = 0;
            boolean z3 = false;
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i4 >= size) {
                    this.updateSuspended = false;
                    return;
                }
                MenuItemImpl menuItemImpl = this.this$0.menu.getVisibleItems().get(i4);
                if (menuItemImpl.isChecked()) {
                    setCheckedItem(menuItemImpl);
                }
                if (menuItemImpl.isCheckable()) {
                    menuItemImpl.setExclusiveCheckable(false);
                }
                if (menuItemImpl.hasSubMenu()) {
                    SubMenu subMenu = menuItemImpl.getSubMenu();
                    groupId = i3;
                    z = z3;
                    i2 = i6;
                    if (subMenu.hasVisibleItems()) {
                        if (i4 != 0) {
                            this.items.add(new NavigationMenuSeparatorItem(this.this$0.paddingSeparator, 0));
                        }
                        this.items.add(new NavigationMenuTextItem(menuItemImpl));
                        int size2 = this.items.size();
                        int size3 = subMenu.size();
                        int i7 = 0;
                        boolean z4 = false;
                        while (true) {
                            z2 = z4;
                            if (i7 >= size3) {
                                break;
                            }
                            MenuItemImpl menuItemImpl2 = (MenuItemImpl) subMenu.getItem(i7);
                            boolean z5 = z2;
                            if (menuItemImpl2.isVisible()) {
                                z5 = z2;
                                if (!z2) {
                                    z5 = z2;
                                    if (menuItemImpl2.getIcon() != null) {
                                        z5 = true;
                                    }
                                }
                                if (menuItemImpl2.isCheckable()) {
                                    menuItemImpl2.setExclusiveCheckable(false);
                                }
                                if (menuItemImpl.isChecked()) {
                                    setCheckedItem(menuItemImpl);
                                }
                                this.items.add(new NavigationMenuTextItem(menuItemImpl2));
                            }
                            i7++;
                            z4 = z5;
                        }
                        groupId = i3;
                        z = z3;
                        i2 = i6;
                        if (z2) {
                            appendTransparentIconIfMissing(size2, this.items.size());
                            groupId = i3;
                            z = z3;
                            i2 = i6;
                        }
                    }
                } else {
                    groupId = menuItemImpl.getGroupId();
                    if (groupId != i3) {
                        int size4 = this.items.size();
                        boolean z6 = menuItemImpl.getIcon() != null;
                        z = z6;
                        i = size4;
                        if (i4 != 0) {
                            i = size4 + 1;
                            this.items.add(new NavigationMenuSeparatorItem(this.this$0.paddingSeparator, this.this$0.paddingSeparator));
                            z = z6;
                        }
                    } else {
                        z = z3;
                        i = i6;
                        if (!z3) {
                            z = z3;
                            i = i6;
                            if (menuItemImpl.getIcon() != null) {
                                appendTransparentIconIfMissing(i6, this.items.size());
                                z = true;
                                i = i6;
                            }
                        }
                    }
                    NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuTextItem(menuItemImpl);
                    navigationMenuTextItem.needsEmptyIcon = z;
                    this.items.add(navigationMenuTextItem);
                    i2 = i;
                }
                i4++;
                i3 = groupId;
                z3 = z;
                i5 = i2;
            }
        }

        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            MenuItemImpl menuItemImpl = this.checkedItem;
            if (menuItemImpl != null) {
                bundle.putInt(STATE_CHECKED_ITEM, menuItemImpl.getItemId());
            }
            SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
            int size = this.items.size();
            for (int i = 0; i < size; i++) {
                NavigationMenuItem navigationMenuItem = this.items.get(i);
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    MenuItemImpl menuItem = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem();
                    View actionView = menuItem != null ? menuItem.getActionView() : null;
                    if (actionView != null) {
                        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState(parcelableSparseArray);
                        sparseArray.put(menuItem.getItemId(), parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray(STATE_ACTION_VIEWS, sparseArray);
            return bundle;
        }

        public MenuItemImpl getCheckedItem() {
            return this.checkedItem;
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.items.size();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            NavigationMenuItem navigationMenuItem = this.items.get(i);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (navigationMenuItem instanceof NavigationMenuTextItem) {
                return ((NavigationMenuTextItem) navigationMenuItem).getMenuItem().hasSubMenu() ? 1 : 0;
            }
            throw new RuntimeException("Unknown item type.");
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder viewHolder, int i) throws Resources.NotFoundException {
            int itemViewType = getItemViewType(i);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ((TextView) viewHolder.itemView).setText(((NavigationMenuTextItem) this.items.get(i)).getMenuItem().getTitle());
                    return;
                } else {
                    if (itemViewType != 2) {
                        return;
                    }
                    NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem) this.items.get(i);
                    viewHolder.itemView.setPadding(0, navigationMenuSeparatorItem.getPaddingTop(), 0, navigationMenuSeparatorItem.getPaddingBottom());
                    return;
                }
            }
            NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) viewHolder.itemView;
            navigationMenuItemView.setIconTintList(this.this$0.iconTintList);
            if (this.this$0.textAppearanceSet) {
                navigationMenuItemView.setTextAppearance(this.this$0.textAppearance);
            }
            if (this.this$0.textColor != null) {
                navigationMenuItemView.setTextColor(this.this$0.textColor);
            }
            ViewCompat.setBackground(navigationMenuItemView, this.this$0.itemBackground != null ? this.this$0.itemBackground.getConstantState().newDrawable() : null);
            NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) this.items.get(i);
            navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
            navigationMenuItemView.setHorizontalPadding(this.this$0.itemHorizontalPadding);
            navigationMenuItemView.setIconPadding(this.this$0.itemIconPadding);
            navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 0) {
                return new NormalViewHolder(this.this$0.layoutInflater, viewGroup, this.this$0.onClickListener);
            }
            if (i == 1) {
                return new SubheaderViewHolder(this.this$0.layoutInflater, viewGroup);
            }
            if (i == 2) {
                return new SeparatorViewHolder(this.this$0.layoutInflater, viewGroup);
            }
            if (i != 3) {
                return null;
            }
            return new HeaderViewHolder(this.this$0.headerLayout);
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onViewRecycled(ViewHolder viewHolder) {
            if (viewHolder instanceof NormalViewHolder) {
                ((NavigationMenuItemView) viewHolder.itemView).recycle();
            }
        }

        public void restoreInstanceState(Bundle bundle) {
            MenuItemImpl menuItem;
            View actionView;
            ParcelableSparseArray parcelableSparseArray;
            MenuItemImpl menuItem2;
            int i = bundle.getInt(STATE_CHECKED_ITEM, 0);
            if (i != 0) {
                this.updateSuspended = true;
                int size = this.items.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    NavigationMenuItem navigationMenuItem = this.items.get(i2);
                    if ((navigationMenuItem instanceof NavigationMenuTextItem) && (menuItem2 = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem()) != null && menuItem2.getItemId() == i) {
                        setCheckedItem(menuItem2);
                        break;
                    }
                    i2++;
                }
                this.updateSuspended = false;
                prepareMenuItems();
            }
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(STATE_ACTION_VIEWS);
            if (sparseParcelableArray != null) {
                int size2 = this.items.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    NavigationMenuItem navigationMenuItem2 = this.items.get(i3);
                    if ((navigationMenuItem2 instanceof NavigationMenuTextItem) && (menuItem = ((NavigationMenuTextItem) navigationMenuItem2).getMenuItem()) != null && (actionView = menuItem.getActionView()) != null && (parcelableSparseArray = (ParcelableSparseArray) sparseParcelableArray.get(menuItem.getItemId())) != null) {
                        actionView.restoreHierarchyState(parcelableSparseArray);
                    }
                }
            }
        }

        public void setCheckedItem(MenuItemImpl menuItemImpl) {
            if (this.checkedItem == menuItemImpl || !menuItemImpl.isCheckable()) {
                return;
            }
            MenuItemImpl menuItemImpl2 = this.checkedItem;
            if (menuItemImpl2 != null) {
                menuItemImpl2.setChecked(false);
            }
            this.checkedItem = menuItemImpl;
            menuItemImpl.setChecked(true);
        }

        public void setUpdateSuspended(boolean z) {
            this.updateSuspended = z;
        }

        public void update() {
            prepareMenuItems();
            notifyDataSetChanged();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NavigationMenuHeaderItem.class */
    private static class NavigationMenuHeaderItem implements NavigationMenuItem {
        NavigationMenuHeaderItem() {
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NavigationMenuItem.class */
    private interface NavigationMenuItem {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NavigationMenuSeparatorItem.class */
    private static class NavigationMenuSeparatorItem implements NavigationMenuItem {
        private final int paddingBottom;
        private final int paddingTop;

        public NavigationMenuSeparatorItem(int i, int i2) {
            this.paddingTop = i;
            this.paddingBottom = i2;
        }

        public int getPaddingBottom() {
            return this.paddingBottom;
        }

        public int getPaddingTop() {
            return this.paddingTop;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NavigationMenuTextItem.class */
    private static class NavigationMenuTextItem implements NavigationMenuItem {
        private final MenuItemImpl menuItem;
        boolean needsEmptyIcon;

        NavigationMenuTextItem(MenuItemImpl menuItemImpl) {
            this.menuItem = menuItemImpl;
        }

        public MenuItemImpl getMenuItem() {
            return this.menuItem;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$NormalViewHolder.class */
    private static class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, View.OnClickListener onClickListener) {
            super(layoutInflater.inflate(C0029R.layout.design_navigation_item, viewGroup, false));
            this.itemView.setOnClickListener(onClickListener);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$SeparatorViewHolder.class */
    private static class SeparatorViewHolder extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(C0029R.layout.design_navigation_item_separator, viewGroup, false));
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$SubheaderViewHolder.class */
    private static class SubheaderViewHolder extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(C0029R.layout.design_navigation_item_subheader, viewGroup, false));
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/internal/NavigationMenuPresenter$ViewHolder.class */
    private static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public void addHeaderView(View view) {
        this.headerLayout.addView(view);
        NavigationMenuView navigationMenuView = this.menuView;
        navigationMenuView.setPadding(0, 0, 0, navigationMenuView.getPaddingBottom());
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public void dispatchApplyWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
        if (this.paddingTopDefault != systemWindowInsetTop) {
            this.paddingTopDefault = systemWindowInsetTop;
            if (this.headerLayout.getChildCount() == 0) {
                NavigationMenuView navigationMenuView = this.menuView;
                navigationMenuView.setPadding(0, this.paddingTopDefault, 0, navigationMenuView.getPaddingBottom());
            }
        }
        ViewCompat.dispatchApplyWindowInsets(this.headerLayout, windowInsetsCompat);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    public MenuItemImpl getCheckedItem() {
        return this.adapter.getCheckedItem();
    }

    public int getHeaderCount() {
        return this.headerLayout.getChildCount();
    }

    public View getHeaderView(int i) {
        return this.headerLayout.getChildAt(i);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public int getId() {
        return this.f18id;
    }

    public Drawable getItemBackground() {
        return this.itemBackground;
    }

    public int getItemHorizontalPadding() {
        return this.itemHorizontalPadding;
    }

    public int getItemIconPadding() {
        return this.itemIconPadding;
    }

    public ColorStateList getItemTextColor() {
        return this.textColor;
    }

    public ColorStateList getItemTintList() {
        return this.iconTintList;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.menuView == null) {
            this.menuView = (NavigationMenuView) this.layoutInflater.inflate(C0029R.layout.design_navigation_menu, viewGroup, false);
            if (this.adapter == null) {
                this.adapter = new NavigationMenuAdapter(this);
            }
            this.headerLayout = (LinearLayout) this.layoutInflater.inflate(C0029R.layout.design_navigation_item_header, (ViewGroup) this.menuView, false);
            this.menuView.setAdapter(this.adapter);
        }
        return this.menuView;
    }

    public View inflateHeaderView(int i) {
        View viewInflate = this.layoutInflater.inflate(i, (ViewGroup) this.headerLayout, false);
        addHeaderView(viewInflate);
        return viewInflate;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.layoutInflater = LayoutInflater.from(context);
        this.menu = menuBuilder;
        this.paddingSeparator = context.getResources().getDimensionPixelOffset(C0029R.dimen.design_navigation_separator_vertical_padding);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        MenuPresenter.Callback callback = this.callback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.menuView.restoreHierarchyState(sparseParcelableArray);
            }
            Bundle bundle2 = bundle.getBundle(STATE_ADAPTER);
            if (bundle2 != null) {
                this.adapter.restoreInstanceState(bundle2);
            }
            SparseArray sparseParcelableArray2 = bundle.getSparseParcelableArray(STATE_HEADER);
            if (sparseParcelableArray2 != null) {
                this.headerLayout.restoreHierarchyState(sparseParcelableArray2);
            }
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (this.menuView != null) {
            SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
            this.menuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            bundle.putBundle(STATE_ADAPTER, navigationMenuAdapter.createInstanceState());
        }
        if (this.headerLayout != null) {
            SparseArray<? extends Parcelable> sparseArray2 = new SparseArray<>();
            this.headerLayout.saveHierarchyState(sparseArray2);
            bundle.putSparseParcelableArray(STATE_HEADER, sparseArray2);
        }
        return bundle;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        return false;
    }

    public void removeHeaderView(View view) {
        this.headerLayout.removeView(view);
        if (this.headerLayout.getChildCount() == 0) {
            NavigationMenuView navigationMenuView = this.menuView;
            navigationMenuView.setPadding(0, this.paddingTopDefault, 0, navigationMenuView.getPaddingBottom());
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.Callback callback) {
        this.callback = callback;
    }

    public void setCheckedItem(MenuItemImpl menuItemImpl) {
        this.adapter.setCheckedItem(menuItemImpl);
    }

    public void setId(int i) {
        this.f18id = i;
    }

    public void setItemBackground(Drawable drawable) {
        this.itemBackground = drawable;
        updateMenuView(false);
    }

    public void setItemHorizontalPadding(int i) {
        this.itemHorizontalPadding = i;
        updateMenuView(false);
    }

    public void setItemIconPadding(int i) {
        this.itemIconPadding = i;
        updateMenuView(false);
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.iconTintList = colorStateList;
        updateMenuView(false);
    }

    public void setItemTextAppearance(int i) {
        this.textAppearance = i;
        this.textAppearanceSet = true;
        updateMenuView(false);
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.textColor = colorStateList;
        updateMenuView(false);
    }

    public void setUpdateSuspended(boolean z) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.setUpdateSuspended(z);
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.update();
        }
    }
}
