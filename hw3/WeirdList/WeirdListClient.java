/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        return L.map(new Adder(n));
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        Summer sum = new Summer();
        L.map(sum);
        return sum.getSum();
    }

    /** Class implement IntUnaryFunction to do the adding */
    private static class Adder implements IntUnaryFunction {

        /** what to add to x */
        private int anInt;

        public Adder(int n) {
            anInt = n;
        }

        /** return add anInt to x */
        public int apply(int x) {
            return anInt + x;
        }
    }

    /** Class that will do the summing and storing sum */
    private static class Summer implements IntUnaryFunction {

        /** hold the current sum */
        private int sum;

        public Summer() {
            sum = 0;
        }

        public int apply(int x) {
            sum = sum + x;
            return sum;
        }

        /** get the current sum */
        public int getSum() {
            return sum;
        }
    }

    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
