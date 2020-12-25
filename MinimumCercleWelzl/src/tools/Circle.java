package tools;

import java.awt.*;

public class Circle {
    private Point center;
    private double radius;
    private Color c;

    protected Circle(Point center, double r, Color c) {
        this.center = center;
        this.radius = r;
        this.c = c;
    }

    public Circle(Point center, double r) {
        this.center = center;
        this.radius = r;
        this.c = Color.RED;
    }

    public Point getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public Color getColor() {
        return this.c;
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
