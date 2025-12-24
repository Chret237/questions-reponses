package android.arch.lifecycle;

@Deprecated
/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LifecycleRegistryOwner.class */
public interface LifecycleRegistryOwner extends LifecycleOwner {
    @Override // android.arch.lifecycle.LifecycleOwner
    LifecycleRegistry getLifecycle();
}
