package com.alibaba.fastjson.support.spring.messaging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.nio.charset.Charset;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/spring/messaging/MappingFastJsonMessageConverter.class */
public class MappingFastJsonMessageConverter extends AbstractMessageConverter {
    private FastJsonConfig fastJsonConfig;

    public MappingFastJsonMessageConverter() {
        super(new MimeType("application", "json", Charset.forName("UTF-8")));
        this.fastJsonConfig = new FastJsonConfig();
    }

    protected boolean canConvertFrom(Message<?> message, Class<?> cls) {
        return supports(cls);
    }

    protected boolean canConvertTo(Object obj, MessageHeaders messageHeaders) {
        return supports(obj.getClass());
    }

    protected Object convertFromInternal(Message<?> message, Class<?> cls, Object obj) {
        Object payload = message.getPayload();
        return payload instanceof byte[] ? JSON.parseObject((byte[]) payload, this.fastJsonConfig.getCharset(), cls, this.fastJsonConfig.getParserConfig(), this.fastJsonConfig.getParseProcess(), JSON.DEFAULT_PARSER_FEATURE, this.fastJsonConfig.getFeatures()) : payload instanceof String ? JSON.parseObject((String) payload, cls, this.fastJsonConfig.getParserConfig(), this.fastJsonConfig.getParseProcess(), JSON.DEFAULT_PARSER_FEATURE, this.fastJsonConfig.getFeatures()) : null;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.Object convertToInternal(java.lang.Object r9, org.springframework.messaging.MessageHeaders r10, java.lang.Object r11) {
        /*
            r8 = this;
            java.lang.Class<byte[]> r0 = byte[].class
            r1 = r8
            java.lang.Class r1 = r1.getSerializedPayloadClass()
            if (r0 != r1) goto L59
            r0 = r9
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L2b
            r0 = r9
            java.lang.String r0 = (java.lang.String) r0
            r10 = r0
            r0 = r10
            boolean r0 = com.alibaba.fastjson.JSON.isValid(r0)
            if (r0 == 0) goto L2b
            r0 = r10
            r1 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r1 = r1.fastJsonConfig
            java.nio.charset.Charset r1 = r1.getCharset()
            byte[] r0 = r0.getBytes(r1)
            r9 = r0
            goto L91
        L2b:
            r0 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r0 = r0.fastJsonConfig
            java.nio.charset.Charset r0 = r0.getCharset()
            r1 = r9
            r2 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r2 = r2.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializeConfig r2 = r2.getSerializeConfig()
            r3 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r3 = r3.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializeFilter[] r3 = r3.getSerializeFilters()
            r4 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r4 = r4.fastJsonConfig
            java.lang.String r4 = r4.getDateFormat()
            int r5 = com.alibaba.fastjson.JSON.DEFAULT_GENERATE_FEATURE
            r6 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r6 = r6.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializerFeature[] r6 = r6.getSerializerFeatures()
            byte[] r0 = com.alibaba.fastjson.JSON.toJSONBytes(r0, r1, r2, r3, r4, r5, r6)
            r9 = r0
            goto L91
        L59:
            r0 = r9
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L6d
            r0 = r9
            java.lang.String r0 = (java.lang.String) r0
            boolean r0 = com.alibaba.fastjson.JSON.isValid(r0)
            if (r0 == 0) goto L6d
            goto L91
        L6d:
            r0 = r9
            r1 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r1 = r1.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializeConfig r1 = r1.getSerializeConfig()
            r2 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r2 = r2.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializeFilter[] r2 = r2.getSerializeFilters()
            r3 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r3 = r3.fastJsonConfig
            java.lang.String r3 = r3.getDateFormat()
            int r4 = com.alibaba.fastjson.JSON.DEFAULT_GENERATE_FEATURE
            r5 = r8
            com.alibaba.fastjson.support.config.FastJsonConfig r5 = r5.fastJsonConfig
            com.alibaba.fastjson.serializer.SerializerFeature[] r5 = r5.getSerializerFeatures()
            java.lang.String r0 = com.alibaba.fastjson.JSON.toJSONString(r0, r1, r2, r3, r4, r5)
            r9 = r0
        L91:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.support.spring.messaging.MappingFastJsonMessageConverter.convertToInternal(java.lang.Object, org.springframework.messaging.MessageHeaders, java.lang.Object):java.lang.Object");
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    protected boolean supports(Class<?> cls) {
        return true;
    }
}
