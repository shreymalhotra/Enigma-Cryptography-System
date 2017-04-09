package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Shrey Malhotra
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _cycles = cycles;
        _alphabet = alphabet;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        while (!cycle.equals("")){
          _cycles += "(" + cycle + ")";
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
      char CharacterIterator = permute(_alphabet.toChar(p));
      return _alphabet.toInt(CharacterIterator);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
      char CharacterIterator = (char) invert(_alphabet.toChar(c));
      return _alphabet.toInt(CharacterIterator);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (cyclelen() == 0) {
            return p;
        }
        char curr = _cycles.charAt(0);
        for (int k = 0; k < cyclelen(); k += 1) {
            if (_cycles.charAt(k) == '(') {
                curr = _cycles.charAt(k + 1);
            }
            if (_cycles.charAt(k) == p) {
                if (_cycles.charAt(k + 1) == ')') {
                    return curr;
                }
                return _cycles.charAt(k + 1);
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        if (cyclelen() == 0) {
            return c;
        }
        char curr = _cycles.charAt(0);
        for (int k = cyclelen() - 1; k >= 0; k -= 1) {
            if (_cycles.charAt(k) == c) {
                if (_cycles.charAt(k - 1) == '(') {
                    return curr;
                }
                return _cycles.charAt(k - 1);}
            if (_cycles.charAt(k) == ')') {
                curr = _cycles.charAt(k - 1);
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
        for (int k = 0; k < alphabetsize(); k +=1) {
            char x = alphabet().toChar(k);
            if (x == permute(x)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    private String _cycles;

    private int cyclelen() {
        return _cycles.length();
    }

    private int alphabetsize() {
        return _alphabet.size();
    }

}
