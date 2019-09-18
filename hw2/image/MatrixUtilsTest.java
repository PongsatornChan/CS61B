package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** test for MatrixUtils's methods
 *  @author Pongsatorn Chanpanichravee
 */

public class MatrixUtilsTest {
    /** Test accumulateVertical()
     */
    @Test
    public void testAccumulateVertical() {
        double[][] m = {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990,   30003,   1000000},
                {1000000, 30002,   103046,  1000000},
                {1000000, 29515,   38273,   1000000},
                {1000000, 73403,   35399,   1000000},
                {1000000, 1000000, 1000000, 1000000}
        };

        double[][] expected = {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        assertArrayEquals(expected, MatrixUtils.accumulateVertical(m));
    }

    @Test
    public void testT() {
        double[][] m = {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990,   30003,   1000000},
                {1000000, 30002,   103046,  1000000},
                {1000000, 29515,   38273,   1000000},
                {1000000, 73403,   35399,   1000000},
                {1000000, 1000000, 1000000, 1000000}
        };

        double[][] mT = {
                {1000000, 1000000, 1000000, 1000000, 1000000, 1000000},
                {1000000, 75990,   30002,   29515,   73403,   1000000},
                {1000000, 30003,   103046,  38273,   35399,   1000000},
                {1000000, 1000000, 1000000, 1000000, 1000000, 1000000}
        };
        assertArrayEquals(mT, MatrixUtils.t(m));
    }

    @Test
    public void testAccumulate() {
        double[][] m = {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990,   30003,   1000000},
                {1000000, 30002,   103046,  1000000},
                {1000000, 29515,   38273,   1000000},
                {1000000, 73403,   35399,   1000000},
                {1000000, 1000000, 1000000, 1000000}
        };

        double[][] expectedV = {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };

        double[][] expectedH = {
                {1000000, 2000000, 2075990, 2060005},
                {1000000, 1075990, 1060005, 2060005},
                {1000000, 1030002, 1132561, 2060005},
                {1000000, 1029515, 1067788, 2064914},
                {1000000, 1073403, 1064914, 2064914},
                {1000000, 2000000, 2073403, 2064914}
        };
        assertArrayEquals(expectedV, MatrixUtils.accumulate(m, MatrixUtils.Orientation.VERTICAL));
        assertArrayEquals(expectedH, MatrixUtils.accumulate(m, MatrixUtils.Orientation.HORIZONTAL));

    }

    @Test
    public void testFindVerticalSeam() {
        double[][] input = {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };

        int[] expected = {1, 2, 1, 1, 2, 1};

        assertArrayEquals(expected, MatrixUtils.findVerticalSeam(input));
    }

    @Test
    public void testFindSeam() {
        double[][] input = {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };

        int[] expected = {1, 2, 1, 1, 2, 1};

        assertArrayEquals(expected, MatrixUtils.findSeam(input, MatrixUtils.Orientation.VERTICAL));

        double[][] inputT = MatrixUtils.t(input);
        assertArrayEquals(expected, MatrixUtils.findSeam(inputT, MatrixUtils.Orientation.HORIZONTAL));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
