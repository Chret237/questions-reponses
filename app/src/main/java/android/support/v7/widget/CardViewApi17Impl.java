package android.support.v7.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RoundRectDrawableWithShadow;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/CardViewApi17Impl.class */
class CardViewApi17Impl extends CardViewBaseImpl {
    CardViewApi17Impl() {
    }

    @Override // android.support.v7.widget.CardViewBaseImpl, android.support.v7.widget.CardViewImpl
    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper(this) { // from class: android.support.v7.widget.CardViewApi17Impl.1
            final CardViewApi17Impl this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.RoundRectDrawableWithShadow.RoundRectHelper
            public void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint) {
                canvas.drawRoundRect(rectF, f, f, paint);
            }
        };
    }
}
