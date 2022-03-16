import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import lib.AES;
import lib.IO;
import lib.SparkDB;
import lib.log;

public class main {
	/**
	 * Entry point for the server
	 *
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		// Stage 1
		ArrayList<String> FrontendFiles = new ArrayList<>() {
			{
				add("index.html");
				add("about.html");
				add("support.html");
				add("tools.html");
				add("_next/static/css/2ef95f13bec2f900.css");
				add("_next/static/chunks/34-bdf13a4717a36307.js");
				add("_next/static/chunks/651.243d23442247d286.js");
				add("_next/static/chunks/78e521c3-a0e872a2df8ff9e8.js");
				add("_next/static/chunks/863-a68842678ea25cb0.js");
				add("_next/static/chunks/framework-5f4595e5518b5600.js");
				add("_next/static/chunks/main-e267bb9839e5051c.js");
				add("_next/static/chunks/polyfills-5cd94c89d3acac5f.js");
				add("_next/static/chunks/webpack-c35fdbd9f927d367.js");
				add("_next/static/chunks/pages/_app-ce99db96f4671041.js");
				add("_next/static/chunks/pages/_error-2f883067a14f4c4a.js");
				add("_next/static/chunks/pages/about-32f4107dc1e7a18d.js");
				add("_next/static/chunks/pages/index-f3f292817416b1b3.js");
				add("_next/static/chunks/pages/support-f695cd8f0843fdc3.js");
				add("_next/static/chunks/pages/tools-ef4fd2538ba0ef4a.js");
				add("_next/static/2CXVybD6p7sPSHHHDiJuN/_buildManifest.js");
				add("_next/static/2CXVybD6p7sPSHHHDiJuN/_middlewareManifest.js");
				add("_next/static/2CXVybD6p7sPSHHHDiJuN/_ssgManifest.js");
				/*
				 * All frontend filenames
				 */
			}
		}; // List of all frontend files after './www/'
		for (String frontendFile : FrontendFiles) {
			if (!new File("./www/" + frontendFile).exists()) {
				log.e("File " + frontendFile + " doesn't exist");
				System.exit(1);
			} else {
				log.s("File " + frontendFile + " exist");
			}
		}
		// Stage 2
		boolean ServerKey = new File("./conf/server.key").exists();
		boolean UserDB = new File("./conf/users.db").exists();
		boolean DocsDB = new File("./conf/docs.db").exists();
		boolean InfoDB = new File("./conf/info.txt").exists();
		if (ServerKey && UserDB && DocsDB && InfoDB) {
			// Good to go
			System.out.print("Enter the server key: ");
			String k = s.nextLine();
			boolean key = !AES.decrypt(new String(IO.read("./conf/server.key")), k).equals("ERR.ERR.ERR");
			if (key) {
				log.s("Encryption key is correct");
				Engine server = new Engine(k);
				server.run();
			} else {
				log.e("Encryption key is not correct");
			}
		} else if (!ServerKey && !UserDB && !DocsDB && !InfoDB) {
			// New user
			new File("./conf/server.key").createNewFile();
			new File("./conf/users.db").createNewFile();
			new File("./conf/docs.db").createNewFile();
			new File("./conf/info.txt").createNewFile();
			new File("./conf/queries.txt").createNewFile();
			IO.write("./conf/queries.txt", "0", false);
			System.out.print("Enter the new server key: ");
			String ServerK = s.nextLine();
			IO.write("./conf/users.db", AES.encrypt("\"full_name\",\"user\",\"password\"", ServerK), false);
			IO.write("./conf/docs.db",
					AES.encrypt("\"pub_code\",\"verify_code\",\"path\",\"doc_name\",\"verifier\",\"writer\",\"date\"",
							ServerK),
					false);
			System.out.print("Enter the school name: ");
			String SchoolName = s.nextLine();
			System.out.print("Enter the number of verifiers: ");
			int Num = Integer.parseInt(s.nextLine());
			SparkDB users = new SparkDB();
			users.readfromstring(AES.decrypt(new String(IO.read("./conf/users.db")), ServerK));
			for (int i = 0; i < Num; i++) {
				System.out.print("For verifier " + (i + 1) + ". Enter the fullname: ");
				String full = s.nextLine();
				System.out.print("For verifier " + (i + 1) + ". Enter the username: ");
				String user = s.nextLine();
				System.out.print("For verifier " + (i + 1) + ". Enter the password: ");
				String pass = s.nextLine();

				users.add(new String[] { full, // fullname
						user, // user
						pass// password
				});
			}
			IO.write("./conf/users.db", AES.encrypt(users.print(), ServerK), false);
			IO.write("./conf/info.txt", AES.encrypt(SchoolName, ServerK), false);
			IO.write("./conf/server.key", AES.encrypt(ServerK, ServerK), false);
			log.s("Setup is complete.");
		} else {
			// Data is corrupted
			log.e("Possible data corruption, self destruction in process.");
			new File("./conf/users.db").delete();
			new File("./conf/info.txt").delete();
			new File("./conf/server.key").delete();
			new File("./conf/docs.db").delete();
		}

		s.close();
	}
}
