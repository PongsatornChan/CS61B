package arrays;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;
import static org.junit.Assert.*;

/** Just another simple test for hw 2 part C
 *  @author Pongsatorn Chanpanichravee
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void testCatenate() {
        int[] A = {1, 2, 3};
        int[] B = {4, 5, 6};
        int[] ab = {1, 2, 3, 4, 5, 6};
        assertArrayEquals(ab, Arrays.catenate(A, B));

        int[] empty = {};
        assertArrayEquals(B, Arrays.catenate(empty, B));
        assertArrayEquals(A, Arrays.catenate(A, empty));
    }

    @Test
    public void testRemove() {
        int[] A = {1, 2, 3, 4, 5};
        int[] expect = {1, 5};
        assertArrayEquals(expect, Arrays.remove(A, 1, 3));

        int[] expect1 = {1, 2};
        assertArrayEquals(expect1, Arrays.remove(A, 2, 4));

        assertArrayEquals(A, Arrays.remove(A, 2, 0));
        assertArrayEquals(A, Arrays.remove(A, 5, 2));
    }

    @Test
    public void testNaturalRuns() {
        int[] A = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] expected = {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertArrayEquals(expected, Arrays.naturalRuns(A));

        int[][] c = {{10}, {9}, {8}, {7}};
        int[] d = {10, 9, 8, 7};
        assertArrayEquals(c, Arrays.naturalRuns(d));

        int[][] e = {{7, 8, 9, 10}};
        int[] b = {7, 8, 9, 10};
        assertArrayEquals(e, Arrays.naturalRuns(b));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
