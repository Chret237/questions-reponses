package android.support.v4.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v4/graphics/PathParser.class */
public class PathParser {
    private static final String LOGTAG = "PathParser";

    /* loaded from: classes-dex2jar.jar:android/support/v4/graphics/PathParser$ExtractFloatResult.class */
    private static class ExtractFloatResult {
        int mEndPosition;
        boolean mEndWithNegOrDot;

        ExtractFloatResult() {
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/graphics/PathParser$PathDataNode.class */
    public static class PathDataNode {
        public float[] mParams;
        public char mType;

        PathDataNode(char c, float[] fArr) {
            this.mType = c;
            this.mParams = fArr;
        }

        PathDataNode(PathDataNode pathDataNode) {
            this.mType = pathDataNode.mType;
            float[] fArr = pathDataNode.mParams;
            this.mParams = PathParser.copyOfRange(fArr, 0, fArr.length);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:89:0x03ab  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x041a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static void addCommand(android.graphics.Path r11, float[] r12, char r13, char r14, float[] r15) {
            /*
                Method dump skipped, instructions count: 2131
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.PathParser.PathDataNode.addCommand(android.graphics.Path, float[], char, char, float[]):void");
        }

        private static void arcToBezier(Path path, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            int iCeil = (int) Math.ceil(Math.abs((d9 * 4.0d) / 3.141592653589793d));
            double dCos = Math.cos(d7);
            double dSin = Math.sin(d7);
            double dCos2 = Math.cos(d8);
            double dSin2 = Math.sin(d8);
            double d10 = -d3;
            double d11 = d10 * dCos;
            double d12 = d4 * dSin;
            double d13 = d10 * dSin;
            double d14 = d4 * dCos;
            double d15 = iCeil;
            Double.isNaN(d15);
            double d16 = d9 / d15;
            double d17 = d8;
            double d18 = (d11 * dSin2) - (d12 * dCos2);
            int i = 0;
            double d19 = d5;
            double d20 = (dSin2 * d13) + (dCos2 * d14);
            double d21 = d6;
            while (i < iCeil) {
                double d22 = d17 + d16;
                double dSin3 = Math.sin(d22);
                double dCos3 = Math.cos(d22);
                double d23 = (d + ((d3 * dCos) * dCos3)) - (d12 * dSin3);
                double d24 = d2 + (d3 * dSin * dCos3) + (d14 * dSin3);
                double d25 = (d11 * dSin3) - (d12 * dCos3);
                double d26 = (dSin3 * d13) + (dCos3 * d14);
                double d27 = d22 - d17;
                double dTan = Math.tan(d27 / 2.0d);
                double dSin4 = (Math.sin(d27) * (Math.sqrt(((dTan * 3.0d) * dTan) + 4.0d) - 1.0d)) / 3.0d;
                path.rLineTo(0.0f, 0.0f);
                path.cubicTo((float) (d19 + (d18 * dSin4)), (float) (d21 + (d20 * dSin4)), (float) (d23 - (dSin4 * d25)), (float) (d24 - (dSin4 * d26)), (float) d23, (float) d24);
                i++;
                d19 = d23;
                d17 = d22;
                d20 = d26;
                d18 = d25;
                d21 = d24;
            }
        }

        private static void drawArc(Path path, float f, float f2, float f3, float f4, float f5, float f6, float f7, boolean z, boolean z2) {
            double d;
            double d2;
            double radians = Math.toRadians(f7);
            double dCos = Math.cos(radians);
            double dSin = Math.sin(radians);
            double d3 = f;
            Double.isNaN(d3);
            double d4 = f2;
            Double.isNaN(d4);
            double d5 = f5;
            Double.isNaN(d5);
            double d6 = ((d3 * dCos) + (d4 * dSin)) / d5;
            double d7 = -f;
            Double.isNaN(d7);
            Double.isNaN(d4);
            double d8 = f6;
            Double.isNaN(d8);
            double d9 = ((d7 * dSin) + (d4 * dCos)) / d8;
            double d10 = f3;
            Double.isNaN(d10);
            double d11 = f4;
            Double.isNaN(d11);
            Double.isNaN(d5);
            double d12 = ((d10 * dCos) + (d11 * dSin)) / d5;
            double d13 = -f3;
            Double.isNaN(d13);
            Double.isNaN(d11);
            Double.isNaN(d8);
            double d14 = ((d13 * dSin) + (d11 * dCos)) / d8;
            double d15 = d6 - d12;
            double d16 = d9 - d14;
            double d17 = (d6 + d12) / 2.0d;
            double d18 = (d9 + d14) / 2.0d;
            double d19 = (d15 * d15) + (d16 * d16);
            if (d19 == 0.0d) {
                Log.w(PathParser.LOGTAG, " Points are coincident");
                return;
            }
            double d20 = (1.0d / d19) - 0.25d;
            if (d20 < 0.0d) {
                Log.w(PathParser.LOGTAG, "Points are too far apart " + d19);
                float fSqrt = (float) (Math.sqrt(d19) / 1.99999d);
                drawArc(path, f, f2, f3, f4, f5 * fSqrt, f6 * fSqrt, f7, z, z2);
                return;
            }
            double dSqrt = Math.sqrt(d20);
            double d21 = d15 * dSqrt;
            double d22 = dSqrt * d16;
            if (z == z2) {
                d = d17 - d22;
                d2 = d18 + d21;
            } else {
                d = d17 + d22;
                d2 = d18 - d21;
            }
            double dAtan2 = Math.atan2(d9 - d2, d6 - d);
            double dAtan22 = Math.atan2(d14 - d2, d12 - d) - dAtan2;
            double d23 = dAtan22;
            if (z2 != (dAtan22 >= 0.0d)) {
                d23 = dAtan22 > 0.0d ? dAtan22 - 6.283185307179586d : dAtan22 + 6.283185307179586d;
            }
            Double.isNaN(d5);
            double d24 = d * d5;
            Double.isNaN(d8);
            double d25 = d2 * d8;
            arcToBezier(path, (d24 * dCos) - (d25 * dSin), (d24 * dSin) + (d25 * dCos), d5, d8, d3, d4, radians, dAtan2, d23);
        }

        public static void nodesToPath(PathDataNode[] pathDataNodeArr, Path path) {
            float[] fArr = new float[6];
            char c = 'm';
            for (int i = 0; i < pathDataNodeArr.length; i++) {
                addCommand(path, fArr, c, pathDataNodeArr[i].mType, pathDataNodeArr[i].mParams);
                c = pathDataNodeArr[i].mType;
            }
        }

        public void interpolatePathDataNode(PathDataNode pathDataNode, PathDataNode pathDataNode2, float f) {
            int i = 0;
            while (true) {
                float[] fArr = pathDataNode.mParams;
                if (i >= fArr.length) {
                    return;
                }
                this.mParams[i] = (fArr[i] * (1.0f - f)) + (pathDataNode2.mParams[i] * f);
                i++;
            }
        }
    }

    private PathParser() {
    }

    private static void addNode(ArrayList<PathDataNode> arrayList, char c, float[] fArr) {
        arrayList.add(new PathDataNode(c, fArr));
    }

    public static boolean canMorph(PathDataNode[] pathDataNodeArr, PathDataNode[] pathDataNodeArr2) {
        if (pathDataNodeArr == null || pathDataNodeArr2 == null || pathDataNodeArr.length != pathDataNodeArr2.length) {
            return false;
        }
        for (int i = 0; i < pathDataNodeArr.length; i++) {
            if (pathDataNodeArr[i].mType != pathDataNodeArr2[i].mType || pathDataNodeArr[i].mParams.length != pathDataNodeArr2[i].mParams.length) {
                return false;
            }
        }
        return true;
    }

    static float[] copyOfRange(float[] fArr, int i, int i2) {
        if (i > i2) {
            throw new IllegalArgumentException();
        }
        int length = fArr.length;
        if (i < 0 || i > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i3 = i2 - i;
        int iMin = Math.min(i3, length - i);
        float[] fArr2 = new float[i3];
        System.arraycopy(fArr, i, fArr2, 0, iMin);
        return fArr2;
    }

    public static PathDataNode[] createNodesFromPathData(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i = 1;
        int i2 = 0;
        while (i < str.length()) {
            int iNextStart = nextStart(str, i);
            String strTrim = str.substring(i2, iNextStart).trim();
            if (strTrim.length() > 0) {
                addNode(arrayList, strTrim.charAt(0), getFloats(strTrim));
            }
            i2 = iNextStart;
            i = iNextStart + 1;
        }
        if (i - i2 == 1 && i2 < str.length()) {
            addNode(arrayList, str.charAt(i2), new float[0]);
        }
        return (PathDataNode[]) arrayList.toArray(new PathDataNode[arrayList.size()]);
    }

    public static Path createPathFromPathData(String str) {
        Path path = new Path();
        PathDataNode[] pathDataNodeArrCreateNodesFromPathData = createNodesFromPathData(str);
        if (pathDataNodeArrCreateNodesFromPathData == null) {
            return null;
        }
        try {
            PathDataNode.nodesToPath(pathDataNodeArrCreateNodesFromPathData, path);
            return path;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error in parsing " + str, e);
        }
    }

    public static PathDataNode[] deepCopyNodes(PathDataNode[] pathDataNodeArr) {
        if (pathDataNodeArr == null) {
            return null;
        }
        PathDataNode[] pathDataNodeArr2 = new PathDataNode[pathDataNodeArr.length];
        for (int i = 0; i < pathDataNodeArr.length; i++) {
            pathDataNodeArr2[i] = new PathDataNode(pathDataNodeArr[i]);
        }
        return pathDataNodeArr2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0095 A[LOOP:0: B:3:0x0010->B:29:0x0095, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void extract(java.lang.String r3, int r4, android.support.v4.graphics.PathParser.ExtractFloatResult r5) {
        /*
            r0 = r5
            r1 = 0
            r0.mEndWithNegOrDot = r1
            r0 = r4
            r8 = r0
            r0 = 0
            r6 = r0
            r0 = 0
            r9 = r0
            r0 = 0
            r7 = r0
        L10:
            r0 = r8
            r1 = r3
            int r1 = r1.length()
            if (r0 >= r1) goto L9b
            r0 = r3
            r1 = r8
            char r0 = r0.charAt(r1)
            r10 = r0
            r0 = r10
            r1 = 32
            if (r0 == r1) goto L88
            r0 = r10
            r1 = 69
            if (r0 == r1) goto L83
            r0 = r10
            r1 = 101(0x65, float:1.42E-43)
            if (r0 == r1) goto L83
            r0 = r10
            switch(r0) {
                case 44: goto L88;
                case 45: goto L6c;
                case 46: goto L57;
                default: goto L54;
            }
        L54:
            goto L7e
        L57:
            r0 = r9
            if (r0 != 0) goto L64
            r0 = 0
            r6 = r0
            r0 = 1
            r9 = r0
            goto L8d
        L64:
            r0 = r5
            r1 = 1
            r0.mEndWithNegOrDot = r1
            goto L88
        L6c:
            r0 = r8
            r1 = r4
            if (r0 == r1) goto L7e
            r0 = r6
            if (r0 != 0) goto L7e
            r0 = r5
            r1 = 1
            r0.mEndWithNegOrDot = r1
            goto L88
        L7e:
            r0 = 0
            r6 = r0
            goto L8d
        L83:
            r0 = 1
            r6 = r0
            goto L8d
        L88:
            r0 = 0
            r6 = r0
            r0 = 1
            r7 = r0
        L8d:
            r0 = r7
            if (r0 == 0) goto L95
            goto L9b
        L95:
            int r8 = r8 + 1
            goto L10
        L9b:
            r0 = r5
            r1 = r8
            r0.mEndPosition = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.PathParser.extract(java.lang.String, int, android.support.v4.graphics.PathParser$ExtractFloatResult):void");
    }

    private static float[] getFloats(String str) {
        if (str.charAt(0) == 'z' || str.charAt(0) == 'Z') {
            return new float[0];
        }
        try {
            float[] fArr = new float[str.length()];
            ExtractFloatResult extractFloatResult = new ExtractFloatResult();
            int length = str.length();
            int i = 1;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i >= length) {
                    return copyOfRange(fArr, 0, i3);
                }
                extract(str, i, extractFloatResult);
                int i4 = extractFloatResult.mEndPosition;
                int i5 = i3;
                if (i < i4) {
                    fArr[i3] = Float.parseFloat(str.substring(i, i4));
                    i5 = i3 + 1;
                }
                if (extractFloatResult.mEndWithNegOrDot) {
                    i = i4;
                    i2 = i5;
                } else {
                    i = i4 + 1;
                    i2 = i5;
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("error in parsing \"" + str + "\"", e);
        }
    }

    private static int nextStart(String str, int i) {
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            if (((cCharAt - 'A') * (cCharAt - 'Z') <= 0 || (cCharAt - 'a') * (cCharAt - 'z') <= 0) && cCharAt != 'e' && cCharAt != 'E') {
                return i;
            }
            i++;
        }
        return i;
    }

    public static void updateNodes(PathDataNode[] pathDataNodeArr, PathDataNode[] pathDataNodeArr2) {
        for (int i = 0; i < pathDataNodeArr2.length; i++) {
            pathDataNodeArr[i].mType = pathDataNodeArr2[i].mType;
            for (int i2 = 0; i2 < pathDataNodeArr2[i].mParams.length; i2++) {
                pathDataNodeArr[i].mParams[i2] = pathDataNodeArr2[i].mParams[i2];
            }
        }
    }
}
