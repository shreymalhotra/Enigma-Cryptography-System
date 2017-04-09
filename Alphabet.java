package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Shrey Malhotra
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        characters = chars;
    }

    /** Returns the size of the alphabet. */
    int size() {
        return characters.length();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return (characters.charAt(c) > 0);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return characters.charAt(index);
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        return characters.indexOf(c);
    }

    private String characters;
}
