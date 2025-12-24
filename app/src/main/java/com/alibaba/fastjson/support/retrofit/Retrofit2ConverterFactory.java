package com.alibaba.fastjson.support.retrofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/retrofit/Retrofit2ConverterFactory.class */
public class Retrofit2ConverterFactory extends Converter.Factory {
    private FastJsonConfig fastJsonConfig;

    @Deprecated
    private int featureValues;

    @Deprecated
    private Feature[] features;

    @Deprecated
    private ParserConfig parserConfig;

    @Deprecated
    private SerializeConfig serializeConfig;

    @Deprecated
    private SerializerFeature[] serializerFeatures;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Deprecated
    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/retrofit/Retrofit2ConverterFactory$RequestBodyConverter.class */
    final class RequestBodyConverter<T> implements Converter<T, RequestBody> {
        final Retrofit2ConverterFactory this$0;

        RequestBodyConverter(Retrofit2ConverterFactory retrofit2ConverterFactory) {
            this.this$0 = retrofit2ConverterFactory;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // retrofit2.Converter
        public /* bridge */ /* synthetic */ RequestBody convert(Object obj) throws IOException {
            return convert((RequestBodyConverter<T>) obj);
        }

        @Override // retrofit2.Converter
        public RequestBody convert(T t) throws IOException {
            try {
                return RequestBody.create(Retrofit2ConverterFactory.MEDIA_TYPE, JSON.toJSONBytes(this.this$0.fastJsonConfig.getCharset(), t, this.this$0.fastJsonConfig.getSerializeConfig(), this.this$0.fastJsonConfig.getSerializeFilters(), this.this$0.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.this$0.fastJsonConfig.getSerializerFeatures()));
            } catch (Exception e) {
                throw new IOException("Could not write JSON: " + e.getMessage(), e);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/support/retrofit/Retrofit2ConverterFactory$ResponseBodyConverter.class */
    final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        final Retrofit2ConverterFactory this$0;
        private Type type;

        ResponseBodyConverter(Retrofit2ConverterFactory retrofit2ConverterFactory, Type type) {
            this.this$0 = retrofit2ConverterFactory;
            this.type = type;
        }

        @Override // retrofit2.Converter
        public T convert(ResponseBody responseBody) throws IOException {
            try {
                try {
                    T t = (T) JSON.parseObject(responseBody.bytes(), this.this$0.fastJsonConfig.getCharset(), this.type, this.this$0.fastJsonConfig.getParserConfig(), this.this$0.fastJsonConfig.getParseProcess(), JSON.DEFAULT_PARSER_FEATURE, this.this$0.fastJsonConfig.getFeatures());
                    responseBody.close();
                    return t;
                } catch (Exception e) {
                    throw new IOException("JSON parse error: " + e.getMessage(), e);
                }
            } catch (Throwable th) {
                responseBody.close();
                throw th;
            }
        }
    }

    public Retrofit2ConverterFactory() {
        this.parserConfig = ParserConfig.getGlobalInstance();
        this.featureValues = JSON.DEFAULT_PARSER_FEATURE;
        this.fastJsonConfig = new FastJsonConfig();
    }

    public Retrofit2ConverterFactory(FastJsonConfig fastJsonConfig) {
        this.parserConfig = ParserConfig.getGlobalInstance();
        this.featureValues = JSON.DEFAULT_PARSER_FEATURE;
        this.fastJsonConfig = fastJsonConfig;
    }

    public static Retrofit2ConverterFactory create() {
        return create(new FastJsonConfig());
    }

    public static Retrofit2ConverterFactory create(FastJsonConfig fastJsonConfig) {
        if (fastJsonConfig != null) {
            return new Retrofit2ConverterFactory(fastJsonConfig);
        }
        throw new NullPointerException("fastJsonConfig == null");
    }

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    @Deprecated
    public ParserConfig getParserConfig() {
        return this.fastJsonConfig.getParserConfig();
    }

    @Deprecated
    public int getParserFeatureValues() {
        return JSON.DEFAULT_PARSER_FEATURE;
    }

    @Deprecated
    public Feature[] getParserFeatures() {
        return this.fastJsonConfig.getFeatures();
    }

    @Deprecated
    public SerializeConfig getSerializeConfig() {
        return this.fastJsonConfig.getSerializeConfig();
    }

    @Deprecated
    public SerializerFeature[] getSerializerFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Override // retrofit2.Converter.Factory
    public Converter<Object, RequestBody> requestBodyConverter(Type type, Annotation[] annotationArr, Annotation[] annotationArr2, Retrofit retrofit) {
        return new RequestBodyConverter(this);
    }

    @Override // retrofit2.Converter.Factory
    public Converter<ResponseBody, Object> responseBodyConverter(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        return new ResponseBodyConverter(this, type);
    }

    public Retrofit2ConverterFactory setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
        return this;
    }

    @Deprecated
    public Retrofit2ConverterFactory setParserConfig(ParserConfig parserConfig) {
        this.fastJsonConfig.setParserConfig(parserConfig);
        return this;
    }

    @Deprecated
    public Retrofit2ConverterFactory setParserFeatureValues(int i) {
        return this;
    }

    @Deprecated
    public Retrofit2ConverterFactory setParserFeatures(Feature[] featureArr) {
        this.fastJsonConfig.setFeatures(featureArr);
        return this;
    }

    @Deprecated
    public Retrofit2ConverterFactory setSerializeConfig(SerializeConfig serializeConfig) {
        this.fastJsonConfig.setSerializeConfig(serializeConfig);
        return this;
    }

    @Deprecated
    public Retrofit2ConverterFactory setSerializerFeatures(SerializerFeature[] serializerFeatureArr) {
        this.fastJsonConfig.setSerializerFeatures(serializerFeatureArr);
        return this;
    }
}
