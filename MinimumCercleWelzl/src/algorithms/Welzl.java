package algorithms;

import tools.Circle;
import tools.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static tools.Maths.*;

public class Welzl {

    Random r = new Random();

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
    //   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
    public Circle calculCercleMin(ArrayList<Point> points) {
        return welzl(points, new ArrayList<>());
    }

    private Circle welzl(ArrayList<Point> P, ArrayList<Point> R){
        ArrayList<Point> cP = new ArrayList<>(P);
        ArrayList<Point> cR = new ArrayList<>(R);

        Circle D = new Circle(new Point(0,0), 0);

        if(cP.size() < 1 || cR.size() >= 3){
            if(isConcyclic(cR))
                D = pointsToCircle(cR);
        }else {
            int rand = r.nextInt(cP.size());
            Point p = cP.remove(rand);
            D = welzl(cP, cR);
            if(! pointInCircle(D, p)){
                cR.add(p);
                D = welzl(cP, cR);
            }
        }
        return D;
    }
}
