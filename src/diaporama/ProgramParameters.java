package diaporama;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

/**
 * Reads and Contains the parameters of the program read from the given setup file
 */
public final class ProgramParameters {
    private static final Logger LOG = Logger.getLogger(ProgramParameters.class.getName());
    private static final String ls = System.lineSeparator();


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
    private final double videoBufferingTime;
    private final Duration filePathUpdateTime;

    /**
     * Reads the files and sets the values of the program parameters
     * @param setupPath the setup file path
     */
    public ProgramParameters(String setupPath) {
        LOG.info( ()-> setupPath);

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
        var vidBufferingTimeDefault = 10.0;
        var filePathUpdateDefault = Duration.ofDays(1);

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
                            break;
                        case "Image-RDM":
                            imRdmDefault = "true".equals(line);
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
                        case "Video-Buffering-Time":
                            vidBufferingTimeDefault = Double.parseDouble(line);
                            break;
                        case "File-Paths-Update-Time":
                            filePathUpdateDefault = Duration.parse(line);
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
            LOG.severe(e::toString);
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
        videoBufferingTime = vidBufferingTimeDefault;
        filePathUpdateTime = filePathUpdateDefault;

        LOG.info(() -> "Paths                 " + paths.toString() + ls +
                       "Extensions            " + extensions.toString() + ls +
                       "Image Fade Time       " + imageFadeTime + ls +
                       "Image Show Time       " + imageShowTime + ls +
                       "Video Fade Time       " + videoFadeTime + ls +
                       "Random Image?         " + imageRandom + ls +
                       "Random Video?         " + videoRandom + ls +
                       "Image Queue Size      " + imageQueueSize + ls +
                       "Video Queue Size      " + videoQueueSize + ls +
                       "Video Starting Chance " + videoStartChance + ls +
                       "Video Buffering Time  " + videoBufferingTime + ls +
                       "Filepath Update Time  " + filePathUpdateTime + ls);

    }

    /**
     * returns the paths in which to look for the files
     * ##Paths
     * @return an unmodifiable set containing the path names
     */
    public Set<String> getPaths() {
        return paths;
    }

    /**
     * returns the extensions to keep as well as the type of media they are related to
     * ##Image-Extensions
     * ##Video-Extensions
     * @return an unmodifiable map mapping extension -> media type
     */
    public Map<String, String> getExtensions() {
        return extensions;
    }

    /**
     * ##ImageFadeTime
     * @return The time it take for the image to fade in or out
     */
    public double getImageFadeTime() {
        return imageFadeTime;
    }

    /**
     * ##VideoFadeTime
     * @return The time it take for the video to fade in or out
     */
    public double getVideoFadeTime() {
        return videoFadeTime;
    }

    /**
     * ##ImageShowTime
     * @return The time the image stays on the screen
     */
    public double getImageShowTime() {
        return imageShowTime;
    }

    /**
     * ##Image-Queue-Size
     * @return the number of Image that are to be waiting at the same time
     */
    public int getImageQueueSize() {
        return imageQueueSize;
    }

    /**
     * ##Image-RDM
     * @return whether to read the images sequentially or randomly
     */
    public boolean getImageRandom() {
        return imageRandom;
    }

    /**
     * ##Video-RDM
     * @return whether to read the videos sequentially or randomly
     */
    public boolean getVideoRandom() {
        return videoRandom;
    }

    /**
     * ##Video-Queue-Size
     * @return the number of Media that are to be waiting at the same time
     */
    public int getVideoQueueSize() {
        return videoQueueSize;
    }

    /**
     * ##Video-Start-Chance
     * @return the per thousand chance that a video will be chosen to be shown next
     */
    public int getVideoStartChance() {
        return videoStartChance;
    }

    /**
     * ##Video-Buffering-Time
     * @return the number of seconds to buffer if the video stall
     */
    public double getVideoBufferingTime() {
        return videoBufferingTime;
    }

    /**
     * ##File-Paths-Update-Time
     * @return the wait duration in milliseconds before rewalking the path to get new files
     */
    public long getUpdateTime() {
        return filePathUpdateTime.toMillis();
    }
}
