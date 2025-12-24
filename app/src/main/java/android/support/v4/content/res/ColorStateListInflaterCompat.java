package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.compat.C0017R;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes-dex2jar.jar:android/support/v4/content/res/ColorStateListInflaterCompat.class */
public final class ColorStateListInflaterCompat {
    private static final int DEFAULT_COLOR = -65536;

    private ColorStateListInflaterCompat() {
    }

    public static ColorStateList createFromXml(Resources resources, XmlPullParser xmlPullParser, Resources.Theme theme) throws XmlPullParserException, IOException {
        int next;
        AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(xmlPullParser);
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return createFromXmlInner(resources, xmlPullParser, attributeSetAsAttributeSet, theme);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static ColorStateList createFromXmlInner(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        String name = xmlPullParser.getName();
        if (name.equals("selector")) {
            return inflate(resources, xmlPullParser, attributeSet, theme);
        }
        throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid color state list tag " + name);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [int[], int[][], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v53, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r0v55, types: [int[][]] */
    private static ColorStateList inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        int depth;
        int i;
        int depth2 = xmlPullParser.getDepth() + 1;
        int[] iArr = new int[20];
        int[] iArrAppend = new int[20];
        int i2 = 0;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1 || ((depth = xmlPullParser.getDepth()) < depth2 && next == 3)) {
                break;
            }
            if (next == 2 && depth <= depth2 && xmlPullParser.getName().equals("item")) {
                TypedArray typedArrayObtainAttributes = obtainAttributes(resources, theme, attributeSet, C0017R.styleable.ColorStateListItem);
                int color = typedArrayObtainAttributes.getColor(C0017R.styleable.ColorStateListItem_android_color, -65281);
                float f = 1.0f;
                if (typedArrayObtainAttributes.hasValue(C0017R.styleable.ColorStateListItem_android_alpha)) {
                    f = typedArrayObtainAttributes.getFloat(C0017R.styleable.ColorStateListItem_android_alpha, 1.0f);
                } else if (typedArrayObtainAttributes.hasValue(C0017R.styleable.ColorStateListItem_alpha)) {
                    f = typedArrayObtainAttributes.getFloat(C0017R.styleable.ColorStateListItem_alpha, 1.0f);
                }
                typedArrayObtainAttributes.recycle();
                int attributeCount = attributeSet.getAttributeCount();
                int[] iArr2 = new int[attributeCount];
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    i = i4;
                    if (i3 >= attributeCount) {
                        break;
                    }
                    int attributeNameResource = attributeSet.getAttributeNameResource(i3);
                    int i5 = i;
                    if (attributeNameResource != 16843173) {
                        i5 = i;
                        if (attributeNameResource != 16843551) {
                            i5 = i;
                            if (attributeNameResource != C0017R.attr.alpha) {
                                iArr2[i] = attributeSet.getAttributeBooleanValue(i3, false) ? attributeNameResource : -attributeNameResource;
                                i5 = i + 1;
                            }
                        }
                    }
                    i3++;
                    i4 = i5;
                }
                int[] iArrTrimStateSet = StateSet.trimStateSet(iArr2, i);
                int iModulateColorAlpha = modulateColorAlpha(color, f);
                if (i2 != 0) {
                    int length = iArrTrimStateSet.length;
                }
                iArrAppend = GrowingArrayUtils.append(iArrAppend, i2, iModulateColorAlpha);
                iArr = (int[][]) GrowingArrayUtils.append((int[][]) iArr, i2, iArrTrimStateSet);
                i2++;
            }
        }
        int[] iArr3 = new int[i2];
        ?? r0 = new int[i2];
        System.arraycopy(iArrAppend, 0, iArr3, 0, i2);
        System.arraycopy(iArr, 0, r0, 0, i2);
        return new ColorStateList(r0, iArr3);
    }

    private static int modulateColorAlpha(int i, float f) {
        return (i & ViewCompat.MEASURED_SIZE_MASK) | (Math.round(Color.alpha(i) * f) << 24);
    }

    private static TypedArray obtainAttributes(Resources resources, Resources.Theme theme, AttributeSet attributeSet, int[] iArr) {
        return theme == null ? resources.obtainAttributes(attributeSet, iArr) : theme.obtainStyledAttributes(attributeSet, iArr, 0, 0);
    }
}
