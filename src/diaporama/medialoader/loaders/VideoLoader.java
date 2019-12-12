package diaporama.medialoader.loaders;

import diaporama.views.DiaporamaScreen;
import diaporama.ProgramParameters;
import diaporama.medialoader.QueueFiller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.logging.Logger;

/**
 * Managers the video objects
 * Only 1 video can be shown at a time, preventing other callers from pulling a video
 * if it didnt reserve this object
 */
public class VideoLoader extends Loader<MediaPlayer> {
    private static final Logger LOG = Logger.getLogger(VideoLoader.class.getName());
    private final Object lock;
    private DiaporamaScreen using;

    public VideoLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getVideoQueueSize(), param.getVideoRandom()) {
            @Override
            protected MediaPlayer generateMedia(String fileName) {
                LOG.fine( ()-> "generating media " + fileName);
                return new MediaPlayer(new Media(fileName));
            }
        });
        using = null;
        lock = new Object();
    }

    public synchronized boolean canUse(DiaporamaScreen ds) {
        synchronized (lock) {
            if (!isEmpty()) {
                if (using == null){
                    LOG.fine(() -> ds + " Acquires me");
                    using = ds;
                    return true;
                } else if (using == ds){
                    LOG.fine(ds + " already owns me");
                    return true;
                } else {
                    LOG.fine(() -> ds + " cannot use me, i am under " + using + " control");
                }
            }
            return false;
        }
    }

    public synchronized void release(DiaporamaScreen ds){
        synchronized (lock) {
            if (using == ds){
                LOG.fine(ds + " Release me");
                using = null;
            } else {
                LOG.severe(() -> ds + " tried to release me while " + using + " owns me");
            }
        }
    }

    @Override
    public MediaPlayer getNext() {
        LOG.severe(() -> "I dont have your identity, so i dont trust you. sending null");
        return null;
    }

    public MediaPlayer getNext(DiaporamaScreen ds) throws InterruptedException, IllegalAccessException {
        synchronized (lock){
            if (using == ds){
                LOG.fine(() -> "Hi current master, sending you an object");
                return super.getNext();
            } else {
                LOG.info(() -> "You don't own me, so i cry " + ds);
                throw new IllegalAccessException(ds + " tried to get a MediaPlayer without acquiring ownership first");
            }
        }
    }
}
