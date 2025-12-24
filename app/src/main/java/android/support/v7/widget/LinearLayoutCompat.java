package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.C0287R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/LinearLayoutCompat.class */
public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/LinearLayoutCompat$DividerMode.class */
    public @interface DividerMode {
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/LinearLayoutCompat$LayoutParams.class */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0287R.styleable.LinearLayoutCompat_Layout);
            this.weight = typedArrayObtainStyledAttributes.getFloat(C0287R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = typedArrayObtainStyledAttributes.getInt(C0287R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            typedArrayObtainStyledAttributes.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/LinearLayoutCompat$OrientationMode.class */
    public @interface OrientationMode {
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0287R.styleable.LinearLayoutCompat, i, 0);
        int i2 = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = tintTypedArrayObtainStyledAttributes.getFloat(C0287R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.LinearLayoutCompat_dividerPadding, 0);
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    private void forceUniformHeight(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, iMakeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, iMakeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int i;
        int virtualChildCount = getVirtualChildCount();
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i2)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                drawVerticalDivider(canvas, zIsLayoutRtl ? virtualChildAt.getRight() + layoutParams.rightMargin : (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (zIsLayoutRtl) {
                    left = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i = this.mDividerWidth;
                    right = left - i;
                } else {
                    right = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                }
            } else if (zIsLayoutRtl) {
                right = getPaddingLeft();
            } else {
                left = getWidth() - getPaddingRight();
                i = this.mDividerWidth;
                right = left - i;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            drawHorizontalDivider(canvas, virtualChildAt2 == null ? (getHeight() - getPaddingBottom()) - this.mDividerHeight : virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.View
    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount <= i) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(i);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            }
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
        int i2 = this.mBaselineChildTop;
        int bottom = i2;
        if (this.mOrientation == 1) {
            int i3 = this.mGravity & 112;
            bottom = i2;
            if (i3 != 48) {
                bottom = i3 != 16 ? i3 != 80 ? i2 : ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength : i2 + (((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2);
            }
        }
        return bottom + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getGravity() {
        return this.mGravity;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    protected boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            boolean z2 = false;
            if ((this.mShowDividers & 1) != 0) {
                z2 = true;
            }
            return z2;
        }
        if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        }
        boolean z3 = false;
        if ((this.mShowDividers & 2) != 0) {
            while (true) {
                i--;
                z3 = false;
                if (i < 0) {
                    break;
                }
                if (getChildAt(i).getVisibility() != 8) {
                    z3 = true;
                    break;
                }
            }
        }
        return z3;
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int measuredHeight;
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i7 = i4 - i2;
        int paddingBottom = getPaddingBottom();
        int paddingBottom2 = getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i8 = this.mGravity;
        int i9 = i8 & 112;
        boolean z = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(8388615 & i8, ViewCompat.getLayoutDirection(this));
        int paddingLeft = absoluteGravity != 1 ? absoluteGravity != 5 ? getPaddingLeft() : ((getPaddingLeft() + i3) - i) - this.mTotalLength : getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        if (zIsLayoutRtl) {
            i5 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i5 = 0;
            i6 = 1;
        }
        int childrenSkipCount = 0;
        while (childrenSkipCount < virtualChildCount) {
            int i10 = i5 + (i6 * childrenSkipCount);
            View virtualChildAt = getVirtualChildAt(i10);
            if (virtualChildAt == null) {
                paddingLeft += measureNullChild(i10);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight2 = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int baseline = (!z || layoutParams.height == -1) ? -1 : virtualChildAt.getBaseline();
                int i11 = layoutParams.gravity;
                int i12 = i11;
                if (i11 < 0) {
                    i12 = i9;
                }
                int i13 = i12 & 112;
                if (i13 == 16) {
                    measuredHeight = ((((((i7 - paddingTop) - paddingBottom2) - measuredHeight2) / 2) + paddingTop) + layoutParams.topMargin) - layoutParams.bottomMargin;
                } else if (i13 == 48) {
                    int i14 = layoutParams.topMargin + paddingTop;
                    measuredHeight = i14;
                    if (baseline != -1) {
                        measuredHeight = i14 + (iArr[1] - baseline);
                    }
                } else if (i13 != 80) {
                    measuredHeight = paddingTop;
                } else {
                    int i15 = ((i7 - paddingBottom) - measuredHeight2) - layoutParams.bottomMargin;
                    measuredHeight = i15;
                    if (baseline != -1) {
                        measuredHeight = i15 - (iArr2[2] - (virtualChildAt.getMeasuredHeight() - baseline));
                    }
                }
                int i16 = paddingLeft;
                if (hasDividerBeforeChildAt(i10)) {
                    i16 = paddingLeft + this.mDividerWidth;
                }
                int i17 = layoutParams.leftMargin + i16;
                setChildFrame(virtualChildAt, i17 + getLocationOffset(virtualChildAt), measuredHeight, measuredWidth, measuredHeight2);
                int i18 = layoutParams.rightMargin;
                int nextLocationOffset = getNextLocationOffset(virtualChildAt);
                childrenSkipCount += getChildrenSkipCount(virtualChildAt, i10);
                paddingLeft = i17 + measuredWidth + i18 + nextLocationOffset;
            }
            childrenSkipCount++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void layoutVertical(int r8, int r9, int r10, int r11) {
        /*
            Method dump skipped, instructions count: 372
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.LinearLayoutCompat.layoutVertical(int, int, int, int):void");
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x03e1  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0291  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x02ed  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0312  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void measureHorizontal(int r9, int r10) {
        /*
            Method dump skipped, instructions count: 2165
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.LinearLayoutCompat.measureHorizontal(int, int):void");
    }

    int measureNullChild(int i) {
        return 0;
    }

    void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        boolean z;
        int iMax;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i6 = this.mBaselineAlignedChildIndex;
        boolean z2 = this.mUseLargestChild;
        float f = 0.0f;
        int i7 = 0;
        int iMax2 = 0;
        int iMax3 = 0;
        int iMax4 = 0;
        int iMax5 = 0;
        int childrenSkipCount = 0;
        boolean z3 = false;
        boolean z4 = true;
        boolean z5 = false;
        while (childrenSkipCount < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(childrenSkipCount);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(childrenSkipCount);
            } else if (virtualChildAt.getVisibility() == 8) {
                childrenSkipCount += getChildrenSkipCount(virtualChildAt, childrenSkipCount);
            } else {
                if (hasDividerBeforeChildAt(childrenSkipCount)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                f += layoutParams.weight;
                if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    int i8 = this.mTotalLength;
                    this.mTotalLength = Math.max(i8, layoutParams.topMargin + i8 + layoutParams.bottomMargin);
                    z3 = true;
                } else {
                    if (layoutParams.height != 0 || layoutParams.weight <= 0.0f) {
                        i5 = Integer.MIN_VALUE;
                    } else {
                        layoutParams.height = -2;
                        i5 = 0;
                    }
                    measureChildBeforeLayout(virtualChildAt, childrenSkipCount, i, 0, i2, f == 0.0f ? this.mTotalLength : 0);
                    if (i5 != Integer.MIN_VALUE) {
                        layoutParams.height = i5;
                    }
                    int measuredHeight = virtualChildAt.getMeasuredHeight();
                    int i9 = this.mTotalLength;
                    this.mTotalLength = Math.max(i9, i9 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(virtualChildAt));
                    if (z2) {
                        iMax3 = Math.max(measuredHeight, iMax3);
                    }
                }
                if (i6 >= 0 && i6 == childrenSkipCount + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (childrenSkipCount < i6 && layoutParams.weight > 0.0f) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                if (mode == 1073741824 || layoutParams.width != -1) {
                    z = false;
                } else {
                    z = true;
                    z5 = true;
                }
                int i10 = layoutParams.leftMargin + layoutParams.rightMargin;
                int measuredWidth = virtualChildAt.getMeasuredWidth() + i10;
                iMax2 = Math.max(iMax2, measuredWidth);
                int iCombineMeasuredStates = View.combineMeasuredStates(i7, virtualChildAt.getMeasuredState());
                boolean z6 = z4 && layoutParams.width == -1;
                if (layoutParams.weight > 0.0f) {
                    if (!z) {
                        i10 = measuredWidth;
                    }
                    iMax4 = Math.max(iMax4, i10);
                    iMax = iMax5;
                } else {
                    if (!z) {
                        i10 = measuredWidth;
                    }
                    iMax = Math.max(iMax5, i10);
                }
                iMax5 = iMax;
                childrenSkipCount = getChildrenSkipCount(virtualChildAt, childrenSkipCount) + childrenSkipCount;
                z4 = z6;
                i7 = iCombineMeasuredStates;
            }
            childrenSkipCount++;
        }
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (z2 && (mode2 == Integer.MIN_VALUE || mode2 == 0)) {
            this.mTotalLength = 0;
            int childrenSkipCount2 = 0;
            while (childrenSkipCount2 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(childrenSkipCount2);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(childrenSkipCount2);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    childrenSkipCount2 += getChildrenSkipCount(virtualChildAt2, childrenSkipCount2);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    int i11 = this.mTotalLength;
                    this.mTotalLength = Math.max(i11, i11 + iMax3 + layoutParams2.topMargin + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt2));
                }
                childrenSkipCount2++;
            }
        }
        int paddingTop = this.mTotalLength + getPaddingTop() + getPaddingBottom();
        this.mTotalLength = paddingTop;
        int iResolveSizeAndState = View.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i2, 0);
        int i12 = (16777215 & iResolveSizeAndState) - this.mTotalLength;
        if (z3 || (i12 != 0 && f > 0.0f)) {
            float f2 = this.mWeightSum;
            if (f2 > 0.0f) {
                f = f2;
            }
            this.mTotalLength = 0;
            int iCombineMeasuredStates2 = i7;
            int i13 = i12;
            i3 = iMax2;
            for (int i14 = 0; i14 < virtualChildCount; i14++) {
                View virtualChildAt3 = getVirtualChildAt(i14);
                if (virtualChildAt3.getVisibility() != 8) {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f3 = layoutParams3.weight;
                    if (f3 > 0.0f) {
                        int i15 = (int) ((i13 * f3) / f);
                        int paddingLeft = getPaddingLeft();
                        int paddingRight = getPaddingRight();
                        int i16 = layoutParams3.leftMargin;
                        int i17 = layoutParams3.rightMargin;
                        int i18 = layoutParams3.width;
                        int i19 = i13 - i15;
                        int childMeasureSpec = getChildMeasureSpec(i, paddingLeft + paddingRight + i16 + i17, i18);
                        if (layoutParams3.height == 0 && mode2 == 1073741824) {
                            virtualChildAt3.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i15 > 0 ? i15 : 0, 1073741824));
                        } else {
                            int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i15;
                            int i20 = measuredHeight2;
                            if (measuredHeight2 < 0) {
                                i20 = 0;
                            }
                            virtualChildAt3.measure(childMeasureSpec, View.MeasureSpec.makeMeasureSpec(i20, 1073741824));
                        }
                        iCombineMeasuredStates2 = View.combineMeasuredStates(iCombineMeasuredStates2, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                        f -= f3;
                        i13 = i19;
                    }
                    int i21 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                    int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i21;
                    int iMax6 = Math.max(i3, measuredWidth2);
                    iMax5 = Math.max(iMax5, mode != 1073741824 && layoutParams3.width == -1 ? i21 : measuredWidth2);
                    z4 = z4 && layoutParams3.width == -1;
                    int i22 = this.mTotalLength;
                    this.mTotalLength = Math.max(i22, virtualChildAt3.getMeasuredHeight() + i22 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                    i3 = iMax6;
                }
            }
            this.mTotalLength += getPaddingTop() + getPaddingBottom();
            i7 = iCombineMeasuredStates2;
            i4 = iMax5;
        } else {
            int iMax7 = Math.max(iMax5, iMax4);
            if (z2 && mode2 != 1073741824) {
                for (int i23 = 0; i23 < virtualChildCount; i23++) {
                    View virtualChildAt4 = getVirtualChildAt(i23);
                    if (virtualChildAt4 != null && virtualChildAt4.getVisibility() != 8 && ((LayoutParams) virtualChildAt4.getLayoutParams()).weight > 0.0f) {
                        virtualChildAt4.measure(View.MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(iMax3, 1073741824));
                    }
                }
            }
            i4 = iMax7;
            i3 = iMax2;
        }
        if (z4 || mode == 1073741824) {
            i4 = i3;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i4 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i, i7), iResolveSizeAndState);
        if (z5) {
            forceUniformWidth(virtualChildCount, i2);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i >= 0 && i < getChildCount()) {
            this.mBaselineAlignedChildIndex = i;
            return;
        }
        throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        boolean z = false;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (drawable == null) {
            z = true;
        }
        setWillNotDraw(z);
        requestLayout();
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            int i2 = i;
            if ((8388615 & i) == 0) {
                i2 = i | GravityCompat.START;
            }
            int i3 = i2;
            if ((i2 & 112) == 0) {
                i3 = i2 | 48;
            }
            this.mGravity = i3;
            requestLayout();
        }
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | ((-8388616) & i3);
            requestLayout();
        }
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & (-113));
            requestLayout();
        }
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
