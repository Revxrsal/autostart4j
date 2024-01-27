package io.revxrsal.autolaunch;

enum OperatingSystem {
    LINUX,
    WINDOWS,
    MAC_OS,
    UNKNOWN;

    public static OperatingSystem current() {
        String name = System.getProperty("os.name");
        if (name == null) name = "";
        if (name.startsWith("Linux"))
            return OperatingSystem.LINUX;
        if (name.startsWith("Win"))
            return OperatingSystem.WINDOWS;
        if (name.startsWith("Mac OS X"))
            return OperatingSystem.MAC_OS;
        return OperatingSystem.UNKNOWN;
    }
}