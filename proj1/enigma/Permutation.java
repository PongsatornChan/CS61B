package enigma;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

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
        if (cyclePattern.matcher(cycles).matches()) {
            Scanner scan = new Scanner(cycles);
            String cycle = scan.findWithinHorizon("([^\\*\\(\\)\\s])+", 0);
            while (cycle != null) {
                translateSets.add(cycle);
                cycle = scan.findWithinHorizon("([^\\*\\(\\)\\s])+", 0);
            }
        } else {
            throw new EnigmaException("Cycle is in wrong format.");
        }
    }

    /** reuse as a function to handle multiple line input
     *  CYCLES is in form "(cccc) (cc) ...".
     */
    void addCycle(String cycles) {
        if (cyclePattern.matcher(cycles).matches()) {
            Scanner scan = new Scanner(cycles);
            String cycle = scan.findWithinHorizon("([^\\*\\(\\)\\s])+", 0);
            while (cycle != null) {
                translateSets.add(cycle);
                cycle = scan.findWithinHorizon("([^\\*\\(\\)\\s])+", 0);
            }
        } else {
            throw new EnigmaException("Cycle is in wrong format.");
        }
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
            if (indexChar != -1) {
                if (indexChar + 1 == aSet.length()) {
                    outChar = aSet.charAt(0);
                } else {
                    outChar = aSet.charAt(indexChar + 1);
                }
                return outChar;
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char outChar;
        for (String aSet : translateSets) {
            int indexChar = aSet.indexOf(c);
            if (indexChar != -1) {
                if (indexChar == 0) {
                    outChar = aSet.charAt(aSet.length() - 1);
                } else {
                    outChar = aSet.charAt(indexChar - 1);
                }
                return outChar;
            }
        }
        return c;
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

    /**
     * Return translateSets.
     */
    ArrayList<String> getTranslateSets() {
        return translateSets;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** ArrayList that keeps all cycles. */
    private ArrayList<String> translateSets;

    /** Pattern to search and check for cycles. */
    private Pattern cyclePattern =
            Pattern.compile("([(]([^\\*\\(\\)\\s])+[)]\\s*)*");
}
