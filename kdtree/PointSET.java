/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class PointSET {

    private SET<Point2D> set = new SET<>();

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add point to set (if not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!set.contains(p)) set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
            Point2D point = iter.next();
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<>();
        for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
            Point2D point = iter.next();
            if (rect.contains(point)) q.enqueue(point);
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        double minDist = Double.POSITIVE_INFINITY;
        Point2D min = null;
        for (Iterator<Point2D> iter = set.iterator(); iter.hasNext(); ) {
            Point2D point = iter.next();
            double dist = p.distanceTo(point);
            if (dist < minDist) {
                min = point;
                minDist = dist;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        RectHV rect = new RectHV(10.0, 10.0, 30.0, 30.0);
        PointSET set = new PointSET();
        set.insert(new Point2D(20.0, 2.0));
        set.insert(new Point2D(25.0, 25.0));
        set.insert(new Point2D(5.0, 5.0));
        set.insert(new Point2D(30.0, 30.0));
        set.insert(new Point2D(50.0, 50.0));
        set.insert(new Point2D(5.0, 0.0));
        StdOut.println(StdDraw.getPenColor());
        StdOut.println(StdDraw.getPenRadius());
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(0.0, 50.0);
        StdDraw.setPenRadius(0.006);
        StdDraw.setPenColor(StdDraw.BLACK);
        set.draw();
        for (Point2D p : set.range(rect)) {
            StdOut.println(p);
        }

        StdOut.println(Double.compare(0.0, 1.0));
    }
}
