package diaporama.medialoader;

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

    public synchronized boolean add(T obj){
        boolean added = false;
        if (set.add(obj)){
            added = list.add(obj);
        }
        return added;
    }

    public synchronized boolean contains(T obj){
        return set.contains(obj);
    }

    public synchronized int size(){
            return list.size();
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized T get(int index) {
        return list.get(index);
    }
}
