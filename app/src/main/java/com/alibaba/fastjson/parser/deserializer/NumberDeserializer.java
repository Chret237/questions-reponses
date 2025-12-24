package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/NumberDeserializer.class */
public class NumberDeserializer implements ObjectDeserializer {
    public static final NumberDeserializer instance = new NumberDeserializer();

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Object objValueOf;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 2) {
            if (type == Double.TYPE || type == Double.class) {
                String strNumberString = jSONLexer.numberString();
                jSONLexer.nextToken(16);
                return (T) Double.valueOf(Double.parseDouble(strNumberString));
            }
            long jLongValue = jSONLexer.longValue();
            jSONLexer.nextToken(16);
            if (type == Short.TYPE || type == Short.class) {
                if (jLongValue <= 32767 && jLongValue >= -32768) {
                    return (T) Short.valueOf((short) jLongValue);
                }
                throw new JSONException("short overflow : " + jLongValue);
            }
            if (type != Byte.TYPE && type != Byte.class) {
                return (jLongValue < -2147483648L || jLongValue > 2147483647L) ? (T) Long.valueOf(jLongValue) : (T) Integer.valueOf((int) jLongValue);
            }
            if (jLongValue <= 127 && jLongValue >= -128) {
                return (T) Byte.valueOf((byte) jLongValue);
            }
            throw new JSONException("short overflow : " + jLongValue);
        }
        if (jSONLexer.token() == 3) {
            if (type == Double.TYPE || type == Double.class) {
                String strNumberString2 = jSONLexer.numberString();
                jSONLexer.nextToken(16);
                return (T) Double.valueOf(Double.parseDouble(strNumberString2));
            }
            if (type == Short.TYPE || type == Short.class) {
                BigDecimal bigDecimalDecimalValue = jSONLexer.decimalValue();
                jSONLexer.nextToken(16);
                return (T) Short.valueOf(TypeUtils.shortValue(bigDecimalDecimalValue));
            }
            if (type == Byte.TYPE || type == Byte.class) {
                BigDecimal bigDecimalDecimalValue2 = jSONLexer.decimalValue();
                jSONLexer.nextToken(16);
                return (T) Byte.valueOf(TypeUtils.byteValue(bigDecimalDecimalValue2));
            }
            T t = (T) jSONLexer.decimalValue();
            jSONLexer.nextToken(16);
            return t;
        }
        if (jSONLexer.token() == 18 && "NaN".equals(jSONLexer.stringVal())) {
            jSONLexer.nextToken();
            if (type == Double.class) {
                objValueOf = Double.valueOf(Double.NaN);
            } else {
                objValueOf = null;
                if (type == Float.class) {
                    objValueOf = Float.valueOf(Float.NaN);
                }
            }
            return (T) objValueOf;
        }
        Object obj2 = defaultJSONParser.parse();
        if (obj2 == null) {
            return null;
        }
        if (type == Double.TYPE || type == Double.class) {
            try {
                return (T) TypeUtils.castToDouble(obj2);
            } catch (Exception e) {
                throw new JSONException("parseDouble error, field : " + obj, e);
            }
        }
        if (type == Short.TYPE || type == Short.class) {
            try {
                return (T) TypeUtils.castToShort(obj2);
            } catch (Exception e2) {
                throw new JSONException("parseShort error, field : " + obj, e2);
            }
        }
        if (type != Byte.TYPE && type != Byte.class) {
            return (T) TypeUtils.castToBigDecimal(obj2);
        }
        try {
            return (T) TypeUtils.castToByte(obj2);
        } catch (Exception e3) {
            throw new JSONException("parseByte error, field : " + obj, e3);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
