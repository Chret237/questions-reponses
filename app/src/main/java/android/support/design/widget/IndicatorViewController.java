package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.design.C0029R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/IndicatorViewController.class */
final class IndicatorViewController {
    private static final int CAPTION_OPACITY_FADE_ANIMATION_DURATION = 167;
    private static final int CAPTION_STATE_ERROR = 1;
    private static final int CAPTION_STATE_HELPER_TEXT = 2;
    private static final int CAPTION_STATE_NONE = 0;
    private static final int CAPTION_TRANSLATE_Y_ANIMATION_DURATION = 217;
    static final int COUNTER_INDEX = 2;
    static final int ERROR_INDEX = 0;
    static final int HELPER_INDEX = 1;
    private Animator captionAnimator;
    private FrameLayout captionArea;
    private int captionDisplayed;
    private int captionToShow;
    private final float captionTranslationYPx;
    private int captionViewsAdded;
    private final Context context;
    private boolean errorEnabled;
    private CharSequence errorText;
    private int errorTextAppearance;
    private TextView errorView;
    private CharSequence helperText;
    private boolean helperTextEnabled;
    private int helperTextTextAppearance;
    private TextView helperTextView;
    private LinearLayout indicatorArea;
    private int indicatorsAdded;
    private final TextInputLayout textInputView;
    private Typeface typeface;

    public IndicatorViewController(TextInputLayout textInputLayout) {
        this.context = textInputLayout.getContext();
        this.textInputView = textInputLayout;
        this.captionTranslationYPx = r0.getResources().getDimensionPixelSize(C0029R.dimen.design_textinput_caption_translate_y);
    }

    private boolean canAdjustIndicatorPadding() {
        return (this.indicatorArea == null || this.textInputView.getEditText() == null) ? false : true;
    }

    private void createCaptionAnimators(List<Animator> list, boolean z, TextView textView, int i, int i2, int i3) {
        if (textView == null || !z) {
            return;
        }
        if (i == i3 || i == i2) {
            list.add(createCaptionOpacityAnimator(textView, i3 == i));
            if (i3 == i) {
                list.add(createCaptionTranslationYAnimator(textView));
            }
        }
    }

    private ObjectAnimator createCaptionOpacityAnimator(TextView textView, boolean z) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(textView, (Property<TextView, Float>) View.ALPHA, z ? 1.0f : 0.0f);
        objectAnimatorOfFloat.setDuration(167L);
        objectAnimatorOfFloat.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        return objectAnimatorOfFloat;
    }

    private ObjectAnimator createCaptionTranslationYAnimator(TextView textView) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(textView, (Property<TextView, Float>) View.TRANSLATION_Y, -this.captionTranslationYPx, 0.0f);
        objectAnimatorOfFloat.setDuration(217L);
        objectAnimatorOfFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        return objectAnimatorOfFloat;
    }

    private TextView getCaptionViewFromDisplayState(int i) {
        if (i == 1) {
            return this.errorView;
        }
        if (i != 2) {
            return null;
        }
        return this.helperTextView;
    }

    private boolean isCaptionStateError(int i) {
        boolean z = true;
        if (i != 1 || this.errorView == null || TextUtils.isEmpty(this.errorText)) {
            z = false;
        }
        return z;
    }

    private boolean isCaptionStateHelperText(int i) {
        return (i != 2 || this.helperTextView == null || TextUtils.isEmpty(this.helperText)) ? false : true;
    }

    private void setCaptionViewVisibilities(int i, int i2) {
        TextView captionViewFromDisplayState;
        TextView captionViewFromDisplayState2;
        if (i == i2) {
            return;
        }
        if (i2 != 0 && (captionViewFromDisplayState2 = getCaptionViewFromDisplayState(i2)) != null) {
            captionViewFromDisplayState2.setVisibility(0);
            captionViewFromDisplayState2.setAlpha(1.0f);
        }
        if (i != 0 && (captionViewFromDisplayState = getCaptionViewFromDisplayState(i)) != null) {
            captionViewFromDisplayState.setVisibility(4);
            if (i == 1) {
                captionViewFromDisplayState.setText((CharSequence) null);
            }
        }
        this.captionDisplayed = i2;
    }

    private void setTextViewTypeface(TextView textView, Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    private void setViewGroupGoneIfEmpty(ViewGroup viewGroup, int i) {
        if (i == 0) {
            viewGroup.setVisibility(8);
        }
    }

    private boolean shouldAnimateCaptionView(TextView textView, CharSequence charSequence) {
        return ViewCompat.isLaidOut(this.textInputView) && this.textInputView.isEnabled() && !(this.captionToShow == this.captionDisplayed && textView != null && TextUtils.equals(textView.getText(), charSequence));
    }

    private void updateCaptionViewsVisibility(int i, int i2, boolean z) {
        if (z) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.captionAnimator = animatorSet;
            ArrayList arrayList = new ArrayList();
            createCaptionAnimators(arrayList, this.helperTextEnabled, this.helperTextView, 2, i, i2);
            createCaptionAnimators(arrayList, this.errorEnabled, this.errorView, 1, i, i2);
            AnimatorSetCompat.playTogether(animatorSet, arrayList);
            animatorSet.addListener(new AnimatorListenerAdapter(this, i2, getCaptionViewFromDisplayState(i), i, getCaptionViewFromDisplayState(i2)) { // from class: android.support.design.widget.IndicatorViewController.1
                final IndicatorViewController this$0;
                final int val$captionToHide;
                final int val$captionToShow;
                final TextView val$captionViewToHide;
                final TextView val$captionViewToShow;

                {
                    this.this$0 = this;
                    this.val$captionToShow = i2;
                    this.val$captionViewToHide = textView;
                    this.val$captionToHide = i;
                    this.val$captionViewToShow = textView;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    this.this$0.captionDisplayed = this.val$captionToShow;
                    this.this$0.captionAnimator = null;
                    TextView textView = this.val$captionViewToHide;
                    if (textView != null) {
                        textView.setVisibility(4);
                        if (this.val$captionToHide != 1 || this.this$0.errorView == null) {
                            return;
                        }
                        this.this$0.errorView.setText((CharSequence) null);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    TextView textView = this.val$captionViewToShow;
                    if (textView != null) {
                        textView.setVisibility(0);
                    }
                }
            });
            animatorSet.start();
        } else {
            setCaptionViewVisibilities(i, i2);
        }
        this.textInputView.updateEditTextBackground();
        this.textInputView.updateLabelState(z);
        this.textInputView.updateTextInputBoxState();
    }

    void addIndicator(TextView textView, int i) {
        if (this.indicatorArea == null && this.captionArea == null) {
            LinearLayout linearLayout = new LinearLayout(this.context);
            this.indicatorArea = linearLayout;
            linearLayout.setOrientation(0);
            this.textInputView.addView(this.indicatorArea, -1, -2);
            FrameLayout frameLayout = new FrameLayout(this.context);
            this.captionArea = frameLayout;
            this.indicatorArea.addView(frameLayout, -1, new FrameLayout.LayoutParams(-2, -2));
            this.indicatorArea.addView(new Space(this.context), new LinearLayout.LayoutParams(0, 0, 1.0f));
            if (this.textInputView.getEditText() != null) {
                adjustIndicatorPadding();
            }
        }
        if (isCaptionView(i)) {
            this.captionArea.setVisibility(0);
            this.captionArea.addView(textView);
            this.captionViewsAdded++;
        } else {
            this.indicatorArea.addView(textView, i);
        }
        this.indicatorArea.setVisibility(0);
        this.indicatorsAdded++;
    }

    void adjustIndicatorPadding() {
        if (canAdjustIndicatorPadding()) {
            ViewCompat.setPaddingRelative(this.indicatorArea, ViewCompat.getPaddingStart(this.textInputView.getEditText()), 0, ViewCompat.getPaddingEnd(this.textInputView.getEditText()), 0);
        }
    }

    void cancelCaptionAnimator() {
        Animator animator = this.captionAnimator;
        if (animator != null) {
            animator.cancel();
        }
    }

    boolean errorIsDisplayed() {
        return isCaptionStateError(this.captionDisplayed);
    }

    boolean errorShouldBeShown() {
        return isCaptionStateError(this.captionToShow);
    }

    CharSequence getErrorText() {
        return this.errorText;
    }

    int getErrorViewCurrentTextColor() {
        TextView textView = this.errorView;
        return textView != null ? textView.getCurrentTextColor() : -1;
    }

    ColorStateList getErrorViewTextColors() {
        TextView textView = this.errorView;
        return textView != null ? textView.getTextColors() : null;
    }

    CharSequence getHelperText() {
        return this.helperText;
    }

    ColorStateList getHelperTextViewColors() {
        TextView textView = this.helperTextView;
        return textView != null ? textView.getTextColors() : null;
    }

    int getHelperTextViewCurrentTextColor() {
        TextView textView = this.helperTextView;
        return textView != null ? textView.getCurrentTextColor() : -1;
    }

    boolean helperTextIsDisplayed() {
        return isCaptionStateHelperText(this.captionDisplayed);
    }

    boolean helperTextShouldBeShown() {
        return isCaptionStateHelperText(this.captionToShow);
    }

    void hideError() {
        this.errorText = null;
        cancelCaptionAnimator();
        if (this.captionDisplayed == 1) {
            if (!this.helperTextEnabled || TextUtils.isEmpty(this.helperText)) {
                this.captionToShow = 0;
            } else {
                this.captionToShow = 2;
            }
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.errorView, null));
    }

    void hideHelperText() {
        cancelCaptionAnimator();
        if (this.captionDisplayed == 2) {
            this.captionToShow = 0;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, null));
    }

    boolean isCaptionView(int i) {
        boolean z = true;
        if (i != 0) {
            z = i == 1;
        }
        return z;
    }

    boolean isErrorEnabled() {
        return this.errorEnabled;
    }

    boolean isHelperTextEnabled() {
        return this.helperTextEnabled;
    }

    void removeIndicator(TextView textView, int i) {
        FrameLayout frameLayout;
        if (this.indicatorArea == null) {
            return;
        }
        if (!isCaptionView(i) || (frameLayout = this.captionArea) == null) {
            this.indicatorArea.removeView(textView);
        } else {
            int i2 = this.captionViewsAdded - 1;
            this.captionViewsAdded = i2;
            setViewGroupGoneIfEmpty(frameLayout, i2);
            this.captionArea.removeView(textView);
        }
        int i3 = this.indicatorsAdded - 1;
        this.indicatorsAdded = i3;
        setViewGroupGoneIfEmpty(this.indicatorArea, i3);
    }

    void setErrorEnabled(boolean z) {
        if (this.errorEnabled == z) {
            return;
        }
        cancelCaptionAnimator();
        if (z) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
            this.errorView = appCompatTextView;
            appCompatTextView.setId(C0029R.id.textinput_error);
            Typeface typeface = this.typeface;
            if (typeface != null) {
                this.errorView.setTypeface(typeface);
            }
            setErrorTextAppearance(this.errorTextAppearance);
            this.errorView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion(this.errorView, 1);
            addIndicator(this.errorView, 0);
        } else {
            hideError();
            removeIndicator(this.errorView, 0);
            this.errorView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.errorEnabled = z;
    }

    void setErrorTextAppearance(int i) {
        this.errorTextAppearance = i;
        TextView textView = this.errorView;
        if (textView != null) {
            this.textInputView.setTextAppearanceCompatWithErrorFallback(textView, i);
        }
    }

    void setErrorViewTextColor(ColorStateList colorStateList) {
        TextView textView = this.errorView;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    void setHelperTextAppearance(int i) {
        this.helperTextTextAppearance = i;
        TextView textView = this.helperTextView;
        if (textView != null) {
            TextViewCompat.setTextAppearance(textView, i);
        }
    }

    void setHelperTextEnabled(boolean z) {
        if (this.helperTextEnabled == z) {
            return;
        }
        cancelCaptionAnimator();
        if (z) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
            this.helperTextView = appCompatTextView;
            appCompatTextView.setId(C0029R.id.textinput_helper_text);
            Typeface typeface = this.typeface;
            if (typeface != null) {
                this.helperTextView.setTypeface(typeface);
            }
            this.helperTextView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion(this.helperTextView, 1);
            setHelperTextAppearance(this.helperTextTextAppearance);
            addIndicator(this.helperTextView, 1);
        } else {
            hideHelperText();
            removeIndicator(this.helperTextView, 1);
            this.helperTextView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.helperTextEnabled = z;
    }

    void setHelperTextViewTextColor(ColorStateList colorStateList) {
        TextView textView = this.helperTextView;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    void setTypefaces(Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            setTextViewTypeface(this.errorView, typeface);
            setTextViewTypeface(this.helperTextView, typeface);
        }
    }

    void showError(CharSequence charSequence) {
        cancelCaptionAnimator();
        this.errorText = charSequence;
        this.errorView.setText(charSequence);
        if (this.captionDisplayed != 1) {
            this.captionToShow = 1;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.errorView, charSequence));
    }

    void showHelper(CharSequence charSequence) {
        cancelCaptionAnimator();
        this.helperText = charSequence;
        this.helperTextView.setText(charSequence);
        if (this.captionDisplayed != 2) {
            this.captionToShow = 2;
        }
        updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, shouldAnimateCaptionView(this.helperTextView, charSequence));
    }
}
