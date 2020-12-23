package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import Utils.Circle;
import Utils.Line;

import static Utils.Maths.*;

public class Naive {


    public Line calculDiametre(ArrayList<Point> points) {
        if (points.size() < 3) {
            return null;
        }

        Point p = points.get(0);
        Point q = points.get(1);

        double distance_max = p.distance(q);

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double d = points.get(i).distance(points.get(j));
                if (distance_max < d) {
                    distance_max = d;
                    p = points.get(i);
                    q = points.get(j);
                }
            }

        }

        return new Line(p, q);
    }

    // calculCercleMin: ArrayList<Point> --> Circle
    //   renvoie un cercle couvrant tout point de la liste, de rayon minimum
    public Circle calculCercleMin(ArrayList<Point> points) {
        if (points.isEmpty()) {
            return null;
        }

        Point center = points.get(0);
        double radius = 1.0;
        Circle circle = new Circle(center, (int)radius);

        for (Point p : points) {
            for (Point q : points) {

                center = midPoint(p, q);
                radius = p.distance(q) / 2;
                circle = new Circle(center, (int)radius);

                if (pointsInCircle(circle, points)) {
                    return circle;
                }
            }
        }

        center = points.get(0);
        radius = -1;
        circle.setCenter(center);
        circle.setRadius((int)radius);

        for(Point p : points){
            for(Point q : points){
                for(Point r : points){
                    if(crossProduct(p,q,p,r) == 0)
                        continue;

                    Circle circleTemp = triangleCircumscribedCircle(p,q,r);

                    if (pointsInCircle(circleTemp, points)) {
                        if (circleTemp.getRadius() < circle.getRadius() || circle.getRadius() < 0)
                            circle = circleTemp;
                    }
                }
            }
        }
        return circle;
    }
}
