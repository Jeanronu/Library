/**
 * PriorityQueue interface represents a collection of elements that are maintained in a certain order
 * and can be accessed according to their priority. The elements must implement the Comparable interface
 * in order to be compared to each other.
 * @param <E> the type of elements in this PriorityQueue, must implement the Comparable interface.
 */
public interface PriorityQueue<E extends Comparable<E>> {

    /**
     * Inserts the specified element into this PriorityQueue.
     * @param element the element to be inserted.
     */
    void insert(E element);

    /**
     * Retrieves and returns the maximum element in this PriorityQueue.
     * @return the maximum element in this PriorityQueue.
     */
    E max();

    /**
     * Retrieves and removes the maximum element in this PriorityQueue.
     * @return the maximum element in this PriorityQueue, or null if the PriorityQueue is empty.
     */
    E removeMax();

    /**
     * Returns the number of elements in this PriorityQueue.
     * @return the number of elements in this PriorityQueue.
     */
    int size();

    /**
     * Returns true if this PriorityQueue contains no elements.
     * @return true if this PriorityQueue contains no elements, false otherwise.
     */
    boolean isEmpty();
}