package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.NetworkInfo;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkRequestHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/BitmapHunter.class */
class BitmapHunter implements Runnable {
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifRotation;
    Future<?> future;
    final String key;
    Picasso.LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    Picasso.Priority priority;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence = SEQUENCE_GENERATOR.incrementAndGet();
    final Stats stats;
    private static final Object DECODE_LOCK = new Object();
    private static final ThreadLocal<StringBuilder> NAME_BUILDER = new ThreadLocal<StringBuilder>() { // from class: com.squareup.picasso.BitmapHunter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public StringBuilder initialValue() {
            return new StringBuilder("Picasso-");
        }
    };
    private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger();
    private static final RequestHandler ERRORING_HANDLER = new RequestHandler() { // from class: com.squareup.picasso.BitmapHunter.2
        @Override // com.squareup.picasso.RequestHandler
        public boolean canHandleRequest(Request request) {
            return true;
        }

        @Override // com.squareup.picasso.RequestHandler
        public RequestHandler.Result load(Request request, int i) throws IOException {
            throw new IllegalStateException("Unrecognized type of request: " + request);
        }
    };

    BitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, RequestHandler requestHandler) {
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.action = action;
        this.key = action.getKey();
        this.data = action.getRequest();
        this.priority = action.getPriority();
        this.memoryPolicy = action.getMemoryPolicy();
        this.networkPolicy = action.getNetworkPolicy();
        this.requestHandler = requestHandler;
        this.retryCount = requestHandler.getRetryCount();
    }

    static Bitmap applyCustomTransformations(List<Transformation> list, Bitmap bitmap) {
        int size = list.size();
        int i = 0;
        while (i < size) {
            Transformation transformation = list.get(i);
            try {
                Bitmap bitmapTransform = transformation.transform(bitmap);
                if (bitmapTransform == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Transformation ");
                    sb.append(transformation.key());
                    sb.append(" returned null after ");
                    sb.append(i);
                    sb.append(" previous transformation(s).\n\nTransformation list:\n");
                    Iterator<Transformation> it = list.iterator();
                    while (it.hasNext()) {
                        sb.append(it.next().key());
                        sb.append('\n');
                    }
                    Picasso.HANDLER.post(new Runnable(sb) { // from class: com.squareup.picasso.BitmapHunter.4
                        final StringBuilder val$builder;

                        {
                            this.val$builder = sb;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            throw new NullPointerException(this.val$builder.toString());
                        }
                    });
                    return null;
                }
                if (bitmapTransform == bitmap && bitmap.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable(transformation) { // from class: com.squareup.picasso.BitmapHunter.5
                        final Transformation val$transformation;

                        {
                            this.val$transformation = transformation;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " returned input Bitmap but recycled it.");
                        }
                    });
                    return null;
                }
                if (bitmapTransform != bitmap && !bitmap.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable(transformation) { // from class: com.squareup.picasso.BitmapHunter.6
                        final Transformation val$transformation;

                        {
                            this.val$transformation = transformation;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " mutated input Bitmap but failed to recycle the original.");
                        }
                    });
                    return null;
                }
                i++;
                bitmap = bitmapTransform;
            } catch (RuntimeException e) {
                Picasso.HANDLER.post(new Runnable(transformation, e) { // from class: com.squareup.picasso.BitmapHunter.3
                    final RuntimeException val$e;
                    final Transformation val$transformation;

                    {
                        this.val$transformation = transformation;
                        this.val$e = e;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        throw new RuntimeException("Transformation " + this.val$transformation.key() + " crashed with exception.", this.val$e);
                    }
                });
                return null;
            }
        }
        return bitmap;
    }

    private Picasso.Priority computeNewPriority() {
        Picasso.Priority priority = Picasso.Priority.LOW;
        List<Action> list = this.actions;
        boolean z = (list == null || list.isEmpty()) ? false : true;
        boolean z2 = true;
        if (this.action == null) {
            z2 = z;
        }
        if (!z2) {
            return priority;
        }
        Action action = this.action;
        if (action != null) {
            priority = action.getPriority();
        }
        Picasso.Priority priority2 = priority;
        if (z) {
            int size = this.actions.size();
            int i = 0;
            while (true) {
                priority2 = priority;
                if (i >= size) {
                    break;
                }
                Picasso.Priority priority3 = this.actions.get(i).getPriority();
                Picasso.Priority priority4 = priority;
                if (priority3.ordinal() > priority.ordinal()) {
                    priority4 = priority3;
                }
                i++;
                priority = priority4;
            }
        }
        return priority2;
    }

    static Bitmap decodeStream(InputStream inputStream, Request request) throws IOException {
        MarkableInputStream markableInputStream = new MarkableInputStream(inputStream);
        long jSavePosition = markableInputStream.savePosition(65536);
        BitmapFactory.Options optionsCreateBitmapOptions = RequestHandler.createBitmapOptions(request);
        boolean zRequiresInSampleSize = RequestHandler.requiresInSampleSize(optionsCreateBitmapOptions);
        boolean zIsWebPFile = Utils.isWebPFile(markableInputStream);
        markableInputStream.reset(jSavePosition);
        if (zIsWebPFile) {
            byte[] byteArray = Utils.toByteArray(markableInputStream);
            if (zRequiresInSampleSize) {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, optionsCreateBitmapOptions);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, optionsCreateBitmapOptions, request);
            }
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, optionsCreateBitmapOptions);
        }
        if (zRequiresInSampleSize) {
            BitmapFactory.decodeStream(markableInputStream, null, optionsCreateBitmapOptions);
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, optionsCreateBitmapOptions, request);
            markableInputStream.reset(jSavePosition);
        }
        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(markableInputStream, null, optionsCreateBitmapOptions);
        if (bitmapDecodeStream != null) {
            return bitmapDecodeStream;
        }
        throw new IOException("Failed to decode stream.");
    }

    static BitmapHunter forRequest(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        Request request = action.getRequest();
        List<RequestHandler> requestHandlers = picasso.getRequestHandlers();
        int size = requestHandlers.size();
        for (int i = 0; i < size; i++) {
            RequestHandler requestHandler = requestHandlers.get(i);
            if (requestHandler.canHandleRequest(request)) {
                return new BitmapHunter(picasso, dispatcher, cache, stats, action, requestHandler);
            }
        }
        return new BitmapHunter(picasso, dispatcher, cache, stats, action, ERRORING_HANDLER);
    }

    private static boolean shouldResize(boolean z, int i, int i2, int i3, int i4) {
        return !z || i > i3 || i2 > i4;
    }

    static Bitmap transformResult(Request request, Bitmap bitmap, int i) {
        int i2;
        int iCeil;
        int i3;
        int i4;
        float f;
        float f2;
        float f3;
        float f4;
        int iCeil2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean z = request.onlyScaleDown;
        Matrix matrix = new Matrix();
        int i5 = 0;
        if (request.needsMatrixTransform()) {
            int i6 = request.targetWidth;
            int i7 = request.targetHeight;
            float f5 = request.rotationDegrees;
            if (f5 != 0.0f) {
                if (request.hasRotationPivot) {
                    matrix.setRotate(f5, request.rotationPivotX, request.rotationPivotY);
                } else {
                    matrix.setRotate(f5);
                }
            }
            if (request.centerCrop) {
                float f6 = i6;
                float f7 = f6 / width;
                float f8 = i7;
                float f9 = f8 / height;
                if (f7 > f9) {
                    iCeil = (int) Math.ceil(r0 * (f9 / f7));
                    i3 = (height - iCeil) / 2;
                    f9 = f8 / iCeil;
                    iCeil2 = width;
                } else {
                    iCeil2 = (int) Math.ceil(r0 * (f7 / f9));
                    i5 = (width - iCeil2) / 2;
                    f7 = f6 / iCeil2;
                    iCeil = height;
                    i3 = 0;
                }
                if (shouldResize(z, width, height, i6, i7)) {
                    matrix.preScale(f7, f9);
                }
                i4 = i5;
                i2 = iCeil2;
            } else {
                if (request.centerInside) {
                    float f10 = i6 / width;
                    float f11 = i7 / height;
                    if (f10 < f11) {
                        f11 = f10;
                    }
                    if (shouldResize(z, width, height, i6, i7)) {
                        matrix.preScale(f11, f11);
                    }
                } else if ((i6 != 0 || i7 != 0) && (i6 != width || i7 != height)) {
                    if (i6 != 0) {
                        f = i6;
                        f2 = width;
                    } else {
                        f = i7;
                        f2 = height;
                    }
                    float f12 = f / f2;
                    if (i7 != 0) {
                        f3 = i7;
                        f4 = height;
                    } else {
                        f3 = i6;
                        f4 = width;
                    }
                    float f13 = f3 / f4;
                    if (shouldResize(z, width, height, i6, i7)) {
                        matrix.preScale(f12, f13);
                    }
                }
                i2 = width;
                iCeil = height;
                i3 = 0;
                i4 = 0;
            }
        } else {
            i2 = width;
            iCeil = height;
            i3 = 0;
            i4 = 0;
        }
        if (i != 0) {
            matrix.preRotate(i);
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, i4, i3, i2, iCeil, matrix, true);
        Bitmap bitmap2 = bitmap;
        if (bitmapCreateBitmap != bitmap) {
            bitmap.recycle();
            bitmap2 = bitmapCreateBitmap;
        }
        return bitmap2;
    }

    static void updateThreadName(Request request) {
        String name = request.getName();
        StringBuilder sb = NAME_BUILDER.get();
        sb.ensureCapacity(name.length() + 8);
        sb.replace(8, sb.length(), name);
        Thread.currentThread().setName(sb.toString());
    }

    void attach(Action action) {
        boolean z = this.picasso.loggingEnabled;
        Request request = action.request;
        if (this.action == null) {
            this.action = action;
            if (z) {
                List<Action> list = this.actions;
                if (list == null || list.isEmpty()) {
                    Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
                    return;
                } else {
                    Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
                    return;
                }
            }
            return;
        }
        if (this.actions == null) {
            this.actions = new ArrayList(3);
        }
        this.actions.add(action);
        if (z) {
            Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
        }
        Picasso.Priority priority = action.getPriority();
        if (priority.ordinal() > this.priority.ordinal()) {
            this.priority = priority;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean cancel() {
        /*
            r3 = this;
            r0 = r3
            com.squareup.picasso.Action r0 = r0.action
            r6 = r0
            r0 = 0
            r5 = r0
            r0 = r5
            r4 = r0
            r0 = r6
            if (r0 != 0) goto L3a
            r0 = r3
            java.util.List<com.squareup.picasso.Action> r0 = r0.actions
            r6 = r0
            r0 = r6
            if (r0 == 0) goto L21
            r0 = r5
            r4 = r0
            r0 = r6
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L3a
        L21:
            r0 = r3
            java.util.concurrent.Future<?> r0 = r0.future
            r6 = r0
            r0 = r5
            r4 = r0
            r0 = r6
            if (r0 == 0) goto L3a
            r0 = r5
            r4 = r0
            r0 = r6
            r1 = 0
            boolean r0 = r0.cancel(r1)
            if (r0 == 0) goto L3a
            r0 = 1
            r4 = r0
        L3a:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.BitmapHunter.cancel():boolean");
    }

    void detach(Action action) {
        boolean zRemove;
        if (this.action == action) {
            this.action = null;
            zRemove = true;
        } else {
            List<Action> list = this.actions;
            zRemove = list != null ? list.remove(action) : false;
        }
        if (zRemove && action.getPriority() == this.priority) {
            this.priority = computeNewPriority();
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    Action getAction() {
        return this.action;
    }

    List<Action> getActions() {
        return this.actions;
    }

    Request getData() {
        return this.data;
    }

    Exception getException() {
        return this.exception;
    }

    String getKey() {
        return this.key;
    }

    Picasso.LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }

    int getMemoryPolicy() {
        return this.memoryPolicy;
    }

    Picasso getPicasso() {
        return this.picasso;
    }

    Picasso.Priority getPriority() {
        return this.priority;
    }

    Bitmap getResult() {
        return this.result;
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x014b A[Catch: all -> 0x018e, TRY_LEAVE, TryCatch #1 {, blocks: (B:37:0x0101, B:41:0x010d, B:51:0x0141, B:53:0x014b, B:55:0x0159, B:57:0x0163, B:60:0x017a, B:44:0x0115, B:46:0x0123, B:48:0x012d), top: B:74:0x0101 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.graphics.Bitmap hunt() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 406
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.BitmapHunter.hunt():android.graphics.Bitmap");
    }

    boolean isCancelled() {
        Future<?> future = this.future;
        return future != null && future.isCancelled();
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                try {
                    try {
                        try {
                            try {
                                updateThreadName(this.data);
                                if (this.picasso.loggingEnabled) {
                                    Utils.log("Hunter", "executing", Utils.getLogIdsForHunter(this));
                                }
                                Bitmap bitmapHunt = hunt();
                                this.result = bitmapHunt;
                                if (bitmapHunt == null) {
                                    this.dispatcher.dispatchFailed(this);
                                } else {
                                    this.dispatcher.dispatchComplete(this);
                                }
                            } catch (OutOfMemoryError e) {
                                StringWriter stringWriter = new StringWriter();
                                this.stats.createSnapshot().dump(new PrintWriter(stringWriter));
                                this.exception = new RuntimeException(stringWriter.toString(), e);
                                this.dispatcher.dispatchFailed(this);
                            }
                        } catch (NetworkRequestHandler.ContentLengthException e2) {
                            this.exception = e2;
                            this.dispatcher.dispatchRetry(this);
                        }
                    } catch (IOException e3) {
                        this.exception = e3;
                        this.dispatcher.dispatchRetry(this);
                    }
                } catch (Exception e4) {
                    this.exception = e4;
                    this.dispatcher.dispatchFailed(this);
                }
            } catch (Downloader.ResponseException e5) {
                if (!e5.localCacheOnly || e5.responseCode != 504) {
                    this.exception = e5;
                }
                this.dispatcher.dispatchFailed(this);
            }
            Thread.currentThread().setName("Picasso-Idle");
        } catch (Throwable th) {
            Thread.currentThread().setName("Picasso-Idle");
            throw th;
        }
    }

    boolean shouldRetry(boolean z, NetworkInfo networkInfo) {
        if (!(this.retryCount > 0)) {
            return false;
        }
        this.retryCount--;
        return this.requestHandler.shouldRetry(z, networkInfo);
    }

    boolean supportsReplay() {
        return this.requestHandler.supportsReplay();
    }
}
