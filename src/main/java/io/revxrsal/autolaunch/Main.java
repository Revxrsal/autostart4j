package io.revxrsal.autolaunch;

import java.io.File;

public class Main {
    public static void main(String[] args) {
AutoLaunch autoLaunch = AutoLaunch.builder()
        .appName("Everything")
        .appFile(new File("C:/Program Files/Everything/Everything.exe"))
        .args("--minimized")

        // For MacOS: This will use the launch agent instead of AppleScript
        .useLaunchAgent()
        .build();

// To enable:
autoLaunch.enable();

// To disable:
autoLaunch.disable();

// To toggle:
autoLaunch.toggle();

// Check if it is enabled:
boolean enabled = autoLaunch.isEnabled();

    }
}
