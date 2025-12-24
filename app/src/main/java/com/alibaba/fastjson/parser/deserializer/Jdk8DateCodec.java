package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/Jdk8DateCodec.class */
public class Jdk8DateCodec extends ContextObjectDeserializer implements ObjectSerializer, ContextObjectSerializer, ObjectDeserializer {
    private static final String formatter_iso8601_pattern_23 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String formatter_iso8601_pattern_29 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final Jdk8DateCodec instance = new Jdk8DateCodec();
    private static final String defaultPatttern = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(defaultPatttern);
    private static final DateTimeFormatter defaultFormatter_23 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter formatter_dt19_tw = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_cn_1 = DateTimeFormatter.ofPattern("yyyy年M月d日 H时m分s秒");
    private static final DateTimeFormatter formatter_dt19_kr = DateTimeFormatter.ofPattern("yyyy년M월d일 HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_us = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_eur = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_de = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_dt19_in = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter formatter_d8 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter formatter_d10_tw = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatter_d10_cn = DateTimeFormatter.ofPattern("yyyy年M月d日");
    private static final DateTimeFormatter formatter_d10_kr = DateTimeFormatter.ofPattern("yyyy년M월d일");
    private static final DateTimeFormatter formatter_d10_us = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter formatter_d10_eur = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatter_d10_de = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatter_d10_in = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter ISO_FIXED_FORMAT = DateTimeFormatter.ofPattern(defaultPatttern).withZone(ZoneId.systemDefault());
    private static final String formatter_iso8601_pattern = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter formatter_iso8601 = DateTimeFormatter.ofPattern(formatter_iso8601_pattern);

    /* JADX WARN: Type inference failed for: r0v16, types: [java.time.ZonedDateTime] */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.time.ZonedDateTime] */
    private void write(SerializeWriter serializeWriter, TemporalAccessor temporalAccessor, String str) throws IOException {
        if ("unixtime".equals(str)) {
            if (temporalAccessor instanceof ChronoZonedDateTime) {
                serializeWriter.writeInt((int) ((ChronoZonedDateTime) temporalAccessor).toEpochSecond());
                return;
            } else if (temporalAccessor instanceof LocalDateTime) {
                serializeWriter.writeInt((int) ((LocalDateTime) temporalAccessor).atZone(JSON.defaultTimeZone.toZoneId()).toEpochSecond());
                return;
            }
        }
        if ("millis".equals(str)) {
            Instant instant = null;
            if (temporalAccessor instanceof ChronoZonedDateTime) {
                instant = ((ChronoZonedDateTime) temporalAccessor).toInstant();
            } else if (temporalAccessor instanceof LocalDateTime) {
                instant = ((LocalDateTime) temporalAccessor).atZone(JSON.defaultTimeZone.toZoneId()).toInstant();
            }
            if (instant != null) {
                serializeWriter.writeLong(instant.toEpochMilli());
                return;
            }
        }
        serializeWriter.writeString((str == formatter_iso8601_pattern ? formatter_iso8601 : DateTimeFormatter.ofPattern(str)).format(temporalAccessor));
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        long j;
        LocalTime localTimeOf;
        LocalDate localDate;
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
            if ("unixtime".equals(str)) {
                j = jLongValue * 1000;
            } else {
                j = jLongValue;
                if ("yyyyMMddHHmmss".equals(str)) {
                    int i2 = (int) (jLongValue / 10000000000L);
                    int i3 = (int) ((jLongValue / 100000000) % 100);
                    int i4 = (int) ((jLongValue / 1000000) % 100);
                    int i5 = (int) ((jLongValue / 10000) % 100);
                    int i6 = (int) ((jLongValue / 100) % 100);
                    int i7 = (int) (jLongValue % 100);
                    j = jLongValue;
                    if (type == LocalDateTime.class) {
                        return (T) LocalDateTime.of(i2, i3, i4, i5, i6, i7);
                    }
                }
            }
            if (type == LocalDateTime.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(j), JSON.defaultTimeZone.toZoneId());
            }
            if (type == LocalDate.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(j), JSON.defaultTimeZone.toZoneId()).toLocalDate();
            }
            if (type == LocalTime.class) {
                return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(j), JSON.defaultTimeZone.toZoneId()).toLocalTime();
            }
            if (type == ZonedDateTime.class) {
                return (T) ZonedDateTime.ofInstant(Instant.ofEpochMilli(j), JSON.defaultTimeZone.toZoneId());
            }
            throw new UnsupportedOperationException();
        }
        String strStringVal = jSONLexer.stringVal();
        jSONLexer.nextToken();
        DateTimeFormatter dateTimeFormatterOfPattern = str != null ? defaultPatttern.equals(str) ? defaultFormatter : DateTimeFormatter.ofPattern(str) : null;
        if (BuildConfig.FLAVOR.equals(strStringVal)) {
            return null;
        }
        if (type == LocalDateTime.class) {
            return (T) ((strStringVal.length() == 10 || strStringVal.length() == 8) ? LocalDateTime.of(parseLocalDate(strStringVal, str, dateTimeFormatterOfPattern), LocalTime.MIN) : parseDateTime(strStringVal, dateTimeFormatterOfPattern));
        }
        if (type == LocalDate.class) {
            if (strStringVal.length() == 23) {
                LocalDateTime localDateTime = LocalDateTime.parse(strStringVal);
                localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
            } else {
                localDate = parseLocalDate(strStringVal, str, dateTimeFormatterOfPattern);
            }
            return (T) localDate;
        }
        if (type == LocalTime.class) {
            if (strStringVal.length() == 23) {
                LocalDateTime localDateTime2 = LocalDateTime.parse(strStringVal);
                localTimeOf = LocalTime.of(localDateTime2.getHour(), localDateTime2.getMinute(), localDateTime2.getSecond(), localDateTime2.getNano());
            } else {
                localTimeOf = LocalTime.parse(strStringVal);
            }
            return (T) localTimeOf;
        }
        if (type == ZonedDateTime.class) {
            DateTimeFormatter dateTimeFormatter = dateTimeFormatterOfPattern;
            if (dateTimeFormatterOfPattern == defaultFormatter) {
                dateTimeFormatter = ISO_FIXED_FORMAT;
            }
            if (dateTimeFormatter == null && strStringVal.length() <= 19) {
                JSONScanner jSONScanner = new JSONScanner(strStringVal);
                TimeZone timeZone = defaultJSONParser.lexer.getTimeZone();
                jSONScanner.setTimeZone(timeZone);
                if (jSONScanner.scanISO8601DateIfMatch(false)) {
                    return (T) ZonedDateTime.ofInstant(jSONScanner.getCalendar().getTime().toInstant(), timeZone.toZoneId());
                }
            }
            return (T) parseZonedDateTime(strStringVal, dateTimeFormatter);
        }
        if (type == OffsetDateTime.class) {
            return (T) OffsetDateTime.parse(strStringVal);
        }
        if (type == OffsetTime.class) {
            return (T) OffsetTime.parse(strStringVal);
        }
        if (type == ZoneId.class) {
            return (T) ZoneId.of(strStringVal);
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
        return null;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 4;
    }

    protected LocalDateTime parseDateTime(String str, DateTimeFormatter dateTimeFormatter) {
        DateTimeFormatter dateTimeFormatter2;
        if (dateTimeFormatter == null) {
            if (str.length() == 19) {
                char cCharAt = str.charAt(4);
                char cCharAt2 = str.charAt(7);
                char cCharAt3 = str.charAt(10);
                char cCharAt4 = str.charAt(13);
                char cCharAt5 = str.charAt(16);
                if (cCharAt4 == ':' && cCharAt5 == ':') {
                    if (cCharAt == '-' && cCharAt2 == '-') {
                        if (cCharAt3 == 'T') {
                            dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        } else if (cCharAt3 == ' ') {
                            dateTimeFormatter = defaultFormatter;
                        }
                    } else if (cCharAt == '/' && cCharAt2 == '/') {
                        dateTimeFormatter = formatter_dt19_tw;
                    } else {
                        char cCharAt6 = str.charAt(0);
                        char cCharAt7 = str.charAt(1);
                        char cCharAt8 = str.charAt(2);
                        char cCharAt9 = str.charAt(3);
                        char cCharAt10 = str.charAt(5);
                        if (cCharAt8 == '/' && cCharAt10 == '/') {
                            if (((cCharAt6 - '0') * 10) + (cCharAt7 - '0') > 12) {
                                dateTimeFormatter = formatter_dt19_eur;
                            } else if (((cCharAt9 - '0') * 10) + (cCharAt - '0') > 12) {
                                dateTimeFormatter = formatter_dt19_us;
                            } else {
                                String country = Locale.getDefault().getCountry();
                                if (country.equals("US")) {
                                    dateTimeFormatter = formatter_dt19_us;
                                } else if (country.equals("BR") || country.equals("AU")) {
                                    dateTimeFormatter = formatter_dt19_eur;
                                }
                            }
                        } else if (cCharAt8 == '.' && cCharAt10 == '.') {
                            dateTimeFormatter = formatter_dt19_de;
                        } else if (cCharAt8 == '-' && cCharAt10 == '-') {
                            dateTimeFormatter = formatter_dt19_in;
                        }
                    }
                }
            } else if (str.length() == 23) {
                char cCharAt11 = str.charAt(4);
                char cCharAt12 = str.charAt(7);
                char cCharAt13 = str.charAt(10);
                char cCharAt14 = str.charAt(13);
                char cCharAt15 = str.charAt(16);
                char cCharAt16 = str.charAt(19);
                if (cCharAt14 == ':' && cCharAt15 == ':' && cCharAt11 == '-' && cCharAt12 == '-' && cCharAt13 == ' ' && cCharAt16 == '.') {
                    dateTimeFormatter = defaultFormatter_23;
                }
            }
            dateTimeFormatter2 = dateTimeFormatter;
            if (str.length() >= 17) {
                char cCharAt17 = str.charAt(4);
                if (cCharAt17 == 24180) {
                    dateTimeFormatter2 = str.charAt(str.length() - 1) == 31186 ? formatter_dt19_cn_1 : formatter_dt19_cn;
                } else {
                    dateTimeFormatter2 = dateTimeFormatter;
                    if (cCharAt17 == 45380) {
                        dateTimeFormatter2 = formatter_dt19_kr;
                    }
                }
            }
        } else {
            dateTimeFormatter2 = dateTimeFormatter;
        }
        if (dateTimeFormatter2 == null) {
            JSONScanner jSONScanner = new JSONScanner(str);
            if (jSONScanner.scanISO8601DateIfMatch(false)) {
                return LocalDateTime.ofInstant(jSONScanner.getCalendar().toInstant(), ZoneId.systemDefault());
            }
        }
        return dateTimeFormatter2 == null ? LocalDateTime.parse(str) : LocalDateTime.parse(str, dateTimeFormatter2);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.time.LocalDate parseLocalDate(java.lang.String r5, java.lang.String r6, java.time.format.DateTimeFormatter r7) {
        /*
            Method dump skipped, instructions count: 347
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.parseLocalDate(java.lang.String, java.lang.String, java.time.format.DateTimeFormatter):java.time.LocalDate");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x012a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.time.ZonedDateTime parseZonedDateTime(java.lang.String r5, java.time.format.DateTimeFormatter r6) {
        /*
            Method dump skipped, instructions count: 449
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec.parseZonedDateTime(java.lang.String, java.time.format.DateTimeFormatter):java.time.ZonedDateTime");
    }

    @Override // com.alibaba.fastjson.serializer.ContextObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, BeanContext beanContext) throws IOException {
        write(jSONSerializer.out, (TemporalAccessor) obj, beanContext.getFormat());
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [java.time.ZonedDateTime] */
    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        int nano;
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
            str = ((mask & i) != 0 || jSONSerializer.isEnabled(SerializerFeature.UseISO8601DateFormat) || (nano = localDateTime.getNano()) == 0) ? formatter_iso8601_pattern : nano % 1000000 == 0 ? formatter_iso8601_pattern_23 : formatter_iso8601_pattern_29;
        }
        if (str != null) {
            write(serializeWriter, localDateTime, str);
        } else if (serializeWriter.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
            write(serializeWriter, localDateTime, JSON.DEFFAULT_DATE_FORMAT);
        } else {
            serializeWriter.writeLong(localDateTime.atZone(JSON.defaultTimeZone.toZoneId()).toInstant().toEpochMilli());
        }
    }
}
