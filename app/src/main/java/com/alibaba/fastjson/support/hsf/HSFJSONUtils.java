package com.alibaba.fastjson.support.hsf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/hsf/HSFJSONUtils.class */
public class HSFJSONUtils {
    static final SymbolTable typeSymbolTable = new SymbolTable(1024);
    static final char[] fieldName_argsTypes = "\"argsTypes\"".toCharArray();
    static final char[] fieldName_argsObjs = "\"argsObjs\"".toCharArray();
    static final char[] fieldName_type = "\"@type\":".toCharArray();

    public static Object[] parseInvocationArguments(String str, MethodLocator methodLocator) {
        Object[] array;
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str);
        JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.getLexer();
        ParseContext context = defaultJSONParser.setContext(null, null);
        int i = jSONLexerBase.token();
        if (i == 12) {
            String[] strArrScanFieldStringArray = jSONLexerBase.scanFieldStringArray(fieldName_argsTypes, -1, typeSymbolTable);
            String[] strArrScanFieldStringArray2 = strArrScanFieldStringArray;
            if (strArrScanFieldStringArray == null) {
                strArrScanFieldStringArray2 = strArrScanFieldStringArray;
                if (jSONLexerBase.matchStat == -2) {
                    strArrScanFieldStringArray2 = strArrScanFieldStringArray;
                    if ("com.alibaba.fastjson.JSONObject".equals(jSONLexerBase.scanFieldString(fieldName_type))) {
                        strArrScanFieldStringArray2 = jSONLexerBase.scanFieldStringArray(fieldName_argsTypes, -1, typeSymbolTable);
                    }
                }
            }
            Method methodFindMethod = methodLocator.findMethod(strArrScanFieldStringArray2);
            if (methodFindMethod == null) {
                jSONLexerBase.close();
                JSONObject object = JSON.parseObject(str);
                Method methodFindMethod2 = methodLocator.findMethod((String[]) object.getObject("argsTypes", String[].class));
                JSONArray jSONArray = object.getJSONArray("argsObjs");
                if (jSONArray != null) {
                    Type[] genericParameterTypes = methodFindMethod2.getGenericParameterTypes();
                    Object[] objArr = new Object[genericParameterTypes.length];
                    int i2 = 0;
                    while (true) {
                        array = objArr;
                        if (i2 >= genericParameterTypes.length) {
                            break;
                        }
                        objArr[i2] = jSONArray.getObject(i2, genericParameterTypes[i2]);
                        i2++;
                    }
                } else {
                    array = null;
                }
            } else {
                Type[] genericParameterTypes2 = methodFindMethod.getGenericParameterTypes();
                jSONLexerBase.skipWhitespace();
                if (jSONLexerBase.getCurrent() == ',') {
                    jSONLexerBase.next();
                }
                array = null;
                if (jSONLexerBase.matchField2(fieldName_argsObjs)) {
                    jSONLexerBase.nextToken();
                    ParseContext context2 = defaultJSONParser.setContext(context, null, "argsObjs");
                    array = defaultJSONParser.parseArray(genericParameterTypes2);
                    context2.object = array;
                    defaultJSONParser.accept(13);
                    defaultJSONParser.handleResovleTask(null);
                }
                defaultJSONParser.close();
            }
        } else {
            array = null;
            if (i == 14) {
                String[] strArrScanFieldStringArray3 = jSONLexerBase.scanFieldStringArray(null, -1, typeSymbolTable);
                jSONLexerBase.skipWhitespace();
                char current = jSONLexerBase.getCurrent();
                if (current == ']') {
                    Type[] genericParameterTypes3 = methodLocator.findMethod(null).getGenericParameterTypes();
                    Object[] objArr2 = new Object[strArrScanFieldStringArray3.length];
                    for (int i3 = 0; i3 < strArrScanFieldStringArray3.length; i3++) {
                        Type type = genericParameterTypes3[i3];
                        String str2 = strArrScanFieldStringArray3[i3];
                        if (type != String.class) {
                            objArr2[i3] = TypeUtils.cast(str2, type, defaultJSONParser.getConfig());
                        } else {
                            objArr2[i3] = str2;
                        }
                    }
                    return objArr2;
                }
                if (current == ',') {
                    jSONLexerBase.next();
                    jSONLexerBase.skipWhitespace();
                }
                jSONLexerBase.nextToken(14);
                array = defaultJSONParser.parseArray(methodLocator.findMethod(strArrScanFieldStringArray3).getGenericParameterTypes());
                jSONLexerBase.close();
            }
        }
        return array;
    }
}
