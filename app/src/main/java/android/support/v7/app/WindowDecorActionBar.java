package android.support.v7.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.ActionMode;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/WindowDecorActionBar.class */
public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
    static final boolean $assertionsDisabled = false;
    private static final long FADE_IN_DURATION_MS = 200;
    private static final long FADE_OUT_DURATION_MS = 100;
    private static final int INVALID_POSITION = -1;
    private static final String TAG = "WindowDecorActionBar";
    private static final Interpolator sHideInterpolator = new AccelerateInterpolator();
    private static final Interpolator sShowInterpolator = new DecelerateInterpolator();
    ActionModeImpl mActionMode;
    private Activity mActivity;
    ActionBarContainer mContainerView;
    View mContentView;
    Context mContext;
    ActionBarContextView mContextView;
    ViewPropertyAnimatorCompatSet mCurrentShowAnim;
    DecorToolbar mDecorToolbar;
    ActionMode mDeferredDestroyActionMode;
    ActionMode.Callback mDeferredModeDestroyCallback;
    private Dialog mDialog;
    private boolean mDisplayHomeAsUpSet;
    private boolean mHasEmbeddedTabs;
    boolean mHiddenByApp;
    boolean mHiddenBySystem;
    boolean mHideOnContentScroll;
    private boolean mLastMenuVisibility;
    ActionBarOverlayLayout mOverlayLayout;
    private TabImpl mSelectedTab;
    private boolean mShowHideAnimationEnabled;
    private boolean mShowingForMode;
    ScrollingTabContainerView mTabScrollView;
    private Context mThemedContext;
    private ArrayList<TabImpl> mTabs = new ArrayList<>();
    private int mSavedTabPosition = -1;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList<>();
    private int mCurWindowVisibility = 0;
    boolean mContentAnimations = true;
    private boolean mNowShowing = true;
    final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter(this) { // from class: android.support.v7.app.WindowDecorActionBar.1
        final WindowDecorActionBar this$0;

        {
            this.this$0 = this;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
        public void onAnimationEnd(View view) {
            if (this.this$0.mContentAnimations && this.this$0.mContentView != null) {
                this.this$0.mContentView.setTranslationY(0.0f);
                this.this$0.mContainerView.setTranslationY(0.0f);
            }
            this.this$0.mContainerView.setVisibility(8);
            this.this$0.mContainerView.setTransitioning(false);
            this.this$0.mCurrentShowAnim = null;
            this.this$0.completeDeferredDestroyActionMode();
            if (this.this$0.mOverlayLayout != null) {
                ViewCompat.requestApplyInsets(this.this$0.mOverlayLayout);
            }
        }
    };
    final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter(this) { // from class: android.support.v7.app.WindowDecorActionBar.2
        final WindowDecorActionBar this$0;

        {
            this.this$0 = this;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
        public void onAnimationEnd(View view) {
            this.this$0.mCurrentShowAnim = null;
            this.this$0.mContainerView.requestLayout();
        }
    };
    final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener(this) { // from class: android.support.v7.app.WindowDecorActionBar.3
        final WindowDecorActionBar this$0;

        {
            this.this$0 = this;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorUpdateListener
        public void onAnimationUpdate(View view) {
            ((View) this.this$0.mContainerView.getParent()).invalidate();
        }
    };

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/WindowDecorActionBar$ActionModeImpl.class */
    public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback {
        private final Context mActionModeContext;
        private ActionMode.Callback mCallback;
        private WeakReference<View> mCustomView;
        private final MenuBuilder mMenu;
        final WindowDecorActionBar this$0;

        public ActionModeImpl(WindowDecorActionBar windowDecorActionBar, Context context, ActionMode.Callback callback) {
            this.this$0 = windowDecorActionBar;
            this.mActionModeContext = context;
            this.mCallback = callback;
            MenuBuilder defaultShowAsAction = new MenuBuilder(context).setDefaultShowAsAction(1);
            this.mMenu = defaultShowAsAction;
            defaultShowAsAction.setCallback(this);
        }

        public boolean dispatchOnCreate() {
            this.mMenu.stopDispatchingItemsChanged();
            try {
                return this.mCallback.onCreateActionMode(this, this.mMenu);
            } finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        @Override // android.support.v7.view.ActionMode
        public void finish() {
            if (this.this$0.mActionMode != this) {
                return;
            }
            if (WindowDecorActionBar.checkShowingFlags(this.this$0.mHiddenByApp, this.this$0.mHiddenBySystem, false)) {
                this.mCallback.onDestroyActionMode(this);
            } else {
                this.this$0.mDeferredDestroyActionMode = this;
                this.this$0.mDeferredModeDestroyCallback = this.mCallback;
            }
            this.mCallback = null;
            this.this$0.animateToMode(false);
            this.this$0.mContextView.closeMode();
            this.this$0.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
            this.this$0.mOverlayLayout.setHideOnContentScrollEnabled(this.this$0.mHideOnContentScroll);
            this.this$0.mActionMode = null;
        }

        @Override // android.support.v7.view.ActionMode
        public View getCustomView() {
            WeakReference<View> weakReference = this.mCustomView;
            return weakReference != null ? weakReference.get() : null;
        }

        @Override // android.support.v7.view.ActionMode
        public Menu getMenu() {
            return this.mMenu;
        }

        @Override // android.support.v7.view.ActionMode
        public MenuInflater getMenuInflater() {
            return new SupportMenuInflater(this.mActionModeContext);
        }

        @Override // android.support.v7.view.ActionMode
        public CharSequence getSubtitle() {
            return this.this$0.mContextView.getSubtitle();
        }

        @Override // android.support.v7.view.ActionMode
        public CharSequence getTitle() {
            return this.this$0.mContextView.getTitle();
        }

        @Override // android.support.v7.view.ActionMode
        public void invalidate() {
            if (this.this$0.mActionMode != this) {
                return;
            }
            this.mMenu.stopDispatchingItemsChanged();
            try {
                this.mCallback.onPrepareActionMode(this, this.mMenu);
            } finally {
                this.mMenu.startDispatchingItemsChanged();
            }
        }

        @Override // android.support.v7.view.ActionMode
        public boolean isTitleOptional() {
            return this.this$0.mContextView.isTitleOptional();
        }

        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        }

        public void onCloseSubMenu(SubMenuBuilder subMenuBuilder) {
        }

        @Override // android.support.v7.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            ActionMode.Callback callback = this.mCallback;
            if (callback != null) {
                return callback.onActionItemClicked(this, menuItem);
            }
            return false;
        }

        @Override // android.support.v7.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (this.mCallback == null) {
                return;
            }
            invalidate();
            this.this$0.mContextView.showOverflowMenu();
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            if (this.mCallback == null) {
                return false;
            }
            if (!subMenuBuilder.hasVisibleItems()) {
                return true;
            }
            new MenuPopupHelper(this.this$0.getThemedContext(), subMenuBuilder).show();
            return true;
        }

        @Override // android.support.v7.view.ActionMode
        public void setCustomView(View view) {
            this.this$0.mContextView.setCustomView(view);
            this.mCustomView = new WeakReference<>(view);
        }

        @Override // android.support.v7.view.ActionMode
        public void setSubtitle(int i) {
            setSubtitle(this.this$0.mContext.getResources().getString(i));
        }

        @Override // android.support.v7.view.ActionMode
        public void setSubtitle(CharSequence charSequence) {
            this.this$0.mContextView.setSubtitle(charSequence);
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitle(int i) {
            setTitle(this.this$0.mContext.getResources().getString(i));
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitle(CharSequence charSequence) {
            this.this$0.mContextView.setTitle(charSequence);
        }

        @Override // android.support.v7.view.ActionMode
        public void setTitleOptionalHint(boolean z) {
            super.setTitleOptionalHint(z);
            this.this$0.mContextView.setTitleOptional(z);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/WindowDecorActionBar$TabImpl.class */
    public class TabImpl extends ActionBar.Tab {
        private ActionBar.TabListener mCallback;
        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;
        final WindowDecorActionBar this$0;

        public TabImpl(WindowDecorActionBar windowDecorActionBar) {
            this.this$0 = windowDecorActionBar;
        }

        public ActionBar.TabListener getCallback() {
            return this.mCallback;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public View getCustomView() {
            return this.mCustomView;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public Drawable getIcon() {
            return this.mIcon;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public int getPosition() {
            return this.mPosition;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public Object getTag() {
            return this.mTag;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public CharSequence getText() {
            return this.mText;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public void select() {
            this.this$0.selectTab(this);
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setContentDescription(int i) {
            return setContentDescription(this.this$0.mContext.getResources().getText(i));
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setContentDescription(CharSequence charSequence) {
            this.mContentDesc = charSequence;
            if (this.mPosition >= 0) {
                this.this$0.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setCustomView(int i) {
            return setCustomView(LayoutInflater.from(this.this$0.getThemedContext()).inflate(i, (ViewGroup) null));
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setCustomView(View view) {
            this.mCustomView = view;
            if (this.mPosition >= 0) {
                this.this$0.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setIcon(int i) {
            return setIcon(AppCompatResources.getDrawable(this.this$0.mContext, i));
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setIcon(Drawable drawable) {
            this.mIcon = drawable;
            if (this.mPosition >= 0) {
                this.this$0.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        public void setPosition(int i) {
            this.mPosition = i;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setTabListener(ActionBar.TabListener tabListener) {
            this.mCallback = tabListener;
            return this;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setTag(Object obj) {
            this.mTag = obj;
            return this;
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setText(int i) {
            return setText(this.this$0.mContext.getResources().getText(i));
        }

        @Override // android.support.v7.app.ActionBar.Tab
        public ActionBar.Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                this.this$0.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }
    }

    public WindowDecorActionBar(Activity activity, boolean z) {
        this.mActivity = activity;
        View decorView = activity.getWindow().getDecorView();
        init(decorView);
        if (z) {
            return;
        }
        this.mContentView = decorView.findViewById(R.id.content);
    }

    public WindowDecorActionBar(Dialog dialog) {
        this.mDialog = dialog;
        init(dialog.getWindow().getDecorView());
    }

    public WindowDecorActionBar(View view) {
        init(view);
    }

    static boolean checkShowingFlags(boolean z, boolean z2, boolean z3) {
        if (z3) {
            return true;
        }
        return (z || z2) ? false : true;
    }

    private void cleanupTabs() {
        if (this.mSelectedTab != null) {
            selectTab(null);
        }
        this.mTabs.clear();
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            scrollingTabContainerView.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }

    private void configureTab(ActionBar.Tab tab, int i) {
        TabImpl tabImpl = (TabImpl) tab;
        if (tabImpl.getCallback() == null) {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }
        tabImpl.setPosition(i);
        this.mTabs.add(i, tabImpl);
        int size = this.mTabs.size();
        while (true) {
            i++;
            if (i >= size) {
                return;
            } else {
                this.mTabs.get(i).setPosition(i);
            }
        }
    }

    private void ensureTabsExist() {
        if (this.mTabScrollView != null) {
            return;
        }
        ScrollingTabContainerView scrollingTabContainerView = new ScrollingTabContainerView(this.mContext);
        if (this.mHasEmbeddedTabs) {
            scrollingTabContainerView.setVisibility(0);
            this.mDecorToolbar.setEmbeddedTabView(scrollingTabContainerView);
        } else {
            if (getNavigationMode() == 2) {
                scrollingTabContainerView.setVisibility(0);
                ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
                if (actionBarOverlayLayout != null) {
                    ViewCompat.requestApplyInsets(actionBarOverlayLayout);
                }
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
            this.mContainerView.setTabContainer(scrollingTabContainerView);
        }
        this.mTabScrollView = scrollingTabContainerView;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Can't make a decor toolbar out of ");
        sb.append(view != 0 ? view.getClass().getSimpleName() : "null");
        throw new IllegalStateException(sb.toString());
    }

    private void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.setShowingForActionMode(false);
            }
            updateVisibility(false);
        }
    }

    private void init(View view) {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) view.findViewById(C0287R.id.decor_content_parent);
        this.mOverlayLayout = actionBarOverlayLayout;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setActionBarVisibilityCallback(this);
        }
        this.mDecorToolbar = getDecorToolbar(view.findViewById(C0287R.id.action_bar));
        this.mContextView = (ActionBarContextView) view.findViewById(C0287R.id.action_context_bar);
        ActionBarContainer actionBarContainer = (ActionBarContainer) view.findViewById(C0287R.id.action_bar_container);
        this.mContainerView = actionBarContainer;
        DecorToolbar decorToolbar = this.mDecorToolbar;
        if (decorToolbar == null || this.mContextView == null || actionBarContainer == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
        }
        this.mContext = decorToolbar.getContext();
        boolean z = (this.mDecorToolbar.getDisplayOptions() & 4) != 0;
        if (z) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
        setHomeButtonEnabled(actionBarPolicy.enableHomeButtonByDefault() || z);
        setHasEmbeddedTabs(actionBarPolicy.hasEmbeddedTabs());
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(null, C0287R.styleable.ActionBar, C0287R.attr.actionBarStyle, 0);
        if (typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.ActionBar_hideOnContentScroll, false)) {
            setHideOnContentScrollEnabled(true);
        }
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.ActionBar_elevation, 0);
        if (dimensionPixelSize != 0) {
            setElevation(dimensionPixelSize);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void setHasEmbeddedTabs(boolean z) {
        this.mHasEmbeddedTabs = z;
        if (z) {
            this.mContainerView.setTabContainer(null);
            this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
        } else {
            this.mDecorToolbar.setEmbeddedTabView(null);
            this.mContainerView.setTabContainer(this.mTabScrollView);
        }
        boolean z2 = getNavigationMode() == 2;
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            if (z2) {
                scrollingTabContainerView.setVisibility(0);
                ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
                if (actionBarOverlayLayout != null) {
                    ViewCompat.requestApplyInsets(actionBarOverlayLayout);
                }
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
        }
        this.mDecorToolbar.setCollapsible(!this.mHasEmbeddedTabs && z2);
        this.mOverlayLayout.setHasNonEmbeddedTabs(!this.mHasEmbeddedTabs && z2);
    }

    private boolean shouldAnimateContextView() {
        return ViewCompat.isLaidOut(this.mContainerView);
    }

    private void showForActionMode() {
        if (this.mShowingForMode) {
            return;
        }
        this.mShowingForMode = true;
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setShowingForActionMode(true);
        }
        updateVisibility(false);
    }

    private void updateVisibility(boolean z) {
        if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (this.mNowShowing) {
                return;
            }
            this.mNowShowing = true;
            doShow(z);
            return;
        }
        if (this.mNowShowing) {
            this.mNowShowing = false;
            doHide(z);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add(onMenuVisibilityListener);
    }

    @Override // android.support.v7.app.ActionBar
    public void addTab(ActionBar.Tab tab) {
        addTab(tab, this.mTabs.isEmpty());
    }

    @Override // android.support.v7.app.ActionBar
    public void addTab(ActionBar.Tab tab, int i) {
        addTab(tab, i, this.mTabs.isEmpty());
    }

    @Override // android.support.v7.app.ActionBar
    public void addTab(ActionBar.Tab tab, int i, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, i, z);
        configureTab(tab, i);
        if (z) {
            selectTab(tab);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void addTab(ActionBar.Tab tab, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, z);
        configureTab(tab, this.mTabs.size());
        if (z) {
            selectTab(tab);
        }
    }

    public void animateToMode(boolean z) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2;
        if (z) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        if (!shouldAnimateContextView()) {
            if (z) {
                this.mDecorToolbar.setVisibility(4);
                this.mContextView.setVisibility(0);
                return;
            } else {
                this.mDecorToolbar.setVisibility(0);
                this.mContextView.setVisibility(8);
                return;
            }
        }
        if (z) {
            viewPropertyAnimatorCompat2 = this.mDecorToolbar.setupAnimatorToVisibility(4, FADE_OUT_DURATION_MS);
            viewPropertyAnimatorCompat = this.mContextView.setupAnimatorToVisibility(0, FADE_IN_DURATION_MS);
        } else {
            viewPropertyAnimatorCompat = this.mDecorToolbar.setupAnimatorToVisibility(0, FADE_IN_DURATION_MS);
            viewPropertyAnimatorCompat2 = this.mContextView.setupAnimatorToVisibility(8, FADE_OUT_DURATION_MS);
        }
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = new ViewPropertyAnimatorCompatSet();
        viewPropertyAnimatorCompatSet.playSequentially(viewPropertyAnimatorCompat2, viewPropertyAnimatorCompat);
        viewPropertyAnimatorCompatSet.start();
    }

    @Override // android.support.v7.app.ActionBar
    public boolean collapseActionView() {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        if (decorToolbar == null || !decorToolbar.hasExpandedActionView()) {
            return false;
        }
        this.mDecorToolbar.collapseActionView();
        return true;
    }

    void completeDeferredDestroyActionMode() {
        ActionMode.Callback callback = this.mDeferredModeDestroyCallback;
        if (callback != null) {
            callback.onDestroyActionMode(this.mDeferredDestroyActionMode);
            this.mDeferredDestroyActionMode = null;
            this.mDeferredModeDestroyCallback = null;
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void dispatchMenuVisibilityChanged(boolean z) {
        if (z == this.mLastMenuVisibility) {
            return;
        }
        this.mLastMenuVisibility = z;
        int size = this.mMenuVisibilityListeners.size();
        for (int i = 0; i < size; i++) {
            this.mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(z);
        }
    }

    public void doHide(boolean z) {
        View view;
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.mCurrentShowAnim;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.cancel();
        }
        if (this.mCurWindowVisibility != 0 || (!this.mShowHideAnimationEnabled && !z)) {
            this.mHideListener.onAnimationEnd(null);
            return;
        }
        this.mContainerView.setAlpha(1.0f);
        this.mContainerView.setTransitioning(true);
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet2 = new ViewPropertyAnimatorCompatSet();
        float f = -this.mContainerView.getHeight();
        float f2 = f;
        if (z) {
            this.mContainerView.getLocationInWindow(new int[]{0, 0});
            f2 = f - r0[1];
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompatTranslationY = ViewCompat.animate(this.mContainerView).translationY(f2);
        viewPropertyAnimatorCompatTranslationY.setUpdateListener(this.mUpdateListener);
        viewPropertyAnimatorCompatSet2.play(viewPropertyAnimatorCompatTranslationY);
        if (this.mContentAnimations && (view = this.mContentView) != null) {
            viewPropertyAnimatorCompatSet2.play(ViewCompat.animate(view).translationY(f2));
        }
        viewPropertyAnimatorCompatSet2.setInterpolator(sHideInterpolator);
        viewPropertyAnimatorCompatSet2.setDuration(250L);
        viewPropertyAnimatorCompatSet2.setListener(this.mHideListener);
        this.mCurrentShowAnim = viewPropertyAnimatorCompatSet2;
        viewPropertyAnimatorCompatSet2.start();
    }

    public void doShow(boolean z) {
        View view;
        View view2;
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.mCurrentShowAnim;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.cancel();
        }
        this.mContainerView.setVisibility(0);
        if (this.mCurWindowVisibility == 0 && (this.mShowHideAnimationEnabled || z)) {
            this.mContainerView.setTranslationY(0.0f);
            float f = -this.mContainerView.getHeight();
            float f2 = f;
            if (z) {
                this.mContainerView.getLocationInWindow(new int[]{0, 0});
                f2 = f - r0[1];
            }
            this.mContainerView.setTranslationY(f2);
            ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet2 = new ViewPropertyAnimatorCompatSet();
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompatTranslationY = ViewCompat.animate(this.mContainerView).translationY(0.0f);
            viewPropertyAnimatorCompatTranslationY.setUpdateListener(this.mUpdateListener);
            viewPropertyAnimatorCompatSet2.play(viewPropertyAnimatorCompatTranslationY);
            if (this.mContentAnimations && (view2 = this.mContentView) != null) {
                view2.setTranslationY(f2);
                viewPropertyAnimatorCompatSet2.play(ViewCompat.animate(this.mContentView).translationY(0.0f));
            }
            viewPropertyAnimatorCompatSet2.setInterpolator(sShowInterpolator);
            viewPropertyAnimatorCompatSet2.setDuration(250L);
            viewPropertyAnimatorCompatSet2.setListener(this.mShowListener);
            this.mCurrentShowAnim = viewPropertyAnimatorCompatSet2;
            viewPropertyAnimatorCompatSet2.start();
        } else {
            this.mContainerView.setAlpha(1.0f);
            this.mContainerView.setTranslationY(0.0f);
            if (this.mContentAnimations && (view = this.mContentView) != null) {
                view.setTranslationY(0.0f);
            }
            this.mShowListener.onAnimationEnd(null);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.mOverlayLayout;
        if (actionBarOverlayLayout != null) {
            ViewCompat.requestApplyInsets(actionBarOverlayLayout);
        }
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void enableContentAnimations(boolean z) {
        this.mContentAnimations = z;
    }

    @Override // android.support.v7.app.ActionBar
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }

    @Override // android.support.v7.app.ActionBar
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    @Override // android.support.v7.app.ActionBar
    public float getElevation() {
        return ViewCompat.getElevation(this.mContainerView);
    }

    @Override // android.support.v7.app.ActionBar
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    @Override // android.support.v7.app.ActionBar
    public int getHideOffset() {
        return this.mOverlayLayout.getActionBarHideOffset();
    }

    @Override // android.support.v7.app.ActionBar
    public int getNavigationItemCount() {
        int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 1) {
            return this.mDecorToolbar.getDropdownItemCount();
        }
        if (navigationMode != 2) {
            return 0;
        }
        return this.mTabs.size();
    }

    @Override // android.support.v7.app.ActionBar
    public int getNavigationMode() {
        return this.mDecorToolbar.getNavigationMode();
    }

    @Override // android.support.v7.app.ActionBar
    public int getSelectedNavigationIndex() {
        int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 1) {
            return this.mDecorToolbar.getDropdownSelectedPosition();
        }
        int position = -1;
        if (navigationMode != 2) {
            return -1;
        }
        TabImpl tabImpl = this.mSelectedTab;
        if (tabImpl != null) {
            position = tabImpl.getPosition();
        }
        return position;
    }

    @Override // android.support.v7.app.ActionBar
    public ActionBar.Tab getSelectedTab() {
        return this.mSelectedTab;
    }

    @Override // android.support.v7.app.ActionBar
    public CharSequence getSubtitle() {
        return this.mDecorToolbar.getSubtitle();
    }

    @Override // android.support.v7.app.ActionBar
    public ActionBar.Tab getTabAt(int i) {
        return this.mTabs.get(i);
    }

    @Override // android.support.v7.app.ActionBar
    public int getTabCount() {
        return this.mTabs.size();
    }

    @Override // android.support.v7.app.ActionBar
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(C0287R.attr.actionBarWidgetTheme, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, i);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    @Override // android.support.v7.app.ActionBar
    public CharSequence getTitle() {
        return this.mDecorToolbar.getTitle();
    }

    public boolean hasIcon() {
        return this.mDecorToolbar.hasIcon();
    }

    public boolean hasLogo() {
        return this.mDecorToolbar.hasLogo();
    }

    @Override // android.support.v7.app.ActionBar
    public void hide() {
        if (this.mHiddenByApp) {
            return;
        }
        this.mHiddenByApp = true;
        updateVisibility(false);
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void hideForSystem() {
        if (this.mHiddenBySystem) {
            return;
        }
        this.mHiddenBySystem = true;
        updateVisibility(true);
    }

    @Override // android.support.v7.app.ActionBar
    public boolean isHideOnContentScrollEnabled() {
        return this.mOverlayLayout.isHideOnContentScrollEnabled();
    }

    @Override // android.support.v7.app.ActionBar
    public boolean isShowing() {
        int height = getHeight();
        return this.mNowShowing && (height == 0 || getHideOffset() < height);
    }

    @Override // android.support.v7.app.ActionBar
    public boolean isTitleTruncated() {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        return decorToolbar != null && decorToolbar.isTitleTruncated();
    }

    @Override // android.support.v7.app.ActionBar
    public ActionBar.Tab newTab() {
        return new TabImpl(this);
    }

    @Override // android.support.v7.app.ActionBar
    public void onConfigurationChanged(Configuration configuration) {
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onContentScrollStarted() {
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet = this.mCurrentShowAnim;
        if (viewPropertyAnimatorCompatSet != null) {
            viewPropertyAnimatorCompatSet.cancel();
            this.mCurrentShowAnim = null;
        }
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onContentScrollStopped() {
    }

    @Override // android.support.v7.app.ActionBar
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        Menu menu;
        ActionModeImpl actionModeImpl = this.mActionMode;
        if (actionModeImpl == null || (menu = actionModeImpl.getMenu()) == null) {
            return false;
        }
        boolean z = true;
        if (KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() == 1) {
            z = false;
        }
        menu.setQwertyMode(z);
        return menu.performShortcut(i, keyEvent, 0);
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void onWindowVisibilityChanged(int i) {
        this.mCurWindowVisibility = i;
    }

    @Override // android.support.v7.app.ActionBar
    public void removeAllTabs() {
        cleanupTabs();
    }

    @Override // android.support.v7.app.ActionBar
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove(onMenuVisibilityListener);
    }

    @Override // android.support.v7.app.ActionBar
    public void removeTab(ActionBar.Tab tab) {
        removeTabAt(tab.getPosition());
    }

    @Override // android.support.v7.app.ActionBar
    public void removeTabAt(int i) {
        if (this.mTabScrollView == null) {
            return;
        }
        TabImpl tabImpl = this.mSelectedTab;
        int position = tabImpl != null ? tabImpl.getPosition() : this.mSavedTabPosition;
        this.mTabScrollView.removeTabAt(i);
        TabImpl tabImplRemove = this.mTabs.remove(i);
        if (tabImplRemove != null) {
            tabImplRemove.setPosition(-1);
        }
        int size = this.mTabs.size();
        for (int i2 = i; i2 < size; i2++) {
            this.mTabs.get(i2).setPosition(i2);
        }
        if (position == i) {
            selectTab(this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, i - 1)));
        }
    }

    @Override // android.support.v7.app.ActionBar
    public boolean requestFocus() {
        ViewGroup viewGroup = this.mDecorToolbar.getViewGroup();
        if (viewGroup == null || viewGroup.hasFocus()) {
            return false;
        }
        viewGroup.requestFocus();
        return true;
    }

    @Override // android.support.v7.app.ActionBar
    public void selectTab(ActionBar.Tab tab) {
        int position = -1;
        if (getNavigationMode() != 2) {
            if (tab != null) {
                position = tab.getPosition();
            }
            this.mSavedTabPosition = position;
            return;
        }
        FragmentTransaction fragmentTransactionDisallowAddToBackStack = (!(this.mActivity instanceof FragmentActivity) || this.mDecorToolbar.getViewGroup().isInEditMode()) ? null : ((FragmentActivity) this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
        TabImpl tabImpl = this.mSelectedTab;
        if (tabImpl != tab) {
            ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
            if (tab != null) {
                position = tab.getPosition();
            }
            scrollingTabContainerView.setTabSelected(position);
            TabImpl tabImpl2 = this.mSelectedTab;
            if (tabImpl2 != null) {
                tabImpl2.getCallback().onTabUnselected(this.mSelectedTab, fragmentTransactionDisallowAddToBackStack);
            }
            TabImpl tabImpl3 = (TabImpl) tab;
            this.mSelectedTab = tabImpl3;
            if (tabImpl3 != null) {
                tabImpl3.getCallback().onTabSelected(this.mSelectedTab, fragmentTransactionDisallowAddToBackStack);
            }
        } else if (tabImpl != null) {
            tabImpl.getCallback().onTabReselected(this.mSelectedTab, fragmentTransactionDisallowAddToBackStack);
            this.mTabScrollView.animateToTab(tab.getPosition());
        }
        if (fragmentTransactionDisallowAddToBackStack == null || fragmentTransactionDisallowAddToBackStack.isEmpty()) {
            return;
        }
        fragmentTransactionDisallowAddToBackStack.commit();
    }

    @Override // android.support.v7.app.ActionBar
    public void setBackgroundDrawable(Drawable drawable) {
        this.mContainerView.setPrimaryBackground(drawable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setCustomView(int i) {
        setCustomView(LayoutInflater.from(getThemedContext()).inflate(i, this.mDecorToolbar.getViewGroup(), false));
    }

    @Override // android.support.v7.app.ActionBar
    public void setCustomView(View view) {
        this.mDecorToolbar.setCustomView(view);
    }

    @Override // android.support.v7.app.ActionBar
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        this.mDecorToolbar.setCustomView(view);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDefaultDisplayHomeAsUpEnabled(boolean z) {
        if (this.mDisplayHomeAsUpSet) {
            return;
        }
        setDisplayHomeAsUpEnabled(z);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayHomeAsUpEnabled(boolean z) {
        setDisplayOptions(z ? 4 : 0, 4);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayOptions(int i) {
        if ((i & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayOptions(int i, int i2) {
        int displayOptions = this.mDecorToolbar.getDisplayOptions();
        if ((i2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mDecorToolbar.setDisplayOptions((i & i2) | ((i2 ^ (-1)) & displayOptions));
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayShowCustomEnabled(boolean z) {
        setDisplayOptions(z ? 16 : 0, 16);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayShowHomeEnabled(boolean z) {
        setDisplayOptions(z ? 2 : 0, 2);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayShowTitleEnabled(boolean z) {
        setDisplayOptions(z ? 8 : 0, 8);
    }

    @Override // android.support.v7.app.ActionBar
    public void setDisplayUseLogoEnabled(boolean z) {
        setDisplayOptions(z ? 1 : 0, 1);
    }

    @Override // android.support.v7.app.ActionBar
    public void setElevation(float f) {
        ViewCompat.setElevation(this.mContainerView, f);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHideOffset(int i) {
        if (i != 0 && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
        }
        this.mOverlayLayout.setActionBarHideOffset(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHideOnContentScrollEnabled(boolean z) {
        if (z && !this.mOverlayLayout.isInOverlayMode()) {
            throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
        }
        this.mHideOnContentScroll = z;
        this.mOverlayLayout.setHideOnContentScrollEnabled(z);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeActionContentDescription(int i) {
        this.mDecorToolbar.setNavigationContentDescription(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeActionContentDescription(CharSequence charSequence) {
        this.mDecorToolbar.setNavigationContentDescription(charSequence);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeAsUpIndicator(int i) {
        this.mDecorToolbar.setNavigationIcon(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeAsUpIndicator(Drawable drawable) {
        this.mDecorToolbar.setNavigationIcon(drawable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setHomeButtonEnabled(boolean z) {
        this.mDecorToolbar.setHomeButtonEnabled(z);
    }

    @Override // android.support.v7.app.ActionBar
    public void setIcon(int i) {
        this.mDecorToolbar.setIcon(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setIcon(Drawable drawable) {
        this.mDecorToolbar.setIcon(drawable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener onNavigationListener) {
        this.mDecorToolbar.setDropdownParams(spinnerAdapter, new NavItemSelectedListener(onNavigationListener));
    }

    @Override // android.support.v7.app.ActionBar
    public void setLogo(int i) {
        this.mDecorToolbar.setLogo(i);
    }

    @Override // android.support.v7.app.ActionBar
    public void setLogo(Drawable drawable) {
        this.mDecorToolbar.setLogo(drawable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setNavigationMode(int i) {
        ActionBarOverlayLayout actionBarOverlayLayout;
        int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 2) {
            this.mSavedTabPosition = getSelectedNavigationIndex();
            selectTab(null);
            this.mTabScrollView.setVisibility(8);
        }
        if (navigationMode != i && !this.mHasEmbeddedTabs && (actionBarOverlayLayout = this.mOverlayLayout) != null) {
            ViewCompat.requestApplyInsets(actionBarOverlayLayout);
        }
        this.mDecorToolbar.setNavigationMode(i);
        if (i == 2) {
            ensureTabsExist();
            this.mTabScrollView.setVisibility(0);
            int i2 = this.mSavedTabPosition;
            if (i2 != -1) {
                setSelectedNavigationItem(i2);
                this.mSavedTabPosition = -1;
            }
        }
        this.mDecorToolbar.setCollapsible(i == 2 && !this.mHasEmbeddedTabs);
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.mOverlayLayout;
        boolean z = false;
        if (i == 2) {
            z = false;
            if (!this.mHasEmbeddedTabs) {
                z = true;
            }
        }
        actionBarOverlayLayout2.setHasNonEmbeddedTabs(z);
    }

    @Override // android.support.v7.app.ActionBar
    public void setSelectedNavigationItem(int i) {
        int navigationMode = this.mDecorToolbar.getNavigationMode();
        if (navigationMode == 1) {
            this.mDecorToolbar.setDropdownSelectedPosition(i);
        } else {
            if (navigationMode != 2) {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            selectTab(this.mTabs.get(i));
        }
    }

    @Override // android.support.v7.app.ActionBar
    public void setShowHideAnimationEnabled(boolean z) {
        ViewPropertyAnimatorCompatSet viewPropertyAnimatorCompatSet;
        this.mShowHideAnimationEnabled = z;
        if (z || (viewPropertyAnimatorCompatSet = this.mCurrentShowAnim) == null) {
            return;
        }
        viewPropertyAnimatorCompatSet.cancel();
    }

    @Override // android.support.v7.app.ActionBar
    public void setSplitBackgroundDrawable(Drawable drawable) {
    }

    @Override // android.support.v7.app.ActionBar
    public void setStackedBackgroundDrawable(Drawable drawable) {
        this.mContainerView.setStackedBackground(drawable);
    }

    @Override // android.support.v7.app.ActionBar
    public void setSubtitle(int i) {
        setSubtitle(this.mContext.getString(i));
    }

    @Override // android.support.v7.app.ActionBar
    public void setSubtitle(CharSequence charSequence) {
        this.mDecorToolbar.setSubtitle(charSequence);
    }

    @Override // android.support.v7.app.ActionBar
    public void setTitle(int i) {
        setTitle(this.mContext.getString(i));
    }

    @Override // android.support.v7.app.ActionBar
    public void setTitle(CharSequence charSequence) {
        this.mDecorToolbar.setTitle(charSequence);
    }

    @Override // android.support.v7.app.ActionBar
    public void setWindowTitle(CharSequence charSequence) {
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    @Override // android.support.v7.app.ActionBar
    public void show() {
        if (this.mHiddenByApp) {
            this.mHiddenByApp = false;
            updateVisibility(false);
        }
    }

    @Override // android.support.v7.widget.ActionBarOverlayLayout.ActionBarVisibilityCallback
    public void showForSystem() {
        if (this.mHiddenBySystem) {
            this.mHiddenBySystem = false;
            updateVisibility(true);
        }
    }

    @Override // android.support.v7.app.ActionBar
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionModeImpl actionModeImpl = this.mActionMode;
        if (actionModeImpl != null) {
            actionModeImpl.finish();
        }
        this.mOverlayLayout.setHideOnContentScrollEnabled(false);
        this.mContextView.killMode();
        ActionModeImpl actionModeImpl2 = new ActionModeImpl(this, this.mContextView.getContext(), callback);
        if (!actionModeImpl2.dispatchOnCreate()) {
            return null;
        }
        this.mActionMode = actionModeImpl2;
        actionModeImpl2.invalidate();
        this.mContextView.initForMode(actionModeImpl2);
        animateToMode(true);
        this.mContextView.sendAccessibilityEvent(32);
        return actionModeImpl2;
    }
}
