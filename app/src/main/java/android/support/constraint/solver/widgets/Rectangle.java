package android.support.constraint.solver.widgets;

/* loaded from: classes-dex2jar.jar:android/support/constraint/solver/widgets/Rectangle.class */
public class Rectangle {
    public int height;
    public int width;

    /* renamed from: x */
    public int f10x;

    /* renamed from: y */
    public int f11y;

    public boolean contains(int i, int i2) {
        int i3;
        int i4 = this.f10x;
        return i >= i4 && i < i4 + this.width && i2 >= (i3 = this.f11y) && i2 < i3 + this.height;
    }

    public int getCenterX() {
        return (this.f10x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.f11y + this.height) / 2;
    }

    void grow(int i, int i2) {
        this.f10x -= i;
        this.f11y -= i2;
        this.width += i * 2;
        this.height += i2 * 2;
    }

    boolean intersects(Rectangle rectangle) {
        int i;
        int i2;
        int i3 = this.f10x;
        int i4 = rectangle.f10x;
        return i3 >= i4 && i3 < i4 + rectangle.width && (i = this.f11y) >= (i2 = rectangle.f11y) && i < i2 + rectangle.height;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        this.f10x = i;
        this.f11y = i2;
        this.width = i3;
        this.height = i4;
    }
}
