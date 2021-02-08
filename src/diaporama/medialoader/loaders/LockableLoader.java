package diaporama.medialoader.loaders;

import diaporama.views.DiaporamaScreen;
import javafx.scene.media.MediaPlayer;

import java.util.logging.Logger;

/**
 * A media loader that lets only 1 Diaporama screen show its medias at a time
 * @param <T> The type of media that is produced by this loader
 */
public class LockableLoader<T extends MediaWithInfo> extends Loader<T> {
    private static final Logger LOG = Logger.getLogger(LockableLoader.class.getName());

    protected final Object lock;      // a lock to synchronize access
    protected DiaporamaScreen using;  // the current user of this loader, null if there's none
    /**
     * creates a new Loader
     *
     * @param filler the queue that returns the objects
     */
    public LockableLoader(QueueFiller<T> filler) {
        super(filler);
        using = null;
        lock = new Object();
    }

    /**
     * check if the caller (hopefully) can use this
     * and if true, takes ownership
     * @param ds the caller (hopefully)
     * @return true if the owner is the caller or there's no owner, else false
     */
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

    /**
     * releases ownership if the caller (hopefully) owns it
     * @param ds the caller (hopefully)
     */
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

    /**
     * Gives the next MediaPlayer to play if the caller (hopefully) is the owner
     * @param ds the caller (hopefully)
     * @return  The MediaPlayer to play
     * @throws InterruptedException     If no MediaPlayer is available after a certain thing
     * @throws IllegalAccessException   If the caller doesn't owe the lock
     */
    public T getNext(DiaporamaScreen ds) throws InterruptedException, IllegalAccessException {
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

    /**
     * This is the Loader getNext that is overridden to block its use
     * This is to respect the ownership
     * @return null ALWAYS!
     */
    @Override
    public T getNext() {
        LOG.severe(() -> "I dont have your identity, so i dont trust you. sending null");
        return null;
    }
}