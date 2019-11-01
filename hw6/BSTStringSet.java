import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Pongsatorn Chanpanichravee
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (_root == null) {
            _root = new Node(s);
        } else {
            putHelper(s, _root);
        }
    }

    /**
     * recursice function to put S at the right place
     * by comparing value of Node T
     * */
    public void putHelper(String s, Node T) {
        if (T.s.compareTo(s) > 0) {
            if (T.left == null) {
                T.left = new Node(s);
            } else {
                putHelper(s, T.left);
            }
        } else if (T.s.compareTo(s) < 0) {
            if (T.right == null) {
                T.right = new Node(s);
            } else {
                putHelper(s, T.right);
            }
        }
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(s, _root);
    }

    /**
     * contains helper function that recursively search for S
     * in the tree by comparing value of T and move on to
     * the right direction
     * return true if compareTo() give 0
     */
    public boolean containsHelper(String s, Node T) {
        if (T == null) {
            return false;
        } else if (T.s.compareTo(s) > 0) {
            return containsHelper(s, T.left);
        } else if (T.s.compareTo(s) < 0) {
            return containsHelper(s, T.right);
        } else {
            return true;
        }
    }

    @Override
    public List<String> asList() {
        ArrayList<String> lst = new ArrayList<String>();
        listHelper(lst, _root);
        return lst;
    }

    public void listHelper(List<String> lst, Node T) {
        if (T != null) {
            listHelper(lst, T.left);
            lst.add(T.s);
            listHelper(lst, T.right);
        }

    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIteratorRange(_root, low, high);
    }

    private static class BSTIteratorRange implements Iterator<String> {
        private String low;
        private String high;
        private Stack<Node> _toDo = new Stack<>();

        BSTIteratorRange(Node node, String low, String high) {
            this.low = low;
            this.high = high;
            addTree(node);
        }

        /** A new iterator over the labels in NODE. */

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            if (node.right != null && node.right.s.compareTo(high) <= 0) {
                addTree(node.right);
            }
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                if (node.s.compareTo(low) < 0) {
                    if (node.right == null || node.right.s.compareTo(low) < 0) {
                        break;
                    } else {
                        addTree(node.right);
                    }
                    _toDo.push(node);
                    break;
                } else if (node.s.compareTo(high) > 0) {
                    node = node.left;
                } else {
                    _toDo.push(node);
                    node = node.left;
                }
            }
        }

    }


    /** Root node of the tree. */
    private Node _root;
}
