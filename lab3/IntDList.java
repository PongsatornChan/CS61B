/**
 * Scheme-like pairs that can be used to form a list of integers.
 * @author P. N. Hilfinger or unknown TA
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int num = 0;
        for(DNode curr = _front; curr != null; curr = curr._next)
            num++;
        return num;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size
     *          for positive indices and -size <= i < 0 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        DNode curr;
        if(i < 0) {
            curr = _back;
            for(int j = -1; j > i;j--)
                curr = curr._prev;
        } else {
            curr = _front;
            for(int j = 0; j < i;j++)
                curr = curr._next;
        }
        return curr._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if(_front == null) {
            _front = new DNode(null,d,null);
            _back = _front;
        } else {
            _front = new DNode(null, d, _front);
            _front._next._prev = _front;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        if(_back == null) {
            _back = new DNode(null,d,null);
            _front = _back;
        } else {
            _back = new DNode(_back, d, null);
            _back._prev._next = _back;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int result = _back._val;
        if(size() == 1) {
            _back = null;
            _front = null;
        } else {
            DNode temp = _back;
            _back = _back._prev;
            _back._next = null;
            temp._prev = null;
        }
        return result;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if(_front == null)
            return "[]";
        else {
            String lstStr = "[";
            DNode curr;
            for(curr = _front; curr._next != null; curr = curr._next) {
                lstStr = lstStr + curr._val +", ";
            }

            lstStr += curr._val+"]";
            return lstStr;
        }
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    protected static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
