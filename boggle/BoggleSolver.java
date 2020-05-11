/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {

    private final TrieDict dict = new TrieDict();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary)
            dict.insert(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSET v = new TrieSET();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++)
                boggleSearch(new int[] { i, j }, board, v);
        }
        return v;
    }

    private void boggleSearch(int[] start, BoggleBoard b, TrieSET v) {
        boolean[][] marked = new boolean[b.rows()][b.cols()];
        dfs(start, marked, "" + b.getLetter(start[0], start[1]), b, v);
    }

    private void dfs(int[] cur, boolean[][] marked, String prefix, BoggleBoard b, TrieSET v) {
        marked[cur[0]][cur[1]] = true;
        if (prefix.endsWith("Q")) prefix = prefix + "U";
        if (!dict.prefixCheck(prefix)) {
            marked[cur[0]][cur[1]] = false;
            return;
        }
        if (dict.contains(prefix) && prefix.length() > 2) v.add(prefix);
        for (int[] n : adj(cur, b))
            if (!marked[n[0]][n[1]]) dfs(n, marked, prefix + b.getLetter(n[0], n[1]), b, v);
        marked[cur[0]][cur[1]] = false;
    }

    private Queue<int[]> adj(int[] coord, BoggleBoard b) {
        int i = coord[0];
        int j = coord[1];
        boolean[] bools = new boolean[] { i != 0, i != b.rows() - 1, j != 0, j != b.cols() - 1 };
        Queue<int[]> q = new Queue<>();
        if (bools[0]) q.enqueue(new int[] { i - 1, j });
        if (bools[1]) q.enqueue(new int[] { i + 1, j });
        if (bools[2]) q.enqueue(new int[] { i, j - 1 });
        if (bools[3]) q.enqueue(new int[] { i, j + 1 });
        if (bools[0] && bools[2]) q.enqueue(new int[] { i - 1, j - 1 });
        if (bools[0] && bools[3]) q.enqueue(new int[] { i - 1, j + 1 });
        if (bools[1] && bools[2]) q.enqueue(new int[] { i + 1, j - 1 });
        if (bools[1] && bools[3]) q.enqueue(new int[] { i + 1, j + 1 });
        return q;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();
        if (!dict.contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len < 5) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
