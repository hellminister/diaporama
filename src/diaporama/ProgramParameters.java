package diaporama;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public final class ProgramParameters {
    private static final Logger LOG = Logger.getLogger(ProgramParameters.class.getName());

    private final Set<String> paths;
    private final Map<String, String> extensions;
    private final double imageFadeTime;
    private final double videoFadeTime;
    private final double imageShowTime;
    private final boolean videoRandom;
    private final boolean imageRandom;
    private final int imageQueueSize;
    private final int videoQueueSize;
    private final int videoStartChance;

    public ProgramParameters(String setupPath) {
        LOG.log(INFO, ()-> setupPath);

        var dirDefault = new HashSet<String>();
        var extDefault = new HashMap<String, String>();
        var imFadeTimeDefault = 500d;
        var imShowTimeDefault = 4000d;
        var vidFadeTimeDefault = 500d;
        var vidRdmDefault = true;
        var imRdmDefault = true;
        var imQueueSizeDefault = 10;
        var vidQueueSizeDefault = 2;
        var vidStartChanceDefault = 100;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(setupPath)))){
            String line = reader.readLine();
            String treatingSection = "";

            while (line != null){
                if (line.startsWith("##")){
                    treatingSection = line.substring(2);
                } else //noinspection StatementWithEmptyBody
                    if (line.startsWith(";")) {
                    // ignore this line, it is comments
                } else if (!line.isBlank()){
                    switch (treatingSection){
                        case "Paths" :
                            dirDefault.add(line);
                            break;
                        case "Image-Extensions" :
                            extDefault.put(line, "Image");
                            break;
                        case "Video-Extensions":
                            extDefault.put(line, "Video");
                            break;
                        case "ImageFadeTime" :
                            imFadeTimeDefault = Double.parseDouble(line);
                            break;
                        case "ImageShowTime" :
                            imShowTimeDefault = Double.parseDouble(line);
                            break;
                        case "VideoFadeTime" :
                            vidFadeTimeDefault = Double.parseDouble(line);
                            break;
                        case "Video-RDM":
                            vidRdmDefault = "true".equals(line);
                            boolean finalVidRdmDefault = vidRdmDefault;
                            String finalLine2 = line;
                            LOG.info(() -> "Video-RDM value : " + finalVidRdmDefault + " line value " + finalLine2);
                            break;
                        case "Image-RDM":
                            imRdmDefault = "true".equals(line);
                            boolean finalimRdmDefault = imRdmDefault;
                            String finalLine1 = line;
                            LOG.info(() -> "Image-RDM value : " + finalimRdmDefault + " line value " + finalLine1);
                            break;
                        case "Image-Queue-Size":
                            imQueueSizeDefault = Integer.parseInt(line);
                            break;
                        case "Video-Queue-Size":
                            vidQueueSizeDefault = Integer.parseInt(line);
                            break;
                        case "Video-Start-Chance":
                            vidStartChanceDefault = Integer.parseInt(line);
                            break;
                        default:
                            String finalTreatingSection = treatingSection;
                            String finalLine = line;
                            LOG.info( ()-> "Unknown Section Name " + finalTreatingSection + " value : " + finalLine);
                    }
                }
                line = reader.readLine();
            }



        } catch (IOException e) {
            LOG.log(Level.SEVERE, e::toString);
        }

        paths = Collections.unmodifiableSet(dirDefault);
        extensions = Collections.unmodifiableMap(extDefault);
        imageFadeTime = imFadeTimeDefault;
        imageShowTime = imShowTimeDefault;
        videoFadeTime = vidFadeTimeDefault;
        imageRandom = imRdmDefault;
        videoRandom = vidRdmDefault;
        imageQueueSize = imQueueSizeDefault;
        videoQueueSize = vidQueueSizeDefault;
        videoStartChance = vidStartChanceDefault;

    }

    public Set<String> getPaths() {
        return paths;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public double getImageFadeTime() {
        return imageFadeTime;
    }

    public double getVideoFadeTime() {
        return videoFadeTime;
    }

    public double getImageShowTime() {
        return imageShowTime;
    }

    public int getImageQueueSize() {
        return imageQueueSize;
    }

    public boolean getImageRandom() {
        return imageRandom;
    }

    public boolean getVideoRandom() {
        return videoRandom;
    }

    public int getVideoQueueSize() {
        return videoQueueSize;
    }

    public int getVideoStartChance() {
        return videoStartChance;
    }
}
