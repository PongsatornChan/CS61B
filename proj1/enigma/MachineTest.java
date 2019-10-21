package enigma;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Pongsatorn
 */

public class MachineTest {
    Pattern cyclePattern = Pattern.compile("([(][A-Z]+[)] *)*");
    Pattern rotorPattern = Pattern.compile(
            "([A-Z][a-zA-Z]*) ([MNR][A-Z]*)( *[(][A-Z]+[)])+");
    Pattern settingPattern = Pattern.compile(" ([0-9]+) ([0-9]+)");

    /**
     * Make rotors according to default.conf
     * put then in ArrayList
     * @return ArrayList<Rotor> all Rotor from default.conf
     */
    ArrayList<Rotor> makeRotors() {
        ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File("testing/correct/default.conf"));
        } catch (FileNotFoundException e) {
            System.out.println("Can't open the default.conf");
            scanner = new Scanner(System.in);
        }
        String buffer = scanner.nextLine();
        Alphabet alpha = new Alphabet(buffer);
        int numRotor = 0;
        int numPawl = 0;
        buffer = scanner.nextLine();
        if (settingPattern.matcher(buffer).matches()) {
            String[] strList = buffer.trim().split(" ");
            if (strList.length == 2) {
                numRotor = Integer.parseInt(strList[0]);
                numPawl = Integer.parseInt(strList[1]);
            }
        } else {
            throw new EnigmaException("Wrong format: " + buffer);
        }
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            input = input.trim();
            if (rotorPattern.matcher(input).matches()) {
                String[] rotorConfig = input.split(" +", 3);
                String nameRotor = rotorConfig[0];
                String notches = rotorConfig[1];
                String cycles = rotorConfig[2];
                Permutation perm;
                if (cyclePattern.matcher(cycles).matches()) {
                    perm = new Permutation(cycles, alpha);
                } else {
                    throw new EnigmaException("Wrong cycle format: " + cycles);
                }
                Rotor rotor;
                if (notches.charAt(0) == 'M') {
                    rotor = new MovingRotor(nameRotor,
                            perm, notches.substring(1));
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
    }

    @Test
    public void checkMoveRotors() {
        ArrayList<Rotor> allRotors;
        try {
            allRotors = makeRotors();
            Machine enigma =
                    new Machine(allRotors.get(0).alphabet(), 5, 3, allRotors);
            String[] rotorNames = {"B", "Beta", "III", "IV", "I"};
            enigma.insertRotors(rotorNames);
            enigma.setRotors("AXLE");
            Permutation plugboard = new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                    allRotors.get(0).alphabet());
            enigma.setPlugboard(plugboard);

            assertEquals("QVPQ", enigma.convert("FROM"));
            enigma.setRotors("AXLE");
            assertEquals("QVPQS OKOIL PUBKJ ZPISF XDW",
                    enigma.convert("FROM HIS SHOULDER HIAWATHA"));
        } catch (EnigmaException e) {
            System.out.println(e.getMessage());
        }
    }
}
