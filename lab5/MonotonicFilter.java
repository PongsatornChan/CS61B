import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets all the VALUE elements of its input sequence
 *  that are larger than all the preceding values to go through the
 *  Filter.  So, if its input delivers (1, 2, 3, 3, 2, 1, 5), then it
 *  will produce (1, 2, 3, 5).
 *  @author Pongsatorn Chanpanichravee
 */
class MonotonicFilter<Value extends Comparable<Value>> extends Filter<Value> {

    /** A filter of values from INPUT that delivers a monotonic
     *  subsequence.  */
    MonotonicFilter(Iterator<Value> input) {
        super(input);
        isFirst = true;
    }

    @Override
    protected boolean keep() {
        if (isFirst) {
            isFirst = false;
            highest = _next;
            return true;
        } else {
            if (highest.compareTo(_next) == -1) {
                highest = _next;
                return true;
            } else {
                return false;
            }
        }
    }
    
    private Value highest;
    private boolean isFirst;

}
