package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Commit implements Serializable {

    public static final Commit FIRST_COMMIT =
            new Commit(System.currentTimeMillis() / 1000L, "initial commit",
                    new HashMap<String, String>(), null );

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

    public Commit(long date, String message, HashMap<String, String> hashBlobs, String hashParentCommit1) {
        this.date = date;
        this.message = message;
        this.hashBlobs = hashBlobs;
        this.hashParentCommit1 = hashParentCommit1;
        this.name = Utils.sha1(date, message, hashBlobs, hashParentCommit1);
    }

    /**
     * Reads in and deserializes a blob from a file with hashName NAME in COMMIT_FOLDER.
     *
     * @param name SHA1-Name of commit to load
     * @return commit read from file
     */
    public static Commit fromFile(String name) {
        File commitFile = Utils.join(COMMIT_FOLDER, name);
        if (!commitFile.exists()) {
            throw new IllegalArgumentException(
                    "No commit with that name found ;(");
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Saves a blob to a file for future use.
     */
    public void saveCommit() {
        Utils.writeObject(Utils.join(COMMIT_FOLDER, this.name), this);
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, String> getHashBlobs() {
        return hashBlobs;
    }

}
