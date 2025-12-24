package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/WidgetContainer.class */
public class WidgetContainer extends ConstraintWidget {
    protected ArrayList<ConstraintWidget> mChildren;

    public WidgetContainer() {
        this.mChildren = new ArrayList<>();
    }

    public WidgetContainer(int i, int i2) {
        super(i, i2);
        this.mChildren = new ArrayList<>();
    }

    public WidgetContainer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
        this.mChildren = new ArrayList<>();
    }

    public static Rectangle getBounds(ArrayList<ConstraintWidget> arrayList) {
        Rectangle rectangle = new Rectangle();
        if (arrayList.size() == 0) {
            return rectangle;
        }
        int size = arrayList.size();
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i3 >= size) {
                rectangle.setBounds(i, i2, i4 - i, i6 - i2);
                return rectangle;
            }
            ConstraintWidget constraintWidget = arrayList.get(i3);
            int x = i;
            if (constraintWidget.getX() < i) {
                x = constraintWidget.getX();
            }
            int y = i2;
            if (constraintWidget.getY() < i2) {
                y = constraintWidget.getY();
            }
            int right = i4;
            if (constraintWidget.getRight() > i4) {
                right = constraintWidget.getRight();
            }
            int bottom = i6;
            if (constraintWidget.getBottom() > i6) {
                bottom = constraintWidget.getBottom();
            }
            i3++;
            i = x;
            i2 = y;
            i4 = right;
            i5 = bottom;
        }
    }

    public void add(ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer) constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }

    public void add(ConstraintWidget... constraintWidgetArr) {
        for (ConstraintWidget constraintWidget : constraintWidgetArr) {
            add(constraintWidget);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20, types: [android.support.constraint.solver.widgets.ConstraintWidget] */
    /* JADX WARN: Type inference failed for: r0v48, types: [android.support.constraint.solver.widgets.ConstraintWidget] */
    public ConstraintWidget findWidget(float f, float f2) {
        WidgetContainer widgetContainer;
        WidgetContainer widgetContainer2;
        int drawX = getDrawX();
        int drawY = getDrawY();
        WidgetContainer widgetContainer3 = (f < ((float) drawX) || f > ((float) (getWidth() + drawX)) || f2 < ((float) drawY) || f2 > ((float) (getHeight() + drawY))) ? null : this;
        int i = 0;
        int size = this.mChildren.size();
        while (i < size) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ?? FindWidget = ((WidgetContainer) constraintWidget).findWidget(f, f2);
                widgetContainer = widgetContainer3;
                if (FindWidget != 0) {
                    widgetContainer2 = FindWidget;
                    widgetContainer = widgetContainer2;
                }
            } else {
                int drawX2 = constraintWidget.getDrawX();
                int drawY2 = constraintWidget.getDrawY();
                int width = constraintWidget.getWidth();
                int height = constraintWidget.getHeight();
                widgetContainer = widgetContainer3;
                if (f >= drawX2) {
                    widgetContainer = widgetContainer3;
                    if (f <= width + drawX2) {
                        widgetContainer = widgetContainer3;
                        if (f2 >= drawY2) {
                            widgetContainer = widgetContainer3;
                            if (f2 <= height + drawY2) {
                                widgetContainer2 = constraintWidget;
                                widgetContainer = widgetContainer2;
                            }
                        }
                    }
                }
            }
            i++;
            widgetContainer3 = widgetContainer;
        }
        return widgetContainer3;
    }

    public ArrayList<ConstraintWidget> findWidgets(int i, int i2, int i3, int i4) {
        ArrayList<ConstraintWidget> arrayList = new ArrayList<>();
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, i2, i3, i4);
        int size = this.mChildren.size();
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i5);
            Rectangle rectangle2 = new Rectangle();
            rectangle2.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
            if (rectangle.intersects(rectangle2)) {
                arrayList.add(constraintWidget);
            }
        }
        return arrayList;
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = getParent();
        ConstraintWidgetContainer constraintWidgetContainer = this instanceof ConstraintWidgetContainer ? (ConstraintWidgetContainer) this : null;
        while (parent != null) {
            ConstraintWidget parent2 = parent.getParent();
            if (parent instanceof ConstraintWidgetContainer) {
                constraintWidgetContainer = (ConstraintWidgetContainer) parent;
            }
            parent = parent2;
        }
        return constraintWidgetContainer;
    }

    public void layout() {
        updateDrawPosition();
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer) constraintWidget).layout();
            }
        }
    }

    public void remove(ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.setParent(null);
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void setOffset(int i, int i2) {
        super.setOffset(i, i2);
        int size = this.mChildren.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mChildren.get(i3).setOffset(getRootX(), getRootY());
        }
    }

    @Override // android.support.constraint.solver.widgets.ConstraintWidget
    public void updateDrawPosition() {
        super.updateDrawPosition();
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.setOffset(getDrawX(), getDrawY());
            if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                constraintWidget.updateDrawPosition();
            }
        }
    }
}
