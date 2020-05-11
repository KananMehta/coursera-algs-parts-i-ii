/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private int numSegs = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkArgs(points);
        Point[] pCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pCopy[i] = points[i];
        }
        segments = new LineSegment[points.length * points.length];
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            // StdOut.println(p.toString());
            Comparator<Point> comp = p.slopeOrder();
            Arrays.sort(pCopy, comp);
            int a = 1;
            int z = a;
            while (z < pCopy.length) {
                // StdOut.printf("p[z] = %s z = %d slope = %f\n", pCopy[z].toString(), z,
                // p.slopeTo(pCopy[z]));
                if (z == pCopy.length - 1 || p.slopeTo(pCopy[z]) != p.slopeTo(pCopy[z + 1])) {
                    if (z - a >= 2) {
                        Point[] extrema = extrema(pCopy, p, a, z);
                        Point min = extrema[0];
                        Point max = extrema[1];
                        if (p.equals(min)) {
                            segments[numSegs] = new LineSegment(min, max);
                            numSegs++;
                        }
                    }
                    a = z + 1;
                }
                z += 1;
            }
        }
    }

    // given Point[], comparison point, and start/stop (inclusive) locs find min and max
    private Point[] extrema(Point[] points, Point p, int start, int stop) {
        Point max = p;
        Point min = p;
        for (int i = start; i < stop + 1; i++) {
            if (points[i].compareTo(max) > 0) max = points[i];
            if (points[i].compareTo(min) < 0) min = points[i];
        }
        Point[] extrema = { min, max };
        return extrema;
    }

    private void checkArgs(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
    }

    /*
        // calculate max possible number of elements
        private int comb(int n, int r) {
            int ans = 1;
            for (int i = 1; i <= r; i++) {
                ans *= n - (r - i);
                ans /= i;
            }
            return ans;
        }
    */

    // the number of line segments
    public int numberOfSegments() {
        return numSegs;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segTemp = new LineSegment[numSegs];
        for (int i = 0; i < numSegs; i++) {
            segTemp[i] = segments[i];
        }
        return segTemp;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println(collinear.numSegs);
    }
}
