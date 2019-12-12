package diaporama.medialoader;

import diaporama.ProgramParameters;
import diaporama.medialoader.loaders.ImageLoader;
import diaporama.medialoader.loaders.VideoLoader;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

public class MediaLoader {
    private static final Logger LOG = Logger.getLogger(MediaLoader.class.getName());
    private final ImageLoader imageLoader;
    private final VideoLoader videoLoader;

    private final List<FileFinder> finders;

    public MediaLoader( ProgramParameters param){
        imageLoader = new ImageLoader(param);
        videoLoader = new VideoLoader(param);
        finders = new LinkedList<>();

        for(String p : param.getPaths()){
            FileFinder ff = new FileFinder(Paths.get(p), param.getExtensions(), imageLoader, videoLoader);
            finders.add(ff);
            Thread t = new Thread(ff);
            t.start();
            LOG.log(FINE, ()-> "Started FileFinder for " + p);
        }

        imageLoader.start();
        videoLoader.start();


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
