package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/ResolutionAnchor.class */
public class ResolutionAnchor extends ResolutionNode {
    public static final int BARRIER_CONNECTION = 5;
    public static final int CENTER_CONNECTION = 2;
    public static final int CHAIN_CONNECTION = 4;
    public static final int DIRECT_CONNECTION = 1;
    public static final int MATCH_CONNECTION = 3;
    public static final int UNCONNECTED = 0;
    float computedValue;
    ConstraintAnchor myAnchor;
    float offset;
    private ResolutionAnchor opposite;
    private float oppositeOffset;
    float resolvedOffset;
    ResolutionAnchor resolvedTarget;
    ResolutionAnchor target;
    int type = 0;
    private ResolutionDimension dimension = null;
    private int dimensionMultiplier = 1;
    private ResolutionDimension oppositeDimension = null;
    private int oppositeDimensionMultiplier = 1;

    public ResolutionAnchor(ConstraintAnchor constraintAnchor) {
        this.myAnchor = constraintAnchor;
    }

    void addResolvedValue(LinearSystem linearSystem) {
        SolverVariable solverVariable = this.myAnchor.getSolverVariable();
        ResolutionAnchor resolutionAnchor = this.resolvedTarget;
        if (resolutionAnchor == null) {
            linearSystem.addEquality(solverVariable, (int) (this.resolvedOffset + 0.5f));
        } else {
            linearSystem.addEquality(solverVariable, linearSystem.createObjectVariable(resolutionAnchor.myAnchor), (int) (this.resolvedOffset + 0.5f), 6);
        }
    }

    public void dependsOn(int i, ResolutionAnchor resolutionAnchor, int i2) {
        this.type = i;
        this.target = resolutionAnchor;
        this.offset = i2;
        resolutionAnchor.addDependent(this);
    }

    public void dependsOn(ResolutionAnchor resolutionAnchor, int i) {
        this.target = resolutionAnchor;
        this.offset = i;
        resolutionAnchor.addDependent(this);
    }

    public void dependsOn(ResolutionAnchor resolutionAnchor, int i, ResolutionDimension resolutionDimension) {
        this.target = resolutionAnchor;
        resolutionAnchor.addDependent(this);
        this.dimension = resolutionDimension;
        this.dimensionMultiplier = i;
        resolutionDimension.addDependent(this);
    }

    public float getResolvedValue() {
        return this.resolvedOffset;
    }

    @Override // android.support.constraint.solver.widgets.ResolutionNode
    public void remove(ResolutionDimension resolutionDimension) {
        ResolutionDimension resolutionDimension2 = this.dimension;
        if (resolutionDimension2 == resolutionDimension) {
            this.dimension = null;
            this.offset = this.dimensionMultiplier;
        } else if (resolutionDimension2 == this.oppositeDimension) {
            this.oppositeDimension = null;
            this.oppositeOffset = this.oppositeDimensionMultiplier;
        }
        resolve();
    }

    @Override // android.support.constraint.solver.widgets.ResolutionNode
    public void reset() {
        super.reset();
        this.target = null;
        this.offset = 0.0f;
        this.dimension = null;
        this.dimensionMultiplier = 1;
        this.oppositeDimension = null;
        this.oppositeDimensionMultiplier = 1;
        this.resolvedTarget = null;
        this.resolvedOffset = 0.0f;
        this.computedValue = 0.0f;
        this.opposite = null;
        this.oppositeOffset = 0.0f;
        this.type = 0;
    }

    @Override // android.support.constraint.solver.widgets.ResolutionNode
    public void resolve() {
        ResolutionAnchor resolutionAnchor;
        ResolutionAnchor resolutionAnchor2;
        ResolutionAnchor resolutionAnchor3;
        ResolutionAnchor resolutionAnchor4;
        ResolutionAnchor resolutionAnchor5;
        ResolutionAnchor resolutionAnchor6;
        float f;
        float f2;
        float width;
        float f3;
        ResolutionAnchor resolutionAnchor7;
        if (this.state == 1 || this.type == 4) {
            return;
        }
        ResolutionDimension resolutionDimension = this.dimension;
        if (resolutionDimension != null) {
            if (resolutionDimension.state != 1) {
                return;
            } else {
                this.offset = this.dimensionMultiplier * this.dimension.value;
            }
        }
        ResolutionDimension resolutionDimension2 = this.oppositeDimension;
        if (resolutionDimension2 != null) {
            if (resolutionDimension2.state != 1) {
                return;
            } else {
                this.oppositeOffset = this.oppositeDimensionMultiplier * this.oppositeDimension.value;
            }
        }
        if (this.type == 1 && ((resolutionAnchor7 = this.target) == null || resolutionAnchor7.state == 1)) {
            ResolutionAnchor resolutionAnchor8 = this.target;
            if (resolutionAnchor8 == null) {
                this.resolvedTarget = this;
                this.resolvedOffset = this.offset;
            } else {
                this.resolvedTarget = resolutionAnchor8.resolvedTarget;
                this.resolvedOffset = resolutionAnchor8.resolvedOffset + this.offset;
            }
            didResolve();
            return;
        }
        if (this.type != 2 || (resolutionAnchor4 = this.target) == null || resolutionAnchor4.state != 1 || (resolutionAnchor5 = this.opposite) == null || (resolutionAnchor6 = resolutionAnchor5.target) == null || resolutionAnchor6.state != 1) {
            if (this.type != 3 || (resolutionAnchor = this.target) == null || resolutionAnchor.state != 1 || (resolutionAnchor2 = this.opposite) == null || (resolutionAnchor3 = resolutionAnchor2.target) == null || resolutionAnchor3.state != 1) {
                if (this.type == 5) {
                    this.myAnchor.mOwner.resolve();
                    return;
                }
                return;
            }
            if (LinearSystem.getMetrics() != null) {
                LinearSystem.getMetrics().matchConnectionResolved++;
            }
            ResolutionAnchor resolutionAnchor9 = this.target;
            this.resolvedTarget = resolutionAnchor9.resolvedTarget;
            ResolutionAnchor resolutionAnchor10 = this.opposite;
            ResolutionAnchor resolutionAnchor11 = resolutionAnchor10.target;
            resolutionAnchor10.resolvedTarget = resolutionAnchor11.resolvedTarget;
            this.resolvedOffset = resolutionAnchor9.resolvedOffset + this.offset;
            resolutionAnchor10.resolvedOffset = resolutionAnchor11.resolvedOffset + resolutionAnchor10.offset;
            didResolve();
            this.opposite.didResolve();
            return;
        }
        if (LinearSystem.getMetrics() != null) {
            LinearSystem.getMetrics().centerConnectionResolved++;
        }
        this.resolvedTarget = this.target.resolvedTarget;
        ResolutionAnchor resolutionAnchor12 = this.opposite;
        resolutionAnchor12.resolvedTarget = resolutionAnchor12.target.resolvedTarget;
        int i = 0;
        boolean z = true;
        if (this.myAnchor.mType != ConstraintAnchor.Type.RIGHT) {
            z = this.myAnchor.mType == ConstraintAnchor.Type.BOTTOM;
        }
        if (z) {
            f = this.target.resolvedOffset;
            f2 = this.opposite.target.resolvedOffset;
        } else {
            f = this.opposite.target.resolvedOffset;
            f2 = this.target.resolvedOffset;
        }
        float f4 = f - f2;
        if (this.myAnchor.mType == ConstraintAnchor.Type.LEFT || this.myAnchor.mType == ConstraintAnchor.Type.RIGHT) {
            width = f4 - this.myAnchor.mOwner.getWidth();
            f3 = this.myAnchor.mOwner.mHorizontalBiasPercent;
        } else {
            width = f4 - this.myAnchor.mOwner.getHeight();
            f3 = this.myAnchor.mOwner.mVerticalBiasPercent;
        }
        int margin = this.myAnchor.getMargin();
        int margin2 = this.opposite.myAnchor.getMargin();
        if (this.myAnchor.getTarget() == this.opposite.myAnchor.getTarget()) {
            f3 = 0.5f;
            margin2 = 0;
        } else {
            i = margin;
        }
        float f5 = i;
        float f6 = margin2;
        float f7 = (width - f5) - f6;
        if (z) {
            ResolutionAnchor resolutionAnchor13 = this.opposite;
            resolutionAnchor13.resolvedOffset = resolutionAnchor13.target.resolvedOffset + f6 + (f7 * f3);
            this.resolvedOffset = (this.target.resolvedOffset - f5) - (f7 * (1.0f - f3));
        } else {
            this.resolvedOffset = this.target.resolvedOffset + f5 + (f7 * f3);
            ResolutionAnchor resolutionAnchor14 = this.opposite;
            resolutionAnchor14.resolvedOffset = (resolutionAnchor14.target.resolvedOffset - f6) - (f7 * (1.0f - f3));
        }
        didResolve();
        this.opposite.didResolve();
    }

    public void resolve(ResolutionAnchor resolutionAnchor, float f) {
        if (this.state == 0 || !(this.resolvedTarget == resolutionAnchor || this.resolvedOffset == f)) {
            this.resolvedTarget = resolutionAnchor;
            this.resolvedOffset = f;
            if (this.state == 1) {
                invalidate();
            }
            didResolve();
        }
    }

    String sType(int i) {
        return i == 1 ? "DIRECT" : i == 2 ? "CENTER" : i == 3 ? "MATCH" : i == 4 ? "CHAIN" : i == 5 ? "BARRIER" : "UNCONNECTED";
    }

    public void setOpposite(ResolutionAnchor resolutionAnchor, float f) {
        this.opposite = resolutionAnchor;
        this.oppositeOffset = f;
    }

    public void setOpposite(ResolutionAnchor resolutionAnchor, int i, ResolutionDimension resolutionDimension) {
        this.opposite = resolutionAnchor;
        this.oppositeDimension = resolutionDimension;
        this.oppositeDimensionMultiplier = i;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String toString() {
        if (this.state != 1) {
            return "{ " + this.myAnchor + " UNRESOLVED} type: " + sType(this.type);
        }
        if (this.resolvedTarget == this) {
            return "[" + this.myAnchor + ", RESOLVED: " + this.resolvedOffset + "]  type: " + sType(this.type);
        }
        return "[" + this.myAnchor + ", RESOLVED: " + this.resolvedTarget + ":" + this.resolvedOffset + "] type: " + sType(this.type);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void update() {
        /*
            r4 = this;
            r0 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.myAnchor
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            r7 = r0
            r0 = r7
            if (r0 != 0) goto Ld
            return
        Ld:
            r0 = r7
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            r1 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r1 = r1.myAnchor
            if (r0 != r1) goto L25
            r0 = r4
            r1 = 4
            r0.type = r1
            r0 = r7
            android.support.constraint.solver.widgets.ResolutionAnchor r0 = r0.getResolutionNode()
            r1 = 4
            r0.type = r1
        L25:
            r0 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.myAnchor
            int r0 = r0.getMargin()
            r6 = r0
            r0 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.myAnchor
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r0 = r0.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            if (r0 == r1) goto L49
            r0 = r6
            r5 = r0
            r0 = r4
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.myAnchor
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r0 = r0.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r0 != r1) goto L4c
        L49:
            r0 = r6
            int r0 = -r0
            r5 = r0
        L4c:
            r0 = r4
            r1 = r7
            android.support.constraint.solver.widgets.ResolutionAnchor r1 = r1.getResolutionNode()
            r2 = r5
            r0.dependsOn(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ResolutionAnchor.update():void");
    }
}
