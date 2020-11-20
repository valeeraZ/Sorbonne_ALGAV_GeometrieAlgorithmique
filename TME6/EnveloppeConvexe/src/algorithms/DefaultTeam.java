package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

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

  // enveloppeConvexe: ArrayList<Point> --> ArrayList<Point>
  //   renvoie l'enveloppe convexe de la liste.
  public ArrayList<Point> enveloppeConvexe(ArrayList<Point> points){
    //return enveloppeConvexeNaif(triPixel(points));
    //return jarvis(triPixel(points));
    return graham(points);
  }

  //enveloppe convexe: algorithme naif
  private ArrayList<Point> enveloppeConvexeNaif(ArrayList<Point> points){
    if (points.size() < 3)
      return points;

    ArrayList<Point> enveloppe = new ArrayList<Point>();

    for (Point p : points){
      for (Point q : points){
        if (p == q)
          continue;
        //vérifier si pq est un bord de convexe
        Point ref = p;
        for(Point r: points){
          //pq et pr non colinéaire -> r n'est pas sur l'enveloppe
          //si colinéaire -> p,q,r sont alignés qui sont tous sur l'enveloppe
          //faut prendre 2 points les plus éloignés parmi p,q et r
          if(crossProduct(p,q,p,r) != 0){
            ref = r;
            break;
          }
        }
        //les plus éloignés
        if (ref == p){
          enveloppe.add(p);
          enveloppe.add(q);
          continue;
        }
        //les autres n-2 points doivent être au même coté
        double sign = crossProduct(p,q,p,ref);
        boolean memeCote = true;
        for(Point s : points){
          if(sign * crossProduct(p,q,p,s) < 0){
            memeCote = false;
            break;
          }
        }
        if(memeCote){
          enveloppe.add(p);
          enveloppe.add(q);
        }
      }
    }
    return enveloppe;
  }

  private ArrayList<Point> triPixel(ArrayList<Point> points){
    if (points.size() < 3)
      return points;
    int maxAbcisse = points.get(0).x;
    for(Point p : points){
      if(p.x > maxAbcisse)
        maxAbcisse = p.x;
    }
    Point[] ymin = new Point[maxAbcisse + 1];
    Point[] ymax = new Point[maxAbcisse + 1];

    //les points plus haut et plus bas forment l'enveloppe
    for (Point p : points){
      if ((ymin[p.x] == null) || ymin[p.x].y > p.y)
        ymin[p.x] = p;
      if ((ymax[p.x] == null) || ymax[p.x].y < p.y)
        ymax[p.x] = p;
    }
    ArrayList<Point> result = new ArrayList<>();
    for(int i = 0; i < maxAbcisse + 1; i ++){
      if (ymax[i] != null)
        result.add(ymax[i]);
    }
    for(int i = maxAbcisse; i >= 0; i --){
      if (ymin[i] != null && !result.get(result.size() - 1).equals(ymin[i]))
        result.add(ymin[i]);
    }
    if (result.get(result.size()-1).equals(result.get(0)))
      result.remove(result.size()-1);
    return result;
  }

  private ArrayList<Point> jarvis(ArrayList<Point> points){
    if (points.size()<4) return points;
    Point p = points.get(0);
    for (Point point: points) {
      if ( (point.x < p.x) || (p.x == point.x && p.x < point.x ))
        p = point;
    }

    ArrayList<Point> result = new ArrayList<>();
    result.add(p);

    //chercher PQ
    for (Point q : points){
      if (p == q) continue;
      Point ref = q;
      for (Point point : points){
        if (crossProduct(p,q,p,ref) != 0){
          ref = point;
          break;
        }
      }
      if(ref == q) {
        result.add(q);
        continue;
      }
      //les autres n-2 points doivent être au même coté
      double sign = crossProduct(p,q,p,ref);
      boolean memeCote = true;
      for(Point s : points){
        if(sign * crossProduct(p,q,p,s) < 0){
          memeCote = false;
          break;
        }
      }
      if(memeCote){
        result.add(q);
        break;
      }
    }

    //chercher QR
    while(! (result.get(0) == result.get(result.size() - 1))){
      Point p2 = result.get(result.size() - 2);
      Point q2 = result.get(result.size() - 1);
      Point r = points.get(0);
      for(Point point : points){
        if (point != p2 && point != q2){
          r = point;
          break;
        }
      }
      //minimum angle
      for(Point point : points){
        if (point == p2 || point == q2) continue;
        if(angle(p2, q2, q2, point) < angle(p2, q2, q2, r)) r = point;
      }
      result.add(r);
    }
    result.remove(result.get(0));
    return result;
  }

  private ArrayList<Point> graham(ArrayList<Point> points){
    if (points.size()<4) return points;

    ArrayList<Point> result = triPixel(points);
    for (int i=1;i<result.size()+2;i++) {
      Point p = result.get((i-1)%result.size());
      Point q = result.get(i%result.size());
      Point r = result.get((i+1)%(result.size()));
      if (crossProduct(p,q,p,r) > 0) {
        result.remove(q);
        if (i==2) i=1;
        if (i>2) i-=2;
      }
    }
    return result;
  }

  private double angle(Point p, Point q, Point s, Point t) {
    if (p.equals(q)||s.equals(t)) return Double.MAX_VALUE;
    double cosTheta = dotProduct(p,q,s,t)/(double)(p.distance(q)*s.distance(t));
    return Math.acos(cosTheta);
  }
  private double dotProduct(Point p, Point q, Point s, Point t) {
    return ((q.x-p.x)*(t.x-s.x)+(q.y-p.y)*(t.y-s.y));
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
