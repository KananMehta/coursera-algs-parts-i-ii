/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int numSegs = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkArgs(points);
        segments = new LineSegment[points.length / 4 + 1]; // len/4 + 1
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int n = k + 1; n < points.length; n++) {
                        double slopePQ = points[i].slopeTo(points[j]);
                        double slopePR = points[i].slopeTo(points[k]);
                        double slopePS = points[i].slopeTo(points[n]);
                        if (!(slopePQ == slopePR && slopePR == slopePS)) continue;
                        Point max = points[i];
                        Point min = points[i];
                        if (points[j].compareTo(max) > 0) max = points[j];
                        if (points[j].compareTo(min) < 0) min = points[j];
                        if (points[k].compareTo(max) > 0) max = points[k];
                        if (points[k].compareTo(min) < 0) min = points[k];
                        if (points[n].compareTo(max) > 0) max = points[n];
                        if (points[n].compareTo(min) < 0) min = points[n];
                        LineSegment line = new LineSegment(min, max);
                        segments[numSegs] = line;
                        numSegs++;
                    }
                }
            }
        }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println(collinear.numSegs);
    }
}
