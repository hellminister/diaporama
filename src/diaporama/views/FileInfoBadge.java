package diaporama.views;

import diaporama.ProgramParameters;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileInfoBadge extends VBox {
    private final Label creationDate;
    private final Label filename;

    private final DateTimeFormatter datePattern;

    public FileInfoBadge(ProgramParameters param) {
        setAlignment(Pos.CENTER_RIGHT);

        setStyle("-fx-background-color: rgba(" + param.getFileInfoBadgeBackgroundColor().getRed() + ", " + param.getFileInfoBadgeBackgroundColor().getGreen() +
                ", " + param.getFileInfoBadgeBackgroundColor().getBlue() + ", " + param.getFileInfoBadgeBackgroundOpacity() + ")");

        setOpacity(param.getFileInfoBadgeOpacity());

        if (param.getShowCreationDate()){
            creationDate = new Label();
            datePattern = DateTimeFormatter.ofPattern(param.getFileCreationDatePattern());

            var background = new StackPane();
            background.setAlignment(Pos.CENTER_RIGHT);

            background.getChildren().add(creationDate);
            creationDate.setTextFill(param.getFileInfoBadgeCreationTimeColor());
            creationDate.setFont(new Font(param.getFileInfoBadgeCreationTimeFont(), param.getFileInfoBadgeCreationTimeFontSize()));
            getChildren().add(background);
        } else {
            creationDate = null;
            datePattern = null;
        }

        if (param.getShowFileName()){
            filename = new Label();
            var background = new StackPane();
            background.setAlignment(Pos.CENTER_RIGHT);

            background.getChildren().add(filename);
            filename.setTextFill(param.getFileInfoBadgeFileNameColor());
            filename.setFont(new Font(param.getFileInfoBadgeFileNameFont(), param.getFileInfoBadgeFileNameFontSize()));

            getChildren().add(background);
        } else {
            filename = null;
        }

    }

    public void setFilename(String name){
        if (filename != null){
            filename.setText(name);
        }
    }

    public void setCreationDate(LocalDateTime time){
        if (creationDate != null){
            creationDate.setText(time.format(datePattern));
        }
    }

}