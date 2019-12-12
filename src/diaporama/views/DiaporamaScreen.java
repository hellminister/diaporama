package diaporama.views;

import diaporama.ProgramParameters;
import diaporama.medialoader.MediaLoader;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DiaporamaScreen extends Scene {
    private static final Logger LOG = Logger.getLogger(DiaporamaScreen.class.getName());

    private final Screen screen;

    private final StackPane root;
    private final ImageView imageNode;

    private final MediaLoader mediaLoader;
    private final MediaView videoNode;
    private final ImageTransitioning imageTransition;
    private final VideoTransitioning videoTransition;

    private final Random rdm;

    private final int videoStartChance;

    public DiaporamaScreen(MediaLoader media, Screen screen, ProgramParameters param) {
        super(new StackPane());
        videoStartChance = param.getVideoStartChance();

        rdm = new Random();

        root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: black");
        mediaLoader = media;


        imageTransition = new ImageTransitioning(mediaLoader.getImageLoader(), this, param);
        imageNode = imageTransition.getView();

        videoTransition = new VideoTransitioning(mediaLoader.getVideoLoader(), this, param);
        videoNode = videoTransition.getView();

        this.screen = screen;

        Pane imagePane = createImagePane();
        Pane videoPane = createVideoPane();


        linkBackgroundSizeToRootPane();

        root.getChildren().add(createCenteringPanesFor(imagePane));
        root.getChildren().add(createCenteringPanesFor(videoPane));

        setOnKeyReleased(this::onKeyReleasedActions);

        try {
            nextAnimation();
        } catch (InterruptedException | IllegalAccessException e) {
            LOG.log(Level.SEVERE, ()-> "got exception trying to first start animation " + e.toString());
        }

    }


    private void onKeyReleasedActions(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            mediaLoader.stop();
            Platform.exit();
        }
    }

    private Pane createVideoPane(){
        videoNode.setPreserveRatio(true);

        videoNode.setFitHeight(screen.getBounds().getHeight());
        videoNode.setFitHeight(screen.getBounds().getWidth());

        Pane videoPane = new Pane();
        videoPane.getChildren().add(videoNode);

        return videoPane;
    }

    private Pane createImagePane() {
        imageNode.setPreserveRatio(true);

        imageNode.setFitHeight(screen.getBounds().getHeight());
        imageNode.setFitWidth(screen.getBounds().getWidth());

        var imagePane = new Pane();
        imagePane.getChildren().add(imageNode);
        return imagePane;
    }

    private void linkBackgroundSizeToRootPane() {
        root.setPrefSize(screen.getBounds().getWidth(), screen.getBounds().getHeight());

        imageNode.fitHeightProperty().bind(root.heightProperty());
        imageNode.fitWidthProperty().bind(root.widthProperty());

        videoNode.fitHeightProperty().bind(root.heightProperty());
        videoNode.fitWidthProperty().bind(root.widthProperty());
    }


    private VBox createCenteringPanesFor(Pane toCenter) {
        VBox verticalCentering = new VBox();
        verticalCentering.setAlignment(Pos.CENTER);

        HBox horizontalCentering = new HBox();
        horizontalCentering.setAlignment(Pos.CENTER);

        verticalCentering.getChildren().add(horizontalCentering);
        horizontalCentering.getChildren().add(toCenter);

        return verticalCentering;
    }

    public void nextAnimation() throws InterruptedException, IllegalAccessException {
        int value = rdm.nextInt(1000);

        if (value <= videoStartChance && (videoTransition.canUse())){
            LOG.info(() -> this + " chose a video");
            videoTransition.prepareAndStart();
        } else {
            LOG.info(() -> this + " chose a photo");
            imageTransition.prepareAndStart();
        }

    }
}
