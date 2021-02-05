package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Images
 */
public class ImageLoaderAWT extends Loader<Image> {
    private static final Logger LOG = Logger.getLogger(ImageLoaderAWT.class.getName());

    public ImageLoaderAWT(ProgramParameters param) {
        super(new QueueFiller<>(param.getImageQueueSize(), param.getImageRandom()) {
            @Override
            protected Image generateMedia(String fileName) {
                LOG.log(Level.INFO, ()-> "generating image " + fileName);

                Image img = null;
                try {
                    img = SwingFXUtils.toFXImage(ImageIO.read(new File(fileName)), null);
                } catch (IOException e) {
                    LOG.severe(e::toString);
                }
                return img;
            }
        });
    }
}
