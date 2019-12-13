package diaporama.views;

import diaporama.medialoader.loaders.Loader;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;

import java.util.logging.Logger;

/**
 * Manages the transitions and showing of the media to show
 * @param <T> the Node type that shows the Media
 * @param <R> The Loader type that produces the Media to show
 */
public abstract class Transitioning<T extends Node, R extends Loader<?>> {
    private static final Logger LOG = Logger.getLogger(Transitioning.class.getName());

    protected final SequentialTransition transition;
    protected final T shower;
    protected final R producer;
    protected final DiaporamaScreen currentPlaying;

    /**
     * creates and sets the basis of the transition
     * @param prod the media producer
     * @param show the media shower
     * @param ds the owning DiaporamaScreen
     */
    protected Transitioning(R prod, T show, DiaporamaScreen ds){
        shower = show;
        producer = prod;
        currentPlaying = ds;
        transition = new SequentialTransition();
        transition.setNode(shower);

        transition.setOnFinished(e -> {
            try {
                actionsOnFinished();
                currentPlaying.nextAnimation();
            } catch (Exception ex) {
                LOG.severe(ex::toString);
            }
        });
    }

    /**
     * Override this if there's other actions to be taken after the transition finished playing
     * this is ran before calling for the next transition to show
     */
    protected void actionsOnFinished(){
        // do nothing by default
    }

    /**
     * prepares and shows the media to show
     * Override this if you dont want the transition to be started
     * @throws InterruptedException was unable to get an image to show
     */
    public void prepareAndStart() throws InterruptedException, IllegalAccessException {
        changeMediaToShow();
        transition.playFromStart();
    }

    /**
     * How to change the Media to show
     * @throws InterruptedException No media to get
     */
    protected abstract void changeMediaToShow() throws InterruptedException, IllegalAccessException;

    /**
     * @return The Node that can show the image
     */
    public T getView(){
        return shower;
    }
}
