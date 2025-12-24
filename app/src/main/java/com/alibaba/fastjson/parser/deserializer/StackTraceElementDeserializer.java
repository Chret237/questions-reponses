package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/StackTraceElementDeserializer.class */
public class StackTraceElementDeserializer implements ObjectDeserializer {
    public static final StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        String strStringVal;
        int iIntValue;
        String strStringVal2;
        String strStringVal3;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken();
            return null;
        }
        if (jSONLexer.token() != 12 && jSONLexer.token() != 16) {
            throw new JSONException("syntax error: " + JSONToken.name(jSONLexer.token()));
        }
        String str = null;
        String str2 = null;
        String str3 = null;
        int i = 0;
        while (true) {
            String strScanSymbol = jSONLexer.scanSymbol(defaultJSONParser.getSymbolTable());
            if (strScanSymbol == null) {
                if (jSONLexer.token() == 13) {
                    jSONLexer.nextToken(16);
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                    break;
                }
                if (jSONLexer.token() != 16 || !jSONLexer.isEnabled(Feature.AllowArbitraryCommas)) {
                }
            }
            jSONLexer.nextTokenWithColon(4);
            if ("className".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    strStringVal3 = null;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    strStringVal3 = jSONLexer.stringVal();
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                }
            } else if ("methodName".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    strStringVal2 = null;
                    strStringVal3 = str;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    strStringVal2 = jSONLexer.stringVal();
                    strStringVal3 = str;
                    strStringVal = str3;
                    iIntValue = i;
                }
            } else if ("fileName".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    strStringVal = null;
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    strStringVal = jSONLexer.stringVal();
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    iIntValue = i;
                }
            } else if ("lineNumber".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    iIntValue = 0;
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                } else {
                    if (jSONLexer.token() != 2) {
                        throw new JSONException("syntax error");
                    }
                    iIntValue = jSONLexer.intValue();
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                }
            } else if ("nativeMethod".equals(strScanSymbol)) {
                if (jSONLexer.token() != 8 && jSONLexer.token() != 6 && jSONLexer.token() != 7) {
                    throw new JSONException("syntax error");
                }
                jSONLexer.nextToken(16);
                strStringVal3 = str;
                strStringVal2 = str2;
                strStringVal = str3;
                iIntValue = i;
            } else if (strScanSymbol == JSON.DEFAULT_TYPE_KEY) {
                if (jSONLexer.token() == 4) {
                    String strStringVal4 = jSONLexer.stringVal();
                    if (!strStringVal4.equals("java.lang.StackTraceElement")) {
                        throw new JSONException("syntax error : " + strStringVal4);
                    }
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 8) {
                        throw new JSONException("syntax error");
                    }
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                }
            } else if ("moduleName".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    jSONLexer.stringVal();
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                }
            } else if ("moduleVersion".equals(strScanSymbol)) {
                if (jSONLexer.token() == 8) {
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    jSONLexer.stringVal();
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                }
            } else {
                if (!"classLoaderName".equals(strScanSymbol)) {
                    throw new JSONException("syntax error : " + strScanSymbol);
                }
                if (jSONLexer.token() == 8) {
                    strStringVal3 = str;
                    strStringVal2 = str2;
                    strStringVal = str3;
                    iIntValue = i;
                } else {
                    if (jSONLexer.token() != 4) {
                        throw new JSONException("syntax error");
                    }
                    jSONLexer.stringVal();
                    iIntValue = i;
                    strStringVal = str3;
                    strStringVal2 = str2;
                    strStringVal3 = str;
                }
            }
            str = strStringVal3;
            str2 = strStringVal2;
            str3 = strStringVal;
            i = iIntValue;
            if (jSONLexer.token() == 13) {
                jSONLexer.nextToken(16);
                break;
            }
        }
        return (T) new StackTraceElement(strStringVal3, strStringVal2, strStringVal, iIntValue);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
