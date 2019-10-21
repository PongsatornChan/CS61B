package enigma;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
 */
public final class Main {

    Pattern alphaPattern = Pattern.compile("\\s*([^\\*\\(\\)\\s])*\\s*");
    Pattern settingPattern = Pattern.compile("\\s*([0-9]+)(\\s*[0-9]+)");
    Pattern cyclePattern = Pattern.compile("([(][A-Z]+[)]\\s*)*");
    Pattern rotorPattern = Pattern.compile("([A-Z][a-zA-Z]*)(\\s*[MNR][A-Z]*)(\\s*[(][A-Z]+[)])+");

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
        Machine enigma = readConfig();

        while (_input.hasNextLine()) {
            String input = _input.nextLine();
            if (input.charAt(0) == '*') {
                setUp(enigma, input);
            } else {
                String output = enigma.convert(input.toUpperCase());
                printMessageLine(output);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            Scanner scanner = _config;
            String buffer = scanner.findWithinHorizon(alphaPattern, 0);
            if (buffer != null) {
                _alphabet = new Alphabet(buffer);
            } else {
                throw new EnigmaException("Alphabet is not found.");
            }

            int numRotor = 0;
            int numPawl = 0;
            buffer = null;
            buffer = scanner.findWithinHorizon(settingPattern, 0);
            if (buffer != null) {
                String[] strList = buffer.trim().split(" ");
                numRotor = Integer.parseInt(strList[0]);
                numPawl = Integer.parseInt(strList[1]);
                if (numRotor <= numPawl) {
                    throw new EnigmaException("Number of rotors must be more than Pawls.");
                }
            } else {
                throw new EnigmaException("Wrong format for number of Rotors and Pawl");
            }
            ArrayList<Rotor> allRotors = readRotor();
            Machine enigma = new Machine(_alphabet, numRotor, numPawl, allRotors);
            return enigma;
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private ArrayList<Rotor> readRotor() {
        try {
            Scanner scanner = _config;
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (scanner.hasNextLine()) {
                String input = scanner.findWithinHorizon(rotorPattern, 0);
                input = input.trim();
                if (rotorPattern.matcher(input).matches()) {
                    String[] rotorConfig = input.split(" +", 3);
                    String nameRotor = rotorConfig[0];
                    String notches = rotorConfig[1];
                    String cycles = rotorConfig[2];
                    Permutation perm;
                    if (cyclePattern.matcher(cycles).matches()) {
                        perm = new Permutation(cycles, _alphabet);
                    } else {
                        throw new EnigmaException("Wrong cycle format: " + cycles);
                    }
                    Rotor rotor;
                    if (notches.charAt(0) == 'M') {
                        rotor = new MovingRotor(nameRotor, perm, notches.substring(1));
                    } else if (notches.charAt(0) == 'N') {
                        rotor = new Rotor(nameRotor, perm);
                    } else if (notches.charAt(0) == 'R') {
                        rotor = new Reflector(nameRotor, perm);
                    } else {
                        throw new EnigmaException("Wrong notch format: " + notches);
                    }
                    allRotors.add(rotor);
                } else if (cyclePattern.matcher(input).matches()) {
                    Rotor prevRotor = allRotors.get(allRotors.size() - 1);
                    Permutation perm = prevRotor.permutation();
                    perm.addCycle(input);
                } else {
                    throw new EnigmaException("Wrong format: " + input);
                }
            }
            return allRotors;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] strList = settings.split(" +");
        String[] names = new String[M.numRotors()];
        for (int i = 1; i <= names.length; i++) {
            names[i - 1] = strList[i];
        }
        M.insertRotors(names);
        M.setRotors(strList[M.numRotors() + 1]);

        strList = settings.split(" [(]", 2);
        if (strList.length == 2) {
            strList[1] = "(" + strList[1];
            M.setPlugboard(new Permutation(strList[1], _alphabet));
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        _output.println(msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
