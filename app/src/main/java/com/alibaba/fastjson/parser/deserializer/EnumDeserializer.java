package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/EnumDeserializer.class */
public class EnumDeserializer implements ObjectDeserializer {
    protected final Class<?> enumClass;
    protected long[] enumNameHashCodes;
    protected final Enum[] enums;
    protected final Enum[] ordinalEnums;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x018d A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v92, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public EnumDeserializer(java.lang.Class<?> r6) {
        /*
            Method dump skipped, instructions count: 542
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.EnumDeserializer.<init>(java.lang.Class):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v78, types: [int] */
    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        try {
            JSONLexer jSONLexer = defaultJSONParser.lexer;
            int i = jSONLexer.token();
            if (i == 2) {
                int iIntValue = jSONLexer.intValue();
                jSONLexer.nextToken(16);
                if (iIntValue >= 0 && iIntValue <= this.ordinalEnums.length) {
                    return (T) this.ordinalEnums[iIntValue];
                }
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + iIntValue);
            }
            if (i != 4) {
                if (i == 8) {
                    jSONLexer.nextToken(16);
                    return null;
                }
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + defaultJSONParser.parse());
            }
            String strStringVal = jSONLexer.stringVal();
            jSONLexer.nextToken(16);
            if (strStringVal.length() == 0) {
                return null;
            }
            long j = -3750763034362895579L;
            long j2 = -3750763034362895579L;
            for (int i2 = 0; i2 < strStringVal.length(); i2++) {
                char cCharAt = strStringVal.charAt(i2);
                long j3 = cCharAt;
                char c = cCharAt;
                if (cCharAt >= 'A') {
                    c = cCharAt;
                    if (cCharAt <= 'Z') {
                        c = cCharAt + ' ';
                    }
                }
                j = (j ^ j3) * 1099511628211L;
                j2 = (j2 ^ c) * 1099511628211L;
            }
            Enum enumByHashCode = getEnumByHashCode(j);
            Enum enumByHashCode2 = enumByHashCode;
            if (enumByHashCode == null) {
                enumByHashCode2 = enumByHashCode;
                if (j2 != j) {
                    enumByHashCode2 = getEnumByHashCode(j2);
                }
            }
            if (enumByHashCode2 == null && jSONLexer.isEnabled(Feature.ErrorOnEnumNotMatch)) {
                throw new JSONException("not match enum value, " + this.enumClass.getName() + " : " + strStringVal);
            }
            return (T) enumByHashCode2;
        } catch (JSONException e) {
            throw e;
        } catch (Exception e2) {
            throw new JSONException(e2.getMessage(), e2);
        }
    }

    public Enum getEnumByHashCode(long j) {
        int iBinarySearch;
        if (this.enums != null && (iBinarySearch = Arrays.binarySearch(this.enumNameHashCodes, j)) >= 0) {
            return this.enums[iBinarySearch];
        }
        return null;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }

    public Enum<?> valueOf(int i) {
        return this.ordinalEnums[i];
    }
}
