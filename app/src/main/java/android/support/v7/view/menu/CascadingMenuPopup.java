package android.support.v7.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.MenuPopupWindow;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/CascadingMenuPopup.class */
final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, View.OnKeyListener, PopupWindow.OnDismissListener {
    static final int HORIZ_POSITION_LEFT = 0;
    static final int HORIZ_POSITION_RIGHT = 1;
    private static final int ITEM_LAYOUT = C0287R.layout.abc_cascading_menu_item_layout;
    static final int SUBMENU_TIMEOUT_MS = 200;
    private View mAnchorView;
    private final Context mContext;
    private boolean mHasXOffset;
    private boolean mHasYOffset;
    private final int mMenuMaxWidth;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    boolean mShouldCloseImmediately;
    private boolean mShowTitle;
    View mShownAnchorView;
    final Handler mSubMenuHoverHandler;
    ViewTreeObserver mTreeObserver;
    private int mXOffset;
    private int mYOffset;
    private final List<MenuBuilder> mPendingMenus = new ArrayList();
    final List<CascadingMenuInfo> mShowingMenus = new ArrayList();
    final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(this) { // from class: android.support.v7.view.menu.CascadingMenuPopup.1
        final CascadingMenuPopup this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!this.this$0.isShowing() || this.this$0.mShowingMenus.size() <= 0 || this.this$0.mShowingMenus.get(0).window.isModal()) {
                return;
            }
            View view = this.this$0.mShownAnchorView;
            if (view == null || !view.isShown()) {
                this.this$0.dismiss();
                return;
            }
            Iterator<CascadingMenuInfo> it = this.this$0.mShowingMenus.iterator();
            while (it.hasNext()) {
                it.next().window.show();
            }
        }
    };
    private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener(this) { // from class: android.support.v7.view.menu.CascadingMenuPopup.2
        final CascadingMenuPopup this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            if (this.this$0.mTreeObserver != null) {
                if (!this.this$0.mTreeObserver.isAlive()) {
                    this.this$0.mTreeObserver = view.getViewTreeObserver();
                }
                this.this$0.mTreeObserver.removeGlobalOnLayoutListener(this.this$0.mGlobalLayoutListener);
            }
            view.removeOnAttachStateChangeListener(this);
        }
    };
    private final MenuItemHoverListener mMenuItemHoverListener = new C03013(this);
    private int mRawDropDownGravity = 0;
    private int mDropDownGravity = 0;
    private boolean mForceShowIcon = false;
    private int mLastPosition = getInitialMenuPosition();

    /* renamed from: android.support.v7.view.menu.CascadingMenuPopup$3 */
    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/CascadingMenuPopup$3.class */
    class C03013 implements MenuItemHoverListener {
        final CascadingMenuPopup this$0;

        C03013(CascadingMenuPopup cascadingMenuPopup) {
            this.this$0 = cascadingMenuPopup;
        }

        @Override // android.support.v7.widget.MenuItemHoverListener
        public void onItemHoverEnter(MenuBuilder menuBuilder, MenuItem menuItem) {
            CascadingMenuInfo cascadingMenuInfo = null;
            this.this$0.mSubMenuHoverHandler.removeCallbacksAndMessages(null);
            int size = this.this$0.mShowingMenus.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    i = -1;
                    break;
                } else if (menuBuilder == this.this$0.mShowingMenus.get(i).menu) {
                    break;
                } else {
                    i++;
                }
            }
            if (i == -1) {
                return;
            }
            int i2 = i + 1;
            if (i2 < this.this$0.mShowingMenus.size()) {
                cascadingMenuInfo = this.this$0.mShowingMenus.get(i2);
            }
            this.this$0.mSubMenuHoverHandler.postAtTime(new Runnable(this, cascadingMenuInfo, menuItem, menuBuilder) { // from class: android.support.v7.view.menu.CascadingMenuPopup.3.1
                final C03013 this$1;
                final MenuItem val$item;
                final MenuBuilder val$menu;
                final CascadingMenuInfo val$nextInfo;

                {
                    this.this$1 = this;
                    this.val$nextInfo = cascadingMenuInfo;
                    this.val$item = menuItem;
                    this.val$menu = menuBuilder;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (this.val$nextInfo != null) {
                        this.this$1.this$0.mShouldCloseImmediately = true;
                        this.val$nextInfo.menu.close(false);
                        this.this$1.this$0.mShouldCloseImmediately = false;
                    }
                    if (this.val$item.isEnabled() && this.val$item.hasSubMenu()) {
                        this.val$menu.performItemAction(this.val$item, 4);
                    }
                }
            }, menuBuilder, SystemClock.uptimeMillis() + 200);
        }

        @Override // android.support.v7.widget.MenuItemHoverListener
        public void onItemHoverExit(MenuBuilder menuBuilder, MenuItem menuItem) {
            this.this$0.mSubMenuHoverHandler.removeCallbacksAndMessages(menuBuilder);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/CascadingMenuPopup$CascadingMenuInfo.class */
    private static class CascadingMenuInfo {
        public final MenuBuilder menu;
        public final int position;
        public final MenuPopupWindow window;

        public CascadingMenuInfo(MenuPopupWindow menuPopupWindow, MenuBuilder menuBuilder, int i) {
            this.window = menuPopupWindow;
            this.menu = menuBuilder;
            this.position = i;
        }

        public ListView getListView() {
            return this.window.getListView();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/CascadingMenuPopup$HorizPosition.class */
    public @interface HorizPosition {
    }

    public CascadingMenuPopup(Context context, View view, int i, int i2, boolean z) {
        this.mContext = context;
        this.mAnchorView = view;
        this.mPopupStyleAttr = i;
        this.mPopupStyleRes = i2;
        this.mOverflowOnly = z;
        Resources resources = context.getResources();
        this.mMenuMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(C0287R.dimen.abc_config_prefDialogWidth));
        this.mSubMenuHoverHandler = new Handler();
    }

    private MenuPopupWindow createPopupWindow() {
        MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuPopupWindow.setHoverListener(this.mMenuItemHoverListener);
        menuPopupWindow.setOnItemClickListener(this);
        menuPopupWindow.setOnDismissListener(this);
        menuPopupWindow.setAnchorView(this.mAnchorView);
        menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
        menuPopupWindow.setModal(true);
        menuPopupWindow.setInputMethodMode(2);
        return menuPopupWindow;
    }

    private int findIndexOfAddedMenu(MenuBuilder menuBuilder) {
        int size = this.mShowingMenus.size();
        for (int i = 0; i < size; i++) {
            if (menuBuilder == this.mShowingMenus.get(i).menu) {
                return i;
            }
        }
        return -1;
    }

    private MenuItem findMenuItemForSubmenu(MenuBuilder menuBuilder, MenuBuilder menuBuilder2) {
        int size = menuBuilder.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menuBuilder.getItem(i);
            if (item.hasSubMenu() && menuBuilder2 == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    private View findParentViewForSubmenu(CascadingMenuInfo cascadingMenuInfo, MenuBuilder menuBuilder) {
        MenuAdapter menuAdapter;
        int headersCount;
        int firstVisiblePosition;
        MenuItem menuItemFindMenuItemForSubmenu = findMenuItemForSubmenu(cascadingMenuInfo.menu, menuBuilder);
        if (menuItemFindMenuItemForSubmenu == null) {
            return null;
        }
        ListView listView = cascadingMenuInfo.getListView();
        ListAdapter adapter = listView.getAdapter();
        int i = 0;
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            headersCount = headerViewListAdapter.getHeadersCount();
            menuAdapter = (MenuAdapter) headerViewListAdapter.getWrappedAdapter();
        } else {
            menuAdapter = (MenuAdapter) adapter;
            headersCount = 0;
        }
        int count = menuAdapter.getCount();
        while (true) {
            if (i >= count) {
                i = -1;
                break;
            }
            if (menuItemFindMenuItemForSubmenu == menuAdapter.getItem(i)) {
                break;
            }
            i++;
        }
        if (i != -1 && (firstVisiblePosition = (i + headersCount) - listView.getFirstVisiblePosition()) >= 0 && firstVisiblePosition < listView.getChildCount()) {
            return listView.getChildAt(firstVisiblePosition);
        }
        return null;
    }

    private int getInitialMenuPosition() {
        int i = 1;
        if (ViewCompat.getLayoutDirection(this.mAnchorView) == 1) {
            i = 0;
        }
        return i;
    }

    private int getNextMenuPosition(int i) {
        List<CascadingMenuInfo> list = this.mShowingMenus;
        ListView listView = list.get(list.size() - 1).getListView();
        int[] iArr = new int[2];
        listView.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        this.mShownAnchorView.getWindowVisibleDisplayFrame(rect);
        return this.mLastPosition == 1 ? (iArr[0] + listView.getWidth()) + i > rect.right ? 0 : 1 : iArr[0] - i < 0 ? 1 : 0;
    }

    private void showMenu(MenuBuilder menuBuilder) {
        CascadingMenuInfo cascadingMenuInfo;
        View viewFindParentViewForSubmenu;
        int i;
        int i2;
        int i3;
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        MenuAdapter menuAdapter = new MenuAdapter(menuBuilder, layoutInflaterFrom, this.mOverflowOnly, ITEM_LAYOUT);
        if (!isShowing() && this.mForceShowIcon) {
            menuAdapter.setForceShowIcon(true);
        } else if (isShowing()) {
            menuAdapter.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(menuBuilder));
        }
        int iMeasureIndividualMenuWidth = measureIndividualMenuWidth(menuAdapter, null, this.mContext, this.mMenuMaxWidth);
        MenuPopupWindow menuPopupWindowCreatePopupWindow = createPopupWindow();
        menuPopupWindowCreatePopupWindow.setAdapter(menuAdapter);
        menuPopupWindowCreatePopupWindow.setContentWidth(iMeasureIndividualMenuWidth);
        menuPopupWindowCreatePopupWindow.setDropDownGravity(this.mDropDownGravity);
        if (this.mShowingMenus.size() > 0) {
            List<CascadingMenuInfo> list = this.mShowingMenus;
            cascadingMenuInfo = list.get(list.size() - 1);
            viewFindParentViewForSubmenu = findParentViewForSubmenu(cascadingMenuInfo, menuBuilder);
        } else {
            cascadingMenuInfo = null;
            viewFindParentViewForSubmenu = null;
        }
        if (viewFindParentViewForSubmenu != null) {
            menuPopupWindowCreatePopupWindow.setTouchModal(false);
            menuPopupWindowCreatePopupWindow.setEnterTransition(null);
            int nextMenuPosition = getNextMenuPosition(iMeasureIndividualMenuWidth);
            boolean z = nextMenuPosition == 1;
            this.mLastPosition = nextMenuPosition;
            if (Build.VERSION.SDK_INT >= 26) {
                menuPopupWindowCreatePopupWindow.setAnchorView(viewFindParentViewForSubmenu);
                i2 = 0;
                i = 0;
            } else {
                int[] iArr = new int[2];
                this.mAnchorView.getLocationOnScreen(iArr);
                int[] iArr2 = new int[2];
                viewFindParentViewForSubmenu.getLocationOnScreen(iArr2);
                if ((this.mDropDownGravity & 7) == 5) {
                    iArr[0] = iArr[0] + this.mAnchorView.getWidth();
                    iArr2[0] = iArr2[0] + viewFindParentViewForSubmenu.getWidth();
                }
                i = iArr2[0] - iArr[0];
                i2 = iArr2[1] - iArr[1];
            }
            if ((this.mDropDownGravity & 5) == 5) {
                if (!z) {
                    iMeasureIndividualMenuWidth = viewFindParentViewForSubmenu.getWidth();
                    i3 = i - iMeasureIndividualMenuWidth;
                }
                i3 = i + iMeasureIndividualMenuWidth;
            } else {
                if (z) {
                    iMeasureIndividualMenuWidth = viewFindParentViewForSubmenu.getWidth();
                    i3 = i + iMeasureIndividualMenuWidth;
                }
                i3 = i - iMeasureIndividualMenuWidth;
            }
            menuPopupWindowCreatePopupWindow.setHorizontalOffset(i3);
            menuPopupWindowCreatePopupWindow.setOverlapAnchor(true);
            menuPopupWindowCreatePopupWindow.setVerticalOffset(i2);
        } else {
            if (this.mHasXOffset) {
                menuPopupWindowCreatePopupWindow.setHorizontalOffset(this.mXOffset);
            }
            if (this.mHasYOffset) {
                menuPopupWindowCreatePopupWindow.setVerticalOffset(this.mYOffset);
            }
            menuPopupWindowCreatePopupWindow.setEpicenterBounds(getEpicenterBounds());
        }
        this.mShowingMenus.add(new CascadingMenuInfo(menuPopupWindowCreatePopupWindow, menuBuilder, this.mLastPosition));
        menuPopupWindowCreatePopupWindow.show();
        ListView listView = menuPopupWindowCreatePopupWindow.getListView();
        listView.setOnKeyListener(this);
        if (cascadingMenuInfo == null && this.mShowTitle && menuBuilder.getHeaderTitle() != null) {
            FrameLayout frameLayout = (FrameLayout) layoutInflaterFrom.inflate(C0287R.layout.abc_popup_menu_header_item_layout, (ViewGroup) listView, false);
            TextView textView = (TextView) frameLayout.findViewById(R.id.title);
            frameLayout.setEnabled(false);
            textView.setText(menuBuilder.getHeaderTitle());
            listView.addHeaderView(frameLayout, null, false);
            menuPopupWindowCreatePopupWindow.show();
        }
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void addMenu(MenuBuilder menuBuilder) {
        menuBuilder.addMenuPresenter(this, this.mContext);
        if (isShowing()) {
            showMenu(menuBuilder);
        } else {
            this.mPendingMenus.add(menuBuilder);
        }
    }

    @Override // android.support.v7.view.menu.MenuPopup
    protected boolean closeMenuOnSubMenuOpened() {
        return false;
    }

    @Override // android.support.v7.view.menu.ShowableListMenu
    public void dismiss() {
        int size = this.mShowingMenus.size();
        if (size <= 0) {
            return;
        }
        CascadingMenuInfo[] cascadingMenuInfoArr = (CascadingMenuInfo[]) this.mShowingMenus.toArray(new CascadingMenuInfo[size]);
        while (true) {
            size--;
            if (size < 0) {
                return;
            }
            CascadingMenuInfo cascadingMenuInfo = cascadingMenuInfoArr[size];
            if (cascadingMenuInfo.window.isShowing()) {
                cascadingMenuInfo.window.dismiss();
            }
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // android.support.v7.view.menu.ShowableListMenu
    public ListView getListView() {
        ListView listView;
        if (this.mShowingMenus.isEmpty()) {
            listView = null;
        } else {
            List<CascadingMenuInfo> list = this.mShowingMenus;
            listView = list.get(list.size() - 1).getListView();
        }
        return listView;
    }

    @Override // android.support.v7.view.menu.ShowableListMenu
    public boolean isShowing() {
        boolean z = false;
        if (this.mShowingMenus.size() > 0) {
            z = false;
            if (this.mShowingMenus.get(0).window.isShowing()) {
                z = true;
            }
        }
        return z;
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        int iFindIndexOfAddedMenu = findIndexOfAddedMenu(menuBuilder);
        if (iFindIndexOfAddedMenu < 0) {
            return;
        }
        int i = iFindIndexOfAddedMenu + 1;
        if (i < this.mShowingMenus.size()) {
            this.mShowingMenus.get(i).menu.close(false);
        }
        CascadingMenuInfo cascadingMenuInfoRemove = this.mShowingMenus.remove(iFindIndexOfAddedMenu);
        cascadingMenuInfoRemove.menu.removeMenuPresenter(this);
        if (this.mShouldCloseImmediately) {
            cascadingMenuInfoRemove.window.setExitTransition(null);
            cascadingMenuInfoRemove.window.setAnimationStyle(0);
        }
        cascadingMenuInfoRemove.window.dismiss();
        int size = this.mShowingMenus.size();
        if (size > 0) {
            this.mLastPosition = this.mShowingMenus.get(size - 1).position;
        } else {
            this.mLastPosition = getInitialMenuPosition();
        }
        if (size != 0) {
            if (z) {
                this.mShowingMenus.get(0).menu.close(false);
                return;
            }
            return;
        }
        dismiss();
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, true);
        }
        ViewTreeObserver viewTreeObserver = this.mTreeObserver;
        if (viewTreeObserver != null) {
            if (viewTreeObserver.isAlive()) {
                this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
            }
            this.mTreeObserver = null;
        }
        this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
        this.mOnDismissListener.onDismiss();
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        CascadingMenuInfo cascadingMenuInfo;
        int size = this.mShowingMenus.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                cascadingMenuInfo = null;
                break;
            }
            cascadingMenuInfo = this.mShowingMenus.get(i);
            if (!cascadingMenuInfo.window.isShowing()) {
                break;
            } else {
                i++;
            }
        }
        if (cascadingMenuInfo != null) {
            cascadingMenuInfo.menu.close(false);
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1 || i != 82) {
            return false;
        }
        dismiss();
        return true;
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
        for (CascadingMenuInfo cascadingMenuInfo : this.mShowingMenus) {
            if (subMenuBuilder == cascadingMenuInfo.menu) {
                cascadingMenuInfo.getListView().requestFocus();
                return true;
            }
        }
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        addMenu(subMenuBuilder);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback == null) {
            return true;
        }
        callback.onOpenSubMenu(subMenuBuilder);
        return true;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setAnchorView(View view) {
        if (this.mAnchorView != view) {
            this.mAnchorView = view;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(view));
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setGravity(int i) {
        if (this.mRawDropDownGravity != i) {
            this.mRawDropDownGravity = i;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection(this.mAnchorView));
        }
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setHorizontalOffset(int i) {
        this.mHasXOffset = true;
        this.mXOffset = i;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setShowTitle(boolean z) {
        this.mShowTitle = z;
    }

    @Override // android.support.v7.view.menu.MenuPopup
    public void setVerticalOffset(int i) {
        this.mHasYOffset = true;
        this.mYOffset = i;
    }

    @Override // android.support.v7.view.menu.ShowableListMenu
    public void show() {
        if (isShowing()) {
            return;
        }
        Iterator<MenuBuilder> it = this.mPendingMenus.iterator();
        while (it.hasNext()) {
            showMenu(it.next());
        }
        this.mPendingMenus.clear();
        View view = this.mAnchorView;
        this.mShownAnchorView = view;
        if (view != null) {
            boolean z = this.mTreeObserver == null;
            ViewTreeObserver viewTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            this.mTreeObserver = viewTreeObserver;
            if (z) {
                viewTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
            this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
        }
    }

    @Override // android.support.v7.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        Iterator<CascadingMenuInfo> it = this.mShowingMenus.iterator();
        while (it.hasNext()) {
            toMenuAdapter(it.next().getListView().getAdapter()).notifyDataSetChanged();
        }
    }
}
