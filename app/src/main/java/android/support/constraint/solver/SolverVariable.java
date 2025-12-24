package android.support.constraint.solver;

import com.github.clans.fab.BuildConfig;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/SolverVariable.class */
public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 7;
    public static final int STRENGTH_BARRIER = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueConstantId = 1;
    private static int uniqueErrorId = 1;
    private static int uniqueId = 1;
    private static int uniqueSlackId = 1;
    private static int uniqueUnrestrictedId = 1;
    public float computedValue;
    int definitionId;

    /* renamed from: id */
    public int f3id;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    public int usageInRowCount;

    /* renamed from: android.support.constraint.solver.SolverVariable$1 */
    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/SolverVariable$1.class */
    static /* synthetic */ class C00201 {
        static final int[] $SwitchMap$android$support$constraint$solver$SolverVariable$Type;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:19:0x004d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.support.constraint.solver.SolverVariable$Type[] r0 = android.support.constraint.solver.SolverVariable.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.support.constraint.solver.SolverVariable.C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type = r0
                r0 = r4
                android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED     // Catch: java.lang.NoSuchFieldError -> L41
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L41
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L41
            L14:
                int[] r0 = android.support.constraint.solver.SolverVariable.C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type     // Catch: java.lang.NoSuchFieldError -> L41 java.lang.NoSuchFieldError -> L45
                android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.CONSTANT     // Catch: java.lang.NoSuchFieldError -> L45
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L45
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L45
            L1f:
                int[] r0 = android.support.constraint.solver.SolverVariable.C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type     // Catch: java.lang.NoSuchFieldError -> L45 java.lang.NoSuchFieldError -> L49
                android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.SLACK     // Catch: java.lang.NoSuchFieldError -> L49
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L49
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L49
            L2a:
                int[] r0 = android.support.constraint.solver.SolverVariable.C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type     // Catch: java.lang.NoSuchFieldError -> L49 java.lang.NoSuchFieldError -> L4d
                android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.ERROR     // Catch: java.lang.NoSuchFieldError -> L4d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L4d
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L4d
            L35:
                int[] r0 = android.support.constraint.solver.SolverVariable.C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type     // Catch: java.lang.NoSuchFieldError -> L4d java.lang.NoSuchFieldError -> L51
                android.support.constraint.solver.SolverVariable$Type r1 = android.support.constraint.solver.SolverVariable.Type.UNKNOWN     // Catch: java.lang.NoSuchFieldError -> L51
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L51
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L51
            L40:
                return
            L41:
                r4 = move-exception
                goto L14
            L45:
                r4 = move-exception
                goto L1f
            L49:
                r4 = move-exception
                goto L2a
            L4d:
                r4 = move-exception
                goto L35
            L51:
                r4 = move-exception
                goto L40
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.SolverVariable.C00201.m39clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/constraint/solver/SolverVariable$Type.class */
    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public SolverVariable(Type type, String str) {
        this.f3id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mType = type;
    }

    public SolverVariable(String str, Type type) {
        this.f3id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mName = str;
        this.mType = type;
    }

    private static String getUniqueName(Type type, String str) {
        if (str != null) {
            return str + uniqueErrorId;
        }
        int i = C00201.$SwitchMap$android$support$constraint$solver$SolverVariable$Type[type.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("U");
            int i2 = uniqueUnrestrictedId + 1;
            uniqueUnrestrictedId = i2;
            sb.append(i2);
            return sb.toString();
        }
        if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("C");
            int i3 = uniqueConstantId + 1;
            uniqueConstantId = i3;
            sb2.append(i3);
            return sb2.toString();
        }
        if (i == 3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("S");
            int i4 = uniqueSlackId + 1;
            uniqueSlackId = i4;
            sb3.append(i4);
            return sb3.toString();
        }
        if (i == 4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("e");
            int i5 = uniqueErrorId + 1;
            uniqueErrorId = i5;
            sb4.append(i5);
            return sb4.toString();
        }
        if (i != 5) {
            throw new AssertionError(type.name());
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("V");
        int i6 = uniqueId + 1;
        uniqueId = i6;
        sb5.append(i6);
        return sb5.toString();
    }

    static void increaseErrorId() {
        uniqueErrorId++;
    }

    public final void addToRow(ArrayRow arrayRow) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i >= i2) {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = arrayRow;
                this.mClientEquationsCount = i3 + 1;
                return;
            }
            if (this.mClientEquations[i] == arrayRow) {
                return;
            } else {
                i++;
            }
        }
    }

    void clearStrengths() {
        for (int i = 0; i < 7; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    public String getName() {
        return this.mName;
    }

    public final void removeFromRow(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            if (this.mClientEquations[i2] == arrayRow) {
                for (int i3 = 0; i3 < (i - i2) - 1; i3++) {
                    ArrayRow[] arrayRowArr = this.mClientEquations;
                    int i4 = i2 + i3;
                    arrayRowArr[i4] = arrayRowArr[i4 + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.f3id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public void setType(Type type, String str) {
        this.mType = type;
    }

    String strengthsToString() {
        String str = this + "[";
        boolean z = false;
        boolean z2 = true;
        for (int i = 0; i < this.strengthVector.length; i++) {
            String str2 = str + this.strengthVector[i];
            float[] fArr = this.strengthVector;
            if (fArr[i] > 0.0f) {
                z = false;
            } else if (fArr[i] < 0.0f) {
                z = true;
            }
            if (this.strengthVector[i] != 0.0f) {
                z2 = false;
            }
            str = i < this.strengthVector.length - 1 ? str2 + ", " : str2 + "] ";
        }
        String str3 = str;
        if (z) {
            str3 = str + " (-)";
        }
        String str4 = str3;
        if (z2) {
            str4 = str3 + " (*)";
        }
        return str4;
    }

    public String toString() {
        return BuildConfig.FLAVOR + this.mName;
    }

    public final void updateReferencesWithNewDefinition(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].variables.updateFromRow(this.mClientEquations[i2], arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }
}
