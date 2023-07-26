package lib;

import java.io.IOException;

public class CommandExecutor {
    public static void executeCommand(String command) throws IOException, InterruptedException {
        String osName = System.getProperty("os.name").toLowerCase();
        Process process;
        if (osName.contains("win")) {
            // For Windows systems
            process = Runtime.getRuntime().exec("cmd /c " + command);
        } else {
            // For Linux and other Unix-like systems
            process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
        }
        process.waitFor();
    }
}