package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Pongsatorn
 */
public class AlphabetTest {

    @Rule
    public ExpectedException exceptionE = ExpectedException.none();

    @Test
    public void checkContains() {
        Alphabet a = new Alphabet();
        assertTrue(a.contains('F'));
        assertFalse(a.contains('f'));

        Alphabet b = new Alphabet("ABCD12");
        assertTrue(b.contains('1'));
        assertFalse(b.contains('F'));

        Alphabet c = new Alphabet("ABCabc/\\{}");
        assertTrue(c.contains('{'));
        assertTrue(c.contains('/'));
        assertTrue(c.contains('\\'));

        exceptionE.expect(EnigmaException.class);
        c = new Alphabet("asdf(");
    }

    @Test
    public void checkToChar() {
        exceptionE.expect(EnigmaException.class);
        Alphabet a = new Alphabet();
        assertEquals('A', a.toChar(0));
        a.toChar(28);
    }

    @Test
    public void checkToInt() {
        Alphabet a = new Alphabet();
        assertEquals(1, a.toInt('B'));
        assertEquals(-1, a.toInt('b'));
    }
}
