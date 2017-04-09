package enigma;
import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Shrey Malhotra
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return currentsetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        currentsetting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        currentsetting = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        start = _permutation.wrap(currentsetting + p );
        step = _permutation.permute(start);
        exit = _permutation.wrap(step - currentsetting);

        return exit;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        start = _permutation.wrap(currentsetting + e);
        step = _permutation.invert(start);
        exit = _permutation.wrap(step - currentsetting);

        return exit;
    }


    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemnted by this rotor in its 0 position. */
    public Permutation _permutation;

    public int currentsetting;

    public int start;
    public int exit;
    public int step;


}
