package io.revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.revxrsal.autolaunch.Utils.notNull;

/**
 * The entrypoint class for creating and configuring applications
 * on system start-up.
 * <p>
 * This class is immutable and stores no state, hence is safe to share
 * across threads.
 */
public final class AutoLaunch {

    /**
     * The corresponding {@link PlatformAutoLaunch} for this platform.
     */
    private static final PlatformAutoLaunch PLATFORM_AUTO_LAUNCH = PlatformAutoLaunch.create();

    /**
     * The application name. This is used mostly as an identifier key, so it
     * is arbitrary. It should be the application name, and should be unique
     * from other applications.
     * <p>
     * In Windows, this is the name that appears in Task Manager's {@literal Startup}
     * menu.
     */
    private final @NotNull String appName;

    /**
     * The binary file of this application to be started.
     * <p>
     * This file must exist, and macOS requires the file to be absolute.
     */
    private final @NotNull File appFile;

    /**
     * The arguments to launch the application with
     */
    private final @NotNull @Unmodifiable List<String> args;

    /**
     * (macOS only)
     * Whether to use the launch agent or not. If disabled, it will be
     * launched using AppleScript.
     */
    private final boolean useLaunchAgent;

    AutoLaunch(@NotNull String appName, @NotNull File appFile, @NotNull List<String> args, boolean useLaunchAgent) {
        this.appName = notNull(appName, "app name");
        this.appFile = notNull(appFile, "app file");
        this.args = Collections.unmodifiableList(new ArrayList<>(notNull(args, "args")));
        this.useLaunchAgent = useLaunchAgent;
    }

    /**
     * The application name. This is used mostly as an identifier key, so it
     * is arbitrary. It should be the application name, and should be unique
     * from other applications.
     * <p>
     * In Windows, this is the name that appears in Task Manager's {@literal Startup}
     * menu.
     *
     * @return The application name
     */
    public @NotNull String getAppName() {
        return appName;
    }

    /**
     * The binary file of this application to be started.
     * <p>
     * This file must exist, and macOS requires the file to be absolute.
     *
     * @return The binary file
     */
    public @NotNull File getAppFile() {
        return appFile;
    }

    /**
     * The arguments to launch the application with
     *
     * @return The arguments to launch the application with
     */
    public @NotNull @Unmodifiable List<String> getArgs() {
        return args;
    }

    /**
     * (macOS only)
     * Whether to use the launch agent or not. If disabled, it will be
     * launched using AppleScript.
     *
     * @return Whether to use the launch agent or not
     */
    public boolean useLaunchAgent() {
        return useLaunchAgent;
    }

    /**
     * A shortcut function that joins the absolute path of the {@link #getAppFile()}
     * to the {@link #getArgs()}, joined by spaces.,
     *
     * @return The start-up command
     */
    public @NotNull String startupCommand() {
        return startupCommand(true);
    }

    /**
     * A shortcut function that joins the absolute path of the {@link #getAppFile()}
     * to the {@link #getArgs()}, joined by spaces.,
     *
     * @param quotes If the file path should be surrounded by quotes
     * @return The start-up command
     */
    public @NotNull String startupCommand(boolean quotes) {
        if (quotes)
            return '"' + appFile.getAbsolutePath() + '"' + " " + String.join(" ", args);
        return appFile.getAbsolutePath() + " " + String.join(" ", args);
    }

    /**
     * Enables auto-launching this application. If it is already enabled, this
     * method will have no effect.
     */
    public void enable() {
        PLATFORM_AUTO_LAUNCH.enable(this);
    }

    /**
     * Disables auto-launching this application. If it is already disabled, this
     * method will have no effect.
     */
    public void disable() {
        PLATFORM_AUTO_LAUNCH.disable(this);
    }

    /**
     * Checks if auto-launching this application is enabled or not
     *
     * @return whether auto-launching this application is enabled or not
     */
    public boolean isEnabled() {
        return PLATFORM_AUTO_LAUNCH.isEnabled(this);
    }

    /**
     * A utility function for toggling the state of this auto-launcher
     */
    public void toggle() {
        if (isEnabled())
            disable();
        else
            enable();
    }

    /**
     * Creates a new {@link Builder}
     *
     * @return A new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for creating {@link AutoLaunch} instances.
     */
    public static class Builder {

        /**
         * The application name. This is used mostly as an identifier key, so it
         * is arbitrary. It should be the application name.
         * <p>
         * In Windows, this is the name that appears in Task Manager's {@literal Startup}
         * menu.
         */
        private String appName;

        /**
         * The binary file of this application to be started.
         * <p>
         * This file must exist, and macOS requires the file to be absolute.
         */
        private File appFile;

        /**
         * The arguments to launch the application with
         */
        private List<String> args = Collections.emptyList();

        /**
         * (macOS only)
         * Whether to use the launch agent or not. If disabled, it will be
         * launched using AppleScript.
         */
        private boolean useLaunchAgent = false;

        /**
         * Sets the application name. This is used mostly as an identifier key, so it
         * is arbitrary. It should be the application name, and should be unique
         * from other applications.
         * <p>
         * In Windows, this is the name that appears in Task Manager's {@literal Startup}
         * menu.
         *
         * @param appName The application name
         * @return this builder instance
         */
        public Builder appName(String appName) {
            this.appName = notNull(appName, "app name");
            if (appName.isEmpty())
                throw new IllegalArgumentException("Application name cannot be empty!");
            return this;
        }

        /**
         * Sets the binary file of this application to be started.
         * <p>
         * This file must exist, and macOS requires the file to be absolute.
         *
         * @param appFile The application file
         * @return this builder instance
         */
        public Builder appFile(File appFile) {
            this.appFile = notNull(appFile, "app file");
            return this;
        }

        /**
         * (macOS only)
         * Whether to use the launch agent or not. If disabled, it will be
         * launched using AppleScript.
         *
         * @return this builder instance
         */
        public Builder useLaunchAgent() {
            this.useLaunchAgent = true;
            return this;
        }

        /**
         * Sets the arguments to launch the application with.
         *
         * @param args Arguments to launch with
         * @return this builder instance
         */
        public Builder args(List<String> args) {
            this.args = notNull(args, "args");
            return this;
        }

        /**
         * Sets the arguments to launch the application with.
         *
         * @param args Arguments to launch with
         * @return this builder instance
         */
        public Builder args(String @NotNull ... args) {
            this.args = Arrays.asList(notNull(args, "args"));
            return this;
        }

        /**
         * Constructs a new {@link AutoLaunch} based on the given configuration.
         *
         * @return The new {@link AutoLaunch} instance.
         */
        public @NotNull AutoLaunch build() {
            notNull(appName, "app name");
            notNull(appFile, "app file");
            notNull(args, "app name");
            if (!appFile.exists())
                throw new IllegalStateException("File " + appFile + " does not exist!");
            return new AutoLaunch(
                    appName,
                    appFile,
                    args,
                    useLaunchAgent
            );
        }
    }
}
