package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/Chain.class */
class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        int i3;
        ChainHead[] chainHeadArr;
        if (i == 0) {
            i3 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i2 = 0;
        } else {
            i2 = 2;
            i3 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            ChainHead chainHead = chainHeadArr[i4];
            chainHead.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0370  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00bc A[PHI: r18 r19
  0x00bc: PHI (r18v3 boolean) = (r18v1 boolean), (r18v23 boolean) binds: [B:28:0x00b9, B:17:0x0082] A[DONT_GENERATE, DONT_INLINE]
  0x00bc: PHI (r19v3 boolean) = (r19v1 boolean), (r19v25 boolean) binds: [B:28:0x00b9, B:17:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c6 A[PHI: r20 r21
  0x00c6: PHI (r20v4 boolean) = (r20v0 boolean), (r20v5 boolean) binds: [B:28:0x00b9, B:17:0x0082] A[DONT_GENERATE, DONT_INLINE]
  0x00c6: PHI (r21v2 boolean) = (r21v0 boolean), (r21v3 boolean) binds: [B:28:0x00b9, B:17:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer r10, android.support.constraint.solver.LinearSystem r11, int r12, int r13, android.support.constraint.solver.widgets.ChainHead r14) {
        /*
            Method dump skipped, instructions count: 2518
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Chain.applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):void");
    }
}
