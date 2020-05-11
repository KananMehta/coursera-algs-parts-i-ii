/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {

    private Picture pic;
    private boolean changed;
    private int[][] image;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = new Picture(picture);
        image = new int[pic.width()][pic.height()];
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                image[i][j] = pic.getRGB(i, j);
            }
        }
        changed = false;
    }

    // current picture
    public Picture picture() {
        if (changed) {
            pic = new Picture(image.length, image[0].length);
            for (int i = 0; i < pic.width(); i++) {
                for (int j = 0; j < pic.height(); j++)
                    pic.setRGB(i, j, image[i][j]);
            }
            changed = false;
        }
        return pic;
    }

    // width of current picture
    public int width() {
        return image.length;
    }

    // height of current picture
    public int height() {
        return image[0].length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0) throw new IllegalArgumentException();
        if (x >= width() || y >= height()) throw new IllegalArgumentException();
        if (x >= pic.width() - 1 || y >= pic.height() - 1 || y == 0 || x == 0) return 1000;
        return Math.sqrt(colorGradient(pic.get(x + 1, y), pic.get(x - 1, y)) + colorGradient(
                pic.get(x, y + 1), pic.get(x, y - 1)));
    }

    private double colorGradient(Color c1, Color c2) {
        int r = c1.getRed() - c2.getRed();
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        return r * r + g * g + b * b;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        //pic = picture();
        //if (height() < 1) return null;
        double[][] energies = new double[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energies[i][j] = energy(i, j);
                edgeTo[i][j] = -1;
                if (i == 0) distTo[i][j] = energies[i][j];
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int i = 0; i < width() - 1; i++) {
            for (int j = 0; j < height(); j++) {
                //StdOut.printf("i: %d j: %d\n", i, j);
                double dist = distTo[i][j];
                if (distTo[i + 1][j] > dist + energies[i + 1][j]) {
                    edgeTo[i + 1][j] = j;
                    distTo[i + 1][j] = dist + energies[i + 1][j];
                }
                if (j != 0 && distTo[i + 1][j - 1] > dist + energies[i + 1][j - 1]) {
                    edgeTo[i + 1][j - 1] = j;
                    distTo[i + 1][j - 1] = dist + energies[i + 1][j - 1];
                }
                if (j != height() - 1 && distTo[i + 1][j + 1] > dist + energies[i + 1][j + 1]) {
                    edgeTo[i + 1][j + 1] = j;
                    distTo[i + 1][j + 1] = dist + energies[i + 1][j + 1];
                }
            }
        }
        int[] seam = new int[width()];
        double min = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int j = 0; j < height(); j++) {
            if (distTo[width() - 1][j] < min) {
                min = distTo[width() - 1][j];
                last = j;
            }
        }
        Stack<Integer> seamStack = new Stack<>();
        int i = width() - 1;
        seamStack.push(last);
        while (edgeTo[i][last] != -1) {
            last = edgeTo[i--][last];
            seamStack.push(last);
        }
        int j = 0;
        for (int item : seamStack) {
            seam[j++] = item;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        //if (width() < 1) return null;
        //pic = picture();
        double[][] energies = new double[width()][height()];
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energies[i][j] = energy(i, j);
                edgeTo[i][j] = -1;
                if (j == 0) distTo[i][j] = energies[i][j];
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int j = 0; j < height() - 1; j++) {
            for (int i = 0; i < width(); i++) {
                double dist = distTo[i][j];
                if (distTo[i][j + 1] > dist + energies[i][j + 1]) {
                    edgeTo[i][j + 1] = i;
                    distTo[i][j + 1] = dist + energies[i][j + 1];
                }
                if (i != 0 && distTo[i - 1][j + 1] > dist + energies[i - 1][j + 1]) {
                    edgeTo[i - 1][j + 1] = i;
                    distTo[i - 1][j + 1] = dist + energies[i - 1][j + 1];
                }
                if (i != width() - 1 && distTo[i + 1][j + 1] > dist + energies[i + 1][j + 1]) {
                    edgeTo[i + 1][j + 1] = i;
                    distTo[i + 1][j + 1] = dist + energies[i + 1][j + 1];
                }
            }
        }
        int[] seam = new int[height()];
        double min = Double.POSITIVE_INFINITY;
        int last = -1;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < min) {
                min = distTo[i][height() - 1];
                last = i;
            }
        }
        Stack<Integer> seamStack = new Stack<>();
        int j = height() - 1;
        seamStack.push(last);
        while (edgeTo[last][j] != -1) {
            last = edgeTo[last][j--];
            seamStack.push(last);
        }
        int i = 0;
        for (int item : seamStack) {
            seam[i++] = item;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height() <= 1) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1) throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
        }
        changed = true;
        int[][] newImage = new int[width()][height() - 1];
        int c = 0;
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (j == seam[i]) continue;
                newImage[i][c++] = image[i][j];
            }
            c = 0;
        }
        image = newImage;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width() <= 1) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1) throw new IllegalArgumentException();
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException();
        }
        transposeImage();
        removeHorizontalSeam(seam);
        transposeImage();
    }

    private void transposeImage() {
        int[][] temp = new int[height()][width()];
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                temp[j][i] = image[i][j];
            }
        }
        image = temp;
    }

    private void printArray(int[][] arr) {
        for (int i = 0; i < arr[0].length; i++) {
            for (int j = 0; j < arr.length; j++) {
                StdOut.print(arr[j][i]);
                StdOut.print(" ");
            }
            StdOut.print("\n");
        }
    }

    private void printArray(double[][] arr) {
        for (int i = 0; i < arr[0].length; i++) {
            for (int j = 0; j < arr.length; j++) {
                StdOut.print(arr[j][i]);
                StdOut.print(" ");
            }
            StdOut.print("\n");
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pict = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(pict);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", pict.width(), pict.height());

        StdOut.printf("Vertical seam is %d long\n", sc.findVerticalSeam().length);
        for (int i : sc.findVerticalSeam()) StdOut.println(i);
        //StdOut.printf("Horizontal seam is %d long\n", sc.findHorizontalSeam().length);
        //for (int i : sc.findHorizontalSeam()) StdOut.println(i);

        //sc.removeVerticalSeam(sc.findVerticalSeam());
        //sc.removeVerticalSeam(sc.findVerticalSeam());

        for (int i = 0; i < 8; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
            StdOut.printf("Width %d\n", sc.image.length);
        }

        // sc.pic.show();

    }

}
