package diaporama;

import diaporama.medialoader.loaders.ImageLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ImageTransitioning implements  TransitionAnimation<ImageView>{

    private SequentialTransition transition;
    private ImageView image;
    private ImageLoader producer;
    private DiaporamaScreen currentPlaying;


    ImageTransitioning(ImageLoader imageLoader, DiaporamaScreen ds, ProgramParameters parameters) {
        image = new ImageView();
        producer = imageLoader;
        currentPlaying = ds;

        FadeTransition sft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()));
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        FadeTransition mft = new FadeTransition(Duration.millis(parameters.getImageShowTime()));
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        FadeTransition eft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()));
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        transition = new SequentialTransition(sft, mft, eft);
        transition.setNode(image);

        transition.setOnFinished(e -> {
            try {
                currentPlaying.nextAnimation();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void start(){
        transition.playFromStart();
    }

    @Override
    public void changeImage() throws InterruptedException {
        image.setImage(producer.getNext());
    }

    @Override
    public ImageView getView(){
        return image;
    }
}
