package diaporama.views.screenshutdown;

import java.util.logging.Logger;

public class ScreenSleeperFactory {
    private static final Logger LOG = Logger.getLogger(ScreenSleeperFactory.class.getName());

    public static ScreenSleeperRun getScreenSleeperRun() {

        String os = System.getProperty("os.name");

        LOG.info(() -> os);

        ScreenSleeperRun ssr;
        switch (os){
            case "Windows 10":
                LOG.info("Windows 10 sleeper");
                ssr = new NothingScreenSleeperRun();
                break;
            default:
                ssr = new NothingScreenSleeperRun();
        }

        return ssr;

    }
}