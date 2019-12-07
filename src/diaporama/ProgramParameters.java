package diaporama;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProgramParameters {
    private static final Logger LOG = Logger.getLogger(ProgramParameters.class.getName());

    public final Set<String> imageDirs;
    public final Map<String, String> extensions;
    public final double imageFadeTime;
    public final double imageShowTime;

    public ProgramParameters(String setupPath) {
        LOG.log(Level.INFO, ()-> setupPath);

        var imDirTemp = new HashSet<String>();
        var extTemp = new HashMap<String, String>();
        var imFadeTimeTemp = 500d;
        var imShowTimeTemp = 4000d;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(setupPath)))){
            String line = reader.readLine();
            String treatingSection = "";

            while (line != null){
                if (line.startsWith("##")){
                    treatingSection = line.substring(2);
                } else if (line.startsWith(";")) {
                    // ignore this line, it is comments
                } else if (!line.isBlank()){
                    switch (treatingSection){
                        case "ImagePaths" :
                            imDirTemp.add(line);
                            break;
                        case "Image-Extensions" :
                            String finalLine = line;
                            extTemp.put(line, "Image");
                            break;
                        case "Movie-Extensions":
                            String finalLine2 = line;
                            extTemp.put(line, "Video");
                            break;
                        case "ImageFadeTime" :
                            imFadeTimeTemp = Double.parseDouble(line);
                            break;
                        case "ImageShowTime" :
                            imShowTimeTemp = Double.parseDouble(line);
                            break;
                    }
                }
                line = reader.readLine();
            }



        } catch (IOException e) {
            LOG.log(Level.SEVERE, e::toString);
        }

        imageDirs = Collections.unmodifiableSet(imDirTemp);
        extensions = Collections.unmodifiableMap(extTemp);
        imageFadeTime = imFadeTimeTemp;
        imageShowTime = imShowTimeTemp;
    }

    public Set<String> getImageDirs() {
        return imageDirs;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public double getImageFadeTime() {
        return imageFadeTime;
    }

    public double getImageShowTime() {
        return imageShowTime;
    }
}
