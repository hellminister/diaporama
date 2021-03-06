package diaporama.views.screenshutdown;

import diaporama.ProgramParameters;
import diaporama.views.DiaporamaScreen;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Takes care of putting to sleep and waking the screen based on a given schedule
 */
public class ScreenSleeper {
    private static final Logger LOG = Logger.getLogger(ScreenSleeper.class.getName());

    private static final int SECONDS_IN_DAY = 24*3600;

    private final ScreenSleeperRun screenSleeperRun;

    private final ScheduledThreadPoolExecutor stpe;

    private final AtomicBoolean stopped;

    public ScreenSleeper(ProgramParameters param, List<DiaporamaScreen> lds) {
        screenSleeperRun = ScreenSleeperFactory.getScreenSleeperRun();

        stopped = new AtomicBoolean(false);

        stpe = new ScheduledThreadPoolExecutor(2);

        for (String s : param.getSleepTime()){
            LocalTime t = LocalTime.of(Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));
            int runIn = t.toSecondOfDay() - LocalTime.now().toSecondOfDay();
            if (runIn < 0){
                runIn = SECONDS_IN_DAY - runIn;
            }
            int finalRunIn = runIn;
            LOG.info(() -> "Sleep time in " + finalRunIn);
            stpe.scheduleAtFixedRate(() -> {
                LOG.info("starting sleep");
                screenSleeperRun.sleepScreen();
                for (var ds : lds){
                    ds.pause();
                }
            }, runIn, SECONDS_IN_DAY, TimeUnit.SECONDS);
        }

        for (String s : param.getWakeTime()){
            LocalTime t = LocalTime.of(Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));
            int runIn = t.toSecondOfDay() - LocalTime.now().toSecondOfDay();
            if (runIn < 0){
                runIn = SECONDS_IN_DAY - runIn;
            }
            int finalRunIn = runIn;
            LOG.info(() ->  "Wake time in " + finalRunIn);
            stpe.scheduleAtFixedRate(() -> {
                LOG.info("starting wake");
                screenSleeperRun.wakeScreen();
                for (var ds : lds){
                    ds.unpause();
                }
            }, runIn, SECONDS_IN_DAY, TimeUnit.SECONDS);

        }

        stpe.scheduleAtFixedRate(() -> LOG.info("Tic!"), 1, 1, TimeUnit.HOURS);

    }

    /**
     * Shuts down the ScreenSleeper
     */
    public void stop(){
        if (stopped.compareAndSet(false, true)) {
            stpe.shutdownNow();
        }
    }
}