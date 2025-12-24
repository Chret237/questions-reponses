package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/TintInfo.class */
class TintInfo {
    public boolean mHasTintList;
    public boolean mHasTintMode;
    public ColorStateList mTintList;
    public PorterDuff.Mode mTintMode;

    TintInfo() {
    }

    void clear() {
        this.mTintList = null;
        this.mHasTintList = false;
        this.mTintMode = null;
        this.mHasTintMode = false;
    }
}
