package org.questionsreponses.View.ViewPagers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/ViewPagers/HomeViewPager.class */
public class HomeViewPager extends ViewPager {
    private boolean enabled;

    public HomeViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.enabled = true;
    }

    @Override // android.support.v4.view.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.enabled) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public void setPagingEnabled(boolean z) {
        this.enabled = z;
    }
}
