import java.util.EmptyStackException;
import java.util.Iterator;

/**
 This class represents a generic stack data structure that stores elements of type T.
 It supports standard stack operations such as push, pop, peek, and isEmpty.
 It also implements Iterable<T> interface to enable the use of for-each loop to iterate over the elements in the stack.
 @param <T> the type of elements stored in the stack.
 */
public class Stack<T> implements Iterable<T> {
    private Node<T> top; // the top node of the stack
    private int size; // the number of elements in the stack

    /**
     Creates an empty stack with no elements.
     */
    public Stack() {
        top = null;
        size = 0;
    }
    /**
     Adds a new element to the top of the stack.
     @param data the element to be added to the stack.
     */
    public void push(T data) {
        Node<T> newNode = new Node<T>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }
    /**
     Removes and returns the element at the top of the stack.
     Throws an EmptyStackException if the stack is empty.
     @return the element at the top of the stack.
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    /**
     Returns the element at the top of the stack without removing it.
     Throws an EmptyStackException if the stack is empty.
     @return the element at the top of the stack.
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.data;
    }
    /**
     Returns true if the stack is empty, false otherwise.
     @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return top == null;
    }
    /**
     Returns the number of elements in the stack.
     @return the number of elements in the stack.
     */
    public int size() {
        return size;
    }
    /**
     Returns an iterator over the elements in the stack.
     The elements are returned in the reverse order they were added to the stack (LIFO order).
     @return an iterator over the elements in the stack.
     */
    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }
    /**
     This private inner class implements the Iterator<T> interface to enable the use of for-each loop to iterate over the elements in the stack.
     The elements are returned in the reverse order they were added to the stack (LIFO order).
     */
    private class StackIterator implements Iterator<T> {
        private Node<T> currentNode;

        /**
         Creates a new iterator for the stack starting from the top node.
         */
        public StackIterator() {
            currentNode = top;
        }
        /**
         Returns true if there are more elements in the stack, false otherwise.
         @return true if there are more elements in the stack, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            T data = currentNode.data;
            currentNode = currentNode.next;
            return data;
        }
    }

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            next = null;
        }
    }
}