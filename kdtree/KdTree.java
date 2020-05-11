/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private Node root = null;
    private int size = 0;

    private class Node implements Comparable<Node> {
        private Point2D point;
        private Node left = null;
        private Node right = null;
        private int level;

        private Node(Point2D p, int l) {
            point = p;
            level = l;
        }

        public String toString() {
            return point.toString() + " lv: " + level;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.level, that.level);
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add point to set (if not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        root = put(root, p, 0);
        size++;
    }

    // recursively add point to tree
    private Node put(Node x, Point2D p, int level) {
        if (x == null) return new Node(p, level);
        int coord = x.level % 2;
        double[] nodeCoords = new double[] { x.point.x(), x.point.y() };
        double[] pCoords = new double[] { p.x(), p.y() };
        int cmp = Double.compare(pCoords[coord], nodeCoords[coord]);
        if (cmp < 0) x.left = put(x.left, p, x.level + 1);
        else x.right = put(x.right, p, x.level + 1);
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        double[] pCoords = new double[] { p.x(), p.y() };
        while (x != null) {
            int lv = x.level % 2;
            double[] nodeCoords = new double[] { x.point.x(), x.point.y() };
            int cmp = Double.compare(pCoords[lv], nodeCoords[lv]);
            if (p.equals(x.point)) return true;
            if (cmp < 0) x = x.left;
            else x = x.right;
        }
        return false;
    }

    public void draw() {
        RectHV current = new RectHV(0, 0, 1, 1);
        draw(root, current);
    }

    private void draw(Node x, RectHV cur) {
        if (x == null) return;
        int lv = x.level % 2;
        double[] topRightLL = new double[] { x.point.x(), cur.xmax(), cur.ymax(), x.point.y() };
        double[] botLeftRU = new double[] { x.point.x(), cur.xmin(), cur.ymin(), x.point.y() };
        // RectHV constructor xo, yo, xf, yf
        RectHV leftLower = new RectHV(cur.xmin(), cur.ymin(), topRightLL[lv], topRightLL[lv + 2]);
        RectHV rightUpper = new RectHV(botLeftRU[lv], botLeftRU[lv + 2], cur.xmax(), cur.ymax());
        StdDraw.setPenRadius(0.005);
        if (lv == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(leftLower.xmax(), leftLower.ymin(), leftLower.xmax(), leftLower.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rightUpper.xmin(), rightUpper.ymin(), rightUpper.xmax(),
                         rightUpper.ymin());
        }
        draw(x.left, leftLower);
        draw(x.right, rightUpper);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        x.point.draw();
    }

    // check if node is an ancestor of another node
    private boolean isAncestor(Node ancestor, Node child) {
        Node parent = findParent(child);
        while (parent != null) {
            if (parent.point.equals(ancestor.point)) return true;
            parent = findParent(parent);
        }
        return false;
    }

    // return parent of a given node
    private Node findParent(Node child) {
        Point2D p = child.point;
        Node x = root;
        Node parent = null;
        double[] pCoords = new double[] { p.x(), p.y() };
        while (x != null) {
            int lv = x.level % 2;
            double[] nodeCoords = new double[] { x.point.x(), x.point.y() };
            int cmp = Double.compare(pCoords[lv], nodeCoords[lv]);
            if (p.equals(x.point)) return parent;
            parent = x;
            if (cmp < 0) x = x.left;
            else x = x.right;
        }
        return null;
    }

    // returns queue of inorder traversal of tree
    private Iterable<Node> all() {
        Queue<Node> q = new Queue<>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Node> q) {
        if (x == null) return;
        inorder(x.left, q);
        q.enqueue(x);
        inorder(x.right, q);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<>();
        RectHV current = new RectHV(0, 0, 1, 1);
        rangeSearch(root, rect, current, q);
        return q;
    }

    private void rangeSearch(Node x, RectHV query, RectHV cur, Queue<Point2D> q) {
        if (x == null) return;
        int lv = x.level % 2;
        double[] topRightLL = new double[] { x.point.x(), cur.xmax(), cur.ymax(), x.point.y() };
        double[] botLeftRU = new double[] { x.point.x(), cur.xmin(), cur.ymin(), x.point.y() };
        // RectHV constructor xo, yo, xf, yf
        RectHV leftLower = new RectHV(cur.xmin(), cur.ymin(), topRightLL[lv], topRightLL[lv + 2]);
        RectHV rightUpper = new RectHV(botLeftRU[lv], botLeftRU[lv + 2], cur.xmax(), cur.ymax());
        if (query.intersects(leftLower)) rangeSearch(x.left, query, leftLower, q);
        if (query.intersects(rightUpper)) rangeSearch(x.right, query, rightUpper, q);
        if (query.contains(x.point)) q.enqueue(x.point);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (size == 0) return null;
        RectHV current = new RectHV(0.0, 0.0, 1.0, 1.0);
        return nearestNeighbor(root, p, root.point, current);
    }

    private Point2D nearestNeighbor(Node x, Point2D goal, Point2D closestSoFar, RectHV cur) {
        Point2D closest = closestSoFar;
        if (x == null) return closest;
        if (goal.distanceSquaredTo(closest) < cur.distanceSquaredTo(goal)) return closest;
        if (goal.distanceSquaredTo(x.point) < goal.distanceSquaredTo(closest)) closest = x.point;
        if (x.left == null && x.right == null) return closest;
        int lv = x.level % 2;
        double[] topRightLL = new double[] { x.point.x(), cur.xmax(), cur.ymax(), x.point.y() };
        double[] botLeftRU = new double[] { x.point.x(), cur.xmin(), cur.ymin(), x.point.y() };
        RectHV leftLower = new RectHV(cur.xmin(), cur.ymin(), topRightLL[lv], topRightLL[lv + 2]);
        RectHV rightUpper = new RectHV(botLeftRU[lv], botLeftRU[lv + 2], cur.xmax(), cur.ymax());
        if (x.left == null) return nearestNeighbor(x.right, goal, closest, rightUpper);
        if (x.right == null) return nearestNeighbor(x.left, goal, closest, leftLower);
        if (leftLower.distanceSquaredTo(goal) < rightUpper.distanceSquaredTo(goal)) {
            closest = nearestNeighbor(x.left, goal, closest, leftLower);
            closest = nearestNeighbor(x.right, goal, closest, rightUpper);
        }
        else {
            closest = nearestNeighbor(x.right, goal, closest, rightUpper);
            closest = nearestNeighbor(x.left, goal, closest, leftLower);
        }
        return closest;
    }

    public static void main(String[] args) {
        KdTree set = new KdTree();
        set.insert(new Point2D(0.7, 0.2));
        set.insert(new Point2D(0.5, 0.4));
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.4, 0.7));
        set.insert(new Point2D(0.9, 0.6));
        set.insert(new Point2D(0.4, 0.1));
        StdOut.println(set.contains(new Point2D(0.7, 0.2)));
        StdOut.println(set.contains(new Point2D(0.5, 0.4)));
        StdOut.println(set.contains(new Point2D(0.2, 0.3)));
        StdOut.println(set.contains(new Point2D(0.4, 0.7)));
        StdOut.println(set.contains(new Point2D(0.9, 0.6)));
        Point2D p = new Point2D(0.2, 0.1);
        set.insert(p);
        StdOut.println(set.contains(p));
        StdOut.println(set.contains(new Point2D(0.6, 0.9)));
        StdOut.println(set.size());
        set.insert(new Point2D(0.2, 0.7));
        //set.insert(new Point2D(6.0, 5.0));

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(0.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        set.draw();

        for (Point2D found : set.range(new RectHV(0.3, 0, 0.6, 0.5))) {
            StdOut.println(found);
        }

        StdOut.println("Nearest to 0.2, 0.1");
        StdOut.println(set.nearest(p));
    }
}
