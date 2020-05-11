/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {

    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = G;
    }

    private void checkVert(int v) {
        if (v < 0 || v >= G.V()) throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVert(v);
        checkVert(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (!(bfsV.hasPathTo(i) && bfsW.hasPathTo(i))) continue;
            int dist = bfsV.distTo(i) + bfsW.distTo(i);
            if (dist < min) {
                min = dist;
                shortest = i;
            }
        }
        if (shortest == -1) return -1;
        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVert(v);
        checkVert(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (!(bfsV.hasPathTo(i) && bfsW.hasPathTo(i))) continue;
            int dist = bfsV.distTo(i) + bfsW.distTo(i);
            if (dist < min) {
                min = dist;
                shortest = i;
            }
        }
        return shortest;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (int n : v) checkVert(n);
        for (int n : w) checkVert(n);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (!(bfsV.hasPathTo(i) && bfsW.hasPathTo(i))) continue;
            int dist = bfsV.distTo(i) + bfsW.distTo(i);
            if (dist < min) {
                min = dist;
                shortest = i;
            }
        }
        if (shortest == -1) return -1;
        return min;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int n : v) checkVert(n);
        for (int n : w) checkVert(n);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (!(bfsV.hasPathTo(i) && bfsW.hasPathTo(i))) continue;
            int dist = bfsV.distTo(i) + bfsW.distTo(i);
            if (dist < min) {
                min = dist;
                shortest = i;
            }
        }
        return shortest;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

