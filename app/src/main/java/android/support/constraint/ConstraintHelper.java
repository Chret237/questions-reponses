package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.C0019R;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:android/support/constraint/ConstraintHelper.class */
public abstract class ConstraintHelper extends View {
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds;
    private String mReferenceIds;
    protected boolean mUseViewMeasure;
    protected Context myContext;

    public ConstraintHelper(Context context) throws IllegalAccessException, IllegalArgumentException {
        super(context);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = context;
        init(null);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet) throws IllegalAccessException, IllegalArgumentException {
        super(context, attributeSet);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = context;
        init(attributeSet);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet, int i) throws IllegalAccessException, IllegalArgumentException {
        super(context, attributeSet, i);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = context;
        init(attributeSet);
    }

    private void addID(String str) throws IllegalAccessException, IllegalArgumentException {
        int i;
        if (str == null || this.myContext == null) {
            return;
        }
        String strTrim = str.trim();
        try {
            i = C0019R.id.class.getField(strTrim).getInt(null);
        } catch (Exception e) {
            i = 0;
        }
        int identifier = i;
        if (i == 0) {
            identifier = this.myContext.getResources().getIdentifier(strTrim, "id", this.myContext.getPackageName());
        }
        int iIntValue = identifier;
        if (identifier == 0) {
            iIntValue = identifier;
            if (isInEditMode()) {
                iIntValue = identifier;
                if (getParent() instanceof ConstraintLayout) {
                    Object designInformation = ((ConstraintLayout) getParent()).getDesignInformation(0, strTrim);
                    iIntValue = identifier;
                    if (designInformation != null) {
                        iIntValue = identifier;
                        if (designInformation instanceof Integer) {
                            iIntValue = ((Integer) designInformation).intValue();
                        }
                    }
                }
            }
        }
        if (iIntValue != 0) {
            setTag(iIntValue, null);
            return;
        }
        Log.w("ConstraintHelper", "Could not find id of \"" + strTrim + "\"");
    }

    private void setIds(String str) throws IllegalAccessException, IllegalArgumentException {
        if (str == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            int iIndexOf = str.indexOf(44, i2);
            if (iIndexOf == -1) {
                addID(str.substring(i2));
                return;
            } else {
                addID(str.substring(i2, iIndexOf));
                i = iIndexOf + 1;
            }
        }
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }

    protected void init(AttributeSet attributeSet) throws IllegalAccessException, IllegalArgumentException {
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, C0019R.styleable.ConstraintLayout_Layout);
            int indexCount = typedArrayObtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = typedArrayObtainStyledAttributes.getIndex(i);
                if (index == C0019R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    String string = typedArrayObtainStyledAttributes.getString(index);
                    this.mReferenceIds = string;
                    setIds(string);
                }
            }
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mUseViewMeasure) {
            super.onMeasure(i, i2);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public void setReferencedIds(int[] iArr) {
        this.mCount = 0;
        for (int i : iArr) {
            setTag(i, null);
        }
    }

    @Override // android.view.View
    public void setTag(int i, Object obj) {
        int i2 = this.mCount;
        int[] iArr = this.mIds;
        if (i2 + 1 > iArr.length) {
            this.mIds = Arrays.copyOf(iArr, iArr.length * 2);
        }
        int[] iArr2 = this.mIds;
        int i3 = this.mCount;
        iArr2[i3] = i;
        this.mCount = i3 + 1;
    }

    public void updatePostLayout(ConstraintLayout constraintLayout) {
    }

    public void updatePostMeasure(ConstraintLayout constraintLayout) {
    }

    public void updatePreLayout(ConstraintLayout constraintLayout) throws IllegalAccessException, IllegalArgumentException {
        if (isInEditMode()) {
            setIds(this.mReferenceIds);
        }
        Helper helper = this.mHelperWidget;
        if (helper == null) {
            return;
        }
        helper.removeAllIds();
        for (int i = 0; i < this.mCount; i++) {
            View viewById = constraintLayout.getViewById(this.mIds[i]);
            if (viewById != null) {
                this.mHelperWidget.add(constraintLayout.getViewWidget(viewById));
            }
        }
    }

    public void validateParams() {
        if (this.mHelperWidget == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams) layoutParams).widget = this.mHelperWidget;
        }
    }
}
