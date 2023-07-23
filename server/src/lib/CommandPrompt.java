package lib;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a command prompt
 * @author Morad A.
 */
public abstract class CommandPrompt {
    /* Key: Command, Value: Explanation/Usage */
    public HashMap<String, String> helpCommands = new HashMap<>();
    /* The List of available developer-defined commands */
    public ArrayList<String> availableCommands = new ArrayList<>();

    /**
     * Main instance
     * @param alias The current name of the command prompt
     */
    public void run(String alias) {
        Console C = System.console();

        availableCommands.add("exit");
        availableCommands.add("help");

        helpCommands.put("exit", "Exit the main mode/instance");
        helpCommands.put("help", "Display help page");

        cmdLoop : while(true) {
            String cmd = new String(C.readLine(alias + "> "));
            /* Help Screen */
            if(cmd.equals("help")) for(java.util.Map.Entry<String, String> Entry : helpCommands.entrySet()) System.out.println(Entry.getKey() + " : " + Entry.getValue());
            /* Exit the current command prompt */
            else if(cmd.equals("exit")) break cmdLoop;
            /* If it is an available command, pass to handler */
            else if(availableCommands.contains(cmd)) handler(cmd);
            /* Else, print an error */
            else System.out.println("The command " + cmd + " is not found, please type 'help' for more information about available commands.");
        }
    }

    /**
     * The handler function, contains the logic of the commands.
     * @param command The command passed by run() 'Main instance'
     */
    public abstract void handler(String command);
}
