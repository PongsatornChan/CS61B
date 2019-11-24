package capers;

import java.io.File;

public class Main {
    static final File MAIN_FOLDER = new File(".capers");

    /**
     * Runs one of three comands:
     * story [text] -- writes "text" to a story file in the .capers directory
     * dog [name] [breed] [age] -- creates a dog with the specified parameters
     * birthday [name] -- advances a dog's age and prints out a celebratory message
     * 
     * All persistent data should be stored in a ".capers" 
     * directory in the current working directory.
     * 
     * Recommended structure (you do not have to follow):
     * .capers/ -- top level folder for all persistent data
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     * 
     * Useful Util functions (as a start, you will likely need to use more):
     * writeContents - writes out strings/byte arrays to a file
     * readContentsAsString - reads in a file as a string
     * readContents - reads ina a file as a byte array
     * writeObject - writes a serializable object to a file
     * readObject - reads in a serializable object from a file
     * join - joins together strings or files into a path 
     *     (eg. Utils.join(".capers", "dogs") would give you a File object 
     *      with the path of ".capers/dogs")
     * @param args arguments from the command line
     */
    public static void main(String[] args) {
        
    }
}
