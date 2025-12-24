package android.support.v7.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ListMenuPresenter.class */
public class ListMenuPresenter implements MenuPresenter, AdapterView.OnItemClickListener {
    private static final String TAG = "ListMenuPresenter";
    public static final String VIEWS_TAG = "android:menu:list";
    MenuAdapter mAdapter;
    private MenuPresenter.Callback mCallback;
    Context mContext;
    private int mId;
    LayoutInflater mInflater;
    int mItemIndexOffset;
    int mItemLayoutRes;
    MenuBuilder mMenu;
    ExpandedMenuView mMenuView;
    int mThemeRes;

    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ListMenuPresenter$MenuAdapter.class */
    private class MenuAdapter extends BaseAdapter {
        private int mExpandedIndex = -1;
        final ListMenuPresenter this$0;

        public MenuAdapter(ListMenuPresenter listMenuPresenter) {
            this.this$0 = listMenuPresenter;
            findExpandedIndex();
        }

        void findExpandedIndex() {
            MenuItemImpl expandedItem = this.this$0.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList<MenuItemImpl> nonActionItems = this.this$0.mMenu.getNonActionItems();
                int size = nonActionItems.size();
                for (int i = 0; i < size; i++) {
                    if (nonActionItems.get(i) == expandedItem) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int size = this.this$0.mMenu.getNonActionItems().size() - this.this$0.mItemIndexOffset;
            return this.mExpandedIndex < 0 ? size : size - 1;
        }

        @Override // android.widget.Adapter
        public MenuItemImpl getItem(int i) {
            ArrayList<MenuItemImpl> nonActionItems = this.this$0.mMenu.getNonActionItems();
            int i2 = i + this.this$0.mItemIndexOffset;
            int i3 = this.mExpandedIndex;
            int i4 = i2;
            if (i3 >= 0) {
                i4 = i2;
                if (i2 >= i3) {
                    i4 = i2 + 1;
                }
            }
            return nonActionItems.get(i4);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInflate = view;
            if (view == null) {
                viewInflate = this.this$0.mInflater.inflate(this.this$0.mItemLayoutRes, viewGroup, false);
            }
            ((MenuView.ItemView) viewInflate).initialize(getItem(i), 0);
            return viewInflate;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }

    public ListMenuPresenter(int i, int i2) {
        this.mItemLayoutRes = i;
        this.mThemeRes = i2;
    }

    public ListMenuPresenter(Context context, int i) {
        this(i, 0);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    public ListAdapter getAdapter() {
        if (this.mAdapter == null) {
            this.mAdapter = new MenuAdapter(this);
        }
        return this.mAdapter;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public int getId() {
        return this.mId;
    }

    int getItemIndexOffset() {
        return this.mItemIndexOffset;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (ExpandedMenuView) this.mInflater.inflate(C0287R.layout.abc_expanded_menu_layout, viewGroup, false);
            if (this.mAdapter == null) {
                this.mAdapter = new MenuAdapter(this);
            }
            this.mMenuView.setAdapter((ListAdapter) this.mAdapter);
            this.mMenuView.setOnItemClickListener(this);
        }
        return this.mMenuView;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        if (this.mThemeRes != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.mThemeRes);
            this.mContext = contextThemeWrapper;
            this.mInflater = LayoutInflater.from(contextThemeWrapper);
        } else if (this.mContext != null) {
            this.mContext = context;
            if (this.mInflater == null) {
                this.mInflater = LayoutInflater.from(context);
            }
        }
        this.mMenu = menuBuilder;
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.mMenu.performItemAction(this.mAdapter.getItem(i), this, 0);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
        restoreHierarchyState((Bundle) parcelable);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        if (this.mMenuView == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        saveHierarchyState(bundle);
        return bundle;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        new MenuDialogHelper(subMenuBuilder).show(null);
        MenuPresenter.Callback callback = this.mCallback;
        if (callback == null) {
            return true;
        }
        callback.onOpenSubMenu(subMenuBuilder);
        return true;
    }

    public void restoreHierarchyState(Bundle bundle) {
        SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray(VIEWS_TAG);
        if (sparseParcelableArray != null) {
            this.mMenuView.restoreHierarchyState(sparseParcelableArray);
        }
    }

    public void saveHierarchyState(Bundle bundle) {
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        ExpandedMenuView expandedMenuView = this.mMenuView;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray(VIEWS_TAG, sparseArray);
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    public void setId(int i) {
        this.mId = i;
    }

    public void setItemIndexOffset(int i) {
        this.mItemIndexOffset = i;
        if (this.mMenuView != null) {
            updateMenuView(false);
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }
}
