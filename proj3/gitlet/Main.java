package gitlet;

import java.io.*;
import java.nio.file.Files;
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
    /** file to keep all log */
    static final File LOG_FLIE = Utils.join(MAIN_FOLDER, "log");

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
                doRemove(args);
                break;
            case "log":
                doLog(args);
                break;
            case "global-log":
                doGlobalLog(args);
                break;
            case "find":
                break;
            case "status":
                break;
            case "checkout":
                doCheckout(args);
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
     * help get sha1 commit id that REFNAME refer to.
     *
     * @param refName name of branches or head
     * @return commit ID (sha1) that REFNAME refer to
     */
    public static String getRef(String refName) {
        if (refName.equalsIgnoreCase("head")) {
            return Utils.readContentsAsString(Utils.join(MAIN_FOLDER, "head"));
        } else {
            File ref = Utils.join(BRANCHES_FOLDER, refName);
            if (!ref.exists()) {
                return null;
            }
            return Utils.readContentsAsString(ref);
        }
    }

    /**
     * Helper function that write CONTENT into FILE.
     * Have options to append or override.
     *
     * file have to exist if should override
     *
     * @param file file to be writen
     * @param content message to be writen
     * @param isAppend true, append CONTENT at the end
     *                 false, overwrited file with CONTENT
     */
    public static void writeFile(File file, String content, boolean isAppend) {
        PrintWriter logFile = null;
        if (isAppend) {
            if (!file.exists()) {
                exitWithError(file.getName() + "does not exist.");
            }
            try {
                logFile = new PrintWriter(new FileWriter(LOG_FLIE, true));
            } catch (IOException e) {
                exitWithError(e.getMessage());
            }
        } else {
            try {
                file.createNewFile();
                logFile = new PrintWriter(new FileWriter(file, false));
            } catch (IOException e) {
                exitWithError(e.getMessage());
            }
        }
        logFile.print(content);
        logFile.close();
    }

    /**
     * Initial all the files and directories that are needed.
     */
    public static void initFileDir() {
        MAIN_FOLDER.mkdir(); Blob.BLOBS_FOLDER.mkdir();
        Commit.COMMIT_FOLDER.mkdir(); STAGE_ADD.mkdir();
        STAGE_RM.mkdir(); BRANCHES_FOLDER.mkdirs();
        TAGS_FOLDER.mkdirs();
    }

    /**
     * delete all files in DIR
     * keep the directory and subdirectory.
     * @param dir directory to be clean
     */
    public static void cleanDirectory(File dir) {
        if (!dir.isDirectory()) {
            return;
        }
        for(File file: dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
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
        initFileDir();

        header = Commit.FIRST_COMMIT.getName();
        branchName = "master";

        writeFile(Utils.join(MAIN_FOLDER, "head"), header, false);
        writeFile(Utils.join(BRANCHES_FOLDER, "master"), header, false);

        writeFile(LOG_FLIE, Commit.FIRST_COMMIT.logMsg(), false);
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

        header = getRef("head");
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
        // Commit from detact head still need to be devolop
        validateNumArgs(args, 2);
        String commitMsg = args[1];

        header = getRef("head");
        Commit parentCommit = Commit.fromFile(header);
        HashMap<String, String> newHashBlobs = new HashMap<String, String>();
        newHashBlobs.putAll(parentCommit.getHashBlobs());

        for(File x : STAGE_ADD.listFiles()) {
            Blob theBlob = Utils.readObject(x, Blob.class);
            newHashBlobs.put(x.getName(), theBlob.getHashName());
            theBlob.saveBlob();
            x.delete();
        }

        for(File x : STAGE_RM.listFiles()) {
            newHashBlobs.remove(x.getName());
            x.delete();
        }

        Commit thisCommit = new Commit(System.currentTimeMillis(), commitMsg
                , newHashBlobs, parentCommit.getName());

        header = thisCommit.getName();
        branchName = "master";

        writeFile(Utils.join(MAIN_FOLDER, "head"), header, false);
        writeFile(Utils.join(BRANCHES_FOLDER, branchName), header, false);

        writeFile(LOG_FLIE, thisCommit.logMsg(), true);
        thisCommit.saveCommit();
    }

    public static void doLog(String[] args) {
        validateNumArgs(args, 1);

        header = getRef("head");

        for (String curr = header; !curr.equals(""); ){
            Commit currCom = Commit.fromFile(curr);
            System.out.print(currCom.logMsg());
            curr = currCom.getHashParentCommit1();
        }
    }

    public static void doGlobalLog(String[] args) {
        validateNumArgs(args, 1);
        System.out.print(Utils.readContentsAsString(LOG_FLIE));
    }

    /**
     * Override file in the working directory with file FILENAME
     * from MY_COMMIT.
     *
     * @param myCommit a commit
     * @param filename the real filename (not sha1)
     */
    public static void checkoutHelper(Commit myCommit, String filename) {
        File myFile = Utils.join(CWD, filename);
        Blob myBlob = myCommit.getBlobFromFile(filename);
        if (myBlob == null) {
            exitWithError("File does not exist in that commit.");
        }
        writeFile(myFile, myBlob.getContent(), false);
    }

    /**
     * -- [file name] : (3) override FILE NAME in working dir with
     *  file from head commit.
     *
     * [commit id] -- [file name] : (4) override FILE NAME in
     *  working directory with file from COMMIT ID.
     *
     * [branch name] : (2) override all files in working directory
     *  and delete file that not in commit at BRANCH NAME.
     *
     * @param args
     */
    public static void doCheckout(String[] args) {
        if (args.length == 2) {
            header = getRef("head");
            String comId = getRef(args[1]);
            if (header.equals(comId)) {
                exitWithError("No need to checkout the current branch.");
            } else if (comId == null) {
                exitWithError("No such branch exists.");
            }
            Commit myCommit = Commit.fromFile(comId);
            for (File file : CWD.listFiles()) {
                if (file.isFile() && !myCommit.isTracked(file.getName())) {
                    exitWithError("There is an untracked file in the way; delete it or add it first.");
                }
            }
            cleanDirectory(CWD);
            cleanDirectory(STAGE_ADD);
            cleanDirectory(STAGE_RM);
            for (String filename : myCommit.getAllFile()) {
                checkoutHelper(myCommit, filename);
            }

        } else if (args.length == 3) {
            header = getRef("head");
            Commit myCommit = Commit.fromFile(header);
            checkoutHelper(myCommit, args[2]);
        } else if (args.length == 4) {
            Commit myCommit = Commit.fromFile(args[1]);
            checkoutHelper(myCommit, args[3]);
        } else {
            exitWithError("Incorrect operands.");
        }
    }

    public static void doRemove(String[] args) {
        validateNumArgs(args, 2);

        boolean isStage = true;
        String filename = args[1];
        File myFile = Utils.join(STAGE_ADD, filename);
        if (myFile.exists()) {
            myFile.delete();
        } else {
            isStage = false;
        }

        header = getRef("head");
        Commit currCommit = Commit.fromFile(header);
        if (currCommit.isTracked(filename)) {
            writeFile(Utils.join(STAGE_RM, filename), "", false);
            File workFile = Utils.join(CWD, filename);
            Utils.restrictedDelete(workFile);
        } else if (!isStage) {
            exitWithError("No reason to remove the file.");
        }
    }
}
