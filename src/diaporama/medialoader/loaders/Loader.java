package diaporama.medialoader.loaders;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Loads and manages objects of type T
 * @param <T>
 */
public class Loader<T extends MediaWithInfo> {
    private static final Logger LOG = Logger.getLogger(Loader.class.getName());


    protected final DistinctArrayList<Path> files;
    protected final QueueFiller<T> queueFiller;


    /**
     * creates a new Loader
     * @param filler the queue that returns the objects
     */
    public Loader(QueueFiller<T> filler){
        files = new DistinctArrayList<>();
        queueFiller = filler;
        queueFiller.setLoader(this);
    }

    /**
     * starts the async queue thread
     */
    public void start(){
        queueFiller.start();
    }

    /**
     * Adds the given file path in the list
     * @param s the file path string
     */
    public void addFileName(Path s) {
        LOG.fine( () -> this.getClass().getName() + " adds " + s);
        files.add(s);
    }

    /**
     * @return the list of files
     */
    public DistinctArrayList<Path> getFiles() {
        return files;
    }

    /**
     * stops the async queue thread
     */
    public void stop() {
        queueFiller.stop();
    }

    /**
     * @return The next Object ready in the queue
     * @throws InterruptedException thrown if no element was present after 5 seconds
     */
    public T getNext() throws InterruptedException {
        return queueFiller.get();
    }

    /**
     * @return whether there is an element ready in the queue
     */
    public boolean isEmpty() {
        return queueFiller.isEmpty();
    }
}