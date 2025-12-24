package android.support.v7.widget;

import android.content.Context;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/PopupMenu.class */
public class PopupMenu {
    private final View mAnchor;
    private final Context mContext;
    private View.OnTouchListener mDragListener;
    private final MenuBuilder mMenu;
    OnMenuItemClickListener mMenuItemClickListener;
    OnDismissListener mOnDismissListener;
    final MenuPopupHelper mPopup;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/PopupMenu$OnDismissListener.class */
    public interface OnDismissListener {
        void onDismiss(PopupMenu popupMenu);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/PopupMenu$OnMenuItemClickListener.class */
    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public PopupMenu(Context context, View view) {
        this(context, view, 0);
    }

    public PopupMenu(Context context, View view, int i) {
        this(context, view, i, C0287R.attr.popupMenuStyle, 0);
    }

    public PopupMenu(Context context, View view, int i, int i2, int i3) {
        this.mContext = context;
        this.mAnchor = view;
        MenuBuilder menuBuilder = new MenuBuilder(context);
        this.mMenu = menuBuilder;
        menuBuilder.setCallback(new MenuBuilder.Callback(this) { // from class: android.support.v7.widget.PopupMenu.1
            final PopupMenu this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public boolean onMenuItemSelected(MenuBuilder menuBuilder2, MenuItem menuItem) {
                if (this.this$0.mMenuItemClickListener != null) {
                    return this.this$0.mMenuItemClickListener.onMenuItemClick(menuItem);
                }
                return false;
            }

            @Override // android.support.v7.view.menu.MenuBuilder.Callback
            public void onMenuModeChange(MenuBuilder menuBuilder2) {
            }
        });
        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(context, this.mMenu, view, false, i2, i3);
        this.mPopup = menuPopupHelper;
        menuPopupHelper.setGravity(i);
        this.mPopup.setOnDismissListener(new PopupWindow.OnDismissListener(this) { // from class: android.support.v7.widget.PopupMenu.2
            final PopupMenu this$0;

            {
                this.this$0 = this;
            }

            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (this.this$0.mOnDismissListener != null) {
                    this.this$0.mOnDismissListener.onDismiss(this.this$0);
                }
            }
        });
    }

    public void dismiss() {
        this.mPopup.dismiss();
    }

    public View.OnTouchListener getDragToOpenListener() {
        if (this.mDragListener == null) {
            this.mDragListener = new ForwardingListener(this, this.mAnchor) { // from class: android.support.v7.widget.PopupMenu.3
                final PopupMenu this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.support.v7.widget.ForwardingListener
                public ShowableListMenu getPopup() {
                    return this.this$0.mPopup.getPopup();
                }

                @Override // android.support.v7.widget.ForwardingListener
                protected boolean onForwardingStarted() {
                    this.this$0.show();
                    return true;
                }

                @Override // android.support.v7.widget.ForwardingListener
                protected boolean onForwardingStopped() {
                    this.this$0.dismiss();
                    return true;
                }
            };
        }
        return this.mDragListener;
    }

    public int getGravity() {
        return this.mPopup.getGravity();
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContext);
    }

    ListView getMenuListView() {
        if (this.mPopup.isShowing()) {
            return this.mPopup.getListView();
        }
        return null;
    }

    public void inflate(int i) {
        getMenuInflater().inflate(i, this.mMenu);
    }

    public void setGravity(int i) {
        this.mPopup.setGravity(i);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuItemClickListener = onMenuItemClickListener;
    }

    public void show() {
        this.mPopup.show();
    }
}
