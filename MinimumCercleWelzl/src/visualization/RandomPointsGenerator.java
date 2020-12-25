package visualization;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class RandomPointsGenerator {
    private static String filename = "input.points";
    private static int numberOfPoints = 256;
    private static int maxWidth = 1400;
    private static int maxHeight = 900;
    private static int radius = 200;

    public RandomPointsGenerator() {
    }

    public static double distanceToCenter(int x, int y) {
        return Math.sqrt(Math.pow((double)(x - maxWidth / 2), 2.0D) + Math.pow((double)(y - maxHeight / 2), 2.0D));
    }

    public static void main(String[] args) {
        try {
            PrintStream output = new PrintStream(new FileOutputStream(filename));
            Random generator = new Random();

            for(int i = 0; i < numberOfPoints; ++i) {
                int x;
                int y;
                do {
                    x = generator.nextInt(maxWidth);
                    y = generator.nextInt(maxHeight);
                } while(distanceToCenter(x, y) >= (double)radius * 1.4D && (distanceToCenter(x, y) >= (double)radius * 1.6D || generator.nextInt(5) != 1) && (distanceToCenter(x, y) >= (double)radius * 1.8D || generator.nextInt(10) != 1) && (maxHeight / 5 >= x || x >= 4 * maxHeight / 5 || maxHeight / 5 >= y || y >= 4 * maxHeight / 5 || generator.nextInt(100) != 1));

                output.println(Integer.toString(x) + " " + Integer.toString(y));
            }

            output.close();
        } catch (FileNotFoundException var6) {
            System.err.println("I/O exception: unable to create " + filename);
        }

    }
}
