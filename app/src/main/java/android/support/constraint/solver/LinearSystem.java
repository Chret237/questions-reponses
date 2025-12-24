package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import com.github.clans.fab.BuildConfig;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/LinearSystem.class */
public class LinearSystem {
    private static final boolean DEBUG = false;
    public static final boolean FULL_DEBUG = false;
    private static int POOL_SIZE = 1000;
    public static Metrics sMetrics;
    final Cache mCache;
    private Row mGoal;
    ArrayRow[] mRows;
    private final Row mTempGoal;
    int mVariablesID = 0;
    private HashMap<String, SolverVariable> mVariables = null;
    private int TABLE_SIZE = 32;
    private int mMaxColumns = 32;
    public boolean graphOptimizer = false;
    private boolean[] mAlreadyTestedCandidates = new boolean[32];
    int mNumColumns = 1;
    int mNumRows = 0;
    private int mMaxRows = 32;
    private SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
    private int mPoolVariablesCount = 0;
    private ArrayRow[] tempClientsCopy = new ArrayRow[32];

    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/LinearSystem$Row.class */
    interface Row {
        void addError(SolverVariable solverVariable);

        void clear();

        SolverVariable getKey();

        SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr);

        void initFromRow(Row row);

        boolean isEmpty();
    }

    public LinearSystem() {
        this.mRows = null;
        this.mRows = new ArrayRow[32];
        releaseRows();
        Cache cache = new Cache();
        this.mCache = cache;
        this.mGoal = new GoalRow(cache);
        this.mTempGoal = new ArrayRow(this.mCache);
    }

    private SolverVariable acquireSolverVariable(SolverVariable.Type type, String str) {
        SolverVariable solverVariable;
        SolverVariable solverVariableAcquire = this.mCache.solverVariablePool.acquire();
        if (solverVariableAcquire == null) {
            SolverVariable solverVariable2 = new SolverVariable(type, str);
            solverVariable2.setType(type, str);
            solverVariable = solverVariable2;
        } else {
            solverVariableAcquire.reset();
            solverVariableAcquire.setType(type, str);
            solverVariable = solverVariableAcquire;
        }
        int i = this.mPoolVariablesCount;
        int i2 = POOL_SIZE;
        if (i >= i2) {
            int i3 = i2 * 2;
            POOL_SIZE = i3;
            this.mPoolVariables = (SolverVariable[]) Arrays.copyOf(this.mPoolVariables, i3);
        }
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i4 = this.mPoolVariablesCount;
        this.mPoolVariablesCount = i4 + 1;
        solverVariableArr[i4] = solverVariable;
        return solverVariable;
    }

    private void addError(ArrayRow arrayRow) {
        arrayRow.addError(this, 0);
    }

    private final void addRow(ArrayRow arrayRow) {
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = arrayRow;
        arrayRow.variable.definitionId = this.mNumRows;
        this.mNumRows++;
        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
    }

    private void addSingleError(ArrayRow arrayRow, int i) {
        addSingleError(arrayRow, i, 0);
    }

    private void computeValues() {
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public static ArrayRow createRowCentering(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int i, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int i2, boolean z) {
        ArrayRow arrayRowCreateRow = linearSystem.createRow();
        arrayRowCreateRow.createRowCentering(solverVariable, solverVariable2, i, f, solverVariable3, solverVariable4, i2);
        if (z) {
            arrayRowCreateRow.addError(linearSystem, 4);
        }
        return arrayRowCreateRow;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, float f, boolean z) {
        ArrayRow arrayRowCreateRow = linearSystem.createRow();
        if (z) {
            linearSystem.addError(arrayRowCreateRow);
        }
        return arrayRowCreateRow.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, f);
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int i, boolean z) {
        ArrayRow arrayRowCreateRow = linearSystem.createRow();
        arrayRowCreateRow.createRowEquals(solverVariable, solverVariable2, i);
        if (z) {
            linearSystem.addSingleError(arrayRowCreateRow, 1);
        }
        return arrayRowCreateRow;
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int i, boolean z) {
        SolverVariable solverVariableCreateSlackVariable = linearSystem.createSlackVariable();
        ArrayRow arrayRowCreateRow = linearSystem.createRow();
        arrayRowCreateRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, i);
        if (z) {
            linearSystem.addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)));
        }
        return arrayRowCreateRow;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable solverVariable, SolverVariable solverVariable2, int i, boolean z) {
        SolverVariable solverVariableCreateSlackVariable = linearSystem.createSlackVariable();
        ArrayRow arrayRowCreateRow = linearSystem.createRow();
        arrayRowCreateRow.createRowLowerThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, i);
        if (z) {
            linearSystem.addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)));
        }
        return arrayRowCreateRow;
    }

    private SolverVariable createVariable(String str, SolverVariable.Type type) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.variables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable solverVariableAcquireSolverVariable = acquireSolverVariable(type, null);
        solverVariableAcquireSolverVariable.setName(str);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        solverVariableAcquireSolverVariable.f3id = i;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        this.mVariables.put(str, solverVariableAcquireSolverVariable);
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariableAcquireSolverVariable;
        return solverVariableAcquireSolverVariable;
    }

    private void displayRows() {
        displaySolverVariables();
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < this.mNumRows; i++) {
            str = (str + this.mRows[i]) + "\n";
        }
        System.out.println(str + this.mGoal + "\n");
    }

    private void displaySolverVariables() {
        System.out.println("Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ")\n");
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x0163  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int enforceBFS(android.support.constraint.solver.LinearSystem.Row r7) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 530
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.LinearSystem.enforceBFS(android.support.constraint.solver.LinearSystem$Row):int");
    }

    private String getDisplaySize(int i) {
        int i2 = i * 4;
        int i3 = i2 / 1024;
        int i4 = i3 / 1024;
        if (i4 > 0) {
            return BuildConfig.FLAVOR + i4 + " Mb";
        }
        if (i3 > 0) {
            return BuildConfig.FLAVOR + i3 + " Kb";
        }
        return BuildConfig.FLAVOR + i2 + " bytes";
    }

    private String getDisplayStrength(int i) {
        return i == 1 ? "LOW" : i == 2 ? "MEDIUM" : i == 3 ? "HIGH" : i == 4 ? "HIGHEST" : i == 5 ? "EQUALITY" : i == 6 ? "FIXED" : "NONE";
    }

    public static Metrics getMetrics() {
        return sMetrics;
    }

    private void increaseTableSize() {
        int i = this.TABLE_SIZE * 2;
        this.TABLE_SIZE = i;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, i);
        Cache cache = this.mCache;
        cache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
        int i2 = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[i2];
        this.mMaxColumns = i2;
        this.mMaxRows = i2;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.tableSizeIncrease++;
            Metrics metrics2 = sMetrics;
            metrics2.maxTableSize = Math.max(metrics2.maxTableSize, this.TABLE_SIZE);
            Metrics metrics3 = sMetrics;
            metrics3.lastTableSize = metrics3.maxTableSize;
        }
    }

    private final int optimize(Row row, boolean z) {
        int i;
        float f;
        int i2;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.optimize++;
        }
        for (int i3 = 0; i3 < this.mNumColumns; i3++) {
            this.mAlreadyTestedCandidates[i3] = false;
        }
        boolean z2 = false;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (z2) {
                return i5;
            }
            Metrics metrics2 = sMetrics;
            if (metrics2 != null) {
                metrics2.iterations++;
            }
            int i6 = i5 + 1;
            if (i6 >= this.mNumColumns * 2) {
                return i6;
            }
            if (row.getKey() != null) {
                this.mAlreadyTestedCandidates[row.getKey().f3id] = true;
            }
            SolverVariable pivotCandidate = row.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.f3id]) {
                    return i6;
                }
                this.mAlreadyTestedCandidates[pivotCandidate.f3id] = true;
            }
            if (pivotCandidate != null) {
                float f2 = Float.MAX_VALUE;
                int i7 = 0;
                int i8 = -1;
                while (true) {
                    i = i8;
                    if (i7 >= this.mNumRows) {
                        break;
                    }
                    ArrayRow arrayRow = this.mRows[i7];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        f = f2;
                        i2 = i;
                    } else if (arrayRow.isSimpleDefinition) {
                        f = f2;
                        i2 = i;
                    } else {
                        f = f2;
                        i2 = i;
                        if (arrayRow.hasVariable(pivotCandidate)) {
                            float f3 = arrayRow.variables.get(pivotCandidate);
                            f = f2;
                            i2 = i;
                            if (f3 < 0.0f) {
                                float f4 = (-arrayRow.constantValue) / f3;
                                f = f2;
                                i2 = i;
                                if (f4 < f2) {
                                    i2 = i7;
                                    f = f4;
                                }
                            }
                        }
                    }
                    i7++;
                    f2 = f;
                    i8 = i2;
                }
                if (i > -1) {
                    ArrayRow arrayRow2 = this.mRows[i];
                    arrayRow2.variable.definitionId = -1;
                    Metrics metrics3 = sMetrics;
                    if (metrics3 != null) {
                        metrics3.pivots++;
                    }
                    arrayRow2.pivot(pivotCandidate);
                    arrayRow2.variable.definitionId = i;
                    arrayRow2.variable.updateReferencesWithNewDefinition(arrayRow2);
                    i4 = i6;
                }
            }
            z2 = true;
            i4 = i6;
        }
    }

    private void releaseRows() {
        int i = 0;
        while (true) {
            ArrayRow[] arrayRowArr = this.mRows;
            if (i >= arrayRowArr.length) {
                return;
            }
            ArrayRow arrayRow = arrayRowArr[i];
            if (arrayRow != null) {
                this.mCache.arrayRowPool.release(arrayRow);
            }
            this.mRows[i] = null;
            i++;
        }
    }

    private final void updateRowFromVariables(ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }

    public void addCenterPoint(ConstraintWidget constraintWidget, ConstraintWidget constraintWidget2, float f, int i) {
        SolverVariable solverVariableCreateObjectVariable = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariableCreateObjectVariable2 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable solverVariableCreateObjectVariable3 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable solverVariableCreateObjectVariable4 = createObjectVariable(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        SolverVariable solverVariableCreateObjectVariable5 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
        SolverVariable solverVariableCreateObjectVariable6 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
        SolverVariable solverVariableCreateObjectVariable7 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
        SolverVariable solverVariableCreateObjectVariable8 = createObjectVariable(constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow arrayRowCreateRow = createRow();
        double d = f;
        double dSin = Math.sin(d);
        double d2 = i;
        Double.isNaN(d2);
        arrayRowCreateRow.createRowWithAngle(solverVariableCreateObjectVariable2, solverVariableCreateObjectVariable4, solverVariableCreateObjectVariable6, solverVariableCreateObjectVariable8, (float) (dSin * d2));
        addConstraint(arrayRowCreateRow);
        ArrayRow arrayRowCreateRow2 = createRow();
        double dCos = Math.cos(d);
        Double.isNaN(d2);
        arrayRowCreateRow2.createRowWithAngle(solverVariableCreateObjectVariable, solverVariableCreateObjectVariable3, solverVariableCreateObjectVariable5, solverVariableCreateObjectVariable7, (float) (dCos * d2));
        addConstraint(arrayRowCreateRow2);
    }

    public void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int i, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int i2, int i3) {
        ArrayRow arrayRowCreateRow = createRow();
        arrayRowCreateRow.createRowCentering(solverVariable, solverVariable2, i, f, solverVariable3, solverVariable4, i2);
        if (i3 != 6) {
            arrayRowCreateRow.addError(this, i3);
        }
        addConstraint(arrayRowCreateRow);
    }

    public void addConstraint(ArrayRow arrayRow) {
        SolverVariable solverVariablePickPivot;
        if (arrayRow == null) {
            return;
        }
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.constraints++;
            if (arrayRow.isSimpleDefinition) {
                sMetrics.simpleconstraints++;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        boolean z = false;
        if (!arrayRow.isSimpleDefinition) {
            updateRowFromVariables(arrayRow);
            if (arrayRow.isEmpty()) {
                return;
            }
            arrayRow.ensurePositiveConstant();
            if (arrayRow.chooseSubject(this)) {
                SolverVariable solverVariableCreateExtraVariable = createExtraVariable();
                arrayRow.variable = solverVariableCreateExtraVariable;
                addRow(arrayRow);
                this.mTempGoal.initFromRow(arrayRow);
                optimize(this.mTempGoal, true);
                z = true;
                if (solverVariableCreateExtraVariable.definitionId == -1) {
                    if (arrayRow.variable == solverVariableCreateExtraVariable && (solverVariablePickPivot = arrayRow.pickPivot(solverVariableCreateExtraVariable)) != null) {
                        Metrics metrics2 = sMetrics;
                        if (metrics2 != null) {
                            metrics2.pivots++;
                        }
                        arrayRow.pivot(solverVariablePickPivot);
                    }
                    if (!arrayRow.isSimpleDefinition) {
                        arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
                    }
                    this.mNumRows--;
                    z = true;
                }
            } else {
                z = false;
            }
            if (!arrayRow.hasKeyVariable()) {
                return;
            }
        }
        if (z) {
            return;
        }
        addRow(arrayRow);
    }

    public ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow arrayRowCreateRow = createRow();
        arrayRowCreateRow.createRowEquals(solverVariable, solverVariable2, i);
        if (i2 != 6) {
            arrayRowCreateRow.addError(this, i2);
        }
        addConstraint(arrayRowCreateRow);
        return arrayRowCreateRow;
    }

    public void addEquality(SolverVariable solverVariable, int i) {
        int i2 = solverVariable.definitionId;
        if (solverVariable.definitionId == -1) {
            ArrayRow arrayRowCreateRow = createRow();
            arrayRowCreateRow.createRowDefinition(solverVariable, i);
            addConstraint(arrayRowCreateRow);
            return;
        }
        ArrayRow arrayRow = this.mRows[i2];
        if (arrayRow.isSimpleDefinition) {
            arrayRow.constantValue = i;
            return;
        }
        if (arrayRow.variables.currentSize == 0) {
            arrayRow.isSimpleDefinition = true;
            arrayRow.constantValue = i;
        } else {
            ArrayRow arrayRowCreateRow2 = createRow();
            arrayRowCreateRow2.createRowEquals(solverVariable, i);
            addConstraint(arrayRowCreateRow2);
        }
    }

    public void addEquality(SolverVariable solverVariable, int i, int i2) {
        int i3 = solverVariable.definitionId;
        if (solverVariable.definitionId == -1) {
            ArrayRow arrayRowCreateRow = createRow();
            arrayRowCreateRow.createRowDefinition(solverVariable, i);
            arrayRowCreateRow.addError(this, i2);
            addConstraint(arrayRowCreateRow);
            return;
        }
        ArrayRow arrayRow = this.mRows[i3];
        if (arrayRow.isSimpleDefinition) {
            arrayRow.constantValue = i;
            return;
        }
        ArrayRow arrayRowCreateRow2 = createRow();
        arrayRowCreateRow2.createRowEquals(solverVariable, i);
        arrayRowCreateRow2.addError(this, i2);
        addConstraint(arrayRowCreateRow2);
    }

    public void addGreaterBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, boolean z) {
        ArrayRow arrayRowCreateRow = createRow();
        SolverVariable solverVariableCreateSlackVariable = createSlackVariable();
        solverVariableCreateSlackVariable.strength = 0;
        arrayRowCreateRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, 0);
        if (z) {
            addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)), 1);
        }
        addConstraint(arrayRowCreateRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, int i) {
        ArrayRow arrayRowCreateRow = createRow();
        SolverVariable solverVariableCreateSlackVariable = createSlackVariable();
        solverVariableCreateSlackVariable.strength = 0;
        arrayRowCreateRow.createRowGreaterThan(solverVariable, i, solverVariableCreateSlackVariable);
        addConstraint(arrayRowCreateRow);
    }

    public void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow arrayRowCreateRow = createRow();
        SolverVariable solverVariableCreateSlackVariable = createSlackVariable();
        solverVariableCreateSlackVariable.strength = 0;
        arrayRowCreateRow.createRowGreaterThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, i);
        if (i2 != 6) {
            addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)), i2);
        }
        addConstraint(arrayRowCreateRow);
    }

    public void addLowerBarrier(SolverVariable solverVariable, SolverVariable solverVariable2, boolean z) {
        ArrayRow arrayRowCreateRow = createRow();
        SolverVariable solverVariableCreateSlackVariable = createSlackVariable();
        solverVariableCreateSlackVariable.strength = 0;
        arrayRowCreateRow.createRowLowerThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, 0);
        if (z) {
            addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)), 1);
        }
        addConstraint(arrayRowCreateRow);
    }

    public void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow arrayRowCreateRow = createRow();
        SolverVariable solverVariableCreateSlackVariable = createSlackVariable();
        solverVariableCreateSlackVariable.strength = 0;
        arrayRowCreateRow.createRowLowerThan(solverVariable, solverVariable2, solverVariableCreateSlackVariable, i);
        if (i2 != 6) {
            addSingleError(arrayRowCreateRow, (int) (arrayRowCreateRow.variables.get(solverVariableCreateSlackVariable) * (-1.0f)), i2);
        }
        addConstraint(arrayRowCreateRow);
    }

    public void addRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f, int i) {
        ArrayRow arrayRowCreateRow = createRow();
        arrayRowCreateRow.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, f);
        if (i != 6) {
            arrayRowCreateRow.addError(this, i);
        }
        addConstraint(arrayRowCreateRow);
    }

    void addSingleError(ArrayRow arrayRow, int i, int i2) {
        arrayRow.addSingleError(createErrorVariable(i2, null), i);
    }

    public SolverVariable createErrorVariable(int i, String str) {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.errors++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable solverVariableAcquireSolverVariable = acquireSolverVariable(SolverVariable.Type.ERROR, str);
        int i2 = this.mVariablesID + 1;
        this.mVariablesID = i2;
        this.mNumColumns++;
        solverVariableAcquireSolverVariable.f3id = i2;
        solverVariableAcquireSolverVariable.strength = i;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariableAcquireSolverVariable;
        this.mGoal.addError(solverVariableAcquireSolverVariable);
        return solverVariableAcquireSolverVariable;
    }

    public SolverVariable createExtraVariable() {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.extravariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable solverVariableAcquireSolverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        solverVariableAcquireSolverVariable.f3id = i;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariableAcquireSolverVariable;
        return solverVariableAcquireSolverVariable;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.support.constraint.solver.SolverVariable createObjectVariable(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
            r0 = r5
            if (r0 != 0) goto L8
            r0 = 0
            return r0
        L8:
            r0 = r4
            int r0 = r0.mNumColumns
            r1 = 1
            int r0 = r0 + r1
            r1 = r4
            int r1 = r1.mMaxColumns
            if (r0 < r1) goto L19
            r0 = r4
            r0.increaseTableSize()
        L19:
            r0 = r5
            boolean r0 = r0 instanceof android.support.constraint.solver.widgets.ConstraintAnchor
            if (r0 == 0) goto La2
            r0 = r5
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = (android.support.constraint.solver.widgets.ConstraintAnchor) r0
            r8 = r0
            r0 = r8
            android.support.constraint.solver.SolverVariable r0 = r0.getSolverVariable()
            r7 = r0
            r0 = r7
            r5 = r0
            r0 = r7
            if (r0 != 0) goto L41
            r0 = r8
            r1 = r4
            android.support.constraint.solver.Cache r1 = r1.mCache
            r0.resetSolverVariable(r1)
            r0 = r8
            android.support.constraint.solver.SolverVariable r0 = r0.getSolverVariable()
            r5 = r0
        L41:
            r0 = r5
            int r0 = r0.f3id
            r1 = -1
            if (r0 == r1) goto L65
            r0 = r5
            int r0 = r0.f3id
            r1 = r4
            int r1 = r1.mVariablesID
            if (r0 > r1) goto L65
            r0 = r5
            r7 = r0
            r0 = r4
            android.support.constraint.solver.Cache r0 = r0.mCache
            android.support.constraint.solver.SolverVariable[] r0 = r0.mIndexedVariables
            r1 = r5
            int r1 = r1.f3id
            r0 = r0[r1]
            if (r0 != 0) goto La2
        L65:
            r0 = r5
            int r0 = r0.f3id
            r1 = -1
            if (r0 == r1) goto L71
            r0 = r5
            r0.reset()
        L71:
            r0 = r4
            int r0 = r0.mVariablesID
            r1 = 1
            int r0 = r0 + r1
            r6 = r0
            r0 = r4
            r1 = r6
            r0.mVariablesID = r1
            r0 = r4
            r1 = r4
            int r1 = r1.mNumColumns
            r2 = 1
            int r1 = r1 + r2
            r0.mNumColumns = r1
            r0 = r5
            r1 = r6
            r0.f3id = r1
            r0 = r5
            android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED
            r0.mType = r1
            r0 = r4
            android.support.constraint.solver.Cache r0 = r0.mCache
            android.support.constraint.solver.SolverVariable[] r0 = r0.mIndexedVariables
            r1 = r4
            int r1 = r1.mVariablesID
            r2 = r5
            r0[r1] = r2
            r0 = r5
            r7 = r0
        La2:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.LinearSystem.createObjectVariable(java.lang.Object):android.support.constraint.solver.SolverVariable");
    }

    public ArrayRow createRow() {
        ArrayRow arrayRowAcquire = this.mCache.arrayRowPool.acquire();
        if (arrayRowAcquire == null) {
            arrayRowAcquire = new ArrayRow(this.mCache);
        } else {
            arrayRowAcquire.reset();
        }
        SolverVariable.increaseErrorId();
        return arrayRowAcquire;
    }

    public SolverVariable createSlackVariable() {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.slackvariables++;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable solverVariableAcquireSolverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        solverVariableAcquireSolverVariable.f3id = i;
        this.mCache.mIndexedVariables[this.mVariablesID] = solverVariableAcquireSolverVariable;
        return solverVariableAcquireSolverVariable;
    }

    void displayReadableRows() {
        displaySolverVariables();
        String str = " #  ";
        for (int i = 0; i < this.mNumRows; i++) {
            str = (str + this.mRows[i].toReadableString()) + "\n #  ";
        }
        String str2 = str;
        if (this.mGoal != null) {
            str2 = str + this.mGoal + "\n";
        }
        System.out.println(str2);
    }

    void displaySystemInformations() {
        int i;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            i = i3;
            if (i2 >= this.TABLE_SIZE) {
                break;
            }
            ArrayRow[] arrayRowArr = this.mRows;
            int iSizeInBytes = i;
            if (arrayRowArr[i2] != null) {
                iSizeInBytes = i + arrayRowArr[i2].sizeInBytes();
            }
            i2++;
            i3 = iSizeInBytes;
        }
        int i4 = 0;
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i4 >= this.mNumRows) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("Linear System -> Table size: ");
                sb.append(this.TABLE_SIZE);
                sb.append(" (");
                int i7 = this.TABLE_SIZE;
                sb.append(getDisplaySize(i7 * i7));
                sb.append(") -- row sizes: ");
                sb.append(getDisplaySize(i));
                sb.append(", actual size: ");
                sb.append(getDisplaySize(i6));
                sb.append(" rows: ");
                sb.append(this.mNumRows);
                sb.append("/");
                sb.append(this.mMaxRows);
                sb.append(" cols: ");
                sb.append(this.mNumColumns);
                sb.append("/");
                sb.append(this.mMaxColumns);
                sb.append(" ");
                sb.append(0);
                sb.append(" occupied cells, ");
                sb.append(getDisplaySize(0));
                printStream.println(sb.toString());
                return;
            }
            ArrayRow[] arrayRowArr2 = this.mRows;
            int iSizeInBytes2 = i6;
            if (arrayRowArr2[i4] != null) {
                iSizeInBytes2 = i6 + arrayRowArr2[i4].sizeInBytes();
            }
            i4++;
            i5 = iSizeInBytes2;
        }
    }

    public void displayVariablesReadableRows() {
        displaySolverVariables();
        String str = BuildConfig.FLAVOR;
        int i = 0;
        while (i < this.mNumRows) {
            String str2 = str;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                str2 = (str + this.mRows[i].toReadableString()) + "\n";
            }
            i++;
            str = str2;
        }
        System.out.println(str + this.mGoal + "\n");
    }

    public void fillMetrics(Metrics metrics) {
        sMetrics = metrics;
    }

    public Cache getCache() {
        return this.mCache;
    }

    Row getGoal() {
        return this.mGoal;
    }

    public int getMemoryUsed() {
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= this.mNumRows) {
                return i3;
            }
            ArrayRow[] arrayRowArr = this.mRows;
            int iSizeInBytes = i3;
            if (arrayRowArr[i] != null) {
                iSizeInBytes = i3 + arrayRowArr[i].sizeInBytes();
            }
            i++;
            i2 = iSizeInBytes;
        }
    }

    public int getNumEquations() {
        return this.mNumRows;
    }

    public int getNumVariables() {
        return this.mVariablesID;
    }

    public int getObjectVariableValue(Object obj) {
        SolverVariable solverVariable = ((ConstraintAnchor) obj).getSolverVariable();
        if (solverVariable != null) {
            return (int) (solverVariable.computedValue + 0.5f);
        }
        return 0;
    }

    ArrayRow getRow(int i) {
        return this.mRows[i];
    }

    float getValueFor(String str) {
        SolverVariable variable = getVariable(str, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }

    SolverVariable getVariable(String str, SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<>();
        }
        SolverVariable solverVariable = this.mVariables.get(str);
        SolverVariable solverVariableCreateVariable = solverVariable;
        if (solverVariable == null) {
            solverVariableCreateVariable = createVariable(str, type);
        }
        return solverVariableCreateVariable;
    }

    public void minimize() throws Exception {
        boolean z;
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.minimize++;
        }
        if (!this.graphOptimizer) {
            minimizeGoal(this.mGoal);
            return;
        }
        Metrics metrics2 = sMetrics;
        if (metrics2 != null) {
            metrics2.graphOptimizer++;
        }
        int i = 0;
        while (true) {
            if (i >= this.mNumRows) {
                z = true;
                break;
            } else {
                if (!this.mRows[i].isSimpleDefinition) {
                    z = false;
                    break;
                }
                i++;
            }
        }
        if (!z) {
            minimizeGoal(this.mGoal);
            return;
        }
        Metrics metrics3 = sMetrics;
        if (metrics3 != null) {
            metrics3.fullySolved++;
        }
        computeValues();
    }

    void minimizeGoal(Row row) throws Exception {
        Metrics metrics = sMetrics;
        if (metrics != null) {
            metrics.minimizeGoal++;
            Metrics metrics2 = sMetrics;
            metrics2.maxVariables = Math.max(metrics2.maxVariables, this.mNumColumns);
            Metrics metrics3 = sMetrics;
            metrics3.maxRows = Math.max(metrics3.maxRows, this.mNumRows);
        }
        updateRowFromVariables((ArrayRow) row);
        enforceBFS(row);
        optimize(row, false);
        computeValues();
    }

    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; i++) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, (Object) null);
        HashMap<String, SolverVariable> map = this.mVariables;
        if (map != null) {
            map.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            this.mRows[i2].used = false;
        }
        releaseRows();
        this.mNumRows = 0;
    }
}
