package android.support.design.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.C0029R;
import android.support.design.internal.ThemeEnforcement;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/* loaded from: classes-dex2jar.jar:android/support/design/card/MaterialCardView.class */
public class MaterialCardView extends CardView {
    private final MaterialCardViewHelper cardViewHelper;

    public MaterialCardView(Context context) {
        this(context, null);
    }

    public MaterialCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.materialCardViewStyle);
    }

    public MaterialCardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0029R.styleable.MaterialCardView, i, C0029R.style.Widget_MaterialComponents_CardView, new int[0]);
        MaterialCardViewHelper materialCardViewHelper = new MaterialCardViewHelper(this);
        this.cardViewHelper = materialCardViewHelper;
        materialCardViewHelper.loadFromAttributes(typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
    }

    public int getStrokeColor() {
        return this.cardViewHelper.getStrokeColor();
    }

    public int getStrokeWidth() {
        return this.cardViewHelper.getStrokeWidth();
    }

    @Override // android.support.v7.widget.CardView
    public void setRadius(float f) {
        super.setRadius(f);
        this.cardViewHelper.updateForeground();
    }

    public void setStrokeColor(int i) {
        this.cardViewHelper.setStrokeColor(i);
    }

    public void setStrokeWidth(int i) {
        this.cardViewHelper.setStrokeWidth(i);
    }
}
