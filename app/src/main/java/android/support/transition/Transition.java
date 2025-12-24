package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import com.github.clans.fab.BuildConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/* loaded from: classes-dex2jar.jar:android/support/transition/Transition.class */
public abstract class Transition implements Cloneable {
    static final boolean DBG = false;
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private ArrayList<TransitionValues> mEndValuesList;
    private EpicenterCallback mEpicenterCallback;
    private ArrayMap<String, String> mNameOverrides;
    TransitionPropagation mPropagation;
    private ArrayList<TransitionValues> mStartValuesList;
    private static final int[] DEFAULT_MATCH_ORDER = {2, 1, 3, 4};
    private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion() { // from class: android.support.transition.Transition.1
        @Override // android.support.transition.PathMotion
        public Path getPath(float f, float f2, float f3, float f4) {
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f3, f4);
            return path;
        }
    };
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<>();
    private String mName = getClass().getName();
    private long mStartDelay = -1;
    long mDuration = -1;
    private TimeInterpolator mInterpolator = null;
    ArrayList<Integer> mTargetIds = new ArrayList<>();
    ArrayList<View> mTargets = new ArrayList<>();
    private ArrayList<String> mTargetNames = null;
    private ArrayList<Class> mTargetTypes = null;
    private ArrayList<Integer> mTargetIdExcludes = null;
    private ArrayList<View> mTargetExcludes = null;
    private ArrayList<Class> mTargetTypeExcludes = null;
    private ArrayList<String> mTargetNameExcludes = null;
    private ArrayList<Integer> mTargetIdChildExcludes = null;
    private ArrayList<View> mTargetChildExcludes = null;
    private ArrayList<Class> mTargetTypeChildExcludes = null;
    private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
    private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
    TransitionSet mParent = null;
    private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
    private ViewGroup mSceneRoot = null;
    boolean mCanRemoveViews = false;
    ArrayList<Animator> mCurrentAnimators = new ArrayList<>();
    private int mNumInstances = 0;
    private boolean mPaused = false;
    private boolean mEnded = false;
    private ArrayList<TransitionListener> mListeners = null;
    private ArrayList<Animator> mAnimators = new ArrayList<>();
    private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;

    /* loaded from: classes-dex2jar.jar:android/support/transition/Transition$AnimationInfo.class */
    private static class AnimationInfo {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;

        AnimationInfo(View view, String str, Transition transition, WindowIdImpl windowIdImpl, TransitionValues transitionValues) {
            this.mView = view;
            this.mName = str;
            this.mValues = transitionValues;
            this.mWindowId = windowIdImpl;
            this.mTransition = transition;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Transition$ArrayListManager.class */
    private static class ArrayListManager {
        private ArrayListManager() {
        }

        static <T> ArrayList<T> add(ArrayList<T> arrayList, T t) {
            ArrayList<T> arrayList2 = arrayList;
            if (arrayList == null) {
                arrayList2 = new ArrayList<>();
            }
            if (!arrayList2.contains(t)) {
                arrayList2.add(t);
            }
            return arrayList2;
        }

        static <T> ArrayList<T> remove(ArrayList<T> arrayList, T t) {
            ArrayList<T> arrayList2 = arrayList;
            if (arrayList != null) {
                arrayList.remove(t);
                arrayList2 = arrayList;
                if (arrayList.isEmpty()) {
                    arrayList2 = null;
                }
            }
            return arrayList2;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Transition$EpicenterCallback.class */
    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(Transition transition);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/transition/Transition$MatchOrder.class */
    public @interface MatchOrder {
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Transition$TransitionListener.class */
    public interface TransitionListener {
        void onTransitionCancel(Transition transition);

        void onTransitionEnd(Transition transition);

        void onTransitionPause(Transition transition);

        void onTransitionResume(Transition transition);

        void onTransitionStart(Transition transition);
    }

    public Transition() {
    }

    public Transition(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.TRANSITION);
        XmlResourceParser xmlResourceParser = (XmlResourceParser) attributeSet;
        long namedInt = TypedArrayUtils.getNamedInt(typedArrayObtainStyledAttributes, xmlResourceParser, "duration", 1, -1);
        if (namedInt >= 0) {
            setDuration(namedInt);
        }
        long namedInt2 = TypedArrayUtils.getNamedInt(typedArrayObtainStyledAttributes, xmlResourceParser, "startDelay", 2, -1);
        if (namedInt2 > 0) {
            setStartDelay(namedInt2);
        }
        int namedResourceId = TypedArrayUtils.getNamedResourceId(typedArrayObtainStyledAttributes, xmlResourceParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            setInterpolator(AnimationUtils.loadInterpolator(context, namedResourceId));
        }
        String namedString = TypedArrayUtils.getNamedString(typedArrayObtainStyledAttributes, xmlResourceParser, "matchOrder", 3);
        if (namedString != null) {
            setMatchOrder(parseMatchOrder(namedString));
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void addUnmatched(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        int i;
        int i2 = 0;
        while (true) {
            if (i2 >= arrayMap.size()) {
                break;
            }
            TransitionValues transitionValuesValueAt = arrayMap.valueAt(i2);
            if (isValidTarget(transitionValuesValueAt.view)) {
                this.mStartValuesList.add(transitionValuesValueAt);
                this.mEndValuesList.add(null);
            }
            i2++;
        }
        for (i = 0; i < arrayMap2.size(); i++) {
            TransitionValues transitionValuesValueAt2 = arrayMap2.valueAt(i);
            if (isValidTarget(transitionValuesValueAt2.view)) {
                this.mEndValuesList.add(transitionValuesValueAt2);
                this.mStartValuesList.add(null);
            }
        }
    }

    private static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, null);
            } else {
                transitionValuesMaps.mIdValues.put(id, view);
            }
        }
        String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName != null) {
            if (transitionValuesMaps.mNameValues.containsKey(transitionName)) {
                transitionValuesMaps.mNameValues.put(transitionName, null);
            } else {
                transitionValuesMaps.mNameValues.put(transitionName, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listView = (ListView) view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                if (transitionValuesMaps.mItemIdValues.indexOfKey(itemIdAtPosition) < 0) {
                    ViewCompat.setHasTransientState(view, true);
                    transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, view);
                    return;
                }
                View view2 = transitionValuesMaps.mItemIdValues.get(itemIdAtPosition);
                if (view2 != null) {
                    ViewCompat.setHasTransientState(view2, false);
                    transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, null);
                }
            }
        }
    }

    private static boolean alreadyContains(int[] iArr, int i) {
        int i2 = iArr[i];
        for (int i3 = 0; i3 < i; i3++) {
            if (iArr[i3] == i2) {
                return true;
            }
        }
        return false;
    }

    private void captureHierarchy(View view, boolean z) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        ArrayList<Integer> arrayList = this.mTargetIdExcludes;
        if (arrayList == null || !arrayList.contains(Integer.valueOf(id))) {
            ArrayList<View> arrayList2 = this.mTargetExcludes;
            if (arrayList2 == null || !arrayList2.contains(view)) {
                ArrayList<Class> arrayList3 = this.mTargetTypeExcludes;
                if (arrayList3 != null) {
                    int size = arrayList3.size();
                    for (int i = 0; i < size; i++) {
                        if (this.mTargetTypeExcludes.get(i).isInstance(view)) {
                            return;
                        }
                    }
                }
                if (view.getParent() instanceof ViewGroup) {
                    TransitionValues transitionValues = new TransitionValues();
                    transitionValues.view = view;
                    if (z) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.mTargetedTransitions.add(this);
                    capturePropagationValues(transitionValues);
                    if (z) {
                        addViewValues(this.mStartValues, view, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, view, transitionValues);
                    }
                }
                if (view instanceof ViewGroup) {
                    ArrayList<Integer> arrayList4 = this.mTargetIdChildExcludes;
                    if (arrayList4 == null || !arrayList4.contains(Integer.valueOf(id))) {
                        ArrayList<View> arrayList5 = this.mTargetChildExcludes;
                        if (arrayList5 == null || !arrayList5.contains(view)) {
                            ArrayList<Class> arrayList6 = this.mTargetTypeChildExcludes;
                            if (arrayList6 != null) {
                                int size2 = arrayList6.size();
                                for (int i2 = 0; i2 < size2; i2++) {
                                    if (this.mTargetTypeChildExcludes.get(i2).isInstance(view)) {
                                        return;
                                    }
                                }
                            }
                            ViewGroup viewGroup = (ViewGroup) view;
                            for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                                captureHierarchy(viewGroup.getChildAt(i3), z);
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int i, boolean z) {
        ArrayList<Integer> arrayListAdd = arrayList;
        if (i > 0) {
            arrayListAdd = z ? ArrayListManager.add(arrayList, Integer.valueOf(i)) : ArrayListManager.remove(arrayList, Integer.valueOf(i));
        }
        return arrayListAdd;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t, boolean z) {
        ArrayList<T> arrayListAdd = arrayList;
        if (t != null) {
            arrayListAdd = z ? ArrayListManager.add(arrayList, t) : ArrayListManager.remove(arrayList, t);
        }
        return arrayListAdd;
    }

    private ArrayList<Class> excludeType(ArrayList<Class> arrayList, Class cls, boolean z) {
        ArrayList<Class> arrayListAdd = arrayList;
        if (cls != null) {
            arrayListAdd = z ? ArrayListManager.add(arrayList, cls) : ArrayListManager.remove(arrayList, cls);
        }
        return arrayListAdd;
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View view, boolean z) {
        ArrayList<View> arrayListAdd = arrayList;
        if (view != null) {
            arrayListAdd = z ? ArrayListManager.add(arrayList, view) : ArrayListManager.remove(arrayList, view);
        }
        return arrayListAdd;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap = sRunningAnimators.get();
        ArrayMap<Animator, AnimationInfo> arrayMap2 = arrayMap;
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap<>();
            sRunningAnimators.set(arrayMap2);
        }
        return arrayMap2;
    }

    private static boolean isValidMatch(int i) {
        boolean z = true;
        if (i < 1 || i > 4) {
            z = false;
        }
        return z;
    }

    private static boolean isValueChanged(TransitionValues transitionValues, TransitionValues transitionValues2, String str) {
        boolean zEquals;
        Object obj = transitionValues.values.get(str);
        Object obj2 = transitionValues2.values.get(str);
        if (obj == null && obj2 == null) {
            zEquals = false;
        } else {
            zEquals = true;
            if (obj != null) {
                zEquals = obj2 == null ? true : true ^ obj.equals(obj2);
            }
        }
        return zEquals;
    }

    private void matchIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        View view;
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            View viewValueAt = sparseArray.valueAt(i);
            if (viewValueAt != null && isValidTarget(viewValueAt) && (view = sparseArray2.get(sparseArray.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(viewValueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(viewValueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchInstances(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        TransitionValues transitionValuesRemove;
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            View viewKeyAt = arrayMap.keyAt(size);
            if (viewKeyAt != null && isValidTarget(viewKeyAt) && (transitionValuesRemove = arrayMap2.remove(viewKeyAt)) != null && transitionValuesRemove.view != null && isValidTarget(transitionValuesRemove.view)) {
                this.mStartValuesList.add(arrayMap.removeAt(size));
                this.mEndValuesList.add(transitionValuesRemove);
            }
        }
    }

    private void matchItemIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, LongSparseArray<View> longSparseArray, LongSparseArray<View> longSparseArray2) {
        View view;
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            View viewValueAt = longSparseArray.valueAt(i);
            if (viewValueAt != null && isValidTarget(viewValueAt) && (view = longSparseArray2.get(longSparseArray.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(viewValueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(viewValueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchNames(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, ArrayMap<String, View> arrayMap3, ArrayMap<String, View> arrayMap4) {
        View view;
        int size = arrayMap3.size();
        for (int i = 0; i < size; i++) {
            View viewValueAt = arrayMap3.valueAt(i);
            if (viewValueAt != null && isValidTarget(viewValueAt) && (view = arrayMap4.get(arrayMap3.keyAt(i))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = arrayMap.get(viewValueAt);
                TransitionValues transitionValues2 = arrayMap2.get(view);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    arrayMap.remove(viewValueAt);
                    arrayMap2.remove(view);
                }
            }
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        ArrayMap<View, TransitionValues> arrayMap = new ArrayMap<>(transitionValuesMaps.mViewValues);
        ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap<>(transitionValuesMaps2.mViewValues);
        int i = 0;
        while (true) {
            int[] iArr = this.mMatchOrder;
            if (i >= iArr.length) {
                addUnmatched(arrayMap, arrayMap2);
                return;
            }
            int i2 = iArr[i];
            if (i2 == 1) {
                matchInstances(arrayMap, arrayMap2);
            } else if (i2 == 2) {
                matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
            } else if (i2 == 3) {
                matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
            } else if (i2 == 4) {
                matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
            }
            i++;
        }
    }

    private static int[] parseMatchOrder(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int[] iArr = new int[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String strTrim = stringTokenizer.nextToken().trim();
            if (MATCH_ID_STR.equalsIgnoreCase(strTrim)) {
                iArr[i] = 3;
            } else if (MATCH_INSTANCE_STR.equalsIgnoreCase(strTrim)) {
                iArr[i] = 1;
            } else if (MATCH_NAME_STR.equalsIgnoreCase(strTrim)) {
                iArr[i] = 2;
            } else if (MATCH_ITEM_ID_STR.equalsIgnoreCase(strTrim)) {
                iArr[i] = 4;
            } else {
                if (!strTrim.isEmpty()) {
                    throw new InflateException("Unknown match type in matchOrder: '" + strTrim + "'");
                }
                int[] iArr2 = new int[iArr.length - 1];
                System.arraycopy(iArr, 0, iArr2, 0, i);
                i--;
                iArr = iArr2;
            }
            i++;
        }
        return iArr;
    }

    private void runAnimator(Animator animator, ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator != null) {
            animator.addListener(new AnimatorListenerAdapter(this, arrayMap) { // from class: android.support.transition.Transition.2
                final Transition this$0;
                final ArrayMap val$runningAnimators;

                {
                    this.this$0 = this;
                    this.val$runningAnimators = arrayMap;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    this.val$runningAnimators.remove(animator2);
                    this.this$0.mCurrentAnimators.remove(animator2);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator2) {
                    this.this$0.mCurrentAnimators.add(animator2);
                }
            });
            animate(animator);
        }
    }

    public Transition addListener(TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    public Transition addTarget(int i) {
        if (i != 0) {
            this.mTargetIds.add(Integer.valueOf(i));
        }
        return this;
    }

    public Transition addTarget(View view) {
        this.mTargets.add(view);
        return this;
    }

    public Transition addTarget(Class cls) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList<>();
        }
        this.mTargetTypes.add(cls);
        return this;
    }

    public Transition addTarget(String str) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList<>();
        }
        this.mTargetNames.add(str);
        return this;
    }

    protected void animate(Animator animator) {
        if (animator == null) {
            end();
            return;
        }
        if (getDuration() >= 0) {
            animator.setDuration(getDuration());
        }
        if (getStartDelay() >= 0) {
            animator.setStartDelay(getStartDelay());
        }
        if (getInterpolator() != null) {
            animator.setInterpolator(getInterpolator());
        }
        animator.addListener(new AnimatorListenerAdapter(this) { // from class: android.support.transition.Transition.3
            final Transition this$0;

            {
                this.this$0 = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                this.this$0.end();
                animator2.removeListener(this);
            }
        });
        animator.start();
    }

    protected void cancel() {
        for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
            this.mCurrentAnimators.get(size).cancel();
        }
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
        int size2 = arrayList2.size();
        for (int i = 0; i < size2; i++) {
            ((TransitionListener) arrayList2.get(i)).onTransitionCancel(this);
        }
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    void capturePropagationValues(TransitionValues transitionValues) {
        String[] propagationProperties;
        boolean z;
        if (this.mPropagation == null || transitionValues.values.isEmpty() || (propagationProperties = this.mPropagation.getPropagationProperties()) == null) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= propagationProperties.length) {
                z = true;
                break;
            } else {
                if (!transitionValues.values.containsKey(propagationProperties[i])) {
                    z = false;
                    break;
                }
                i++;
            }
        }
        if (z) {
            return;
        }
        this.mPropagation.captureValues(transitionValues);
    }

    public abstract void captureStartValues(TransitionValues transitionValues);

    void captureValues(ViewGroup viewGroup, boolean z) {
        ArrayList<String> arrayList;
        ArrayList<Class> arrayList2;
        ArrayMap<String, String> arrayMap;
        int i;
        clearValues(z);
        if ((this.mTargetIds.size() > 0 || this.mTargets.size() > 0) && (((arrayList = this.mTargetNames) == null || arrayList.isEmpty()) && ((arrayList2 = this.mTargetTypes) == null || arrayList2.isEmpty()))) {
            for (int i2 = 0; i2 < this.mTargetIds.size(); i2++) {
                View viewFindViewById = viewGroup.findViewById(this.mTargetIds.get(i2).intValue());
                if (viewFindViewById != null) {
                    TransitionValues transitionValues = new TransitionValues();
                    transitionValues.view = viewFindViewById;
                    if (z) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.mTargetedTransitions.add(this);
                    capturePropagationValues(transitionValues);
                    if (z) {
                        addViewValues(this.mStartValues, viewFindViewById, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, viewFindViewById, transitionValues);
                    }
                }
            }
            for (int i3 = 0; i3 < this.mTargets.size(); i3++) {
                View view = this.mTargets.get(i3);
                TransitionValues transitionValues2 = new TransitionValues();
                transitionValues2.view = view;
                if (z) {
                    captureStartValues(transitionValues2);
                } else {
                    captureEndValues(transitionValues2);
                }
                transitionValues2.mTargetedTransitions.add(this);
                capturePropagationValues(transitionValues2);
                if (z) {
                    addViewValues(this.mStartValues, view, transitionValues2);
                } else {
                    addViewValues(this.mEndValues, view, transitionValues2);
                }
            }
        } else {
            captureHierarchy(viewGroup, z);
        }
        if (z || (arrayMap = this.mNameOverrides) == null) {
            return;
        }
        int size = arrayMap.size();
        ArrayList arrayList3 = new ArrayList(size);
        int i4 = 0;
        while (true) {
            if (i4 >= size) {
                break;
            }
            arrayList3.add(this.mStartValues.mNameValues.remove(this.mNameOverrides.keyAt(i4)));
            i4++;
        }
        for (i = 0; i < size; i++) {
            View view2 = (View) arrayList3.get(i);
            if (view2 != null) {
                this.mStartValues.mNameValues.put(this.mNameOverrides.valueAt(i), view2);
            }
        }
    }

    void clearValues(boolean z) {
        if (z) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
        } else {
            this.mEndValues.mViewValues.clear();
            this.mEndValues.mIdValues.clear();
            this.mEndValues.mItemIdValues.clear();
        }
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Transition mo119clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.mAnimators = new ArrayList<>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void createAnimators(android.view.ViewGroup r11, android.support.transition.TransitionValuesMaps r12, android.support.transition.TransitionValuesMaps r13, java.util.ArrayList<android.support.transition.TransitionValues> r14, java.util.ArrayList<android.support.transition.TransitionValues> r15) {
        /*
            Method dump skipped, instructions count: 620
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.Transition.createAnimators(android.view.ViewGroup, android.support.transition.TransitionValuesMaps, android.support.transition.TransitionValuesMaps, java.util.ArrayList, java.util.ArrayList):void");
    }

    protected void end() {
        int i = this.mNumInstances - 1;
        this.mNumInstances = i;
        if (i == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((TransitionListener) arrayList2.get(i2)).onTransitionEnd(this);
                }
            }
            for (int i3 = 0; i3 < this.mStartValues.mItemIdValues.size(); i3++) {
                View viewValueAt = this.mStartValues.mItemIdValues.valueAt(i3);
                if (viewValueAt != null) {
                    ViewCompat.setHasTransientState(viewValueAt, false);
                }
            }
            for (int i4 = 0; i4 < this.mEndValues.mItemIdValues.size(); i4++) {
                View viewValueAt2 = this.mEndValues.mItemIdValues.valueAt(i4);
                if (viewValueAt2 != null) {
                    ViewCompat.setHasTransientState(viewValueAt2, false);
                }
            }
            this.mEnded = true;
        }
    }

    public Transition excludeChildren(int i, boolean z) {
        this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, i, z);
        return this;
    }

    public Transition excludeChildren(View view, boolean z) {
        this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, view, z);
        return this;
    }

    public Transition excludeChildren(Class cls, boolean z) {
        this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, cls, z);
        return this;
    }

    public Transition excludeTarget(int i, boolean z) {
        this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, i, z);
        return this;
    }

    public Transition excludeTarget(View view, boolean z) {
        this.mTargetExcludes = excludeView(this.mTargetExcludes, view, z);
        return this;
    }

    public Transition excludeTarget(Class cls, boolean z) {
        this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, cls, z);
        return this;
    }

    public Transition excludeTarget(String str, boolean z) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, str, z);
        return this;
    }

    void forceToEnd(ViewGroup viewGroup) {
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        if (viewGroup == null) {
            return;
        }
        WindowIdImpl windowId = ViewUtils.getWindowId(viewGroup);
        while (true) {
            size--;
            if (size < 0) {
                return;
            }
            AnimationInfo animationInfoValueAt = runningAnimators.valueAt(size);
            if (animationInfoValueAt.mView != null && windowId != null && windowId.equals(animationInfoValueAt.mWindowId)) {
                runningAnimators.keyAt(size).end();
            }
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    TransitionValues getMatchedTransitionValues(View view, boolean z) {
        int i;
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getMatchedTransitionValues(view, z);
        }
        ArrayList<TransitionValues> arrayList = z ? this.mStartValuesList : this.mEndValuesList;
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i2 = 0;
        while (true) {
            i = -1;
            if (i2 >= size) {
                break;
            }
            TransitionValues transitionValues = arrayList.get(i2);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == view) {
                i = i2;
                break;
            }
            i2++;
        }
        TransitionValues transitionValues2 = null;
        if (i >= 0) {
            transitionValues2 = (z ? this.mEndValuesList : this.mStartValuesList).get(i);
        }
        return transitionValues2;
    }

    public String getName() {
        return this.mName;
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<Class> getTargetTypes() {
        return this.mTargetTypes;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public TransitionValues getTransitionValues(View view, boolean z) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getTransitionValues(view, z);
        }
        return (z ? this.mStartValues : this.mEndValues).mViewValues.get(view);
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        boolean z = false;
        if (transitionValues != null) {
            z = false;
            if (transitionValues2 != null) {
                String[] transitionProperties = getTransitionProperties();
                if (transitionProperties != null) {
                    int length = transitionProperties.length;
                    int i = 0;
                    while (true) {
                        z = false;
                        if (i >= length) {
                            break;
                        }
                        if (isValueChanged(transitionValues, transitionValues2, transitionProperties[i])) {
                            break;
                        }
                        i++;
                    }
                    z = true;
                } else {
                    Iterator<String> it = transitionValues.values.keySet().iterator();
                    do {
                        z = false;
                        if (!it.hasNext()) {
                            break;
                        }
                    } while (!isValueChanged(transitionValues, transitionValues2, it.next()));
                    z = true;
                }
            }
        }
        return z;
    }

    boolean isValidTarget(View view) {
        ArrayList<Class> arrayList;
        ArrayList<String> arrayList2;
        int id = view.getId();
        ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
        if (arrayList3 != null && arrayList3.contains(Integer.valueOf(id))) {
            return false;
        }
        ArrayList<View> arrayList4 = this.mTargetExcludes;
        if (arrayList4 != null && arrayList4.contains(view)) {
            return false;
        }
        ArrayList<Class> arrayList5 = this.mTargetTypeExcludes;
        if (arrayList5 != null) {
            int size = arrayList5.size();
            for (int i = 0; i < size; i++) {
                if (this.mTargetTypeExcludes.get(i).isInstance(view)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(view) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(view))) {
            return false;
        }
        if ((this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && (((arrayList = this.mTargetTypes) == null || arrayList.isEmpty()) && ((arrayList2 = this.mTargetNames) == null || arrayList2.isEmpty()))) || this.mTargetIds.contains(Integer.valueOf(id)) || this.mTargets.contains(view)) {
            return true;
        }
        ArrayList<String> arrayList6 = this.mTargetNames;
        if (arrayList6 != null && arrayList6.contains(ViewCompat.getTransitionName(view))) {
            return true;
        }
        if (this.mTargetTypes == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.mTargetTypes.size(); i2++) {
            if (this.mTargetTypes.get(i2).isInstance(view)) {
                return true;
            }
        }
        return false;
    }

    public void pause(View view) {
        if (this.mEnded) {
            return;
        }
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        WindowIdImpl windowId = ViewUtils.getWindowId(view);
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            AnimationInfo animationInfoValueAt = runningAnimators.valueAt(size);
            if (animationInfoValueAt.mView != null && windowId.equals(animationInfoValueAt.mWindowId)) {
                AnimatorUtils.pause(runningAnimators.keyAt(size));
            }
        }
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
            int size2 = arrayList2.size();
            for (int i = 0; i < size2; i++) {
                ((TransitionListener) arrayList2.get(i)).onTransitionPause(this);
            }
        }
        this.mPaused = true;
    }

    void playTransition(ViewGroup viewGroup) {
        AnimationInfo animationInfo;
        this.mStartValuesList = new ArrayList<>();
        this.mEndValuesList = new ArrayList<>();
        matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        WindowIdImpl windowId = ViewUtils.getWindowId(viewGroup);
        while (true) {
            size--;
            if (size < 0) {
                createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
                runAnimators();
                return;
            }
            Animator animatorKeyAt = runningAnimators.keyAt(size);
            if (animatorKeyAt != null && (animationInfo = runningAnimators.get(animatorKeyAt)) != null && animationInfo.mView != null && windowId.equals(animationInfo.mWindowId)) {
                TransitionValues transitionValues = animationInfo.mValues;
                View view = animationInfo.mView;
                TransitionValues transitionValues2 = getTransitionValues(view, true);
                TransitionValues matchedTransitionValues = getMatchedTransitionValues(view, true);
                if (!(transitionValues2 == null && matchedTransitionValues == null) && animationInfo.mTransition.isTransitionRequired(transitionValues, matchedTransitionValues)) {
                    if (animatorKeyAt.isRunning() || animatorKeyAt.isStarted()) {
                        animatorKeyAt.cancel();
                    } else {
                        runningAnimators.remove(animatorKeyAt);
                    }
                }
            }
        }
    }

    public Transition removeListener(TransitionListener transitionListener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(int i) {
        if (i != 0) {
            this.mTargetIds.remove(Integer.valueOf(i));
        }
        return this;
    }

    public Transition removeTarget(View view) {
        this.mTargets.remove(view);
        return this;
    }

    public Transition removeTarget(Class cls) {
        ArrayList<Class> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(cls);
        }
        return this;
    }

    public Transition removeTarget(String str) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(str);
        }
        return this;
    }

    public void resume(View view) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
                int size = runningAnimators.size();
                WindowIdImpl windowId = ViewUtils.getWindowId(view);
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    AnimationInfo animationInfoValueAt = runningAnimators.valueAt(size);
                    if (animationInfoValueAt.mView != null && windowId.equals(animationInfoValueAt.mWindowId)) {
                        AnimatorUtils.resume(runningAnimators.keyAt(size));
                    }
                }
                ArrayList<TransitionListener> arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                    int size2 = arrayList2.size();
                    for (int i = 0; i < size2; i++) {
                        ((TransitionListener) arrayList2.get(i)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    protected void runAnimators() {
        start();
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        Iterator<Animator> it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator next = it.next();
            if (runningAnimators.containsKey(next)) {
                start();
                runAnimator(next, runningAnimators);
            }
        }
        this.mAnimators.clear();
        end();
    }

    void setCanRemoveViews(boolean z) {
        this.mCanRemoveViews = z;
    }

    public Transition setDuration(long j) {
        this.mDuration = j;
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public void setMatchOrder(int... iArr) {
        if (iArr == null || iArr.length == 0) {
            this.mMatchOrder = DEFAULT_MATCH_ORDER;
            return;
        }
        for (int i = 0; i < iArr.length; i++) {
            if (!isValidMatch(iArr[i])) {
                throw new IllegalArgumentException("matches contains invalid value");
            }
            if (alreadyContains(iArr, i)) {
                throw new IllegalArgumentException("matches contains a duplicate value");
            }
        }
        this.mMatchOrder = (int[]) iArr.clone();
    }

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
        } else {
            this.mPathMotion = pathMotion;
        }
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    Transition setSceneRoot(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
        return this;
    }

    public Transition setStartDelay(long j) {
        this.mStartDelay = j;
        return this;
    }

    protected void start() {
        if (this.mNumInstances == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i = 0; i < size; i++) {
                    ((TransitionListener) arrayList2.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    public String toString() {
        return toString(BuildConfig.FLAVOR);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x010d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.String toString(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 551
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.Transition.toString(java.lang.String):java.lang.String");
    }
}
