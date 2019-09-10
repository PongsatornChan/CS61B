import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
        assertEquals(0, 0); */
        assertEquals(20, CompoundInterest.numYears(2039));
        assertEquals(41, CompoundInterest.numYears(2060));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10,12, 2021), tolerance);
        assertEquals(8.574, CompoundInterest.futureValue(10, -5, 2022), tolerance );
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.80265,
                CompoundInterest.futureValueReal(10,12,2021,3),tolerance);
        assertEquals(24.22225,
                CompoundInterest.futureValueReal(10,15,2029, 5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);

    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(14936.375,
                CompoundInterest.totalSavingsReal(5000, 2021, 10, 5), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
