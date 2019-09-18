package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author Pongsatorn Chanpanichravee
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /**
         * Go through the IntList L, when find value
         * that is less than or equal to the previous value,
         * break the IntList and put it in the IntListList
         */
        IntListList sentinel = new IntListList(L, null);
        IntListList pointer = sentinel;
        int highest = L.head;
        for (IntList curr = L; curr.tail != null;) {
            if (curr.tail.head <= highest) {
                pointer.tail = new IntListList(curr.tail, null);
                pointer = pointer.tail;
                highest = curr.tail.head;
                IntList deleter = curr;
                curr = curr.tail;
                deleter.tail = null;
            } else {
                highest = curr.tail.head;
                curr = curr.tail;
            }
        }
        return sentinel;
    }
}
