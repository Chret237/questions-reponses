package retrofit2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler.class */
abstract class ParameterHandler<T> {

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Body.class */
    static final class Body<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;

        Body(Converter<T, RequestBody> converter) {
            this.converter = converter;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) {
            if (t == null) {
                throw new IllegalArgumentException("Body parameter value must not be null.");
            }
            try {
                requestBuilder.setBody(this.converter.convert(t));
            } catch (IOException e) {
                throw new RuntimeException("Unable to convert " + t + " to RequestBody", e);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Field.class */
    static final class Field<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Field(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            String strConvert;
            if (t == null || (strConvert = this.valueConverter.convert(t)) == null) {
                return;
            }
            requestBuilder.addFormField(this.name, strConvert, this.encoded);
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$FieldMap.class */
    static final class FieldMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        FieldMap(Converter<T, String> converter, boolean z) {
            this.valueConverter = converter;
            this.encoded = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // retrofit2.ParameterHandler
        public void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Field map was null.");
            }
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new IllegalArgumentException("Field map contained null key.");
                }
                T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Field map contained null value for key '" + key + "'.");
                }
                String strConvert = this.valueConverter.convert(value);
                if (strConvert == null) {
                    throw new IllegalArgumentException("Field map value '" + value + "' converted to null by " + this.valueConverter.getClass().getName() + " for key '" + key + "'.");
                }
                requestBuilder.addFormField(key, strConvert, this.encoded);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Header.class */
    static final class Header<T> extends ParameterHandler<T> {
        private final String name;
        private final Converter<T, String> valueConverter;

        Header(String str, Converter<T, String> converter) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            String strConvert;
            if (t == null || (strConvert = this.valueConverter.convert(t)) == null) {
                return;
            }
            requestBuilder.addHeader(this.name, strConvert);
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$HeaderMap.class */
    static final class HeaderMap<T> extends ParameterHandler<Map<String, T>> {
        private final Converter<T, String> valueConverter;

        HeaderMap(Converter<T, String> converter) {
            this.valueConverter = converter;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // retrofit2.ParameterHandler
        public void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Header map was null.");
            }
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new IllegalArgumentException("Header map contained null key.");
                }
                T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Header map contained null value for key '" + key + "'.");
                }
                requestBuilder.addHeader(key, this.valueConverter.convert(value));
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Part.class */
    static final class Part<T> extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;
        private final Headers headers;

        Part(Headers headers, Converter<T, RequestBody> converter) {
            this.headers = headers;
            this.converter = converter;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) {
            if (t == null) {
                return;
            }
            try {
                requestBuilder.addPart(this.headers, this.converter.convert(t));
            } catch (IOException e) {
                throw new RuntimeException("Unable to convert " + t + " to RequestBody", e);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$PartMap.class */
    static final class PartMap<T> extends ParameterHandler<Map<String, T>> {
        private final String transferEncoding;
        private final Converter<T, RequestBody> valueConverter;

        PartMap(Converter<T, RequestBody> converter, String str) {
            this.valueConverter = converter;
            this.transferEncoding = str;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // retrofit2.ParameterHandler
        public void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Part map was null.");
            }
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new IllegalArgumentException("Part map contained null key.");
                }
                T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Part map contained null value for key '" + key + "'.");
                }
                requestBuilder.addPart(Headers.m17of("Content-Disposition", "form-data; name=\"" + key + "\"", "Content-Transfer-Encoding", this.transferEncoding), this.valueConverter.convert(value));
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Path.class */
    static final class Path<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Path(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t != null) {
                requestBuilder.addPathParam(this.name, this.valueConverter.convert(t), this.encoded);
                return;
            }
            throw new IllegalArgumentException("Path parameter \"" + this.name + "\" value must not be null.");
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$Query.class */
    static final class Query<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Query(String str, Converter<T, String> converter, boolean z) {
            this.name = (String) Utils.checkNotNull(str, "name == null");
            this.valueConverter = converter;
            this.encoded = z;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            String strConvert;
            if (t == null || (strConvert = this.valueConverter.convert(t)) == null) {
                return;
            }
            requestBuilder.addQueryParam(this.name, strConvert, this.encoded);
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$QueryMap.class */
    static final class QueryMap<T> extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        QueryMap(Converter<T, String> converter, boolean z) {
            this.valueConverter = converter;
            this.encoded = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // retrofit2.ParameterHandler
        public void apply(RequestBuilder requestBuilder, @Nullable Map<String, T> map) throws IOException {
            if (map == null) {
                throw new IllegalArgumentException("Query map was null.");
            }
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new IllegalArgumentException("Query map contained null key.");
                }
                T value = entry.getValue();
                if (value == null) {
                    throw new IllegalArgumentException("Query map contained null value for key '" + key + "'.");
                }
                String strConvert = this.valueConverter.convert(value);
                if (strConvert == null) {
                    throw new IllegalArgumentException("Query map value '" + value + "' converted to null by " + this.valueConverter.getClass().getName() + " for key '" + key + "'.");
                }
                requestBuilder.addQueryParam(key, strConvert, this.encoded);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$QueryName.class */
    static final class QueryName<T> extends ParameterHandler<T> {
        private final boolean encoded;
        private final Converter<T, String> nameConverter;

        QueryName(Converter<T, String> converter, boolean z) {
            this.nameConverter = converter;
            this.encoded = z;
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException {
            if (t == null) {
                return;
            }
            requestBuilder.addQueryParam(this.nameConverter.convert(t), null, this.encoded);
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$RawPart.class */
    static final class RawPart extends ParameterHandler<MultipartBody.Part> {
        static final RawPart INSTANCE = new RawPart();

        private RawPart() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // retrofit2.ParameterHandler
        public void apply(RequestBuilder requestBuilder, @Nullable MultipartBody.Part part) {
            if (part != null) {
                requestBuilder.addPart(part);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/ParameterHandler$RelativeUrl.class */
    static final class RelativeUrl extends ParameterHandler<Object> {
        RelativeUrl() {
        }

        @Override // retrofit2.ParameterHandler
        void apply(RequestBuilder requestBuilder, @Nullable Object obj) {
            Utils.checkNotNull(obj, "@Url parameter is null.");
            requestBuilder.setRelativeUrl(obj);
        }
    }

    ParameterHandler() {
    }

    abstract void apply(RequestBuilder requestBuilder, @Nullable T t) throws IOException;

    final ParameterHandler<Object> array() {
        return new ParameterHandler<Object>(this) { // from class: retrofit2.ParameterHandler.2
            final ParameterHandler this$0;

            {
                this.this$0 = this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // retrofit2.ParameterHandler
            void apply(RequestBuilder requestBuilder, @Nullable Object obj) throws IOException {
                if (obj == null) {
                    return;
                }
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    this.this$0.apply(requestBuilder, Array.get(obj, i));
                }
            }
        };
    }

    final ParameterHandler<Iterable<T>> iterable() {
        return new ParameterHandler<Iterable<T>>(this) { // from class: retrofit2.ParameterHandler.1
            final ParameterHandler this$0;

            {
                this.this$0 = this;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // retrofit2.ParameterHandler
            public void apply(RequestBuilder requestBuilder, @Nullable Iterable<T> iterable) throws IOException {
                if (iterable == null) {
                    return;
                }
                Iterator<T> it = iterable.iterator();
                while (it.hasNext()) {
                    this.this$0.apply(requestBuilder, it.next());
                }
            }
        };
    }
}
