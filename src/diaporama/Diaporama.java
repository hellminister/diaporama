package diaporama;

import diaporama.imageloader.ImageLoader;
import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Diaporama extends Application {

    private static final Logger LOG = Logger.getLogger(Diaporama.class.getName());
    private Set<String> ext;
    private Set<String> imagesDirs;

    private ImageLoader images;

    private List<Stage> stages;

    @Override
    public void start(Stage primaryStage) {
        ext = new HashSet<>();
        imagesDirs = new HashSet<>();

        var param = getParameters().getRaw();
        LOG.log(Level.INFO, () -> "Command line parameters received : " + param.toString());

        String setupFile = !param.isEmpty() ? param.get(0) : "";
        loadSetup(setupFile);

        images = new ImageLoader(imagesDirs, ext);
        stages = new LinkedList<>();
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

    public void loadSetup(String setupPath){
        LOG.log(Level.INFO, ()-> setupPath);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(setupPath)))){
            String line = reader.readLine();
            String treatingSection = "";

            while (line != null){
                if (line.startsWith("##")){
                    treatingSection = line.substring(2);
                } else if (!line.isBlank()){
                    switch (treatingSection){
                        case "Paths" :
                            imagesDirs.add(line);
                            break;
                        case "Extensions" :
                            ext.add(line);
                            break;
                    }
                }
                line = reader.readLine();
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e::toString);
        }
    }

    private void setStage(Stage stage, Screen screen) {
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(new DiaporamaScreen(images, screen));
        stage.sizeToScene();
        stage.setX(screen.getBounds().getMinX());
        stage.setY(screen.getBounds().getMinY());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);


    }
}
