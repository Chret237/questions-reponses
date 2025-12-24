package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ActivityChooserModel;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActivityChooserView.class */
public class ActivityChooserView extends ViewGroup implements ActivityChooserModel.ActivityChooserModelClient {
    private static final String LOG_TAG = "ActivityChooserView";
    private final View mActivityChooserContent;
    private final Drawable mActivityChooserContentBackground;
    final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    final FrameLayout mDefaultActivityButton;
    private final ImageView mDefaultActivityButtonImage;
    final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    int mInitialActivityCount;
    private boolean mIsAttachedToWindow;
    boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    final DataSetObserver mModelDataSetObserver;
    PopupWindow.OnDismissListener mOnDismissListener;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActivityChooserView$ActivityChooserViewAdapter.class */
    private class ActivityChooserViewAdapter extends BaseAdapter {
        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
        private static final int ITEM_VIEW_TYPE_COUNT = 3;
        private static final int ITEM_VIEW_TYPE_FOOTER = 1;
        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount = 4;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;
        final ActivityChooserView this$0;

        ActivityChooserViewAdapter(ActivityChooserView activityChooserView) {
            this.this$0 = activityChooserView;
        }

        public int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int activityCount = this.mDataModel.getActivityCount();
            int i = activityCount;
            if (!this.mShowDefaultActivity) {
                i = activityCount;
                if (this.mDataModel.getDefaultActivity() != null) {
                    i = activityCount - 1;
                }
            }
            int iMin = Math.min(i, this.mMaxActivityCount);
            int i2 = iMin;
            if (this.mShowFooterView) {
                i2 = iMin + 1;
            }
            return i2;
        }

        public ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }

        public ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }

        public int getHistorySize() {
            return this.mDataModel.getHistorySize();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            int itemViewType = getItemViewType(i);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    return null;
                }
                throw new IllegalArgumentException();
            }
            int i2 = i;
            if (!this.mShowDefaultActivity) {
                i2 = i;
                if (this.mDataModel.getDefaultActivity() != null) {
                    i2 = i + 1;
                }
            }
            return this.mDataModel.getActivity(i2);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i) {
            return (this.mShowFooterView && i == getCount() - 1) ? 1 : 0;
        }

        public boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0072  */
        @Override // android.widget.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public android.view.View getView(int r6, android.view.View r7, android.view.ViewGroup r8) {
            /*
                Method dump skipped, instructions count: 233
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 3;
        }

        public int measureContentWidth() {
            int i = this.mMaxActivityCount;
            this.mMaxActivityCount = MAX_ACTIVITY_COUNT_UNLIMITED;
            int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            int count = getCount();
            View view = null;
            int iMax = 0;
            for (int i2 = 0; i2 < count; i2++) {
                view = getView(i2, view, null);
                view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
                iMax = Math.max(iMax, view.getMeasuredWidth());
            }
            this.mMaxActivityCount = i;
            return iMax;
        }

        public void setDataModel(ActivityChooserModel activityChooserModel) {
            ActivityChooserModel dataModel = this.this$0.mAdapter.getDataModel();
            if (dataModel != null && this.this$0.isShown()) {
                dataModel.unregisterObserver(this.this$0.mModelDataSetObserver);
            }
            this.mDataModel = activityChooserModel;
            if (activityChooserModel != null && this.this$0.isShown()) {
                activityChooserModel.registerObserver(this.this$0.mModelDataSetObserver);
            }
            notifyDataSetChanged();
        }

        public void setMaxActivityCount(int i) {
            if (this.mMaxActivityCount != i) {
                this.mMaxActivityCount = i;
                notifyDataSetChanged();
            }
        }

        public void setShowDefaultActivity(boolean z, boolean z2) {
            if (this.mShowDefaultActivity == z && this.mHighlightDefaultActivity == z2) {
                return;
            }
            this.mShowDefaultActivity = z;
            this.mHighlightDefaultActivity = z2;
            notifyDataSetChanged();
        }

        public void setShowFooterView(boolean z) {
            if (this.mShowFooterView != z) {
                this.mShowFooterView = z;
                notifyDataSetChanged();
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActivityChooserView$Callbacks.class */
    private class Callbacks implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {
        final ActivityChooserView this$0;

        Callbacks(ActivityChooserView activityChooserView) {
            this.this$0 = activityChooserView;
        }

        private void notifyOnDismissListener() {
            if (this.this$0.mOnDismissListener != null) {
                this.this$0.mOnDismissListener.onDismiss();
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (view != this.this$0.mDefaultActivityButton) {
                if (view != this.this$0.mExpandActivityOverflowButton) {
                    throw new IllegalArgumentException();
                }
                this.this$0.mIsSelectingDefaultActivity = false;
                ActivityChooserView activityChooserView = this.this$0;
                activityChooserView.showPopupUnchecked(activityChooserView.mInitialActivityCount);
                return;
            }
            this.this$0.dismissPopup();
            Intent intentChooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(this.this$0.mAdapter.getDataModel().getActivityIndex(this.this$0.mAdapter.getDefaultActivity()));
            if (intentChooseActivity != null) {
                intentChooseActivity.addFlags(524288);
                this.this$0.getContext().startActivity(intentChooseActivity);
            }
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            notifyOnDismissListener();
            if (this.this$0.mProvider != null) {
                this.this$0.mProvider.subUiVisibilityChanged(false);
            }
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            int itemViewType = ((ActivityChooserViewAdapter) adapterView.getAdapter()).getItemViewType(i);
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    throw new IllegalArgumentException();
                }
                this.this$0.showPopupUnchecked(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
                return;
            }
            this.this$0.dismissPopup();
            if (this.this$0.mIsSelectingDefaultActivity) {
                if (i > 0) {
                    this.this$0.mAdapter.getDataModel().setDefaultActivity(i);
                    return;
                }
                return;
            }
            if (!this.this$0.mAdapter.getShowDefaultActivity()) {
                i++;
            }
            Intent intentChooseActivity = this.this$0.mAdapter.getDataModel().chooseActivity(i);
            if (intentChooseActivity != null) {
                intentChooseActivity.addFlags(524288);
                this.this$0.getContext().startActivity(intentChooseActivity);
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (view != this.this$0.mDefaultActivityButton) {
                throw new IllegalArgumentException();
            }
            if (this.this$0.mAdapter.getCount() <= 0) {
                return true;
            }
            this.this$0.mIsSelectingDefaultActivity = true;
            ActivityChooserView activityChooserView = this.this$0;
            activityChooserView.showPopupUnchecked(activityChooserView.mInitialActivityCount);
            return true;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ActivityChooserView$InnerLayout.class */
    public static class InnerLayout extends LinearLayout {
        private static final int[] TINT_ATTRS = {R.attr.background};

        public InnerLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, TINT_ATTRS);
            setBackgroundDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(0));
            tintTypedArrayObtainStyledAttributes.recycle();
        }
    }

    public ActivityChooserView(Context context) {
        this(context, null);
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mModelDataSetObserver = new DataSetObserver(this) { // from class: android.support.v7.widget.ActivityChooserView.1
            final ActivityChooserView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                this.this$0.mAdapter.notifyDataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                super.onInvalidated();
                this.this$0.mAdapter.notifyDataSetInvalidated();
            }
        };
        this.mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(this) { // from class: android.support.v7.widget.ActivityChooserView.2
            final ActivityChooserView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
                if (this.this$0.isShowingPopup()) {
                    if (!this.this$0.isShown()) {
                        this.this$0.getListPopupWindow().dismiss();
                        return;
                    }
                    this.this$0.getListPopupWindow().show();
                    if (this.this$0.mProvider != null) {
                        this.this$0.mProvider.subUiVisibilityChanged(true);
                    }
                }
            }
        };
        this.mInitialActivityCount = 4;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0287R.styleable.ActivityChooserView, i, 0);
        this.mInitialActivityCount = typedArrayObtainStyledAttributes.getInt(C0287R.styleable.ActivityChooserView_initialActivityCount, 4);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        typedArrayObtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(C0287R.layout.abc_activity_chooser_view, (ViewGroup) this, true);
        this.mCallbacks = new Callbacks(this);
        View viewFindViewById = findViewById(C0287R.id.activity_chooser_view_content);
        this.mActivityChooserContent = viewFindViewById;
        this.mActivityChooserContentBackground = viewFindViewById.getBackground();
        FrameLayout frameLayout = (FrameLayout) findViewById(C0287R.id.default_activity_button);
        this.mDefaultActivityButton = frameLayout;
        frameLayout.setOnClickListener(this.mCallbacks);
        this.mDefaultActivityButton.setOnLongClickListener(this.mCallbacks);
        this.mDefaultActivityButtonImage = (ImageView) this.mDefaultActivityButton.findViewById(C0287R.id.image);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(C0287R.id.expand_activities_button);
        frameLayout2.setOnClickListener(this.mCallbacks);
        frameLayout2.setAccessibilityDelegate(new View.AccessibilityDelegate(this) { // from class: android.support.v7.widget.ActivityChooserView.3
            final ActivityChooserView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCanOpenPopup(true);
            }
        });
        frameLayout2.setOnTouchListener(new ForwardingListener(this, frameLayout2) { // from class: android.support.v7.widget.ActivityChooserView.4
            final ActivityChooserView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.ForwardingListener
            public ShowableListMenu getPopup() {
                return this.this$0.getListPopupWindow();
            }

            @Override // android.support.v7.widget.ForwardingListener
            protected boolean onForwardingStarted() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
                this.this$0.showPopup();
                return true;
            }

            @Override // android.support.v7.widget.ForwardingListener
            protected boolean onForwardingStopped() {
                this.this$0.dismissPopup();
                return true;
            }
        });
        this.mExpandActivityOverflowButton = frameLayout2;
        ImageView imageView = (ImageView) frameLayout2.findViewById(C0287R.id.image);
        this.mExpandActivityOverflowButtonImage = imageView;
        imageView.setImageDrawable(drawable);
        ActivityChooserViewAdapter activityChooserViewAdapter = new ActivityChooserViewAdapter(this);
        this.mAdapter = activityChooserViewAdapter;
        activityChooserViewAdapter.registerDataSetObserver(new DataSetObserver(this) { // from class: android.support.v7.widget.ActivityChooserView.5
            final ActivityChooserView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                this.this$0.updateAppearance();
            }
        });
        Resources resources = context.getResources();
        this.mListPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(C0287R.dimen.abc_config_prefDialogWidth));
    }

    public boolean dismissPopup() {
        if (!isShowingPopup()) {
            return true;
        }
        getListPopupWindow().dismiss();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (!viewTreeObserver.isAlive()) {
            return true;
        }
        viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        return true;
    }

    public ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }

    ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
            this.mListPopupWindow = listPopupWindow;
            listPopupWindow.setAdapter(this.mAdapter);
            this.mListPopupWindow.setAnchorView(this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener(this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener(this.mCallbacks);
        }
        return this.mListPopupWindow;
    }

    public boolean isShowingPopup() {
        return getListPopupWindow().isShowing();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver(this.mModelDataSetObserver);
        }
        this.mIsAttachedToWindow = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver(this.mModelDataSetObserver);
        }
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (isShowingPopup()) {
            dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mActivityChooserContent.layout(0, 0, i3 - i, i4 - i2);
        if (isShowingPopup()) {
            return;
        }
        dismissPopup();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        View view = this.mActivityChooserContent;
        int iMakeMeasureSpec = i2;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824);
        }
        measureChild(view, i, iMakeMeasureSpec);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override // android.support.v7.widget.ActivityChooserModel.ActivityChooserModelClient
    public void setActivityChooserModel(ActivityChooserModel activityChooserModel) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.mAdapter.setDataModel(activityChooserModel);
        if (isShowingPopup()) {
            dismissPopup();
            showPopup();
        }
    }

    public void setDefaultActionButtonContentDescription(int i) {
        this.mDefaultActionButtonContentDescription = i;
    }

    public void setExpandActivityOverflowButtonContentDescription(int i) {
        this.mExpandActivityOverflowButtonImage.setContentDescription(getContext().getString(i));
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
    }

    public void setInitialActivityCount(int i) {
        this.mInitialActivityCount = i;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void setProvider(ActionProvider actionProvider) {
        this.mProvider = actionProvider;
    }

    public boolean showPopup() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (isShowingPopup() || !this.mIsAttachedToWindow) {
            return false;
        }
        this.mIsSelectingDefaultActivity = false;
        showPopupUnchecked(this.mInitialActivityCount);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void showPopupUnchecked(int i) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (this.mAdapter.getDataModel() == null) {
            throw new IllegalStateException("No data model. Did you call #setDataModel?");
        }
        getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        int i2 = this.mDefaultActivityButton.getVisibility() == 0 ? 1 : 0;
        int activityCount = this.mAdapter.getActivityCount();
        if (i == Integer.MAX_VALUE || activityCount <= i + i2) {
            this.mAdapter.setShowFooterView(false);
            this.mAdapter.setMaxActivityCount(i);
        } else {
            this.mAdapter.setShowFooterView(true);
            this.mAdapter.setMaxActivityCount(i - 1);
        }
        ListPopupWindow listPopupWindow = getListPopupWindow();
        if (listPopupWindow.isShowing()) {
            return;
        }
        if (this.mIsSelectingDefaultActivity || i2 == 0) {
            this.mAdapter.setShowDefaultActivity(true, i2);
        } else {
            this.mAdapter.setShowDefaultActivity(false, false);
        }
        listPopupWindow.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
        listPopupWindow.show();
        ActionProvider actionProvider = this.mProvider;
        if (actionProvider != null) {
            actionProvider.subUiVisibilityChanged(true);
        }
        listPopupWindow.getListView().setContentDescription(getContext().getString(C0287R.string.abc_activitychooserview_choose_application));
        listPopupWindow.getListView().setSelector(new ColorDrawable(0));
    }

    void updateAppearance() {
        if (this.mAdapter.getCount() > 0) {
            this.mExpandActivityOverflowButton.setEnabled(true);
        } else {
            this.mExpandActivityOverflowButton.setEnabled(false);
        }
        int activityCount = this.mAdapter.getActivityCount();
        int historySize = this.mAdapter.getHistorySize();
        if (activityCount == 1 || (activityCount > 1 && historySize > 0)) {
            this.mDefaultActivityButton.setVisibility(0);
            ResolveInfo defaultActivity = this.mAdapter.getDefaultActivity();
            PackageManager packageManager = getContext().getPackageManager();
            this.mDefaultActivityButtonImage.setImageDrawable(defaultActivity.loadIcon(packageManager));
            if (this.mDefaultActionButtonContentDescription != 0) {
                this.mDefaultActivityButton.setContentDescription(getContext().getString(this.mDefaultActionButtonContentDescription, defaultActivity.loadLabel(packageManager)));
            }
        } else {
            this.mDefaultActivityButton.setVisibility(8);
        }
        if (this.mDefaultActivityButton.getVisibility() == 0) {
            this.mActivityChooserContent.setBackgroundDrawable(this.mActivityChooserContentBackground);
        } else {
            this.mActivityChooserContent.setBackgroundDrawable(null);
        }
    }
}
