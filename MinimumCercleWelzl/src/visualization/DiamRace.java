package visualization;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class DiamRace {
    private static int width = 0;
    private static int height = 0;
    private static String title = "Diameter Racer";
    private static FramedDiamRace framedGUI;

    public DiamRace() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                framedGUI = new FramedDiamRace(width, height, title);
            }
        });
        synchronized(Variables.lock) {
            try {
                Variables.lock.wait();
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }

        readFile();
    }

    public static void readFile() {
        ArrayList<Point> points = new ArrayList<>();

        try {
            String filename = "input.points";
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

            try {
                String line;
                while((line = input.readLine()) != null) {
                    String[] coordinates = line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
                }

                framedGUI.drawPoints(points);
                synchronized(Variables.lock2) {
                    Variables.lock2.notify();
                }
            } catch (IOException var16) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException var14) {
                    System.err.println("I/O exception: unable to close " + filename);
                }

            }
        } catch (FileNotFoundException var18) {
            System.err.println("Input file not found.");
        }

    }
}
