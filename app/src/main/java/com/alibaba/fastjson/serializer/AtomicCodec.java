package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/AtomicCodec.class */
public class AtomicCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AtomicCodec instance = new AtomicCodec();

    /* JADX WARN: Type inference failed for: r0v13, types: [T, java.util.concurrent.atomic.AtomicIntegerArray] */
    /* JADX WARN: Type inference failed for: r0v8, types: [T, java.util.concurrent.atomic.AtomicLongArray] */
    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        if (defaultJSONParser.lexer.token() == 8) {
            defaultJSONParser.lexer.nextToken(16);
            return null;
        }
        JSONArray jSONArray = new JSONArray();
        defaultJSONParser.parseArray(jSONArray);
        if (type == AtomicIntegerArray.class) {
            ?? r0 = (T) new AtomicIntegerArray(jSONArray.size());
            for (int i = 0; i < jSONArray.size(); i++) {
                r0.set(i, jSONArray.getInteger(i).intValue());
            }
            return r0;
        }
        ?? r02 = (T) new AtomicLongArray(jSONArray.size());
        for (int i2 = 0; i2 < jSONArray.size(); i2++) {
            r02.set(i2, jSONArray.getLong(i2).longValue());
        }
        return r02;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 14;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj instanceof AtomicInteger) {
            serializeWriter.writeInt(((AtomicInteger) obj).get());
            return;
        }
        if (obj instanceof AtomicLong) {
            serializeWriter.writeLong(((AtomicLong) obj).get());
            return;
        }
        if (obj instanceof AtomicBoolean) {
            serializeWriter.append(((AtomicBoolean) obj).get() ? "true" : "false");
            return;
        }
        if (obj == null) {
            serializeWriter.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        if (!(obj instanceof AtomicIntegerArray)) {
            AtomicLongArray atomicLongArray = (AtomicLongArray) obj;
            int length = atomicLongArray.length();
            serializeWriter.write(91);
            for (int i2 = 0; i2 < length; i2++) {
                long j = atomicLongArray.get(i2);
                if (i2 != 0) {
                    serializeWriter.write(44);
                }
                serializeWriter.writeLong(j);
            }
            serializeWriter.write(93);
            return;
        }
        AtomicIntegerArray atomicIntegerArray = (AtomicIntegerArray) obj;
        int length2 = atomicIntegerArray.length();
        serializeWriter.write(91);
        for (int i3 = 0; i3 < length2; i3++) {
            int i4 = atomicIntegerArray.get(i3);
            if (i3 != 0) {
                serializeWriter.write(44);
            }
            serializeWriter.writeInt(i4);
        }
        serializeWriter.write(93);
    }
}
