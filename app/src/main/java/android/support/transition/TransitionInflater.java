package android.support.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes-dex2jar.jar:android/support/transition/TransitionInflater.class */
public class TransitionInflater {
    private final Context mContext;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    private static final ArrayMap<String, Constructor> CONSTRUCTORS = new ArrayMap<>();

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    private Object createCustom(AttributeSet attributeSet, Class cls, String str) {
        Object objNewInstance;
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        if (attributeValue == null) {
            throw new InflateException(str + " tag must have a 'class' attribute");
        }
        try {
            synchronized (CONSTRUCTORS) {
                Constructor constructor = CONSTRUCTORS.get(attributeValue);
                Constructor constructor2 = constructor;
                if (constructor == null) {
                    Class<? extends U> clsAsSubclass = this.mContext.getClassLoader().loadClass(attributeValue).asSubclass(cls);
                    constructor2 = constructor;
                    if (clsAsSubclass != 0) {
                        constructor2 = clsAsSubclass.getConstructor(CONSTRUCTOR_SIGNATURE);
                        constructor2.setAccessible(true);
                        CONSTRUCTORS.put(attributeValue, constructor2);
                    }
                }
                objNewInstance = constructor2.newInstance(this.mContext, attributeSet);
            }
            return objNewInstance;
        } catch (Exception e) {
            throw new InflateException("Could not instantiate " + cls + " class " + attributeValue, e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:89:0x0281, code lost:
    
        return r14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.support.transition.Transition createTransitionFromXml(org.xmlpull.v1.XmlPullParser r7, android.util.AttributeSet r8, android.support.transition.Transition r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 642
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.TransitionInflater.createTransitionFromXml(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.support.transition.Transition):android.support.transition.Transition");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0094, code lost:
    
        return r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.support.transition.TransitionManager createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser r7, android.util.AttributeSet r8, android.view.ViewGroup r9) throws org.xmlpull.v1.XmlPullParserException, android.content.res.Resources.NotFoundException, java.io.IOException {
        /*
            r6 = this;
            r0 = r7
            int r0 = r0.getDepth()
            r10 = r0
            r0 = 0
            r12 = r0
        Lb:
            r0 = r7
            int r0 = r0.next()
            r11 = r0
            r0 = r11
            r1 = 3
            if (r0 != r1) goto L24
            r0 = r7
            int r0 = r0.getDepth()
            r1 = r10
            if (r0 <= r1) goto L92
        L24:
            r0 = r11
            r1 = 1
            if (r0 == r1) goto L92
            r0 = r11
            r1 = 2
            if (r0 == r1) goto L33
            goto Lb
        L33:
            r0 = r7
            java.lang.String r0 = r0.getName()
            r13 = r0
            r0 = r13
            java.lang.String r1 = "transitionManager"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L51
            android.support.transition.TransitionManager r0 = new android.support.transition.TransitionManager
            r1 = r0
            r1.<init>()
            r12 = r0
            goto Lb
        L51:
            r0 = r13
            java.lang.String r1 = "transition"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6c
            r0 = r12
            if (r0 == 0) goto L6c
            r0 = r6
            r1 = r8
            r2 = r7
            r3 = r9
            r4 = r12
            r0.loadTransition(r1, r2, r3, r4)
            goto Lb
        L6c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r8 = r0
            r0 = r8
            java.lang.String r1 = "Unknown scene name: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r8
            r1 = r7
            java.lang.String r1 = r1.getName()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            r2 = r8
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L92:
            r0 = r12
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.TransitionInflater.createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.view.ViewGroup):android.support.transition.TransitionManager");
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    private void getTargetIds(XmlPullParser xmlPullParser, AttributeSet attributeSet, Transition transition) throws XmlPullParserException, IOException {
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return;
            }
            if (next == 2) {
                if (!xmlPullParser.getName().equals("target")) {
                    throw new RuntimeException("Unknown scene name: " + xmlPullParser.getName());
                }
                TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, Styleable.TRANSITION_TARGET);
                int namedResourceId = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlPullParser, "targetId", 1, 0);
                if (namedResourceId != 0) {
                    transition.addTarget(namedResourceId);
                } else {
                    int namedResourceId2 = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlPullParser, "excludeId", 2, 0);
                    if (namedResourceId2 != 0) {
                        transition.excludeTarget(namedResourceId2, true);
                    } else {
                        String namedString = TypedArrayUtils.getNamedString(typedArrayObtainStyledAttributes, xmlPullParser, "targetName", 4);
                        if (namedString != null) {
                            transition.addTarget(namedString);
                        } else {
                            String namedString2 = TypedArrayUtils.getNamedString(typedArrayObtainStyledAttributes, xmlPullParser, "excludeName", 5);
                            if (namedString2 != null) {
                                transition.excludeTarget(namedString2, true);
                            } else {
                                String namedString3 = TypedArrayUtils.getNamedString(typedArrayObtainStyledAttributes, xmlPullParser, "excludeClass", 3);
                                if (namedString3 != null) {
                                    try {
                                        transition.excludeTarget((Class) Class.forName(namedString3), true);
                                    } catch (ClassNotFoundException e) {
                                        typedArrayObtainStyledAttributes.recycle();
                                        throw new RuntimeException("Could not create " + namedString3, e);
                                    }
                                } else {
                                    String namedString4 = TypedArrayUtils.getNamedString(typedArrayObtainStyledAttributes, xmlPullParser, "targetClass", 0);
                                    if (namedString4 != null) {
                                        transition.addTarget(Class.forName(namedString4));
                                    }
                                }
                            }
                        }
                    }
                }
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    private void loadTransition(AttributeSet attributeSet, XmlPullParser xmlPullParser, ViewGroup viewGroup, TransitionManager transitionManager) throws Resources.NotFoundException {
        Transition transitionInflateTransition;
        TypedArray typedArrayObtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, Styleable.TRANSITION_MANAGER);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlPullParser, "transition", 2, -1);
        int namedResourceId2 = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlPullParser, "fromScene", 0, -1);
        Scene sceneForLayout = namedResourceId2 < 0 ? null : Scene.getSceneForLayout(viewGroup, namedResourceId2, this.mContext);
        int namedResourceId3 = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlPullParser, "toScene", 1, -1);
        Scene sceneForLayout2 = namedResourceId3 < 0 ? null : Scene.getSceneForLayout(viewGroup, namedResourceId3, this.mContext);
        if (namedResourceId >= 0 && (transitionInflateTransition = inflateTransition(namedResourceId)) != null) {
            if (sceneForLayout2 == null) {
                throw new RuntimeException("No toScene for transition ID " + namedResourceId);
            }
            if (sceneForLayout == null) {
                transitionManager.setTransition(sceneForLayout2, transitionInflateTransition);
            } else {
                transitionManager.setTransition(sceneForLayout, sceneForLayout2, transitionInflateTransition);
            }
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    public Transition inflateTransition(int i) throws Resources.NotFoundException {
        XmlResourceParser xml = this.mContext.getResources().getXml(i);
        try {
            try {
                Transition transitionCreateTransitionFromXml = createTransitionFromXml(xml, Xml.asAttributeSet(xml), null);
                xml.close();
                return transitionCreateTransitionFromXml;
            } catch (IOException e) {
                throw new InflateException(xml.getPositionDescription() + ": " + e.getMessage(), e);
            } catch (XmlPullParserException e2) {
                throw new InflateException(e2.getMessage(), e2);
            }
        } catch (Throwable th) {
            xml.close();
            throw th;
        }
    }

    public TransitionManager inflateTransitionManager(int i, ViewGroup viewGroup) throws Resources.NotFoundException {
        XmlResourceParser xml = this.mContext.getResources().getXml(i);
        try {
            try {
                TransitionManager transitionManagerCreateTransitionManagerFromXml = createTransitionManagerFromXml(xml, Xml.asAttributeSet(xml), viewGroup);
                xml.close();
                return transitionManagerCreateTransitionManagerFromXml;
            } catch (IOException e) {
                InflateException inflateException = new InflateException(xml.getPositionDescription() + ": " + e.getMessage());
                inflateException.initCause(e);
                throw inflateException;
            } catch (XmlPullParserException e2) {
                InflateException inflateException2 = new InflateException(e2.getMessage());
                inflateException2.initCause(e2);
                throw inflateException2;
            }
        } catch (Throwable th) {
            xml.close();
            throw th;
        }
    }
}
