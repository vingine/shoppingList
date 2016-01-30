package listControl;

import java.util.Iterator;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1201
 * @since   1.8
 */
public class MyLinkedList<T> implements MyList<T>, Iterable<T> {

    /**
     * References to the first node in the list.
     */
    private MyNode<T> head;

    /**
     * Holds the size (=length) of the list.
     */
    private int size;

    /**
     * Creates a linked list of MyNodes.
     *
     * Each node holds a reference to the next node in line,
     * and an index number for ordering the list. The constructor just
     * initializes the list size and the first node.
     */
    public MyLinkedList() {

        head = null;
        size = 0;
    }

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an MyListIterator
     */
    public Iterator<T> iterator() {

        return new MyIterator<T>(head);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e the element to be appended to the list
     */
    public void add(T e) {

        MyNode<T> newNode = new MyNode<>();
        newNode.setElement(e);

        if (head == null) {

            head = newNode;
        } else {

            MyNode<T> currentNode = head;

            while (currentNode.getNextNode() != null) {

                currentNode = currentNode.getNextNode();
            }

            currentNode.setNextNode(newNode);
        }

        size++;
    }

    /**
     * Clears this list of all the elements inside.
     */
    public void clear() {

        head = null;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the object at the specified position in this list
     * @throws IndexOutOfBoundsException if tried to use index that does not
     * exist
     */
    public T get(int index) 
        throws IndexOutOfBoundsException {

        T returnObject;

        if ( index < 0 || index > size() ) {

            throw new IndexOutOfBoundsException("No element at that index or" +
                " no such index exists!");
        } else {

            int holder = 0;

            MyNode<T> currentNode = head;

            while (holder < index) {

                currentNode = currentNode.getNextNode();
                holder++;
            }

            returnObject = currentNode.getElement();
        }

        return returnObject;
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true, if the list contains no elements.
     */
    public boolean isEmpty() {

        boolean empty = true;
        
        if (head != null) {

            empty = false;
        }
        
        return empty;
    }

    /**
     * Removes the element at the specified position in this list. 
     *
     * When the element that is going to be removed is found,
     * the element is returned and the node holding the element
     * will be left out of the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified location
     * @throws IndexOutOfBoundsException if tried to use index that does not
     * exist
     */
    public T remove(int index) 
        throws IndexOutOfBoundsException {

        T returnObject;

        if ( index < 0 || index >= size() ) {

            throw new IndexOutOfBoundsException("No element at that index or" +
                " no such index exists!");
        } else {

            int holder = 0;

            MyNode<T> currentNode = head;

            if (index == 0) {

                returnObject = currentNode.getElement();
                head = currentNode.getNextNode();
            } else {

                while (holder < index) {

                    if ( !(holder + 1 == index) ) {

                        currentNode = currentNode.getNextNode();
                    }

                    holder++;
                }

                returnObject = currentNode.getNextNode().getElement();

                MyNode<T> jumpNode = currentNode.getNextNode().getNextNode();
                currentNode.setNextNode(jumpNode);
            }

            // System.out.println("REMOVE: element removed");

            size--;
        }

        return returnObject;
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * Works only if the element is present (return true).
     *
     * @param o the element to be deleted from this list, if present
     * @return true, if the list contained the specified element.
     * @throws NullPointerException if the target object is null
     */
    public boolean remove(T o) 
        throws NullPointerException {

        boolean elementRemoved = false;

        if (o == null) {

            throw new NullPointerException("The parameter element is null!");
        } else {

            MyNode<T> currentNode = head;

            if (currentNode.getElement() == o) {

                elementRemoved = true;
                head = currentNode.getNextNode();
            } else {

                while (currentNode.getNextNode() != null && !elementRemoved) {

                    if (currentNode.getNextNode().getElement() != o) {

                        currentNode = currentNode.getNextNode();
                    } else {

                        elementRemoved = true;
                    }
                }

                if (elementRemoved && currentNode.getNextNode() != null) {

                    MyNode<T> jumpNode = currentNode.getNextNode().
                        getNextNode();
                    currentNode.setNextNode(jumpNode);
                } else if (elementRemoved) {

                    currentNode.setNextNode(null);
                }
            }

            // Only decrease the size of the list
            // if an element was removed from the list.
            if (elementRemoved) {

                // System.out.println("REMOVE: element removed");
                size--;
            }
        }

        return elementRemoved;
    }

    /**
     * Returns the index of the first occurrence of the specified element.
     * 
     * Returns -1 if this list does not contain the element.
     *
     * @param o the element to look from the list.
     * @return the index of the specified element or -1 if element doesn't
     * exist.
     * @throws NullPointerException if the target object is null
     */
    public int indexOf(T o) 
        throws NullPointerException {

        int index = -1;
        int holder = 0;

        if (o == null) {

            throw new NullPointerException("The parameter element is null!");
        } else {

            MyNode<T> currentNode = head;

            if (currentNode.getElement() == o) {

                index = holder;
            } else {

                while (currentNode.getNextNode() != null && index == -1) {

                    holder++;

                    if (currentNode.getNextNode().getElement() != o) {

                        currentNode = currentNode.getNextNode();
                    } else {

                        index = holder;
                    }
                }
            }
        }

        return index;
    }
    
    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list.
     */
    public int size() {

        return size;
    }
}
