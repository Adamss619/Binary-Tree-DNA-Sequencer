/************************************
 * Implementation of IcacheADT using double linked list 
 * which holds generic object of type <T>
 *
 ************************************/

import java.util.NoSuchElementException;

public class BTreeCache<T> implements ICache<T> {


    private DLLNode<T> head, tail;
    private int size;

    private double cacheAccess;
    private double cacheHits;
    private final int defaultCacheSize = 100;
    private int maxCacheSize;

    /**
     * Creates an empty caches with a default size of 100
     */
    public BTreeCache() {
        head = tail = null;
        size = 0;
        cacheAccess = 0;
        cacheHits = 0;
        maxCacheSize = defaultCacheSize;
    }

    /**
     * Creates an empty cache with the specified size
     *
     * @param cacheSize Size of a cache
     */
    public BTreeCache(int cacheSize) {
        head = tail = null;
        size = 0;
        cacheAccess = 0;
        cacheHits = 0;
        maxCacheSize = cacheSize;
    }

    /**
     * Gets the data from cache and moves it to the front, if it's there. If not,
     * returns null reference.
     *
     * @param target - object of type T
     * @return object of type T, or null reference
     */
    @Override
    public T get(T target) {
        cacheAccess++;

        DLLNode<T> current = head;
        boolean found = false;

        while (current != null && !found) {
            if (current.getElement().equals(target))
                found = true;
            else
                current = current.getNext();
        }
        if (!found) {
            add(target);
            return null;
        } else {
            if (current == head) {
                // Do nothing. Data is already at the front.
                //return current.getElement();
            } else if (current == tail) {
                tail = current.getPrevious();
                tail.setNext(null);
                current.setPrevious(null);

                head.setPrevious(current);
                current.setNext(head);
                head = current;
                current = null;
            } else {
                current.getPrevious().setNext(current.getNext());
                current.getNext().setPrevious(current.getPrevious());

                current.setPrevious(null);
                current.setNext(head);
                head.setPrevious(current);
                head = current;
                current = null;
            }
        }
        //cacheAccess++;
        cacheHits++;
        return head.getElement();
    }

    /***
     * Clears contents of the cache, but doesn't change its capacity.
     */
    @Override
    public void clear() {

        head = null;
        tail = null;
        size = 0;
    }

    /***
     * Adds given data to front of cache. Removes data in last position, if full.
     *
     * @param data - object of type T
     */
    @Override
    public void add(T data) {

        DLLNode<T> newNode = new DLLNode<T>(data);

        if (isEmpty()) {
            head = newNode;
            tail = head;
        } else if (size == maxCacheSize) {
            removeLast();
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        size++;
    }

    /***
     * Removes data in last position in cache.
     *
     * @throws IllegalStateException - if cache is empty.
     */
    @Override
    public void removeLast() {

        if (isEmpty())
            throw new IllegalStateException("Cache is empty.");

        DLLNode<T> current = tail;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = current.getPrevious();
            tail.setNext(null);
            current = null;
        }
        size--;
    }

    /**
     * Removes the given target data from the cache.
     *
     * @param target - object of type T
     * @throws NoSuchElementException - if target not found
     */
    @Override
    public void remove(T target) {

        boolean found = false;
        DLLNode<T> current = head;

        while (current != null && !found) {
            if (current.getElement().equals(target))
                found = true;
            else
                current = current.getNext();
        }

        if (!found)
            throw new NoSuchElementException();

        if (current == head && current == tail) {
            head = null;
            tail = null;
        } else if (current == head) {
            head = current.getNext();
            head.setPrevious(null);
            current = null;
        } else if (current == tail) {
            tail = current.getPrevious();
            tail.setNext(null);
            current = null;
        } else {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
        }
        size--;
    }

    /**
     * Moves data already in cache to the front.
     *
     * @param data - object of type T
     * @throws NoSuchElementException - if data not in cache
     */
    @Override
    public void write(T data) {
        //cacheAccess++;
        boolean found = false;
        DLLNode<T> current = head;

        while (current != null && !found) {
            if (current.getElement().equals(data))
                found = true;
            else
                current = current.getNext();
        }

        if (!found) {
            add(data);
            throw new NoSuchElementException();
        }

        //cacheHits++;
        if (current == head) {
            // Do nothing. Data is already at the front.
        } else if (current == tail) {
            tail = current.getPrevious();
            tail.setNext(null);
            current.setPrevious(null);

            head.setPrevious(current);
            current.setNext(head);
            head = current;
            current = null;
        } else {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());

            current.setPrevious(null);
            current.setNext(head);
            head.setPrevious(current);
            head = current;
            current = null;
        }
    }

    /**
     * Get hit rate of the cache.
     *
     * @return double value
     */
    @Override
    public double getHitRate() {
        if (cacheAccess != 0)
            return cacheHits / cacheAccess;
        else return 0.00;
    }

    /**
     * Get miss rate of the cache.
     *
     * @return double value
     */
    @Override
    public double getMissRate() {
        return 1.00 - getHitRate();
    }

    /**
     * Whether there's any data in cache.
     *
     * @return boolean value
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Public method which returns number of cache access
     *
     * @return Returns number of cache assess
     */
    public double getCacheAccess() {
        return cacheAccess;
    }

    /**
     * @return Returns the cache hits
     */
    public double getCacheHits() {
        return cacheHits;
    }

    /**
     * Overriding toString method to print meaningful output
     */
    public String toString() {

        String str = "";
        DLLNode<T> current = head;

        if (isEmpty())
            return str = "[]";

        str = "[";

        while (current != null) {
            if (current != tail)
                str += current.getElement() + ",";
            else
                str += current.getElement() + "]";
            current = current.getNext();
        }

        return str;
    }

    public int getSize() {
        return size;
    }
}
