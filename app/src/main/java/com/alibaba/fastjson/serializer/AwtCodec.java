package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/AwtCodec.class */
public class AwtCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AwtCodec instance = new AwtCodec();

    private Object parseRef(DefaultJSONParser defaultJSONParser, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        lexer.nextTokenWithColon(4);
        String strStringVal = lexer.stringVal();
        defaultJSONParser.setContext(defaultJSONParser.getContext(), obj);
        defaultJSONParser.addResolveTask(new DefaultJSONParser.ResolveTask(defaultJSONParser.getContext(), strStringVal));
        defaultJSONParser.popContext();
        defaultJSONParser.setResolveStatus(1);
        lexer.nextToken(13);
        defaultJSONParser.accept(13);
        return null;
    }

    public static boolean support(Class<?> cls) {
        return cls == Point.class || cls == Rectangle.class || cls == Font.class || cls == Color.class;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        Point font;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        if (jSONLexer.token() == 8) {
            jSONLexer.nextToken(16);
            return null;
        }
        if (jSONLexer.token() != 12 && jSONLexer.token() != 16) {
            throw new JSONException("syntax error");
        }
        jSONLexer.nextToken();
        if (type == Point.class) {
            font = parsePoint(defaultJSONParser, obj);
        } else if (type == Rectangle.class) {
            font = parseRectangle(defaultJSONParser);
        } else if (type == Color.class) {
            font = parseColor(defaultJSONParser);
        } else {
            if (type != Font.class) {
                throw new JSONException("not support awt class : " + type);
            }
            font = parseFont(defaultJSONParser);
        }
        ParseContext context = defaultJSONParser.getContext();
        defaultJSONParser.setContext(font, obj);
        defaultJSONParser.setContext(context);
        return (T) font;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }

    protected Color parseColor(DefaultJSONParser defaultJSONParser) {
        int i;
        int i2;
        int i3;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            String strStringVal = jSONLexer.stringVal();
            jSONLexer.nextTokenWithColon(2);
            if (jSONLexer.token() != 2) {
                throw new JSONException("syntax error");
            }
            int iIntValue = jSONLexer.intValue();
            jSONLexer.nextToken();
            if (strStringVal.equalsIgnoreCase("r")) {
                i3 = i5;
                i2 = i6;
                i = i7;
            } else if (strStringVal.equalsIgnoreCase("g")) {
                i3 = iIntValue;
                iIntValue = i4;
                i2 = i6;
                i = i7;
            } else if (strStringVal.equalsIgnoreCase("b")) {
                i2 = iIntValue;
                iIntValue = i4;
                i3 = i5;
                i = i7;
            } else {
                if (!strStringVal.equalsIgnoreCase("alpha")) {
                    throw new JSONException("syntax error, " + strStringVal);
                }
                i = iIntValue;
                i2 = i6;
                i3 = i5;
                iIntValue = i4;
            }
            i4 = iIntValue;
            i5 = i3;
            i6 = i2;
            i7 = i;
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken(4);
                i4 = iIntValue;
                i5 = i3;
                i6 = i2;
                i7 = i;
            }
        }
        jSONLexer.nextToken();
        return new Color(i4, i5, i6, i7);
    }

    protected Font parseFont(DefaultJSONParser defaultJSONParser) {
        int iIntValue;
        String strStringVal;
        int iIntValue2;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i = 0;
        String str = null;
        int i2 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            String strStringVal2 = jSONLexer.stringVal();
            jSONLexer.nextTokenWithColon(2);
            if (strStringVal2.equalsIgnoreCase("name")) {
                if (jSONLexer.token() != 4) {
                    throw new JSONException("syntax error");
                }
                strStringVal = jSONLexer.stringVal();
                jSONLexer.nextToken();
                iIntValue2 = i;
                iIntValue = i2;
            } else if (strStringVal2.equalsIgnoreCase("style")) {
                if (jSONLexer.token() != 2) {
                    throw new JSONException("syntax error");
                }
                iIntValue2 = jSONLexer.intValue();
                jSONLexer.nextToken();
                iIntValue = i2;
                strStringVal = str;
            } else {
                if (!strStringVal2.equalsIgnoreCase("size")) {
                    throw new JSONException("syntax error, " + strStringVal2);
                }
                if (jSONLexer.token() != 2) {
                    throw new JSONException("syntax error");
                }
                iIntValue = jSONLexer.intValue();
                jSONLexer.nextToken();
                strStringVal = str;
                iIntValue2 = i;
            }
            i = iIntValue2;
            i2 = iIntValue;
            str = strStringVal;
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken(4);
                i = iIntValue2;
                i2 = iIntValue;
                str = strStringVal;
            }
        }
        jSONLexer.nextToken();
        return new Font(str, i, i2);
    }

    protected Point parsePoint(DefaultJSONParser defaultJSONParser, Object obj) {
        int iFloatValue;
        int i;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i2 = 0;
        int i3 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            String strStringVal = jSONLexer.stringVal();
            if (JSON.DEFAULT_TYPE_KEY.equals(strStringVal)) {
                defaultJSONParser.acceptType("java.awt.Point");
            } else {
                if ("$ref".equals(strStringVal)) {
                    return (Point) parseRef(defaultJSONParser, obj);
                }
                jSONLexer.nextTokenWithColon(2);
                int i4 = jSONLexer.token();
                if (i4 == 2) {
                    iFloatValue = jSONLexer.intValue();
                    jSONLexer.nextToken();
                } else {
                    if (i4 != 3) {
                        throw new JSONException("syntax error : " + jSONLexer.tokenName());
                    }
                    iFloatValue = (int) jSONLexer.floatValue();
                    jSONLexer.nextToken();
                }
                if (strStringVal.equalsIgnoreCase("x")) {
                    i = iFloatValue;
                    iFloatValue = i3;
                } else {
                    if (!strStringVal.equalsIgnoreCase("y")) {
                        throw new JSONException("syntax error, " + strStringVal);
                    }
                    i = i2;
                }
                i2 = i;
                i3 = iFloatValue;
                if (jSONLexer.token() == 16) {
                    jSONLexer.nextToken(4);
                    i2 = i;
                    i3 = iFloatValue;
                }
            }
        }
        jSONLexer.nextToken();
        return new Point(i2, i3);
    }

    protected Rectangle parseRectangle(DefaultJSONParser defaultJSONParser) {
        int iFloatValue;
        int i;
        int i2;
        int i3;
        JSONLexer jSONLexer = defaultJSONParser.lexer;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (jSONLexer.token() != 13) {
            if (jSONLexer.token() != 4) {
                throw new JSONException("syntax error");
            }
            String strStringVal = jSONLexer.stringVal();
            jSONLexer.nextTokenWithColon(2);
            int i8 = jSONLexer.token();
            if (i8 == 2) {
                iFloatValue = jSONLexer.intValue();
                jSONLexer.nextToken();
            } else {
                if (i8 != 3) {
                    throw new JSONException("syntax error");
                }
                iFloatValue = (int) jSONLexer.floatValue();
                jSONLexer.nextToken();
            }
            if (strStringVal.equalsIgnoreCase("x")) {
                i3 = i5;
                i2 = i6;
                i = i7;
            } else if (strStringVal.equalsIgnoreCase("y")) {
                i3 = iFloatValue;
                iFloatValue = i4;
                i2 = i6;
                i = i7;
            } else if (strStringVal.equalsIgnoreCase("width")) {
                i2 = iFloatValue;
                iFloatValue = i4;
                i3 = i5;
                i = i7;
            } else {
                if (!strStringVal.equalsIgnoreCase("height")) {
                    throw new JSONException("syntax error, " + strStringVal);
                }
                i = iFloatValue;
                i2 = i6;
                i3 = i5;
                iFloatValue = i4;
            }
            i4 = iFloatValue;
            i5 = i3;
            i6 = i2;
            i7 = i;
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken(4);
                i4 = iFloatValue;
                i5 = i3;
                i6 = i2;
                i7 = i;
            }
        }
        jSONLexer.nextToken();
        return new Rectangle(i4, i5, i6, i7);
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (obj == null) {
            serializeWriter.writeNull();
            return;
        }
        if (obj instanceof Point) {
            Point point = (Point) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Point.class, '{'), "x", point.x);
            serializeWriter.writeFieldValue(',', "y", point.y);
        } else if (obj instanceof Font) {
            Font font = (Font) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Font.class, '{'), "name", font.getName());
            serializeWriter.writeFieldValue(',', "style", font.getStyle());
            serializeWriter.writeFieldValue(',', "size", font.getSize());
        } else if (obj instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Rectangle.class, '{'), "x", rectangle.x);
            serializeWriter.writeFieldValue(',', "y", rectangle.y);
            serializeWriter.writeFieldValue(',', "width", rectangle.width);
            serializeWriter.writeFieldValue(',', "height", rectangle.height);
        } else {
            if (!(obj instanceof Color)) {
                throw new JSONException("not support awt class : " + obj.getClass().getName());
            }
            Color color = (Color) obj;
            serializeWriter.writeFieldValue(writeClassName(serializeWriter, Color.class, '{'), "r", color.getRed());
            serializeWriter.writeFieldValue(',', "g", color.getGreen());
            serializeWriter.writeFieldValue(',', "b", color.getBlue());
            if (color.getAlpha() > 0) {
                serializeWriter.writeFieldValue(',', "alpha", color.getAlpha());
            }
        }
        serializeWriter.write(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE);
    }

    protected char writeClassName(SerializeWriter serializeWriter, Class<?> cls, char c) {
        if (serializeWriter.isEnabled(SerializerFeature.WriteClassName)) {
            serializeWriter.write(CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
            serializeWriter.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            serializeWriter.writeString(cls.getName());
            c = ',';
        }
        return c;
    }
}
