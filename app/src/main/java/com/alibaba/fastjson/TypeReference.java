package com.alibaba.fastjson;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/TypeReference.class */
public class TypeReference<T> {
    protected final Type type;
    static ConcurrentMap<Type, Type> classTypeCache = new ConcurrentHashMap(16, 0.75f, 1);
    public static final Type LIST_STRING = new TypeReference<List<String>>() { // from class: com.alibaba.fastjson.TypeReference.1
    }.getType();

    protected TypeReference() {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Type type2 = classTypeCache.get(type);
        Type type3 = type2;
        if (type2 == null) {
            classTypeCache.putIfAbsent(type, type);
            type3 = classTypeCache.get(type);
        }
        this.type = type3;
    }

    protected TypeReference(Type... typeArr) {
        Class<?> cls = getClass();
        int i = 0;
        ParameterizedType parameterizedType = (ParameterizedType) ((ParameterizedType) cls.getGenericSuperclass()).getActualTypeArguments()[0];
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= actualTypeArguments.length) {
                break;
            }
            int i4 = i3;
            if (actualTypeArguments[i] instanceof TypeVariable) {
                i4 = i3;
                if (i3 < typeArr.length) {
                    actualTypeArguments[i] = typeArr[i3];
                    i4 = i3 + 1;
                }
            }
            if (actualTypeArguments[i] instanceof GenericArrayType) {
                actualTypeArguments[i] = TypeUtils.checkPrimitiveArray((GenericArrayType) actualTypeArguments[i]);
            }
            if (actualTypeArguments[i] instanceof ParameterizedType) {
                actualTypeArguments[i] = handlerParameterizedType((ParameterizedType) actualTypeArguments[i], typeArr, i4);
            }
            i++;
            i2 = i4;
        }
        ParameterizedTypeImpl parameterizedTypeImpl = new ParameterizedTypeImpl(actualTypeArguments, cls, rawType);
        Type type = classTypeCache.get(parameterizedTypeImpl);
        Type type2 = type;
        if (type == null) {
            classTypeCache.putIfAbsent(parameterizedTypeImpl, parameterizedTypeImpl);
            type2 = classTypeCache.get(parameterizedTypeImpl);
        }
        this.type = type2;
    }

    private Type handlerParameterizedType(ParameterizedType parameterizedType, Type[] typeArr, int i) {
        Class<?> cls = getClass();
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        int i2 = i;
        int i3 = 0;
        while (i3 < actualTypeArguments.length) {
            int i4 = i2;
            if (actualTypeArguments[i3] instanceof TypeVariable) {
                i4 = i2;
                if (i2 < typeArr.length) {
                    actualTypeArguments[i3] = typeArr[i2];
                    i4 = i2 + 1;
                }
            }
            if (actualTypeArguments[i3] instanceof GenericArrayType) {
                actualTypeArguments[i3] = TypeUtils.checkPrimitiveArray((GenericArrayType) actualTypeArguments[i3]);
            }
            if (actualTypeArguments[i3] instanceof ParameterizedType) {
                return handlerParameterizedType((ParameterizedType) actualTypeArguments[i3], typeArr, i4);
            }
            i3++;
            i2 = i4;
        }
        return new ParameterizedTypeImpl(actualTypeArguments, cls, rawType);
    }

    public Type getType() {
        return this.type;
    }
}
