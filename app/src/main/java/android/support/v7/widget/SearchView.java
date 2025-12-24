package android.support.v7.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.CollapsibleActionView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.github.clans.fab.BuildConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView.class */
public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
    static final boolean DBG = false;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    static final String LOG_TAG = "SearchView";
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View.OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView.OnEditorActionListener mOnEditorActionListener;
    private final AdapterView.OnItemClickListener mOnItemClickListener;
    private final AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    View.OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View.OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable.ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    final SearchAutoComplete mSearchSrcTextView;
    private Rect mSearchSrcTextViewBounds;
    private Rect mSearchSrtTextViewBoundsExpanded;
    SearchableInfo mSearchable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    CursorAdapter mSuggestionsAdapter;
    private int[] mTemp;
    private int[] mTemp2;
    View.OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$AutoCompleteTextViewReflector.class */
    private static class AutoCompleteTextViewReflector {
        private Method doAfterTextChanged;
        private Method doBeforeTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() throws NoSuchMethodException, SecurityException {
            try {
                Method declaredMethod = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.doBeforeTextChanged = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
            }
            try {
                Method declaredMethod2 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.doAfterTextChanged = declaredMethod2;
                declaredMethod2.setAccessible(true);
            } catch (NoSuchMethodException e2) {
            }
            try {
                Method method = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
                this.ensureImeVisible = method;
                method.setAccessible(true);
            } catch (NoSuchMethodException e3) {
            }
        }

        void doAfterTextChanged(AutoCompleteTextView autoCompleteTextView) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Method method = this.doAfterTextChanged;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        void doBeforeTextChanged(AutoCompleteTextView autoCompleteTextView) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Method method = this.doBeforeTextChanged;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception e) {
                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView autoCompleteTextView, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            Method method = this.ensureImeVisible;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, Boolean.valueOf(z));
                } catch (Exception e) {
                }
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$OnCloseListener.class */
    public interface OnCloseListener {
        boolean onClose();
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$OnQueryTextListener.class */
    public interface OnQueryTextListener {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$OnSuggestionListener.class */
    public interface OnSuggestionListener {
        boolean onSuggestionClick(int i);

        boolean onSuggestionSelect(int i);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$SavedState.class */
    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: android.support.v7.widget.SearchView.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean isIconified;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isIconified = ((Boolean) parcel.readValue(null)).booleanValue();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.isIconified + "}";
        }

        @Override // android.support.v4.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.isIconified));
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$SearchAutoComplete.class */
    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
        private boolean mHasPendingShowSoftInputRequest;
        final Runnable mRunShowSoftInputIfNecessary;
        private SearchView mSearchView;
        private int mThreshold;

        public SearchAutoComplete(Context context) {
            this(context, null);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, C0287R.attr.autoCompleteTextViewStyle);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.mRunShowSoftInputIfNecessary = new Runnable(this) { // from class: android.support.v7.widget.SearchView.SearchAutoComplete.1
                final SearchAutoComplete this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.this$0.showSoftInputIfNecessary();
                }
            };
            this.mThreshold = getThreshold();
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration configuration = getResources().getConfiguration();
            int i = configuration.screenWidthDp;
            int i2 = configuration.screenHeightDp;
            if (i < 960 || i2 < 720 || configuration.orientation != 2) {
                return i < 600 ? (i < 640 || i2 < 480) ? Opcodes.IF_ICMPNE : Opcodes.CHECKCAST : Opcodes.CHECKCAST;
            }
            return 256;
        }

        @Override // android.widget.AutoCompleteTextView
        public boolean enoughToFilter() {
            return this.mThreshold <= 0 || super.enoughToFilter();
        }

        boolean isEmpty() {
            return TextUtils.getTrimmedLength(getText()) == 0;
        }

        @Override // android.support.v7.widget.AppCompatAutoCompleteTextView, android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.mHasPendingShowSoftInputRequest) {
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                post(this.mRunShowSoftInputIfNecessary);
            }
            return inputConnectionOnCreateInputConnection;
        }

        @Override // android.view.View
        protected void onFinishInflate() {
            super.onFinishInflate();
            setMinWidth((int) TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), getResources().getDisplayMetrics()));
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        protected void onFocusChanged(boolean z, int i, Rect rect) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            super.onFocusChanged(z, i, rect);
            this.mSearchView.onTextFocusChanged();
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (i == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState keyDispatcherState = getKeyDispatcherState();
                    if (keyDispatcherState == null) {
                        return true;
                    }
                    keyDispatcherState.startTracking(keyEvent, this);
                    return true;
                }
                if (keyEvent.getAction() == 1) {
                    KeyEvent.DispatcherState keyDispatcherState2 = getKeyDispatcherState();
                    if (keyDispatcherState2 != null) {
                        keyDispatcherState2.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.mSearchView.clearFocus();
                        setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(i, keyEvent);
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public void onWindowFocusChanged(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            super.onWindowFocusChanged(z);
            if (z && this.mSearchView.hasFocus() && getVisibility() == 0) {
                this.mHasPendingShowSoftInputRequest = true;
                if (SearchView.isLandscapeMode(getContext())) {
                    SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        @Override // android.widget.AutoCompleteTextView
        public void performCompletion() {
        }

        @Override // android.widget.AutoCompleteTextView
        protected void replaceText(CharSequence charSequence) {
        }

        void setImeVisibility(boolean z) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            if (!z) {
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else {
                if (!inputMethodManager.isActive(this)) {
                    this.mHasPendingShowSoftInputRequest = true;
                    return;
                }
                this.mHasPendingShowSoftInputRequest = false;
                removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.showSoftInput(this, 0);
            }
        }

        void setSearchView(SearchView searchView) {
            this.mSearchView = searchView;
        }

        @Override // android.widget.AutoCompleteTextView
        public void setThreshold(int i) {
            super.setThreshold(i);
            this.mThreshold = i;
        }

        void showSoftInputIfNecessary() {
            if (this.mHasPendingShowSoftInputRequest) {
                ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this, 0);
                this.mHasPendingShowSoftInputRequest = false;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/SearchView$UpdatableTouchDelegate.class */
    private static class UpdatableTouchDelegate extends TouchDelegate {
        private final Rect mActualBounds;
        private boolean mDelegateTargeted;
        private final View mDelegateView;
        private final int mSlop;
        private final Rect mSlopBounds;
        private final Rect mTargetBounds;

        public UpdatableTouchDelegate(Rect rect, Rect rect2, View view) {
            super(rect, view);
            this.mSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.mTargetBounds = new Rect();
            this.mSlopBounds = new Rect();
            this.mActualBounds = new Rect();
            setBounds(rect, rect2);
            this.mDelegateView = view;
        }

        @Override // android.view.TouchDelegate
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z;
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int action = motionEvent.getAction();
            boolean z2 = true;
            if (action != 0) {
                if (action == 1 || action == 2) {
                    boolean z3 = this.mDelegateTargeted;
                    z2 = z3;
                    if (z3) {
                        z2 = z3;
                        if (!this.mSlopBounds.contains(x, y)) {
                            z2 = z3;
                            z = false;
                        }
                    }
                } else {
                    if (action == 3) {
                        z2 = this.mDelegateTargeted;
                        this.mDelegateTargeted = false;
                    }
                    z = true;
                    z2 = false;
                }
                z = true;
            } else {
                if (this.mTargetBounds.contains(x, y)) {
                    this.mDelegateTargeted = true;
                    z = true;
                }
                z = true;
                z2 = false;
            }
            boolean zDispatchTouchEvent = false;
            if (z2) {
                if (!z || this.mActualBounds.contains(x, y)) {
                    motionEvent.setLocation(x - this.mActualBounds.left, y - this.mActualBounds.top);
                } else {
                    motionEvent.setLocation(this.mDelegateView.getWidth() / 2, this.mDelegateView.getHeight() / 2);
                }
                zDispatchTouchEvent = this.mDelegateView.dispatchTouchEvent(motionEvent);
            }
            return zDispatchTouchEvent;
        }

        public void setBounds(Rect rect, Rect rect2) {
            this.mTargetBounds.set(rect);
            this.mSlopBounds.set(rect);
            Rect rect3 = this.mSlopBounds;
            int i = this.mSlop;
            rect3.inset(-i, -i);
            this.mActualBounds.set(rect2);
        }
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0287R.attr.searchViewStyle);
    }

    public SearchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSearchSrcTextViewBounds = new Rect();
        this.mSearchSrtTextViewBoundsExpanded = new Rect();
        this.mTemp = new int[2];
        this.mTemp2 = new int[2];
        this.mUpdateDrawableStateRunnable = new Runnable(this) { // from class: android.support.v7.widget.SearchView.1
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.this$0.updateFocusedState();
            }
        };
        this.mReleaseCursorRunnable = new Runnable(this) { // from class: android.support.v7.widget.SearchView.2
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.this$0.mSuggestionsAdapter == null || !(this.this$0.mSuggestionsAdapter instanceof SuggestionsAdapter)) {
                    return;
                }
                this.this$0.mSuggestionsAdapter.changeCursor(null);
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap<>();
        this.mOnClickListener = new View.OnClickListener(this) { // from class: android.support.v7.widget.SearchView.5
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (view == this.this$0.mSearchButton) {
                    this.this$0.onSearchClicked();
                    return;
                }
                if (view == this.this$0.mCloseButton) {
                    this.this$0.onCloseClicked();
                    return;
                }
                if (view == this.this$0.mGoButton) {
                    this.this$0.onSubmitQuery();
                } else if (view == this.this$0.mVoiceButton) {
                    this.this$0.onVoiceClicked();
                } else if (view == this.this$0.mSearchSrcTextView) {
                    this.this$0.forceSuggestionQuery();
                }
            }
        };
        this.mTextKeyListener = new View.OnKeyListener(this) { // from class: android.support.v7.widget.SearchView.6
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i2, KeyEvent keyEvent) {
                if (this.this$0.mSearchable == null) {
                    return false;
                }
                if (this.this$0.mSearchSrcTextView.isPopupShowing() && this.this$0.mSearchSrcTextView.getListSelection() != -1) {
                    return this.this$0.onSuggestionsKey(view, i2, keyEvent);
                }
                if (this.this$0.mSearchSrcTextView.isEmpty() || !keyEvent.hasNoModifiers() || keyEvent.getAction() != 1 || i2 != 66) {
                    return false;
                }
                view.cancelLongPress();
                SearchView searchView = this.this$0;
                searchView.launchQuerySearch(0, null, searchView.mSearchSrcTextView.getText().toString());
                return true;
            }
        };
        this.mOnEditorActionListener = new TextView.OnEditorActionListener(this) { // from class: android.support.v7.widget.SearchView.7
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i2, KeyEvent keyEvent) {
                this.this$0.onSubmitQuery();
                return true;
            }
        };
        this.mOnItemClickListener = new AdapterView.OnItemClickListener(this) { // from class: android.support.v7.widget.SearchView.8
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                this.this$0.onItemClicked(i2, 0, null);
            }
        };
        this.mOnItemSelectedListener = new AdapterView.OnItemSelectedListener(this) { // from class: android.support.v7.widget.SearchView.9
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i2, long j) {
                this.this$0.onItemSelected(i2);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        this.mTextWatcher = new TextWatcher(this) { // from class: android.support.v7.widget.SearchView.10
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                this.this$0.onTextChanged(charSequence);
            }
        };
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0287R.styleable.SearchView, i, 0);
        LayoutInflater.from(context).inflate(tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.SearchView_layout, C0287R.layout.abc_search_view), (ViewGroup) this, true);
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) findViewById(C0287R.id.search_src_text);
        this.mSearchSrcTextView = searchAutoComplete;
        searchAutoComplete.setSearchView(this);
        this.mSearchEditFrame = findViewById(C0287R.id.search_edit_frame);
        this.mSearchPlate = findViewById(C0287R.id.search_plate);
        this.mSubmitArea = findViewById(C0287R.id.submit_area);
        this.mSearchButton = (ImageView) findViewById(C0287R.id.search_button);
        this.mGoButton = (ImageView) findViewById(C0287R.id.search_go_btn);
        this.mCloseButton = (ImageView) findViewById(C0287R.id.search_close_btn);
        this.mVoiceButton = (ImageView) findViewById(C0287R.id.search_voice_btn);
        this.mCollapsedIcon = (ImageView) findViewById(C0287R.id.search_mag_icon);
        ViewCompat.setBackground(this.mSearchPlate, tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(this.mSubmitArea, tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_submitBackground));
        this.mSearchButton.setImageDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_searchIcon));
        this.mGoButton.setImageDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_goIcon));
        this.mCloseButton.setImageDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_closeIcon));
        this.mVoiceButton.setImageDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_voiceIcon));
        this.mCollapsedIcon.setImageDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SearchView_searchHintIcon);
        TooltipCompat.setTooltipText(this.mSearchButton, getResources().getString(C0287R.string.abc_searchview_description_search));
        this.mSuggestionRowLayout = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.SearchView_suggestionRowLayout, C0287R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.SearchView_commitIcon, 0);
        this.mSearchButton.setOnClickListener(this.mOnClickListener);
        this.mCloseButton.setOnClickListener(this.mOnClickListener);
        this.mGoButton.setOnClickListener(this.mOnClickListener);
        this.mVoiceButton.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
        this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
        this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
        this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
        this.mSearchSrcTextView.setOnFocusChangeListener(new View.OnFocusChangeListener(this) { // from class: android.support.v7.widget.SearchView.3
            final SearchView this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View view, boolean z) {
                if (this.this$0.mOnQueryTextFocusChangeListener != null) {
                    this.this$0.mOnQueryTextFocusChangeListener.onFocusChange(this.this$0, z);
                }
            }
        });
        setIconifiedByDefault(tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.SearchView_iconifiedByDefault, true));
        int dimensionPixelSize = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.SearchView_android_maxWidth, -1);
        if (dimensionPixelSize != -1) {
            setMaxWidth(dimensionPixelSize);
        }
        this.mDefaultQueryHint = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.SearchView_queryHint);
        int i2 = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.SearchView_android_imeOptions, -1);
        if (i2 != -1) {
            setImeOptions(i2);
        }
        int i3 = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.SearchView_android_inputType, -1);
        if (i3 != -1) {
            setInputType(i3);
        }
        setFocusable(tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.SearchView_android_focusable, true));
        tintTypedArrayObtainStyledAttributes.recycle();
        Intent intent = new Intent("android.speech.action.WEB_SEARCH");
        this.mVoiceWebSearchIntent = intent;
        intent.addFlags(268435456);
        this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        Intent intent2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.mVoiceAppSearchIntent = intent2;
        intent2.addFlags(268435456);
        View viewFindViewById = findViewById(this.mSearchSrcTextView.getDropDownAnchor());
        this.mDropDownAnchor = viewFindViewById;
        if (viewFindViewById != null) {
            viewFindViewById.addOnLayoutChangeListener(new View.OnLayoutChangeListener(this) { // from class: android.support.v7.widget.SearchView.4
                final SearchView this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnLayoutChangeListener
                public void onLayoutChange(View view, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
                    this.this$0.adjustDropDownSizeAndPosition();
                }
            });
        }
        updateViewsVisibility(this.mIconifiedByDefault);
        updateQueryHint();
    }

    private Intent createIntent(String str, Uri uri, String str2, String str3, int i, String str4) {
        Intent intent = new Intent(str);
        intent.addFlags(268435456);
        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra("user_query", this.mUserQuery);
        if (str3 != null) {
            intent.putExtra("query", str3);
        }
        if (str2 != null) {
            intent.putExtra("intent_extra_data_key", str2);
        }
        Bundle bundle = this.mAppSearchData;
        if (bundle != null) {
            intent.putExtra("app_data", bundle);
        }
        if (i != 0) {
            intent.putExtra("action_key", i);
            intent.putExtra("action_msg", str4);
        }
        intent.setComponent(this.mSearchable.getSearchActivity());
        return intent;
    }

    private Intent createIntentFromSuggestion(Cursor cursor, int i, String str) {
        int position;
        try {
            String columnString = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_action");
            String suggestIntentAction = columnString;
            if (columnString == null) {
                suggestIntentAction = this.mSearchable.getSuggestIntentAction();
            }
            String str2 = suggestIntentAction;
            if (suggestIntentAction == null) {
                str2 = "android.intent.action.SEARCH";
            }
            String columnString2 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data");
            String suggestIntentData = columnString2;
            if (columnString2 == null) {
                suggestIntentData = this.mSearchable.getSuggestIntentData();
            }
            String str3 = suggestIntentData;
            if (suggestIntentData != null) {
                String columnString3 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data_id");
                str3 = suggestIntentData;
                if (columnString3 != null) {
                    str3 = suggestIntentData + "/" + Uri.encode(columnString3);
                }
            }
            return createIntent(str2, str3 == null ? null : Uri.parse(str3), SuggestionsAdapter.getColumnString(cursor, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString(cursor, "suggest_intent_query"), i, str);
        } catch (RuntimeException e) {
            try {
                position = cursor.getPosition();
            } catch (RuntimeException e2) {
                position = -1;
            }
            Log.w(LOG_TAG, "Search suggestions cursor at row " + position + " returned exception.", e);
            return null;
        }
    }

    private Intent createVoiceAppSearchIntent(Intent intent, SearchableInfo searchableInfo) {
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        Intent intent2 = new Intent("android.intent.action.SEARCH");
        intent2.setComponent(searchActivity);
        PendingIntent activity = PendingIntent.getActivity(getContext(), 0, intent2, 1073741824);
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.mAppSearchData;
        if (bundle2 != null) {
            bundle.putParcelable("app_data", bundle2);
        }
        Intent intent3 = new Intent(intent);
        int voiceMaxResults = 1;
        Resources resources = getResources();
        String string = searchableInfo.getVoiceLanguageModeId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageModeId()) : "free_form";
        String string2 = searchableInfo.getVoicePromptTextId() != 0 ? resources.getString(searchableInfo.getVoicePromptTextId()) : null;
        String string3 = searchableInfo.getVoiceLanguageId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageId()) : null;
        if (searchableInfo.getVoiceMaxResults() != 0) {
            voiceMaxResults = searchableInfo.getVoiceMaxResults();
        }
        intent3.putExtra("android.speech.extra.LANGUAGE_MODEL", string);
        intent3.putExtra("android.speech.extra.PROMPT", string2);
        intent3.putExtra("android.speech.extra.LANGUAGE", string3);
        intent3.putExtra("android.speech.extra.MAX_RESULTS", voiceMaxResults);
        intent3.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", activity);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent3;
    }

    private Intent createVoiceWebSearchIntent(Intent intent, SearchableInfo searchableInfo) {
        Intent intent2 = new Intent(intent);
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        intent2.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        return intent2;
    }

    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }

    private void getChildBoundsWithinSearchView(View view, Rect rect) {
        view.getLocationInWindow(this.mTemp);
        getLocationInWindow(this.mTemp2);
        int[] iArr = this.mTemp;
        int i = iArr[1];
        int[] iArr2 = this.mTemp2;
        int i2 = i - iArr2[1];
        int i3 = iArr[0] - iArr2[0];
        rect.set(i3, i2, view.getWidth() + i3, view.getHeight() + i2);
    }

    private CharSequence getDecoratedHint(CharSequence charSequence) {
        if (!this.mIconifiedByDefault || this.mSearchHintIcon == null) {
            return charSequence;
        }
        double textSize = this.mSearchSrcTextView.getTextSize();
        Double.isNaN(textSize);
        int i = (int) (textSize * 1.25d);
        this.mSearchHintIcon.setBounds(0, 0, i, i);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("   ");
        spannableStringBuilder.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
        spannableStringBuilder.append(charSequence);
        return spannableStringBuilder;
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(C0287R.dimen.abc_search_view_preferred_height);
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(C0287R.dimen.abc_search_view_preferred_width);
    }

    private boolean hasVoiceSearch() {
        SearchableInfo searchableInfo = this.mSearchable;
        boolean z = false;
        if (searchableInfo != null) {
            z = false;
            if (searchableInfo.getVoiceSearchEnabled()) {
                Intent intent = null;
                if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
                    intent = this.mVoiceWebSearchIntent;
                } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
                    intent = this.mVoiceAppSearchIntent;
                }
                z = false;
                if (intent != null) {
                    z = false;
                    if (getContext().getPackageManager().resolveActivity(intent, 65536) != null) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private boolean isSubmitAreaEnabled() {
        return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !isIconified();
    }

    private void launchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            getContext().startActivity(intent);
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "Failed launch activity: " + intent, e);
        }
    }

    private boolean launchSuggestion(int i, int i2, String str) {
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor == null || !cursor.moveToPosition(i)) {
            return false;
        }
        launchIntent(createIntentFromSuggestion(cursor, i2, str));
        return true;
    }

    private void postUpdateFocusedState() {
        post(this.mUpdateDrawableStateRunnable);
    }

    private void rewriteQueryFromSuggestion(int i) {
        Editable text = this.mSearchSrcTextView.getText();
        Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor == null) {
            return;
        }
        if (!cursor.moveToPosition(i)) {
            setQuery(text);
            return;
        }
        CharSequence charSequenceConvertToString = this.mSuggestionsAdapter.convertToString(cursor);
        if (charSequenceConvertToString != null) {
            setQuery(charSequenceConvertToString);
        } else {
            setQuery(text);
        }
    }

    private void setQuery(CharSequence charSequence) {
        this.mSearchSrcTextView.setText(charSequence);
        this.mSearchSrcTextView.setSelection(TextUtils.isEmpty(charSequence) ? 0 : charSequence.length());
    }

    private void updateCloseButton() {
        boolean z = !TextUtils.isEmpty(this.mSearchSrcTextView.getText());
        boolean z2 = true;
        if (!z) {
            z2 = this.mIconifiedByDefault && !this.mExpandedInActionView;
        }
        this.mCloseButton.setVisibility(z2 ? 0 : 8);
        Drawable drawable = this.mCloseButton.getDrawable();
        if (drawable != null) {
            drawable.setState(z ? ENABLED_STATE_SET : EMPTY_STATE_SET);
        }
    }

    private void updateQueryHint() {
        CharSequence queryHint = getQueryHint();
        SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
        CharSequence charSequence = queryHint;
        if (queryHint == null) {
            charSequence = BuildConfig.FLAVOR;
        }
        searchAutoComplete.setHint(getDecoratedHint(charSequence));
    }

    private void updateSearchAutoComplete() {
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int inputType = this.mSearchable.getInputType();
        int i = inputType;
        if ((inputType & 15) == 1) {
            int i2 = inputType & (-65537);
            i = i2;
            if (this.mSearchable.getSuggestAuthority() != null) {
                i = i2 | 65536 | 524288;
            }
        }
        this.mSearchSrcTextView.setInputType(i);
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter != null) {
            cursorAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            SuggestionsAdapter suggestionsAdapter = new SuggestionsAdapter(getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSuggestionsAdapter = suggestionsAdapter;
            this.mSearchSrcTextView.setAdapter(suggestionsAdapter);
            SuggestionsAdapter suggestionsAdapter2 = (SuggestionsAdapter) this.mSuggestionsAdapter;
            int i3 = 1;
            if (this.mQueryRefinement) {
                i3 = 2;
            }
            suggestionsAdapter2.setQueryRefinement(i3);
        }
    }

    private void updateSubmitArea() {
        this.mSubmitArea.setVisibility((isSubmitAreaEnabled() && (this.mGoButton.getVisibility() == 0 || this.mVoiceButton.getVisibility() == 0)) ? 0 : 8);
    }

    private void updateSubmitButton(boolean z) {
        this.mGoButton.setVisibility((this.mSubmitButtonEnabled && isSubmitAreaEnabled() && hasFocus() && (z || !this.mVoiceButtonEnabled)) ? 0 : 8);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateViewsVisibility(boolean r5) {
        /*
            r4 = this;
            r0 = r4
            r1 = r5
            r0.mIconified = r1
            r0 = 0
            r7 = r0
            r0 = r5
            if (r0 == 0) goto L10
            r0 = 0
            r6 = r0
            goto L13
        L10:
            r0 = 8
            r6 = r0
        L13:
            r0 = r4
            android.support.v7.widget.SearchView$SearchAutoComplete r0 = r0.mSearchSrcTextView
            android.text.Editable r0 = r0.getText()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r1 = 1
            r0 = r0 ^ r1
            r8 = r0
            r0 = r4
            android.widget.ImageView r0 = r0.mSearchButton
            r1 = r6
            r0.setVisibility(r1)
            r0 = r4
            r1 = r8
            r0.updateSubmitButton(r1)
            r0 = r4
            android.view.View r0 = r0.mSearchEditFrame
            r9 = r0
            r0 = r5
            if (r0 == 0) goto L3f
            r0 = 8
            r6 = r0
            goto L41
        L3f:
            r0 = 0
            r6 = r0
        L41:
            r0 = r9
            r1 = r6
            r0.setVisibility(r1)
            r0 = r4
            android.widget.ImageView r0 = r0.mCollapsedIcon
            android.graphics.drawable.Drawable r0 = r0.getDrawable()
            if (r0 == 0) goto L5a
            r0 = r7
            r6 = r0
            r0 = r4
            boolean r0 = r0.mIconifiedByDefault
            if (r0 == 0) goto L5d
        L5a:
            r0 = 8
            r6 = r0
        L5d:
            r0 = r4
            android.widget.ImageView r0 = r0.mCollapsedIcon
            r1 = r6
            r0.setVisibility(r1)
            r0 = r4
            r0.updateCloseButton()
            r0 = r4
            r1 = r8
            r2 = 1
            r1 = r1 ^ r2
            r0.updateVoiceButton(r1)
            r0 = r4
            r0.updateSubmitArea()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.SearchView.updateViewsVisibility(boolean):void");
    }

    private void updateVoiceButton(boolean z) {
        int i = 8;
        if (this.mVoiceButtonEnabled) {
            i = 8;
            if (!isIconified()) {
                i = 8;
                if (z) {
                    this.mGoButton.setVisibility(8);
                    i = 0;
                }
            }
        }
        this.mVoiceButton.setVisibility(i);
    }

    void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            Resources resources = getContext().getResources();
            int paddingLeft = this.mSearchPlate.getPaddingLeft();
            Rect rect = new Rect();
            boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
            int dimensionPixelSize = this.mIconifiedByDefault ? resources.getDimensionPixelSize(C0287R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(C0287R.dimen.abc_dropdownitem_text_padding_left) : 0;
            this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
            this.mSearchSrcTextView.setDropDownHorizontalOffset(zIsLayoutRtl ? -rect.left : paddingLeft - (rect.left + dimensionPixelSize));
            this.mSearchSrcTextView.setDropDownWidth((((this.mDropDownAnchor.getWidth() + rect.left) + rect.right) + dimensionPixelSize) - paddingLeft);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void clearFocus() {
        this.mClearingFocus = true;
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mSearchSrcTextView.setImeVisibility(false);
        this.mClearingFocus = false;
    }

    void forceSuggestionQuery() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
    }

    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }

    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public CharSequence getQuery() {
        return this.mSearchSrcTextView.getText();
    }

    public CharSequence getQueryHint() {
        CharSequence text = this.mQueryHint;
        if (text == null) {
            SearchableInfo searchableInfo = this.mSearchable;
            text = (searchableInfo == null || searchableInfo.getHintId() == 0) ? this.mDefaultQueryHint : getContext().getText(this.mSearchable.getHintId());
        }
        return text;
    }

    int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }

    int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }

    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }

    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }

    public boolean isIconified() {
        return this.mIconified;
    }

    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }

    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }

    void launchQuerySearch(int i, String str, String str2) {
        getContext().startActivity(createIntent("android.intent.action.SEARCH", null, null, str2, i, str));
    }

    @Override // android.support.v7.view.CollapsibleActionView
    public void onActionViewCollapsed() {
        setQuery(BuildConfig.FLAVOR, false);
        clearFocus();
        updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }

    @Override // android.support.v7.view.CollapsibleActionView
    public void onActionViewExpanded() {
        if (this.mExpandedInActionView) {
            return;
        }
        this.mExpandedInActionView = true;
        int imeOptions = this.mSearchSrcTextView.getImeOptions();
        this.mCollapsedImeOptions = imeOptions;
        this.mSearchSrcTextView.setImeOptions(imeOptions | 33554432);
        this.mSearchSrcTextView.setText(BuildConfig.FLAVOR);
        setIconified(false);
    }

    void onCloseClicked() {
        if (!TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
            this.mSearchSrcTextView.setText(BuildConfig.FLAVOR);
            this.mSearchSrcTextView.requestFocus();
            this.mSearchSrcTextView.setImeVisibility(true);
        } else if (this.mIconifiedByDefault) {
            OnCloseListener onCloseListener = this.mOnCloseListener;
            if (onCloseListener == null || !onCloseListener.onClose()) {
                clearFocus();
                updateViewsVisibility(true);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.mUpdateDrawableStateRunnable);
        post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }

    boolean onItemClicked(int i, int i2, String str) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null && onSuggestionListener.onSuggestionClick(i)) {
            return false;
        }
        launchSuggestion(i, 0, null);
        this.mSearchSrcTextView.setImeVisibility(false);
        dismissSuggestions();
        return true;
    }

    boolean onItemSelected(int i) {
        OnSuggestionListener onSuggestionListener = this.mOnSuggestionListener;
        if (onSuggestionListener != null && onSuggestionListener.onSuggestionSelect(i)) {
            return false;
        }
        rewriteQueryFromSuggestion(i);
        return true;
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            getChildBoundsWithinSearchView(this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
            this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, i4 - i2);
            UpdatableTouchDelegate updatableTouchDelegate = this.mTouchDelegate;
            if (updatableTouchDelegate != null) {
                updatableTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
                return;
            }
            UpdatableTouchDelegate updatableTouchDelegate2 = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, this.mSearchSrcTextView);
            this.mTouchDelegate = updatableTouchDelegate2;
            setTouchDelegate(updatableTouchDelegate2);
        }
    }

    @Override // android.support.v7.widget.LinearLayoutCompat, android.view.View
    protected void onMeasure(int i, int i2) {
        int iMin;
        if (isIconified()) {
            super.onMeasure(i, i2);
            return;
        }
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == Integer.MIN_VALUE) {
            int i3 = this.mMaxWidth;
            iMin = i3 > 0 ? Math.min(i3, size) : Math.min(getPreferredWidth(), size);
        } else if (mode == 0) {
            iMin = this.mMaxWidth;
            if (iMin <= 0) {
                iMin = getPreferredWidth();
            }
        } else if (mode != 1073741824) {
            iMin = size;
        } else {
            int i4 = this.mMaxWidth;
            iMin = size;
            if (i4 > 0) {
                iMin = Math.min(i4, size);
            }
        }
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode2 == Integer.MIN_VALUE) {
            size2 = Math.min(getPreferredHeight(), size2);
        } else if (mode2 == 0) {
            size2 = getPreferredHeight();
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(iMin, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
    }

    void onQueryRefine(CharSequence charSequence) {
        setQuery(charSequence);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        updateViewsVisibility(savedState.isIconified);
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isIconified = isIconified();
        return savedState;
    }

    void onSearchClicked() {
        updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.mSearchSrcTextView.setImeVisibility(true);
        View.OnClickListener onClickListener = this.mOnSearchClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    void onSubmitQuery() {
        Editable text = this.mSearchSrcTextView.getText();
        if (text == null || TextUtils.getTrimmedLength(text) <= 0) {
            return;
        }
        OnQueryTextListener onQueryTextListener = this.mOnQueryChangeListener;
        if (onQueryTextListener == null || !onQueryTextListener.onQueryTextSubmit(text.toString())) {
            if (this.mSearchable != null) {
                launchQuerySearch(0, null, text.toString());
            }
            this.mSearchSrcTextView.setImeVisibility(false);
            dismissSuggestions();
        }
    }

    boolean onSuggestionsKey(View view, int i, KeyEvent keyEvent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (this.mSearchable == null || this.mSuggestionsAdapter == null || keyEvent.getAction() != 0 || !keyEvent.hasNoModifiers()) {
            return false;
        }
        if (i == 66 || i == 84 || i == 61) {
            return onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, null);
        }
        if (i != 21 && i != 22) {
            return (i == 19 && this.mSearchSrcTextView.getListSelection() == 0) ? false : false;
        }
        this.mSearchSrcTextView.setSelection(i == 21 ? 0 : this.mSearchSrcTextView.length());
        this.mSearchSrcTextView.setListSelection(0);
        this.mSearchSrcTextView.clearListSelection();
        HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
        return true;
    }

    void onTextChanged(CharSequence charSequence) {
        Editable text = this.mSearchSrcTextView.getText();
        this.mUserQuery = text;
        boolean z = !TextUtils.isEmpty(text);
        updateSubmitButton(z);
        updateVoiceButton(!z);
        updateCloseButton();
        updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals(charSequence, this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(charSequence.toString());
        }
        this.mOldQueryText = charSequence.toString();
    }

    void onTextFocusChanged() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        updateViewsVisibility(isIconified());
        postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            forceSuggestionQuery();
        }
    }

    void onVoiceClicked() {
        SearchableInfo searchableInfo = this.mSearchable;
        if (searchableInfo == null) {
            return;
        }
        try {
            if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                getContext().startActivity(createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, searchableInfo));
            } else if (searchableInfo.getVoiceSearchLaunchRecognizer()) {
                getContext().startActivity(createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, searchableInfo));
            }
        } catch (ActivityNotFoundException e) {
            Log.w(LOG_TAG, "Could not find voice search activity");
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        postUpdateFocusedState();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean requestFocus(int i, Rect rect) {
        if (this.mClearingFocus || !isFocusable()) {
            return false;
        }
        if (isIconified()) {
            return super.requestFocus(i, rect);
        }
        boolean zRequestFocus = this.mSearchSrcTextView.requestFocus(i, rect);
        if (zRequestFocus) {
            updateViewsVisibility(false);
        }
        return zRequestFocus;
    }

    public void setAppSearchData(Bundle bundle) {
        this.mAppSearchData = bundle;
    }

    public void setIconified(boolean z) {
        if (z) {
            onCloseClicked();
        } else {
            onSearchClicked();
        }
    }

    public void setIconifiedByDefault(boolean z) {
        if (this.mIconifiedByDefault == z) {
            return;
        }
        this.mIconifiedByDefault = z;
        updateViewsVisibility(z);
        updateQueryHint();
    }

    public void setImeOptions(int i) {
        this.mSearchSrcTextView.setImeOptions(i);
    }

    public void setInputType(int i) {
        this.mSearchSrcTextView.setInputType(i);
    }

    public void setMaxWidth(int i) {
        this.mMaxWidth = i;
        requestLayout();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mOnQueryTextFocusChangeListener = onFocusChangeListener;
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.mOnQueryChangeListener = onQueryTextListener;
    }

    public void setOnSearchClickListener(View.OnClickListener onClickListener) {
        this.mOnSearchClickListener = onClickListener;
    }

    public void setOnSuggestionListener(OnSuggestionListener onSuggestionListener) {
        this.mOnSuggestionListener = onSuggestionListener;
    }

    public void setQuery(CharSequence charSequence, boolean z) {
        this.mSearchSrcTextView.setText(charSequence);
        if (charSequence != null) {
            SearchAutoComplete searchAutoComplete = this.mSearchSrcTextView;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.mUserQuery = charSequence;
        }
        if (!z || TextUtils.isEmpty(charSequence)) {
            return;
        }
        onSubmitQuery();
    }

    public void setQueryHint(CharSequence charSequence) {
        this.mQueryHint = charSequence;
        updateQueryHint();
    }

    public void setQueryRefinementEnabled(boolean z) {
        this.mQueryRefinement = z;
        CursorAdapter cursorAdapter = this.mSuggestionsAdapter;
        if (cursorAdapter instanceof SuggestionsAdapter) {
            ((SuggestionsAdapter) cursorAdapter).setQueryRefinement(z ? 2 : 1);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        this.mSearchable = searchableInfo;
        if (searchableInfo != null) {
            updateSearchAutoComplete();
            updateQueryHint();
        }
        boolean zHasVoiceSearch = hasVoiceSearch();
        this.mVoiceButtonEnabled = zHasVoiceSearch;
        if (zHasVoiceSearch) {
            this.mSearchSrcTextView.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
        }
        updateViewsVisibility(isIconified());
    }

    public void setSubmitButtonEnabled(boolean z) {
        this.mSubmitButtonEnabled = z;
        updateViewsVisibility(isIconified());
    }

    public void setSuggestionsAdapter(CursorAdapter cursorAdapter) {
        this.mSuggestionsAdapter = cursorAdapter;
        this.mSearchSrcTextView.setAdapter(cursorAdapter);
    }

    void updateFocusedState() {
        int[] iArr = this.mSearchSrcTextView.hasFocus() ? FOCUSED_STATE_SET : EMPTY_STATE_SET;
        Drawable background = this.mSearchPlate.getBackground();
        if (background != null) {
            background.setState(iArr);
        }
        Drawable background2 = this.mSubmitArea.getBackground();
        if (background2 != null) {
            background2.setState(iArr);
        }
        invalidate();
    }
}
