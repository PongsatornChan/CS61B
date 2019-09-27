import utils.Filter;
import utils.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

/** Exercises for Lab 5.
 *  @author Pongsatorn Chanpanichravee
 */
public class FilterClient {

    /** A couple of test cases. */
    private static final Integer[][] TESTS = {
        { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
        { 1, 2, 3, 0, 7, 8, 6, 9, 10, 1 }
    };

    /** Print out the items returned by L. */
    static void printAll(Filter<Integer> L) {
        System.out.print("[");
        String sep;
        sep = "";
        for (Integer i : L) {
            System.out.print(sep + i);
            sep = ", ";
        }
        System.out.println("]");
    }

    /** A sample space where you can experiment with your filter.
      * ARGS is unused. */
    public static void main(String[] args) {
        for (Integer[] data: TESTS) {
            List<Integer> L = Arrays.asList(data);
            System.out.println(L);
            Filter<Integer> f1 = new TrivialFilter<Integer>(L.iterator());
            printAll(f1);
            System.out.println();
            // ADD TO ME IF NEEDED

            List<Integer> L1 = Arrays.asList(data);
            System.out.println(L1);
            Filter<Integer> filter4 = everyFourth(L1.iterator());
            printAll(filter4);
            System.out.println();

            List<Integer> L2 = Arrays.asList(data);
            System.out.println(L2);
            Filter<Integer> filterEven = evenNumberFilter(L2.iterator());
            printAll(filterEven);
            System.out.println();

        }
    }


    /** Returns a filter that delivers every fourth item of INPUT,
     *  starting with the first.  You should not need to define a new
     *  class. */
    static Filter<Integer> everyFourth(Iterator<Integer> input) {
        AlternatingFilter<Integer> a1 = new AlternatingFilter<Integer>(input);
        AlternatingFilter<Integer> a2 = new AlternatingFilter<>(a1);
        return a2;
    }

    /** Returns a filter that delivers every even valued integer of
     *  INPUT. You should not need to define a new class. */
    static Filter<Integer> evenNumberFilter(Iterator<Integer> input) {
        PredicateFilter<Integer> a1 = new PredicateFilter<Integer>(new Even(), input);
        return a1;
    }

    /** A class whose instances represent the test for evenness. */
    static class Even implements Predicate<Integer> {
        @Override
        public boolean test(Integer x) {
            return x % 2 == 0;
        }
    }
}
