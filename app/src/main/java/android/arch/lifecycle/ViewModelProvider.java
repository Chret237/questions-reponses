package android.arch.lifecycle;

import android.app.Application;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ViewModelProvider.class */
public class ViewModelProvider {
    private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ViewModelProvider$AndroidViewModelFactory.class */
    public static class AndroidViewModelFactory extends NewInstanceFactory {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;

        public AndroidViewModelFactory(Application application) {
            this.mApplication = application;
        }

        public static AndroidViewModelFactory getInstance(Application application) {
            if (sInstance == null) {
                sInstance = new AndroidViewModelFactory(application);
            }
            return sInstance;
        }

        @Override // android.arch.lifecycle.ViewModelProvider.NewInstanceFactory, android.arch.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            if (!AndroidViewModel.class.isAssignableFrom(cls)) {
                return (T) super.create(cls);
            }
            try {
                return cls.getConstructor(Application.class).newInstance(this.mApplication);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + cls, e);
            } catch (InstantiationException e2) {
                throw new RuntimeException("Cannot create an instance of " + cls, e2);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException("Cannot create an instance of " + cls, e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Cannot create an instance of " + cls, e4);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ViewModelProvider$Factory.class */
    public interface Factory {
        <T extends ViewModel> T create(Class<T> cls);
    }

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ViewModelProvider$NewInstanceFactory.class */
    public static class NewInstanceFactory implements Factory {
        @Override // android.arch.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            try {
                return cls.newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + cls, e);
            } catch (InstantiationException e2) {
                throw new RuntimeException("Cannot create an instance of " + cls, e2);
            }
        }
    }

    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }

    public ViewModelProvider(ViewModelStoreOwner viewModelStoreOwner, Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }

    public <T extends ViewModel> T get(Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return (T) get("android.arch.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
    }

    public <T extends ViewModel> T get(String str, Class<T> cls) {
        T t = (T) this.mViewModelStore.get(str);
        if (cls.isInstance(t)) {
            return t;
        }
        T t2 = (T) this.mFactory.create(cls);
        this.mViewModelStore.put(str, t2);
        return t2;
    }
}
