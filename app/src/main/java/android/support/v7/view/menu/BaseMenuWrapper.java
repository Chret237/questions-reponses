package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v4.util.ArrayMap;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/BaseMenuWrapper.class */
abstract class BaseMenuWrapper<T> extends BaseWrapper<T> {
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(Context context, T t) {
        super(t);
        this.mContext = context;
    }

    final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        MenuItem menuItemWrapSupportMenuItem = menuItem;
        if (menuItem instanceof SupportMenuItem) {
            SupportMenuItem supportMenuItem = (SupportMenuItem) menuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayMap();
            }
            MenuItem menuItem2 = this.mMenuItems.get(menuItem);
            menuItemWrapSupportMenuItem = menuItem2;
            if (menuItem2 == null) {
                menuItemWrapSupportMenuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, menuItemWrapSupportMenuItem);
            }
        }
        return menuItemWrapSupportMenuItem;
    }

    final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (!(subMenu instanceof SupportSubMenu)) {
            return subMenu;
        }
        SupportSubMenu supportSubMenu = (SupportSubMenu) subMenu;
        if (this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap();
        }
        SubMenu subMenu2 = this.mSubMenus.get(supportSubMenu);
        SubMenu subMenuWrapSupportSubMenu = subMenu2;
        if (subMenu2 == null) {
            subMenuWrapSupportSubMenu = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
            this.mSubMenus.put(supportSubMenu, subMenuWrapSupportSubMenu);
        }
        return subMenuWrapSupportSubMenu;
    }

    final void internalClear() {
        Map<SupportMenuItem, MenuItem> map = this.mMenuItems;
        if (map != null) {
            map.clear();
        }
        Map<SupportSubMenu, SubMenu> map2 = this.mSubMenus;
        if (map2 != null) {
            map2.clear();
        }
    }

    final void internalRemoveGroup(int i) {
        Map<SupportMenuItem, MenuItem> map = this.mMenuItems;
        if (map == null) {
            return;
        }
        Iterator<SupportMenuItem> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (i == it.next().getGroupId()) {
                it.remove();
            }
        }
    }

    final void internalRemoveItem(int i) {
        Map<SupportMenuItem, MenuItem> map = this.mMenuItems;
        if (map == null) {
            return;
        }
        Iterator<SupportMenuItem> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (i == it.next().getItemId()) {
                it.remove();
                return;
            }
        }
    }
}
