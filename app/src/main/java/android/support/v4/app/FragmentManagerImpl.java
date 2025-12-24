package android.support.v4.app;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArraySet;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl.class */
final class FragmentManagerImpl extends FragmentManager implements LayoutInflater.Factory2 {
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    boolean mDestroyed;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    boolean mStateSaved;
    boolean mStopped;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    int mNextFragmentIndex = 0;
    final ArrayList<Fragment> mAdded = new ArrayList<>();
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
    int mCurState = 0;
    Bundle mStateBundle = null;
    SparseArray<Parcelable> mStateArray = null;
    Runnable mExecCommit = new Runnable(this) { // from class: android.support.v4.app.FragmentManagerImpl.1
        final FragmentManagerImpl this$0;

        {
            this.this$0 = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.this$0.execPendingActions();
        }
    };

    /* renamed from: android.support.v4.app.FragmentManagerImpl$2 */
    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$2.class */
    class C01792 extends AnimationListenerWrapper {
        final FragmentManagerImpl this$0;
        final ViewGroup val$container;
        final Fragment val$fragment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01792(FragmentManagerImpl fragmentManagerImpl, Animation.AnimationListener animationListener, ViewGroup viewGroup, Fragment fragment) {
            super(animationListener);
            this.this$0 = fragmentManagerImpl;
            this.val$container = viewGroup;
            this.val$fragment = fragment;
        }

        @Override // android.support.v4.app.FragmentManagerImpl.AnimationListenerWrapper, android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            this.val$container.post(new Runnable(this) { // from class: android.support.v4.app.FragmentManagerImpl.2.1
                final C01792 this$1;

                {
                    this.this$1 = this;
                }

                @Override // java.lang.Runnable
                public void run() throws NoSuchFieldException, Resources.NotFoundException {
                    if (this.this$1.val$fragment.getAnimatingAway() != null) {
                        this.this$1.val$fragment.setAnimatingAway(null);
                        this.this$1.this$0.moveToState(this.this$1.val$fragment, this.this$1.val$fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$AnimateOnHWLayerIfNeededListener.class */
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View view, Animation.AnimationListener animationListener) {
            super(animationListener);
            this.mView = view;
        }

        @Override // android.support.v4.app.FragmentManagerImpl.AnimationListenerWrapper, android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            if (ViewCompat.isAttachedToWindow(this.mView) || Build.VERSION.SDK_INT >= 24) {
                this.mView.post(new Runnable(this) { // from class: android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.1
                    final AnimateOnHWLayerIfNeededListener this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this.this$0.mView.setLayerType(0, null);
                    }
                });
            } else {
                this.mView.setLayerType(0, null);
            }
            super.onAnimationEnd(animation);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$AnimationListenerWrapper.class */
    private static class AnimationListenerWrapper implements Animation.AnimationListener {
        private final Animation.AnimationListener mWrapped;

        AnimationListenerWrapper(Animation.AnimationListener animationListener) {
            this.mWrapped = animationListener;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationEnd(animation);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationRepeat(animation);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            Animation.AnimationListener animationListener = this.mWrapped;
            if (animationListener != null) {
                animationListener.onAnimationStart(animation);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$AnimationOrAnimator.class */
    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator) {
            this.animation = null;
            this.animator = animator;
            if (animator == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }

        AnimationOrAnimator(Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$AnimatorOnHWLayerIfNeededListener.class */
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View view) {
            this.mView = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.mView.setLayerType(0, null);
            animator.removeListener(this);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.mView.setLayerType(2, null);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$EndViewTransitionAnimator.class */
    private static class EndViewTransitionAnimator extends AnimationSet implements Runnable {
        private boolean mAnimating;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimator(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.mAnimating = true;
            this.mParent = viewGroup;
            this.mChild = view;
            addAnimation(animation);
            this.mParent.post(this);
        }

        @Override // android.view.animation.AnimationSet, android.view.animation.Animation
        public boolean getTransformation(long j, Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (super.getTransformation(j, transformation)) {
                return true;
            }
            this.mEnded = true;
            OneShotPreDrawListener.add(this.mParent, this);
            return true;
        }

        @Override // android.view.animation.Animation
        public boolean getTransformation(long j, Transformation transformation, float f) {
            this.mAnimating = true;
            if (this.mEnded) {
                return !this.mTransitionEnded;
            }
            if (super.getTransformation(j, transformation, f)) {
                return true;
            }
            this.mEnded = true;
            OneShotPreDrawListener.add(this.mParent, this);
            return true;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mEnded || !this.mAnimating) {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
            } else {
                this.mAnimating = false;
                this.mParent.post(this);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$FragmentLifecycleCallbacksHolder.class */
    private static final class FragmentLifecycleCallbacksHolder {
        final FragmentManager.FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z) {
            this.mCallback = fragmentLifecycleCallbacks;
            this.mRecursive = z;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$FragmentTag.class */
    static class FragmentTag {
        public static final int[] Fragment = {R.attr.name, R.attr.id, R.attr.tag};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        private FragmentTag() {
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$OpGenerator.class */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$PopBackStackState.class */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;
        final FragmentManagerImpl this$0;

        PopBackStackState(FragmentManagerImpl fragmentManagerImpl, String str, int i, int i2) {
            this.this$0 = fragmentManagerImpl;
            this.mName = str;
            this.mId = i;
            this.mFlags = i2;
        }

        @Override // android.support.v4.app.FragmentManagerImpl.OpGenerator
        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            FragmentManager fragmentManagerPeekChildFragmentManager;
            if (this.this$0.mPrimaryNav == null || this.mId >= 0 || this.mName != null || (fragmentManagerPeekChildFragmentManager = this.this$0.mPrimaryNav.peekChildFragmentManager()) == null || !fragmentManagerPeekChildFragmentManager.popBackStackImmediate()) {
                return this.this$0.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
            }
            return false;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentManagerImpl$StartEnterTransitionListener.class */
    static class StartEnterTransitionListener implements Fragment.OnStartEnterTransitionListener {
        final boolean mIsBack;
        private int mNumPostponed;
        final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean z) {
            this.mIsBack = z;
            this.mRecord = backStackRecord;
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }

        public void completeTransaction() {
            boolean z = this.mNumPostponed > 0;
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            int size = fragmentManagerImpl.mAdded.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragmentManagerImpl.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (z && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, !z, true);
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        @Override // android.support.v4.app.Fragment.OnStartEnterTransitionListener
        public void onStartEnterTransition() {
            int i = this.mNumPostponed - 1;
            this.mNumPostponed = i;
            if (i != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }

        @Override // android.support.v4.app.Fragment.OnStartEnterTransitionListener
        public void startListening() {
            this.mNumPostponed++;
        }
    }

    FragmentManagerImpl() {
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) throws NoSuchFieldException, Resources.NotFoundException {
        int i = this.mCurState;
        if (i < 1) {
            return;
        }
        int iMin = Math.min(i, 3);
        int size = this.mAdded.size();
        for (int i2 = 0; i2 < size; i2++) {
            Fragment fragment = this.mAdded.get(i2);
            if (fragment.mState < iMin) {
                moveToState(fragment, iMin, fragment.getNextAnim(), fragment.getNextTransition(), false);
                if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                    arraySet.add(fragment);
                }
            }
        }
    }

    private void animateRemoveFragment(Fragment fragment, AnimationOrAnimator animationOrAnimator, int i) throws NoSuchFieldException {
        View view = fragment.mView;
        ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        fragment.setStateAfterAnimating(i);
        if (animationOrAnimator.animation != null) {
            EndViewTransitionAnimator endViewTransitionAnimator = new EndViewTransitionAnimator(animationOrAnimator.animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            endViewTransitionAnimator.setAnimationListener(new C01792(this, getAnimationListener(endViewTransitionAnimator), viewGroup, fragment));
            setHWLayerAnimListenerIfAlpha(view, animationOrAnimator);
            fragment.mView.startAnimation(endViewTransitionAnimator);
            return;
        }
        Animator animator = animationOrAnimator.animator;
        fragment.setAnimator(animationOrAnimator.animator);
        animator.addListener(new AnimatorListenerAdapter(this, viewGroup, view, fragment) { // from class: android.support.v4.app.FragmentManagerImpl.3
            final FragmentManagerImpl this$0;
            final ViewGroup val$container;
            final Fragment val$fragment;
            final View val$viewToAnimate;

            {
                this.this$0 = this;
                this.val$container = viewGroup;
                this.val$viewToAnimate = view;
                this.val$fragment = fragment;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) throws NoSuchFieldException, Resources.NotFoundException {
                this.val$container.endViewTransition(this.val$viewToAnimate);
                Animator animator3 = this.val$fragment.getAnimator();
                this.val$fragment.setAnimator(null);
                if (animator3 == null || this.val$container.indexOfChild(this.val$viewToAnimate) >= 0) {
                    return;
                }
                FragmentManagerImpl fragmentManagerImpl = this.this$0;
                Fragment fragment2 = this.val$fragment;
                fragmentManagerImpl.moveToState(fragment2, fragment2.getStateAfterAnimating(), 0, 0, false);
            }
        });
        animator.setTarget(fragment.mView);
        setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animator.start();
    }

    private void burpActive() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray != null) {
            for (int size = sparseArray.size() - 1; size >= 0; size--) {
                if (this.mActive.valueAt(size) == null) {
                    SparseArray<Fragment> sparseArray2 = this.mActive;
                    sparseArray2.delete(sparseArray2.keyAt(size));
                }
            }
        }
    }

    private void checkStateLoss() {
        if (isStateSaved()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause == null) {
            return;
        }
        throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void dispatchStateChange(int i) {
        try {
            this.mExecutingActions = true;
            moveToState(i, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    private void endAnimatingAwayFragments() throws NoSuchFieldException, Resources.NotFoundException {
        SparseArray<Fragment> sparseArray = this.mActive;
        int size = sparseArray == null ? 0 : sparseArray.size();
        for (int i = 0; i < size; i++) {
            Fragment fragmentValueAt = this.mActive.valueAt(i);
            if (fragmentValueAt != null) {
                if (fragmentValueAt.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragmentValueAt.getStateAfterAnimating();
                    View animatingAway = fragmentValueAt.getAnimatingAway();
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    fragmentValueAt.setAnimatingAway(null);
                    moveToState(fragmentValueAt, stateAfterAnimating, 0, 0, false);
                } else if (fragmentValueAt.getAnimator() != null) {
                    fragmentValueAt.getAnimator().end();
                }
            }
        }
    }

    private void ensureExecReady(boolean z) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment host has been destroyed");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!z) {
            checkStateLoss();
        }
        if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<>();
            this.mTmpIsPop = new ArrayList<>();
        }
        this.mExecutingActions = true;
        try {
            executePostponedTransaction(null, null);
        } finally {
            this.mExecutingActions = false;
        }
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2) {
        while (i < i2) {
            BackStackRecord backStackRecord = arrayList.get(i);
            boolean z = true;
            if (arrayList2.get(i).booleanValue()) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != i2 - 1) {
                    z = false;
                }
                backStackRecord.executePopOps(z);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            i++;
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2) throws NoSuchFieldException, Resources.NotFoundException {
        int iPostponePostponableTransactions;
        boolean z = arrayList.get(i).mReorderingAllowed;
        ArrayList<Fragment> arrayList3 = this.mTmpAddedFragments;
        if (arrayList3 == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            arrayList3.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment primaryNavigationFragment = getPrimaryNavigationFragment();
        boolean z2 = false;
        for (int i3 = i; i3 < i2; i3++) {
            BackStackRecord backStackRecord = arrayList.get(i3);
            primaryNavigationFragment = !arrayList2.get(i3).booleanValue() ? backStackRecord.expandOps(this.mTmpAddedFragments, primaryNavigationFragment) : backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, primaryNavigationFragment);
            z2 = z2 || backStackRecord.mAddToBackStack;
        }
        this.mTmpAddedFragments.clear();
        if (!z) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, i, i2, false);
        }
        executeOps(arrayList, arrayList2, i, i2);
        if (z) {
            ArraySet<Fragment> arraySet = new ArraySet<>();
            addAddedFragments(arraySet);
            iPostponePostponableTransactions = postponePostponableTransactions(arrayList, arrayList2, i, i2, arraySet);
            makeRemovedFragmentsInvisible(arraySet);
        } else {
            iPostponePostponableTransactions = i2;
        }
        int i4 = i;
        if (iPostponePostponableTransactions != i) {
            i4 = i;
            if (z) {
                FragmentTransition.startTransitions(this, arrayList, arrayList2, i, iPostponePostponableTransactions, true);
                moveToState(this.mCurState, true);
                i4 = i;
            }
        }
        while (i4 < i2) {
            BackStackRecord backStackRecord2 = arrayList.get(i4);
            if (arrayList2.get(i4).booleanValue() && backStackRecord2.mIndex >= 0) {
                freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            i4++;
        }
        if (z2) {
            reportBackStackChanged();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0093  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void executePostponedTransaction(java.util.ArrayList<android.support.v4.app.BackStackRecord> r6, java.util.ArrayList<java.lang.Boolean> r7) {
        /*
            Method dump skipped, instructions count: 234
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.executePostponedTransaction(java.util.ArrayList, java.util.ArrayList):void");
    }

    private Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (viewGroup == null || view == null) {
            return null;
        }
        for (int iIndexOf = this.mAdded.indexOf(fragment) - 1; iIndexOf >= 0; iIndexOf--) {
            Fragment fragment2 = this.mAdded.get(iIndexOf);
            if (fragment2.mContainer == viewGroup && fragment2.mView != null) {
                return fragment2;
            }
        }
        return null;
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        synchronized (this) {
            try {
                if (this.mPendingActions != null && this.mPendingActions.size() != 0) {
                    int size = this.mPendingActions.size();
                    boolean zGenerateOps = false;
                    for (int i = 0; i < size; i++) {
                        zGenerateOps |= this.mPendingActions.get(i).generateOps(arrayList, arrayList2);
                    }
                    this.mPendingActions.clear();
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    return zGenerateOps;
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static Animation.AnimationListener getAnimationListener(Animation animation) throws NoSuchFieldException {
        Animation.AnimationListener animationListener;
        try {
            if (sAnimationListenerField == null) {
                Field declaredField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField = declaredField;
                declaredField.setAccessible(true);
            }
            animationListener = (Animation.AnimationListener) sAnimationListenerField.get(animation);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Cannot access Animation's mListener field", e);
            animationListener = null;
            return animationListener;
        } catch (NoSuchFieldException e2) {
            Log.e(TAG, "No field with the name mListener is found in Animation class", e2);
            animationListener = null;
            return animationListener;
        }
        return animationListener;
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return new AnimationOrAnimator(alphaAnimation);
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f3, f4);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        animationSet.addAnimation(alphaAnimation);
        return new AnimationOrAnimator(animationSet);
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            Fragment fragmentValueAt = arraySet.valueAt(i);
            if (!fragmentValueAt.mAdded) {
                View view = fragmentValueAt.getView();
                fragmentValueAt.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    static boolean modifiesAlpha(Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            for (PropertyValuesHolder propertyValuesHolder : ((ValueAnimator) animator).getValues()) {
                if ("alpha".equals(propertyValuesHolder.getPropertyName())) {
                    return true;
                }
            }
            return false;
        }
        if (!(animator instanceof AnimatorSet)) {
            return false;
        }
        ArrayList<Animator> childAnimations = ((AnimatorSet) animator).getChildAnimations();
        for (int i = 0; i < childAnimations.size(); i++) {
            if (modifiesAlpha(childAnimations.get(i))) {
                return true;
            }
        }
        return false;
    }

    static boolean modifiesAlpha(AnimationOrAnimator animationOrAnimator) {
        if (animationOrAnimator.animation instanceof AlphaAnimation) {
            return true;
        }
        if (!(animationOrAnimator.animation instanceof AnimationSet)) {
            return modifiesAlpha(animationOrAnimator.animator);
        }
        List<Animation> animations = ((AnimationSet) animationOrAnimator.animation).getAnimations();
        for (int i = 0; i < animations.size(); i++) {
            if (animations.get(i) instanceof AlphaAnimation) {
                return true;
            }
        }
        return false;
    }

    private boolean popBackStackImmediate(String str, int i, int i2) {
        FragmentManager fragmentManagerPeekChildFragmentManager;
        execPendingActions();
        ensureExecReady(true);
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null && i < 0 && str == null && (fragmentManagerPeekChildFragmentManager = fragment.peekChildFragmentManager()) != null && fragmentManagerPeekChildFragmentManager.popBackStackImmediate()) {
            return true;
        }
        boolean zPopBackStackState = popBackStackState(this.mTmpRecords, this.mTmpIsPop, str, i, i2);
        if (zPopBackStackState) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
        return zPopBackStackState;
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2, ArraySet<Fragment> arraySet) throws NoSuchFieldException, Resources.NotFoundException {
        int i3 = i2 - 1;
        int i4 = i2;
        while (true) {
            int i5 = i4;
            if (i3 < i) {
                return i5;
            }
            BackStackRecord backStackRecord = arrayList.get(i3);
            boolean zBooleanValue = arrayList2.get(i3).booleanValue();
            int i6 = i5;
            if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i3 + 1, i2)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, zBooleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (zBooleanValue) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                i6 = i5 - 1;
                if (i3 != i6) {
                    arrayList.remove(i3);
                    arrayList.add(i6, backStackRecord);
                }
                addAddedFragments(arraySet);
            }
            i3--;
            i4 = i6;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) throws NoSuchFieldException, Resources.NotFoundException {
        int i;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        if (arrayList2 == null || arrayList.size() != arrayList2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
        }
        executePostponedTransaction(arrayList, arrayList2);
        int size = arrayList.size();
        int i2 = 0;
        int i3 = 0;
        while (true) {
            i = i3;
            if (i2 >= size) {
                break;
            }
            int i4 = i2;
            int i5 = i;
            if (!arrayList.get(i2).mReorderingAllowed) {
                if (i != i2) {
                    executeOpsTogether(arrayList, arrayList2, i, i2);
                }
                int i6 = i2 + 1;
                i5 = i6;
                if (arrayList2.get(i2).booleanValue()) {
                    while (true) {
                        i5 = i6;
                        if (i6 >= size) {
                            break;
                        }
                        i5 = i6;
                        if (!arrayList2.get(i6).booleanValue()) {
                            break;
                        }
                        i5 = i6;
                        if (arrayList.get(i6).mReorderingAllowed) {
                            break;
                        } else {
                            i6++;
                        }
                    }
                }
                executeOpsTogether(arrayList, arrayList2, i2, i5);
                i4 = i5 - 1;
            }
            i2 = i4 + 1;
            i3 = i5;
        }
        if (i != size) {
            executeOpsTogether(arrayList, arrayList2, i, size);
        }
    }

    public static int reverseTransit(int i) {
        int i2 = 8194;
        if (i != 4097) {
            i2 = i != 4099 ? i != 8194 ? 0 : 4097 : 4099;
        }
        return i2;
    }

    private static void setHWLayerAnimListenerIfAlpha(View view, AnimationOrAnimator animationOrAnimator) throws NoSuchFieldException {
        if (view == null || animationOrAnimator == null || !shouldRunOnHWLayer(view, animationOrAnimator)) {
            return;
        }
        if (animationOrAnimator.animator != null) {
            animationOrAnimator.animator.addListener(new AnimatorOnHWLayerIfNeededListener(view));
            return;
        }
        Animation.AnimationListener animationListener = getAnimationListener(animationOrAnimator.animation);
        view.setLayerType(2, null);
        animationOrAnimator.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(view, animationListener));
    }

    private static void setRetaining(FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig == null) {
            return;
        }
        List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
        if (fragments != null) {
            Iterator<Fragment> it = fragments.iterator();
            while (it.hasNext()) {
                it.next().mRetaining = true;
            }
        }
        List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
        if (childNonConfigs != null) {
            Iterator<FragmentManagerNonConfig> it2 = childNonConfigs.iterator();
            while (it2.hasNext()) {
                setRetaining(it2.next());
            }
        }
    }

    static boolean shouldRunOnHWLayer(View view, AnimationOrAnimator animationOrAnimator) {
        boolean z = false;
        if (view != null) {
            if (animationOrAnimator == null) {
                z = false;
            } else {
                z = false;
                if (Build.VERSION.SDK_INT >= 19) {
                    z = false;
                    if (view.getLayerType() == 0) {
                        z = false;
                        if (ViewCompat.hasOverlappingRendering(view)) {
                            z = false;
                            if (modifiesAlpha(animationOrAnimator)) {
                                z = true;
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e(TAG, runtimeException.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        FragmentHostCallback fragmentHostCallback = this.mHost;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.onDump("  ", null, printWriter, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, printWriter, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw runtimeException;
    }

    public static int transitToStyleIndex(int i, boolean z) {
        return i != 4097 ? i != 4099 ? i != 8194 ? -1 : z ? 3 : 4 : z ? 5 : 6 : z ? 1 : 2;
    }

    void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(backStackRecord);
    }

    public void addFragment(Fragment fragment, boolean z) {
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        synchronized (this.mAdded) {
            this.mAdded.add(fragment);
        }
        fragment.mAdded = true;
        fragment.mRemoving = false;
        if (fragment.mView == null) {
            fragment.mHiddenChanged = false;
        }
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (z) {
            moveToState(fragment);
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                int iIntValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1).intValue();
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + iIntValue + " with " + backStackRecord);
                }
                this.mBackStackIndices.set(iIntValue, backStackRecord);
                return iIntValue;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int size = this.mBackStackIndices.size();
            if (DEBUG) {
                Log.v(TAG, "Setting back stack index " + size + " to " + backStackRecord);
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }

    public void attachController(FragmentHostCallback fragmentHostCallback, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = fragmentHostCallback;
        this.mContainer = fragmentContainer;
        this.mParent = fragment;
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (this.mAdded.contains(fragment)) {
                throw new IllegalStateException("Fragment already added: " + fragment);
            }
            if (DEBUG) {
                Log.v(TAG, "add from attach: " + fragment);
            }
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    void completeExecute(BackStackRecord backStackRecord, boolean z, boolean z2, boolean z3) {
        if (z) {
            backStackRecord.executePopOps(z3);
        } else {
            backStackRecord.executeOps();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(backStackRecord);
        arrayList2.add(Boolean.valueOf(z));
        if (z2) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, 0, 1, true);
        }
        if (z3) {
            moveToState(this.mCurState, true);
        }
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray != null) {
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                Fragment fragmentValueAt = this.mActive.valueAt(i);
                if (fragmentValueAt != null && fragmentValueAt.mView != null && fragmentValueAt.mIsNewlyAdded && backStackRecord.interactsWith(fragmentValueAt.mContainerId)) {
                    if (fragmentValueAt.mPostponedAlpha > 0.0f) {
                        fragmentValueAt.mView.setAlpha(fragmentValueAt.mPostponedAlpha);
                    }
                    if (z3) {
                        fragmentValueAt.mPostponedAlpha = 0.0f;
                    } else {
                        fragmentValueAt.mPostponedAlpha = -1.0f;
                        fragmentValueAt.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }

    void completeShowHideFragment(Fragment fragment) throws NoSuchFieldException, Resources.NotFoundException {
        if (fragment.mView != null) {
            AnimationOrAnimator animationOrAnimatorLoadAnimation = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (animationOrAnimatorLoadAnimation == null || animationOrAnimatorLoadAnimation.animator == null) {
                if (animationOrAnimatorLoadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimatorLoadAnimation);
                    fragment.mView.startAnimation(animationOrAnimatorLoadAnimation.animation);
                    animationOrAnimatorLoadAnimation.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                animationOrAnimatorLoadAnimation.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    ViewGroup viewGroup = fragment.mContainer;
                    View view = fragment.mView;
                    viewGroup.startViewTransition(view);
                    animationOrAnimatorLoadAnimation.animator.addListener(new AnimatorListenerAdapter(this, viewGroup, view, fragment) { // from class: android.support.v4.app.FragmentManagerImpl.4
                        final FragmentManagerImpl this$0;
                        final View val$animatingView;
                        final ViewGroup val$container;
                        final Fragment val$fragment;

                        {
                            this.this$0 = this;
                            this.val$container = viewGroup;
                            this.val$animatingView = view;
                            this.val$fragment = fragment;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            this.val$container.endViewTransition(this.val$animatingView);
                            animator.removeListener(this);
                            if (this.val$fragment.mView != null) {
                                this.val$fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimatorLoadAnimation);
                animationOrAnimatorLoadAnimation.animator.start();
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (fragment.mDetached) {
            return;
        }
        fragment.mDetached = true;
        if (fragment.mAdded) {
            if (DEBUG) {
                Log.v(TAG, "remove from detach: " + fragment);
            }
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
        }
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(2);
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(1);
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        boolean z;
        if (this.mCurState < 1) {
            return false;
        }
        ArrayList<Fragment> arrayList = null;
        int i = 0;
        boolean z2 = false;
        while (true) {
            z = z2;
            if (i >= this.mAdded.size()) {
                break;
            }
            Fragment fragment = this.mAdded.get(i);
            ArrayList<Fragment> arrayList2 = arrayList;
            boolean z3 = z;
            if (fragment != null) {
                arrayList2 = arrayList;
                z3 = z;
                if (fragment.performCreateOptionsMenu(menu, menuInflater)) {
                    arrayList2 = arrayList;
                    if (arrayList == null) {
                        arrayList2 = new ArrayList<>();
                    }
                    arrayList2.add(fragment);
                    z3 = true;
                }
            }
            i++;
            arrayList = arrayList2;
            z2 = z3;
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment fragment2 = this.mCreatedMenus.get(i2);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }

    public void dispatchMultiWindowModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = this.mAdded.get(size);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(z);
            }
        }
    }

    void dispatchOnFragmentActivityCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentActivityCreated(this, fragment, bundle);
            }
        }
    }

    void dispatchOnFragmentAttached(Fragment fragment, Context context, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentAttached(this, fragment, context);
            }
        }
    }

    void dispatchOnFragmentCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentCreated(this, fragment, bundle);
            }
        }
    }

    void dispatchOnFragmentDestroyed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentDestroyed(this, fragment);
            }
        }
    }

    void dispatchOnFragmentDetached(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentDetached(this, fragment);
            }
        }
    }

    void dispatchOnFragmentPaused(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentPaused(this, fragment);
            }
        }
    }

    void dispatchOnFragmentPreAttached(Fragment fragment, Context context, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentPreAttached(this, fragment, context);
            }
        }
    }

    void dispatchOnFragmentPreCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentPreCreated(this, fragment, bundle);
            }
        }
    }

    void dispatchOnFragmentResumed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentResumed(this, fragment);
            }
        }
    }

    void dispatchOnFragmentSaveInstanceState(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentSaveInstanceState(this, fragment, bundle);
            }
        }
    }

    void dispatchOnFragmentStarted(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentStarted(this, fragment);
            }
        }
    }

    void dispatchOnFragmentStopped(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentStopped(this, fragment);
            }
        }
    }

    void dispatchOnFragmentViewCreated(Fragment fragment, View view, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentViewCreated(this, fragment, view, bundle);
            }
        }
    }

    void dispatchOnFragmentViewDestroyed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mParent;
        if (fragment2 != null) {
            FragmentManager fragmentManager = fragment2.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (!z || next.mRecursive) {
                next.mCallback.onFragmentViewDestroyed(this, fragment);
            }
        }
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mCurState < 1) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mCurState < 1) {
            return;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }

    public void dispatchPause() {
        dispatchStateChange(3);
    }

    public void dispatchPictureInPictureModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = this.mAdded.get(size);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(z);
            }
        }
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        int i = 0;
        if (this.mCurState < 1) {
            return false;
        }
        boolean z = false;
        while (true) {
            boolean z2 = z;
            if (i >= this.mAdded.size()) {
                return z2;
            }
            Fragment fragment = this.mAdded.get(i);
            boolean z3 = z2;
            if (fragment != null) {
                z3 = z2;
                if (fragment.performPrepareOptionsMenu(menu)) {
                    z3 = true;
                }
            }
            i++;
            z = z3;
        }
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(4);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        this.mStopped = false;
        dispatchStateChange(3);
    }

    public void dispatchStop() {
        this.mStopped = true;
        dispatchStateChange(2);
    }

    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            this.mHavePendingDeferredStart = false;
            startPendingDeferredFragments();
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        int size3;
        int size4;
        int size5;
        String str2 = str + "    ";
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray != null && (size5 = sparseArray.size()) > 0) {
            printWriter.print(str);
            printWriter.print("Active Fragments in ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(":");
            for (int i = 0; i < size5; i++) {
                Fragment fragmentValueAt = this.mActive.valueAt(i);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(fragmentValueAt);
                if (fragmentValueAt != null) {
                    fragmentValueAt.dump(str2, fileDescriptor, printWriter, strArr);
                }
            }
        }
        int size6 = this.mAdded.size();
        if (size6 > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i2 = 0; i2 < size6; i2++) {
                Fragment fragment = this.mAdded.get(i2);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i2);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
        ArrayList<Fragment> arrayList = this.mCreatedMenus;
        if (arrayList != null && (size4 = arrayList.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i3 = 0; i3 < size4; i3++) {
                Fragment fragment2 = this.mCreatedMenus.get(i3);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(fragment2.toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.mBackStack;
        if (arrayList2 != null && (size3 = arrayList2.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i4 = 0; i4 < size3; i4++) {
                BackStackRecord backStackRecord = this.mBackStack.get(i4);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i4);
                printWriter.print(": ");
                printWriter.println(backStackRecord.toString());
                backStackRecord.dump(str2, fileDescriptor, printWriter, strArr);
            }
        }
        synchronized (this) {
            try {
                if (this.mBackStackIndices != null && (size2 = this.mBackStackIndices.size()) > 0) {
                    printWriter.print(str);
                    printWriter.println("Back Stack Indices:");
                    for (int i5 = 0; i5 < size2; i5++) {
                        Object obj = (BackStackRecord) this.mBackStackIndices.get(i5);
                        printWriter.print(str);
                        printWriter.print("  #");
                        printWriter.print(i5);
                        printWriter.print(": ");
                        printWriter.println(obj);
                    }
                }
                if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                    printWriter.print(str);
                    printWriter.print("mAvailBackStackIndices: ");
                    printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        ArrayList<OpGenerator> arrayList3 = this.mPendingActions;
        if (arrayList3 != null && (size = arrayList3.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Pending Actions:");
            for (int i6 = 0; i6 < size; i6++) {
                Object obj2 = (OpGenerator) this.mPendingActions.get(i6);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i6);
                printWriter.print(": ");
                printWriter.println(obj2);
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(str);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
    }

    public void enqueueAction(OpGenerator opGenerator, boolean z) {
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList<>();
                }
                this.mPendingActions.add(opGenerator);
                scheduleCommit();
                return;
            }
            if (!z) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    void ensureInflatedFragmentView(Fragment fragment) {
        if (!fragment.mFromLayout || fragment.mPerformedCreateView) {
            return;
        }
        fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
        if (fragment.mView == null) {
            fragment.mInnerView = null;
            return;
        }
        fragment.mInnerView = fragment.mView;
        fragment.mView.setSaveFromParentEnabled(false);
        if (fragment.mHidden) {
            fragment.mView.setVisibility(8);
        }
        fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
        dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
    }

    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean z = false;
        while (true) {
            boolean z2 = z;
            if (!generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
                doPendingDeferredStart();
                burpActive();
                return z2;
            }
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                z = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
    }

    public void execSingleAction(OpGenerator opGenerator, boolean z) {
        if (z && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        ensureExecReady(z);
        if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean executePendingTransactions() {
        boolean zExecPendingActions = execPendingActions();
        forcePostponedTransactions();
        return zExecPendingActions;
    }

    @Override // android.support.v4.app.FragmentManager
    public Fragment findFragmentById(int i) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = this.mAdded.get(size);
            if (fragment != null && fragment.mFragmentId == i) {
                return fragment;
            }
        }
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return null;
        }
        for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
            Fragment fragmentValueAt = this.mActive.valueAt(size2);
            if (fragmentValueAt != null && fragmentValueAt.mFragmentId == i) {
                return fragmentValueAt;
            }
        }
        return null;
    }

    @Override // android.support.v4.app.FragmentManager
    public Fragment findFragmentByTag(String str) {
        if (str != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = this.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null || str == null) {
            return null;
        }
        for (int size2 = sparseArray.size() - 1; size2 >= 0; size2--) {
            Fragment fragmentValueAt = this.mActive.valueAt(size2);
            if (fragmentValueAt != null && str.equals(fragmentValueAt.mTag)) {
                return fragmentValueAt;
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String str) {
        Fragment fragmentFindFragmentByWho;
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null || str == null) {
            return null;
        }
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            Fragment fragmentValueAt = this.mActive.valueAt(size);
            if (fragmentValueAt != null && (fragmentFindFragmentByWho = fragmentValueAt.findFragmentByWho(str)) != null) {
                return fragmentFindFragmentByWho;
            }
        }
        return null;
    }

    public void freeBackStackIndex(int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + i);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(i));
        }
    }

    int getActiveFragmentCount() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return 0;
        }
        return sparseArray.size();
    }

    List<Fragment> getActiveFragments() {
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null) {
            return null;
        }
        int size = sparseArray.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(this.mActive.valueAt(i));
        }
        return arrayList;
    }

    @Override // android.support.v4.app.FragmentManager
    public FragmentManager.BackStackEntry getBackStackEntryAt(int i) {
        return this.mBackStack.get(i);
    }

    @Override // android.support.v4.app.FragmentManager
    public int getBackStackEntryCount() {
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        return arrayList != null ? arrayList.size() : 0;
    }

    @Override // android.support.v4.app.FragmentManager
    public Fragment getFragment(Bundle bundle, String str) {
        int i = bundle.getInt(str, -1);
        if (i == -1) {
            return null;
        }
        Fragment fragment = this.mActive.get(i);
        if (fragment == null) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + str + ": index " + i));
        }
        return fragment;
    }

    @Override // android.support.v4.app.FragmentManager
    public List<Fragment> getFragments() {
        List<Fragment> list;
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    LayoutInflater.Factory2 getLayoutInflaterFactory() {
        return this;
    }

    @Override // android.support.v4.app.FragmentManager
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (fragment.mHidden) {
            return;
        }
        fragment.mHidden = true;
        fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    boolean isStateAtLeast(int i) {
        return this.mCurState >= i;
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean isStateSaved() {
        return this.mStateSaved || this.mStopped;
    }

    AnimationOrAnimator loadAnimation(Fragment fragment, int i, boolean z, int i2) throws Resources.NotFoundException {
        int iTransitToStyleIndex;
        int nextAnim = fragment.getNextAnim();
        Animation animationOnCreateAnimation = fragment.onCreateAnimation(i, z, nextAnim);
        if (animationOnCreateAnimation != null) {
            return new AnimationOrAnimator(animationOnCreateAnimation);
        }
        Animator animatorOnCreateAnimator = fragment.onCreateAnimator(i, z, nextAnim);
        if (animatorOnCreateAnimator != null) {
            return new AnimationOrAnimator(animatorOnCreateAnimator);
        }
        if (nextAnim != 0) {
            boolean zEquals = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean z2 = false;
            if (zEquals) {
                try {
                    Animation animationLoadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animationLoadAnimation != null) {
                        return new AnimationOrAnimator(animationLoadAnimation);
                    }
                    z2 = true;
                } catch (Resources.NotFoundException e) {
                    throw e;
                } catch (RuntimeException e2) {
                    z2 = false;
                }
            }
            if (!z2) {
                try {
                    Animator animatorLoadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (animatorLoadAnimator != null) {
                        return new AnimationOrAnimator(animatorLoadAnimator);
                    }
                } catch (RuntimeException e3) {
                    if (zEquals) {
                        throw e3;
                    }
                    Animation animationLoadAnimation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (animationLoadAnimation2 != null) {
                        return new AnimationOrAnimator(animationLoadAnimation2);
                    }
                }
            }
        }
        if (i == 0 || (iTransitToStyleIndex = transitToStyleIndex(i, z)) < 0) {
            return null;
        }
        switch (iTransitToStyleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                int iOnGetWindowAnimations = i2;
                if (i2 == 0) {
                    iOnGetWindowAnimations = i2;
                    if (this.mHost.onHasWindowAnimations()) {
                        iOnGetWindowAnimations = this.mHost.onGetWindowAnimations();
                    }
                }
                if (iOnGetWindowAnimations == 0) {
                }
                return null;
        }
    }

    void makeActive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        int i = this.mNextFragmentIndex;
        this.mNextFragmentIndex = i + 1;
        fragment.setIndex(i, this.mParent);
        if (this.mActive == null) {
            this.mActive = new SparseArray<>();
        }
        this.mActive.put(fragment.mIndex, fragment);
        if (DEBUG) {
            Log.v(TAG, "Allocated fragment index " + fragment);
        }
    }

    void makeInactive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (DEBUG) {
            Log.v(TAG, "Freeing fragment index " + fragment);
        }
        this.mActive.put(fragment.mIndex, null);
        fragment.initState();
    }

    void moveFragmentToExpectedState(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        int i = this.mCurState;
        int iMin = i;
        if (fragment.mRemoving) {
            iMin = fragment.isInBackStack() ? Math.min(i, 1) : Math.min(i, 0);
        }
        moveToState(fragment, iMin, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
        if (fragment.mView != null) {
            Fragment fragmentFindFragmentUnder = findFragmentUnder(fragment);
            if (fragmentFindFragmentUnder != null) {
                View view = fragmentFindFragmentUnder.mView;
                ViewGroup viewGroup = fragment.mContainer;
                int iIndexOfChild = viewGroup.indexOfChild(view);
                int iIndexOfChild2 = viewGroup.indexOfChild(fragment.mView);
                if (iIndexOfChild2 < iIndexOfChild) {
                    viewGroup.removeViewAt(iIndexOfChild2);
                    viewGroup.addView(fragment.mView, iIndexOfChild);
                }
            }
            if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                if (fragment.mPostponedAlpha > 0.0f) {
                    fragment.mView.setAlpha(fragment.mPostponedAlpha);
                }
                fragment.mPostponedAlpha = 0.0f;
                fragment.mIsNewlyAdded = false;
                AnimationOrAnimator animationOrAnimatorLoadAnimation = loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                if (animationOrAnimatorLoadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimatorLoadAnimation);
                    if (animationOrAnimatorLoadAnimation.animation != null) {
                        fragment.mView.startAnimation(animationOrAnimatorLoadAnimation.animation);
                    } else {
                        animationOrAnimatorLoadAnimation.animator.setTarget(fragment.mView);
                        animationOrAnimatorLoadAnimation.animator.start();
                    }
                }
            }
        }
        if (fragment.mHiddenChanged) {
            completeShowHideFragment(fragment);
        }
    }

    void moveToState(int i, boolean z) {
        FragmentHostCallback fragmentHostCallback;
        if (this.mHost == null && i != 0) {
            throw new IllegalStateException("No activity");
        }
        if (z || i != this.mCurState) {
            this.mCurState = i;
            if (this.mActive != null) {
                int size = this.mAdded.size();
                for (int i2 = 0; i2 < size; i2++) {
                    moveFragmentToExpectedState(this.mAdded.get(i2));
                }
                int size2 = this.mActive.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    Fragment fragmentValueAt = this.mActive.valueAt(i3);
                    if (fragmentValueAt != null && ((fragmentValueAt.mRemoving || fragmentValueAt.mDetached) && !fragmentValueAt.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(fragmentValueAt);
                    }
                }
                startPendingDeferredFragments();
                if (this.mNeedMenuInvalidate && (fragmentHostCallback = this.mHost) != null && this.mCurState == 4) {
                    fragmentHostCallback.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    void moveToState(Fragment fragment) throws NoSuchFieldException, Resources.NotFoundException {
        moveToState(fragment, this.mCurState, 0, 0, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x050c  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x06e0  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x07bb  */
    /* JADX WARN: Removed duplicated region for block: B:238:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void moveToState(android.support.v4.app.Fragment r8, int r9, int r10, int r11, boolean r12) throws java.lang.NoSuchFieldException, android.content.res.Resources.NotFoundException {
        /*
            Method dump skipped, instructions count: 2065
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        this.mStopped = false;
        int size = this.mAdded.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    @Override // android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) throws NoSuchFieldException, Resources.NotFoundException {
        Fragment fragmentInstantiate;
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, FragmentTag.Fragment);
        int id = 0;
        String string = attributeValue;
        if (attributeValue == null) {
            string = typedArrayObtainStyledAttributes.getString(0);
        }
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(1, -1);
        String string2 = typedArrayObtainStyledAttributes.getString(2);
        typedArrayObtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string)) {
            return null;
        }
        if (view != null) {
            id = view.getId();
        }
        if (id == -1 && resourceId == -1 && string2 == null) {
            throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + string);
        }
        Fragment fragmentFindFragmentById = resourceId != -1 ? findFragmentById(resourceId) : null;
        Fragment fragmentFindFragmentByTag = fragmentFindFragmentById;
        if (fragmentFindFragmentById == null) {
            fragmentFindFragmentByTag = fragmentFindFragmentById;
            if (string2 != null) {
                fragmentFindFragmentByTag = findFragmentByTag(string2);
            }
        }
        Fragment fragmentFindFragmentById2 = fragmentFindFragmentByTag;
        if (fragmentFindFragmentByTag == null) {
            fragmentFindFragmentById2 = fragmentFindFragmentByTag;
            if (id != -1) {
                fragmentFindFragmentById2 = findFragmentById(id);
            }
        }
        if (DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(resourceId) + " fname=" + string + " existing=" + fragmentFindFragmentById2);
        }
        if (fragmentFindFragmentById2 == null) {
            fragmentInstantiate = this.mContainer.instantiate(context, string, null);
            fragmentInstantiate.mFromLayout = true;
            fragmentInstantiate.mFragmentId = resourceId != 0 ? resourceId : id;
            fragmentInstantiate.mContainerId = id;
            fragmentInstantiate.mTag = string2;
            fragmentInstantiate.mInLayout = true;
            fragmentInstantiate.mFragmentManager = this;
            fragmentInstantiate.mHost = this.mHost;
            fragmentInstantiate.onInflate(this.mHost.getContext(), attributeSet, fragmentInstantiate.mSavedFragmentState);
            addFragment(fragmentInstantiate, true);
        } else {
            if (fragmentFindFragmentById2.mInLayout) {
                throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string2 + ", or parent id 0x" + Integer.toHexString(id) + " with another fragment for " + string);
            }
            fragmentFindFragmentById2.mInLayout = true;
            fragmentFindFragmentById2.mHost = this.mHost;
            fragmentInstantiate = fragmentFindFragmentById2;
            if (!fragmentFindFragmentById2.mRetaining) {
                fragmentFindFragmentById2.onInflate(this.mHost.getContext(), attributeSet, fragmentFindFragmentById2.mSavedFragmentState);
                fragmentInstantiate = fragmentFindFragmentById2;
            }
        }
        if (this.mCurState >= 1 || !fragmentInstantiate.mFromLayout) {
            moveToState(fragmentInstantiate);
        } else {
            moveToState(fragmentInstantiate, 1, 0, 0, false);
        }
        if (fragmentInstantiate.mView != null) {
            if (resourceId != 0) {
                fragmentInstantiate.mView.setId(resourceId);
            }
            if (fragmentInstantiate.mView.getTag() == null) {
                fragmentInstantiate.mView.setTag(string2);
            }
            return fragmentInstantiate.mView;
        }
        throw new IllegalStateException("Fragment " + string + " did not create a view.");
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
            } else {
                fragment.mDeferStart = false;
                moveToState(fragment, this.mCurState, 0, 0, false);
            }
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public void popBackStack() {
        enqueueAction(new PopBackStackState(this, null, -1, 0), false);
    }

    @Override // android.support.v4.app.FragmentManager
    public void popBackStack(int i, int i2) {
        if (i >= 0) {
            enqueueAction(new PopBackStackState(this, null, i, i2), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + i);
    }

    @Override // android.support.v4.app.FragmentManager
    public void popBackStack(String str, int i) {
        enqueueAction(new PopBackStackState(this, str, -1, i), false);
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean popBackStackImmediate(int i, int i2) {
        checkStateLoss();
        execPendingActions();
        if (i >= 0) {
            return popBackStackImmediate(null, i, i2);
        }
        throw new IllegalArgumentException("Bad id: " + i);
    }

    @Override // android.support.v4.app.FragmentManager
    public boolean popBackStackImmediate(String str, int i) {
        checkStateLoss();
        return popBackStackImmediate(str, -1, i);
    }

    boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String str, int i, int i2) {
        int i3;
        ArrayList<BackStackRecord> arrayList3 = this.mBackStack;
        if (arrayList3 == null) {
            return false;
        }
        if (str == null && i < 0 && (i2 & 1) == 0) {
            int size = arrayList3.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(size));
            arrayList2.add(true);
            return true;
        }
        if (str != null || i >= 0) {
            int size2 = this.mBackStack.size() - 1;
            while (size2 >= 0) {
                BackStackRecord backStackRecord = this.mBackStack.get(size2);
                if ((str != null && str.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                    break;
                }
                size2--;
            }
            if (size2 < 0) {
                return false;
            }
            int i4 = size2;
            if ((i2 & 1) != 0) {
                while (true) {
                    int i5 = size2 - 1;
                    i4 = i5;
                    if (i5 < 0) {
                        break;
                    }
                    BackStackRecord backStackRecord2 = this.mBackStack.get(i5);
                    if (str != null) {
                        size2 = i5;
                        if (str.equals(backStackRecord2.getName())) {
                            continue;
                        }
                    }
                    i4 = i5;
                    if (i < 0) {
                        break;
                    }
                    i4 = i5;
                    if (i != backStackRecord2.mIndex) {
                        break;
                    }
                    size2 = i5;
                }
            }
            i3 = i4;
        } else {
            i3 = -1;
        }
        if (i3 == this.mBackStack.size() - 1) {
            return false;
        }
        for (int size3 = this.mBackStack.size() - 1; size3 > i3; size3--) {
            arrayList.add(this.mBackStack.remove(size3));
            arrayList2.add(true);
        }
        return true;
    }

    @Override // android.support.v4.app.FragmentManager
    public void putFragment(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(str, fragment.mIndex);
    }

    @Override // android.support.v4.app.FragmentManager
    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(fragmentLifecycleCallbacks, z));
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean zIsInBackStack = fragment.isInBackStack();
        if (!fragment.mDetached || (!zIsInBackStack)) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        ArrayList<FragmentManager.OnBackStackChangedListener> arrayList = this.mBackStackChangeListeners;
        if (arrayList != null) {
            arrayList.remove(onBackStackChangedListener);
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }

    void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        List<FragmentManagerNonConfig> list;
        List<ViewModelStore> list2;
        if (parcelable == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
        if (fragmentManagerState.mActive == null) {
            return;
        }
        if (fragmentManagerNonConfig != null) {
            List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            List<ViewModelStore> viewModelStores = fragmentManagerNonConfig.getViewModelStores();
            int size = fragments != null ? fragments.size() : 0;
            int i = 0;
            while (true) {
                list = childNonConfigs;
                list2 = viewModelStores;
                if (i >= size) {
                    break;
                }
                Fragment fragment = fragments.get(i);
                if (DEBUG) {
                    Log.v(TAG, "restoreAllState: re-attaching retained " + fragment);
                }
                int i2 = 0;
                while (i2 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[i2].mIndex != fragment.mIndex) {
                    i2++;
                }
                if (i2 == fragmentManagerState.mActive.length) {
                    throwException(new IllegalStateException("Could not find active fragment with index " + fragment.mIndex));
                }
                FragmentState fragmentState = fragmentManagerState.mActive[i2];
                fragmentState.mInstance = fragment;
                fragment.mSavedViewState = null;
                fragment.mBackStackNesting = 0;
                fragment.mInLayout = false;
                fragment.mAdded = false;
                fragment.mTarget = null;
                if (fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                    fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                    fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                }
                i++;
            }
        } else {
            list = null;
            list2 = null;
        }
        this.mActive = new SparseArray<>(fragmentManagerState.mActive.length);
        int i3 = 0;
        while (i3 < fragmentManagerState.mActive.length) {
            FragmentState fragmentState2 = fragmentManagerState.mActive[i3];
            if (fragmentState2 != null) {
                Fragment fragmentInstantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, (list == null || i3 >= list.size()) ? null : list.get(i3), (list2 == null || i3 >= list2.size()) ? null : list2.get(i3));
                if (DEBUG) {
                    Log.v(TAG, "restoreAllState: active #" + i3 + ": " + fragmentInstantiate);
                }
                this.mActive.put(fragmentInstantiate.mIndex, fragmentInstantiate);
                fragmentState2.mInstance = null;
            }
            i3++;
        }
        if (fragmentManagerNonConfig != null) {
            List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
            int size2 = fragments2 != null ? fragments2.size() : 0;
            for (int i4 = 0; i4 < size2; i4++) {
                Fragment fragment2 = fragments2.get(i4);
                if (fragment2.mTargetIndex >= 0) {
                    fragment2.mTarget = this.mActive.get(fragment2.mTargetIndex);
                    if (fragment2.mTarget == null) {
                        Log.w(TAG, "Re-attaching retained fragment " + fragment2 + " target no longer exists: " + fragment2.mTargetIndex);
                    }
                }
            }
        }
        this.mAdded.clear();
        if (fragmentManagerState.mAdded != null) {
            for (int i5 = 0; i5 < fragmentManagerState.mAdded.length; i5++) {
                Fragment fragment3 = this.mActive.get(fragmentManagerState.mAdded[i5]);
                if (fragment3 == null) {
                    throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[i5]));
                }
                fragment3.mAdded = true;
                if (DEBUG) {
                    Log.v(TAG, "restoreAllState: added #" + i5 + ": " + fragment3);
                }
                if (this.mAdded.contains(fragment3)) {
                    throw new IllegalStateException("Already added!");
                }
                synchronized (this.mAdded) {
                    this.mAdded.add(fragment3);
                }
            }
        }
        if (fragmentManagerState.mBackStack != null) {
            this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
            for (int i6 = 0; i6 < fragmentManagerState.mBackStack.length; i6++) {
                BackStackRecord backStackRecordInstantiate = fragmentManagerState.mBackStack[i6].instantiate(this);
                if (DEBUG) {
                    Log.v(TAG, "restoreAllState: back stack #" + i6 + " (index " + backStackRecordInstantiate.mIndex + "): " + backStackRecordInstantiate);
                    PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
                    backStackRecordInstantiate.dump("  ", printWriter, false);
                    printWriter.close();
                }
                this.mBackStack.add(backStackRecordInstantiate);
                if (backStackRecordInstantiate.mIndex >= 0) {
                    setBackStackIndex(backStackRecordInstantiate.mIndex, backStackRecordInstantiate);
                }
            }
        } else {
            this.mBackStack = null;
        }
        if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
            this.mPrimaryNav = this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
        }
        this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
    }

    FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    Parcelable saveAllState() throws NoSuchFieldException, Resources.NotFoundException {
        int[] iArr;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        SparseArray<Fragment> sparseArray = this.mActive;
        if (sparseArray == null || sparseArray.size() <= 0) {
            return null;
        }
        int size = this.mActive.size();
        FragmentState[] fragmentStateArr = new FragmentState[size];
        boolean z = false;
        for (int i = 0; i < size; i++) {
            Fragment fragmentValueAt = this.mActive.valueAt(i);
            if (fragmentValueAt != null) {
                if (fragmentValueAt.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + fragmentValueAt + " has cleared index: " + fragmentValueAt.mIndex));
                }
                FragmentState fragmentState = new FragmentState(fragmentValueAt);
                fragmentStateArr[i] = fragmentState;
                if (fragmentValueAt.mState <= 0 || fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState = fragmentValueAt.mSavedFragmentState;
                } else {
                    fragmentState.mSavedFragmentState = saveFragmentBasicState(fragmentValueAt);
                    if (fragmentValueAt.mTarget != null) {
                        if (fragmentValueAt.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + fragmentValueAt + " has target not in fragment manager: " + fragmentValueAt.mTarget));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fragmentState.mSavedFragmentState, TARGET_STATE_TAG, fragmentValueAt.mTarget);
                        if (fragmentValueAt.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, fragmentValueAt.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + fragmentValueAt + ": " + fragmentState.mSavedFragmentState);
                }
                z = true;
            }
        }
        if (!z) {
            if (!DEBUG) {
                return null;
            }
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
        int size2 = this.mAdded.size();
        if (size2 > 0) {
            int[] iArr2 = new int[size2];
            int i2 = 0;
            while (true) {
                iArr = iArr2;
                if (i2 >= size2) {
                    break;
                }
                iArr2[i2] = this.mAdded.get(i2).mIndex;
                if (iArr2[i2] < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i2) + " has cleared index: " + iArr2[i2]));
                }
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding fragment #" + i2 + ": " + this.mAdded.get(i2));
                }
                i2++;
            }
        } else {
            iArr = null;
        }
        ArrayList<BackStackRecord> arrayList = this.mBackStack;
        BackStackState[] backStackStateArr = null;
        if (arrayList != null) {
            int size3 = arrayList.size();
            backStackStateArr = null;
            if (size3 > 0) {
                BackStackState[] backStackStateArr2 = new BackStackState[size3];
                int i3 = 0;
                while (true) {
                    backStackStateArr = backStackStateArr2;
                    if (i3 >= size3) {
                        break;
                    }
                    backStackStateArr2[i3] = new BackStackState(this.mBackStack.get(i3));
                    if (DEBUG) {
                        Log.v(TAG, "saveAllState: adding back stack #" + i3 + ": " + this.mBackStack.get(i3));
                    }
                    i3++;
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = fragmentStateArr;
        fragmentManagerState.mAdded = iArr;
        fragmentManagerState.mBackStack = backStackStateArr;
        Fragment fragment = this.mPrimaryNav;
        if (fragment != null) {
            fragmentManagerState.mPrimaryNavActiveIndex = fragment.mIndex;
        }
        fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
        saveNonConfig();
        return fragmentManagerState;
    }

    Bundle saveFragmentBasicState(Fragment fragment) {
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        Bundle bundle = null;
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            saveFragmentViewState(fragment);
        }
        Bundle bundle2 = bundle;
        if (fragment.mSavedViewState != null) {
            bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putSparseParcelableArray(VIEW_STATE_TAG, fragment.mSavedViewState);
        }
        Bundle bundle3 = bundle2;
        if (!fragment.mUserVisibleHint) {
            bundle3 = bundle2;
            if (bundle2 == null) {
                bundle3 = new Bundle();
            }
            bundle3.putBoolean(USER_VISIBLE_HINT_TAG, fragment.mUserVisibleHint);
        }
        return bundle3;
    }

    @Override // android.support.v4.app.FragmentManager
    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        Fragment.SavedState savedState = null;
        if (fragment.mState > 0) {
            Bundle bundleSaveFragmentBasicState = saveFragmentBasicState(fragment);
            savedState = null;
            if (bundleSaveFragmentBasicState != null) {
                savedState = new Fragment.SavedState(bundleSaveFragmentBasicState);
            }
        }
        return savedState;
    }

    void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        SparseArray<Parcelable> sparseArray = this.mStateArray;
        if (sparseArray == null) {
            this.mStateArray = new SparseArray<>();
        } else {
            sparseArray.clear();
        }
        fragment.mInnerView.saveHierarchyState(this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }

    void saveNonConfig() {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        FragmentManagerNonConfig fragmentManagerNonConfig;
        if (this.mActive != null) {
            ArrayList arrayList4 = null;
            ArrayList arrayList5 = null;
            ArrayList arrayList6 = null;
            int i = 0;
            while (true) {
                arrayList = arrayList4;
                arrayList2 = arrayList5;
                arrayList3 = arrayList6;
                if (i >= this.mActive.size()) {
                    break;
                }
                Fragment fragmentValueAt = this.mActive.valueAt(i);
                ArrayList arrayList7 = arrayList4;
                ArrayList arrayList8 = arrayList5;
                ArrayList arrayList9 = arrayList6;
                if (fragmentValueAt != null) {
                    ArrayList arrayList10 = arrayList4;
                    if (fragmentValueAt.mRetainInstance) {
                        ArrayList arrayList11 = arrayList4;
                        if (arrayList4 == null) {
                            arrayList11 = new ArrayList();
                        }
                        arrayList11.add(fragmentValueAt);
                        fragmentValueAt.mTargetIndex = fragmentValueAt.mTarget != null ? fragmentValueAt.mTarget.mIndex : -1;
                        arrayList10 = arrayList11;
                        if (DEBUG) {
                            Log.v(TAG, "retainNonConfig: keeping retained " + fragmentValueAt);
                            arrayList10 = arrayList11;
                        }
                    }
                    if (fragmentValueAt.mChildFragmentManager != null) {
                        fragmentValueAt.mChildFragmentManager.saveNonConfig();
                        fragmentManagerNonConfig = fragmentValueAt.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        fragmentManagerNonConfig = fragmentValueAt.mChildNonConfig;
                    }
                    ArrayList arrayList12 = arrayList5;
                    if (arrayList5 == null) {
                        arrayList12 = arrayList5;
                        if (fragmentManagerNonConfig != null) {
                            ArrayList arrayList13 = new ArrayList(this.mActive.size());
                            int i2 = 0;
                            while (true) {
                                arrayList12 = arrayList13;
                                if (i2 >= i) {
                                    break;
                                }
                                arrayList13.add(null);
                                i2++;
                            }
                        }
                    }
                    if (arrayList12 != null) {
                        arrayList12.add(fragmentManagerNonConfig);
                    }
                    ArrayList arrayList14 = arrayList6;
                    if (arrayList6 == null) {
                        arrayList14 = arrayList6;
                        if (fragmentValueAt.mViewModelStore != null) {
                            ArrayList arrayList15 = new ArrayList(this.mActive.size());
                            int i3 = 0;
                            while (true) {
                                arrayList14 = arrayList15;
                                if (i3 >= i) {
                                    break;
                                }
                                arrayList15.add(null);
                                i3++;
                            }
                        }
                    }
                    arrayList7 = arrayList10;
                    arrayList8 = arrayList12;
                    arrayList9 = arrayList14;
                    if (arrayList14 != null) {
                        arrayList14.add(fragmentValueAt.mViewModelStore);
                        arrayList9 = arrayList14;
                        arrayList8 = arrayList12;
                        arrayList7 = arrayList10;
                    }
                }
                i++;
                arrayList4 = arrayList7;
                arrayList5 = arrayList8;
                arrayList6 = arrayList9;
            }
        } else {
            arrayList = null;
            arrayList2 = null;
            arrayList3 = null;
        }
        if (arrayList == null && arrayList2 == null && arrayList3 == null) {
            this.mSavedNonConfig = null;
        } else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(arrayList, arrayList2, arrayList3);
        }
    }

    void scheduleCommit() {
        synchronized (this) {
            boolean z = (this.mPostponedTransactions == null || this.mPostponedTransactions.isEmpty()) ? false : true;
            boolean z2 = false;
            if (this.mPendingActions != null) {
                z2 = false;
                if (this.mPendingActions.size() == 1) {
                    z2 = true;
                }
            }
            if (z || z2) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        synchronized (this) {
            try {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList<>();
                }
                int size = this.mBackStackIndices.size();
                if (i < size) {
                    if (DEBUG) {
                        Log.v(TAG, "Setting back stack index " + i + " to " + backStackRecord);
                    }
                    this.mBackStackIndices.set(i, backStackRecord);
                } else {
                    for (int i2 = size; i2 < i; i2++) {
                        this.mBackStackIndices.add(null);
                        if (this.mAvailBackStackIndices == null) {
                            this.mAvailBackStackIndices = new ArrayList<>();
                        }
                        if (DEBUG) {
                            Log.v(TAG, "Adding available back stack index " + i2);
                        }
                        this.mAvailBackStackIndices.add(Integer.valueOf(i2));
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding back stack index " + i + " with " + backStackRecord);
                    }
                    this.mBackStackIndices.add(backStackRecord);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment == null || (this.mActive.get(fragment.mIndex) == fragment && (fragment.mHost == null || fragment.getFragmentManager() == this))) {
            this.mPrimaryNav = fragment;
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); i++) {
            Fragment fragmentValueAt = this.mActive.valueAt(i);
            if (fragmentValueAt != null) {
                performPendingDeferredStart(fragmentValueAt);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.mParent;
        if (fragment != null) {
            DebugUtils.buildShortClassTag(fragment, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    @Override // android.support.v4.app.FragmentManager
    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            try {
                int size = this.mLifecycleCallbacks.size();
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    if (this.mLifecycleCallbacks.get(i).mCallback == fragmentLifecycleCallbacks) {
                        this.mLifecycleCallbacks.remove(i);
                        break;
                    }
                    i++;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
