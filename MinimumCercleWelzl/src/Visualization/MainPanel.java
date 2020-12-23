package Visualization;


import Utils.Circle;
import Utils.Line;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
    private static final long serialVersionUID = -662473925955493029L;
    protected DisplayPanel displayPanel = new DisplayPanel();

    public MainPanel() {
        super(new BorderLayout());
        this.add(this.displayPanel, "Center");
    }

    public void drawPoints(ArrayList<Point> points) {
        this.displayPanel.drawPoints(points);
    }

    public void shiftLeftAll() {
        this.displayPanel.shiftLeftAll();
    }

    public void shiftUpAll() {
        this.displayPanel.shiftUpAll();
    }

    public void shiftDownAll() {
        this.displayPanel.shiftDownAll();
    }

    public void shiftRightAll() {
        this.displayPanel.shiftRightAll();
    }

    public ArrayList<Point> getPoints() {
        return this.displayPanel.getPoints();
    }

    public void addLine(Line l) {
        this.displayPanel.addLine(l);
    }

    public void addLineAndT(Line l, long t) {
        this.displayPanel.addLineAndT(l, t);
    }

    public void refreshLine() {
        this.displayPanel.refreshLine();
    }

    public void addCircle(Circle c) {
        this.displayPanel.addCircle(c);
    }

    public void addCircleAndT(Circle c, long t) {
        this.displayPanel.addCircleAndT(c, t);
    }
}
