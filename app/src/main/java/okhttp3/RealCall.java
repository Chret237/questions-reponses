package okhttp3;

import android.support.v4.app.NotificationCompat;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

/* loaded from: classes-dex2jar.jar:okhttp3/RealCall.class */
final class RealCall implements Call {
    final OkHttpClient client;
    private EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

    /* loaded from: classes-dex2jar.jar:okhttp3/RealCall$AsyncCall.class */
    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;
        final RealCall this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AsyncCall(RealCall realCall, Callback callback) {
            super("OkHttp %s", realCall.redactedUrl());
            this.this$0 = realCall;
            this.responseCallback = callback;
        }

        @Override // okhttp3.internal.NamedRunnable
        protected void execute() {
            Response responseWithInterceptorChain;
            boolean z = true;
            try {
                try {
                    responseWithInterceptorChain = this.this$0.getResponseWithInterceptorChain();
                } catch (IOException e) {
                    e = e;
                    z = false;
                }
                try {
                    if (this.this$0.retryAndFollowUpInterceptor.isCanceled()) {
                        this.responseCallback.onFailure(this.this$0, new IOException("Canceled"));
                    } else {
                        this.responseCallback.onResponse(this.this$0, responseWithInterceptorChain);
                    }
                } catch (IOException e2) {
                    e = e2;
                    if (z) {
                        Platform.get().log(4, "Callback failure for " + this.this$0.toLoggableString(), e);
                    } else {
                        this.this$0.eventListener.callFailed(this.this$0, e);
                        this.responseCallback.onFailure(this.this$0, e);
                    }
                }
            } finally {
                this.this$0.client.dispatcher().finished(this);
            }
        }

        RealCall get() {
            return this.this$0;
        }

        String host() {
            return this.this$0.originalRequest.url().host();
        }

        Request request() {
            return this.this$0.originalRequest;
        }
    }

    private RealCall(OkHttpClient okHttpClient, Request request, boolean z) {
        this.client = okHttpClient;
        this.originalRequest = request;
        this.forWebSocket = z;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(okHttpClient, z);
    }

    private void captureCallStackTrace() {
        this.retryAndFollowUpInterceptor.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }

    static RealCall newRealCall(OkHttpClient okHttpClient, Request request, boolean z) {
        RealCall realCall = new RealCall(okHttpClient, request, z);
        realCall.eventListener = okHttpClient.eventListenerFactory().create(realCall);
        return realCall;
    }

    @Override // okhttp3.Call
    public void cancel() throws IOException {
        this.retryAndFollowUpInterceptor.cancel();
    }

    @Override // okhttp3.Call
    public RealCall clone() {
        return newRealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    @Override // okhttp3.Call
    public void enqueue(Callback callback) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.eventListener.callStart(this);
        this.client.dispatcher().enqueue(new AsyncCall(this, callback));
    }

    @Override // okhttp3.Call
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.eventListener.callStart(this);
        try {
            try {
                this.client.dispatcher().executed(this);
                Response responseWithInterceptorChain = getResponseWithInterceptorChain();
                if (responseWithInterceptorChain == null) {
                    throw new IOException("Canceled");
                }
                this.client.dispatcher().finished(this);
                return responseWithInterceptorChain;
            } catch (IOException e) {
                this.eventListener.callFailed(this, e);
                throw e;
            }
        } catch (Throwable th) {
            this.client.dispatcher().finished(this);
            throw th;
        }
    }

    Response getResponseWithInterceptorChain() throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.client.interceptors());
        arrayList.add(this.retryAndFollowUpInterceptor);
        arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
        arrayList.add(new CacheInterceptor(this.client.internalCache()));
        arrayList.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            arrayList.addAll(this.client.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.forWebSocket));
        return new RealInterceptorChain(arrayList, null, null, null, 0, this.originalRequest, this, this.eventListener, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis()).proceed(this.originalRequest);
    }

    @Override // okhttp3.Call
    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }

    @Override // okhttp3.Call
    public boolean isExecuted() {
        boolean z;
        synchronized (this) {
            z = this.executed;
        }
        return z;
    }

    String redactedUrl() {
        return this.originalRequest.url().redact();
    }

    @Override // okhttp3.Call
    public Request request() {
        return this.originalRequest;
    }

    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }

    String toLoggableString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isCanceled() ? "canceled " : BuildConfig.FLAVOR);
        sb.append(this.forWebSocket ? "web socket" : NotificationCompat.CATEGORY_CALL);
        sb.append(" to ");
        sb.append(redactedUrl());
        return sb.toString();
    }
}
