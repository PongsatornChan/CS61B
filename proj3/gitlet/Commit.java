package gitlet;

import java.io.Serializable;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Commit implements Serializable {

    public static final Commit FIRST_COMMIT =
            new Commit(0, "initial commit",
                    new HashMap<String, String>(), "" );

    /** Folder that commits live in. */
    static final File COMMIT_FOLDER = Utils.join(Main.MAIN_FOLDER, "commits");

    private String name;

    private long date;

    private String message;

    /** keys are the name of the files this commit is tracking.
     *  the values are hash name of specific Blob
     * */
    private HashMap<String, String> hashBlobs;
    private List<Blob> blobs;

    private String hashParentCommit1;
    private Commit parentCommit1;

    private String hashParentCommit2;

    public Commit(long date, String message, HashMap<String, String> hashBlobs, String hashParentCommit1) {
        this.date = date;
        this.message = message;
        this.hashBlobs = hashBlobs;
        this.hashParentCommit1 = hashParentCommit1;
        this.hashParentCommit2 = "";
        this.name = Utils.sha1(new Date(date).toString(), message, hashBlobs.toString(), hashParentCommit1);
    }

    public Commit(long date, String message, HashMap<String, String> hashBlobs,
                  String hashParentCommit1, String hashParentCommit2) {
        this.date = date;
        this.message = message;
        this.hashBlobs = hashBlobs;
        this.hashParentCommit1 = hashParentCommit1;
        this.hashParentCommit2 = hashParentCommit2;
        this.name = Utils.sha1(new Date(date).toString(), message, hashBlobs.toString(),
                hashParentCommit1, hashParentCommit2);
    }

    /**
     * Reads in and deserializes a commit from a file with hashName NAME in COMMIT_FOLDER.
     *
     * @param name SHA1-Name of commit to load
     * @return commit read from file
     */
    public static Commit fromFile(String name) {
        File commitFile = Utils.join(COMMIT_FOLDER, name);
        if (!commitFile.exists()) {
            System.out.print("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Saves a blob to a file for future use.
     */
    public void saveCommit() {
        Utils.writeObject(Utils.join(COMMIT_FOLDER, this.name), this);
    }

    public String logMsg() {
        String log = "===\n";
        log += "commit " + this.name + "\n";
        log += "Date: ";

        Date date = new Date(this.date);
        SimpleDateFormat format = new SimpleDateFormat("Z");
        String lst = date.toString().replaceFirst(" PST ", " ");
        lst += " " + format.format(date);

        log += lst + "\n";
        if (!hashParentCommit2.equals("")) {
            log += "Merge: " + hashParentCommit1.substring(0, 7);
            log += hashParentCommit2.substring(0, 7) + "\n";
        }
        log += this.message + "\n\n";
        return log;
    }

    /**
     * helper function that return the blob with FILENAME.
     *
     * @param filename name (not sha1) of file to be return as blob
     * @return the blob or null if this commit does not contain file with FILENAME
     */
    public Blob getBlobFromFile(String filename) {
        if (!hashBlobs.containsKey(filename)) {
            return null;
        } else {
            return Blob.fromFile(hashBlobs.get(filename));
        }
    }

    /**
     * check if a file with name FILENAME is tracked at this commit.
     *
     * @param filename name of file (not sha1) to be checked
     * @return true if tracked, false otherwise
     */
    public boolean isTracked(String filename) {
        return hashBlobs.containsKey(filename);
    }

    /**
     *
     * @return set of filename that this commit tracked
     */
    public Set<String> getAllFile() {
        return hashBlobs.keySet();
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, String> getHashBlobs() {
        return hashBlobs;
    }

    public String getHashParentCommit1() {
        return this.hashParentCommit1;
    }

}
