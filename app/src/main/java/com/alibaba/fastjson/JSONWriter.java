package com.alibaba.fastjson;

import android.support.v4.view.PointerIconCompat;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONWriter.class */
public class JSONWriter implements Closeable, Flushable {
    private JSONStreamContext context;
    private JSONSerializer serializer;
    private SerializeWriter writer;

    public JSONWriter(Writer writer) {
        SerializeWriter serializeWriter = new SerializeWriter(writer);
        this.writer = serializeWriter;
        this.serializer = new JSONSerializer(serializeWriter);
    }

    private void afterWriter() {
        int i;
        JSONStreamContext jSONStreamContext = this.context;
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

    private void beforeWrite() {
        JSONStreamContext jSONStreamContext = this.context;
        if (jSONStreamContext == null) {
            return;
        }
        int i = jSONStreamContext.state;
        if (i == 1002) {
            this.writer.write(58);
        } else if (i == 1003) {
            this.writer.write(44);
        } else {
            if (i != 1005) {
                return;
            }
            this.writer.write(44);
        }
    }

    private void beginStructure() {
        int i = this.context.state;
        switch (this.context.state) {
            case PointerIconCompat.TYPE_CONTEXT_MENU /* 1001 */:
            case PointerIconCompat.TYPE_WAIT /* 1004 */:
                return;
            case PointerIconCompat.TYPE_HAND /* 1002 */:
                this.writer.write(58);
                return;
            case PointerIconCompat.TYPE_HELP /* 1003 */:
            default:
                throw new JSONException("illegal state : " + i);
            case 1005:
                this.writer.write(44);
                return;
        }
    }

    private void endStructure() {
        JSONStreamContext jSONStreamContext = this.context.parent;
        this.context = jSONStreamContext;
        if (jSONStreamContext == null) {
            return;
        }
        int i = jSONStreamContext.state;
        int i2 = 1002;
        if (i != 1001) {
            i2 = i != 1002 ? i != 1004 ? -1 : 1005 : 1003;
        }
        if (i2 != -1) {
            this.context.state = i2;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.writer.close();
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        this.writer.config(serializerFeature, z);
    }

    public void endArray() {
        this.writer.write(93);
        endStructure();
    }

    public void endObject() {
        this.writer.write(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE);
        endStructure();
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    public void startArray() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_WAIT);
        this.writer.write(91);
    }

    public void startObject() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, PointerIconCompat.TYPE_CONTEXT_MENU);
        this.writer.write(CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
    }

    @Deprecated
    public void writeEndArray() {
        endArray();
    }

    @Deprecated
    public void writeEndObject() {
        endObject();
    }

    public void writeKey(String str) {
        writeObject(str);
    }

    public void writeObject(Object obj) {
        beforeWrite();
        this.serializer.write(obj);
        afterWriter();
    }

    public void writeObject(String str) {
        beforeWrite();
        this.serializer.write(str);
        afterWriter();
    }

    @Deprecated
    public void writeStartArray() {
        startArray();
    }

    @Deprecated
    public void writeStartObject() {
        startObject();
    }

    public void writeValue(Object obj) {
        writeObject(obj);
    }
}
