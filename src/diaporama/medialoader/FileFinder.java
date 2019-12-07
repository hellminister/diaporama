package diaporama.medialoader;

import diaporama.medialoader.loaders.ImageLoader;
import diaporama.medialoader.loaders.VideoLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileFinder implements Runnable {
    private static final Logger LOG = Logger.getLogger(FileFinder.class.getName());

    private Path directory;
    private Map<String,String> extensions;
    private ImageLoader imageBag;
    private VideoLoader videoBag;
    private AtomicBoolean running;

    public FileFinder(Path directory, Map<String, String> accExtensions, ImageLoader imageBag,
                      VideoLoader videoBag) {
        this.directory = directory;
        this.extensions = accExtensions;
        this.imageBag = imageBag;
        this.videoBag = videoBag;

        running = new AtomicBoolean(false);
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);

        try (Stream<Path> walk = Files.walk(directory)) {
            var toLoop = walk.filter(Files::isRegularFile).map(q -> {
                try {
                    return q.toUri().toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    LOG.log(Level.SEVERE, e, () -> "Got a malformed url for " + q.toString() + "\n" + e.toString());
                }
                return "";
            })
                    .filter(Predicate.not(String::isBlank))
                    .filter(s -> this.extensions.containsKey(s.toLowerCase().substring(s.lastIndexOf("."))))
                    .iterator();

            while (toLoop.hasNext() && running.get()) {
                String s = toLoop.next();
                String ext = extensions.get(s.toLowerCase().substring(s.lastIndexOf(".")));
                if ("Image".equals(ext)){
                    imageBag.addFileName(s);
                } else if ("Video".equals(ext)){
                    videoBag.addFileName(s);
                }
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e, () -> "Trouble while walking this path " + directory + "\n" + e.toString());
        }
        LOG.log(Level.INFO, () -> this.getClass().getName() + " Stopped");

    }
}
