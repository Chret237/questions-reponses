package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes-dex2jar.jar:android/support/v4/print/PrintHelper.class */
public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
    private static final String LOG_TAG = "PrintHelper";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    final Object mLock = new Object();
    int mScaleMode = 2;
    int mColorMode = 2;
    int mOrientation = 1;

    /* loaded from: classes-dex2jar.jar:android/support/v4/print/PrintHelper$OnPrintFinishCallback.class */
    public interface OnPrintFinishCallback {
        void onFinish();
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/print/PrintHelper$PrintBitmapAdapter.class */
    private class PrintBitmapAdapter extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        private final Bitmap mBitmap;
        private final OnPrintFinishCallback mCallback;
        private final int mFittingMode;
        private final String mJobName;
        final PrintHelper this$0;

        PrintBitmapAdapter(PrintHelper printHelper, String str, int i, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
            this.this$0 = printHelper;
            this.mJobName = str;
            this.mFittingMode = i;
            this.mBitmap = bitmap;
            this.mCallback = onPrintFinishCallback;
        }

        @Override // android.print.PrintDocumentAdapter
        public void onFinish() {
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
        }

        @Override // android.print.PrintDocumentAdapter
        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.mAttributes = printAttributes2;
            layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), !printAttributes2.equals(printAttributes));
        }

        @Override // android.print.PrintDocumentAdapter
        public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/print/PrintHelper$PrintUriAdapter.class */
    private class PrintUriAdapter extends PrintDocumentAdapter {
        PrintAttributes mAttributes;
        Bitmap mBitmap = null;
        final OnPrintFinishCallback mCallback;
        final int mFittingMode;
        final Uri mImageFile;
        final String mJobName;
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        final PrintHelper this$0;

        /* renamed from: android.support.v4.print.PrintHelper$PrintUriAdapter$1 */
        /* loaded from: classes-dex2jar.jar:android/support/v4/print/PrintHelper$PrintUriAdapter$1.class */
        class AsyncTaskC02141 extends AsyncTask<Uri, Boolean, Bitmap> {
            final PrintUriAdapter this$1;
            final CancellationSignal val$cancellationSignal;
            final PrintDocumentAdapter.LayoutResultCallback val$layoutResultCallback;
            final PrintAttributes val$newPrintAttributes;
            final PrintAttributes val$oldPrintAttributes;

            AsyncTaskC02141(PrintUriAdapter printUriAdapter, CancellationSignal cancellationSignal, PrintAttributes printAttributes, PrintAttributes printAttributes2, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback) {
                this.this$1 = printUriAdapter;
                this.val$cancellationSignal = cancellationSignal;
                this.val$newPrintAttributes = printAttributes;
                this.val$oldPrintAttributes = printAttributes2;
                this.val$layoutResultCallback = layoutResultCallback;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Bitmap doInBackground(Uri... uriArr) {
                try {
                    return this.this$1.this$0.loadConstrainedBitmap(this.this$1.mImageFile);
                } catch (FileNotFoundException e) {
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onCancelled(Bitmap bitmap) {
                this.val$layoutResultCallback.onLayoutCancelled();
                this.this$1.mLoadBitmap = null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Removed duplicated region for block: B:8:0x0020  */
            @Override // android.os.AsyncTask
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onPostExecute(android.graphics.Bitmap r9) {
                /*
                    Method dump skipped, instructions count: 190
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelper.PrintUriAdapter.AsyncTaskC02141.onPostExecute(android.graphics.Bitmap):void");
            }

            @Override // android.os.AsyncTask
            protected void onPreExecute() {
                this.val$cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(this) { // from class: android.support.v4.print.PrintHelper.PrintUriAdapter.1.1
                    final AsyncTaskC02141 this$2;

                    {
                        this.this$2 = this;
                    }

                    @Override // android.os.CancellationSignal.OnCancelListener
                    public void onCancel() {
                        this.this$2.this$1.cancelLoad();
                        this.this$2.cancel(false);
                    }
                });
            }
        }

        PrintUriAdapter(PrintHelper printHelper, String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback, int i) {
            this.this$0 = printHelper;
            this.mJobName = str;
            this.mImageFile = uri;
            this.mCallback = onPrintFinishCallback;
            this.mFittingMode = i;
        }

        void cancelLoad() {
            synchronized (this.this$0.mLock) {
                if (this.this$0.mDecodeOptions != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        this.this$0.mDecodeOptions.requestCancelDecode();
                    }
                    this.this$0.mDecodeOptions = null;
                }
            }
        }

        @Override // android.print.PrintDocumentAdapter
        public void onFinish() {
            super.onFinish();
            cancelLoad();
            AsyncTask<Uri, Boolean, Bitmap> asyncTask = this.mLoadBitmap;
            if (asyncTask != null) {
                asyncTask.cancel(true);
            }
            OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
            if (onPrintFinishCallback != null) {
                onPrintFinishCallback.onFinish();
            }
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.mBitmap = null;
            }
        }

        @Override // android.print.PrintDocumentAdapter
        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            synchronized (this) {
                this.mAttributes = printAttributes2;
            }
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
            } else if (this.mBitmap != null) {
                layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(this.mJobName).setContentType(1).setPageCount(1).build(), !printAttributes2.equals(printAttributes));
            } else {
                this.mLoadBitmap = new AsyncTaskC02141(this, cancellationSignal, printAttributes2, printAttributes, layoutResultCallback).execute(new Uri[0]);
            }
        }

        @Override // android.print.PrintDocumentAdapter
        public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            this.this$0.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    static {
        PRINT_ACTIVITY_RESPECTS_ORIENTATION = Build.VERSION.SDK_INT < 20 || Build.VERSION.SDK_INT > 23;
        boolean z = false;
        if (Build.VERSION.SDK_INT != 23) {
            z = true;
        }
        IS_MIN_MARGINS_HANDLING_CORRECT = z;
    }

    public PrintHelper(Context context) {
        this.mContext = context;
    }

    static Bitmap convertBitmapForColorMode(Bitmap bitmap, int i) {
        if (i != 1) {
            return bitmap;
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap(null);
        return bitmapCreateBitmap;
    }

    private static PrintAttributes.Builder copyAttributes(PrintAttributes printAttributes) {
        PrintAttributes.Builder minMargins = new PrintAttributes.Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            minMargins.setColorMode(printAttributes.getColorMode());
        }
        if (Build.VERSION.SDK_INT >= 23 && printAttributes.getDuplexMode() != 0) {
            minMargins.setDuplexMode(printAttributes.getDuplexMode());
        }
        return minMargins;
    }

    static Matrix getMatrix(int i, int i2, RectF rectF, int i3) {
        Matrix matrix = new Matrix();
        float f = i;
        float fWidth = rectF.width() / f;
        float fMax = i3 == 2 ? Math.max(fWidth, rectF.height() / i2) : Math.min(fWidth, rectF.height() / i2);
        matrix.postScale(fMax, fMax);
        matrix.postTranslate((rectF.width() - (f * fMax)) / 2.0f, (rectF.height() - (i2 * fMax)) / 2.0f);
        return matrix;
    }

    static boolean isPortrait(Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }

    private Bitmap loadBitmap(Uri uri, BitmapFactory.Options options) throws Throwable {
        Context context;
        InputStream inputStream;
        InputStream inputStreamOpenInputStream;
        if (uri == null || (context = this.mContext) == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        try {
            inputStreamOpenInputStream = context.getContentResolver().openInputStream(uri);
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
            if (inputStreamOpenInputStream != null) {
                try {
                    inputStreamOpenInputStream.close();
                } catch (IOException e) {
                    Log.w(LOG_TAG, "close fail ", e);
                }
            }
            return bitmapDecodeStream;
        } catch (Throwable th2) {
            th = th2;
            inputStream = inputStreamOpenInputStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    Log.w(LOG_TAG, "close fail ", e2);
                }
            }
            throw th;
        }
    }

    public static boolean systemSupportsPrint() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public int getOrientation() {
        if (Build.VERSION.SDK_INT < 19 || this.mOrientation != 0) {
            return this.mOrientation;
        }
        return 1;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    Bitmap loadConstrainedBitmap(Uri uri) throws Throwable {
        int i;
        BitmapFactory.Options options;
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inJustDecodeBounds = true;
        loadBitmap(uri, options2);
        int i2 = options2.outWidth;
        int i3 = options2.outHeight;
        if (i2 <= 0 || i3 <= 0) {
            return null;
        }
        int iMax = Math.max(i2, i3);
        int i4 = 1;
        while (true) {
            i = i4;
            if (iMax <= MAX_PRINT_SIZE) {
                break;
            }
            iMax >>>= 1;
            i4 = i << 1;
        }
        if (i <= 0 || Math.min(i2, i3) / i <= 0) {
            return null;
        }
        synchronized (this.mLock) {
            BitmapFactory.Options options3 = new BitmapFactory.Options();
            this.mDecodeOptions = options3;
            options3.inMutable = true;
            this.mDecodeOptions.inSampleSize = i;
            options = this.mDecodeOptions;
        }
        try {
            Bitmap bitmapLoadBitmap = loadBitmap(uri, options);
            synchronized (this.mLock) {
                this.mDecodeOptions = null;
            }
            return bitmapLoadBitmap;
        } catch (Throwable th) {
            synchronized (this.mLock) {
                this.mDecodeOptions = null;
                throw th;
            }
        }
    }

    public void printBitmap(String str, Bitmap bitmap) {
        printBitmap(str, bitmap, (OnPrintFinishCallback) null);
    }

    public void printBitmap(String str, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        if (Build.VERSION.SDK_INT < 19 || bitmap == null) {
            return;
        }
        ((PrintManager) this.mContext.getSystemService("print")).print(str, new PrintBitmapAdapter(this, str, this.mScaleMode, bitmap, onPrintFinishCallback), new PrintAttributes.Builder().setMediaSize(isPortrait(bitmap) ? PrintAttributes.MediaSize.UNKNOWN_PORTRAIT : PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE).setColorMode(this.mColorMode).build());
    }

    public void printBitmap(String str, Uri uri) throws FileNotFoundException {
        printBitmap(str, uri, (OnPrintFinishCallback) null);
    }

    public void printBitmap(String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        PrintUriAdapter printUriAdapter = new PrintUriAdapter(this, str, uri, onPrintFinishCallback, this.mScaleMode);
        PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        int i = this.mOrientation;
        if (i == 1 || i == 0) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        } else if (i == 2) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(str, printUriAdapter, builder.build());
    }

    public void setColorMode(int i) {
        this.mColorMode = i;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public void setScaleMode(int i) {
        this.mScaleMode = i;
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [android.support.v4.print.PrintHelper$1] */
    void writeBitmap(PrintAttributes printAttributes, int i, Bitmap bitmap, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        new AsyncTask<Void, Void, Throwable>(this, cancellationSignal, IS_MIN_MARGINS_HANDLING_CORRECT ? printAttributes : copyAttributes(printAttributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build(), bitmap, printAttributes, i, parcelFileDescriptor, writeResultCallback) { // from class: android.support.v4.print.PrintHelper.1
            final PrintHelper this$0;
            final PrintAttributes val$attributes;
            final Bitmap val$bitmap;
            final CancellationSignal val$cancellationSignal;
            final ParcelFileDescriptor val$fileDescriptor;
            final int val$fittingMode;
            final PrintAttributes val$pdfAttributes;
            final PrintDocumentAdapter.WriteResultCallback val$writeResultCallback;

            {
                this.this$0 = this;
                this.val$cancellationSignal = cancellationSignal;
                this.val$pdfAttributes = printAttributes;
                this.val$bitmap = bitmap;
                this.val$attributes = printAttributes;
                this.val$fittingMode = i;
                this.val$fileDescriptor = parcelFileDescriptor;
                this.val$writeResultCallback = writeResultCallback;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Throwable doInBackground(Void... voidArr) {
                RectF rectF;
                try {
                    if (this.val$cancellationSignal.isCanceled()) {
                        return null;
                    }
                    PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(this.this$0.mContext, this.val$pdfAttributes);
                    Bitmap bitmapConvertBitmapForColorMode = PrintHelper.convertBitmapForColorMode(this.val$bitmap, this.val$pdfAttributes.getColorMode());
                    if (this.val$cancellationSignal.isCanceled()) {
                        return null;
                    }
                    try {
                        PdfDocument.Page pageStartPage = printedPdfDocument.startPage(1);
                        if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                            rectF = new RectF(pageStartPage.getInfo().getContentRect());
                        } else {
                            PrintedPdfDocument printedPdfDocument2 = new PrintedPdfDocument(this.this$0.mContext, this.val$attributes);
                            PdfDocument.Page pageStartPage2 = printedPdfDocument2.startPage(1);
                            rectF = new RectF(pageStartPage2.getInfo().getContentRect());
                            printedPdfDocument2.finishPage(pageStartPage2);
                            printedPdfDocument2.close();
                        }
                        Matrix matrix = PrintHelper.getMatrix(bitmapConvertBitmapForColorMode.getWidth(), bitmapConvertBitmapForColorMode.getHeight(), rectF, this.val$fittingMode);
                        if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                            matrix.postTranslate(rectF.left, rectF.top);
                            pageStartPage.getCanvas().clipRect(rectF);
                        }
                        pageStartPage.getCanvas().drawBitmap(bitmapConvertBitmapForColorMode, matrix, null);
                        printedPdfDocument.finishPage(pageStartPage);
                        if (this.val$cancellationSignal.isCanceled()) {
                        }
                        printedPdfDocument.writeTo(new FileOutputStream(this.val$fileDescriptor.getFileDescriptor()));
                        printedPdfDocument.close();
                        if (this.val$fileDescriptor != null) {
                            try {
                                this.val$fileDescriptor.close();
                            } catch (IOException e) {
                            }
                        }
                        if (bitmapConvertBitmapForColorMode == this.val$bitmap) {
                            return null;
                        }
                        bitmapConvertBitmapForColorMode.recycle();
                        return null;
                    } finally {
                        printedPdfDocument.close();
                        if (this.val$fileDescriptor != null) {
                            try {
                                this.val$fileDescriptor.close();
                            } catch (IOException e2) {
                            }
                        }
                        if (bitmapConvertBitmapForColorMode != this.val$bitmap) {
                            bitmapConvertBitmapForColorMode.recycle();
                        }
                    }
                } catch (Throwable th) {
                    return th;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Throwable th) {
                if (this.val$cancellationSignal.isCanceled()) {
                    this.val$writeResultCallback.onWriteCancelled();
                } else if (th == null) {
                    this.val$writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } else {
                    Log.e(PrintHelper.LOG_TAG, "Error writing printed content", th);
                    this.val$writeResultCallback.onWriteFailed(null);
                }
            }
        }.execute(new Void[0]);
    }
}
