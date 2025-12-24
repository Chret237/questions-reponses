package android.support.v7.view.menu;

import android.widget.ListView;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ShowableListMenu.class */
public interface ShowableListMenu {
    void dismiss();

    ListView getListView();

    boolean isShowing();

    void show();
}
