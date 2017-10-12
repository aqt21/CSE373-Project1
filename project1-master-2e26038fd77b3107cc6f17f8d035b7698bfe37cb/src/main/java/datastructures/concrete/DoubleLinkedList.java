// Andrew Tran
// Constance La
// CSE 373
// Project 1: Part 1

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.size == 0) {
            Node<T> currentNode = new Node<T>(item);
            this.front = currentNode;
            this.back = currentNode;
            size++;
        } else {
            Node<T> currentNode = new Node<T>(this.back, item, null);
            this.back.next = currentNode;
            this.back = currentNode;
            size++;
        }
    }

    @Override
    public T remove() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        } else if (this.size == 1) {
            T data = this.back.data;
            this.front = null;
            this.back = null;
            this.size = 0;
            return data;
        } else {
            T data = this.back.data;
            this.back = this.back.prev;
            this.back.next = null;
            size--;
            return data;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> tempNode = this.front;
            for (int i = 0; i < index; i++) {
                tempNode = tempNode.next;
            }
            return tempNode.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> tempNode = this.front;
            for (int i = 0; i < index; i++) {
                tempNode = tempNode.next;
            }
            Node<T> newNode = new Node<T>(tempNode.prev, item, tempNode.next);
            if (tempNode != this.front) {
                tempNode.prev.next = newNode;
            } else {
                this.front = newNode;
            }
            if (tempNode != this.back) {
                tempNode.next.prev = newNode;
            } else {
                this.back = newNode;
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                Node<T> newNode = new Node<T>(null, item, this.front);
                if (this.size() == 0) {
                    this.back = newNode;
                } else {
                    this.front.prev = newNode;
                }
                this.front = newNode;
            } else if (index == this.size()){
                Node<T> newNode = new Node<T>(this.back, item, null);
                this.back.next = newNode;
                this.back = newNode;
            } else {
                Node<T> tempNode;
                if (index <= this.size() / 2) {
                    tempNode = this.front;
                    for (int i = 0; i < index; i++) {
                        tempNode = tempNode.next;
                    }
                } else {
                    tempNode = this.back;
                    for (int i = this.size-1; i > index; i--) {
                        tempNode = tempNode.prev;
                    }
                }
                Node<T> nextNode = tempNode;
                Node<T> prevNode = tempNode.prev;
                Node<T> newNode = new Node<T>(prevNode, item, nextNode);
                prevNode.next = newNode;
                nextNode.prev = newNode;
            }
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> tempNode = this.front;
            for (int i = 0; i < index; i++) {
                tempNode = tempNode.next;
            }
            Node<T> nextNode = tempNode.next;
            Node<T> prevNode = tempNode.prev;
            if (tempNode == this.front && tempNode == this.back) {
                this.front = null;
                this.back = null;
            } else if (tempNode == this.front) {
                nextNode.prev = null;
                this.front = nextNode;
            } else if (tempNode == this.back) {
                prevNode.next = null;
                this.back = prevNode;
            } else {
                nextNode.prev = prevNode;
                prevNode.next = nextNode;
            }
            this.size--;
            return tempNode.data;
        }
    }

    @Override
    public int indexOf(T item) {
        Node<T> tempNode = this.front;
        int index = 0;
        while (tempNode != null) {
            if (tempNode.data == item || tempNode.data.equals(item)) {
                return index;
            } else {
                index++;
                tempNode = tempNode.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> tempNode = this.front;
        while (tempNode != null) {
            if (tempNode.data == other || tempNode.data.equals(other)) {
                return true;
            } else {
                tempNode = tempNode.next;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current != null) {
                T data = current.data;
                current = current.next;
                return data;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
