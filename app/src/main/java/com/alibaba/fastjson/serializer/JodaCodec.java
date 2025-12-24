package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/JodaCodec.class */
public class JodaCodec implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {
    private static final String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final JodaCodec instance = new JodaCodec();
    private static final String defaultPatttern = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter defaultFormatter = DateTimeFormat.forPattern(defaultPatttern);
    private static final DateTimeFormatter defaultFormatter_23 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter formatter_dt19_tw = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn = DateTimeFormat.forPattern("yyyy年M月d日 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormat.forPattern("yyyy年M月d日 H时m分s秒");
    private static final DateTimeFormatter formatter_dt19_kr = DateTimeFormat.forPattern("yyyy년M월d일 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_us = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_eur = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_de = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_in = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_d8 = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter formatter_d10_tw = DateTimeFormat.forPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatter_d10_cn = DateTimeFormat.forPattern("yyyy年M月d日");
    private static final DateTimeFormatter formatter_d10_kr = DateTimeFormat.forPattern("yyyy년M월d일");
    private static final DateTimeFormatter formatter_d10_us = DateTimeFormat.forPattern("MM/dd/yyyy");
    private static final DateTimeFormatter formatter_d10_eur = DateTimeFormat.forPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatter_d10_de = DateTimeFormat.forPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatter_d10_in = DateTimeFormat.forPattern("dd-MM-yyyy");
    private static final DateTimeFormatter ISO_FIXED_FORMAT = DateTimeFormat.forPattern(defaultPatttern).withZone(DateTimeZone.getDefault());
    private static final String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter formatter_iso8601 = DateTimeFormat.forPattern(formatter_iso8601_pattern);

    private void write(SerializeWriter serializeWriter, ReadablePartial readablePartial, String str) {
        serializeWriter.writeString((str.equals(formatter_iso8601_pattern) ? formatter_iso8601 : DateTimeFormat.forPattern(str)).print(readablePartial));
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser, type, obj, null, 0);
    }

    /* JADX WARN: Type inference failed for: r0v16, types: [T, org.joda.time.LocalDateTime] */
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            return null;
        }
        if (jSONLexer.token() != 4) {
            if (jSONLexer.token() != 2) {
                throw new UnsupportedOperationException();
            }
            long jLongValue = jSONLexer.longValue();
            jSONLexer.nextToken();
            TimeZone timeZone = JSON.defaultTimeZone;
            TimeZone timeZone2 = timeZone;
            if (timeZone == null) {
                timeZone2 = TimeZone.getDefault();
            }
            if (type == DateTime.class) {
                return (T) new DateTime(jLongValue, DateTimeZone.forTimeZone(timeZone2));
            }
            ?? r0 = (T) new LocalDateTime(jLongValue, DateTimeZone.forTimeZone(timeZone2));
            if (type == LocalDateTime.class) {
                return r0;
            }
            if (type == LocalDate.class) {
                return (T) r0.toLocalDate();
            }
            if (type == LocalTime.class) {
                return (T) r0.toLocalTime();
            }
            if (type == Instant.class) {
                return (T) new Instant(jLongValue);
            }
            throw new UnsupportedOperationException();
        }
        String strStringVal = jSONLexer.stringVal();
        jSONLexer.nextToken();
        DateTimeFormatter dateTimeFormatterForPattern = str != null ? defaultPatttern.equals(str) ? defaultFormatter : DateTimeFormat.forPattern(str) : null;
        if (BuildConfig.FLAVOR.equals(strStringVal)) {
            return null;
        }
        if (type == LocalDateTime.class) {
            return (T) ((strStringVal.length() == 10 || strStringVal.length() == 8) ? parseLocalDate(strStringVal, str, dateTimeFormatterForPattern).toLocalDateTime(LocalTime.MIDNIGHT) : parseDateTime(strStringVal, dateTimeFormatterForPattern));
        }
        if (type == LocalDate.class) {
            return (T) (strStringVal.length() == 23 ? LocalDateTime.parse(strStringVal).toLocalDate() : parseLocalDate(strStringVal, str, dateTimeFormatterForPattern));
        }
        if (type == LocalTime.class) {
            return (T) (strStringVal.length() == 23 ? LocalDateTime.parse(strStringVal).toLocalTime() : LocalTime.parse(strStringVal));
        }
        if (type == DateTime.class) {
            DateTimeFormatter dateTimeFormatter = dateTimeFormatterForPattern;
            if (dateTimeFormatterForPattern == defaultFormatter) {
                dateTimeFormatter = ISO_FIXED_FORMAT;
            }
            return (T) parseZonedDateTime(strStringVal, dateTimeFormatter);
        }
        if (type == DateTimeZone.class) {
            return (T) DateTimeZone.forID(strStringVal);
        }
        if (type == Period.class) {
            return (T) Period.parse(strStringVal);
        }
        if (type == Duration.class) {
            return (T) Duration.parse(strStringVal);
        }
        if (type == Instant.class) {
            return (T) Instant.parse(strStringVal);
        }
        if (type == DateTimeFormatter.class) {
            return (T) DateTimeFormat.forPattern(strStringVal);
        }
        return null;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 4;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0127  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected org.joda.time.LocalDateTime parseDateTime(java.lang.String r5, org.joda.time.format.DateTimeFormatter r6) {
        /*
            Method dump skipped, instructions count: 571
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JodaCodec.parseDateTime(java.lang.String, org.joda.time.format.DateTimeFormatter):org.joda.time.LocalDateTime");
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected org.joda.time.LocalDate parseLocalDate(java.lang.String r5, java.lang.String r6, org.joda.time.format.DateTimeFormatter r7) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JodaCodec.parseLocalDate(java.lang.String, java.lang.String, org.joda.time.format.DateTimeFormatter):org.joda.time.LocalDate");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x012a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected org.joda.time.DateTime parseZonedDateTime(java.lang.String r5, org.joda.time.format.DateTimeFormatter r6) {
        /*
            Method dump skipped, instructions count: 449
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.JodaCodec.parseZonedDateTime(java.lang.String, org.joda.time.format.DateTimeFormatter):org.joda.time.DateTime");
    }

    @Override // com.alibaba.fastjson.serializer.ContextObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, BeanContext beanContext) throws IOException {
        write(jSONSerializer.out, (ReadablePartial) obj, beanContext.getFormat());
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        Type type2 = type;
        if (type == null) {
            type2 = obj.getClass();
        }
        if (type2 != LocalDateTime.class) {
            serializeWriter.writeString(obj.toString());
            return;
        }
        int mask = SerializerFeature.UseISO8601DateFormat.getMask();
        LocalDateTime localDateTime = (LocalDateTime) obj;
        String dateFormatPattern = jSONSerializer.getDateFormatPattern();
        String str = dateFormatPattern;
        if (dateFormatPattern == null) {
            str = ((mask & i) != 0 || jSONSerializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) ? formatter_iso8601_pattern : localDateTime.getMillisOfSecond() == 0 ? formatter_iso8601_pattern_23 : formatter_iso8601_pattern_29;
        }
        if (str != null) {
            write(serializeWriter, (ReadablePartial) localDateTime, str);
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
            write(serializeWriter, (ReadablePartial) localDateTime, JSON.DEFFAULT_DATE_FORMAT);
        } else {
            serializeWriter.writeLong(localDateTime.toDateTime(DateTimeZone.forTimeZone(JSON.defaultTimeZone)).toInstant().getMillis());
        }
    }
}
