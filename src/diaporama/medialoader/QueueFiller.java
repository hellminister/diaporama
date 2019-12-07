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
    protected AtomicBoolean running;
    protected ArrayBlockingQueue<T> queue;

    protected Random randomNumGen;
    protected boolean random;
    protected int next = 0;

    protected QueueFiller(int size) {
        queue = new ArrayBlockingQueue<>(size);
        running = new AtomicBoolean(false);
        randomNumGen = new Random();
        random = true;
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
            queue.offer(generateMedia(fileName));
        }
        LOG.log(Level.INFO, () -> fileChoices.getClass().getName() + " queue stopped");

    }

    protected abstract T generateMedia(String fileName);

    public T get() throws InterruptedException {
        return queue.poll(1000, TimeUnit.MILLISECONDS);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    protected int getNextIndex() {
        if (random){
            return randomNumGen.nextInt(fileChoices.getFiles().size());
        } else {
            return next++;
        }
    }

}
