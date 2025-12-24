package android.support.v4.graphics;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.util.Pair;

/* loaded from: classes-dex2jar.jar:android/support/v4/graphics/PaintCompat.class */
public final class PaintCompat {
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "��";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<>();

    private PaintCompat() {
    }

    public static boolean hasGlyph(Paint paint, String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return paint.hasGlyph(str);
        }
        int length = str.length();
        if (length == 1 && Character.isWhitespace(str.charAt(0))) {
            return true;
        }
        float fMeasureText = paint.measureText(TOFU_STRING);
        float fMeasureText2 = paint.measureText(EM_STRING);
        float fMeasureText3 = paint.measureText(str);
        float fMeasureText4 = 0.0f;
        if (fMeasureText3 == 0.0f) {
            return false;
        }
        if (str.codePointCount(0, str.length()) > 1) {
            if (fMeasureText3 > fMeasureText2 * 2.0f) {
                return false;
            }
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
                int iCharCount = Character.charCount(str.codePointAt(i2)) + i2;
                fMeasureText4 += paint.measureText(str, i2, iCharCount);
                i = iCharCount;
            }
            if (fMeasureText3 >= fMeasureText4) {
                return false;
            }
        }
        if (fMeasureText3 != fMeasureText) {
            return true;
        }
        Pair<Rect, Rect> pairObtainEmptyRects = obtainEmptyRects();
        paint.getTextBounds(TOFU_STRING, 0, 2, pairObtainEmptyRects.first);
        paint.getTextBounds(str, 0, length, pairObtainEmptyRects.second);
        return !pairObtainEmptyRects.first.equals(pairObtainEmptyRects.second);
    }

    private static Pair<Rect, Rect> obtainEmptyRects() {
        Pair<Rect, Rect> pair = sRectThreadLocal.get();
        if (pair == null) {
            pair = new Pair<>(new Rect(), new Rect());
            sRectThreadLocal.set(pair);
        } else {
            pair.first.setEmpty();
            pair.second.setEmpty();
        }
        return pair;
    }
}
