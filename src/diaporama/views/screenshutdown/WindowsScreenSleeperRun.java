package diaporama.views.screenshutdown;

//import com.sun.jna.Native;
//import com.sun.jna.platform.win32.WinDef.HWND;
//import com.sun.jna.platform.win32.WinDef.LPARAM;
//import com.sun.jna.platform.win32.WinDef.LRESULT;
//import com.sun.jna.platform.win32.WinUser;
//import com.sun.jna.win32.StdCallLibrary;

import java.lang.annotation.Native;
import java.util.logging.Logger;

/**
 * This class controls the sleep mode of the screen for the Windows OS
 * This uses jna and the jna library
 * Will need to reactive
 */
public class WindowsScreenSleeperRun implements ScreenSleeperRun {
    private static final Logger LOG = Logger.getLogger(WindowsScreenSleeperRun.class.getName());
//    public interface User32 extends StdCallLibrary {
//        User32 INSTANCE = Native.load("user32", User32.class);
//        int SC_MONITOR_POWER = 0xF170;
//        int SC_MONITOR_OFF = 2;
//        int SC_MONITOR_ON = -1;
//
//
//        LRESULT SendMessageA(HWND paramHWND, int paramInt, int paramInt2,
//                             LPARAM paramLPARAM);
//    }

//    final User32 user32 = User32.INSTANCE;


    @Override
    public void sleepScreen() {
        LOG.info(() -> "sleeping screens");
//        user32.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
//                User32.SC_MONITOR_POWER, new LPARAM(User32.SC_MONITOR_OFF));
    }

    @Override
    public void wakeScreen() {
        LOG.info(() -> "waking screens");
//        user32.SendMessageA(WinUser.HWND_BROADCAST, WinUser.WM_SYSCOMMAND,
//                User32.SC_MONITOR_POWER, new LPARAM(User32.SC_MONITOR_ON));
    }
}