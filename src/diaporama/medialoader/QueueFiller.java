package diaporama.medialoader;

import diaporama.medialoader.loaders.Loader;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class QueueFiller<T> implements Runnable{
    private static final Logger LOG = Logger.getLogger(QueueFiller.class.getName());

    protected Loader<T> fileChoices;
    protected final AtomicBoolean running;
    protected final ArrayBlockingQueue<T> queue;

    protected final Random randomNumGen;
    protected boolean random;
    protected int next;

    protected QueueFiller(int size, boolean rdm) {
        queue = new ArrayBlockingQueue<>(size);
        running = new AtomicBoolean(false);
        randomNumGen = new Random();
        random = rdm;
        next = 0;
    }

    public void start(){
        Thread runner = new Thread(this);
        runner.start();
    }

    public void setLoader(Loader<T> fc){
        fileChoices = fc;
    }

    public void stop(){
        running.set(false);
        queue.clear();
    }

    @Override
    public void run() {
        running.set(true);

        while (fileChoices.getFiles().isEmpty() && running.get()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, e::toString);
            }
        }

        while (running.get()){
            String fileName = fileChoices.getFiles().get(getNextIndex());
            LOG.log(Level.FINE,
                    () -> fileChoices.getClass().getName() + " will offer " + fileName + " " + queue.size());
            try {
                queue.offer(generateMedia(fileName), 5, TimeUnit.MINUTES);
                LOG.log(Level.FINE, () -> fileChoices.getClass().getName() + " offers " + fileName);
            } catch (InterruptedException e) {
                LOG.log(Level.INFO,
                        () -> "took too long before a space is available, might try a new one " + fileChoices.getClass().getName() + " " + fileName + "\n" + e.toString());
            }

        }
        LOG.log(Level.FINE, () -> fileChoices.getClass().getName() + " queue stopped");

    }

    protected abstract T generateMedia(String fileName);

    public T get() throws InterruptedException {
        LOG.log(Level.FINE, () -> fileChoices.getClass().getName() + " will send Media");
        T toSend = queue.poll(5000, TimeUnit.MILLISECONDS);
        LOG.log(Level.FINE, () -> fileChoices.getClass().getName() + " sending Media");
        return toSend;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

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
