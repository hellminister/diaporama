package diaporama.imageloader;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

class QueueFilling implements Runnable {
        private static final Logger LOG = Logger.getLogger(QueueFilling.class.getName());

        private List<String> fileChoices;
        private ArrayBlockingQueue<Image> queue;
        private AtomicBoolean running;

        private Random randomNumGen;
        private boolean random;
        private int next = 0;

        public QueueFilling(ArrayBlockingQueue<Image> theQueue, List<String> fileChoices) {
            queue = theQueue;
            this.fileChoices = fileChoices;
            running = new AtomicBoolean(false);
            randomNumGen = new Random();
            random = true;
        }

        public void stop(){
            running.set(false);
            queue.clear();
        }

        @Override
        public void run() {
            running.set(true);
            while (running.get()){
                queue.offer(new Image(fileChoices.get(getNextIndex())));
            }

        }

        private int getNextIndex() {
            if (random){
                return randomNumGen.nextInt(fileChoices.size());
            } else {
                return next++;
            }
        }
}
