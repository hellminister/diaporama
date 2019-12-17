package diaporama.views;

import diaporama.ProgramParameters;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ScreenSleeper {
    private static final Logger LOG = Logger.getLogger(ScreenSleeper.class.getName());

    private static int SECONDS_IN_DAY = 24*3600;

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
            LOG.info(() -> "" + finalRunIn);
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
            LOG.info(() ->  "" + finalRunIn);
            stpe.scheduleAtFixedRate(() -> {
                LOG.info("starting wake");
                screenSleeperRun.wakeScreen();
                for (var ds : lds){
                    ds.unpause();
                }
            }, runIn, SECONDS_IN_DAY, TimeUnit.SECONDS);
        }

    }

    public void stop(){
        if (stopped.compareAndSet(false, true)) {
            stpe.shutdownNow();
        }
    }
}
