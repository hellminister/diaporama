package diaporama.medialoader.loaders;

import javafx.scene.media.MediaException;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * A queue that preloads a given number of object taken from the list of file given async
 * @param <T>
 */
public abstract class QueueFiller<T> implements Runnable{
    private static final Logger LOG = Logger.getLogger(QueueFiller.class.getName());

    protected Loader<T> fileChoices;
    protected final AtomicBoolean running;
    protected final ArrayBlockingQueue<T> queue;

    protected final Random randomNumGen;
    protected boolean random;
    protected int next;

    /**
     *
     * @param size the size of the queue (number of element it can contains)
     * @param rdm whether the elements are pulled sequentially or randomly from the list of files
     */
    protected QueueFiller(int size, boolean rdm) {
        queue = new ArrayBlockingQueue<>(size);
        running = new AtomicBoolean(false);
        randomNumGen = new Random();
        random = rdm;
        next = 0;
    }

    /**
     * Starts the queue thread to start filling it
     */
    public void start(){
        Thread runner = new Thread(this);
        runner.start();
    }

    /**
     * sets its loader
     * @param fc the associated Loader
     */
    public void setLoader(Loader<T> fc){
        fileChoices = fc;
    }

    /**
     * sets the queue thread to stop
     */
    public void stop(){
        running.set(false);
        queue.clear();
    }

    /**
     * The filling of the queue
     */
    @Override
    public void run() {
        running.set(true);

        // waits until some files are ready before starting to fill the queue
        while (fileChoices.getFiles().isEmpty() && running.get()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.severe(e::toString);
            }
        }

        while (running.get()){
            String fileName = fileChoices.getFiles().get(getNextIndex());
            LOG.fine(() -> fileChoices.getClass().getName() + " will offer " + fileName + " " + queue.size());
            try {
                // Inserts the generated object in the queue when there's space
                queue.offer(generateMedia(fileName), 5, TimeUnit.MINUTES);
                LOG.fine(() -> fileChoices.getClass().getName() + " offers " + fileName);
            } catch (InterruptedException e) {
                LOG.info(() -> "Took too long before a space is available, might try a new one " + fileChoices.getClass().getName() + " " + fileName + "\n" + e.toString());
            } catch (IllegalArgumentException | UnsupportedOperationException | MediaException ex) {
                LOG.info(() -> "Problem with " + fileName + "! Removing from list.");
                fileChoices.getFiles().remove(fileName);
            }

        }
        LOG.fine(() -> fileChoices.getClass().getName() + " queue stopped");

    }

    /**
     *
     * @param fileName the filename of the object to generate
     * @return the generated object
     */
    protected abstract T generateMedia(String fileName);

    /**
     * @return the first item in the queue
     * @throws InterruptedException thrown if none is available within 5 seconds
     */
    public T get() throws InterruptedException {
        LOG.fine(() -> fileChoices.getClass().getName() + " will send Media");
        T toSend = queue.poll(5000, TimeUnit.MILLISECONDS);
        LOG.fine(() -> fileChoices.getClass().getName() + " sending Media");
        return toSend;
    }

    /**
     * @return whether the queue is empty
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * @return the index of the filename of the next item to generate
     */
    protected int getNextIndex() {
        if (random){
            return randomNumGen.nextInt(fileChoices.getFiles().size());
        } else {
            ++next;
            if (next >= fileChoices.getFiles().size()){
                next = 0;
            }
            return next;
        }
    }

}
