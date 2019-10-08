import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Pongsatorn Chanpanichravee
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        // TODO: YOUR CODE HERE
        assert (from.length() == to.length());
        source = str;
        this.from = from;
        this.to = to;
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

    /**
     *
     * @param buf
     * @param offset
     * @param count
     * @return number of times read() was called (exclude -1)
     * @throws IOException
     *
     * read from source offset + count times and store char in
     * buf starting from offset until offset + count
     */
    public int read(char buf[], int offset, int count) throws IOException {
        if (buf.length < count) {
            throw new IOException("buf length is too short");
        }
        if (isClose) {
            throw new IOException("the reader is closed");
        }

        int numCharRead = 0;
        for (int i = 0; i < offset; i++) {
            if (source.read() == -1) {
                return -1;
            }
        }
        for (int i = 0; i < count; i++) {
            int srcIn = source.read();
            if (srcIn != -1) {
                buf[i] = (char) srcIn;
                numCharRead++;
                for (int j = 0; j < from.length(); j++) {
                    if (from.charAt(j) == buf[i]) {
                        buf[i] = to.charAt(j);
                        break;
                    }
                }
            } else {
                return -1;
            }
        }
        return numCharRead;
    }

    public void close() throws IOException {
        source.close();
        isClose = true;
    }

    private boolean isClose = false;
    private Reader source;
    private String from;
    private String to;
}
