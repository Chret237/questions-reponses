package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/Guideline.class */
public class Guideline extends ConstraintWidget {
    public static final int HORIZONTAL = 0;
    public static final int RELATIVE_BEGIN = 1;
    public static final int RELATIVE_END = 2;
    public static final int RELATIVE_PERCENT = 0;
    public static final int RELATIVE_UNKNWON = -1;
    public static final int VERTICAL = 1;
    protected float mRelativePercent = -1.0f;
    protected int mRelativeBegin = -1;
    protected int mRelativeEnd = -1;
    private ConstraintAnchor mAnchor = this.mTop;
    private int mOrientation = 0;
    private boolean mIsPositionRelaxed = false;
    private int mMinimumPosition = 0;
    private Rectangle mHead = new Rectangle();
    private int mHeadSize = 8;

    /* renamed from: android.support.constraint.solver.widgets.Guideline$1 */
    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/Guideline$1.class */
    static /* synthetic */ class C00231 {

        /* renamed from: $SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type */
        static final int[] f9x1d400623;

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
                android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623 = r0
                r0 = r4
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT     // Catch: java.lang.NoSuchFieldError -> L71
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L71
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L71
            L14:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L71 java.lang.NoSuchFieldError -> L75
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT     // Catch: java.lang.NoSuchFieldError -> L75
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L75
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L75
            L1f:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L75 java.lang.NoSuchFieldError -> L79
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP     // Catch: java.lang.NoSuchFieldError -> L79
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L79
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L79
            L2a:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L79 java.lang.NoSuchFieldError -> L7d
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM     // Catch: java.lang.NoSuchFieldError -> L7d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L7d
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L7d
            L35:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L7d java.lang.NoSuchFieldError -> L81
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BASELINE     // Catch: java.lang.NoSuchFieldError -> L81
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L81
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L81
            L40:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L81 java.lang.NoSuchFieldError -> L85
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER     // Catch: java.lang.NoSuchFieldError -> L85
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L85
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L85
            L4c:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L85 java.lang.NoSuchFieldError -> L89
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_X     // Catch: java.lang.NoSuchFieldError -> L89
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L89
                r2 = 7
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L89
            L58:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L89 java.lang.NoSuchFieldError -> L8d
                android.support.constraint.solver.widgets.ConstraintAnchor$Type r1 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.CENTER_Y     // Catch: java.lang.NoSuchFieldError -> L8d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L8d
                r2 = 8
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L8d
            L64:
                int[] r0 = android.support.constraint.solver.widgets.Guideline.C00231.f9x1d400623     // Catch: java.lang.NoSuchFieldError -> L8d java.lang.NoSuchFieldError -> L91
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
            throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Guideline.C00231.m50clinit():void");
        }
    }

    public Guideline() {
        this.mAnchors.clear();
        this.mAnchors.add(this.mAnchor);
        int length = this.mListAnchors.length;
        for (int i = 0; i < length; i++) {
            this.mListAnchors[i] = this.mAnchor;
        }
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void addToSolver(LinearSystem linearSystem) {
        ConstraintWidgetContainer constraintWidgetContainer = (ConstraintWidgetContainer) getParent();
        if (constraintWidgetContainer == null) {
            return;
        }
        ConstraintAnchor anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.RIGHT);
        boolean z = this.mParent != null && this.mParent.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (this.mOrientation == 0) {
            anchor = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.TOP);
            anchor2 = constraintWidgetContainer.getAnchor(ConstraintAnchor.Type.BOTTOM);
            z = this.mParent != null && this.mParent.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        }
        if (this.mRelativeBegin != -1) {
            SolverVariable solverVariableCreateObjectVariable = linearSystem.createObjectVariable(this.mAnchor);
            linearSystem.addEquality(solverVariableCreateObjectVariable, linearSystem.createObjectVariable(anchor), this.mRelativeBegin, 6);
            if (z) {
                linearSystem.addGreaterThan(linearSystem.createObjectVariable(anchor2), solverVariableCreateObjectVariable, 0, 5);
                return;
            }
            return;
        }
        if (this.mRelativeEnd == -1) {
            if (this.mRelativePercent != -1.0f) {
                linearSystem.addConstraint(LinearSystem.createRowDimensionPercent(linearSystem, linearSystem.createObjectVariable(this.mAnchor), linearSystem.createObjectVariable(anchor), linearSystem.createObjectVariable(anchor2), this.mRelativePercent, this.mIsPositionRelaxed));
                return;
            }
            return;
        }
        SolverVariable solverVariableCreateObjectVariable2 = linearSystem.createObjectVariable(this.mAnchor);
        SolverVariable solverVariableCreateObjectVariable3 = linearSystem.createObjectVariable(anchor2);
        linearSystem.addEquality(solverVariableCreateObjectVariable2, solverVariableCreateObjectVariable3, -this.mRelativeEnd, 6);
        if (z) {
            linearSystem.addGreaterThan(solverVariableCreateObjectVariable2, linearSystem.createObjectVariable(anchor), 0, 5);
            linearSystem.addGreaterThan(solverVariableCreateObjectVariable3, solverVariableCreateObjectVariable2, 0, 5);
        }
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public boolean allowedInBarrier() {
        return true;
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void analyze(int i) {
        ConstraintWidget parent = getParent();
        if (parent == null) {
            return;
        }
        if (getOrientation() == 1) {
            this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), 0);
            this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), 0);
            if (this.mRelativeBegin != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), this.mRelativeBegin);
                this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), this.mRelativeBegin);
                return;
            } else if (this.mRelativeEnd != -1) {
                this.mLeft.getResolutionNode().dependsOn(1, parent.mRight.getResolutionNode(), -this.mRelativeEnd);
                this.mRight.getResolutionNode().dependsOn(1, parent.mRight.getResolutionNode(), -this.mRelativeEnd);
                return;
            } else {
                if (this.mRelativePercent == -1.0f || parent.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
                    return;
                }
                int i2 = (int) (parent.mWidth * this.mRelativePercent);
                this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), i2);
                this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), i2);
                return;
            }
        }
        this.mLeft.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), 0);
        this.mRight.getResolutionNode().dependsOn(1, parent.mLeft.getResolutionNode(), 0);
        if (this.mRelativeBegin != -1) {
            this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), this.mRelativeBegin);
            this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), this.mRelativeBegin);
        } else if (this.mRelativeEnd != -1) {
            this.mTop.getResolutionNode().dependsOn(1, parent.mBottom.getResolutionNode(), -this.mRelativeEnd);
            this.mBottom.getResolutionNode().dependsOn(1, parent.mBottom.getResolutionNode(), -this.mRelativeEnd);
        } else {
            if (this.mRelativePercent == -1.0f || parent.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.FIXED) {
                return;
            }
            int i3 = (int) (parent.mHeight * this.mRelativePercent);
            this.mTop.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), i3);
            this.mBottom.getResolutionNode().dependsOn(1, parent.mTop.getResolutionNode(), i3);
        }
    }

    public void cyclePosition() {
        if (this.mRelativeBegin != -1) {
            inferRelativePercentPosition();
        } else if (this.mRelativePercent != -1.0f) {
            inferRelativeEndPosition();
        } else if (this.mRelativeEnd != -1) {
            inferRelativeBeginPosition();
        }
    }

    public ConstraintAnchor getAnchor() {
        return this.mAnchor;
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (C00231.f9x1d400623[type.ordinal()]) {
            case 1:
            case 2:
                if (this.mOrientation == 1) {
                    return this.mAnchor;
                }
                break;
            case 3:
            case 4:
                if (this.mOrientation == 0) {
                    return this.mAnchor;
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
        }
        throw new AssertionError(type.name());
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public Rectangle getHead() {
        Rectangle rectangle = this.mHead;
        int drawX = getDrawX();
        int i = this.mHeadSize;
        int drawY = getDrawY();
        int i2 = this.mHeadSize;
        rectangle.setBounds(drawX - i, drawY - (i2 * 2), i2 * 2, i2 * 2);
        if (getOrientation() == 0) {
            Rectangle rectangle2 = this.mHead;
            int drawX2 = getDrawX();
            int i3 = this.mHeadSize;
            int drawY2 = getDrawY();
            int i4 = this.mHeadSize;
            rectangle2.setBounds(drawX2 - (i3 * 2), drawY2 - i4, i4 * 2, i4 * 2);
        }
        return this.mHead;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getRelativeBegin() {
        return this.mRelativeBegin;
    }

    public int getRelativeBehaviour() {
        if (this.mRelativePercent != -1.0f) {
            return 0;
        }
        if (this.mRelativeBegin != -1) {
            return 1;
        }
        return this.mRelativeEnd != -1 ? 2 : -1;
    }

    public int getRelativeEnd() {
        return this.mRelativeEnd;
    }

    public float getRelativePercent() {
        return this.mRelativePercent;
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public String getType() {
        return "Guideline";
    }

    void inferRelativeBeginPosition() {
        int x = getX();
        if (this.mOrientation == 0) {
            x = getY();
        }
        setGuideBegin(x);
    }

    void inferRelativeEndPosition() {
        int width = getParent().getWidth() - getX();
        if (this.mOrientation == 0) {
            width = getParent().getHeight() - getY();
        }
        setGuideEnd(width);
    }

    void inferRelativePercentPosition() {
        float x = getX() / getParent().getWidth();
        if (this.mOrientation == 0) {
            x = getY() / getParent().getHeight();
        }
        setGuidePercent(x);
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void setDrawOrigin(int i, int i2) {
        if (this.mOrientation == 1) {
            int i3 = i - this.mOffsetX;
            if (this.mRelativeBegin != -1) {
                setGuideBegin(i3);
                return;
            } else if (this.mRelativeEnd != -1) {
                setGuideEnd(getParent().getWidth() - i3);
                return;
            } else {
                if (this.mRelativePercent != -1.0f) {
                    setGuidePercent(i3 / getParent().getWidth());
                    return;
                }
                return;
            }
        }
        int i4 = i2 - this.mOffsetY;
        if (this.mRelativeBegin != -1) {
            setGuideBegin(i4);
        } else if (this.mRelativeEnd != -1) {
            setGuideEnd(getParent().getHeight() - i4);
        } else if (this.mRelativePercent != -1.0f) {
            setGuidePercent(i4 / getParent().getHeight());
        }
    }

    public void setGuideBegin(int i) {
        if (i > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = i;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuideEnd(int i) {
        if (i > -1) {
            this.mRelativePercent = -1.0f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = i;
        }
    }

    public void setGuidePercent(float f) {
        if (f > -1.0f) {
            this.mRelativePercent = f;
            this.mRelativeBegin = -1;
            this.mRelativeEnd = -1;
        }
    }

    public void setGuidePercent(int i) {
        setGuidePercent(i / 100.0f);
    }

    public void setMinimumPosition(int i) {
        this.mMinimumPosition = i;
    }

    public void setOrientation(int i) {
        if (this.mOrientation == i) {
            return;
        }
        this.mOrientation = i;
        this.mAnchors.clear();
        if (this.mOrientation == 1) {
            this.mAnchor = this.mLeft;
        } else {
            this.mAnchor = this.mTop;
        }
        this.mAnchors.add(this.mAnchor);
        int length = this.mListAnchors.length;
        for (int i2 = 0; i2 < length; i2++) {
            this.mListAnchors[i2] = this.mAnchor;
        }
    }

    public void setPositionRelaxed(boolean z) {
        if (this.mIsPositionRelaxed == z) {
            return;
        }
        this.mIsPositionRelaxed = z;
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void updateFromSolver(LinearSystem linearSystem) {
        if (getParent() == null) {
            return;
        }
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mAnchor);
        if (this.mOrientation == 1) {
            setX(objectVariableValue);
            setY(0);
            setHeight(getParent().getHeight());
            setWidth(0);
            return;
        }
        setX(0);
        setY(objectVariableValue);
        setWidth(getParent().getWidth());
        setHeight(0);
    }
}
