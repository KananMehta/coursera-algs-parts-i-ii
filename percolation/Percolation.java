/* *****************************************************************************
 *  Name:    Kanan Mehta
 *  NetID:   unknown
 *  Precept: P00
 *
 *  Description:  Percolation system using Union-Find
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int GRID_SHIFT = 1;

    private boolean[][] grid; // empty percolation grid
    private int dim; // dimension n of array, set in constructor
    private int numOpen = 0; // number of open sites, starts at 0
    private WeightedQuickUnionUF unionFindV; // UF object with virt sites
    private WeightedQuickUnionUF unionFind; // UF without virt sites

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        dim = n;
        grid = new boolean[n][n];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                grid[i][j] = false;
            }
        }
        unionFindV = new WeightedQuickUnionUF(dim * dim + 2);
        unionFind = new WeightedQuickUnionUF(dim * dim + 1);

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIndex(row);
        checkIndex(col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row - GRID_SHIFT][col - GRID_SHIFT] = true;
        numOpen++;

        int id = convertIndex(row, col);
        if (row == 0 + GRID_SHIFT) {
            unionFindV.union(id, 0);
            unionFind.union(id, 0);
        }
        else if (grid[row - GRID_SHIFT - 1][col - GRID_SHIFT]) { // not on top
            int above = id - dim;
            unionFindV.union(id, above);
            unionFind.union(id, above);
        }
        if (row == dim - 1 + GRID_SHIFT) {
            unionFindV.union(id, dim * dim + 1);
        }
        else if (grid[row - GRID_SHIFT + 1][col - GRID_SHIFT]) { // not on bottom
            int below = id + dim;
            unionFindV.union(id, below);
            unionFind.union(id, below);
        }
        if (col != 0 + GRID_SHIFT && grid[row - GRID_SHIFT][col - GRID_SHIFT
                - 1]) { // not on left
            int left = id - 1;
            unionFindV.union(id, left);
            unionFind.union(id, left);
        }
        if (col != dim - 1 + GRID_SHIFT && grid[row - GRID_SHIFT][col - GRID_SHIFT
                + 1]) { // not on right
            int right = id + 1;
            unionFindV.union(id, right);
            unionFind.union(id, right);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndex(row);
        checkIndex(col);
        return grid[row - GRID_SHIFT][col - GRID_SHIFT];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkIndex(row);
        checkIndex(col);
        return unionFind.connected(0, convertIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFindV.connected(0, dim * dim + 1);
    }

    // checks if index is within bounds
    private void checkIndex(int n) {
        if ((n < (0 + GRID_SHIFT)) || (n >= (dim + GRID_SHIFT))) {
            throw new IllegalArgumentException();
        }
    }

    // converts grid index to WeightedQuickUnionUF array index
    private int convertIndex(int row, int col) {
        int id;
        id = (row - GRID_SHIFT) * dim + col + 1 - GRID_SHIFT;
        return id;
    }


    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(5);
        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 1);
        perc.open(3, 2);
        perc.open(4, 2);
        StdOut.printf("%b\n", perc.percolates());
        perc.open(5, 2);
        StdOut.printf("%b\n", perc.percolates());
        StdOut.println(Math.sqrt(3));
    }
}
