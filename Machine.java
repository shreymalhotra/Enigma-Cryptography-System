package enigma;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Shrey Malhotra
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        rotorslots = numRotors;
        AllEnigmaRotors = new Rotor[rotorslots];
        currpawls = pawls;
        AvailRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return rotorslots;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return currpawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (AllEnigmaRotors.length > rotorslots) {
            throw new Error("Rotors Exceed Requirement");
        }
        for (int k = 0; k < rotors.length; k +=1) {
            for (Rotor m: AvailRotors) {
                if (rotors[k].equalsIgnoreCase(m.name())) {
                    AllEnigmaRotors[k] = m;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int k = 1; k <= setting.length(); k += 1) {
            char r = setting.charAt(k - 1);
            AllEnigmaRotors[k].set(_alphabet.toInt(r));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        thePlugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int rotormin1 = rotorslots -1;
        int endresult = c;
        boolean [] rotorstep = new boolean[rotorslots];
        AllEnigmaRotors[rotorslots -1].advance();
        int i = rotormin1;
        while (i > 1) {
            if (AllEnigmaRotors[i].atNotch()) {
                rotorstep[i] = true;
                rotorstep[i - 1] = true;
                i -= 1;
            }
        }
        int k = 0;
        while (k < rotormin1){
            if (rotorstep == null) break;
            if (rotorstep[k]) AllEnigmaRotors[k].advance();
            k += 1;
        }

        while (thePlugboard != null){
            endresult = thePlugboard.permute(endresult);
        }
        for  (int m = rotormin1; m >= 0; m -= 1) {
            endresult = AllEnigmaRotors[m].convertForward(endresult);
        }
        for (int m = 1; m < rotorslots; m +=1) {
            endresult = AllEnigmaRotors[m].convertBackward(endresult);
        }
        return endresult;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String endresult = "";
        char[] mc = msg.toCharArray();
        for (int k : mc) {
            endresult += _alphabet.toChar(convert(mc[k] - 65));
        }
        return endresult;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;


    /** PLUGBOARD. */

    private Permutation thePlugboard;

    /** ALL ROTORS. */

    private Rotor[] AllEnigmaRotors;

    /** ALL AVAILABLE ROTORS. */

    private Collection<Rotor> AvailRotors;

    /** THE AVAILABLE PAWLS. */

    private final int currpawls;

    /** AVAILABLE ROTOR SLOTS. */

    private final int rotorslots;

}
