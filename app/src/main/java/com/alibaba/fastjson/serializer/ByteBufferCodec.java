package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/ByteBufferCodec.class */
public class ByteBufferCodec implements ObjectSerializer, ObjectDeserializer {
    public static final ByteBufferCodec instance = new ByteBufferCodec();

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/ByteBufferCodec$ByteBufferBean.class */
    public static class ByteBufferBean {
        public byte[] array;
        public int limit;
        public int position;

        public ByteBuffer byteBuffer() {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(this.array);
            byteBufferWrap.limit(this.limit);
            byteBufferWrap.position(this.position);
            return byteBufferWrap;
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) ((ByteBufferBean) defaultJSONParser.parseObject((Class) ByteBufferBean.class)).byteBuffer();
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 14;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) obj;
        byte[] bArrArray = byteBuffer.array();
        SerializeWriter serializeWriter = jSONSerializer.out;
        serializeWriter.write(CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
        serializeWriter.writeFieldName("array");
        serializeWriter.writeByteArray(bArrArray);
        serializeWriter.writeFieldValue(',', "limit", byteBuffer.limit());
        serializeWriter.writeFieldValue(',', "position", byteBuffer.position());
        serializeWriter.write(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE);
    }
}
