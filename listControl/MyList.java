package listControl;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1127
 * @since   1.8
 */
public interface MyList<T> {

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e the element to be appended to the list
     */
    void add(T e);

    /**
     * Removes all of the elements from this list.
     */
    void clear();

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the object at the specified position in this list
     */
    T get(int index);

    /**
     * Returns true if this list contains no elements.
     *
     * @return true, if the list contains no elements.
     */
    boolean isEmpty();

    /**
     * Removes the element at the specified position in this list. 
     *
     * When the element that is going to be removed is found,
     * the element is returned and the node holding the element
     * will be left out of the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified location
     */
    T remove(int index);

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * Works only if the element is present (return true).
     *
     * @param o the element to be deleted from this list, if present
     * @return true, if the list contained the specified element.
     */
    boolean remove(T o);

    /**
     * Returns the index of the first occurrence of the specified element.
     * 
     * Returns -1 if this list does not contain the element.
     *
     * @param o the element to look from the list.
     * @return the index of the specified element or -1 if element doesn't
     * exist.
     */
    int indexOf(T o);

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list.
     */
    int size();
}
