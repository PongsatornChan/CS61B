/** A WeirdList holds a sequence of integers.
 * @author Pongsatorn Chanpanichravee
 */
public class WeirdList {
    /** The empty sequence of integers. */
    public static final WeirdList EMPTY =
        new endList();

    /** item in this node */
    private int head;

    /** pointer that point to the next one */
    private WeirdList tail;

    /** A new WeirdList whose head is HEAD and tail is TAIL. */
  public WeirdList(int head, WeirdList tail) {
      this.head = head;
      this.tail = tail;
  }

    /** Returns the number of elements in the sequence that
     *  starts with THIS. */
    public int length() {
        return 1 + tail.length();
    }

    /** Return a string containing my contents as a sequence of numerals
     *  each preceded by a blank.  Thus, if my list contains
     *  5, 4, and 2, this returns " 5 4 2". */
    @Override
    public String toString() {
        String result = " " + this.head + this.tail.toString();
        return result;
    }

    /** Part 3b: Apply FUNC.apply to every element of THIS WeirdList in
     *  sequence, and return a WeirdList of the resulting values. */
    public WeirdList map(IntUnaryFunction func) {
        return new WeirdList(func.apply(this.head), this.tail.map(func));
    }

    /** class to signal the end of WeirdList */
    private static class endList extends WeirdList {

        public endList() {
            super(0, null);
        }

        /** Return a 1 since it is the last element */
        @Override
        public int length() {
            return 0;
        }

        /** Return empty String */
        @Override
        public String toString() {
            return "";
        }

        /** Return itself */
        @Override
        public WeirdList map(IntUnaryFunction func) {
            return this;
        }
    }
}

