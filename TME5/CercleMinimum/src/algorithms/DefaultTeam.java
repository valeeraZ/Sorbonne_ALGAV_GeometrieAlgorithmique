package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

  /**
   * renvoie une paire de points de la liste, de distance maximum.
   * @param points
   * @return Ligne contenant ces deux points
   */
  public Line calculDiametre(ArrayList<Point> points) {
    if (points.size()<3) {
      return null;
    }

    Point p=points.get(0);
    Point q=points.get(1);

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

    return new Line(p,q);
  }

  /**
   * renvoie un cercle couvrant tout point de la liste, de rayon minimum.
   * @param points
   * @return ce circle minimum
   */
  public Circle calculCercleMin(ArrayList<Point> points) {
    if (points.isEmpty()) {
      return null;
    }

    Point c = points.get(0);
    double radius = 100;

    //ces deux traitements tous marchent bien
    //ArrayList<Point> pointsAlgo = filtrageToussaint(points);
    ArrayList<Point> pointsAlgo = quickHull(points);

    Random rand = new Random();
    int random = 0;

    /*
    //1
    Random rand = new Random();
    int random = rand.nextInt(pointsAlgo.size());
    Point dummy = pointsAlgo.get(random);

    //2
    double distance_max = 0;
    Point p = new Point();

    for (Point point : pointsAlgo) {
      double d = dummy.distance(point);
      if (d > distance_max) {
        distance_max = d;
        p = point;
      }
    }

    //3
    distance_max = 0;
    Point q = new Point();
    for (Point point : pointsAlgo) {
      double d = p.distance(point);
      if (d > distance_max) {
        distance_max = d;
        q = point;
      }
    }
     */

    //remplacer étapes 1 2 3 en utilisant calculDiametre
    Line pq = calculDiametre(pointsAlgo);
    Point p = pq.getP();
    Point q = pq.getQ();

    //4 & 5
    c = centre(p,q);
    radius = p.distance(q) / 2 ;

    //6
    pointsAlgo.remove(p);
    pointsAlgo.remove(q);

    //7 - 12
    Point s = new Point();
    Point t = new Point();
    while (!pointsAlgo.isEmpty()) {

      random = rand.nextInt(pointsAlgo.size());
      s = pointsAlgo.get(random);
      double distance_cs = c.distance(s);

      if (distance_cs <= radius) {
        pointsAlgo.remove(s);
      } else {
        radius = (distance_cs + radius) / 2;
        double alpha = radius / distance_cs;
        double beta = 1 - alpha;

        // Calcul du nouveau centre
        double x = alpha * c.getX() + beta * s.getX();
        double y = alpha * c.getY() + beta * s.getY();
        c.setLocation(x, y);

        pointsAlgo.remove(s);
      }
    }
    return new Circle(c, (int) radius);
  }

  /**
   * filtrer les points dans un quadrilatère
   * avant de calculer le cercle minimum
   * @param points
   * @return une liste de points filtrée
   */
  private ArrayList<Point> filtrageToussaint(ArrayList<Point> points){
    if (points.size() < 4)
      return points;

    Point a = points.get(0);
    Point b = points.get(0);
    Point c = points.get(0);
    Point d = points.get(0);
    for (Point point : points) {
      if (point.x < a.x)
        a = point;
      if (point.y > b.y)
        b = point;
      if (point.x > c.x)
        c = point;
      if (point.y < d.y)
        d = point;
    }

    ArrayList<Point> result = new ArrayList<>();
    for (Point x : points) {
      //un quadrilatère -> deux triangles
      if (!baryCentrique(a, b, c, x) && !baryCentrique(a, c, d, x)) {
        result.add(x);
      }
    }
    return result;
  }

  /**
   * QuickHull, O(n log n2)
   * @param points les points formant un Convex
   * @return la liste de ces points
   */
  private ArrayList<Point> quickHull(ArrayList<Point> points){
    ArrayList<Point> pointsFiltre = filtrageToussaint(points);
    //le resultat contient les points qui font un Convex (polygone couvrant tous les points)
    ArrayList<Point> result = new ArrayList<>();

    Point a = points.get(0);
    Point b = points.get(0);
    Point c = points.get(0);
    Point d = points.get(0);
    for (Point point : points) {
      if (point.x < a.x)
        a = point;
      if (point.y > b.y)
        b = point;
      if (point.x > c.x)
        c = point;
      if (point.y < d.y)
        d = point;
    }
    result.add(a);
    result.add(b);
    result.add(c);
    result.add(d);

    for(int i = 0; i < result.size(); i++){
      Point A = result.get(i);
      Point B = result.get((i + 1) % result.size());
      Point C = result.get((i + 2) % result.size());

      double sign = crossProduct(A,B,A,C);
      double maxProduct = 0;
      // le point le plus éloigné de AB
      Point X = A;

      //cherche X
      for(Point point : points){
        double productValue = crossProduct(A,B,A,point);
        //X n'appartient pas a triangle ABC et X est plus loin
        if(sign * productValue < 0 && Math.abs(productValue) > maxProduct){
          maxProduct = Math.abs(productValue);
          X = point;
        }
      }
      //enlever les points dans ABX
      //(autrement dit ajouter les points qui n'y sont pas au résultat)
      if(maxProduct != 0){
        for(int j = 0; j < pointsFiltre.size(); j++){
          if(baryCentrique(A,B,X,pointsFiltre.get(j))){
            pointsFiltre.remove(pointsFiltre.get(j));
            j--;
          }
        }
        //remplacer le terme quadrilatère par pentagone
        result.add(i+1, X);
        i--;
      }
    }
    return result;
  }

  /**
   * centre d'un segment pq
   * @param p un côté du segment
   * @param q l'autre côté
   * @return point centre
   */
  private Point centre(Point p, Point q) {
    double x = (p.getX() + q.getX()) / 2;
    double y = (p.getY() + q.getY()) / 2;
    Point res = new Point();
    res.setLocation(x, y);
    return res;
  }

  /**
   * tester le point x est dans le triangle(a,b,c) ou non
   * @param A un point de triangle
   * @param B un point de triangle
   * @param C un point de triangle
   * @param X le point à tester
   * @return true si x est dans le triangle
   */
  private boolean baryCentrique(Point A, Point B, Point C, Point X){
    double l1 = ((B.y - C.y) * (X.x - C.x) + (C.x - B.x) * (X.y - C.y)) / (double) ((B.y - C.y) * (A.x - C.x) + (C.x - B.x) * (A.y - C.y));
    double l2 = ((C.y - A.y) * (X.x - C.x) + (A.x - C.x) * (X.y - C.y)) / (double) ((B.y - C.y) * (A.x - C.x) + (C.x - B.x) * (A.y - C.y));
    double l3 = 1 - l1 - l2;
    return ( 0 < l1 && l1 < 1 && 0 < l2 && l2 < 1 && 0 < l3 && l3 < 1);
  }

  /**
   * produit vectoriel de deux vecteurs
   * |valeur| : aire du quadrilatère formé par ces deux vecteurs
   * si |valeur|  est plus grande alors l'angle entre ces deux vecteurs est plus grand aussi (0 - 90 degrés)
   * si valeur < 0: angle obtus
   * si valeur > 0: angle aigu
   * @param p un point de vecteur a
   * @param q l'autre point de vecteur a
   * @param s un point de vecteur b
   * @param t un autre point de vecteur b
   * @return valeur de produit factoriel
   */
  private double crossProduct(Point p, Point q, Point s, Point t){
      return ((q.x-p.x)*(t.y-s.y)-(q.y-p.y)*(t.x-s.x));
  }

}
