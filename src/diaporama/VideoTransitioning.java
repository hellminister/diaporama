package diaporama;

import diaporama.medialoader.loaders.VideoLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class VideoTransitioning implements TransitionAnimation<MediaView> {
    private static AtomicBoolean free;

    static {
        free = new AtomicBoolean(true);
    }

    private SequentialTransition transition;
    private MediaView media;
    private VideoLoader producer;
    private FadeTransition mft;
    private DiaporamaScreen currentPlaying;

    VideoTransitioning(VideoLoader videoLoader, DiaporamaScreen ds, ProgramParameters parameters) {
        media = new MediaView();
        producer = videoLoader;

        currentPlaying = ds;

        FadeTransition sft = new FadeTransition(Duration.millis(parameters.getVideoFadeTime()));
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        mft = new FadeTransition();
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        FadeTransition eft = new FadeTransition(Duration.millis(parameters.getVideoFadeTime()));
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        transition = new SequentialTransition(sft, mft, eft);
        transition.setNode(media);

        transition.setOnFinished(e -> {
            try {
                free.set(true);
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
        MediaPlayer mp = producer.getNext();
        mp.setAutoPlay(true);
        media.setMediaPlayer(mp);

        mft.setDuration(media.getMediaPlayer().getMedia().getDuration());
    }

    @Override
    public MediaView getView(){
        return media;
    }

    synchronized boolean canUse(){
        return free.compareAndSet(true, false) && !producer.isEmpty();
    }
}
