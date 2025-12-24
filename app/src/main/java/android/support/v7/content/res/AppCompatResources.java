package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ColorStateListInflaterCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;

/* loaded from: classes-dex2jar.jar:android/support/v7/content/res/AppCompatResources.class */
public final class AppCompatResources {
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<>(0);
    private static final Object sColorStateCacheLock = new Object();

    /* loaded from: classes-dex2jar.jar:android/support/v7/content/res/AppCompatResources$ColorStateListCacheEntry.class */
    private static class ColorStateListCacheEntry {
        final Configuration configuration;
        final ColorStateList value;

        ColorStateListCacheEntry(ColorStateList colorStateList, Configuration configuration) {
            this.value = colorStateList;
            this.configuration = configuration;
        }
    }

    private AppCompatResources() {
    }

    private static void addColorStateListToCache(Context context, int i, ColorStateList colorStateList) {
        synchronized (sColorStateCacheLock) {
            SparseArray<ColorStateListCacheEntry> sparseArray = sColorStateCaches.get(context);
            SparseArray<ColorStateListCacheEntry> sparseArray2 = sparseArray;
            if (sparseArray == null) {
                sparseArray2 = new SparseArray<>();
                sColorStateCaches.put(context, sparseArray2);
            }
            sparseArray2.append(i, new ColorStateListCacheEntry(colorStateList, context.getResources().getConfiguration()));
        }
    }

    private static ColorStateList getCachedColorStateList(Context context, int i) {
        ColorStateListCacheEntry colorStateListCacheEntry;
        synchronized (sColorStateCacheLock) {
            SparseArray<ColorStateListCacheEntry> sparseArray = sColorStateCaches.get(context);
            if (sparseArray != null && sparseArray.size() > 0 && (colorStateListCacheEntry = sparseArray.get(i)) != null) {
                if (colorStateListCacheEntry.configuration.equals(context.getResources().getConfiguration())) {
                    return colorStateListCacheEntry.value;
                }
                sparseArray.remove(i);
            }
            return null;
        }
    }

    public static ColorStateList getColorStateList(Context context, int i) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColorStateList(i);
        }
        ColorStateList cachedColorStateList = getCachedColorStateList(context, i);
        if (cachedColorStateList != null) {
            return cachedColorStateList;
        }
        ColorStateList colorStateListInflateColorStateList = inflateColorStateList(context, i);
        if (colorStateListInflateColorStateList == null) {
            return ContextCompat.getColorStateList(context, i);
        }
        addColorStateListToCache(context, i, colorStateListInflateColorStateList);
        return colorStateListInflateColorStateList;
    }

    public static Drawable getDrawable(Context context, int i) {
        return AppCompatDrawableManager.get().getDrawable(context, i);
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue = TL_TYPED_VALUE.get();
        TypedValue typedValue2 = typedValue;
        if (typedValue == null) {
            typedValue2 = new TypedValue();
            TL_TYPED_VALUE.set(typedValue2);
        }
        return typedValue2;
    }

    private static ColorStateList inflateColorStateList(Context context, int i) throws Resources.NotFoundException {
        if (isColorInt(context, i)) {
            return null;
        }
        Resources resources = context.getResources();
        try {
            return ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(i), context.getTheme());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to inflate ColorStateList, leaving it to the framework", e);
            return null;
        }
    }

    private static boolean isColorInt(Context context, int i) throws Resources.NotFoundException {
        Resources resources = context.getResources();
        TypedValue typedValue = getTypedValue();
        boolean z = true;
        resources.getValue(i, typedValue, true);
        if (typedValue.type < 28 || typedValue.type > 31) {
            z = false;
        }
        return z;
    }
}
