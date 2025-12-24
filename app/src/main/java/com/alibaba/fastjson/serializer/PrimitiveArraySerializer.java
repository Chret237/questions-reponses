package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/PrimitiveArraySerializer.class */
public class PrimitiveArraySerializer implements ObjectSerializer {
    public static PrimitiveArraySerializer instance = new PrimitiveArraySerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            serializeWriter.write(91);
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (i2 != 0) {
                    serializeWriter.write(44);
                }
                serializeWriter.writeInt(iArr[i2]);
            }
            serializeWriter.write(93);
            return;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            serializeWriter.write(91);
            for (int i3 = 0; i3 < sArr.length; i3++) {
                if (i3 != 0) {
                    serializeWriter.write(44);
                }
                serializeWriter.writeInt(sArr[i3]);
            }
            serializeWriter.write(93);
            return;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            serializeWriter.write(91);
            for (int i4 = 0; i4 < jArr.length; i4++) {
                if (i4 != 0) {
                    serializeWriter.write(44);
                }
                serializeWriter.writeLong(jArr[i4]);
            }
            serializeWriter.write(93);
            return;
        }
        if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            serializeWriter.write(91);
            for (int i5 = 0; i5 < zArr.length; i5++) {
                if (i5 != 0) {
                    serializeWriter.write(44);
                }
                serializeWriter.write(zArr[i5]);
            }
            serializeWriter.write(93);
            return;
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            serializeWriter.write(91);
            for (int i6 = 0; i6 < fArr.length; i6++) {
                if (i6 != 0) {
                    serializeWriter.write(44);
                }
                float f = fArr[i6];
                if (Float.isNaN(f)) {
                    serializeWriter.writeNull();
                } else {
                    serializeWriter.append((CharSequence) Float.toString(f));
                }
            }
            serializeWriter.write(93);
            return;
        }
        if (!(obj instanceof double[])) {
            if (obj instanceof byte[]) {
                serializeWriter.writeByteArray((byte[]) obj);
                return;
            } else {
                serializeWriter.writeString((char[]) obj);
                return;
            }
        }
        double[] dArr = (double[]) obj;
        serializeWriter.write(91);
        for (int i7 = 0; i7 < dArr.length; i7++) {
            if (i7 != 0) {
                serializeWriter.write(44);
            }
            double d = dArr[i7];
            if (Double.isNaN(d)) {
                serializeWriter.writeNull();
            } else {
                serializeWriter.append((CharSequence) Double.toString(d));
            }
        }
        serializeWriter.write(93);
    }
}
