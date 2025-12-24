package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/FieldSerializer.class */
public class FieldSerializer implements Comparable<FieldSerializer> {
    protected boolean browserCompatible;
    protected boolean disableCircularReferenceDetect;
    private final String double_quoted_fieldPrefix;
    protected int features;
    protected BeanContext fieldContext;
    public final FieldInfo fieldInfo;
    private String format;
    protected boolean persistenceXToMany;
    private RuntimeSerializerInfo runtimeInfo;
    protected boolean serializeUsing = false;
    private String single_quoted_fieldPrefix;
    private String un_quoted_fieldPrefix;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected final boolean writeNull;

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/FieldSerializer$RuntimeSerializerInfo.class */
    static class RuntimeSerializerInfo {
        final ObjectSerializer fieldSerializer;
        final Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer objectSerializer, Class<?> cls) {
            this.fieldSerializer = objectSerializer;
            this.runtimeFieldClass = cls;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x01ed  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public FieldSerializer(java.lang.Class<?> r7, com.alibaba.fastjson.util.FieldInfo r8) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 503
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.FieldSerializer.<init>(java.lang.Class, com.alibaba.fastjson.util.FieldInfo):void");
    }

    @Override // java.lang.Comparable
    public int compareTo(FieldSerializer fieldSerializer) {
        return this.fieldInfo.compareTo(fieldSerializer.fieldInfo);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object getPropertyValue(java.lang.Object r6) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        /*
            r5 = this;
            r0 = r5
            com.alibaba.fastjson.util.FieldInfo r0 = r0.fieldInfo
            r1 = r6
            java.lang.Object r0 = r0.get(r1)
            r7 = r0
            r0 = r7
            r6 = r0
            r0 = r5
            java.lang.String r0 = r0.format
            if (r0 == 0) goto L4e
            r0 = r7
            r6 = r0
            r0 = r7
            if (r0 == 0) goto L4e
            r0 = r5
            com.alibaba.fastjson.util.FieldInfo r0 = r0.fieldInfo
            java.lang.Class<?> r0 = r0.fieldClass
            java.lang.Class<java.util.Date> r1 = java.util.Date.class
            if (r0 == r1) goto L32
            r0 = r7
            r6 = r0
            r0 = r5
            com.alibaba.fastjson.util.FieldInfo r0 = r0.fieldInfo
            java.lang.Class<?> r0 = r0.fieldClass
            java.lang.Class<java.sql.Date> r1 = java.sql.Date.class
            if (r0 != r1) goto L4e
        L32:
            java.text.SimpleDateFormat r0 = new java.text.SimpleDateFormat
            r1 = r0
            r2 = r5
            java.lang.String r2 = r2.format
            java.util.Locale r3 = com.alibaba.fastjson.JSON.defaultLocale
            r1.<init>(r2, r3)
            r6 = r0
            r0 = r6
            java.util.TimeZone r1 = com.alibaba.fastjson.JSON.defaultTimeZone
            r0.setTimeZone(r1)
            r0 = r6
            r1 = r7
            java.lang.String r0 = r0.format(r1)
            r6 = r0
        L4e:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.FieldSerializer.getPropertyValue(java.lang.Object):java.lang.Object");
    }

    public Object getPropertyValueDirect(Object obj) throws IllegalAccessException, InvocationTargetException {
        Object obj2 = this.fieldInfo.get(obj);
        Object obj3 = obj2;
        if (this.persistenceXToMany) {
            obj3 = obj2;
            if (!TypeUtils.isHibernateInitialized(obj2)) {
                obj3 = null;
            }
        }
        return obj3;
    }

    public void writePrefix(JSONSerializer jSONSerializer) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (!serializeWriter.quoteFieldNames) {
            if (this.un_quoted_fieldPrefix == null) {
                this.un_quoted_fieldPrefix = this.fieldInfo.name + ":";
            }
            serializeWriter.write(this.un_quoted_fieldPrefix);
            return;
        }
        if (!SerializerFeature.isEnabled(serializeWriter.features, this.fieldInfo.serialzeFeatures, SerializerFeature.UseSingleQuotes)) {
            serializeWriter.write(this.double_quoted_fieldPrefix);
            return;
        }
        if (this.single_quoted_fieldPrefix == null) {
            this.single_quoted_fieldPrefix = '\'' + this.fieldInfo.name + "':";
        }
        serializeWriter.write(this.single_quoted_fieldPrefix);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00f4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeValue(com.alibaba.fastjson.serializer.JSONSerializer r9, java.lang.Object r10) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 951
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.FieldSerializer.writeValue(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object):void");
    }
}
