/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;
    private static final int LGR = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] table = gen();
        while (!BinaryStdIn.isEmpty()) {
            char next = BinaryStdIn.readChar(LGR);
            int n = -1;
            while (table[++n] != next) continue;
            BinaryStdOut.write((char) n, LGR);
            for (int i = 0; i <= n; i++) {
                char temp = table[i];
                table[i] = next;
                next = temp;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] table = gen();
        while (!BinaryStdIn.isEmpty()) {
            char in = BinaryStdIn.readChar(LGR);
            char next = table[in];
            BinaryStdOut.write(next, LGR);
            for (int j = 0; j <= in; j++) {
                char temp = table[j];
                table[j] = next;
                next = temp;
            }
        }
        BinaryStdOut.close();
    }

    private static char[] gen() {
        char[] table = new char[R];
        for (char i = 0; i < R; i++)
            table[i] = i;
        return table;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
