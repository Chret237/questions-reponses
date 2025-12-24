package android.support.design.theme;

import android.content.Context;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/* loaded from: classes-dex2jar.jar:android/support/design/theme/MaterialComponentsViewInflater.class */
public class MaterialComponentsViewInflater extends AppCompatViewInflater {
    @Override // android.support.v7.app.AppCompatViewInflater
    protected AppCompatButton createButton(Context context, AttributeSet attributeSet) {
        return new MaterialButton(context, attributeSet);
    }
}
