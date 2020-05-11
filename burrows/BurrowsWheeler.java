/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int zero = 0;
        for (int i = 0; i < s.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                zero = i;
                break;
            }
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == zero) BinaryStdOut.write(s.charAt(s.length() - 1));
            else BinaryStdOut.write(s.charAt(csa.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String in = BinaryStdIn.readString();
        int len = in.length();
        char[] t = in.toCharArray();
        int[] next = new int[len];
        char[] sorted = Arrays.copyOf(t, len);
        Arrays.sort(sorted);
        HashMap<Character, Queue<Integer>> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            char cur = t[i];
            if (map.get(cur) == null) {
                Queue<Integer> q = new Queue<>();
                q.enqueue(i);
                map.put(cur, q);
            }
            else map.get(cur).enqueue(i);
        }
        for (int i = 0; i < len; i++)
            next[i] = map.get(sorted[i]).dequeue();
        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(sorted[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
