package android.support.v7.app;

import android.support.v7.view.ActionMode;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatCallback.class */
public interface AppCompatCallback {
    void onSupportActionModeFinished(ActionMode actionMode);

    void onSupportActionModeStarted(ActionMode actionMode);

    ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback);
}
