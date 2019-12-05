package diaporama;

import diaporama.imageloader.ImageLoader;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
import static java.util.logging.Level.SEVERE;


public class DiaporamaScreen extends Scene {
    private static final Logger LOG = Logger.getLogger(DiaporamaScreen.class.getName());

    private Screen screen;
    private Rectangle2D screenSize;

    private final StackPane root;
    private final StackPane mainPane;
    private ImageView backgroundPlate;
    private Pane backgroundImagePane;

    private ImageLoader imageLoader;
    private SequentialTransition st;
    private FadeTransition sft;
    private FadeTransition mft;
    private FadeTransition eft;

    public DiaporamaScreen(ImageLoader images, Screen screen) {
        super(new StackPane());
        root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: black");

        this.screen = screen;
        screenSize = this.screen.getBounds();

        imageLoader = images;


        mainPane = new StackPane();
        mainPane.setAlignment(Pos.CENTER);

        try {
            createBackgroundImagePane();
        } catch (InterruptedException e) {
            LOG.log(SEVERE,e , ()-> e.toString());
        }

        mainPane.getChildren().add(backgroundImagePane);

        linkBackgroundSizeToRootPane();

        root.getChildren().add(createCenteringPanesFor(mainPane));

        setOnKeyReleased(this::onKeyReleasedActions);

        sft = new FadeTransition(Duration.millis(1000), backgroundPlate);
        sft.setFromValue(0.0);
        sft.setToValue(1.0);
        sft.setCycleCount(1);

        mft = new FadeTransition(Duration.millis(3000), backgroundPlate);
        mft.setFromValue(1.0);
        mft.setToValue(1.0);
        mft.setCycleCount(1);

        eft = new FadeTransition(Duration.millis(1000), backgroundPlate);
        eft.setFromValue(1.0);
        eft.setToValue(0.0);
        eft.setCycleCount(1);

        st = new SequentialTransition(sft, mft, eft);

        st.setOnFinished(e ->{
            try {
                backgroundPlate.setImage(imageLoader.getNextImage());
                st.playFromStart();
            } catch (NoSuchElementException | InterruptedException ex) {
                LOG.log(INFO, ()-> ex.toString());
            }

        });

        st.play();


    }

    private void onKeyReleasedActions(KeyEvent e) {


        switch (e.getCode()) {
            case ESCAPE:
                st.stop();
                imageLoader.stop();
                Platform.exit();
                break;
            default:
                break;
        }
    }

    private void createBackgroundImagePane() throws NoSuchElementException, InterruptedException {
        backgroundPlate = new ImageView(imageLoader.getNextImage());
        backgroundPlate.setPreserveRatio(true);

        backgroundPlate.setFitHeight(screenSize.getHeight());
        backgroundPlate.setFitWidth(screenSize.getWidth());

        backgroundImagePane = new Pane();
        backgroundImagePane.getChildren().add(backgroundPlate);
    }

    private void linkBackgroundSizeToRootPane() {
        root.setPrefSize(screenSize.getWidth(), screenSize.getHeight());
        backgroundPlate.fitHeightProperty().bind(root.heightProperty());
        backgroundPlate.fitWidthProperty().bind(root.widthProperty());
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
