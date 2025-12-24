package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ComputableLiveData.class */
public abstract class ComputableLiveData<T> {
    private AtomicBoolean mComputing;
    private final Executor mExecutor;
    private AtomicBoolean mInvalid;
    final Runnable mInvalidationRunnable;
    private final LiveData<T> mLiveData;
    final Runnable mRefreshRunnable;

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(Executor executor) {
        this.mInvalid = new AtomicBoolean(true);
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new Runnable(this) { // from class: android.arch.lifecycle.ComputableLiveData.2
            final ComputableLiveData this$0;

            {
                this.this$0 = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                do {
                    boolean z = false;
                    if (this.this$0.mComputing.compareAndSet(false, true)) {
                        T tCompute = null;
                        boolean z2 = false;
                        while (true) {
                            try {
                                z = z2;
                                if (!this.this$0.mInvalid.compareAndSet(true, false)) {
                                    break;
                                }
                                tCompute = this.this$0.compute();
                                z2 = true;
                            } finally {
                                this.this$0.mComputing.set(false);
                            }
                        }
                        if (z) {
                            this.this$0.mLiveData.postValue(tCompute);
                        }
                    }
                    if (!z) {
                        return;
                    }
                } while (this.this$0.mInvalid.get());
            }
        };
        this.mInvalidationRunnable = new Runnable(this) { // from class: android.arch.lifecycle.ComputableLiveData.3
            final ComputableLiveData this$0;

            {
                this.this$0 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean zHasActiveObservers = this.this$0.mLiveData.hasActiveObservers();
                if (this.this$0.mInvalid.compareAndSet(false, true) && zHasActiveObservers) {
                    this.this$0.mExecutor.execute(this.this$0.mRefreshRunnable);
                }
            }
        };
        this.mExecutor = executor;
        this.mLiveData = new LiveData<T>(this) { // from class: android.arch.lifecycle.ComputableLiveData.1
            final ComputableLiveData this$0;

            {
                this.this$0 = this;
            }

            @Override // android.arch.lifecycle.LiveData
            protected void onActive() {
                this.this$0.mExecutor.execute(this.this$0.mRefreshRunnable);
            }
        };
    }

    protected abstract T compute();

    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
