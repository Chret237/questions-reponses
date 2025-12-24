package android.support.v7.recyclerview.extensions;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes-dex2jar.jar:android/support/v7/recyclerview/extensions/AsyncListDiffer.class */
public class AsyncListDiffer<T> {
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    final Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;

    /* renamed from: android.support.v7.recyclerview.extensions.AsyncListDiffer$1 */
    /* loaded from: classes-dex2jar.jar:android/support/v7/recyclerview/extensions/AsyncListDiffer$1.class */
    class RunnableC02921 implements Runnable {
        final AsyncListDiffer this$0;
        final List val$newList;
        final List val$oldList;
        final int val$runGeneration;

        RunnableC02921(AsyncListDiffer asyncListDiffer, List list, List list2, int i) {
            this.this$0 = asyncListDiffer;
            this.val$oldList = list;
            this.val$newList = list2;
            this.val$runGeneration = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.this$0.mMainThreadExecutor.execute(new Runnable(this, DiffUtil.calculateDiff(new DiffUtil.Callback(this) { // from class: android.support.v7.recyclerview.extensions.AsyncListDiffer.1.1
                final RunnableC02921 this$1;

                {
                    this.this$1 = this;
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // android.support.v7.util.DiffUtil.Callback
                public boolean areContentsTheSame(int i, int i2) {
                    Object obj = this.this$1.val$oldList.get(i);
                    Object obj2 = this.this$1.val$newList.get(i2);
                    if (obj != null && obj2 != null) {
                        return this.this$1.this$0.mConfig.getDiffCallback().areContentsTheSame(obj, obj2);
                    }
                    if (obj == null && obj2 == null) {
                        return true;
                    }
                    throw new AssertionError();
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // android.support.v7.util.DiffUtil.Callback
                public boolean areItemsTheSame(int i, int i2) {
                    Object obj = this.this$1.val$oldList.get(i);
                    Object obj2 = this.this$1.val$newList.get(i2);
                    if (obj == null || obj2 == null) {
                        return obj == null && obj2 == null;
                    }
                    return this.this$1.this$0.mConfig.getDiffCallback().areItemsTheSame(obj, obj2);
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // android.support.v7.util.DiffUtil.Callback
                public Object getChangePayload(int i, int i2) {
                    Object obj = this.this$1.val$oldList.get(i);
                    Object obj2 = this.this$1.val$newList.get(i2);
                    if (obj == null || obj2 == null) {
                        throw new AssertionError();
                    }
                    return this.this$1.this$0.mConfig.getDiffCallback().getChangePayload(obj, obj2);
                }

                @Override // android.support.v7.util.DiffUtil.Callback
                public int getNewListSize() {
                    return this.this$1.val$newList.size();
                }

                @Override // android.support.v7.util.DiffUtil.Callback
                public int getOldListSize() {
                    return this.this$1.val$oldList.size();
                }
            })) { // from class: android.support.v7.recyclerview.extensions.AsyncListDiffer.1.2
                final RunnableC02921 this$1;
                final DiffUtil.DiffResult val$result;

                {
                    this.this$1 = this;
                    this.val$result = diffResult;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (this.this$1.this$0.mMaxScheduledGeneration == this.this$1.val$runGeneration) {
                        this.this$1.this$0.latchList(this.this$1.val$newList, this.val$result);
                    }
                }
            });
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/recyclerview/extensions/AsyncListDiffer$MainThreadExecutor.class */
    private static class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    public AsyncListDiffer(ListUpdateCallback listUpdateCallback, AsyncDifferConfig<T> asyncDifferConfig) {
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        if (asyncDifferConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor();
        } else {
            this.mMainThreadExecutor = sMainThreadExecutor;
        }
    }

    public AsyncListDiffer(RecyclerView.Adapter adapter, DiffUtil.ItemCallback<T> itemCallback) {
        this(new AdapterListUpdateCallback(adapter), new AsyncDifferConfig.Builder(itemCallback).build());
    }

    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    void latchList(List<T> list, DiffUtil.DiffResult diffResult) {
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }

    public void submitList(List<T> list) {
        int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        List<T> list2 = this.mList;
        if (list == list2) {
            return;
        }
        if (list == null) {
            int size = list2.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            return;
        }
        if (list2 != null) {
            this.mConfig.getBackgroundThreadExecutor().execute(new RunnableC02921(this, list2, list, i));
            return;
        }
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        this.mUpdateCallback.onInserted(0, list.size());
    }
}
