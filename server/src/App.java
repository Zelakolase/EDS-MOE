import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import lib.AES;
import lib.CommandExecutor;
import lib.CommandPrompt;
import lib.EntropyCalc;
import lib.FilePaths;
import lib.FileToAL;
import lib.IO;
import lib.SHA;
import lib.SparkDB;
import lib.TOTP;
import lib.log;
import lib.TOTP.Secret;
import lib.TOTP.Secret.Size;

public class App {
	/* Changes value according to 'validate()' function */
	public static boolean validated = false;
	/* Global AES Object */
	public static AES aes;
	/* Global Console Object */
	public static Console console = System.console();

	/**
	 * Entry point for the application
	 */
	public static void main(String[] args) throws Exception {
		/* Mitigation against LOGJAM TLS Attack */
		System.setProperty("jdk.tls.ephemeralDHKeySize", "2048");
		/* Mitigation against Client Renegotiation Attack */
		System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true");

		/* Commmand Prompt Instance */
		CommandPrompt CP = new CommandPrompt() {
			@Override
			public void handler(String command) {
				if (command.equals("config")) config();
				if (command.equals("validate")) validate();
				if (command.equals("create")) create();
				if (command.equals("start")) start();
			}
		};

		/* Start the server (if exists) */
		CP.availableCommands.add("start");
		/* Create new server */
		CP.availableCommands.add("create");
		/* Validate all configuration files */
		CP.availableCommands.add("validate");
		/* Run configuration mode */
		CP.availableCommands.add("config");

		CP.helpCommands.put("start", "Start the HTTPS Server");
		CP.helpCommands.put("create", "Create a new server");
		CP.helpCommands.put("validate", "Validate all configuration files, databases, keys, and frontend files");
		CP.helpCommands.put("config", "Start Configuration mode");

		CP.run("Server");
	}

	/**
	 * Start HTTPS Server
	 */
	public static int start() {
		if (!validated) log.e("Please run 'validate' command first, then try again.");
		else {
			String inputServerKey = new String(console.readPassword("Enter the server key: "));
			aes = new AES(inputServerKey);
			/* Check if input key can decrypt server key file */
			boolean key = false;
			try {
				key = !aes.decrypt(new String(IO.read(FilePaths.ConfigurationDirectory.getValue() + "server.key"))).equals("ERR.ERR.ERR");
			} catch (Exception e) {}
			/* Check key state */
			if (!key) {
				log.e("Encryption key is not correct");
				return 1;
			}

			new Engine(inputServerKey).run();
		}

		return 0;
	}

	/**
	 * Validate all server files
	 */
	public static void validate() {
		validated = true; boolean state = false;

		checkFile("Frontend files", "WWWFiles.db");
		checkFile("Server key", FilePaths.ConfigurationDirectory.getValue() + "server.key");
		checkFile("Verifiers Database", FilePaths.ConfigurationDirectory.getValue() + "users.db");
		checkFile("Document Shard Map", FilePaths.ConfigurationDirectory.getValue() + "docs.db");
		checkFile("Document Metadata folder", FilePaths.ShardDirectory.getValue());

		/* Check all Database Shards */
		System.out.print("Documents Database Shards check: ");
		state = true;
		for (int i = 0; i < 1000; i++) { // Check every doc table
			state &= new File(FilePaths.ShardDirectory.getValue() + String.format("%03d", i) + ".db").exists();
		}
		update(state);

		checkFile("Server Metadata File", FilePaths.ConfigurationDirectory.getValue() + "metadata.db");
		checkFile("Document Storage folder", FilePaths.DocumentsDirectory.getValue());
		checkFile("Document Metadata Table", FilePaths.ConfigurationDirectory.getValue() + "Table.db");
		checkFile("MIME Table", "mime.db");
		checkFile("TLS/SSL KeyStore", FilePaths.ConfigurationDirectory.getValue() + "keystore.jks");
	}

	/**
	 * Start Configuration Mode
	 */
	public static void config() {
		ConfigMode CM = new ConfigMode();
		CM.main(aes);
	}

	/**
	 * Create a new server
	 */
	public static void create() {
		/* 1. Verifers' Profiles Insertion */
		String numVerifiers = console.readLine("Enter the number of verifiers: ");
		HashMap<String, ArrayList<Object>> verifiers = new HashMap<>(); // Key: Full name, Values: Username(String), Password(String), OTP(byte[])
		for (int iteration = 0; iteration < Integer.parseInt(numVerifiers); iteration++) {
			String user = "", full = "";
			/* isSimilar checks for collisions */
			boolean isSimilar = true;
			/* Loop for Full name collision */
			while (isSimilar) {
				full = console.readLine("Enter the verifier " + iteration + " full name: ");
				if (!verifiers.keySet().contains(full)) isSimilar = false;
				else log.e("The full name already exists");
			}

			isSimilar = true;
			/* Loop for username collision */
			while (isSimilar) {
				user = console.readLine("Enter the verifier " + iteration + " user name: ");

				boolean found = false;
				for(Entry<String, ArrayList<Object>> E : verifiers.entrySet()) {
					if(E.getValue().contains((String) user)) {
						found = true;
						break;
					}
				}
				if (! found) isSimilar = false;
				else log.e("The user name already exists");
			}

			/* Loop for a sophisticated password */
			String pass = "";
			boolean EntropyTestPass = false;
			while (!EntropyTestPass) {
				pass = new String(console.readPassword("Enter the verifier " + iteration + " password: "));
				String tempPass = new String(console.readPassword("Confirm the verifier " + iteration + " password: "));
				if(! tempPass.equals(pass)) {
					System.out.println("Passwords do not match!");
					continue;
				}
				if (EntropyCalc.calculate(pass) < 50.0) log.e("Weak password, Try typing a more sophisticated password.\n");
				else EntropyTestPass = true;
			}

			/* OTP Generation */
			byte[] RANDOTP = Secret.generate(Size.LARGE);
			System.out.println("Scan QR Code using Google Authenticator: " + TOTP.getQRUrl(user, "EDS", Secret.toBase32(RANDOTP)));

			final String fUser = user;
			final String fPass = pass;
			verifiers.put(full, new ArrayList<>() {{
				add((String) fUser);
				add((String) fPass);
				add((byte[]) RANDOTP);
			}});
		}

		/* 1.1. Write verifiers on disk, start of Try..Catch block */
		try {
			SparkDB Verifiers = new SparkDB();
			Verifiers.create(new ArrayList<>() {
				{
					add("full_name");
					add("user");
					add("pass");
					add("otp");
				}
			});

			for (Entry<String, ArrayList<Object>> Verifier : verifiers.entrySet()) {
				Verifiers.add(new HashMap<>() {
					{
						put("full_name", Verifier.getKey());
						put("user", SHA.gen((String) Verifier.getValue().get(0)));
						put("pass", SHA.gen((String) Verifier.getValue().get(1)));
						put("otp", Secret.toBase32((byte[]) Verifier.getValue().get(2)));
					}
				});
			}

			/* 2. Get Entity name */
			String entityName = console.readLine("Enter the entity name: ");
			/* 3. Get Server Key */
			String ServerK = new String(console.readPassword("Enter the new server key: "));
			aes = new AES(ServerK);
			/* 4. Generate folders */
			new File(FilePaths.ParentDirectory.getValue()).mkdir();
			new File(FilePaths.ConfigurationDirectory.getValue()).mkdir();
			new File(FilePaths.DocumentsDirectory.getValue()).mkdir();
			new File(FilePaths.ShardDirectory.getValue()).mkdir();
			/* 5. Store Server Key */
			new File(FilePaths.ConfigurationDirectory.getValue() + "server.key").createNewFile();
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "server.key", aes.encrypt(ServerK), false);
			/* 6. Store Verifiers */
			new File(FilePaths.ConfigurationDirectory.getValue() + "users.db").createNewFile();
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "users.db", aes.encrypt(Verifiers.toString()), false);
			/* 7. Generate Key Store */
			CommandExecutor.executeCommand("keytool -genkeypair -keyalg RSA -keysize 2048 -validity 360000 -keystore " + FilePaths.ConfigurationDirectory.getValue() + "keystore.jks -storepass " + SHA.gen(ServerK).substring(0, 10) + " -keypass " + SHA.gen(ServerK).substring(0, 10) + " -dname \"O=EDS\"");
			/* 8. Generate Server Metadata DB */
			SparkDB ServerMetaData = new SparkDB();
			ServerMetaData.create(new ArrayList<>() {{
				add("entityName");
				add("numQueries");
				add("numDocs");
			}});
			ServerMetaData.add(new HashMap<>() {{
				put("entityName", entityName);
				put("numQueries", "0");
				put("numDocs", "0");
			}});
			new File(FilePaths.ConfigurationDirectory.getValue() + "metadata.db").createNewFile();
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "metadata.db", aes.encrypt(ServerMetaData.toString()), false);
			/* 9. Generate Table DB */
			SparkDB T = new SparkDB();
			T.create(new ArrayList<String>() {
				{
					add("DocName");
					add("Verifier");
					add("Writer");
					add("DocNum");
				}
			});
			T.add(new HashMap<String, String>() {
				{
					put("DocName", "0");
					put("Verifier", "0");
					put("DocNum", "0");
					put("Writer", "0");
				}
			});
			new File(FilePaths.ConfigurationDirectory.getValue() + "Table.db").createNewFile();
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "Table.db", aes.encrypt(T.toString()), false);
			/* 10. Generate Database Shard List */
			SparkDB temp = new SparkDB();
			temp.create(new ArrayList<String>() {
			{
				add("prefix");
				add("size");
				add("min_query");
			}
			});
			for (int i = 0; i < 1000; i++) {
				final String tempStr = String.format("%03d", i);
				temp.add(new HashMap<String, String>() {
					{
						put("prefix", tempStr);
						put("size", "0");
						put("min_query", "0");
					}
				});
			}
			new File(FilePaths.ConfigurationDirectory.getValue() + "docs.db").createNewFile();
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "docs.db", aes.encrypt(temp.toString()), false);
			/* 11. Generate Database Shards */
			SparkDB tempDB = new SparkDB();
			tempDB.create(new ArrayList<String>() {
				{
					add("code");
					add("path");
					add("doc_name");
					add("verifier");
					add("writer");
					add("sha");
					add("age");
					add("date");
				}
			});
			for (int i = 0; i < 1000; i++) {
				new File(FilePaths.ShardDirectory.getValue() + String.format("%03d", i) + ".db").createNewFile();
				IO.write(FilePaths.ShardDirectory.getValue() + String.format("%03d", i) + ".db", aes.encrypt(tempDB.toString()), false);
			}
			/* 12. Encrypt WWW Files */
			ArrayList<String> Paths = FileToAL.convert("WWWFiles.db");
			for(String path : Paths) {
				if(new File("./www/" + path).isFile()) IO.write("./www/" + path, aes.encrypt(IO.read("./www/" + path)), false);
			}
		} catch (Exception e) {
			log.e(e, "main", "create");
		}
	}

	/**
	 * Check all frontend files
	 * 
	 * @param frontendFiles The list of frontend files
	 * @return Statu
	 */
	public static boolean FFC(ArrayList<String> frontendFiles) {
		if (frontendFiles.size() == 0) return false;

		for (String frontendFile : frontendFiles) {
			if (!new File("./www/" + frontendFile).exists()) return false;
		}

		return true;
	}

	/**
	 * A function for some repeated code
	 */
	public static void update(boolean state) {
		if (state) log.s("OK");
		else log.e("FAILED");
		validated &= state;
	}

	/**
	 * Define a helper method to avoid repetition
	 */ 
	private static void checkFile(String name, String path) {
		System.out.print(name + " check: ");
		update(new File(path).exists());
	}
}