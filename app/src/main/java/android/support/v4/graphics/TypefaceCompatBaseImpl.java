package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes-dex2jar.jar:android/support/v4/graphics/TypefaceCompatBaseImpl.class */
class TypefaceCompatBaseImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    /* loaded from: classes-dex2jar.jar:android/support/v4/graphics/TypefaceCompatBaseImpl$StyleExtractor.class */
    private interface StyleExtractor<T> {
        int getWeight(T t);

        boolean isItalic(T t);
    }

    TypefaceCompatBaseImpl() {
    }

    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, int i) {
        return (FontResourcesParserCompat.FontFileResourceEntry) findBestFont(fontFamilyFilesResourceEntry.getEntries(), i, new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>(this) { // from class: android.support.v4.graphics.TypefaceCompatBaseImpl.2
            final TypefaceCompatBaseImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.graphics.TypefaceCompatBaseImpl.StyleExtractor
            public int getWeight(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }

            @Override // android.support.v4.graphics.TypefaceCompatBaseImpl.StyleExtractor
            public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0076  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static <T> T findBestFont(T[] r3, int r4, android.support.v4.graphics.TypefaceCompatBaseImpl.StyleExtractor<T> r5) {
        /*
            r0 = r4
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto Ld
            r0 = 400(0x190, float:5.6E-43)
            r6 = r0
            goto L11
        Ld:
            r0 = 700(0x2bc, float:9.81E-43)
            r6 = r0
        L11:
            r0 = r4
            r1 = 2
            r0 = r0 & r1
            if (r0 == 0) goto L1d
            r0 = 1
            r11 = r0
            goto L20
        L1d:
            r0 = 0
            r11 = r0
        L20:
            r0 = 0
            r12 = r0
            r0 = 2147483647(0x7fffffff, float:NaN)
            r7 = r0
            r0 = r3
            int r0 = r0.length
            r10 = r0
            r0 = 0
            r4 = r0
        L2d:
            r0 = r4
            r1 = r10
            if (r0 >= r1) goto L88
            r0 = r3
            r1 = r4
            r0 = r0[r1]
            r13 = r0
            r0 = r5
            r1 = r13
            int r0 = r0.getWeight(r1)
            r1 = r6
            int r0 = r0 - r1
            int r0 = java.lang.Math.abs(r0)
            r9 = r0
            r0 = r5
            r1 = r13
            boolean r0 = r0.isItalic(r1)
            r1 = r11
            if (r0 != r1) goto L5a
            r0 = 0
            r8 = r0
            goto L5d
        L5a:
            r0 = 1
            r8 = r0
        L5d:
            r0 = r9
            r1 = 2
            int r0 = r0 * r1
            r1 = r8
            int r0 = r0 + r1
            r9 = r0
            r0 = r12
            if (r0 == 0) goto L76
            r0 = r7
            r8 = r0
            r0 = r7
            r1 = r9
            if (r0 <= r1) goto L7e
        L76:
            r0 = r13
            r12 = r0
            r0 = r9
            r8 = r0
        L7e:
            int r4 = r4 + 1
            r0 = r8
            r7 = r0
            goto L2d
        L88:
            r0 = r12
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.TypefaceCompatBaseImpl.findBestFont(java.lang.Object[], int, android.support.v4.graphics.TypefaceCompatBaseImpl$StyleExtractor):java.lang.Object");
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) {
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntryFindBestEntry = findBestEntry(fontFamilyFilesResourceEntry, i);
        if (fontFileResourceEntryFindBestEntry == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, fontFileResourceEntryFindBestEntry.getResourceId(), fontFileResourceEntryFindBestEntry.getFileName(), i);
    }

    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fontInfoArr, int i) throws Throwable {
        InputStream inputStreamOpenInputStream;
        if (fontInfoArr.length < 1) {
            return null;
        }
        try {
            inputStreamOpenInputStream = context.getContentResolver().openInputStream(findBestInfo(fontInfoArr, i).getUri());
            try {
                Typeface typefaceCreateFromInputStream = createFromInputStream(context, inputStreamOpenInputStream);
                TypefaceCompatUtil.closeQuietly(inputStreamOpenInputStream);
                return typefaceCreateFromInputStream;
            } catch (IOException e) {
                TypefaceCompatUtil.closeQuietly(inputStreamOpenInputStream);
                return null;
            } catch (Throwable th) {
                th = th;
                TypefaceCompatUtil.closeQuietly(inputStreamOpenInputStream);
                throw th;
            }
        } catch (IOException e2) {
            inputStreamOpenInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            inputStreamOpenInputStream = null;
        }
    }

    protected Typeface createFromInputStream(Context context, InputStream inputStream) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, inputStream)) {
                tempFile.delete();
                return null;
            }
            Typeface typefaceCreateFromFile = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return typefaceCreateFromFile;
        } catch (RuntimeException e) {
            tempFile.delete();
            return null;
        } catch (Throwable th) {
            tempFile.delete();
            throw th;
        }
    }

    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        File tempFile = TypefaceCompatUtil.getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile(tempFile, resources, i)) {
                tempFile.delete();
                return null;
            }
            Typeface typefaceCreateFromFile = Typeface.createFromFile(tempFile.getPath());
            tempFile.delete();
            return typefaceCreateFromFile;
        } catch (RuntimeException e) {
            tempFile.delete();
            return null;
        } catch (Throwable th) {
            tempFile.delete();
            throw th;
        }
    }

    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] fontInfoArr, int i) {
        return (FontsContractCompat.FontInfo) findBestFont(fontInfoArr, i, new StyleExtractor<FontsContractCompat.FontInfo>(this) { // from class: android.support.v4.graphics.TypefaceCompatBaseImpl.1
            final TypefaceCompatBaseImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.graphics.TypefaceCompatBaseImpl.StyleExtractor
            public int getWeight(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.getWeight();
            }

            @Override // android.support.v4.graphics.TypefaceCompatBaseImpl.StyleExtractor
            public boolean isItalic(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }
}
