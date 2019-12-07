package diaporama;

import diaporama.medialoader.MediaLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
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
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


class DiaporamaScreen extends Scene {
    private static final Logger LOG = Logger.getLogger(DiaporamaScreen.class.getName());

    private final ProgramParameters parameters;

    private Screen screen;

    private final StackPane root;
    private ImageView imageNode;
    private Pane imagePane;

    private MediaLoader mediaLoader;
    private SequentialTransition st;

    public DiaporamaScreen(MediaLoader media, Screen screen, ProgramParameters param) {
        super(new StackPane());
        parameters = param;
        root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: black");

        this.screen = screen;

        mediaLoader = media;


        StackPane mainPane = new StackPane();
        mainPane.setAlignment(Pos.CENTER);


            createImagePane();


        mainPane.getChildren().add(imagePane);

        linkBackgroundSizeToRootPane();

        root.getChildren().add(createCenteringPanesFor(mainPane));

        setOnKeyReleased(this::onKeyReleasedActions);

        setTransitions();

        st.play();
    }

    private void setTransitions() {
        FadeTransition sft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()), imageNode);
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        FadeTransition mft = new FadeTransition(Duration.millis(parameters.getImageShowTime()), imageNode);
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        FadeTransition eft = new FadeTransition(Duration.millis(parameters.getImageFadeTime()), imageNode);
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        st = new SequentialTransition(sft, mft, eft);

        st.setOnFinished(e ->{
            try {
                imageNode.setImage(mediaLoader.getImageLoader().getNext());
                st.playFromStart();
            } catch (NoSuchElementException | InterruptedException ex) {
                LOG.log(INFO, ex::toString);
            }

        });
    }

    private void onKeyReleasedActions(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            st.stop();
            mediaLoader.stop();
            Platform.exit();
        }
    }

    private void createImagePane() {
        imageNode = new ImageView();
        imageNode.setPreserveRatio(true);

        imageNode.setFitHeight(screen.getBounds().getHeight());
        imageNode.setFitWidth(screen.getBounds().getWidth());

        imagePane = new Pane();
        imagePane.getChildren().add(imageNode);
    }

    private void linkBackgroundSizeToRootPane() {
        root.setPrefSize(screen.getBounds().getWidth(), screen.getBounds().getHeight());
        imageNode.fitHeightProperty().bind(root.heightProperty());
        imageNode.fitWidthProperty().bind(root.widthProperty());
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

}
