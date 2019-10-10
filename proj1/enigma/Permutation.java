package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Pongsatorn Chanpanichravee
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        translateSets = new ArrayList<>();
        String [] sets = cycles.split(" ");
        for (int i = 0; i < sets.length ; i++) {
            sets[i] = sets[i].replace('(', ' ');
            sets[i] = sets[i].replace(')', ' ');
            sets[i] = sets[i].trim();
            translateSets.add(sets[i]);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        translateSets.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char inChar = _alphabet.toChar(p);
        char outChar = permute(inChar);
        return _alphabet.toInt(outChar);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char inChar = _alphabet.toChar(c);
        char outChar = invert(inChar);
        return _alphabet.toInt(outChar);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char outChar;
        for (String aSet : translateSets) {
            int indexChar = aSet.indexOf(p);
            if (indexChar != -1) { // if in this cycle
                if (indexChar + 1 == aSet.length()) { // if it is at the end of cycle
                    outChar = aSet.charAt(0);
                } else {
                    outChar = aSet.charAt(indexChar + 1);
                }
                return outChar;
            }
        }
        return p; // if not in any cycle
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char outChar;
        for (String aSet : translateSets) {
            int indexChar = aSet.indexOf(c);
            if (indexChar != -1) { // if in this cycle
                if (indexChar == 0) { // if it is at the start of cycle
                    outChar = aSet.charAt(aSet.length() - 1);
                } else {
                    outChar = aSet.charAt(indexChar - 1);
                }
                return outChar;
            }
        }
        return c; // if not in any cycle
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int sum = 0;
        for (String aSet : translateSets) {
            sum += aSet.length();
        }
        return sum >= _alphabet.size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    ArrayList<String> translateSets;

}
