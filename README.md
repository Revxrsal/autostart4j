[![](https://jitpack.io/v/Revxrsal/autostart4j.svg)](https://jitpack.io/#Revxrsal/autostart4j)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java CI with Gradle](https://github.com/Revxrsal/autostart4j/actions/workflows/gradle.yml/badge.svg)](https://github.com/Revxrsal/autostart4j/actions/workflows/gradle.yml)
[![Discord](https://discord.com/api/guilds/939962855476846614/widget.png)](https://discord.gg/pEGGF785zp)

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