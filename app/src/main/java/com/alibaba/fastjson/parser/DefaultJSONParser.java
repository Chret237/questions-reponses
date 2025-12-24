package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/DefaultJSONParser.class */
public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    private String[] autoTypeAccept;
    private boolean autoTypeEnable;
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    protected transient BeanContext lastBeanContext;
    public final JSONLexer lexer;
    private int objectKeyLevel;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask.class */
    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }
    }

    static {
        Class<?> cls = Boolean.TYPE;
        Class<?> cls2 = Byte.TYPE;
        Class<?> cls3 = Short.TYPE;
        Class<?> cls4 = Integer.TYPE;
        Class<?> cls5 = Long.TYPE;
        Class<?> cls6 = Float.TYPE;
        Class<?> cls7 = Double.TYPE;
        for (int i = 0; i < 17; i++) {
            primitiveClasses.add(new Class[]{cls, cls2, cls3, cls4, cls5, cls6, cls7, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, String.class}[i]);
        }
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this((Object) null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.objectKeyLevel = 0;
        this.autoTypeAccept = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        char current = jSONLexer.getCurrent();
        if (current == '{') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 12;
        } else if (current != '[') {
            jSONLexer.nextToken();
        } else {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 14;
        }
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this(str, new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this(str, new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this(cArr, new JSONScanner(cArr, i, i2), parserConfig);
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        ParseContext[] parseContextArr = this.contextArray;
        if (parseContextArr == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= parseContextArr.length) {
            ParseContext[] parseContextArr2 = new ParseContext[(parseContextArr.length * 3) / 2];
            System.arraycopy(parseContextArr, 0, parseContextArr2, 0, parseContextArr.length);
            this.contextArray = parseContextArr2;
        }
        this.contextArray[i] = parseContext;
    }

    public final void accept(int i) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken(i2);
        } else {
            throwException(i);
        }
    }

    public void acceptType(String str) {
        JSONLexer jSONLexer = this.lexer;
        jSONLexer.nextTokenWithColon();
        if (jSONLexer.token() != 4) {
            throw new JSONException("type not match error");
        }
        if (!str.equals(jSONLexer.stringVal())) {
            throw new JSONException("type not match error");
        }
        jSONLexer.nextToken();
        if (jSONLexer.token() == 16) {
            jSONLexer.nextToken();
        }
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus == 1) {
            if (!(collection instanceof List)) {
                ResolveTask lastResolveTask = getLastResolveTask();
                lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(collection);
                lastResolveTask.ownerContext = this.context;
                setResolveStatus(0);
                return;
            }
            int size = collection.size();
            ResolveTask lastResolveTask2 = getLastResolveTask();
            lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, size - 1);
            lastResolveTask2.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public void checkMapResolve(Map map, Object obj) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        JSONLexer jSONLexer = this.lexer;
        try {
            if (jSONLexer.isEnabled(Feature.AutoCloseSource) && jSONLexer.token() != 20) {
                throw new JSONException("not close json text, token : " + JSONToken.name(jSONLexer.token()));
            }
        } finally {
            jSONLexer.close();
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public ParseContext getContext() {
        return this.context;
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat = simpleDateFormat;
            simpleDateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public String getInput() {
        Object obj = this.input;
        return obj instanceof char[] ? new String((char[]) this.input) : obj.toString();
    }

    public ResolveTask getLastResolveTask() {
        List<ResolveTask> list = this.resolveTaskList;
        return list.get(list.size() - 1);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public void handleResovleTask(Object obj) {
        Object objEval;
        List<ResolveTask> list = this.resolveTaskList;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ResolveTask resolveTask = this.resolveTaskList.get(i);
            String str = resolveTask.referenceValue;
            Object obj2 = resolveTask.ownerContext != null ? resolveTask.ownerContext.object : null;
            if (str.startsWith("$")) {
                Object object = getObject(str);
                objEval = object;
                if (object == null) {
                    try {
                        objEval = JSONPath.eval(obj, str);
                    } catch (JSONPathException e) {
                        objEval = object;
                    }
                }
            } else {
                objEval = resolveTask.context.object;
            }
            FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
            if (fieldDeserializer != null) {
                Object objEval2 = objEval;
                if (objEval != null) {
                    objEval2 = objEval;
                    if (objEval.getClass() == JSONObject.class) {
                        objEval2 = objEval;
                        if (fieldDeserializer.fieldInfo != null) {
                            objEval2 = objEval;
                            if (!Map.class.isAssignableFrom(fieldDeserializer.fieldInfo.fieldClass)) {
                                objEval2 = JSONPath.eval(this.contextArray[0].object, str);
                            }
                        }
                    }
                }
                fieldDeserializer.setValue(obj2, objEval2);
            }
        }
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public Object parse() {
        return parse(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:98:0x0407, code lost:
    
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable r6, java.lang.Object r7) {
        /*
            Method dump skipped, instructions count: 1117
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable, java.lang.Object):java.lang.Object");
    }

    public Object parse(Object obj) {
        JSONLexer jSONLexer = this.lexer;
        int i = jSONLexer.token();
        if (i == 2) {
            Number numberIntegerValue = jSONLexer.integerValue();
            jSONLexer.nextToken();
            return numberIntegerValue;
        }
        if (i == 3) {
            Number numberDecimalValue = jSONLexer.decimalValue(jSONLexer.isEnabled(Feature.UseBigDecimal));
            jSONLexer.nextToken();
            return numberDecimalValue;
        }
        if (i == 4) {
            String strStringVal = jSONLexer.stringVal();
            jSONLexer.nextToken(16);
            if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner jSONScanner = new JSONScanner(strStringVal);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        return jSONScanner.getCalendar().getTime();
                    }
                } finally {
                    jSONScanner.close();
                }
            }
            return strStringVal;
        }
        if (i == 12) {
            return parseObject(new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), obj);
        }
        if (i == 14) {
            JSONArray jSONArray = new JSONArray();
            parseArray(jSONArray, obj);
            return jSONLexer.isEnabled(Feature.UseObjectArray) ? jSONArray.toArray() : jSONArray;
        }
        if (i == 18) {
            if ("NaN".equals(jSONLexer.stringVal())) {
                jSONLexer.nextToken();
                return null;
            }
            throw new JSONException("syntax error, " + jSONLexer.info());
        }
        if (i == 26) {
            byte[] bArrBytesValue = jSONLexer.bytesValue();
            jSONLexer.nextToken();
            return bArrBytesValue;
        }
        switch (i) {
            case 6:
                jSONLexer.nextToken();
                return Boolean.TRUE;
            case 7:
                jSONLexer.nextToken();
                return Boolean.FALSE;
            case 8:
                jSONLexer.nextToken();
                return null;
            case 9:
                jSONLexer.nextToken(18);
                if (jSONLexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                jSONLexer.nextToken(10);
                accept(10);
                long jLongValue = jSONLexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(jLongValue);
            default:
                switch (i) {
                    case 20:
                        if (jSONLexer.isBlankInput()) {
                            return null;
                        }
                        throw new JSONException("unterminated json string, " + jSONLexer.info());
                    case 21:
                        jSONLexer.nextToken();
                        HashSet hashSet = new HashSet();
                        parseArray(hashSet, obj);
                        return hashSet;
                    case 22:
                        jSONLexer.nextToken();
                        TreeSet treeSet = new TreeSet();
                        parseArray(treeSet, obj);
                        return treeSet;
                    case 23:
                        jSONLexer.nextToken();
                        return null;
                    default:
                        throw new JSONException("syntax error, " + jSONLexer.info());
                }
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parseArray(java.lang.reflect.Type r7, java.util.Collection r8, java.lang.Object r9) {
        /*
            Method dump skipped, instructions count: 503
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.lang.reflect.Type, java.util.Collection, java.lang.Object):void");
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    public final void parseArray(Collection collection, Object obj) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == 21 || jSONLexer.token() == 22) {
            jSONLexer.nextToken();
        }
        if (jSONLexer.token() != 14) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(jSONLexer.token()) + ", pos " + jSONLexer.pos() + ", fieldName " + obj);
        }
        jSONLexer.nextToken(4);
        ParseContext parseContext = this.context;
        if (parseContext != null && parseContext.level > 512) {
            throw new JSONException("array level > 512");
        }
        ParseContext parseContext2 = this.context;
        setContext(collection, obj);
        int i = 0;
        while (true) {
            try {
                if (jSONLexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (jSONLexer.token() == 16) {
                        jSONLexer.nextToken();
                    }
                }
                int i2 = jSONLexer.token();
                Object objIntegerValue = null;
                if (i2 == 2) {
                    objIntegerValue = jSONLexer.integerValue();
                    jSONLexer.nextToken(16);
                } else if (i2 == 3) {
                    objIntegerValue = jSONLexer.isEnabled(Feature.UseBigDecimal) ? jSONLexer.decimalValue(true) : jSONLexer.decimalValue(false);
                    jSONLexer.nextToken(16);
                } else if (i2 == 4) {
                    String strStringVal = jSONLexer.stringVal();
                    jSONLexer.nextToken(16);
                    objIntegerValue = strStringVal;
                    if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                        JSONScanner jSONScanner = new JSONScanner(strStringVal);
                        objIntegerValue = strStringVal;
                        if (jSONScanner.scanISO8601DateIfMatch()) {
                            objIntegerValue = jSONScanner.getCalendar().getTime();
                        }
                        jSONScanner.close();
                    }
                } else if (i2 == 6) {
                    objIntegerValue = Boolean.TRUE;
                    jSONLexer.nextToken(16);
                } else if (i2 == 7) {
                    objIntegerValue = Boolean.FALSE;
                    jSONLexer.nextToken(16);
                } else if (i2 == 8) {
                    jSONLexer.nextToken(4);
                } else if (i2 == 12) {
                    objIntegerValue = parseObject(new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), Integer.valueOf(i));
                } else {
                    if (i2 == 20) {
                        throw new JSONException("unclosed jsonArray");
                    }
                    if (i2 == 23) {
                        jSONLexer.nextToken(4);
                    } else if (i2 == 14) {
                        JSONArray jSONArray = new JSONArray();
                        parseArray(jSONArray, Integer.valueOf(i));
                        objIntegerValue = jSONArray;
                        if (jSONLexer.isEnabled(Feature.UseObjectArray)) {
                            objIntegerValue = jSONArray.toArray();
                        }
                    } else {
                        if (i2 == 15) {
                            jSONLexer.nextToken(16);
                            return;
                        }
                        objIntegerValue = parse();
                    }
                }
                collection.add(objIntegerValue);
                checkListResolve(collection);
                if (jSONLexer.token() == 16) {
                    jSONLexer.nextToken(4);
                }
                i++;
            } finally {
                setContext(parseContext2);
            }
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object objCast;
        Class<?> componentType;
        boolean zIsArray;
        Class cls;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        }
        Object[] objArr = new Object[typeArr.length];
        if (typeArr.length == 0) {
            this.lexer.nextToken(15);
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return new Object[0];
        }
        this.lexer.nextToken(2);
        for (int i = 0; i < typeArr.length; i++) {
            if (this.lexer.token() == 8) {
                this.lexer.nextToken(16);
                objCast = null;
            } else {
                Type type = typeArr[i];
                if (type == Integer.TYPE || type == Integer.class) {
                    if (this.lexer.token() == 2) {
                        objCast = Integer.valueOf(this.lexer.intValue());
                        this.lexer.nextToken(16);
                    } else {
                        objCast = TypeUtils.cast(parse(), type, this.config);
                    }
                } else if (type != String.class) {
                    if (i == typeArr.length - 1 && (type instanceof Class) && !(((cls = (Class) type) == byte[].class || cls == char[].class) && this.lexer.token() == 4)) {
                        zIsArray = cls.isArray();
                        componentType = cls.getComponentType();
                    } else {
                        componentType = null;
                        zIsArray = false;
                    }
                    if (!zIsArray || this.lexer.token() == 14) {
                        objCast = this.config.getDeserializer(type).deserialze(this, type, Integer.valueOf(i));
                    } else {
                        ArrayList arrayList = new ArrayList();
                        ObjectDeserializer deserializer = this.config.getDeserializer(componentType);
                        int fastMatchToken = deserializer.getFastMatchToken();
                        if (this.lexer.token() != 15) {
                            while (true) {
                                arrayList.add(deserializer.deserialze(this, type, null));
                                if (this.lexer.token() != 16) {
                                    break;
                                }
                                this.lexer.nextToken(fastMatchToken);
                            }
                            if (this.lexer.token() != 15) {
                                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                            }
                        }
                        objCast = TypeUtils.cast(arrayList, type, this.config);
                    }
                } else if (this.lexer.token() == 4) {
                    objCast = this.lexer.stringVal();
                    this.lexer.nextToken(16);
                } else {
                    objCast = TypeUtils.cast(parse(), type, this.config);
                }
            }
            objArr[i] = objCast;
            if (this.lexer.token() == 15) {
                break;
            }
            if (this.lexer.token() != 16) {
                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
            }
            if (i == typeArr.length - 1) {
                this.lexer.nextToken(15);
            } else {
                this.lexer.nextToken(2);
            }
        }
        if (this.lexer.token() != 15) {
            throw new JSONException("syntax error");
        }
        this.lexer.nextToken(16);
        return objArr;
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new JSONException("not support type " + type);
        }
        Type type2 = actualTypeArguments[0];
        if (type2 instanceof Class) {
            ArrayList arrayList = new ArrayList();
            parseArray((Class<?>) type2, (Collection) arrayList);
            return arrayList;
        }
        if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            Type type3 = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(type3)) {
                ArrayList arrayList2 = new ArrayList();
                parseArray((Class<?>) type3, (Collection) arrayList2);
                return arrayList2;
            }
            if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            }
            throw new JSONException("not support type : " + type);
        }
        if (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type2;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length != 1) {
                throw new JSONException("not support : " + typeVariable);
            }
            Type type4 = bounds[0];
            if (type4 instanceof Class) {
                ArrayList arrayList3 = new ArrayList();
                parseArray((Class<?>) type4, (Collection) arrayList3);
                return arrayList3;
            }
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type2;
            ArrayList arrayList4 = new ArrayList();
            parseArray(parameterizedType, arrayList4);
            return arrayList4;
        }
        throw new JSONException("TODO : " + type);
    }

    public void parseExtra(Object obj, String str) {
        this.lexer.nextTokenWithColon();
        List<ExtraTypeProvider> list = this.extraTypeProviders;
        Type type = null;
        if (list != null) {
            Iterator<ExtraTypeProvider> it = list.iterator();
            Type extraType = null;
            while (true) {
                type = extraType;
                if (!it.hasNext()) {
                    break;
                } else {
                    extraType = it.next().getExtraType(obj, str);
                }
            }
        }
        Object object = type == null ? parse() : parseObject(type);
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, object);
            return;
        }
        List<ExtraProcessor> list2 = this.extraProcessors;
        if (list2 != null) {
            Iterator<ExtraProcessor> it2 = list2.iterator();
            while (it2.hasNext()) {
                it2.next().processExtra(obj, str, object);
            }
        }
        if (this.resolveStatus == 1) {
            this.resolveStatus = 0;
        }
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse(null);
        }
        String strStringVal = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return strStringVal;
    }

    public JSONObject parseObject() {
        Object object = parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        if (object == null) {
            return null;
        }
        return new JSONObject((Map<String, Object>) object);
    }

    public <T> T parseObject(Class<T> cls) {
        return (T) parseObject(cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return (T) parseObject(type, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T parseObject(Type type, Object obj) {
        int i = this.lexer.token();
        if (i == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (i == 4) {
            if (type == byte[].class) {
                T t = (T) this.lexer.bytesValue();
                this.lexer.nextToken();
                return t;
            }
            if (type == char[].class) {
                String strStringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return (T) strStringVal.toCharArray();
            }
        }
        ObjectDeserializer deserializer = this.config.getDeserializer(type);
        try {
            if (deserializer.getClass() != JavaBeanDeserializer.class) {
                return (T) deserializer.deserialze(this, type, obj);
            }
            if (this.lexer.token() != 12 && this.lexer.token() != 14) {
                throw new JSONException("syntax error,except start with { or [,but actually start with " + this.lexer.tokenName());
            }
            return (T) ((JavaBeanDeserializer) deserializer).deserialze(this, type, obj, 0);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            throw new JSONException(th.getMessage(), th);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:330:0x0650, code lost:
    
        r0.nextToken(16);
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0666, code lost:
    
        if (r0.token() != 13) goto L379;
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0669, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x066d, code lost:
    
        r0.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0678, code lost:
    
        r16 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0686, code lost:
    
        if ((r5.config.getDeserializer(r15) instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0689, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x068d, code lost:
    
        r7 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r6, (java.lang.Class<java.lang.Object>) r15, r5.config);
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x069b, code lost:
    
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x069d, code lost:
    
        r6 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x06a0, code lost:
    
        if (r7 != null) goto L368;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x06a8, code lost:
    
        if (r15 != java.lang.Cloneable.class) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x06af, code lost:
    
        r6 = new java.util.HashMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x06c6, code lost:
    
        if ("java.util.Collections$EmptyMap".equals(r0) == false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x06c9, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x06cd, code lost:
    
        r6 = java.util.Collections.emptyMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x06e0, code lost:
    
        if ("java.util.Collections$UnmodifiableMap".equals(r0) == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x06e3, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x06e7, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x06ef, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x06f7, code lost:
    
        r6 = java.util.Collections.unmodifiableMap(new java.util.HashMap());
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x0703, code lost:
    
        r6 = r15.newInstance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x070a, code lost:
    
        setContext(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0710, code lost:
    
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x0711, code lost:
    
        r6 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x0716, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x071e, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x072b, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error", r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x072c, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0730, code lost:
    
        setResolveStatus(2);
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x073d, code lost:
    
        if (r5.context == null) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0741, code lost:
    
        if (r7 == null) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x074c, code lost:
    
        if ((r7 instanceof java.lang.Integer) != false) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x074f, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x075d, code lost:
    
        if ((r5.context.fieldName instanceof java.lang.Integer) != false) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x0760, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0764, code lost:
    
        popContext();
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x0768, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0772, code lost:
    
        if (r6.size() <= 0) goto L410;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x0775, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0779, code lost:
    
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r6, (java.lang.Class<java.lang.Object>) r15, r5.config);
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x0788, code lost:
    
        setResolveStatus(0);
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0791, code lost:
    
        parseObject(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0797, code lost:
    
        setContext(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x079d, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x07a2, code lost:
    
        r0 = r5.config.getDeserializer(r15);
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x07b0, code lost:
    
        r0 = r0.getClass();
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x07c2, code lost:
    
        if (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class.isAssignableFrom(r0) == false) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x07ca, code lost:
    
        if (r0 == com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x07d2, code lost:
    
        if (r0 == com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.class) goto L424;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x07d9, code lost:
    
        setResolveStatus(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x07e9, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.MapDeserializer) == false) goto L430;
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x07ec, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x07f0, code lost:
    
        setResolveStatus(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x07f5, code lost:
    
        r0 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x07f9, code lost:
    
        r0 = r0.deserialze(r5, r15, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x0805, code lost:
    
        setContext(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x080b, code lost:
    
        return r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x053e  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x080c  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x09c3  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x0ac0  */
    /* JADX WARN: Removed duplicated region for block: B:790:0x0e92  */
    /* JADX WARN: Removed duplicated region for block: B:793:0x0ea1  */
    /* JADX WARN: Removed duplicated region for block: B:803:0x0eca  */
    /* JADX WARN: Removed duplicated region for block: B:809:0x0eed  */
    /* JADX WARN: Removed duplicated region for block: B:822:0x0f27  */
    /* JADX WARN: Removed duplicated region for block: B:932:0x0f0a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.Object parseObject(java.util.Map r6, java.lang.Object r7) {
        /*
            Method dump skipped, instructions count: 4268
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public void parseObject(Object obj) {
        Object objDeserialze;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.config.getDeserializer(cls);
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (this.lexer.token() != 12 && this.lexer.token() != 16) {
            throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
        }
        while (true) {
            String strScanSymbol = this.lexer.scanSymbol(this.symbolTable);
            if (strScanSymbol == null) {
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                } else if (this.lexer.token() != 16 || !this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                }
            }
            FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(strScanSymbol) : null;
            if (fieldDeserializer != null) {
                Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                Type type = fieldDeserializer.fieldInfo.fieldType;
                if (cls2 == Integer.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    objDeserialze = IntegerCodec.instance.deserialze(this, type, null);
                } else if (cls2 == String.class) {
                    this.lexer.nextTokenWithColon(4);
                    objDeserialze = StringCodec.deserialze(this);
                } else if (cls2 == Long.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    objDeserialze = LongCodec.instance.deserialze(this, type, null);
                } else {
                    ObjectDeserializer deserializer2 = this.config.getDeserializer(cls2, type);
                    this.lexer.nextTokenWithColon(deserializer2.getFastMatchToken());
                    objDeserialze = deserializer2.deserialze(this, type, null);
                }
                fieldDeserializer.setValue(obj, objDeserialze);
                if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                }
            } else {
                if (!this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + strScanSymbol);
                }
                this.lexer.nextTokenWithColon();
                parse();
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken();
                    return;
                }
            }
        }
    }

    public void popContext() {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = this.context.parent;
        int i = this.contextArrayIndex;
        if (i <= 0) {
            return;
        }
        int i2 = i - 1;
        this.contextArrayIndex = i2;
        this.contextArray[i2] = null;
    }

    public Object resolveReference(String str) {
        if (this.contextArray == null) {
            return null;
        }
        int i = 0;
        while (true) {
            ParseContext[] parseContextArr = this.contextArray;
            if (i >= parseContextArr.length || i >= this.contextArrayIndex) {
                return null;
            }
            ParseContext parseContext = parseContextArr[i];
            if (parseContext.toString().equals(str)) {
                return parseContext.object;
            }
            i++;
        }
    }

    public void setConfig(ParserConfig parserConfig) {
        this.config = parserConfig;
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        ParseContext parseContext2 = new ParseContext(parseContext, obj, obj2);
        this.context = parseContext2;
        addContext(parseContext2);
        return this.context;
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public void setContext(ParseContext parseContext) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = parseContext;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public void throwException(int i) {
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(this.lexer.token()));
    }
}
