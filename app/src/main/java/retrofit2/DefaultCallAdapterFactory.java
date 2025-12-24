package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import retrofit2.CallAdapter;

/* loaded from: classes-dex2jar.jar:retrofit2/DefaultCallAdapterFactory.class */
final class DefaultCallAdapterFactory extends CallAdapter.Factory {
    static final CallAdapter.Factory INSTANCE = new DefaultCallAdapterFactory();

    DefaultCallAdapterFactory() {
    }

    @Override // retrofit2.CallAdapter.Factory
    public CallAdapter<?, ?> get(Type type, Annotation[] annotationArr, Retrofit retrofit) {
        if (getRawType(type) != Call.class) {
            return null;
        }
        return new CallAdapter<Object, Call<?>>(this, Utils.getCallResponseType(type)) { // from class: retrofit2.DefaultCallAdapterFactory.1
            final DefaultCallAdapterFactory this$0;
            final Type val$responseType;

            {
                this.this$0 = this;
                this.val$responseType = type;
            }

            @Override // retrofit2.CallAdapter
            public Call<?> adapt(Call<Object> call) {
                return call;
            }

            @Override // retrofit2.CallAdapter
            public Type responseType() {
                return this.val$responseType;
            }
        };
    }
}
