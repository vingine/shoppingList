package listControl;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1127
 * @since   1.8
 */
public class MyNode<T> {

    /**
     * Holds an integer value for MyList based operations.
     */
    private int index;

    /**
     * Holds a reference to the next Node in the list, or null if last.
     */
    private MyNode<T> nextNode;

    /**
     * Holds a reference to the element this Node should hold.
     */
    private T element;

    /**
     * Sets the index of this Node.
     *
     * @param index the index of this node
     */
    public void setIndex(int index) {

        this.index = index;
    }

    /**
     * Sets a pointer to the next node or to null.
     *
     * @param nextNode the reference to the next node or to the end of list.
     */
    public void setNextNode(MyNode<T> nextNode) {

        this.nextNode = nextNode;
    }

    /**
     * Sets the element of this object.
     *
     * @param element the element this node holds.
     */
    public void setElement(T element) {

        this.element = element;
    }

    /**
     * Returns the index of this node.
     *
     * @return the index of this node
     */
    public int getIndex() {

        return index;
    }

    /**
     * Returns the reference to the next node or to the end of the list.
     *
     * @return the next node in list, or null if the last node.
     */
    public MyNode<T> getNextNode() {

        return nextNode;
    }

    /**
     * Returns the element held in this node.
     *
     * @return the element held in this node
     */
    public T getElement() {

        return element;
    }
}
