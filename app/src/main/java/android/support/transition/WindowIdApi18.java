package android.support.transition;

import android.view.View;
import android.view.WindowId;

/* loaded from: classes-dex2jar.jar:android/support/transition/WindowIdApi18.class */
class WindowIdApi18 implements WindowIdImpl {
    private final WindowId mWindowId;

    WindowIdApi18(View view) {
        this.mWindowId = view.getWindowId();
    }

    public boolean equals(Object obj) {
        return (obj instanceof WindowIdApi18) && ((WindowIdApi18) obj).mWindowId.equals(this.mWindowId);
    }

    public int hashCode() {
        return this.mWindowId.hashCode();
    }
}
