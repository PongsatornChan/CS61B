import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Cursory test of the Translate class.
 *  @author Pongsatorn Chanpanichravee
 */
public class TranslateTest {

    /** Just a test to check if Translate class work
     *  correctly.
     * */
    @Test
    public void testTranslate() {
        Translate r = new Translate();
        String result = r.translate("Hello there. General Kanobi", "Ho", "Oh");
        assertEquals(result, "Oellh there. General Kanhbi");

        result = r.translate("Hello world!", "", "");
        assertEquals(result, "Hello world!");
    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(TranslateTest.class));
    }

}
