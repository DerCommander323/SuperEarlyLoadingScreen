package dev.dercommander.sels;

import java.util.*;

public class PlatformParameters {
    public Platform platform;
    long nativeWindowPointer;
    long waylandDisplayPointer;

    public static PlatformParameters parseFromArgs(String[] args) throws IllegalArgumentException {
        Iterator<String> iterator = Arrays.stream(args).iterator();
        PlatformParameters platformParameters = new PlatformParameters();

        while (iterator.hasNext()) {
            String arg = iterator.next();

            if (arg.equalsIgnoreCase("--platform")) {
                String platform = iterator.next();

                platformParameters.platform = Platform.parseFromString(platform);
            } else if (arg.equalsIgnoreCase("--windowPointer")) {
                String pointer = iterator.next();

                try {
                    platformParameters.nativeWindowPointer = Long.parseLong(pointer);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid windowPointer: " + pointer);
                }
            } else if (arg.equalsIgnoreCase("--waylandDisplayPointer")) {
                String pointer = iterator.next();

                try {
                    platformParameters.waylandDisplayPointer = Long.parseLong(pointer);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid waylandDisplayPointer: " + pointer);
                }
            }
        }

        List<String> missing = platformParameters.missingParameters();
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing arguments: " + String.join(", ", missing));
        }

        return platformParameters;
    }

    private List<String> missingParameters() {
        ArrayList<String> list = new ArrayList<>();

        if (platform != null) {
            if (platform == Platform.WAYLAND && waylandDisplayPointer == 0) {
                list.add("--waylandDisplayPointer");
            }
        } else {
            list.add("--platform");
        }

        if (nativeWindowPointer == 0) {
            list.add("--windowPointer");
        }

        return list;
    }
}

