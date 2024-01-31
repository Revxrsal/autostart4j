package revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static revxrsal.autolaunch.Utils.escapeJava;
import static revxrsal.autolaunch.Utils.sneakyThrow;

/**
 * The {@link PlatformAutoLaunch} implementation for macOS
 */
final class MacOSAutoLaunch implements PlatformAutoLaunch {

    public static final MacOSAutoLaunch INSTANCE = new MacOSAutoLaunch();

    private MacOSAutoLaunch() {
    }

    private static String formatDoc(
            @NotNull String appName,
            @NotNull String section
    ) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "    <dict>\n" +
                "        <key>Label</key>\n" +
                "        <string>" + appName + "</string>\n" +
                "        <key>ProgramArguments</key>\n" +
                "        <array>" + section + "</array>\n" +
                "        <key>RunAtLoad</key>\n" +
                "        <true/>\n" +
                "    </dict>\n" +
                "</plist>";
    }

    @Override
    public void enable(@NotNull AutoLaunch autoLaunch) {
        try {
            if (!autoLaunch.getAppFile().isAbsolute())
                throw new IllegalArgumentException("AutoLaunch.getAppFile() must be absolute");
            if (autoLaunch.useLaunchAgent()) {
                Path dir = getLaunchAgentDirectory();
                if (!Files.exists(dir))
                    Files.createDirectory(dir);

                List<String> args = new ArrayList<>();
                args.add(autoLaunch.getAppFile().getAbsolutePath());
                args.addAll(autoLaunch.getArgs());
                String section = args
                        .stream()
                        .map(s -> "<string>" + s + "</string>")
                        .collect(Collectors.joining());
                String data = formatDoc(autoLaunch.getAppName(), section);
                Path file = getFile(autoLaunch);

                if (!Files.exists(file)) {
                    Files.createFile(file);
                    Files.write(file, data.getBytes());
                }
            } else {
                boolean hidden = autoLaunch.getArgs()
                        .stream()
                        .anyMatch(v -> v.equals("--hidden") || v.equals("--minimized"));
                String props = String.format(
                        "{{name:\"%s\",path:\"%s\",hidden:%s}}",
                        autoLaunch.getAppName(),
                        autoLaunch.getAppFile().getAbsolutePath(),
                        hidden
                );
                String command = "make login item at end with properties " + props;
                Process process = executeAppleScript(command);
                int code = process.waitFor();
                if (code != 0)
                    throw new RuntimeException("AppleScript failed to execute. Error code: " + code);
            }
        } catch (Throwable t) {
            sneakyThrow(t);
        }
    }

    // get the plist file path
    private static Path getFile(AutoLaunch autoLaunch) {
        return getLaunchAgentDirectory().resolve(autoLaunch.getAppName() + ".plist");
    }

    private static Path getLaunchAgentDirectory() {
        String home = System.getProperty("user.home");
        return Paths.get(home).resolve("Library").resolve("LaunchAgents");
    }

    private static @NotNull Process executeAppleScript(String script) {
        try {
            String command = '"' + escapeJava(script) + '"';
            ProcessBuilder processBuilder = new ProcessBuilder("osascript", "-e", command);
            return processBuilder.start();
        } catch (IOException e) {
            sneakyThrow(e);
            return null;
        }
    }

    @Override
    public void disable(@NotNull AutoLaunch autoLaunch) {
        try {
            if (autoLaunch.useLaunchAgent()) {
                Path path = getFile(autoLaunch);
                Files.deleteIfExists(path);
            } else {
                String command = "delete login item \"" + autoLaunch.getAppName() + "\"";
                Process process = executeAppleScript(command);
                int code = process.waitFor();
                if (code != 0)
                    throw new RuntimeException("AppleScript failed to execute. Error code: " + code);
            }
        } catch (Throwable e) {
            sneakyThrow(e);
        }
    }

    @Override
    public boolean isEnabled(@NotNull AutoLaunch autoLaunch) {
        try {
            if (autoLaunch.useLaunchAgent()) {
                return Files.exists(getFile(autoLaunch));
            } else {
                String command = "get the name of every login item";
                Process process = executeAppleScript(command);
                boolean enable = false;
                int code = process.waitFor();
                if (code != 0) {
                    enable = Arrays
                            .stream(Utils.getOutput(process).split(","))
                            .map(String::trim)
                            .anyMatch(i -> i.equals(autoLaunch.getAppName()));
                }
                return enable;
            }
        } catch (Throwable e) {
            sneakyThrow(e);
            return false;
        }
    }
}
