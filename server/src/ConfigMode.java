import java.io.Console;
import java.util.HashMap;

import lib.*;
import lib.TOTP.Secret;
import lib.TOTP.Secret.*;

public class ConfigMode {
    static Console console = System.console();
    private AES Paes;
    static String userDBDirectory = FilePaths.ConfigurationDirectory.getValue() + "users.db";

    public void main(AES aes) throws Exception {
        this.Paes = aes;

        CommandPrompt CP = new CommandPrompt() {
            @Override
            public void handler(String command) {
                if (command.equals("adduser")) addUser();
                if (command.equals("deleteuser")) deleteUser();
                if (command.equals("edituser")) editUser();
            }
        };

        CP.availableCommands.add("adduser");
        CP.availableCommands.add("deleteuser");
        CP.availableCommands.add("edituser");

        CP.helpCommands.put("adduser", "Adds a verifier profile");
        CP.helpCommands.put("deleteuser", "Deletes a verifier profile");
        CP.helpCommands.put("edituser", "Modifies an existing verifier profile password");

        CP.run("Config Mode");
    }

    public void addUser() {
        /* Initialize and read User DB */
        SparkDB users = new SparkDB();
        try {
            users.readFromString(Paes.decrypt(new String(IO.read(userDBDirectory))));
        } catch (Exception e) {
            log.e("Failed to read users.db");
        }

        String user = "", full = "";
        /* isSimilar checks for collisions */
        boolean isSimilar = true;
        /* Loop for Full name collision */
        while (isSimilar) {
            full = console.readLine("Enter the verifier's full name: ");
            if (!users.getColumn("full_name").contains(full)) isSimilar = false;
            else log.e("The full name already exists");
        }

        isSimilar = true;
        /* Loop for username collision */
        while (isSimilar) {
            user = console.readLine("Enter the verifier's user name: ");
            if (!users.getColumn("user").contains(SHA.gen(user))) isSimilar = false;
            else log.e("The user name already exists");
        }

        /* Loop for a sophisticated password */
        String pass = "";
        boolean EntropyTestPass = false;
        while (!EntropyTestPass) {
            pass = new String(console.readPassword("Enter the verifier's password: "));
            if (EntropyCalc.calculate(pass) < 50.0) log.e("Weak password, Try typing a more sophisticated password.\n");
            else EntropyTestPass = true;
        }

        /* OTP Generation */
        byte[] RANDOTP = Secret.generate(Size.LARGE);
        System.out.println("Scan QR Code using Google Authenticator: " + TOTP.getQRUrl(user, "EDS", Secret.toBase32(RANDOTP)));

        /* Write data to database */
        final String FPass = pass;
        final String FFull = full;
        final String FUser = user;
        users.add(new HashMap<String, String>() {
            {
                put("full_name", FFull);
                put("user", SHA.gen(FUser));
                put("otp", Secret.toBase32(RANDOTP));
                put("pass", SHA.gen(FPass));
            }
        });
        try {
            IO.write(userDBDirectory, Paes.encrypt(users.toString()), false);
        } catch (Exception e) {
            log.e("Failed to update users.db");
        }
        log.s("Done!");
    }

    public int deleteUser() {
        /* Initialize and read User DB */
        SparkDB users = new SparkDB();
        try {
            users.readFromString(Paes.decrypt(new String(IO.read(userDBDirectory))));
        } catch (Exception e) {
            log.e("Failed to read users.db or decrypt it");
        }

        /* Retrieve User name for user */
        String user = SHA.gen(console.readLine("Enter the verifier's user name: "));
        if(! users.getColumn("user").contains(user)) {
            log.e("The username doesn't exist");
            return 1;
        }

        /* Retrieve Password */
        String pass = new String(console.readPassword("Enter the verifier's password"));
        String correctPassHashed = users.get(new HashMap<String, String>() {
            {
                put("user", user);
            }
        }, "pass", 1).get(0); /* The correct hashed password */

        /* Compare Password Hashes */
        if(! SHA.gen(pass).equals(correctPassHashed)) {
            log.e("The password is incorrect");
            return 1;
        }

        /* If passed, delete the user row */
        users.delete(new HashMap<String, String>() {
            {
                put("user", user);
            }
        }, 1);

        /* Update users.db file */
        try {
            IO.write(userDBDirectory, Paes.encrypt(users.toString()), false);
        } catch (Exception e) {
            log.e("Failed to update users.db");
            return 1;
        }
        log.s("Done!");

        return 0;
    }

    public int editUser() {
        /* Read the user DB */
        SparkDB users = new SparkDB();
        try {
            users.readFromString(Paes.decrypt(new String(IO.read(userDBDirectory))));
        } catch (Exception e) {
            log.e("Failed to read users.db or decrypt it");
        }

        /* Retrieve the username */
        String username = SHA.gen(console.readLine("Enter the verifier's username: "));
        /* See if username exists */
        if(! users.getColumn("user").contains(username)) {
            log.e("The username doesn't exist");
            return 1;
        }

        /* Retrieve old password */
        String oldPassword = new String(console.readPassword("Enter the old password: "));
        /* Retrieve correct password hash */
        String correctHashedOldPassword = users.get(new HashMap<String, String>() {
            {
                put("user", username);
            }
        }, "pass", 1).get(0);
        /* Compare password hashes */
        if(! SHA.gen(oldPassword).equals(correctHashedOldPassword)) {
            log.e("The password is incorrect");
            return 1;
        }

        /* Retrieve new password */
        String newPassword = new String(console.readPassword("Enter the new password: "));
        /* Modify passwords */
        users.modify(new HashMap<String, String>() {
            {
                put("user", username);
            }
        }, new HashMap<String, String>() {
            {
                put("pass", SHA.gen(newPassword));
            }
        });
        /* Update user DB */
        try {
            IO.write(userDBDirectory, Paes.encrypt(users.toString()), false);
        } catch (Exception e) {
            log.e("Failed to update users.db");
            return 1;
        }
        log.s("Done!");

        return 0;
    }
}
