package io.revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;

interface PlatformAutoLaunch {

    void enable(@NotNull AutoLaunch autoLaunch);

    void disable(@NotNull AutoLaunch autoLaunch);

    boolean isEnabled(@NotNull AutoLaunch autoLaunch);

    static PlatformAutoLaunch create() {
        OperatingSystem platform = OperatingSystem.current();
        switch (platform) {
            case LINUX:
                return LinuxAutoLaunch.INSTANCE;
            case WINDOWS:
                return WindowsAutoLaunch.INSTANCE;
            case MAC_OS:
                return MacOSAutoLaunch.INSTANCE;
            default:
                throw new IllegalStateException("Unsupported platform.");
        }
    }

}
