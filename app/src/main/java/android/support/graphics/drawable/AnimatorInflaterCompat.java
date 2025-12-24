package android.support.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes-dex2jar.jar:android/support/graphics/drawable/AnimatorInflaterCompat.class */
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    /* loaded from: classes-dex2jar.jar:android/support/graphics/drawable/AnimatorInflaterCompat$PathDataEvaluator.class */
    private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]> {
        private PathParser.PathDataNode[] mNodeArray;

        PathDataEvaluator() {
        }

        PathDataEvaluator(PathParser.PathDataNode[] pathDataNodeArr) {
            this.mNodeArray = pathDataNodeArr;
        }

        @Override // android.animation.TypeEvaluator
        public PathParser.PathDataNode[] evaluate(float f, PathParser.PathDataNode[] pathDataNodeArr, PathParser.PathDataNode[] pathDataNodeArr2) {
            if (!PathParser.canMorph(pathDataNodeArr, pathDataNodeArr2)) {
                throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            }
            PathParser.PathDataNode[] pathDataNodeArr3 = this.mNodeArray;
            if (pathDataNodeArr3 == null || !PathParser.canMorph(pathDataNodeArr3, pathDataNodeArr)) {
                this.mNodeArray = PathParser.deepCopyNodes(pathDataNodeArr);
            }
            for (int i = 0; i < pathDataNodeArr.length; i++) {
                this.mNodeArray[i].interpolatePathDataNode(pathDataNodeArr[i], pathDataNodeArr2[i], f);
            }
            return this.mNodeArray;
        }
    }

    private AnimatorInflaterCompat() {
    }

    private static Animator createAnimatorFromXml(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, float f) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, resources, theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x0158, code lost:
    
        if (r14 == null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x015d, code lost:
    
        if (r22 == null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0160, code lost:
    
        r0 = new android.animation.Animator[r22.size()];
        r0 = r22.iterator();
        r17 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0179, code lost:
    
        if (r0.hasNext() == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x017c, code lost:
    
        r0[r17] = (android.animation.Animator) r0.next();
        r17 = r17 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0191, code lost:
    
        if (r15 != 0) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0194, code lost:
    
        r14.playTogether(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x019d, code lost:
    
        r14.playSequentially(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01a5, code lost:
    
        return r23;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r21v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.animation.Animator createAnimatorFromXml(android.content.Context r9, android.content.res.Resources r10, android.content.res.Resources.Theme r11, org.xmlpull.v1.XmlPullParser r12, android.util.AttributeSet r13, android.animation.AnimatorSet r14, int r15, float r16) throws org.xmlpull.v1.XmlPullParserException, android.content.res.Resources.NotFoundException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 422
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.createAnimatorFromXml(android.content.Context, android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.animation.AnimatorSet, int, float):android.animation.Animator");
    }

    private static Keyframe createNewKeyframe(Keyframe keyframe, float f) {
        return keyframe.getType() == Float.TYPE ? Keyframe.ofFloat(f) : keyframe.getType() == Integer.TYPE ? Keyframe.ofInt(f) : Keyframe.ofObject(f);
    }

    private static void distributeKeyframes(Keyframe[] keyframeArr, float f, int i, int i2) {
        float f2 = f / ((i2 - i) + 2);
        while (i <= i2) {
            keyframeArr[i].setFraction(keyframeArr[i - 1].getFraction() + f2);
            i++;
        }
    }

    private static void dumpKeyframes(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            return;
        }
        Log.d(TAG, str);
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            Keyframe keyframe = (Keyframe) objArr[i];
            StringBuilder sb = new StringBuilder();
            sb.append("Keyframe ");
            sb.append(i);
            sb.append(": fraction ");
            sb.append(keyframe.getFraction() < 0.0f ? "null" : Float.valueOf(keyframe.getFraction()));
            sb.append(", ");
            sb.append(", value : ");
            Object value = "null";
            if (keyframe.hasValue()) {
                value = keyframe.getValue();
            }
            sb.append(value);
            Log.d(TAG, sb.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.animation.PropertyValuesHolder getPVH(android.content.res.TypedArray r7, int r8, int r9, int r10, java.lang.String r11) {
        /*
            Method dump skipped, instructions count: 730
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.getPVH(android.content.res.TypedArray, int, int, int, java.lang.String):android.animation.PropertyValuesHolder");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0067  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int inferValueTypeFromValues(android.content.res.TypedArray r3, int r4, int r5) {
        /*
            r0 = r3
            r1 = r4
            android.util.TypedValue r0 = r0.peekValue(r1)
            r9 = r0
            r0 = 1
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = r9
            if (r0 == 0) goto L17
            r0 = 1
            r4 = r0
            goto L19
        L17:
            r0 = 0
            r4 = r0
        L19:
            r0 = r4
            if (r0 == 0) goto L26
            r0 = r9
            int r0 = r0.type
            r6 = r0
            goto L28
        L26:
            r0 = 0
            r6 = r0
        L28:
            r0 = r3
            r1 = r5
            android.util.TypedValue r0 = r0.peekValue(r1)
            r3 = r0
            r0 = r3
            if (r0 == 0) goto L38
            r0 = r7
            r5 = r0
            goto L3a
        L38:
            r0 = 0
            r5 = r0
        L3a:
            r0 = r5
            if (r0 == 0) goto L47
            r0 = r3
            int r0 = r0.type
            r7 = r0
            goto L4a
        L47:
            r0 = 0
            r7 = r0
        L4a:
            r0 = r4
            if (r0 == 0) goto L55
            r0 = r6
            boolean r0 = isColorType(r0)
            if (r0 != 0) goto L67
        L55:
            r0 = r8
            r4 = r0
            r0 = r5
            if (r0 == 0) goto L69
            r0 = r8
            r4 = r0
            r0 = r7
            boolean r0 = isColorType(r0)
            if (r0 == 0) goto L69
        L67:
            r0 = 3
            r4 = r0
        L69:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.graphics.drawable.AnimatorInflaterCompat.inferValueTypeFromValues(android.content.res.TypedArray, int, int):int");
    }

    private static int inferValueTypeOfKeyframe(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        TypedArray typedArrayObtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        TypedValue typedValuePeekNamedValue = TypedArrayUtils.peekNamedValue(typedArrayObtainAttributes, xmlPullParser, "value", 0);
        int i = 0;
        if (typedValuePeekNamedValue != null) {
            i = 0;
            if (isColorType(typedValuePeekNamedValue.type)) {
                i = 3;
            }
        }
        typedArrayObtainAttributes.recycle();
        return i;
    }

    private static boolean isColorType(int i) {
        return i >= 28 && i <= 31;
    }

    public static Animator loadAnimator(Context context, int i) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator(context, i) : loadAnimator(context, context.getResources(), context.getTheme(), i);
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, int i) throws Resources.NotFoundException {
        return loadAnimator(context, resources, theme, i, 1.0f);
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, int i, float f) throws Resources.NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        XmlResourceParser xmlResourceParser2 = null;
        XmlResourceParser xmlResourceParser3 = null;
        try {
            try {
                XmlResourceParser animation = resources.getAnimation(i);
                xmlResourceParser3 = animation;
                xmlResourceParser = animation;
                xmlResourceParser2 = animation;
                Animator animatorCreateAnimatorFromXml = createAnimatorFromXml(context, resources, theme, animation, f);
                if (animation != null) {
                    animation.close();
                }
                return animatorCreateAnimatorFromXml;
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Can't load animation resource ID #0x");
                sb.append(Integer.toHexString(i));
                Resources.NotFoundException notFoundException = new Resources.NotFoundException(sb.toString());
                notFoundException.initCause(e);
                throw notFoundException;
            } catch (XmlPullParserException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Can't load animation resource ID #0x");
                sb2.append(Integer.toHexString(i));
                Resources.NotFoundException notFoundException2 = new Resources.NotFoundException(sb2.toString());
                notFoundException2.initCause(e2);
                throw notFoundException2;
            }
        } catch (Throwable th) {
            if (xmlResourceParser3 != null) {
                xmlResourceParser3.close();
            }
            throw th;
        }
    }

    private static ValueAnimator loadAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, ValueAnimator valueAnimator, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        TypedArray typedArrayObtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR);
        TypedArray typedArrayObtainAttributes2 = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        ValueAnimator valueAnimator2 = valueAnimator;
        if (valueAnimator == null) {
            valueAnimator2 = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(valueAnimator2, typedArrayObtainAttributes, typedArrayObtainAttributes2, f, xmlPullParser);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(typedArrayObtainAttributes, xmlPullParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            valueAnimator2.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        typedArrayObtainAttributes.recycle();
        if (typedArrayObtainAttributes2 != null) {
            typedArrayObtainAttributes2.recycle();
        }
        return valueAnimator2;
    }

    private static Keyframe loadKeyframe(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, int i, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        float namedFloat = TypedArrayUtils.getNamedFloat(typedArrayObtainAttributes, xmlPullParser, "fraction", 3, -1.0f);
        TypedValue typedValuePeekNamedValue = TypedArrayUtils.peekNamedValue(typedArrayObtainAttributes, xmlPullParser, "value", 0);
        boolean z = typedValuePeekNamedValue != null;
        int i2 = i;
        if (i == 4) {
            i2 = (z && isColorType(typedValuePeekNamedValue.type)) ? 3 : 0;
        }
        Keyframe keyframeOfInt = z ? i2 != 0 ? (i2 == 1 || i2 == 3) ? Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(typedArrayObtainAttributes, xmlPullParser, "value", 0, 0)) : null : Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(typedArrayObtainAttributes, xmlPullParser, "value", 0, 0.0f)) : i2 == 0 ? Keyframe.ofFloat(namedFloat) : Keyframe.ofInt(namedFloat);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(typedArrayObtainAttributes, xmlPullParser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframeOfInt.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        typedArrayObtainAttributes.recycle();
        return keyframeOfInt;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, resources, theme, attributeSet, objectAnimator, f, xmlPullParser);
        return objectAnimator;
    }

    private static PropertyValuesHolder loadPvh(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, String str, int i) throws XmlPullParserException, IOException {
        ArrayList arrayList = null;
        int i2 = i;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 3 || next == 1) {
                break;
            }
            if (xmlPullParser.getName().equals("keyframe")) {
                int iInferValueTypeOfKeyframe = i2;
                if (i2 == 4) {
                    iInferValueTypeOfKeyframe = inferValueTypeOfKeyframe(resources, theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
                }
                Keyframe keyframeLoadKeyframe = loadKeyframe(context, resources, theme, Xml.asAttributeSet(xmlPullParser), iInferValueTypeOfKeyframe, xmlPullParser);
                ArrayList arrayList2 = arrayList;
                if (keyframeLoadKeyframe != null) {
                    arrayList2 = arrayList;
                    if (arrayList == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(keyframeLoadKeyframe);
                }
                xmlPullParser.next();
                arrayList = arrayList2;
                i2 = iInferValueTypeOfKeyframe;
            }
        }
        PropertyValuesHolder propertyValuesHolder = null;
        if (arrayList != null) {
            int size = arrayList.size();
            propertyValuesHolder = null;
            if (size > 0) {
                Keyframe keyframe = (Keyframe) arrayList.get(0);
                Keyframe keyframe2 = (Keyframe) arrayList.get(size - 1);
                float fraction = keyframe2.getFraction();
                int i3 = size;
                if (fraction < 1.0f) {
                    if (fraction < 0.0f) {
                        keyframe2.setFraction(1.0f);
                        i3 = size;
                    } else {
                        arrayList.add(arrayList.size(), createNewKeyframe(keyframe2, 1.0f));
                        i3 = size + 1;
                    }
                }
                float fraction2 = keyframe.getFraction();
                int i4 = i3;
                if (fraction2 != 0.0f) {
                    if (fraction2 < 0.0f) {
                        keyframe.setFraction(0.0f);
                        i4 = i3;
                    } else {
                        arrayList.add(0, createNewKeyframe(keyframe, 0.0f));
                        i4 = i3 + 1;
                    }
                }
                Keyframe[] keyframeArr = new Keyframe[i4];
                arrayList.toArray(keyframeArr);
                for (int i5 = 0; i5 < i4; i5++) {
                    Keyframe keyframe3 = keyframeArr[i5];
                    if (keyframe3.getFraction() < 0.0f) {
                        if (i5 == 0) {
                            keyframe3.setFraction(0.0f);
                        } else {
                            int i6 = i4 - 1;
                            if (i5 == i6) {
                                keyframe3.setFraction(1.0f);
                            } else {
                                int i7 = i5;
                                for (int i8 = i5 + 1; i8 < i6 && keyframeArr[i8].getFraction() < 0.0f; i8++) {
                                    i7 = i8;
                                }
                                distributeKeyframes(keyframeArr, keyframeArr[i7 + 1].getFraction() - keyframeArr[i5 - 1].getFraction(), i5, i7);
                            }
                        }
                    }
                }
                PropertyValuesHolder propertyValuesHolderOfKeyframe = PropertyValuesHolder.ofKeyframe(str, keyframeArr);
                propertyValuesHolder = propertyValuesHolderOfKeyframe;
                if (i2 == 3) {
                    propertyValuesHolderOfKeyframe.setEvaluator(ArgbEvaluator.getInstance());
                    propertyValuesHolder = propertyValuesHolderOfKeyframe;
                }
            }
        }
        return propertyValuesHolder;
    }

    private static PropertyValuesHolder[] loadValues(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int i;
        ArrayList arrayList = null;
        while (true) {
            int eventType = xmlPullParser.getEventType();
            i = 0;
            if (eventType == 3 || eventType == 1) {
                break;
            }
            if (eventType != 2) {
                xmlPullParser.next();
            } else {
                if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                    TypedArray typedArrayObtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    String namedString = TypedArrayUtils.getNamedString(typedArrayObtainAttributes, xmlPullParser, "propertyName", 3);
                    int namedInt = TypedArrayUtils.getNamedInt(typedArrayObtainAttributes, xmlPullParser, "valueType", 2, 4);
                    PropertyValuesHolder propertyValuesHolderLoadPvh = loadPvh(context, resources, theme, xmlPullParser, namedString, namedInt);
                    PropertyValuesHolder pvh = propertyValuesHolderLoadPvh;
                    if (propertyValuesHolderLoadPvh == null) {
                        pvh = getPVH(typedArrayObtainAttributes, namedInt, 0, 1, namedString);
                    }
                    ArrayList arrayList2 = arrayList;
                    if (pvh != null) {
                        arrayList2 = arrayList;
                        if (arrayList == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add(pvh);
                    }
                    typedArrayObtainAttributes.recycle();
                    arrayList = arrayList2;
                }
                xmlPullParser.next();
            }
        }
        PropertyValuesHolder[] propertyValuesHolderArr = null;
        if (arrayList != null) {
            int size = arrayList.size();
            PropertyValuesHolder[] propertyValuesHolderArr2 = new PropertyValuesHolder[size];
            while (true) {
                propertyValuesHolderArr = propertyValuesHolderArr2;
                if (i >= size) {
                    break;
                }
                propertyValuesHolderArr2[i] = (PropertyValuesHolder) arrayList.get(i);
                i++;
            }
        }
        return propertyValuesHolderArr;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator valueAnimator, TypedArray typedArray, TypedArray typedArray2, float f, XmlPullParser xmlPullParser) {
        long namedInt = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        long namedInt2 = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int namedInt3 = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        int i = namedInt3;
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            i = namedInt3;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int iInferValueTypeFromValues = namedInt3;
                if (namedInt3 == 4) {
                    iInferValueTypeFromValues = inferValueTypeFromValues(typedArray, 5, 6);
                }
                PropertyValuesHolder pvh = getPVH(typedArray, iInferValueTypeFromValues, 5, 6, BuildConfig.FLAVOR);
                i = iInferValueTypeFromValues;
                if (pvh != null) {
                    valueAnimator.setValues(pvh);
                    i = iInferValueTypeFromValues;
                }
            }
        }
        valueAnimator.setDuration(namedInt);
        valueAnimator.setStartDelay(namedInt2);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            setupObjectAnimator(valueAnimator, typedArray2, i, f, xmlPullParser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator valueAnimator, TypedArray typedArray, int i, float f, XmlPullParser xmlPullParser) {
        ObjectAnimator objectAnimator = (ObjectAnimator) valueAnimator;
        String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 1);
        if (namedString == null) {
            objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 0));
            return;
        }
        String namedString2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyXName", 2);
        String namedString3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyYName", 3);
        if (i != 2) {
        }
        if (namedString2 != null || namedString3 != null) {
            setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, f * 0.5f, namedString2, namedString3);
            return;
        }
        throw new InflateException(typedArray.getPositionDescription() + " propertyXName or propertyYName is needed for PathData");
    }

    private static void setupPathMotion(Path path, ObjectAnimator objectAnimator, float f, String str, String str2) {
        float length;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Float.valueOf(0.0f));
        float f2 = 0.0f;
        do {
            length = f2 + pathMeasure.getLength();
            arrayList.add(Float.valueOf(length));
            f2 = length;
        } while (pathMeasure.nextContour());
        PathMeasure pathMeasure2 = new PathMeasure(path, false);
        int iMin = Math.min(100, ((int) (length / f)) + 1);
        float[] fArr = new float[iMin];
        float[] fArr2 = new float[iMin];
        float[] fArr3 = new float[2];
        float f3 = length / (iMin - 1);
        int i = 0;
        int i2 = 0;
        float f4 = 0.0f;
        while (i < iMin) {
            pathMeasure2.getPosTan(f4 - ((Float) arrayList.get(i2)).floatValue(), fArr3, null);
            fArr[i] = fArr3[0];
            fArr2[i] = fArr3[1];
            f4 += f3;
            int i3 = i2 + 1;
            int i4 = i2;
            if (i3 < arrayList.size()) {
                i4 = i2;
                if (f4 > ((Float) arrayList.get(i3)).floatValue()) {
                    pathMeasure2.nextContour();
                    i4 = i3;
                }
            }
            i++;
            i2 = i4;
        }
        PropertyValuesHolder propertyValuesHolderOfFloat = str != null ? PropertyValuesHolder.ofFloat(str, fArr) : null;
        PropertyValuesHolder propertyValuesHolderOfFloat2 = null;
        if (str2 != null) {
            propertyValuesHolderOfFloat2 = PropertyValuesHolder.ofFloat(str2, fArr2);
        }
        if (propertyValuesHolderOfFloat == null) {
            objectAnimator.setValues(propertyValuesHolderOfFloat2);
        } else if (propertyValuesHolderOfFloat2 == null) {
            objectAnimator.setValues(propertyValuesHolderOfFloat);
        } else {
            objectAnimator.setValues(propertyValuesHolderOfFloat, propertyValuesHolderOfFloat2);
        }
    }
}
