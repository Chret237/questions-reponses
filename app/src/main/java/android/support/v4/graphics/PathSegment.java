package android.support.v4.graphics;

import android.graphics.PointF;
import android.support.v4.util.Preconditions;

/* loaded from: classes-dex2jar.jar:android/support/v4/graphics/PathSegment.class */
public final class PathSegment {
    private final PointF mEnd;
    private final float mEndFraction;
    private final PointF mStart;
    private final float mStartFraction;

    public PathSegment(PointF pointF, float f, PointF pointF2, float f2) {
        this.mStart = (PointF) Preconditions.checkNotNull(pointF, "start == null");
        this.mStartFraction = f;
        this.mEnd = (PointF) Preconditions.checkNotNull(pointF2, "end == null");
        this.mEndFraction = f2;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PathSegment)) {
            return false;
        }
        PathSegment pathSegment = (PathSegment) obj;
        if (Float.compare(this.mStartFraction, pathSegment.mStartFraction) != 0 || Float.compare(this.mEndFraction, pathSegment.mEndFraction) != 0 || !this.mStart.equals(pathSegment.mStart) || !this.mEnd.equals(pathSegment.mEnd)) {
            z = false;
        }
        return z;
    }

    public PointF getEnd() {
        return this.mEnd;
    }

    public float getEndFraction() {
        return this.mEndFraction;
    }

    public PointF getStart() {
        return this.mStart;
    }

    public float getStartFraction() {
        return this.mStartFraction;
    }

    public int hashCode() {
        int iHashCode = this.mStart.hashCode();
        float f = this.mStartFraction;
        int iFloatToIntBits = 0;
        int iFloatToIntBits2 = f != 0.0f ? Float.floatToIntBits(f) : 0;
        int iHashCode2 = this.mEnd.hashCode();
        float f2 = this.mEndFraction;
        if (f2 != 0.0f) {
            iFloatToIntBits = Float.floatToIntBits(f2);
        }
        return (((((iHashCode * 31) + iFloatToIntBits2) * 31) + iHashCode2) * 31) + iFloatToIntBits;
    }

    public String toString() {
        return "PathSegment{start=" + this.mStart + ", startFraction=" + this.mStartFraction + ", end=" + this.mEnd + ", endFraction=" + this.mEndFraction + '}';
    }
}
