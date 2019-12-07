package diaporama.medialoader.loaders;

import diaporama.medialoader.QueueFiller;
import javafx.scene.media.Media;

public class VideoLoader extends Loader<Media> {
    public VideoLoader() {
        super(new QueueFiller<>(2) {
            @Override
            protected Media generateMedia(String fileName) {
                return new Media(fileName);
            }
        });
    }
}
