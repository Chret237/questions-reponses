package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath.class */
public class JSONPath implements JSONAware {
    static final long LENGTH = -1580386065683472715L;
    static final long SIZE = 5614464919154503228L;
    private static ConcurrentMap<String, JSONPath> pathCache = new ConcurrentHashMap(128, 0.75f, 1);
    private boolean hasRefSegment;
    private ParserConfig parserConfig;
    private final String path;
    private Segment[] segments;
    private SerializeConfig serializeConfig;

    /* renamed from: com.alibaba.fastjson.JSONPath$1 */
    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$1.class */
    static /* synthetic */ class C03951 {
        static final int[] $SwitchMap$com$alibaba$fastjson$JSONPath$Operator;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:23:0x005d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                com.alibaba.fastjson.JSONPath$Operator[] r0 = com.alibaba.fastjson.JSONPath.Operator.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator = r0
                r0 = r4
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.EQ     // Catch: java.lang.NoSuchFieldError -> L4d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L4d
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L4d
            L14:
                int[] r0 = com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator     // Catch: java.lang.NoSuchFieldError -> L4d java.lang.NoSuchFieldError -> L51
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.NE     // Catch: java.lang.NoSuchFieldError -> L51
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L51
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L51
            L1f:
                int[] r0 = com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator     // Catch: java.lang.NoSuchFieldError -> L51 java.lang.NoSuchFieldError -> L55
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.GE     // Catch: java.lang.NoSuchFieldError -> L55
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L55
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L55
            L2a:
                int[] r0 = com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator     // Catch: java.lang.NoSuchFieldError -> L55 java.lang.NoSuchFieldError -> L59
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.GT     // Catch: java.lang.NoSuchFieldError -> L59
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L59
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L59
            L35:
                int[] r0 = com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator     // Catch: java.lang.NoSuchFieldError -> L59 java.lang.NoSuchFieldError -> L5d
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.LE     // Catch: java.lang.NoSuchFieldError -> L5d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L5d
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L5d
            L40:
                int[] r0 = com.alibaba.fastjson.JSONPath.C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator     // Catch: java.lang.NoSuchFieldError -> L5d java.lang.NoSuchFieldError -> L61
                com.alibaba.fastjson.JSONPath$Operator r1 = com.alibaba.fastjson.JSONPath.Operator.LT     // Catch: java.lang.NoSuchFieldError -> L61
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L61
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L61
            L4c:
                return
            L4d:
                r4 = move-exception
                goto L14
            L51:
                r4 = move-exception
                goto L1f
            L55:
                r4 = move-exception
                goto L2a
            L59:
                r4 = move-exception
                goto L35
            L5d:
                r4 = move-exception
                goto L40
            L61:
                r4 = move-exception
                goto L4c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.C03951.m281clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$ArrayAccessSegment.class */
    static class ArrayAccessSegment implements Segment {
        private final int index;

        public ArrayAccessSegment(int i) {
            this.index = i;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getArrayItem(obj2, this.index);
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (((JSONLexerBase) defaultJSONParser.lexer).seekArrayToItem(this.index) && context.eval) {
                context.object = defaultJSONParser.parse();
            }
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removeArrayItem(jSONPath, obj, this.index);
        }

        public boolean setValue(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.setArrayItem(jSONPath, obj, this.index, obj2);
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$Context.class */
    private static class Context {
        final boolean eval;
        Object object;
        final Context parent;

        public Context(Context context, boolean z) {
            this.parent = context;
            this.eval = z;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$DoubleOpSegement.class */
    static class DoubleOpSegement implements Filter {

        /* renamed from: op */
        private final Operator f48op;
        private final String propertyName;
        private final long propertyNameHash;
        private final double value;

        public DoubleOpSegement(String str, double d, Operator operator) {
            this.propertyName = str;
            this.value = d;
            this.f48op = operator;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            boolean z = false;
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            double dDoubleValue = ((Number) propertyValue).doubleValue();
            switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f48op.ordinal()]) {
                case 1:
                    boolean z2 = false;
                    if (dDoubleValue == this.value) {
                        z2 = true;
                    }
                    return z2;
                case 2:
                    boolean z3 = false;
                    if (dDoubleValue != this.value) {
                        z3 = true;
                    }
                    return z3;
                case 3:
                    boolean z4 = false;
                    if (dDoubleValue >= this.value) {
                        z4 = true;
                    }
                    return z4;
                case 4:
                    if (dDoubleValue > this.value) {
                        z = true;
                    }
                    return z;
                case 5:
                    boolean z5 = false;
                    if (dDoubleValue <= this.value) {
                        z5 = true;
                    }
                    return z5;
                case 6:
                    boolean z6 = false;
                    if (dDoubleValue < this.value) {
                        z6 = true;
                    }
                    return z6;
                default:
                    return false;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$Filter.class */
    interface Filter {
        boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3);
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$FilterGroup.class */
    static class FilterGroup implements Filter {
        private boolean and;
        private List<Filter> fitlers;

        public FilterGroup(Filter filter, Filter filter2, boolean z) {
            ArrayList arrayList = new ArrayList(2);
            this.fitlers = arrayList;
            arrayList.add(filter);
            this.fitlers.add(filter2);
            this.and = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            if (this.and) {
                Iterator<Filter> it = this.fitlers.iterator();
                while (it.hasNext()) {
                    if (!it.next().apply(jSONPath, obj, obj2, obj3)) {
                        return false;
                    }
                }
                return true;
            }
            Iterator<Filter> it2 = this.fitlers.iterator();
            while (it2.hasNext()) {
                if (it2.next().apply(jSONPath, obj, obj2, obj3)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$FilterSegment.class */
    public static class FilterSegment implements Segment {
        private final Filter filter;

        public FilterSegment(Filter filter) {
            this.filter = filter;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null) {
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            if (!(obj2 instanceof Iterable)) {
                if (this.filter.apply(jSONPath, obj, obj2, obj2)) {
                    return obj2;
                }
                return null;
            }
            for (Object obj3 : (Iterable) obj2) {
                if (this.filter.apply(jSONPath, obj, obj2, obj3)) {
                    jSONArray.add(obj3);
                }
            }
            return jSONArray;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null || !(obj2 instanceof Iterable)) {
                return false;
            }
            Iterator it = ((Iterable) obj2).iterator();
            while (it.hasNext()) {
                if (this.filter.apply(jSONPath, obj, obj2, it.next())) {
                    it.remove();
                }
            }
            return true;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$IntBetweenSegement.class */
    static class IntBetweenSegement implements Filter {
        private final long endValue;
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final long startValue;

        public IntBetweenSegement(String str, long j, long j2, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.startValue = j;
            this.endValue = j2;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) propertyValue);
                if (jLongExtractValue >= this.startValue && jLongExtractValue <= this.endValue) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$IntInSegement.class */
    static class IntInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final long[] values;

        public IntInSegement(String str, long[] jArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = jArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            if (propertyValue instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) propertyValue);
                for (long j : this.values) {
                    if (j == jLongExtractValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$IntObjInSegement.class */
    static class IntObjInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final Long[] values;

        public IntObjInSegement(String str, Long[] lArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = lArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                for (Long l : this.values) {
                    if (l == null) {
                        return !this.not;
                    }
                }
                return this.not;
            }
            if (propertyValue instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) propertyValue);
                for (Long l2 : this.values) {
                    if (l2 != null && l2.longValue() == jLongExtractValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$IntOpSegement.class */
    static class IntOpSegement implements Filter {

        /* renamed from: op */
        private final Operator f49op;
        private final String propertyName;
        private final long propertyNameHash;
        private final long value;
        private BigDecimal valueDecimal;
        private Double valueDouble;
        private Float valueFloat;

        public IntOpSegement(String str, long j, Operator operator) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = j;
            this.f49op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            boolean z = false;
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            if (propertyValue instanceof BigDecimal) {
                if (this.valueDecimal == null) {
                    this.valueDecimal = BigDecimal.valueOf(this.value);
                }
                int iCompareTo = this.valueDecimal.compareTo((BigDecimal) propertyValue);
                switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f49op.ordinal()]) {
                    case 1:
                        boolean z2 = false;
                        if (iCompareTo == 0) {
                            z2 = true;
                        }
                        return z2;
                    case 2:
                        boolean z3 = false;
                        if (iCompareTo != 0) {
                            z3 = true;
                        }
                        return z3;
                    case 3:
                        boolean z4 = false;
                        if (iCompareTo <= 0) {
                            z4 = true;
                        }
                        return z4;
                    case 4:
                        boolean z5 = false;
                        if (iCompareTo < 0) {
                            z5 = true;
                        }
                        return z5;
                    case 5:
                        boolean z6 = false;
                        if (iCompareTo >= 0) {
                            z6 = true;
                        }
                        return z6;
                    case 6:
                        boolean z7 = false;
                        if (iCompareTo > 0) {
                            z7 = true;
                        }
                        return z7;
                    default:
                        return false;
                }
            }
            if (propertyValue instanceof Float) {
                if (this.valueFloat == null) {
                    this.valueFloat = Float.valueOf(this.value);
                }
                int iCompareTo2 = this.valueFloat.compareTo((Float) propertyValue);
                switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f49op.ordinal()]) {
                    case 1:
                        boolean z8 = false;
                        if (iCompareTo2 == 0) {
                            z8 = true;
                        }
                        return z8;
                    case 2:
                        boolean z9 = false;
                        if (iCompareTo2 != 0) {
                            z9 = true;
                        }
                        return z9;
                    case 3:
                        boolean z10 = false;
                        if (iCompareTo2 <= 0) {
                            z10 = true;
                        }
                        return z10;
                    case 4:
                        boolean z11 = false;
                        if (iCompareTo2 < 0) {
                            z11 = true;
                        }
                        return z11;
                    case 5:
                        boolean z12 = false;
                        if (iCompareTo2 >= 0) {
                            z12 = true;
                        }
                        return z12;
                    case 6:
                        boolean z13 = false;
                        if (iCompareTo2 > 0) {
                            z13 = true;
                        }
                        return z13;
                    default:
                        return false;
                }
            }
            if (!(propertyValue instanceof Double)) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) propertyValue);
                switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f49op.ordinal()]) {
                    case 1:
                        boolean z14 = false;
                        if (jLongExtractValue == this.value) {
                            z14 = true;
                        }
                        return z14;
                    case 2:
                        boolean z15 = false;
                        if (jLongExtractValue != this.value) {
                            z15 = true;
                        }
                        return z15;
                    case 3:
                        boolean z16 = false;
                        if (jLongExtractValue >= this.value) {
                            z16 = true;
                        }
                        return z16;
                    case 4:
                        boolean z17 = false;
                        if (jLongExtractValue > this.value) {
                            z17 = true;
                        }
                        return z17;
                    case 5:
                        if (jLongExtractValue <= this.value) {
                            z = true;
                        }
                        return z;
                    case 6:
                        boolean z18 = false;
                        if (jLongExtractValue < this.value) {
                            z18 = true;
                        }
                        return z18;
                    default:
                        return false;
                }
            }
            if (this.valueDouble == null) {
                this.valueDouble = Double.valueOf(this.value);
            }
            int iCompareTo3 = this.valueDouble.compareTo((Double) propertyValue);
            switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f49op.ordinal()]) {
                case 1:
                    boolean z19 = false;
                    if (iCompareTo3 == 0) {
                        z19 = true;
                    }
                    return z19;
                case 2:
                    boolean z20 = false;
                    if (iCompareTo3 != 0) {
                        z20 = true;
                    }
                    return z20;
                case 3:
                    boolean z21 = false;
                    if (iCompareTo3 <= 0) {
                        z21 = true;
                    }
                    return z21;
                case 4:
                    boolean z22 = false;
                    if (iCompareTo3 < 0) {
                        z22 = true;
                    }
                    return z22;
                case 5:
                    boolean z23 = false;
                    if (iCompareTo3 >= 0) {
                        z23 = true;
                    }
                    return z23;
                case 6:
                    boolean z24 = false;
                    if (iCompareTo3 > 0) {
                        z24 = true;
                    }
                    return z24;
                default:
                    return false;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$JSONPathParser.class */
    static class JSONPathParser {

        /* renamed from: ch */
        private char f50ch;
        private boolean hasRefSegment;
        private int level;
        private final String path;
        private int pos;
        private static final String strArrayRegex = "'\\s*,\\s*'";
        private static final Pattern strArrayPatternx = Pattern.compile(strArrayRegex);

        public JSONPathParser(String str) {
            this.path = str;
            next();
        }

        static boolean isDigitFirst(char c) {
            return c == '-' || c == '+' || (c >= '0' && c <= '9');
        }

        void accept(char c) {
            if (this.f50ch == c) {
                if (isEOF()) {
                    return;
                }
                next();
            } else {
                throw new JSONPathException("expect '" + c + ", but '" + this.f50ch + "'");
            }
        }

        Segment buildArraySegement(String str) {
            int length = str.length();
            char cCharAt = str.charAt(0);
            int i = length - 1;
            char cCharAt2 = str.charAt(i);
            int iIndexOf = str.indexOf(44);
            if (str.length() > 2 && cCharAt == '\'' && cCharAt2 == '\'') {
                String strSubstring = str.substring(1, i);
                return (iIndexOf == -1 || !strArrayPatternx.matcher(str).find()) ? new PropertySegment(strSubstring, false) : new MultiPropertySegment(strSubstring.split(strArrayRegex));
            }
            int iIndexOf2 = str.indexOf(58);
            if (iIndexOf == -1 && iIndexOf2 == -1) {
                if (TypeUtils.isNumber(str)) {
                    try {
                        return new ArrayAccessSegment(Integer.parseInt(str));
                    } catch (NumberFormatException e) {
                        return new PropertySegment(str, false);
                    }
                }
                String strSubstring2 = str;
                if (str.charAt(0) == '\"') {
                    strSubstring2 = str;
                    if (str.charAt(str.length() - 1) == '\"') {
                        strSubstring2 = str.substring(1, str.length() - 1);
                    }
                }
                return new PropertySegment(strSubstring2, false);
            }
            if (iIndexOf != -1) {
                String[] strArrSplit = str.split(",");
                int[] iArr = new int[strArrSplit.length];
                for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                    iArr[i2] = Integer.parseInt(strArrSplit[i2]);
                }
                return new MultiIndexSegment(iArr);
            }
            if (iIndexOf2 == -1) {
                throw new UnsupportedOperationException();
            }
            String[] strArrSplit2 = str.split(":");
            int length2 = strArrSplit2.length;
            int[] iArr2 = new int[length2];
            for (int i3 = 0; i3 < strArrSplit2.length; i3++) {
                String str2 = strArrSplit2[i3];
                if (str2.length() != 0) {
                    iArr2[i3] = Integer.parseInt(str2);
                } else {
                    if (i3 != 0) {
                        throw new UnsupportedOperationException();
                    }
                    iArr2[i3] = 0;
                }
            }
            int i4 = iArr2[0];
            int i5 = length2 > 1 ? iArr2[1] : -1;
            int i6 = length2 == 3 ? iArr2[2] : 1;
            if (i5 < 0 || i5 >= i4) {
                if (i6 > 0) {
                    return new RangeSegment(i4, i5, i6);
                }
                throw new UnsupportedOperationException("step must greater than zero : " + i6);
            }
            throw new UnsupportedOperationException("end must greater than or equals start. start " + i4 + ",  end " + i5);
        }

        public Segment[] explain() {
            String str = this.path;
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException();
            }
            Segment[] segmentArr = new Segment[8];
            while (true) {
                Segment segement = readSegement();
                if (segement == null) {
                    break;
                }
                if (segement instanceof PropertySegment) {
                    PropertySegment propertySegment = (PropertySegment) segement;
                    if (propertySegment.deep || !propertySegment.propertyName.equals("*")) {
                    }
                }
                int i = this.level;
                Segment[] segmentArr2 = segmentArr;
                if (i == segmentArr.length) {
                    segmentArr2 = new Segment[(i * 3) / 2];
                    System.arraycopy(segmentArr, 0, segmentArr2, 0, i);
                }
                int i2 = this.level;
                this.level = i2 + 1;
                segmentArr2[i2] = segement;
                segmentArr = segmentArr2;
            }
            int i3 = this.level;
            if (i3 == segmentArr.length) {
                return segmentArr;
            }
            Segment[] segmentArr3 = new Segment[i3];
            System.arraycopy(segmentArr, 0, segmentArr3, 0, i3);
            return segmentArr3;
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0040  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        com.alibaba.fastjson.JSONPath.Filter filterRest(com.alibaba.fastjson.JSONPath.Filter r7) {
            /*
                r6 = this;
                r0 = r6
                char r0 = r0.f50ch
                r9 = r0
                r0 = 1
                r8 = r0
                r0 = r9
                r1 = 38
                if (r0 != r1) goto L13
                r0 = 1
                r10 = r0
                goto L16
            L13:
                r0 = 0
                r10 = r0
            L16:
                r0 = r6
                char r0 = r0.f50ch
                r1 = 38
                if (r0 != r1) goto L28
                r0 = r6
                char r0 = r0.getNextChar()
                r1 = 38
                if (r0 == r1) goto L40
            L28:
                r0 = r7
                r11 = r0
                r0 = r6
                char r0 = r0.f50ch
                r1 = 124(0x7c, float:1.74E-43)
                if (r0 != r1) goto L8f
                r0 = r7
                r11 = r0
                r0 = r6
                char r0 = r0.getNextChar()
                r1 = 124(0x7c, float:1.74E-43)
                if (r0 != r1) goto L8f
            L40:
                r0 = r6
                r0.next()
                r0 = r6
                r0.next()
                r0 = r6
                char r0 = r0.f50ch
                r1 = 40
                if (r0 != r1) goto L58
                r0 = r6
                r0.next()
                goto L5a
            L58:
                r0 = 0
                r8 = r0
            L5a:
                r0 = r6
                char r0 = r0.f50ch
                r1 = 32
                if (r0 != r1) goto L6a
                r0 = r6
                r0.next()
                goto L5a
            L6a:
                com.alibaba.fastjson.JSONPath$FilterGroup r0 = new com.alibaba.fastjson.JSONPath$FilterGroup
                r1 = r0
                r2 = r7
                r3 = r6
                r4 = 0
                java.lang.Object r3 = r3.parseArrayAccessFilter(r4)
                com.alibaba.fastjson.JSONPath$Filter r3 = (com.alibaba.fastjson.JSONPath.Filter) r3
                r4 = r10
                r1.<init>(r2, r3, r4)
                r11 = r0
                r0 = r8
                if (r0 == 0) goto L8f
                r0 = r6
                char r0 = r0.f50ch
                r1 = 41
                if (r0 != r1) goto L8f
                r0 = r6
                r0.next()
            L8f:
                r0 = r11
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.JSONPathParser.filterRest(com.alibaba.fastjson.JSONPath$Filter):com.alibaba.fastjson.JSONPath$Filter");
        }

        char getNextChar() {
            return this.path.charAt(this.pos);
        }

        boolean isEOF() {
            return this.pos >= this.path.length();
        }

        void next() {
            String str = this.path;
            int i = this.pos;
            this.pos = i + 1;
            this.f50ch = str.charAt(i);
        }

        Segment parseArrayAccess(boolean z) {
            Object arrayAccessFilter = parseArrayAccessFilter(z);
            return arrayAccessFilter instanceof Segment ? (Segment) arrayAccessFilter : new FilterSegment((Filter) arrayAccessFilter);
        }

        /* JADX WARN: Removed duplicated region for block: B:110:0x024b  */
        /* JADX WARN: Removed duplicated region for block: B:156:0x033a  */
        /* JADX WARN: Removed duplicated region for block: B:220:0x04a0  */
        /* JADX WARN: Removed duplicated region for block: B:250:0x0543  */
        /* JADX WARN: Removed duplicated region for block: B:280:0x05e6  */
        /* JADX WARN: Removed duplicated region for block: B:306:0x0688 A[LOOP:11: B:304:0x067c->B:306:0x0688, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:309:0x06a0  */
        /* JADX WARN: Removed duplicated region for block: B:310:0x06a6  */
        /* JADX WARN: Removed duplicated region for block: B:313:0x06b8  */
        /* JADX WARN: Removed duplicated region for block: B:318:0x06df  */
        /* JADX WARN: Removed duplicated region for block: B:350:0x081b  */
        /* JADX WARN: Removed duplicated region for block: B:406:0x0982  */
        /* JADX WARN: Removed duplicated region for block: B:431:0x0a0f  */
        /* JADX WARN: Removed duplicated region for block: B:452:0x0a9d  */
        /* JADX WARN: Removed duplicated region for block: B:477:0x0b27  */
        /* JADX WARN: Removed duplicated region for block: B:494:0x0b98  */
        /* JADX WARN: Removed duplicated region for block: B:520:0x0c38  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x010a  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0178  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x01f4  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        java.lang.Object parseArrayAccessFilter(boolean r10) {
            /*
                Method dump skipped, instructions count: 3338
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.JSONPathParser.parseArrayAccessFilter(boolean):java.lang.Object");
        }

        protected double readDoubleValue(long j) throws NumberFormatException {
            int i = this.pos;
            next();
            while (true) {
                char c = this.f50ch;
                if (c < '0' || c > '9') {
                    break;
                }
                next();
            }
            double d = Double.parseDouble(this.path.substring(i - 1, this.pos - 1));
            double d2 = j;
            Double.isNaN(d2);
            return d + d2;
        }

        protected long readLongValue() {
            int i = this.pos;
            char c = this.f50ch;
            if (c == '+' || c == '-') {
                next();
            }
            while (true) {
                char c2 = this.f50ch;
                if (c2 < '0' || c2 > '9') {
                    break;
                }
                next();
            }
            return Long.parseLong(this.path.substring(i - 1, this.pos - 1));
        }

        String readName() {
            skipWhitespace();
            char c = this.f50ch;
            if (c != '\\' && !Character.isJavaIdentifierStart(c)) {
                throw new JSONPathException("illeal jsonpath syntax. " + this.path);
            }
            StringBuilder sb = new StringBuilder();
            while (!isEOF()) {
                char c2 = this.f50ch;
                if (c2 == '\\') {
                    next();
                    sb.append(this.f50ch);
                    if (isEOF()) {
                        return sb.toString();
                    }
                    next();
                } else {
                    if (!Character.isJavaIdentifierPart(c2)) {
                        break;
                    }
                    sb.append(this.f50ch);
                    next();
                }
            }
            if (isEOF() && Character.isJavaIdentifierPart(this.f50ch)) {
                sb.append(this.f50ch);
            }
            return sb.toString();
        }

        protected Operator readOp() {
            Operator operator;
            char c = this.f50ch;
            if (c == '=') {
                next();
                char c2 = this.f50ch;
                if (c2 == '~') {
                    next();
                    operator = Operator.REG_MATCH;
                } else if (c2 == '=') {
                    next();
                    operator = Operator.EQ;
                } else {
                    operator = Operator.EQ;
                }
            } else if (c == '!') {
                next();
                accept('=');
                operator = Operator.NE;
            } else if (c == '<') {
                next();
                if (this.f50ch == '=') {
                    next();
                    operator = Operator.LE;
                } else {
                    operator = Operator.LT;
                }
            } else if (c == '>') {
                next();
                if (this.f50ch == '=') {
                    next();
                    operator = Operator.GE;
                } else {
                    operator = Operator.GT;
                }
            } else {
                operator = null;
            }
            Operator operator2 = operator;
            if (operator == null) {
                String name = readName();
                if ("not".equalsIgnoreCase(name)) {
                    skipWhitespace();
                    String name2 = readName();
                    if ("like".equalsIgnoreCase(name2)) {
                        operator2 = Operator.NOT_LIKE;
                    } else if ("rlike".equalsIgnoreCase(name2)) {
                        operator2 = Operator.NOT_RLIKE;
                    } else if ("in".equalsIgnoreCase(name2)) {
                        operator2 = Operator.NOT_IN;
                    } else {
                        if (!"between".equalsIgnoreCase(name2)) {
                            throw new UnsupportedOperationException();
                        }
                        operator2 = Operator.NOT_BETWEEN;
                    }
                } else if ("nin".equalsIgnoreCase(name)) {
                    operator2 = Operator.NOT_IN;
                } else if ("like".equalsIgnoreCase(name)) {
                    operator2 = Operator.LIKE;
                } else if ("rlike".equalsIgnoreCase(name)) {
                    operator2 = Operator.RLIKE;
                } else if ("in".equalsIgnoreCase(name)) {
                    operator2 = Operator.IN;
                } else {
                    if (!"between".equalsIgnoreCase(name)) {
                        throw new UnsupportedOperationException();
                    }
                    operator2 = Operator.BETWEEN;
                }
            }
            return operator2;
        }

        Segment readSegement() {
            boolean z;
            char c;
            if (this.level == 0 && this.path.length() == 1) {
                if (isDigitFirst(this.f50ch)) {
                    return new ArrayAccessSegment(this.f50ch - '0');
                }
                char c2 = this.f50ch;
                if ((c2 >= 'a' && c2 <= 'z') || ((c = this.f50ch) >= 'A' && c <= 'Z')) {
                    return new PropertySegment(Character.toString(this.f50ch), false);
                }
            }
            while (!isEOF()) {
                skipWhitespace();
                char c3 = this.f50ch;
                if (c3 != '$') {
                    if (c3 != '.' && c3 != '/') {
                        if (c3 == '[') {
                            return parseArrayAccess(true);
                        }
                        if (this.level == 0) {
                            return new PropertySegment(readName(), false);
                        }
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    char c4 = this.f50ch;
                    next();
                    if (c4 == '.' && this.f50ch == '.') {
                        next();
                        int length = this.path.length();
                        int i = this.pos;
                        z = true;
                        if (length > i + 3) {
                            z = true;
                            if (this.f50ch == '[') {
                                z = true;
                                if (this.path.charAt(i) == '*') {
                                    z = true;
                                    if (this.path.charAt(this.pos + 1) == ']') {
                                        z = true;
                                        if (this.path.charAt(this.pos + 2) == '.') {
                                            next();
                                            next();
                                            next();
                                            next();
                                            z = true;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        z = false;
                    }
                    char c5 = this.f50ch;
                    if (c5 == '*') {
                        if (!isEOF()) {
                            next();
                        }
                        return z ? WildCardSegment.instance_deep : WildCardSegment.instance;
                    }
                    if (isDigitFirst(c5)) {
                        return parseArrayAccess(false);
                    }
                    String name = readName();
                    if (this.f50ch != '(') {
                        return new PropertySegment(name, z);
                    }
                    next();
                    if (this.f50ch != ')') {
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    if (!isEOF()) {
                        next();
                    }
                    if ("size".equals(name) || "length".equals(name)) {
                        return SizeSegment.instance;
                    }
                    if ("max".equals(name)) {
                        return MaxSegment.instance;
                    }
                    if ("min".equals(name)) {
                        return MinSegment.instance;
                    }
                    if ("keySet".equals(name)) {
                        return KeySetSegment.instance;
                    }
                    throw new JSONPathException("not support jsonpath : " + this.path);
                }
                next();
            }
            return null;
        }

        String readString() {
            char c = this.f50ch;
            next();
            int i = this.pos;
            while (this.f50ch != c && !isEOF()) {
                next();
            }
            String strSubstring = this.path.substring(i - 1, isEOF() ? this.pos : this.pos - 1);
            accept(c);
            return strSubstring;
        }

        protected Object readValue() {
            skipWhitespace();
            if (isDigitFirst(this.f50ch)) {
                return Long.valueOf(readLongValue());
            }
            char c = this.f50ch;
            if (c == '\"' || c == '\'') {
                return readString();
            }
            if (c != 'n') {
                throw new UnsupportedOperationException();
            }
            if ("null".equals(readName())) {
                return null;
            }
            throw new JSONPathException(this.path);
        }

        public final void skipWhitespace() {
            while (true) {
                char c = this.f50ch;
                if (c > ' ') {
                    return;
                }
                if (c != ' ' && c != '\r' && c != '\n' && c != '\t' && c != '\f' && c != '\b') {
                    return;
                } else {
                    next();
                }
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$KeySetSegment.class */
    static class KeySetSegment implements Segment {
        public static final KeySetSegment instance = new KeySetSegment();

        KeySetSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.evalKeySet(obj2);
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$MatchSegement.class */
    static class MatchSegement implements Filter {
        private final String[] containsValues;
        private final String endsWithValue;
        private final int minLength;
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final String startsWithValue;

        public MatchSegement(String str, String str2, String str3, String[] strArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.startsWithValue = str2;
            this.endsWithValue = str3;
            this.containsValues = strArr;
            this.not = z;
            int i = 0;
            int length = str2 != null ? str2.length() + 0 : 0;
            int length2 = str3 != null ? length + str3.length() : length;
            int i2 = length2;
            if (strArr != null) {
                int length3 = strArr.length;
                while (true) {
                    i2 = length2;
                    if (i >= length3) {
                        break;
                    }
                    length2 += strArr[i].length();
                    i++;
                }
            }
            this.minLength = i2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            int length;
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            String string = propertyValue.toString();
            if (string.length() < this.minLength) {
                return this.not;
            }
            String str = this.startsWithValue;
            if (str == null) {
                length = 0;
            } else {
                if (!string.startsWith(str)) {
                    return this.not;
                }
                length = this.startsWithValue.length() + 0;
            }
            String[] strArr = this.containsValues;
            if (strArr != null) {
                int length2 = length;
                for (String str2 : strArr) {
                    int iIndexOf = string.indexOf(str2, length2);
                    if (iIndexOf == -1) {
                        return this.not;
                    }
                    length2 = iIndexOf + str2.length();
                }
            }
            String str3 = this.endsWithValue;
            return (str3 == null || string.endsWith(str3)) ? !this.not : this.not;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$MaxSegment.class */
    static class MaxSegment implements Segment {
        public static final MaxSegment instance = new MaxSegment();

        MaxSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!(obj instanceof Collection)) {
                throw new UnsupportedOperationException();
            }
            Object obj3 = null;
            for (Object obj4 : (Collection) obj) {
                if (obj4 != null && (obj3 == null || JSONPath.compare(obj3, obj4) < 0)) {
                    obj3 = obj4;
                }
            }
            return obj3;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$MinSegment.class */
    static class MinSegment implements Segment {
        public static final MinSegment instance = new MinSegment();

        MinSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!(obj instanceof Collection)) {
                throw new UnsupportedOperationException();
            }
            Object obj3 = null;
            for (Object obj4 : (Collection) obj) {
                if (obj4 != null && (obj3 == null || JSONPath.compare(obj3, obj4) > 0)) {
                    obj3 = obj4;
                }
            }
            return obj3;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$MultiIndexSegment.class */
    static class MultiIndexSegment implements Segment {
        private final int[] indexes;

        public MultiIndexSegment(int[] iArr) {
            this.indexes = iArr;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            JSONArray jSONArray = new JSONArray(this.indexes.length);
            int i = 0;
            while (true) {
                int[] iArr = this.indexes;
                if (i >= iArr.length) {
                    return jSONArray;
                }
                jSONArray.add(jSONPath.getArrayItem(obj2, iArr[i]));
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (context.eval) {
                Object obj = defaultJSONParser.parse();
                if (obj instanceof List) {
                    int[] iArr = this.indexes;
                    int length = iArr.length;
                    int[] iArr2 = new int[length];
                    System.arraycopy(iArr, 0, iArr2, 0, length);
                    boolean z = iArr2[0] >= 0;
                    List list = (List) obj;
                    if (z) {
                        for (int size = list.size() - 1; size >= 0; size--) {
                            if (Arrays.binarySearch(iArr2, size) < 0) {
                                list.remove(size);
                            }
                        }
                        context.object = list;
                        return;
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$MultiPropertySegment.class */
    static class MultiPropertySegment implements Segment {
        private final String[] propertyNames;
        private final long[] propertyNamesHash;

        public MultiPropertySegment(String[] strArr) {
            this.propertyNames = strArr;
            this.propertyNamesHash = new long[strArr.length];
            int i = 0;
            while (true) {
                long[] jArr = this.propertyNamesHash;
                if (i >= jArr.length) {
                    return;
                }
                jArr[i] = TypeUtils.fnv1a_64(strArr[i]);
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.propertyNames.length);
            int i = 0;
            while (true) {
                String[] strArr = this.propertyNames;
                if (i >= strArr.length) {
                    return arrayList;
                }
                arrayList.add(jSONPath.getPropertyValue(obj2, strArr[i], this.propertyNamesHash[i]));
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) throws NumberFormatException {
            JSONArray jSONArray;
            Object objIntegerValue;
            JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
            if (context.object == null) {
                JSONArray jSONArray2 = new JSONArray();
                context.object = jSONArray2;
                jSONArray = jSONArray2;
            } else {
                jSONArray = (JSONArray) context.object;
            }
            for (int size = jSONArray.size(); size < this.propertyNamesHash.length; size++) {
                jSONArray.add(null);
            }
            do {
                int iSeekObjectToField = jSONLexerBase.seekObjectToField(this.propertyNamesHash);
                if (jSONLexerBase.matchStat != 3) {
                    return;
                }
                int i = jSONLexerBase.token();
                if (i == 2) {
                    objIntegerValue = jSONLexerBase.integerValue();
                    jSONLexerBase.nextToken(16);
                } else if (i == 3) {
                    objIntegerValue = jSONLexerBase.decimalValue();
                    jSONLexerBase.nextToken(16);
                } else if (i != 4) {
                    objIntegerValue = defaultJSONParser.parse();
                } else {
                    objIntegerValue = jSONLexerBase.stringVal();
                    jSONLexerBase.nextToken(16);
                }
                jSONArray.set(iSeekObjectToField, objIntegerValue);
            } while (jSONLexerBase.token() == 16);
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$NotNullSegement.class */
    static class NotNullSegement implements Filter {
        private final String propertyName;
        private final long propertyNameHash;

        public NotNullSegement(String str) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash) != null;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$NullSegement.class */
    static class NullSegement implements Filter {
        private final String propertyName;
        private final long propertyNameHash;

        public NullSegement(String str) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash) == null;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$Operator.class */
    enum Operator {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT_LIKE,
        RLIKE,
        NOT_RLIKE,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN,
        And,
        Or,
        REG_MATCH
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$PropertySegment.class */
    static class PropertySegment implements Segment {
        private final boolean deep;
        private final String propertyName;
        private final long propertyNameHash;

        public PropertySegment(String str, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.deep = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!this.deep) {
                return jSONPath.getPropertyValue(obj2, this.propertyName, this.propertyNameHash);
            }
            ArrayList arrayList = new ArrayList();
            jSONPath.deepScan(obj2, this.propertyName, arrayList);
            return arrayList;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) throws NumberFormatException {
            Object objIntegerValue;
            Object objIntegerValue2;
            Object objIntegerValue3;
            JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
            if (this.deep && context.object == null) {
                context.object = new JSONArray();
            }
            if (jSONLexerBase.token() != 14) {
                boolean z = this.deep;
                if (!z) {
                    if (jSONLexerBase.seekObjectToField(this.propertyNameHash, z) == 3 && context.eval) {
                        int i = jSONLexerBase.token();
                        if (i == 2) {
                            objIntegerValue2 = jSONLexerBase.integerValue();
                            jSONLexerBase.nextToken(16);
                        } else if (i == 3) {
                            objIntegerValue2 = jSONLexerBase.decimalValue();
                            jSONLexerBase.nextToken(16);
                        } else if (i != 4) {
                            objIntegerValue2 = defaultJSONParser.parse();
                        } else {
                            objIntegerValue2 = jSONLexerBase.stringVal();
                            jSONLexerBase.nextToken(16);
                        }
                        if (context.eval) {
                            context.object = objIntegerValue2;
                            return;
                        }
                        return;
                    }
                    return;
                }
                while (true) {
                    int iSeekObjectToField = jSONLexerBase.seekObjectToField(this.propertyNameHash, this.deep);
                    if (iSeekObjectToField == -1) {
                        return;
                    }
                    if (iSeekObjectToField == 3) {
                        if (context.eval) {
                            int i2 = jSONLexerBase.token();
                            if (i2 == 2) {
                                objIntegerValue = jSONLexerBase.integerValue();
                                jSONLexerBase.nextToken(16);
                            } else if (i2 == 3) {
                                objIntegerValue = jSONLexerBase.decimalValue();
                                jSONLexerBase.nextToken(16);
                            } else if (i2 != 4) {
                                objIntegerValue = defaultJSONParser.parse();
                            } else {
                                objIntegerValue = jSONLexerBase.stringVal();
                                jSONLexerBase.nextToken(16);
                            }
                            if (context.eval) {
                                if (context.object instanceof List) {
                                    List list = (List) context.object;
                                    if (list.size() == 0 && (objIntegerValue instanceof List)) {
                                        context.object = objIntegerValue;
                                    } else {
                                        list.add(objIntegerValue);
                                    }
                                } else {
                                    context.object = objIntegerValue;
                                }
                            }
                        }
                    } else if (iSeekObjectToField == 1 || iSeekObjectToField == 2) {
                        extract(jSONPath, defaultJSONParser, context);
                    }
                }
            } else {
                if ("*".equals(this.propertyName)) {
                    return;
                }
                jSONLexerBase.nextToken();
                JSONArray jSONArray = this.deep ? (JSONArray) context.object : new JSONArray();
                while (true) {
                    int i3 = jSONLexerBase.token();
                    if (i3 == 12) {
                        boolean z2 = this.deep;
                        if (z2) {
                            extract(jSONPath, defaultJSONParser, context);
                        } else {
                            int iSeekObjectToField2 = jSONLexerBase.seekObjectToField(this.propertyNameHash, z2);
                            if (iSeekObjectToField2 == 3) {
                                int i4 = jSONLexerBase.token();
                                if (i4 == 2) {
                                    objIntegerValue3 = jSONLexerBase.integerValue();
                                    jSONLexerBase.nextToken();
                                } else if (i4 != 4) {
                                    objIntegerValue3 = defaultJSONParser.parse();
                                } else {
                                    objIntegerValue3 = jSONLexerBase.stringVal();
                                    jSONLexerBase.nextToken();
                                }
                                jSONArray.add(objIntegerValue3);
                                if (jSONLexerBase.token() == 13) {
                                    jSONLexerBase.nextToken();
                                } else {
                                    jSONLexerBase.skipObject(false);
                                }
                            } else if (iSeekObjectToField2 == -1) {
                                continue;
                            } else {
                                if (this.deep) {
                                    throw new UnsupportedOperationException(jSONLexerBase.info());
                                }
                                jSONLexerBase.skipObject(false);
                            }
                        }
                    } else if (i3 != 14) {
                        switch (i3) {
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                jSONLexerBase.nextToken();
                                break;
                        }
                    } else if (this.deep) {
                        extract(jSONPath, defaultJSONParser, context);
                    } else {
                        jSONLexerBase.skipObject(false);
                    }
                    if (jSONLexerBase.token() == 15) {
                        jSONLexerBase.nextToken();
                        if (this.deep || jSONArray.size() <= 0) {
                            return;
                        }
                        context.object = jSONArray;
                        return;
                    }
                    if (jSONLexerBase.token() != 16) {
                        throw new JSONException("illegal json : " + jSONLexerBase.info());
                    }
                    jSONLexerBase.nextToken();
                }
            }
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removePropertyValue(obj, this.propertyName, this.deep);
        }

        public void setValue(JSONPath jSONPath, Object obj, Object obj2) {
            if (this.deep) {
                jSONPath.deepSet(obj, this.propertyName, this.propertyNameHash, obj2);
            } else {
                jSONPath.setPropertyValue(obj, this.propertyName, this.propertyNameHash, obj2);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$RangeSegment.class */
    static class RangeSegment implements Segment {
        private final int end;
        private final int start;
        private final int step;

        public RangeSegment(int i, int i2, int i3) {
            this.start = i;
            this.end = i2;
            this.step = i3;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            int iIntValue = SizeSegment.instance.eval(jSONPath, obj, obj2).intValue();
            int i = this.start;
            if (i < 0) {
                i += iIntValue;
            }
            int i2 = this.end;
            if (i2 < 0) {
                i2 += iIntValue;
            }
            int i3 = ((i2 - i) / this.step) + 1;
            if (i3 == -1) {
                return null;
            }
            ArrayList arrayList = new ArrayList(i3);
            while (i <= i2 && i < iIntValue) {
                arrayList.add(jSONPath.getArrayItem(obj2, i));
                i += this.step;
            }
            return arrayList;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$RefOpSegement.class */
    static class RefOpSegement implements Filter {

        /* renamed from: op */
        private final Operator f59op;
        private final String propertyName;
        private final long propertyNameHash;
        private final Segment refSgement;

        public RefOpSegement(String str, Segment segment, Operator operator) {
            this.propertyName = str;
            this.refSgement = segment;
            this.f59op = operator;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            boolean z = false;
            if (propertyValue == null || !(propertyValue instanceof Number)) {
                return false;
            }
            Object objEval = this.refSgement.eval(jSONPath, obj, obj);
            if ((objEval instanceof Integer) || (objEval instanceof Long) || (objEval instanceof Short) || (objEval instanceof Byte)) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) objEval);
                if ((propertyValue instanceof Integer) || (propertyValue instanceof Long) || (propertyValue instanceof Short) || (propertyValue instanceof Byte)) {
                    long jLongExtractValue2 = TypeUtils.longExtractValue((Number) propertyValue);
                    switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f59op.ordinal()]) {
                        case 1:
                            boolean z2 = false;
                            if (jLongExtractValue2 == jLongExtractValue) {
                                z2 = true;
                            }
                            return z2;
                        case 2:
                            boolean z3 = false;
                            if (jLongExtractValue2 != jLongExtractValue) {
                                z3 = true;
                            }
                            return z3;
                        case 3:
                            boolean z4 = false;
                            if (jLongExtractValue2 >= jLongExtractValue) {
                                z4 = true;
                            }
                            return z4;
                        case 4:
                            boolean z5 = false;
                            if (jLongExtractValue2 > jLongExtractValue) {
                                z5 = true;
                            }
                            return z5;
                        case 5:
                            boolean z6 = false;
                            if (jLongExtractValue2 <= jLongExtractValue) {
                                z6 = true;
                            }
                            return z6;
                        case 6:
                            boolean z7 = false;
                            if (jLongExtractValue2 < jLongExtractValue) {
                                z7 = true;
                            }
                            return z7;
                    }
                }
                if (propertyValue instanceof BigDecimal) {
                    int iCompareTo = BigDecimal.valueOf(jLongExtractValue).compareTo((BigDecimal) propertyValue);
                    switch (C03951.$SwitchMap$com$alibaba$fastjson$JSONPath$Operator[this.f59op.ordinal()]) {
                        case 1:
                            boolean z8 = false;
                            if (iCompareTo == 0) {
                                z8 = true;
                            }
                            return z8;
                        case 2:
                            boolean z9 = false;
                            if (iCompareTo != 0) {
                                z9 = true;
                            }
                            return z9;
                        case 3:
                            boolean z10 = false;
                            if (iCompareTo <= 0) {
                                z10 = true;
                            }
                            return z10;
                        case 4:
                            boolean z11 = false;
                            if (iCompareTo < 0) {
                                z11 = true;
                            }
                            return z11;
                        case 5:
                            if (iCompareTo >= 0) {
                                z = true;
                            }
                            return z;
                        case 6:
                            boolean z12 = false;
                            if (iCompareTo > 0) {
                                z12 = true;
                            }
                            return z12;
                        default:
                            return false;
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$RegMatchSegement.class */
    static class RegMatchSegement implements Filter {

        /* renamed from: op */
        private final Operator f60op;
        private final Pattern pattern;
        private final String propertyName;
        private final long propertyNameHash;

        public RegMatchSegement(String str, Pattern pattern, Operator operator) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.pattern = pattern;
            this.f60op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            return this.pattern.matcher(propertyValue.toString()).matches();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$RlikeSegement.class */
    static class RlikeSegement implements Filter {
        private final boolean not;
        private final Pattern pattern;
        private final String propertyName;
        private final long propertyNameHash;

        public RlikeSegement(String str, String str2, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.pattern = Pattern.compile(str2);
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (propertyValue == null) {
                return false;
            }
            boolean zMatches = this.pattern.matcher(propertyValue.toString()).matches();
            boolean z = zMatches;
            if (this.not) {
                z = !zMatches;
            }
            return z;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$Segment.class */
    interface Segment {
        Object eval(JSONPath jSONPath, Object obj, Object obj2);

        void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context);
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$SizeSegment.class */
    static class SizeSegment implements Segment {
        public static final SizeSegment instance = new SizeSegment();

        SizeSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Integer eval(JSONPath jSONPath, Object obj, Object obj2) {
            return Integer.valueOf(jSONPath.evalSize(obj2));
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$StringInSegement.class */
    static class StringInSegement implements Filter {
        private final boolean not;
        private final String propertyName;
        private final long propertyNameHash;
        private final String[] values;

        public StringInSegement(String str, String[] strArr, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.values = strArr;
            this.not = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            for (String str : this.values) {
                if (str == propertyValue) {
                    return !this.not;
                }
                if (str != null && str.equals(propertyValue)) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$StringOpSegement.class */
    static class StringOpSegement implements Filter {

        /* renamed from: op */
        private final Operator f61op;
        private final String propertyName;
        private final long propertyNameHash;
        private final String value;

        public StringOpSegement(String str, String str2, Operator operator) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = str2;
            this.f61op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object propertyValue = jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash);
            if (this.f61op == Operator.EQ) {
                return this.value.equals(propertyValue);
            }
            boolean z = true;
            if (this.f61op == Operator.NE) {
                return !this.value.equals(propertyValue);
            }
            if (propertyValue == null) {
                return false;
            }
            int iCompareTo = this.value.compareTo(propertyValue.toString());
            if (this.f61op == Operator.GE) {
                return iCompareTo <= 0;
            }
            if (this.f61op == Operator.GT) {
                return iCompareTo < 0;
            }
            if (this.f61op == Operator.LE) {
                return iCompareTo >= 0;
            }
            if (this.f61op != Operator.LT) {
                return false;
            }
            if (iCompareTo <= 0) {
                z = false;
            }
            return z;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$ValueSegment.class */
    static class ValueSegment implements Filter {

        /* renamed from: eq */
        private boolean f62eq;
        private final String propertyName;
        private final long propertyNameHash;
        private final Object value;

        public ValueSegment(String str, Object obj, boolean z) {
            this.f62eq = true;
            if (obj == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.value = obj;
            this.f62eq = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            boolean zEquals = this.value.equals(jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash));
            boolean z = zEquals;
            if (!this.f62eq) {
                z = !zEquals;
            }
            return z;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPath$WildCardSegment.class */
    static class WildCardSegment implements Segment {
        public static final WildCardSegment instance = new WildCardSegment(false);
        public static final WildCardSegment instance_deep = new WildCardSegment(true);
        private boolean deep;

        private WildCardSegment(boolean z) {
            this.deep = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!this.deep) {
                return jSONPath.getPropertyValues(obj2);
            }
            ArrayList arrayList = new ArrayList();
            jSONPath.deepGetPropertyValues(obj2, arrayList);
            return arrayList;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (context.eval) {
                Object obj = defaultJSONParser.parse();
                if (this.deep) {
                    ArrayList arrayList = new ArrayList();
                    jSONPath.deepGetPropertyValues(obj, arrayList);
                    context.object = arrayList;
                    return;
                } else {
                    if (obj instanceof JSONObject) {
                        Collection<Object> collectionValues = ((JSONObject) obj).values();
                        JSONArray jSONArray = new JSONArray(collectionValues.size());
                        Iterator<Object> it = collectionValues.iterator();
                        while (it.hasNext()) {
                            jSONArray.add(it.next());
                        }
                        context.object = jSONArray;
                        return;
                    }
                    if (obj instanceof JSONArray) {
                        context.object = obj;
                        return;
                    }
                }
            }
            throw new JSONException("TODO");
        }
    }

    public JSONPath(String str) {
        this(str, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance());
    }

    public JSONPath(String str, SerializeConfig serializeConfig, ParserConfig parserConfig) {
        if (str == null || str.length() == 0) {
            throw new JSONPathException("json-path can not be null or empty");
        }
        this.path = str;
        this.serializeConfig = serializeConfig;
        this.parserConfig = parserConfig;
    }

    public static void arrayAdd(Object obj, String str, Object... objArr) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        compile(str).arrayAdd(obj, objArr);
    }

    static int compare(Object obj, Object obj2) {
        Object obj3;
        Object obj4;
        Object d;
        Object f;
        if (obj.getClass() == obj2.getClass()) {
            return ((Comparable) obj).compareTo(obj2);
        }
        Class<?> cls = obj.getClass();
        Class<?> cls2 = obj2.getClass();
        if (cls == BigDecimal.class) {
            if (cls2 == Integer.class) {
                f = new BigDecimal(((Integer) obj2).intValue());
            } else if (cls2 == Long.class) {
                f = new BigDecimal(((Long) obj2).longValue());
            } else if (cls2 == Float.class) {
                f = new BigDecimal(((Float) obj2).floatValue());
            } else {
                obj3 = obj;
                obj4 = obj2;
                if (cls2 == Double.class) {
                    f = new BigDecimal(((Double) obj2).doubleValue());
                }
            }
            obj3 = obj;
            obj4 = f;
        } else if (cls == Long.class) {
            if (cls2 == Integer.class) {
                f = new Long(((Integer) obj2).intValue());
                obj3 = obj;
                obj4 = f;
            } else {
                if (cls2 == BigDecimal.class) {
                    d = new BigDecimal(((Long) obj).longValue());
                } else if (cls2 == Float.class) {
                    d = new Float(((Long) obj).longValue());
                } else {
                    obj3 = obj;
                    obj4 = obj2;
                    if (cls2 == Double.class) {
                        d = new Double(((Long) obj).longValue());
                    }
                }
                obj3 = d;
                obj4 = obj2;
            }
        } else if (cls == Integer.class) {
            if (cls2 == Long.class) {
                d = new Long(((Integer) obj).intValue());
            } else if (cls2 == BigDecimal.class) {
                d = new BigDecimal(((Integer) obj).intValue());
            } else if (cls2 == Float.class) {
                d = new Float(((Integer) obj).intValue());
            } else {
                obj3 = obj;
                obj4 = obj2;
                if (cls2 == Double.class) {
                    d = new Double(((Integer) obj).intValue());
                }
            }
            obj3 = d;
            obj4 = obj2;
        } else if (cls == Double.class) {
            if (cls2 == Integer.class) {
                f = new Double(((Integer) obj2).intValue());
            } else if (cls2 == Long.class) {
                f = new Double(((Long) obj2).longValue());
            } else {
                obj3 = obj;
                obj4 = obj2;
                if (cls2 == Float.class) {
                    f = new Double(((Float) obj2).floatValue());
                }
            }
            obj3 = obj;
            obj4 = f;
        } else {
            obj3 = obj;
            obj4 = obj2;
            if (cls == Float.class) {
                if (cls2 == Integer.class) {
                    f = new Float(((Integer) obj2).intValue());
                } else if (cls2 == Long.class) {
                    f = new Float(((Long) obj2).longValue());
                } else {
                    obj3 = obj;
                    obj4 = obj2;
                    if (cls2 == Double.class) {
                        d = new Double(((Float) obj).floatValue());
                        obj3 = d;
                        obj4 = obj2;
                    }
                }
                obj3 = obj;
                obj4 = f;
            }
        }
        return ((Comparable) obj3).compareTo(obj4);
    }

    public static JSONPath compile(String str) {
        if (str == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        JSONPath jSONPath = pathCache.get(str);
        JSONPath jSONPath2 = jSONPath;
        if (jSONPath == null) {
            JSONPath jSONPath3 = new JSONPath(str);
            jSONPath2 = jSONPath3;
            if (pathCache.size() < 1024) {
                pathCache.putIfAbsent(str, jSONPath3);
                jSONPath2 = pathCache.get(str);
            }
        }
        return jSONPath2;
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return compile(str).contains(obj);
    }

    public static boolean containsValue(Object obj, String str, Object obj2) {
        return compile(str).containsValue(obj, obj2);
    }

    /* renamed from: eq */
    static boolean m10eq(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.getClass() != obj2.getClass() && (obj instanceof Number)) {
            if (obj2 instanceof Number) {
                return eqNotNull((Number) obj, (Number) obj2);
            }
            return false;
        }
        return obj.equals(obj2);
    }

    static boolean eqNotNull(Number number, Number number2) {
        Class<?> cls = number.getClass();
        boolean zIsInt = isInt(cls);
        Class<?> cls2 = number2.getClass();
        boolean zIsInt2 = isInt(cls2);
        if (number instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) number;
            if (zIsInt2) {
                return bigDecimal.equals(BigDecimal.valueOf(TypeUtils.longExtractValue(number2)));
            }
        }
        boolean z = true;
        if (zIsInt) {
            if (zIsInt2) {
                return number.longValue() == number2.longValue();
            }
            if (number2 instanceof BigInteger) {
                return BigInteger.valueOf(number.longValue()).equals((BigInteger) number);
            }
        }
        if (zIsInt2 && (number instanceof BigInteger)) {
            return ((BigInteger) number).equals(BigInteger.valueOf(TypeUtils.longExtractValue(number2)));
        }
        boolean zIsDouble = isDouble(cls);
        boolean zIsDouble2 = isDouble(cls2);
        if ((!zIsDouble || !zIsDouble2) && ((!zIsDouble || !zIsInt2) && (!zIsDouble2 || !zIsInt))) {
            return false;
        }
        if (number.doubleValue() != number2.doubleValue()) {
            z = false;
        }
        return z;
    }

    public static Object eval(Object obj, String str) {
        return compile(str).eval(obj);
    }

    public static Object extract(String str, String str2) {
        return extract(str, str2, ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE, new Feature[0]);
    }

    public static Object extract(String str, String str2, ParserConfig parserConfig, int i, Feature... featureArr) {
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, parserConfig, i | Feature.OrderedField.mask);
        Object objExtract = compile(str2).extract(defaultJSONParser);
        defaultJSONParser.lexer.close();
        return objExtract;
    }

    protected static boolean isDouble(Class<?> cls) {
        return cls == Float.class || cls == Double.class;
    }

    protected static boolean isInt(Class<?> cls) {
        return cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class;
    }

    public static Set<?> keySet(Object obj, String str) {
        JSONPath jSONPathCompile = compile(str);
        return jSONPathCompile.evalKeySet(jSONPathCompile.eval(obj));
    }

    public static Map<String, Object> paths(Object obj) {
        return paths(obj, SerializeConfig.globalInstance);
    }

    public static Map<String, Object> paths(Object obj, SerializeConfig serializeConfig) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        HashMap map = new HashMap();
        paths(identityHashMap, map, "/", obj, serializeConfig);
        return map;
    }

    private static void paths(Map<Object, String> map, Map<String, Object> map2, String str, Object obj, SerializeConfig serializeConfig) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        StringBuilder sb4;
        if (obj == null) {
            return;
        }
        if (map.put(obj, str) != null) {
            if (!((obj instanceof String) || (obj instanceof Number) || (obj instanceof Date) || (obj instanceof UUID))) {
                return;
            }
        }
        map2.put(str, obj);
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    if (str.equals("/")) {
                        sb4 = new StringBuilder();
                    } else {
                        sb4 = new StringBuilder();
                        sb4.append(str);
                    }
                    sb4.append("/");
                    sb4.append(key);
                    paths(map, map2, sb4.toString(), entry.getValue(), serializeConfig);
                }
            }
            return;
        }
        if (obj instanceof Collection) {
            int i = 0;
            for (Object obj2 : (Collection) obj) {
                if (str.equals("/")) {
                    sb3 = new StringBuilder();
                } else {
                    sb3 = new StringBuilder();
                    sb3.append(str);
                }
                sb3.append("/");
                sb3.append(i);
                paths(map, map2, sb3.toString(), obj2, serializeConfig);
                i++;
            }
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int length = Array.getLength(obj);
            for (int i2 = 0; i2 < length; i2++) {
                Object obj3 = Array.get(obj, i2);
                if (str.equals("/")) {
                    sb2 = new StringBuilder();
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(str);
                }
                sb2.append("/");
                sb2.append(i2);
                paths(map, map2, sb2.toString(), obj3, serializeConfig);
            }
            return;
        }
        if (ParserConfig.isPrimitive2(cls) || cls.isEnum()) {
            return;
        }
        ObjectSerializer objectWriter = serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            try {
                for (Map.Entry<String, Object> entry2 : ((JavaBeanSerializer) objectWriter).getFieldValuesMap(obj).entrySet()) {
                    String key2 = entry2.getKey();
                    if (key2 instanceof String) {
                        if (str.equals("/")) {
                            sb = new StringBuilder();
                            sb.append("/");
                            sb.append(key2);
                        } else {
                            sb = new StringBuilder();
                            sb.append(str);
                            sb.append("/");
                            sb.append(key2);
                        }
                        paths(map, map2, sb.toString(), entry2.getValue(), serializeConfig);
                    }
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
        }
    }

    public static Object read(String str, String str2) {
        return compile(str2).eval(JSON.parse(str));
    }

    public static boolean remove(Object obj, String str) {
        return compile(str).remove(obj);
    }

    public static Object reserveToArray(Object obj, String... strArr) {
        JSONArray jSONArray = new JSONArray();
        if (strArr != null && strArr.length != 0) {
            for (String str : strArr) {
                JSONPath jSONPathCompile = compile(str);
                jSONPathCompile.init();
                jSONArray.add(jSONPathCompile.eval(obj));
            }
        }
        return jSONArray;
    }

    public static Object reserveToObject(Object obj, String... strArr) {
        Object objEval;
        if (strArr == null || strArr.length == 0) {
            return obj;
        }
        JSONObject jSONObject = new JSONObject(true);
        for (String str : strArr) {
            JSONPath jSONPathCompile = compile(str);
            jSONPathCompile.init();
            Segment[] segmentArr = jSONPathCompile.segments;
            if ((segmentArr[segmentArr.length - 1] instanceof PropertySegment) && (objEval = jSONPathCompile.eval(obj)) != null) {
                jSONPathCompile.set(jSONObject, objEval);
            }
        }
        return jSONObject;
    }

    public static boolean set(Object obj, String str, Object obj2) {
        return compile(str).set(obj, obj2);
    }

    public static int size(Object obj, String str) {
        JSONPath jSONPathCompile = compile(str);
        return jSONPathCompile.evalSize(jSONPathCompile.eval(obj));
    }

    public void arrayAdd(Object obj, Object... objArr) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (objArr == null || objArr.length == 0 || obj == null) {
            return;
        }
        init();
        Object obj2 = null;
        Object objEval = obj;
        int i = 0;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                break;
            }
            if (i == segmentArr.length - 1) {
                obj2 = objEval;
            }
            objEval = this.segments[i].eval(this, obj, objEval);
            i++;
        }
        if (objEval == null) {
            throw new JSONPathException("value not found in path " + this.path);
        }
        if (objEval instanceof Collection) {
            Collection collection = (Collection) objEval;
            for (Object obj3 : objArr) {
                collection.add(obj3);
            }
            return;
        }
        Class<?> cls = objEval.getClass();
        if (!cls.isArray()) {
            throw new JSONException("unsupported array put operation. " + cls);
        }
        int length = Array.getLength(objEval);
        Object objNewInstance = Array.newInstance(cls.getComponentType(), objArr.length + length);
        System.arraycopy(objEval, 0, objNewInstance, 0, length);
        for (int i2 = 0; i2 < objArr.length; i2++) {
            Array.set(objNewInstance, length + i2, objArr[i2]);
        }
        Segment[] segmentArr2 = this.segments;
        Segment segment = segmentArr2[segmentArr2.length - 1];
        if (segment instanceof PropertySegment) {
            ((PropertySegment) segment).setValue(this, obj2, objNewInstance);
        } else {
            if (!(segment instanceof ArrayAccessSegment)) {
                throw new UnsupportedOperationException();
            }
            ((ArrayAccessSegment) segment).setValue(this, obj2, objNewInstance);
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        init();
        Object obj2 = obj;
        int i = 0;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return true;
            }
            Object objEval = segmentArr[i].eval(this, obj, obj2);
            if (objEval == null) {
                return false;
            }
            if (objEval == Collections.EMPTY_LIST && (obj2 instanceof List)) {
                return ((List) obj2).contains(objEval);
            }
            i++;
            obj2 = objEval;
        }
    }

    public boolean containsValue(Object obj, Object obj2) {
        Object objEval = eval(obj);
        if (objEval == obj2) {
            return true;
        }
        if (objEval == null) {
            return false;
        }
        if (!(objEval instanceof Iterable)) {
            return m10eq(objEval, obj2);
        }
        Iterator it = ((Iterable) objEval).iterator();
        while (it.hasNext()) {
            if (m10eq(it.next(), obj2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v35, types: [java.util.Collection] */
    /* JADX WARN: Type inference failed for: r0v38, types: [java.util.Collection] */
    protected void deepGetPropertyValues(Object obj, List<Object> list) {
        List<Object> fieldValues;
        Class<?> cls = obj.getClass();
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(cls);
        if (javaBeanSerializer != null) {
            try {
                fieldValues = javaBeanSerializer.getFieldValues(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        } else {
            fieldValues = obj instanceof Map ? ((Map) obj).values() : obj instanceof Collection ? (Collection) obj : null;
        }
        if (fieldValues == null) {
            throw new UnsupportedOperationException(cls.getName());
        }
        for (Object obj2 : fieldValues) {
            if (obj2 == null || ParserConfig.isPrimitive2(obj2.getClass())) {
                list.add(obj2);
            } else {
                deepGetPropertyValues(obj2, list);
            }
        }
    }

    protected void deepScan(Object obj, String str, List<Object> list) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object value = entry.getValue();
                if (str.equals(entry.getKey())) {
                    if (value instanceof Collection) {
                        list.addAll((Collection) value);
                    } else {
                        list.add(value);
                    }
                } else if (value != null && !ParserConfig.isPrimitive2(value.getClass())) {
                    deepScan(value, str, list);
                }
            }
            return;
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (!ParserConfig.isPrimitive2(obj2.getClass())) {
                    deepScan(obj2, str, list);
                }
            }
            return;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            if (obj instanceof List) {
                List list2 = (List) obj;
                for (int i = 0; i < list2.size(); i++) {
                    deepScan(list2.get(i), str, list);
                }
                return;
            }
            return;
        }
        try {
            FieldSerializer fieldSerializer = javaBeanSerializer.getFieldSerializer(str);
            if (fieldSerializer == null) {
                Iterator<Object> it = javaBeanSerializer.getFieldValues(obj).iterator();
                while (it.hasNext()) {
                    deepScan(it.next(), str, list);
                }
                return;
            }
            try {
                list.add(fieldSerializer.getPropertyValueDirect(obj));
            } catch (IllegalAccessException e) {
                throw new JSONException("getFieldValue error." + str, e);
            } catch (InvocationTargetException e2) {
                throw new JSONException("getFieldValue error." + str, e2);
            }
        } catch (Exception e3) {
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e3);
        }
    }

    protected void deepSet(Object obj, String str, long j, Object obj2) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(str)) {
                map.get(str);
                map.put(str, obj2);
                return;
            } else {
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    deepSet(it.next(), str, j, obj2);
                }
                return;
            }
        }
        Class<?> cls = obj.getClass();
        JavaBeanDeserializer javaBeanDeserializer = getJavaBeanDeserializer(cls);
        if (javaBeanDeserializer == null) {
            if (obj instanceof List) {
                List list = (List) obj;
                for (int i = 0; i < list.size(); i++) {
                    deepSet(list.get(i), str, j, obj2);
                }
                return;
            }
            return;
        }
        try {
            FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
            if (fieldDeserializer != null) {
                fieldDeserializer.setValue(obj, obj2);
                return;
            }
            Iterator<Object> it2 = getJavaBeanSerializer(cls).getObjectFieldValues(obj).iterator();
            while (it2.hasNext()) {
                deepSet(it2.next(), str, j, obj2);
            }
        } catch (Exception e) {
            throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
        }
    }

    public Object eval(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return objEval;
            }
            objEval = segmentArr[i].eval(this, obj, objEval);
            i++;
        }
    }

    Set<?> evalKeySet(Object obj) {
        JavaBeanSerializer javaBeanSerializer;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map) obj).keySet();
        }
        if ((obj instanceof Collection) || (obj instanceof Object[]) || obj.getClass().isArray() || (javaBeanSerializer = getJavaBeanSerializer(obj.getClass())) == null) {
            return null;
        }
        try {
            return javaBeanSerializer.getFieldNames(obj);
        } catch (Exception e) {
            throw new JSONPathException("evalKeySet error : " + this.path, e);
        }
    }

    int evalSize(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof Map) {
            int i = 0;
            Iterator it = ((Map) obj).values().iterator();
            while (it.hasNext()) {
                if (it.next() != null) {
                    i++;
                }
            }
            return i;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            return -1;
        }
        try {
            return javaBeanSerializer.getSize(obj);
        } catch (Exception e) {
            throw new JSONPathException("evalSize error : " + this.path, e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object extract(com.alibaba.fastjson.parser.DefaultJSONParser r7) {
        /*
            Method dump skipped, instructions count: 326
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.extract(com.alibaba.fastjson.parser.DefaultJSONParser):java.lang.Object");
    }

    protected Object getArrayItem(Object obj, int i) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                if (i < list.size()) {
                    return list.get(i);
                }
                return null;
            }
            if (Math.abs(i) <= list.size()) {
                return list.get(list.size() + i);
            }
            return null;
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    return Array.get(obj, i);
                }
                return null;
            }
            if (Math.abs(i) <= length) {
                return Array.get(obj, length + i);
            }
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object obj2 = map.get(Integer.valueOf(i));
            Object obj3 = obj2;
            if (obj2 == null) {
                obj3 = map.get(Integer.toString(i));
            }
            return obj3;
        }
        if (!(obj instanceof Collection)) {
            throw new UnsupportedOperationException();
        }
        int i2 = 0;
        for (Object obj4 : (Collection) obj) {
            if (i2 == i) {
                return obj4;
            }
            i2++;
        }
        return null;
    }

    protected JavaBeanDeserializer getJavaBeanDeserializer(Class<?> cls) {
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(cls);
        return deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
    }

    protected JavaBeanSerializer getJavaBeanSerializer(Class<?> cls) {
        ObjectSerializer objectWriter = this.serializeConfig.getObjectWriter(cls);
        return objectWriter instanceof JavaBeanSerializer ? (JavaBeanSerializer) objectWriter : null;
    }

    public String getPath() {
        return this.path;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.Object getPropertyValue(java.lang.Object r8, java.lang.String r9, long r10) {
        /*
            Method dump skipped, instructions count: 733
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.getPropertyValue(java.lang.Object, java.lang.String, long):java.lang.Object");
    }

    protected Collection<Object> getPropertyValues(Object obj) {
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            if (obj instanceof Map) {
                return ((Map) obj).values();
            }
            if (obj instanceof Collection) {
                return (Collection) obj;
            }
            throw new UnsupportedOperationException();
        }
        try {
            return javaBeanSerializer.getFieldValues(obj);
        } catch (Exception e) {
            throw new JSONPathException("jsonpath error, path " + this.path, e);
        }
    }

    protected void init() {
        if (this.segments != null) {
            return;
        }
        if ("*".equals(this.path)) {
            this.segments = new Segment[]{WildCardSegment.instance};
            return;
        }
        JSONPathParser jSONPathParser = new JSONPathParser(this.path);
        this.segments = jSONPathParser.explain();
        this.hasRefSegment = jSONPathParser.hasRefSegment;
    }

    public Set<?> keySet(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return evalKeySet(objEval);
            }
            objEval = segmentArr[i].eval(this, obj, objEval);
            i++;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x0144, code lost:
    
        if (r11 != null) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0147, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x014e, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.JSONPath.PropertySegment) == false) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0151, code lost:
    
        r0 = (com.alibaba.fastjson.JSONPath.PropertySegment) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x015c, code lost:
    
        if ((r11 instanceof java.util.Collection) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x015f, code lost:
    
        r0 = r6.segments;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0169, code lost:
    
        if (r0.length <= 1) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x016c, code lost:
    
        r0 = r0[r0.length - 2];
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x017b, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.JSONPath.RangeSegment) != false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0183, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.JSONPath.MultiIndexSegment) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0186, code lost:
    
        r0 = ((java.util.Collection) r11).iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0199, code lost:
    
        if (r0.hasNext() == false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01a8, code lost:
    
        if (r0.remove(r6, r0.next()) == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01ab, code lost:
    
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01b1, code lost:
    
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01b9, code lost:
    
        return r0.remove(r6, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01bf, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegment) == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01cd, code lost:
    
        return ((com.alibaba.fastjson.JSONPath.ArrayAccessSegment) r0).remove(r6, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01d3, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.JSONPath.FilterSegment) == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01e2, code lost:
    
        return ((com.alibaba.fastjson.JSONPath.FilterSegment) r0).remove(r6, r7, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01ef, code lost:
    
        throw new java.lang.UnsupportedOperationException();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean remove(java.lang.Object r7) {
        /*
            Method dump skipped, instructions count: 499
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.remove(java.lang.Object):boolean");
    }

    public boolean removeArrayItem(JSONPath jSONPath, Object obj, int i) {
        if (!(obj instanceof List)) {
            throw new JSONPathException("unsupported set operation." + obj.getClass());
        }
        List list = (List) obj;
        if (i >= 0) {
            if (i >= list.size()) {
                return false;
            }
            list.remove(i);
            return true;
        }
        int size = list.size() + i;
        if (size < 0) {
            return false;
        }
        list.remove(size);
        return true;
    }

    protected boolean removePropertyValue(Object obj, String str, boolean z) {
        boolean z2 = true;
        if (obj instanceof Map) {
            Map map = (Map) obj;
            boolean z3 = map.remove(str) != null;
            if (z) {
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    removePropertyValue(it.next(), str, z);
                }
            }
            return z3;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer == null) {
            if (z) {
                return false;
            }
            throw new UnsupportedOperationException();
        }
        FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
        if (fieldDeserializer != null) {
            fieldDeserializer.setValue(obj, (String) null);
        } else {
            z2 = false;
        }
        if (z) {
            for (Object obj2 : getPropertyValues(obj)) {
                if (obj2 != null) {
                    removePropertyValue(obj2, str, z);
                }
            }
        }
        return z2;
    }

    public boolean set(Object obj, Object obj2) {
        return set(obj, obj2, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean set(java.lang.Object r6, java.lang.Object r7, boolean r8) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 388
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.set(java.lang.Object, java.lang.Object, boolean):boolean");
    }

    public boolean setArrayItem(JSONPath jSONPath, Object obj, int i, Object obj2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                list.set(i, obj2);
                return true;
            }
            list.set(list.size() + i, obj2);
            return true;
        }
        Class<?> cls = obj.getClass();
        if (!cls.isArray()) {
            throw new JSONPathException("unsupported set operation." + cls);
        }
        int length = Array.getLength(obj);
        if (i >= 0) {
            if (i >= length) {
                return true;
            }
            Array.set(obj, i, obj2);
            return true;
        }
        if (Math.abs(i) > length) {
            return true;
        }
        Array.set(obj, length + i, obj2);
        return true;
    }

    protected boolean setPropertyValue(Object obj, String str, long j, Object obj2) {
        if (obj instanceof Map) {
            ((Map) obj).put(str, obj2);
            return true;
        }
        if (obj instanceof List) {
            for (Object obj3 : (List) obj) {
                if (obj3 != null) {
                    setPropertyValue(obj3, str, j, obj2);
                }
            }
            return true;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer == null) {
            throw new UnsupportedOperationException();
        }
        FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(j);
        if (fieldDeserializer == null) {
            return false;
        }
        fieldDeserializer.setValue(obj, obj2);
        return true;
    }

    public int size(Object obj) {
        if (obj == null) {
            return -1;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return evalSize(objEval);
            }
            objEval = segmentArr[i].eval(this, obj, objEval);
            i++;
        }
    }

    @Override // com.alibaba.fastjson.JSONAware
    public String toJSONString() {
        return JSON.toJSONString(this.path);
    }
}
