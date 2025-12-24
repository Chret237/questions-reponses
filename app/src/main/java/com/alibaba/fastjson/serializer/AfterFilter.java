package com.alibaba.fastjson.serializer;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/AfterFilter.class */
public abstract class AfterFilter implements SerializeFilter {
    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<>();
    private static final ThreadLocal<Character> seperatorLocal = new ThreadLocal<>();
    private static final Character COMMA = ',';

    final char writeAfter(JSONSerializer jSONSerializer, Object obj, char c) {
        serializerLocal.set(jSONSerializer);
        seperatorLocal.set(Character.valueOf(c));
        writeAfter(obj);
        serializerLocal.set(null);
        return seperatorLocal.get().charValue();
    }

    public abstract void writeAfter(Object obj);

    protected final void writeKeyValue(String str, Object obj) {
        JSONSerializer jSONSerializer = serializerLocal.get();
        char cCharValue = seperatorLocal.get().charValue();
        jSONSerializer.writeKeyValue(cCharValue, str, obj);
        if (cCharValue != ',') {
            seperatorLocal.set(COMMA);
        }
    }
}
