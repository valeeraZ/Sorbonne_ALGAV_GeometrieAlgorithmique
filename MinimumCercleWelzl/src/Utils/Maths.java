package Utils;

import java.awt.*;
import java.util.ArrayList;

public class Maths {

    public static Point midPoint(Point p, Point q) {
        double x = (p.getX() + q.getX()) / 2;
        double y = (p.getY() + q.getY()) / 2;
        Point res = new Point();
        res.setLocation(x, y);
        return res;
    }

    public static double crossProduct(Point p, Point q, Point s, Point t){
        return ((q.x-p.x)*(t.y-s.y)-(q.y-p.y)*(t.x-s.x));
    }

    public static Circle triangleCircumscribedCircle(Point p, Point q, Point r){
        double a1 = 2 * (q.x - p.x);
        double b1 = 2 * (q.y - p.y);
        double c1 = java.lang.Math.pow(q.x,2) + java.lang.Math.pow(q.y, 2) - java.lang.Math.pow(p.x, 2) - java.lang.Math.pow(p.y, 2);

        double a2 = 2 * (r.x - q.x);
        double b2 = 2 * (r.y - q.y);
        double c2 = java.lang.Math.pow(r.x, 2) + java.lang.Math.pow(r.y, 2) - java.lang.Math.pow(q.x, 2) - java.lang.Math.pow(q.y, 2);

        //CRAMMER rule
        //a1 * x + b1 * y = c1;
        //a2 * x + b2 * y = c2;
        double x = ((c1*b2)-(c2*b1)) / ((a1*b2)-(a2*b1));
        double y = ((a1*c2)-(a2*c1)) / ((a1*b2)-(a2*b1));

        Point center = new Point((int)x, (int) y);
        double radius = p.distance(center);

        return new Circle(center, (int)radius);
    }

    public static boolean pointsInCircle(Circle circle, ArrayList<Point> points){
        for (Point s : points) {
            if ((int)s.distance(circle.getCenter()) > circle.getRadius()) {
                return false;
            }
        }
        return true;
    }
}
