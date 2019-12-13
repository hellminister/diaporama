package diaporama.medialoader.loaders;

import java.util.*;
import java.util.logging.Logger;

/**
 * Does not implement list because it has way less functionality than a list
 * exemple, you cant remove an object
 * TODO get it to implement List and Set
 * @param <T>
 */
public class DistinctArrayList<T> {
    private final List<T> list;
    private final Set<T> set;

    public  DistinctArrayList() {
        list = Collections.synchronizedList(new ArrayList<>());
        set = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * adds the object in the list if and only if it is not already there
     * @param obj the object to add
     * @return whether the object was added or not
     */
    public synchronized boolean add(T obj){
        boolean added = false;
        if (set.add(obj)){
            added = list.add(obj);
        }
        return added;
    }

    /**
     * @param obj the item to verify if it is in the list
     * @return true if the object is in the list
     */
    public synchronized boolean contains(T obj){
        return set.contains(obj);
    }

    /**
     * @return the number of elements in the list
     */
    public synchronized int size(){
            return list.size();
    }

    /**
     * @return true if the list is empty
     */
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * @param index the position from which to get the object
     * @return the item situated at the given position
     */
    public synchronized T get(int index) {
        return list.get(index);
    }
}
