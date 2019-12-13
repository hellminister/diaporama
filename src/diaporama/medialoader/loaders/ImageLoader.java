package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import javafx.scene.image.Image;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Images
 */
public class ImageLoader extends Loader<Image> {
    private static final Logger LOG = Logger.getLogger(ImageLoader.class.getName());

    public ImageLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getImageQueueSize(), param.getImageRandom()) {
            @Override
            protected Image generateMedia(String fileName) {
                LOG.log(Level.FINE, ()-> "generating image " + fileName);
                return new Image(fileName);
            }
        });
    }
}
