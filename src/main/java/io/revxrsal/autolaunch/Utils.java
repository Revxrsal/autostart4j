package io.revxrsal.autolaunch;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Contains a few shared utility functions
 */
final class Utils {

    private Utils() {}

    @Contract("null, _ -> fail")
    public static <T> T notNull(T v, String paramName) {
        if (v == null)
            throw new NullPointerException(paramName + " cannot be null");
        return v;
    }

    @Contract("_ -> fail")
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    public static @NotNull String getOutput(@NotNull Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append('\n');
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return output.toString().trim();
            } else {
                throw new RuntimeException("Command execution failed with exit code: " + exitCode);
            }
        } catch (Throwable e) {
            sneakyThrow(e);
            return "unreachable";
        }
    }
}
