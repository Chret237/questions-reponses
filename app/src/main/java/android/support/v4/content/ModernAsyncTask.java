package android.support.v4.content;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask.class */
abstract class ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final String LOG_TAG = "AsyncTask";
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int MESSAGE_POST_PROGRESS = 2;
    private static final int MESSAGE_POST_RESULT = 1;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor sDefaultExecutor;
    private static InternalHandler sHandler;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() { // from class: android.support.v4.content.ModernAsyncTask.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue(10);
    private volatile Status mStatus = Status.PENDING;
    final AtomicBoolean mCancelled = new AtomicBoolean();
    final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>(this) { // from class: android.support.v4.content.ModernAsyncTask.2
        final ModernAsyncTask this$0;

        {
            this.this$0 = this;
        }

        @Override // java.util.concurrent.Callable
        public Result call() throws Exception {
            this.this$0.mTaskInvoked.set(true);
            Result result = null;
            try {
                Process.setThreadPriority(10);
                Result result2 = (Result) this.this$0.doInBackground(this.mParams);
                result = result2;
                Binder.flushPendingCommands();
                this.this$0.postResult(result2);
                return result2;
            } finally {
            }
        }
    };
    private final FutureTask<Result> mFuture = new FutureTask<Result>(this, this.mWorker) { // from class: android.support.v4.content.ModernAsyncTask.3
        final ModernAsyncTask this$0;

        {
            this.this$0 = this;
        }

        @Override // java.util.concurrent.FutureTask
        protected void done() {
            try {
                this.this$0.postResultIfNotInvoked(get());
            } catch (InterruptedException e) {
                Log.w(ModernAsyncTask.LOG_TAG, e);
            } catch (CancellationException e2) {
                this.this$0.postResultIfNotInvoked(null);
            } catch (ExecutionException e3) {
                throw new RuntimeException("An error occurred while executing doInBackground()", e3.getCause());
            } catch (Throwable th) {
                throw new RuntimeException("An error occurred while executing doInBackground()", th);
            }
        }
    };

    /* renamed from: android.support.v4.content.ModernAsyncTask$4 */
    /* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask$4.class */
    static /* synthetic */ class C02054 {
        static final int[] $SwitchMap$androidx$loader$content$ModernAsyncTask$Status;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:7:0x0020
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.support.v4.content.ModernAsyncTask$Status[] r0 = android.support.v4.content.ModernAsyncTask.Status.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.support.v4.content.ModernAsyncTask.C02054.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status = r0
                r0 = r4
                android.support.v4.content.ModernAsyncTask$Status r1 = android.support.v4.content.ModernAsyncTask.Status.RUNNING     // Catch: java.lang.NoSuchFieldError -> L20
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L20
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L20
            L14:
                int[] r0 = android.support.v4.content.ModernAsyncTask.C02054.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status     // Catch: java.lang.NoSuchFieldError -> L20 java.lang.NoSuchFieldError -> L24
                android.support.v4.content.ModernAsyncTask$Status r1 = android.support.v4.content.ModernAsyncTask.Status.FINISHED     // Catch: java.lang.NoSuchFieldError -> L24
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L24
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L24
            L1f:
                return
            L20:
                r4 = move-exception
                goto L14
            L24:
                r4 = move-exception
                goto L1f
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.ModernAsyncTask.C02054.m151clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask$AsyncTaskResult.class */
    private static class AsyncTaskResult<Data> {
        final Data[] mData;
        final ModernAsyncTask mTask;

        AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data... dataArr) {
            this.mTask = modernAsyncTask;
            this.mData = dataArr;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask$InternalHandler.class */
    private static class InternalHandler extends Handler {
        InternalHandler() {
            super(Looper.getMainLooper());
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncTaskResult asyncTaskResult = (AsyncTaskResult) message.obj;
            int i = message.what;
            if (i == 1) {
                asyncTaskResult.mTask.finish(asyncTaskResult.mData[0]);
            } else {
                if (i != 2) {
                    return;
                }
                asyncTaskResult.mTask.onProgressUpdate(asyncTaskResult.mData);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask$Status.class */
    public enum Status {
        PENDING,
        RUNNING,
        FINISHED
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/content/ModernAsyncTask$WorkerRunnable.class */
    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;

        WorkerRunnable() {
        }
    }

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
        sDefaultExecutor = threadPoolExecutor;
    }

    ModernAsyncTask() {
    }

    public static void execute(Runnable runnable) {
        sDefaultExecutor.execute(runnable);
    }

    private static Handler getHandler() {
        InternalHandler internalHandler;
        synchronized (ModernAsyncTask.class) {
            try {
                if (sHandler == null) {
                    sHandler = new InternalHandler();
                }
                internalHandler = sHandler;
            } catch (Throwable th) {
                throw th;
            }
        }
        return internalHandler;
    }

    public static void setDefaultExecutor(Executor executor) {
        sDefaultExecutor = executor;
    }

    public final boolean cancel(boolean z) {
        this.mCancelled.set(true);
        return this.mFuture.cancel(z);
    }

    protected abstract Result doInBackground(Params... paramsArr);

    public final ModernAsyncTask<Params, Progress, Result> execute(Params... paramsArr) {
        return executeOnExecutor(sDefaultExecutor, paramsArr);
    }

    public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor executor, Params... paramsArr) {
        if (this.mStatus == Status.PENDING) {
            this.mStatus = Status.RUNNING;
            onPreExecute();
            this.mWorker.mParams = paramsArr;
            executor.execute(this.mFuture);
            return this;
        }
        int i = C02054.$SwitchMap$androidx$loader$content$ModernAsyncTask$Status[this.mStatus.ordinal()];
        if (i == 1) {
            throw new IllegalStateException("Cannot execute task: the task is already running.");
        }
        if (i != 2) {
            throw new IllegalStateException("We should never reach this state");
        }
        throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
    }

    void finish(Result result) {
        if (isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        this.mStatus = Status.FINISHED;
    }

    public final Result get() throws ExecutionException, InterruptedException {
        return this.mFuture.get();
    }

    public final Result get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.mFuture.get(j, timeUnit);
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public final boolean isCancelled() {
        return this.mCancelled.get();
    }

    protected void onCancelled() {
    }

    protected void onCancelled(Result result) {
        onCancelled();
    }

    protected void onPostExecute(Result result) {
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Progress... progressArr) {
    }

    Result postResult(Result result) {
        getHandler().obtainMessage(1, new AsyncTaskResult(this, result)).sendToTarget();
        return result;
    }

    void postResultIfNotInvoked(Result result) {
        if (this.mTaskInvoked.get()) {
            return;
        }
        postResult(result);
    }

    protected final void publishProgress(Progress... progressArr) {
        if (isCancelled()) {
            return;
        }
        getHandler().obtainMessage(2, new AsyncTaskResult(this, progressArr)).sendToTarget();
    }
}
