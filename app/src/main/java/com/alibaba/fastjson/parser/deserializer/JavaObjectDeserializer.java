package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/JavaObjectDeserializer.class */
public class JavaObjectDeserializer implements ObjectDeserializer {
    public static final JavaObjectDeserializer instance = new JavaObjectDeserializer();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        if (!(type instanceof GenericArrayType)) {
            return (!(type instanceof Class) || type == Object.class || type == Serializable.class || type == Cloneable.class || type == Closeable.class || type == Comparable.class) ? (T) defaultJSONParser.parse(obj) : (T) defaultJSONParser.parseObject(type);
        }
        Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
        Type type2 = genericComponentType;
        if (genericComponentType instanceof TypeVariable) {
            type2 = ((TypeVariable) genericComponentType).getBounds()[0];
        }
        ArrayList arrayList = new ArrayList();
        defaultJSONParser.parseArray(type2, arrayList);
        T t = (T) ((Object[]) Array.newInstance(TypeUtils.getRawClass(type2), arrayList.size()));
        arrayList.toArray((Object[]) t);
        return t;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
