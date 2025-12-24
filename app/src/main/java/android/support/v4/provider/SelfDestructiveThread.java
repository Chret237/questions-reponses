package android.support.v4.provider;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes-dex2jar.jar:android/support/v4/provider/SelfDestructiveThread.class */
public class SelfDestructiveThread {
    private static final int MSG_DESTRUCTION = 0;
    private static final int MSG_INVOKE_RUNNABLE = 1;
    private final int mDestructAfterMillisec;
    private Handler mHandler;
    private final int mPriority;
    private HandlerThread mThread;
    private final String mThreadName;
    private final Object mLock = new Object();
    private Handler.Callback mCallback = new Handler.Callback(this) { // from class: android.support.v4.provider.SelfDestructiveThread.1
        final SelfDestructiveThread this$0;

        {
            this.this$0 = this;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.this$0.onDestruction();
                return true;
            }
            if (i != 1) {
                return true;
            }
            this.this$0.onInvokeRunnable((Runnable) message.obj);
            return true;
        }
    };
    private int mGeneration = 0;

    /* renamed from: android.support.v4.provider.SelfDestructiveThread$2 */
    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/SelfDestructiveThread$2.class */
    class RunnableC02212 implements Runnable {
        final SelfDestructiveThread this$0;
        final Callable val$callable;
        final Handler val$callingHandler;
        final ReplyCallback val$reply;

        RunnableC02212(SelfDestructiveThread selfDestructiveThread, Callable callable, Handler handler, ReplyCallback replyCallback) {
            this.this$0 = selfDestructiveThread;
            this.val$callable = callable;
            this.val$callingHandler = handler;
            this.val$reply = replyCallback;
        }

        @Override // java.lang.Runnable
        public void run() throws Exception {
            Object objCall;
            try {
                objCall = this.val$callable.call();
            } catch (Exception e) {
                objCall = null;
            }
            this.val$callingHandler.post(new Runnable(this, objCall) { // from class: android.support.v4.provider.SelfDestructiveThread.2.1
                final RunnableC02212 this$1;
                final Object val$result;

                {
                    this.this$1 = this;
                    this.val$result = objCall;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.this$1.val$reply.onReply(this.val$result);
                }
            });
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/SelfDestructiveThread$ReplyCallback.class */
    public interface ReplyCallback<T> {
        void onReply(T t);
    }

    public SelfDestructiveThread(String str, int i, int i2) {
        this.mThreadName = str;
        this.mPriority = i;
        this.mDestructAfterMillisec = i2;
    }

    private void post(Runnable runnable) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                HandlerThread handlerThread = new HandlerThread(this.mThreadName, this.mPriority);
                this.mThread = handlerThread;
                handlerThread.start();
                this.mHandler = new Handler(this.mThread.getLooper(), this.mCallback);
                this.mGeneration++;
            }
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, runnable));
        }
    }

    public int getGeneration() {
        int i;
        synchronized (this.mLock) {
            i = this.mGeneration;
        }
        return i;
    }

    public boolean isRunning() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mThread != null;
        }
        return z;
    }

    void onDestruction() {
        synchronized (this.mLock) {
            if (this.mHandler.hasMessages(1)) {
                return;
            }
            this.mThread.quit();
            this.mThread = null;
            this.mHandler = null;
        }
    }

    void onInvokeRunnable(Runnable runnable) {
        runnable.run();
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), this.mDestructAfterMillisec);
        }
    }

    public <T> void postAndReply(Callable<T> callable, ReplyCallback<T> replyCallback) {
        post(new RunnableC02212(this, callable, new Handler(), replyCallback));
    }

    public <T> T postAndWait(Callable<T> callable, int i) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition conditionNewCondition = reentrantLock.newCondition();
        AtomicReference atomicReference = new AtomicReference();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        post(new Runnable(this, atomicReference, callable, reentrantLock, atomicBoolean, conditionNewCondition) { // from class: android.support.v4.provider.SelfDestructiveThread.3
            final SelfDestructiveThread this$0;
            final Callable val$callable;
            final Condition val$cond;
            final AtomicReference val$holder;
            final ReentrantLock val$lock;
            final AtomicBoolean val$running;

            {
                this.this$0 = this;
                this.val$holder = atomicReference;
                this.val$callable = callable;
                this.val$lock = reentrantLock;
                this.val$running = atomicBoolean;
                this.val$cond = conditionNewCondition;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    this.val$holder.set(this.val$callable.call());
                } catch (Exception e) {
                }
                this.val$lock.lock();
                try {
                    this.val$running.set(false);
                    this.val$cond.signal();
                } finally {
                    this.val$lock.unlock();
                }
            }
        });
        reentrantLock.lock();
        try {
            if (!atomicBoolean.get()) {
                return (T) atomicReference.get();
            }
            long nanos = TimeUnit.MILLISECONDS.toNanos(i);
            do {
                try {
                    nanos = conditionNewCondition.awaitNanos(nanos);
                } catch (InterruptedException e) {
                }
                if (!atomicBoolean.get()) {
                    return (T) atomicReference.get();
                }
            } while (nanos > 0);
            throw new InterruptedException("timeout");
        } finally {
            reentrantLock.unlock();
        }
    }
}
