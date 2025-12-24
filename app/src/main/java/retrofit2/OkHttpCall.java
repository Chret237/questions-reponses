package retrofit2;

import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/* loaded from: classes-dex2jar.jar:retrofit2/OkHttpCall.class */
final class OkHttpCall<T> implements Call<T> {

    @Nullable
    private final Object[] args;
    private volatile boolean canceled;

    @Nullable
    private Throwable creationFailure;
    private boolean executed;

    @Nullable
    private okhttp3.Call rawCall;
    private final ServiceMethod<T, ?> serviceMethod;

    /* loaded from: classes-dex2jar.jar:retrofit2/OkHttpCall$ExceptionCatchingRequestBody.class */
    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody responseBody) {
            this.delegate = responseBody;
        }

        @Override // okhttp3.ResponseBody, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.delegate.close();
        }

        @Override // okhttp3.ResponseBody
        public long contentLength() {
            return this.delegate.contentLength();
        }

        @Override // okhttp3.ResponseBody
        public MediaType contentType() {
            return this.delegate.contentType();
        }

        @Override // okhttp3.ResponseBody
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(this, this.delegate.source()) { // from class: retrofit2.OkHttpCall.ExceptionCatchingRequestBody.1
                final ExceptionCatchingRequestBody this$0;

                {
                    this.this$0 = this;
                }

                @Override // okio.ForwardingSource, okio.Source
                public long read(Buffer buffer, long j) throws IOException {
                    try {
                        return super.read(buffer, j);
                    } catch (IOException e) {
                        this.this$0.thrownException = e;
                        throw e;
                    }
                }
            });
        }

        void throwIfCaught() throws IOException {
            IOException iOException = this.thrownException;
            if (iOException != null) {
                throw iOException;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/OkHttpCall$NoContentResponseBody.class */
    static final class NoContentResponseBody extends ResponseBody {
        private final long contentLength;
        private final MediaType contentType;

        NoContentResponseBody(MediaType mediaType, long j) {
            this.contentType = mediaType;
            this.contentLength = j;
        }

        @Override // okhttp3.ResponseBody
        public long contentLength() {
            return this.contentLength;
        }

        @Override // okhttp3.ResponseBody
        public MediaType contentType() {
            return this.contentType;
        }

        @Override // okhttp3.ResponseBody
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }

    OkHttpCall(ServiceMethod<T, ?> serviceMethod, @Nullable Object[] objArr) {
        this.serviceMethod = serviceMethod;
        this.args = objArr;
    }

    private okhttp3.Call createRawCall() throws IOException {
        okhttp3.Call call = this.serviceMethod.toCall(this.args);
        if (call != null) {
            return call;
        }
        throw new NullPointerException("Call.Factory returned null.");
    }

    @Override // retrofit2.Call
    public void cancel() {
        okhttp3.Call call;
        this.canceled = true;
        synchronized (this) {
            call = this.rawCall;
        }
        if (call != null) {
            call.cancel();
        }
    }

    @Override // retrofit2.Call
    public OkHttpCall<T> clone() {
        return new OkHttpCall<>(this.serviceMethod, this.args);
    }

    @Override // retrofit2.Call
    public void enqueue(Callback<T> callback) {
        okhttp3.Call callCreateRawCall;
        Throwable th;
        Utils.checkNotNull(callback, "callback == null");
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            okhttp3.Call call = this.rawCall;
            Throwable th2 = this.creationFailure;
            callCreateRawCall = call;
            th = th2;
            if (call == null) {
                callCreateRawCall = call;
                th = th2;
                if (th2 == null) {
                    try {
                        callCreateRawCall = createRawCall();
                        this.rawCall = callCreateRawCall;
                        th = th2;
                    } catch (Throwable th3) {
                        th = th3;
                        Utils.throwIfFatal(th);
                        this.creationFailure = th;
                        callCreateRawCall = call;
                    }
                }
            }
        }
        if (th != null) {
            callback.onFailure(this, th);
            return;
        }
        if (this.canceled) {
            callCreateRawCall.cancel();
        }
        callCreateRawCall.enqueue(new okhttp3.Callback(this, callback) { // from class: retrofit2.OkHttpCall.1
            final OkHttpCall this$0;
            final Callback val$callback;

            {
                this.this$0 = this;
                this.val$callback = callback;
            }

            private void callFailure(Throwable th4) {
                try {
                    this.val$callback.onFailure(this.this$0, th4);
                } catch (Throwable th5) {
                    th5.printStackTrace();
                }
            }

            @Override // okhttp3.Callback
            public void onFailure(okhttp3.Call call2, IOException iOException) {
                callFailure(iOException);
            }

            @Override // okhttp3.Callback
            public void onResponse(okhttp3.Call call2, okhttp3.Response response) {
                try {
                    try {
                        this.val$callback.onResponse(this.this$0, this.this$0.parseResponse(response));
                    } catch (Throwable th4) {
                        th4.printStackTrace();
                    }
                } catch (Throwable th5) {
                    callFailure(th5);
                }
            }
        });
    }

    @Override // retrofit2.Call
    public Response<T> execute() throws IOException {
        okhttp3.Call callCreateRawCall;
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            if (this.creationFailure != null) {
                if (this.creationFailure instanceof IOException) {
                    throw ((IOException) this.creationFailure);
                }
                if (this.creationFailure instanceof RuntimeException) {
                    throw ((RuntimeException) this.creationFailure);
                }
                throw ((Error) this.creationFailure);
            }
            okhttp3.Call call = this.rawCall;
            callCreateRawCall = call;
            if (call == null) {
                try {
                    callCreateRawCall = createRawCall();
                    this.rawCall = callCreateRawCall;
                } catch (IOException | Error | RuntimeException e) {
                    Utils.throwIfFatal(e);
                    this.creationFailure = e;
                    throw e;
                }
            }
        }
        if (this.canceled) {
            callCreateRawCall.cancel();
        }
        return parseResponse(callCreateRawCall.execute());
    }

    @Override // retrofit2.Call
    public boolean isCanceled() {
        boolean z = true;
        if (this.canceled) {
            return true;
        }
        synchronized (this) {
            if (this.rawCall == null || !this.rawCall.isCanceled()) {
                z = false;
            }
        }
        return z;
    }

    @Override // retrofit2.Call
    public boolean isExecuted() {
        boolean z;
        synchronized (this) {
            z = this.executed;
        }
        return z;
    }

    Response<T> parseResponse(okhttp3.Response response) throws IOException {
        ResponseBody responseBodyBody = response.body();
        okhttp3.Response responseBuild = response.newBuilder().body(new NoContentResponseBody(responseBodyBody.contentType(), responseBodyBody.contentLength())).build();
        int iCode = responseBuild.code();
        if (iCode < 200 || iCode >= 300) {
            try {
                return Response.error(Utils.buffer(responseBodyBody), responseBuild);
            } finally {
                responseBodyBody.close();
            }
        }
        if (iCode == 204 || iCode == 205) {
            responseBodyBody.close();
            return Response.success((Object) null, responseBuild);
        }
        ExceptionCatchingRequestBody exceptionCatchingRequestBody = new ExceptionCatchingRequestBody(responseBodyBody);
        try {
            return Response.success(this.serviceMethod.toResponse(exceptionCatchingRequestBody), responseBuild);
        } catch (RuntimeException e) {
            exceptionCatchingRequestBody.throwIfCaught();
            throw e;
        }
    }

    @Override // retrofit2.Call
    public Request request() {
        synchronized (this) {
            okhttp3.Call call = this.rawCall;
            if (call != null) {
                return call.request();
            }
            if (this.creationFailure != null) {
                if (this.creationFailure instanceof IOException) {
                    throw new RuntimeException("Unable to create request.", this.creationFailure);
                }
                if (this.creationFailure instanceof RuntimeException) {
                    throw ((RuntimeException) this.creationFailure);
                }
                throw ((Error) this.creationFailure);
            }
            try {
                okhttp3.Call callCreateRawCall = createRawCall();
                this.rawCall = callCreateRawCall;
                return callCreateRawCall.request();
            } catch (IOException e) {
                this.creationFailure = e;
                throw new RuntimeException("Unable to create request.", e);
            } catch (Error e2) {
                e = e2;
                Utils.throwIfFatal(e);
                this.creationFailure = e;
                throw e;
            } catch (RuntimeException e3) {
                e = e3;
                Utils.throwIfFatal(e);
                this.creationFailure = e;
                throw e;
            }
        }
    }
}
