package diaporama.views;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.ImageLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.logging.Logger;

/**
 * This class takes care of showing Images and the transition at the start and end of it
 */
public class ImageTransitioning {
    private static final Logger LOG = Logger.getLogger(ImageTransitioning.class.getName());

    private final SequentialTransition transition;
    private final ImageView image;
    private final ImageLoader producer;
    private final DiaporamaScreen currentPlaying;

    /**
     * creates and set the image transition used to show an image
     * @param imageLoader The loader that contains all the images to show
     * @param ds the screen to which this node is attached
     * @param parameters The parameters of the program
     */
    ImageTransitioning(ImageLoader imageLoader, DiaporamaScreen ds, ProgramParameters parameters) {
        image = new ImageView();
        producer = imageLoader;
        currentPlaying = ds;

        // Fade in
        FadeTransition sft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()));
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        // Keep the image on screen
        FadeTransition mft = new FadeTransition(Duration.millis(parameters.getImageShowTime()));
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        // Fade out
        FadeTransition eft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()));
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        transition = new SequentialTransition(sft, mft, eft);
        transition.setNode(image);

        transition.setOnFinished(e -> {
            try {
                currentPlaying.nextAnimation();
            } catch (InterruptedException | IllegalAccessException ex) {
                LOG.severe(ex::toString);
            }
        });
    }

    /**
     * prepares and shows the images to show
     * @throws InterruptedException was unable to get an image to show
     */
    public void prepareAndStart() throws InterruptedException {
        image.setImage(producer.getNext());
        transition.playFromStart();
    }

    /**
     * @return The Node that can show the image
     */
    public ImageView getView(){
        return image;
    }
}
