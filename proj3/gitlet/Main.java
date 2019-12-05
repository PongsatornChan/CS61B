package gitlet;

import java.io.File;
import java.util.HashMap;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Pongsatorn Chanpanichravee
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");
    /** Main metadata folder. */
    static final File MAIN_FOLDER = Utils.join(CWD, ".gitlet");
    /** add stage folder. */
    static final File STAGE_ADD = Utils.join(MAIN_FOLDER, "stage_add");
    /** remove stage folder. */
    static final File STAGE_RM = Utils.join(MAIN_FOLDER, "stage_rm");
    /** branches folder. */
    static final File BRANCHES_FOLDER = Utils.join(MAIN_FOLDER, "refs", "branches");
    /** tags folder. */
    static final File TAGS_FOLDER = Utils.join(MAIN_FOLDER, "refs", "tags");

    /** head pointer. */
    static String header;

    /** master pointer. */
    static String branchName;

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }

        switch (args[0]) {
            case "init":
                doInit(args);
                break;
            case "add":
                doAdd(args);
                break;
            case "commit":
                doCommit(args);
                break;
            case "rm":
                break;
            case "log":
                break;
            case "global-log":
                break;
            case "find":
                break;
            case "status":
                break;
            case "checkout":
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            default:
                exitWithError("No command with that name exists.");
        }
        return;
    }



    /**
     * Prints out MESSAGE and exits with error code 0.
     * @param message message to print
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }

    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

    /**
     * Saves a ref to a file for future use.
     *
     * @param targetedDir location to save this ref
     * @param name name of the file
     * @param ref  what this reference points to
     */
    public static void saveRef(File targetedDir, String name, String ref) {
        Utils.writeObject(Utils.join(targetedDir, name), ref);
    }

    /**
     * init gitlet in the current directory (proj3 by default)
     * @param args
     */
    public static void doInit(String[] args) {
        validateNumArgs(args, 1);
        if (MAIN_FOLDER.exists()) {
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        }
        MAIN_FOLDER.mkdir();
        Blob.BLOBS_FOLDER.mkdir();
        Commit.COMMIT_FOLDER.mkdir();
        STAGE_ADD.mkdir();
        STAGE_RM.mkdir();
        BRANCHES_FOLDER.mkdirs();
        TAGS_FOLDER.mkdirs();

        header = Commit.FIRST_COMMIT.getName();
        branchName = "master";
        saveRef(MAIN_FOLDER, "head", header);
        saveRef(BRANCHES_FOLDER, branchName, header);

        Commit.FIRST_COMMIT.saveCommit();
    }

    public static void doAdd(String[] args) {
        validateNumArgs(args, 2);
        File toAdd = Utils.join(CWD, args[1]);
        String filename = args[1];
        if (!toAdd.exists()) {
            exitWithError("File does not exist.");
        } else if (!MAIN_FOLDER.exists()) {
            exitWithError("Not in an initialized Gitlet directory.");
        }

        header = Utils.readObject(Utils.join(MAIN_FOLDER, "head"), String.class);
        Blob toAddB = new Blob(args[1]);
        String hashNameToAdd = toAddB.getHashName();
        Commit currCommit = Commit.fromFile(header);

        String hashBlob = currCommit.getHashBlobs().get(filename);
        if (hashBlob != null && hashBlob.equals(hashNameToAdd)) {
            File[] fileLst = STAGE_ADD.listFiles();
            for (File x : fileLst) {
                if (x.getName().equals(toAdd.getName())) {
                    x.delete();
                }
            }
        } else {
            Utils.writeObject(Utils.join(STAGE_ADD, toAddB.getFilename()), toAddB);
        }
    }

    public static void doCommit(String[] args) {
        validateNumArgs(args, 2);
        String commitMsg = args[1];

        header = Utils.readObject(Utils.join(MAIN_FOLDER, "head"), String.class);
        Commit parentCommit = Commit.fromFile(header);
        HashMap<String, String> newHashBlobs = new HashMap<String, String>();
        newHashBlobs.putAll(parentCommit.getHashBlobs());

        for(File x : STAGE_ADD.listFiles()) {
            Blob theBlob = Utils.readObject(x, Blob.class);
            newHashBlobs.put(x.getName(), theBlob.getHashName());
            theBlob.saveBlob();
            x.delete();
        }

        Commit thisCommit = new Commit(System.currentTimeMillis(), commitMsg
                , newHashBlobs, parentCommit.getName());

        header = thisCommit.getName();
        branchName = "master";
        saveRef(MAIN_FOLDER, "head", header);
        saveRef(BRANCHES_FOLDER, branchName, header);

        thisCommit.saveCommit();
    }

    public static void doLog(String[] args) {
        validateNumArgs(args, 1);

    }
}
