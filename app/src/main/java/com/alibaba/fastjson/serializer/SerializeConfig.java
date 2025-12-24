package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.TypeUtils;
import com.github.clans.fab.BuildConfig;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/SerializeConfig.class */
public class SerializeConfig {
    private boolean asm;
    private ASMSerializerFactory asmFactory;
    private long[] denyClasses;
    private final boolean fieldBased;
    private final IdentityHashMap<Type, IdentityHashMap<Type, ObjectSerializer>> mixInSerializers;
    private List<Module> modules;
    public PropertyNamingStrategy propertyNamingStrategy;
    private final IdentityHashMap<Type, ObjectSerializer> serializers;
    protected String typeKey;
    public static final SerializeConfig globalInstance = new SerializeConfig();
    private static boolean awtError = false;
    private static boolean jdk8Error = false;
    private static boolean oracleJdbcError = false;
    private static boolean springfoxError = false;
    private static boolean guavaError = false;
    private static boolean jsonnullError = false;
    private static boolean jsonobjectError = false;
    private static boolean jodaError = false;

    public SerializeConfig() {
        this(8192);
    }

    public SerializeConfig(int i) {
        this(i, false);
    }

    public SerializeConfig(int i, boolean z) {
        this.asm = !ASMUtils.IS_ANDROID;
        this.typeKey = JSON.DEFAULT_TYPE_KEY;
        this.denyClasses = new long[]{4165360493669296979L, 4446674157046724083L};
        this.modules = new ArrayList();
        this.fieldBased = z;
        this.serializers = new IdentityHashMap<>(i);
        this.mixInSerializers = new IdentityHashMap<>(16);
        try {
            if (this.asm) {
                this.asmFactory = new ASMSerializerFactory();
            }
        } catch (Throwable th) {
            this.asm = false;
        }
        initSerializers();
    }

    public SerializeConfig(boolean z) {
        this(8192, z);
    }

    private final JavaBeanSerializer createASMSerializer(SerializeBeanInfo serializeBeanInfo) throws Exception {
        JavaBeanSerializer javaBeanSerializerCreateJavaBeanSerializer = this.asmFactory.createJavaBeanSerializer(serializeBeanInfo);
        for (int i = 0; i < javaBeanSerializerCreateJavaBeanSerializer.sortedGetters.length; i++) {
            Class<?> cls = javaBeanSerializerCreateJavaBeanSerializer.sortedGetters[i].fieldInfo.fieldClass;
            if (cls.isEnum() && !(getObjectWriter(cls) instanceof EnumSerializer)) {
                javaBeanSerializerCreateJavaBeanSerializer.writeDirect = false;
            }
        }
        return javaBeanSerializerCreateJavaBeanSerializer;
    }

    public static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    /* JADX WARN: Removed duplicated region for block: B:154:0x04a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.alibaba.fastjson.serializer.ObjectSerializer getObjectWriter(java.lang.Class<?> r7, boolean r8) throws java.lang.ClassNotFoundException {
        /*
            Method dump skipped, instructions count: 2495
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeConfig.getObjectWriter(java.lang.Class, boolean):com.alibaba.fastjson.serializer.ObjectSerializer");
    }

    private void initSerializers() {
        put(Boolean.class, (ObjectSerializer) BooleanCodec.instance);
        put(Character.class, (ObjectSerializer) CharacterCodec.instance);
        put(Byte.class, (ObjectSerializer) IntegerCodec.instance);
        put(Short.class, (ObjectSerializer) IntegerCodec.instance);
        put(Integer.class, (ObjectSerializer) IntegerCodec.instance);
        put(Long.class, (ObjectSerializer) LongCodec.instance);
        put(Float.class, (ObjectSerializer) FloatCodec.instance);
        put(Double.class, (ObjectSerializer) DoubleSerializer.instance);
        put(BigDecimal.class, (ObjectSerializer) BigDecimalCodec.instance);
        put(BigInteger.class, (ObjectSerializer) BigIntegerCodec.instance);
        put(String.class, (ObjectSerializer) StringCodec.instance);
        put(byte[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(short[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(int[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(long[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(float[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(double[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(boolean[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(char[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(Object[].class, (ObjectSerializer) ObjectArrayCodec.instance);
        put(Class.class, (ObjectSerializer) MiscCodec.instance);
        put(SimpleDateFormat.class, (ObjectSerializer) MiscCodec.instance);
        put(Currency.class, (ObjectSerializer) new MiscCodec());
        put(TimeZone.class, (ObjectSerializer) MiscCodec.instance);
        put(InetAddress.class, (ObjectSerializer) MiscCodec.instance);
        put(Inet4Address.class, (ObjectSerializer) MiscCodec.instance);
        put(Inet6Address.class, (ObjectSerializer) MiscCodec.instance);
        put(InetSocketAddress.class, (ObjectSerializer) MiscCodec.instance);
        put(File.class, (ObjectSerializer) MiscCodec.instance);
        put(Appendable.class, (ObjectSerializer) AppendableSerializer.instance);
        put(StringBuffer.class, (ObjectSerializer) AppendableSerializer.instance);
        put(StringBuilder.class, (ObjectSerializer) AppendableSerializer.instance);
        put(Charset.class, (ObjectSerializer) ToStringSerializer.instance);
        put(Pattern.class, (ObjectSerializer) ToStringSerializer.instance);
        put(Locale.class, (ObjectSerializer) ToStringSerializer.instance);
        put(URI.class, (ObjectSerializer) ToStringSerializer.instance);
        put(URL.class, (ObjectSerializer) ToStringSerializer.instance);
        put(UUID.class, (ObjectSerializer) ToStringSerializer.instance);
        put(AtomicBoolean.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicInteger.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicLong.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(AtomicIntegerArray.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicLongArray.class, (ObjectSerializer) AtomicCodec.instance);
        put(WeakReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(SoftReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(LinkedList.class, (ObjectSerializer) CollectionCodec.instance);
    }

    public void addFilter(Class<?> cls, SerializeFilter serializeFilter) {
        Object objectWriter = getObjectWriter(cls);
        if (objectWriter instanceof SerializeFilterable) {
            SerializeFilterable serializeFilterable = (SerializeFilterable) objectWriter;
            if (this == globalInstance || serializeFilterable != MapSerializer.instance) {
                serializeFilterable.addFilter(serializeFilter);
                return;
            }
            MapSerializer mapSerializer = new MapSerializer();
            put((Type) cls, (ObjectSerializer) mapSerializer);
            mapSerializer.addFilter(serializeFilter);
        }
    }

    public void clearSerializers() {
        this.serializers.clear();
        initSerializers();
    }

    public void config(Class<?> cls, SerializerFeature serializerFeature, boolean z) throws ClassNotFoundException {
        ObjectSerializer objectWriter = getObjectWriter(cls, false);
        if (objectWriter == null) {
            SerializeBeanInfo serializeBeanInfoBuildBeanInfo = TypeUtils.buildBeanInfo(cls, null, this.propertyNamingStrategy);
            if (z) {
                serializeBeanInfoBuildBeanInfo.features = serializerFeature.mask | serializeBeanInfoBuildBeanInfo.features;
            } else {
                serializeBeanInfoBuildBeanInfo.features = (serializerFeature.mask ^ (-1)) & serializeBeanInfoBuildBeanInfo.features;
            }
            put((Type) cls, createJavaBeanSerializer(serializeBeanInfoBuildBeanInfo));
            return;
        }
        if (objectWriter instanceof JavaBeanSerializer) {
            SerializeBeanInfo serializeBeanInfo = ((JavaBeanSerializer) objectWriter).beanInfo;
            int i = serializeBeanInfo.features;
            if (z) {
                serializeBeanInfo.features = serializerFeature.mask | serializeBeanInfo.features;
            } else {
                serializeBeanInfo.features = (serializerFeature.mask ^ (-1)) & serializeBeanInfo.features;
            }
            if (i == serializeBeanInfo.features || objectWriter.getClass() == JavaBeanSerializer.class) {
                return;
            }
            put((Type) cls, createJavaBeanSerializer(serializeBeanInfo));
        }
    }

    public void configEnumAsJavaBean(Class<? extends Enum>... clsArr) {
        for (Class<? extends Enum> cls : clsArr) {
            put((Type) cls, createJavaBeanSerializer(cls));
        }
    }

    public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo serializeBeanInfo) {
        boolean z;
        boolean z2;
        JSONType jSONType = serializeBeanInfo.jsonType;
        boolean z3 = this.asm && !this.fieldBased;
        boolean z4 = z3;
        if (jSONType != null) {
            Class<?> clsSerializer = jSONType.serializer();
            if (clsSerializer != Void.class) {
                try {
                    Object objNewInstance = clsSerializer.newInstance();
                    if (objNewInstance instanceof ObjectSerializer) {
                        return (ObjectSerializer) objNewInstance;
                    }
                } catch (Throwable th) {
                }
            }
            if (!jSONType.asm()) {
                z3 = false;
            }
            boolean z5 = z3;
            if (z3) {
                SerializerFeature[] serializerFeatureArrSerialzeFeatures = jSONType.serialzeFeatures();
                int length = serializerFeatureArrSerialzeFeatures.length;
                int i = 0;
                while (true) {
                    z5 = z3;
                    if (i >= length) {
                        break;
                    }
                    SerializerFeature serializerFeature = serializerFeatureArrSerialzeFeatures[i];
                    if (SerializerFeature.WriteNonStringValueAsString == serializerFeature || SerializerFeature.WriteEnumUsingToString == serializerFeature || SerializerFeature.NotWriteDefaultValue == serializerFeature || SerializerFeature.BrowserCompatible == serializerFeature) {
                        break;
                    }
                    i++;
                }
                z5 = false;
            }
            z4 = z5;
            if (z5) {
                z4 = z5;
                if (jSONType.serialzeFilters().length != 0) {
                    z4 = false;
                }
            }
        }
        Class<?> cls = serializeBeanInfo.beanType;
        if (!Modifier.isPublic(serializeBeanInfo.beanType.getModifiers())) {
            return new JavaBeanSerializer(serializeBeanInfo);
        }
        if ((z4 && this.asmFactory.classLoader.isExternalClass(cls)) || cls == Serializable.class || cls == Object.class) {
            z4 = false;
        }
        boolean z6 = z4;
        if (z4) {
            z6 = z4;
            if (!ASMUtils.checkName(cls.getSimpleName())) {
                z6 = false;
            }
        }
        boolean z7 = z6;
        if (z6) {
            z7 = z6;
            if (serializeBeanInfo.beanType.isInterface()) {
                z7 = false;
            }
        }
        boolean z8 = z7;
        if (z7) {
            FieldInfo[] fieldInfoArr = serializeBeanInfo.fields;
            int length2 = fieldInfoArr.length;
            int i2 = 0;
            while (true) {
                z8 = z7;
                if (i2 >= length2) {
                    break;
                }
                FieldInfo fieldInfo = fieldInfoArr[i2];
                Field field = fieldInfo.field;
                if (field != null && !field.getType().equals(fieldInfo.fieldClass)) {
                    z = false;
                    break;
                }
                Method method = fieldInfo.method;
                if (method != null && !method.getReturnType().equals(fieldInfo.fieldClass)) {
                    z = false;
                    break;
                }
                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null) {
                    String str = annotation.format();
                    if (str.length() != 0) {
                        z = false;
                        if (fieldInfo.fieldClass != String.class) {
                            break;
                        }
                        z = false;
                        if (!"trim".equals(str)) {
                            break;
                        }
                    }
                    z = false;
                    if (!ASMUtils.checkName(annotation.name())) {
                        break;
                    }
                    z = false;
                    if (annotation.jsonDirect()) {
                        break;
                    }
                    z = false;
                    if (annotation.serializeUsing() != Void.class) {
                        break;
                    }
                    if (annotation.unwrapped()) {
                        z = false;
                        break;
                    }
                    SerializerFeature[] serializerFeatureArrSerialzeFeatures2 = annotation.serialzeFeatures();
                    int length3 = serializerFeatureArrSerialzeFeatures2.length;
                    int i3 = 0;
                    while (true) {
                        z2 = z7;
                        if (i3 >= length3) {
                            break;
                        }
                        SerializerFeature serializerFeature2 = serializerFeatureArrSerialzeFeatures2[i3];
                        if (SerializerFeature.WriteNonStringValueAsString == serializerFeature2 || SerializerFeature.WriteEnumUsingToString == serializerFeature2 || SerializerFeature.NotWriteDefaultValue == serializerFeature2 || SerializerFeature.BrowserCompatible == serializerFeature2 || SerializerFeature.WriteClassName == serializerFeature2) {
                            break;
                        }
                        i3++;
                    }
                    z2 = false;
                    z = false;
                    if (TypeUtils.isAnnotationPresentOneToMany(method)) {
                        break;
                    }
                    if (TypeUtils.isAnnotationPresentManyToMany(method)) {
                        z = false;
                        break;
                    }
                    z7 = z2;
                    if (annotation.defaultValue() != null) {
                        z7 = z2;
                        if (!BuildConfig.FLAVOR.equals(annotation.defaultValue())) {
                            z = false;
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                i2++;
            }
        } else {
            z = z8;
        }
        if (z) {
            try {
                JavaBeanSerializer javaBeanSerializerCreateASMSerializer = createASMSerializer(serializeBeanInfo);
                if (javaBeanSerializerCreateASMSerializer != null) {
                    return javaBeanSerializerCreateASMSerializer;
                }
            } catch (ClassCastException | ClassFormatError | ClassNotFoundException e) {
            } catch (OutOfMemoryError e2) {
                if (e2.getMessage().indexOf("Metaspace") != -1) {
                    throw e2;
                }
            } catch (Throwable th2) {
                throw new JSONException("create asm serializer error, verson 1.2.62, class " + cls, th2);
            }
        }
        return new JavaBeanSerializer(serializeBeanInfo);
    }

    public final ObjectSerializer createJavaBeanSerializer(Class<?> cls) {
        String name = cls.getName();
        if (Arrays.binarySearch(this.denyClasses, TypeUtils.fnv1a_64(name)) < 0) {
            SerializeBeanInfo serializeBeanInfoBuildBeanInfo = TypeUtils.buildBeanInfo(cls, null, this.propertyNamingStrategy, this.fieldBased);
            return (serializeBeanInfoBuildBeanInfo.fields.length == 0 && Iterable.class.isAssignableFrom(cls)) ? MiscCodec.instance : createJavaBeanSerializer(serializeBeanInfoBuildBeanInfo);
        }
        throw new JSONException("not support class : " + name);
    }

    public final ObjectSerializer get(Type type) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.serializers.get(type);
        }
        IdentityHashMap<Type, ObjectSerializer> identityHashMap = this.mixInSerializers.get(type);
        if (identityHashMap == null) {
            return null;
        }
        return identityHashMap.get(mixInAnnotations);
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        return getObjectWriter(cls, true);
    }

    public String getTypeKey() {
        return this.typeKey;
    }

    public boolean isAsmEnable() {
        return this.asm;
    }

    public boolean put(Object obj, Object obj2) {
        return put((Type) obj, (ObjectSerializer) obj2);
    }

    public boolean put(Type type, ObjectSerializer objectSerializer) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.serializers.put(type, objectSerializer);
        }
        IdentityHashMap<Type, ObjectSerializer> identityHashMap = this.mixInSerializers.get(type);
        IdentityHashMap<Type, ObjectSerializer> identityHashMap2 = identityHashMap;
        if (identityHashMap == null) {
            identityHashMap2 = new IdentityHashMap<>(4);
            this.mixInSerializers.put(type, identityHashMap2);
        }
        return identityHashMap2.put(mixInAnnotations, objectSerializer);
    }

    public void register(Module module) {
        this.modules.add(module);
    }

    public void setAsmEnable(boolean z) {
        if (ASMUtils.IS_ANDROID) {
            return;
        }
        this.asm = z;
    }

    public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        this.propertyNamingStrategy = propertyNamingStrategy;
    }

    public void setTypeKey(String str) {
        this.typeKey = str;
    }
}
