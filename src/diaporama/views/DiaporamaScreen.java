package diaporama.views;

import diaporama.ProgramParameters;
import diaporama.medialoader.MediaLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;


import javafx.scene.control.Label;

import java.time.LocalTime;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The slide show scene
 */
public class DiaporamaScreen extends Scene {
    private static final Logger LOG = Logger.getLogger(DiaporamaScreen.class.getName());

    private final MediaLoader mediaLoader;

    private final RandomImageTransitioning imageTransition;
    private final VideoTransitioning videoTransition;

    private final Random rdm;

    private final int videoStartChance;

    private Transitioning<?, ?> current;
    private ScreenSleeper screenSleeper;

    /**
     * creates the slide show scene
     * @param media the media loader to use
     * @param screen the screen to which this scene is attached
     * @param param the program parameters
     */
    public DiaporamaScreen(MediaLoader media, Screen screen, ProgramParameters param) {
        super(new StackPane());

        mediaLoader = media;

        videoStartChance = param.getVideoStartChance();

        rdm = new Random();

        var root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: black");
        root.setPrefSize(screen.getBounds().getWidth(), screen.getBounds().getHeight());

        imageTransition = new RandomImageTransitioning(mediaLoader.getImageLoader(), this, param);

        // sets the Node that will contains Images/Photos
        var imageNode = imageTransition.getView();

        imageNode.setPreserveRatio(true);
        imageNode.fitHeightProperty().bind(root.heightProperty());
        imageNode.fitWidthProperty().bind(root.widthProperty());


        videoTransition = new VideoTransitioning(mediaLoader.getVideoLoader(), this, param);

        // sets the Node that will play Videos
        var videoNode = videoTransition.getView();
        videoNode.setPreserveRatio(true);
        videoNode.fitHeightProperty().bind(root.heightProperty());
        videoNode.fitWidthProperty().bind(root.widthProperty());

        root.getChildren().add(createCenteringPanesFor(imageNode));
        root.getChildren().add(createCenteringPanesFor(videoNode));

        var infoPane = createInfoPane(param);

        root.getChildren().add(infoPane);
        infoPane.toFront();

        setOnKeyReleased(this::onKeyReleasedActions);

        try {
            nextAnimation();
        } catch (InterruptedException | IllegalAccessException e) {
            LOG.severe( ()-> "got exception trying to first start animation " + e.toString());
        }

    }

    private AnchorPane createInfoPane(ProgramParameters param) {
        var infoPane = new AnchorPane();
        infoPane.setStyle("-fx-background-color: transparent");

        if (param.getShowClock()) {
            createClock(param, infoPane);
        }

        return infoPane;
    }

    private void createClock(ProgramParameters param, AnchorPane infoPane) {
        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: rgba(" + param.getClockBackgroundColor().getRed() + ", " + param.getClockBackgroundColor().getGreen() + ", " + param.getClockBackgroundColor().getBlue() + ", " + param.getClockBackgroundOpacity() + ")");
        background.setOpacity(param.getClockOpacity());
        Label time = new Label();
        time.setTextFill(param.getClockColor());
        time.setFont(new Font(param.getClockFont(), param.getClockFontSize()));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            time.setText(currentTime.getHour() + ":" + String.format("%02d",currentTime.getMinute()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        background.getChildren().add(time);
        infoPane.getChildren().addAll(background);
        AnchorPane.setTopAnchor(background, param.getClockTopDistance());
        AnchorPane.setLeftAnchor(background, param.getClockLeftDistance());
    }


    /**
     * Actions to do on a key released
     * For now it quits the program on ESC
     * @param e the key event
     */
    private void onKeyReleasedActions(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            mediaLoader.stop();
            screenSleeper.stop();
            Platform.exit();
        }
    }

    /**
     * sets the given pane in a pane that will keep it always centered
     * @param toCenter the pane to keep centered
     * @return The centered pane
     */
    private static Pane createCenteringPanesFor(Node toCenter) {
        Pane container = new Pane();
        container.getChildren().add(toCenter);

        VBox verticalCentering = new VBox();
        verticalCentering.setAlignment(Pos.CENTER);

        HBox horizontalCentering = new HBox();
        horizontalCentering.setAlignment(Pos.CENTER);

        verticalCentering.getChildren().add(horizontalCentering);
        horizontalCentering.getChildren().add(container);

        return verticalCentering;
    }

    /**
     * choose and runs the next animation
     * @throws InterruptedException a problem getting the next object to show
     * @throws IllegalAccessException wasn't allowed to get the next video
     */
    public void nextAnimation() throws InterruptedException, IllegalAccessException {
        int value = rdm.nextInt(1000);

        if (value <= videoStartChance && (videoTransition.canUse())){
            LOG.info(() -> this + " chose a video");
            current = videoTransition;
        } else {
            LOG.info(() -> this + " chose a photo");
            current = imageTransition;
        }

        current.prepareAndStart();

    }

    public void pause() {
        current.pause();
    }

    public void unpause() {
        current.unpause();
    }

    public void registerSleeper(ScreenSleeper ss) {
        screenSleeper = ss;
    }
}