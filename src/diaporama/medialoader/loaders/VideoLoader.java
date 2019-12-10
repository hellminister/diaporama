package diaporama.medialoader.loaders;

import diaporama.ProgramParameters;
import diaporama.medialoader.QueueFiller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoLoader extends Loader<MediaPlayer> {
    private static final Logger LOG = Logger.getLogger(VideoLoader.class.getName());

    public VideoLoader(ProgramParameters param) {
        super(new QueueFiller<>(param.getVideoQueueSize(), param.getVideoRandom()) {
            @Override
            protected MediaPlayer generateMedia(String fileName) {
                LOG.log(Level.FINE, ()-> "generating media " + fileName);
                return new MediaPlayer(new Media(fileName));
            }
        });
    }
}
