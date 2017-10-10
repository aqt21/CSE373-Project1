// Andrew Tran
// Constance La
// CSE 373
// Project 1: Part 1

package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(1);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        int index = this.indexOf(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            return this.pairs[index].value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (this.containsKey(key)) {
            this.pairs[this.indexOf(key)].value = value;
        } else {
            // If array is full, create new array double size of old one and copy over old elements
            int nextEmpty = this.nextEmpty(); 
            if (nextEmpty == -1) {
                int oldSize = this.pairs.length;
                int newSize = oldSize * 2;
                Pair<K, V>[] newPairs = this.makeArrayOfPairs(newSize);
                for (int i = 0; i < oldSize; i++) {
                    newPairs[i] = this.pairs[i];
                }
                nextEmpty = oldSize;
                this.pairs = newPairs;
            }
            Pair<K, V> newPair = new Pair<K, V>(key, value);
            this.pairs[nextEmpty] = newPair;
            size++;
        }
    }

    @Override
    public V remove(K key) {
        int index = this.indexOf(key);
        V value;
        if (index == -1) {
            throw new NoSuchKeyException();
        } else {
            value = this.pairs[index].value;
            for (int i = index; i < this.size()-1; i++) {
                this.pairs[i] = this.pairs[i+1];
            }
            // Set last pair to null, in the case of removing from full array
            this.pairs[this.size()-1] = null;
            size--;
        }
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        return !(this.indexOf(key) == -1);
    }

    @Override
    public int size() {
        return this.size;
    }

    // Return next empty index in array, returns -1 if if array is full
    public int nextEmpty() {
        for (int i = 0; i < this.pairs.length; i++) {
            if (this.pairs[i] == null) {
                return i;
            }
        }
        // Array is full
        return -1;
    }
    
    // Returns index of given key, returns -1 if key is not in array
    public int indexOf(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (this.pairs[i].key == null || this.pairs[i].key.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}