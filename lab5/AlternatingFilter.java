import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author Pongsatorn Chanpanichravee
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that lets through every other
     *  value. */
    AlternatingFilter(Iterator<Value> input) {
        super(input);
        pass = false;
    }

    @Override
    protected boolean keep() {
        pass = !pass;
        return pass;
    }

    private boolean pass;
}