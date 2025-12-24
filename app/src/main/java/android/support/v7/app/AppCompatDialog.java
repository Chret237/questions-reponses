package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v7.appcompat.C0287R;
import android.support.v7.view.ActionMode;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatDialog.class */
public class AppCompatDialog extends Dialog implements AppCompatCallback {
    private AppCompatDelegate mDelegate;
    private final KeyEventDispatcher.Component mKeyDispatcher;

    public AppCompatDialog(Context context) {
        this(context, 0);
    }

    public AppCompatDialog(Context context, int i) {
        super(context, getThemeResId(context, i));
        this.mKeyDispatcher = new KeyEventDispatcher.Component(this) { // from class: android.support.v7.app.AppCompatDialog.1
            final AppCompatDialog this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.view.KeyEventDispatcher.Component
            public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return this.this$0.superDispatchKeyEvent(keyEvent);
            }
        };
        getDelegate().onCreate(null);
        getDelegate().applyDayNight();
    }

    protected AppCompatDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.mKeyDispatcher = new KeyEventDispatcher.Component(this) { // from class: android.support.v7.app.AppCompatDialog.1
            final AppCompatDialog this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.view.KeyEventDispatcher.Component
            public boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return this.this$0.superDispatchKeyEvent(keyEvent);
            }
        };
    }

    private static int getThemeResId(Context context, int i) {
        int i2 = i;
        if (i == 0) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(C0287R.attr.dialogTheme, typedValue, true);
            i2 = typedValue.resourceId;
        }
        return i2;
    }

    @Override // android.app.Dialog
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        getDelegate().addContentView(view, layoutParams);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventDispatcher.dispatchKeyEvent(this.mKeyDispatcher, getWindow().getDecorView(), this, keyEvent);
    }

    @Override // android.app.Dialog
    public <T extends View> T findViewById(int i) {
        return (T) getDelegate().findViewById(i);
    }

    public AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create(this, this);
        }
        return this.mDelegate;
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    @Override // android.app.Dialog
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        getDelegate().installViewFactory();
        super.onCreate(bundle);
        getDelegate().onCreate(bundle);
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override // android.support.v7.app.AppCompatCallback
    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    @Override // android.support.v7.app.AppCompatCallback
    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    @Override // android.support.v7.app.AppCompatCallback
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override // android.app.Dialog
    public void setContentView(int i) {
        getDelegate().setContentView(i);
    }

    @Override // android.app.Dialog
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override // android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        getDelegate().setContentView(view, layoutParams);
    }

    @Override // android.app.Dialog
    public void setTitle(int i) {
        super.setTitle(i);
        getDelegate().setTitle(getContext().getString(i));
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        getDelegate().setTitle(charSequence);
    }

    boolean superDispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean supportRequestWindowFeature(int i) {
        return getDelegate().requestWindowFeature(i);
    }
}
