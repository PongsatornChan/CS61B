package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Pongsatorn
 */

public class MachineTest {

    @Test
    public void checkMoveRotors() {
        Alphabet alpha = new Alphabet();
        Rotor[] allRotors = new Rotor[5];

        Permutation perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alpha );
        Rotor rotorI = new MovingRotor("I", perm, "Q");

        // Permutation permII = new Permutation();
        allRotors[0] = rotorI;

        // Machine enigma = new Machine(alpha, 5, 3, );
    }
}
