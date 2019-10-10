package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static enigma.TestUtils.*;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Rotor class.
 *  @author Pongsatorn
 */
public class RotorTest {

    @Rule
    public ExpectedException exceptionE = ExpectedException.none();

    @Test
    public void checkConvertForward() {
        Alphabet a = new Alphabet();
        Permutation perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", a );
        Rotor rotorI = new Rotor("I", perm);
        char output = a.toChar(rotorI.convertForward(0));
        assertEquals('E', output);

        rotorI.set('E');
        output = a.toChar(rotorI.convertForward(0));
        assertEquals('H', output);

        output = a.toChar(rotorI.convertForward(22));
        assertEquals('A', output);

        output = a.toChar(rotorI.convertForward(16));
        assertEquals('W', output);

    }

    @Test
    public void checkConvertBackward() {
        Alphabet a = new Alphabet();
        Permutation perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", a );
        Rotor rotorI = new Rotor("I", perm);

        rotorI.set('E');
        char output = a.toChar(rotorI.convertBackward(7));
        assertEquals('A', output);
    }
}

