package com.alibaba.fastjson.serializer;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Found several "values" enum fields: [] */
/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/SerializerFeature.class */
public final class SerializerFeature {
    private static final SerializerFeature[] $VALUES;
    public static final SerializerFeature[] EMPTY;
    public static final SerializerFeature MapSortField;
    public static final int WRITE_MAP_NULL_FEATURES;
    public final int mask = 1 << ordinal();
    public static final SerializerFeature QuoteFieldNames = new SerializerFeature("QuoteFieldNames", 0);
    public static final SerializerFeature UseSingleQuotes = new SerializerFeature("UseSingleQuotes", 1);
    public static final SerializerFeature WriteMapNullValue = new SerializerFeature("WriteMapNullValue", 2);
    public static final SerializerFeature WriteEnumUsingToString = new SerializerFeature("WriteEnumUsingToString", 3);
    public static final SerializerFeature WriteEnumUsingName = new SerializerFeature("WriteEnumUsingName", 4);
    public static final SerializerFeature UseISO8601DateFormat = new SerializerFeature("UseISO8601DateFormat", 5);
    public static final SerializerFeature WriteNullListAsEmpty = new SerializerFeature("WriteNullListAsEmpty", 6);
    public static final SerializerFeature WriteNullStringAsEmpty = new SerializerFeature("WriteNullStringAsEmpty", 7);
    public static final SerializerFeature WriteNullNumberAsZero = new SerializerFeature("WriteNullNumberAsZero", 8);
    public static final SerializerFeature WriteNullBooleanAsFalse = new SerializerFeature("WriteNullBooleanAsFalse", 9);
    public static final SerializerFeature SkipTransientField = new SerializerFeature("SkipTransientField", 10);
    public static final SerializerFeature SortField = new SerializerFeature("SortField", 11);

    @Deprecated
    public static final SerializerFeature WriteTabAsSpecial = new SerializerFeature("WriteTabAsSpecial", 12);
    public static final SerializerFeature PrettyFormat = new SerializerFeature("PrettyFormat", 13);
    public static final SerializerFeature WriteClassName = new SerializerFeature("WriteClassName", 14);
    public static final SerializerFeature DisableCircularReferenceDetect = new SerializerFeature("DisableCircularReferenceDetect", 15);
    public static final SerializerFeature WriteSlashAsSpecial = new SerializerFeature("WriteSlashAsSpecial", 16);
    public static final SerializerFeature BrowserCompatible = new SerializerFeature("BrowserCompatible", 17);
    public static final SerializerFeature WriteDateUseDateFormat = new SerializerFeature("WriteDateUseDateFormat", 18);
    public static final SerializerFeature NotWriteRootClassName = new SerializerFeature("NotWriteRootClassName", 19);
    public static final SerializerFeature DisableCheckSpecialChar = new SerializerFeature("DisableCheckSpecialChar", 20);
    public static final SerializerFeature BeanToArray = new SerializerFeature("BeanToArray", 21);
    public static final SerializerFeature WriteNonStringKeyAsString = new SerializerFeature("WriteNonStringKeyAsString", 22);
    public static final SerializerFeature NotWriteDefaultValue = new SerializerFeature("NotWriteDefaultValue", 23);
    public static final SerializerFeature BrowserSecure = new SerializerFeature("BrowserSecure", 24);
    public static final SerializerFeature IgnoreNonFieldGetter = new SerializerFeature("IgnoreNonFieldGetter", 25);
    public static final SerializerFeature WriteNonStringValueAsString = new SerializerFeature("WriteNonStringValueAsString", 26);
    public static final SerializerFeature IgnoreErrorGetter = new SerializerFeature("IgnoreErrorGetter", 27);
    public static final SerializerFeature WriteBigDecimalAsPlain = new SerializerFeature("WriteBigDecimalAsPlain", 28);

    static {
        SerializerFeature serializerFeature = new SerializerFeature("MapSortField", 29);
        MapSortField = serializerFeature;
        SerializerFeature serializerFeature2 = QuoteFieldNames;
        SerializerFeature serializerFeature3 = UseSingleQuotes;
        SerializerFeature serializerFeature4 = WriteMapNullValue;
        $VALUES = new SerializerFeature[]{serializerFeature2, serializerFeature3, serializerFeature4, WriteEnumUsingToString, WriteEnumUsingName, UseISO8601DateFormat, WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero, WriteNullBooleanAsFalse, SkipTransientField, SortField, WriteTabAsSpecial, PrettyFormat, WriteClassName, DisableCircularReferenceDetect, WriteSlashAsSpecial, BrowserCompatible, WriteDateUseDateFormat, NotWriteRootClassName, DisableCheckSpecialChar, BeanToArray, WriteNonStringKeyAsString, NotWriteDefaultValue, BrowserSecure, IgnoreNonFieldGetter, WriteNonStringValueAsString, IgnoreErrorGetter, WriteBigDecimalAsPlain, serializerFeature};
        EMPTY = new SerializerFeature[0];
        WRITE_MAP_NULL_FEATURES = serializerFeature4.getMask() | WriteNullBooleanAsFalse.getMask() | WriteNullListAsEmpty.getMask() | WriteNullNumberAsZero.getMask() | WriteNullStringAsEmpty.getMask();
    }

    private SerializerFeature(String str, int i) {
    }

    public static int config(int i, SerializerFeature serializerFeature, boolean z) {
        return z ? i | serializerFeature.mask : i & (serializerFeature.mask ^ (-1));
    }

    public static boolean isEnabled(int i, int i2, SerializerFeature serializerFeature) {
        int i3 = serializerFeature.mask;
        return ((i & i3) == 0 && (i2 & i3) == 0) ? false : true;
    }

    public static boolean isEnabled(int i, SerializerFeature serializerFeature) {
        return (i & serializerFeature.mask) != 0;
    }

    /* renamed from: of */
    public static int m12of(SerializerFeature[] serializerFeatureArr) {
        if (serializerFeatureArr == null) {
            return 0;
        }
        int i = 0;
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.mask;
        }
        return i;
    }

    public static SerializerFeature valueOf(String str) {
        return (SerializerFeature) Enum.valueOf(SerializerFeature.class, str);
    }

    public static SerializerFeature[] values() {
        return (SerializerFeature[]) $VALUES.clone();
    }

    public final int getMask() {
        return this.mask;
    }
}
