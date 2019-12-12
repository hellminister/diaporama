package diaporama.medialoader.loaders;

import diaporama.Diaporama;
import diaporama.DiaporamaScreen;
import diaporama.ProgramParameters;
import diaporama.medialoader.QueueFiller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoLoader extends Loader<MediaPlayer> {
    private static final Logger LOG = Logger.getLogger(VideoLoader.class.getName());
    private AtomicBoolean used;
    private Object lock;
    private DiaporamaScreen using;

    public VideoLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getVideoQueueSize(), param.getVideoRandom()) {
            @Override
            protected MediaPlayer generateMedia(String fileName) {
                LOG.log(Level.FINE, ()-> "generating media " + fileName);
                return new MediaPlayer(new Media(fileName));
            }
        });
        used = new AtomicBoolean(false);
        using = null;
        lock = new Object();
    }

 /*   public synchronized boolean canUse() {
        if (!isEmpty()){
            LOG.info(() -> "Initial " + used.get());
            boolean check = used.compareAndSet(false, true);
            LOG.info(() -> "New " + check);
            return check;
        }
        return false;
    }

    public synchronized void release(){
        LOG.info("Released");
        used.set(false);
    }*/

    public synchronized boolean canUse(DiaporamaScreen ds) {
        synchronized (lock) {
            if (!isEmpty()) {
                if (using == null){
                    LOG.info(() -> ds + " Acquires me");
                    using = ds;
                    return true;
                } else if (using == ds){
                    LOG.info(ds + " already owns me");
                    return true;
                } else {
                    LOG.info(() -> ds + " cannot use me, i am under " + using + " control");
                }
            }
            return false;
        }
    }

    public synchronized void release(DiaporamaScreen ds){
        synchronized (lock) {
            if (using == ds){
                LOG.info(ds + " Release me");
                using = null;
            } else {
                LOG.severe(() -> ds + " tried to release me while " + using + " owns me");
            }
        }
    }
}
