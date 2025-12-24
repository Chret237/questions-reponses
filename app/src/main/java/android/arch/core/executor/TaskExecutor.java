package android.arch.core.executor;

/* loaded from: classes-dex2jar.jar:android/arch/core/executor/TaskExecutor.class */
public abstract class TaskExecutor {
    public abstract void executeOnDiskIO(Runnable runnable);

    public void executeOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }

    public abstract boolean isMainThread();

    public abstract void postToMainThread(Runnable runnable);
}
