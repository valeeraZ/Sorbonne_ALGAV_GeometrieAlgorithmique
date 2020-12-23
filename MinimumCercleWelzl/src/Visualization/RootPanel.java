package Visualization;

import Utils.Circle;
import Utils.Line;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RootPanel extends JPanel {
    private static final long serialVersionUID = -7573425545656207548L;
    protected CardLayout layout = (CardLayout)this.getLayout();
    protected MainPanel mainPanel = new MainPanel();

    public RootPanel() {
        super(new CardLayout());
        this.add(this.mainPanel, "1664");
        this.layout.show(this, "1664");
    }

    public void drawPoints(ArrayList<Point> points) {
        this.mainPanel.drawPoints(points);
    }

    public void shiftLeftAll() {
        this.mainPanel.shiftLeftAll();
    }

    public void shiftUpAll() {
        this.mainPanel.shiftUpAll();
    }

    public void shiftDownAll() {
        this.mainPanel.shiftDownAll();
    }

    public void shiftRightAll() {
        this.mainPanel.shiftRightAll();
    }

    public ArrayList<Point> getPoints() {
        return this.mainPanel.getPoints();
    }

    public void addLine(Line l) {
        this.mainPanel.addLine(l);
    }

    public void addLineAndT(Line l, long t) {
        this.mainPanel.addLineAndT(l, t);
    }

    public void refreshLine() {
        this.mainPanel.refreshLine();
    }

    public void addCircle(Circle c) {
        this.mainPanel.addCircle(c);
    }

    public void addCircleAndT(Circle c, long t) {
        this.mainPanel.addCircleAndT(c, t);
    }
}
