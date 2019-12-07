package diaporama;

import diaporama.medialoader.MediaLoader;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Diaporama extends Application {

    private static final Logger LOG = Logger.getLogger(Diaporama.class.getName());
    private ProgramParameters parameters;

    private MediaLoader media;

    @Override
    public void start(Stage primaryStage) {
        var param = getParameters().getRaw();
        LOG.log(Level.INFO, () -> "Command line parameters received : " + param.toString());

        String setupFile = !param.isEmpty() ? param.get(0) : "";

        parameters = new ProgramParameters(setupFile);

        media = new MediaLoader(parameters.getImageDirs(), parameters.getExtensions());
        List<Stage> stages = new LinkedList<>();
        stages.add(primaryStage);

        var screens = Screen.getScreens();

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
