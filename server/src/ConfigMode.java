import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

import lib.*;
import lib.TOTP.Secret;
import lib.TOTP.Secret.*;

public class ConfigMode {
    public static void main(AES aes, String Key) throws Exception {
		Console console = System.console();
		Scanner s = new Scanner(System.in);
        cmdLoop : while(true) {
            String cmd = new String(console.readLine("$ "));
            if(cmd.equals("exit")) {
                break cmdLoop;
            }else if(cmd.equals("adduser")) {
                SparkDB users = new SparkDB();
                users.readFromString(aes.decrypt(new String(IO.read("./conf/users.db"))));
                String user = "", full = "";
                boolean isSimilar = true;
                while(isSimilar) {
                full = console.readLine("Enter the verifier's full name: ");
                if(! users.getColumn("full_name").contains(full)) isSimilar = false;
                else log.e("The full name already exists");
                }
                isSimilar = true;
                while(isSimilar) {
                user = console.readLine("Enter the verifier's user name: ");
                if(! users.getColumn("user").contains(SHA.gen(user))) isSimilar = false;
                else log.e("The user name already exists");
                }
                String pass = "";
                double tempEntropy = -1;
                while(tempEntropy == -1 || tempEntropy >= 55.0) {
                    pass = new String(console.readPassword("Enter the verifier's password: "));
                    tempEntropy = EntropyCalc.calculate(pass);
                    if(tempEntropy < 55) {
                        log.e("Weak password, The length of the password should be higher than 9 alphanumeric characters with digits\n");
                    }
                }
                byte[] RANDOTP = Secret.generate(Size.LARGE);
                System.out.println("Scan QR Code using Google Authenticator: "+TOTP.getQRUrl(user, "EDS", Secret.toBase32(RANDOTP)));
                final String FPass = pass;
                final String FFull = full;
                final String FUser = user;
                users.add(new HashMap<String, String>() {{
                    put("full_name", FFull);
                    put("user", SHA.gen(FUser));
                    put("otp", Secret.toBase32(RANDOTP));
                    put("pass", SHA.gen(FPass));
                }});
                IO.write("./conf/users.db", aes.encrypt(users.toString()), false);
                log.s("Done!");
            }else if(cmd.equals("deleteuser")) {
                SparkDB users = new SparkDB();
                users.readFromString(aes.decrypt(new String(IO.read("./conf/users.db"))));
                String user = SHA.gen(console.readLine("Enter the verifier's user name: "));
                if(users.getColumn("user").contains(user)) {
                    String pass = new String(console.readPassword("Enter the verifier's password: "));
                    if(users.get(new HashMap<String, String>() {{
                        put("user", user);
                    }}, "pass", 1).get(0).equals(SHA.gen(pass))) {
                        users.delete(new HashMap<String, String>() {{
                            put("user", user);
                        }}, 1);
                        log.s("Deleted the verifier!");
                    }
                }else {
                    log.e("The username doesn't exist");
                }
            }
            else if(cmd.equals("deletedoc")) {
                String code = console.readLine("Enter the document number in 123-456-789 format or 123456789 format: ");
                SparkDB db = new SparkDB();
			    db.readFromFile("./conf/doc/"+code.substring(0, 3)+".db", Key); // read ./conf/doc/000.db
                if(db.getColumn("code").contains(code)) {
                    db.delete(new HashMap<String, String>() {{
                        put("code", code);
                    }}, 1);
                    log.s("Done!");
                }else {
                    log.e("The document code doesn't exist");
                }
            }
            else if(cmd.equals("edituser")) {
                SparkDB users = new SparkDB();
                users.readFromString(aes.decrypt(new String(IO.read("./conf/users.db"))));
                String username = SHA.gen(console.readLine("Enter the verifier's username: "));
                if(users.getColumn("user").contains(username)) {
                    String oldPassword = new String(console.readPassword("Enter the old password: "));
                    if(users.get(new HashMap<String, String>() {
                        {
                            put("user", username);
                        }
                    }, "pass", 1).get(0).equals(SHA.gen(oldPassword))) {
                        String newPassword = new String(console.readPassword("Enter the new password: "));
                        users.modify(new HashMap<String, String>() {
                            {
                                put("user", username);
                            }
                        }, new HashMap<String, String>() {
                            {
                                put("pass", SHA.gen(newPassword));
                            }
                        });
                        log.s("Done!");
                    }else {
                        log.e("The password is incorrect");
                    }
                }else {
                    log.e("The username doesn't exist");
                }
            }
        }
        s.close();
    }
}
