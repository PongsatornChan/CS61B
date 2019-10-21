package enigma;

import java.util.*;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _rotorSlot = new Rotor[numRotors];
        _numPawl = pawls;
        _rotors = new TreeMap<String, Rotor>(String.CASE_INSENSITIVE_ORDER);
        for ( Rotor rotor : allRotors) {
            _rotors.put(rotor.name(), rotor);
        }
        _plugboard = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _rotorSlot.length;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawl;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < _rotorSlot.length; i++) {
            _rotorSlot[i] = _rotors.get(rotors[i]);
            _rotorSlot[i].set(0);
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1 ) {
            throw new EnigmaException("Wrong initial setting");
        }
        for (int i = 1; i < numRotors(); i++ ) {
            _rotorSlot[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        moveRotors();
        int result = _plugboard.permute(c);
        for (int i = numRotors() - 1; i >= 0; i--) {
            result = _rotorSlot[i].convertForward(result);
        }
        for (int i = 1; i < numRotors(); i++) {
            result = _rotorSlot[i].convertBackward(result);
        }
        result = _plugboard.permute(result);
        return result;
    }

    /** Advance all the rotor if possible
     * @return number of rotors move
     */
    int moveRotors() {
        int numMove = 0;
        int firstMoving = numRotors() - numPawls();
        for (int i = firstMoving; i < numRotors(); i++) {
            if (i == numRotors() - 1) {
                _rotorSlot[i].advance();
                numMove++;
            } else if (_rotorSlot[i + 1].atNotch()) {
                _rotorSlot[i].advance();
                numMove++;
            } else if (_rotorSlot[i].atNotch() && i != firstMoving) {
                _rotorSlot[i].advance();
                numMove++;
            }
        }
        return numMove;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        ArrayList<Character> result = new ArrayList<Character>();
        for (int i = 0; i < msg.length(); i++) {
            if (_alphabet.contains(msg.charAt(i))) {
                int c = _alphabet.toInt(msg.charAt(i));
                c = convert(c);
                if (result.size() % 6 == 5) {
                    result.add(' ');
                }
                result.add(_alphabet.toChar(c));
            }
        }
        char[] charList = new char[result.size()];
        for (int i = 0; i < result.size(); i++) {
            charList[i] = result.get(i);
        }
        return new String(charList);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
    private Rotor[] _rotorSlot;

    private final int _numPawl;

    private final Map<String, Rotor> _rotors;

    private Permutation _plugboard;
}
