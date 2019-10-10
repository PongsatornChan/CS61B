package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Pongsatorn Chanpanichravee
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String inNotches) {
        super(name, perm);
        rightRotor = null;
        this.notches = new ArrayList<>();
        for (int i = 0; i < inNotches.length(); i++) {
            this.notches.add(perm.alphabet().toInt(inNotches.charAt(i)));
        }
    }

    MovingRotor(String name, Permutation perm, String inNotches, Rotor right) {
        super(name, perm);
        rightRotor = right;
        this.notches = new ArrayList<>();
        for (int i = 0; i < inNotches.length(); i++) {
            this.notches.add(perm.alphabet().toInt(inNotches.charAt(i)));
        }
    }


    @Override
    void advance() {
        if (this.rotates()) {
            if (rightRotor != null) {
                if (rightRotor.atNotch()) {
                    set((setting() + 1) % size());
                }
            } else {
                set((setting() + 1) % size());
            }
        }
    }

    @Override
    boolean rotates() { return true; }

    @Override
    boolean atNotch() {
        for (int n : notches) {
            if (n == setting()) {
                return true;
            }
        }
        return false;
    }

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
    private ArrayList<Integer> notches;
    Rotor rightRotor;
}