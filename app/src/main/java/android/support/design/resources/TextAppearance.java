package android.support.design.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.C0029R;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.Log;

/* loaded from: classes-dex2jar.jar:android/support/design/resources/TextAppearance.class */
public class TextAppearance {
    private static final String TAG = "TextAppearance";
    private static final int TYPEFACE_MONOSPACE = 3;
    private static final int TYPEFACE_SANS = 1;
    private static final int TYPEFACE_SERIF = 2;
    private Typeface font;
    public final String fontFamily;
    private final int fontFamilyResourceId;
    private boolean fontResolved = false;
    public final ColorStateList shadowColor;
    public final float shadowDx;
    public final float shadowDy;
    public final float shadowRadius;
    public final boolean textAllCaps;
    public final ColorStateList textColor;
    public final ColorStateList textColorHint;
    public final ColorStateList textColorLink;
    public final float textSize;
    public final int textStyle;
    public final int typeface;

    public TextAppearance(Context context, int i) throws Resources.NotFoundException {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(i, C0029R.styleable.TextAppearance);
        this.textSize = typedArrayObtainStyledAttributes.getDimension(C0029R.styleable.TextAppearance_android_textSize, 0.0f);
        this.textColor = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TextAppearance_android_textColor);
        this.textColorHint = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TextAppearance_android_textColorHint);
        this.textColorLink = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TextAppearance_android_textColorLink);
        this.textStyle = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TextAppearance_android_textStyle, 0);
        this.typeface = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TextAppearance_android_typeface, 1);
        int indexWithValue = MaterialResources.getIndexWithValue(typedArrayObtainStyledAttributes, C0029R.styleable.TextAppearance_fontFamily, C0029R.styleable.TextAppearance_android_fontFamily);
        this.fontFamilyResourceId = typedArrayObtainStyledAttributes.getResourceId(indexWithValue, 0);
        this.fontFamily = typedArrayObtainStyledAttributes.getString(indexWithValue);
        this.textAllCaps = typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.TextAppearance_textAllCaps, false);
        this.shadowColor = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TextAppearance_android_shadowColor);
        this.shadowDx = typedArrayObtainStyledAttributes.getFloat(C0029R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.shadowDy = typedArrayObtainStyledAttributes.getFloat(C0029R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.shadowRadius = typedArrayObtainStyledAttributes.getFloat(C0029R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        typedArrayObtainStyledAttributes.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createFallbackTypeface() {
        if (this.font == null) {
            this.font = Typeface.create(this.fontFamily, this.textStyle);
        }
        if (this.font == null) {
            int i = this.typeface;
            if (i == 1) {
                this.font = Typeface.SANS_SERIF;
            } else if (i == 2) {
                this.font = Typeface.SERIF;
            } else if (i != 3) {
                this.font = Typeface.DEFAULT;
            } else {
                this.font = Typeface.MONOSPACE;
            }
            Typeface typeface = this.font;
            if (typeface != null) {
                this.font = Typeface.create(typeface, this.textStyle);
            }
        }
    }

    public Typeface getFont(Context context) {
        if (this.fontResolved) {
            return this.font;
        }
        if (!context.isRestricted()) {
            try {
                try {
                    Typeface font = ResourcesCompat.getFont(context, this.fontFamilyResourceId);
                    this.font = font;
                    if (font != null) {
                        this.font = Typeface.create(font, this.textStyle);
                    }
                } catch (Resources.NotFoundException | UnsupportedOperationException e) {
                }
            } catch (Exception e2) {
                Log.d(TAG, "Error loading font " + this.fontFamily, e2);
            }
        }
        createFallbackTypeface();
        this.fontResolved = true;
        return this.font;
    }

    public void getFontAsync(Context context, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        if (this.fontResolved) {
            updateTextPaintMeasureState(textPaint, this.font);
            return;
        }
        createFallbackTypeface();
        if (context.isRestricted()) {
            this.fontResolved = true;
            updateTextPaintMeasureState(textPaint, this.font);
            return;
        }
        try {
            ResourcesCompat.getFont(context, this.fontFamilyResourceId, new ResourcesCompat.FontCallback(this, textPaint, fontCallback) { // from class: android.support.design.resources.TextAppearance.1
                final TextAppearance this$0;
                final ResourcesCompat.FontCallback val$callback;
                final TextPaint val$textPaint;

                {
                    this.this$0 = this;
                    this.val$textPaint = textPaint;
                    this.val$callback = fontCallback;
                }

                @Override // android.support.v4.content.res.ResourcesCompat.FontCallback
                public void onFontRetrievalFailed(int i) {
                    this.this$0.createFallbackTypeface();
                    this.this$0.fontResolved = true;
                    this.val$callback.onFontRetrievalFailed(i);
                }

                @Override // android.support.v4.content.res.ResourcesCompat.FontCallback
                public void onFontRetrieved(Typeface typeface) {
                    TextAppearance textAppearance = this.this$0;
                    textAppearance.font = Typeface.create(typeface, textAppearance.textStyle);
                    this.this$0.updateTextPaintMeasureState(this.val$textPaint, typeface);
                    this.this$0.fontResolved = true;
                    this.val$callback.onFontRetrieved(typeface);
                }
            }, null);
        } catch (Resources.NotFoundException | UnsupportedOperationException e) {
        } catch (Exception e2) {
            Log.d(TAG, "Error loading font " + this.fontFamily, e2);
        }
    }

    public void updateDrawState(Context context, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        updateMeasureState(context, textPaint, fontCallback);
        ColorStateList colorStateList = this.textColor;
        textPaint.setColor(colorStateList != null ? colorStateList.getColorForState(textPaint.drawableState, this.textColor.getDefaultColor()) : -16777216);
        float f = this.shadowRadius;
        float f2 = this.shadowDx;
        float f3 = this.shadowDy;
        ColorStateList colorStateList2 = this.shadowColor;
        textPaint.setShadowLayer(f, f2, f3, colorStateList2 != null ? colorStateList2.getColorForState(textPaint.drawableState, this.shadowColor.getDefaultColor()) : 0);
    }

    public void updateMeasureState(Context context, TextPaint textPaint, ResourcesCompat.FontCallback fontCallback) {
        if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
            updateTextPaintMeasureState(textPaint, getFont(context));
            return;
        }
        getFontAsync(context, textPaint, fontCallback);
        if (this.fontResolved) {
            return;
        }
        updateTextPaintMeasureState(textPaint, this.font);
    }

    public void updateTextPaintMeasureState(TextPaint textPaint, Typeface typeface) {
        textPaint.setTypeface(typeface);
        int style = (typeface.getStyle() ^ (-1)) & this.textStyle;
        textPaint.setFakeBoldText((style & 1) != 0);
        textPaint.setTextSkewX((style & 2) != 0 ? -0.25f : 0.0f);
        textPaint.setTextSize(this.textSize);
    }
}
