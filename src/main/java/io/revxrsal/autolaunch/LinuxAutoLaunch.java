package io.revxrsal.autolaunch;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.revxrsal.autolaunch.Utils.sneakyThrow;

final class LinuxAutoLaunch implements PlatformAutoLaunch {

    public static final LinuxAutoLaunch INSTANCE = new LinuxAutoLaunch();

    private LinuxAutoLaunch() {
    }

    public static String formatDoc(AutoLaunch autoLaunch) {
        return "[Desktop Entry]\n" +
                "Type=Application\n" +
                "Version=1.0\n" +
                "Name=" + autoLaunch.getAppName() + "\n" +
                "Comment=" + autoLaunch.getAppName() + " startup script\n" +
                "Exec=" + autoLaunch.startupCommand(false) + "\n" +
                "StartupNotify=false\n" +
                "Terminal=false";
    }


    // get the .desktop file path
    private static Path getFile(AutoLaunch autoLaunch) {
        return getAutoStartDir().resolve(autoLaunch.getAppName() + ".desktop");
    }

    private static Path getAutoStartDir() {
        String home = System.getProperty("user.home");
        return Paths.get(home).resolve(".config").resolve("autostart");
    }

    @Override
    public void enable(@NotNull AutoLaunch autoLaunch) {
        try {
            String data = formatDoc(autoLaunch);
            Path dir = getAutoStartDir();
            if (!Files.exists(dir))
                Files.createDirectory(dir);

            Path file = getFile(autoLaunch);
            if (!Files.exists(file)) {
                Files.createFile(file);
                Files.write(file, data.getBytes());
            }
        } catch (IOException e) {
            sneakyThrow(e);
        }
    }

    @Override
    public void disable(@NotNull AutoLaunch autoLaunch) {
        try {
            Path file = getFile(autoLaunch);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            sneakyThrow(e);
        }
    }

    @Override
    public boolean isEnabled(@NotNull AutoLaunch autoLaunch) {
        return Files.exists(getFile(autoLaunch));
    }
}
