# autostart4j
A tiny, cross-platform library for automatically launching any application or executable at startup. 

Supports Windows, macOS, and Linux.

This library is a direct port of [auto-launch](https://github.com/zzzgydi/auto-launch.git) written in Rust.

## Example
```java
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
```