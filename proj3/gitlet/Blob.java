package gitlet;

import java.io.Serializable;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;

/**
 * Hung Tran helped with Blob concept
 */

public class Blob implements Serializable{

    /** Folder that commits live in. */
    static final File BLOBS_FOLDER = Utils.join(Main.MAIN_FOLDER, "blobs");

    private String filename;

    private String content;

    private String hashName;

    /**
     * make a blob object out of filename
     * assume filename is the name of file that exits
     * @param filename
     */
    public Blob(String filename) {
        this.filename = filename;

        content = Utils.readContentsAsString(new File(filename));

        this.hashName = Utils.sha1(content, filename);
    }

    /**
     * Reads in and deserializes a blob from a file with hashName NAME in BLOBS_FOLDER.
     *
     * @param name SHA1-Name of blob to load
     * @return Blob read from file
     */
    public static Blob fromFile(String name) {
        File blobFile = Utils.join(BLOBS_FOLDER, name);
        if (!blobFile.exists()) {
            throw new IllegalArgumentException(
                    "No blob with that name found ;(");
        }
        return Utils.readObject(blobFile, Blob.class);
    }

    /**
     * Saves a blob to a file for future use.
     */
    public void saveBlob() {
        Utils.writeObject(Utils.join(BLOBS_FOLDER, this.hashName), this);
    }

    public String getHashName() {
        return this.hashName;
    }

    public String getFilename() {
        return this.filename;
    }

}
