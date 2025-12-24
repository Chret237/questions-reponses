package android.support.v4.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v4/app/BackStackRecord.class */
final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManagerImpl.OpGenerator {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SET_PRIMARY_NAV = 8;
    static final int OP_SHOW = 5;
    static final int OP_UNSET_PRIMARY_NAV = 9;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    final FragmentManagerImpl mManager;
    String mName;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;
    ArrayList<C0171Op> mOps = new ArrayList<>();
    boolean mAllowAddToBackStack = true;
    int mIndex = -1;
    boolean mReorderingAllowed = false;

    /* renamed from: android.support.v4.app.BackStackRecord$Op */
    /* loaded from: classes-dex2jar.jar:android/support/v4/app/BackStackRecord$Op.class */
    static final class C0171Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;

        C0171Op() {
        }

        C0171Op(int i, Fragment fragment) {
            this.cmd = i;
            this.fragment = fragment;
        }
    }

    public BackStackRecord(FragmentManagerImpl fragmentManagerImpl) {
        this.mManager = fragmentManagerImpl;
    }

    private void doAddOp(int i, Fragment fragment, String str, int i2) {
        Class<?> cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (cls.isAnonymousClass() || !Modifier.isPublic(modifiers) || (cls.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalStateException("Fragment " + cls.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
        }
        fragment.mFragmentManager = this.mManager;
        if (str != null) {
            if (fragment.mTag != null && !str.equals(fragment.mTag)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + str);
            }
            fragment.mTag = str;
        }
        if (i != 0) {
            if (i == -1) {
                throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + str + " to container view with no id");
            }
            if (fragment.mFragmentId != 0 && fragment.mFragmentId != i) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + i);
            }
            fragment.mFragmentId = i;
            fragment.mContainerId = i;
        }
        addOp(new C0171Op(i2, fragment));
    }

    private static boolean isFragmentPostponed(C0171Op c0171Op) {
        Fragment fragment = c0171Op.fragment;
        return (fragment == null || !fragment.mAdded || fragment.mView == null || fragment.mDetached || fragment.mHidden || !fragment.isPostponed()) ? false : true;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction add(int i, Fragment fragment) {
        doAddOp(i, fragment, null, 1);
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction add(int i, Fragment fragment, String str) {
        doAddOp(i, fragment, str, 1);
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction add(Fragment fragment, String str) {
        doAddOp(0, fragment, str, 1);
        return this;
    }

    void addOp(C0171Op c0171Op) {
        this.mOps.add(c0171Op);
        c0171Op.enterAnim = this.mEnterAnim;
        c0171Op.exitAnim = this.mExitAnim;
        c0171Op.popEnterAnim = this.mPopEnterAnim;
        c0171Op.popExitAnim = this.mPopExitAnim;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction addSharedElement(View view, String str) {
        if (FragmentTransition.supportsTransition()) {
            String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList<>();
                this.mSharedElementTargetNames = new ArrayList<>();
            } else {
                if (this.mSharedElementTargetNames.contains(str)) {
                    throw new IllegalArgumentException("A shared element with the target name '" + str + "' has already been added to the transaction.");
                }
                if (this.mSharedElementSourceNames.contains(transitionName)) {
                    throw new IllegalArgumentException("A shared element with the source name '" + transitionName + " has already been added to the transaction.");
                }
            }
            this.mSharedElementSourceNames.add(transitionName);
            this.mSharedElementTargetNames.add(str);
        }
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction addToBackStack(String str) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        this.mAddToBackStack = true;
        this.mName = str;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction attach(Fragment fragment) {
        addOp(new C0171Op(7, fragment));
        return this;
    }

    void bumpBackStackNesting(int i) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v(TAG, "Bump nesting in " + this + " by " + i);
            }
            int size = this.mOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                C0171Op c0171Op = this.mOps.get(i2);
                if (c0171Op.fragment != null) {
                    c0171Op.fragment.mBackStackNesting += i;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v(TAG, "Bump nesting of " + c0171Op.fragment + " to " + c0171Op.fragment.mBackStackNesting);
                    }
                }
            }
        }
    }

    @Override // android.support.v4.app.FragmentTransaction
    public int commit() {
        return commitInternal(false);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    int commitInternal(boolean z) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Commit: " + this);
            PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
            dump("  ", null, printWriter, null);
            printWriter.close();
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, z);
        return this.mIndex;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public void commitNow() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public void commitNowAllowingStateLoss() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction detach(Fragment fragment) {
        addOp(new C0171Op(6, fragment));
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dump(str, printWriter, true);
    }

    public void dump(String str, PrintWriter printWriter, boolean z) {
        String str2;
        if (z) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (this.mOps.isEmpty()) {
            return;
        }
        printWriter.print(str);
        printWriter.println("Operations:");
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            C0171Op c0171Op = this.mOps.get(i);
            switch (c0171Op.cmd) {
                case 0:
                    str2 = "NULL";
                    break;
                case 1:
                    str2 = "ADD";
                    break;
                case 2:
                    str2 = "REPLACE";
                    break;
                case 3:
                    str2 = "REMOVE";
                    break;
                case 4:
                    str2 = "HIDE";
                    break;
                case 5:
                    str2 = "SHOW";
                    break;
                case 6:
                    str2 = "DETACH";
                    break;
                case 7:
                    str2 = "ATTACH";
                    break;
                case 8:
                    str2 = "SET_PRIMARY_NAV";
                    break;
                case 9:
                    str2 = "UNSET_PRIMARY_NAV";
                    break;
                default:
                    str2 = "cmd=" + c0171Op.cmd;
                    break;
            }
            printWriter.print(str);
            printWriter.print("  Op #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.print(str2);
            printWriter.print(" ");
            printWriter.println(c0171Op.fragment);
            if (z) {
                if (c0171Op.enterAnim != 0 || c0171Op.exitAnim != 0) {
                    printWriter.print(str);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(c0171Op.enterAnim));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(c0171Op.exitAnim));
                }
                if (c0171Op.popEnterAnim != 0 || c0171Op.popExitAnim != 0) {
                    printWriter.print(str);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(c0171Op.popEnterAnim));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(c0171Op.popExitAnim));
                }
            }
        }
    }

    void executeOps() {
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            C0171Op c0171Op = this.mOps.get(i);
            Fragment fragment = c0171Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            switch (c0171Op.cmd) {
                case 1:
                    fragment.setNextAnim(c0171Op.enterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + c0171Op.cmd);
                case 3:
                    fragment.setNextAnim(c0171Op.exitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                case 4:
                    fragment.setNextAnim(c0171Op.exitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                case 5:
                    fragment.setNextAnim(c0171Op.enterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                case 6:
                    fragment.setNextAnim(c0171Op.exitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                case 7:
                    fragment.setNextAnim(c0171Op.enterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
            }
            if (!this.mReorderingAllowed && c0171Op.cmd != 1 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (this.mReorderingAllowed) {
            return;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mManager;
        fragmentManagerImpl.moveToState(fragmentManagerImpl.mCurState, true);
    }

    void executePopOps(boolean z) {
        for (int size = this.mOps.size() - 1; size >= 0; size--) {
            C0171Op c0171Op = this.mOps.get(size);
            Fragment fragment = c0171Op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            switch (c0171Op.cmd) {
                case 1:
                    fragment.setNextAnim(c0171Op.popExitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + c0171Op.cmd);
                case 3:
                    fragment.setNextAnim(c0171Op.popEnterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                case 4:
                    fragment.setNextAnim(c0171Op.popEnterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                case 5:
                    fragment.setNextAnim(c0171Op.popExitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                case 6:
                    fragment.setNextAnim(c0171Op.popEnterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                case 7:
                    fragment.setNextAnim(c0171Op.popExitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
            }
            if (!this.mReorderingAllowed && c0171Op.cmd != 3 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (this.mReorderingAllowed || !z) {
            return;
        }
        FragmentManagerImpl fragmentManagerImpl = this.mManager;
        fragmentManagerImpl.moveToState(fragmentManagerImpl.mCurState, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x01a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.support.v4.app.Fragment expandOps(java.util.ArrayList<android.support.v4.app.Fragment> r8, android.support.v4.app.Fragment r9) {
        /*
            Method dump skipped, instructions count: 454
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.BackStackRecord.expandOps(java.util.ArrayList, android.support.v4.app.Fragment):android.support.v4.app.Fragment");
    }

    @Override // android.support.v4.app.FragmentManagerImpl.OpGenerator
    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(false);
        if (!this.mAddToBackStack) {
            return true;
        }
        this.mManager.addBackStackState(this);
        return true;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public CharSequence getBreadCrumbShortTitle() {
        return this.mBreadCrumbShortTitleRes != 0 ? this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes) : this.mBreadCrumbShortTitleText;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public CharSequence getBreadCrumbTitle() {
        return this.mBreadCrumbTitleRes != 0 ? this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes) : this.mBreadCrumbTitleText;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public int getId() {
        return this.mIndex;
    }

    @Override // android.support.v4.app.FragmentManager.BackStackEntry
    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction hide(Fragment fragment) {
        addOp(new C0171Op(4, fragment));
        return this;
    }

    boolean interactsWith(int i) {
        int size = this.mOps.size();
        for (int i2 = 0; i2 < size; i2++) {
            C0171Op c0171Op = this.mOps.get(i2);
            int i3 = c0171Op.fragment != null ? c0171Op.fragment.mContainerId : 0;
            if (i3 != 0 && i3 == i) {
                return true;
            }
        }
        return false;
    }

    boolean interactsWith(ArrayList<BackStackRecord> arrayList, int i, int i2) {
        if (i2 == i) {
            return false;
        }
        int size = this.mOps.size();
        int i3 = -1;
        int i4 = 0;
        while (i4 < size) {
            C0171Op c0171Op = this.mOps.get(i4);
            int i5 = c0171Op.fragment != null ? c0171Op.fragment.mContainerId : 0;
            int i6 = i3;
            if (i5 != 0) {
                i6 = i3;
                if (i5 != i3) {
                    for (int i7 = i; i7 < i2; i7++) {
                        BackStackRecord backStackRecord = arrayList.get(i7);
                        int size2 = backStackRecord.mOps.size();
                        for (int i8 = 0; i8 < size2; i8++) {
                            C0171Op c0171Op2 = backStackRecord.mOps.get(i8);
                            if ((c0171Op2.fragment != null ? c0171Op2.fragment.mContainerId : 0) == i5) {
                                return true;
                            }
                        }
                    }
                    i6 = i5;
                } else {
                    continue;
                }
            }
            i4++;
            i3 = i6;
        }
        return false;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }

    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); i++) {
            if (isFragmentPostponed(this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction remove(Fragment fragment) {
        addOp(new C0171Op(3, fragment));
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction replace(int i, Fragment fragment) {
        return replace(i, fragment, null);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction replace(int i, Fragment fragment, String str) {
        if (i == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(i, fragment, str, 2);
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction runOnCommit(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable cannot be null");
        }
        disallowAddToBackStack();
        if (this.mCommitRunnables == null) {
            this.mCommitRunnables = new ArrayList<>();
        }
        this.mCommitRunnables.add(runnable);
        return this;
    }

    public void runOnCommitRunnables() {
        ArrayList<Runnable> arrayList = this.mCommitRunnables;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
        }
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setAllowOptimization(boolean z) {
        return setReorderingAllowed(z);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setBreadCrumbShortTitle(int i) {
        this.mBreadCrumbShortTitleRes = i;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = charSequence;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setBreadCrumbTitle(int i) {
        this.mBreadCrumbTitleRes = i;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = charSequence;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setCustomAnimations(int i, int i2) {
        return setCustomAnimations(i, i2, 0, 0);
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setCustomAnimations(int i, int i2, int i3, int i4) {
        this.mEnterAnim = i;
        this.mExitAnim = i2;
        this.mPopEnterAnim = i3;
        this.mPopExitAnim = i4;
        return this;
    }

    void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); i++) {
            C0171Op c0171Op = this.mOps.get(i);
            if (isFragmentPostponed(c0171Op)) {
                c0171Op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        addOp(new C0171Op(8, fragment));
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setReorderingAllowed(boolean z) {
        this.mReorderingAllowed = z;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setTransition(int i) {
        this.mTransition = i;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction setTransitionStyle(int i) {
        this.mTransitionStyle = i;
        return this;
    }

    @Override // android.support.v4.app.FragmentTransaction
    public FragmentTransaction show(Fragment fragment) {
        addOp(new C0171Op(5, fragment));
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.support.v4.app.Fragment trackAddedFragmentsInPop(java.util.ArrayList<android.support.v4.app.Fragment> r4, android.support.v4.app.Fragment r5) {
        /*
            r3 = this;
            r0 = 0
            r6 = r0
        L2:
            r0 = r6
            r1 = r3
            java.util.ArrayList<android.support.v4.app.BackStackRecord$Op> r1 = r1.mOps
            int r1 = r1.size()
            if (r0 >= r1) goto L7a
            r0 = r3
            java.util.ArrayList<android.support.v4.app.BackStackRecord$Op> r0 = r0.mOps
            r1 = r6
            java.lang.Object r0 = r0.get(r1)
            android.support.v4.app.BackStackRecord$Op r0 = (android.support.v4.app.BackStackRecord.C0171Op) r0
            r8 = r0
            r0 = r8
            int r0 = r0.cmd
            r7 = r0
            r0 = r7
            r1 = 1
            if (r0 == r1) goto L6a
            r0 = r7
            r1 = 3
            if (r0 == r1) goto L5d
            r0 = r7
            switch(r0) {
                case 6: goto L5d;
                case 7: goto L6a;
                case 8: goto L58;
                case 9: goto L4f;
                default: goto L4c;
            }
        L4c:
            goto L74
        L4f:
            r0 = r8
            android.support.v4.app.Fragment r0 = r0.fragment
            r5 = r0
            goto L74
        L58:
            r0 = 0
            r5 = r0
            goto L74
        L5d:
            r0 = r4
            r1 = r8
            android.support.v4.app.Fragment r1 = r1.fragment
            boolean r0 = r0.add(r1)
            goto L74
        L6a:
            r0 = r4
            r1 = r8
            android.support.v4.app.Fragment r1 = r1.fragment
            boolean r0 = r0.remove(r1)
        L74:
            int r6 = r6 + 1
            goto L2
        L7a:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.BackStackRecord.trackAddedFragmentsInPop(java.util.ArrayList, android.support.v4.app.Fragment):android.support.v4.app.Fragment");
    }
}
