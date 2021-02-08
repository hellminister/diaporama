package diaporama.medialoader.loaders;

import javafx.scene.media.MediaPlayer;

import java.time.LocalDateTime;

/**
 * The mediaPlayer object with its file info
 */
public class MediaPlayerWithInfo extends MediaWithInfo{
    private final MediaPlayer mediaPlayer;

    public MediaPlayerWithInfo(LocalDateTime creationDate, String filename, MediaPlayer mediaPlayer) {
        super(creationDate, filename);
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * @return the MediaPlayer object
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}