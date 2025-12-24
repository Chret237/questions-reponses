package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/SqlDateDeserializer.class */
public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final SqlDateDeserializer instance = new SqlDateDeserializer();
    public static final SqlDateDeserializer instance_timestamp = new SqlDateDeserializer(true);
    private boolean timestamp;

    public SqlDateDeserializer() {
        this.timestamp = false;
    }

    public SqlDateDeserializer(boolean z) {
        this.timestamp = false;
        this.timestamp = true;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer
    protected <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        long timeInMillis;
        Date date;
        if (this.timestamp) {
            return (T) castTimestamp(defaultJSONParser, type, obj, obj2);
        }
        if (obj2 == null) {
            return null;
        }
        if (obj2 instanceof java.util.Date) {
            date = new Date(((java.util.Date) obj2).getTime());
        } else if (obj2 instanceof BigDecimal) {
            date = new Date(TypeUtils.longValue((BigDecimal) obj2));
        } else {
            if (!(obj2 instanceof Number)) {
                if (!(obj2 instanceof String)) {
                    throw new JSONException("parse error : " + obj2);
                }
                String str = (String) obj2;
                if (str.length() == 0) {
                    return null;
                }
                JSONScanner jSONScanner = new JSONScanner(str);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        timeInMillis = jSONScanner.getCalendar().getTimeInMillis();
                    } else {
                        try {
                            return (T) new Date(defaultJSONParser.getDateFormat().parse(str).getTime());
                        } catch (ParseException e) {
                            timeInMillis = Long.parseLong(str);
                        }
                    }
                    jSONScanner.close();
                    return (T) new Date(timeInMillis);
                } finally {
                    jSONScanner.close();
                }
            }
            date = new Date(((Number) obj2).longValue());
        }
        return (T) date;
    }

    protected <T> T castTimestamp(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        long timeInMillis;
        if (obj2 == null) {
            return null;
        }
        if (obj2 instanceof java.util.Date) {
            return (T) new Timestamp(((java.util.Date) obj2).getTime());
        }
        if (obj2 instanceof BigDecimal) {
            return (T) new Timestamp(TypeUtils.longValue((BigDecimal) obj2));
        }
        if (obj2 instanceof Number) {
            return (T) new Timestamp(((Number) obj2).longValue());
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
                timeInMillis = jSONScanner.getCalendar().getTimeInMillis();
            } else {
                try {
                    return (T) new Timestamp(defaultJSONParser.getDateFormat().parse(str).getTime());
                } catch (ParseException e) {
                    timeInMillis = Long.parseLong(str);
                }
            }
            jSONScanner.close();
            return (T) new Timestamp(timeInMillis);
        } finally {
            jSONScanner.close();
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
