package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/LongCodec.class */
public class LongCodec implements ObjectSerializer, ObjectDeserializer {
    public static LongCodec instance = new LongCodec();

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Long lCastToLong;
        Long lValueOf;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        try {
            int i = jSONLexer.token();
            if (i == 2) {
                long jLongValue = jSONLexer.longValue();
                jSONLexer.nextToken(16);
                lValueOf = Long.valueOf(jLongValue);
            } else if (i == 3) {
                lValueOf = Long.valueOf(TypeUtils.longValue(jSONLexer.decimalValue()));
                jSONLexer.nextToken(16);
            } else {
                if (i == 12) {
                    JSONObject jSONObject = new JSONObject(true);
                    defaultJSONParser.parseObject((Map) jSONObject);
                    lCastToLong = TypeUtils.castToLong(jSONObject);
                } else {
                    lCastToLong = TypeUtils.castToLong(defaultJSONParser.parse());
                }
                lValueOf = lCastToLong;
                if (lCastToLong == null) {
                    return null;
                }
            }
            Object atomicLong = lValueOf;
            if (type == AtomicLong.class) {
                atomicLong = new AtomicLong(lValueOf.longValue());
            }
            return (T) atomicLong;
        } catch (Exception e) {
            throw new JSONException("parseLong error, field : " + obj, e);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull(SerializerFeature.WriteNullNumberAsZero);
            return;
        }
        long jLongValue = ((Long) obj).longValue();
        serializeWriter.writeLong(jLongValue);
        if (!serializeWriter.isEnabled(SerializerFeature.WriteClassName) || jLongValue > 2147483647L || jLongValue < -2147483648L || type == Long.class || type == Long.TYPE) {
            return;
        }
        serializeWriter.write(76);
    }
}
