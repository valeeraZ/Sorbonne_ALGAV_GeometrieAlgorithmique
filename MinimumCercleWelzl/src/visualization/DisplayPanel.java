package visualization;

import tools.Circle;
import tools.Line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel{
    private static final long serialVersionUID = -1401707925288150149L;
    private static final int xBorder = 10;
    private static final int yBorder = 10;
    private static final int xStep = 10;
    private static final int yStep = 10;
    private int xModifier;
    private int yModifier;
    private Graphics2D g2d;
    private ArrayList<Point> points;
    private ArrayList<Line> lines;
    private ArrayList<Circle> circles;
    private long time;

    public DisplayPanel() {
        this.setPreferredSize(new Dimension(1500, 1000));
        this.xModifier = 10;
        this.yModifier = 10;
        this.points = new ArrayList();
        this.lines = new ArrayList();
        this.circles = new ArrayList();
        this.time = 0L;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g2d = (Graphics2D)g.create();
        this.g2d.setFont(new Font(this.g2d.getFont().getName(), 1, 18));
        this.g2d.drawString("Clavier:", 5, 20);
        this.g2d.drawString("- 'r' pour rafraîchir le nuage de points", 15, 40);
        this.g2d.drawString("- 'd' pour lancer calculDiametre", 15, 60);
        this.g2d.drawString("- 'c' pour lancer calculCercleMin", 15, 80);
        this.g2d.drawString("- 'e' pour lancer calculCercleMin en Algorithme Welzl", 15, 100);
        this.g2d.drawString("- 'h', 'j', 'k', 'l' pour déplacer les points", 15, 120);
        this.g2d.setColor(Color.BLUE);
        this.g2d.setStroke(new BasicStroke(6.0F, 1, 1));

        int x;
        int y;
        int r;
        for(r = 0; r < this.points.size(); ++r) {
            if ((x = (int)((Point)this.points.get(r)).getX() + this.xModifier) >= 10 && (y = (int)((Point)this.points.get(r)).getY() + this.yModifier) >= 10) {
                this.g2d.drawLine(x, y, x, y);
            }
        }

        this.g2d.setStroke(new BasicStroke(4.0F, 1, 1));

        for(r = 0; r < this.lines.size(); ++r) {
            x = (int)((Line)this.lines.get(r)).getP().getX() + this.xModifier;
            y = (int)((Line)this.lines.get(r)).getP().getY() + this.yModifier;
            int v = (int)((Line)this.lines.get(r)).getQ().getX() + this.xModifier;
            int w = (int)((Line)this.lines.get(r)).getQ().getY() + this.yModifier;
            this.g2d.setColor(((Line)this.lines.get(r)).getColor());
            this.g2d.drawLine(x, y, v, w);
        }

        for(int i = 0; i < this.circles.size(); ++i) {
            x = (int)((Circle)this.circles.get(i)).getCenter().getX() + this.xModifier;
            y = (int)((Circle)this.circles.get(i)).getCenter().getY() + this.yModifier;
            r = (int)((Circle)this.circles.get(i)).getRadius();
            this.g2d.setColor(((Circle)this.circles.get(i)).getColor());
            Double c = new Double((double)(x - r), (double)(y - r), (double)(2 * r), (double)(2 * r));
            this.g2d.draw(c);
        }

        if (this.time != 0L) {
            this.g2d.setColor(Color.BLACK);
            this.g2d.setFont(new Font(this.g2d.getFont().getName(), 1, 32));
            this.g2d.drawString("Temps de calcul: " + Long.toString(this.time) + " ms", 20, 150);
        }

    }

    public void drawPoints(ArrayList<Point> points) {
        this.points = points;
        this.repaint();
    }

    public void shiftLeftAll() {
        this.xModifier -= 10;
        this.repaint();
    }

    public void shiftUpAll() {
        this.yModifier -= 10;
        this.repaint();
    }

    public void shiftDownAll() {
        this.yModifier += 10;
        this.repaint();
    }

    public void shiftRightAll() {
        this.xModifier += 10;
        this.repaint();
    }

    public ArrayList<Point> getPoints() {
        return this.points;
    }

    public void addLine(Line l) {
        this.lines.add(l);
        this.repaint();
    }

    public void addLineAndT(Line l, long t) {
        this.lines.add(l);
        this.time = t;
        this.repaint();
    }

    public void refreshLine() {
        this.lines.clear();
        this.circles.clear();
        this.time = 0L;
        this.repaint();
    }

    public void addCircle(Circle c) {
        this.circles.add(c);
        this.repaint();
    }

    public void addCircleAndT(Circle c, long t) {
        this.circles.add(c);
        this.time = t;
        this.repaint();
    }
}
