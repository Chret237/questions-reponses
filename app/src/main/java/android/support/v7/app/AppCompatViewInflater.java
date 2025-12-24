package android.support.v7.app;

import android.R;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;
import com.github.clans.fab.BuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatViewInflater.class */
public class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private final Object[] mConstructorArgs = new Object[2];
    private static final Class<?>[] sConstructorSignature = {Context.class, AttributeSet.class};
    private static final int[] sOnClickAttrs = {R.attr.onClick};
    private static final String[] sClassPrefixList = {"android.widget.", "android.view.", "android.webkit."};
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap();

    /* loaded from: classes-dex2jar.jar:android/support/v7/app/AppCompatViewInflater$DeclaredOnClickListener.class */
    private static class DeclaredOnClickListener implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(View view, String str) {
            this.mHostView = view;
            this.mMethodName = str;
        }

        private void resolveMethod(Context context, String str) {
            String str2;
            Method method;
            while (context != null) {
                try {
                    if (!context.isRestricted() && (method = context.getClass().getMethod(this.mMethodName, View.class)) != null) {
                        this.mResolvedMethod = method;
                        this.mResolvedContext = context;
                        return;
                    }
                } catch (NoSuchMethodException e) {
                }
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
            int id = this.mHostView.getId();
            if (id == -1) {
                str2 = BuildConfig.FLAVOR;
            } else {
                str2 = " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(id) + "'";
            }
            throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick attribute defined on view " + this.mHostView.getClass() + str2);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (this.mResolvedMethod == null) {
                resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, view);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException("Could not execute method for android:onClick", e2);
            }
        }
    }

    private void checkOnClickListener(View view, AttributeSet attributeSet) {
        Context context = view.getContext();
        if (context instanceof ContextWrapper) {
            if (Build.VERSION.SDK_INT < 15 || ViewCompat.hasOnClickListeners(view)) {
                TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, sOnClickAttrs);
                String string = typedArrayObtainStyledAttributes.getString(0);
                if (string != null) {
                    view.setOnClickListener(new DeclaredOnClickListener(view, string));
                }
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    private View createViewByPrefix(Context context, String str, String str2) throws InflateException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        String str3;
        Constructor<? extends View> constructor = sConstructorMap.get(str);
        Constructor<? extends View> constructor2 = constructor;
        if (constructor == null) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                if (str2 != null) {
                    str3 = str2 + str;
                } else {
                    str3 = str;
                }
                constructor2 = classLoader.loadClass(str3).asSubclass(View.class).getConstructor(sConstructorSignature);
                sConstructorMap.put(str, constructor2);
            } catch (Exception e) {
                return null;
            }
        }
        constructor2.setAccessible(true);
        return constructor2.newInstance(this.mConstructorArgs);
    }

    private View createViewFromTag(Context context, String str, AttributeSet attributeSet) {
        String attributeValue = str;
        if (str.equals("view")) {
            attributeValue = attributeSet.getAttributeValue(null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 != attributeValue.indexOf(46)) {
                View viewCreateViewByPrefix = createViewByPrefix(context, attributeValue, null);
                Object[] objArr = this.mConstructorArgs;
                objArr[0] = null;
                objArr[1] = null;
                return viewCreateViewByPrefix;
            }
            for (int i = 0; i < sClassPrefixList.length; i++) {
                View viewCreateViewByPrefix2 = createViewByPrefix(context, attributeValue, sClassPrefixList[i]);
                if (viewCreateViewByPrefix2 != null) {
                    Object[] objArr2 = this.mConstructorArgs;
                    objArr2[0] = null;
                    objArr2[1] = null;
                    return viewCreateViewByPrefix2;
                }
            }
            Object[] objArr3 = this.mConstructorArgs;
            objArr3[0] = null;
            objArr3[1] = null;
            return null;
        } catch (Exception e) {
            Object[] objArr4 = this.mConstructorArgs;
            objArr4[0] = null;
            objArr4[1] = null;
            return null;
        } catch (Throwable th) {
            Object[] objArr5 = this.mConstructorArgs;
            objArr5[0] = null;
            objArr5[1] = null;
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x006f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.content.Context themifyContext(android.content.Context r6, android.util.AttributeSet r7, boolean r8, boolean r9) {
        /*
            r0 = r6
            r1 = r7
            int[] r2 = android.support.v7.appcompat.C0287R.styleable.View
            r3 = 0
            r4 = 0
            android.content.res.TypedArray r0 = r0.obtainStyledAttributes(r1, r2, r3, r4)
            r7 = r0
            r0 = r8
            if (r0 == 0) goto L1c
            r0 = r7
            int r1 = android.support.v7.appcompat.C0287R.styleable.View_android_theme
            r2 = 0
            int r0 = r0.getResourceId(r1, r2)
            r10 = r0
            goto L1f
        L1c:
            r0 = 0
            r10 = r0
        L1f:
            r0 = r10
            r11 = r0
            r0 = r9
            if (r0 == 0) goto L4f
            r0 = r10
            r11 = r0
            r0 = r10
            if (r0 != 0) goto L4f
            r0 = r7
            int r1 = android.support.v7.appcompat.C0287R.styleable.View_theme
            r2 = 0
            int r0 = r0.getResourceId(r1, r2)
            r10 = r0
            r0 = r10
            r11 = r0
            r0 = r10
            if (r0 == 0) goto L4f
            java.lang.String r0 = "AppCompatViewInflater"
            java.lang.String r1 = "app:theme is now deprecated. Please move to using android:theme instead."
            int r0 = android.util.Log.i(r0, r1)
            r0 = r10
            r11 = r0
        L4f:
            r0 = r7
            r0.recycle()
            r0 = r6
            r7 = r0
            r0 = r11
            if (r0 == 0) goto L7a
            r0 = r6
            boolean r0 = r0 instanceof android.support.v7.view.ContextThemeWrapper
            if (r0 == 0) goto L6f
            r0 = r6
            r7 = r0
            r0 = r6
            android.support.v7.view.ContextThemeWrapper r0 = (android.support.v7.view.ContextThemeWrapper) r0
            int r0 = r0.getThemeResId()
            r1 = r11
            if (r0 == r1) goto L7a
        L6f:
            android.support.v7.view.ContextThemeWrapper r0 = new android.support.v7.view.ContextThemeWrapper
            r1 = r0
            r2 = r6
            r3 = r11
            r1.<init>(r2, r3)
            r7 = r0
        L7a:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatViewInflater.themifyContext(android.content.Context, android.util.AttributeSet, boolean, boolean):android.content.Context");
    }

    private void verifyNotNull(View view, String str) {
        if (view != null) {
            return;
        }
        throw new IllegalStateException(getClass().getName() + " asked to inflate view for <" + str + ">, but returned null");
    }

    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatButton createButton(Context context, AttributeSet attributeSet) {
        return new AppCompatButton(context, attributeSet);
    }

    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckBox(context, attributeSet);
    }

    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckedTextView(context, attributeSet);
    }

    protected AppCompatEditText createEditText(Context context, AttributeSet attributeSet) {
        return new AppCompatEditText(context, attributeSet);
    }

    protected AppCompatImageButton createImageButton(Context context, AttributeSet attributeSet) {
        return new AppCompatImageButton(context, attributeSet);
    }

    protected AppCompatImageView createImageView(Context context, AttributeSet attributeSet) {
        return new AppCompatImageView(context, attributeSet);
    }

    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatMultiAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attributeSet) {
        return new AppCompatRadioButton(context, attributeSet);
    }

    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attributeSet) {
        return new AppCompatRatingBar(context, attributeSet);
    }

    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attributeSet) {
        return new AppCompatSeekBar(context, attributeSet);
    }

    protected AppCompatSpinner createSpinner(Context context, AttributeSet attributeSet) {
        return new AppCompatSpinner(context, attributeSet);
    }

    protected AppCompatTextView createTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatTextView(context, attributeSet);
    }

    protected View createView(Context context, String str, AttributeSet attributeSet) {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final android.view.View createView(android.view.View r6, java.lang.String r7, android.content.Context r8, android.util.AttributeSet r9, boolean r10, boolean r11, boolean r12, boolean r13) {
        /*
            Method dump skipped, instructions count: 751
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.AppCompatViewInflater.createView(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet, boolean, boolean, boolean, boolean):android.view.View");
    }
}
