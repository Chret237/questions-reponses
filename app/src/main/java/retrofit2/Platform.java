package retrofit2;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import retrofit2.CallAdapter;

/* loaded from: classes-dex2jar.jar:retrofit2/Platform.class */
class Platform {
    private static final Platform PLATFORM = findPlatform();

    /* loaded from: classes-dex2jar.jar:retrofit2/Platform$Android.class */
    static class Android extends Platform {

        /* loaded from: classes-dex2jar.jar:retrofit2/Platform$Android$MainThreadExecutor.class */
        static class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            MainThreadExecutor() {
            }

            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                this.handler.post(runnable);
            }
        }

        Android() {
        }

        @Override // retrofit2.Platform
        CallAdapter.Factory defaultCallAdapterFactory(@Nullable Executor executor) {
            if (executor != null) {
                return new ExecutorCallAdapterFactory(executor);
            }
            throw new AssertionError();
        }

        @Override // retrofit2.Platform
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }
    }

    /* loaded from: classes-dex2jar.jar:retrofit2/Platform$Java8.class */
    static class Java8 extends Platform {
        Java8() {
        }

        @Override // retrofit2.Platform
        Object invokeDefaultMethod(Method method, Class<?> cls, Object obj, @Nullable Object... objArr) throws Throwable {
            Constructor declaredConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
            declaredConstructor.setAccessible(true);
            return ((MethodHandles.Lookup) declaredConstructor.newInstance(cls, -1)).unreflectSpecial(method, cls).bindTo(obj).invokeWithArguments(objArr);
        }

        @Override // retrofit2.Platform
        boolean isDefaultMethod(Method method) {
            return method.isDefault();
        }
    }

    Platform() {
    }

    private static Platform findPlatform() throws ClassNotFoundException {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("java.util.Optional");
            return new Java8();
        } catch (ClassNotFoundException e2) {
            return new Platform();
        }
    }

    static Platform get() {
        return PLATFORM;
    }

    CallAdapter.Factory defaultCallAdapterFactory(@Nullable Executor executor) {
        return executor != null ? new ExecutorCallAdapterFactory(executor) : DefaultCallAdapterFactory.INSTANCE;
    }

    @Nullable
    Executor defaultCallbackExecutor() {
        return null;
    }

    @Nullable
    Object invokeDefaultMethod(Method method, Class<?> cls, Object obj, @Nullable Object... objArr) throws Throwable {
        throw new UnsupportedOperationException();
    }

    boolean isDefaultMethod(Method method) {
        return false;
    }
}
