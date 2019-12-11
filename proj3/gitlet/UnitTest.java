package gitlet;

import java.text.SimpleDateFormat;
import java.util.Formatter;
import ucb.junit.textui;
import org.junit.Test;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public void testSplit() {
        String test = "===\n" +
                "commit a0da1ea5a15ab613bf9961fd86f010cf74c7ee48\n" +
                "Date: Thu Nov 9 20:00:05 2017 -0800\n" +
                "A commit message.\n" +
                "\n" +
                "===\n" +
                "commit 3e8bf1d794ca2e9ef8a4007275acf3751c7170ff\n" +
                "Date: Thu Nov 9 17:01:33 2017 -0800\n" +
                "Another commit message.\n" +
                "\n" +
                "===\n" +
                "commit e881c9575d180a215d1a636545b8fd9abfb1d2bb\n" +
                "Date: Wed Dec 31 16:00:00 1969 -0800\n" +
                "initial commit\n";
        String[] lst = test.split("\\s\\s");
        for (String s : lst) {
            System.out.print(s);
            System.out.print("\n********\n");
        }
        String n = "initial commit";
        CharSequence nm = n.subSequence(0, 7);
        System.out.print(nm);
        System.out.println(lst[0].contains(nm));
        System.out.println(lst[2].contains(nm));
        Pattern pattern = Pattern.compile("\\w{40}");
        Matcher matcher = pattern.matcher(lst[2]);
        System.out.println(matcher.find());
        System.out.println(matcher.group());
    }

}


