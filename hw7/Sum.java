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
        while(j != 0) {
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

}
