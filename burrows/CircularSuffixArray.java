/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private final int size;
    private final int[] index;

    private class CS implements Comparable<CS> {
        private String s;
        private int offset;

        private CS(String s, int offset) {
            this.s = s;
            this.offset = offset;
        }

        public int compareTo(CS other) {
            for (int i = 0; i < size; i++) {
                if (this.getChar(i) > other.getChar(i)) return 1;
                if (this.getChar(i) < other.getChar(i)) return -1;
            }
            return 0;
        }

        private char getChar(int i) {
            return s.charAt((offset + i) % size);
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        size = s.length();
        index = new int[size];
        CS[] arr = new CS[size];
        for (int i = 0; i < size; i++)
            arr[i] = new CS(s, i);
        Arrays.sort(arr);
        for (int i = 0; i < size; i++)
            index[i] = arr[i].offset;
    }

    // length of s
    public int length() {
        return size;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > size - 1) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        StdOut.printf("Length %d\n", csa.length());
        for (int i = 0; i < csa.length(); i++)
            StdOut.printf("index[%d] = %d\n", i, csa.index(i));
    }
}
