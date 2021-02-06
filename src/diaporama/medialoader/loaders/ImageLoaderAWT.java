package diaporama.medialoader.loaders;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import diaporama.ProgramParameters;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Images
 */
public class ImageLoaderAWT extends Loader<ImageWithInfo> {
    private static final Logger LOG = Logger.getLogger(ImageLoaderAWT.class.getName());

    public ImageLoaderAWT(ProgramParameters param) {
        super(new QueueFiller<>(param.getImageQueueSize(), param.getImageRandom()) {
            @Override
            protected ImageWithInfo generateMedia(Path fileName) {
                LOG.log(Level.INFO, ()-> "generating image " + fileName);
                Image img = null;
                int orientation = 1;
                try {
                    img = new Image(fileName.toUri().toURL().toExternalForm(), true);
                    Metadata metadata = ImageMetadataReader.readMetadata(fileName.toFile());
                    Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

                    try {
                        orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                    } catch (Exception e){
                        LOG.info(e::toString);
                    }
                } catch (ImageProcessingException | IOException e) {
                    LOG.severe(e::toString);
                }
                return new ImageWithInfo(img, orientation);
            }
        });
    }
}