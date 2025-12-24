package com.alibaba.fastjson;

import android.support.v4.view.PointerIconCompat;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONReader.class */
public class JSONReader implements Closeable {
    private JSONStreamContext context;
    private final DefaultJSONParser parser;

    public JSONReader(DefaultJSONParser defaultJSONParser) {
        this.parser = defaultJSONParser;
    }

    public JSONReader(JSONLexer jSONLexer) {
        this(new DefaultJSONParser(jSONLexer));
    }

    public JSONReader(Reader reader) {
        this(reader, new Feature[0]);
    }

    public JSONReader(Reader reader, Feature... featureArr) {
        this(new JSONReaderScanner(reader));
        for (Feature feature : featureArr) {
            config(feature, true);
        }
    }

    private void endStructure() {
        int i;
        JSONStreamContext jSONStreamContext = this.context.parent;
        this.context = jSONStreamContext;
        if (jSONStreamContext == null) {
            return;
        }
        switch (jSONStreamContext.state) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_HELP /* 1003 */:
                i = 1002;
                break;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                i = 1003;
                break;
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
                i = 1005;
                break;
            default:
                i = -1;
                break;
        }
        if (i != -1) {
            this.context.state = i;
        }
    }

    private void readAfter() {
        int i = this.context.state;
        int i2 = 1002;
        switch (i) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_HELP /* 1003 */:
                break;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                i2 = 1003;
                break;
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
                i2 = 1005;
                break;
            case 1005:
                i2 = -1;
                break;
            default:
                throw new JSONException("illegal state : " + i);
        }
        if (i2 != -1) {
            this.context.state = i2;
        }
    }

    private void readBefore() {
        int i = this.context.state;
        switch (i) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
                return;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                this.parser.accept(17);
                return;
            case PointerIconCompat.TYPE_HELP /* 1003 */:
                this.parser.accept(16, 18);
                return;
            case 1005:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + i);
        }
    }

    private void startStructure() {
        switch (this.context.state) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
                return;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                this.parser.accept(17);
                return;
            case PointerIconCompat.TYPE_HELP /* 1003 */:
            case 1005:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + this.context.state);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.parser.close();
    }

    public void config(Feature feature, boolean z) {
        this.parser.config(feature, z);
    }

    public void endArray() {
        this.parser.accept(15);
        endStructure();
    }

    public void endObject() {
        this.parser.accept(13);
        endStructure();
    }

    public Locale getLocal() {
        return this.parser.lexer.getLocale();
    }

    public TimeZone getTimzeZone() {
        return this.parser.lexer.getTimeZone();
    }

    public boolean hasNext() {
        if (this.context == null) {
            throw new JSONException("context is null");
        }
        int i = this.parser.lexer.token();
        int i2 = this.context.state;
        boolean z = true;
        switch (i2) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_HELP /* 1003 */:
                if (i == 13) {
                    z = false;
                }
                return z;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
            default:
                throw new JSONException("illegal state : " + i2);
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
            case 1005:
                return i != 15;
        }
    }

    public int peek() {
        return this.parser.lexer.token();
    }

    public Integer readInteger() {
        Object obj;
        if (this.context == null) {
            obj = this.parser.parse();
        } else {
            readBefore();
            obj = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToInt(obj);
    }

    public Long readLong() {
        Object obj;
        if (this.context == null) {
            obj = this.parser.parse();
        } else {
            readBefore();
            obj = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToLong(obj);
    }

    public Object readObject() {
        if (this.context == null) {
            return this.parser.parse();
        }
        readBefore();
        int i = this.context.state;
        Object key = (i == 1001 || i == 1003) ? this.parser.parseKey() : this.parser.parse();
        readAfter();
        return key;
    }

    public <T> T readObject(TypeReference<T> typeReference) {
        return (T) readObject(typeReference.getType());
    }

    public <T> T readObject(Class<T> cls) {
        if (this.context == null) {
            return (T) this.parser.parseObject((Class) cls);
        }
        readBefore();
        T t = (T) this.parser.parseObject((Class) cls);
        readAfter();
        return t;
    }

    public <T> T readObject(Type type) {
        if (this.context == null) {
            return (T) this.parser.parseObject(type);
        }
        readBefore();
        T t = (T) this.parser.parseObject(type);
        readAfter();
        return t;
    }

    public Object readObject(Map map) {
        if (this.context == null) {
            return this.parser.parseObject(map);
        }
        readBefore();
        Object object = this.parser.parseObject(map);
        readAfter();
        return object;
    }

    public void readObject(Object obj) {
        if (this.context == null) {
            this.parser.parseObject(obj);
            return;
        }
        readBefore();
        this.parser.parseObject(obj);
        readAfter();
    }

    public String readString() {
        Object objStringVal;
        if (this.context == null) {
            objStringVal = this.parser.parse();
        } else {
            readBefore();
            JSONLexer jSONLexer = this.parser.lexer;
            if (this.context.state == 1001 && jSONLexer.token() == 18) {
                objStringVal = jSONLexer.stringVal();
                jSONLexer.nextToken();
            } else {
                objStringVal = this.parser.parse();
            }
            readAfter();
        }
        return TypeUtils.castToString(objStringVal);
    }

    public void setLocale(Locale locale) {
        this.parser.lexer.setLocale(locale);
    }

    public void setTimzeZone(TimeZone timeZone) {
        this.parser.lexer.setTimeZone(timeZone);
    }

    public void startArray() {
        if (this.context == null) {
            this.context = new JSONStreamContext(null, PointerIconCompat.TYPE_WAIT);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_WAIT);
        }
        this.parser.accept(14);
    }

    public void startObject() {
        if (this.context == null) {
            this.context = new JSONStreamContext(null, PointerIconCompat.TYPE_CONTEXT_MENU);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_CONTEXT_MENU);
        }
        this.parser.accept(12, 18);
    }
}
