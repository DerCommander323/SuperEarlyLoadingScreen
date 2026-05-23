package dev.dercommander.sels;

public enum Platform {
    WIN32,
    XORG,
    WAYLAND,
    COCOA;

    public static Platform parseFromString(String platform) throws IllegalArgumentException {
        if (platform.equalsIgnoreCase("win32")) {
            return WIN32;
        } else if (platform.equalsIgnoreCase("wayland")) {
            return WAYLAND;
        } else if (platform.equalsIgnoreCase("xorg")) {
            return XORG;
        } else if (platform.equalsIgnoreCase("cocoa")) {
            return COCOA;
        } else {
            throw new IllegalArgumentException("Unrecognized Platform string: " + platform
                    + " expected one of win32, wayland, xorg, cocoa");
        }
    }
}
