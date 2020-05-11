/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        int ind = StdRandom.uniform(0, ++size);
        Item backup = q[ind];
        q[ind] = item;
        if (ind != size - 1) q[size - 1] = backup;
        if (size == q.length) resize(2 * q.length);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        if (size > 0 && size == q.length / 4) resize(q.length / 2);
        return q[--size];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = q[i];
        }
        q = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, size);
        return q[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int c = size;
        private Item[] copy = (Item[]) new Object[size];

        private ListIterator() {
            for (int i = 0; i < size; i++) {
                copy[i] = q[i];
            }
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() {
            return c > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy[--c];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        while (true) {
            StdOut.println("1 = Enqueue\n2 = Dequeue\n3 = Sample\n4 = break");
            int op = StdIn.readInt();
            if (op == 1) {
                StdOut.println("Provide input to add");
                rq.enqueue(StdIn.readInt());
            }
            if (op == 2) StdOut.printf("Returned %d\n", rq.dequeue());
            if (op == 3) StdOut.printf("Sampled %d\n", rq.sample());
            if (op == 4) break;
            StdOut.print("Contents of queue: ");
            for (Iterator<Integer> i = rq.iterator(); i.hasNext(); ) {
                StdOut.print(i.next());
            }
            StdOut.print("\n");
        }
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
