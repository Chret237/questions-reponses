package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/ObjectArrayCodec.class */
public class ObjectArrayCodec implements ObjectSerializer, ObjectDeserializer {
    public static final ObjectArrayCodec instance = new ObjectArrayCodec();

    /* JADX WARN: Removed duplicated region for block: B:31:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private <T> T toObjectArray(com.alibaba.fastjson.parser.DefaultJSONParser r6, java.lang.Class<?> r7, com.alibaba.fastjson.JSONArray r8) throws java.lang.ArrayIndexOutOfBoundsException, java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 237
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ObjectArrayCodec.toObjectArray(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.Class, com.alibaba.fastjson.JSONArray):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r0v73, types: [java.lang.reflect.Type[]] */
    /* JADX WARN: Type inference failed for: r0v74 */
    /* JADX WARN: Type inference failed for: r0v75, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r0v9, types: [T, byte[]] */
    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Class<?> componentType;
        Class<?> cls;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = jSONLexer.token();
        Class<?> cls2 = null;
        if (i == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        if (i == 4 || i == 26) {
            ?? r0 = (T) jSONLexer.bytesValue();
            jSONLexer.nextToken(16);
            if (r0.length != 0 || type == byte[].class) {
                return r0;
            }
            return null;
        }
        if (type instanceof GenericArrayType) {
            ?? genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            if (genericComponentType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) genericComponentType;
                Type type2 = defaultJSONParser.getContext().type;
                int i2 = 0;
                if (type2 instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type2;
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class) {
                        TypeVariable<Class<T>>[] typeParameters = ((Class) rawType).getTypeParameters();
                        Class<?> cls3 = null;
                        while (true) {
                            cls2 = cls3;
                            if (i2 >= typeParameters.length) {
                                break;
                            }
                            if (typeParameters[i2].getName().equals(typeVariable.getName())) {
                                cls3 = parameterizedType.getActualTypeArguments()[i2];
                            }
                            i2++;
                        }
                    }
                    if (cls2 instanceof Class) {
                        cls = cls2;
                        componentType = genericComponentType;
                    } else {
                        cls = Object.class;
                        componentType = genericComponentType;
                    }
                } else {
                    cls = TypeUtils.getClass(typeVariable.getBounds()[0]);
                    componentType = genericComponentType;
                }
            } else {
                cls = TypeUtils.getClass(genericComponentType);
                componentType = genericComponentType;
            }
        } else {
            componentType = ((Class) type).getComponentType();
            cls = componentType;
        }
        JSONArray jSONArray = new JSONArray();
        defaultJSONParser.parseArray(componentType, jSONArray, obj);
        return (T) toObjectArray(defaultJSONParser, cls, jSONArray);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 14;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        Object[] objArr = (Object[]) obj;
        if (obj == null) {
            serializeWriter.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        int length = objArr.length;
        int i2 = length - 1;
        if (i2 == -1) {
            serializeWriter.append((CharSequence) "[]");
            return;
        }
        SerialContext serialContext = jSONSerializer.context;
        jSONSerializer.setContext(serialContext, obj, obj2, 0);
        try {
            serializeWriter.append('[');
            if (serializeWriter.isEnabled(SerializerFeature.PrettyFormat)) {
                jSONSerializer.incrementIndent();
                jSONSerializer.println();
                for (int i3 = 0; i3 < length; i3++) {
                    if (i3 != 0) {
                        serializeWriter.write(44);
                        jSONSerializer.println();
                    }
                    jSONSerializer.write(objArr[i3]);
                }
                jSONSerializer.decrementIdent();
                jSONSerializer.println();
                serializeWriter.write(93);
                return;
            }
            Class<?> cls = null;
            ObjectSerializer objectWriter = null;
            for (int i4 = 0; i4 < i2; i4++) {
                Object obj3 = objArr[i4];
                if (obj3 == null) {
                    serializeWriter.append((CharSequence) "null,");
                } else {
                    if (jSONSerializer.containsReference(obj3)) {
                        jSONSerializer.writeReference(obj3);
                    } else {
                        Class<?> cls2 = obj3.getClass();
                        if (cls2 == cls) {
                            objectWriter.write(jSONSerializer, obj3, Integer.valueOf(i4), null, 0);
                        } else {
                            objectWriter = jSONSerializer.getObjectWriter(cls2);
                            objectWriter.write(jSONSerializer, obj3, Integer.valueOf(i4), null, 0);
                            cls = cls2;
                        }
                    }
                    serializeWriter.append(',');
                }
            }
            Object obj4 = objArr[i2];
            if (obj4 == null) {
                serializeWriter.append((CharSequence) "null]");
            } else {
                if (jSONSerializer.containsReference(obj4)) {
                    jSONSerializer.writeReference(obj4);
                } else {
                    jSONSerializer.writeWithFieldName(obj4, Integer.valueOf(i2));
                }
                serializeWriter.append(']');
            }
        } finally {
            jSONSerializer.context = serialContext;
        }
    }
}
