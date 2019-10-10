package enigma;

import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Pongsatorn Chanpanichravee
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        list = new ArrayList<>();
        if (chars.length() < 1) {
            throw new EnigmaException("Alphabet: bad argument");
        }
        for (int i = 0; i < chars.length(); i++) {
            list.add(chars.charAt(i));
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return list.size();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return list.contains(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return list.get(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!list.contains(ch)) {
            throw new EnigmaException("Character is not in alphabet list");
        }
        return list.indexOf(ch);
    }

    ArrayList<Character> list;
}
