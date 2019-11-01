import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test of a BST-based String Set.
 * @author Pongsatorn Chanpancihravee
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    private static void initWords(long seed, int N) {
        WORDS1.clear();
        for (int i = 0; i < N; i += 1) {
            WORDS1.add(StringUtils.randomString(4));
        }
        Collections.shuffle(WORDS1, new Random(seed));
    }

    @Test
    public void testContain() {
        ECHashStringSet t = new ECHashStringSet();
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
    public void testReHash() {
        initWords(4, 50);
        ECHashStringSet t = new ECHashStringSet();
        for (String i : WORDS1) {
            t.put(i);
        }
        for (String i : WORDS1) {
            assertEquals(true, t.contains(i));
        }
    }

    private static final ArrayList<String> WORDS1 = new ArrayList<>();

}
