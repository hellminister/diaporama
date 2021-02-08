package diaporama.medialoader.loaders;

import java.time.LocalDateTime;

/**
 * A superclass for media that contains the file name and the file creation date
 */
public class MediaWithInfo {
    protected final LocalDateTime creationDate;
    protected final String filename;

    public MediaWithInfo(LocalDateTime creationDate, String filename) {
        this.creationDate = creationDate;
        this.filename = filename;
    }

    /**
     * @return the file's creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return The file's name
     */
    public String getFilename() {
        return filename;
    }
}