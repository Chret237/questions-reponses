package com.alibaba.fastjson.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.clans.fab.BuildConfig;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/FieldInfo.class */
public class FieldInfo implements Comparable<FieldInfo> {
    public final String[] alternateNames;
    public final Class<?> declaringClass;
    public final Field field;
    public final boolean fieldAccess;
    private final JSONField fieldAnnotation;
    public final Class<?> fieldClass;
    public final boolean fieldTransient;
    public final Type fieldType;
    public final String format;
    public final boolean getOnly;
    public final boolean isEnum;
    public final boolean jsonDirect;
    public final String label;
    public final Method method;
    private final JSONField methodAnnotation;
    public final String name;
    public final char[] name_chars;
    private int ordinal;
    public final int parserFeatures;
    public final int serialzeFeatures;
    public final boolean unwrapped;

    public FieldInfo(String str, Class<?> cls, Class<?> cls2, Type type, Field field, int i, int i2, int i3) {
        this.ordinal = 0;
        int i4 = i < 0 ? 0 : i;
        this.name = str;
        this.declaringClass = cls;
        this.fieldClass = cls2;
        this.fieldType = type;
        this.method = null;
        this.field = field;
        this.ordinal = i4;
        this.serialzeFeatures = i2;
        this.parserFeatures = i3;
        this.isEnum = cls2.isEnum();
        if (field != null) {
            int modifiers = field.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || this.method == null;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldTransient = false;
            this.fieldAccess = false;
        }
        this.name_chars = genFieldNameChars();
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        this.label = BuildConfig.FLAVOR;
        this.fieldAnnotation = field == null ? null : (JSONField) TypeUtils.getAnnotation(field, JSONField.class);
        this.methodAnnotation = null;
        this.getOnly = false;
        this.jsonDirect = false;
        this.unwrapped = false;
        this.format = null;
        this.alternateNames = new String[0];
    }

    public FieldInfo(String str, Method method, Field field, Class<?> cls, Type type, int i, int i2, int i3, JSONField jSONField, JSONField jSONField2, String str2) {
        this(str, method, field, cls, type, i, i2, i3, jSONField, jSONField2, str2, null);
    }

    public FieldInfo(String str, Method method, Field field, Class<?> cls, Type type, int i, int i2, int i3, JSONField jSONField, JSONField jSONField2, String str2, Map<TypeVariable, Type> map) {
        boolean zJsonDirect;
        Class<?> type2;
        boolean zIsFinal;
        Type genericReturnType;
        Type inheritGenericType;
        Class<?> returnType;
        Class<?> cls2;
        Type type3;
        this.ordinal = 0;
        String str3 = str;
        if (field != null) {
            String name = field.getName();
            str3 = str;
            if (name.equals(str)) {
                str3 = name;
            }
        }
        int i4 = i < 0 ? 0 : i;
        this.name = str3;
        this.method = method;
        this.field = field;
        this.ordinal = i4;
        this.serialzeFeatures = i2;
        this.parserFeatures = i3;
        this.fieldAnnotation = jSONField;
        this.methodAnnotation = jSONField2;
        if (field != null) {
            int modifiers = field.getModifiers();
            this.fieldAccess = (modifiers & 1) != 0 || method == null;
            this.fieldTransient = Modifier.isTransient(modifiers) || TypeUtils.isTransient(method);
        } else {
            this.fieldAccess = false;
            this.fieldTransient = TypeUtils.isTransient(method);
        }
        if (str2 == null || str2.length() <= 0) {
            this.label = BuildConfig.FLAVOR;
        } else {
            this.label = str2;
        }
        JSONField annotation = getAnnotation();
        String str4 = null;
        if (annotation != null) {
            String str5 = annotation.format();
            str4 = str5.trim().length() != 0 ? str5 : str4;
            zJsonDirect = annotation.jsonDirect();
            this.unwrapped = annotation.unwrapped();
            this.alternateNames = annotation.alternateNames();
        } else {
            this.unwrapped = false;
            this.alternateNames = new String[0];
            zJsonDirect = false;
            str4 = null;
        }
        this.format = str4;
        this.name_chars = genFieldNameChars();
        if (method != null) {
            TypeUtils.setAccessible(method);
        }
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        if (method != null) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                cls2 = parameterTypes[0];
                type3 = method.getGenericParameterTypes()[0];
            } else if (parameterTypes.length == 2 && parameterTypes[0] == String.class && parameterTypes[1] == Object.class) {
                cls2 = parameterTypes[0];
                type3 = cls2;
            } else {
                returnType = method.getReturnType();
                genericReturnType = method.getGenericReturnType();
                zIsFinal = true;
                this.declaringClass = method.getDeclaringClass();
                type2 = returnType;
            }
            zIsFinal = false;
            returnType = cls2;
            genericReturnType = type3;
            this.declaringClass = method.getDeclaringClass();
            type2 = returnType;
        } else {
            type2 = field.getType();
            Type genericType = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
            zIsFinal = Modifier.isFinal(field.getModifiers());
            genericReturnType = genericType;
        }
        this.getOnly = zIsFinal;
        boolean z = false;
        if (zJsonDirect) {
            z = false;
            if (type2 == String.class) {
                z = true;
            }
        }
        this.jsonDirect = z;
        if (cls != null && type2 == Object.class && (genericReturnType instanceof TypeVariable) && (inheritGenericType = getInheritGenericType(cls, type, (TypeVariable) genericReturnType)) != null) {
            this.fieldClass = TypeUtils.getClass(inheritGenericType);
            this.fieldType = inheritGenericType;
            this.isEnum = type2.isEnum();
            return;
        }
        Class<?> cls3 = type2;
        Type fieldType = genericReturnType;
        if (!(genericReturnType instanceof Class)) {
            fieldType = getFieldType(cls, type == null ? cls : type, genericReturnType, map);
            cls3 = type2;
            if (fieldType != genericReturnType) {
                if (fieldType instanceof ParameterizedType) {
                    cls3 = TypeUtils.getClass(fieldType);
                } else {
                    cls3 = type2;
                    if (fieldType instanceof Class) {
                        cls3 = TypeUtils.getClass(fieldType);
                    }
                }
            }
        }
        this.fieldType = fieldType;
        this.fieldClass = cls3;
        this.isEnum = cls3.isEnum();
    }

    private static boolean getArgument(Type[] typeArr, Map<TypeVariable, Type> map) {
        boolean z;
        int i = 0;
        if (map == null || map.size() == 0) {
            return false;
        }
        boolean z2 = false;
        while (true) {
            boolean z3 = z2;
            if (i >= typeArr.length) {
                return z3;
            }
            Type type = typeArr[i];
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                z = z3;
                if (getArgument(actualTypeArguments, map)) {
                    typeArr[i] = new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
                    z = true;
                }
            } else {
                z = z3;
                if (type instanceof TypeVariable) {
                    z = z3;
                    if (map.containsKey(type)) {
                        typeArr[i] = map.get(type);
                        z = true;
                    }
                }
            }
            i++;
            z2 = z;
        }
    }

    private static boolean getArgument(Type[] typeArr, TypeVariable[] typeVariableArr, Type[] typeArr2) {
        boolean z;
        if (typeArr2 == null || typeVariableArr.length == 0) {
            return false;
        }
        int i = 0;
        boolean z2 = false;
        while (true) {
            boolean z3 = z2;
            if (i >= typeArr.length) {
                return z3;
            }
            Type type = typeArr[i];
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                z = z3;
                if (getArgument(actualTypeArguments, typeVariableArr, typeArr2)) {
                    typeArr[i] = new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
                    z = true;
                }
            } else {
                z = z3;
                if (type instanceof TypeVariable) {
                    int i2 = 0;
                    while (true) {
                        z = z3;
                        if (i2 < typeVariableArr.length) {
                            if (type.equals(typeVariableArr[i2])) {
                                typeArr[i] = typeArr2[i2];
                                z3 = true;
                            }
                            i2++;
                        }
                    }
                }
            }
            i++;
            z2 = z;
        }
    }

    public static Type getFieldType(Class<?> cls, Type type, Type type2) {
        return getFieldType(cls, type, type2, null);
    }

    public static Type getFieldType(Class<?> cls, Type type, Type type2, Map<TypeVariable, Type> map) {
        TypeVariable<Class<?>>[] typeParameters;
        ParameterizedType parameterizedType;
        if (cls != null && type != null) {
            if (type2 instanceof GenericArrayType) {
                Type genericComponentType = ((GenericArrayType) type2).getGenericComponentType();
                Type fieldType = getFieldType(cls, type, genericComponentType, map);
                return genericComponentType != fieldType ? Array.newInstance(TypeUtils.getClass(fieldType), 0).getClass() : type2;
            }
            if (!TypeUtils.isGenericParamType(type)) {
                return type2;
            }
            if (type2 instanceof TypeVariable) {
                ParameterizedType parameterizedType2 = (ParameterizedType) TypeUtils.getGenericParamType(type);
                Class<?> cls2 = TypeUtils.getClass(parameterizedType2);
                TypeVariable typeVariable = (TypeVariable) type2;
                TypeVariable<Class<?>>[] typeParameters2 = cls2.getTypeParameters();
                for (int i = 0; i < typeParameters2.length; i++) {
                    if (typeParameters2[i].getName().equals(typeVariable.getName())) {
                        return parameterizedType2.getActualTypeArguments()[i];
                    }
                }
            }
            if (type2 instanceof ParameterizedType) {
                ParameterizedType parameterizedType3 = (ParameterizedType) type2;
                Type[] actualTypeArguments = parameterizedType3.getActualTypeArguments();
                boolean argument = getArgument(actualTypeArguments, map);
                boolean argument2 = argument;
                if (!argument) {
                    if (type instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType) type;
                        typeParameters = cls.getTypeParameters();
                    } else if (cls.getGenericSuperclass() instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType) cls.getGenericSuperclass();
                        typeParameters = cls.getSuperclass().getTypeParameters();
                    } else {
                        typeParameters = type.getClass().getTypeParameters();
                        parameterizedType = parameterizedType3;
                    }
                    argument2 = getArgument(actualTypeArguments, typeParameters, parameterizedType.getActualTypeArguments());
                }
                if (argument2) {
                    return new ParameterizedTypeImpl(actualTypeArguments, parameterizedType3.getOwnerType(), parameterizedType3.getRawType());
                }
            }
        }
        return type2;
    }

    private static Type getInheritGenericType(Class<?> cls, Type type, TypeVariable<?> typeVariable) {
        Type[] typeArr;
        Type[] actualTypeArguments;
        Class<?> cls2 = typeVariable.getGenericDeclaration() instanceof Class ? (Class) typeVariable.getGenericDeclaration() : null;
        if (cls2 == cls) {
            actualTypeArguments = type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments() : null;
        } else {
            Type[] typeArr2 = null;
            while (true) {
                typeArr = typeArr2;
                if (cls == null || cls == Object.class || cls == cls2) {
                    break;
                }
                Type genericSuperclass = cls.getGenericSuperclass();
                Type[] actualTypeArguments2 = typeArr;
                if (genericSuperclass instanceof ParameterizedType) {
                    actualTypeArguments2 = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                    getArgument(actualTypeArguments2, cls.getTypeParameters(), typeArr);
                }
                cls = cls.getSuperclass();
                typeArr2 = actualTypeArguments2;
            }
            actualTypeArguments = typeArr;
        }
        Type type2 = null;
        if (actualTypeArguments != null) {
            if (cls2 == null) {
                type2 = null;
            } else {
                TypeVariable<Class<?>>[] typeParameters = cls2.getTypeParameters();
                int i = 0;
                while (true) {
                    type2 = null;
                    if (i >= typeParameters.length) {
                        break;
                    }
                    if (typeVariable.equals(typeParameters[i])) {
                        type2 = actualTypeArguments[i];
                        break;
                    }
                    i++;
                }
            }
        }
        return type2;
    }

    @Override // java.lang.Comparable
    public int compareTo(FieldInfo fieldInfo) {
        int i = this.ordinal;
        int i2 = fieldInfo.ordinal;
        if (i < i2) {
            return -1;
        }
        if (i > i2) {
            return 1;
        }
        int iCompareTo = this.name.compareTo(fieldInfo.name);
        if (iCompareTo != 0) {
            return iCompareTo;
        }
        Class<?> declaredClass = getDeclaredClass();
        Class<?> declaredClass2 = fieldInfo.getDeclaredClass();
        if (declaredClass != null && declaredClass2 != null && declaredClass != declaredClass2) {
            if (declaredClass.isAssignableFrom(declaredClass2)) {
                return -1;
            }
            if (declaredClass2.isAssignableFrom(declaredClass)) {
                return 1;
            }
        }
        Field field = this.field;
        boolean z = field != null && field.getType() == this.fieldClass;
        Field field2 = fieldInfo.field;
        boolean z2 = false;
        if (field2 != null) {
            z2 = false;
            if (field2.getType() == fieldInfo.fieldClass) {
                z2 = true;
            }
        }
        if (z && !z2) {
            return 1;
        }
        if (z2 && !z) {
            return -1;
        }
        if (fieldInfo.fieldClass.isPrimitive() && !this.fieldClass.isPrimitive()) {
            return 1;
        }
        if (this.fieldClass.isPrimitive() && !fieldInfo.fieldClass.isPrimitive()) {
            return -1;
        }
        if (fieldInfo.fieldClass.getName().startsWith("java.") && !this.fieldClass.getName().startsWith("java.")) {
            return 1;
        }
        if (!this.fieldClass.getName().startsWith("java.") || fieldInfo.fieldClass.getName().startsWith("java.")) {
            return this.fieldClass.getName().compareTo(fieldInfo.fieldClass.getName());
        }
        return -1;
    }

    protected char[] genFieldNameChars() {
        int length = this.name.length();
        char[] cArr = new char[length + 3];
        String str = this.name;
        str.getChars(0, str.length(), cArr, 1);
        cArr[0] = '\"';
        cArr[length + 1] = '\"';
        cArr[length + 2] = ':';
        return cArr;
    }

    public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
        Method method = this.method;
        return method != null ? method.invoke(obj, new Object[0]) : this.field.get(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.annotation.Annotation] */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.annotation.Annotation] */
    public <T extends Annotation> T getAnnation(Class<T> cls) {
        if (cls == JSONField.class) {
            return getAnnotation();
        }
        T annotation = null;
        Method method = this.method;
        if (method != null) {
            annotation = TypeUtils.getAnnotation(method, cls);
        }
        T annotation2 = annotation;
        if (annotation == null) {
            Field field = this.field;
            annotation2 = annotation;
            if (field != null) {
                annotation2 = TypeUtils.getAnnotation(field, cls);
            }
        }
        return annotation2;
    }

    public JSONField getAnnotation() {
        JSONField jSONField = this.fieldAnnotation;
        return jSONField != null ? jSONField : this.methodAnnotation;
    }

    protected Class<?> getDeclaredClass() {
        Method method = this.method;
        if (method != null) {
            return method.getDeclaringClass();
        }
        Field field = this.field;
        if (field != null) {
            return field.getDeclaringClass();
        }
        return null;
    }

    public String getFormat() {
        return this.format;
    }

    public Member getMember() {
        Method method = this.method;
        return method != null ? method : this.field;
    }

    public void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = this.method;
        if (method != null) {
            method.invoke(obj, obj2);
        } else {
            this.field.set(obj, obj2);
        }
    }

    public void setAccessible() throws SecurityException {
        Method method = this.method;
        if (method != null) {
            TypeUtils.setAccessible(method);
        } else {
            TypeUtils.setAccessible(this.field);
        }
    }

    public String toString() {
        return this.name;
    }
}
