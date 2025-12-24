package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.clans.fab.BuildConfig;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/TypeUtils.class */
public class TypeUtils {
    private static Class<? extends Annotation> class_ManyToMany;
    private static boolean class_ManyToMany_error = false;
    private static Class<? extends Annotation> class_OneToMany;
    private static boolean class_OneToMany_error = false;
    public static boolean compatibleWithFieldName = false;
    public static boolean compatibleWithJavaBean = false;
    private static volatile Map<Class, String[]> kotlinIgnores;
    private static volatile boolean kotlinIgnores_error = false;
    private static volatile boolean kotlin_class_klass_error = false;
    private static volatile boolean kotlin_error = false;
    private static volatile Constructor kotlin_kclass_constructor;
    private static volatile Method kotlin_kclass_getConstructors;
    private static volatile Method kotlin_kfunction_getParameters;
    private static volatile Method kotlin_kparameter_getName;
    private static volatile Class kotlin_metadata;
    private static volatile boolean kotlin_metadata_error = false;
    private static Method method_HibernateIsInitialized;
    private static boolean method_HibernateIsInitialized_error = false;
    private static Class<?> optionalClass;
    private static boolean optionalClassInited = false;
    private static Method oracleDateMethod;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleTimestampMethod;
    private static boolean oracleTimestampMethodInited = false;
    private static Class<?> pathClass;
    private static boolean setAccessibleEnable = true;
    private static Class<? extends Annotation> transientClass;
    private static boolean transientClassInited = false;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap(256, 0.75f, 1);
    private static boolean pathClass_error = false;
    private static Class<? extends Annotation> class_JacksonCreator = null;
    private static boolean class_JacksonCreator_error = false;
    private static volatile Class class_Clob = null;
    private static volatile boolean class_Clob_error = false;
    private static volatile Class class_XmlAccessType = null;
    private static volatile Class class_XmlAccessorType = null;
    private static volatile boolean classXmlAccessorType_error = false;
    private static volatile Method method_XmlAccessorType_value = null;
    private static volatile Field field_XmlAccessType_FIELD = null;
    private static volatile Object field_XmlAccessType_FIELD_VALUE = null;

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:6:0x005a
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    static {
        /*
            java.util.concurrent.ConcurrentHashMap r0 = new java.util.concurrent.ConcurrentHashMap
            r1 = r0
            r2 = 256(0x100, float:3.59E-43)
            r3 = 1061158912(0x3f400000, float:0.75)
            r4 = 1
            r1.<init>(r2, r3, r4)
            com.alibaba.fastjson.util.TypeUtils.mappings = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.pathClass_error = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_JacksonCreator = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_JacksonCreator_error = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_Clob = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_Clob_error = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_XmlAccessType = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.class_XmlAccessorType = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.classXmlAccessorType_error = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.method_XmlAccessorType_value = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.field_XmlAccessType_FIELD = r0
            r0 = 0
            com.alibaba.fastjson.util.TypeUtils.field_XmlAccessType_FIELD_VALUE = r0
            java.lang.String r0 = "true"
            java.lang.String r1 = "fastjson.compatibleWithJavaBean"
            java.lang.String r1 = com.alibaba.fastjson.util.IOUtils.getStringProperty(r1)     // Catch: java.lang.Throwable -> L5a
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L5a
            com.alibaba.fastjson.util.TypeUtils.compatibleWithJavaBean = r0     // Catch: java.lang.Throwable -> L5a
            java.lang.String r0 = "true"
            java.lang.String r1 = "fastjson.compatibleWithFieldName"
            java.lang.String r1 = com.alibaba.fastjson.util.IOUtils.getStringProperty(r1)     // Catch: java.lang.Throwable -> L5a
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L5a
            com.alibaba.fastjson.util.TypeUtils.compatibleWithFieldName = r0     // Catch: java.lang.Throwable -> L5a
        L56:
            addBaseClassMappings()     // Catch: java.lang.Throwable -> L5a
            return
        L5a:
            r6 = move-exception
            goto L56
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.m375clinit():void");
    }

    private static void addBaseClassMappings() {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        Class<?> clsLoadClass = loadClass("java.lang.AutoCloseable");
        Class<?> cls = Collections.EMPTY_MAP.getClass();
        for (int i = 0; i < 71; i++) {
            Class<?> cls2 = new Class[]{Object.class, Cloneable.class, clsLoadClass, Exception.class, RuntimeException.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class, HashMap.class, Hashtable.class, TreeMap.class, java.util.IdentityHashMap.class, WeakHashMap.class, LinkedHashMap.class, HashSet.class, LinkedHashSet.class, TreeSet.class, ArrayList.class, TimeUnit.class, ConcurrentHashMap.class, AtomicInteger.class, AtomicLong.class, cls, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Number.class, String.class, BigDecimal.class, BigInteger.class, BitSet.class, Calendar.class, Date.class, Locale.class, UUID.class, Time.class, java.sql.Date.class, Timestamp.class, SimpleDateFormat.class, JSONObject.class, JSONPObject.class, JSONArray.class}[i];
            if (cls2 != null) {
                mappings.put(cls2.getName(), cls2);
            }
        }
    }

    public static void addMapping(String str, Class<?> cls) {
        mappings.put(str, cls);
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> cls, Map<String, String> map, PropertyNamingStrategy propertyNamingStrategy) {
        return buildBeanInfo(cls, map, propertyNamingStrategy, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static SerializeBeanInfo buildBeanInfo(Class<?> cls, Map<String, String> map, PropertyNamingStrategy propertyNamingStrategy, boolean z) {
        PropertyNamingStrategy propertyNamingStrategy2;
        String[] strArr;
        String str;
        String str2;
        int i;
        List arrayList;
        String str3;
        String strTypeKey;
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType != null) {
            String[] strArrOrders = jSONType.orders();
            String strTypeName = jSONType.typeName();
            String str4 = strTypeName;
            if (strTypeName.length() == 0) {
                str4 = null;
            }
            PropertyNamingStrategy propertyNamingStrategyNaming = jSONType.naming();
            if (propertyNamingStrategyNaming != PropertyNamingStrategy.CamelCase) {
                propertyNamingStrategy = propertyNamingStrategyNaming;
            }
            int iM12of = SerializerFeature.m12of(jSONType.serialzeFeatures());
            Class<? super Object> superclass = cls.getSuperclass();
            String strTypeKey2 = null;
            while (true) {
                str3 = strTypeKey2;
                if (superclass == null) {
                    break;
                }
                str3 = strTypeKey2;
                if (superclass == Object.class) {
                    break;
                }
                JSONType jSONType2 = (JSONType) getAnnotation(superclass, JSONType.class);
                if (jSONType2 == null) {
                    str3 = strTypeKey2;
                    break;
                }
                strTypeKey2 = jSONType2.typeKey();
                if (strTypeKey2.length() != 0) {
                    str3 = strTypeKey2;
                    break;
                }
                superclass = superclass.getSuperclass();
            }
            Class<?>[] interfaces = cls.getInterfaces();
            int length = interfaces.length;
            int i2 = 0;
            String str5 = str3;
            while (true) {
                strTypeKey = str5;
                if (i2 >= length) {
                    break;
                }
                JSONType jSONType3 = (JSONType) getAnnotation(interfaces[i2], JSONType.class);
                if (jSONType3 != null) {
                    strTypeKey = jSONType3.typeKey();
                    str5 = strTypeKey;
                    if (strTypeKey.length() != 0) {
                        break;
                    }
                }
                i2++;
            }
            strArr = strArrOrders;
            PropertyNamingStrategy propertyNamingStrategy3 = propertyNamingStrategy;
            i = iM12of;
            str = str4;
            str2 = (strTypeKey == null || strTypeKey.length() != 0) ? strTypeKey : null;
            propertyNamingStrategy2 = propertyNamingStrategy3;
        } else {
            propertyNamingStrategy2 = propertyNamingStrategy;
            strArr = null;
            str = null;
            str2 = null;
            i = 0;
        }
        HashMap map2 = new HashMap();
        ParserConfig.parserAllFieldToCache(cls, map2);
        List<FieldInfo> listComputeGettersWithFieldBase = z ? computeGettersWithFieldBase(cls, map, false, propertyNamingStrategy2) : computeGetters(cls, jSONType, map, map2, false, propertyNamingStrategy2);
        FieldInfo[] fieldInfoArr = new FieldInfo[listComputeGettersWithFieldBase.size()];
        listComputeGettersWithFieldBase.toArray(fieldInfoArr);
        if (strArr == null || strArr.length == 0) {
            arrayList = new ArrayList(listComputeGettersWithFieldBase);
            Collections.sort(arrayList);
        } else {
            arrayList = z ? computeGettersWithFieldBase(cls, map, true, propertyNamingStrategy2) : computeGetters(cls, jSONType, map, map2, true, propertyNamingStrategy2);
        }
        FieldInfo[] fieldInfoArr2 = new FieldInfo[arrayList.size()];
        arrayList.toArray(fieldInfoArr2);
        return new SerializeBeanInfo(cls, jSONType, str, str2, i, fieldInfoArr, Arrays.equals(fieldInfoArr2, fieldInfoArr) ? fieldInfoArr : fieldInfoArr2);
    }

    public static byte byteValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (byte) 0;
        }
        int iScale = bigDecimal.scale();
        return (iScale < -100 || iScale > 100) ? bigDecimal.byteValueExact() : bigDecimal.byteValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        Calendar calendar;
        int i = 0;
        if (obj == 0) {
            if (cls == Integer.TYPE) {
                return (T) 0;
            }
            if (cls == Long.TYPE) {
                return (T) 0L;
            }
            if (cls == Short.TYPE) {
                return (T) (short) 0;
            }
            if (cls == Byte.TYPE) {
                return (T) (byte) 0;
            }
            if (cls == Float.TYPE) {
                return (T) Float.valueOf(0.0f);
            }
            if (cls == Double.TYPE) {
                return (T) Double.valueOf(0.0d);
            }
            if (cls == Boolean.TYPE) {
                return (T) Boolean.FALSE;
            }
            return null;
        }
        if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        }
        if (cls == obj.getClass()) {
            return obj;
        }
        if (obj instanceof Map) {
            if (cls == Map.class) {
                return obj;
            }
            Map map = (Map) obj;
            return (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) ? (T) castToJavaBean(map, cls, parserConfig) : obj;
        }
        if (cls.isArray()) {
            if (obj instanceof Collection) {
                Collection collection = (Collection) obj;
                T t = (T) Array.newInstance(cls.getComponentType(), collection.size());
                Iterator it = collection.iterator();
                while (it.hasNext()) {
                    Array.set(t, i, cast(it.next(), (Class) cls.getComponentType(), parserConfig));
                    i++;
                }
                return t;
            }
            if (cls == byte[].class) {
                return (T) castToBytes(obj);
            }
        }
        if (cls.isAssignableFrom(obj.getClass())) {
            return obj;
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return (T) castToBoolean(obj);
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return (T) castToByte(obj);
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return (T) castToChar(obj);
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return (T) castToShort(obj);
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return (T) castToInt(obj);
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return (T) castToLong(obj);
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return (T) castToFloat(obj);
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return (T) castToDouble(obj);
        }
        if (cls == String.class) {
            return (T) castToString(obj);
        }
        if (cls == BigDecimal.class) {
            return (T) castToBigDecimal(obj);
        }
        if (cls == BigInteger.class) {
            return (T) castToBigInteger(obj);
        }
        if (cls == Date.class) {
            return (T) castToDate(obj);
        }
        if (cls == java.sql.Date.class) {
            return (T) castToSqlDate(obj);
        }
        if (cls == Time.class) {
            return (T) castToSqlTime(obj);
        }
        if (cls == Timestamp.class) {
            return (T) castToTimestamp(obj);
        }
        if (cls.isEnum()) {
            return (T) castToEnum(obj, cls, parserConfig);
        }
        if (Calendar.class.isAssignableFrom(cls)) {
            Date dateCastToDate = castToDate(obj);
            if (cls == Calendar.class) {
                calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            } else {
                try {
                    calendar = (Calendar) cls.newInstance();
                } catch (Exception e) {
                    throw new JSONException("can not cast to : " + cls.getName(), e);
                }
            }
            calendar.setTime(dateCastToDate);
            return (T) calendar;
        }
        String name = cls.getName();
        if (name.equals("javax.xml.datatype.XMLGregorianCalendar")) {
            Date dateCastToDate2 = castToDate(obj);
            Calendar calendar2 = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            calendar2.setTime(dateCastToDate2);
            return (T) CalendarCodec.instance.createXMLGregorianCalendar(calendar2);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (cls == Currency.class) {
                return (T) Currency.getInstance(str);
            }
            if (cls == Locale.class) {
                return (T) toLocale(str);
            }
            if (name.startsWith("java.time.")) {
                return (T) JSON.parseObject(JSON.toJSONString(str), cls);
            }
        }
        if (parserConfig.get(cls) != null) {
            return (T) JSON.parseObject(JSON.toJSONString(obj), cls);
        }
        throw new JSONException("can not cast to : " + cls.getName());
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [T, java.util.ArrayList, java.util.List] */
    /* JADX WARN: Type inference failed for: r0v75, types: [T, java.util.HashMap, java.util.Map] */
    public static <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        Type rawType = parameterizedType.getRawType();
        if (rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof List) {
                List list = (List) obj;
                ?? r0 = (T) new ArrayList(list.size());
                for (int i = 0; i < list.size(); i++) {
                    Object obj2 = list.get(i);
                    r0.add(type instanceof Class ? (obj2 == null || obj2.getClass() != JSONObject.class) ? cast(obj2, (Class<Object>) type, parserConfig) : ((JSONObject) obj2).toJavaObject((Class) type, parserConfig, 0) : cast(obj2, type, parserConfig));
                }
                return r0;
            }
        }
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == Collection.class || rawType == List.class || rawType == ArrayList.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                AbstractCollection hashSet = (rawType == Set.class || rawType == HashSet.class) ? new HashSet() : rawType == TreeSet.class ? new TreeSet() : new ArrayList();
                Iterator<T> it = ((Iterable) obj).iterator();
                while (it.hasNext()) {
                    T next = it.next();
                    hashSet.add(type2 instanceof Class ? (next == null || next.getClass() != JSONObject.class) ? cast((Object) next, (Class<Object>) type2, parserConfig) : ((JSONObject) next).toJavaObject((Class) type2, parserConfig, 0) : cast(next, type2, parserConfig));
                }
                return (T) hashSet;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type3 = parameterizedType.getActualTypeArguments()[0];
            Type type4 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                ?? r02 = (T) new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    r02.put(cast(entry.getKey(), type3, parserConfig), cast(entry.getValue(), type4, parserConfig));
                }
                return r02;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (parameterizedType.getActualTypeArguments().length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return (T) cast(obj, rawType, parserConfig);
        }
        if (rawType == Map.Entry.class && (obj instanceof Map)) {
            Map map = (Map) obj;
            if (map.size() == 1) {
                return (T) ((Map.Entry) map.entrySet().iterator().next());
            }
        }
        if (rawType instanceof Class) {
            ParserConfig parserConfig2 = parserConfig;
            if (parserConfig == null) {
                parserConfig2 = ParserConfig.global;
            }
            ObjectDeserializer deserializer = parserConfig2.getDeserializer(rawType);
            if (deserializer != null) {
                return (T) deserializer.deserialze(new DefaultJSONParser(JSON.toJSONString(obj), parserConfig2), parameterizedType, null);
            }
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == 0) {
            return null;
        }
        if (type instanceof Class) {
            return (T) cast(obj, (Class) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, parserConfig);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static BigDecimal castToBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        String string = obj.toString();
        if (string.length() == 0) {
            return null;
        }
        if ((obj instanceof Map) && ((Map) obj).size() == 0) {
            return null;
        }
        return new BigDecimal(string);
    }

    public static BigInteger castToBigInteger(Object obj) {
        BigDecimal bigDecimal;
        int iScale;
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }
        if ((obj instanceof Float) || (obj instanceof Double)) {
            return BigInteger.valueOf(((Number) obj).longValue());
        }
        if ((obj instanceof BigDecimal) && (iScale = (bigDecimal = (BigDecimal) obj).scale()) > -1000 && iScale < 1000) {
            return bigDecimal.toBigInteger();
        }
        String string = obj.toString();
        BigInteger bigInteger = null;
        if (string.length() != 0) {
            bigInteger = null;
            if (!"null".equals(string)) {
                bigInteger = "NULL".equals(string) ? null : new BigInteger(string);
            }
        }
        return bigInteger;
    }

    public static Boolean castToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        boolean z = false;
        if (obj instanceof BigDecimal) {
            boolean z2 = false;
            if (intValue((BigDecimal) obj) == 1) {
                z2 = true;
            }
            return Boolean.valueOf(z2);
        }
        if (obj instanceof Number) {
            if (((Number) obj).intValue() == 1) {
                z = true;
            }
            return Boolean.valueOf(z);
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                return Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(str) || "T".equals(str)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(str) || "N".equals(str)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + obj);
    }

    public static Byte castToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Byte.valueOf(byteValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Byte.valueOf(((Number) obj).byteValue());
        }
        if (!(obj instanceof String)) {
            throw new JSONException("can not cast to byte, value : " + obj);
        }
        String str = (String) obj;
        if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
            return null;
        }
        return Byte.valueOf(Byte.parseByte(str));
    }

    public static byte[] castToBytes(Object obj) {
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return IOUtils.decodeBase64((String) obj);
        }
        throw new JSONException("can not cast to byte[], value : " + obj);
    }

    public static Character castToChar(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Character) {
            return (Character) obj;
        }
        if (!(obj instanceof String)) {
            throw new JSONException("can not cast to char, value : " + obj);
        }
        String str = (String) obj;
        if (str.length() == 0) {
            return null;
        }
        if (str.length() == 1) {
            return Character.valueOf(str.charAt(0));
        }
        throw new JSONException("can not cast to char, value : " + obj);
    }

    public static Date castToDate(Object obj) {
        return castToDate(obj, null);
    }

    public static Date castToDate(Object obj, String str) throws NumberFormatException {
        long j;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }
        if (obj instanceof BigDecimal) {
            return new Date(longValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            long jLongValue = ((Number) obj).longValue();
            long j2 = jLongValue;
            if ("unixtime".equals(str)) {
                j2 = jLongValue * 1000;
            }
            return new Date(j2);
        }
        if (obj instanceof String) {
            String str2 = (String) obj;
            JSONScanner jSONScanner = new JSONScanner(str2);
            try {
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    return jSONScanner.getCalendar().getTime();
                }
                jSONScanner.close();
                String strSubstring = str2;
                if (str2.startsWith("/Date(")) {
                    strSubstring = str2;
                    if (str2.endsWith(")/")) {
                        strSubstring = str2.substring(6, str2.length() - 2);
                    }
                }
                if (strSubstring.indexOf(45) > 0 || strSubstring.indexOf(43) > 0) {
                    String str3 = str;
                    if (str == null) {
                        str3 = (strSubstring.length() == JSON.DEFFAULT_DATE_FORMAT.length() || (strSubstring.length() == 22 && JSON.DEFFAULT_DATE_FORMAT.equals("yyyyMMddHHmmssSSSZ"))) ? JSON.DEFFAULT_DATE_FORMAT : strSubstring.length() == 10 ? "yyyy-MM-dd" : strSubstring.length() == 19 ? "yyyy-MM-dd HH:mm:ss" : (strSubstring.length() == 29 && strSubstring.charAt(26) == ':' && strSubstring.charAt(28) == '0') ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : (strSubstring.length() == 23 && strSubstring.charAt(19) == ',') ? "yyyy-MM-dd HH:mm:ss,SSS" : "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str3, JSON.defaultLocale);
                    simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
                    try {
                        return simpleDateFormat.parse(strSubstring);
                    } catch (ParseException e) {
                        throw new JSONException("can not cast to Date, value : " + strSubstring);
                    }
                }
                if (strSubstring.length() == 0) {
                    return null;
                }
                j = Long.parseLong(strSubstring);
            } finally {
                jSONScanner.close();
            }
        } else {
            j = -1;
        }
        if (j != -1) {
            return new Date(j);
        }
        Class<?> cls = obj.getClass();
        if ("oracle.sql.TIMESTAMP".equals(cls.getName())) {
            if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                try {
                    oracleTimestampMethod = cls.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException e2) {
                } catch (Throwable th) {
                    oracleTimestampMethodInited = true;
                    throw th;
                }
                oracleTimestampMethodInited = true;
            }
            try {
                return (Date) oracleTimestampMethod.invoke(obj, new Object[0]);
            } catch (Exception e3) {
                throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e3);
            }
        }
        if (!"oracle.sql.DATE".equals(cls.getName())) {
            throw new JSONException("can not cast to Date, value : " + obj);
        }
        if (oracleDateMethod == null && !oracleDateMethodInited) {
            try {
                oracleDateMethod = cls.getMethod("toJdbc", new Class[0]);
            } catch (NoSuchMethodException e4) {
            } catch (Throwable th2) {
                oracleDateMethodInited = true;
                throw th2;
            }
            oracleDateMethodInited = true;
        }
        try {
            return (Date) oracleDateMethod.invoke(obj, new Object[0]);
        } catch (Exception e5) {
            throw new JSONException("can not cast oracle.sql.DATE to Date", e5);
        }
    }

    public static Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Double.valueOf(((Number) obj).doubleValue());
        }
        if (!(obj instanceof String)) {
            throw new JSONException("can not cast to double, value : " + obj);
        }
        String string = obj.toString();
        if (string.length() == 0 || "null".equals(string) || "NULL".equals(string)) {
            return null;
        }
        String strReplaceAll = string;
        if (string.indexOf(44) != 0) {
            strReplaceAll = string.replaceAll(",", BuildConfig.FLAVOR);
        }
        return Double.valueOf(Double.parseDouble(strReplaceAll));
    }

    public static <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                ParserConfig globalInstance = parserConfig;
                if (parserConfig == null) {
                    globalInstance = ParserConfig.getGlobalInstance();
                }
                ObjectDeserializer deserializer = globalInstance.getDeserializer(cls);
                return deserializer instanceof EnumDeserializer ? (T) ((EnumDeserializer) deserializer).getEnumByHashCode(fnv1a_64(str)) : (T) Enum.valueOf(cls, str);
            }
            if (obj instanceof BigDecimal) {
                int iIntValue = intValue((BigDecimal) obj);
                T[] enumConstants = cls.getEnumConstants();
                if (iIntValue < enumConstants.length) {
                    return enumConstants[iIntValue];
                }
            }
            if (obj instanceof Number) {
                int iIntValue2 = ((Number) obj).intValue();
                T[] enumConstants2 = cls.getEnumConstants();
                if (iIntValue2 < enumConstants2.length) {
                    return enumConstants2[iIntValue2];
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    public static Float castToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return Float.valueOf(((Number) obj).floatValue());
        }
        if (!(obj instanceof String)) {
            throw new JSONException("can not cast to float, value : " + obj);
        }
        String string = obj.toString();
        if (string.length() == 0 || "null".equals(string) || "NULL".equals(string)) {
            return null;
        }
        String strReplaceAll = string;
        if (string.indexOf(44) != 0) {
            strReplaceAll = string.replaceAll(",", BuildConfig.FLAVOR);
        }
        return Float.valueOf(Float.parseFloat(strReplaceAll));
    }

    public static Integer castToInt(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof BigDecimal) {
            return Integer.valueOf(intValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Integer.valueOf(((Number) obj).intValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            String strReplaceAll = str;
            if (str.indexOf(44) != 0) {
                strReplaceAll = str.replaceAll(",", BuildConfig.FLAVOR);
            }
            return Integer.valueOf(Integer.parseInt(strReplaceAll));
        }
        if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                Iterator it = map.values().iterator();
                it.next();
                return castToInt(it.next());
            }
        }
        throw new JSONException("can not cast to int, value : " + obj);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> cls) {
        return (T) cast(obj, (Class) cls, ParserConfig.getGlobalInstance());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig) throws ClassNotFoundException {
        int iIntValueExact = 0;
        try {
            if (cls == StackTraceElement.class) {
                String str = (String) map.get("className");
                String str2 = (String) map.get("methodName");
                String str3 = (String) map.get("fileName");
                Number number = (Number) map.get("lineNumber");
                if (number != null) {
                    iIntValueExact = number instanceof BigDecimal ? ((BigDecimal) number).intValueExact() : number.intValue();
                }
                return (T) new StackTraceElement(str, str2, str3, iIntValueExact);
            }
            Object obj = map.get(JSON.DEFAULT_TYPE_KEY);
            ParserConfig parserConfig2 = parserConfig;
            if (obj instanceof String) {
                String str4 = (String) obj;
                ParserConfig parserConfig3 = parserConfig;
                if (parserConfig == null) {
                    parserConfig3 = ParserConfig.global;
                }
                Class<?> clsCheckAutoType = parserConfig3.checkAutoType(str4, null);
                if (clsCheckAutoType == null) {
                    throw new ClassNotFoundException(str4 + " not found");
                }
                parserConfig2 = parserConfig3;
                if (!clsCheckAutoType.equals(cls)) {
                    return (T) castToJavaBean(map, clsCheckAutoType, parserConfig3);
                }
            }
            if (cls.isInterface()) {
                JSONObject jSONObject = map instanceof JSONObject ? (JSONObject) map : new JSONObject(map);
                ParserConfig globalInstance = parserConfig2;
                if (parserConfig2 == null) {
                    globalInstance = ParserConfig.getGlobalInstance();
                }
                return globalInstance.get(cls) != null ? (T) JSON.parseObject(JSON.toJSONString(jSONObject), cls) : (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, jSONObject);
            }
            if (cls == Locale.class) {
                Object obj2 = map.get("language");
                Object obj3 = map.get("country");
                if (obj2 instanceof String) {
                    String str5 = (String) obj2;
                    if (obj3 instanceof String) {
                        return (T) new Locale(str5, (String) obj3);
                    }
                    if (obj3 == null) {
                        return (T) new Locale(str5);
                    }
                }
            }
            if (cls == String.class && (map instanceof JSONObject)) {
                return (T) map.toString();
            }
            if (cls == JSON.class && (map instanceof JSONObject)) {
                return map;
            }
            if (cls == LinkedHashMap.class && (map instanceof JSONObject)) {
                T t = (T) ((JSONObject) map).getInnerMap();
                if (t instanceof LinkedHashMap) {
                    return t;
                }
                new LinkedHashMap().putAll(t);
            }
            if (cls.isInstance(map)) {
                return map;
            }
            if (cls == JSONObject.class) {
                return (T) new JSONObject(map);
            }
            ParserConfig globalInstance2 = parserConfig2;
            if (parserConfig2 == null) {
                globalInstance2 = ParserConfig.getGlobalInstance();
            }
            ObjectDeserializer deserializer = globalInstance2.getDeserializer(cls);
            JavaBeanDeserializer javaBeanDeserializer = null;
            if (deserializer instanceof JavaBeanDeserializer) {
                javaBeanDeserializer = (JavaBeanDeserializer) deserializer;
            }
            if (javaBeanDeserializer != null) {
                return (T) javaBeanDeserializer.createInstance(map, globalInstance2);
            }
            throw new JSONException("can not get javaBeanDeserializer. " + cls.getName());
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static Long castToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Long.valueOf(longValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Long.valueOf(((Number) obj).longValue());
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            String strReplaceAll = str;
            if (str.indexOf(44) != 0) {
                strReplaceAll = str.replaceAll(",", BuildConfig.FLAVOR);
            }
            try {
                return Long.valueOf(Long.parseLong(strReplaceAll));
            } catch (NumberFormatException e) {
                JSONScanner jSONScanner = new JSONScanner(strReplaceAll);
                Calendar calendar = null;
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    calendar = jSONScanner.getCalendar();
                }
                jSONScanner.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.size() == 2 && map.containsKey("andIncrement") && map.containsKey("andDecrement")) {
                Iterator it = map.values().iterator();
                it.next();
                return castToLong(it.next());
            }
        }
        throw new JSONException("can not cast to long, value : " + obj);
    }

    public static Short castToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof BigDecimal) {
            return Short.valueOf(shortValue((BigDecimal) obj));
        }
        if (obj instanceof Number) {
            return Short.valueOf(((Number) obj).shortValue());
        }
        if (!(obj instanceof String)) {
            throw new JSONException("can not cast to short, value : " + obj);
        }
        String str = (String) obj;
        if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
            return null;
        }
        return Short.valueOf(Short.parseShort(str));
    }

    public static java.sql.Date castToSqlDate(Object obj) throws NumberFormatException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof java.sql.Date) {
            return (java.sql.Date) obj;
        }
        if (obj instanceof Date) {
            return new java.sql.Date(((Date) obj).getTime());
        }
        if (obj instanceof Calendar) {
            return new java.sql.Date(((Calendar) obj).getTimeInMillis());
        }
        long jLongValue = obj instanceof BigDecimal ? longValue((BigDecimal) obj) : obj instanceof Number ? ((Number) obj).longValue() : 0L;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (isNumber(str)) {
                jLongValue = Long.parseLong(str);
            } else {
                JSONScanner jSONScanner = new JSONScanner(str);
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    throw new JSONException("can not cast to Timestamp, value : " + str);
                }
                jLongValue = jSONScanner.getCalendar().getTime().getTime();
            }
        }
        if (jLongValue > 0) {
            return new java.sql.Date(jLongValue);
        }
        throw new JSONException("can not cast to Date, value : " + obj);
    }

    public static Time castToSqlTime(Object obj) throws NumberFormatException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Time) {
            return (Time) obj;
        }
        if (obj instanceof Date) {
            return new Time(((Date) obj).getTime());
        }
        if (obj instanceof Calendar) {
            return new Time(((Calendar) obj).getTimeInMillis());
        }
        long jLongValue = obj instanceof BigDecimal ? longValue((BigDecimal) obj) : obj instanceof Number ? ((Number) obj).longValue() : 0L;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
                return null;
            }
            if (isNumber(str)) {
                jLongValue = Long.parseLong(str);
            } else {
                JSONScanner jSONScanner = new JSONScanner(str);
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    throw new JSONException("can not cast to Timestamp, value : " + str);
                }
                jLongValue = jSONScanner.getCalendar().getTime().getTime();
            }
        }
        if (jLongValue > 0) {
            return new Time(jLongValue);
        }
        throw new JSONException("can not cast to Date, value : " + obj);
    }

    public static String castToString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static Timestamp castToTimestamp(Object obj) throws NumberFormatException {
        String strSubstring;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Calendar) {
            return new Timestamp(((Calendar) obj).getTimeInMillis());
        }
        if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        }
        if (obj instanceof Date) {
            return new Timestamp(((Date) obj).getTime());
        }
        long jLongValue = obj instanceof BigDecimal ? longValue((BigDecimal) obj) : obj instanceof Number ? ((Number) obj).longValue() : 0L;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.length() == 0 || "null".equals(str) || "NULL".equals(str)) {
                return null;
            }
            if (str.endsWith(".000000000")) {
                strSubstring = str.substring(0, str.length() - 10);
            } else {
                strSubstring = str;
                if (str.endsWith(".000000")) {
                    strSubstring = str.substring(0, str.length() - 7);
                }
            }
            if (isNumber(strSubstring)) {
                jLongValue = Long.parseLong(strSubstring);
            } else {
                JSONScanner jSONScanner = new JSONScanner(strSubstring);
                if (!jSONScanner.scanISO8601DateIfMatch(false)) {
                    throw new JSONException("can not cast to Timestamp, value : " + strSubstring);
                }
                jLongValue = jSONScanner.getCalendar().getTime().getTime();
            }
        }
        if (jLongValue > 0) {
            return new Timestamp(jLongValue);
        }
        throw new JSONException("can not cast to Timestamp, value : " + obj);
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) throws ClassNotFoundException {
        String str;
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String str2 = "[";
        while (true) {
            str = str2;
            if (!(genericComponentType instanceof GenericArrayType)) {
                break;
            }
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            str2 = str + str;
        }
        Type cls = genericArrayType;
        if (genericComponentType instanceof Class) {
            Class cls2 = (Class) genericComponentType;
            cls = genericArrayType;
            if (cls2.isPrimitive()) {
                try {
                    if (cls2 == Boolean.TYPE) {
                        cls = Class.forName(str + "Z");
                    } else if (cls2 == Character.TYPE) {
                        cls = Class.forName(str + "C");
                    } else if (cls2 == Byte.TYPE) {
                        cls = Class.forName(str + "B");
                    } else if (cls2 == Short.TYPE) {
                        cls = Class.forName(str + "S");
                    } else if (cls2 == Integer.TYPE) {
                        cls = Class.forName(str + "I");
                    } else if (cls2 == Long.TYPE) {
                        cls = Class.forName(str + "J");
                    } else if (cls2 == Float.TYPE) {
                        cls = Class.forName(str + "F");
                    } else {
                        cls = genericArrayType;
                        if (cls2 == Double.TYPE) {
                            cls = Class.forName(str + "D");
                        }
                    }
                } catch (ClassNotFoundException e) {
                    cls = genericArrayType;
                }
            }
        }
        return cls;
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00d6 A[PHI: r26
  0x00d6: PHI (r26v2 java.lang.String) = (r26v1 java.lang.String), (r26v3 java.lang.String) binds: [B:22:0x00ba, B:24:0x00d0] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void computeFields(java.lang.Class<?> r16, java.util.Map<java.lang.String, java.lang.String> r17, com.alibaba.fastjson.PropertyNamingStrategy r18, java.util.Map<java.lang.String, com.alibaba.fastjson.util.FieldInfo> r19, java.lang.reflect.Field[] r20) {
        /*
            Method dump skipped, instructions count: 282
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeFields(java.lang.Class, java.util.Map, com.alibaba.fastjson.PropertyNamingStrategy, java.util.Map, java.lang.reflect.Field[]):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0321, code lost:
    
        if (r0 == null) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x05ab, code lost:
    
        if (r0 == null) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0611, code lost:
    
        if (r0 == null) goto L111;
     */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x04de  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x05fd  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0620  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0669 A[PHI: r24 r25 r26 r34
  0x0669: PHI (r24v2 int) = (r24v1 int), (r24v6 int) binds: [B:108:0x039c, B:197:0x0635] A[DONT_GENERATE, DONT_INLINE]
  0x0669: PHI (r25v2 int) = (r25v1 int), (r25v8 int) binds: [B:108:0x039c, B:197:0x0635] A[DONT_GENERATE, DONT_INLINE]
  0x0669: PHI (r26v2 int) = (r26v1 int), (r26v8 int) binds: [B:108:0x039c, B:197:0x0635] A[DONT_GENERATE, DONT_INLINE]
  0x0669: PHI (r34v7 java.lang.String) = (r34v6 java.lang.String), (r34v13 java.lang.String) binds: [B:108:0x039c, B:197:0x0635] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0677  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x06ab  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x06f8  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0781  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0899  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x08c3  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x08d6  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x08e4  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0182 A[PHI: r37
  0x0182: PHI (r37v8 java.lang.reflect.Constructor<?>[]) = (r37v7 java.lang.reflect.Constructor<?>[]), (r37v12 java.lang.reflect.Constructor<?>[]) binds: [B:40:0x0109, B:42:0x011b] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x02bc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r16, com.alibaba.fastjson.annotation.JSONType r17, java.util.Map<java.lang.String, java.lang.String> r18, java.util.Map<java.lang.String, java.lang.reflect.Field> r19, boolean r20, com.alibaba.fastjson.PropertyNamingStrategy r21) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 2383
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, com.alibaba.fastjson.annotation.JSONType, java.util.Map, java.util.Map, boolean, com.alibaba.fastjson.PropertyNamingStrategy):java.util.List");
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map) {
        return computeGetters(cls, map, true);
    }

    public static List<FieldInfo> computeGetters(Class<?> cls, Map<String, String> map, boolean z) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        HashMap map2 = new HashMap();
        ParserConfig.parserAllFieldToCache(cls, map2);
        return computeGetters(cls, jSONType, map, map2, z, PropertyNamingStrategy.CamelCase);
    }

    public static List<FieldInfo> computeGettersWithFieldBase(Class<?> cls, Map<String, String> map, boolean z, PropertyNamingStrategy propertyNamingStrategy) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls2 = superclass;
            if (cls2 == null) {
                return getFieldInfos(cls, z, linkedHashMap);
            }
            computeFields(cls2, map, propertyNamingStrategy, linkedHashMap, cls2.getDeclaredFields());
            superclass = cls2.getSuperclass();
        }
    }

    private static Map<TypeVariable, Type> createActualTypeMap(TypeVariable[] typeVariableArr, Type[] typeArr) {
        int length = typeVariableArr.length;
        HashMap map = new HashMap(length);
        for (int i = 0; i < length; i++) {
            map.put(typeVariableArr[i], typeArr[i]);
        }
        return map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.util.Collection] */
    /* JADX WARN: Type inference failed for: r0v36, types: [java.lang.reflect.Type[]] */
    /* JADX WARN: Type inference failed for: r0v37 */
    public static Collection createCollection(Type type) {
        AbstractCollection arrayList;
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            arrayList = new ArrayList();
        } else if (rawClass.isAssignableFrom(HashSet.class)) {
            arrayList = new HashSet();
        } else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            arrayList = new LinkedHashSet();
        } else if (rawClass.isAssignableFrom(TreeSet.class)) {
            arrayList = new TreeSet();
        } else if (rawClass.isAssignableFrom(ArrayList.class)) {
            arrayList = new ArrayList();
        } else if (rawClass.isAssignableFrom(EnumSet.class)) {
            arrayList = EnumSet.noneOf(type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments()[0] : Object.class);
        } else if (rawClass.isAssignableFrom(Queue.class)) {
            arrayList = new LinkedList();
        } else {
            try {
                arrayList = (Collection) rawClass.newInstance();
            } catch (Exception e) {
                throw new JSONException("create instance error, class " + rawClass.getName());
            }
        }
        return arrayList;
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static long fnv1a_64(String str) {
        long jCharAt = -3750763034362895579L;
        for (int i = 0; i < str.length(); i++) {
            jCharAt = (jCharAt ^ str.charAt(i)) * 1099511628211L;
        }
        return jCharAt;
    }

    public static long fnv1a_64_lower(String str) {
        long j = -3750763034362895579L;
        int i = 0;
        while (i < str.length()) {
            char cCharAt = str.charAt(i);
            long j2 = j;
            if (cCharAt != '_') {
                if (cCharAt == '-') {
                    j2 = j;
                } else {
                    char c = cCharAt;
                    if (cCharAt >= 'A') {
                        c = cCharAt;
                        if (cCharAt <= 'Z') {
                            c = (char) (cCharAt + ' ');
                        }
                    }
                    j2 = (j ^ c) * 1099511628211L;
                }
            }
            i++;
            j = j2;
        }
        return j;
    }

    private static Type getActualType(Type type, Map<TypeVariable, Type> map) {
        return type instanceof TypeVariable ? map.get(type) : type instanceof ParameterizedType ? makeParameterizedType(getRawClass(type), ((ParameterizedType) type).getActualTypeArguments(), map) : type instanceof GenericArrayType ? new GenericArrayTypeImpl(getActualType(((GenericArrayType) type).getGenericComponentType(), map)) : type;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> cls2) {
        Annotation annotation = cls.getAnnotation(cls2);
        Type mixInAnnotations = JSON.getMixInAnnotations(cls);
        Class cls3 = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls3 != null) {
            Annotation annotation2 = cls3.getAnnotation(cls2);
            Annotation annotation3 = annotation2;
            if (annotation2 == null) {
                annotation3 = annotation2;
                if (cls3.getAnnotations().length > 0) {
                    annotation3 = annotation2;
                    for (Annotation annotation4 : cls3.getAnnotations()) {
                        annotation3 = annotation4.annotationType().getAnnotation(cls2);
                        if (annotation3 != null) {
                            break;
                        }
                    }
                }
            }
            if (annotation3 != null) {
                return (A) annotation3;
            }
        }
        Annotation annotation5 = annotation;
        if (annotation == null) {
            annotation5 = annotation;
            if (cls.getAnnotations().length > 0) {
                annotation5 = annotation;
                for (Annotation annotation6 : cls.getAnnotations()) {
                    annotation5 = annotation6.annotationType().getAnnotation(cls2);
                    if (annotation5 != null) {
                        break;
                    }
                }
            }
        }
        return (A) annotation5;
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> cls) {
        Field declaredField;
        A a = (A) field.getAnnotation(cls);
        Type mixInAnnotations = JSON.getMixInAnnotations(field.getDeclaringClass());
        Class superclass = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (superclass != null) {
            String name = field.getName();
            while (true) {
                declaredField = null;
                if (superclass == null) {
                    break;
                }
                declaredField = null;
                if (superclass == Object.class) {
                    break;
                }
                try {
                    declaredField = superclass.getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException e) {
                    superclass = superclass.getSuperclass();
                }
            }
            if (declaredField == null) {
                return a;
            }
            A a2 = (A) declaredField.getAnnotation(cls);
            if (a2 != null) {
                return a2;
            }
        }
        return a;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> cls) {
        Method declaredMethod;
        A a = (A) method.getAnnotation(cls);
        Type mixInAnnotations = JSON.getMixInAnnotations(method.getDeclaringClass());
        Class superclass = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (superclass != null) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            while (true) {
                declaredMethod = null;
                if (superclass == null) {
                    break;
                }
                declaredMethod = null;
                if (superclass == Object.class) {
                    break;
                }
                try {
                    declaredMethod = superclass.getDeclaredMethod(name, parameterTypes);
                    break;
                } catch (NoSuchMethodException e) {
                    superclass = superclass.getSuperclass();
                }
            }
            if (declaredMethod == null) {
                return a;
            }
            A a2 = (A) declaredMethod.getAnnotation(cls);
            if (a2 != null) {
                return a2;
            }
        }
        return a;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            Type type2 = ((TypeVariable) type).getBounds()[0];
            return type2 instanceof Class ? (Class) type2 : getClass(type2);
        }
        if (!(type instanceof WildcardType)) {
            return Object.class;
        }
        Type[] upperBounds = ((WildcardType) type).getUpperBounds();
        return upperBounds.length == 1 ? getClass(upperBounds[0]) : Object.class;
    }

    public static Class<?> getClassFromMapping(String str) {
        return mappings.get(str);
    }

    public static Class<?> getCollectionItemClass(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }
        Type type2 = ((ParameterizedType) type).getActualTypeArguments()[0];
        Type type3 = type2;
        if (type2 instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type2).getUpperBounds();
            type3 = type2;
            if (upperBounds.length == 1) {
                type3 = upperBounds[0];
            }
        }
        if (!(type3 instanceof Class)) {
            throw new JSONException("can not create ASMParser");
        }
        Class<?> cls = (Class) type3;
        if (Modifier.isPublic(cls.getModifiers())) {
            return cls;
        }
        throw new JSONException("can not create ASMParser");
    }

    private static Type getCollectionItemType(Class<?> cls) {
        return cls.getName().startsWith("java.") ? Object.class : getCollectionItemType(getCollectionSuperType(cls));
    }

    private static Type getCollectionItemType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (rawType == Collection.class) {
            return getWildcardTypeUpperBounds(actualTypeArguments[0]);
        }
        Class cls = (Class) rawType;
        Map<TypeVariable, Type> mapCreateActualTypeMap = createActualTypeMap(cls.getTypeParameters(), actualTypeArguments);
        Type collectionSuperType = getCollectionSuperType(cls);
        if (!(collectionSuperType instanceof ParameterizedType)) {
            return getCollectionItemType((Class<?>) collectionSuperType);
        }
        Class<?> rawClass = getRawClass(collectionSuperType);
        Type[] actualTypeArguments2 = ((ParameterizedType) collectionSuperType).getActualTypeArguments();
        return actualTypeArguments2.length > 0 ? getCollectionItemType(makeParameterizedType(rawClass, actualTypeArguments2, mapCreateActualTypeMap)) : getCollectionItemType(rawClass);
    }

    public static Type getCollectionItemType(Type type) {
        return type instanceof ParameterizedType ? getCollectionItemType((ParameterizedType) type) : type instanceof Class ? getCollectionItemType((Class<?>) type) : Object.class;
    }

    private static Type getCollectionSuperType(Class<?> cls) {
        Type type = null;
        for (Type type2 : cls.getGenericInterfaces()) {
            Class<?> rawClass = getRawClass(type2);
            if (rawClass == Collection.class) {
                return type2;
            }
            if (Collection.class.isAssignableFrom(rawClass)) {
                type = type2;
            }
        }
        Type genericSuperclass = type;
        if (type == null) {
            genericSuperclass = cls.getGenericSuperclass();
        }
        return genericSuperclass;
    }

    public static Field getField(Class<?> cls, String str, Field[] fieldArr) {
        char cCharAt;
        char cCharAt2;
        for (Field field : fieldArr) {
            String name = field.getName();
            if (str.equals(name)) {
                return field;
            }
            if (str.length() > 2 && (cCharAt = str.charAt(0)) >= 'a' && cCharAt <= 'z' && (cCharAt2 = str.charAt(1)) >= 'A' && cCharAt2 <= 'Z' && str.equalsIgnoreCase(name)) {
                return field;
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return getField(superclass, str, superclass.getDeclaredFields());
    }

    private static List<FieldInfo> getFieldInfos(Class<?> cls, boolean z, Map<String, FieldInfo> map) {
        ArrayList arrayList = new ArrayList();
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        String[] strArrOrders = jSONType != null ? jSONType.orders() : null;
        if (strArrOrders == null || strArrOrders.length <= 0) {
            Iterator<FieldInfo> it = map.values().iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            if (z) {
                Collections.sort(arrayList);
            }
        } else {
            LinkedHashMap linkedHashMap = new LinkedHashMap(arrayList.size());
            for (FieldInfo fieldInfo : map.values()) {
                linkedHashMap.put(fieldInfo.name, fieldInfo);
            }
            for (String str : strArrOrders) {
                FieldInfo fieldInfo2 = (FieldInfo) linkedHashMap.get(str);
                if (fieldInfo2 != null) {
                    arrayList.add(fieldInfo2);
                    linkedHashMap.remove(str);
                }
            }
            Iterator it2 = linkedHashMap.values().iterator();
            while (it2.hasNext()) {
                arrayList.add((FieldInfo) it2.next());
            }
        }
        return arrayList;
    }

    public static Type getGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return type;
        }
        Type genericParamType = type;
        if (type instanceof Class) {
            genericParamType = getGenericParamType(((Class) type).getGenericSuperclass());
        }
        return genericParamType;
    }

    public static Constructor getKoltinConstructor(Constructor[] constructorArr) {
        return getKoltinConstructor(constructorArr, null);
    }

    public static Constructor getKoltinConstructor(Constructor[] constructorArr, String[] strArr) {
        Constructor constructor = null;
        for (Constructor constructor2 : constructorArr) {
            Class<?>[] parameterTypes = constructor2.getParameterTypes();
            if ((strArr == null || parameterTypes.length == strArr.length) && ((parameterTypes.length <= 0 || !parameterTypes[parameterTypes.length - 1].getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")) && (constructor == null || constructor.getParameterTypes().length < parameterTypes.length))) {
                constructor = constructor2;
            }
        }
        return constructor;
    }

    public static String[] getKoltinConstructorParameters(Class cls) {
        if (kotlin_kclass_constructor == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_constructor = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getConstructor(Class.class);
            } catch (Throwable th) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null) {
            return null;
        }
        if (kotlin_kclass_getConstructors == null && !kotlin_class_klass_error) {
            try {
                kotlin_kclass_getConstructors = Class.forName("kotlin.reflect.jvm.internal.KClassImpl").getMethod("getConstructors", new Class[0]);
            } catch (Throwable th2) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kfunction_getParameters == null && !kotlin_class_klass_error) {
            try {
                kotlin_kfunction_getParameters = Class.forName("kotlin.reflect.KFunction").getMethod("getParameters", new Class[0]);
            } catch (Throwable th3) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kparameter_getName == null && !kotlin_class_klass_error) {
            try {
                kotlin_kparameter_getName = Class.forName("kotlin.reflect.KParameter").getMethod("getName", new Class[0]);
            } catch (Throwable th4) {
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_error) {
            return null;
        }
        try {
            Iterator it = ((Iterable) kotlin_kclass_getConstructors.invoke(kotlin_kclass_constructor.newInstance(cls), new Object[0])).iterator();
            Object obj = null;
            while (it.hasNext()) {
                Object next = it.next();
                List list = (List) kotlin_kfunction_getParameters.invoke(next, new Object[0]);
                if (obj == null || list.size() != 0) {
                    obj = next;
                }
                it.hasNext();
            }
            List list2 = (List) kotlin_kfunction_getParameters.invoke(obj, new Object[0]);
            String[] strArr = new String[list2.size()];
            for (int i = 0; i < list2.size(); i++) {
                strArr[i] = (String) kotlin_kparameter_getName.invoke(list2.get(i), new Object[0]);
            }
            return strArr;
        } catch (Throwable th5) {
            th5.printStackTrace();
            kotlin_error = true;
            return null;
        }
    }

    public static Annotation[][] getParameterAnnotations(Constructor constructor) {
        Constructor constructor2;
        Constructor declaredConstructor;
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        Type mixInAnnotations = JSON.getMixInAnnotations(constructor.getDeclaringClass());
        Class cls = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (cls != null) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            ArrayList arrayList = new ArrayList(2);
            Class<?> enclosingClass = cls.getEnclosingClass();
            while (true) {
                Class<?> cls2 = enclosingClass;
                if (cls2 == null) {
                    break;
                }
                arrayList.add(cls2);
                enclosingClass = cls2.getEnclosingClass();
            }
            int size = arrayList.size();
            Class superclass = cls;
            while (true) {
                Class cls3 = superclass;
                constructor2 = null;
                if (cls3 == null) {
                    break;
                }
                constructor2 = null;
                if (cls3 == Object.class) {
                    break;
                }
                try {
                    if (size != 0) {
                        Class<?>[] clsArr = new Class[parameterTypes.length + size];
                        System.arraycopy(parameterTypes, 0, clsArr, size, parameterTypes.length);
                        for (int i = size; i > 0; i--) {
                            int i2 = i - 1;
                            clsArr[i2] = (Class) arrayList.get(i2);
                        }
                        declaredConstructor = cls.getDeclaredConstructor(clsArr);
                    } else {
                        declaredConstructor = cls.getDeclaredConstructor(parameterTypes);
                    }
                    constructor2 = declaredConstructor;
                } catch (NoSuchMethodException e) {
                    size--;
                    superclass = cls3.getSuperclass();
                }
            }
            if (constructor2 == null) {
                return parameterAnnotations;
            }
            Annotation[][] parameterAnnotations2 = constructor2.getParameterAnnotations();
            if (parameterAnnotations2 != null) {
                return parameterAnnotations2;
            }
        }
        return parameterAnnotations;
    }

    public static Annotation[][] getParameterAnnotations(Method method) {
        Method declaredMethod;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Type mixInAnnotations = JSON.getMixInAnnotations(method.getDeclaringClass());
        Class superclass = mixInAnnotations instanceof Class ? (Class) mixInAnnotations : null;
        if (superclass != null) {
            String name = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            while (true) {
                declaredMethod = null;
                if (superclass == null) {
                    break;
                }
                declaredMethod = null;
                if (superclass == Object.class) {
                    break;
                }
                try {
                    declaredMethod = superclass.getDeclaredMethod(name, parameterTypes);
                    break;
                } catch (NoSuchMethodException e) {
                    superclass = superclass.getSuperclass();
                }
            }
            if (declaredMethod == null) {
                return parameterAnnotations;
            }
            Annotation[][] parameterAnnotations2 = declaredMethod.getParameterAnnotations();
            if (parameterAnnotations2 != null) {
                return parameterAnnotations2;
            }
        }
        return parameterAnnotations;
    }

    public static int getParserFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return Feature.m11of(jSONType.parseFeatures());
    }

    private static String getPropertyNameByCompatibleFieldName(Map<String, Field> map, String str, String str2, int i) {
        String str3 = str2;
        if (compatibleWithFieldName) {
            str3 = str2;
            if (!map.containsKey(str2)) {
                String strSubstring = str.substring(i);
                str3 = str2;
                if (map.containsKey(strSubstring)) {
                    str3 = strSubstring;
                }
            }
        }
        return str3;
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        throw new JSONException("TODO");
    }

    public static int getSerializeFeatures(Class<?> cls) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType == null) {
            return 0;
        }
        return SerializerFeature.m12of(jSONType.serialzeFeatures());
    }

    public static JSONField getSuperMethodAnnotation(Class<?> cls, Method method) throws SecurityException {
        boolean z;
        JSONField jSONField;
        boolean z2;
        JSONField jSONField2;
        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> cls2 : interfaces) {
                for (Method method2 : cls2.getMethods()) {
                    Class<?>[] parameterTypes2 = method2.getParameterTypes();
                    if (parameterTypes2.length == parameterTypes.length && method2.getName().equals(method.getName())) {
                        int i = 0;
                        while (true) {
                            if (i >= parameterTypes.length) {
                                z2 = true;
                                break;
                            }
                            if (!parameterTypes2[i].equals(parameterTypes[i])) {
                                z2 = false;
                                break;
                            }
                            i++;
                        }
                        if (z2 && (jSONField2 = (JSONField) getAnnotation(method2, JSONField.class)) != null) {
                            return jSONField2;
                        }
                    }
                }
            }
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null || !Modifier.isAbstract(superclass.getModifiers())) {
            return null;
        }
        Class<?>[] parameterTypes3 = method.getParameterTypes();
        for (Method method3 : superclass.getMethods()) {
            Class<?>[] parameterTypes4 = method3.getParameterTypes();
            if (parameterTypes4.length == parameterTypes3.length && method3.getName().equals(method.getName())) {
                int i2 = 0;
                while (true) {
                    if (i2 >= parameterTypes3.length) {
                        z = true;
                        break;
                    }
                    if (!parameterTypes4[i2].equals(parameterTypes3[i2])) {
                        z = false;
                        break;
                    }
                    i2++;
                }
                if (z && (jSONField = (JSONField) getAnnotation(method3, JSONField.class)) != null) {
                    return jSONField;
                }
            }
        }
        return null;
    }

    private static Type getWildcardTypeUpperBounds(Type type) {
        Class cls = type;
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            cls = upperBounds.length > 0 ? upperBounds[0] : Object.class;
        }
        return cls;
    }

    public static Annotation getXmlAccessorType(Class cls) {
        if (class_XmlAccessorType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch (Throwable th) {
                classXmlAccessorType_error = true;
            }
        }
        if (class_XmlAccessorType == null) {
            return null;
        }
        return getAnnotation((Class<?>) cls, class_XmlAccessorType);
    }

    public static int intValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        int iScale = bigDecimal.scale();
        return (iScale < -100 || iScale > 100) ? bigDecimal.intValueExact() : bigDecimal.intValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isAnnotationPresentManyToMany(java.lang.reflect.Method r3) {
        /*
            r0 = 0
            r5 = r0
            r0 = r3
            if (r0 != 0) goto L8
            r0 = 0
            return r0
        L8:
            java.lang.Class<? extends java.lang.annotation.Annotation> r0 = com.alibaba.fastjson.util.TypeUtils.class_ManyToMany
            if (r0 != 0) goto L25
            boolean r0 = com.alibaba.fastjson.util.TypeUtils.class_ManyToMany_error
            if (r0 != 0) goto L25
            java.lang.String r0 = "javax.persistence.ManyToMany"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch: java.lang.Throwable -> L20
            com.alibaba.fastjson.util.TypeUtils.class_ManyToMany = r0     // Catch: java.lang.Throwable -> L20
            goto L25
        L20:
            r6 = move-exception
            r0 = 1
            com.alibaba.fastjson.util.TypeUtils.class_ManyToMany_error = r0
        L25:
            r0 = r5
            r4 = r0
            java.lang.Class<? extends java.lang.annotation.Annotation> r0 = com.alibaba.fastjson.util.TypeUtils.class_ManyToMany
            if (r0 == 0) goto L45
            r0 = r3
            java.lang.Class<? extends java.lang.annotation.Annotation> r1 = com.alibaba.fastjson.util.TypeUtils.class_OneToMany
            boolean r0 = r0.isAnnotationPresent(r1)
            if (r0 != 0) goto L43
            r0 = r5
            r4 = r0
            r0 = r3
            java.lang.Class<? extends java.lang.annotation.Annotation> r1 = com.alibaba.fastjson.util.TypeUtils.class_ManyToMany
            boolean r0 = r0.isAnnotationPresent(r1)
            if (r0 == 0) goto L45
        L43:
            r0 = 1
            r4 = r0
        L45:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.isAnnotationPresentManyToMany(java.lang.reflect.Method):boolean");
    }

    public static boolean isAnnotationPresentOneToMany(Method method) {
        if (method == null) {
            return false;
        }
        if (class_OneToMany == null && !class_OneToMany_error) {
            try {
                class_OneToMany = Class.forName("javax.persistence.OneToMany");
            } catch (Throwable th) {
                class_OneToMany_error = true;
            }
        }
        Class<? extends Annotation> cls = class_OneToMany;
        boolean z = false;
        if (cls != null) {
            z = false;
            if (method.isAnnotationPresent(cls)) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isClob(Class cls) {
        if (class_Clob == null && !class_Clob_error) {
            try {
                class_Clob = Class.forName("java.sql.Clob");
            } catch (Throwable th) {
                class_Clob_error = true;
            }
        }
        if (class_Clob == null) {
            return false;
        }
        return class_Clob.isAssignableFrom(cls);
    }

    public static boolean isGenericParamType(Type type) {
        boolean z = true;
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class)) {
            return false;
        }
        Type genericSuperclass = ((Class) type).getGenericSuperclass();
        if (genericSuperclass == Object.class || !isGenericParamType(genericSuperclass)) {
            z = false;
        }
        return z;
    }

    public static boolean isHibernateInitialized(Object obj) {
        if (obj == null) {
            return false;
        }
        if (method_HibernateIsInitialized == null && !method_HibernateIsInitialized_error) {
            try {
                method_HibernateIsInitialized = Class.forName("org.hibernate.Hibernate").getMethod("isInitialized", Object.class);
            } catch (Throwable th) {
                method_HibernateIsInitialized_error = true;
            }
        }
        Method method = method_HibernateIsInitialized;
        if (method == null) {
            return true;
        }
        try {
            return ((Boolean) method.invoke(null, obj)).booleanValue();
        } catch (Throwable th2) {
            return true;
        }
    }

    private static boolean isJSONTypeIgnore(Class<?> cls, String str) {
        JSONType jSONType = (JSONType) getAnnotation(cls, JSONType.class);
        if (jSONType != null) {
            String[] strArrIncludes = jSONType.includes();
            if (strArrIncludes.length > 0) {
                for (String str2 : strArrIncludes) {
                    if (str.equals(str2)) {
                        return false;
                    }
                }
                return true;
            }
            for (String str3 : jSONType.ignores()) {
                if (str.equals(str3)) {
                    return true;
                }
            }
        }
        if (cls.getSuperclass() == Object.class || cls.getSuperclass() == null) {
            return false;
        }
        return isJSONTypeIgnore(cls.getSuperclass(), str);
    }

    public static boolean isJacksonCreator(Method method) {
        if (method == null) {
            return false;
        }
        if (class_JacksonCreator == null && !class_JacksonCreator_error) {
            try {
                class_JacksonCreator = Class.forName("com.fasterxml.jackson.annotation.JsonCreator");
            } catch (Throwable th) {
                class_JacksonCreator_error = true;
            }
        }
        Class<? extends Annotation> cls = class_JacksonCreator;
        boolean z = false;
        if (cls != null) {
            z = false;
            if (method.isAnnotationPresent(cls)) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isKotlin(Class cls) {
        boolean z = true;
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable th) {
                kotlin_metadata_error = true;
            }
        }
        if (kotlin_metadata == null || !cls.isAnnotationPresent(kotlin_metadata)) {
            z = false;
        }
        return z;
    }

    private static boolean isKotlinIgnore(Class cls, String str) {
        boolean z = true;
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                HashMap map = new HashMap();
                map.put(Class.forName("kotlin.ranges.CharRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.IntRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.LongRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.ClosedFloatRange"), new String[]{"getEndInclusive", "isEmpty"});
                map.put(Class.forName("kotlin.ranges.ClosedDoubleRange"), new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = map;
            } catch (Throwable th) {
                kotlinIgnores_error = true;
            }
        }
        if (kotlinIgnores == null) {
            return false;
        }
        String[] strArr = kotlinIgnores.get(cls);
        if (strArr == null || Arrays.binarySearch(strArr, str) < 0) {
            z = false;
        }
        return z;
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '+' || cCharAt == '-') {
                if (i != 0) {
                    return false;
                }
            } else if (cCharAt < '0' || cCharAt > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isPath(Class<?> cls) {
        if (pathClass == null && !pathClass_error) {
            try {
                pathClass = Class.forName("java.nio.file.Path");
            } catch (Throwable th) {
                pathClass_error = true;
            }
        }
        Class<?> cls2 = pathClass;
        if (cls2 != null) {
            return cls2.isAssignableFrom(cls);
        }
        return false;
    }

    public static boolean isProxy(Class<?> cls) {
        for (Class<?> cls2 : cls.getInterfaces()) {
            String name = cls2.getName();
            if (name.equals("net.sf.cglib.proxy.Factory") || name.equals("org.springframework.cglib.proxy.Factory") || name.equals("javassist.util.proxy.ProxyObject") || name.equals("org.apache.ibatis.javassist.util.proxy.ProxyObject") || name.equals("org.hibernate.proxy.HibernateProxy")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTransient(Method method) {
        if (method == null) {
            return false;
        }
        if (!transientClassInited) {
            try {
                transientClass = Class.forName("java.beans.Transient");
            } catch (Exception e) {
            } catch (Throwable th) {
                transientClassInited = true;
                throw th;
            }
            transientClassInited = true;
        }
        Class<? extends Annotation> cls = transientClass;
        boolean z = false;
        if (cls != null) {
            z = false;
            if (getAnnotation(method, cls) != null) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isXmlField(Class cls) {
        Annotation annotation;
        Object objInvoke;
        boolean z = true;
        if (class_XmlAccessorType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch (Throwable th) {
                classXmlAccessorType_error = true;
            }
        }
        if (class_XmlAccessorType == null || (annotation = getAnnotation((Class<?>) cls, (Class<Annotation>) class_XmlAccessorType)) == null) {
            return false;
        }
        if (method_XmlAccessorType_value == null && !classXmlAccessorType_error) {
            try {
                method_XmlAccessorType_value = class_XmlAccessorType.getMethod("value", new Class[0]);
            } catch (Throwable th2) {
                classXmlAccessorType_error = true;
            }
        }
        if (method_XmlAccessorType_value == null) {
            return false;
        }
        if (classXmlAccessorType_error) {
            objInvoke = null;
        } else {
            try {
                objInvoke = method_XmlAccessorType_value.invoke(annotation, new Object[0]);
            } catch (Throwable th3) {
                classXmlAccessorType_error = true;
            }
        }
        if (objInvoke == null) {
            return false;
        }
        if (class_XmlAccessType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessType = Class.forName("javax.xml.bind.annotation.XmlAccessType");
                field_XmlAccessType_FIELD = class_XmlAccessType.getField("FIELD");
                field_XmlAccessType_FIELD_VALUE = field_XmlAccessType_FIELD.get(null);
            } catch (Throwable th4) {
                classXmlAccessorType_error = true;
            }
        }
        if (objInvoke != field_XmlAccessType_FIELD_VALUE) {
            z = false;
        }
        return z;
    }

    public static Class<?> loadClass(String str) {
        return loadClass(str, null);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader) {
        return loadClass(str, classLoader, false);
    }

    public static Class<?> loadClass(String str, ClassLoader classLoader, boolean z) {
        Class<?> clsLoadClass;
        if (str == null || str.length() == 0 || str.length() > 128) {
            return null;
        }
        Class<?> cls = mappings.get(str);
        if (cls != null) {
            return cls;
        }
        if (str.charAt(0) == '[') {
            return Array.newInstance(loadClass(str.substring(1), classLoader), 0).getClass();
        }
        if (str.startsWith("L") && str.endsWith(";")) {
            return loadClass(str.substring(1, str.length() - 1), classLoader);
        }
        Class<?> cls2 = cls;
        if (classLoader != null) {
            cls2 = cls;
            try {
                Class<?> clsLoadClass2 = classLoader.loadClass(str);
                if (z) {
                    cls2 = clsLoadClass2;
                    mappings.put(str, clsLoadClass2);
                }
                return clsLoadClass2;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            clsLoadClass = cls2;
            if (contextClassLoader != null) {
                clsLoadClass = cls2;
                if (contextClassLoader != classLoader) {
                    clsLoadClass = contextClassLoader.loadClass(str);
                    if (z) {
                        try {
                            mappings.put(str, clsLoadClass);
                        } catch (Throwable th2) {
                        }
                    }
                    return clsLoadClass;
                }
            }
        } catch (Throwable th3) {
            clsLoadClass = cls2;
        }
        try {
            Class<?> cls3 = Class.forName(str);
            clsLoadClass = cls3;
            if (z) {
                clsLoadClass = cls3;
                mappings.put(str, cls3);
                clsLoadClass = cls3;
            }
        } catch (Throwable th4) {
        }
        return clsLoadClass;
    }

    public static long longExtractValue(Number number) {
        return number instanceof BigDecimal ? ((BigDecimal) number).longValueExact() : number.longValue();
    }

    public static long longValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0L;
        }
        int iScale = bigDecimal.scale();
        return (iScale < -100 || iScale > 100) ? bigDecimal.longValueExact() : bigDecimal.longValue();
    }

    private static ParameterizedType makeParameterizedType(Class<?> cls, Type[] typeArr, Map<TypeVariable, Type> map) {
        int length = typeArr.length;
        Type[] typeArr2 = new Type[length];
        for (int i = 0; i < length; i++) {
            typeArr2[i] = getActualType(typeArr[i], map);
        }
        return new ParameterizedTypeImpl(typeArr2, null, cls);
    }

    public static double parseDouble(String str) {
        double d;
        double d2;
        int length = str.length();
        if (length > 10) {
            return Double.parseDouble(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '-' && i2 == 0) {
                z = true;
            } else if (cCharAt == '.') {
                if (i != 0) {
                    return Double.parseDouble(str);
                }
                i = (length - i2) - 1;
            } else {
                if (cCharAt < '0' || cCharAt > '9') {
                    return Double.parseDouble(str);
                }
                j = (j * 10) + (cCharAt - '0');
            }
        }
        long j2 = j;
        if (z) {
            j2 = -j;
        }
        switch (i) {
            case 0:
                return j2;
            case 1:
                d = j2;
                d2 = 10.0d;
                Double.isNaN(d);
                break;
            case 2:
                d = j2;
                d2 = 100.0d;
                Double.isNaN(d);
                break;
            case 3:
                d = j2;
                d2 = 1000.0d;
                Double.isNaN(d);
                break;
            case 4:
                d = j2;
                d2 = 10000.0d;
                Double.isNaN(d);
                break;
            case 5:
                d = j2;
                d2 = 100000.0d;
                Double.isNaN(d);
                break;
            case 6:
                d = j2;
                d2 = 1000000.0d;
                Double.isNaN(d);
                break;
            case 7:
                d = j2;
                d2 = 1.0E7d;
                Double.isNaN(d);
                break;
            case 8:
                d = j2;
                d2 = 1.0E8d;
                Double.isNaN(d);
                break;
            case 9:
                d = j2;
                d2 = 1.0E9d;
                Double.isNaN(d);
                break;
            default:
                return Double.parseDouble(str);
        }
        return d / d2;
    }

    public static float parseFloat(String str) {
        float f;
        float f2;
        int length = str.length();
        if (length >= 10) {
            return Float.parseFloat(str);
        }
        long j = 0;
        boolean z = false;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '-' && i2 == 0) {
                z = true;
            } else if (cCharAt == '.') {
                if (i != 0) {
                    return Float.parseFloat(str);
                }
                i = (length - i2) - 1;
            } else {
                if (cCharAt < '0' || cCharAt > '9') {
                    return Float.parseFloat(str);
                }
                j = (j * 10) + (cCharAt - '0');
            }
        }
        long j2 = j;
        if (z) {
            j2 = -j;
        }
        switch (i) {
            case 0:
                return j2;
            case 1:
                f = j2;
                f2 = 10.0f;
                break;
            case 2:
                f = j2;
                f2 = 100.0f;
                break;
            case 3:
                f = j2;
                f2 = 1000.0f;
                break;
            case 4:
                f = j2;
                f2 = 10000.0f;
                break;
            case 5:
                f = j2;
                f2 = 100000.0f;
                break;
            case 6:
                f = j2;
                f2 = 1000000.0f;
                break;
            case 7:
                f = j2;
                f2 = 1.0E7f;
                break;
            case 8:
                f = j2;
                f2 = 1.0E8f;
                break;
            case 9:
                f = j2;
                f2 = 1.0E9f;
                break;
            default:
                return Float.parseFloat(str);
        }
        return f / f2;
    }

    static void setAccessible(AccessibleObject accessibleObject) {
        if (setAccessibleEnable && !accessibleObject.isAccessible()) {
            try {
                accessibleObject.setAccessible(true);
            } catch (AccessControlException e) {
                setAccessibleEnable = false;
            }
        }
    }

    public static short shortValue(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return (short) 0;
        }
        int iScale = bigDecimal.scale();
        return (iScale < -100 || iScale > 100) ? bigDecimal.shortValueExact() : bigDecimal.shortValue();
    }

    public static Locale toLocale(String str) {
        String[] strArrSplit = str.split("_");
        return strArrSplit.length == 1 ? new Locale(strArrSplit[0]) : strArrSplit.length == 2 ? new Locale(strArrSplit[0], strArrSplit[1]) : new Locale(strArrSplit[0], strArrSplit[1], strArrSplit[2]);
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception e) {
            } catch (Throwable th) {
                optionalClassInited = true;
                throw th;
            }
            optionalClassInited = true;
        }
        Type type2 = type;
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            type2 = type;
            if (parameterizedType.getRawType() == optionalClass) {
                type2 = parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type2;
    }
}
