package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/AbstractDateDeserializer.class */
public abstract class AbstractDateDeserializer extends ContextObjectDeserializer implements ObjectDeserializer {
    protected abstract <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2);

    @Override // com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser, type, obj, null, 0);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj, String str, int i) {
        Object objValueOf;
        Type type2;
        SimpleDateFormat simpleDateFormat;
        Date date;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 2) {
            long jLongValue = jSONLexer.longValue();
            jSONLexer.nextToken(16);
            long j = jLongValue;
            if ("unixtime".equals(str)) {
                j = jLongValue * 1000;
            }
            objValueOf = Long.valueOf(j);
            type2 = type;
        } else if (jSONLexer.token() == 4) {
            String strStringVal = jSONLexer.stringVal();
            Date date2 = null;
            if (str != null) {
                try {
                    simpleDateFormat = new SimpleDateFormat(str, defaultJSONParser.lexer.getLocale());
                } catch (IllegalArgumentException e) {
                    if (str.contains("T")) {
                        try {
                            simpleDateFormat = new SimpleDateFormat(str.replaceAll("T", "'T'"), defaultJSONParser.lexer.getLocale());
                        } catch (IllegalArgumentException e2) {
                            throw e;
                        }
                    } else {
                        simpleDateFormat = null;
                    }
                }
                if (JSON.defaultTimeZone != null) {
                    simpleDateFormat.setTimeZone(defaultJSONParser.lexer.getTimeZone());
                }
                try {
                    date = simpleDateFormat.parse(strStringVal);
                } catch (ParseException e3) {
                    date = null;
                }
                Date date3 = date;
                if (date == null) {
                    date3 = date;
                    if (JSON.defaultLocale == Locale.CHINA) {
                        try {
                            simpleDateFormat = new SimpleDateFormat(str, Locale.US);
                        } catch (IllegalArgumentException e4) {
                            if (str.contains("T")) {
                                try {
                                    simpleDateFormat = new SimpleDateFormat(str.replaceAll("T", "'T'"), defaultJSONParser.lexer.getLocale());
                                } catch (IllegalArgumentException e5) {
                                    throw e4;
                                }
                            }
                        }
                        simpleDateFormat.setTimeZone(defaultJSONParser.lexer.getTimeZone());
                        try {
                            date3 = simpleDateFormat.parse(strStringVal);
                        } catch (ParseException e6) {
                            date3 = null;
                        }
                    }
                }
                if (date3 == null) {
                    date2 = null;
                    if (str.equals("yyyy-MM-dd'T'HH:mm:ss.SSS")) {
                        date2 = null;
                        if (strStringVal.length() == 19) {
                            try {
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", JSON.defaultLocale);
                                simpleDateFormat2.setTimeZone(JSON.defaultTimeZone);
                                date2 = simpleDateFormat2.parse(strStringVal);
                            } catch (ParseException e7) {
                                date2 = null;
                            }
                        }
                    }
                } else {
                    date2 = date3;
                }
            }
            objValueOf = date2;
            type2 = type;
            if (date2 == null) {
                jSONLexer.nextToken(16);
                objValueOf = strStringVal;
                if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner jSONScanner = new JSONScanner(strStringVal);
                    objValueOf = strStringVal;
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        objValueOf = jSONScanner.getCalendar().getTime();
                    }
                    jSONScanner.close();
                }
                type2 = type;
            }
        } else if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            objValueOf = null;
            type2 = type;
        } else if (jSONLexer.token() == 12) {
            jSONLexer.nextToken();
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            Type type3 = type;
            if (JSON.DEFAULT_TYPE_KEY.equals(jSONLexer.stringVal())) {
                jSONLexer.nextToken();
                defaultJSONParser.accept(17);
                Class<?> clsCheckAutoType = defaultJSONParser.getConfig().checkAutoType(jSONLexer.stringVal(), null, jSONLexer.getFeatures());
                if (clsCheckAutoType != null) {
                    type = clsCheckAutoType;
                }
                defaultJSONParser.accept(4);
                defaultJSONParser.accept(16);
                type3 = type;
            }
            jSONLexer.nextTokenWithColon(2);
            if (jSONLexer.token() != 2) {
                throw new JSONException("syntax error : " + jSONLexer.tokenName());
            }
            long jLongValue2 = jSONLexer.longValue();
            jSONLexer.nextToken();
            objValueOf = Long.valueOf(jLongValue2);
            defaultJSONParser.accept(13);
            type2 = type3;
        } else if (defaultJSONParser.getResolveStatus() == 2) {
            defaultJSONParser.setResolveStatus(0);
            defaultJSONParser.accept(16);
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            if (!"val".equals(jSONLexer.stringVal())) {
                throw new JSONException("syntax error");
            }
            jSONLexer.nextToken();
            defaultJSONParser.accept(17);
            objValueOf = defaultJSONParser.parse();
            defaultJSONParser.accept(13);
            type2 = type;
        } else {
            objValueOf = defaultJSONParser.parse();
            type2 = type;
        }
        return (T) cast(defaultJSONParser, type2, obj, objValueOf);
    }
}
