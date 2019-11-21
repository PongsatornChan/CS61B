import org.junit.Test;
import static org.junit.Assert.*;

/** HW #7, Two-sum problem.
 * @author Pongsatorn Chanpanichravee
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        new MySortingAlgorithms.MergeSort().sort(A, A.length);
        new MySortingAlgorithms.MergeSort().sort(B, B.length);
        int i = 0;
        int j = B.length - 1;
        while(j != 0 && i != A.length - 1) {
            if (A[i] + B[j] == m) {
                return true;
            } else if (A[i] + B[j] < m) {
                i++;
            } else {
                j--;
            }
        }
        return false;
    }

    @Test
    public void sumToTest() {
        int[] a = {3, 2, 7, 9, 6, 4};
        int[] b = {4, 2, 5, 6, 8, 1};
        assertTrue(sumsTo(a, b, 5));
        assertFalse(sumsTo(a, b, 2));
    }
}
