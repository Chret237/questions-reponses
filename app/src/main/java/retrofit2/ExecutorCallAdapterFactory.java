package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import okhttp3.Request;
import retrofit2.CallAdapter;

/* loaded from: classes-dex2jar.jar:retrofit2/ExecutorCallAdapterFactory.class */
final class ExecutorCallAdapterFactory extends CallAdapter.Factory {
    final Executor callbackExecutor;

    /* loaded from: classes-dex2jar.jar:retrofit2/ExecutorCallAdapterFactory$ExecutorCallbackCall.class */
    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;

        /* renamed from: retrofit2.ExecutorCallAdapterFactory$ExecutorCallbackCall$1 */
        /* loaded from: classes-dex2jar.jar:retrofit2/ExecutorCallAdapterFactory$ExecutorCallbackCall$1.class */
        class C06701 implements Callback<T> {
            final ExecutorCallbackCall this$0;
            final Callback val$callback;

            C06701(ExecutorCallbackCall executorCallbackCall, Callback callback) {
                this.this$0 = executorCallbackCall;
                this.val$callback = callback;
            }

            @Override // retrofit2.Callback
            public void onFailure(Call<T> call, Throwable th) {
                this.this$0.callbackExecutor.execute(new Runnable(this, th) { // from class: retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.1.2
                    final C06701 this$1;
                    final Throwable val$t;

                    {
                        this.this$1 = this;
                        this.val$t = th;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this.this$1.val$callback.onFailure(this.this$1.this$0, this.val$t);
                    }
                });
            }

            @Override // retrofit2.Callback
            public void onResponse(Call<T> call, Response<T> response) {
                this.this$0.callbackExecutor.execute(new Runnable(this, response) { // from class: retrofit2.ExecutorCallAdapterFactory.ExecutorCallbackCall.1.1
                    final C06701 this$1;
                    final Response val$response;

                    {
                        this.this$1 = this;
                        this.val$response = response;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (this.this$1.this$0.delegate.isCanceled()) {
                            this.this$1.val$callback.onFailure(this.this$1.this$0, new IOException("Canceled"));
                        } else {
                            this.this$1.val$callback.onResponse(this.this$1.this$0, this.val$response);
                        }
                    }
                });
            }
        }

        ExecutorCallbackCall(Executor executor, Call<T> call) {
            this.callbackExecutor = executor;
            this.delegate = call;
        }

        @Override // retrofit2.Call
        public void cancel() {
            this.delegate.cancel();
        }

        @Override // retrofit2.Call
        public Call<T> clone() {
            return new ExecutorCallbackCall(this.callbackExecutor, this.delegate.clone());
        }

        @Override // retrofit2.Call
        public void enqueue(Callback<T> callback) {
            Utils.checkNotNull(callback, "callback == null");
            this.delegate.enqueue(new C06701(this, callback));
        }

        @Override // retrofit2.Call
        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }

        @Override // retrofit2.Call
        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }

        @Override // retrofit2.Call
        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }

        @Override // retrofit2.Call
        public Request request() {
            return this.delegate.request();
        }
    }

    ExecutorCallAdapterFactory(Executor executor) {
        this.callbackExecutor = executor;
    }

    @Override // retrofit2.CallAdapter.Factory
    public CallAdapter<?, ?> get(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        if (getRawType(type) != Call.class) {
            return null;
        }
        return new CallAdapter<Object, Call<?>>(this, Utils.getCallResponseType(type)) { // from class: retrofit2.ExecutorCallAdapterFactory.1
            final ExecutorCallAdapterFactory this$0;
            final Type val$responseType;

            {
                this.this$0 = this;
                this.val$responseType = type;
            }

            @Override // retrofit2.CallAdapter
            public Call<?> adapt(Call<Object> call) {
                return new ExecutorCallbackCall(this.this$0.callbackExecutor, call);
            }

            @Override // retrofit2.CallAdapter
            public Type responseType() {
                return this.val$responseType;
            }
        };
    }
}
