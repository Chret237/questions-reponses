package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.transition.AnimatorUtils;
import android.support.transition.Transition;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:android/support/transition/Visibility.class */
public abstract class Visibility extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    private int mMode;
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String[] sTransitionProperties = {PROPNAME_VISIBILITY, PROPNAME_PARENT};

    /* loaded from: classes-dex2jar.jar:android/support/transition/Visibility$DisappearListener.class */
    private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final boolean mSuppressLayout;
        private final View mView;

        DisappearListener(View view, int i, boolean z) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            this.mView = view;
            this.mFinalVisibility = i;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = z;
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            if (!this.mCanceled) {
                ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
                ViewGroup viewGroup = this.mParent;
                if (viewGroup != null) {
                    viewGroup.invalidate();
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean z) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            ViewGroup viewGroup;
            if (!this.mSuppressLayout || this.mLayoutSuppressed == z || (viewGroup = this.mParent) == null) {
                return;
            }
            this.mLayoutSuppressed = z;
            ViewGroupUtils.suppressLayout(viewGroup, z);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.mCanceled = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            hideViewWhenNotCanceled();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, android.support.transition.AnimatorUtils.AnimatorPauseListenerCompat
        public void onAnimationPause(Animator animator) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
            if (this.mCanceled) {
                return;
            }
            ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener, android.support.transition.AnimatorUtils.AnimatorPauseListenerCompat
        public void onAnimationResume(Animator animator) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
            if (this.mCanceled) {
                return;
            }
            ViewUtils.setTransitionVisibility(this.mView, 0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // android.support.transition.Transition.TransitionListener
        public void onTransitionCancel(Transition transition) {
        }

        @Override // android.support.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        @Override // android.support.transition.Transition.TransitionListener
        public void onTransitionPause(Transition transition) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            suppressLayout(false);
        }

        @Override // android.support.transition.Transition.TransitionListener
        public void onTransitionResume(Transition transition) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            suppressLayout(true);
        }

        @Override // android.support.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/transition/Visibility$Mode.class */
    public @interface Mode {
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Visibility$VisibilityInfo.class */
    private static class VisibilityInfo {
        ViewGroup mEndParent;
        int mEndVisibility;
        boolean mFadeIn;
        ViewGroup mStartParent;
        int mStartVisibility;
        boolean mVisibilityChange;

        VisibilityInfo() {
        }
    }

    public Visibility() {
        this.mMode = 3;
    }

    public Visibility(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMode = 3;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.VISIBILITY_TRANSITION);
        int namedInt = TypedArrayUtils.getNamedInt(typedArrayObtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionVisibilityMode", 0, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (namedInt != 0) {
            setMode(namedInt);
        }
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_VISIBILITY, Integer.valueOf(transitionValues.view.getVisibility()));
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, iArr);
    }

    private VisibilityInfo getVisibilityChangeInfo(TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityInfo = new VisibilityInfo();
        visibilityInfo.mVisibilityChange = false;
        visibilityInfo.mFadeIn = false;
        if (transitionValues == null || !transitionValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mStartVisibility = -1;
            visibilityInfo.mStartParent = null;
        } else {
            visibilityInfo.mStartVisibility = ((Integer) transitionValues.values.get(PROPNAME_VISIBILITY)).intValue();
            visibilityInfo.mStartParent = (ViewGroup) transitionValues.values.get(PROPNAME_PARENT);
        }
        if (transitionValues2 == null || !transitionValues2.values.containsKey(PROPNAME_VISIBILITY)) {
            visibilityInfo.mEndVisibility = -1;
            visibilityInfo.mEndParent = null;
        } else {
            visibilityInfo.mEndVisibility = ((Integer) transitionValues2.values.get(PROPNAME_VISIBILITY)).intValue();
            visibilityInfo.mEndParent = (ViewGroup) transitionValues2.values.get(PROPNAME_PARENT);
        }
        if (transitionValues == null || transitionValues2 == null) {
            if (transitionValues == null && visibilityInfo.mEndVisibility == 0) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            } else if (transitionValues2 == null && visibilityInfo.mStartVisibility == 0) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            }
        } else {
            if (visibilityInfo.mStartVisibility == visibilityInfo.mEndVisibility && visibilityInfo.mStartParent == visibilityInfo.mEndParent) {
                return visibilityInfo;
            }
            if (visibilityInfo.mStartVisibility != visibilityInfo.mEndVisibility) {
                if (visibilityInfo.mStartVisibility == 0) {
                    visibilityInfo.mFadeIn = false;
                    visibilityInfo.mVisibilityChange = true;
                } else if (visibilityInfo.mEndVisibility == 0) {
                    visibilityInfo.mFadeIn = true;
                    visibilityInfo.mVisibilityChange = true;
                }
            } else if (visibilityInfo.mEndParent == null) {
                visibilityInfo.mFadeIn = false;
                visibilityInfo.mVisibilityChange = true;
            } else if (visibilityInfo.mStartParent == null) {
                visibilityInfo.mFadeIn = true;
                visibilityInfo.mVisibilityChange = true;
            }
        }
        return visibilityInfo;
    }

    @Override // android.support.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        VisibilityInfo visibilityChangeInfo = getVisibilityChangeInfo(transitionValues, transitionValues2);
        if (!visibilityChangeInfo.mVisibilityChange) {
            return null;
        }
        if (visibilityChangeInfo.mStartParent == null && visibilityChangeInfo.mEndParent == null) {
            return null;
        }
        return visibilityChangeInfo.mFadeIn ? onAppear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility) : onDisappear(viewGroup, transitionValues, visibilityChangeInfo.mStartVisibility, transitionValues2, visibilityChangeInfo.mEndVisibility);
    }

    public int getMode() {
        return this.mMode;
    }

    @Override // android.support.transition.Transition
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0052  */
    @Override // android.support.transition.Transition
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isTransitionRequired(android.support.transition.TransitionValues r5, android.support.transition.TransitionValues r6) {
        /*
            r4 = this;
            r0 = 0
            r8 = r0
            r0 = r5
            if (r0 != 0) goto Ld
            r0 = r6
            if (r0 != 0) goto Ld
            r0 = 0
            return r0
        Ld:
            r0 = r5
            if (r0 == 0) goto L30
            r0 = r6
            if (r0 == 0) goto L30
            r0 = r6
            java.util.Map<java.lang.String, java.lang.Object> r0 = r0.values
            java.lang.String r1 = "android:visibility:visibility"
            boolean r0 = r0.containsKey(r1)
            r1 = r5
            java.util.Map<java.lang.String, java.lang.Object> r1 = r1.values
            java.lang.String r2 = "android:visibility:visibility"
            boolean r1 = r1.containsKey(r2)
            if (r0 == r1) goto L30
            r0 = 0
            return r0
        L30:
            r0 = r4
            r1 = r5
            r2 = r6
            android.support.transition.Visibility$VisibilityInfo r0 = r0.getVisibilityChangeInfo(r1, r2)
            r5 = r0
            r0 = r8
            r7 = r0
            r0 = r5
            boolean r0 = r0.mVisibilityChange
            if (r0 == 0) goto L54
            r0 = r5
            int r0 = r0.mStartVisibility
            if (r0 == 0) goto L52
            r0 = r8
            r7 = r0
            r0 = r5
            int r0 = r0.mEndVisibility
            if (r0 != 0) goto L54
        L52:
            r0 = 1
            r7 = r0
        L54:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.Visibility.isTransitionRequired(android.support.transition.TransitionValues, android.support.transition.TransitionValues):boolean");
    }

    public boolean isVisible(TransitionValues transitionValues) {
        if (transitionValues == null) {
            return false;
        }
        int iIntValue = ((Integer) transitionValues.values.get(PROPNAME_VISIBILITY)).intValue();
        View view = (View) transitionValues.values.get(PROPNAME_PARENT);
        boolean z = false;
        if (iIntValue == 0) {
            z = false;
            if (view != null) {
                z = true;
            }
        }
        return z;
    }

    public Animator onAppear(ViewGroup viewGroup, TransitionValues transitionValues, int i, TransitionValues transitionValues2, int i2) {
        if ((this.mMode & 1) != 1 || transitionValues2 == null) {
            return null;
        }
        if (transitionValues == null) {
            View view = (View) transitionValues2.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(view, false), getTransitionValues(view, false)).mVisibilityChange) {
                return null;
            }
        }
        return onAppear(viewGroup, transitionValues2.view, transitionValues, transitionValues2);
    }

    public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0091  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.animation.Animator onDisappear(android.view.ViewGroup r8, android.support.transition.TransitionValues r9, int r10, android.support.transition.TransitionValues r11, int r12) throws java.lang.IllegalAccessException, java.lang.NoSuchFieldException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 479
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.Visibility.onDisappear(android.view.ViewGroup, android.support.transition.TransitionValues, int, android.support.transition.TransitionValues, int):android.animation.Animator");
    }

    public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public void setMode(int i) {
        if ((i & (-4)) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        this.mMode = i;
    }
}
