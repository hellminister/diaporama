package diaporama.medialoader.loaders;

import diaporama.medialoader.DistinctArrayList;
import diaporama.medialoader.QueueFiller;

import java.util.logging.Logger;

public class Loader<T> {
    private static final Logger LOG = Logger.getLogger(Loader.class.getName());


    protected final DistinctArrayList<String> files;
    protected final QueueFiller<T> queueFiller;


    public Loader(QueueFiller<T> filler){
        files = new DistinctArrayList<>();
        queueFiller = filler;
        queueFiller.setLoader(this);
    }

    public void start(){
        queueFiller.start();
    }

    public void addFileName(String s) {
        LOG.fine( () -> this.getClass().getName() + " adds " + s);
        files.add(s);
    }

    public DistinctArrayList<String> getFiles() {
        return files;
    }

    public void stop() {
        queueFiller.stop();
    }

    public T getNext() throws InterruptedException {
        return queueFiller.get();
    }


    public boolean isEmpty() {
        return queueFiller.isEmpty();
    }
}
