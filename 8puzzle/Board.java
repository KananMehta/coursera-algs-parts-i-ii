/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] board;
    private int dim;
    private int[] zero; // row and column number of 0 location
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dim = tiles.length;
        board = new int[dim][dim];
        zero = new int[2];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zero[0] = i;
                    zero[1] = j;
                }
            }
        }
        calcHamming();
        calcManhattan();
    }

    private void calcHamming() {
        hamming = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) continue;
                int correct = i * dim + j + 1;
                if (board[i][j] != correct) hamming++;
            }
        }
    }

    private void calcManhattan() {
        manhattan = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int num = board[i][j];
                if (num == 0) continue;
                int corJ;
                if (num % dim == 0) corJ = dim - 1;
                else corJ = (num % dim) - 1;
                int corI = (num - corJ - 1) / dim;
                // StdOut.printf("num: %d pos: %d,%d cor: %d,%d\n", num, i, j, corI, corJ);
                manhattan += Math.abs(corI - i) + Math.abs(corJ - j);
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                buf.append(" " + board[i][j]);
                if (j == dim - 1) buf.append("\n");
            }
        }
        return buf.toString();
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == dim - 1 && i == j && board[i][j] == 0) continue;
                int correct = i * dim + j + 1;
                if (board[i][j] != correct) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != getClass()) return false;
        Board b = (Board) y;
        if (this.dim != b.dim) return false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != b.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int i = zero[0];
        int j = zero[1];
        if (zero[0] != 0) q.enqueue(swap(i - 1, j)); // not on top
        if (zero[0] != dim - 1) q.enqueue(swap(i + 1, j)); // not on bottom
        if (zero[1] != 0) q.enqueue(swap(i, j - 1)); // not on left
        if (zero[1] != dim - 1) q.enqueue(swap(i, j + 1)); // not on right
        return q;
    }

    private Board swap(int i, int j) {
        /*
        int[][] b2 = board.clone();
        int emptyI = zero[0];
        int emptyJ = zero[1];
        b2[emptyI][emptyJ] = board[i][j];
        b2[i][j] = 0;
        return new Board(b2);
         */

        Board b2 = new Board(board);
        int emptyI = zero[0];
        int emptyJ = zero[1];
        b2.board[emptyI][emptyJ] = board[i][j];
        b2.board[i][j] = 0;
        b2.zero = new int[] { i, j };
        b2.calcManhattan();
        b2.calcHamming();
        return b2;
    }

    // a board that is obtained by exchanging any pair of tiles

    public Board twin() {
        int i1 = 0;
        int j1 = 0;
        int i2 = dim - 1;
        int j2 = 0;
        if (board[i1][j1] == 0) j1++;
        if (board[i2][j2] == 0) j2++;
        /*
        int[][] b2 = board.clone();
        b2[i1][j1] = board[i2][j2];
        b2[i2][j2] = board[i1][j1];
        return new Board(b2);

         */
        Board b2 = new Board(this.board);
        b2.board[i1][j1] = board[i2][j2];
        b2.board[i2][j2] = board[i1][j1];
        b2.calcManhattan();
        b2.calcHamming();
        return b2;

    }


/*
    public Board twin() {
        int i1 = StdRandom.uniform(dim);
        int j1 = StdRandom.uniform(dim);
        int i2 = StdRandom.uniform(dim);
        int j2 = StdRandom.uniform(dim);
        while (board[i1][j1] == 0 || board[i2][j2] == 0 || board[i1][j1] == board[i2][j2]) {
            if (board[i1][j1] == 0) i1 = StdRandom.uniform(dim);
            if (board[i2][j2] == 0) i2 = StdRandom.uniform(dim);
            if (board[i1][j1] == board[i2][j2]) j1 = StdRandom.uniform(dim);
        }
        Board b2 = new Board(this.board);
        b2.board[i1][j1] = this.board[i2][j2];
        b2.board[i2][j2] = this.board[i1][j1];
        b2.calcHamming();
        b2.calcManhattan();
        return b2;
    }
 */


    // unit testing (not graded)
    public static void main(String[] args) {
        int[] nums = { 0, 1, 2, 3 };
        // StdRandom.shuffle(nums);
        int dim = (int) Math.sqrt(nums.length);
        int[][] tiles = new int[dim][dim];
        int c = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tiles[i][j] = nums[c];
                c++;
            }
        }
        Board board = new Board(tiles);
        StdOut.print(board.toString());
        StdOut.printf("hamming = %d manhattan = %d\n", board.hamming(), board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.print(board.twin().toString());
        StdOut.print("Neighbors:\n");
        Queue<Board> q = (Queue<Board>) board.neighbors();
        for (Board item : q) {
            StdOut.print(item.toString());
            StdOut.printf("hamming %d\nmanhattan %d\n", item.hamming(), item.manhattan());
        }
    }
}
