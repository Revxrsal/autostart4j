package revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;

/**
 * An interface for handling the operating-system-dependent operations
 * for auto-launching.
 */
interface PlatformAutoLaunch {

    /**
     * Enables auto-launching the given application. If it is already enabled, this
     * method will have no effect.
     *
     * @param autoLaunch App to launch
     */
    void enable(@NotNull AutoLaunch autoLaunch);

    /**
     * Disables auto-launching the given application. If it is already disabled, this
     * method will have no effect.
     *
     * @param autoLaunch App to disable
     */
    void disable(@NotNull AutoLaunch autoLaunch);

    /**
     * Checks if auto-launching the given application is enabled or not
     *
     * @param autoLaunch App to check for
     * @return whether auto-launching the given application is enabled or not
     */
    boolean isEnabled(@NotNull AutoLaunch autoLaunch);

    /**
     * Creates a {@link PlatformAutoLaunch} for the current platform
     *
     * @return The new {@link PlatformAutoLaunch}
     * @throws IllegalStateException if the current platform is not supported.
     */
    static @NotNull PlatformAutoLaunch create() {
        switch (OperatingSystem.current()) {
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
