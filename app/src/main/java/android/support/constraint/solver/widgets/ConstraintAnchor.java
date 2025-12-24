package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ConstraintAnchor.class */
public class ConstraintAnchor {
    private static final boolean ALLOW_BINARY = false;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    ConstraintAnchor mTarget;
    final Type mType;
    private ResolutionAnchor mResolutionAnchor = new ResolutionAnchor(this);
    public int mMargin = 0;
    int mGoneMargin = -1;
    private Strength mStrength = Strength.NONE;
    private ConnectionType mConnectionType = ConnectionType.RELAXED;
    private int mConnectionCreator = 0;

    /* renamed from: android.support.constraint.solver.widgets.ConstraintAnchor$1 */
    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ConstraintAnchor$1.class */
    static /* synthetic */ class C00211 {

        /* renamed from: $SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type */
        static final int[] f4x1d400623;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:35:0x008d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.support.constraint.solver.widgets.ConstraintAnchor$Type[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623 = r0
                r0 = r4
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER     // Catch: java.lang.NoSuchFieldError -> L71
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L71
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L71
            L14:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L71 java.lang.NoSuchFieldError -> L75
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT     // Catch: java.lang.NoSuchFieldError -> L75
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L75
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L75
            L1f:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L75 java.lang.NoSuchFieldError -> L79
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT     // Catch: java.lang.NoSuchFieldError -> L79
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L79
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L79
            L2a:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L79 java.lang.NoSuchFieldError -> L7d
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP     // Catch: java.lang.NoSuchFieldError -> L7d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L7d
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L7d
            L35:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L7d java.lang.NoSuchFieldError -> L81
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM     // Catch: java.lang.NoSuchFieldError -> L81
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L81
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L81
            L40:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L81 java.lang.NoSuchFieldError -> L85
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE     // Catch: java.lang.NoSuchFieldError -> L85
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L85
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L85
            L4c:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L85 java.lang.NoSuchFieldError -> L89
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X     // Catch: java.lang.NoSuchFieldError -> L89
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L89
                r2 = 7
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L89
            L58:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L89 java.lang.NoSuchFieldError -> L8d
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y     // Catch: java.lang.NoSuchFieldError -> L8d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L8d
                r2 = 8
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L8d
            L64:
                int[] r0 = android.support.constraint.solver.widgets.ConstraintAnchor.C00211.f4x1d400623     // Catch: java.lang.NoSuchFieldError -> L8d java.lang.NoSuchFieldError -> L91
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.NONE     // Catch: java.lang.NoSuchFieldError -> L91
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L91
                r2 = 9
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L91
            L70:
                return
            L71:
                r4 = move-exception
                goto L14
            L75:
                r4 = move-exception
                goto L1f
            L79:
                r4 = move-exception
                goto L2a
            L7d:
                r4 = move-exception
                goto L35
            L81:
                r4 = move-exception
                goto L40
            L85:
                r4 = move-exception
                goto L4c
            L89:
                r4 = move-exception
                goto L58
            L8d:
                r4 = move-exception
                goto L64
            L91:
                r4 = move-exception
                goto L70
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintAnchor.C00211.m41clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ConstraintAnchor$ConnectionType.class */
    public enum ConnectionType {
        RELAXED,
        STRICT
    }

    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ConstraintAnchor$Strength.class */
    public enum Strength {
        NONE,
        STRONG,
        WEAK
    }

    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ConstraintAnchor$Type.class */
    public enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.mOwner = constraintWidget;
        this.mType = type;
    }

    private boolean isConnectionToMe(ConstraintWidget constraintWidget, HashSet<ConstraintWidget> hashSet) {
        if (hashSet.contains(constraintWidget)) {
            return false;
        }
        hashSet.add(constraintWidget);
        if (constraintWidget == getOwner()) {
            return true;
        }
        ArrayList<ConstraintAnchor> anchors = constraintWidget.getAnchors();
        int size = anchors.size();
        for (int i = 0; i < size; i++) {
            ConstraintAnchor constraintAnchor = anchors.get(i);
            if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), hashSet)) {
                return true;
            }
        }
        return false;
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int i) {
        return connect(constraintAnchor, i, -1, Strength.STRONG, 0, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int i, int i2) {
        return connect(constraintAnchor, i, -1, Strength.STRONG, i2, false);
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int i, int i2, Strength strength, int i3, boolean z) {
        if (constraintAnchor == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!z && !isValidConnection(constraintAnchor)) {
            return false;
        }
        this.mTarget = constraintAnchor;
        if (i > 0) {
            this.mMargin = i;
        } else {
            this.mMargin = 0;
        }
        this.mGoneMargin = i2;
        this.mStrength = strength;
        this.mConnectionCreator = i3;
        return true;
    }

    public boolean connect(ConstraintAnchor constraintAnchor, int i, Strength strength, int i2) {
        return connect(constraintAnchor, i, -1, strength, i2, false);
    }

    public int getConnectionCreator() {
        return this.mConnectionCreator;
    }

    public ConnectionType getConnectionType() {
        return this.mConnectionType;
    }

    public int getMargin() {
        ConstraintAnchor constraintAnchor;
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        return (this.mGoneMargin <= -1 || (constraintAnchor = this.mTarget) == null || constraintAnchor.mOwner.getVisibility() != 8) ? this.mMargin : this.mGoneMargin;
    }

    public final ConstraintAnchor getOpposite() {
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
            case 2:
                return this.mOwner.mRight;
            case 3:
                return this.mOwner.mLeft;
            case 4:
                return this.mOwner.mBottom;
            case 5:
                return this.mOwner.mTop;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public ConstraintWidget getOwner() {
        return this.mOwner;
    }

    public int getPriorityLevel() {
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
                return 1;
            case 7:
            case 8:
            case 9:
                return 0;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public ResolutionAnchor getResolutionNode() {
        return this.mResolutionAnchor;
    }

    public int getSnapPriorityLevel() {
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
                return 3;
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
                return 0;
            case 6:
                return 2;
            case 7:
                return 0;
            case 8:
                return 1;
            case 9:
                return 0;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }

    public Strength getStrength() {
        return this.mStrength;
    }

    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }

    public Type getType() {
        return this.mType;
    }

    public boolean isConnected() {
        return this.mTarget != null;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget) {
        if (isConnectionToMe(constraintWidget, new HashSet<>())) {
            return false;
        }
        ConstraintWidget parent = getOwner().getParent();
        return parent == constraintWidget || constraintWidget.getParent() == parent;
    }

    public boolean isConnectionAllowed(ConstraintWidget constraintWidget, ConstraintAnchor constraintAnchor) {
        return isConnectionAllowed(constraintWidget);
    }

    public boolean isSideAnchor() {
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return false;
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isSimilarDimensionConnection(ConstraintAnchor constraintAnchor) {
        Type type = constraintAnchor.getType();
        boolean z = true;
        if (type == this.mType) {
            return true;
        }
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
                if (type == Type.BASELINE) {
                    z = false;
                }
                return z;
            case 2:
            case 3:
            case 7:
                boolean z2 = true;
                if (type != Type.LEFT) {
                    z2 = true;
                    if (type != Type.RIGHT) {
                        z2 = type == Type.CENTER_X;
                    }
                }
                return z2;
            case 4:
            case 5:
            case 6:
            case 8:
                boolean z3 = true;
                if (type != Type.TOP) {
                    z3 = true;
                    if (type != Type.BOTTOM) {
                        z3 = true;
                        if (type != Type.CENTER_Y) {
                            z3 = type == Type.BASELINE;
                        }
                    }
                }
                return z3;
            case 9:
                return false;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public boolean isSnapCompatibleWith(ConstraintAnchor constraintAnchor) {
        if (this.mType == Type.CENTER) {
            return false;
        }
        if (this.mType == constraintAnchor.getType()) {
            return true;
        }
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
            case 6:
            case 9:
                return false;
            case 2:
                int i = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i == 3 || i == 7;
            case 3:
                int i2 = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i2 == 2 || i2 == 7;
            case 4:
                int i3 = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i3 == 5 || i3 == 8;
            case 5:
                int i4 = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i4 == 4 || i4 == 8;
            case 7:
                int i5 = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i5 == 2 || i5 == 3;
            case 8:
                int i6 = C00211.f4x1d400623[constraintAnchor.getType().ordinal()];
                return i6 == 4 || i6 == 5;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0101  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isValidConnection(android.support.constraint.solver.widgets.ConstraintAnchor r5) {
        /*
            Method dump skipped, instructions count: 297
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintAnchor.isValidConnection(android.support.constraint.solver.widgets.ConstraintAnchor):boolean");
    }

    public boolean isVerticalAnchor() {
        switch (C00211.f4x1d400623[this.mType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 7:
                return false;
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
                return true;
            default:
                throw new AssertionError(this.mType.name());
        }
    }

    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.STRONG;
        this.mConnectionCreator = 0;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mResolutionAnchor.reset();
    }

    public void resetSolverVariable(Cache cache) {
        SolverVariable solverVariable = this.mSolverVariable;
        if (solverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, (String) null);
        } else {
            solverVariable.reset();
        }
    }

    public void setConnectionCreator(int i) {
        this.mConnectionCreator = i;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.mConnectionType = connectionType;
    }

    public void setGoneMargin(int i) {
        if (isConnected()) {
            this.mGoneMargin = i;
        }
    }

    public void setMargin(int i) {
        if (isConnected()) {
            this.mMargin = i;
        }
    }

    public void setStrength(Strength strength) {
        if (isConnected()) {
            this.mStrength = strength;
        }
    }

    public String toString() {
        return this.mOwner.getDebugName() + ":" + this.mType.toString();
    }
}
