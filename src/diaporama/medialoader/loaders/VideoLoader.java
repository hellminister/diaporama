package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Managers the video objects
 * Only 1 video can be shown at a time, preventing other callers from pulling a video
 * if it didnt reserve this object
 */
public class VideoLoader extends LockableLoader<MediaPlayer> {
    private static final Logger LOG = Logger.getLogger(VideoLoader.class.getName());

    public VideoLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getVideoQueueSize(), param.getVideoRandom()) {
            @Override
            protected MediaPlayer generateMedia(Path fileName) {
                LOG.fine( ()-> "generating media " + fileName);
                try {
                    return new MediaPlayer(new Media(fileName.toUri().toURL().toExternalForm()));
                } catch (MalformedURLException e) {
                    LOG.severe(e::toString);
                }
                return null;
            }
        });

    }
}