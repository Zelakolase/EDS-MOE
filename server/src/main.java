import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import lib.AES;
import lib.EntropyCalc;
import lib.FileToAL;
import lib.IO;
import lib.SHA;
import lib.SparkDB;
import lib.TOTP;
import lib.log;
import lib.TOTP.Secret;
import lib.TOTP.Secret.Size;

public class main {
	/**
	 * Entry point for the application
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("jdk.tls.ephemeralDHKeySize", "2048"); // Mitigation against LOGJAM TLS Attack
		System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true"); // Mitigation against Client Renegotiation Attack
		AES aes;
		Console console = System.console();
		Scanner s = new Scanner(System.in);
		// Stage 1
		ArrayList<String> FrontendFiles = FileToAL.convert("WWWFiles.db"); // List of all frontend files after './www/'
		for (String frontendFile : FrontendFiles) {
			if (!new File("./www/" + frontendFile).exists()) {
				log.e("File " + frontendFile + " doesn't exist");
				System.exit(1);
			}
		}
		log.s("Frontend Files Check - Done");
		// Stage 2
		boolean ServerKey = new File("./conf/server.key").exists(); // Server Enc/Dec Key
		boolean UserDB = new File("./conf/users.db").exists(); // Users DB
		boolean DocsDB = new File("./conf/docs.db").exists(); // Docs Mapping for DocDB
		boolean DocDBFolder = new File("./conf/doc").exists(); // DocDB Folder folder
		boolean DocDB = true;
		for(int i = 0;i<1000;i++) { // Check every doc table
			DocDB = DocDB && new File("./conf/doc/"+String.format("%03d", i)+".db").exists();
		}
		boolean InfoDB = new File("./conf/info.txt").exists(); // School name
		boolean Queries = new File("./conf/queries.txt").exists(); // Number of queries
		boolean Doc = new File("./conf/doc.txt").exists(); // Number of documents
		boolean Docs = new File("./docs/").exists(); // Documents folder
		/**
		 * If Server is not new
		 */
		if (ServerKey && UserDB && DocsDB && InfoDB && Queries && Docs && DocDBFolder && Doc) {
			// Good to go
			String k = new String(console.readPassword("Enter the server key: "));
			aes = new AES(k);
			boolean key = !aes.decrypt(new String(IO.read("./conf/server.key"))).equals("ERR.ERR.ERR");
			boolean config = args.length > 0; // check if the user wants to go config mode, next line same comment.
			config = config ? config && args[0].equals("config") : false;
			if (key) {
				log.s("Encryption key is correct");
				if(!config) {
					Engine server = new Engine(k);
					server.run();
				}
				else {
					ConfigMode.main(aes, k);
				}
			} else {
				log.e("Encryption key is not correct");
				System.exit(1);
			}
		}
		/**
		 * If Server is new
		 */
		else {
			// 1. Verifiers Initialization
			SparkDB Verifiers = new SparkDB();
			Verifiers.create(new ArrayList<String>() {{
				add("full_name");
				add("user");
				add("pass");
				add("otp");
			}});
			System.out.print("Enter the number of verifiers: ");
			int Num = Integer.parseInt(s.nextLine());
			for (int i = 0; i < Num; i++) {
				String user = "", full = "";
				boolean isSimilar = true;
				while(isSimilar) {
				full = console.readLine("For verifier " + (i + 1) + " -> Enter the fullname: ");
				if(! Verifiers.getColumn("full_name").contains(full)) isSimilar = false;
				else log.e("The full name already exists");
				}
				isSimilar = true;
				while(isSimilar) {
				user = console.readLine("For verifier " + (i + 1) + " -> Enter the username: ");
				if(! Verifiers.getColumn("user").contains(SHA.gen(user))) isSimilar = false;
				else log.e("The user name already exists");
			}
				String pass = "";
				byte[] RANDOTP = Secret.generate(Size.LARGE);
				final String FUser = user;
				final String FFull = full;
				System.out.println("Scan QR Code using Google Authenticator: "+TOTP.getQRUrl(FUser, "EDS", Secret.toBase32(RANDOTP)));
				final String FPass = pass;
				Verifiers.add(new HashMap<String, String>() {{
					put("full_name", FFull);
					put("user",SHA.gen(FUser));
					put("pass",SHA.gen(FPass));
					put("otp", Secret.toBase32(RANDOTP));
				}});
			}
			// 2. School Name
			System.out.print("Enter the school name: ");
			String SchoolName = s.nextLine();
			// 3. Server Key
			String ServerK = new String(console.readPassword("Enter the new server key: "));
			aes = new AES(ServerK);
			// 4. Write on disk
			new File("./conf/").mkdir(); // <-- Configuration files
			log.s("Created directory conf/");
			new File("./conf/doc").mkdir(); // <-- DocDB 000-999
			log.s("Created directory conf/doc/");
			new File("./docs/").mkdir(); // <-- Documents
			log.s("Created directory docs/");
				// a. Write Server Key
				new File("./conf/server.key").createNewFile();
				IO.write("./conf/server.key", aes.encrypt(ServerK), false);
				log.s("Stored Server Key");
				// b. Write School name
				new File("./conf/info.txt").createNewFile();
				IO.write("./conf/info.txt", aes.encrypt(SchoolName), false);
				log.s("Stored School Name");
				// c. Write Verifiers
				new File("./conf/users.db").createNewFile();
				IO.write("./conf/users.db", aes.encrypt(Verifiers.toString()), false);
				log.s("Stored Verifiers data");
				// d. Write DocsDB
				SparkDB temp = new SparkDB();
				temp.create(new ArrayList<String>() {{
					add("prefix"); add("size"); add("min_query");
				}});
				for(int i = 0;i<1000;i++) {
					final String tempStr = String.format("%03d", i);
					temp.add(new HashMap<String, String>() {{
						put("prefix", tempStr);
						put("size", "0");
						put("min_query","0");
					}});
				}
				new File("./conf/docs.db").createNewFile();
				IO.write("./conf/docs.db",
						aes.encrypt(temp.toString())
						, false);
				log.s("Created main document database");
				// e. add queries.txt
				new File("./conf/queries.txt").createNewFile();
				IO.write("./conf/queries.txt", aes.encrypt("0"), false);
				log.s("Created queries counter");
				// f. Write DocDB 000-999
				SparkDB tempDB = new SparkDB();
				tempDB.create(new ArrayList<String>() {{
					add("code"); add("path"); add("doc_name"); add("verifier"); add("writer"); add("sha"); add("age"); add("date");
				}});
				for(int i = 0;i<1000;i++) {
					new File("./conf/doc/"+String.format("%03d", i)+".db").createNewFile();
					IO.write("./conf/doc/"+String.format("%03d", i)+".db",
							aes.encrypt(tempDB.toString())
							, false);
				}
				log.s("Created document databases");
				// g. add doc.txt
				new File("./conf/doc.txt").createNewFile();
				IO.write("./conf/doc.txt", aes.encrypt("0"), false);
				log.s("Created document counter");
				// h. Encrypt WWW files
				for(String path : FrontendFiles) {
					if(new File("./www/"+path).isFile()) {
						IO.write("./www/"+path,
								aes.encrypt(IO.read("./www/"+path))
								, false);
					}
				}
			log.s("You may relaunch the server!");
		}
		s.close();
	}
	}
