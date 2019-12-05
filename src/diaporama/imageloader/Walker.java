package diaporama.imageloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

class Walker implements Runnable {
    private static final Logger LOG = Logger.getLogger(Walker.class.getName());

    private String directory;
    private Set<String> accExtensions;
    private List<String> bag;
    private AtomicBoolean running;

    public Walker(String directory, Set<String> accExtensions, List<String> bag) {
        this.directory = directory;
        this.accExtensions = accExtensions;
        this.bag = bag;
        running = new AtomicBoolean(false);
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);

        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
            var toLoop = walk.filter(Files::isRegularFile).map(q -> {
                try {
                    return q.toUri().toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    LOG.log(Level.SEVERE, e, () -> "Got a malformed url for " + q.toString() + "\n" + e.toString());
                }
                return "";
            })
                    .filter(Predicate.not(String::isBlank))
                    .filter(s -> this.accExtensions.contains(s.toLowerCase().substring(s.lastIndexOf("."))))
                    .iterator();

            while (toLoop.hasNext() && running.get()) {
                bag.add(toLoop.next());
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e, () -> "Trouble while walking this path " + directory + "\n" + e.toString());
        }
    }
}
