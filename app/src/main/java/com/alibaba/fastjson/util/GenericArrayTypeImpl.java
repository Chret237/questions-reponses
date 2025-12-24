package com.alibaba.fastjson.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/GenericArrayTypeImpl.class */
public class GenericArrayTypeImpl implements GenericArrayType {
    static final boolean $assertionsDisabled = false;
    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type type) {
        this.genericComponentType = type;
    }

    public boolean equals(Object obj) {
        if (obj instanceof GenericArrayType) {
            return this.genericComponentType.equals(((GenericArrayType) obj).getGenericComponentType());
        }
        return false;
    }

    @Override // java.lang.reflect.GenericArrayType
    public Type getGenericComponentType() {
        return this.genericComponentType;
    }

    public int hashCode() {
        return this.genericComponentType.hashCode();
    }

    public String toString() {
        Type genericComponentType = getGenericComponentType();
        StringBuilder sb = new StringBuilder();
        if (genericComponentType instanceof Class) {
            sb.append(((Class) genericComponentType).getName());
        } else {
            sb.append(genericComponentType.toString());
        }
        sb.append("[]");
        return sb.toString();
    }
}
