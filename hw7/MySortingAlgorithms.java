import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            for (int i = 1; i < k; i++) {
                for (int j = i; j > 0; j--) {
                    if (array[j - 1] <= array[j]) {
                        break;
                    }
                    swap(array, j - 1, j);
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            int smallest;
            int smallIndex;
            for (int i = 0; i < k; i++) {
                smallIndex = i;
                smallest = array[i];
                for (int j = i; j < k; j++) {
                    if (smallest > array[j]) {
                        smallest = array[j];
                        smallIndex = j;
                    }
                }
                swap(array, i, smallIndex);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            sort(array, 0, k - 1);
        }

        // may want to add additional methods
        public void sort(int[] array, int start, int end) {
            if (start >= end) {
                return;
            } else if (start == end - 1) {
                if (array[start] > array[end]) {
                    swap(array, start, end);
                }
            } else {
                int mid = (end + start)/2;
                sort(array, start, mid);
                sort(array, mid + 1, end);
                merge(array, start, mid, end);
            }
        }

        public void merge(int[] array, int start, int mid, int end) {
            int[] list = new int[end - start + 1];
            int i1 = start;
            int i2 = mid + 1;
            for (int i = 0; i < list.length ; i++) {
                if (i2 == end + 1) {
                    list[i] = array[i1];
                    i1++;
                } else if (i1 == mid + 1) {
                    list[i] = array[i2];
                    i2++;
                } else if (array[i1] > array[i2]) {
                    list[i] = array[i2];
                    i2++;
                } else {
                    list[i] = array[i1];
                    i1++;
                }
            }
            for (int i = 0; i < list.length; i++) {
                array[start + i] = list[i];
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
            ArrayList<Integer>[] digitList = new ArrayList[10];
            for (int i = 0; i < digitList.length; i++) {
                digitList[i] = new ArrayList<Integer>();
            }
            int div = 1;
            boolean moreDigit = true;
            while (moreDigit) {
                moreDigit = false;
                for (int i = 0; i < k; i++) {
                    int digit = (a[i] % (div * 10))/div;
                    if (digit > 0) {
                        moreDigit = true;
                    }
                    digitList[digit].add(a[i]);
                }
                int i = 0;
                for (ArrayList<Integer> lst : digitList) {
                    for (int num : lst) {
                        a[i] = num;
                        i++;
                    }
                    lst.clear();
                }
                div *= 10;
            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
