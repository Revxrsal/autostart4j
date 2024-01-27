package io.revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.revxrsal.autolaunch.Utils.notNull;

public final class AutoLaunch {

    private static final PlatformAutoLaunch PLATFORM_AUTO_LAUNCH = PlatformAutoLaunch.create();

    private final @NotNull String appName;
    private final @NotNull File appFile;
    private final @NotNull @Unmodifiable List<String> args;
    private final boolean useLaunchAgent;

    AutoLaunch(@NotNull String appName, @NotNull File appFile, @NotNull List<String> args, boolean useLaunchAgent) {
        this.appName = notNull(appName, "app name");
        this.appFile = notNull(appFile, "app file");
        this.args = Collections.unmodifiableList(new ArrayList<>(notNull(args, "args")));
        this.useLaunchAgent = useLaunchAgent;
    }

    public @NotNull String getAppName() {
        return appName;
    }

    public @NotNull File getAppFile() {
        return appFile;
    }

    public @NotNull List<String> getArgs() {
        return args;
    }

    public boolean useLaunchAgent() {
        return useLaunchAgent;
    }

    public @NotNull String startupCommand() {
        return startupCommand(true);
    }

    public @NotNull String startupCommand(boolean quotes) {
        if (quotes)
            return '"' + appFile.getAbsolutePath() + '"' + " " + String.join(" ", args);
        return appFile.getAbsolutePath() + " " + String.join(" ", args);
    }

    public void enable() {
        PLATFORM_AUTO_LAUNCH.enable(this);
    }

    public void disable() {
        PLATFORM_AUTO_LAUNCH.disable(this);
    }

    public boolean isEnabled() {
        return PLATFORM_AUTO_LAUNCH.isEnabled(this);
    }

    public void toggle() {
        if (isEnabled())
            disable();
        else
            enable();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String appName;
        private File appFile;
        private boolean useLaunchAgent = false;
        private List<String> args = Collections.emptyList();

        public Builder appName(String appName) {
            this.appName = notNull(appName, "app name");
            return this;
        }

        public Builder appFile(File appFile) {
            this.appFile = notNull(appFile, "app file");
            return this;
        }

        public Builder useLaunchAgent() {
            this.useLaunchAgent = true;
            return this;
        }

        public Builder args(List<String> args) {
            this.args = notNull(args, "args");
            return this;
        }

        public Builder args(String @NotNull ... args) {
            this.args = Arrays.asList(notNull(args, "args"));
            return this;
        }

        public AutoLaunch build() {
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
