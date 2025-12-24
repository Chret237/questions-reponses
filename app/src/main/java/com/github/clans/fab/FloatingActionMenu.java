package com.github.clans.fab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes-dex2jar.jar:com/github/clans/fab/FloatingActionMenu.class */
public class FloatingActionMenu extends ViewGroup {
    private static final int ANIMATION_DURATION = 300;
    private static final float CLOSED_PLUS_ROTATION = 0.0f;
    private static final int LABELS_POSITION_LEFT = 0;
    private static final int LABELS_POSITION_RIGHT = 1;
    private static final float OPENED_PLUS_ROTATION_LEFT = -135.0f;
    private static final float OPENED_PLUS_ROTATION_RIGHT = 135.0f;
    private static final int OPEN_DOWN = 1;
    private static final int OPEN_UP = 0;
    private int mAnimationDelayPerItem;
    private int mBackgroundColor;
    private int mButtonSpacing;
    private int mButtonsCount;
    private AnimatorSet mCloseAnimatorSet;
    private Interpolator mCloseInterpolator;
    private Typeface mCustomTypefaceFromFont;
    private ValueAnimator mHideBackgroundAnimator;
    private Drawable mIcon;
    private boolean mIconAnimated;
    private AnimatorSet mIconToggleSet;
    private ImageView mImageToggle;
    private Animation mImageToggleHideAnimation;
    private Animation mImageToggleShowAnimation;
    private boolean mIsAnimated;
    private boolean mIsMenuButtonAnimationRunning;
    private boolean mIsMenuOpening;
    private boolean mIsSetClosedOnTouchOutside;
    private int mLabelsColorNormal;
    private int mLabelsColorPressed;
    private int mLabelsColorRipple;
    private Context mLabelsContext;
    private int mLabelsCornerRadius;
    private int mLabelsEllipsize;
    private int mLabelsHideAnimation;
    private int mLabelsMargin;
    private int mLabelsMaxLines;
    private int mLabelsPaddingBottom;
    private int mLabelsPaddingLeft;
    private int mLabelsPaddingRight;
    private int mLabelsPaddingTop;
    private int mLabelsPosition;
    private int mLabelsShowAnimation;
    private boolean mLabelsShowShadow;
    private boolean mLabelsSingleLine;
    private int mLabelsStyle;
    private ColorStateList mLabelsTextColor;
    private float mLabelsTextSize;
    private int mLabelsVerticalOffset;
    private int mMaxButtonWidth;
    private FloatingActionButton mMenuButton;
    private Animation mMenuButtonHideAnimation;
    private Animation mMenuButtonShowAnimation;
    private int mMenuColorNormal;
    private int mMenuColorPressed;
    private int mMenuColorRipple;
    private int mMenuFabSize;
    private String mMenuLabelText;
    private boolean mMenuOpened;
    private int mMenuShadowColor;
    private float mMenuShadowRadius;
    private float mMenuShadowXOffset;
    private float mMenuShadowYOffset;
    private boolean mMenuShowShadow;
    private AnimatorSet mOpenAnimatorSet;
    private int mOpenDirection;
    private Interpolator mOpenInterpolator;
    private ValueAnimator mShowBackgroundAnimator;
    private OnMenuToggleListener mToggleListener;
    private Handler mUiHandler;
    private boolean mUsingMenuLabel;

    /* loaded from: classes-dex2jar.jar:com/github/clans/fab/FloatingActionMenu$OnMenuToggleListener.class */
    public interface OnMenuToggleListener {
        void onMenuToggle(boolean z);
    }

    public FloatingActionMenu(Context context) {
        this(context, null);
    }

    public FloatingActionMenu(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOpenAnimatorSet = new AnimatorSet();
        this.mCloseAnimatorSet = new AnimatorSet();
        this.mButtonSpacing = Util.dpToPx(getContext(), 0.0f);
        this.mLabelsMargin = Util.dpToPx(getContext(), 0.0f);
        this.mLabelsVerticalOffset = Util.dpToPx(getContext(), 0.0f);
        this.mUiHandler = new Handler();
        this.mLabelsPaddingTop = Util.dpToPx(getContext(), 4.0f);
        this.mLabelsPaddingRight = Util.dpToPx(getContext(), 8.0f);
        this.mLabelsPaddingBottom = Util.dpToPx(getContext(), 4.0f);
        this.mLabelsPaddingLeft = Util.dpToPx(getContext(), 8.0f);
        this.mLabelsCornerRadius = Util.dpToPx(getContext(), 3.0f);
        this.mMenuShadowRadius = 4.0f;
        this.mMenuShadowXOffset = 1.0f;
        this.mMenuShadowYOffset = 3.0f;
        this.mIsAnimated = true;
        this.mIconAnimated = true;
        init(context, attributeSet);
    }

    private void addLabel(FloatingActionButton floatingActionButton) {
        String labelText = floatingActionButton.getLabelText();
        if (TextUtils.isEmpty(labelText)) {
            return;
        }
        Label label = new Label(this.mLabelsContext);
        label.setClickable(true);
        label.setFab(floatingActionButton);
        label.setShowAnimation(AnimationUtils.loadAnimation(getContext(), this.mLabelsShowAnimation));
        label.setHideAnimation(AnimationUtils.loadAnimation(getContext(), this.mLabelsHideAnimation));
        if (this.mLabelsStyle > 0) {
            label.setTextAppearance(getContext(), this.mLabelsStyle);
            label.setShowShadow(false);
            label.setUsingStyle(true);
        } else {
            label.setColors(this.mLabelsColorNormal, this.mLabelsColorPressed, this.mLabelsColorRipple);
            label.setShowShadow(this.mLabelsShowShadow);
            label.setCornerRadius(this.mLabelsCornerRadius);
            if (this.mLabelsEllipsize > 0) {
                setLabelEllipsize(label);
            }
            label.setMaxLines(this.mLabelsMaxLines);
            label.updateBackground();
            label.setTextSize(0, this.mLabelsTextSize);
            label.setTextColor(this.mLabelsTextColor);
            int i = this.mLabelsPaddingLeft;
            int i2 = this.mLabelsPaddingTop;
            int shadowRadius = i;
            int shadowRadius2 = i2;
            if (this.mLabelsShowShadow) {
                shadowRadius = i + floatingActionButton.getShadowRadius() + Math.abs(floatingActionButton.getShadowXOffset());
                shadowRadius2 = i2 + floatingActionButton.getShadowRadius() + Math.abs(floatingActionButton.getShadowYOffset());
            }
            label.setPadding(shadowRadius, shadowRadius2, this.mLabelsPaddingLeft, this.mLabelsPaddingTop);
            if (this.mLabelsMaxLines < 0 || this.mLabelsSingleLine) {
                label.setSingleLine(this.mLabelsSingleLine);
            }
        }
        Typeface typeface = this.mCustomTypefaceFromFont;
        if (typeface != null) {
            label.setTypeface(typeface);
        }
        label.setText(labelText);
        label.setOnClickListener(floatingActionButton.getOnClickListener());
        addView(label);
        floatingActionButton.setTag(C0418R.id.fab_label, label);
    }

    private int adjustForOvershoot(int i) {
        double d = i;
        Double.isNaN(d);
        Double.isNaN(d);
        return (int) ((0.03d * d) + d);
    }

    private void createDefaultIconAnimation() {
        float f;
        float f2 = 135.0f;
        if (this.mOpenDirection == 0) {
            float f3 = this.mLabelsPosition == 0 ? -135.0f : 135.0f;
            f = f3;
            if (this.mLabelsPosition == 0) {
                f = f3;
                f2 = -135.0f;
            }
        } else {
            float f4 = this.mLabelsPosition == 0 ? 135.0f : -135.0f;
            f = f4;
            if (this.mLabelsPosition == 0) {
                f = f4;
            } else {
                f2 = -135.0f;
            }
        }
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.mImageToggle, "rotation", f, 0.0f);
        this.mOpenAnimatorSet.play(ObjectAnimator.ofFloat(this.mImageToggle, "rotation", 0.0f, f2));
        this.mCloseAnimatorSet.play(objectAnimatorOfFloat);
        this.mOpenAnimatorSet.setInterpolator(this.mOpenInterpolator);
        this.mCloseAnimatorSet.setInterpolator(this.mCloseInterpolator);
        this.mOpenAnimatorSet.setDuration(300L);
        this.mCloseAnimatorSet.setDuration(300L);
    }

    private void createLabels() {
        for (int i = 0; i < this.mButtonsCount; i++) {
            if (getChildAt(i) != this.mImageToggle) {
                FloatingActionButton floatingActionButton = (FloatingActionButton) getChildAt(i);
                if (floatingActionButton.getTag(C0418R.id.fab_label) == null) {
                    addLabel(floatingActionButton);
                    FloatingActionButton floatingActionButton2 = this.mMenuButton;
                    if (floatingActionButton == floatingActionButton2) {
                        floatingActionButton2.setOnClickListener(new View.OnClickListener(this) { // from class: com.github.clans.fab.FloatingActionMenu.3
                            final FloatingActionMenu this$0;

                            {
                                this.this$0 = this;
                            }

                            @Override // android.view.View.OnClickListener
                            public void onClick(View view) {
                                FloatingActionMenu floatingActionMenu = this.this$0;
                                floatingActionMenu.toggle(floatingActionMenu.mIsAnimated);
                            }
                        });
                    }
                }
            }
        }
    }

    private void createMenuButton() {
        FloatingActionButton floatingActionButton = new FloatingActionButton(getContext());
        this.mMenuButton = floatingActionButton;
        floatingActionButton.mShowShadow = this.mMenuShowShadow;
        if (this.mMenuShowShadow) {
            this.mMenuButton.mShadowRadius = Util.dpToPx(getContext(), this.mMenuShadowRadius);
            this.mMenuButton.mShadowXOffset = Util.dpToPx(getContext(), this.mMenuShadowXOffset);
            this.mMenuButton.mShadowYOffset = Util.dpToPx(getContext(), this.mMenuShadowYOffset);
        }
        this.mMenuButton.setColors(this.mMenuColorNormal, this.mMenuColorPressed, this.mMenuColorRipple);
        this.mMenuButton.mShadowColor = this.mMenuShadowColor;
        this.mMenuButton.mFabSize = this.mMenuFabSize;
        this.mMenuButton.updateBackground();
        this.mMenuButton.setLabelText(this.mMenuLabelText);
        ImageView imageView = new ImageView(getContext());
        this.mImageToggle = imageView;
        imageView.setImageDrawable(this.mIcon);
        addView(this.mMenuButton, super.generateDefaultLayoutParams());
        addView(this.mImageToggle);
        createDefaultIconAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideMenuButtonWithImage(boolean z) {
        if (isMenuButtonHidden()) {
            return;
        }
        this.mMenuButton.hide(z);
        if (z) {
            this.mImageToggle.startAnimation(this.mImageToggleHideAnimation);
        }
        this.mImageToggle.setVisibility(4);
        this.mIsMenuButtonAnimationRunning = false;
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0418R.styleable.FloatingActionMenu, 0, 0);
        this.mButtonSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_buttonSpacing, this.mButtonSpacing);
        this.mLabelsMargin = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_margin, this.mLabelsMargin);
        this.mLabelsPosition = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_labels_position, 0);
        this.mLabelsShowAnimation = typedArrayObtainStyledAttributes.getResourceId(C0418R.styleable.FloatingActionMenu_menu_labels_showAnimation, this.mLabelsPosition == 0 ? C0418R.anim.fab_slide_in_from_right : C0418R.anim.fab_slide_in_from_left);
        this.mLabelsHideAnimation = typedArrayObtainStyledAttributes.getResourceId(C0418R.styleable.FloatingActionMenu_menu_labels_hideAnimation, this.mLabelsPosition == 0 ? C0418R.anim.fab_slide_out_to_right : C0418R.anim.fab_slide_out_to_left);
        this.mLabelsPaddingTop = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_paddingTop, this.mLabelsPaddingTop);
        this.mLabelsPaddingRight = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_paddingRight, this.mLabelsPaddingRight);
        this.mLabelsPaddingBottom = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_paddingBottom, this.mLabelsPaddingBottom);
        this.mLabelsPaddingLeft = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_paddingLeft, this.mLabelsPaddingLeft);
        ColorStateList colorStateList = typedArrayObtainStyledAttributes.getColorStateList(C0418R.styleable.FloatingActionMenu_menu_labels_textColor);
        this.mLabelsTextColor = colorStateList;
        if (colorStateList == null) {
            this.mLabelsTextColor = ColorStateList.valueOf(-1);
        }
        this.mLabelsTextSize = typedArrayObtainStyledAttributes.getDimension(C0418R.styleable.FloatingActionMenu_menu_labels_textSize, getResources().getDimension(C0418R.dimen.labels_text_size));
        this.mLabelsCornerRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_cornerRadius, this.mLabelsCornerRadius);
        this.mLabelsShowShadow = typedArrayObtainStyledAttributes.getBoolean(C0418R.styleable.FloatingActionMenu_menu_labels_showShadow, true);
        this.mLabelsColorNormal = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_labels_colorNormal, -13421773);
        this.mLabelsColorPressed = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_labels_colorPressed, -12303292);
        this.mLabelsColorRipple = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_labels_colorRipple, 1728053247);
        this.mMenuShowShadow = typedArrayObtainStyledAttributes.getBoolean(C0418R.styleable.FloatingActionMenu_menu_showShadow, true);
        this.mMenuShadowColor = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_shadowColor, 1711276032);
        this.mMenuShadowRadius = typedArrayObtainStyledAttributes.getDimension(C0418R.styleable.FloatingActionMenu_menu_shadowRadius, this.mMenuShadowRadius);
        this.mMenuShadowXOffset = typedArrayObtainStyledAttributes.getDimension(C0418R.styleable.FloatingActionMenu_menu_shadowXOffset, this.mMenuShadowXOffset);
        this.mMenuShadowYOffset = typedArrayObtainStyledAttributes.getDimension(C0418R.styleable.FloatingActionMenu_menu_shadowYOffset, this.mMenuShadowYOffset);
        this.mMenuColorNormal = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_colorNormal, -2473162);
        this.mMenuColorPressed = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_colorPressed, -1617853);
        this.mMenuColorRipple = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_colorRipple, -1711276033);
        this.mAnimationDelayPerItem = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_animationDelayPerItem, 50);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(C0418R.styleable.FloatingActionMenu_menu_icon);
        this.mIcon = drawable;
        if (drawable == null) {
            this.mIcon = getResources().getDrawable(C0418R.drawable.fab_add);
        }
        this.mLabelsSingleLine = typedArrayObtainStyledAttributes.getBoolean(C0418R.styleable.FloatingActionMenu_menu_labels_singleLine, false);
        this.mLabelsEllipsize = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_labels_ellipsize, 0);
        this.mLabelsMaxLines = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_labels_maxLines, -1);
        this.mMenuFabSize = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_fab_size, 0);
        this.mLabelsStyle = typedArrayObtainStyledAttributes.getResourceId(C0418R.styleable.FloatingActionMenu_menu_labels_style, 0);
        String string = typedArrayObtainStyledAttributes.getString(C0418R.styleable.FloatingActionMenu_menu_labels_customFont);
        try {
            if (!TextUtils.isEmpty(string)) {
                this.mCustomTypefaceFromFont = Typeface.createFromAsset(getContext().getAssets(), string);
            }
            this.mOpenDirection = typedArrayObtainStyledAttributes.getInt(C0418R.styleable.FloatingActionMenu_menu_openDirection, 0);
            this.mBackgroundColor = typedArrayObtainStyledAttributes.getColor(C0418R.styleable.FloatingActionMenu_menu_backgroundColor, 0);
            if (typedArrayObtainStyledAttributes.hasValue(C0418R.styleable.FloatingActionMenu_menu_fab_label)) {
                this.mUsingMenuLabel = true;
                this.mMenuLabelText = typedArrayObtainStyledAttributes.getString(C0418R.styleable.FloatingActionMenu_menu_fab_label);
            }
            if (typedArrayObtainStyledAttributes.hasValue(C0418R.styleable.FloatingActionMenu_menu_labels_padding)) {
                initPadding(typedArrayObtainStyledAttributes.getDimensionPixelSize(C0418R.styleable.FloatingActionMenu_menu_labels_padding, 0));
            }
            this.mOpenInterpolator = new OvershootInterpolator();
            this.mCloseInterpolator = new AnticipateInterpolator();
            this.mLabelsContext = new ContextThemeWrapper(getContext(), this.mLabelsStyle);
            initBackgroundDimAnimation();
            createMenuButton();
            initMenuButtonAnimations(typedArrayObtainStyledAttributes);
            typedArrayObtainStyledAttributes.recycle();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Unable to load specified custom font: " + string, e);
        }
    }

    private void initBackgroundDimAnimation() {
        int iAlpha = Color.alpha(this.mBackgroundColor);
        int iRed = Color.red(this.mBackgroundColor);
        int iGreen = Color.green(this.mBackgroundColor);
        int iBlue = Color.blue(this.mBackgroundColor);
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(0, iAlpha);
        this.mShowBackgroundAnimator = valueAnimatorOfInt;
        valueAnimatorOfInt.setDuration(300L);
        this.mShowBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, iRed, iGreen, iBlue) { // from class: com.github.clans.fab.FloatingActionMenu.1
            final FloatingActionMenu this$0;
            final int val$blue;
            final int val$green;
            final int val$red;

            {
                this.this$0 = this;
                this.val$red = iRed;
                this.val$green = iGreen;
                this.val$blue = iBlue;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.setBackgroundColor(Color.argb(((Integer) valueAnimator.getAnimatedValue()).intValue(), this.val$red, this.val$green, this.val$blue));
            }
        });
        ValueAnimator valueAnimatorOfInt2 = ValueAnimator.ofInt(iAlpha, 0);
        this.mHideBackgroundAnimator = valueAnimatorOfInt2;
        valueAnimatorOfInt2.setDuration(300L);
        this.mHideBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, iRed, iGreen, iBlue) { // from class: com.github.clans.fab.FloatingActionMenu.2
            final FloatingActionMenu this$0;
            final int val$blue;
            final int val$green;
            final int val$red;

            {
                this.this$0 = this;
                this.val$red = iRed;
                this.val$green = iGreen;
                this.val$blue = iBlue;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.setBackgroundColor(Color.argb(((Integer) valueAnimator.getAnimatedValue()).intValue(), this.val$red, this.val$green, this.val$blue));
            }
        });
    }

    private void initMenuButtonAnimations(TypedArray typedArray) {
        int resourceId = typedArray.getResourceId(C0418R.styleable.FloatingActionMenu_menu_fab_show_animation, C0418R.anim.fab_scale_up);
        setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getContext(), resourceId));
        this.mImageToggleShowAnimation = AnimationUtils.loadAnimation(getContext(), resourceId);
        int resourceId2 = typedArray.getResourceId(C0418R.styleable.FloatingActionMenu_menu_fab_hide_animation, C0418R.anim.fab_scale_down);
        setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getContext(), resourceId2));
        this.mImageToggleHideAnimation = AnimationUtils.loadAnimation(getContext(), resourceId2);
    }

    private void initPadding(int i) {
        this.mLabelsPaddingTop = i;
        this.mLabelsPaddingRight = i;
        this.mLabelsPaddingBottom = i;
        this.mLabelsPaddingLeft = i;
    }

    private boolean isBackgroundEnabled() {
        return this.mBackgroundColor != 0;
    }

    private void setLabelEllipsize(Label label) {
        int i = this.mLabelsEllipsize;
        if (i == 1) {
            label.setEllipsize(TextUtils.TruncateAt.START);
            return;
        }
        if (i == 2) {
            label.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        } else if (i == 3) {
            label.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            if (i != 4) {
                return;
            }
            label.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
    }

    private void showMenuButtonWithImage(boolean z) {
        if (isMenuButtonHidden()) {
            this.mMenuButton.show(z);
            if (z) {
                this.mImageToggle.startAnimation(this.mImageToggleShowAnimation);
            }
            this.mImageToggle.setVisibility(0);
        }
    }

    public void addMenuButton(FloatingActionButton floatingActionButton) {
        addView(floatingActionButton, this.mButtonsCount - 2);
        this.mButtonsCount++;
        addLabel(floatingActionButton);
    }

    public void addMenuButton(FloatingActionButton floatingActionButton, int i) {
        int i2;
        int i3 = this.mButtonsCount - 2;
        if (i < 0) {
            i2 = 0;
        } else {
            i2 = i;
            if (i > i3) {
                i2 = i3;
            }
        }
        addView(floatingActionButton, i2);
        this.mButtonsCount++;
        addLabel(floatingActionButton);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams;
    }

    public void close(boolean z) {
        if (!isOpened()) {
            return;
        }
        if (isBackgroundEnabled()) {
            this.mHideBackgroundAnimator.start();
        }
        if (this.mIconAnimated) {
            AnimatorSet animatorSet = this.mIconToggleSet;
            if (animatorSet != null) {
                animatorSet.start();
            } else {
                this.mCloseAnimatorSet.start();
                this.mOpenAnimatorSet.cancel();
            }
        }
        int i = 0;
        this.mIsMenuOpening = false;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i >= getChildCount()) {
                this.mUiHandler.postDelayed(new Runnable(this) { // from class: com.github.clans.fab.FloatingActionMenu.7
                    final FloatingActionMenu this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this.this$0.mMenuOpened = false;
                        if (this.this$0.mToggleListener != null) {
                            this.this$0.mToggleListener.onMenuToggle(false);
                        }
                    }
                }, (i2 + 1) * this.mAnimationDelayPerItem);
                return;
            }
            View childAt = getChildAt(i);
            int i5 = i2;
            int i6 = i4;
            if (childAt instanceof FloatingActionButton) {
                i5 = i2;
                i6 = i4;
                if (childAt.getVisibility() != 8) {
                    i5 = i2 + 1;
                    this.mUiHandler.postDelayed(new Runnable(this, (FloatingActionButton) childAt, z) { // from class: com.github.clans.fab.FloatingActionMenu.6
                        final FloatingActionMenu this$0;
                        final boolean val$animate;
                        final FloatingActionButton val$fab;

                        {
                            this.this$0 = this;
                            this.val$fab = floatingActionButton;
                            this.val$animate = z;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (this.this$0.isOpened()) {
                                if (this.val$fab != this.this$0.mMenuButton) {
                                    this.val$fab.hide(this.val$animate);
                                }
                                Label label = (Label) this.val$fab.getTag(C0418R.id.fab_label);
                                if (label == null || !label.isHandleVisibilityChanges()) {
                                    return;
                                }
                                label.hide(this.val$animate);
                            }
                        }
                    }, i4);
                    i6 = i4 + this.mAnimationDelayPerItem;
                }
            }
            i++;
            i2 = i5;
            i3 = i6;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.MarginLayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ViewGroup.MarginLayoutParams(layoutParams);
    }

    public int getAnimationDelayPerItem() {
        return this.mAnimationDelayPerItem;
    }

    public AnimatorSet getIconToggleAnimatorSet() {
        return this.mIconToggleSet;
    }

    public int getMenuButtonColorNormal() {
        return this.mMenuColorNormal;
    }

    public int getMenuButtonColorPressed() {
        return this.mMenuColorPressed;
    }

    public int getMenuButtonColorRipple() {
        return this.mMenuColorRipple;
    }

    public String getMenuButtonLabelText() {
        return this.mMenuLabelText;
    }

    public ImageView getMenuIconView() {
        return this.mImageToggle;
    }

    public void hideMenu(boolean z) {
        if (isMenuHidden() || this.mIsMenuButtonAnimationRunning) {
            return;
        }
        this.mIsMenuButtonAnimationRunning = true;
        if (isOpened()) {
            close(z);
            this.mUiHandler.postDelayed(new Runnable(this, z) { // from class: com.github.clans.fab.FloatingActionMenu.8
                final FloatingActionMenu this$0;
                final boolean val$animate;

                {
                    this.this$0 = this;
                    this.val$animate = z;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (this.val$animate) {
                        FloatingActionMenu floatingActionMenu = this.this$0;
                        floatingActionMenu.startAnimation(floatingActionMenu.mMenuButtonHideAnimation);
                    }
                    this.this$0.setVisibility(4);
                    this.this$0.mIsMenuButtonAnimationRunning = false;
                }
            }, this.mAnimationDelayPerItem * this.mButtonsCount);
        } else {
            if (z) {
                startAnimation(this.mMenuButtonHideAnimation);
            }
            setVisibility(4);
            this.mIsMenuButtonAnimationRunning = false;
        }
    }

    public void hideMenuButton(boolean z) {
        if (isMenuButtonHidden() || this.mIsMenuButtonAnimationRunning) {
            return;
        }
        this.mIsMenuButtonAnimationRunning = true;
        if (!isOpened()) {
            hideMenuButtonWithImage(z);
        } else {
            close(z);
            this.mUiHandler.postDelayed(new Runnable(this, z) { // from class: com.github.clans.fab.FloatingActionMenu.9
                final FloatingActionMenu this$0;
                final boolean val$animate;

                {
                    this.this$0 = this;
                    this.val$animate = z;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.this$0.hideMenuButtonWithImage(this.val$animate);
                }
            }, this.mAnimationDelayPerItem * this.mButtonsCount);
        }
    }

    public boolean isAnimated() {
        return this.mIsAnimated;
    }

    public boolean isIconAnimated() {
        return this.mIconAnimated;
    }

    public boolean isMenuButtonHidden() {
        return this.mMenuButton.isHidden();
    }

    public boolean isMenuHidden() {
        return getVisibility() == 4;
    }

    public boolean isOpened() {
        return this.mMenuOpened;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        bringChildToFront(this.mMenuButton);
        bringChildToFront(this.mImageToggle);
        this.mButtonsCount = getChildCount();
        createLabels();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingRight = this.mLabelsPosition == 0 ? ((i3 - i) - (this.mMaxButtonWidth / 2)) - getPaddingRight() : (this.mMaxButtonWidth / 2) + getPaddingLeft();
        boolean z2 = this.mOpenDirection == 0;
        int measuredHeight = z2 ? ((i4 - i2) - this.mMenuButton.getMeasuredHeight()) - getPaddingBottom() : getPaddingTop();
        int measuredWidth = paddingRight - (this.mMenuButton.getMeasuredWidth() / 2);
        FloatingActionButton floatingActionButton = this.mMenuButton;
        floatingActionButton.layout(measuredWidth, measuredHeight, floatingActionButton.getMeasuredWidth() + measuredWidth, this.mMenuButton.getMeasuredHeight() + measuredHeight);
        int measuredWidth2 = paddingRight - (this.mImageToggle.getMeasuredWidth() / 2);
        int measuredHeight2 = ((this.mMenuButton.getMeasuredHeight() / 2) + measuredHeight) - (this.mImageToggle.getMeasuredHeight() / 2);
        ImageView imageView = this.mImageToggle;
        imageView.layout(measuredWidth2, measuredHeight2, imageView.getMeasuredWidth() + measuredWidth2, this.mImageToggle.getMeasuredHeight() + measuredHeight2);
        int measuredHeight3 = measuredHeight;
        if (z2) {
            measuredHeight3 = measuredHeight + this.mMenuButton.getMeasuredHeight() + this.mButtonSpacing;
        }
        int measuredHeight4 = measuredHeight3;
        for (int i5 = this.mButtonsCount - 1; i5 >= 0; i5--) {
            View childAt = getChildAt(i5);
            if (childAt != this.mImageToggle) {
                FloatingActionButton floatingActionButton2 = (FloatingActionButton) childAt;
                if (floatingActionButton2.getVisibility() != 8) {
                    int measuredWidth3 = paddingRight - (floatingActionButton2.getMeasuredWidth() / 2);
                    int measuredHeight5 = measuredHeight4;
                    if (z2) {
                        measuredHeight5 = (measuredHeight4 - floatingActionButton2.getMeasuredHeight()) - this.mButtonSpacing;
                    }
                    if (floatingActionButton2 != this.mMenuButton) {
                        floatingActionButton2.layout(measuredWidth3, measuredHeight5, floatingActionButton2.getMeasuredWidth() + measuredWidth3, floatingActionButton2.getMeasuredHeight() + measuredHeight5);
                        if (!this.mIsMenuOpening) {
                            floatingActionButton2.hide(false);
                        }
                    }
                    View view = (View) floatingActionButton2.getTag(C0418R.id.fab_label);
                    if (view != null) {
                        int measuredWidth4 = ((this.mUsingMenuLabel ? this.mMaxButtonWidth : floatingActionButton2.getMeasuredWidth()) / 2) + this.mLabelsMargin;
                        int i6 = this.mLabelsPosition == 0 ? paddingRight - measuredWidth4 : measuredWidth4 + paddingRight;
                        int measuredWidth5 = this.mLabelsPosition == 0 ? i6 - view.getMeasuredWidth() : view.getMeasuredWidth() + i6;
                        int i7 = this.mLabelsPosition == 0 ? measuredWidth5 : i6;
                        if (this.mLabelsPosition != 0) {
                            i6 = measuredWidth5;
                        }
                        int measuredHeight6 = (measuredHeight5 - this.mLabelsVerticalOffset) + ((floatingActionButton2.getMeasuredHeight() - view.getMeasuredHeight()) / 2);
                        view.layout(i7, measuredHeight6, i6, view.getMeasuredHeight() + measuredHeight6);
                        if (!this.mIsMenuOpening) {
                            view.setVisibility(4);
                        }
                    }
                    measuredHeight4 = z2 ? measuredHeight5 - this.mButtonSpacing : measuredHeight5 + childAt.getMeasuredHeight() + this.mButtonSpacing;
                }
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        this.mMaxButtonWidth = 0;
        measureChildWithMargins(this.mImageToggle, i, 0, i2, 0);
        for (int i4 = 0; i4 < this.mButtonsCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8 && childAt != this.mImageToggle) {
                measureChildWithMargins(childAt, i, 0, i2, 0);
                this.mMaxButtonWidth = Math.max(this.mMaxButtonWidth, childAt.getMeasuredWidth());
            }
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            i3 = i7;
            if (i6 >= this.mButtonsCount) {
                break;
            }
            View childAt2 = getChildAt(i6);
            int i8 = i5;
            int iMax = i3;
            if (childAt2.getVisibility() != 8) {
                if (childAt2 == this.mImageToggle) {
                    i8 = i5;
                    iMax = i3;
                } else {
                    int measuredWidth = childAt2.getMeasuredWidth();
                    int measuredHeight = childAt2.getMeasuredHeight();
                    Label label = (Label) childAt2.getTag(C0418R.id.fab_label);
                    iMax = i3;
                    if (label != null) {
                        int measuredWidth2 = (this.mMaxButtonWidth - childAt2.getMeasuredWidth()) / (this.mUsingMenuLabel ? 1 : 2);
                        measureChildWithMargins(label, i, childAt2.getMeasuredWidth() + label.calculateShadowWidth() + this.mLabelsMargin + measuredWidth2, i2, 0);
                        iMax = Math.max(i3, measuredWidth + 0 + label.getMeasuredWidth() + measuredWidth2);
                    }
                    i8 = i5 + measuredHeight;
                }
            }
            i6++;
            i5 = i8;
            i7 = iMax;
        }
        int iMax2 = Math.max(this.mMaxButtonWidth, i3 + this.mLabelsMargin) + getPaddingLeft() + getPaddingRight();
        int iAdjustForOvershoot = adjustForOvershoot(i5 + (this.mButtonSpacing * (this.mButtonsCount - 1)) + getPaddingTop() + getPaddingBottom());
        if (getLayoutParams().width == -1) {
            iMax2 = getDefaultSize(getSuggestedMinimumWidth(), i);
        }
        int defaultSize = iAdjustForOvershoot;
        if (getLayoutParams().height == -1) {
            defaultSize = getDefaultSize(getSuggestedMinimumHeight(), i2);
        }
        setMeasuredDimension(iMax2, defaultSize);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsSetClosedOnTouchOutside) {
            return super.onTouchEvent(motionEvent);
        }
        boolean zIsOpened = false;
        int action = motionEvent.getAction();
        if (action == 0) {
            zIsOpened = isOpened();
        } else if (action == 1) {
            close(this.mIsAnimated);
            zIsOpened = true;
        }
        return zIsOpened;
    }

    public void open(boolean z) {
        if (isOpened()) {
            return;
        }
        if (isBackgroundEnabled()) {
            this.mShowBackgroundAnimator.start();
        }
        if (this.mIconAnimated) {
            AnimatorSet animatorSet = this.mIconToggleSet;
            if (animatorSet != null) {
                animatorSet.start();
            } else {
                this.mCloseAnimatorSet.cancel();
                this.mOpenAnimatorSet.start();
            }
        }
        this.mIsMenuOpening = true;
        int childCount = getChildCount() - 1;
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (childCount < 0) {
                this.mUiHandler.postDelayed(new Runnable(this) { // from class: com.github.clans.fab.FloatingActionMenu.5
                    final FloatingActionMenu this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this.this$0.mMenuOpened = true;
                        if (this.this$0.mToggleListener != null) {
                            this.this$0.mToggleListener.onMenuToggle(true);
                        }
                    }
                }, (i + 1) * this.mAnimationDelayPerItem);
                return;
            }
            View childAt = getChildAt(childCount);
            int i4 = i;
            int i5 = i3;
            if (childAt instanceof FloatingActionButton) {
                i4 = i;
                i5 = i3;
                if (childAt.getVisibility() != 8) {
                    i4 = i + 1;
                    this.mUiHandler.postDelayed(new Runnable(this, (FloatingActionButton) childAt, z) { // from class: com.github.clans.fab.FloatingActionMenu.4
                        final FloatingActionMenu this$0;
                        final boolean val$animate;
                        final FloatingActionButton val$fab;

                        {
                            this.this$0 = this;
                            this.val$fab = floatingActionButton;
                            this.val$animate = z;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            if (this.this$0.isOpened()) {
                                return;
                            }
                            if (this.val$fab != this.this$0.mMenuButton) {
                                this.val$fab.show(this.val$animate);
                            }
                            Label label = (Label) this.val$fab.getTag(C0418R.id.fab_label);
                            if (label == null || !label.isHandleVisibilityChanges()) {
                                return;
                            }
                            label.show(this.val$animate);
                        }
                    }, i3);
                    i5 = i3 + this.mAnimationDelayPerItem;
                }
            }
            childCount--;
            i = i4;
            i2 = i5;
        }
    }

    public void removeAllMenuButtons() {
        close(true);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != this.mMenuButton && childAt != this.mImageToggle && (childAt instanceof FloatingActionButton)) {
                arrayList.add((FloatingActionButton) childAt);
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            removeMenuButton((FloatingActionButton) it.next());
        }
    }

    public void removeMenuButton(FloatingActionButton floatingActionButton) {
        removeView(floatingActionButton.getLabelView());
        removeView(floatingActionButton);
        this.mButtonsCount--;
    }

    public void setAnimated(boolean z) {
        this.mIsAnimated = z;
        this.mOpenAnimatorSet.setDuration(z ? 300L : 0L);
        this.mCloseAnimatorSet.setDuration(z ? 300L : 0L);
    }

    public void setAnimationDelayPerItem(int i) {
        this.mAnimationDelayPerItem = i;
    }

    public void setClosedOnTouchOutside(boolean z) {
        this.mIsSetClosedOnTouchOutside = z;
    }

    public void setIconAnimated(boolean z) {
        this.mIconAnimated = z;
    }

    public void setIconAnimationCloseInterpolator(Interpolator interpolator) {
        this.mCloseAnimatorSet.setInterpolator(interpolator);
    }

    public void setIconAnimationInterpolator(Interpolator interpolator) {
        this.mOpenAnimatorSet.setInterpolator(interpolator);
        this.mCloseAnimatorSet.setInterpolator(interpolator);
    }

    public void setIconAnimationOpenInterpolator(Interpolator interpolator) {
        this.mOpenAnimatorSet.setInterpolator(interpolator);
    }

    public void setIconToggleAnimatorSet(AnimatorSet animatorSet) {
        this.mIconToggleSet = animatorSet;
    }

    public void setMenuButtonColorNormal(int i) {
        this.mMenuColorNormal = i;
        this.mMenuButton.setColorNormal(i);
    }

    public void setMenuButtonColorNormalResId(int i) {
        this.mMenuColorNormal = getResources().getColor(i);
        this.mMenuButton.setColorNormalResId(i);
    }

    public void setMenuButtonColorPressed(int i) {
        this.mMenuColorPressed = i;
        this.mMenuButton.setColorPressed(i);
    }

    public void setMenuButtonColorPressedResId(int i) {
        this.mMenuColorPressed = getResources().getColor(i);
        this.mMenuButton.setColorPressedResId(i);
    }

    public void setMenuButtonColorRipple(int i) {
        this.mMenuColorRipple = i;
        this.mMenuButton.setColorRipple(i);
    }

    public void setMenuButtonColorRippleResId(int i) {
        this.mMenuColorRipple = getResources().getColor(i);
        this.mMenuButton.setColorRippleResId(i);
    }

    public void setMenuButtonHideAnimation(Animation animation) {
        this.mMenuButtonHideAnimation = animation;
        this.mMenuButton.setHideAnimation(animation);
    }

    public void setMenuButtonLabelText(String str) {
        this.mMenuButton.setLabelText(str);
    }

    public void setMenuButtonShowAnimation(Animation animation) {
        this.mMenuButtonShowAnimation = animation;
        this.mMenuButton.setShowAnimation(animation);
    }

    public void setOnMenuButtonClickListener(View.OnClickListener onClickListener) {
        this.mMenuButton.setOnClickListener(onClickListener);
    }

    public void setOnMenuButtonLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mMenuButton.setOnLongClickListener(onLongClickListener);
    }

    public void setOnMenuToggleListener(OnMenuToggleListener onMenuToggleListener) {
        this.mToggleListener = onMenuToggleListener;
    }

    public void showMenu(boolean z) {
        if (isMenuHidden()) {
            if (z) {
                startAnimation(this.mMenuButtonShowAnimation);
            }
            setVisibility(0);
        }
    }

    public void showMenuButton(boolean z) {
        if (isMenuButtonHidden()) {
            showMenuButtonWithImage(z);
        }
    }

    public void toggle(boolean z) {
        if (isOpened()) {
            close(z);
        } else {
            open(z);
        }
    }

    public void toggleMenu(boolean z) {
        if (isMenuHidden()) {
            showMenu(z);
        } else {
            hideMenu(z);
        }
    }

    public void toggleMenuButton(boolean z) {
        if (isMenuButtonHidden()) {
            showMenuButton(z);
        } else {
            hideMenuButton(z);
        }
    }
}
