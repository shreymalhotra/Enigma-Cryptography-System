package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Shrey Malhotra
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine Enigma = readConfig();
        String settings = _input.nextLine().substring(1).trim();
        String input = _input.nextLine().trim();
        String output = Enigma.convert(input);
        setUp(Enigma, settings);

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new UpperCaseAlphabet();
            String alphabet = _config.nextLine();
            rotorslots = _config.nextInt();
            currpawls = _config.nextInt();
            Collection<Rotor> AllEnigmaRotors = new ArrayList<Rotor>();
            while (_config.hasNextLine()) {
                AllEnigmaRotors.add(readRotor());
            }
            return new Machine(_alphabet, rotorslots, currpawls,
                    AllEnigmaRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        Rotor newRotor = null;
        String process = _config.nextLine().trim();
        Permutation permutation = new Permutation(process, _alphabet);
        try {
            if (_config.next().charAt(0) == 'R') {
                process += " " + _config.nextLine().trim();
                newRotor = new Reflector(_config.next(), permutation);
            } else if (_config.next().charAt(0) == 'N') {
                newRotor = new FixedRotor(_config.next(), permutation);
            } else if (_config.next().charAt(0) == 'M') {
                newRotor = new MovingRotor(_config.next(),
                        permutation, _config.next().substring(1));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return newRotor;
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        if (M == null) {
            throw new Error("Rotors Exceed Slots");
        }
        Scanner scanner = new Scanner(settings);
        String[] RotorSettings = new String [rotorslots];
        int k = 0;
        while (k < rotorslots) {
            RotorSettings[k] = scanner.next();
            System.out.println(RotorSettings[k]);
            k += 1;
        }
        if (_config.hasNextLine()) {
            String plugboardSetting = scanner.nextLine().trim();
            Permutation permutation = new Permutation(plugboardSetting, _alphabet);
            M.setPlugboard(permutation);
        }

        M.insertRotors(RotorSettings);
        M.setRotors(scanner.next());

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() == 0) {
            System.out.println();
        }
        int k = 0;
        for (char c : msg.toCharArray()) {
            if (k == 5) {
                System.out.print(" ");
                k = 0;}
            k += 1;
            System.out.print(c);
        }
        System.out.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** THE AVAILABLE PAWLS. */

    private int currpawls;

    /** AVAILABLE ROTOR SLOTS. */

    private int rotorslots;

    /** A SCANNER. */

    private  Scanner scanner;
}

