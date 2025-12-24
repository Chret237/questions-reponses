package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.design.C0029R;
import android.support.design.snackbar.ContentViewCallback;
import android.support.design.widget.BaseTransientBottomBar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/Snackbar.class */
public final class Snackbar extends BaseTransientBottomBar<Snackbar> {
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = {C0029R.attr.snackbarButtonStyle};
    private final AccessibilityManager accessibilityManager;
    private BaseTransientBottomBar.BaseCallback<Snackbar> callback;
    private boolean hasAction;

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/Snackbar$Callback.class */
    public static class Callback extends BaseTransientBottomBar.BaseCallback<Snackbar> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        @Override // android.support.design.widget.BaseTransientBottomBar.BaseCallback
        public void onDismissed(Snackbar snackbar, int i) {
        }

        @Override // android.support.design.widget.BaseTransientBottomBar.BaseCallback
        public void onShown(Snackbar snackbar) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/widget/Snackbar$Duration.class */
    public @interface Duration {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/Snackbar$SnackbarLayout.class */
    public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int childCount = getChildCount();
            int measuredWidth = getMeasuredWidth();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getLayoutParams().width == -1) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec((measuredWidth - paddingLeft) - paddingRight, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getMeasuredHeight(), 1073741824));
                }
            }
        }
    }

    private Snackbar(ViewGroup viewGroup, View view, ContentViewCallback contentViewCallback) {
        super(viewGroup, view, contentViewCallback);
        this.accessibilityManager = (AccessibilityManager) viewGroup.getContext().getSystemService("accessibility");
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        View view2 = view;
        while (!(view2 instanceof CoordinatorLayout)) {
            ViewGroup viewGroup2 = viewGroup;
            if (view2 instanceof FrameLayout) {
                if (view2.getId() == 16908290) {
                    return (ViewGroup) view2;
                }
                viewGroup2 = (ViewGroup) view2;
            }
            View view3 = view2;
            if (view2 != null) {
                Object parent = view2.getParent();
                view3 = parent instanceof View ? (View) parent : null;
            }
            viewGroup = viewGroup2;
            view2 = view3;
            if (view3 == null) {
                return viewGroup2;
            }
        }
        return (ViewGroup) view2;
    }

    protected static boolean hasSnackbarButtonStyleAttr(Context context) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
        boolean z = false;
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(0, -1);
        typedArrayObtainStyledAttributes.recycle();
        if (resourceId != -1) {
            z = true;
        }
        return z;
    }

    public static Snackbar make(View view, int i, int i2) {
        return make(view, view.getResources().getText(i), i2);
    }

    public static Snackbar make(View view, CharSequence charSequence, int i) {
        ViewGroup viewGroupFindSuitableParent = findSuitableParent(view);
        if (viewGroupFindSuitableParent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
        }
        SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout) LayoutInflater.from(viewGroupFindSuitableParent.getContext()).inflate(hasSnackbarButtonStyleAttr(viewGroupFindSuitableParent.getContext()) ? C0029R.layout.mtrl_layout_snackbar_include : C0029R.layout.design_layout_snackbar_include, viewGroupFindSuitableParent, false);
        Snackbar snackbar = new Snackbar(viewGroupFindSuitableParent, snackbarContentLayout, snackbarContentLayout);
        snackbar.setText(charSequence);
        snackbar.setDuration(i);
        return snackbar;
    }

    @Override // android.support.design.widget.BaseTransientBottomBar
    public void dismiss() {
        super.dismiss();
    }

    @Override // android.support.design.widget.BaseTransientBottomBar
    public int getDuration() {
        return (this.hasAction && this.accessibilityManager.isTouchExplorationEnabled()) ? -2 : super.getDuration();
    }

    @Override // android.support.design.widget.BaseTransientBottomBar
    public boolean isShown() {
        return super.isShown();
    }

    public Snackbar setAction(int i, View.OnClickListener onClickListener) {
        return setAction(getContext().getText(i), onClickListener);
    }

    public Snackbar setAction(CharSequence charSequence, View.OnClickListener onClickListener) {
        Button actionView = ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView();
        if (TextUtils.isEmpty(charSequence) || onClickListener == null) {
            actionView.setVisibility(8);
            actionView.setOnClickListener(null);
            this.hasAction = false;
        } else {
            this.hasAction = true;
            actionView.setVisibility(0);
            actionView.setText(charSequence);
            actionView.setOnClickListener(new View.OnClickListener(this, onClickListener) { // from class: android.support.design.widget.Snackbar.1
                final Snackbar this$0;
                final View.OnClickListener val$listener;

                {
                    this.this$0 = this;
                    this.val$listener = onClickListener;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.val$listener.onClick(view);
                    this.this$0.dispatchDismiss(1);
                }
            });
        }
        return this;
    }

    public Snackbar setActionTextColor(int i) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView().setTextColor(i);
        return this;
    }

    public Snackbar setActionTextColor(ColorStateList colorStateList) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getActionView().setTextColor(colorStateList);
        return this;
    }

    @Deprecated
    public Snackbar setCallback(Callback callback) {
        BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.callback;
        if (baseCallback != null) {
            removeCallback(baseCallback);
        }
        if (callback != null) {
            addCallback(callback);
        }
        this.callback = callback;
        return this;
    }

    public Snackbar setText(int i) {
        return setText(getContext().getText(i));
    }

    public Snackbar setText(CharSequence charSequence) {
        ((SnackbarContentLayout) this.view.getChildAt(0)).getMessageView().setText(charSequence);
        return this;
    }

    @Override // android.support.design.widget.BaseTransientBottomBar
    public void show() {
        super.show();
    }
}
