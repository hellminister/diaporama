package diaporama;

import javafx.scene.paint.Color;

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
    private final double imageScaleTime;
    private final double imageScaleHorTime;
    private final double imageScaleVertTime;
    private final double videoFadeTime;
    private final double imageShowTime;
    private final boolean videoRandom;
    private final boolean imageRandom;
    private final int imageQueueSize;
    private final int videoQueueSize;
    private final int videoStartChance;
    private final double videoBufferingTime;
    private final Duration filePathUpdateTime;
    private final String imageTransitionType;
    private final List<String> sleepTime;
    private final List<String> wakeTime;
    private final String clockFont;
    private final double clockFontSize;
    private final Color clockColor;
    private final boolean showClock;
    private final double clockTopDistance;
    private final double clockLeftDistance;
    private final Color clockBackgroundColor;
    private final double clockBackgroundOpacity;
    private final double clockOpacity;
    private final boolean showCreationDate;
    private final boolean showFileName;
    private final double infoBadgeBottomDistance;
    private final double infoBadgeRightDistance;
    private final String fileCreationDatePattern;
    private final Color fileInfoBadgeBackgroundColor;
    private final double fileInfoBadgeBackgroundOpacity;
    private final double fileInfoBadgeOpacity;
    private final Color fileInfoBadgeCreationTimeColor;
    private final String fileInfoBadgeCreationTimeFont;
    private final double fileInfoBadgeCreationTimeFontSize;
    private final Color fileInfoBadgeFileNameColor;
    private final String fileInfoBadgeFileNameFont;
    private final double fileInfoBadgeFileNameFontSize;

    /**
     * Reads the files and sets the values of the program parameters
     * @param setupPath the setup file path
     */
    public ProgramParameters(String setupPath) {
        LOG.info( ()-> setupPath);

        // this section is used to extracts the parameters value from the settings file
        // usually only the last read value is used
        // and to set the default values of the parameters
        var dirDefault = new HashSet<String>();
        var extDefault = new HashMap<String, String>();
        var imFadeTimeDefault = 500d;
        var imScaleTimeDefault = 500d;
        var imScaleHorTimeDefault = 500d;
        var imScaleVertTimeDefault = 500d;
        var imShowTimeDefault = 4000d;
        var vidFadeTimeDefault = 500d;
        var vidRdmDefault = true;
        var imRdmDefault = true;
        var imQueueSizeDefault = 10;
        var vidQueueSizeDefault = 2;
        var vidStartChanceDefault = 100;
        var vidBufferingTimeDefault = 10.0;
        var filePathUpdateDefault = Duration.ofDays(1);
        var imageTransitionTypeDefault = "LinkedRandom";
        var sleepTimeDefault = new LinkedList<String>();
        var wakeTimeDefault = new LinkedList<String>();
        var clockFontTemp = "Arial";
        var clockFontSizeTemp = 72d;
        var clockColorTemp = Color.CYAN;
        var showClockTemp = false;
        var clockTopDistanceTemp = 50.0;
        var clockLeftDistanceTemp = 50.0;
        var clockBackgroundColorTemp = Color.BLACK;
        var clockBackgroundOpacityTemp = 0.5d;
        var clockOpacityTemp = 1d;
        var showFileNameTemp = false;
        var showCreationDateTemp = false;
        var infoBadgeBottomDistanceTemp = 50d;
        var infoBadgeRightDistanceTemp = 50d;
        var fileCreationDatePatternTemp = "dd/MM/yyyy";
        var fileInfoBadgeBackgroundColorTemp = Color.BLACK;
        var fileInfoBadgeBackgroundOpacityTemp = 0.5d;
        var fileInfoBadgeOpacityTemp = 1d;
        var fileInfoBadgeCreationTimeColorTemp = Color.CYAN;
        var fileInfoBadgeCreationTimeFontTemp = "Arial";
        var fileInfoBadgeCreationTimeFontSizeTemp = 36d;
        var fileInfoBadgeFileNameColorTemp = Color.CYAN;
        var fileInfoBadgeFileNameFontTemp = "Arial";
        var fileInfoBadgeFileNameFontSizeTemp = 18d;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(setupPath)))){
            String line = reader.readLine();
            String treatingSection = "";

            while (line != null){
                if (line.startsWith("##")){
                    treatingSection = line.substring(2);
                } else if (!line.isBlank() && !line.startsWith(";")){
                    // skips blank lines and line starting with ; (comment lines)
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
                        case "ImageScaleTime" :
                            imScaleTimeDefault = Double.parseDouble(line);
                            break;
                        case "ImageScaleHorTime" :
                            imScaleHorTimeDefault = Double.parseDouble(line);
                            break;
                        case "ImageScaleVertTime" :
                            imScaleVertTimeDefault = Double.parseDouble(line);
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
                        case "ImageTransitionType":
                            imageTransitionTypeDefault = line;
                            break;
                        case "ScreenSleep":
                            sleepTimeDefault.add(line);
                            break;
                        case "ScreenWake":
                            wakeTimeDefault.add(line);
                            break;
                        case "Show-Clock":
                            showClockTemp = "true".equals(line);
                            break;
                        case "Clock-Font":
                            clockFontTemp = line;
                            break;
                        case "Clock-Font-Size":
                            clockFontSizeTemp = Double.parseDouble(line);
                            break;
                        case "Clock-Color":
                            try {
                                clockColorTemp = Color.valueOf(line.toUpperCase());
                            } catch (Exception e) {
                                clockColorTemp = Color.CYAN;
                                String finalLine1 = line;
                                LOG.warning(() -> "Couldn't read color : " + finalLine1 + " defaulting to CYAN");
                            }
                            break;
                        case "Clock-Top":
                            clockTopDistanceTemp = Double.parseDouble(line);
                            break;
                        case "Clock-Left":
                            clockLeftDistanceTemp = Double.parseDouble(line);
                            break;
                        case "Clock-Background-Color":
                            try {
                                clockBackgroundColorTemp = Color.valueOf(line.toUpperCase());
                            } catch (Exception e) {
                                clockBackgroundColorTemp = Color.BLACK;
                                String finalLine1 = line;
                                LOG.warning(() -> "Couldn't read color : " + finalLine1 + " defaulting to BLACK");
                            }
                            break;
                        case "Clock-Background-Opacity":
                            clockBackgroundOpacityTemp = Double.parseDouble(line);
                            break;
                        case "Clock-Opacity":
                            clockOpacityTemp = Double.parseDouble(line);
                            break;
                        case "Show-FileName":
                            showFileNameTemp = "true".equals(line);
                            break;
                        case "Show-CreationTime":
                            showCreationDateTemp = "true".equals(line);
                            break;
                        case "FileInfoBadge-Bottom":
                            infoBadgeBottomDistanceTemp = Double.parseDouble(line);
                            break;
                        case "FileInfoBadge-Right":
                            infoBadgeRightDistanceTemp = Double.parseDouble(line);
                            break;
                        case "FileInfoBadge-Background-Color":
                            try {
                                fileInfoBadgeBackgroundColorTemp = Color.valueOf(line.toUpperCase());
                            } catch (Exception e) {
                                fileInfoBadgeBackgroundColorTemp = Color.BLACK;
                                String finalLine1 = line;
                                LOG.warning(() -> "Couldn't read color : " + finalLine1 + " defaulting to BLACK");
                            }
                            break;
                        case "FileInfoBadge-Background-Opacity":
                            fileInfoBadgeBackgroundOpacityTemp = Double.parseDouble(line);
                            break;
                        case "FileInfoBadge-Opacity":
                            fileInfoBadgeOpacityTemp = Double.parseDouble(line);
                            break;
                        case "FileInfo-CreationDate-Pattern":
                            fileCreationDatePatternTemp = line;
                            break;
                        case "FileInfo-CreationDate-Font":
                            fileInfoBadgeCreationTimeFontTemp = line;
                            break;
                        case "FileInfo-CreationDate-Font-Size":
                            fileInfoBadgeCreationTimeFontSizeTemp = Double.parseDouble(line);
                            break;
                        case "FileInfo-CreationDate-Color":
                            try {
                                fileInfoBadgeCreationTimeColorTemp = Color.valueOf(line.toUpperCase());
                            } catch (Exception e) {
                                fileInfoBadgeCreationTimeColorTemp = Color.CYAN;
                                String finalLine1 = line;
                                LOG.warning(() -> "Couldn't read color : " + finalLine1 + " defaulting to CYAN");
                            }
                            break;
                        case "FileInfo-FileName-Font":
                            fileInfoBadgeFileNameFontTemp = line;
                            break;
                        case "FileInfo-FileName-Font-Size":
                            fileInfoBadgeFileNameFontSizeTemp = Double.parseDouble(line);
                            break;
                        case "FileInfo-FileName-Color":
                            try {
                                fileInfoBadgeFileNameColorTemp = Color.valueOf(line.toUpperCase());
                            } catch (Exception e) {
                                fileInfoBadgeFileNameColorTemp = Color.CYAN;
                                String finalLine1 = line;
                                LOG.warning(() -> "Couldn't read color : " + finalLine1 + " defaulting to CYAN");
                            }
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

        // This section sets the parameters in the correct field
        paths = Collections.unmodifiableSet(dirDefault);
        extensions = Collections.unmodifiableMap(extDefault);
        imageFadeTime = imFadeTimeDefault;
        imageScaleTime = imScaleTimeDefault;
        imageScaleHorTime = imScaleHorTimeDefault;
        imageScaleVertTime = imScaleVertTimeDefault;
        imageShowTime = imShowTimeDefault;
        videoFadeTime = vidFadeTimeDefault;
        imageRandom = imRdmDefault;
        videoRandom = vidRdmDefault;
        imageQueueSize = imQueueSizeDefault;
        videoQueueSize = vidQueueSizeDefault;
        videoStartChance = vidStartChanceDefault;
        videoBufferingTime = vidBufferingTimeDefault;
        filePathUpdateTime = filePathUpdateDefault;
        imageTransitionType = imageTransitionTypeDefault;
        sleepTime = Collections.unmodifiableList(sleepTimeDefault);
        wakeTime = Collections.unmodifiableList(wakeTimeDefault);
        clockFont = clockFontTemp;
        clockFontSize = clockFontSizeTemp;
        clockColor = clockColorTemp;
        showClock = showClockTemp;
        clockTopDistance = clockTopDistanceTemp;
        clockLeftDistance = clockLeftDistanceTemp;
        clockBackgroundColor = clockBackgroundColorTemp;
        clockBackgroundOpacity = clockBackgroundOpacityTemp;
        clockOpacity = clockOpacityTemp;
        showCreationDate = showCreationDateTemp;
        showFileName = showFileNameTemp;
        infoBadgeBottomDistance = infoBadgeBottomDistanceTemp;
        infoBadgeRightDistance = infoBadgeRightDistanceTemp;
        fileCreationDatePattern = fileCreationDatePatternTemp;
        fileInfoBadgeBackgroundColor = fileInfoBadgeBackgroundColorTemp;
        fileInfoBadgeBackgroundOpacity = fileInfoBadgeBackgroundOpacityTemp;
        fileInfoBadgeOpacity = fileInfoBadgeOpacityTemp;
        fileInfoBadgeCreationTimeColor = fileInfoBadgeCreationTimeColorTemp;
        fileInfoBadgeCreationTimeFont = fileInfoBadgeCreationTimeFontTemp;
        fileInfoBadgeCreationTimeFontSize = fileInfoBadgeCreationTimeFontSizeTemp;
        fileInfoBadgeFileNameColor = fileInfoBadgeFileNameColorTemp;
        fileInfoBadgeFileNameFont = fileInfoBadgeFileNameFontTemp;
        fileInfoBadgeFileNameFontSize = fileInfoBadgeFileNameFontSizeTemp;


        LOG.info(() -> "Paths                                " + paths.toString() + ls +
                       "Extensions                           " + extensions.toString() + ls +
                       "Image Fade Time                      " + imageFadeTime + ls +
                       "Image Show Time                      " + imageScaleTime + ls +
                       "Image Show_Hor Time                  " + imageScaleHorTime + ls +
                       "Image Show_Vert Time                 " + imageScaleVertTime + ls +
                       "Image Show Time                      " + imageShowTime + ls +
                       "Video Fade Time                      " + videoFadeTime + ls +
                       "Random Image?                        " + imageRandom + ls +
                       "Random Video?                        " + videoRandom + ls +
                       "Image Queue Size                     " + imageQueueSize + ls +
                       "Video Queue Size                     " + videoQueueSize + ls +
                       "Video Starting Chance                " + videoStartChance + ls +
                       "Video Buffering Time                 " + videoBufferingTime + ls +
                       "Filepath Update Time                 " + filePathUpdateTime + ls +
                       "Image Transition Type                " + imageTransitionType + ls +
                       "Sleeping Times                       " + sleepTime + ls +
                       "Waking Times                         " + wakeTime + ls +
                       "Show Clock                           " + showClock + ls +
                       "Clock Font                           " + clockFont + ls +
                       "Clock Font Size                      " + clockFontSize + ls +
                       "Clock Color                          " + clockColor + ls +
                       "Clock Distance from Top              " + clockTopDistance + ls +
                       "Clock Distance from Left             " + clockLeftDistance + ls +
                       "Clock Background Color               " + clockBackgroundColor + ls +
                       "Clock Background Opacity             " + clockBackgroundOpacity + ls +
                       "Clock Opacity                        " + clockOpacity + ls +
                       "Show File Name                       " + showFileName + ls +
                       "Show File Creation Time              " + showCreationDate + ls +
                       "File Info Badge Distance From Bottom " + infoBadgeBottomDistance + ls +
                       "File Info Badge Distance From Right  " + infoBadgeRightDistance + ls +
                       "File Info Badge Background Color     " + fileInfoBadgeBackgroundColor + ls +
                       "File Info Badge Background Opacity   " + fileInfoBadgeBackgroundOpacity + ls +
                       "File Info Badge Opacity              " + fileInfoBadgeOpacity + ls +
                       "File Creation Time Pattern           " + fileCreationDatePattern + ls +
                       "File Creation Time Font              " + fileInfoBadgeCreationTimeFont + ls +
                       "File Creation Time Font Size         " + fileInfoBadgeCreationTimeFontSize + ls +
                       "File Creation Time Color             " + fileInfoBadgeCreationTimeColor + ls +
                       "File Name Font                       " + fileInfoBadgeFileNameFont + ls +
                       "File Name Font Size                  " + fileInfoBadgeFileNameFontSize + ls +
                       "File Name Color                      " + fileInfoBadgeFileNameColor + ls);

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
     * ##ImageScaleTime
     * @return The time it take for the image to scale in or out
     */
    public double getImageScaleTime() {
        return imageScaleTime;
    }

    /**
     * ##ImageScaleHorTime
     * @return The time it take for the image to scale horizontally in or out
     */
    public double getImageScaleHorTime() {
        return imageScaleHorTime;
    }

    /**
     * ##ImageScaleVertTime
     * @return The time it take for the image to scale vertically in or out
     */
    public double getImageScaleVertTime() {
        return imageScaleVertTime;
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

    /**
     * ##ImageTransitionType
     * a string with one of the value from Type.java or Random or LinkedRandom
     * @return The transition to use with images
     */
    public String getImageTransitionType() { return imageTransitionType;}

    public List<String> getSleepTime() {
        return sleepTime;
    }

    public List<String> getWakeTime() {
        return wakeTime;
    }

    public String getClockFont() {
        return clockFont;
    }

    public double getClockFontSize() {
        return clockFontSize;
    }

    public Color getClockColor() {
        return clockColor;
    }

    public boolean getShowClock() {
        return showClock;
    }

    public double getClockTopDistance() {
        return clockTopDistance;
    }

    public double getClockLeftDistance() {
        return clockLeftDistance;
    }

    public Color getClockBackgroundColor() {
        return clockBackgroundColor;
    }

    public double getClockBackgroundOpacity() {
        return clockBackgroundOpacity;
    }

    public double getClockOpacity() {
        return clockOpacity;
    }

    public boolean getShowFileName() {
        return showFileName;
    }

    public boolean getShowCreationDate() {
        return showCreationDate;
    }

    public double getInfoBadgeBottomDistance() {
        return infoBadgeBottomDistance;
    }

    public double getInfoBadgeRightDistance() {
        return infoBadgeRightDistance;
    }

    public String getFileCreationDatePattern() {
        return fileCreationDatePattern;
    }

    public Color getFileInfoBadgeBackgroundColor() {
        return fileInfoBadgeBackgroundColor;
    }

    public double getFileInfoBadgeBackgroundOpacity() {
        return fileInfoBadgeBackgroundOpacity;
    }

    public double getFileInfoBadgeOpacity() {
        return fileInfoBadgeOpacity;
    }

    public Color getFileInfoBadgeCreationTimeColor() {
        return fileInfoBadgeCreationTimeColor;
    }

    public String getFileInfoBadgeCreationTimeFont() {
        return fileInfoBadgeCreationTimeFont;
    }

    public double getFileInfoBadgeCreationTimeFontSize() {
        return fileInfoBadgeCreationTimeFontSize;
    }

    public Color getFileInfoBadgeFileNameColor() {
        return fileInfoBadgeFileNameColor;
    }

    public String getFileInfoBadgeFileNameFont() {
        return fileInfoBadgeFileNameFont;
    }

    public double getFileInfoBadgeFileNameFontSize() {
        return fileInfoBadgeFileNameFontSize;
    }
}