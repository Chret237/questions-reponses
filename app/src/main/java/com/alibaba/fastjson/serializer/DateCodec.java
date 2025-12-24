package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/DateCodec.class */
public class DateCodec extends AbstractDateDeserializer implements ObjectSerializer, ObjectDeserializer {
    public static final DateCodec instance = new DateCodec();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v43, types: [java.util.Date] */
    /* JADX WARN: Type inference failed for: r0v56, types: [T, java.util.Calendar] */
    /* JADX WARN: Type inference failed for: r0v77, types: [T, java.util.Calendar] */
    /* JADX WARN: Type inference failed for: r10v0, types: [T, java.lang.Object] */
    @Override // com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer
    public <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        if (obj2 == 0) {
            return null;
        }
        if (obj2 instanceof Date) {
            return obj2;
        }
        if (obj2 instanceof BigDecimal) {
            return (T) new Date(TypeUtils.longValue((BigDecimal) obj2));
        }
        if (obj2 instanceof Number) {
            return (T) new Date(((Number) obj2).longValue());
        }
        if (!(obj2 instanceof String)) {
            throw new JSONException("parse error");
        }
        String str = (String) obj2;
        if (str.length() == 0) {
            return null;
        }
        JSONScanner jSONScanner = new JSONScanner(str);
        try {
            if (jSONScanner.scanISO8601DateIfMatch(false)) {
                ?? r0 = (T) jSONScanner.getCalendar();
                return type == Calendar.class ? r0 : (T) r0.getTime();
            }
            jSONScanner.close();
            if (str.length() == defaultJSONParser.getDateFomartPattern().length() || (str.length() == 22 && defaultJSONParser.getDateFomartPattern().equals("yyyyMMddHHmmssSSSZ"))) {
                try {
                    return (T) defaultJSONParser.getDateFormat().parse(str);
                } catch (ParseException e) {
                }
            }
            String strSubstring = str;
            if (str.startsWith("/Date(")) {
                strSubstring = str;
                if (str.endsWith(")/")) {
                    strSubstring = str.substring(6, str.length() - 2);
                }
            }
            T date = null;
            if (!"0000-00-00".equals(strSubstring)) {
                date = null;
                if (!"0000-00-00T00:00:00".equalsIgnoreCase(strSubstring)) {
                    if ("0001-01-01T00:00:00+08:00".equalsIgnoreCase(strSubstring)) {
                        date = null;
                    } else {
                        int iLastIndexOf = strSubstring.lastIndexOf(124);
                        if (iLastIndexOf > 20) {
                            TimeZone timeZone = TimeZone.getTimeZone(strSubstring.substring(iLastIndexOf + 1));
                            if (!"GMT".equals(timeZone.getID())) {
                                JSONScanner jSONScanner2 = new JSONScanner(strSubstring.substring(0, iLastIndexOf));
                                try {
                                    if (jSONScanner2.scanISO8601DateIfMatch(false)) {
                                        ?? r02 = (T) jSONScanner2.getCalendar();
                                        r02.setTimeZone(timeZone);
                                        return type == Calendar.class ? r02 : (T) r02.getTime();
                                    }
                                } finally {
                                }
                            }
                        }
                        date = new Date(Long.parseLong(strSubstring));
                    }
                }
            }
            return date;
        } finally {
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        char[] charArray;
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        Class<?> cls = obj.getClass();
        if (cls == java.sql.Date.class) {
            if ((((java.sql.Date) obj).getTime() + jSONSerializer.timeZone.getOffset(r0)) % 86400000 == 0 && !SerializerFeature.isEnabled(serializeWriter.features, i, SerializerFeature.WriteClassName)) {
                serializeWriter.writeString(obj.toString());
                return;
            }
        }
        if (cls == Time.class) {
            long time = ((Time) obj).getTime();
            if ("unixtime".equals(jSONSerializer.getDateFormatPattern())) {
                serializeWriter.writeLong(time / 1000);
                return;
            } else if ("millis".equals(jSONSerializer.getDateFormatPattern())) {
                serializeWriter.writeLong(time);
                return;
            } else if (time < 86400000) {
                serializeWriter.writeString(obj.toString());
                return;
            }
        }
        Date dateCastToDate = obj instanceof Date ? (Date) obj : TypeUtils.castToDate(obj);
        if ("unixtime".equals(jSONSerializer.getDateFormatPattern())) {
            serializeWriter.writeLong(dateCastToDate.getTime() / 1000);
            return;
        }
        if ("millis".equals(jSONSerializer.getDateFormatPattern())) {
            serializeWriter.writeLong(dateCastToDate.getTime());
            return;
        }
        if (serializeWriter.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
            DateFormat dateFormat = jSONSerializer.getDateFormat();
            DateFormat simpleDateFormat = dateFormat;
            if (dateFormat == null) {
                simpleDateFormat = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT, jSONSerializer.locale);
                simpleDateFormat.setTimeZone(jSONSerializer.timeZone);
            }
            serializeWriter.writeString(simpleDateFormat.format(dateCastToDate));
            return;
        }
        if (serializeWriter.isEnabled(SerializerFeature.WriteClassName) && cls != type) {
            if (cls == Date.class) {
                serializeWriter.write("new Date(");
                serializeWriter.writeLong(((Date) obj).getTime());
                serializeWriter.write(41);
                return;
            } else {
                serializeWriter.write(CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
                serializeWriter.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                jSONSerializer.write(cls.getName());
                serializeWriter.writeFieldValue(',', "val", ((Date) obj).getTime());
                serializeWriter.write(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE);
                return;
            }
        }
        long time2 = dateCastToDate.getTime();
        if (!serializeWriter.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            serializeWriter.writeLong(time2);
            return;
        }
        int i2 = serializeWriter.isEnabled(SerializerFeature.UseSingleQuotes) ? 39 : 34;
        serializeWriter.write(i2);
        Calendar calendar = Calendar.getInstance(jSONSerializer.timeZone, jSONSerializer.locale);
        calendar.setTimeInMillis(time2);
        int i3 = calendar.get(1);
        int i4 = calendar.get(2) + 1;
        int i5 = calendar.get(5);
        int i6 = calendar.get(11);
        int i7 = calendar.get(12);
        int i8 = calendar.get(13);
        int i9 = calendar.get(14);
        if (i9 != 0) {
            charArray = "0000-00-00T00:00:00.000".toCharArray();
            IOUtils.getChars(i9, 23, charArray);
            IOUtils.getChars(i8, 19, charArray);
            IOUtils.getChars(i7, 16, charArray);
            IOUtils.getChars(i6, 13, charArray);
            IOUtils.getChars(i5, 10, charArray);
            IOUtils.getChars(i4, 7, charArray);
            IOUtils.getChars(i3, 4, charArray);
        } else if (i8 == 0 && i7 == 0 && i6 == 0) {
            charArray = "0000-00-00".toCharArray();
            IOUtils.getChars(i5, 10, charArray);
            IOUtils.getChars(i4, 7, charArray);
            IOUtils.getChars(i3, 4, charArray);
        } else {
            charArray = "0000-00-00T00:00:00".toCharArray();
            IOUtils.getChars(i8, 19, charArray);
            IOUtils.getChars(i7, 16, charArray);
            IOUtils.getChars(i6, 13, charArray);
            IOUtils.getChars(i5, 10, charArray);
            IOUtils.getChars(i4, 7, charArray);
            IOUtils.getChars(i3, 4, charArray);
        }
        serializeWriter.write(charArray);
        float offset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis()) / 3600000.0f;
        int i10 = (int) offset;
        if (i10 == 0.0d) {
            serializeWriter.write(90);
        } else {
            if (i10 > 9) {
                serializeWriter.write(43);
                serializeWriter.writeInt(i10);
            } else if (i10 > 0) {
                serializeWriter.write(43);
                serializeWriter.write(48);
                serializeWriter.writeInt(i10);
            } else if (i10 < -9) {
                serializeWriter.write(45);
                serializeWriter.writeInt(i10);
            } else if (i10 < 0) {
                serializeWriter.write(45);
                serializeWriter.write(48);
                serializeWriter.writeInt(-i10);
            }
            serializeWriter.write(58);
            serializeWriter.append((CharSequence) String.format("%02d", Integer.valueOf((int) ((offset - i10) * 60.0f))));
        }
        serializeWriter.write(i2);
    }
}
