/* *****************************************************************************
 *  Name:    Kanan Mehta
 *  NetID:   unknown
 *  Precept: P00
 *
 *  Description:  Statistical experiment for Percolation.java
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final int GRID_SHIFT = 1;

    private double[] results; // open sites required for each trial
    private int trials; // trials

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        trials = t;
        results = new double[t];
        int totalSites = n * n;
        for (int i = 0; i < trials; i++) {
            results[i] = (double) solve(n) / (double) totalSites;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt((trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    //
    private int solve(int n) {
        Percolation perc = new Percolation(n);
        int x = 0;
        int y = 0;
        while (!perc.percolates()) {
            x = StdRandom.uniform(GRID_SHIFT, n + GRID_SHIFT);
            y = StdRandom.uniform(GRID_SHIFT, n + GRID_SHIFT);
            if (perc.isOpen(x, y)) {
                continue;
            }
            perc.open(x, y);
        }
        return perc.numberOfOpenSites();
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        StdOut.printf("%d %d\n", n, t);
        PercolationStats exp = new PercolationStats(n, t);
        StdOut.printf("mean = %f\n", exp.mean());
        StdOut.printf("stddev = %f\n", exp.stddev());
        StdOut.printf("95%% confidence interval = [%f %f]", exp.confidenceLo(), exp.confidenceHi());
    }

}
