package diaporama.medialoader.loaders;

import java.time.LocalDateTime;

public class MediaWithInfo {
    protected final LocalDateTime creationDate;
    protected final String filename;

    public MediaWithInfo(LocalDateTime creationDate, String filename) {
        this.creationDate = creationDate;
        this.filename = filename;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getFilename() {
        return filename;
    }
}