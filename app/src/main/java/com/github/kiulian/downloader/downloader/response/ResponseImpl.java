package com.github.kiulian.downloader.downloader.response;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/downloader/response/ResponseImpl.class */
public class ResponseImpl<T> implements Response<T> {
    private Future<T> data;
    private Throwable error;

    private ResponseImpl(Future<T> future, Throwable th) {
        this.data = future;
        this.error = th;
    }

    public static <T> ResponseImpl<T> error(Throwable th) {
        return new ResponseImpl<>(null, th);
    }

    public static <T> ResponseImpl<T> from(T t) {
        return fromFuture(new Future<T>(t) { // from class: com.github.kiulian.downloader.downloader.response.ResponseImpl.1
            final Object val$data;

            {
                this.val$data = t;
            }

            @Override // java.util.concurrent.Future
            public boolean cancel(boolean z) {
                return false;
            }

            @Override // java.util.concurrent.Future
            public T get() {
                return (T) this.val$data;
            }

            @Override // java.util.concurrent.Future
            public T get(long j, TimeUnit timeUnit) {
                return (T) get();
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return false;
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return true;
            }
        });
    }

    public static <T> ResponseImpl<T> fromFuture(Future<T> future) {
        return new ResponseImpl<>(future, null);
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    public boolean cancel() {
        if (this.error != null) {
            return false;
        }
        return this.data.cancel(true);
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    public T data() {
        Future<T> future = this.data;
        if (future == null) {
            return null;
        }
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            this.error = e;
            return null;
        }
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    public T data(long j, TimeUnit timeUnit) throws TimeoutException {
        Future<T> future = this.data;
        if (future == null) {
            return null;
        }
        try {
            return future.get(j, timeUnit);
        } catch (InterruptedException | ExecutionException e) {
            this.error = e;
            return null;
        }
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    public Throwable error() throws ExecutionException, InterruptedException {
        Future<T> future = this.data;
        if (future != null) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                this.error = e;
                return e;
            }
        }
        return this.error;
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    /* renamed from: ok */
    public boolean mo13ok() throws ExecutionException, InterruptedException {
        if (this.error != null) {
            return false;
        }
        try {
            this.data.get();
            return true;
        } catch (CancellationException e) {
            return false;
        } catch (Exception e2) {
            this.error = e2;
            return false;
        }
    }

    @Override // com.github.kiulian.downloader.downloader.response.Response
    public ResponseStatus status() throws ExecutionException, InterruptedException, TimeoutException {
        Future<T> future;
        if (this.error == null && (future = this.data) != null) {
            if (future.isCancelled()) {
                return ResponseStatus.canceled;
            }
            try {
                this.data.get(1L, TimeUnit.MILLISECONDS);
                return ResponseStatus.completed;
            } catch (InterruptedException e) {
                e = e;
                this.error = e;
                return ResponseStatus.error;
            } catch (CancellationException e2) {
                return ResponseStatus.canceled;
            } catch (ExecutionException e3) {
                e = e3;
                this.error = e;
                return ResponseStatus.error;
            } catch (TimeoutException e4) {
                return ResponseStatus.downloading;
            }
        }
        return ResponseStatus.error;
    }
}
