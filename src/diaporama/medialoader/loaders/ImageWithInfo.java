package diaporama.medialoader.loaders;

import javafx.scene.image.Image;

import java.time.LocalDateTime;

/**
 * The image object with its file info
 */
public class ImageWithInfo extends MediaWithInfo {

    private final int orientation;
    private final Image image;

    public ImageWithInfo(Image img, int orientation, LocalDateTime creationDate, String filename) {
        super(creationDate, filename);
        image = img;
        this.orientation = orientation;
    }

    /**
     * @return the contained Image
     */
    public Image getImage(){
        return image;
    }

    /**
     * @return The image orientation
     */
    public int getOrientation() {
        return orientation;
    }


}