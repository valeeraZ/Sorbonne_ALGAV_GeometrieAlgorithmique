package test;

import org.junit.jupiter.api.BeforeAll;
import tools.Circle;
import algorithms.Naive;
import algorithms.Welzl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Test {

    Naive naive = new Naive();
    Welzl welzl = new Welzl();

    static int number = 0;

    protected static String samplesDirName = "samples";
    protected static File result = new File("result");
    protected static String pattern = ".*\\.points";

    long t1, t2;
    double deltaX, deltaY, deltaRadius;

    public static File[] getFileList() throws Exception {
        final Pattern p = Pattern.compile("^" + pattern + "$");
        final FilenameFilter ff = new FilenameFilter() {
            @Override
            public boolean accept (File dir, String name) {
                final Matcher m = p.matcher(name);
                return m.matches();
            }
        };
        File samplesDir = new File(samplesDirName);
        final File[] testFiles = samplesDir.listFiles(ff);
        if (testFiles == null) {
            throw new IllegalArgumentException("Directory does not exist : " + samplesDirName);
        }
        assertNotNull(testFiles);

        if ( testFiles.length == 0 ) {
            final String msg = "Cannot find a single test like " + pattern + " in " + samplesDirName;
            throw new IllegalArgumentException(msg);
        }
        java.util.Arrays.sort(testFiles,
                (f1, f2) -> f1.getName().compareTo(f2.getName()));
        return testFiles;
    }

    public static ArrayList<Point> readFile(File file){
        ArrayList<Point> points = new ArrayList<>();
        try{
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            try {
                String line;
                while((line = input.readLine()) != null) {
                    String[] coordinates = line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
                }
            } catch (IOException e) {
                System.err.println("Exception: interrupted I/O.");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("I/O exception: unable to close " + file.getName());
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return points;
    }

    public static void writeFile(File file, String contents) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(contents);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Stream<ArrayList> dataProvider() throws Exception {
        ArrayList[] data = new ArrayList[1664];
        File[] files = getFileList();
        for(int i = 0; i < files.length; i++){
            data[i] = readFile(files[i]);
        }
        return Stream.of(data);
    }

    @BeforeAll
    static void prepareFileResult(){
        try {
            if(!result.exists()) {
                result.createNewFile();
            }else {
                FileWriter writer = new FileWriter(result,false);
                writer.write("");
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void correctAlgorithms(ArrayList<Point> points){

        number++;

        t1 = System.currentTimeMillis();
        Circle naiveCircle = naive.calculCercleMin(points);
        t1 = System.currentTimeMillis() - t1;
        System.out.print("naiveCircle = " + naiveCircle);
        System.out.println(" time = " + t1);

        t2 = System.currentTimeMillis();
        Circle welzlCircle = welzl.calculCercleMin(points);
        t2 = System.currentTimeMillis() - t2;
        System.out.print("welzlCircle = " + welzlCircle);
        System.out.println(" time = " + t2);

        double naiveX = naiveCircle.getCenter().x;
        double naiveY = naiveCircle.getCenter().y;
        double naiveRadius = naiveCircle.getRadius();

        double welzlX = welzlCircle.getCenter().x;
        double welzlY = welzlCircle.getCenter().y;
        double welzlRadius = welzlCircle.getRadius();

        deltaX = Math.abs(naiveX - welzlX);
        deltaY = Math.abs(naiveY - welzlY);
        deltaRadius = Math.abs(naiveRadius - welzlRadius);

        String contents = number + " " + t1 + " " + t2 + " " + deltaX + " " + deltaY + " " + deltaRadius + "\n";
        writeFile(result, contents);

        assertEquals(naiveX, welzlX, 4.0);
        assertEquals(naiveY, welzlY, 4.0);
        assertEquals(naiveRadius, welzlRadius, 4.0);
    }
}
