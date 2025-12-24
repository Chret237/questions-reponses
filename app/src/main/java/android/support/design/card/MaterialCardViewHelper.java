package android.support.design.card;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.C0029R;

/* loaded from: classes-dex2jar.jar:android/support/design/card/MaterialCardViewHelper.class */
class MaterialCardViewHelper {
    private static final int DEFAULT_STROKE_VALUE = -1;
    private final MaterialCardView materialCardView;
    private int strokeColor;
    private int strokeWidth;

    public MaterialCardViewHelper(MaterialCardView materialCardView) {
        this.materialCardView = materialCardView;
    }

    private void adjustContentPadding() {
        this.materialCardView.setContentPadding(this.materialCardView.getContentPaddingLeft() + this.strokeWidth, this.materialCardView.getContentPaddingTop() + this.strokeWidth, this.materialCardView.getContentPaddingRight() + this.strokeWidth, this.materialCardView.getContentPaddingBottom() + this.strokeWidth);
    }

    private Drawable createForegroundDrawable() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(this.materialCardView.getRadius());
        int i = this.strokeColor;
        if (i != -1) {
            gradientDrawable.setStroke(this.strokeWidth, i);
        }
        return gradientDrawable;
    }

    int getStrokeColor() {
        return this.strokeColor;
    }

    int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void loadFromAttributes(TypedArray typedArray) {
        this.strokeColor = typedArray.getColor(C0029R.styleable.MaterialCardView_strokeColor, -1);
        this.strokeWidth = typedArray.getDimensionPixelSize(C0029R.styleable.MaterialCardView_strokeWidth, 0);
        updateForeground();
        adjustContentPadding();
    }

    void setStrokeColor(int i) {
        this.strokeColor = i;
        updateForeground();
    }

    void setStrokeWidth(int i) {
        this.strokeWidth = i;
        updateForeground();
        adjustContentPadding();
    }

    void updateForeground() {
        this.materialCardView.setForeground(createForegroundDrawable());
    }
}
