package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/JavaBeanSerializer.class */
public class JavaBeanSerializer extends SerializeFilterable implements ObjectSerializer {
    protected SerializeBeanInfo beanInfo;
    protected final FieldSerializer[] getters;
    private volatile transient long[] hashArray;
    private volatile transient short[] hashArrayMapping;
    protected final FieldSerializer[] sortedGetters;

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:41:0x0130
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public JavaBeanSerializer(com.alibaba.fastjson.serializer.SerializeBeanInfo r9) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.<init>(com.alibaba.fastjson.serializer.SerializeBeanInfo):void");
    }

    public JavaBeanSerializer(Class<?> cls) {
        this(cls, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> cls, Map<String, String> map) {
        this(TypeUtils.buildBeanInfo(cls, map, null));
    }

    public JavaBeanSerializer(Class<?> cls, String... strArr) {
        this(cls, createAliasMap(strArr));
    }

    static Map<String, String> createAliasMap(String... strArr) {
        HashMap map = new HashMap();
        for (String str : strArr) {
            map.put(str, str);
        }
        return map;
    }

    protected boolean applyLabel(JSONSerializer jSONSerializer, String str) {
        if (jSONSerializer.labelFilters != null) {
            Iterator<LabelFilter> it = jSONSerializer.labelFilters.iterator();
            while (it.hasNext()) {
                if (!it.next().apply(str)) {
                    return false;
                }
            }
        }
        if (this.labelFilters == null) {
            return true;
        }
        Iterator<LabelFilter> it2 = this.labelFilters.iterator();
        while (it2.hasNext()) {
            if (!it2.next().apply(str)) {
                return false;
            }
        }
        return true;
    }

    protected BeanContext getBeanContext(int i) {
        return this.sortedGetters[i].fieldContext;
    }

    public Set<String> getFieldNames(Object obj) throws Exception {
        HashSet hashSet = new HashSet();
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            if (fieldSerializer.getPropertyValueDirect(obj) != null) {
                hashSet.add(fieldSerializer.fieldInfo.name);
            }
        }
        return hashSet;
    }

    public FieldSerializer getFieldSerializer(long j) {
        PropertyNamingStrategy[] propertyNamingStrategyArrValues;
        int iBinarySearch;
        if (this.hashArray == null) {
            propertyNamingStrategyArrValues = PropertyNamingStrategy.values();
            long[] jArr = new long[this.sortedGetters.length * propertyNamingStrategyArrValues.length];
            int i = 0;
            int i2 = 0;
            while (true) {
                FieldSerializer[] fieldSerializerArr = this.sortedGetters;
                if (i >= fieldSerializerArr.length) {
                    break;
                }
                String str = fieldSerializerArr[i].fieldInfo.name;
                jArr[i2] = TypeUtils.fnv1a_64(str);
                i2++;
                for (PropertyNamingStrategy propertyNamingStrategy : propertyNamingStrategyArrValues) {
                    String strTranslate = propertyNamingStrategy.translate(str);
                    if (!str.equals(strTranslate)) {
                        jArr[i2] = TypeUtils.fnv1a_64(strTranslate);
                        i2++;
                    }
                }
                i++;
            }
            Arrays.sort(jArr, 0, i2);
            this.hashArray = new long[i2];
            System.arraycopy(jArr, 0, this.hashArray, 0, i2);
        } else {
            propertyNamingStrategyArrValues = null;
        }
        int iBinarySearch2 = Arrays.binarySearch(this.hashArray, j);
        if (iBinarySearch2 < 0) {
            return null;
        }
        if (this.hashArrayMapping == null) {
            PropertyNamingStrategy[] propertyNamingStrategyArrValues2 = propertyNamingStrategyArrValues;
            if (propertyNamingStrategyArrValues == null) {
                propertyNamingStrategyArrValues2 = PropertyNamingStrategy.values();
            }
            short[] sArr = new short[this.hashArray.length];
            Arrays.fill(sArr, (short) -1);
            int i3 = 0;
            while (true) {
                FieldSerializer[] fieldSerializerArr2 = this.sortedGetters;
                if (i3 >= fieldSerializerArr2.length) {
                    break;
                }
                String str2 = fieldSerializerArr2[i3].fieldInfo.name;
                int iBinarySearch3 = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(str2));
                if (iBinarySearch3 >= 0) {
                    sArr[iBinarySearch3] = (short) i3;
                }
                for (PropertyNamingStrategy propertyNamingStrategy2 : propertyNamingStrategyArrValues2) {
                    String strTranslate2 = propertyNamingStrategy2.translate(str2);
                    if (!str2.equals(strTranslate2) && (iBinarySearch = Arrays.binarySearch(this.hashArray, TypeUtils.fnv1a_64(strTranslate2))) >= 0) {
                        sArr[iBinarySearch] = (short) i3;
                    }
                }
                i3++;
            }
            this.hashArrayMapping = sArr;
        }
        short s = this.hashArrayMapping[iBinarySearch2];
        if (s != -1) {
            return this.sortedGetters[s];
        }
        return null;
    }

    public FieldSerializer getFieldSerializer(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        int length = this.sortedGetters.length - 1;
        while (i <= length) {
            int i2 = (i + length) >>> 1;
            int iCompareTo = this.sortedGetters[i2].fieldInfo.name.compareTo(str);
            if (iCompareTo < 0) {
                i = i2 + 1;
            } else {
                if (iCompareTo <= 0) {
                    return this.sortedGetters[i2];
                }
                length = i2 - 1;
            }
        }
        return null;
    }

    protected Type getFieldType(int i) {
        return this.sortedGetters[i].fieldInfo.fieldType;
    }

    public Object getFieldValue(Object obj, String str) {
        FieldSerializer fieldSerializer = getFieldSerializer(str);
        if (fieldSerializer == null) {
            throw new JSONException("field not found. " + str);
        }
        try {
            return fieldSerializer.getPropertyValue(obj);
        } catch (IllegalAccessException e) {
            throw new JSONException("getFieldValue error." + str, e);
        } catch (InvocationTargetException e2) {
            throw new JSONException("getFieldValue error." + str, e2);
        }
    }

    public Object getFieldValue(Object obj, String str, long j, boolean z) {
        FieldSerializer fieldSerializer = getFieldSerializer(j);
        if (fieldSerializer == null) {
            if (!z) {
                return null;
            }
            throw new JSONException("field not found. " + str);
        }
        try {
            return fieldSerializer.getPropertyValue(obj);
        } catch (IllegalAccessException e) {
            throw new JSONException("getFieldValue error." + str, e);
        } catch (InvocationTargetException e2) {
            throw new JSONException("getFieldValue error." + str, e2);
        }
    }

    public List<Object> getFieldValues(Object obj) throws Exception {
        ArrayList arrayList = new ArrayList(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            arrayList.add(fieldSerializer.getPropertyValue(obj));
        }
        return arrayList;
    }

    public Map<String, Object> getFieldValuesMap(Object obj) throws Exception {
        LinkedHashMap linkedHashMap = new LinkedHashMap(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            boolean zIsEnabled = SerializerFeature.isEnabled(fieldSerializer.features, SerializerFeature.SkipTransientField);
            FieldInfo fieldInfo = fieldSerializer.fieldInfo;
            if (!zIsEnabled || fieldInfo == null || !fieldInfo.fieldTransient) {
                if (fieldSerializer.fieldInfo.unwrapped) {
                    Object json = JSON.toJSON(fieldSerializer.getPropertyValue(obj));
                    if (json instanceof Map) {
                        linkedHashMap.putAll((Map) json);
                    } else {
                        linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
                    }
                } else {
                    linkedHashMap.put(fieldSerializer.fieldInfo.name, fieldSerializer.getPropertyValue(obj));
                }
            }
        }
        return linkedHashMap;
    }

    public List<Object> getObjectFieldValues(Object obj) throws Exception {
        ArrayList arrayList = new ArrayList(this.sortedGetters.length);
        for (FieldSerializer fieldSerializer : this.sortedGetters) {
            Class<?> cls = fieldSerializer.fieldInfo.fieldClass;
            if (!cls.isPrimitive() && !cls.getName().startsWith("java.lang.")) {
                arrayList.add(fieldSerializer.getPropertyValue(obj));
            }
        }
        return arrayList;
    }

    public int getSize(Object obj) throws Exception {
        FieldSerializer[] fieldSerializerArr = this.sortedGetters;
        int length = fieldSerializerArr.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i >= length) {
                return i3;
            }
            int i4 = i3;
            if (fieldSerializerArr[i].getPropertyValueDirect(obj) != null) {
                i4 = i3 + 1;
            }
            i++;
            i2 = i4;
        }
    }

    public Class<?> getType() {
        return this.beanInfo.beanType;
    }

    protected boolean isWriteAsArray(JSONSerializer jSONSerializer) {
        return isWriteAsArray(jSONSerializer, 0);
    }

    protected boolean isWriteAsArray(JSONSerializer jSONSerializer, int i) {
        int i2 = SerializerFeature.BeanToArray.mask;
        return ((this.beanInfo.features & i2) == 0 && !jSONSerializer.out.beanToArray && (i & i2) == 0) ? false : true;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws Throwable {
        write(jSONSerializer, obj, obj2, type, i, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:425:0x0838, code lost:
    
        if (r17 == false) goto L427;
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x083b, code lost:
    
        r16 = ',';
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0842, code lost:
    
        r16 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0845, code lost:
    
        writeAfter(r9, r10, r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0851, code lost:
    
        if (r0.length <= 0) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x085c, code lost:
    
        if (r0.isEnabled(com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat) == false) goto L433;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x085f, code lost:
    
        r9.decrementIdent();
        r9.println();
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x0869, code lost:
    
        if (r14 != false) goto L436;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x086c, code lost:
    
        r0.append(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0875, code lost:
    
        r9.context = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x087a, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x087f, code lost:
    
        r12 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:143:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x03b0  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0405  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x045d  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x04b6 A[Catch: Exception -> 0x0808, Exception -> 0x0808, all -> 0x087b, all -> 0x087b, TRY_ENTER, TRY_LEAVE, TryCatch #9 {Exception -> 0x0808, blocks: (B:156:0x02fa, B:159:0x0312, B:161:0x0324, B:163:0x032e, B:166:0x0347, B:168:0x0357, B:171:0x036b, B:175:0x038b, B:181:0x03a5, B:184:0x03b1, B:187:0x03c0, B:191:0x03e0, B:197:0x03fa, B:201:0x040d, B:203:0x0418, B:207:0x0438, B:213:0x0452, B:216:0x045e, B:217:0x0466, B:219:0x0471, B:223:0x0491, B:229:0x04ab, B:231:0x04b6, B:231:0x04b6, B:236:0x04cb, B:240:0x04d7, B:245:0x04ed, B:247:0x04f5, B:249:0x0504, B:252:0x0517, B:254:0x0524, B:256:0x052c, B:259:0x053a, B:261:0x0542, B:263:0x054a, B:266:0x0558, B:268:0x0560, B:270:0x0568, B:273:0x0576, B:275:0x057e, B:277:0x0586, B:280:0x0596, B:282:0x059e, B:284:0x05a6, B:287:0x05b6, B:289:0x05be, B:291:0x05c6, B:294:0x05d6, B:296:0x05de, B:298:0x05e6, B:303:0x05f9, B:305:0x0601, B:307:0x0609, B:310:0x0619, B:312:0x062b, B:317:0x063e, B:318:0x0649, B:383:0x0791, B:385:0x0797, B:387:0x079f, B:391:0x07b6, B:393:0x07c0, B:395:0x07d0, B:397:0x07d8, B:324:0x065e, B:326:0x0665, B:329:0x0672, B:331:0x0683, B:340:0x06a6, B:348:0x06bd, B:349:0x06d1, B:352:0x06df, B:357:0x06f2, B:361:0x0704, B:363:0x0713, B:366:0x0725, B:367:0x072d, B:368:0x0738, B:370:0x0744, B:371:0x074e, B:372:0x0759, B:374:0x0761, B:376:0x0769, B:380:0x077c, B:381:0x0787), top: B:500:0x02fa }] */
    /* JADX WARN: Removed duplicated region for block: B:301:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x05f9 A[Catch: Exception -> 0x0808, all -> 0x087b, TRY_ENTER, TryCatch #9 {Exception -> 0x0808, blocks: (B:156:0x02fa, B:159:0x0312, B:161:0x0324, B:163:0x032e, B:166:0x0347, B:168:0x0357, B:171:0x036b, B:175:0x038b, B:181:0x03a5, B:184:0x03b1, B:187:0x03c0, B:191:0x03e0, B:197:0x03fa, B:201:0x040d, B:203:0x0418, B:207:0x0438, B:213:0x0452, B:216:0x045e, B:217:0x0466, B:219:0x0471, B:223:0x0491, B:229:0x04ab, B:231:0x04b6, B:231:0x04b6, B:236:0x04cb, B:240:0x04d7, B:245:0x04ed, B:247:0x04f5, B:249:0x0504, B:252:0x0517, B:254:0x0524, B:256:0x052c, B:259:0x053a, B:261:0x0542, B:263:0x054a, B:266:0x0558, B:268:0x0560, B:270:0x0568, B:273:0x0576, B:275:0x057e, B:277:0x0586, B:280:0x0596, B:282:0x059e, B:284:0x05a6, B:287:0x05b6, B:289:0x05be, B:291:0x05c6, B:294:0x05d6, B:296:0x05de, B:298:0x05e6, B:303:0x05f9, B:305:0x0601, B:307:0x0609, B:310:0x0619, B:312:0x062b, B:317:0x063e, B:318:0x0649, B:383:0x0791, B:385:0x0797, B:387:0x079f, B:391:0x07b6, B:393:0x07c0, B:395:0x07d0, B:397:0x07d8, B:324:0x065e, B:326:0x0665, B:329:0x0672, B:331:0x0683, B:340:0x06a6, B:348:0x06bd, B:349:0x06d1, B:352:0x06df, B:357:0x06f2, B:361:0x0704, B:363:0x0713, B:366:0x0725, B:367:0x072d, B:368:0x0738, B:370:0x0744, B:371:0x074e, B:372:0x0759, B:374:0x0761, B:376:0x0769, B:380:0x077c, B:381:0x0787), top: B:500:0x02fa }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:320:0x0652  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0797 A[Catch: Exception -> 0x0808, all -> 0x087b, TryCatch #9 {Exception -> 0x0808, blocks: (B:156:0x02fa, B:159:0x0312, B:161:0x0324, B:163:0x032e, B:166:0x0347, B:168:0x0357, B:171:0x036b, B:175:0x038b, B:181:0x03a5, B:184:0x03b1, B:187:0x03c0, B:191:0x03e0, B:197:0x03fa, B:201:0x040d, B:203:0x0418, B:207:0x0438, B:213:0x0452, B:216:0x045e, B:217:0x0466, B:219:0x0471, B:223:0x0491, B:229:0x04ab, B:231:0x04b6, B:231:0x04b6, B:236:0x04cb, B:240:0x04d7, B:245:0x04ed, B:247:0x04f5, B:249:0x0504, B:252:0x0517, B:254:0x0524, B:256:0x052c, B:259:0x053a, B:261:0x0542, B:263:0x054a, B:266:0x0558, B:268:0x0560, B:270:0x0568, B:273:0x0576, B:275:0x057e, B:277:0x0586, B:280:0x0596, B:282:0x059e, B:284:0x05a6, B:287:0x05b6, B:289:0x05be, B:291:0x05c6, B:294:0x05d6, B:296:0x05de, B:298:0x05e6, B:303:0x05f9, B:305:0x0601, B:307:0x0609, B:310:0x0619, B:312:0x062b, B:317:0x063e, B:318:0x0649, B:383:0x0791, B:385:0x0797, B:387:0x079f, B:391:0x07b6, B:393:0x07c0, B:395:0x07d0, B:397:0x07d8, B:324:0x065e, B:326:0x0665, B:329:0x0672, B:331:0x0683, B:340:0x06a6, B:348:0x06bd, B:349:0x06d1, B:352:0x06df, B:357:0x06f2, B:361:0x0704, B:363:0x0713, B:366:0x0725, B:367:0x072d, B:368:0x0738, B:370:0x0744, B:371:0x074e, B:372:0x0759, B:374:0x0761, B:376:0x0769, B:380:0x077c, B:381:0x0787), top: B:500:0x02fa }] */
    /* JADX WARN: Removed duplicated region for block: B:404:0x07f7  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x07ff  */
    /* JADX WARN: Removed duplicated region for block: B:514:0x0827 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0141  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void write(com.alibaba.fastjson.serializer.JSONSerializer r9, java.lang.Object r10, java.lang.Object r11, java.lang.reflect.Type r12, int r13, boolean r14) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 2514
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JavaBeanSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int, boolean):void");
    }

    protected char writeAfter(JSONSerializer jSONSerializer, Object obj, char c) {
        char cWriteAfter = c;
        if (jSONSerializer.afterFilters != null) {
            Iterator<AfterFilter> it = jSONSerializer.afterFilters.iterator();
            while (true) {
                cWriteAfter = c;
                if (!it.hasNext()) {
                    break;
                }
                c = it.next().writeAfter(jSONSerializer, obj, c);
            }
        }
        char c2 = cWriteAfter;
        if (this.afterFilters != null) {
            Iterator<AfterFilter> it2 = this.afterFilters.iterator();
            while (true) {
                c2 = cWriteAfter;
                if (!it2.hasNext()) {
                    break;
                }
                cWriteAfter = it2.next().writeAfter(jSONSerializer, obj, cWriteAfter);
            }
        }
        return c2;
    }

    public void writeAsArray(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws Throwable {
        write(jSONSerializer, obj, obj2, type, i);
    }

    public void writeAsArrayNonContext(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws Throwable {
        write(jSONSerializer, obj, obj2, type, i);
    }

    protected char writeBefore(JSONSerializer jSONSerializer, Object obj, char c) {
        char cWriteBefore = c;
        if (jSONSerializer.beforeFilters != null) {
            Iterator<BeforeFilter> it = jSONSerializer.beforeFilters.iterator();
            while (true) {
                cWriteBefore = c;
                if (!it.hasNext()) {
                    break;
                }
                c = it.next().writeBefore(jSONSerializer, obj, c);
            }
        }
        char c2 = cWriteBefore;
        if (this.beforeFilters != null) {
            Iterator<BeforeFilter> it2 = this.beforeFilters.iterator();
            while (true) {
                c2 = cWriteBefore;
                if (!it2.hasNext()) {
                    break;
                }
                cWriteBefore = it2.next().writeBefore(jSONSerializer, obj, cWriteBefore);
            }
        }
        return c2;
    }

    protected void writeClassName(JSONSerializer jSONSerializer, String str, Object obj) {
        String str2 = str;
        if (str == null) {
            str2 = jSONSerializer.config.typeKey;
        }
        jSONSerializer.out.writeFieldName(str2, false);
        String str3 = this.beanInfo.typeName;
        String name = str3;
        if (str3 == null) {
            Class<?> cls = obj.getClass();
            Class<?> superclass = cls;
            if (TypeUtils.isProxy(cls)) {
                superclass = cls.getSuperclass();
            }
            name = superclass.getName();
        }
        jSONSerializer.write(name);
    }

    public void writeDirectNonContext(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws Throwable {
        write(jSONSerializer, obj, obj2, type, i);
    }

    public void writeNoneASM(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws Throwable {
        write(jSONSerializer, obj, obj2, type, i, false);
    }

    public boolean writeReference(JSONSerializer jSONSerializer, Object obj, int i) {
        SerialContext serialContext = jSONSerializer.context;
        int i2 = SerializerFeature.DisableCircularReferenceDetect.mask;
        if (serialContext == null || (serialContext.features & i2) != 0 || (i & i2) != 0 || jSONSerializer.references == null || !jSONSerializer.references.containsKey(obj)) {
            return false;
        }
        jSONSerializer.writeReference(obj);
        return true;
    }
}
