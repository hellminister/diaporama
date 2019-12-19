package diaporama.views;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.VideoLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.logging.Logger;

/**
 * This class takes care of starting/controlling videos and the transition at the start and end of it
 * Only 1 of the instances created can play at a time
 */
public class VideoTransitioning extends Transitioning<MediaView, VideoLoader>{
    private static final Logger LOG = Logger.getLogger(VideoTransitioning.class.getName());

    private final FadeTransition mft;
    private final Duration bufferingTime;

    /**
     * creates and set the video transition used to start and stop a video
     * @param videoLoader The loader that contains all the videos to show
     * @param ds the screen to which this node is attached
     * @param parameters The parameters of the program
     */
    VideoTransitioning(VideoLoader videoLoader, DiaporamaScreen ds, ProgramParameters parameters) {
        super(videoLoader, new MediaView(), ds);
        bufferingTime = Duration.seconds(parameters.getVideoBufferingTime());

        // Fade in
        FadeTransition sft = new FadeTransition(Duration.millis(parameters.getVideoFadeTime()));
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        // Keep video on screen
        mft = new FadeTransition();
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        // Fade out
        FadeTransition eft = new FadeTransition(Duration.millis(parameters.getVideoFadeTime()));
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        transition.getChildren().addAll(sft, mft, eft);
    }

    @Override
    protected void actionsOnFinished() {
        producer.release(currentPlaying);
    }

    /**
     * prepares and starts the Video to play
     * @throws InterruptedException was unable to get a video to play
     * @throws IllegalAccessException wasn't allowed to get a video to play
     */
    @Override
    public void prepareAndStart() throws InterruptedException, IllegalAccessException {
        changeMediaToShow();
        shower.setVisible(true);
    }

    @Override
    protected void changeMediaToShow() throws InterruptedException, IllegalAccessException {
        MediaPlayer mp = producer.getNext(currentPlaying);
        shower.setMediaPlayer(mp);

        LOG.fine(() -> "preparing video to play " + mp.getStatus());

        mp.setOnStalled(() -> {
            mft.pause();
            mp.pause();

            LOG.fine(() -> "mediaPlayer stalled");

            buffering(mp);

            mp.play();
            mft.play();
        });

        if (mp.getStatus() != MediaPlayer.Status.READY) {
            mp.setOnReady(() -> {
                LOG.fine(() -> "Buffering mediaPlayer");
                doOnceReady(mp);
            });
        } else {
            LOG.fine(() -> "Buffering mediaPlayer after ready" );
            doOnceReady(mp);
        }
    }


    /**
     * Sets and prepares the different parts of the transition once the MediaPlayer got all its info
     * @param mp the MediaPlayer on which we are waiting
     */
    private void doOnceReady(MediaPlayer mp) {
        buffering(mp);
        mp.setAutoPlay(true);

        mft.setDuration(mp.getMedia().getDuration());

        LOG.fine(() -> "starting the video " + mp.getStatus() + " with a mft duration of " + mft.getDuration());
        transition.playFromStart();
    }

    /**
     * Gets at least the time specified in the setup file in the buffer
     * @param mp the MediaPlayer to buffer
     */
    private void buffering(MediaPlayer mp) {
        boolean enoughBuffer;
        do {
            Duration bufferTime = mp.getBufferProgressTime().subtract(mp.getCurrentTime());
            enoughBuffer = bufferTime.greaterThanOrEqualTo(bufferingTime)
                            || mp.getBufferProgressTime().greaterThanOrEqualTo(mp.getMedia().getDuration());
            LOG.fine(() -> "Buffered Time " + bufferTime);
            LOG.fine(() -> "bufferTime.greaterThanOrEqualTo(bufferingTime) " + bufferTime.greaterThanOrEqualTo(bufferingTime));
            LOG.fine(() -> "mp.getBufferProgressTime().greaterThanOrEqualTo(mp.getMedia().getDuration()) " + mp.getBufferProgressTime().greaterThanOrEqualTo(mp.getMedia().getDuration()));
            if (!enoughBuffer) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.info(e::getMessage);
                }
            }
        } while (!enoughBuffer);
    }

    /**
     * @return whether this transition can be used
     */
    boolean canUse(){
        return producer.canUse(currentPlaying);
    }

    @Override
    public synchronized void pause() {
        transition.pause();
        shower.getMediaPlayer().pause();
    }

    @Override
    public synchronized void unpause() {
        transition.play();
        shower.getMediaPlayer().play();
    }
}
