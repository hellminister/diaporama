package diaporama;

import diaporama.medialoader.MediaLoader;
import diaporama.views.DiaporamaScreen;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main class of the slide shower
 */
public class Diaporama extends Application {

    private static final Logger LOG = Logger.getLogger(Diaporama.class.getName());
    private ProgramParameters parameters;

    private MediaLoader media;

    /**
     * the main function of the application
     * @param primaryStage the primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        var param = getParameters().getRaw();
        LOG.log(Level.INFO, () -> "Command line parameters received : " + param.toString());

        String setupFile = !param.isEmpty() ? param.get(0) : "";

        parameters = new ProgramParameters(setupFile);

        media = new MediaLoader(parameters);
        List<Stage> stages = new LinkedList<>();
        stages.add(primaryStage);

        var screens = Screen.getScreens();

        // creates 1 stage per screen
        if (screens.size() == 1){
            setStage(stages.get(0), screens.get(0));
        } else {
            boolean first = true;
            for (Screen screen : screens){
                if (first){
                    setStage(stages.get(0), screen);
                    first = false;
                } else {
                    Stage stage = new Stage();
                    stages.add(stage);
                    setStage(stage, screen);
                }
            }
        }
    }

    /**
     * Attaches a scene to a stage
     * links it to a screen
     * sets the stage
     * @param stage the stage to set
     * @param screen the screen to attach to the stage
     */
    private void setStage(Stage stage, Screen screen) {
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(new DiaporamaScreen(media, screen, parameters));
        stage.sizeToScene();
        stage.setX(screen.getBounds().getMinX());
        stage.setY(screen.getBounds().getMinY());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
