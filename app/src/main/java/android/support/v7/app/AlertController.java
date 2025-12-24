package android.support.v7.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.appcompat.C0287R;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.lang.ref.WeakReference;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController.class */
class AlertController {
    ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final int mButtonIconDimen;
    Button mButtonNegative;
    private Drawable mButtonNegativeIcon;
    Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    Button mButtonNeutral;
    private Drawable mButtonNeutralIcon;
    Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelSideLayout;
    Button mButtonPositive;
    private Drawable mButtonPositiveIcon;
    Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    private final Context mContext;
    private View mCustomTitleView;
    final AppCompatDialog mDialog;
    Handler mHandler;
    private Drawable mIcon;
    private ImageView mIconView;
    int mListItemLayout;
    int mListLayout;
    ListView mListView;
    private CharSequence mMessage;
    private TextView mMessageView;
    int mMultiChoiceItemLayout;
    NestedScrollView mScrollView;
    private boolean mShowTitle;
    int mSingleChoiceItemLayout;
    private CharSequence mTitle;
    private TextView mTitleView;
    private View mView;
    private int mViewLayoutResId;
    private int mViewSpacingBottom;
    private int mViewSpacingLeft;
    private int mViewSpacingRight;
    private int mViewSpacingTop;
    private final Window mWindow;
    private boolean mViewSpacingSpecified = false;
    private int mIconId = 0;
    int mCheckedItem = -1;
    private int mButtonPanelLayoutHint = 0;
    private final View.OnClickListener mButtonHandler = new View.OnClickListener(this) { // from class: android.support.v7.app.AlertController.1
        final AlertController this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Message messageObtain = (view != this.this$0.mButtonPositive || this.this$0.mButtonPositiveMessage == null) ? (view != this.this$0.mButtonNegative || this.this$0.mButtonNegativeMessage == null) ? (view != this.this$0.mButtonNeutral || this.this$0.mButtonNeutralMessage == null) ? null : Message.obtain(this.this$0.mButtonNeutralMessage) : Message.obtain(this.this$0.mButtonNegativeMessage) : Message.obtain(this.this$0.mButtonPositiveMessage);
            if (messageObtain != null) {
                messageObtain.sendToTarget();
            }
            this.this$0.mHandler.obtainMessage(1, this.this$0.mDialog).sendToTarget();
        }
    };

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController$AlertParams.class */
    public static class AlertParams {
        public ListAdapter mAdapter;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public Drawable mNegativeButtonIcon;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public Drawable mNeutralButtonIcon;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface.OnClickListener mOnClickListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public Drawable mPositiveButtonIcon;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public int mViewSpacingTop;
        public int mIconId = 0;
        public int mIconAttrId = 0;
        public boolean mViewSpacingSpecified = false;
        public int mCheckedItem = -1;
        public boolean mRecycleOnMeasure = true;
        public boolean mCancelable = true;

        /* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController$AlertParams$OnPrepareListViewListener.class */
        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            this.mContext = context;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        private void createListView(AlertController alertController) {
            ListAdapter checkedItemAdapter;
            RecycleListView recycleListView = (RecycleListView) this.mInflater.inflate(alertController.mListLayout, (ViewGroup) null);
            if (this.mIsMultiChoice) {
                checkedItemAdapter = this.mCursor == null ? new ArrayAdapter<CharSequence>(this, this.mContext, alertController.mMultiChoiceItemLayout, R.id.text1, this.mItems, recycleListView) { // from class: android.support.v7.app.AlertController.AlertParams.1
                    final AlertParams this$0;
                    final RecycleListView val$listView;

                    {
                        this.this$0 = this;
                        this.val$listView = recycleListView;
                    }

                    @Override // android.widget.ArrayAdapter, android.widget.Adapter
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        View view2 = super.getView(i, view, viewGroup);
                        if (this.this$0.mCheckedItems != null && this.this$0.mCheckedItems[i]) {
                            this.val$listView.setItemChecked(i, true);
                        }
                        return view2;
                    }
                } : new CursorAdapter(this, this.mContext, this.mCursor, false, recycleListView, alertController) { // from class: android.support.v7.app.AlertController.AlertParams.2
                    private final int mIsCheckedIndex;
                    private final int mLabelIndex;
                    final AlertParams this$0;
                    final AlertController val$dialog;
                    final RecycleListView val$listView;

                    {
                        this.this$0 = this;
                        this.val$listView = recycleListView;
                        this.val$dialog = alertController;
                        Cursor cursor = getCursor();
                        this.mLabelIndex = cursor.getColumnIndexOrThrow(this.this$0.mLabelColumn);
                        this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(this.this$0.mIsCheckedColumn);
                    }

                    @Override // android.widget.CursorAdapter
                    public void bindView(View view, Context context, Cursor cursor) {
                        ((CheckedTextView) view.findViewById(R.id.text1)).setText(cursor.getString(this.mLabelIndex));
                        RecycleListView recycleListView2 = this.val$listView;
                        int position = cursor.getPosition();
                        boolean z = true;
                        if (cursor.getInt(this.mIsCheckedIndex) != 1) {
                            z = false;
                        }
                        recycleListView2.setItemChecked(position, z);
                    }

                    @Override // android.widget.CursorAdapter
                    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                        return this.this$0.mInflater.inflate(this.val$dialog.mMultiChoiceItemLayout, viewGroup, false);
                    }
                };
            } else {
                int i = this.mIsSingleChoice ? alertController.mSingleChoiceItemLayout : alertController.mListItemLayout;
                if (this.mCursor != null) {
                    checkedItemAdapter = new SimpleCursorAdapter(this.mContext, i, this.mCursor, new String[]{this.mLabelColumn}, new int[]{R.id.text1});
                } else {
                    checkedItemAdapter = this.mAdapter;
                    if (checkedItemAdapter == null) {
                        checkedItemAdapter = new CheckedItemAdapter(this.mContext, i, R.id.text1, this.mItems);
                    }
                }
            }
            OnPrepareListViewListener onPrepareListViewListener = this.mOnPrepareListViewListener;
            if (onPrepareListViewListener != null) {
                onPrepareListViewListener.onPrepareListView(recycleListView);
            }
            alertController.mAdapter = checkedItemAdapter;
            alertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(this, alertController) { // from class: android.support.v7.app.AlertController.AlertParams.3
                    final AlertParams this$0;
                    final AlertController val$dialog;

                    {
                        this.this$0 = this;
                        this.val$dialog = alertController;
                    }

                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        this.this$0.mOnClickListener.onClick(this.val$dialog.mDialog, i2);
                        if (this.this$0.mIsSingleChoice) {
                            return;
                        }
                        this.val$dialog.mDialog.dismiss();
                    }
                });
            } else if (this.mOnCheckboxClickListener != null) {
                recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(this, recycleListView, alertController) { // from class: android.support.v7.app.AlertController.AlertParams.4
                    final AlertParams this$0;
                    final AlertController val$dialog;
                    final RecycleListView val$listView;

                    {
                        this.this$0 = this;
                        this.val$listView = recycleListView;
                        this.val$dialog = alertController;
                    }

                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        if (this.this$0.mCheckedItems != null) {
                            this.this$0.mCheckedItems[i2] = this.val$listView.isItemChecked(i2);
                        }
                        this.this$0.mOnCheckboxClickListener.onClick(this.val$dialog.mDialog, i2, this.val$listView.isItemChecked(i2));
                    }
                });
            }
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                recycleListView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                recycleListView.setChoiceMode(1);
            } else if (this.mIsMultiChoice) {
                recycleListView.setChoiceMode(2);
            }
            alertController.mListView = recycleListView;
        }

        public void apply(AlertController alertController) {
            View view = this.mCustomTitleView;
            if (view != null) {
                alertController.setCustomTitle(view);
            } else {
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    alertController.setTitle(charSequence);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    alertController.setIcon(drawable);
                }
                int i = this.mIconId;
                if (i != 0) {
                    alertController.setIcon(i);
                }
                int i2 = this.mIconAttrId;
                if (i2 != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(i2));
                }
            }
            CharSequence charSequence2 = this.mMessage;
            if (charSequence2 != null) {
                alertController.setMessage(charSequence2);
            }
            if (this.mPositiveButtonText != null || this.mPositiveButtonIcon != null) {
                alertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null, this.mPositiveButtonIcon);
            }
            if (this.mNegativeButtonText != null || this.mNegativeButtonIcon != null) {
                alertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null, this.mNegativeButtonIcon);
            }
            if (this.mNeutralButtonText != null || this.mNeutralButtonIcon != null) {
                alertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, null, this.mNeutralButtonIcon);
            }
            if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
                createListView(alertController);
            }
            View view2 = this.mView;
            if (view2 != null) {
                if (this.mViewSpacingSpecified) {
                    alertController.setView(view2, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
                    return;
                } else {
                    alertController.setView(view2);
                    return;
                }
            }
            int i3 = this.mViewLayoutResId;
            if (i3 != 0) {
                alertController.setView(i3);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController$ButtonHandler.class */
    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<>(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == -3 || i == -2 || i == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick(this.mDialog.get(), message.what);
            } else {
                if (i != 1) {
                    return;
                }
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController$CheckedItemAdapter.class */
    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public CheckedItemAdapter(Context context, int i, int i2, CharSequence[] charSequenceArr) {
            super(context, i, i2, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AlertController$RecycleListView.class */
    public static class RecycleListView extends ListView {
        private final int mPaddingBottomNoButtons;
        private final int mPaddingTopNoTitle;

        public RecycleListView(Context context) {
            this(context, null);
        }

        public RecycleListView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0287R.styleable.RecycleListView);
            this.mPaddingBottomNoButtons = typedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.RecycleListView_paddingBottomNoButtons, -1);
            this.mPaddingTopNoTitle = typedArrayObtainStyledAttributes.getDimensionPixelOffset(C0287R.styleable.RecycleListView_paddingTopNoTitle, -1);
        }

        public void setHasDecor(boolean z, boolean z2) {
            if (z2 && z) {
                return;
            }
            setPadding(getPaddingLeft(), z ? getPaddingTop() : this.mPaddingTopNoTitle, getPaddingRight(), z2 ? getPaddingBottom() : this.mPaddingBottomNoButtons);
        }
    }

    public AlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        this.mContext = context;
        this.mDialog = appCompatDialog;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(appCompatDialog);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(null, C0287R.styleable.AlertDialog, C0287R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.AlertDialog_listItemLayout, 0);
        this.mShowTitle = typedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.AlertDialog_showTitle, true);
        this.mButtonIconDimen = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.AlertDialog_buttonIconDimen, 0);
        typedArrayObtainStyledAttributes.recycle();
        appCompatDialog.supportRequestWindowFeature(1);
    }

    static boolean canTextInput(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            int i = childCount - 1;
            childCount = i;
            if (canTextInput(viewGroup.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }

    static void manageScrollIndicators(View view, View view2, View view3) {
        if (view2 != null) {
            view2.setVisibility(view.canScrollVertically(-1) ? 0 : 4);
        }
        if (view3 != null) {
            view3.setVisibility(view.canScrollVertically(1) ? 0 : 4);
        }
    }

    private ViewGroup resolvePanel(View view, View view2) {
        if (view == null) {
            View viewInflate = view2;
            if (view2 instanceof ViewStub) {
                viewInflate = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) viewInflate;
        }
        if (view2 != null) {
            ViewParent parent = view2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view2);
            }
        }
        View viewInflate2 = view;
        if (view instanceof ViewStub) {
            viewInflate2 = ((ViewStub) view).inflate();
        }
        return (ViewGroup) viewInflate2;
    }

    private int selectContentView() {
        int i = this.mButtonPanelSideLayout;
        if (i != 0 && this.mButtonPanelLayoutHint == 1) {
            return i;
        }
        return this.mAlertDialogLayout;
    }

    private void setScrollIndicators(ViewGroup viewGroup, View view, int i, int i2) {
        View viewFindViewById = this.mWindow.findViewById(C0287R.id.scrollIndicatorUp);
        View viewFindViewById2 = this.mWindow.findViewById(C0287R.id.scrollIndicatorDown);
        if (Build.VERSION.SDK_INT >= 23) {
            ViewCompat.setScrollIndicators(view, i, i2);
            if (viewFindViewById != null) {
                viewGroup.removeView(viewFindViewById);
            }
            if (viewFindViewById2 != null) {
                viewGroup.removeView(viewFindViewById2);
                return;
            }
            return;
        }
        View view2 = viewFindViewById;
        if (viewFindViewById != null) {
            view2 = viewFindViewById;
            if ((i & 1) == 0) {
                viewGroup.removeView(viewFindViewById);
                view2 = null;
            }
        }
        View view3 = viewFindViewById2;
        if (viewFindViewById2 != null) {
            view3 = viewFindViewById2;
            if ((i & 2) == 0) {
                viewGroup.removeView(viewFindViewById2);
                view3 = null;
            }
        }
        if (view2 == null && view3 == null) {
            return;
        }
        if (this.mMessage != null) {
            this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(this, view2, view3) { // from class: android.support.v7.app.AlertController.2
                final AlertController this$0;
                final View val$bottom;
                final View val$top;

                {
                    this.this$0 = this;
                    this.val$top = view2;
                    this.val$bottom = view3;
                }

                @Override // android.support.v4.widget.NestedScrollView.OnScrollChangeListener
                public void onScrollChange(NestedScrollView nestedScrollView, int i3, int i4, int i5, int i6) {
                    AlertController.manageScrollIndicators(nestedScrollView, this.val$top, this.val$bottom);
                }
            });
            this.mScrollView.post(new Runnable(this, view2, view3) { // from class: android.support.v7.app.AlertController.3
                final AlertController this$0;
                final View val$bottom;
                final View val$top;

                {
                    this.this$0 = this;
                    this.val$top = view2;
                    this.val$bottom = view3;
                }

                @Override // java.lang.Runnable
                public void run() {
                    AlertController.manageScrollIndicators(this.this$0.mScrollView, this.val$top, this.val$bottom);
                }
            });
            return;
        }
        ListView listView = this.mListView;
        if (listView != null) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener(this, view2, view3) { // from class: android.support.v7.app.AlertController.4
                final AlertController this$0;
                final View val$bottom;
                final View val$top;

                {
                    this.this$0 = this;
                    this.val$top = view2;
                    this.val$bottom = view3;
                }

                @Override // android.widget.AbsListView.OnScrollListener
                public void onScroll(AbsListView absListView, int i3, int i4, int i5) {
                    AlertController.manageScrollIndicators(absListView, this.val$top, this.val$bottom);
                }

                @Override // android.widget.AbsListView.OnScrollListener
                public void onScrollStateChanged(AbsListView absListView, int i3) {
                }
            });
            this.mListView.post(new Runnable(this, view2, view3) { // from class: android.support.v7.app.AlertController.5
                final AlertController this$0;
                final View val$bottom;
                final View val$top;

                {
                    this.this$0 = this;
                    this.val$top = view2;
                    this.val$bottom = view3;
                }

                @Override // java.lang.Runnable
                public void run() {
                    AlertController.manageScrollIndicators(this.this$0.mListView, this.val$top, this.val$bottom);
                }
            });
            return;
        }
        if (view2 != null) {
            viewGroup.removeView(view2);
        }
        if (view3 != null) {
            viewGroup.removeView(view3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setupButtons(ViewGroup viewGroup) {
        boolean z;
        boolean z2;
        boolean z3;
        Button button = (Button) viewGroup.findViewById(R.id.button1);
        this.mButtonPositive = button;
        button.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonPositiveText) && this.mButtonPositiveIcon == null) {
            this.mButtonPositive.setVisibility(8);
            z = false;
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            Drawable drawable = this.mButtonPositiveIcon;
            if (drawable != null) {
                int i = this.mButtonIconDimen;
                drawable.setBounds(0, 0, i, i);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, null, null, null);
            }
            this.mButtonPositive.setVisibility(0);
            z = true;
        }
        Button button2 = (Button) viewGroup.findViewById(R.id.button2);
        this.mButtonNegative = button2;
        button2.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText) && this.mButtonNegativeIcon == null) {
            this.mButtonNegative.setVisibility(8);
            z2 = z;
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            Drawable drawable2 = this.mButtonNegativeIcon;
            if (drawable2 != null) {
                int i2 = this.mButtonIconDimen;
                drawable2.setBounds(0, 0, i2, i2);
                this.mButtonNegative.setCompoundDrawables(this.mButtonNegativeIcon, null, null, null);
            }
            this.mButtonNegative.setVisibility(0);
            z2 = ((z ? 1 : 0) | 2) == true ? 1 : 0;
        }
        Button button3 = (Button) viewGroup.findViewById(R.id.button3);
        this.mButtonNeutral = button3;
        button3.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText) && this.mButtonNeutralIcon == null) {
            this.mButtonNeutral.setVisibility(8);
            z3 = z2;
        } else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            Drawable drawable3 = this.mButtonPositiveIcon;
            if (drawable3 != null) {
                int i3 = this.mButtonIconDimen;
                drawable3.setBounds(0, 0, i3, i3);
                this.mButtonPositive.setCompoundDrawables(this.mButtonPositiveIcon, null, null, null);
            }
            this.mButtonNeutral.setVisibility(0);
            z3 = ((z2 ? 1 : 0) | 4) == true ? 1 : 0;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (z3) {
                centerButton(this.mButtonPositive);
            } else if (z3 == 2) {
                centerButton(this.mButtonNegative);
            } else if (z3 == 4) {
                centerButton(this.mButtonNeutral);
            }
        }
        if (z3) {
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void setupContent(ViewGroup viewGroup) {
        NestedScrollView nestedScrollView = (NestedScrollView) this.mWindow.findViewById(C0287R.id.scrollView);
        this.mScrollView = nestedScrollView;
        nestedScrollView.setFocusable(false);
        this.mScrollView.setNestedScrollingEnabled(false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.message);
        this.mMessageView = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.mMessage;
        if (charSequence != null) {
            textView.setText(charSequence);
            return;
        }
        textView.setVisibility(8);
        this.mScrollView.removeView(this.mMessageView);
        if (this.mListView == null) {
            viewGroup.setVisibility(8);
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mScrollView.getParent();
        int iIndexOfChild = viewGroup2.indexOfChild(this.mScrollView);
        viewGroup2.removeViewAt(iIndexOfChild);
        viewGroup2.addView(this.mListView, iIndexOfChild, new ViewGroup.LayoutParams(-1, -1));
    }

    private void setupCustomContent(ViewGroup viewGroup) {
        View viewInflate = this.mView;
        boolean z = false;
        if (viewInflate == null) {
            viewInflate = this.mViewLayoutResId != 0 ? LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, viewGroup, false) : null;
        }
        if (viewInflate != null) {
            z = true;
        }
        if (!z || !canTextInput(viewInflate)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (!z) {
            viewGroup.setVisibility(8);
            return;
        }
        FrameLayout frameLayout = (FrameLayout) this.mWindow.findViewById(C0287R.id.custom);
        frameLayout.addView(viewInflate, new ViewGroup.LayoutParams(-1, -1));
        if (this.mViewSpacingSpecified) {
            frameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
        }
        if (this.mListView != null) {
            ((LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams()).weight = 0.0f;
        }
    }

    private void setupTitle(ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new ViewGroup.LayoutParams(-1, -2));
            this.mWindow.findViewById(C0287R.id.title_template).setVisibility(8);
            return;
        }
        this.mIconView = (ImageView) this.mWindow.findViewById(R.id.icon);
        if (!(!TextUtils.isEmpty(this.mTitle)) || !this.mShowTitle) {
            this.mWindow.findViewById(C0287R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            viewGroup.setVisibility(8);
            return;
        }
        TextView textView = (TextView) this.mWindow.findViewById(C0287R.id.alertTitle);
        this.mTitleView = textView;
        textView.setText(this.mTitle);
        int i = this.mIconId;
        if (i != 0) {
            this.mIconView.setImageResource(i);
            return;
        }
        Drawable drawable = this.mIcon;
        if (drawable != null) {
            this.mIconView.setImageDrawable(drawable);
        } else {
            this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
            this.mIconView.setVisibility(8);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setupView() {
        View viewFindViewById;
        ListAdapter listAdapter;
        View viewFindViewById2;
        View viewFindViewById3 = this.mWindow.findViewById(C0287R.id.parentPanel);
        View viewFindViewById4 = viewFindViewById3.findViewById(C0287R.id.topPanel);
        View viewFindViewById5 = viewFindViewById3.findViewById(C0287R.id.contentPanel);
        View viewFindViewById6 = viewFindViewById3.findViewById(C0287R.id.buttonPanel);
        ViewGroup viewGroup = (ViewGroup) viewFindViewById3.findViewById(C0287R.id.customPanel);
        setupCustomContent(viewGroup);
        View viewFindViewById7 = viewGroup.findViewById(C0287R.id.topPanel);
        View viewFindViewById8 = viewGroup.findViewById(C0287R.id.contentPanel);
        View viewFindViewById9 = viewGroup.findViewById(C0287R.id.buttonPanel);
        ViewGroup viewGroupResolvePanel = resolvePanel(viewFindViewById7, viewFindViewById4);
        ViewGroup viewGroupResolvePanel2 = resolvePanel(viewFindViewById8, viewFindViewById5);
        ViewGroup viewGroupResolvePanel3 = resolvePanel(viewFindViewById9, viewFindViewById6);
        setupContent(viewGroupResolvePanel2);
        setupButtons(viewGroupResolvePanel3);
        setupTitle(viewGroupResolvePanel);
        boolean z = (viewGroup == null || viewGroup.getVisibility() == 8) ? false : true;
        int i = (viewGroupResolvePanel == null || viewGroupResolvePanel.getVisibility() == 8) ? 0 : 1;
        boolean z2 = (viewGroupResolvePanel3 == null || viewGroupResolvePanel3.getVisibility() == 8) ? false : true;
        if (!z2 && viewGroupResolvePanel2 != null && (viewFindViewById2 = viewGroupResolvePanel2.findViewById(C0287R.id.textSpacerNoButtons)) != null) {
            viewFindViewById2.setVisibility(0);
        }
        if (i != 0) {
            NestedScrollView nestedScrollView = this.mScrollView;
            if (nestedScrollView != null) {
                nestedScrollView.setClipToPadding(true);
            }
            View viewFindViewById10 = null;
            if (this.mMessage != null || this.mListView != null) {
                viewFindViewById10 = viewGroupResolvePanel.findViewById(C0287R.id.titleDividerNoCustom);
            }
            if (viewFindViewById10 != null) {
                viewFindViewById10.setVisibility(0);
            }
        } else if (viewGroupResolvePanel2 != null && (viewFindViewById = viewGroupResolvePanel2.findViewById(C0287R.id.textSpacerNoTitle)) != null) {
            viewFindViewById.setVisibility(0);
        }
        ListView listView = this.mListView;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).setHasDecor(i, z2);
        }
        if (!z) {
            View view = this.mListView;
            if (view == null) {
                view = this.mScrollView;
            }
            if (view != null) {
                int i2 = 0;
                if (z2) {
                    i2 = 2;
                }
                setScrollIndicators(viewGroupResolvePanel2, view, i | i2, 3);
            }
        }
        ListView listView2 = this.mListView;
        if (listView2 == null || (listAdapter = this.mAdapter) == null) {
            return;
        }
        listView2.setAdapter(listAdapter);
        int i3 = this.mCheckedItem;
        if (i3 > -1) {
            listView2.setItemChecked(i3, true);
            listView2.setSelection(i3);
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        TypedValue typedValue = new TypedValue();
        boolean z = true;
        context.getTheme().resolveAttribute(C0287R.attr.alertDialogCenterButtons, typedValue, true);
        if (typedValue.data == 0) {
            z = false;
        }
        return z;
    }

    public Button getButton(int i) {
        if (i == -3) {
            return this.mButtonNeutral;
        }
        if (i == -2) {
            return this.mButtonNegative;
        }
        if (i != -1) {
            return null;
        }
        return this.mButtonPositive;
    }

    public int getIconAttributeResId(int i) {
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView getListView() {
        return this.mListView;
    }

    public void installContent() {
        this.mDialog.setContentView(selectContentView());
        setupView();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.mScrollView;
        return nestedScrollView != null && nestedScrollView.executeKeyEvent(keyEvent);
    }

    public void setButton(int i, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message, Drawable drawable) {
        Message messageObtainMessage = message;
        if (message == null) {
            messageObtainMessage = message;
            if (onClickListener != null) {
                messageObtainMessage = this.mHandler.obtainMessage(i, onClickListener);
            }
        }
        if (i == -3) {
            this.mButtonNeutralText = charSequence;
            this.mButtonNeutralMessage = messageObtainMessage;
            this.mButtonNeutralIcon = drawable;
        } else if (i == -2) {
            this.mButtonNegativeText = charSequence;
            this.mButtonNegativeMessage = messageObtainMessage;
            this.mButtonNegativeIcon = drawable;
        } else {
            if (i != -1) {
                throw new IllegalArgumentException("Button does not exist");
            }
            this.mButtonPositiveText = charSequence;
            this.mButtonPositiveMessage = messageObtainMessage;
            this.mButtonPositiveIcon = drawable;
        }
    }

    public void setButtonPanelLayoutHint(int i) {
        this.mButtonPanelLayoutHint = i;
    }

    public void setCustomTitle(View view) {
        this.mCustomTitleView = view;
    }

    public void setIcon(int i) {
        this.mIcon = null;
        this.mIconId = i;
        ImageView imageView = this.mIconView;
        if (imageView != null) {
            if (i == 0) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                this.mIconView.setImageResource(this.mIconId);
            }
        }
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
        ImageView imageView = this.mIconView;
        if (imageView != null) {
            if (drawable == null) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                this.mIconView.setImageDrawable(drawable);
            }
        }
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
        TextView textView = this.mMessageView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void setView(int i) {
        this.mView = null;
        this.mViewLayoutResId = i;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }

    public void setView(View view, int i, int i2, int i3, int i4) {
        this.mView = view;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = i;
        this.mViewSpacingTop = i2;
        this.mViewSpacingRight = i3;
        this.mViewSpacingBottom = i4;
    }
}
