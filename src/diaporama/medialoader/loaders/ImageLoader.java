package diaporama.medialoader.loaders;

import diaporama.medialoader.QueueFiller;
import javafx.scene.image.Image;

public class ImageLoader extends Loader<Image> {


    public ImageLoader() {
        super(new QueueFiller<>(10) {
            @Override
            protected Image generateMedia(String fileName) {
                return new Image(fileName);
            }
        });
    }
}
