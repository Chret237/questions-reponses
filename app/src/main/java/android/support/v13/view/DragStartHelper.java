package android.support.v13.view;

import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/v13/view/DragStartHelper.class */
public class DragStartHelper {
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View.OnLongClickListener mLongClickListener = new View.OnLongClickListener(this) { // from class: android.support.v13.view.DragStartHelper.1
        final DragStartHelper this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.this$0.onLongClick(view);
        }
    };
    private final View.OnTouchListener mTouchListener = new View.OnTouchListener(this) { // from class: android.support.v13.view.DragStartHelper.2
        final DragStartHelper this$0;

        {
            this.this$0 = this;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return this.this$0.onTouch(view, motionEvent);
        }
    };
    private final View mView;

    /* loaded from: classes-dex2jar.jar:android/support/v13/view/DragStartHelper$OnDragStartListener.class */
    public interface OnDragStartListener {
        boolean onDragStart(View view, DragStartHelper dragStartHelper);
    }

    public DragStartHelper(View view, OnDragStartListener onDragStartListener) {
        this.mView = view;
        this.mListener = onDragStartListener;
    }

    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }

    public void detach() {
        this.mView.setOnLongClickListener(null);
        this.mView.setOnTouchListener(null);
    }

    public void getTouchPosition(Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }

    public boolean onLongClick(View view) {
        return this.mListener.onDragStart(view, this);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mLastTouchX = x;
            this.mLastTouchY = y;
            return false;
        }
        if (action != 1) {
            if (action == 2) {
                if (!MotionEventCompat.isFromSource(motionEvent, 8194) || (motionEvent.getButtonState() & 1) == 0 || this.mDragging) {
                    return false;
                }
                if (this.mLastTouchX == x && this.mLastTouchY == y) {
                    return false;
                }
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                boolean zOnDragStart = this.mListener.onDragStart(view, this);
                this.mDragging = zOnDragStart;
                return zOnDragStart;
            }
            if (action != 3) {
                return false;
            }
        }
        this.mDragging = false;
        return false;
    }
}
