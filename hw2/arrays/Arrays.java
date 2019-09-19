package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Pongsatorn Chanpanichravee
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int num = A.length + B.length;
        int[] ab = new int[num];
        for (int i = 0; i < num; i++) {
            if (i < A.length) {
                ab[i] = A[i];
            } else {
                ab[i] = B[i - A.length];
            }
        }
        return ab;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        if (start >= A.length) {
            return A;
        }
        int num;
        if (A.length < start + len) {
            num = start;
        } else {
            num = A.length - len;
        }
        int[] result = new int[num];
        for (int i = 0; i < result.length; i++) {
            if (i < start) {
                result[i] = A[i];
            } else {
                result[i] = A[i + len];
            }
        }
        return result;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A.length == 0) {
            return new int[][] {{}};
        }
        int num = 1;
        int highest = A[0];
        for (int i = 1; i < A.length; i++) {
            if (A[i] <= highest) {
                num++;
            }
            highest = A[i];
        }

        int[][] result = new int[num][];
        highest = A[0];
        int startPos = 0;
        int resultPos = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i] <= highest) {
                int[] sub = new int[i - startPos];
                System.arraycopy(A, startPos, sub, 0, sub.length);
                result[resultPos] = sub;
                resultPos++;
                startPos = i;
            }
            highest = A[i];
        }
        int[] sub = new int[A.length - startPos];
        System.arraycopy(A, startPos, sub, 0, sub.length);
        result[resultPos] = sub;
        return result;
    }
}
