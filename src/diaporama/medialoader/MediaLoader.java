package diaporama.medialoader;

import diaporama.medialoader.loaders.ImageLoader;
import diaporama.medialoader.loaders.VideoLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MediaLoader {
    private static final Logger LOG = Logger.getLogger(MediaLoader.class.getName());
    private ImageLoader imageLoader;
    private VideoLoader videoLoader;

    private List<FileFinder> finders;

    public MediaLoader(Set<String> paths, Map<String, String> extensions){
        imageLoader = new ImageLoader();
        videoLoader = new VideoLoader();
        finders = new LinkedList<>();

        for(String p : paths){
            FileFinder ff = new FileFinder(Paths.get(p),extensions, imageLoader, videoLoader);
            finders.add(ff);
            Thread t = new Thread(ff);
            t.start();
        }

        imageLoader.start();
        videoLoader.start();


    }

    private Set<Path> extractDirs(Set<String> paths) {
        Set<Path> allDir = new HashSet<>();

        for (String path : paths){
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                allDir.addAll(walk.parallel().filter(Files::isDirectory).collect(Collectors.toSet())
                );

            } catch (IOException e) {
                LOG.log(Level.SEVERE, e, () -> "Trouble while walking this path " + path + "\n" + e.toString());
            }
        }

        return allDir;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public VideoLoader getVideoLoader() {
        return videoLoader;
    }

    public void stop(){
        for(FileFinder ff : finders){
            ff.stop();
        }
        imageLoader.stop();
        videoLoader.stop();
    }
}
