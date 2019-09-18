package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 *  @author Pongsatorn Chanpanichravee
 */

public class ListsTest {
    /** Just a simply test for homework 2
     */

    @Test
    public void testNaturalRuns() {
        int[][] a = {{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        IntListList expected = IntListList.list(a);
        int[] b = {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        IntList input = IntList.list(b);
        assertEquals(expected, Lists.naturalRuns(input));

        int[][] c = {{10}, {9}, {8}, {7}};
        expected = IntListList.list(c);
        int[] d = {10, 9, 8, 7};
        input = IntList.list(d);
        assertEquals(expected, Lists.naturalRuns(input));

        expected = new IntListList(new IntList(), null);
        input = new IntList();
        assertEquals(expected, Lists.naturalRuns(input));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
