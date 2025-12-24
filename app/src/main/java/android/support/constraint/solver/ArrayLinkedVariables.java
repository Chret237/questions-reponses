package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable;
import com.github.clans.fab.BuildConfig;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/ArrayLinkedVariables.class */
public class ArrayLinkedVariables {
    private static final boolean DEBUG = false;
    private static final boolean FULL_NEW_CHECK = false;
    private static final int NONE = -1;
    private final Cache mCache;
    private final ArrayRow mRow;
    int currentSize = 0;
    private int ROW_SIZE = 8;
    private SolverVariable candidate = null;
    private int[] mArrayIndices = new int[8];
    private int[] mArrayNextIndices = new int[8];
    private float[] mArrayValues = new float[8];
    private int mHead = -1;
    private int mLast = -1;
    private boolean mDidFillOnce = false;

    ArrayLinkedVariables(ArrayRow arrayRow, Cache cache) {
        this.mRow = arrayRow;
        this.mCache = cache;
    }

    private boolean isNew(SolverVariable solverVariable, LinearSystem linearSystem) {
        boolean z = true;
        if (solverVariable.usageInRowCount > 1) {
            z = false;
        }
        return z;
    }

    final void add(SolverVariable solverVariable, float f, boolean z) {
        if (f == 0.0f) {
            return;
        }
        int i = this.mHead;
        if (i == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = f;
            this.mArrayIndices[0] = solverVariable.f3id;
            this.mArrayNextIndices[this.mHead] = -1;
            solverVariable.usageInRowCount++;
            solverVariable.addToRow(this.mRow);
            this.currentSize++;
            if (this.mDidFillOnce) {
                return;
            }
            int i2 = this.mLast + 1;
            this.mLast = i2;
            int[] iArr = this.mArrayIndices;
            if (i2 >= iArr.length) {
                this.mDidFillOnce = true;
                this.mLast = iArr.length - 1;
                return;
            }
            return;
        }
        int i3 = -1;
        for (int i4 = 0; i != -1 && i4 < this.currentSize; i4++) {
            if (this.mArrayIndices[i] == solverVariable.f3id) {
                float[] fArr = this.mArrayValues;
                fArr[i] = fArr[i] + f;
                if (fArr[i] == 0.0f) {
                    if (i == this.mHead) {
                        this.mHead = this.mArrayNextIndices[i];
                    } else {
                        int[] iArr2 = this.mArrayNextIndices;
                        iArr2[i3] = iArr2[i];
                    }
                    if (z) {
                        solverVariable.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = i;
                    }
                    solverVariable.usageInRowCount--;
                    this.currentSize--;
                    return;
                }
                return;
            }
            if (this.mArrayIndices[i] < solverVariable.f3id) {
                i3 = i;
            }
            i = this.mArrayNextIndices[i];
        }
        int length = this.mLast;
        if (this.mDidFillOnce) {
            int[] iArr3 = this.mArrayIndices;
            if (iArr3[length] != -1) {
                length = iArr3.length;
            }
        } else {
            length++;
        }
        int[] iArr4 = this.mArrayIndices;
        int i5 = length;
        if (length >= iArr4.length) {
            i5 = length;
            if (this.currentSize < iArr4.length) {
                int i6 = 0;
                while (true) {
                    int[] iArr5 = this.mArrayIndices;
                    i5 = length;
                    if (i6 >= iArr5.length) {
                        break;
                    }
                    if (iArr5[i6] == -1) {
                        i5 = i6;
                        break;
                    }
                    i6++;
                }
            }
        }
        int[] iArr6 = this.mArrayIndices;
        int length2 = i5;
        if (i5 >= iArr6.length) {
            length2 = iArr6.length;
            int i7 = this.ROW_SIZE * 2;
            this.ROW_SIZE = i7;
            this.mDidFillOnce = false;
            this.mLast = length2 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, i7);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length2] = solverVariable.f3id;
        this.mArrayValues[length2] = f;
        if (i3 != -1) {
            int[] iArr7 = this.mArrayNextIndices;
            iArr7[length2] = iArr7[i3];
            iArr7[i3] = length2;
        } else {
            this.mArrayNextIndices[length2] = this.mHead;
            this.mHead = length2;
        }
        solverVariable.usageInRowCount++;
        solverVariable.addToRow(this.mRow);
        this.currentSize++;
        if (!this.mDidFillOnce) {
            this.mLast++;
        }
        int i8 = this.mLast;
        int[] iArr8 = this.mArrayIndices;
        if (i8 >= iArr8.length) {
            this.mDidFillOnce = true;
            this.mLast = iArr8.length - 1;
        }
    }

    SolverVariable chooseSubject(LinearSystem linearSystem) {
        float f;
        int i = this.mHead;
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        int i2 = 0;
        boolean z = false;
        boolean z2 = false;
        float f2 = 0.0f;
        float f3 = 0.0f;
        while (true) {
            float f4 = f3;
            if (i == -1 || i2 >= this.currentSize) {
                break;
            }
            float f5 = this.mArrayValues[i];
            SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            if (f5 < 0.0f) {
                f = f5;
                if (f5 > -0.001f) {
                    this.mArrayValues[i] = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                    f = 0.0f;
                }
            } else {
                f = f5;
                if (f5 < 0.001f) {
                    this.mArrayValues[i] = 0.0f;
                    solverVariable3.removeFromRow(this.mRow);
                    f = 0.0f;
                }
            }
            SolverVariable solverVariable4 = solverVariable;
            SolverVariable solverVariable5 = solverVariable2;
            boolean zIsNew = z;
            boolean z3 = z2;
            float f6 = f2;
            float f7 = f4;
            if (f != 0.0f) {
                if (solverVariable3.mType != SolverVariable.Type.UNRESTRICTED) {
                    solverVariable4 = solverVariable;
                    solverVariable5 = solverVariable2;
                    zIsNew = z;
                    z3 = z2;
                    f6 = f2;
                    f7 = f4;
                    if (solverVariable2 == null) {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        zIsNew = z;
                        z3 = z2;
                        f6 = f2;
                        f7 = f4;
                        if (f < 0.0f) {
                            if (solverVariable != null && f4 <= f) {
                                solverVariable4 = solverVariable;
                                solverVariable5 = solverVariable2;
                                zIsNew = z;
                                z3 = z2;
                                f6 = f2;
                                f7 = f4;
                                if (!z2) {
                                    solverVariable4 = solverVariable;
                                    solverVariable5 = solverVariable2;
                                    zIsNew = z;
                                    z3 = z2;
                                    f6 = f2;
                                    f7 = f4;
                                    if (isNew(solverVariable3, linearSystem)) {
                                        z3 = true;
                                        f7 = f;
                                        f6 = f2;
                                        zIsNew = z;
                                        solverVariable5 = solverVariable2;
                                        solverVariable4 = solverVariable3;
                                    }
                                }
                            } else {
                                boolean zIsNew2 = isNew(solverVariable3, linearSystem);
                                z3 = zIsNew2;
                                solverVariable4 = solverVariable3;
                                solverVariable5 = solverVariable2;
                                zIsNew = z;
                                f6 = f2;
                                f7 = f;
                            }
                        }
                    }
                } else if (solverVariable2 != null && f2 <= f) {
                    solverVariable4 = solverVariable;
                    solverVariable5 = solverVariable2;
                    zIsNew = z;
                    z3 = z2;
                    f6 = f2;
                    f7 = f4;
                    if (!z) {
                        solverVariable4 = solverVariable;
                        solverVariable5 = solverVariable2;
                        zIsNew = z;
                        z3 = z2;
                        f6 = f2;
                        f7 = f4;
                        if (isNew(solverVariable3, linearSystem)) {
                            zIsNew = true;
                            solverVariable4 = solverVariable;
                            solverVariable5 = solverVariable3;
                            z3 = z2;
                            f6 = f;
                            f7 = f4;
                        }
                    }
                } else {
                    zIsNew = isNew(solverVariable3, linearSystem);
                    solverVariable4 = solverVariable;
                    solverVariable5 = solverVariable3;
                    z3 = z2;
                    f6 = f;
                    f7 = f4;
                }
            }
            i = this.mArrayNextIndices[i];
            i2++;
            solverVariable = solverVariable4;
            solverVariable2 = solverVariable5;
            z = zIsNew;
            z2 = z3;
            f2 = f6;
            f3 = f7;
        }
        return solverVariable2 != null ? solverVariable2 : solverVariable;
    }

    public final void clear() {
        int i = this.mHead;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            if (solverVariable != null) {
                solverVariable.removeFromRow(this.mRow);
            }
            i = this.mArrayNextIndices[i];
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }

    final boolean containsKey(SolverVariable solverVariable) {
        int i = this.mHead;
        if (i == -1) {
            return false;
        }
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            if (this.mArrayIndices[i] == solverVariable.f3id) {
                return true;
            }
            i = this.mArrayNextIndices[i];
        }
        return false;
    }

    public void display() {
        int i = this.currentSize;
        System.out.print("{ ");
        for (int i2 = 0; i2 < i; i2++) {
            SolverVariable variable = getVariable(i2);
            if (variable != null) {
                System.out.print(variable + " = " + getVariableValue(i2) + " ");
            }
        }
        System.out.println(" }");
    }

    void divideByAmount(float f) {
        int i = this.mHead;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            float[] fArr = this.mArrayValues;
            fArr[i] = fArr[i] / f;
            i = this.mArrayNextIndices[i];
        }
    }

    public final float get(SolverVariable solverVariable) {
        int i = this.mHead;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            if (this.mArrayIndices[i] == solverVariable.f3id) {
                return this.mArrayValues[i];
            }
            i = this.mArrayNextIndices[i];
        }
        return 0.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.support.constraint.solver.SolverVariable getPivotCandidate() {
        /*
            r4 = this;
            r0 = r4
            android.support.constraint.solver.SolverVariable r0 = r0.candidate
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L66
            r0 = r4
            int r0 = r0.mHead
            r6 = r0
            r0 = 0
            r5 = r0
            r0 = 0
            r7 = r0
        L12:
            r0 = r6
            r1 = -1
            if (r0 == r1) goto L64
            r0 = r5
            r1 = r4
            int r1 = r1.currentSize
            if (r0 >= r1) goto L64
            r0 = r7
            r8 = r0
            r0 = r4
            float[] r0 = r0.mArrayValues
            r1 = r6
            r0 = r0[r1]
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L54
            r0 = r4
            android.support.constraint.solver.Cache r0 = r0.mCache
            android.support.constraint.solver.SolverVariable[] r0 = r0.mIndexedVariables
            r1 = r4
            int[] r1 = r1.mArrayIndices
            r2 = r6
            r1 = r1[r2]
            r0 = r0[r1]
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L50
            r0 = r7
            r8 = r0
            r0 = r7
            int r0 = r0.strength
            r1 = r9
            int r1 = r1.strength
            if (r0 >= r1) goto L54
        L50:
            r0 = r9
            r8 = r0
        L54:
            r0 = r4
            int[] r0 = r0.mArrayNextIndices
            r1 = r6
            r0 = r0[r1]
            r6 = r0
            int r5 = r5 + 1
            r0 = r8
            r7 = r0
            goto L12
        L64:
            r0 = r7
            return r0
        L66:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.ArrayLinkedVariables.getPivotCandidate():android.support.constraint.solver.SolverVariable");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0080  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    android.support.constraint.solver.SolverVariable getPivotCandidate(boolean[] r5, android.support.constraint.solver.SolverVariable r6) {
        /*
            r4 = this;
            r0 = r4
            int r0 = r0.mHead
            r11 = r0
            r0 = 0
            r10 = r0
            r0 = 0
            r13 = r0
            r0 = 0
            r7 = r0
        Le:
            r0 = r11
            r1 = -1
            if (r0 == r1) goto Lb5
            r0 = r10
            r1 = r4
            int r1 = r1.currentSize
            if (r0 >= r1) goto Lb5
            r0 = r13
            r12 = r0
            r0 = r7
            r8 = r0
            r0 = r4
            float[] r0 = r0.mArrayValues
            r1 = r11
            r0 = r0[r1]
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L9f
            r0 = r4
            android.support.constraint.solver.Cache r0 = r0.mCache
            android.support.constraint.solver.SolverVariable[] r0 = r0.mIndexedVariables
            r1 = r4
            int[] r1 = r1.mArrayIndices
            r2 = r11
            r1 = r1[r2]
            r0 = r0[r1]
            r14 = r0
            r0 = r5
            if (r0 == 0) goto L56
            r0 = r13
            r12 = r0
            r0 = r7
            r8 = r0
            r0 = r5
            r1 = r14
            int r1 = r1.f3id
            r0 = r0[r1]
            if (r0 != 0) goto L9f
        L56:
            r0 = r13
            r12 = r0
            r0 = r7
            r8 = r0
            r0 = r14
            r1 = r6
            if (r0 == r1) goto L9f
            r0 = r14
            android.support.constraint.solver.SolverVariable$Type r0 = r0.mType
            android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.SLACK
            if (r0 == r1) goto L80
            r0 = r13
            r12 = r0
            r0 = r7
            r8 = r0
            r0 = r14
            android.support.constraint.solver.SolverVariable$Type r0 = r0.mType
            android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.ERROR
            if (r0 != r1) goto L9f
        L80:
            r0 = r4
            float[] r0 = r0.mArrayValues
            r1 = r11
            r0 = r0[r1]
            r9 = r0
            r0 = r13
            r12 = r0
            r0 = r7
            r8 = r0
            r0 = r9
            r1 = r7
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L9f
            r0 = r14
            r12 = r0
            r0 = r9
            r8 = r0
        L9f:
            r0 = r4
            int[] r0 = r0.mArrayNextIndices
            r1 = r11
            r0 = r0[r1]
            r11 = r0
            int r10 = r10 + 1
            r0 = r12
            r13 = r0
            r0 = r8
            r7 = r0
            goto Le
        Lb5:
            r0 = r13
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.ArrayLinkedVariables.getPivotCandidate(boolean[], android.support.constraint.solver.SolverVariable):android.support.constraint.solver.SolverVariable");
    }

    final SolverVariable getVariable(int i) {
        int i2 = this.mHead;
        for (int i3 = 0; i2 != -1 && i3 < this.currentSize; i3++) {
            if (i3 == i) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[i2]];
            }
            i2 = this.mArrayNextIndices[i2];
        }
        return null;
    }

    final float getVariableValue(int i) {
        int i2 = this.mHead;
        for (int i3 = 0; i2 != -1 && i3 < this.currentSize; i3++) {
            if (i3 == i) {
                return this.mArrayValues[i2];
            }
            i2 = this.mArrayNextIndices[i2];
        }
        return 0.0f;
    }

    boolean hasAtLeastOnePositiveVariable() {
        int i = this.mHead;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            if (this.mArrayValues[i] > 0.0f) {
                return true;
            }
            i = this.mArrayNextIndices[i];
        }
        return false;
    }

    void invert() {
        int i = this.mHead;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            float[] fArr = this.mArrayValues;
            fArr[i] = fArr[i] * (-1.0f);
            i = this.mArrayNextIndices[i];
        }
    }

    public final void put(SolverVariable solverVariable, float f) {
        if (f == 0.0f) {
            remove(solverVariable, true);
            return;
        }
        int i = this.mHead;
        if (i == -1) {
            this.mHead = 0;
            this.mArrayValues[0] = f;
            this.mArrayIndices[0] = solverVariable.f3id;
            this.mArrayNextIndices[this.mHead] = -1;
            solverVariable.usageInRowCount++;
            solverVariable.addToRow(this.mRow);
            this.currentSize++;
            if (this.mDidFillOnce) {
                return;
            }
            int i2 = this.mLast + 1;
            this.mLast = i2;
            int[] iArr = this.mArrayIndices;
            if (i2 >= iArr.length) {
                this.mDidFillOnce = true;
                this.mLast = iArr.length - 1;
                return;
            }
            return;
        }
        int i3 = -1;
        for (int i4 = 0; i != -1 && i4 < this.currentSize; i4++) {
            if (this.mArrayIndices[i] == solverVariable.f3id) {
                this.mArrayValues[i] = f;
                return;
            }
            if (this.mArrayIndices[i] < solverVariable.f3id) {
                i3 = i;
            }
            i = this.mArrayNextIndices[i];
        }
        int length = this.mLast;
        if (this.mDidFillOnce) {
            int[] iArr2 = this.mArrayIndices;
            if (iArr2[length] != -1) {
                length = iArr2.length;
            }
        } else {
            length++;
        }
        int[] iArr3 = this.mArrayIndices;
        int i5 = length;
        if (length >= iArr3.length) {
            i5 = length;
            if (this.currentSize < iArr3.length) {
                int i6 = 0;
                while (true) {
                    int[] iArr4 = this.mArrayIndices;
                    i5 = length;
                    if (i6 >= iArr4.length) {
                        break;
                    }
                    if (iArr4[i6] == -1) {
                        i5 = i6;
                        break;
                    }
                    i6++;
                }
            }
        }
        int[] iArr5 = this.mArrayIndices;
        int length2 = i5;
        if (i5 >= iArr5.length) {
            length2 = iArr5.length;
            int i7 = this.ROW_SIZE * 2;
            this.ROW_SIZE = i7;
            this.mDidFillOnce = false;
            this.mLast = length2 - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, i7);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[length2] = solverVariable.f3id;
        this.mArrayValues[length2] = f;
        if (i3 != -1) {
            int[] iArr6 = this.mArrayNextIndices;
            iArr6[length2] = iArr6[i3];
            iArr6[i3] = length2;
        } else {
            this.mArrayNextIndices[length2] = this.mHead;
            this.mHead = length2;
        }
        solverVariable.usageInRowCount++;
        solverVariable.addToRow(this.mRow);
        this.currentSize++;
        if (!this.mDidFillOnce) {
            this.mLast++;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
        }
        int i8 = this.mLast;
        int[] iArr7 = this.mArrayIndices;
        if (i8 >= iArr7.length) {
            this.mDidFillOnce = true;
            this.mLast = iArr7.length - 1;
        }
    }

    public final float remove(SolverVariable solverVariable, boolean z) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        int i = this.mHead;
        if (i == -1) {
            return 0.0f;
        }
        int i2 = 0;
        int i3 = -1;
        while (i != -1 && i2 < this.currentSize) {
            if (this.mArrayIndices[i] == solverVariable.f3id) {
                if (i == this.mHead) {
                    this.mHead = this.mArrayNextIndices[i];
                } else {
                    int[] iArr = this.mArrayNextIndices;
                    iArr[i3] = iArr[i];
                }
                if (z) {
                    solverVariable.removeFromRow(this.mRow);
                }
                solverVariable.usageInRowCount--;
                this.currentSize--;
                this.mArrayIndices[i] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = i;
                }
                return this.mArrayValues[i];
            }
            i2++;
            i3 = i;
            i = this.mArrayNextIndices[i];
        }
        return 0.0f;
    }

    int sizeInBytes() {
        return (this.mArrayIndices.length * 4 * 3) + 0 + 36;
    }

    public String toString() {
        int i = this.mHead;
        String str = BuildConfig.FLAVOR;
        for (int i2 = 0; i != -1 && i2 < this.currentSize; i2++) {
            str = ((str + " -> ") + this.mArrayValues[i] + " : ") + this.mCache.mIndexedVariables[this.mArrayIndices[i]];
            i = this.mArrayNextIndices[i];
        }
        return str;
    }

    final void updateFromRow(ArrayRow arrayRow, ArrayRow arrayRow2, boolean z) {
        int i = this.mHead;
        while (true) {
            int i2 = i;
            for (int i3 = 0; i2 != -1 && i3 < this.currentSize; i3++) {
                if (this.mArrayIndices[i2] == arrayRow2.variable.f3id) {
                    float f = this.mArrayValues[i2];
                    remove(arrayRow2.variable, z);
                    ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                    int i4 = arrayLinkedVariables.mHead;
                    for (int i5 = 0; i4 != -1 && i5 < arrayLinkedVariables.currentSize; i5++) {
                        add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[i4]], arrayLinkedVariables.mArrayValues[i4] * f, z);
                        i4 = arrayLinkedVariables.mArrayNextIndices[i4];
                    }
                    arrayRow.constantValue += arrayRow2.constantValue * f;
                    if (z) {
                        arrayRow2.variable.removeFromRow(arrayRow);
                    }
                    i = this.mHead;
                } else {
                    i2 = this.mArrayNextIndices[i2];
                }
            }
            return;
        }
    }

    void updateFromSystem(ArrayRow arrayRow, ArrayRow[] arrayRowArr) {
        int i = this.mHead;
        while (true) {
            int i2 = i;
            for (int i3 = 0; i2 != -1 && i3 < this.currentSize; i3++) {
                SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i2]];
                if (solverVariable.definitionId != -1) {
                    float f = this.mArrayValues[i2];
                    remove(solverVariable, true);
                    ArrayRow arrayRow2 = arrayRowArr[solverVariable.definitionId];
                    if (!arrayRow2.isSimpleDefinition) {
                        ArrayLinkedVariables arrayLinkedVariables = arrayRow2.variables;
                        int i4 = arrayLinkedVariables.mHead;
                        for (int i5 = 0; i4 != -1 && i5 < arrayLinkedVariables.currentSize; i5++) {
                            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[i4]], arrayLinkedVariables.mArrayValues[i4] * f, true);
                            i4 = arrayLinkedVariables.mArrayNextIndices[i4];
                        }
                    }
                    arrayRow.constantValue += arrayRow2.constantValue * f;
                    arrayRow2.variable.removeFromRow(arrayRow);
                    i = this.mHead;
                } else {
                    i2 = this.mArrayNextIndices[i2];
                }
            }
            return;
        }
    }
}
