package listControl;

import java.util.Iterator;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1127
 * @since   1.8
 */
public class MyIterator<T> implements Iterator<T> {

    /**
     * The next node the iterator is going through.
     */
    private MyNode<T> node;

    /**
     * Creates a new iterator for a list.
     *
     * Used when MyLinkedList is iterated in an enhanced loop.
     *
     * @param node the first node in the list.
     */
    public MyIterator(MyNode<T> node) {

        this.node = node;
    }

    /**
     * Returns true if the iteration has more elements.
     *
     * That means that it returns true if the next node of current node is not
     * null.
     *
     * @return true if there is a node after the current node.
     */
    public boolean hasNext() {

        boolean returnValue = false;

        if (node != null) {

            returnValue = true;
        }

        return returnValue;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    public T next() {

        T element = node.getElement();
        node = node.getNextNode();
        return element;
    }

    /**
     * Does not work.
     *
     * Is not used, and throws UnsupportedOperationException if used.
     *
     * @throws UnsupportedOperationException if tried to use
     */
    public void remove() throws UnsupportedOperationException {

        throw new UnsupportedOperationException("remove() not supported.");
    }
}
