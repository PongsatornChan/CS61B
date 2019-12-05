package gitlet;

import java.text.SimpleDateFormat;
import java.util.Formatter;
import ucb.junit.textui;
import org.junit.Test;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Test time formatting */
    @Test
    public void testTime() {
        System.out.print(Commit.FIRST_COMMIT.logMsg());

        String test = "012345";
        System.out.println(test.substring(0, 4));
    }

}


