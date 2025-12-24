package android.support.v7.util;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.util.ThreadUtil;
import android.support.v7.util.TileList;
import android.util.Log;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes-dex2jar.jar:android/support/v7/util/MessageThreadUtil.class */
class MessageThreadUtil<T> implements ThreadUtil<T> {

    /* renamed from: android.support.v7.util.MessageThreadUtil$1 */
    /* loaded from: classes-dex2jar.jar:android/support/v7/util/MessageThreadUtil$1.class */
    class C02961 implements ThreadUtil.MainThreadCallback<T> {
        static final int ADD_TILE = 2;
        static final int REMOVE_TILE = 3;
        static final int UPDATE_ITEM_COUNT = 1;
        final MessageThreadUtil this$0;
        final ThreadUtil.MainThreadCallback val$callback;
        final MessageQueue mQueue = new MessageQueue();
        private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
        private Runnable mMainThreadRunnable = new Runnable(this) { // from class: android.support.v7.util.MessageThreadUtil.1.1
            final C02961 this$1;

            {
                this.this$1 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                SyncQueueItem next = this.this$1.mQueue.next();
                while (true) {
                    SyncQueueItem syncQueueItem = next;
                    if (syncQueueItem == null) {
                        return;
                    }
                    int i = syncQueueItem.what;
                    if (i == 1) {
                        this.this$1.val$callback.updateItemCount(syncQueueItem.arg1, syncQueueItem.arg2);
                    } else if (i == 2) {
                        this.this$1.val$callback.addTile(syncQueueItem.arg1, (TileList.Tile) syncQueueItem.data);
                    } else if (i != 3) {
                        Log.e("ThreadUtil", "Unsupported message, what=" + syncQueueItem.what);
                    } else {
                        this.this$1.val$callback.removeTile(syncQueueItem.arg1, syncQueueItem.arg2);
                    }
                    next = this.this$1.mQueue.next();
                }
            }
        };

        C02961(MessageThreadUtil messageThreadUtil, ThreadUtil.MainThreadCallback mainThreadCallback) {
            this.this$0 = messageThreadUtil;
            this.val$callback = mainThreadCallback;
        }

        private void sendMessage(SyncQueueItem syncQueueItem) {
            this.mQueue.sendMessage(syncQueueItem);
            this.mMainThreadHandler.post(this.mMainThreadRunnable);
        }

        @Override // android.support.v7.util.ThreadUtil.MainThreadCallback
        public void addTile(int i, TileList.Tile<T> tile) {
            sendMessage(SyncQueueItem.obtainMessage(2, i, tile));
        }

        @Override // android.support.v7.util.ThreadUtil.MainThreadCallback
        public void removeTile(int i, int i2) {
            sendMessage(SyncQueueItem.obtainMessage(3, i, i2));
        }

        @Override // android.support.v7.util.ThreadUtil.MainThreadCallback
        public void updateItemCount(int i, int i2) {
            sendMessage(SyncQueueItem.obtainMessage(1, i, i2));
        }
    }

    /* renamed from: android.support.v7.util.MessageThreadUtil$2 */
    /* loaded from: classes-dex2jar.jar:android/support/v7/util/MessageThreadUtil$2.class */
    class C02972 implements ThreadUtil.BackgroundCallback<T> {
        static final int LOAD_TILE = 3;
        static final int RECYCLE_TILE = 4;
        static final int REFRESH = 1;
        static final int UPDATE_RANGE = 2;
        final MessageThreadUtil this$0;
        final ThreadUtil.BackgroundCallback val$callback;
        final MessageQueue mQueue = new MessageQueue();
        private final Executor mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        AtomicBoolean mBackgroundRunning = new AtomicBoolean(false);
        private Runnable mBackgroundRunnable = new Runnable(this) { // from class: android.support.v7.util.MessageThreadUtil.2.1
            final C02972 this$1;

            {
                this.this$1 = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    SyncQueueItem next = this.this$1.mQueue.next();
                    if (next == null) {
                        this.this$1.mBackgroundRunning.set(false);
                        return;
                    }
                    int i = next.what;
                    if (i == 1) {
                        this.this$1.mQueue.removeMessages(1);
                        this.this$1.val$callback.refresh(next.arg1);
                    } else if (i == 2) {
                        this.this$1.mQueue.removeMessages(2);
                        this.this$1.mQueue.removeMessages(3);
                        this.this$1.val$callback.updateRange(next.arg1, next.arg2, next.arg3, next.arg4, next.arg5);
                    } else if (i == 3) {
                        this.this$1.val$callback.loadTile(next.arg1, next.arg2);
                    } else if (i != 4) {
                        Log.e("ThreadUtil", "Unsupported message, what=" + next.what);
                    } else {
                        this.this$1.val$callback.recycleTile((TileList.Tile) next.data);
                    }
                }
            }
        };

        C02972(MessageThreadUtil messageThreadUtil, ThreadUtil.BackgroundCallback backgroundCallback) {
            this.this$0 = messageThreadUtil;
            this.val$callback = backgroundCallback;
        }

        private void maybeExecuteBackgroundRunnable() {
            if (this.mBackgroundRunning.compareAndSet(false, true)) {
                this.mExecutor.execute(this.mBackgroundRunnable);
            }
        }

        private void sendMessage(SyncQueueItem syncQueueItem) {
            this.mQueue.sendMessage(syncQueueItem);
            maybeExecuteBackgroundRunnable();
        }

        private void sendMessageAtFrontOfQueue(SyncQueueItem syncQueueItem) {
            this.mQueue.sendMessageAtFrontOfQueue(syncQueueItem);
            maybeExecuteBackgroundRunnable();
        }

        @Override // android.support.v7.util.ThreadUtil.BackgroundCallback
        public void loadTile(int i, int i2) {
            sendMessage(SyncQueueItem.obtainMessage(3, i, i2));
        }

        @Override // android.support.v7.util.ThreadUtil.BackgroundCallback
        public void recycleTile(TileList.Tile<T> tile) {
            sendMessage(SyncQueueItem.obtainMessage(4, 0, tile));
        }

        @Override // android.support.v7.util.ThreadUtil.BackgroundCallback
        public void refresh(int i) {
            sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(1, i, (Object) null));
        }

        @Override // android.support.v7.util.ThreadUtil.BackgroundCallback
        public void updateRange(int i, int i2, int i3, int i4, int i5) {
            sendMessageAtFrontOfQueue(SyncQueueItem.obtainMessage(2, i, i2, i3, i4, i5, null));
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/util/MessageThreadUtil$MessageQueue.class */
    static class MessageQueue {
        private SyncQueueItem mRoot;

        MessageQueue() {
        }

        SyncQueueItem next() {
            synchronized (this) {
                if (this.mRoot == null) {
                    return null;
                }
                SyncQueueItem syncQueueItem = this.mRoot;
                this.mRoot = this.mRoot.next;
                return syncQueueItem;
            }
        }

        void removeMessages(int i) {
            synchronized (this) {
                while (this.mRoot != null && this.mRoot.what == i) {
                    SyncQueueItem syncQueueItem = this.mRoot;
                    this.mRoot = this.mRoot.next;
                    syncQueueItem.recycle();
                }
                if (this.mRoot != null) {
                    SyncQueueItem syncQueueItem2 = this.mRoot;
                    SyncQueueItem syncQueueItem3 = syncQueueItem2.next;
                    while (true) {
                        SyncQueueItem syncQueueItem4 = syncQueueItem3;
                        if (syncQueueItem4 == null) {
                            break;
                        }
                        SyncQueueItem syncQueueItem5 = syncQueueItem4.next;
                        if (syncQueueItem4.what == i) {
                            syncQueueItem2.next = syncQueueItem5;
                            syncQueueItem4.recycle();
                        } else {
                            syncQueueItem2 = syncQueueItem4;
                        }
                        syncQueueItem3 = syncQueueItem5;
                    }
                }
            }
        }

        void sendMessage(SyncQueueItem syncQueueItem) {
            synchronized (this) {
                if (this.mRoot == null) {
                    this.mRoot = syncQueueItem;
                    return;
                }
                SyncQueueItem syncQueueItem2 = this.mRoot;
                while (syncQueueItem2.next != null) {
                    syncQueueItem2 = syncQueueItem2.next;
                }
                syncQueueItem2.next = syncQueueItem;
            }
        }

        void sendMessageAtFrontOfQueue(SyncQueueItem syncQueueItem) {
            synchronized (this) {
                syncQueueItem.next = this.mRoot;
                this.mRoot = syncQueueItem;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/util/MessageThreadUtil$SyncQueueItem.class */
    static class SyncQueueItem {
        private static SyncQueueItem sPool;
        private static final Object sPoolLock = new Object();
        public int arg1;
        public int arg2;
        public int arg3;
        public int arg4;
        public int arg5;
        public Object data;
        SyncQueueItem next;
        public int what;

        SyncQueueItem() {
        }

        static SyncQueueItem obtainMessage(int i, int i2, int i3) {
            return obtainMessage(i, i2, i3, 0, 0, 0, null);
        }

        static SyncQueueItem obtainMessage(int i, int i2, int i3, int i4, int i5, int i6, Object obj) {
            SyncQueueItem syncQueueItem;
            synchronized (sPoolLock) {
                if (sPool == null) {
                    syncQueueItem = new SyncQueueItem();
                } else {
                    syncQueueItem = sPool;
                    sPool = sPool.next;
                    syncQueueItem.next = null;
                }
                syncQueueItem.what = i;
                syncQueueItem.arg1 = i2;
                syncQueueItem.arg2 = i3;
                syncQueueItem.arg3 = i4;
                syncQueueItem.arg4 = i5;
                syncQueueItem.arg5 = i6;
                syncQueueItem.data = obj;
            }
            return syncQueueItem;
        }

        static SyncQueueItem obtainMessage(int i, int i2, Object obj) {
            return obtainMessage(i, i2, 0, 0, 0, 0, obj);
        }

        void recycle() {
            this.next = null;
            this.arg5 = 0;
            this.arg4 = 0;
            this.arg3 = 0;
            this.arg2 = 0;
            this.arg1 = 0;
            this.what = 0;
            this.data = null;
            synchronized (sPoolLock) {
                if (sPool != null) {
                    this.next = sPool;
                }
                sPool = this;
            }
        }
    }

    MessageThreadUtil() {
    }

    @Override // android.support.v7.util.ThreadUtil
    public ThreadUtil.BackgroundCallback<T> getBackgroundProxy(ThreadUtil.BackgroundCallback<T> backgroundCallback) {
        return new C02972(this, backgroundCallback);
    }

    @Override // android.support.v7.util.ThreadUtil
    public ThreadUtil.MainThreadCallback<T> getMainThreadProxy(ThreadUtil.MainThreadCallback<T> mainThreadCallback) {
        return new C02961(this, mainThreadCallback);
    }
}
