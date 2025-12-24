package com.alibaba.fastjson.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/ParameterizedTypeImpl.class */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;

    public ParameterizedTypeImpl(Type[] typeArr, Type type, Type type2) {
        this.actualTypeArguments = typeArr;
        this.ownerType = type;
        this.rawType = type2;
    }

    public boolean equals(Object obj) {
        boolean zEquals = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ParameterizedTypeImpl parameterizedTypeImpl = (ParameterizedTypeImpl) obj;
        if (!Arrays.equals(this.actualTypeArguments, parameterizedTypeImpl.actualTypeArguments)) {
            return false;
        }
        Type type = this.ownerType;
        if (type != null) {
            if (!type.equals(parameterizedTypeImpl.ownerType)) {
                return false;
            }
        } else if (parameterizedTypeImpl.ownerType != null) {
            return false;
        }
        Type type2 = this.rawType;
        Type type3 = parameterizedTypeImpl.rawType;
        if (type2 != null) {
            zEquals = type2.equals(type3);
        } else if (type3 != null) {
            zEquals = false;
        }
        return zEquals;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.rawType;
    }

    public int hashCode() {
        Type[] typeArr = this.actualTypeArguments;
        int iHashCode = 0;
        int iHashCode2 = typeArr != null ? Arrays.hashCode(typeArr) : 0;
        Type type = this.ownerType;
        int iHashCode3 = type != null ? type.hashCode() : 0;
        Type type2 = this.rawType;
        if (type2 != null) {
            iHashCode = type2.hashCode();
        }
        return (((iHashCode2 * 31) + iHashCode3) * 31) + iHashCode;
    }
}
