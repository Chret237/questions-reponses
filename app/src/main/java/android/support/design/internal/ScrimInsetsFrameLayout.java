package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.C0029R;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/* loaded from: classes-dex2jar.jar:android/support/design/internal/ScrimInsetsFrameLayout.class */
public class ScrimInsetsFrameLayout extends FrameLayout {
    Drawable insetForeground;
    Rect insets;
    private Rect tempRect;

    public ScrimInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tempRect = new Rect();
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0029R.styleable.ScrimInsetsFrameLayout, i, C0029R.style.Widget_Design_ScrimInsetsFrameLayout, new int[0]);
        this.insetForeground = typedArrayObtainStyledAttributes.getDrawable(C0029R.styleable.ScrimInsetsFrameLayout_insetForeground);
        typedArrayObtainStyledAttributes.recycle();
        setWillNotDraw(true);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener(this) { // from class: android.support.design.internal.ScrimInsetsFrameLayout.1
            final ScrimInsetsFrameLayout this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.view.OnApplyWindowInsetsListener
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                if (this.this$0.insets == null) {
                    this.this$0.insets = new Rect();
                }
                this.this$0.insets.set(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                this.this$0.onInsetsChanged(windowInsetsCompat);
                this.this$0.setWillNotDraw(!windowInsetsCompat.hasSystemWindowInsets() || this.this$0.insetForeground == null);
                ViewCompat.postInvalidateOnAnimation(this.this$0);
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        });
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (this.insets == null || this.insetForeground == null) {
            return;
        }
        int iSave = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        this.tempRect.set(0, 0, width, this.insets.top);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(canvas);
        this.tempRect.set(0, height - this.insets.bottom, width, height);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(canvas);
        this.tempRect.set(0, this.insets.top, this.insets.left, height - this.insets.bottom);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(canvas);
        this.tempRect.set(width - this.insets.right, this.insets.top, width, height - this.insets.bottom);
        this.insetForeground.setBounds(this.tempRect);
        this.insetForeground.draw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Drawable drawable = this.insetForeground;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Drawable drawable = this.insetForeground;
        if (drawable != null) {
            drawable.setCallback(null);
        }
    }

    protected void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
    }
}
