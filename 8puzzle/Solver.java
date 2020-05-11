/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private boolean solvable = false;
    private Stack<Board> sol = null;
    private int moves = -1;

    private class Node implements Comparable<Node> {
        private Board board;
        private int m;
        private Node previous;
        private int manhattan;
        // private int hamming;

        private Node(Board b, Node previous) {
            this(b, previous, previous.m + 1);
        }

        private Node(Board b, Node previous, int m) {
            board = b;
            this.previous = previous;
            this.m = m;
            manhattan = board.manhattan();
        }

        public int compareTo(Node that) {
            int a = this.manhattan + this.m;
            int b = that.manhattan + that.m;
            return (a - b);
        }

        public Comparator<Node> hamOrder() {
            return new ByHam();
        }

        private class ByHam implements Comparator<Node> {
            public int compare(Node a, Node b) {
                return Integer.compare(a.board.hamming() + a.m, b.board.hamming() + b.m);
            }
        }

        public Comparator<Node> manOrder() {
            return new ByMan();
        }

        private class ByMan implements Comparator<Node> {
            public int compare(Node a, Node b) {
                return Integer.compare(a.manhattan + a.m, b.manhattan + b.m);
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        Node remNode = new Node(initial, null, 0);
        Node twinNode = new Node(initial.twin(), null, 0);
        Comparator<Node> cmp = remNode.manOrder();
        MinPQ<Node> mainPQ = new MinPQ<Node>(cmp); // queue for given board
        MinPQ<Node> twinPQ = new MinPQ<Node>(cmp);
        mainPQ.insert(remNode);
        twinPQ.insert(twinNode);
        while (!mainPQ.isEmpty() && !twinPQ.isEmpty()) {
            remNode = mainPQ.delMin();
            twinNode = twinPQ.delMin();
            // StdOut.println(remNode.board.toString());
            if (remNode.board.isGoal()) {
                solvable = true;
                moves = remNode.m;
                Node current = remNode;
                sol = new Stack<Board>();
                while (current.previous != null) {
                    sol.push(current.board);
                    current = current.previous;
                }
                sol.push(current.board);
                break;
            }
            if (twinNode.board.isGoal()) break;
            for (Board board : remNode.board.neighbors()) {
                if (remNode.previous == null || !board.equals(remNode.previous.board)) {
                    Node n = new Node(board, remNode);
                    mainPQ.insert(n);
                    // StdOut.printf("move number %d, manhattan %d\n", n.m, n.board.manhattan());
                }
            }
            for (Board board : twinNode.board.neighbors()) {
                if (twinNode.previous == null || !board.equals(twinNode.previous.board)) {
                    twinPQ.insert(new Node(board, twinNode));
                    // StdOut.printf("move number %d, manhattan %d\n", n.m, n.board.manhattan());
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

