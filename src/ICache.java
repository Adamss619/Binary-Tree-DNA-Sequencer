
import java.util.NoSuchElementException;

/***
 * Interface for a simple memory cache ADT.  
 * @author CS 321
 *
 * @param <T> - generic type of objects stored in cache
 */
public interface ICache<T> {
    /**
     * Gets the data from cache and moves it to the front, if it's there.
     * If not, returns null reference.
     *
     * @param target - object of type T
     * @return object of type T, or null reference
     */
    T get(T target);

    /***
     * Clears contents of the cache,
     * but doesn't change its capacity.
     */
    void clear();

    /***
     * Adds given data to front of cache.
     * Removes data in last position, if full.
     * @param data - object of type T
     */
    void add(T data);

    /***
     * Removes data in last position in cache.
     * @throws IllegalStateException - if cache is empty.
     */
    void removeLast();

    /**
     * Removes the given target data from the cache.
     *
     * @param target - object of type T
     * @throws NoSuchElementException - if target not found
     */
    void remove(T target);

    /**
     * Moves data already in cache to the front.
     *
     * @param data - object of type T
     * @throws NoSuchElementException - if data not in cache
     */
    void write(T data);

    /**
     * Get hit rate of the cache.
     *
     * @return double value
     */
    double getHitRate();

    /**
     * Get miss rate of the cache.
     *
     * @return double value
     */
    double getMissRate();

    /**
     * Whether there's any data in cache.
     *
     * @return boolean value
     */
    boolean isEmpty();
}
