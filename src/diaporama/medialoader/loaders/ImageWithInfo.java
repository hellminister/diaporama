package diaporama.medialoader.loaders;

import javafx.scene.image.Image;

import java.time.LocalDateTime;

public class ImageWithInfo extends MediaWithInfo {

    private final int orientation;
    private final Image image;

    public ImageWithInfo(Image img, int orientation, LocalDateTime creationDate, String filename) {
        super(creationDate, filename);
        image = img;
        this.orientation = orientation;
    }

    public Image getImage(){
        return image;
    }

    public int getOrientation() {
        return orientation;
    }


}