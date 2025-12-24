package android.support.v7.app;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.Thread;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl.class */
class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, LayoutInflater.Factory2 {
    private static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean IS_PRE_LOLLIPOP;
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    final AppCompatCallback mAppCompatCallback;
    private AppCompatViewInflater mAppCompatViewInflater;
    final Window.Callback mAppCompatWindowCallback;
    private boolean mApplyDayNightCalled;
    private AutoNightModeManager mAutoNightModeManager;
    private boolean mClosingActionMenu;
    final Context mContext;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    boolean mHasActionBar;
    int mInvalidatePanelMenuFeatures;
    boolean mInvalidatePanelMenuPosted;
    boolean mIsDestroyed;
    boolean mIsFloating;
    private boolean mLongPressBackDown;
    MenuInflater mMenuInflater;
    final Window.Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private CharSequence mTitle;
    private TextView mTitleView;
    final Window mWindow;
    boolean mWindowNoTitle;
    ViewPropertyAnimatorCompat mFadeAnim = null;
    private boolean mHandleNativeActionModes = true;
    private int mLocalNightMode = -100;
    private final Runnable mInvalidatePanelMenuRunnable = new Runnable(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.2
        final AppCompatDelegateImpl this$0;

        {
            this.this$0 = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            if ((this.this$0.mInvalidatePanelMenuFeatures & 1) != 0) {
                this.this$0.doInvalidatePanelMenu(0);
            }
            if ((this.this$0.mInvalidatePanelMenuFeatures & 4096) != 0) {
                this.this$0.doInvalidatePanelMenu(108);
            }
            this.this$0.mInvalidatePanelMenuPosted = false;
            this.this$0.mInvalidatePanelMenuFeatures = 0;
        }
    };

    /* renamed from: android.support.v7.app.AppCompatDelegateImpl$6 */
    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$6.class */
    class RunnableC02766 implements Runnable {
        final AppCompatDelegateImpl this$0;

        RunnableC02766(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.this$0.mActionModePopup.showAtLocation(this.this$0.mActionModeView, 55, 0, 0);
            this.this$0.endOnGoingFadeAnimation();
            if (!this.this$0.shouldAnimateActionModeView()) {
                this.this$0.mActionModeView.setAlpha(1.0f);
                this.this$0.mActionModeView.setVisibility(0);
            } else {
                this.this$0.mActionModeView.setAlpha(0.0f);
                AppCompatDelegateImpl appCompatDelegateImpl = this.this$0;
                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(1.0f);
                this.this$0.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.6.1
                    final RunnableC02766 this$1;

                    {
                        this.this$1 = this;
                    }

                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationEnd(View view) {
                        this.this$1.this$0.mActionModeView.setAlpha(1.0f);
                        this.this$1.this$0.mFadeAnim.setListener(null);
                        this.this$1.this$0.mFadeAnim = null;
                    }

                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationStart(View view) {
                        this.this$1.this$0.mActionModeView.setVisibility(0);
                    }
                });
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$ActionBarDrawableToggleImpl.class */
    private class ActionBarDrawableToggleImpl implements ActionBarDrawerToggle.Delegate {
        final AppCompatDelegateImpl this$0;

        ActionBarDrawableToggleImpl(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public Context getActionBarThemedContext() {
            return this.this$0.getActionBarThemedContext();
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public Drawable getThemeUpIndicator() {
            TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getActionBarThemedContext(), (AttributeSet) null, new int[]{C0287R.attr.homeAsUpIndicator});
            Drawable drawable = tintTypedArrayObtainStyledAttributes.getDrawable(0);
            tintTypedArrayObtainStyledAttributes.recycle();
            return drawable;
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public boolean isNavigationVisible() {
            ActionBar supportActionBar = this.this$0.getSupportActionBar();
            return (supportActionBar == null || (supportActionBar.getDisplayOptions() & 4) == 0) ? false : true;
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public void setActionBarDescription(int i) {
            ActionBar supportActionBar = this.this$0.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeActionContentDescription(i);
            }
        }

        @Override // android.support.v7.app.ActionBarDrawerToggle.Delegate
        public void setActionBarUpIndicator(Drawable drawable, int i) {
            ActionBar supportActionBar = this.this$0.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(drawable);
                supportActionBar.setHomeActionContentDescription(i);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$ActionMenuPresenterCallback.class */
    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        final AppCompatDelegateImpl this$0;

        ActionMenuPresenterCallback(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            this.this$0.checkCloseActionMenu(menuBuilder);
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback = this.this$0.getWindowCallback();
            if (windowCallback == null) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$ActionModeCallbackWrapperV9.class */
    class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
        private ActionMode.Callback mWrapped;
        final AppCompatDelegateImpl this$0;

        public ActionModeCallbackWrapperV9(AppCompatDelegateImpl appCompatDelegateImpl, ActionMode.Callback callback) {
            this.this$0 = appCompatDelegateImpl;
            this.mWrapped = callback;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (this.this$0.mActionModePopup != null) {
                this.this$0.mWindow.getDecorView().removeCallbacks(this.this$0.mShowActionModePopup);
            }
            if (this.this$0.mActionModeView != null) {
                this.this$0.endOnGoingFadeAnimation();
                AppCompatDelegateImpl appCompatDelegateImpl = this.this$0;
                appCompatDelegateImpl.mFadeAnim = ViewCompat.animate(appCompatDelegateImpl.mActionModeView).alpha(0.0f);
                this.this$0.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.ActionModeCallbackWrapperV9.1
                    final ActionModeCallbackWrapperV9 this$1;

                    {
                        this.this$1 = this;
                    }

                    @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationEnd(View view) {
                        this.this$1.this$0.mActionModeView.setVisibility(8);
                        if (this.this$1.this$0.mActionModePopup != null) {
                            this.this$1.this$0.mActionModePopup.dismiss();
                        } else if (this.this$1.this$0.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) this.this$1.this$0.mActionModeView.getParent());
                        }
                        this.this$1.this$0.mActionModeView.removeAllViews();
                        this.this$1.this$0.mFadeAnim.setListener(null);
                        this.this$1.this$0.mFadeAnim = null;
                    }
                });
            }
            if (this.this$0.mAppCompatCallback != null) {
                this.this$0.mAppCompatCallback.onSupportActionModeFinished(this.this$0.mActionMode);
            }
            this.this$0.mActionMode = null;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$AppCompatWindowCallback.class */
    class AppCompatWindowCallback extends WindowCallbackWrapper {
        final AppCompatDelegateImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AppCompatWindowCallback(AppCompatDelegateImpl appCompatDelegateImpl, Window.Callback callback) {
            super(callback);
            this.this$0 = appCompatDelegateImpl;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.this$0.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || this.this$0.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onContentChanged() {
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i, Menu menu) {
            if (i != 0 || (menu instanceof MenuBuilder)) {
                return super.onCreatePanelMenu(i, menu);
            }
            return false;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onMenuOpened(int i, Menu menu) {
            super.onMenuOpened(i, menu);
            this.this$0.onMenuOpened(i);
            return true;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onPanelClosed(int i, Menu menu) {
            super.onPanelClosed(i, menu);
            this.this$0.onPanelClosed(i);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public boolean onPreparePanel(int i, View view, Menu menu) {
            MenuBuilder menuBuilder = menu instanceof MenuBuilder ? (MenuBuilder) menu : null;
            if (i == 0 && menuBuilder == null) {
                return false;
            }
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(true);
            }
            boolean zOnPreparePanel = super.onPreparePanel(i, view, menu);
            if (menuBuilder != null) {
                menuBuilder.setOverrideVisibleItems(false);
            }
            return zOnPreparePanel;
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
            PanelFeatureState panelState = this.this$0.getPanelState(0, true);
            if (panelState == null || panelState.menu == null) {
                super.onProvideKeyboardShortcuts(list, menu, i);
            } else {
                super.onProvideKeyboardShortcuts(list, panelState.menu, i);
            }
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            return this.this$0.isHandleNativeActionModesEnabled() ? startAsSupportActionMode(callback) : super.onWindowStartingActionMode(callback);
        }

        @Override // android.support.v7.view.WindowCallbackWrapper, android.view.Window.Callback
        public android.view.ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return (this.this$0.isHandleNativeActionModesEnabled() && i == 0) ? startAsSupportActionMode(callback) : super.onWindowStartingActionMode(callback, i);
        }

        final android.view.ActionMode startAsSupportActionMode(ActionMode.Callback callback) {
            SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(this.this$0.mContext, callback);
            android.support.v7.view.ActionMode actionModeStartSupportActionMode = this.this$0.startSupportActionMode(callbackWrapper);
            if (actionModeStartSupportActionMode != null) {
                return callbackWrapper.getActionModeWrapper(actionModeStartSupportActionMode);
            }
            return null;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$AutoNightModeManager.class */
    final class AutoNightModeManager {
        private BroadcastReceiver mAutoTimeChangeReceiver;
        private IntentFilter mAutoTimeChangeReceiverFilter;
        private boolean mIsNight;
        private TwilightManager mTwilightManager;
        final AppCompatDelegateImpl this$0;

        AutoNightModeManager(AppCompatDelegateImpl appCompatDelegateImpl, TwilightManager twilightManager) {
            this.this$0 = appCompatDelegateImpl;
            this.mTwilightManager = twilightManager;
            this.mIsNight = twilightManager.isNight();
        }

        void cleanup() {
            if (this.mAutoTimeChangeReceiver != null) {
                this.this$0.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
                this.mAutoTimeChangeReceiver = null;
            }
        }

        void dispatchTimeChanged() {
            boolean zIsNight = this.mTwilightManager.isNight();
            if (zIsNight != this.mIsNight) {
                this.mIsNight = zIsNight;
                this.this$0.applyDayNight();
            }
        }

        int getApplyableNightMode() {
            boolean zIsNight = this.mTwilightManager.isNight();
            this.mIsNight = zIsNight;
            return zIsNight ? 2 : 1;
        }

        void setup() {
            cleanup();
            if (this.mAutoTimeChangeReceiver == null) {
                this.mAutoTimeChangeReceiver = new BroadcastReceiver(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.AutoNightModeManager.1
                    final AutoNightModeManager this$1;

                    {
                        this.this$1 = this;
                    }

                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        this.this$1.dispatchTimeChanged();
                    }
                };
            }
            if (this.mAutoTimeChangeReceiverFilter == null) {
                IntentFilter intentFilter = new IntentFilter();
                this.mAutoTimeChangeReceiverFilter = intentFilter;
                intentFilter.addAction("android.intent.action.TIME_SET");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
            }
            this.this$0.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$ListMenuDecorView.class */
    private class ListMenuDecorView extends ContentFrameLayout {
        final AppCompatDelegateImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ListMenuDecorView(AppCompatDelegateImpl appCompatDelegateImpl, Context context) {
            super(context);
            this.this$0 = appCompatDelegateImpl;
        }

        private boolean isOutOfBounds(int i, int i2) {
            return i < -5 || i2 < -5 || i > getWidth() + 5 || i2 > getHeight() + 5;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.this$0.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 || !isOutOfBounds((int) motionEvent.getX(), (int) motionEvent.getY())) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            this.this$0.closePanel(0);
            return true;
        }

        @Override // android.view.View
        public void setBackgroundResource(int i) {
            setBackgroundDrawable(AppCompatResources.getDrawable(getContext(), i));
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$PanelFeatureState.class */
    protected static final class PanelFeatureState {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;

        /* renamed from: x */
        int f32x;

        /* renamed from: y */
        int f33y;

        /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$PanelFeatureState$SavedState.class */
        private static class SavedState implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState.SavedState.1
                @Override // android.os.Parcelable.Creator
                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.readFromParcel(parcel, null);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.ClassLoaderCreator
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.readFromParcel(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                public SavedState[] newArray(int i) {
                    return new SavedState[i];
                }
            };
            int featureId;
            boolean isOpen;
            Bundle menuState;

            SavedState() {
            }

            static SavedState readFromParcel(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                boolean z = true;
                if (parcel.readInt() != 1) {
                    z = false;
                }
                savedState.isOpen = z;
                if (z) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(this.featureId);
                parcel.writeInt(this.isOpen ? 1 : 0);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }

        PanelFeatureState(int i) {
            this.featureId = i;
        }

        void applyFrozenState() {
            Bundle bundle;
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder == null || (bundle = this.frozenMenuState) == null) {
                return;
            }
            menuBuilder.restorePresenterStates(bundle);
            this.frozenMenuState = null;
        }

        public void clearMenuPresenters() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                menuBuilder.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }

        MenuView getListMenuView(MenuPresenter.Callback callback) {
            if (this.menu == null) {
                return null;
            }
            if (this.listMenuPresenter == null) {
                ListMenuPresenter listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, C0287R.layout.abc_list_menu_item_layout);
                this.listMenuPresenter = listMenuPresenter;
                listMenuPresenter.setCallback(callback);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        public boolean hasPanelItems() {
            boolean z = false;
            if (this.shownPanelView == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                z = true;
            }
            return z;
        }

        void onRestoreInstanceState(Parcelable parcelable) {
            SavedState savedState = (SavedState) parcelable;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void setMenu(MenuBuilder menuBuilder) {
            ListMenuPresenter listMenuPresenter;
            MenuBuilder menuBuilder2 = this.menu;
            if (menuBuilder == menuBuilder2) {
                return;
            }
            if (menuBuilder2 != null) {
                menuBuilder2.removeMenuPresenter(this.listMenuPresenter);
            }
            this.menu = menuBuilder;
            if (menuBuilder == null || (listMenuPresenter = this.listMenuPresenter) == null) {
                return;
            }
            menuBuilder.addMenuPresenter(listMenuPresenter);
        }

        void setStyle(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme themeNewTheme = context.getResources().newTheme();
            themeNewTheme.setTo(context.getTheme());
            themeNewTheme.resolveAttribute(C0287R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            }
            themeNewTheme.resolveAttribute(C0287R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                themeNewTheme.applyStyle(typedValue.resourceId, true);
            } else {
                themeNewTheme.applyStyle(C0287R.style.Theme_AppCompat_CompactMenu, true);
            }
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
            contextThemeWrapper.getTheme().setTo(themeNewTheme);
            this.listPresenterContext = contextThemeWrapper;
            TypedArray typedArrayObtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(C0287R.styleable.AppCompatTheme);
            this.background = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDelegateImpl$PanelMenuPresenterCallback.class */
    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        final AppCompatDelegateImpl this$0;

        PanelMenuPresenterCallback(AppCompatDelegateImpl appCompatDelegateImpl) {
            this.this$0 = appCompatDelegateImpl;
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            MenuBuilder rootMenu = menuBuilder.getRootMenu();
            boolean z2 = rootMenu != menuBuilder;
            AppCompatDelegateImpl appCompatDelegateImpl = this.this$0;
            if (z2) {
                menuBuilder = rootMenu;
            }
            PanelFeatureState panelFeatureStateFindMenuPanel = appCompatDelegateImpl.findMenuPanel(menuBuilder);
            if (panelFeatureStateFindMenuPanel != null) {
                if (!z2) {
                    this.this$0.closePanel(panelFeatureStateFindMenuPanel, z);
                } else {
                    this.this$0.callOnPanelClosed(panelFeatureStateFindMenuPanel.featureId, panelFeatureStateFindMenuPanel, rootMenu);
                    this.this$0.closePanel(panelFeatureStateFindMenuPanel, true);
                }
            }
        }

        @Override // android.support.v7.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            Window.Callback windowCallback;
            if (menuBuilder != null || !this.this$0.mHasActionBar || (windowCallback = this.this$0.getWindowCallback()) == null || this.this$0.mIsDestroyed) {
                return true;
            }
            windowCallback.onMenuOpened(108, menuBuilder);
            return true;
        }
    }

    static {
        boolean z = Build.VERSION.SDK_INT < 21;
        IS_PRE_LOLLIPOP = z;
        sWindowBackgroundStyleable = new int[]{R.attr.windowBackground};
        if (!z || sInstalledExceptionHandler) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) { // from class: android.support.v7.app.AppCompatDelegateImpl.1
            final Thread.UncaughtExceptionHandler val$defHandler;

            {
                this.val$defHandler = uncaughtExceptionHandler;
            }

            /* JADX WARN: Removed duplicated region for block: B:10:0x002e  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            private boolean shouldWrapException(java.lang.Throwable r4) {
                /*
                    r3 = this;
                    r0 = r4
                    boolean r0 = r0 instanceof android.content.res.Resources.NotFoundException
                    r7 = r0
                    r0 = 0
                    r6 = r0
                    r0 = r6
                    r5 = r0
                    r0 = r7
                    if (r0 == 0) goto L30
                    r0 = r4
                    java.lang.String r0 = r0.getMessage()
                    r4 = r0
                    r0 = r6
                    r5 = r0
                    r0 = r4
                    if (r0 == 0) goto L30
                    r0 = r4
                    java.lang.String r1 = "drawable"
                    boolean r0 = r0.contains(r1)
                    if (r0 != 0) goto L2e
                    r0 = r6
                    r5 = r0
                    r0 = r4
                    java.lang.String r1 = "Drawable"
                    boolean r0 = r0.contains(r1)
                    if (r0 == 0) goto L30
                L2e:
                    r0 = 1
                    r5 = r0
                L30:
                    r0 = r5
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.C02711.shouldWrapException(java.lang.Throwable):boolean");
            }

            @Override // java.lang.Thread.UncaughtExceptionHandler
            public void uncaughtException(Thread thread, Throwable th) {
                if (!shouldWrapException(th)) {
                    this.val$defHandler.uncaughtException(thread, th);
                    return;
                }
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(th.getMessage() + AppCompatDelegateImpl.EXCEPTION_HANDLER_MESSAGE_SUFFIX);
                notFoundException.initCause(th.getCause());
                notFoundException.setStackTrace(th.getStackTrace());
                this.val$defHandler.uncaughtException(thread, notFoundException);
            }
        });
        sInstalledExceptionHandler = true;
    }

    AppCompatDelegateImpl(Context context, Window window, AppCompatCallback appCompatCallback) {
        this.mContext = context;
        this.mWindow = window;
        this.mAppCompatCallback = appCompatCallback;
        Window.Callback callback = window.getCallback();
        this.mOriginalWindowCallback = callback;
        if (callback instanceof AppCompatWindowCallback) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(this, callback);
        this.mAppCompatWindowCallback = appCompatWindowCallback;
        this.mWindow.setCallback(appCompatWindowCallback);
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, (AttributeSet) null, sWindowBackgroundStyleable);
        Drawable drawableIfKnown = tintTypedArrayObtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            this.mWindow.setBackgroundDrawable(drawableIfKnown);
        }
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    private void applyFixedSizeWindow() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.mSubDecor.findViewById(R.id.content);
        View decorView = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(C0287R.styleable.AppCompatTheme);
        typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            typedArrayObtainStyledAttributes.getValue(C0287R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        typedArrayObtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    private ViewGroup createSubDecor() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ViewGroup viewGroup;
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(C0287R.styleable.AppCompatTheme);
        if (!typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.AppCompatTheme_windowActionBar)) {
            typedArrayObtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AppCompatTheme_windowNoTitle, false)) {
            requestWindowFeature(1);
        } else if (typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AppCompatTheme_windowActionBar, false)) {
            requestWindowFeature(108);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            requestWindowFeature(109);
        }
        if (typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            requestWindowFeature(10);
        }
        this.mIsFloating = typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AppCompatTheme_android_windowIsFloating, false);
        typedArrayObtainStyledAttributes.recycle();
        this.mWindow.getDecorView();
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        if (this.mWindowNoTitle) {
            viewGroup = this.mOverlayActionMode ? (ViewGroup) layoutInflaterFrom.inflate(C0287R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup) null) : (ViewGroup) layoutInflaterFrom.inflate(C0287R.layout.abc_screen_simple, (ViewGroup) null);
            if (Build.VERSION.SDK_INT >= 21) {
                ViewCompat.setOnApplyWindowInsetsListener(viewGroup, new OnApplyWindowInsetsListener(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.3
                    final AppCompatDelegateImpl this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // android.support.v4.view.OnApplyWindowInsetsListener
                    public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                        int iUpdateStatusGuard = this.this$0.updateStatusGuard(systemWindowInsetTop);
                        WindowInsetsCompat windowInsetsCompatReplaceSystemWindowInsets = windowInsetsCompat;
                        if (systemWindowInsetTop != iUpdateStatusGuard) {
                            windowInsetsCompatReplaceSystemWindowInsets = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), iUpdateStatusGuard, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                        }
                        return ViewCompat.onApplyWindowInsets(view, windowInsetsCompatReplaceSystemWindowInsets);
                    }
                });
            } else {
                ((FitWindowsViewGroup) viewGroup).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.4
                    final AppCompatDelegateImpl this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // android.support.v7.widget.FitWindowsViewGroup.OnFitSystemWindowsListener
                    public void onFitSystemWindows(Rect rect) {
                        rect.top = this.this$0.updateStatusGuard(rect.top);
                    }
                });
            }
        } else if (this.mIsFloating) {
            viewGroup = (ViewGroup) layoutInflaterFrom.inflate(C0287R.layout.abc_dialog_title_material, (ViewGroup) null);
            this.mOverlayActionBar = false;
            this.mHasActionBar = false;
        } else if (this.mHasActionBar) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(C0287R.attr.actionBarTheme, typedValue, true);
            ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(typedValue.resourceId != 0 ? new ContextThemeWrapper(this.mContext, typedValue.resourceId) : this.mContext).inflate(C0287R.layout.abc_screen_toolbar, (ViewGroup) null);
            DecorContentParent decorContentParent = (DecorContentParent) viewGroup2.findViewById(C0287R.id.decor_content_parent);
            this.mDecorContentParent = decorContentParent;
            decorContentParent.setWindowCallback(getWindowCallback());
            if (this.mOverlayActionBar) {
                this.mDecorContentParent.initFeature(109);
            }
            if (this.mFeatureProgress) {
                this.mDecorContentParent.initFeature(2);
            }
            viewGroup = viewGroup2;
            if (this.mFeatureIndeterminateProgress) {
                this.mDecorContentParent.initFeature(5);
                viewGroup = viewGroup2;
            }
        } else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
        }
        if (this.mDecorContentParent == null) {
            this.mTitleView = (TextView) viewGroup.findViewById(C0287R.id.title);
        }
        ViewUtils.makeOptionalFitsSystemWindows(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(C0287R.id.action_bar_activity_content);
        ViewGroup viewGroup3 = (ViewGroup) this.mWindow.findViewById(R.id.content);
        if (viewGroup3 != null) {
            while (viewGroup3.getChildCount() > 0) {
                View childAt = viewGroup3.getChildAt(0);
                viewGroup3.removeViewAt(0);
                contentFrameLayout.addView(childAt);
            }
            viewGroup3.setId(-1);
            contentFrameLayout.setId(R.id.content);
            if (viewGroup3 instanceof FrameLayout) {
                ((FrameLayout) viewGroup3).setForeground(null);
            }
        }
        this.mWindow.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new ContentFrameLayout.OnAttachListener(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.5
            final AppCompatDelegateImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.ContentFrameLayout.OnAttachListener
            public void onAttachedFromWindow() {
            }

            @Override // android.support.v7.widget.ContentFrameLayout.OnAttachListener
            public void onDetachedFromWindow() {
                this.this$0.dismissPopups();
            }
        });
        return viewGroup;
    }

    private void ensureAutoNightModeManager() {
        if (this.mAutoNightModeManager == null) {
            this.mAutoNightModeManager = new AutoNightModeManager(this, TwilightManager.getInstance(this.mContext));
        }
    }

    private void ensureSubDecor() {
        if (this.mSubDecorInstalled) {
            return;
        }
        this.mSubDecor = createSubDecor();
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null) {
                decorContentParent.setWindowTitle(title);
            } else if (peekSupportActionBar() != null) {
                peekSupportActionBar().setWindowTitle(title);
            } else {
                TextView textView = this.mTitleView;
                if (textView != null) {
                    textView.setText(title);
                }
            }
        }
        applyFixedSizeWindow();
        onSubDecorInstalled(this.mSubDecor);
        this.mSubDecorInstalled = true;
        PanelFeatureState panelState = getPanelState(0, false);
        if (this.mIsDestroyed) {
            return;
        }
        if (panelState == null || panelState.menu == null) {
            invalidatePanelMenu(108);
        }
    }

    private int getNightMode() {
        int defaultNightMode = this.mLocalNightMode;
        if (defaultNightMode == -100) {
            defaultNightMode = getDefaultNightMode();
        }
        return defaultNightMode;
    }

    private void initWindowDecorActionBar() {
        ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            Window.Callback callback = this.mOriginalWindowCallback;
            if (callback instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity) this.mOriginalWindowCallback, this.mOverlayActionBar);
            } else if (callback instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog) this.mOriginalWindowCallback);
            }
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                actionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
        }
    }

    private boolean initializePanelContent(PanelFeatureState panelFeatureState) {
        boolean z = true;
        if (panelFeatureState.createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
            return true;
        }
        if (panelFeatureState.menu == null) {
            return false;
        }
        if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(this);
        }
        panelFeatureState.shownPanelView = (View) panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
        if (panelFeatureState.shownPanelView == null) {
            z = false;
        }
        return z;
    }

    private boolean initializePanelDecor(PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(getActionBarThemedContext());
        panelFeatureState.decorView = new ListMenuDecorView(this, panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean initializePanelMenu(android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState r6) {
        /*
            Method dump skipped, instructions count: 212
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.initializePanelMenu(android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState):boolean");
    }

    private void invalidatePanelMenu(int i) {
        this.mInvalidatePanelMenuFeatures = (1 << i) | this.mInvalidatePanelMenuFeatures;
        if (this.mInvalidatePanelMenuPosted) {
            return;
        }
        ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
        this.mInvalidatePanelMenuPosted = true;
    }

    private boolean onKeyDownPanel(int i, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        PanelFeatureState panelState = getPanelState(i, true);
        if (panelState.isOpen) {
            return false;
        }
        return preparePanel(panelState, keyEvent);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean onKeyUpPanel(int r5, android.view.KeyEvent r6) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.onKeyUpPanel(int, android.view.KeyEvent):boolean");
    }

    private void openPanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        int i;
        ViewGroup.LayoutParams layoutParams;
        if (panelFeatureState.isOpen || this.mIsDestroyed) {
            return;
        }
        if (panelFeatureState.featureId == 0) {
            if ((this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
                return;
            }
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, panelFeatureState.menu)) {
            closePanel(panelFeatureState, true);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager != null && preparePanel(panelFeatureState, keyEvent)) {
            if (panelFeatureState.decorView != null && !panelFeatureState.refreshDecorView) {
                if (panelFeatureState.createdPanelView != null && (layoutParams = panelFeatureState.createdPanelView.getLayoutParams()) != null && layoutParams.width == -1) {
                    i = -1;
                }
                panelFeatureState.isHandled = false;
                WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams(i, -2, panelFeatureState.f32x, panelFeatureState.f33y, PointerIconCompat.TYPE_HAND, 8519680, -3);
                layoutParams2.gravity = panelFeatureState.gravity;
                layoutParams2.windowAnimations = panelFeatureState.windowAnimations;
                windowManager.addView(panelFeatureState.decorView, layoutParams2);
                panelFeatureState.isOpen = true;
            }
            if (panelFeatureState.decorView == null) {
                if (!initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                    return;
                }
            } else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                panelFeatureState.decorView.removeAllViews();
            }
            if (!initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                return;
            }
            ViewGroup.LayoutParams layoutParams3 = panelFeatureState.shownPanelView.getLayoutParams();
            ViewGroup.LayoutParams layoutParams4 = layoutParams3;
            if (layoutParams3 == null) {
                layoutParams4 = new ViewGroup.LayoutParams(-2, -2);
            }
            panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
            ViewParent parent = panelFeatureState.shownPanelView.getParent();
            if (parent != null && (parent instanceof ViewGroup)) {
                ((ViewGroup) parent).removeView(panelFeatureState.shownPanelView);
            }
            panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams4);
            if (!panelFeatureState.shownPanelView.hasFocus()) {
                panelFeatureState.shownPanelView.requestFocus();
            }
            i = -2;
            panelFeatureState.isHandled = false;
            WindowManager.LayoutParams layoutParams22 = new WindowManager.LayoutParams(i, -2, panelFeatureState.f32x, panelFeatureState.f33y, PointerIconCompat.TYPE_HAND, 8519680, -3);
            layoutParams22.gravity = panelFeatureState.gravity;
            layoutParams22.windowAnimations = panelFeatureState.windowAnimations;
            windowManager.addView(panelFeatureState.decorView, layoutParams22);
            panelFeatureState.isOpen = true;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean performPanelShortcut(android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState r6, int r7, android.view.KeyEvent r8, int r9) {
        /*
            r5 = this;
            r0 = r8
            boolean r0 = r0.isSystem()
            r10 = r0
            r0 = 0
            r11 = r0
            r0 = r10
            if (r0 == 0) goto L10
            r0 = 0
            return r0
        L10:
            r0 = r6
            boolean r0 = r0.isPrepared
            if (r0 != 0) goto L24
            r0 = r11
            r10 = r0
            r0 = r5
            r1 = r6
            r2 = r8
            boolean r0 = r0.preparePanel(r1, r2)
            if (r0 == 0) goto L3c
        L24:
            r0 = r11
            r10 = r0
            r0 = r6
            android.support.v7.view.menu.MenuBuilder r0 = r0.menu
            if (r0 == 0) goto L3c
            r0 = r6
            android.support.v7.view.menu.MenuBuilder r0 = r0.menu
            r1 = r7
            r2 = r8
            r3 = r9
            boolean r0 = r0.performShortcut(r1, r2, r3)
            r10 = r0
        L3c:
            r0 = r10
            if (r0 == 0) goto L55
            r0 = r9
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L55
            r0 = r5
            android.support.v7.widget.DecorContentParent r0 = r0.mDecorContentParent
            if (r0 != 0) goto L55
            r0 = r5
            r1 = r6
            r2 = 1
            r0.closePanel(r1, r2)
        L55:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.performPanelShortcut(android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState, int, android.view.KeyEvent, int):boolean");
    }

    private boolean preparePanel(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        DecorContentParent decorContentParent;
        DecorContentParent decorContentParent2;
        DecorContentParent decorContentParent3;
        if (this.mIsDestroyed) {
            return false;
        }
        if (panelFeatureState.isPrepared) {
            return true;
        }
        PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
        if (panelFeatureState2 != null && panelFeatureState2 != panelFeatureState) {
            closePanel(panelFeatureState2, false);
        }
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null) {
            panelFeatureState.createdPanelView = windowCallback.onCreatePanelView(panelFeatureState.featureId);
        }
        boolean z = panelFeatureState.featureId == 0 || panelFeatureState.featureId == 108;
        if (z && (decorContentParent3 = this.mDecorContentParent) != null) {
            decorContentParent3.setMenuPrepared();
        }
        if (panelFeatureState.createdPanelView == null && (!z || !(peekSupportActionBar() instanceof ToolbarActionBar))) {
            if (panelFeatureState.menu == null || panelFeatureState.refreshMenuContent) {
                if (panelFeatureState.menu == null && (!initializePanelMenu(panelFeatureState) || panelFeatureState.menu == null)) {
                    return false;
                }
                if (z && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(this);
                    }
                    this.mDecorContentParent.setMenu(panelFeatureState.menu, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.stopDispatchingItemsChanged();
                if (!windowCallback.onCreatePanelMenu(panelFeatureState.featureId, panelFeatureState.menu)) {
                    panelFeatureState.setMenu(null);
                    if (!z || (decorContentParent = this.mDecorContentParent) == null) {
                        return false;
                    }
                    decorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    return false;
                }
                panelFeatureState.refreshMenuContent = false;
            }
            panelFeatureState.menu.stopDispatchingItemsChanged();
            if (panelFeatureState.frozenActionViewState != null) {
                panelFeatureState.menu.restoreActionViewStates(panelFeatureState.frozenActionViewState);
                panelFeatureState.frozenActionViewState = null;
            }
            if (!windowCallback.onPreparePanel(0, panelFeatureState.createdPanelView, panelFeatureState.menu)) {
                if (z && (decorContentParent2 = this.mDecorContentParent) != null) {
                    decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                }
                panelFeatureState.menu.startDispatchingItemsChanged();
                return false;
            }
            panelFeatureState.qwertyMode = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            panelFeatureState.menu.setQwertyMode(panelFeatureState.qwertyMode);
            panelFeatureState.menu.startDispatchingItemsChanged();
        }
        panelFeatureState.isPrepared = true;
        panelFeatureState.isHandled = false;
        this.mPreparedPanel = panelFeatureState;
        return true;
    }

    private void reopenMenu(MenuBuilder menuBuilder, boolean z) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent == null || !decorContentParent.canShowOverflowMenu() || (ViewConfiguration.get(this.mContext).hasPermanentMenuKey() && !this.mDecorContentParent.isOverflowMenuShowPending())) {
            PanelFeatureState panelState = getPanelState(0, true);
            panelState.refreshDecorView = true;
            closePanel(panelState, false);
            openPanel(panelState, null);
            return;
        }
        Window.Callback windowCallback = getWindowCallback();
        if (this.mDecorContentParent.isOverflowMenuShowing() && z) {
            this.mDecorContentParent.hideOverflowMenu();
            if (this.mIsDestroyed) {
                return;
            }
            windowCallback.onPanelClosed(108, getPanelState(0, true).menu);
            return;
        }
        if (windowCallback == null || this.mIsDestroyed) {
            return;
        }
        if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
        }
        PanelFeatureState panelState2 = getPanelState(0, true);
        if (panelState2.menu == null || panelState2.refreshMenuContent || !windowCallback.onPreparePanel(0, panelState2.createdPanelView, panelState2.menu)) {
            return;
        }
        windowCallback.onMenuOpened(108, panelState2.menu);
        this.mDecorContentParent.showOverflowMenu();
    }

    private int sanitizeWindowFeatureId(int i) {
        if (i == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        }
        int i2 = i;
        if (i == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            i2 = 109;
        }
        return i2;
    }

    private boolean shouldInheritContext(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.mWindow.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || ViewCompat.isAttachedToWindow((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    private boolean shouldRecreateOnNightModeChange() {
        boolean z = false;
        if (!this.mApplyDayNightCalled) {
            return false;
        }
        Context context = this.mContext;
        if (!(context instanceof Activity)) {
            return false;
        }
        try {
            if ((context.getPackageManager().getActivityInfo(new ComponentName(this.mContext, this.mContext.getClass()), 0).configChanges & 512) == 0) {
                z = true;
            }
            return z;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e);
            return true;
        }
    }

    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private boolean updateForNightMode(int i) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        Resources resources = this.mContext.getResources();
        Configuration configuration = resources.getConfiguration();
        int i2 = configuration.uiMode;
        int i3 = i == 2 ? 32 : 16;
        if ((i2 & 48) == i3) {
            return false;
        }
        if (shouldRecreateOnNightModeChange()) {
            ((Activity) this.mContext).recreate();
            return true;
        }
        Configuration configuration2 = new Configuration(configuration);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration2.uiMode = i3 | (configuration2.uiMode & (-49));
        resources.updateConfiguration(configuration2, displayMetrics);
        if (Build.VERSION.SDK_INT >= 26) {
            return true;
        }
        ResourcesFlusher.flush(resources);
        return true;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ((ViewGroup) this.mSubDecor.findViewById(R.id.content)).addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean applyDayNight() {
        int nightMode = getNightMode();
        int iMapNightMode = mapNightMode(nightMode);
        boolean zUpdateForNightMode = iMapNightMode != -1 ? updateForNightMode(iMapNightMode) : false;
        if (nightMode == 0) {
            ensureAutoNightModeManager();
            this.mAutoNightModeManager.setup();
        }
        this.mApplyDayNightCalled = true;
        return zUpdateForNightMode;
    }

    void callOnPanelClosed(int i, PanelFeatureState panelFeatureState, Menu menu) {
        PanelFeatureState panelFeatureState2 = panelFeatureState;
        Menu menu2 = menu;
        if (menu == null) {
            PanelFeatureState panelFeatureState3 = panelFeatureState;
            if (panelFeatureState == null) {
                panelFeatureState3 = panelFeatureState;
                if (i >= 0) {
                    PanelFeatureState[] panelFeatureStateArr = this.mPanels;
                    panelFeatureState3 = panelFeatureState;
                    if (i < panelFeatureStateArr.length) {
                        panelFeatureState3 = panelFeatureStateArr[i];
                    }
                }
            }
            panelFeatureState2 = panelFeatureState3;
            menu2 = menu;
            if (panelFeatureState3 != null) {
                menu2 = panelFeatureState3.menu;
                panelFeatureState2 = panelFeatureState3;
            }
        }
        if ((panelFeatureState2 == null || panelFeatureState2.isOpen) && !this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(i, menu2);
        }
    }

    void checkCloseActionMenu(MenuBuilder menuBuilder) {
        if (this.mClosingActionMenu) {
            return;
        }
        this.mClosingActionMenu = true;
        this.mDecorContentParent.dismissPopups();
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback != null && !this.mIsDestroyed) {
            windowCallback.onPanelClosed(108, menuBuilder);
        }
        this.mClosingActionMenu = false;
    }

    void closePanel(int i) {
        closePanel(getPanelState(i, true), true);
    }

    void closePanel(PanelFeatureState panelFeatureState, boolean z) {
        DecorContentParent decorContentParent;
        if (z && panelFeatureState.featureId == 0 && (decorContentParent = this.mDecorContentParent) != null && decorContentParent.isOverflowMenuShowing()) {
            checkCloseActionMenu(panelFeatureState.menu);
            return;
        }
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        if (windowManager != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
            windowManager.removeView(panelFeatureState.decorView);
            if (z) {
                callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
            }
        }
        panelFeatureState.isPrepared = false;
        panelFeatureState.isHandled = false;
        panelFeatureState.isOpen = false;
        panelFeatureState.shownPanelView = null;
        panelFeatureState.refreshDecorView = true;
        if (this.mPreparedPanel == panelFeatureState) {
            this.mPreparedPanel = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.v7.app.AppCompatDelegate
    public View createView(View view, String str, Context context, AttributeSet attributeSet) {
        boolean zShouldInheritContext = false;
        if (this.mAppCompatViewInflater == null) {
            String string = this.mContext.obtainStyledAttributes(C0287R.styleable.AppCompatTheme).getString(C0287R.styleable.AppCompatTheme_viewInflaterClass);
            if (string == null || AppCompatViewInflater.class.getName().equals(string)) {
                this.mAppCompatViewInflater = new AppCompatViewInflater();
            } else {
                try {
                    this.mAppCompatViewInflater = (AppCompatViewInflater) Class.forName(string).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (Throwable th) {
                    Log.i("AppCompatDelegate", "Failed to instantiate custom view inflater " + string + ". Falling back to default.", th);
                    this.mAppCompatViewInflater = new AppCompatViewInflater();
                }
            }
        }
        if (!IS_PRE_LOLLIPOP) {
            zShouldInheritContext = false;
        } else if (!(attributeSet instanceof XmlPullParser)) {
            zShouldInheritContext = shouldInheritContext((ViewParent) view);
        } else if (((XmlPullParser) attributeSet).getDepth() > 1) {
            zShouldInheritContext = true;
        }
        return this.mAppCompatViewInflater.createView(view, str, context, attributeSet, zShouldInheritContext, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
    }

    void dismissPopups() {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.dismissPopups();
        }
        if (this.mActionModePopup != null) {
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            if (this.mActionModePopup.isShowing()) {
                try {
                    this.mActionModePopup.dismiss();
                } catch (IllegalArgumentException e) {
                }
            }
            this.mActionModePopup = null;
        }
        endOnGoingFadeAnimation();
        PanelFeatureState panelState = getPanelState(0, false);
        if (panelState == null || panelState.menu == null) {
            return;
        }
        panelState.menu.close();
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent) {
        View decorView;
        Window.Callback callback = this.mOriginalWindowCallback;
        boolean z = true;
        if (((callback instanceof KeyEventDispatcher.Component) || (callback instanceof AppCompatDialog)) && (decorView = this.mWindow.getDecorView()) != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() != 0) {
            z = false;
        }
        return z ? onKeyDown(keyCode, keyEvent) : onKeyUp(keyCode, keyEvent);
    }

    void doInvalidatePanelMenu(int i) {
        PanelFeatureState panelState;
        PanelFeatureState panelState2 = getPanelState(i, true);
        if (panelState2.menu != null) {
            Bundle bundle = new Bundle();
            panelState2.menu.saveActionViewStates(bundle);
            if (bundle.size() > 0) {
                panelState2.frozenActionViewState = bundle;
            }
            panelState2.menu.stopDispatchingItemsChanged();
            panelState2.menu.clear();
        }
        panelState2.refreshMenuContent = true;
        panelState2.refreshDecorView = true;
        if ((i != 108 && i != 0) || this.mDecorContentParent == null || (panelState = getPanelState(0, false)) == null) {
            return;
        }
        panelState.isPrepared = false;
        preparePanel(panelState, null);
    }

    void endOnGoingFadeAnimation() {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mFadeAnim;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
        }
    }

    PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panelFeatureStateArr = this.mPanels;
        int length = panelFeatureStateArr != null ? panelFeatureStateArr.length : 0;
        for (int i = 0; i < length; i++) {
            PanelFeatureState panelFeatureState = panelFeatureStateArr[i];
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public <T extends View> T findViewById(int i) {
        ensureSubDecor();
        return (T) this.mWindow.findViewById(i);
    }

    final Context getActionBarThemedContext() {
        ActionBar supportActionBar = getSupportActionBar();
        Context themedContext = supportActionBar != null ? supportActionBar.getThemedContext() : null;
        Context context = themedContext;
        if (themedContext == null) {
            context = this.mContext;
        }
        return context;
    }

    final AutoNightModeManager getAutoNightModeManager() {
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl(this);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            this.mMenuInflater = new SupportMenuInflater(actionBar != null ? actionBar.getThemedContext() : this.mContext);
        }
        return this.mMenuInflater;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState getPanelState(int r7, boolean r8) {
        /*
            r6 = this;
            r0 = r6
            android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState[] r0 = r0.mPanels
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L15
            r0 = r10
            r9 = r0
            r0 = r10
            int r0 = r0.length
            r1 = r7
            if (r0 > r1) goto L31
        L15:
            r0 = r7
            r1 = 1
            int r0 = r0 + r1
            android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState[] r0 = new android.support.v7.app.AppCompatDelegateImpl.PanelFeatureState[r0]
            r9 = r0
            r0 = r10
            if (r0 == 0) goto L2c
            r0 = r10
            r1 = 0
            r2 = r9
            r3 = 0
            r4 = r10
            int r4 = r4.length
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)
        L2c:
            r0 = r6
            r1 = r9
            r0.mPanels = r1
        L31:
            r0 = r9
            r1 = r7
            r0 = r0[r1]
            r11 = r0
            r0 = r11
            r10 = r0
            r0 = r11
            if (r0 != 0) goto L4e
            android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState r0 = new android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            r10 = r0
            r0 = r9
            r1 = r7
            r2 = r10
            r0[r1] = r2
        L4e:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatDelegateImpl.getPanelState(int, boolean):android.support.v7.app.AppCompatDelegateImpl$PanelFeatureState");
    }

    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public ActionBar getSupportActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    final CharSequence getTitle() {
        Window.Callback callback = this.mOriginalWindowCallback;
        return callback instanceof Activity ? ((Activity) callback).getTitle() : this.mTitle;
    }

    final Window.Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean hasWindowFeature(int i) {
        int iSanitizeWindowFeatureId = sanitizeWindowFeatureId(i);
        boolean z = true;
        if (!(iSanitizeWindowFeatureId != 1 ? iSanitizeWindowFeatureId != 2 ? iSanitizeWindowFeatureId != 5 ? iSanitizeWindowFeatureId != 10 ? iSanitizeWindowFeatureId != 108 ? iSanitizeWindowFeatureId != 109 ? false : this.mOverlayActionBar : this.mHasActionBar : this.mOverlayActionMode : this.mFeatureIndeterminateProgress : this.mFeatureProgress : this.mWindowNoTitle)) {
            z = this.mWindow.hasFeature(i);
        }
        return z;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void installViewFactory() throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        if (layoutInflaterFrom.getFactory() == null) {
            LayoutInflaterCompat.setFactory2(layoutInflaterFrom, this);
        } else {
            if (layoutInflaterFrom.getFactory2() instanceof AppCompatDelegateImpl) {
                return;
            }
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void invalidateOptionsMenu() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null || !supportActionBar.invalidateOptionsMenu()) {
            invalidatePanelMenu(0);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }

    int mapNightMode(int i) {
        if (i == -100) {
            return -1;
        }
        if (i != 0) {
            return i;
        }
        if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager) this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
            return -1;
        }
        ensureAutoNightModeManager();
        return this.mAutoNightModeManager.getApplyableNightMode();
    }

    boolean onBackPressed() {
        android.support.v7.view.ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
            return true;
        }
        ActionBar supportActionBar = getSupportActionBar();
        return supportActionBar != null && supportActionBar.collapseActionView();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onConfigurationChanged(Configuration configuration) {
        ActionBar supportActionBar;
        if (this.mHasActionBar && this.mSubDecorInstalled && (supportActionBar = getSupportActionBar()) != null) {
            supportActionBar.onConfigurationChanged(configuration);
        }
        AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
        applyDayNight();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onCreate(Bundle bundle) {
        Window.Callback callback = this.mOriginalWindowCallback;
        if (callback instanceof Activity) {
            String parentActivityName = null;
            try {
                parentActivityName = NavUtils.getParentActivityName((Activity) callback);
            } catch (IllegalArgumentException e) {
            }
            if (parentActivityName != null) {
                ActionBar actionBarPeekSupportActionBar = peekSupportActionBar();
                if (actionBarPeekSupportActionBar == null) {
                    this.mEnableDefaultActionBarUp = true;
                } else {
                    actionBarPeekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
                }
            }
        }
        if (bundle == null || this.mLocalNightMode != -100) {
            return;
        }
        this.mLocalNightMode = bundle.getInt(KEY_LOCAL_NIGHT_MODE, -100);
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return createView(view, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onDestroy() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
        }
        this.mIsDestroyed = true;
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.onDestroy();
        }
        AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
    }

    boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = true;
        if (i != 4) {
            if (i != 82) {
                return false;
            }
            onKeyDownPanel(0, keyEvent);
            return true;
        }
        if ((keyEvent.getFlags() & 128) == 0) {
            z = false;
        }
        this.mLongPressBackDown = z;
        return false;
    }

    boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && supportActionBar.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        PanelFeatureState panelFeatureState = this.mPreparedPanel;
        if (panelFeatureState != null && performPanelShortcut(panelFeatureState, keyEvent.getKeyCode(), keyEvent, 1)) {
            PanelFeatureState panelFeatureState2 = this.mPreparedPanel;
            if (panelFeatureState2 == null) {
                return true;
            }
            panelFeatureState2.isHandled = true;
            return true;
        }
        if (this.mPreparedPanel != null) {
            return false;
        }
        PanelFeatureState panelState = getPanelState(0, true);
        preparePanel(panelState, keyEvent);
        boolean zPerformPanelShortcut = performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
        panelState.isPrepared = false;
        return zPerformPanelShortcut;
    }

    boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 4) {
            if (i != 82) {
                return false;
            }
            onKeyUpPanel(0, keyEvent);
            return true;
        }
        boolean z = this.mLongPressBackDown;
        this.mLongPressBackDown = false;
        PanelFeatureState panelState = getPanelState(0, false);
        if (panelState == null || !panelState.isOpen) {
            return onBackPressed();
        }
        if (z) {
            return true;
        }
        closePanel(panelState, true);
        return true;
    }

    @Override // android.support.v7.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        PanelFeatureState panelFeatureStateFindMenuPanel;
        Window.Callback windowCallback = getWindowCallback();
        if (windowCallback == null || this.mIsDestroyed || (panelFeatureStateFindMenuPanel = findMenuPanel(menuBuilder.getRootMenu())) == null) {
            return false;
        }
        return windowCallback.onMenuItemSelected(panelFeatureStateFindMenuPanel.featureId, menuItem);
    }

    @Override // android.support.v7.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(menuBuilder, true);
    }

    void onMenuOpened(int i) {
        ActionBar supportActionBar;
        if (i != 108 || (supportActionBar = getSupportActionBar()) == null) {
            return;
        }
        supportActionBar.dispatchMenuVisibilityChanged(true);
    }

    void onPanelClosed(int i) {
        if (i == 108) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
                return;
            }
            return;
        }
        if (i == 0) {
            PanelFeatureState panelState = getPanelState(i, true);
            if (panelState.isOpen) {
                closePanel(panelState, false);
            }
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onPostCreate(Bundle bundle) {
        ensureSubDecor();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onPostResume() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(true);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onSaveInstanceState(Bundle bundle) {
        int i = this.mLocalNightMode;
        if (i != -100) {
            bundle.putInt(KEY_LOCAL_NIGHT_MODE, i);
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onStart() {
        applyDayNight();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void onStop() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
        AutoNightModeManager autoNightModeManager = this.mAutoNightModeManager;
        if (autoNightModeManager != null) {
            autoNightModeManager.cleanup();
        }
    }

    void onSubDecorInstalled(ViewGroup viewGroup) {
    }

    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public boolean requestWindowFeature(int i) {
        int iSanitizeWindowFeatureId = sanitizeWindowFeatureId(i);
        if (this.mWindowNoTitle && iSanitizeWindowFeatureId == 108) {
            return false;
        }
        if (this.mHasActionBar && iSanitizeWindowFeatureId == 1) {
            this.mHasActionBar = false;
        }
        if (iSanitizeWindowFeatureId == 1) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            return true;
        }
        if (iSanitizeWindowFeatureId == 2) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            return true;
        }
        if (iSanitizeWindowFeatureId == 5) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureIndeterminateProgress = true;
            return true;
        }
        if (iSanitizeWindowFeatureId == 10) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionMode = true;
            return true;
        }
        if (iSanitizeWindowFeatureId == 108) {
            throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            return true;
        }
        if (iSanitizeWindowFeatureId != 109) {
            return this.mWindow.requestFeature(iSanitizeWindowFeatureId);
        }
        throwFeatureRequestIfSubDecorInstalled();
        this.mOverlayActionBar = true;
        return true;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(int i) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(View view) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ensureSubDecor();
        ViewGroup viewGroup = (ViewGroup) this.mSubDecor.findViewById(R.id.content);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setHandleNativeActionModesEnabled(boolean z) {
        this.mHandleNativeActionModes = z;
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setLocalNightMode(int i) {
        if (i != -1 && i != 0 && i != 1 && i != 2) {
            Log.i("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
        } else if (this.mLocalNightMode != i) {
            this.mLocalNightMode = i;
            if (this.mApplyDayNightCalled) {
                applyDayNight();
            }
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public void setSupportActionBar(Toolbar toolbar) {
        if (this.mOriginalWindowCallback instanceof Activity) {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar instanceof WindowDecorActionBar) {
                throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
            }
            this.mMenuInflater = null;
            if (supportActionBar != null) {
                supportActionBar.onDestroy();
            }
            if (toolbar != null) {
                ToolbarActionBar toolbarActionBar = new ToolbarActionBar(toolbar, ((Activity) this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = toolbarActionBar;
                this.mWindow.setCallback(toolbarActionBar.getWrappedWindowCallback());
            } else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            invalidateOptionsMenu();
        }
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public final void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setWindowTitle(charSequence);
            return;
        }
        if (peekSupportActionBar() != null) {
            peekSupportActionBar().setWindowTitle(charSequence);
            return;
        }
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    final boolean shouldAnimateActionModeView() {
        ViewGroup viewGroup;
        return this.mSubDecorInstalled && (viewGroup = this.mSubDecor) != null && ViewCompat.isLaidOut(viewGroup);
    }

    @Override // android.support.v7.app.AppCompatDelegate
    public android.support.v7.view.ActionMode startSupportActionMode(ActionMode.Callback callback) {
        AppCompatCallback appCompatCallback;
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        android.support.v7.view.ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionModeCallbackWrapperV9 actionModeCallbackWrapperV9 = new ActionModeCallbackWrapperV9(this, callback);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            android.support.v7.view.ActionMode actionModeStartActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV9);
            this.mActionMode = actionModeStartActionMode;
            if (actionModeStartActionMode != null && (appCompatCallback = this.mAppCompatCallback) != null) {
                appCompatCallback.onSupportActionModeStarted(actionModeStartActionMode);
            }
        }
        if (this.mActionMode == null) {
            this.mActionMode = startSupportActionModeFromWindow(actionModeCallbackWrapperV9);
        }
        return this.mActionMode;
    }

    android.support.v7.view.ActionMode startSupportActionModeFromWindow(ActionMode.Callback callback) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        android.support.v7.view.ActionMode actionModeOnWindowStartingSupportActionMode;
        Context contextThemeWrapper;
        AppCompatCallback appCompatCallback;
        endOnGoingFadeAnimation();
        android.support.v7.view.ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionMode.Callback actionModeCallbackWrapperV9 = callback;
        if (!(callback instanceof ActionModeCallbackWrapperV9)) {
            actionModeCallbackWrapperV9 = new ActionModeCallbackWrapperV9(this, callback);
        }
        AppCompatCallback appCompatCallback2 = this.mAppCompatCallback;
        if (appCompatCallback2 == null || this.mIsDestroyed) {
            actionModeOnWindowStartingSupportActionMode = null;
        } else {
            try {
                actionModeOnWindowStartingSupportActionMode = appCompatCallback2.onWindowStartingSupportActionMode(actionModeCallbackWrapperV9);
            } catch (AbstractMethodError e) {
            }
        }
        if (actionModeOnWindowStartingSupportActionMode != null) {
            this.mActionMode = actionModeOnWindowStartingSupportActionMode;
        } else {
            boolean z = true;
            if (this.mActionModeView == null) {
                if (this.mIsFloating) {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = this.mContext.getTheme();
                    theme.resolveAttribute(C0287R.attr.actionBarTheme, typedValue, true);
                    if (typedValue.resourceId != 0) {
                        Resources.Theme themeNewTheme = this.mContext.getResources().newTheme();
                        themeNewTheme.setTo(theme);
                        themeNewTheme.applyStyle(typedValue.resourceId, true);
                        contextThemeWrapper = new ContextThemeWrapper(this.mContext, 0);
                        contextThemeWrapper.getTheme().setTo(themeNewTheme);
                    } else {
                        contextThemeWrapper = this.mContext;
                    }
                    this.mActionModeView = new ActionBarContextView(contextThemeWrapper);
                    PopupWindow popupWindow = new PopupWindow(contextThemeWrapper, (AttributeSet) null, C0287R.attr.actionModePopupWindowStyle);
                    this.mActionModePopup = popupWindow;
                    PopupWindowCompat.setWindowLayoutType(popupWindow, 2);
                    this.mActionModePopup.setContentView(this.mActionModeView);
                    this.mActionModePopup.setWidth(-1);
                    contextThemeWrapper.getTheme().resolveAttribute(C0287R.attr.actionBarSize, typedValue, true);
                    this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(typedValue.data, contextThemeWrapper.getResources().getDisplayMetrics()));
                    this.mActionModePopup.setHeight(-2);
                    this.mShowActionModePopup = new RunnableC02766(this);
                } else {
                    ViewStubCompat viewStubCompat = (ViewStubCompat) this.mSubDecor.findViewById(C0287R.id.action_mode_bar_stub);
                    if (viewStubCompat != null) {
                        viewStubCompat.setLayoutInflater(LayoutInflater.from(getActionBarThemedContext()));
                        this.mActionModeView = (ActionBarContextView) viewStubCompat.inflate();
                    }
                }
            }
            if (this.mActionModeView != null) {
                endOnGoingFadeAnimation();
                this.mActionModeView.killMode();
                Context context = this.mActionModeView.getContext();
                ActionBarContextView actionBarContextView = this.mActionModeView;
                if (this.mActionModePopup != null) {
                    z = false;
                }
                StandaloneActionMode standaloneActionMode = new StandaloneActionMode(context, actionBarContextView, actionModeCallbackWrapperV9, z);
                if (actionModeCallbackWrapperV9.onCreateActionMode(standaloneActionMode, standaloneActionMode.getMenu())) {
                    standaloneActionMode.invalidate();
                    this.mActionModeView.initForMode(standaloneActionMode);
                    this.mActionMode = standaloneActionMode;
                    if (shouldAnimateActionModeView()) {
                        this.mActionModeView.setAlpha(0.0f);
                        ViewPropertyAnimatorCompat viewPropertyAnimatorCompatAlpha = ViewCompat.animate(this.mActionModeView).alpha(1.0f);
                        this.mFadeAnim = viewPropertyAnimatorCompatAlpha;
                        viewPropertyAnimatorCompatAlpha.setListener(new ViewPropertyAnimatorListenerAdapter(this) { // from class: android.support.v7.app.AppCompatDelegateImpl.7
                            final AppCompatDelegateImpl this$0;

                            {
                                this.this$0 = this;
                            }

                            @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                            public void onAnimationEnd(View view) {
                                this.this$0.mActionModeView.setAlpha(1.0f);
                                this.this$0.mFadeAnim.setListener(null);
                                this.this$0.mFadeAnim = null;
                            }

                            @Override // android.support.v4.view.ViewPropertyAnimatorListenerAdapter, android.support.v4.view.ViewPropertyAnimatorListener
                            public void onAnimationStart(View view) {
                                this.this$0.mActionModeView.setVisibility(0);
                                this.this$0.mActionModeView.sendAccessibilityEvent(32);
                                if (this.this$0.mActionModeView.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View) this.this$0.mActionModeView.getParent());
                                }
                            }
                        });
                    } else {
                        this.mActionModeView.setAlpha(1.0f);
                        this.mActionModeView.setVisibility(0);
                        this.mActionModeView.sendAccessibilityEvent(32);
                        if (this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View) this.mActionModeView.getParent());
                        }
                    }
                    if (this.mActionModePopup != null) {
                        this.mWindow.getDecorView().post(this.mShowActionModePopup);
                    }
                } else {
                    this.mActionMode = null;
                }
            }
        }
        android.support.v7.view.ActionMode actionMode2 = this.mActionMode;
        if (actionMode2 != null && (appCompatCallback = this.mAppCompatCallback) != null) {
            appCompatCallback.onSupportActionModeStarted(actionMode2);
        }
        return this.mActionMode;
    }

    int updateStatusGuard(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean z;
        int i2;
        boolean z2;
        boolean z3;
        boolean z4;
        ActionBarContextView actionBarContextView = this.mActionModeView;
        if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z = false;
            i2 = i;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mActionModeView.getLayoutParams();
            boolean z5 = true;
            if (this.mActionModeView.isShown()) {
                if (this.mTempRect1 == null) {
                    this.mTempRect1 = new Rect();
                    this.mTempRect2 = new Rect();
                }
                Rect rect = this.mTempRect1;
                Rect rect2 = this.mTempRect2;
                rect.set(0, i, 0, 0);
                ViewUtils.computeFitSystemWindows(this.mSubDecor, rect, rect2);
                if (marginLayoutParams.topMargin != (rect2.top == 0 ? i : 0)) {
                    marginLayoutParams.topMargin = i;
                    View view = this.mStatusGuard;
                    if (view == null) {
                        View view2 = new View(this.mContext);
                        this.mStatusGuard = view2;
                        view2.setBackgroundColor(this.mContext.getResources().getColor(C0287R.color.abc_input_method_navigation_guard));
                        this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup.LayoutParams(-1, i));
                    } else {
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        if (layoutParams.height != i) {
                            layoutParams.height = i;
                            this.mStatusGuard.setLayoutParams(layoutParams);
                        }
                    }
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (this.mStatusGuard == null) {
                    z5 = false;
                }
                int i3 = i;
                if (!this.mOverlayActionMode) {
                    i3 = i;
                    if (z5) {
                        i3 = 0;
                    }
                }
                boolean z6 = z4;
                z2 = z5;
                z3 = z6;
                i = i3;
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z2 = false;
                z3 = true;
            } else {
                z2 = false;
                z3 = false;
            }
            z = z2;
            i2 = i;
            if (z3) {
                this.mActionModeView.setLayoutParams(marginLayoutParams);
                z = z2;
                i2 = i;
            }
        }
        View view3 = this.mStatusGuard;
        if (view3 != null) {
            view3.setVisibility(z ? 0 : 8);
        }
        return i2;
    }
}
