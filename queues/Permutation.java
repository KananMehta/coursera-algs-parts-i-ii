/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        //String filename = args[1];
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        //In in = new In(filename);
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }
        int c = 0;
        for (String s : rq) {
            if (c++ == k) break;
            StdOut.println(s);
        }
    }
}
