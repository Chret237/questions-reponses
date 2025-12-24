package android.arch.lifecycle;

import com.github.clans.fab.BuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/Lifecycling.class */
public class Lifecycling {
    private static final int GENERATED_CALLBACK = 2;
    private static final int REFLECTIVE_CALLBACK = 1;
    private static Map<Class, Integer> sCallbackCache = new HashMap();
    private static Map<Class, List<Constructor<? extends GeneratedAdapter>>> sClassToAdapters = new HashMap();

    private static GeneratedAdapter createGeneratedAdapter(Constructor<? extends GeneratedAdapter> constructor, Object obj) {
        try {
            return constructor.newInstance(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    private static Constructor<? extends GeneratedAdapter> generatedConstructor(Class<?> cls) throws NoSuchMethodException, SecurityException {
        String str;
        try {
            Package r0 = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = r0 != null ? r0.getName() : BuildConfig.FLAVOR;
            if (!name.isEmpty()) {
                canonicalName = canonicalName.substring(name.length() + 1);
            }
            String adapterName = getAdapterName(canonicalName);
            if (name.isEmpty()) {
                str = adapterName;
            } else {
                str = name + "." + adapterName;
            }
            Constructor declaredConstructor = Class.forName(str).getDeclaredConstructor(cls);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return declaredConstructor;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static String getAdapterName(String str) {
        return str.replace(".", "_") + "_LifecycleAdapter";
    }

    static GenericLifecycleObserver getCallback(Object obj) {
        if (obj instanceof FullLifecycleObserver) {
            return new FullLifecycleObserverAdapter((FullLifecycleObserver) obj);
        }
        if (obj instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver) obj;
        }
        Class<?> cls = obj.getClass();
        if (getObserverConstructorType(cls) != 2) {
            return new ReflectiveGenericLifecycleObserver(obj);
        }
        List<Constructor<? extends GeneratedAdapter>> list = sClassToAdapters.get(cls);
        if (list.size() == 1) {
            return new SingleGeneratedAdapterObserver(createGeneratedAdapter(list.get(0), obj));
        }
        GeneratedAdapter[] generatedAdapterArr = new GeneratedAdapter[list.size()];
        for (int i = 0; i < list.size(); i++) {
            generatedAdapterArr[i] = createGeneratedAdapter(list.get(i), obj);
        }
        return new CompositeGeneratedAdaptersObserver(generatedAdapterArr);
    }

    private static int getObserverConstructorType(Class<?> cls) throws NoSuchMethodException, SecurityException {
        if (sCallbackCache.containsKey(cls)) {
            return sCallbackCache.get(cls).intValue();
        }
        int iResolveObserverCallbackType = resolveObserverCallbackType(cls);
        sCallbackCache.put(cls, Integer.valueOf(iResolveObserverCallbackType));
        return iResolveObserverCallbackType;
    }

    private static boolean isLifecycleParent(Class<?> cls) {
        return cls != null && LifecycleObserver.class.isAssignableFrom(cls);
    }

    private static int resolveObserverCallbackType(Class<?> cls) throws NoSuchMethodException, SecurityException {
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor<? extends GeneratedAdapter> constructorGeneratedConstructor = generatedConstructor(cls);
        if (constructorGeneratedConstructor != null) {
            sClassToAdapters.put(cls, Collections.singletonList(constructorGeneratedConstructor));
            return 2;
        }
        if (ClassesInfoCache.sInstance.hasLifecycleMethods(cls)) {
            return 1;
        }
        Class<? super Object> superclass = cls.getSuperclass();
        ArrayList arrayList = null;
        if (isLifecycleParent(superclass)) {
            if (getObserverConstructorType(superclass) == 1) {
                return 1;
            }
            arrayList = new ArrayList(sClassToAdapters.get(superclass));
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (isLifecycleParent(cls2)) {
                if (getObserverConstructorType(cls2) == 1) {
                    return 1;
                }
                ArrayList arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList();
                }
                arrayList2.addAll(sClassToAdapters.get(cls2));
                arrayList = arrayList2;
            }
        }
        if (arrayList == null) {
            return 1;
        }
        sClassToAdapters.put(cls, arrayList);
        return 2;
    }
}
