package diaporama.medialoader;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.*;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class managers the loading of the medias to show
 */
public class MediaLoader {
    private static final Logger LOG = Logger.getLogger(MediaLoader.class.getName());
    private final Loader<ImageWithInfo> imageLoader;
    private final LockableLoader<MediaPlayerWithInfo> videoLoader;

    private final ScheduledExecutorService findersExecutor;

    public MediaLoader( ProgramParameters param ) {
        imageLoader = new ImageLoader(param);
        videoLoader = new VideoLoader(param);

        findersExecutor = Executors.newScheduledThreadPool(param.getPaths().size());

        for(String p : param.getPaths()){
            FileFinder ff = new FileFinder(Paths.get(p), imageLoader, videoLoader, param);
            findersExecutor.scheduleWithFixedDelay(ff, 0, param.getUpdateTime(), TimeUnit.MILLISECONDS);
        }

        imageLoader.start();
        videoLoader.start();


    }

    /**
     * @return The object that manages the images
     */
    public Loader<ImageWithInfo> getImageLoader() {
        return imageLoader;
    }

    /**
     * @return The object that manages the videos
     */
    public LockableLoader<MediaPlayerWithInfo> getVideoLoader() {
        return videoLoader;
    }

    /**
     * Stops all the threads that are used by the managers
     */
    public void stop(){
        findersExecutor.shutdownNow();

        imageLoader.stop();
        videoLoader.stop();
    }
}