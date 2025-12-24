package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.graphics.drawable.AnimatedStateListDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager.class */
public final class AppCompatDrawableManager {
    private static final boolean DEBUG = false;
    private static AppCompatDrawableManager INSTANCE;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManag";
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<>(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {C0287R.drawable.abc_textfield_search_default_mtrl_alpha, C0287R.drawable.abc_textfield_default_mtrl_alpha, C0287R.drawable.abc_ab_share_pack_mtrl_alpha};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = {C0287R.drawable.abc_ic_commit_search_api_mtrl_alpha, C0287R.drawable.abc_seekbar_tick_mark_material, C0287R.drawable.abc_ic_menu_share_mtrl_alpha, C0287R.drawable.abc_ic_menu_copy_mtrl_am_alpha, C0287R.drawable.abc_ic_menu_cut_mtrl_alpha, C0287R.drawable.abc_ic_menu_selectall_mtrl_alpha, C0287R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {C0287R.drawable.abc_textfield_activated_mtrl_alpha, C0287R.drawable.abc_textfield_search_activated_mtrl_alpha, C0287R.drawable.abc_cab_background_top_mtrl_alpha, C0287R.drawable.abc_text_cursor_material, C0287R.drawable.abc_text_select_handle_left_mtrl_dark, C0287R.drawable.abc_text_select_handle_middle_mtrl_dark, C0287R.drawable.abc_text_select_handle_right_mtrl_dark, C0287R.drawable.abc_text_select_handle_left_mtrl_light, C0287R.drawable.abc_text_select_handle_middle_mtrl_light, C0287R.drawable.abc_text_select_handle_right_mtrl_light};
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {C0287R.drawable.abc_popup_background_mtrl_mult, C0287R.drawable.abc_cab_background_internal_bg, C0287R.drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = {C0287R.drawable.abc_tab_indicator_material, C0287R.drawable.abc_textfield_search_material};
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = {C0287R.drawable.abc_btn_check_material, C0287R.drawable.abc_btn_radio_material};

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager$AsldcInflateDelegate.class */
    static class AsldcInflateDelegate implements InflateDelegate {
        AsldcInflateDelegate() {
        }

        @Override // android.support.v7.widget.AppCompatDrawableManager.InflateDelegate
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return AnimatedStateListDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", e);
                return null;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager$AvdcInflateDelegate.class */
    private static class AvdcInflateDelegate implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        @Override // android.support.v7.widget.AppCompatDrawableManager.InflateDelegate
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager$ColorFilterLruCache.class */
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int i) {
            super(i);
        }

        private static int generateCacheKey(int i, PorterDuff.Mode mode) {
            return ((i + 31) * 31) + mode.hashCode();
        }

        PorterDuffColorFilter get(int i, PorterDuff.Mode mode) {
            return get(Integer.valueOf(generateCacheKey(i, mode)));
        }

        PorterDuffColorFilter put(int i, PorterDuff.Mode mode, PorterDuffColorFilter porterDuffColorFilter) {
            return put(Integer.valueOf(generateCacheKey(i, mode)), porterDuffColorFilter);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager$InflateDelegate.class */
    private interface InflateDelegate {
        Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatDrawableManager$VdcInflateDelegate.class */
    private static class VdcInflateDelegate implements InflateDelegate {
        VdcInflateDelegate() {
        }

        @Override // android.support.v7.widget.AppCompatDrawableManager.InflateDelegate
        public Drawable createFromXmlInner(Context context, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, attributeSet, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    private void addDelegate(String str, InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<>();
        }
        this.mDelegates.put(str, inflateDelegate);
    }

    private boolean addDrawableToCache(Context context, long j, Drawable drawable) {
        synchronized (this) {
            Drawable.ConstantState constantState = drawable.getConstantState();
            if (constantState == null) {
                return false;
            }
            LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
            LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray2 = longSparseArray;
            if (longSparseArray == null) {
                longSparseArray2 = new LongSparseArray<>();
                this.mDrawableCaches.put(context, longSparseArray2);
            }
            longSparseArray2.put(j, new WeakReference<>(constantState));
            return true;
        }
    }

    private void addTintListToCache(Context context, int i, ColorStateList colorStateList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<>();
        }
        SparseArrayCompat<ColorStateList> sparseArrayCompat = this.mTintLists.get(context);
        SparseArrayCompat<ColorStateList> sparseArrayCompat2 = sparseArrayCompat;
        if (sparseArrayCompat == null) {
            sparseArrayCompat2 = new SparseArrayCompat<>();
            this.mTintLists.put(context, sparseArrayCompat2);
        }
        sparseArrayCompat2.append(i, colorStateList);
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private void checkVectorDrawableSetup(Context context) {
        if (this.mHasCheckedVectorDrawableSetup) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = true;
        Drawable drawable = getDrawable(context, C0287R.drawable.abc_vector_test);
        if (drawable == null || !isVectorDrawable(drawable)) {
            this.mHasCheckedVectorDrawableSetup = false;
            throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
        }
    }

    private ColorStateList createBorderlessButtonColorStateList(Context context) {
        return createButtonColorStateList(context, 0);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [int[], int[][]] */
    private ColorStateList createButtonColorStateList(Context context, int i) {
        int themeAttrColor = ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlHighlight);
        int disabledThemeAttrColor = ThemeUtils.getDisabledThemeAttrColor(context, C0287R.attr.colorButtonNormal);
        int[] iArr = ThemeUtils.DISABLED_STATE_SET;
        int[] iArr2 = ThemeUtils.PRESSED_STATE_SET;
        int iCompositeColors = ColorUtils.compositeColors(themeAttrColor, i);
        return new ColorStateList(new int[]{iArr, iArr2, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET}, new int[]{disabledThemeAttrColor, iCompositeColors, ColorUtils.compositeColors(themeAttrColor, i), i});
    }

    private static long createCacheKey(TypedValue typedValue) {
        return (typedValue.assetCookie << 32) | typedValue.data;
    }

    private ColorStateList createColoredButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorAccent));
    }

    private ColorStateList createDefaultButtonColorStateList(Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorButtonNormal));
    }

    private Drawable createDrawableIfNeeded(Context context, int i) throws Resources.NotFoundException {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        context.getResources().getValue(i, typedValue, true);
        long jCreateCacheKey = createCacheKey(typedValue);
        Drawable cachedDrawable = getCachedDrawable(context, jCreateCacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        if (i == C0287R.drawable.abc_cab_background_top_material) {
            cachedDrawable = new LayerDrawable(new Drawable[]{getDrawable(context, C0287R.drawable.abc_cab_background_internal_bg), getDrawable(context, C0287R.drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (cachedDrawable != null) {
            cachedDrawable.setChangingConfigurations(typedValue.changingConfigurations);
            addDrawableToCache(context, jCreateCacheKey, cachedDrawable);
        }
        return cachedDrawable;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int[], int[][]] */
    private ColorStateList createSwitchThumbColorStateList(Context context) {
        ?? r0 = new int[3];
        int[] iArr = new int[3];
        ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, C0287R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList == null || !themeAttrColorStateList.isStateful()) {
            r0[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr[0] = ThemeUtils.getDisabledThemeAttrColor(context, C0287R.attr.colorSwitchThumbNormal);
            r0[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr[1] = ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlActivated);
            r0[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr[2] = ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorSwitchThumbNormal);
        } else {
            r0[0] = ThemeUtils.DISABLED_STATE_SET;
            iArr[0] = themeAttrColorStateList.getColorForState(r0[0], 0);
            r0[1] = ThemeUtils.CHECKED_STATE_SET;
            iArr[1] = ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlActivated);
            r0[2] = ThemeUtils.EMPTY_STATE_SET;
            iArr[2] = themeAttrColorStateList.getDefaultColor();
        }
        return new ColorStateList(r0, iArr);
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode, int[] iArr) {
        if (colorStateList == null || mode == null) {
            return null;
        }
        return getPorterDuffColorFilter(colorStateList.getColorForState(iArr, 0), mode);
    }

    public static AppCompatDrawableManager get() {
        AppCompatDrawableManager appCompatDrawableManager;
        synchronized (AppCompatDrawableManager.class) {
            try {
                if (INSTANCE == null) {
                    AppCompatDrawableManager appCompatDrawableManager2 = new AppCompatDrawableManager();
                    INSTANCE = appCompatDrawableManager2;
                    installDefaultInflateDelegates(appCompatDrawableManager2);
                }
                appCompatDrawableManager = INSTANCE;
            } catch (Throwable th) {
                throw th;
            }
        }
        return appCompatDrawableManager;
    }

    private Drawable getCachedDrawable(Context context, long j) {
        synchronized (this) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
            if (longSparseArray == null) {
                return null;
            }
            WeakReference<Drawable.ConstantState> weakReference = longSparseArray.get(j);
            if (weakReference != null) {
                Drawable.ConstantState constantState = weakReference.get();
                if (constantState != null) {
                    return constantState.newDrawable(context.getResources());
                }
                longSparseArray.delete(j);
            }
            return null;
        }
    }

    public static PorterDuffColorFilter getPorterDuffColorFilter(int i, PorterDuff.Mode mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        synchronized (AppCompatDrawableManager.class) {
            try {
                PorterDuffColorFilter porterDuffColorFilter2 = COLOR_FILTER_CACHE.get(i, mode);
                porterDuffColorFilter = porterDuffColorFilter2;
                if (porterDuffColorFilter2 == null) {
                    porterDuffColorFilter = new PorterDuffColorFilter(i, mode);
                    COLOR_FILTER_CACHE.put(i, mode, porterDuffColorFilter);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return porterDuffColorFilter;
    }

    private ColorStateList getTintListFromCache(Context context, int i) {
        WeakHashMap<Context, SparseArrayCompat<ColorStateList>> weakHashMap = this.mTintLists;
        ColorStateList colorStateList = null;
        if (weakHashMap != null) {
            SparseArrayCompat<ColorStateList> sparseArrayCompat = weakHashMap.get(context);
            colorStateList = null;
            if (sparseArrayCompat != null) {
                colorStateList = sparseArrayCompat.get(i);
            }
        }
        return colorStateList;
    }

    static PorterDuff.Mode getTintMode(int i) {
        return i == C0287R.drawable.abc_switch_thumb_material ? PorterDuff.Mode.MULTIPLY : null;
    }

    private static void installDefaultInflateDelegates(AppCompatDrawableManager appCompatDrawableManager) {
        if (Build.VERSION.SDK_INT < 24) {
            appCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-selector", new AsldcInflateDelegate());
        }
    }

    private static boolean isVectorDrawable(Drawable drawable) {
        return (drawable instanceof VectorDrawableCompat) || PLATFORM_VD_CLAZZ.equals(drawable.getClass().getName());
    }

    private Drawable loadDrawableFromDelegates(Context context, int i) throws XmlPullParserException, Resources.NotFoundException, IOException {
        int next;
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap == null || arrayMap.isEmpty()) {
            return null;
        }
        SparseArrayCompat<String> sparseArrayCompat = this.mKnownDrawableIdTags;
        if (sparseArrayCompat != null) {
            String str = sparseArrayCompat.get(i);
            if (SKIP_DRAWABLE_TAG.equals(str)) {
                return null;
            }
            if (str != null && this.mDelegates.get(str) == null) {
                return null;
            }
        } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue typedValue = this.mTypedValue;
        Resources resources = context.getResources();
        resources.getValue(i, typedValue, true);
        long jCreateCacheKey = createCacheKey(typedValue);
        Drawable cachedDrawable = getCachedDrawable(context, jCreateCacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        Drawable drawable = cachedDrawable;
        if (typedValue.string != null) {
            drawable = cachedDrawable;
            if (typedValue.string.toString().endsWith(".xml")) {
                drawable = cachedDrawable;
                try {
                    XmlResourceParser xml = resources.getXml(i);
                    AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(xml);
                    do {
                        next = xml.next();
                        if (next == 2) {
                            break;
                        }
                    } while (next != 1);
                    if (next != 2) {
                        throw new XmlPullParserException("No start tag found");
                    }
                    String name = xml.getName();
                    this.mKnownDrawableIdTags.append(i, name);
                    InflateDelegate inflateDelegate = this.mDelegates.get(name);
                    Drawable drawableCreateFromXmlInner = cachedDrawable;
                    if (inflateDelegate != null) {
                        drawableCreateFromXmlInner = inflateDelegate.createFromXmlInner(context, xml, attributeSetAsAttributeSet, context.getTheme());
                    }
                    drawable = drawableCreateFromXmlInner;
                    if (drawableCreateFromXmlInner != null) {
                        drawableCreateFromXmlInner.setChangingConfigurations(typedValue.changingConfigurations);
                        Drawable drawable2 = drawableCreateFromXmlInner;
                        addDrawableToCache(context, jCreateCacheKey, drawableCreateFromXmlInner);
                        drawable = drawableCreateFromXmlInner;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception while inflating drawable", e);
                }
            }
        }
        if (drawable == null) {
            this.mKnownDrawableIdTags.append(i, SKIP_DRAWABLE_TAG);
        }
        return drawable;
    }

    private void removeDelegate(String str, InflateDelegate inflateDelegate) {
        ArrayMap<String, InflateDelegate> arrayMap = this.mDelegates;
        if (arrayMap == null || arrayMap.get(str) != inflateDelegate) {
            return;
        }
        this.mDelegates.remove(str);
    }

    private static void setPorterDuffColorFilter(Drawable drawable, int i, PorterDuff.Mode mode) {
        Drawable drawableMutate = drawable;
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawableMutate = drawable.mutate();
        }
        PorterDuff.Mode mode2 = mode;
        if (mode == null) {
            mode2 = DEFAULT_MODE;
        }
        drawableMutate.setColorFilter(getPorterDuffColorFilter(i, mode2));
    }

    private Drawable tintDrawable(Context context, int i, boolean z, Drawable drawable) {
        Drawable drawable2;
        ColorStateList tintList = getTintList(context, i);
        if (tintList != null) {
            Drawable drawableMutate = drawable;
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawableMutate = drawable.mutate();
            }
            Drawable drawableWrap = DrawableCompat.wrap(drawableMutate);
            DrawableCompat.setTintList(drawableWrap, tintList);
            PorterDuff.Mode tintMode = getTintMode(i);
            drawable2 = drawableWrap;
            if (tintMode != null) {
                DrawableCompat.setTintMode(drawableWrap, tintMode);
                drawable2 = drawableWrap;
            }
        } else if (i == C0287R.drawable.abc_seekbar_track_material) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.background), ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.secondaryProgress), ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(R.id.progress), ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlActivated), DEFAULT_MODE);
            drawable2 = drawable;
        } else if (i == C0287R.drawable.abc_ratingbar_material || i == C0287R.drawable.abc_ratingbar_indicator_material || i == C0287R.drawable.abc_ratingbar_small_material) {
            LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.background), ThemeUtils.getDisabledThemeAttrColor(context, C0287R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.secondaryProgress), ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(R.id.progress), ThemeUtils.getThemeAttrColor(context, C0287R.attr.colorControlActivated), DEFAULT_MODE);
            drawable2 = drawable;
        } else {
            drawable2 = drawable;
            if (!tintDrawableUsingColorFilter(context, i, drawable)) {
                drawable2 = drawable;
                if (z) {
                    drawable2 = null;
                }
            }
        }
        return drawable2;
    }

    static void tintDrawable(Drawable drawable, TintInfo tintInfo, int[] iArr) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable) && drawable.mutate() != drawable) {
            Log.d(TAG, "Mutated drawable is not the same instance as the input.");
            return;
        }
        if (tintInfo.mHasTintList || tintInfo.mHasTintMode) {
            drawable.setColorFilter(createTintFilter(tintInfo.mHasTintList ? tintInfo.mTintList : null, tintInfo.mHasTintMode ? tintInfo.mTintMode : DEFAULT_MODE, iArr));
        } else {
            drawable.clearColorFilter();
        }
        if (Build.VERSION.SDK_INT <= 23) {
            drawable.invalidateSelf();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00a1 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static boolean tintDrawableUsingColorFilter(android.content.Context r4, int r5, android.graphics.drawable.Drawable r6) {
        /*
            android.graphics.PorterDuff$Mode r0 = android.support.v7.widget.AppCompatDrawableManager.DEFAULT_MODE
            r10 = r0
            int[] r0 = android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_TINT_COLOR_CONTROL_NORMAL
            r1 = r5
            boolean r0 = arrayContains(r0, r1)
            r9 = r0
            r0 = 16842801(0x1010031, float:2.3693695E-38)
            r7 = r0
            r0 = r9
            if (r0 == 0) goto L23
            int r0 = android.support.v7.appcompat.C0287R.attr.colorControlNormal
            r5 = r0
        L1b:
            r0 = -1
            r7 = r0
        L1d:
            r0 = 1
            r8 = r0
            goto L70
        L23:
            int[] r0 = android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_COLOR_CONTROL_ACTIVATED
            r1 = r5
            boolean r0 = arrayContains(r0, r1)
            if (r0 == 0) goto L34
            int r0 = android.support.v7.appcompat.C0287R.attr.colorControlActivated
            r5 = r0
            goto L1b
        L34:
            int[] r0 = android.support.v7.widget.AppCompatDrawableManager.COLORFILTER_COLOR_BACKGROUND_MULTIPLY
            r1 = r5
            boolean r0 = arrayContains(r0, r1)
            if (r0 == 0) goto L48
            android.graphics.PorterDuff$Mode r0 = android.graphics.PorterDuff.Mode.MULTIPLY
            r10 = r0
            r0 = r7
            r5 = r0
            goto L1b
        L48:
            r0 = r5
            int r1 = android.support.v7.appcompat.C0287R.drawable.abc_list_divider_mtrl_alpha
            if (r0 != r1) goto L5d
            r0 = 16842800(0x1010030, float:2.3693693E-38)
            r5 = r0
            r0 = 1109603123(0x42233333, float:40.8)
            int r0 = java.lang.Math.round(r0)
            r7 = r0
            goto L1d
        L5d:
            r0 = r5
            int r1 = android.support.v7.appcompat.C0287R.drawable.abc_dialog_material_background
            if (r0 != r1) goto L69
            r0 = r7
            r5 = r0
            goto L1b
        L69:
            r0 = -1
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = 0
            r5 = r0
        L70:
            r0 = r8
            if (r0 == 0) goto La1
            r0 = r6
            r11 = r0
            r0 = r6
            boolean r0 = android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(r0)
            if (r0 == 0) goto L85
            r0 = r6
            android.graphics.drawable.Drawable r0 = r0.mutate()
            r11 = r0
        L85:
            r0 = r11
            r1 = r4
            r2 = r5
            int r1 = android.support.v7.widget.ThemeUtils.getThemeAttrColor(r1, r2)
            r2 = r10
            android.graphics.PorterDuffColorFilter r1 = getPorterDuffColorFilter(r1, r2)
            r0.setColorFilter(r1)
            r0 = r7
            r1 = -1
            if (r0 == r1) goto L9f
            r0 = r11
            r1 = r7
            r0.setAlpha(r1)
        L9f:
            r0 = 1
            return r0
        La1:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatDrawableManager.tintDrawableUsingColorFilter(android.content.Context, int, android.graphics.drawable.Drawable):boolean");
    }

    public Drawable getDrawable(Context context, int i) {
        Drawable drawable;
        synchronized (this) {
            drawable = getDrawable(context, i, false);
        }
        return drawable;
    }

    Drawable getDrawable(Context context, int i, boolean z) {
        Drawable drawableTintDrawable;
        synchronized (this) {
            checkVectorDrawableSetup(context);
            Drawable drawableLoadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
            Drawable drawableCreateDrawableIfNeeded = drawableLoadDrawableFromDelegates;
            if (drawableLoadDrawableFromDelegates == null) {
                drawableCreateDrawableIfNeeded = createDrawableIfNeeded(context, i);
            }
            Drawable drawable = drawableCreateDrawableIfNeeded;
            if (drawableCreateDrawableIfNeeded == null) {
                drawable = ContextCompat.getDrawable(context, i);
            }
            drawableTintDrawable = drawable;
            if (drawable != null) {
                drawableTintDrawable = tintDrawable(context, i, z, drawable);
            }
            if (drawableTintDrawable != null) {
                DrawableUtils.fixDrawable(drawableTintDrawable);
            }
        }
        return drawableTintDrawable;
    }

    ColorStateList getTintList(Context context, int i) {
        ColorStateList colorStateList;
        synchronized (this) {
            ColorStateList tintListFromCache = getTintListFromCache(context, i);
            colorStateList = tintListFromCache;
            if (tintListFromCache == null) {
                if (i == C0287R.drawable.abc_edit_text_material) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_edittext);
                } else if (i == C0287R.drawable.abc_switch_track_mtrl_alpha) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_switch_track);
                } else if (i == C0287R.drawable.abc_switch_thumb_material) {
                    tintListFromCache = createSwitchThumbColorStateList(context);
                } else if (i == C0287R.drawable.abc_btn_default_mtrl_shape) {
                    tintListFromCache = createDefaultButtonColorStateList(context);
                } else if (i == C0287R.drawable.abc_btn_borderless_material) {
                    tintListFromCache = createBorderlessButtonColorStateList(context);
                } else if (i == C0287R.drawable.abc_btn_colored_material) {
                    tintListFromCache = createColoredButtonColorStateList(context);
                } else if (i == C0287R.drawable.abc_spinner_mtrl_am_alpha || i == C0287R.drawable.abc_spinner_textfield_background_material) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_spinner);
                } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, i)) {
                    tintListFromCache = ThemeUtils.getThemeAttrColorStateList(context, C0287R.attr.colorControlNormal);
                } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, i)) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_default);
                } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, i)) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_btn_checkable);
                } else if (i == C0287R.drawable.abc_seekbar_thumb_material) {
                    tintListFromCache = AppCompatResources.getColorStateList(context, C0287R.color.abc_tint_seek_thumb);
                }
                colorStateList = tintListFromCache;
                if (tintListFromCache != null) {
                    addTintListToCache(context, i, tintListFromCache);
                    colorStateList = tintListFromCache;
                }
            }
        }
        return colorStateList;
    }

    public void onConfigurationChanged(Context context) {
        synchronized (this) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> longSparseArray = this.mDrawableCaches.get(context);
            if (longSparseArray != null) {
                longSparseArray.clear();
            }
        }
    }

    Drawable onDrawableLoadedFromResources(Context context, VectorEnabledTintResources vectorEnabledTintResources, int i) {
        synchronized (this) {
            Drawable drawableLoadDrawableFromDelegates = loadDrawableFromDelegates(context, i);
            Drawable drawableSuperGetDrawable = drawableLoadDrawableFromDelegates;
            if (drawableLoadDrawableFromDelegates == null) {
                drawableSuperGetDrawable = vectorEnabledTintResources.superGetDrawable(i);
            }
            if (drawableSuperGetDrawable == null) {
                return null;
            }
            return tintDrawable(context, i, false, drawableSuperGetDrawable);
        }
    }
}
