package android.support.v4.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.SelfDestructiveThread;
import android.support.v4.util.LruCache;
import android.support.v4.util.Preconditions;
import android.support.v4.util.SimpleArrayMap;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat.class */
public class FontsContractCompat {
    public static final String PARCEL_FONT_RESULTS = "font_results";
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    private static final SelfDestructiveThread sBackgroundThread = new SelfDestructiveThread("fonts", 10, BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS);
    static final Object sLock = new Object();
    static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies = new SimpleArrayMap<>();
    private static final Comparator<byte[]> sByteArrayComparator = new Comparator<byte[]>() { // from class: android.support.v4.provider.FontsContractCompat.5
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v15, types: [int] */
        /* JADX WARN: Type inference failed for: r0v17, types: [int] */
        @Override // java.util.Comparator
        public int compare(byte[] bArr, byte[] bArr2) {
            byte length;
            byte length2;
            if (bArr.length == bArr2.length) {
                for (int i = 0; i < bArr.length; i++) {
                    if (bArr[i] != bArr2[i]) {
                        byte b = bArr[i];
                        length = bArr2[i];
                        length2 = b;
                    }
                }
                return 0;
            }
            length2 = bArr.length;
            length = bArr2.length;
            return length2 - length;
        }
    };

    /* renamed from: android.support.v4.provider.FontsContractCompat$4 */
    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$4.class */
    static final class RunnableC02184 implements Runnable {
        final FontRequestCallback val$callback;
        final Handler val$callerThreadHandler;
        final Context val$context;
        final FontRequest val$request;

        RunnableC02184(Context context, FontRequest fontRequest, Handler handler, FontRequestCallback fontRequestCallback) {
            this.val$context = context;
            this.val$request = fontRequest;
            this.val$callerThreadHandler = handler;
            this.val$callback = fontRequestCallback;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                FontFamilyResult fontFamilyResultFetchFonts = FontsContractCompat.fetchFonts(this.val$context, null, this.val$request);
                if (fontFamilyResultFetchFonts.getStatusCode() != 0) {
                    int statusCode = fontFamilyResultFetchFonts.getStatusCode();
                    if (statusCode == 1) {
                        this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.2
                            final RunnableC02184 this$0;

                            {
                                this.this$0 = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                this.this$0.val$callback.onTypefaceRequestFailed(-2);
                            }
                        });
                        return;
                    } else if (statusCode != 2) {
                        this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.4
                            final RunnableC02184 this$0;

                            {
                                this.this$0 = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                this.this$0.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    } else {
                        this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.3
                            final RunnableC02184 this$0;

                            {
                                this.this$0 = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                this.this$0.val$callback.onTypefaceRequestFailed(-3);
                            }
                        });
                        return;
                    }
                }
                FontInfo[] fonts = fontFamilyResultFetchFonts.getFonts();
                if (fonts == null || fonts.length == 0) {
                    this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.5
                        final RunnableC02184 this$0;

                        {
                            this.this$0 = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            this.this$0.val$callback.onTypefaceRequestFailed(1);
                        }
                    });
                    return;
                }
                for (FontInfo fontInfo : fonts) {
                    if (fontInfo.getResultCode() != 0) {
                        int resultCode = fontInfo.getResultCode();
                        if (resultCode < 0) {
                            this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.6
                                final RunnableC02184 this$0;

                                {
                                    this.this$0 = this;
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        } else {
                            this.val$callerThreadHandler.post(new Runnable(this, resultCode) { // from class: android.support.v4.provider.FontsContractCompat.4.7
                                final RunnableC02184 this$0;
                                final int val$resultCode;

                                {
                                    this.this$0 = this;
                                    this.val$resultCode = resultCode;
                                }

                                @Override // java.lang.Runnable
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(this.val$resultCode);
                                }
                            });
                            return;
                        }
                    }
                }
                Typeface typefaceBuildTypeface = FontsContractCompat.buildTypeface(this.val$context, null, fonts);
                if (typefaceBuildTypeface == null) {
                    this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.8
                        final RunnableC02184 this$0;

                        {
                            this.this$0 = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            this.this$0.val$callback.onTypefaceRequestFailed(-3);
                        }
                    });
                } else {
                    this.val$callerThreadHandler.post(new Runnable(this, typefaceBuildTypeface) { // from class: android.support.v4.provider.FontsContractCompat.4.9
                        final RunnableC02184 this$0;
                        final Typeface val$typeface;

                        {
                            this.this$0 = this;
                            this.val$typeface = typefaceBuildTypeface;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            this.this$0.val$callback.onTypefaceRetrieved(this.val$typeface);
                        }
                    });
                }
            } catch (PackageManager.NameNotFoundException e) {
                this.val$callerThreadHandler.post(new Runnable(this) { // from class: android.support.v4.provider.FontsContractCompat.4.1
                    final RunnableC02184 this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this.this$0.val$callback.onTypefaceRequestFailed(-1);
                    }
                });
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$Columns.class */
    public static final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$FontFamilyResult.class */
    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        public FontFamilyResult(int i, FontInfo[] fontInfoArr) {
            this.mStatusCode = i;
            this.mFonts = fontInfoArr;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$FontInfo.class */
    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int i, int i2, boolean z, int i3) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = i;
            this.mWeight = i2;
            this.mItalic = z;
            this.mResultCode = i3;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$FontRequestCallback.class */
    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        public static final int RESULT_OK = 0;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$FontRequestCallback$FontRequestFailReason.class */
        public @interface FontRequestFailReason {
        }

        public void onTypefaceRequestFailed(int i) {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/provider/FontsContractCompat$TypefaceResult.class */
    private static final class TypefaceResult {
        final int mResult;
        final Typeface mTypeface;

        TypefaceResult(Typeface typeface, int i) {
            this.mTypeface = typeface;
            this.mResult = i;
        }
    }

    private FontsContractCompat() {
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fontInfoArr) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, fontInfoArr, 0);
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatureArr) {
        ArrayList arrayList = new ArrayList();
        for (Signature signature : signatureArr) {
            arrayList.add(signature.toByteArray());
        }
        return arrayList;
    }

    private static boolean equalsByteArrayList(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!Arrays.equals(list.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        return provider == null ? new FontFamilyResult(1, null) : new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }

    private static List<List<byte[]>> getCertificates(FontRequest fontRequest, Resources resources) {
        return fontRequest.getCertificates() != null ? fontRequest.getCertificates() : FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x01d3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static android.support.v4.provider.FontsContractCompat.FontInfo[] getFontFromProvider(android.content.Context r9, android.support.v4.provider.FontRequest r10, java.lang.String r11, android.os.CancellationSignal r12) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 513
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.FontsContractCompat.getFontFromProvider(android.content.Context, android.support.v4.provider.FontRequest, java.lang.String, android.os.CancellationSignal):android.support.v4.provider.FontsContractCompat$FontInfo[]");
    }

    static TypefaceResult getFontInternal(Context context, FontRequest fontRequest, int i) {
        try {
            FontFamilyResult fontFamilyResultFetchFonts = fetchFonts(context, null, fontRequest);
            int i2 = -3;
            if (fontFamilyResultFetchFonts.getStatusCode() != 0) {
                if (fontFamilyResultFetchFonts.getStatusCode() == 1) {
                    i2 = -2;
                }
                return new TypefaceResult(null, i2);
            }
            Typeface typefaceCreateFromFontInfo = TypefaceCompat.createFromFontInfo(context, null, fontFamilyResultFetchFonts.getFonts(), i);
            if (typefaceCreateFromFontInfo != null) {
                i2 = 0;
            }
            return new TypefaceResult(typefaceCreateFromFontInfo, i2);
        } catch (PackageManager.NameNotFoundException e) {
            return new TypefaceResult(null, -1);
        }
    }

    public static Typeface getFontSync(Context context, FontRequest fontRequest, ResourcesCompat.FontCallback fontCallback, Handler handler, boolean z, int i, int i2) {
        String str = fontRequest.getIdentifier() + "-" + i2;
        Typeface typeface = sTypefaceCache.get(str);
        if (typeface != null) {
            if (fontCallback != null) {
                fontCallback.onFontRetrieved(typeface);
            }
            return typeface;
        }
        if (z && i == -1) {
            TypefaceResult fontInternal = getFontInternal(context, fontRequest, i2);
            if (fontCallback != null) {
                if (fontInternal.mResult == 0) {
                    fontCallback.callbackSuccessAsync(fontInternal.mTypeface, handler);
                } else {
                    fontCallback.callbackFailAsync(fontInternal.mResult, handler);
                }
            }
            return fontInternal.mTypeface;
        }
        Callable<TypefaceResult> callable = new Callable<TypefaceResult>(context, fontRequest, i2, str) { // from class: android.support.v4.provider.FontsContractCompat.1
            final Context val$context;
            final String val$id;
            final FontRequest val$request;
            final int val$style;

            {
                this.val$context = context;
                this.val$request = fontRequest;
                this.val$style = i2;
                this.val$id = str;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public TypefaceResult call() throws Exception {
                TypefaceResult fontInternal2 = FontsContractCompat.getFontInternal(this.val$context, this.val$request, this.val$style);
                if (fontInternal2.mTypeface != null) {
                    FontsContractCompat.sTypefaceCache.put(this.val$id, fontInternal2.mTypeface);
                }
                return fontInternal2;
            }
        };
        Typeface typeface2 = null;
        if (z) {
            try {
                typeface2 = ((TypefaceResult) sBackgroundThread.postAndWait(callable, i)).mTypeface;
            } catch (InterruptedException e) {
            }
            return typeface2;
        }
        SelfDestructiveThread.ReplyCallback<TypefaceResult> replyCallback = fontCallback == null ? null : new SelfDestructiveThread.ReplyCallback<TypefaceResult>(fontCallback, handler) { // from class: android.support.v4.provider.FontsContractCompat.2
            final ResourcesCompat.FontCallback val$fontCallback;
            final Handler val$handler;

            {
                this.val$fontCallback = fontCallback;
                this.val$handler = handler;
            }

            @Override // android.support.v4.provider.SelfDestructiveThread.ReplyCallback
            public void onReply(TypefaceResult typefaceResult) {
                if (typefaceResult == null) {
                    this.val$fontCallback.callbackFailAsync(1, this.val$handler);
                } else if (typefaceResult.mResult == 0) {
                    this.val$fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, this.val$handler);
                } else {
                    this.val$fontCallback.callbackFailAsync(typefaceResult.mResult, this.val$handler);
                }
            }
        };
        synchronized (sLock) {
            if (sPendingReplies.containsKey(str)) {
                if (replyCallback != null) {
                    sPendingReplies.get(str).add(replyCallback);
                }
                return null;
            }
            if (replyCallback != null) {
                ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> arrayList = new ArrayList<>();
                arrayList.add(replyCallback);
                sPendingReplies.put(str, arrayList);
            }
            sBackgroundThread.postAndReply(callable, new SelfDestructiveThread.ReplyCallback<TypefaceResult>(str) { // from class: android.support.v4.provider.FontsContractCompat.3
                final String val$id;

                {
                    this.val$id = str;
                }

                @Override // android.support.v4.provider.SelfDestructiveThread.ReplyCallback
                public void onReply(TypefaceResult typefaceResult) {
                    synchronized (FontsContractCompat.sLock) {
                        try {
                            ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> arrayList2 = FontsContractCompat.sPendingReplies.get(this.val$id);
                            if (arrayList2 == null) {
                                return;
                            }
                            FontsContractCompat.sPendingReplies.remove(this.val$id);
                            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                                arrayList2.get(i3).onReply(typefaceResult);
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                }
            });
            return null;
        }
    }

    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest fontRequest, Resources resources) throws PackageManager.NameNotFoundException {
        String providerAuthority = fontRequest.getProviderAuthority();
        ProviderInfo providerInfoResolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (providerInfoResolveContentProvider == null) {
            throw new PackageManager.NameNotFoundException("No package found for authority: " + providerAuthority);
        }
        if (!providerInfoResolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
            throw new PackageManager.NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + fontRequest.getProviderPackage());
        }
        List<byte[]> listConvertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(providerInfoResolveContentProvider.packageName, 64).signatures);
        Collections.sort(listConvertToByteArrayList, sByteArrayComparator);
        List<List<byte[]>> certificates = getCertificates(fontRequest, resources);
        for (int i = 0; i < certificates.size(); i++) {
            ArrayList arrayList = new ArrayList(certificates.get(i));
            Collections.sort(arrayList, sByteArrayComparator);
            if (equalsByteArrayList(listConvertToByteArrayList, arrayList)) {
                return providerInfoResolveContentProvider;
            }
        }
        return null;
    }

    public static Map<Uri, ByteBuffer> prepareFontData(Context context, FontInfo[] fontInfoArr, CancellationSignal cancellationSignal) {
        HashMap map = new HashMap();
        for (FontInfo fontInfo : fontInfoArr) {
            if (fontInfo.getResultCode() == 0) {
                Uri uri = fontInfo.getUri();
                if (!map.containsKey(uri)) {
                    map.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    public static void requestFont(Context context, FontRequest fontRequest, FontRequestCallback fontRequestCallback, Handler handler) {
        handler.post(new RunnableC02184(context, fontRequest, new Handler(), fontRequestCallback));
    }

    public static void resetCache() {
        sTypefaceCache.evictAll();
    }
}
