package io.revxrsal.autolaunch;

import com.sun.jna.platform.win32.Win32Exception;
import org.jetbrains.annotations.NotNull;

import static com.sun.jna.platform.win32.Advapi32Util.*;
import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * The {@link PlatformAutoLaunch} implementation for Windows
 */
final class WindowsAutoLaunch implements PlatformAutoLaunch {

    public static final WindowsAutoLaunch INSTANCE = new WindowsAutoLaunch();

    private static final String RUN_APP = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
    private static final String TASK_MANAGER_OVERRIDE_REGKEY = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\StartupApproved\\Run";
    private static final byte[] TASK_MANAGER_OVERRIDE_ENABLED_VALUE = new byte[]{
            0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    @Override
    public void enable(@NotNull AutoLaunch autoLaunch) {
        registrySetStringValue(
                HKEY_CURRENT_USER,
                RUN_APP,
                autoLaunch.getAppName(),
                autoLaunch.startupCommand()
        );
        registrySetBinaryValue(
                HKEY_CURRENT_USER,
                TASK_MANAGER_OVERRIDE_REGKEY,
                autoLaunch.getAppName(),
                TASK_MANAGER_OVERRIDE_ENABLED_VALUE
        );
    }

    @Override
    public void disable(@NotNull AutoLaunch autoLaunch) {
        try {
            registryDeleteValue(
                    HKEY_CURRENT_USER,
                    RUN_APP,
                    autoLaunch.getAppName()
            );
        } catch (Win32Exception ignored) {
            // probably didn't exist in the first place
        }
    }

    @Override
    public boolean isEnabled(@NotNull AutoLaunch autoLaunch) {
        try {
            // this call will throw an exception. if it succeeds, it means we have an entry
            // in the registry.
            registryGetStringValue(
                    HKEY_CURRENT_USER,
                    RUN_APP,
                    autoLaunch.getAppName()
            );

            return isEnabledInTaskManager(autoLaunch);
        } catch (Win32Exception e) {
            return false;
        }
    }

    private boolean isEnabledInTaskManager(@NotNull AutoLaunch autoLaunch) throws Win32Exception {
        byte[] value = registryGetBinaryValue(
                HKEY_CURRENT_USER,
                TASK_MANAGER_OVERRIDE_REGKEY,
                autoLaunch.getAppName()
        );
        return lastEightBytesAllZeroes(value);
    }

    private boolean lastEightBytesAllZeroes(byte[] bytes) {
        if (bytes.length < 8) {
            return false;
        }
        for (int i = bytes.length - 8; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
        return true;
    }
}
