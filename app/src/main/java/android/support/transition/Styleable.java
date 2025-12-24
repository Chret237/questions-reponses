package android.support.transition;

/* loaded from: classes-dex2jar.jar:android/support/transition/Styleable.class */
class Styleable {
    static final int[] TRANSITION_TARGET = {android.R.attr.targetClass, android.R.attr.targetId, android.R.attr.excludeId, android.R.attr.excludeClass, android.R.attr.targetName, android.R.attr.excludeName};
    static final int[] TRANSITION_MANAGER = {android.R.attr.fromScene, android.R.attr.toScene, android.R.attr.transition};
    static final int[] TRANSITION = {android.R.attr.interpolator, android.R.attr.duration, android.R.attr.startDelay, android.R.attr.matchOrder};
    static final int[] CHANGE_BOUNDS = {android.R.attr.resizeClip};
    static final int[] VISIBILITY_TRANSITION = {android.R.attr.transitionVisibilityMode};
    static final int[] FADE = {android.R.attr.fadingMode};
    static final int[] CHANGE_TRANSFORM = {android.R.attr.reparent, android.R.attr.reparentWithOverlay};
    static final int[] SLIDE = {android.R.attr.slideEdge};
    static final int[] TRANSITION_SET = {android.R.attr.transitionOrdering};
    static final int[] ARC_MOTION = {android.R.attr.minimumHorizontalAngle, android.R.attr.minimumVerticalAngle, android.R.attr.maximumAngle};
    static final int[] PATTERN_PATH_MOTION = {android.R.attr.patternPathData};

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$ArcMotion.class */
    interface ArcMotion {
        public static final int MAXIMUM_ANGLE = 2;
        public static final int MINIMUM_HORIZONTAL_ANGLE = 0;
        public static final int MINIMUM_VERTICAL_ANGLE = 1;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$ChangeBounds.class */
    interface ChangeBounds {
        public static final int RESIZE_CLIP = 0;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$ChangeTransform.class */
    interface ChangeTransform {
        public static final int REPARENT = 0;
        public static final int REPARENT_WITH_OVERLAY = 1;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$Fade.class */
    interface Fade {
        public static final int FADING_MODE = 0;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$PatternPathMotion.class */
    interface PatternPathMotion {
        public static final int PATTERN_PATH_DATA = 0;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$Slide.class */
    interface Slide {
        public static final int SLIDE_EDGE = 0;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$Transition.class */
    interface Transition {
        public static final int DURATION = 1;
        public static final int INTERPOLATOR = 0;
        public static final int MATCH_ORDER = 3;
        public static final int START_DELAY = 2;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$TransitionManager.class */
    interface TransitionManager {
        public static final int FROM_SCENE = 0;
        public static final int TO_SCENE = 1;
        public static final int TRANSITION = 2;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$TransitionSet.class */
    interface TransitionSet {
        public static final int TRANSITION_ORDERING = 0;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$TransitionTarget.class */
    interface TransitionTarget {
        public static final int EXCLUDE_CLASS = 3;
        public static final int EXCLUDE_ID = 2;
        public static final int EXCLUDE_NAME = 5;
        public static final int TARGET_CLASS = 0;
        public static final int TARGET_ID = 1;
        public static final int TARGET_NAME = 4;
    }

    /* loaded from: classes-dex2jar.jar:android/support/transition/Styleable$VisibilityTransition.class */
    interface VisibilityTransition {
        public static final int TRANSITION_VISIBILITY_MODE = 0;
    }

    private Styleable() {
    }
}
