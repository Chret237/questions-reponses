package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/GenericLifecycleObserver.class */
public interface GenericLifecycleObserver extends LifecycleObserver {
    void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event);
}
