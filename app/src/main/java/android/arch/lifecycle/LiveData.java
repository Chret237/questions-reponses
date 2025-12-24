package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.Lifecycle;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LiveData.class */
public abstract class LiveData<T> {
    private static final Object NOT_SET = new Object();
    static final int START_VERSION = -1;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private final Object mDataLock = new Object();
    private SafeIterableMap<Observer<T>, LiveData<T>.ObserverWrapper> mObservers = new SafeIterableMap<>();
    private int mActiveCount = 0;
    private volatile Object mData = NOT_SET;
    private volatile Object mPendingData = NOT_SET;
    private int mVersion = -1;
    private final Runnable mPostValueRunnable = new Runnable(this) { // from class: android.arch.lifecycle.LiveData.1
        final LiveData this$0;

        {
            this.this$0 = this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            Object obj;
            synchronized (this.this$0.mDataLock) {
                obj = this.this$0.mPendingData;
                this.this$0.mPendingData = LiveData.NOT_SET;
            }
            this.this$0.setValue(obj);
        }
    };

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LiveData$AlwaysActiveObserver.class */
    private class AlwaysActiveObserver extends LiveData<T>.ObserverWrapper {
        final LiveData this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AlwaysActiveObserver(LiveData liveData, Observer<T> observer) {
            super(liveData, observer);
            this.this$0 = liveData;
        }

        @Override // android.arch.lifecycle.LiveData.ObserverWrapper
        boolean shouldBeActive() {
            return true;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LiveData$LifecycleBoundObserver.class */
    class LifecycleBoundObserver extends LiveData<T>.ObserverWrapper implements GenericLifecycleObserver {
        final LifecycleOwner mOwner;
        final LiveData this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        LifecycleBoundObserver(LiveData liveData, LifecycleOwner lifecycleOwner, Observer<T> observer) {
            super(liveData, observer);
            this.this$0 = liveData;
            this.mOwner = lifecycleOwner;
        }

        @Override // android.arch.lifecycle.LiveData.ObserverWrapper
        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }

        @Override // android.arch.lifecycle.LiveData.ObserverWrapper
        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return this.mOwner == lifecycleOwner;
        }

        @Override // android.arch.lifecycle.GenericLifecycleObserver
        public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                this.this$0.removeObserver(this.mObserver);
            } else {
                activeStateChanged(shouldBeActive());
            }
        }

        @Override // android.arch.lifecycle.LiveData.ObserverWrapper
        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LiveData$ObserverWrapper.class */
    private abstract class ObserverWrapper {
        boolean mActive;
        int mLastVersion = -1;
        final Observer<T> mObserver;
        final LiveData this$0;

        ObserverWrapper(LiveData liveData, Observer<T> observer) {
            this.this$0 = liveData;
            this.mObserver = observer;
        }

        void activeStateChanged(boolean z) {
            if (z == this.mActive) {
                return;
            }
            this.mActive = z;
            int i = 1;
            boolean z2 = this.this$0.mActiveCount == 0;
            LiveData liveData = this.this$0;
            int i2 = liveData.mActiveCount;
            if (!this.mActive) {
                i = -1;
            }
            liveData.mActiveCount = i2 + i;
            if (z2 && this.mActive) {
                this.this$0.onActive();
            }
            if (this.this$0.mActiveCount == 0 && !this.mActive) {
                this.this$0.onInactive();
            }
            if (this.mActive) {
                this.this$0.dispatchingValue(this);
            }
        }

        void detachObserver() {
        }

        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return false;
        }

        abstract boolean shouldBeActive();
    }

    private static void assertMainThread(String str) {
        if (ArchTaskExecutor.getInstance().isMainThread()) {
            return;
        }
        throw new IllegalStateException("Cannot invoke " + str + " on a background thread");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void considerNotify(LiveData<T>.ObserverWrapper observerWrapper) {
        if (observerWrapper.mActive) {
            if (!observerWrapper.shouldBeActive()) {
                observerWrapper.activeStateChanged(false);
                return;
            }
            int i = observerWrapper.mLastVersion;
            int i2 = this.mVersion;
            if (i >= i2) {
                return;
            }
            observerWrapper.mLastVersion = i2;
            observerWrapper.mObserver.onChanged(this.mData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchingValue(LiveData<T>.ObserverWrapper observerWrapper) {
        LiveData<T>.ObserverWrapper observerWrapper2;
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        LiveData<T>.ObserverWrapper observerWrapper3 = observerWrapper;
        do {
            this.mDispatchInvalidated = false;
            if (observerWrapper3 == null) {
                SafeIterableMap<Observer<T>, LiveData<T>.ObserverWrapper>.IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
                while (true) {
                    observerWrapper2 = observerWrapper3;
                    if (!iteratorWithAdditions.hasNext()) {
                        break;
                    }
                    considerNotify((ObserverWrapper) iteratorWithAdditions.next().getValue());
                    if (this.mDispatchInvalidated) {
                        observerWrapper2 = observerWrapper3;
                        break;
                    }
                }
            } else {
                considerNotify(observerWrapper3);
                observerWrapper2 = null;
            }
            observerWrapper3 = observerWrapper2;
        } while (this.mDispatchInvalidated);
        this.mDispatchingValue = false;
    }

    public T getValue() {
        T t = (T) this.mData;
        if (t != NOT_SET) {
            return t;
        }
        return null;
    }

    int getVersion() {
        return this.mVersion;
    }

    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }

    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }

    public void observe(LifecycleOwner lifecycleOwner, Observer<T> observer) {
        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(this, lifecycleOwner, observer);
        LiveData<T>.ObserverWrapper observerWrapperPutIfAbsent = this.mObservers.putIfAbsent(observer, lifecycleBoundObserver);
        if (observerWrapperPutIfAbsent != null && !observerWrapperPutIfAbsent.isAttachedTo(lifecycleOwner)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapperPutIfAbsent != null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
    }

    public void observeForever(Observer<T> observer) {
        AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(this, observer);
        LiveData<T>.ObserverWrapper observerWrapperPutIfAbsent = this.mObservers.putIfAbsent(observer, alwaysActiveObserver);
        if (observerWrapperPutIfAbsent != null && (observerWrapperPutIfAbsent instanceof LifecycleBoundObserver)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapperPutIfAbsent != null) {
            return;
        }
        alwaysActiveObserver.activeStateChanged(true);
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    protected void postValue(T t) {
        boolean z;
        synchronized (this.mDataLock) {
            z = this.mPendingData == NOT_SET;
            this.mPendingData = t;
        }
        if (z) {
            ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
        }
    }

    public void removeObserver(Observer<T> observer) {
        assertMainThread("removeObserver");
        LiveData<T>.ObserverWrapper observerWrapperRemove = this.mObservers.remove(observer);
        if (observerWrapperRemove == null) {
            return;
        }
        observerWrapperRemove.detachObserver();
        observerWrapperRemove.activeStateChanged(false);
    }

    public void removeObservers(LifecycleOwner lifecycleOwner) {
        assertMainThread("removeObservers");
        Iterator<Map.Entry<Observer<T>, LiveData<T>.ObserverWrapper>> it = this.mObservers.iterator();
        while (it.hasNext()) {
            Map.Entry<Observer<T>, LiveData<T>.ObserverWrapper> next = it.next();
            if (next.getValue().isAttachedTo(lifecycleOwner)) {
                removeObserver(next.getKey());
            }
        }
    }

    protected void setValue(T t) {
        assertMainThread("setValue");
        this.mVersion++;
        this.mData = t;
        dispatchingValue(null);
    }
}
