package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

/* loaded from: classes-dex2jar.jar:android/support/v4/widget/TintableImageSourceView.class */
public interface TintableImageSourceView {
    ColorStateList getSupportImageTintList();

    PorterDuff.Mode getSupportImageTintMode();

    void setSupportImageTintList(ColorStateList colorStateList);

    void setSupportImageTintMode(PorterDuff.Mode mode);
}
