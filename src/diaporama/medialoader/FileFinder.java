package diaporama.medialoader;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.Loader;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Passes through all files and subfolders from a given folder
 * to find all usable files
 */
public class FileFinder implements Runnable {
    private static final Logger LOG = Logger.getLogger(FileFinder.class.getName());

    private final Path directory;
    private final Map<String,String> extensions;
    private final Loader<Image> imageBag;
    private final Loader<MediaPlayer> videoBag;
    private int ranXTime;

    /**
     *
     * @param directory the folder to monitor
     * @param imageBag the image manager
     * @param videoBag the video manager
     * @param params the program parameters
     */
    public FileFinder(Path directory, Loader<Image> imageBag,
                      Loader<MediaPlayer> videoBag, ProgramParameters params) {
        this.directory = directory;
        this.extensions = params.getExtensions();
        this.imageBag = imageBag;
        this.videoBag = videoBag;
        ranXTime = 0;
Optional<String> op;
    }

    /**
     * Extracts the full file names and places them in the proper container
     */
    @Override
    public void run() {
        LOG.info(() -> "Walking " + directory + " " + ++ranXTime + " time");

            try (Stream<Path> walk = Files.walk(directory)) {
                // an iterator that filters the files and returns the full path names in string
                var toLoop = walk.filter(Files::isRegularFile).map(q -> {
                    try {
                        return q.toUri().toURL().toExternalForm();
                    } catch (MalformedURLException e) {
                        LOG.severe(() -> "Got a malformed url for " + q.toString() + "\n" + e.toString());
                    }
                    return "";
                })
                        .filter(Predicate.not(String::isBlank))
                        .filter(s -> this.extensions.containsKey(s.toLowerCase().substring(s.lastIndexOf("."))))
                        .iterator();

                // collect all the found files to its proper container
                // use of iterator and while loop so we can stop execution if the thread is interrupted
                while (toLoop.hasNext() && !Thread.currentThread().isInterrupted()) {
                    String s = toLoop.next();
                    String ext = extensions.get(s.toLowerCase().substring(s.lastIndexOf(".")));
                    if ("Image".equals(ext)) {
                        imageBag.addFileName(s);
                    } else if ("Video".equals(ext)) {
                        videoBag.addFileName(s);
                    }
                }

            } catch (IOException e) {
                LOG.severe( () -> "Trouble while walking this path " + directory + "\n" + e.toString());
            }



        LOG.info(() -> "Finished walking " + directory + " for now");

    }
}
