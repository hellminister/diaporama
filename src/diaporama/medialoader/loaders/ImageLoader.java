package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.nio.file.Path;
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
            protected Image generateMedia(Path fileName) {
                LOG.log(Level.INFO, ()-> "generating image " + fileName);
                try {
                    return new Image(fileName.toUri().toURL().toExternalForm(), true);
                } catch (MalformedURLException e) {
                    LOG.severe(e::toString);
                }
                return null;
            }
        });
    }
}