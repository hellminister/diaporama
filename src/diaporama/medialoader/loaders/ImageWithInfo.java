package diaporama.medialoader.loaders;

import javafx.scene.image.Image;

public class ImageWithInfo {

    private int orientation;
    private Image image;
    public ImageWithInfo(Image img, int orientation) {
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