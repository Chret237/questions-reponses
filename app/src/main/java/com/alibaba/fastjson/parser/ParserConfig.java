package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONPDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.parser.deserializer.PropertyProcessable;
import com.alibaba.fastjson.parser.deserializer.PropertyProcessableDeserializer;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.ByteBufferCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.GuavaCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.JodaCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.serializer.ReferenceCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.support.moneta.MonetaCodec;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;
import java.security.AccessControlException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/ParserConfig.class */
public class ParserConfig {
    public static final String AUTOTYPE_ACCEPT = "fastjson.parser.autoTypeAccept";
    private static final String[] AUTO_TYPE_ACCEPT_LIST;
    private static final long[] INTERNAL_WHITELIST_HASHCODES;
    private static boolean awtError;
    public static ParserConfig global;
    private static boolean guavaError;
    private static boolean jdk8Error;
    private static boolean jodaError;
    private long[] acceptHashCodes;
    private boolean asmEnable;
    protected ASMDeserializerFactory asmFactory;
    private boolean autoTypeSupport;
    public boolean compatibleWithJavaBean;
    protected ClassLoader defaultClassLoader;
    private long[] denyHashCodes;
    private final IdentityHashMap<Type, ObjectDeserializer> deserializers;
    public final boolean fieldBased;
    private boolean jacksonCompatible;
    private final IdentityHashMap<Type, IdentityHashMap<Type, ObjectDeserializer>> mixInDeserializers;
    private List<Module> modules;
    public PropertyNamingStrategy propertyNamingStrategy;
    public final SymbolTable symbolTable;
    private final ConcurrentMap<String, Class<?>> typeMapping;
    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    public static final String[] DENYS = splitItemsFormProperty(IOUtils.getStringProperty(DENY_PROPERTY));
    public static final String AUTOTYPE_SUPPORT_PROPERTY = "fastjson.parser.autoTypeSupport";
    public static final boolean AUTO_SUPPORT = "true".equals(IOUtils.getStringProperty(AUTOTYPE_SUPPORT_PROPERTY));

    static {
        String[] strArrSplitItemsFormProperty = splitItemsFormProperty(IOUtils.getStringProperty(AUTOTYPE_ACCEPT));
        String[] strArr = strArrSplitItemsFormProperty;
        if (strArrSplitItemsFormProperty == null) {
            strArr = new String[0];
        }
        AUTO_TYPE_ACCEPT_LIST = strArr;
        long[] jArr = new long[58];
        for (int i = 0; i < 58; i++) {
            jArr[i] = TypeUtils.fnv1a_64(new String[]{"java.awt.Rectangle", "java.awt.Point", "java.awt.Font", "java.awt.Color", "com.alibaba.fastjson.util.AntiCollisionHashMap", "com.alipay.sofa.rpc.core.exception.SofaTimeOutException", "java.util.Collections.UnmodifiableMap", "java.util.concurrent.ConcurrentSkipListMap", "java.util.concurrent.ConcurrentSkipListSet", "org.springframework.dao.CannotAcquireLockException", "org.springframework.dao.CannotSerializeTransactionException", "org.springframework.dao.CleanupFailureDataAccessException", "org.springframework.dao.ConcurrencyFailureException", "org.springframework.dao.DataAccessResourceFailureException", "org.springframework.dao.DataIntegrityViolationException", "org.springframework.dao.DataRetrievalFailureException", "org.springframework.dao.DeadlockLoserDataAccessException", "org.springframework.dao.DuplicateKeyException", "org.springframework.dao.EmptyResultDataAccessException", "org.springframework.dao.IncorrectResultSizeDataAccessException", "org.springframework.dao.IncorrectUpdateSemanticsDataAccessException", "org.springframework.dao.InvalidDataAccessApiUsageException", "org.springframework.dao.InvalidDataAccessResourceUsageException", "org.springframework.dao.NonTransientDataAccessException", "org.springframework.dao.NonTransientDataAccessResourceException", "org.springframework.dao.OptimisticLockingFailureException", "org.springframework.dao.PermissionDeniedDataAccessException", "org.springframework.dao.PessimisticLockingFailureException", "org.springframework.dao.QueryTimeoutException", "org.springframework.dao.RecoverableDataAccessException", "org.springframework.dao.TransientDataAccessException", "org.springframework.dao.TransientDataAccessResourceException", "org.springframework.dao.TypeMismatchDataAccessException", "org.springframework.dao.UncategorizedDataAccessException", "org.springframework.jdbc.BadSqlGrammarException", "org.springframework.jdbc.CannotGetJdbcConnectionException", "org.springframework.jdbc.IncorrectResultSetColumnCountException", "org.springframework.jdbc.InvalidResultSetAccessException", "org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException", "org.springframework.jdbc.LobRetrievalFailureException", "org.springframework.jdbc.SQLWarningException", "org.springframework.jdbc.UncategorizedSQLException", "org.springframework.cache.support.NullValue", "org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken", "org.springframework.security.oauth2.common.DefaultOAuth2AccessToken", "org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken", "org.springframework.util.LinkedMultiValueMap", "org.springframework.util.LinkedCaseInsensitiveMap", "org.springframework.remoting.support.RemoteInvocation", "org.springframework.remoting.support.RemoteInvocationResult", "org.springframework.security.web.savedrequest.DefaultSavedRequest", "org.springframework.security.web.savedrequest.SavedCookie", "org.springframework.security.web.csrf.DefaultCsrfToken", "org.springframework.security.web.authentication.WebAuthenticationDetails", "org.springframework.security.core.context.SecurityContextImpl", "org.springframework.security.authentication.UsernamePasswordAuthenticationToken", "org.springframework.security.core.authority.SimpleGrantedAuthority", "org.springframework.security.core.userdetails.User"}[i]);
        }
        Arrays.sort(jArr);
        INTERNAL_WHITELIST_HASHCODES = jArr;
        global = new ParserConfig();
        awtError = false;
        jdk8Error = false;
        jodaError = false;
        guavaError = false;
    }

    public ParserConfig() {
        this(false);
    }

    public ParserConfig(ASMDeserializerFactory aSMDeserializerFactory) {
        this(aSMDeserializerFactory, null, false);
    }

    private ParserConfig(ASMDeserializerFactory aSMDeserializerFactory, ClassLoader classLoader, boolean z) {
        this.deserializers = new IdentityHashMap<>();
        this.mixInDeserializers = new IdentityHashMap<>(16);
        this.typeMapping = new ConcurrentHashMap(16, 0.75f, 1);
        this.asmEnable = !ASMUtils.IS_ANDROID;
        this.symbolTable = new SymbolTable(4096);
        this.autoTypeSupport = AUTO_SUPPORT;
        this.jacksonCompatible = false;
        this.compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
        this.modules = new ArrayList();
        this.denyHashCodes = $d2j$hex$32979107$decode_J("02ea2fcc0bc7d080ef7aafbef92bfc86a633ea071b2af5874344a9b20cd4ad8e803ff00dfaf9758faf3079153fa57291b84f360e712d1292b84f360e712d1292c5730f58265c30943f7ddf3128793794208b17932fa623a150c44410ce8258a837490cb1fdaf3daa4d339ab9954cffaf4fc96e741c340fb4a2135d7f75ede8b7cef06627c19dddbc8b80f2badee10bc04cfeec58094d66c20624e7e3bf9e59c78e72fd82506963c9346d31b3f4cdefd1b17bd2bbf6dbc9d975b3cd10f3df2ddf2f58424860e49ae08f46bfd5049891e17e466ce5c73aebe22b69ad1fa5d603e62a961d5be54b18e90708f625ad0bf2e99176820ce23a77fc20d7560061fc5bfd0534edf1801addffe5c555cd67e010003ec16c1319167600b80172cb68500803a3aba378bc115b04de5a95a52f296e0bf05efd661b51e60e3e9b9d84cabdb21058317267b4774214f04ad0ea6f2edb14fa4c292db26c4b151ad4aeea97263b19ae3dff58338c0a1e494eef8f04f6d22429af77b832075d2731d929febbefdf2adf4c347a46373a2bd8b051c8bb8d302d4c55d4d8abb43b311083a169530b2f33e9beee6b0b3e9a332f3f521f924bc633f1fd2984e71ea8349b8b0c38b2f42638f00c92012e948f39a9fbc90f561ad1429208aed2c90d3243b95f448f20890e44571f84b5a408c8462c202803b397374a0dd758e754e2a34b75c616ff908cf04e1f82136ddc0dd14fbcbce36cb4b67d52fc4f456d4a502857ac99a013125c9b59fe5e2e075cd85b5ad140ab7130cbb05a760437b9e5d3745d84ed40dedde6925d347c39741224db62b9c7170ae620a263d2f0e03254834967fb95c13ea5d46b74ff310e26b90bb574d30f1d87f560cc75effe07516a587a76f39ca12736eea77a");
        long[] jArr = new long[AUTO_TYPE_ACCEPT_LIST.length];
        int i = 0;
        while (true) {
            String[] strArr = AUTO_TYPE_ACCEPT_LIST;
            if (i >= strArr.length) {
                break;
            }
            jArr[i] = TypeUtils.fnv1a_64(strArr[i]);
            i++;
        }
        Arrays.sort(jArr);
        this.acceptHashCodes = jArr;
        this.fieldBased = z;
        ASMDeserializerFactory aSMDeserializerFactory2 = aSMDeserializerFactory;
        if (aSMDeserializerFactory == null) {
            aSMDeserializerFactory2 = aSMDeserializerFactory;
            if (!ASMUtils.IS_ANDROID) {
                try {
                    aSMDeserializerFactory2 = classLoader == null ? new ASMDeserializerFactory(new ASMClassLoader()) : new ASMDeserializerFactory(classLoader);
                } catch (ExceptionInInitializerError | NoClassDefFoundError | AccessControlException e) {
                    aSMDeserializerFactory2 = aSMDeserializerFactory;
                }
            }
        }
        this.asmFactory = aSMDeserializerFactory2;
        if (aSMDeserializerFactory2 == null) {
            this.asmEnable = false;
        }
        initDeserializers();
        addItemsToDeny(DENYS);
        addItemsToAccept(AUTO_TYPE_ACCEPT_LIST);
    }

    public ParserConfig(ClassLoader classLoader) {
        this(null, classLoader, false);
    }

    public ParserConfig(boolean z) {
        this(null, null, z);
    }

    private void addItemsToAccept(String[] strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            addAccept(str);
        }
    }

    private void addItemsToDeny(String[] strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            addDeny(str);
        }
    }

    public static Field getFieldFromCache(String str, Map<String, Field> map) {
        Field field = map.get(str);
        Field field2 = field;
        if (field == null) {
            field2 = map.get("_" + str);
        }
        Field field3 = field2;
        if (field2 == null) {
            field3 = map.get("m_" + str);
        }
        Field value = field3;
        if (field3 == null) {
            char cCharAt = str.charAt(0);
            Field field4 = field3;
            if (cCharAt >= 'a') {
                field4 = field3;
                if (cCharAt <= 'z') {
                    char[] charArray = str.toCharArray();
                    charArray[0] = (char) (charArray[0] - ' ');
                    field4 = map.get(new String(charArray));
                }
            }
            value = field4;
            if (str.length() > 2) {
                char cCharAt2 = str.charAt(1);
                value = field4;
                if (str.length() > 2) {
                    value = field4;
                    if (cCharAt >= 'a') {
                        value = field4;
                        if (cCharAt <= 'z') {
                            value = field4;
                            if (cCharAt2 >= 'A') {
                                value = field4;
                                if (cCharAt2 <= 'Z') {
                                    Iterator<Map.Entry<String, Field>> it = map.entrySet().iterator();
                                    while (true) {
                                        value = field4;
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        Map.Entry<String, Field> next = it.next();
                                        if (str.equalsIgnoreCase(next.getKey())) {
                                            value = next.getValue();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    private void initDeserializers() {
        this.deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
        this.deserializers.put(Timestamp.class, SqlDateDeserializer.instance_timestamp);
        this.deserializers.put(Date.class, SqlDateDeserializer.instance);
        this.deserializers.put(Time.class, TimeDeserializer.instance);
        this.deserializers.put(java.util.Date.class, DateCodec.instance);
        this.deserializers.put(Calendar.class, CalendarCodec.instance);
        this.deserializers.put(XMLGregorianCalendar.class, CalendarCodec.instance);
        this.deserializers.put(JSONObject.class, MapDeserializer.instance);
        this.deserializers.put(JSONArray.class, CollectionCodec.instance);
        this.deserializers.put(Map.class, MapDeserializer.instance);
        this.deserializers.put(HashMap.class, MapDeserializer.instance);
        this.deserializers.put(LinkedHashMap.class, MapDeserializer.instance);
        this.deserializers.put(TreeMap.class, MapDeserializer.instance);
        this.deserializers.put(ConcurrentMap.class, MapDeserializer.instance);
        this.deserializers.put(ConcurrentHashMap.class, MapDeserializer.instance);
        this.deserializers.put(Collection.class, CollectionCodec.instance);
        this.deserializers.put(List.class, CollectionCodec.instance);
        this.deserializers.put(ArrayList.class, CollectionCodec.instance);
        this.deserializers.put(Object.class, JavaObjectDeserializer.instance);
        this.deserializers.put(String.class, StringCodec.instance);
        this.deserializers.put(StringBuffer.class, StringCodec.instance);
        this.deserializers.put(StringBuilder.class, StringCodec.instance);
        this.deserializers.put(Character.TYPE, CharacterCodec.instance);
        this.deserializers.put(Character.class, CharacterCodec.instance);
        this.deserializers.put(Byte.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Byte.class, NumberDeserializer.instance);
        this.deserializers.put(Short.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Short.class, NumberDeserializer.instance);
        this.deserializers.put(Integer.TYPE, IntegerCodec.instance);
        this.deserializers.put(Integer.class, IntegerCodec.instance);
        this.deserializers.put(Long.TYPE, LongCodec.instance);
        this.deserializers.put(Long.class, LongCodec.instance);
        this.deserializers.put(BigInteger.class, BigIntegerCodec.instance);
        this.deserializers.put(BigDecimal.class, BigDecimalCodec.instance);
        this.deserializers.put(Float.TYPE, FloatCodec.instance);
        this.deserializers.put(Float.class, FloatCodec.instance);
        this.deserializers.put(Double.TYPE, NumberDeserializer.instance);
        this.deserializers.put(Double.class, NumberDeserializer.instance);
        this.deserializers.put(Boolean.TYPE, BooleanCodec.instance);
        this.deserializers.put(Boolean.class, BooleanCodec.instance);
        this.deserializers.put(Class.class, MiscCodec.instance);
        this.deserializers.put(char[].class, new CharArrayCodec());
        this.deserializers.put(AtomicBoolean.class, BooleanCodec.instance);
        this.deserializers.put(AtomicInteger.class, IntegerCodec.instance);
        this.deserializers.put(AtomicLong.class, LongCodec.instance);
        this.deserializers.put(AtomicReference.class, ReferenceCodec.instance);
        this.deserializers.put(WeakReference.class, ReferenceCodec.instance);
        this.deserializers.put(SoftReference.class, ReferenceCodec.instance);
        this.deserializers.put(UUID.class, MiscCodec.instance);
        this.deserializers.put(TimeZone.class, MiscCodec.instance);
        this.deserializers.put(Locale.class, MiscCodec.instance);
        this.deserializers.put(Currency.class, MiscCodec.instance);
        this.deserializers.put(Inet4Address.class, MiscCodec.instance);
        this.deserializers.put(Inet6Address.class, MiscCodec.instance);
        this.deserializers.put(InetSocketAddress.class, MiscCodec.instance);
        this.deserializers.put(File.class, MiscCodec.instance);
        this.deserializers.put(URI.class, MiscCodec.instance);
        this.deserializers.put(URL.class, MiscCodec.instance);
        this.deserializers.put(Pattern.class, MiscCodec.instance);
        this.deserializers.put(Charset.class, MiscCodec.instance);
        this.deserializers.put(JSONPath.class, MiscCodec.instance);
        this.deserializers.put(Number.class, NumberDeserializer.instance);
        this.deserializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        this.deserializers.put(AtomicLongArray.class, AtomicCodec.instance);
        this.deserializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);
        this.deserializers.put(Serializable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Comparable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(Closeable.class, JavaObjectDeserializer.instance);
        this.deserializers.put(JSONPObject.class, new JSONPDeserializer());
    }

    public static boolean isPrimitive2(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Character.class || cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == Float.class || cls == Double.class || cls == BigInteger.class || cls == BigDecimal.class || cls == String.class || cls == java.util.Date.class || cls == Date.class || cls == Time.class || cls == Timestamp.class || cls.isEnum();
    }

    public static void parserAllFieldToCache(Class<?> cls, Map<String, Field> map) {
        for (Field field : cls.getDeclaredFields()) {
            String name = field.getName();
            if (!map.containsKey(name)) {
                map.put(name, field);
            }
        }
        if (cls.getSuperclass() == null || cls.getSuperclass() == Object.class) {
            return;
        }
        parserAllFieldToCache(cls.getSuperclass(), map);
    }

    private static String[] splitItemsFormProperty(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        return str.split(",");
    }

    public void addAccept(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        long jFnv1a_64 = TypeUtils.fnv1a_64(str);
        if (Arrays.binarySearch(this.acceptHashCodes, jFnv1a_64) >= 0) {
            return;
        }
        long[] jArr = this.acceptHashCodes;
        int length = jArr.length + 1;
        long[] jArr2 = new long[length];
        jArr2[length - 1] = jFnv1a_64;
        System.arraycopy(jArr, 0, jArr2, 0, jArr.length);
        Arrays.sort(jArr2);
        this.acceptHashCodes = jArr2;
    }

    public void addDeny(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        long jFnv1a_64 = TypeUtils.fnv1a_64(str);
        if (Arrays.binarySearch(this.denyHashCodes, jFnv1a_64) >= 0) {
            return;
        }
        long[] jArr = this.denyHashCodes;
        int length = jArr.length + 1;
        long[] jArr2 = new long[length];
        jArr2[length - 1] = jFnv1a_64;
        System.arraycopy(jArr, 0, jArr2, 0, jArr.length);
        Arrays.sort(jArr2);
        this.denyHashCodes = jArr2;
    }

    public Class<?> checkAutoType(Class cls) {
        return get(cls) != null ? cls : checkAutoType(cls.getName(), null, JSON.DEFAULT_PARSER_FEATURE);
    }

    public Class<?> checkAutoType(String str, Class<?> cls) {
        return checkAutoType(str, cls, JSON.DEFAULT_PARSER_FEATURE);
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x03c7  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x040c  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x04e8  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x04f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Class<?> checkAutoType(java.lang.String r6, java.lang.Class<?> r7, int r8) {
        /*
            Method dump skipped, instructions count: 1423
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.ParserConfig.checkAutoType(java.lang.String, java.lang.Class, int):java.lang.Class");
    }

    public void clearDeserializers() {
        this.deserializers.clear();
        initDeserializers();
    }

    public void configFromPropety(Properties properties) {
        addItemsToDeny(splitItemsFormProperty(properties.getProperty(DENY_PROPERTY)));
        addItemsToAccept(splitItemsFormProperty(properties.getProperty(AUTOTYPE_ACCEPT)));
        String property = properties.getProperty(AUTOTYPE_SUPPORT_PROPERTY);
        if ("true".equals(property)) {
            this.autoTypeSupport = true;
        } else if ("false".equals(property)) {
            this.autoTypeSupport = false;
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo, FieldInfo fieldInfo) {
        Class<?> cls = javaBeanInfo.clazz;
        Class<?> cls2 = fieldInfo.fieldClass;
        JSONField annotation = fieldInfo.getAnnotation();
        Class<?> clsDeserializeUsing = null;
        if (annotation != null) {
            clsDeserializeUsing = annotation.deserializeUsing();
            if (clsDeserializeUsing == Void.class) {
                clsDeserializeUsing = null;
            }
        }
        return (clsDeserializeUsing == null && (cls2 == List.class || cls2 == ArrayList.class)) ? new ArrayListTypeFieldDeserializer(parserConfig, cls, fieldInfo) : new DefaultFieldDeserializer(parserConfig, cls, fieldInfo);
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> cls, Type type) {
        JSONField annotation;
        boolean zAsm = this.asmEnable & (!this.fieldBased);
        boolean z = zAsm;
        if (zAsm) {
            JSONType jSONType = (JSONType) TypeUtils.getAnnotation(cls, JSONType.class);
            if (jSONType != null) {
                Class<?> clsDeserializer = jSONType.deserializer();
                if (clsDeserializer != Void.class) {
                    try {
                        Object objNewInstance = clsDeserializer.newInstance();
                        if (objNewInstance instanceof ObjectDeserializer) {
                            return (ObjectDeserializer) objNewInstance;
                        }
                    } catch (Throwable th) {
                    }
                }
                zAsm = jSONType.asm();
            }
            z = zAsm;
            if (zAsm) {
                Class<?> builderClass = JavaBeanInfo.getBuilderClass(cls, jSONType);
                Class<?> cls2 = builderClass;
                if (builderClass == null) {
                    cls2 = cls;
                }
                while (true) {
                    if (!Modifier.isPublic(cls2.getModifiers())) {
                        z = false;
                        break;
                    }
                    Class<? super Object> superclass = cls2.getSuperclass();
                    z = zAsm;
                    if (superclass == Object.class) {
                        break;
                    }
                    cls2 = superclass;
                    if (superclass == null) {
                        z = zAsm;
                        break;
                    }
                }
            }
        }
        boolean z2 = z;
        if (cls.getTypeParameters().length != 0) {
            z2 = false;
        }
        boolean z3 = z2;
        if (z2) {
            ASMDeserializerFactory aSMDeserializerFactory = this.asmFactory;
            z3 = z2;
            if (aSMDeserializerFactory != null) {
                z3 = z2;
                if (aSMDeserializerFactory.classLoader.isExternalClass(cls)) {
                    z3 = false;
                }
            }
        }
        boolean zCheckName = z3;
        if (z3) {
            zCheckName = ASMUtils.checkName(cls.getSimpleName());
        }
        boolean z4 = zCheckName;
        if (zCheckName) {
            if (cls.isInterface()) {
                zCheckName = false;
            }
            JavaBeanInfo javaBeanInfoBuild = JavaBeanInfo.build(cls, type, this.propertyNamingStrategy, false, TypeUtils.compatibleWithJavaBean, this.jacksonCompatible);
            boolean z5 = zCheckName;
            if (zCheckName) {
                z5 = zCheckName;
                if (javaBeanInfoBuild.fields.length > 200) {
                    z5 = false;
                }
            }
            Constructor<?> constructor = javaBeanInfoBuild.defaultConstructor;
            boolean z6 = z5;
            if (z5) {
                z6 = z5;
                if (constructor == null) {
                    z6 = z5;
                    if (!cls.isInterface()) {
                        z6 = false;
                    }
                }
            }
            FieldInfo[] fieldInfoArr = javaBeanInfoBuild.fields;
            int length = fieldInfoArr.length;
            int i = 0;
            while (true) {
                z4 = z6;
                if (i >= length) {
                    break;
                }
                FieldInfo fieldInfo = fieldInfoArr[i];
                if (!fieldInfo.getOnly) {
                    Class<?> cls3 = fieldInfo.fieldClass;
                    if (!Modifier.isPublic(cls3.getModifiers()) || ((cls3.isMemberClass() && !Modifier.isStatic(cls3.getModifiers())) || ((fieldInfo.getMember() != null && !ASMUtils.checkName(fieldInfo.getMember().getName())) || (((annotation = fieldInfo.getAnnotation()) != null && (!ASMUtils.checkName(annotation.name()) || annotation.format().length() != 0 || annotation.deserializeUsing() != Void.class || annotation.parseFeatures().length != 0 || annotation.unwrapped())) || ((fieldInfo.method != null && fieldInfo.method.getParameterTypes().length > 1) || (cls3.isEnum() && !(getDeserializer(cls3) instanceof EnumDeserializer))))))) {
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            z4 = false;
        }
        boolean z7 = z4;
        if (z4) {
            z7 = z4;
            if (cls.isMemberClass()) {
                z7 = z4;
                if (!Modifier.isStatic(cls.getModifiers())) {
                    z7 = false;
                }
            }
        }
        if (z7 && TypeUtils.isXmlField(cls)) {
            z7 = false;
        }
        if (!z7) {
            return new JavaBeanDeserializer(this, cls, type);
        }
        JavaBeanInfo javaBeanInfoBuild2 = JavaBeanInfo.build(cls, type, this.propertyNamingStrategy);
        try {
            return this.asmFactory.createJavaBeanDeserializer(this, javaBeanInfoBuild2);
        } catch (JSONException e) {
            return new JavaBeanDeserializer(this, javaBeanInfoBuild2);
        } catch (NoSuchMethodException e2) {
            return new JavaBeanDeserializer(this, cls, type);
        } catch (Exception e3) {
            throw new JSONException("create asm deserializer error, " + cls.getName(), e3);
        }
    }

    public ObjectDeserializer get(Type type) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.deserializers.get(type);
        }
        IdentityHashMap<Type, ObjectDeserializer> identityHashMap = this.mixInDeserializers.get(type);
        if (identityHashMap == null) {
            return null;
        }
        return identityHashMap.get(mixInAnnotations);
    }

    public ClassLoader getDefaultClassLoader() {
        return this.defaultClassLoader;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return this.deserializers;
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public ObjectDeserializer getDeserializer(Class<?> cls, Type type) {
        ObjectDeserializer throwableDeserializer;
        Class<?> clsMappingTo;
        ObjectDeserializer objectDeserializer = get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        Type type2 = type;
        if (type == null) {
            type2 = cls;
        }
        ObjectDeserializer objectDeserializer2 = get(type2);
        if (objectDeserializer2 != null) {
            return objectDeserializer2;
        }
        JSONType jSONType = (JSONType) TypeUtils.getAnnotation(cls, JSONType.class);
        if (jSONType != null && (clsMappingTo = jSONType.mappingTo()) != Void.class) {
            return getDeserializer(clsMappingTo, clsMappingTo);
        }
        if ((type2 instanceof WildcardType) || (type2 instanceof TypeVariable) || (type2 instanceof ParameterizedType)) {
            objectDeserializer2 = get(cls);
        }
        if (objectDeserializer2 != null) {
            return objectDeserializer2;
        }
        Iterator<Module> it = this.modules.iterator();
        while (it.hasNext()) {
            ObjectDeserializer objectDeserializerCreateDeserializer = it.next().createDeserializer(this, cls);
            objectDeserializer2 = objectDeserializerCreateDeserializer;
            if (objectDeserializerCreateDeserializer != null) {
                putDeserializer(type2, objectDeserializerCreateDeserializer);
                return objectDeserializerCreateDeserializer;
            }
        }
        String strReplace = cls.getName().replace('$', '.');
        ObjectDeserializer objectDeserializer3 = objectDeserializer2;
        if (strReplace.startsWith("java.awt.")) {
            objectDeserializer3 = objectDeserializer2;
            if (AwtCodec.support(cls)) {
                objectDeserializer3 = objectDeserializer2;
                if (!awtError) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            String str = new String[]{"java.awt.Point", "java.awt.Font", "java.awt.Rectangle", "java.awt.Color"}[i];
                            if (str.equals(strReplace)) {
                                Type cls2 = Class.forName(str);
                                ObjectDeserializer objectDeserializer4 = AwtCodec.instance;
                                putDeserializer(cls2, objectDeserializer4);
                                return objectDeserializer4;
                            }
                        } catch (Throwable th) {
                            awtError = true;
                        }
                    }
                    objectDeserializer3 = AwtCodec.instance;
                }
            }
        }
        ObjectDeserializer objectDeserializer5 = objectDeserializer3;
        if (!jdk8Error) {
            ObjectDeserializer objectDeserializer6 = objectDeserializer3;
            try {
                if (strReplace.startsWith("java.time.")) {
                    int i2 = 0;
                    while (true) {
                        objectDeserializer5 = objectDeserializer3;
                        if (i2 >= 12) {
                            break;
                        }
                        String str2 = new String[]{"java.time.LocalDateTime", "java.time.LocalDate", "java.time.LocalTime", "java.time.ZonedDateTime", "java.time.OffsetDateTime", "java.time.OffsetTime", "java.time.ZoneOffset", "java.time.ZoneRegion", "java.time.ZoneId", "java.time.Period", "java.time.Duration", "java.time.Instant"}[i2];
                        if (str2.equals(strReplace)) {
                            Type cls3 = Class.forName(str2);
                            ObjectDeserializer objectDeserializer7 = Jdk8DateCodec.instance;
                            putDeserializer(cls3, objectDeserializer7);
                            return objectDeserializer7;
                        }
                        i2++;
                    }
                } else {
                    objectDeserializer5 = objectDeserializer3;
                    if (strReplace.startsWith("java.util.Optional")) {
                        int i3 = 0;
                        while (true) {
                            objectDeserializer5 = objectDeserializer3;
                            if (i3 >= 4) {
                                break;
                            }
                            String str3 = new String[]{"java.util.Optional", "java.util.OptionalDouble", "java.util.OptionalInt", "java.util.OptionalLong"}[i3];
                            if (str3.equals(strReplace)) {
                                Type cls4 = Class.forName(str3);
                                ObjectDeserializer objectDeserializer8 = OptionalCodec.instance;
                                putDeserializer(cls4, objectDeserializer8);
                                return objectDeserializer8;
                            }
                            i3++;
                        }
                    }
                }
            } catch (Throwable th2) {
                jdk8Error = true;
                objectDeserializer5 = objectDeserializer6;
            }
        }
        ObjectDeserializer objectDeserializer9 = objectDeserializer5;
        if (!jodaError) {
            ObjectDeserializer objectDeserializer10 = objectDeserializer5;
            objectDeserializer9 = objectDeserializer5;
            try {
                if (strReplace.startsWith("org.joda.time.")) {
                    int i4 = 0;
                    while (true) {
                        objectDeserializer9 = objectDeserializer5;
                        if (i4 >= 9) {
                            break;
                        }
                        String str4 = new String[]{"org.joda.time.DateTime", "org.joda.time.LocalDate", "org.joda.time.LocalDateTime", "org.joda.time.LocalTime", "org.joda.time.Instant", "org.joda.time.Period", "org.joda.time.Duration", "org.joda.time.DateTimeZone", "org.joda.time.format.DateTimeFormatter"}[i4];
                        if (str4.equals(strReplace)) {
                            Type cls5 = Class.forName(str4);
                            ObjectDeserializer objectDeserializer11 = JodaCodec.instance;
                            objectDeserializer10 = objectDeserializer11;
                            putDeserializer(cls5, objectDeserializer11);
                            return objectDeserializer11;
                        }
                        i4++;
                    }
                }
            } catch (Throwable th3) {
                jodaError = true;
                objectDeserializer9 = objectDeserializer10;
            }
        }
        ObjectDeserializer objectDeserializer12 = objectDeserializer9;
        if (!guavaError) {
            objectDeserializer12 = objectDeserializer9;
            if (strReplace.startsWith("com.google.common.collect.")) {
                int i5 = 0;
                while (true) {
                    objectDeserializer12 = objectDeserializer9;
                    if (i5 >= 5) {
                        break;
                    }
                    objectDeserializer12 = objectDeserializer9;
                    try {
                        String str5 = new String[]{"com.google.common.collect.HashMultimap", "com.google.common.collect.LinkedListMultimap", "com.google.common.collect.LinkedHashMultimap", "com.google.common.collect.ArrayListMultimap", "com.google.common.collect.TreeMultimap"}[i5];
                        if (str5.equals(strReplace)) {
                            Type cls6 = Class.forName(str5);
                            ObjectDeserializer objectDeserializer13 = GuavaCodec.instance;
                            objectDeserializer12 = objectDeserializer13;
                            putDeserializer(cls6, objectDeserializer13);
                            return objectDeserializer13;
                        }
                        i5++;
                    } catch (ClassNotFoundException e) {
                        guavaError = true;
                    }
                }
            }
        }
        ObjectDeserializer objectDeserializer14 = objectDeserializer12;
        if (strReplace.equals("java.nio.ByteBuffer")) {
            objectDeserializer14 = ByteBufferCodec.instance;
            putDeserializer(cls, objectDeserializer14);
        }
        if (strReplace.equals("java.nio.file.Path")) {
            objectDeserializer14 = MiscCodec.instance;
            putDeserializer(cls, objectDeserializer14);
        }
        if (cls == Map.Entry.class) {
            objectDeserializer14 = MiscCodec.instance;
            putDeserializer(cls, objectDeserializer14);
        }
        if (strReplace.equals("org.javamoney.moneta.Money")) {
            objectDeserializer14 = MonetaCodec.instance;
            putDeserializer(cls, objectDeserializer14);
        }
        try {
            for (AutowiredObjectDeserializer autowiredObjectDeserializer : ServiceLoader.load(AutowiredObjectDeserializer.class, Thread.currentThread().getContextClassLoader())) {
                Iterator<Type> it2 = autowiredObjectDeserializer.getAutowiredFor().iterator();
                while (it2.hasNext()) {
                    putDeserializer(it2.next(), autowiredObjectDeserializer);
                }
            }
        } catch (Exception e2) {
        }
        ObjectDeserializer objectDeserializer15 = objectDeserializer14;
        if (objectDeserializer14 == null) {
            objectDeserializer15 = get(type2);
        }
        if (objectDeserializer15 != null) {
            return objectDeserializer15;
        }
        if (cls.isEnum()) {
            if (this.jacksonCompatible) {
                for (Method method : cls.getMethods()) {
                    if (TypeUtils.isJacksonCreator(method)) {
                        ObjectDeserializer objectDeserializerCreateJavaBeanDeserializer = createJavaBeanDeserializer(cls, type2);
                        putDeserializer(type2, objectDeserializerCreateJavaBeanDeserializer);
                        return objectDeserializerCreateJavaBeanDeserializer;
                    }
                }
            }
            JSONType jSONType2 = (JSONType) TypeUtils.getAnnotation(cls, JSONType.class);
            if (jSONType2 != null) {
                try {
                    ObjectDeserializer objectDeserializer16 = (ObjectDeserializer) jSONType2.deserializer().newInstance();
                    putDeserializer(cls, objectDeserializer16);
                    return objectDeserializer16;
                } catch (Throwable th4) {
                }
            }
            throwableDeserializer = new EnumDeserializer(cls);
        } else {
            throwableDeserializer = cls.isArray() ? ObjectArrayCodec.instance : (cls == Set.class || cls == HashSet.class || cls == Collection.class || cls == List.class || cls == ArrayList.class || Collection.class.isAssignableFrom(cls)) ? CollectionCodec.instance : Map.class.isAssignableFrom(cls) ? MapDeserializer.instance : Throwable.class.isAssignableFrom(cls) ? new ThrowableDeserializer(this, cls) : PropertyProcessable.class.isAssignableFrom(cls) ? new PropertyProcessableDeserializer(cls) : cls == InetAddress.class ? MiscCodec.instance : createJavaBeanDeserializer(cls, type2);
        }
        putDeserializer(type2, throwableDeserializer);
        return throwableDeserializer;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer objectDeserializer = get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }
        if (type instanceof Class) {
            return getDeserializer((Class) type, type);
        }
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            return rawType instanceof Class ? getDeserializer((Class) rawType, type) : getDeserializer(rawType);
        }
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getDeserializer(upperBounds[0]);
            }
        }
        return JavaObjectDeserializer.instance;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDeserializers() {
        return this.deserializers;
    }

    public void initJavaBeanDeserializers(Class<?>... clsArr) {
        if (clsArr == null) {
            return;
        }
        for (Class<?> cls : clsArr) {
            if (cls != null) {
                putDeserializer(cls, createJavaBeanDeserializer(cls, cls));
            }
        }
    }

    public boolean isAsmEnable() {
        return this.asmEnable;
    }

    public boolean isAutoTypeSupport() {
        return this.autoTypeSupport;
    }

    public boolean isJacksonCompatible() {
        return this.jacksonCompatible;
    }

    public boolean isPrimitive(Class<?> cls) {
        return isPrimitive2(cls);
    }

    public void putDeserializer(Type type, ObjectDeserializer objectDeserializer) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            this.deserializers.put(type, objectDeserializer);
            return;
        }
        IdentityHashMap<Type, ObjectDeserializer> identityHashMap = this.mixInDeserializers.get(type);
        IdentityHashMap<Type, ObjectDeserializer> identityHashMap2 = identityHashMap;
        if (identityHashMap == null) {
            identityHashMap2 = new IdentityHashMap<>(4);
            this.mixInDeserializers.put(type, identityHashMap2);
        }
        identityHashMap2.put(mixInAnnotations, objectDeserializer);
    }

    public void register(Module module) {
        this.modules.add(module);
    }

    public void register(String str, Class cls) {
        this.typeMapping.putIfAbsent(str, cls);
    }

    public void setAsmEnable(boolean z) {
        this.asmEnable = z;
    }

    public void setAutoTypeSupport(boolean z) {
        this.autoTypeSupport = z;
    }

    public void setDefaultClassLoader(ClassLoader classLoader) {
        this.defaultClassLoader = classLoader;
    }

    public void setJacksonCompatible(boolean z) {
        this.jacksonCompatible = z;
    }

    private static long[] $d2j$hex$32979107$decode_J(String src) {
        byte[] d = $d2j$hex$32979107$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer s = b.asLongBuffer();
        long[] data = new long[d.length / 8];
        s.get(data);
        return data;
    }

    private static int[] $d2j$hex$32979107$decode_I(String src) {
        byte[] d = $d2j$hex$32979107$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer s = b.asIntBuffer();
        int[] data = new int[d.length / 4];
        s.get(data);
        return data;
    }

    private static short[] $d2j$hex$32979107$decode_S(String src) {
        byte[] d = $d2j$hex$32979107$decode_B(src);
        ByteBuffer b = ByteBuffer.wrap(d);
        b.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer s = b.asShortBuffer();
        short[] data = new short[d.length / 2];
        s.get(data);
        return data;
    }

    private static byte[] $d2j$hex$32979107$decode_B(String src) {
        int hh;
        int i;
        char[] d = src.toCharArray();
        byte[] ret = new byte[src.length() / 2];
        for (int i2 = 0; i2 < ret.length; i2++) {
            char h = d[2 * i2];
            char l = d[(2 * i2) + 1];
            if (h >= '0' && h <= '9') {
                hh = h - '0';
            } else if (h >= 'a' && h <= 'f') {
                hh = (h - 'a') + 10;
            } else if (h >= 'A' && h <= 'F') {
                hh = (h - 'A') + 10;
            } else {
                throw new RuntimeException();
            }
            if (l >= '0' && l <= '9') {
                i = l - '0';
            } else if (l >= 'a' && l <= 'f') {
                i = (l - 'a') + 10;
            } else if (l >= 'A' && l <= 'F') {
                i = (l - 'A') + 10;
            } else {
                throw new RuntimeException();
            }
            int ll = i;
            ret[i2] = (byte) ((hh << 4) | ll);
        }
        return ret;
    }
}
