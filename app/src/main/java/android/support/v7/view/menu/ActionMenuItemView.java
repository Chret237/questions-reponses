package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ForwardingListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ActionMenuItemView.class */
public class ActionMenuItemView extends AppCompatTextView implements MenuView.ItemView, View.OnClickListener, ActionMenuView.ActionMenuChildView {
    private static final int MAX_ICON_SIZE = 32;
    private static final String TAG = "ActionMenuItemView";
    private boolean mAllowTextWithIcon;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Drawable mIcon;
    MenuItemImpl mItemData;
    MenuBuilder.ItemInvoker mItemInvoker;
    private int mMaxIconSize;
    private int mMinWidth;
    PopupCallback mPopupCallback;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;

    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ActionMenuItemView$ActionMenuItemForwardingListener.class */
    private class ActionMenuItemForwardingListener extends ForwardingListener {
        final ActionMenuItemView this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ActionMenuItemForwardingListener(ActionMenuItemView actionMenuItemView) {
            super(actionMenuItemView);
            this.this$0 = actionMenuItemView;
        }

        @Override // android.support.v7.widget.ForwardingListener
        public ShowableListMenu getPopup() {
            if (this.this$0.mPopupCallback != null) {
                return this.this$0.mPopupCallback.getPopup();
            }
            return null;
        }

        @Override // android.support.v7.widget.ForwardingListener
        protected boolean onForwardingStarted() {
            boolean z = false;
            if (this.this$0.mItemInvoker != null) {
                z = false;
                if (this.this$0.mItemInvoker.invokeItem(this.this$0.mItemData)) {
                    ShowableListMenu popup = getPopup();
                    z = false;
                    if (popup != null) {
                        z = false;
                        if (popup.isShowing()) {
                            z = true;
                        }
                    }
                }
            }
            return z;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/view/menu/ActionMenuItemView$PopupCallback.class */
    public static abstract class PopupCallback {
        public abstract ShowableListMenu getPopup();
    }

    public ActionMenuItemView(Context context) {
        this(context, null);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Resources resources = context.getResources();
        this.mAllowTextWithIcon = shouldAllowTextWithIcon();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0287R.styleable.ActionMenuItemView, i, 0);
        this.mMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.ActionMenuItemView_android_minWidth, 0);
        typedArrayObtainStyledAttributes.recycle();
        this.mMaxIconSize = (int) ((resources.getDisplayMetrics().density * 32.0f) + 0.5f);
        setOnClickListener(this);
        this.mSavedPaddingLeft = -1;
        setSaveEnabled(false);
    }

    private boolean shouldAllowTextWithIcon() {
        Configuration configuration = getContext().getResources().getConfiguration();
        int i = configuration.screenWidthDp;
        return i >= 480 || (i >= 640 && configuration.screenHeightDp >= 480) || configuration.orientation == 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateTextButtonVisibility() {
        /*
            r3 = this;
            r0 = r3
            java.lang.CharSequence r0 = r0.mTitle
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r6 = r0
            r0 = 1
            r5 = r0
            r0 = r5
            r4 = r0
            r0 = r3
            android.graphics.drawable.Drawable r0 = r0.mIcon
            if (r0 == 0) goto L34
            r0 = r3
            android.support.v7.view.menu.MenuItemImpl r0 = r0.mItemData
            boolean r0 = r0.showsTextAsAction()
            if (r0 == 0) goto L32
            r0 = r5
            r4 = r0
            r0 = r3
            boolean r0 = r0.mAllowTextWithIcon
            if (r0 != 0) goto L34
            r0 = r3
            boolean r0 = r0.mExpandedFormat
            if (r0 == 0) goto L32
            r0 = r5
            r4 = r0
            goto L34
        L32:
            r0 = 0
            r4 = r0
        L34:
            r0 = r6
            r1 = 1
            r0 = r0 ^ r1
            r1 = r4
            r0 = r0 & r1
            r4 = r0
            r0 = 0
            r8 = r0
            r0 = r4
            if (r0 == 0) goto L4a
            r0 = r3
            java.lang.CharSequence r0 = r0.mTitle
            r7 = r0
            goto L4d
        L4a:
            r0 = 0
            r7 = r0
        L4d:
            r0 = r3
            r1 = r7
            r0.setText(r1)
            r0 = r3
            android.support.v7.view.menu.MenuItemImpl r0 = r0.mItemData
            java.lang.CharSequence r0 = r0.getContentDescription()
            r7 = r0
            r0 = r7
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L80
            r0 = r4
            if (r0 == 0) goto L6e
            r0 = 0
            r7 = r0
            goto L77
        L6e:
            r0 = r3
            android.support.v7.view.menu.MenuItemImpl r0 = r0.mItemData
            java.lang.CharSequence r0 = r0.getTitle()
            r7 = r0
        L77:
            r0 = r3
            r1 = r7
            r0.setContentDescription(r1)
            goto L86
        L80:
            r0 = r3
            r1 = r7
            r0.setContentDescription(r1)
        L86:
            r0 = r3
            android.support.v7.view.menu.MenuItemImpl r0 = r0.mItemData
            java.lang.CharSequence r0 = r0.getTooltipText()
            r7 = r0
            r0 = r7
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto Lb4
            r0 = r4
            if (r0 == 0) goto La2
            r0 = r8
            r7 = r0
            goto Lab
        La2:
            r0 = r3
            android.support.v7.view.menu.MenuItemImpl r0 = r0.mItemData
            java.lang.CharSequence r0 = r0.getTitle()
            r7 = r0
        Lab:
            r0 = r3
            r1 = r7
            android.support.v7.widget.TooltipCompat.setTooltipText(r0, r1)
            goto Lba
        Lb4:
            r0 = r3
            r1 = r7
            android.support.v7.widget.TooltipCompat.setTooltipText(r0, r1)
        Lba:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.view.menu.ActionMenuItemView.updateTextButtonVisibility():void");
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(getText());
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void initialize(MenuItemImpl menuItemImpl, int i) {
        this.mItemData = menuItemImpl;
        setIcon(menuItemImpl.getIcon());
        setTitle(menuItemImpl.getTitleForItemView(this));
        setId(menuItemImpl.getItemId());
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        setEnabled(menuItemImpl.isEnabled());
        if (menuItemImpl.hasSubMenu() && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener(this);
        }
    }

    @Override // android.support.v7.widget.ActionMenuView.ActionMenuChildView
    public boolean needsDividerAfter() {
        return hasText();
    }

    @Override // android.support.v7.widget.ActionMenuView.ActionMenuChildView
    public boolean needsDividerBefore() {
        return hasText() && this.mItemData.getIcon() == null;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        MenuBuilder.ItemInvoker itemInvoker = this.mItemInvoker;
        if (itemInvoker != null) {
            itemInvoker.invokeItem(this.mItemData);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAllowTextWithIcon = shouldAllowTextWithIcon();
        updateTextButtonVisibility();
    }

    @Override // android.support.v7.widget.AppCompatTextView, android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        boolean zHasText = hasText();
        if (zHasText && (i3 = this.mSavedPaddingLeft) >= 0) {
            super.setPadding(i3, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int measuredWidth = getMeasuredWidth();
        int iMin = mode == Integer.MIN_VALUE ? Math.min(size, this.mMinWidth) : this.mMinWidth;
        if (mode != 1073741824 && this.mMinWidth > 0 && measuredWidth < iMin) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(iMin, 1073741824), i2);
        }
        if (zHasText || this.mIcon == null) {
            return;
        }
        super.setPadding((getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(null);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener;
        if (this.mItemData.hasSubMenu() && (forwardingListener = this.mForwardingListener) != null && forwardingListener.onTouch(this, motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public boolean prefersCondensedTitle() {
        return true;
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void setCheckable(boolean z) {
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void setChecked(boolean z) {
    }

    public void setExpandedFormat(boolean z) {
        if (this.mExpandedFormat != z) {
            this.mExpandedFormat = z;
            MenuItemImpl menuItemImpl = this.mItemData;
            if (menuItemImpl != null) {
                menuItemImpl.actionFormatChanged();
            }
        }
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int i = this.mMaxIconSize;
            int i2 = intrinsicWidth;
            int i3 = intrinsicHeight;
            if (intrinsicWidth > i) {
                i3 = (int) (intrinsicHeight * (i / intrinsicWidth));
                i2 = i;
            }
            int i4 = this.mMaxIconSize;
            int i5 = i2;
            int i6 = i3;
            if (i3 > i4) {
                i5 = (int) (i2 * (i4 / i3));
                i6 = i4;
            }
            drawable.setBounds(0, 0, i5, i6);
        }
        setCompoundDrawables(drawable, null, null, null);
        updateTextButtonVisibility();
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        this.mSavedPaddingLeft = i;
        super.setPadding(i, i2, i3, i4);
    }

    public void setPopupCallback(PopupCallback popupCallback) {
        this.mPopupCallback = popupCallback;
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void setShortcut(boolean z, char c) {
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        updateTextButtonVisibility();
    }

    @Override // android.support.v7.view.menu.MenuView.ItemView
    public boolean showsIcon() {
        return true;
    }
}
