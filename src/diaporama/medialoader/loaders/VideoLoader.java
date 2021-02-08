package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Managers the video objects
 * Only 1 video can be shown at a time, preventing other callers from pulling a video
 * if it didnt reserve this object
 */
public class VideoLoader extends LockableLoader<MediaPlayerWithInfo> {
    private static final Logger LOG = Logger.getLogger(VideoLoader.class.getName());

    public VideoLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getVideoQueueSize(), param.getVideoRandom()) {
            @Override
            protected MediaPlayerWithInfo generateMedia(Path fileName) {
                LOG.fine( ()-> "generating media " + fileName);
                try {
                    var media = new MediaPlayer(new Media(fileName.toUri().toURL().toExternalForm()));

                    var filename = fileName.toString();

                    LocalDateTime creationDate = null;
                    if (param.getShowCreationDate()) {
                        try {
                            BasicFileAttributes attr = Files.readAttributes(fileName, BasicFileAttributes.class);
                            creationDate = attr.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return new MediaPlayerWithInfo(creationDate, filename, media);
                } catch (MalformedURLException e) {
                    LOG.severe(e::toString);
                }
                return null;
            }
        });

    }
}