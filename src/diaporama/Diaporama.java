package diaporama;

import diaporama.medialoader.MediaLoader;
import diaporama.views.DiaporamaScreen;
import diaporama.views.ScreenSleeper;
import diaporama.views.WindowsScreenSleeperRun;
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


        var lds = new LinkedList<DiaporamaScreen>();

        // creates 1 stage per screen
        if (screens.size() == 1){
            var ds = setStage(stages.get(0), screens.get(0));
            lds.add(ds);
        } else {
            boolean first = true;
            for (Screen screen : screens){
                if (first){
                    var ds = setStage(stages.get(0), screen);
                    first = false;
                    lds.add(ds);
                } else {
                    Stage stage = new Stage();
                    stages.add(stage);
                    var ds = setStage(stage, screen);
                    lds.add(ds);
                }
            }
        }

        ScreenSleeper ss = new ScreenSleeper(parameters, lds);

        for (var ds : lds){
            ds.registerSleeper(ss);
        }
    }

    /**
     * Attaches a scene to a stage
     * links it to a screen
     * sets the stage
     * @param stage the stage to set
     * @param screen the screen to attach to the stage
     */
    private DiaporamaScreen setStage(Stage stage, Screen screen) {
        stage.initStyle(StageStyle.UNDECORATED);

        var ds = new DiaporamaScreen(media, screen, parameters);

        stage.setScene(ds);
        stage.sizeToScene();
        stage.setX(screen.getBounds().getMinX());
        stage.setY(screen.getBounds().getMinY());

        stage.show();

        return ds;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
