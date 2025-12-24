package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/MenuItemWrapperJB.class */
class MenuItemWrapperJB extends MenuItemWrapperICS {

    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/MenuItemWrapperJB$ActionProviderWrapperJB.class */
    class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper implements ActionProvider.VisibilityListener {
        ActionProvider.VisibilityListener mListener;
        final MenuItemWrapperJB this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ActionProviderWrapperJB(MenuItemWrapperJB menuItemWrapperJB, Context context, android.view.ActionProvider actionProvider) {
            super(menuItemWrapperJB, context, actionProvider);
            this.this$0 = menuItemWrapperJB;
        }

        @Override // android.support.v4.view.ActionProvider
        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        @Override // android.view.ActionProvider.VisibilityListener
        public void onActionProviderVisibilityChanged(boolean z) {
            ActionProvider.VisibilityListener visibilityListener = this.mListener;
            if (visibilityListener != null) {
                visibilityListener.onActionProviderVisibilityChanged(z);
            }
        }

        @Override // android.support.v4.view.ActionProvider
        public View onCreateActionView(MenuItem menuItem) {
            return this.mInner.onCreateActionView(menuItem);
        }

        @Override // android.support.v4.view.ActionProvider
        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        @Override // android.support.v4.view.ActionProvider
        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        @Override // android.support.v4.view.ActionProvider
        public void setVisibilityListener(ActionProvider.VisibilityListener visibilityListener) {
            this.mListener = visibilityListener;
            this.mInner.setVisibilityListener(visibilityListener != null ? this : null);
        }
    }

    MenuItemWrapperJB(Context context, SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }

    @Override // android.support.v7.view.menu.MenuItemWrapperICS
    MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider actionProvider) {
        return new ActionProviderWrapperJB(this, this.mContext, actionProvider);
    }
}
