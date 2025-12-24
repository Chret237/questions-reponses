package android.support.design.widget;

import android.content.Context;
import android.support.design.C0029R;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/TextInputEditText.class */
public class TextInputEditText extends AppCompatEditText {
    public TextInputEditText(Context context) {
        this(context, null);
    }

    public TextInputEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.editTextStyle);
    }

    public TextInputEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private CharSequence getHintFromLayout() {
        TextInputLayout textInputLayout = getTextInputLayout();
        return textInputLayout != null ? textInputLayout.getHint() : null;
    }

    private TextInputLayout getTextInputLayout() {
        ViewParent parent = getParent();
        while (true) {
            ViewParent viewParent = parent;
            if (!(viewParent instanceof View)) {
                return null;
            }
            if (viewParent instanceof TextInputLayout) {
                return (TextInputLayout) viewParent;
            }
            parent = viewParent.getParent();
        }
    }

    @Override // android.widget.TextView
    public CharSequence getHint() {
        TextInputLayout textInputLayout = getTextInputLayout();
        return (textInputLayout == null || !textInputLayout.isProvidingHint()) ? super.getHint() : textInputLayout.getHint();
    }

    @Override // android.support.v7.widget.AppCompatEditText, android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
        if (inputConnectionOnCreateInputConnection != null && editorInfo.hintText == null) {
            editorInfo.hintText = getHintFromLayout();
        }
        return inputConnectionOnCreateInputConnection;
    }
}
