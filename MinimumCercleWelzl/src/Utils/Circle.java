package Utils;

import java.awt.*;

public class Circle {
    private Point center;
    private int radius;
    private Color c;

    protected Circle(Point center, int r, Color c) {
        this.center = center;
        this.radius = r;
        this.c = c;
    }

    public Circle(Point center, int r) {
        this.center = center;
        this.radius = r;
        this.c = Color.RED;
    }

    public Point getCenter() {
        return this.center;
    }

    public int getRadius() {
        return this.radius;
    }

    public Color getColor() {
        return this.c;
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCenter(Point center) {
        this.center = center;
    }
}
