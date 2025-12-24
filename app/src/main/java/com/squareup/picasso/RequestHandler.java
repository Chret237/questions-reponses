package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/RequestHandler.class */
public abstract class RequestHandler {

    /* loaded from: classes-dex2jar.jar:com/squareup/picasso/RequestHandler$Result.class */
    public static final class Result {
        private final Bitmap bitmap;
        private final int exifOrientation;
        private final Picasso.LoadedFrom loadedFrom;
        private final InputStream stream;

        public Result(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            this((Bitmap) Utils.checkNotNull(bitmap, "bitmap == null"), null, loadedFrom, 0);
        }

        Result(Bitmap bitmap, InputStream inputStream, Picasso.LoadedFrom loadedFrom, int i) {
            if (!((inputStream == null ? false : true) ^ (bitmap != null))) {
                throw new AssertionError();
            }
            this.bitmap = bitmap;
            this.stream = inputStream;
            this.loadedFrom = (Picasso.LoadedFrom) Utils.checkNotNull(loadedFrom, "loadedFrom == null");
            this.exifOrientation = i;
        }

        public Result(InputStream inputStream, Picasso.LoadedFrom loadedFrom) {
            this(null, (InputStream) Utils.checkNotNull(inputStream, "stream == null"), loadedFrom, 0);
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        int getExifOrientation() {
            return this.exifOrientation;
        }

        public Picasso.LoadedFrom getLoadedFrom() {
            return this.loadedFrom;
        }

        public InputStream getStream() {
            return this.stream;
        }
    }

    static void calculateInSampleSize(int i, int i2, int i3, int i4, BitmapFactory.Options options, Request request) {
        int iMax;
        double dFloor;
        if (i4 > i2 || i3 > i) {
            if (i2 == 0) {
                dFloor = Math.floor(i3 / i);
            } else if (i == 0) {
                dFloor = Math.floor(i4 / i2);
            } else {
                int iFloor = (int) Math.floor(i4 / i2);
                int iFloor2 = (int) Math.floor(i3 / i);
                iMax = request.centerInside ? Math.max(iFloor, iFloor2) : Math.min(iFloor, iFloor2);
            }
            iMax = (int) dFloor;
        } else {
            iMax = 1;
        }
        options.inSampleSize = iMax;
        options.inJustDecodeBounds = false;
    }

    static void calculateInSampleSize(int i, int i2, BitmapFactory.Options options, Request request) {
        calculateInSampleSize(i, i2, options.outWidth, options.outHeight, options, request);
    }

    static BitmapFactory.Options createBitmapOptions(Request request) {
        boolean zHasSize = request.hasSize();
        boolean z = request.config != null;
        BitmapFactory.Options options = null;
        if (zHasSize || z) {
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inJustDecodeBounds = zHasSize;
            options = options2;
            if (z) {
                options2.inPreferredConfig = request.config;
                options = options2;
            }
        }
        return options;
    }

    static boolean requiresInSampleSize(BitmapFactory.Options options) {
        return options != null && options.inJustDecodeBounds;
    }

    public abstract boolean canHandleRequest(Request request);

    int getRetryCount() {
        return 0;
    }

    public abstract Result load(Request request, int i) throws IOException;

    boolean shouldRetry(boolean z, NetworkInfo networkInfo) {
        return false;
    }

    boolean supportsReplay() {
        return false;
    }
}
