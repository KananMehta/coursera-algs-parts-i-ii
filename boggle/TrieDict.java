/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class TrieDict {

    private static final int R = 26;
    private static final char OFFSET = 'A';

    private Node root = null;
    private int size;

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isWord;
    }

    public void insert(String s) {
        root = put(root, s, 0);
        size++;
    }

    private Node put(Node x, String s, int d) {
        if (x == null) x = new Node();
        if (d == s.length()) {
            x.isWord = true;
            return x;
        }
        int c = s.charAt(d) - OFFSET;
        x.next[c] = put(x.next[c], s, d + 1);
        return x;
    }

    // check if Dictionary contains word w
    public boolean contains(String w) {
        Node x = get(root, w, 0);
        if (x == null) return false;
        return x.isWord;
    }

    private Node get(Node x, String s, int d) {
        if (x == null) return null;
        if (d == s.length()) return x;
        int c = s.charAt(d) - OFFSET;
        return get(x.next[c], s, d + 1);
    }

    // check if Dictionary contains at least one word starting with prefix p
    public boolean prefixCheck(String p) {
        return get(root, p, 0) != null;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {

    }
}
