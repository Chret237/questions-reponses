package android.arch.lifecycle;

import android.arch.core.util.Function;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/Transformations.class */
public class Transformations {

    /* JADX INFO: Add missing generic type declarations: [X] */
    /* renamed from: android.arch.lifecycle.Transformations$2 */
    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/Transformations$2.class */
    static final class C00122<X> implements Observer<X> {
        LiveData<Y> mSource;
        final Function val$func;
        final MediatorLiveData val$result;

        C00122(Function function, MediatorLiveData mediatorLiveData) {
            this.val$func = function;
            this.val$result = mediatorLiveData;
        }

        @Override // android.arch.lifecycle.Observer
        public void onChanged(X x) {
            LiveData<Y> liveData = (LiveData) this.val$func.apply(x);
            Object obj = this.mSource;
            if (obj == liveData) {
                return;
            }
            if (obj != null) {
                this.val$result.removeSource(obj);
            }
            this.mSource = liveData;
            if (liveData != 0) {
                this.val$result.addSource(liveData, new Observer<Y>(this) { // from class: android.arch.lifecycle.Transformations.2.1
                    final C00122 this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // android.arch.lifecycle.Observer
                    public void onChanged(Y y) {
                        this.this$0.val$result.setValue(y);
                    }
                });
            }
        }
    }

    private Transformations() {
    }

    public static <X, Y> LiveData<Y> map(LiveData<X> liveData, Function<X, Y> function) {
        MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(mediatorLiveData, function) { // from class: android.arch.lifecycle.Transformations.1
            final Function val$func;
            final MediatorLiveData val$result;

            {
                this.val$result = mediatorLiveData;
                this.val$func = function;
            }

            @Override // android.arch.lifecycle.Observer
            public void onChanged(X x) {
                this.val$result.setValue(this.val$func.apply(x));
            }
        });
        return mediatorLiveData;
    }

    public static <X, Y> LiveData<Y> switchMap(LiveData<X> liveData, Function<X, LiveData<Y>> function) {
        MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new C00122(function, mediatorLiveData));
        return mediatorLiveData;
    }
}
