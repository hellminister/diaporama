package diaporama.imageloader;

import javafx.scene.image.Image;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageLoader {
    private static final Logger LOG = Logger.getLogger(ImageLoader.class.getName());

    private Set<String> folders;

    private List<String> paths;


    private List<Walker> walks;
    private QueueFilling queueFilling;

    private ArrayBlockingQueue<Image> preloading;

    public ImageLoader(Set<String> path, Set<String> extensions) {
        folders = path;

        paths = Collections.synchronizedList(new ArrayList<>());
        preloading = new ArrayBlockingQueue<>(10);

        walks = new LinkedList<>();

        for (String folder : folders) {
            Walker walk = new Walker(folder, extensions, paths);
            walks.add(walk);
            Thread t = new Thread(walk);
            t.start();
        }

        // this is to let Walker start filling the path list
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, e, ()-> "Woke before time from waiting for putting some data in path\n" + e.toString());
        }

        queueFilling = new QueueFilling(preloading, paths);
        Thread t2 = new Thread(queueFilling);
        t2.start();

    }

    public Image getNextImage() throws InterruptedException {
        return preloading.poll(1000, TimeUnit.MILLISECONDS);
    }

    public void stop(){
        for (Walker walk : walks) {
            walk.stop();
        }
        queueFilling.stop();
    }


}
