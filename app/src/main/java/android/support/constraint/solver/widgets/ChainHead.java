package android.support.constraint.solver.widgets;

import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ChainHead.class */
public class ChainHead {
    private boolean mDefined;
    protected ConstraintWidget mFirst;
    protected ConstraintWidget mFirstMatchConstraintWidget;
    protected ConstraintWidget mFirstVisibleWidget;
    protected boolean mHasComplexMatchWeights;
    protected boolean mHasDefinedWeights;
    protected boolean mHasUndefinedWeights;
    protected ConstraintWidget mHead;
    private boolean mIsRtl;
    protected ConstraintWidget mLast;
    protected ConstraintWidget mLastMatchConstraintWidget;
    protected ConstraintWidget mLastVisibleWidget;
    private int mOrientation;
    protected float mTotalWeight = 0.0f;
    protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
    protected int mWidgetsCount;
    protected int mWidgetsMatchCount;

    public ChainHead(ConstraintWidget constraintWidget, int i, boolean z) {
        this.mIsRtl = false;
        this.mFirst = constraintWidget;
        this.mOrientation = i;
        this.mIsRtl = z;
    }

    private void defineChainProperties() {
        int i = this.mOrientation * 2;
        ConstraintWidget constraintWidget = this.mFirst;
        ConstraintWidget constraintWidget2 = constraintWidget;
        boolean z = false;
        while (!z) {
            this.mWidgetsCount++;
            constraintWidget.mNextChainWidget[this.mOrientation] = null;
            constraintWidget.mListNextMatchConstraintsWidget[this.mOrientation] = null;
            if (constraintWidget.getVisibility() != 8) {
                if (this.mFirstVisibleWidget == null) {
                    this.mFirstVisibleWidget = constraintWidget;
                }
                this.mLastVisibleWidget = constraintWidget;
                if (constraintWidget.mListDimensionBehaviors[this.mOrientation] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[this.mOrientation] == 0 || constraintWidget.mResolvedMatchConstraintDefault[this.mOrientation] == 3 || constraintWidget.mResolvedMatchConstraintDefault[this.mOrientation] == 2)) {
                    this.mWidgetsMatchCount++;
                    float f = constraintWidget.mWeight[this.mOrientation];
                    if (f > 0.0f) {
                        this.mTotalWeight += constraintWidget.mWeight[this.mOrientation];
                    }
                    if (isMatchConstraintEqualityCandidate(constraintWidget, this.mOrientation)) {
                        if (f < 0.0f) {
                            this.mHasUndefinedWeights = true;
                        } else {
                            this.mHasDefinedWeights = true;
                        }
                        if (this.mWeightedMatchConstraintsWidgets == null) {
                            this.mWeightedMatchConstraintsWidgets = new ArrayList<>();
                        }
                        this.mWeightedMatchConstraintsWidgets.add(constraintWidget);
                    }
                    if (this.mFirstMatchConstraintWidget == null) {
                        this.mFirstMatchConstraintWidget = constraintWidget;
                    }
                    ConstraintWidget constraintWidget3 = this.mLastMatchConstraintWidget;
                    if (constraintWidget3 != null) {
                        constraintWidget3.mListNextMatchConstraintsWidget[this.mOrientation] = constraintWidget;
                    }
                    this.mLastMatchConstraintWidget = constraintWidget;
                }
            }
            if (constraintWidget2 != constraintWidget) {
                constraintWidget2.mNextChainWidget[this.mOrientation] = constraintWidget;
            }
            ConstraintAnchor constraintAnchor = constraintWidget.mListAnchors[i + 1].mTarget;
            ConstraintWidget constraintWidget4 = null;
            if (constraintAnchor != null) {
                ConstraintWidget constraintWidget5 = constraintAnchor.mOwner;
                constraintWidget4 = null;
                if (constraintWidget5.mListAnchors[i].mTarget != null) {
                    constraintWidget4 = constraintWidget5.mListAnchors[i].mTarget.mOwner != constraintWidget ? null : constraintWidget5;
                }
            }
            if (constraintWidget4 == null) {
                constraintWidget4 = constraintWidget;
                z = true;
            }
            ConstraintWidget constraintWidget6 = constraintWidget;
            constraintWidget = constraintWidget4;
            constraintWidget2 = constraintWidget6;
        }
        this.mLast = constraintWidget;
        if (this.mOrientation == 0 && this.mIsRtl) {
            this.mHead = constraintWidget;
        } else {
            this.mHead = this.mFirst;
        }
        boolean z2 = false;
        if (this.mHasDefinedWeights) {
            z2 = false;
            if (this.mHasUndefinedWeights) {
                z2 = true;
            }
        }
        this.mHasComplexMatchWeights = z2;
    }

    private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget constraintWidget, int i) {
        return constraintWidget.getVisibility() != 8 && constraintWidget.mListDimensionBehaviors[i] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (constraintWidget.mResolvedMatchConstraintDefault[i] == 0 || constraintWidget.mResolvedMatchConstraintDefault[i] == 3);
    }

    public void define() {
        if (!this.mDefined) {
            defineChainProperties();
        }
        this.mDefined = true;
    }

    public ConstraintWidget getFirst() {
        return this.mFirst;
    }

    public ConstraintWidget getFirstMatchConstraintWidget() {
        return this.mFirstMatchConstraintWidget;
    }

    public ConstraintWidget getFirstVisibleWidget() {
        return this.mFirstVisibleWidget;
    }

    public ConstraintWidget getHead() {
        return this.mHead;
    }

    public ConstraintWidget getLast() {
        return this.mLast;
    }

    public ConstraintWidget getLastMatchConstraintWidget() {
        return this.mLastMatchConstraintWidget;
    }

    public ConstraintWidget getLastVisibleWidget() {
        return this.mLastVisibleWidget;
    }

    public float getTotalWeight() {
        return this.mTotalWeight;
    }
}
