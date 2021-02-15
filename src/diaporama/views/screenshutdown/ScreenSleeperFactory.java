package diaporama.views.screenshutdown;

import java.util.logging.Logger;

/**
 * Creates a ScreenSleeperRun object based on the OS this program is running on
 */
public class ScreenSleeperFactory {
    private static final Logger LOG = Logger.getLogger(ScreenSleeperFactory.class.getName());

    public static ScreenSleeperRun getScreenSleeperRun() {

        String os = System.getProperty("os.name");

        LOG.info(() -> os);

        ScreenSleeperRun ssr;
        switch (os){
            case "Windows 10":
                LOG.info("Windows 10 sleeper");
                ssr = new WindowsScreenSleeperRun();
                break;
            default:
                ssr = new NothingScreenSleeperRun();
        }

        return ssr;

    }
}