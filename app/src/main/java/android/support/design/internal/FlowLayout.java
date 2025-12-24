package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.C0029R;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes-dex2jar.jar:android/support/design/internal/FlowLayout.class */
public class FlowLayout extends ViewGroup {
    private int itemSpacing;
    private int lineSpacing;
    private boolean singleLine;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.singleLine = false;
        loadFromAttributes(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.singleLine = false;
        loadFromAttributes(context, attributeSet);
    }

    private static int getMeasuredDimension(int i, int i2, int i3) {
        return i2 != Integer.MIN_VALUE ? i2 != 1073741824 ? i3 : i : Math.min(i3, i);
    }

    private void loadFromAttributes(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C0029R.styleable.FlowLayout, 0, 0);
        this.lineSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.FlowLayout_lineSpacing, 0);
        this.itemSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.FlowLayout_itemSpacing, 0);
        typedArrayObtainStyledAttributes.recycle();
    }

    protected int getItemSpacing() {
        return this.itemSpacing;
    }

    protected int getLineSpacing() {
        return this.lineSpacing;
    }

    protected boolean isSingleLine() {
        return this.singleLine;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int marginEnd;
        int marginStart;
        if (getChildCount() == 0) {
            return;
        }
        boolean z2 = ViewCompat.getLayoutDirection(this) == 1;
        int paddingRight = z2 ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = z2 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int i5 = (i3 - i) - paddingLeft;
        int measuredWidth = paddingRight;
        int i6 = paddingTop;
        for (int i7 = 0; i7 < getChildCount(); i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginStart = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
                    marginEnd = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
                } else {
                    marginEnd = 0;
                    marginStart = 0;
                }
                int measuredWidth2 = childAt.getMeasuredWidth();
                int i8 = measuredWidth;
                int i9 = i6;
                if (!this.singleLine) {
                    i8 = measuredWidth;
                    i9 = i6;
                    if (measuredWidth + marginStart + measuredWidth2 > i5) {
                        i9 = this.lineSpacing + paddingTop;
                        i8 = paddingRight;
                    }
                }
                int i10 = i8 + marginStart;
                int measuredWidth3 = childAt.getMeasuredWidth() + i10;
                paddingTop = childAt.getMeasuredHeight() + i9;
                if (z2) {
                    childAt.layout(i5 - measuredWidth3, i9, (i5 - i8) - marginStart, paddingTop);
                } else {
                    childAt.layout(i10, i9, measuredWidth3, paddingTop);
                }
                measuredWidth = i8 + marginStart + marginEnd + childAt.getMeasuredWidth() + this.itemSpacing;
                i6 = i9;
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i6 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int i7 = paddingTop;
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i, i2);
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    i3 = marginLayoutParams.leftMargin + 0;
                    i4 = marginLayoutParams.rightMargin + 0;
                } else {
                    i3 = 0;
                    i4 = 0;
                }
                if (paddingLeft + i3 + childAt.getMeasuredWidth() <= i6 - paddingRight || isSingleLine()) {
                    i5 = paddingLeft;
                } else {
                    int paddingLeft2 = getPaddingLeft();
                    int i10 = this.lineSpacing + paddingTop;
                    i5 = paddingLeft2;
                    i7 = i10;
                }
                int measuredWidth = i5 + i3 + childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i11 = i8;
                if (measuredWidth > i8) {
                    i11 = measuredWidth;
                }
                int measuredWidth2 = i5 + i3 + i4 + childAt.getMeasuredWidth() + this.itemSpacing;
                paddingTop = i7 + measuredHeight;
                i8 = i11;
                paddingLeft = measuredWidth2;
            }
        }
        setMeasuredDimension(getMeasuredDimension(size, mode, i8), getMeasuredDimension(size2, mode2, paddingTop));
    }

    protected void setItemSpacing(int i) {
        this.itemSpacing = i;
    }

    protected void setLineSpacing(int i) {
        this.lineSpacing = i;
    }

    public void setSingleLine(boolean z) {
        this.singleLine = z;
    }
}
