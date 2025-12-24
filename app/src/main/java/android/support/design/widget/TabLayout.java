package android.support.design.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.C0029R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
/* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout.class */
public class TabLayout extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    private static final int INVALID_WIDTH = -1;
    private static final int MIN_INDICATOR_WIDTH = 24;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private static final Pools.Pool<Tab> tabPool = new Pools.SynchronizedPool(16);
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    private final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff.Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final RectF tabViewContentBounds;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$AdapterChangeListener.class */
    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;
        final TabLayout this$0;

        AdapterChangeListener(TabLayout tabLayout) {
            this.this$0 = tabLayout;
        }

        @Override // android.support.v4.view.ViewPager.OnAdapterChangeListener
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (this.this$0.viewPager == viewPager) {
                this.this$0.setPagerAdapter(pagerAdapter2, this.autoRefresh);
            }
        }

        void setAutoRefresh(boolean z) {
            this.autoRefresh = z;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$BaseOnTabSelectedListener.class */
    public interface BaseOnTabSelectedListener<T extends Tab> {
        void onTabReselected(T t);

        void onTabSelected(T t);

        void onTabUnselected(T t);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$Mode.class */
    public @interface Mode {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$OnTabSelectedListener.class */
    public interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$PagerAdapterObserver.class */
    private class PagerAdapterObserver extends DataSetObserver {
        final TabLayout this$0;

        PagerAdapterObserver(TabLayout tabLayout) {
            this.this$0 = tabLayout;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            this.this$0.populateFromPagerAdapter();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            this.this$0.populateFromPagerAdapter();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$SlidingTabIndicator.class */
    private class SlidingTabIndicator extends LinearLayout {
        private final GradientDrawable defaultSelectionIndicator;
        private ValueAnimator indicatorAnimator;
        private int indicatorLeft;
        private int indicatorRight;
        private int layoutDirection;
        private int selectedIndicatorHeight;
        private final Paint selectedIndicatorPaint;
        int selectedPosition;
        float selectionOffset;
        final TabLayout this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        SlidingTabIndicator(TabLayout tabLayout, Context context) {
            super(context);
            this.this$0 = tabLayout;
            this.selectedPosition = -1;
            this.layoutDirection = -1;
            this.indicatorLeft = -1;
            this.indicatorRight = -1;
            setWillNotDraw(false);
            this.selectedIndicatorPaint = new Paint();
            this.defaultSelectionIndicator = new GradientDrawable();
        }

        private void calculateTabViewContentBounds(TabView tabView, RectF rectF) {
            int contentWidth = tabView.getContentWidth();
            int iDpToPx = contentWidth;
            if (contentWidth < this.this$0.dpToPx(24)) {
                iDpToPx = this.this$0.dpToPx(24);
            }
            int left = (tabView.getLeft() + tabView.getRight()) / 2;
            int i = iDpToPx / 2;
            rectF.set(left - i, 0.0f, left + i, 0.0f);
        }

        private void updateIndicatorPosition() {
            int i;
            int i2;
            View childAt = getChildAt(this.selectedPosition);
            if (childAt == null || childAt.getWidth() <= 0) {
                i = -1;
                i2 = -1;
            } else {
                int left = childAt.getLeft();
                int right = childAt.getRight();
                int i3 = left;
                int i4 = right;
                if (!this.this$0.tabIndicatorFullWidth) {
                    i3 = left;
                    i4 = right;
                    if (childAt instanceof TabView) {
                        calculateTabViewContentBounds((TabView) childAt, this.this$0.tabViewContentBounds);
                        i3 = (int) this.this$0.tabViewContentBounds.left;
                        i4 = (int) this.this$0.tabViewContentBounds.right;
                    }
                }
                i = i3;
                i2 = i4;
                if (this.selectionOffset > 0.0f) {
                    i = i3;
                    i2 = i4;
                    if (this.selectedPosition < getChildCount() - 1) {
                        View childAt2 = getChildAt(this.selectedPosition + 1);
                        int left2 = childAt2.getLeft();
                        int right2 = childAt2.getRight();
                        int i5 = left2;
                        int i6 = right2;
                        if (!this.this$0.tabIndicatorFullWidth) {
                            i5 = left2;
                            i6 = right2;
                            if (childAt2 instanceof TabView) {
                                calculateTabViewContentBounds((TabView) childAt2, this.this$0.tabViewContentBounds);
                                i5 = (int) this.this$0.tabViewContentBounds.left;
                                i6 = (int) this.this$0.tabViewContentBounds.right;
                            }
                        }
                        float f = this.selectionOffset;
                        i = (int) ((i5 * f) + ((1.0f - f) * i3));
                        i2 = (int) ((i6 * f) + ((1.0f - f) * i4));
                    }
                }
            }
            setIndicatorPosition(i, i2);
        }

        void animateIndicatorToPosition(int i, int i2) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            View childAt = getChildAt(i);
            if (childAt == null) {
                updateIndicatorPosition();
                return;
            }
            int left = childAt.getLeft();
            int right = childAt.getRight();
            int i3 = left;
            int i4 = right;
            if (!this.this$0.tabIndicatorFullWidth) {
                i3 = left;
                i4 = right;
                if (childAt instanceof TabView) {
                    calculateTabViewContentBounds((TabView) childAt, this.this$0.tabViewContentBounds);
                    i3 = (int) this.this$0.tabViewContentBounds.left;
                    i4 = (int) this.this$0.tabViewContentBounds.right;
                }
            }
            int i5 = this.indicatorLeft;
            int i6 = this.indicatorRight;
            if (i5 == i3 && i6 == i4) {
                return;
            }
            ValueAnimator valueAnimator2 = new ValueAnimator();
            this.indicatorAnimator = valueAnimator2;
            valueAnimator2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            valueAnimator2.setDuration(i2);
            valueAnimator2.setFloatValues(0.0f, 1.0f);
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, i5, i3, i6, i4) { // from class: android.support.design.widget.TabLayout.SlidingTabIndicator.1
                final SlidingTabIndicator this$1;
                final int val$finalTargetLeft;
                final int val$finalTargetRight;
                final int val$startLeft;
                final int val$startRight;

                {
                    this.this$1 = this;
                    this.val$startLeft = i5;
                    this.val$finalTargetLeft = i3;
                    this.val$startRight = i6;
                    this.val$finalTargetRight = i4;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator3) {
                    float animatedFraction = valueAnimator3.getAnimatedFraction();
                    this.this$1.setIndicatorPosition(AnimationUtils.lerp(this.val$startLeft, this.val$finalTargetLeft, animatedFraction), AnimationUtils.lerp(this.val$startRight, this.val$finalTargetRight, animatedFraction));
                }
            });
            valueAnimator2.addListener(new AnimatorListenerAdapter(this, i) { // from class: android.support.design.widget.TabLayout.SlidingTabIndicator.2
                final SlidingTabIndicator this$1;
                final int val$position;

                {
                    this.this$1 = this;
                    this.val$position = i;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    this.this$1.selectedPosition = this.val$position;
                    this.this$1.selectionOffset = 0.0f;
                }
            });
            valueAnimator2.start();
        }

        boolean childrenNeedLayout() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v40, types: [android.graphics.drawable.Drawable] */
        @Override // android.view.View
        public void draw(Canvas canvas) {
            int height;
            int intrinsicHeight = this.this$0.tabSelectedIndicator != null ? this.this$0.tabSelectedIndicator.getIntrinsicHeight() : 0;
            int i = this.selectedIndicatorHeight;
            if (i >= 0) {
                intrinsicHeight = i;
            }
            int i2 = this.this$0.tabIndicatorGravity;
            if (i2 == 0) {
                height = getHeight() - intrinsicHeight;
                intrinsicHeight = getHeight();
            } else if (i2 != 1) {
                height = 0;
                if (i2 != 2) {
                    if (i2 != 3) {
                        intrinsicHeight = 0;
                        height = 0;
                    } else {
                        intrinsicHeight = getHeight();
                        height = 0;
                    }
                }
            } else {
                height = (getHeight() - intrinsicHeight) / 2;
                intrinsicHeight = (getHeight() + intrinsicHeight) / 2;
            }
            int i3 = this.indicatorLeft;
            if (i3 >= 0 && this.indicatorRight > i3) {
                Drawable drawableWrap = DrawableCompat.wrap(this.this$0.tabSelectedIndicator != null ? this.this$0.tabSelectedIndicator : this.defaultSelectionIndicator);
                drawableWrap.setBounds(this.indicatorLeft, height, this.indicatorRight, intrinsicHeight);
                if (this.selectedIndicatorPaint != null) {
                    if (Build.VERSION.SDK_INT == 21) {
                        drawableWrap.setColorFilter(this.selectedIndicatorPaint.getColor(), PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint(drawableWrap, this.selectedIndicatorPaint.getColor());
                    }
                }
                drawableWrap.draw(canvas);
            }
            super.draw(canvas);
        }

        float getIndicatorPosition() {
            return this.selectedPosition + this.selectionOffset;
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                updateIndicatorPosition();
                return;
            }
            this.indicatorAnimator.cancel();
            animateIndicatorToPosition(this.selectedPosition, Math.round((1.0f - this.indicatorAnimator.getAnimatedFraction()) * this.indicatorAnimator.getDuration()));
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            boolean z;
            super.onMeasure(i, i2);
            if (View.MeasureSpec.getMode(i) == 1073741824 && this.this$0.mode == 1 && this.this$0.tabGravity == 1) {
                int childCount = getChildCount();
                int i4 = 0;
                int i5 = 0;
                while (true) {
                    i3 = i5;
                    if (i4 >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i4);
                    int iMax = i3;
                    if (childAt.getVisibility() == 0) {
                        iMax = Math.max(i3, childAt.getMeasuredWidth());
                    }
                    i4++;
                    i5 = iMax;
                }
                if (i3 <= 0) {
                    return;
                }
                if (i3 * childCount <= getMeasuredWidth() - (this.this$0.dpToPx(16) * 2)) {
                    z = false;
                    for (int i6 = 0; i6 < childCount; i6++) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i6).getLayoutParams();
                        if (layoutParams.width != i3 || layoutParams.weight != 0.0f) {
                            layoutParams.width = i3;
                            layoutParams.weight = 0.0f;
                            z = true;
                        }
                    }
                } else {
                    this.this$0.tabGravity = 0;
                    this.this$0.updateTabViews(false);
                    z = true;
                }
                if (z) {
                    super.onMeasure(i, i2);
                }
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onRtlPropertiesChanged(int i) {
            super.onRtlPropertiesChanged(i);
            if (Build.VERSION.SDK_INT >= 23 || this.layoutDirection == i) {
                return;
            }
            requestLayout();
            this.layoutDirection = i;
        }

        void setIndicatorPosition(int i, int i2) {
            if (i == this.indicatorLeft && i2 == this.indicatorRight) {
                return;
            }
            this.indicatorLeft = i;
            this.indicatorRight = i2;
            ViewCompat.postInvalidateOnAnimation(this);
        }

        void setIndicatorPositionFromTabPosition(int i, float f) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = i;
            this.selectionOffset = f;
            updateIndicatorPosition();
        }

        void setSelectedIndicatorColor(int i) {
            if (this.selectedIndicatorPaint.getColor() != i) {
                this.selectedIndicatorPaint.setColor(i);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void setSelectedIndicatorHeight(int i) {
            if (this.selectedIndicatorHeight != i) {
                this.selectedIndicatorHeight = i;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$Tab.class */
    public static class Tab {
        public static final int INVALID_POSITION = -1;
        private CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        public TabLayout parent;
        private int position = -1;
        private Object tag;
        private CharSequence text;
        public TabView view;

        public CharSequence getContentDescription() {
            TabView tabView = this.view;
            return tabView == null ? null : tabView.getContentDescription();
        }

        public View getCustomView() {
            return this.customView;
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public int getPosition() {
            return this.position;
        }

        public Object getTag() {
            return this.tag;
        }

        public CharSequence getText() {
            return this.text;
        }

        public boolean isSelected() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return tabLayout.getSelectedTabPosition() == this.position;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }

        public void select() {
            TabLayout tabLayout = this.parent;
            if (tabLayout == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            tabLayout.selectTab(this);
        }

        public Tab setContentDescription(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setContentDescription(tabLayout.getResources().getText(i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.contentDesc = charSequence;
            updateView();
            return this;
        }

        public Tab setCustomView(int i) {
            return setCustomView(LayoutInflater.from(this.view.getContext()).inflate(i, (ViewGroup) this.view, false));
        }

        public Tab setCustomView(View view) {
            this.customView = view;
            updateView();
            return this;
        }

        public Tab setIcon(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setIcon(Drawable drawable) {
            this.icon = drawable;
            updateView();
            return this;
        }

        void setPosition(int i) {
            this.position = i;
        }

        public Tab setTag(Object obj) {
            this.tag = obj;
            return this;
        }

        public Tab setText(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setText(tabLayout.getResources().getText(i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setText(CharSequence charSequence) {
            if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(charSequence)) {
                this.view.setContentDescription(charSequence);
            }
            this.text = charSequence;
            updateView();
            return this;
        }

        void updateView() {
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.update();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$TabGravity.class */
    public @interface TabGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$TabIndicatorGravity.class */
    public @interface TabIndicatorGravity {
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$TabLayoutOnPageChangeListener.class */
    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            this.previousScrollState = this.scrollState;
            this.scrollState = i;
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout != null) {
                boolean z = false;
                boolean z2 = this.scrollState != 2 || this.previousScrollState == 1;
                if (this.scrollState != 2 || this.previousScrollState != 0) {
                    z = true;
                }
                tabLayout.setScrollPosition(i, f, z2, z);
            }
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            TabLayout tabLayout = this.tabLayoutRef.get();
            if (tabLayout == null || tabLayout.getSelectedTabPosition() == i || i >= tabLayout.getTabCount()) {
                return;
            }
            int i2 = this.scrollState;
            tabLayout.selectTab(tabLayout.getTabAt(i), i2 == 0 || (i2 == 2 && this.previousScrollState == 0));
        }

        void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$TabView.class */
    class TabView extends LinearLayout {
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;
        final TabLayout this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TabView(TabLayout tabLayout, Context context) {
            super(context);
            this.this$0 = tabLayout;
            this.defaultMaxLines = 2;
            updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative(this, tabLayout.tabPaddingStart, tabLayout.tabPaddingTop, tabLayout.tabPaddingEnd, tabLayout.tabPaddingBottom);
            setGravity(17);
            setOrientation(!tabLayout.inlineLabel ? 1 : 0);
            setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        }

        private float approximateLineWidth(Layout layout, int i, float f) {
            return layout.getLineWidth(i) * (f / layout.getPaint().getTextSize());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drawBackground(Canvas canvas) {
            Drawable drawable = this.baseBackgroundDrawable;
            if (drawable != null) {
                drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getContentWidth() {
            TextView textView = this.textView;
            int i = 0;
            ImageView imageView = this.iconView;
            View view = this.customView;
            int i2 = 0;
            int i3 = 0;
            boolean z = false;
            while (true) {
                boolean z2 = z;
                if (i >= 3) {
                    return i2 - i3;
                }
                View view2 = new View[]{textView, imageView, view}[i];
                int iMax = i2;
                int iMin = i3;
                boolean z3 = z2;
                if (view2 != null) {
                    iMax = i2;
                    iMin = i3;
                    z3 = z2;
                    if (view2.getVisibility() == 0) {
                        iMin = z2 ? Math.min(i3, view2.getLeft()) : view2.getLeft();
                        z3 = true;
                        iMax = z2 ? Math.max(i2, view2.getRight()) : view2.getRight();
                    }
                }
                i++;
                i2 = iMax;
                i3 = iMin;
                z = z3;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateBackgroundDrawable(Context context) {
            if (this.this$0.tabBackgroundResId != 0) {
                Drawable drawable = AppCompatResources.getDrawable(context, this.this$0.tabBackgroundResId);
                this.baseBackgroundDrawable = drawable;
                if (drawable != null && drawable.isStateful()) {
                    this.baseBackgroundDrawable.setState(getDrawableState());
                }
            } else {
                this.baseBackgroundDrawable = null;
            }
            Drawable gradientDrawable = new GradientDrawable();
            ((GradientDrawable) gradientDrawable).setColor(0);
            Drawable layerDrawable = gradientDrawable;
            if (this.this$0.tabRippleColorStateList != null) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setCornerRadius(1.0E-5f);
                gradientDrawable2.setColor(-1);
                ColorStateList colorStateListConvertToRippleDrawableColor = RippleUtils.convertToRippleDrawableColor(this.this$0.tabRippleColorStateList);
                if (Build.VERSION.SDK_INT >= 21) {
                    if (this.this$0.unboundedRipple) {
                        gradientDrawable = null;
                    }
                    if (this.this$0.unboundedRipple) {
                        gradientDrawable2 = null;
                    }
                    layerDrawable = new RippleDrawable(colorStateListConvertToRippleDrawableColor, gradientDrawable, gradientDrawable2);
                } else {
                    Drawable drawableWrap = DrawableCompat.wrap(gradientDrawable2);
                    DrawableCompat.setTintList(drawableWrap, colorStateListConvertToRippleDrawableColor);
                    layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable, drawableWrap});
                }
            }
            ViewCompat.setBackground(this, layerDrawable);
            this.this$0.invalidate();
        }

        private void updateTextAndIcon(TextView textView, ImageView imageView) {
            Tab tab = this.tab;
            Drawable drawableMutate = (tab == null || tab.getIcon() == null) ? null : DrawableCompat.wrap(this.tab.getIcon()).mutate();
            Tab tab2 = this.tab;
            CharSequence text = tab2 != null ? tab2.getText() : null;
            if (imageView != null) {
                if (drawableMutate != null) {
                    imageView.setImageDrawable(drawableMutate);
                    imageView.setVisibility(0);
                    setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
            }
            boolean z = !TextUtils.isEmpty(text);
            if (textView != null) {
                if (z) {
                    textView.setText(text);
                    textView.setVisibility(0);
                    setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText((CharSequence) null);
                }
            }
            if (imageView != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                int iDpToPx = (z && imageView.getVisibility() == 0) ? this.this$0.dpToPx(8) : 0;
                if (this.this$0.inlineLabel) {
                    if (iDpToPx != MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) {
                        MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, iDpToPx);
                        marginLayoutParams.bottomMargin = 0;
                        imageView.setLayoutParams(marginLayoutParams);
                        imageView.requestLayout();
                    }
                } else if (iDpToPx != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.bottomMargin = iDpToPx;
                    MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, 0);
                    imageView.setLayoutParams(marginLayoutParams);
                    imageView.requestLayout();
                }
            }
            Tab tab3 = this.tab;
            CharSequence charSequence = tab3 != null ? tab3.contentDesc : null;
            if (z) {
                charSequence = null;
            }
            TooltipCompat.setTooltipText(this, charSequence);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            int[] drawableState = getDrawableState();
            Drawable drawable = this.baseBackgroundDrawable;
            boolean state = false;
            if (drawable != null) {
                state = false;
                if (drawable.isStateful()) {
                    state = false | this.baseBackgroundDrawable.setState(drawableState);
                }
            }
            if (state) {
                invalidate();
                this.this$0.invalidate();
            }
        }

        public Tab getTab() {
            return this.tab;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName(ActionBar.Tab.class.getName());
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName(ActionBar.Tab.class.getName());
        }

        /* JADX WARN: Removed duplicated region for block: B:37:0x011f  */
        /* JADX WARN: Removed duplicated region for block: B:8:0x002c  */
        @Override // android.widget.LinearLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onMeasure(int r6, int r7) {
            /*
                Method dump skipped, instructions count: 320
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.TabLayout.TabView.onMeasure(int, int):void");
        }

        @Override // android.view.View
        public boolean performClick() {
            boolean zPerformClick = super.performClick();
            boolean z = zPerformClick;
            if (this.tab != null) {
                if (!zPerformClick) {
                    playSoundEffect(0);
                }
                this.tab.select();
                z = true;
            }
            return z;
        }

        void reset() {
            setTab(null);
            setSelected(false);
        }

        @Override // android.view.View
        public void setSelected(boolean z) {
            boolean z2 = isSelected() != z;
            super.setSelected(z);
            if (z2 && z && Build.VERSION.SDK_INT < 16) {
                sendAccessibilityEvent(4);
            }
            TextView textView = this.textView;
            if (textView != null) {
                textView.setSelected(z);
            }
            ImageView imageView = this.iconView;
            if (imageView != null) {
                imageView.setSelected(z);
            }
            View view = this.customView;
            if (view != null) {
                view.setSelected(z);
            }
        }

        void setTab(Tab tab) {
            if (tab != this.tab) {
                this.tab = tab;
                update();
            }
        }

        final void update() {
            Tab tab = this.tab;
            View customView = tab != null ? tab.getCustomView() : null;
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(customView);
                    }
                    addView(customView);
                }
                this.customView = customView;
                TextView textView = this.textView;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.iconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.iconView.setImageDrawable(null);
                }
                TextView textView2 = (TextView) customView.findViewById(R.id.text1);
                this.customTextView = textView2;
                if (textView2 != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines(textView2);
                }
                this.customIconView = (ImageView) customView.findViewById(R.id.icon);
            } else {
                View view = this.customView;
                if (view != null) {
                    removeView(view);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            if (this.customView == null) {
                if (this.iconView == null) {
                    ImageView imageView2 = (ImageView) LayoutInflater.from(getContext()).inflate(C0029R.layout.design_layout_tab_icon, (ViewGroup) this, false);
                    addView(imageView2, 0);
                    this.iconView = imageView2;
                }
                Drawable drawableMutate = null;
                if (tab != null) {
                    drawableMutate = null;
                    if (tab.getIcon() != null) {
                        drawableMutate = DrawableCompat.wrap(tab.getIcon()).mutate();
                    }
                }
                if (drawableMutate != null) {
                    DrawableCompat.setTintList(drawableMutate, this.this$0.tabIconTint);
                    if (this.this$0.tabIconTintMode != null) {
                        DrawableCompat.setTintMode(drawableMutate, this.this$0.tabIconTintMode);
                    }
                }
                if (this.textView == null) {
                    TextView textView3 = (TextView) LayoutInflater.from(getContext()).inflate(C0029R.layout.design_layout_tab_text, (ViewGroup) this, false);
                    addView(textView3);
                    this.textView = textView3;
                    this.defaultMaxLines = TextViewCompat.getMaxLines(textView3);
                }
                TextViewCompat.setTextAppearance(this.textView, this.this$0.tabTextAppearance);
                if (this.this$0.tabTextColors != null) {
                    this.textView.setTextColor(this.this$0.tabTextColors);
                }
                updateTextAndIcon(this.textView, this.iconView);
            } else if (this.customTextView != null || this.customIconView != null) {
                updateTextAndIcon(this.customTextView, this.customIconView);
            }
            if (tab != null && !TextUtils.isEmpty(tab.contentDesc)) {
                setContentDescription(tab.contentDesc);
            }
            boolean z = false;
            if (tab != null) {
                z = false;
                if (tab.isSelected()) {
                    z = true;
                }
            }
            setSelected(z);
        }

        final void updateOrientation() {
            setOrientation(!this.this$0.inlineLabel ? 1 : 0);
            if (this.customTextView == null && this.customIconView == null) {
                updateTextAndIcon(this.textView, this.iconView);
            } else {
                updateTextAndIcon(this.customTextView, this.customIconView);
            }
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/TabLayout$ViewPagerOnTabSelectedListener.class */
    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager viewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override // android.support.design.widget.TabLayout.BaseOnTabSelectedListener
        public void onTabReselected(Tab tab) {
        }

        @Override // android.support.design.widget.TabLayout.BaseOnTabSelectedListener
        public void onTabSelected(Tab tab) throws Resources.NotFoundException {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        @Override // android.support.design.widget.TabLayout.BaseOnTabSelectedListener
        public void onTabUnselected(Tab tab) {
        }
    }

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int i) throws Resources.NotFoundException {
        super(context, attributeSet, i);
        this.tabs = new ArrayList<>();
        this.tabViewContentBounds = new RectF();
        this.tabMaxWidth = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.selectedListeners = new ArrayList<>();
        this.tabViewPool = new Pools.SimplePool(12);
        setHorizontalScrollBarEnabled(false);
        SlidingTabIndicator slidingTabIndicator = new SlidingTabIndicator(this, context);
        this.slidingTabIndicator = slidingTabIndicator;
        super.addView(slidingTabIndicator, 0, new FrameLayout.LayoutParams(-2, -1));
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0029R.styleable.TabLayout, i, C0029R.style.Widget_Design_TabLayout, C0029R.styleable.TabLayout_tabTextAppearance);
        this.slidingTabIndicator.setSelectedIndicatorHeight(typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabIndicatorHeight, -1));
        this.slidingTabIndicator.setSelectedIndicatorColor(typedArrayObtainStyledAttributes.getColor(C0029R.styleable.TabLayout_tabIndicatorColor, 0));
        setSelectedTabIndicator(MaterialResources.getDrawable(context, typedArrayObtainStyledAttributes, C0029R.styleable.TabLayout_tabIndicator));
        setSelectedTabIndicatorGravity(typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TabLayout_tabIndicatorGravity, 0));
        setTabIndicatorFullWidth(typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.TabLayout_tabIndicatorFullWidth, true));
        int dimensionPixelSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabPadding, 0);
        this.tabPaddingBottom = dimensionPixelSize;
        this.tabPaddingEnd = dimensionPixelSize;
        this.tabPaddingTop = dimensionPixelSize;
        this.tabPaddingStart = dimensionPixelSize;
        this.tabPaddingStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
        this.tabPaddingTop = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
        this.tabPaddingEnd = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
        this.tabPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(C0029R.styleable.TabLayout_tabTextAppearance, C0029R.style.TextAppearance_Design_Tab);
        this.tabTextAppearance = resourceId;
        TypedArray typedArrayObtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, C0287R.styleable.TextAppearance);
        try {
            this.tabTextSize = typedArrayObtainStyledAttributes2.getDimensionPixelSize(C0287R.styleable.TextAppearance_android_textSize, 0);
            this.tabTextColors = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes2, C0287R.styleable.TextAppearance_android_textColor);
            typedArrayObtainStyledAttributes2.recycle();
            if (typedArrayObtainStyledAttributes.hasValue(C0029R.styleable.TabLayout_tabTextColor)) {
                this.tabTextColors = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TabLayout_tabTextColor);
            }
            if (typedArrayObtainStyledAttributes.hasValue(C0029R.styleable.TabLayout_tabSelectedTextColor)) {
                this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), typedArrayObtainStyledAttributes.getColor(C0029R.styleable.TabLayout_tabSelectedTextColor, 0));
            }
            this.tabIconTint = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TabLayout_tabIconTint);
            this.tabIconTintMode = ViewUtils.parseTintMode(typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TabLayout_tabIconTintMode, -1), null);
            this.tabRippleColorStateList = MaterialResources.getColorStateList(context, typedArrayObtainStyledAttributes, C0029R.styleable.TabLayout_tabRippleColor);
            this.tabIndicatorAnimationDuration = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TabLayout_tabIndicatorAnimationDuration, ANIMATION_DURATION);
            this.requestedTabMinWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabMinWidth, -1);
            this.requestedTabMaxWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabMaxWidth, -1);
            this.tabBackgroundResId = typedArrayObtainStyledAttributes.getResourceId(C0029R.styleable.TabLayout_tabBackground, 0);
            this.contentInsetStart = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.TabLayout_tabContentStart, 0);
            this.mode = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TabLayout_tabMode, 1);
            this.tabGravity = typedArrayObtainStyledAttributes.getInt(C0029R.styleable.TabLayout_tabGravity, 0);
            this.inlineLabel = typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.TabLayout_tabInlineLabel, false);
            this.unboundedRipple = typedArrayObtainStyledAttributes.getBoolean(C0029R.styleable.TabLayout_tabUnboundedRipple, false);
            typedArrayObtainStyledAttributes.recycle();
            Resources resources = getResources();
            this.tabTextMultiLineSize = resources.getDimensionPixelSize(C0029R.dimen.design_tab_text_size_2line);
            this.scrollableTabMinWidth = resources.getDimensionPixelSize(C0029R.dimen.design_tab_scrollable_min_width);
            applyModeAndGravity();
        } catch (Throwable th) {
            typedArrayObtainStyledAttributes2.recycle();
            throw th;
        }
    }

    private void addTabFromItemView(TabItem tabItem) {
        Tab tabNewTab = newTab();
        if (tabItem.text != null) {
            tabNewTab.setText(tabItem.text);
        }
        if (tabItem.icon != null) {
            tabNewTab.setIcon(tabItem.icon);
        }
        if (tabItem.customLayout != 0) {
            tabNewTab.setCustomView(tabItem.customLayout);
        }
        if (!TextUtils.isEmpty(tabItem.getContentDescription())) {
            tabNewTab.setContentDescription(tabItem.getContentDescription());
        }
        addTab(tabNewTab);
    }

    private void addTabView(Tab tab) {
        this.slidingTabIndicator.addView(tab.view, tab.getPosition(), createLayoutParamsForTabs());
    }

    private void addViewInternal(View view) {
        if (!(view instanceof TabItem)) {
            throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
        }
        addTabFromItemView((TabItem) view);
    }

    private void animateToTab(int i) {
        if (i == -1) {
            return;
        }
        if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.slidingTabIndicator.childrenNeedLayout()) {
            setScrollPosition(i, 0.0f, true);
            return;
        }
        int scrollX = getScrollX();
        int iCalculateScrollXForTab = calculateScrollXForTab(i, 0.0f);
        if (scrollX != iCalculateScrollXForTab) {
            ensureScrollAnimator();
            this.scrollAnimator.setIntValues(scrollX, iCalculateScrollXForTab);
            this.scrollAnimator.start();
        }
        this.slidingTabIndicator.animateIndicatorToPosition(i, this.tabIndicatorAnimationDuration);
    }

    private void applyModeAndGravity() {
        ViewCompat.setPaddingRelative(this.slidingTabIndicator, this.mode == 0 ? Math.max(0, this.contentInsetStart - this.tabPaddingStart) : 0, 0, 0, 0);
        int i = this.mode;
        if (i == 0) {
            this.slidingTabIndicator.setGravity(GravityCompat.START);
        } else if (i == 1) {
            this.slidingTabIndicator.setGravity(1);
        }
        updateTabViews(true);
    }

    private int calculateScrollXForTab(int i, float f) {
        int width = 0;
        if (this.mode != 0) {
            return 0;
        }
        View childAt = this.slidingTabIndicator.getChildAt(i);
        int i2 = i + 1;
        View childAt2 = i2 < this.slidingTabIndicator.getChildCount() ? this.slidingTabIndicator.getChildAt(i2) : null;
        int width2 = childAt != null ? childAt.getWidth() : 0;
        if (childAt2 != null) {
            width = childAt2.getWidth();
        }
        int left = (childAt.getLeft() + (width2 / 2)) - (getWidth() / 2);
        int i3 = (int) ((width2 + width) * 0.5f * f);
        return ViewCompat.getLayoutDirection(this) == 0 ? left + i3 : left - i3;
    }

    private void configureTab(Tab tab, int i) {
        tab.setPosition(i);
        this.tabs.add(i, tab);
        int size = this.tabs.size();
        while (true) {
            i++;
            if (i >= size) {
                return;
            } else {
                this.tabs.get(i).setPosition(i);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [int[], int[][]] */
    private static ColorStateList createColorStateList(int i, int i2) {
        return new ColorStateList(new int[]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{i2, i});
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private TabView createTabView(Tab tab) {
        Pools.Pool<TabView> pool = this.tabViewPool;
        TabView tabViewAcquire = pool != null ? pool.acquire() : null;
        TabView tabView = tabViewAcquire;
        if (tabViewAcquire == null) {
            tabView = new TabView(this, getContext());
        }
        tabView.setTab(tab);
        tabView.setFocusable(true);
        tabView.setMinimumWidth(getTabMinWidth());
        if (TextUtils.isEmpty(tab.contentDesc)) {
            tabView.setContentDescription(tab.text);
        } else {
            tabView.setContentDescription(tab.contentDesc);
        }
        return tabView;
    }

    private void dispatchTabReselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabReselected(tab);
        }
    }

    private void dispatchTabSelected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabUnselected(tab);
        }
    }

    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.scrollAnimator = valueAnimator;
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration(this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: android.support.design.widget.TabLayout.1
                final TabLayout this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    this.this$0.scrollTo(((Integer) valueAnimator2.getAnimatedValue()).intValue(), 0);
                }
            });
        }
    }

    private int getDefaultHeight() {
        boolean z;
        int size = this.tabs.size();
        int i = 0;
        while (true) {
            z = false;
            if (i < size) {
                Tab tab = this.tabs.get(i);
                if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                    z = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return (!z || this.inlineLabel) ? 48 : 72;
    }

    private int getTabMinWidth() {
        int i = this.requestedTabMinWidth;
        if (i != -1) {
            return i;
        }
        return this.mode == 0 ? this.scrollableTabMinWidth : 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.slidingTabIndicator.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private void removeTabViewAt(int i) {
        TabView tabView = (TabView) this.slidingTabIndicator.getChildAt(i);
        this.slidingTabIndicator.removeViewAt(i);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        requestLayout();
    }

    private void setSelectedTabView(int i) {
        int childCount = this.slidingTabIndicator.getChildCount();
        if (i < childCount) {
            int i2 = 0;
            while (i2 < childCount) {
                View childAt = this.slidingTabIndicator.getChildAt(i2);
                childAt.setSelected(i2 == i);
                childAt.setActivated(i2 == i);
                i2++;
            }
        }
    }

    private void setupWithViewPager(ViewPager viewPager, boolean z, boolean z2) {
        ViewPager viewPager2 = this.viewPager;
        if (viewPager2 != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                viewPager2.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            AdapterChangeListener adapterChangeListener = this.adapterChangeListener;
            if (adapterChangeListener != null) {
                this.viewPager.removeOnAdapterChangeListener(adapterChangeListener);
            }
        }
        BaseOnTabSelectedListener baseOnTabSelectedListener = this.currentVpSelectedListener;
        if (baseOnTabSelectedListener != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener);
            this.currentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.viewPager = viewPager;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.pageChangeListener);
            ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            this.currentVpSelectedListener = viewPagerOnTabSelectedListener;
            addOnTabSelectedListener(viewPagerOnTabSelectedListener);
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                setPagerAdapter(adapter, z);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener(this);
            }
            this.adapterChangeListener.setAutoRefresh(z);
            viewPager.addOnAdapterChangeListener(this.adapterChangeListener);
            setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.viewPager = null;
            setPagerAdapter(null, false);
        }
        this.setupViewPagerImplicitly = z2;
    }

    private void updateAllTabs() {
        int size = this.tabs.size();
        for (int i = 0; i < size; i++) {
            this.tabs.get(i).updateView();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mode == 1 && this.tabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
        } else {
            layoutParams.width = -2;
            layoutParams.weight = 0.0f;
        }
    }

    public void addOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        if (this.selectedListeners.contains(baseOnTabSelectedListener)) {
            return;
        }
        this.selectedListeners.add(baseOnTabSelectedListener);
    }

    public void addTab(Tab tab) {
        addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int i) {
        addTab(tab, i, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int i, boolean z) {
        if (tab.parent != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        configureTab(tab, i);
        addTabView(tab);
        if (z) {
            tab.select();
        }
    }

    public void addTab(Tab tab, boolean z) {
        addTab(tab, this.tabs.size(), z);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view) {
        addViewInternal(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i) {
        addViewInternal(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    public void clearOnTabSelectedListeners() {
        this.selectedListeners.clear();
    }

    protected Tab createTabFromPool() {
        Tab tabAcquire = tabPool.acquire();
        Tab tab = tabAcquire;
        if (tabAcquire == null) {
            tab = new Tab();
        }
        return tab;
    }

    int dpToPx(int i) {
        return Math.round(getResources().getDisplayMetrics().density * i);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        Tab tab = this.selectedTab;
        return tab != null ? tab.getPosition() : -1;
    }

    public Tab getTabAt(int i) {
        return (i < 0 || i >= getTabCount()) ? null : this.tabs.get(i);
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    int getTabMaxWidth() {
        return this.tabMaxWidth;
    }

    public int getTabMode() {
        return this.mode;
    }

    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public Tab newTab() {
        Tab tabCreateTabFromPool = createTabFromPool();
        tabCreateTabFromPool.parent = this;
        tabCreateTabFromPool.view = createTabView(tabCreateTabFromPool);
        return tabCreateTabFromPool;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.viewPager == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                setupWithViewPager((ViewPager) parent, true, true);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            setupWithViewPager(null);
            this.setupViewPagerImplicitly = false;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            if (childAt instanceof TabView) {
                ((TabView) childAt).drawBackground(canvas);
            }
        }
        super.onDraw(canvas);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int iDpToPx = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        int mode = View.MeasureSpec.getMode(i2);
        if (mode == Integer.MIN_VALUE) {
            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(iDpToPx, View.MeasureSpec.getSize(i2)), 1073741824);
        } else if (mode == 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(iDpToPx, 1073741824);
        }
        int size = View.MeasureSpec.getSize(i);
        if (View.MeasureSpec.getMode(i) != 0) {
            int iDpToPx2 = this.requestedTabMaxWidth;
            if (iDpToPx2 <= 0) {
                iDpToPx2 = size - dpToPx(56);
            }
            this.tabMaxWidth = iDpToPx2;
        }
        super.onMeasure(i, i2);
        boolean z = true;
        if (getChildCount() == 1) {
            View childAt = getChildAt(0);
            int i3 = this.mode;
            if (i3 == 0 ? childAt.getMeasuredWidth() >= getMeasuredWidth() : i3 != 1 || childAt.getMeasuredWidth() == getMeasuredWidth()) {
                z = false;
            }
            if (z) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), childAt.getLayoutParams().height));
            }
        }
    }

    void populateFromPagerAdapter() {
        int currentItem;
        removeAllTabs();
        PagerAdapter pagerAdapter = this.pagerAdapter;
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            for (int i = 0; i < count; i++) {
                addTab(newTab().setText(this.pagerAdapter.getPageTitle(i)), false);
            }
            ViewPager viewPager = this.viewPager;
            if (viewPager == null || count <= 0 || (currentItem = viewPager.getCurrentItem()) == getSelectedTabPosition() || currentItem >= getTabCount()) {
                return;
            }
            selectTab(getTabAt(currentItem));
        }
    }

    protected boolean releaseFromTabPool(Tab tab) {
        return tabPool.release(tab);
    }

    public void removeAllTabs() {
        for (int childCount = this.slidingTabIndicator.getChildCount() - 1; childCount >= 0; childCount--) {
            removeTabViewAt(childCount);
        }
        Iterator<Tab> it = this.tabs.iterator();
        while (it.hasNext()) {
            Tab next = it.next();
            it.remove();
            next.reset();
            releaseFromTabPool(next);
        }
        this.selectedTab = null;
    }

    public void removeOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        this.selectedListeners.remove(baseOnTabSelectedListener);
    }

    public void removeTab(Tab tab) {
        if (tab.parent != this) {
            throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
        }
        removeTabAt(tab.getPosition());
    }

    public void removeTabAt(int i) {
        Tab tab = this.selectedTab;
        int position = tab != null ? tab.getPosition() : 0;
        removeTabViewAt(i);
        Tab tabRemove = this.tabs.remove(i);
        if (tabRemove != null) {
            tabRemove.reset();
            releaseFromTabPool(tabRemove);
        }
        int size = this.tabs.size();
        for (int i2 = i; i2 < size; i2++) {
            this.tabs.get(i2).setPosition(i2);
        }
        if (position == i) {
            selectTab(this.tabs.isEmpty() ? null : this.tabs.get(Math.max(0, i - 1)));
        }
    }

    void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    void selectTab(Tab tab, boolean z) {
        Tab tab2 = this.selectedTab;
        if (tab2 == tab) {
            if (tab2 != null) {
                dispatchTabReselected(tab);
                animateToTab(tab.getPosition());
                return;
            }
            return;
        }
        int position = tab != null ? tab.getPosition() : -1;
        if (z) {
            if ((tab2 == null || tab2.getPosition() == -1) && position != -1) {
                setScrollPosition(position, 0.0f, true);
            } else {
                animateToTab(position);
            }
            if (position != -1) {
                setSelectedTabView(position);
            }
        }
        this.selectedTab = tab;
        if (tab2 != null) {
            dispatchTabUnselected(tab2);
        }
        if (tab != null) {
            dispatchTabSelected(tab);
        }
    }

    public void setInlineLabel(boolean z) {
        if (this.inlineLabel != z) {
            this.inlineLabel = z;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateOrientation();
                }
            }
            applyModeAndGravity();
        }
    }

    public void setInlineLabelResource(int i) {
        setInlineLabel(getResources().getBoolean(i));
    }

    @Deprecated
    public void setOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        BaseOnTabSelectedListener baseOnTabSelectedListener2 = this.selectedListener;
        if (baseOnTabSelectedListener2 != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener2);
        }
        this.selectedListener = baseOnTabSelectedListener;
        if (baseOnTabSelectedListener != null) {
            addOnTabSelectedListener(baseOnTabSelectedListener);
        }
    }

    void setPagerAdapter(PagerAdapter pagerAdapter, boolean z) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (pagerAdapter2 != null && (dataSetObserver = this.pagerAdapterObserver) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.pagerAdapter = pagerAdapter;
        if (z && pagerAdapter != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver(this);
            }
            pagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
        }
        populateFromPagerAdapter();
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        ensureScrollAnimator();
        this.scrollAnimator.addListener(animatorListener);
    }

    public void setScrollPosition(int i, float f, boolean z) {
        setScrollPosition(i, f, z, true);
    }

    void setScrollPosition(int i, float f, boolean z, boolean z2) {
        int iRound = Math.round(i + f);
        if (iRound < 0 || iRound >= this.slidingTabIndicator.getChildCount()) {
            return;
        }
        if (z2) {
            this.slidingTabIndicator.setIndicatorPositionFromTabPosition(i, f);
        }
        ValueAnimator valueAnimator = this.scrollAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.scrollAnimator.cancel();
        }
        scrollTo(calculateScrollXForTab(i, f), 0);
        if (z) {
            setSelectedTabView(iRound);
        }
    }

    public void setSelectedTabIndicator(int i) {
        if (i != 0) {
            setSelectedTabIndicator(AppCompatResources.getDrawable(getContext(), i));
        } else {
            setSelectedTabIndicator((Drawable) null);
        }
    }

    public void setSelectedTabIndicator(Drawable drawable) {
        if (this.tabSelectedIndicator != drawable) {
            this.tabSelectedIndicator = drawable;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }
    }

    public void setSelectedTabIndicatorColor(int i) {
        this.slidingTabIndicator.setSelectedIndicatorColor(i);
    }

    public void setSelectedTabIndicatorGravity(int i) {
        if (this.tabIndicatorGravity != i) {
            this.tabIndicatorGravity = i;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int i) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(i);
    }

    public void setTabGravity(int i) {
        if (this.tabGravity != i) {
            this.tabGravity = i;
            applyModeAndGravity();
        }
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.tabIconTint != colorStateList) {
            this.tabIconTint = colorStateList;
            updateAllTabs();
        }
    }

    public void setTabIconTintResource(int i) {
        setTabIconTint(AppCompatResources.getColorStateList(getContext(), i));
    }

    public void setTabIndicatorFullWidth(boolean z) {
        this.tabIndicatorFullWidth = z;
        ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
    }

    public void setTabMode(int i) {
        if (i != this.mode) {
            this.mode = i;
            applyModeAndGravity();
        }
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.tabRippleColorStateList != colorStateList) {
            this.tabRippleColorStateList = colorStateList;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setTabRippleColorResource(int i) {
        setTabRippleColor(AppCompatResources.getColorStateList(getContext(), i));
    }

    public void setTabTextColors(int i, int i2) {
        setTabTextColors(createColorStateList(i, i2));
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.tabTextColors != colorStateList) {
            this.tabTextColors = colorStateList;
            updateAllTabs();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        setPagerAdapter(pagerAdapter, false);
    }

    public void setUnboundedRipple(boolean z) {
        if (this.unboundedRipple != z) {
            this.unboundedRipple = z;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setUnboundedRippleResource(int i) {
        setUnboundedRipple(getResources().getBoolean(i));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(ViewPager viewPager, boolean z) {
        setupWithViewPager(viewPager, z, false);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    void updateTabViews(boolean z) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            childAt.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) childAt.getLayoutParams());
            if (z) {
                childAt.requestLayout();
            }
        }
    }
}
