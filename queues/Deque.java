/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size = 0;
    private Node first = null;
    private Node last = null;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    /*
        // construct an empty deque
        public Deque() {

        }
    */
    // is the deque empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (isEmpty()) last = first;
        else oldFirst.previous = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) last = first;
        else first.previous = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        last = last.previous;
        size--;
        if (isEmpty()) first = last;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();
        while (true) {
            StdOut.println("1 = addFirst\n2 = addLast\n3 = removeFirst\n4 = removeLast\n5 = break");
            int op = StdIn.readInt();
            if (op == 1) {
                StdOut.println("Provide input to add");
                deck.addFirst(StdIn.readInt());
            }
            if (op == 2) {
                StdOut.println("Provide input to add");
                deck.addLast(StdIn.readInt());
            }
            if (op == 3) StdOut.printf("Removed %d\n", deck.removeFirst());
            if (op == 4) StdOut.printf("Removed %d\n", deck.removeLast());
            if (op == 5) break;
            StdOut.print("Contents of queue: ");
            for (int i : deck) {
                StdOut.print(i);
            }
            StdOut.print("\n");
        }
    }
}
