package revxrsal.autolaunch;

enum OperatingSystem {
    LINUX,
    WINDOWS,
    MAC_OS,
    UNKNOWN;

    public static OperatingSystem current() {
        String name = System.getProperty("os.name").toLowerCase();
        if (name.startsWith("linux"))
            return OperatingSystem.LINUX;
        if (name.startsWith("win"))
            return OperatingSystem.WINDOWS;
        if (name.startsWith("mac os x"))
            return OperatingSystem.MAC_OS;
        return OperatingSystem.UNKNOWN;
    }
}