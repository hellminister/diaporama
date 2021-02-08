package diaporama.medialoader.loaders;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import diaporama.ProgramParameters;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Images
 */
public class ImageLoader extends Loader<ImageWithInfo> {
    private static final Logger LOG = Logger.getLogger(ImageLoader.class.getName());

    public ImageLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getImageQueueSize(), param.getImageRandom()) {
            @Override
            protected ImageWithInfo generateMedia(Path fileName) {
                LOG.log(Level.INFO, ()-> "generating image " + fileName);
                Image img = null;
                int orientation = 1;
                String filename = "";
                LocalDateTime creationDate = null;
                try {
                    img = new Image(fileName.toUri().toURL().toExternalForm(), true);

                    if (param.getShowCreationDate()) {
                        BasicFileAttributes attr = Files.readAttributes(fileName, BasicFileAttributes.class);
                        creationDate = attr.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }

                    if (param.getShowFileName()){
                        filename = fileName.toString();
                    }

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
                return new ImageWithInfo(img, orientation, creationDate, filename);
            }
        });
    }
}