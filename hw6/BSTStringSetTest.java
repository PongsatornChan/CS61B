import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Pongsatorn Chanpanichravee
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testContain() {
        BSTStringSet t = new BSTStringSet();
        t.put("B");
        t.put("A");
        t.put("C");
        t.put("F");
        t.put("A");
        t.put("E");

        assertEquals(true, t.contains("E"));
        assertEquals(false, t.contains("Z"));
    }

    @Test
    public void testAsList() {
        BSTStringSet t = new BSTStringSet();
        t.put("B");
        t.put("A");
        t.put("C");
        t.put("F");
        t.put("A");
        t.put("E");

        List<String> lst = t.asList();
        String[] strLst = {"A", "B", "C", "E", "F"};
        for (int i = 0; i < strLst.length; i++) {
            assertEquals(strLst[i], lst.get(i));
        }
    }
}
