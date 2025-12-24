package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/JavaBeanInfo.class */
public class JavaBeanInfo {
    public final Method buildMethod;
    public final Class<?> builderClass;
    public final Class<?> clazz;
    public final Constructor<?> creatorConstructor;
    public Type[] creatorConstructorParameterTypes;
    public String[] creatorConstructorParameters;
    public final Constructor<?> defaultConstructor;
    public final int defaultConstructorParameterSize;
    public final Method factoryMethod;
    public final FieldInfo[] fields;
    public final JSONType jsonType;
    public boolean kotlin;
    public Constructor<?> kotlinDefaultConstructor;
    public String[] orders;
    public final int parserFeatures;
    public final FieldInfo[] sortedFields;
    public final String typeKey;
    public final String typeName;

    public JavaBeanInfo(Class<?> cls, Class<?> cls2, Constructor<?> constructor, Constructor<?> constructor2, Method method, Method method2, JSONType jSONType, List<FieldInfo> list) {
        boolean z;
        JSONField jSONField;
        int i;
        this.clazz = cls;
        this.builderClass = cls2;
        this.defaultConstructor = constructor;
        this.creatorConstructor = constructor2;
        this.factoryMethod = method;
        this.parserFeatures = TypeUtils.getParserFeatures(cls);
        this.buildMethod = method2;
        this.jsonType = jSONType;
        if (jSONType != null) {
            String strTypeName = jSONType.typeName();
            String strTypeKey = jSONType.typeKey();
            this.typeKey = strTypeKey.length() <= 0 ? null : strTypeKey;
            if (strTypeName.length() != 0) {
                this.typeName = strTypeName;
            } else {
                this.typeName = cls.getName();
            }
            String[] strArrOrders = jSONType.orders();
            this.orders = strArrOrders.length == 0 ? null : strArrOrders;
        } else {
            this.typeName = cls.getName();
            this.typeKey = null;
            this.orders = null;
        }
        FieldInfo[] fieldInfoArr = new FieldInfo[list.size()];
        this.fields = fieldInfoArr;
        list.toArray(fieldInfoArr);
        FieldInfo[] fieldInfoArr2 = this.fields;
        FieldInfo[] fieldInfoArr3 = new FieldInfo[fieldInfoArr2.length];
        if (this.orders != null) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(list.size());
            for (FieldInfo fieldInfo : this.fields) {
                linkedHashMap.put(fieldInfo.name, fieldInfo);
            }
            String[] strArr = this.orders;
            int length = strArr.length;
            int i2 = 0;
            int i3 = 0;
            while (true) {
                i = i3;
                if (i2 >= length) {
                    break;
                }
                String str = strArr[i2];
                FieldInfo fieldInfo2 = (FieldInfo) linkedHashMap.get(str);
                int i4 = i;
                if (fieldInfo2 != null) {
                    fieldInfoArr3[i] = fieldInfo2;
                    linkedHashMap.remove(str);
                    i4 = i + 1;
                }
                i2++;
                i3 = i4;
            }
            Iterator it = linkedHashMap.values().iterator();
            while (it.hasNext()) {
                fieldInfoArr3[i] = (FieldInfo) it.next();
                i++;
            }
        } else {
            System.arraycopy(fieldInfoArr2, 0, fieldInfoArr3, 0, fieldInfoArr2.length);
            Arrays.sort(fieldInfoArr3);
        }
        this.sortedFields = Arrays.equals(this.fields, fieldInfoArr3) ? this.fields : fieldInfoArr3;
        if (constructor != null) {
            this.defaultConstructorParameterSize = constructor.getParameterTypes().length;
        } else if (method != null) {
            this.defaultConstructorParameterSize = method.getParameterTypes().length;
        } else {
            this.defaultConstructorParameterSize = 0;
        }
        if (constructor2 != null) {
            this.creatorConstructorParameterTypes = constructor2.getParameterTypes();
            boolean zIsKotlin = TypeUtils.isKotlin(cls);
            this.kotlin = zIsKotlin;
            if (!zIsKotlin) {
                if (this.creatorConstructorParameterTypes.length == this.fields.length) {
                    int i5 = 0;
                    while (true) {
                        Type[] typeArr = this.creatorConstructorParameterTypes;
                        if (i5 >= typeArr.length) {
                            z = true;
                            break;
                        } else {
                            if (typeArr[i5] != this.fields[i5].fieldClass) {
                                z = false;
                                break;
                            }
                            i5++;
                        }
                    }
                } else {
                    z = false;
                }
                if (z) {
                    return;
                }
                this.creatorConstructorParameters = ASMUtils.lookupParameterNames(constructor2);
                return;
            }
            this.creatorConstructorParameters = TypeUtils.getKoltinConstructorParameters(cls);
            try {
                this.kotlinDefaultConstructor = cls.getConstructor(new Class[0]);
            } catch (Throwable th) {
            }
            Annotation[][] parameterAnnotations = TypeUtils.getParameterAnnotations(constructor2);
            for (int i6 = 0; i6 < this.creatorConstructorParameters.length && i6 < parameterAnnotations.length; i6++) {
                Annotation[] annotationArr = parameterAnnotations[i6];
                int length2 = annotationArr.length;
                int i7 = 0;
                while (true) {
                    if (i7 >= length2) {
                        jSONField = null;
                        break;
                    }
                    Annotation annotation = annotationArr[i7];
                    if (annotation instanceof JSONField) {
                        jSONField = (JSONField) annotation;
                        break;
                    }
                    i7++;
                }
                if (jSONField != null) {
                    String strName = jSONField.name();
                    if (strName.length() > 0) {
                        this.creatorConstructorParameters[i6] = strName;
                    }
                }
            }
        }
    }

    static boolean add(List<FieldInfo> list, FieldInfo fieldInfo) {
        for (int size = list.size() - 1; size >= 0; size--) {
            FieldInfo fieldInfo2 = list.get(size);
            if (fieldInfo2.name.equals(fieldInfo.name) && (!fieldInfo2.getOnly || fieldInfo.getOnly)) {
                if (fieldInfo2.fieldClass.isAssignableFrom(fieldInfo.fieldClass)) {
                    list.set(size, fieldInfo);
                    return true;
                }
                if (fieldInfo2.compareTo(fieldInfo) >= 0) {
                    return false;
                }
                list.set(size, fieldInfo);
                return true;
            }
        }
        list.add(fieldInfo);
        return true;
    }

    public static JavaBeanInfo build(Class<?> cls, Type type, PropertyNamingStrategy propertyNamingStrategy) {
        return build(cls, type, propertyNamingStrategy, false, TypeUtils.compatibleWithJavaBean, false);
    }

    public static JavaBeanInfo build(Class<?> cls, Type type, PropertyNamingStrategy propertyNamingStrategy, boolean z, boolean z2) {
        return build(cls, type, propertyNamingStrategy, z, z2, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x038f  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x07c7  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x099c  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0a47  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0b9d A[PHI: r23 r24 r25
  0x0b9d: PHI (r23v4 int) = (r23v3 int), (r23v7 int) binds: [B:358:0x0b2b, B:363:0x0b72] A[DONT_GENERATE, DONT_INLINE]
  0x0b9d: PHI (r24v4 int) = (r24v3 int), (r24v7 int) binds: [B:358:0x0b2b, B:363:0x0b72] A[DONT_GENERATE, DONT_INLINE]
  0x0b9d: PHI (r25v1 int) = (r25v0 int), (r25v4 int) binds: [B:358:0x0b2b, B:363:0x0b72] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0bb8  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x0cc3  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0d1f  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0da8  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x0db0  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x0e11  */
    /* JADX WARN: Removed duplicated region for block: B:485:0x0f94  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x025d A[PHI: r31
  0x025d: PHI (r31v75 java.lang.String[]) = (r31v74 java.lang.String[]), (r31v77 java.lang.String[]) binds: [B:74:0x0226, B:82:0x024b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.alibaba.fastjson.util.JavaBeanInfo build(java.lang.Class<?> r16, java.lang.reflect.Type r17, com.alibaba.fastjson.PropertyNamingStrategy r18, boolean r19, boolean r20, boolean r21) {
        /*
            Method dump skipped, instructions count: 4056
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.JavaBeanInfo.build(java.lang.Class, java.lang.reflect.Type, com.alibaba.fastjson.PropertyNamingStrategy, boolean, boolean, boolean):com.alibaba.fastjson.util.JavaBeanInfo");
    }

    private static Map<TypeVariable, Type> buildGenericInfo(Class<?> cls) {
        Class<? super Object> superclass = cls.getSuperclass();
        HashMap map = null;
        Class<? super Object> superclass2 = superclass;
        if (superclass == null) {
            return null;
        }
        while (true) {
            Class<?> cls2 = cls;
            cls = superclass2;
            if (cls == null || cls == Object.class) {
                break;
            }
            HashMap map2 = map;
            if (cls2.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) cls2.getGenericSuperclass()).getActualTypeArguments();
                TypeVariable<Class<?>>[] typeParameters = cls.getTypeParameters();
                int i = 0;
                while (true) {
                    map2 = map;
                    if (i < actualTypeArguments.length) {
                        HashMap map3 = map;
                        if (map == null) {
                            map3 = new HashMap();
                        }
                        if (map3.containsKey(actualTypeArguments[i])) {
                            map3.put(typeParameters[i], map3.get(actualTypeArguments[i]));
                        } else {
                            map3.put(typeParameters[i], actualTypeArguments[i]);
                        }
                        i++;
                        map = map3;
                    }
                }
            }
            superclass2 = cls.getSuperclass();
            map = map2;
        }
        return map;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void computeFields(java.lang.Class<?> r16, java.lang.reflect.Type r17, com.alibaba.fastjson.PropertyNamingStrategy r18, java.util.List<com.alibaba.fastjson.util.FieldInfo> r19, java.lang.reflect.Field[] r20) {
        /*
            Method dump skipped, instructions count: 356
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.JavaBeanInfo.computeFields(java.lang.Class, java.lang.reflect.Type, com.alibaba.fastjson.PropertyNamingStrategy, java.util.List, java.lang.reflect.Field[]):void");
    }

    public static Class<?> getBuilderClass(JSONType jSONType) {
        return getBuilderClass(null, jSONType);
    }

    public static Class<?> getBuilderClass(Class<?> cls, JSONType jSONType) {
        Class<?> clsBuilder;
        if (cls != null && cls.getName().equals("org.springframework.security.web.savedrequest.DefaultSavedRequest")) {
            return TypeUtils.loadClass("org.springframework.security.web.savedrequest.DefaultSavedRequest$Builder");
        }
        if (jSONType == null || (clsBuilder = jSONType.builder()) == Void.class) {
            return null;
        }
        return clsBuilder;
    }

    public static Constructor<?> getCreatorConstructor(Constructor[] constructorArr) {
        boolean z;
        Constructor constructor;
        boolean z2;
        int length = constructorArr.length;
        Constructor constructor2 = null;
        int i = 0;
        while (i < length) {
            Constructor constructor3 = constructorArr[i];
            Constructor constructor4 = constructor2;
            if (((JSONCreator) constructor3.getAnnotation(JSONCreator.class)) != null) {
                if (constructor2 != null) {
                    throw new JSONException("multi-JSONCreator");
                }
                constructor4 = constructor3;
            }
            i++;
            constructor2 = constructor4;
        }
        if (constructor2 != null) {
            return constructor2;
        }
        int length2 = constructorArr.length;
        int i2 = 0;
        while (i2 < length2) {
            Constructor constructor5 = constructorArr[i2];
            Annotation[][] parameterAnnotations = TypeUtils.getParameterAnnotations(constructor5);
            if (parameterAnnotations.length == 0) {
                constructor = constructor2;
            } else {
                int length3 = parameterAnnotations.length;
                int i3 = 0;
                while (true) {
                    z = true;
                    if (i3 >= length3) {
                        break;
                    }
                    Annotation[] annotationArr = parameterAnnotations[i3];
                    int length4 = annotationArr.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length4) {
                            z2 = false;
                            break;
                        }
                        if (annotationArr[i4] instanceof JSONField) {
                            z2 = true;
                            break;
                        }
                        i4++;
                    }
                    if (!z2) {
                        z = false;
                        break;
                    }
                    i3++;
                }
                constructor = constructor2;
                if (!z) {
                    continue;
                } else {
                    if (constructor2 != null) {
                        throw new JSONException("multi-JSONCreator");
                    }
                    constructor = constructor5;
                }
            }
            i2++;
            constructor2 = constructor;
        }
        if (constructor2 != null) {
        }
        return constructor2;
    }

    static Constructor<?> getDefaultConstructor(Class<?> cls, Constructor<?>[] constructorArr) {
        Constructor<?> constructor;
        if (Modifier.isAbstract(cls.getModifiers())) {
            return null;
        }
        int length = constructorArr.length;
        int i = 0;
        while (true) {
            constructor = null;
            if (i >= length) {
                break;
            }
            constructor = constructorArr[i];
            if (constructor.getParameterTypes().length == 0) {
                break;
            }
            i++;
        }
        Constructor<?> constructor2 = constructor;
        if (constructor == null) {
            constructor2 = constructor;
            if (cls.isMemberClass()) {
                constructor2 = constructor;
                if (!Modifier.isStatic(cls.getModifiers())) {
                    int length2 = constructorArr.length;
                    int i2 = 0;
                    while (true) {
                        constructor2 = constructor;
                        if (i2 >= length2) {
                            break;
                        }
                        constructor2 = constructorArr[i2];
                        Class<?>[] parameterTypes = constructor2.getParameterTypes();
                        if (parameterTypes.length == 1 && parameterTypes[0].equals(cls.getDeclaringClass())) {
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
        return constructor2;
    }

    private static Method getFactoryMethod(Class<?> cls, Method[] methodArr, boolean z) {
        Method method;
        int length = methodArr.length;
        Method method2 = null;
        int i = 0;
        while (i < length) {
            Method method3 = methodArr[i];
            if (!Modifier.isStatic(method3.getModifiers())) {
                method = method2;
            } else if (cls.isAssignableFrom(method3.getReturnType())) {
                method = method2;
                if (((JSONCreator) TypeUtils.getAnnotation(method3, JSONCreator.class)) == null) {
                    continue;
                } else {
                    if (method2 != null) {
                        throw new JSONException("multi-JSONCreator");
                    }
                    method = method3;
                }
            } else {
                method = method2;
            }
            i++;
            method2 = method;
        }
        Method method4 = method2;
        if (method2 == null) {
            method4 = method2;
            if (z) {
                int length2 = methodArr.length;
                int i2 = 0;
                while (true) {
                    method4 = method2;
                    if (i2 >= length2) {
                        break;
                    }
                    method4 = methodArr[i2];
                    if (TypeUtils.isJacksonCreator(method4)) {
                        break;
                    }
                    i2++;
                }
            }
        }
        return method4;
    }

    private static FieldInfo getField(List<FieldInfo> list, String str) {
        for (FieldInfo fieldInfo : list) {
            if (fieldInfo.name.equals(str)) {
                return fieldInfo;
            }
            Field field = fieldInfo.field;
            if (field != null && fieldInfo.getAnnotation() != null && field.getName().equals(str)) {
                return fieldInfo;
            }
        }
        return null;
    }
}
