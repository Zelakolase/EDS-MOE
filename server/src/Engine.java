import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import lib.AES;
import lib.FilePaths;
import lib.FileToAL;
import lib.HTTPCode;
import lib.HeaderToHashmap;
import lib.IO;
import lib.MaxSizeHashMap;
import lib.MemMonitor;
import lib.PostRequestMerge;
import lib.SHA;
import lib.SparkDB;
import lib.log;

/**
 * The Web Engine. Responsible for starting the web server and redirecting
 * requests to either Static file processing or API processing.
 * 
 * @author morad
 */
public class Engine extends Server {
	/**
	 * Server Encryption Key
	 */
	static volatile String ENCRYPTION_KEY;
	/**
	 * MIMEs
	 */
	static volatile SparkDB MIME = new SparkDB();
	/**
	 * Session IDs, maximum entries are 100. Key: ID, Value: Username
	 */
	static volatile Map<String, String> SESSION_IDS = Collections
			.synchronizedMap(new MaxSizeHashMap<String, String>(100)); // id, username
	/**
	 * Users DB
	 */
	static volatile SparkDB users = new SparkDB();
	/**
	 * DocsDB for redirection
	 */
	static volatile SparkDB docs = new SparkDB();
	/**
	 * Static Files data. Key: Supposed Relative path, Value: Byte raw data of the
	 * file
	 */
	static volatile Map<String, byte[]> WWWData = new ConcurrentHashMap<>();
	/**
	 * Files relative paths
	 */
	static volatile ArrayList<String> WWWFiles = new ArrayList<>();
	/**
	 * Server Metadata DB
	 */
	static volatile SparkDB Metadata = new SparkDB();
	/**
	 * AES Object
	 */
	static volatile AES aes;

	/**
	 * Engine calling constructor
	 * 
	 * @param ENC Server Encryption Key
	 */
	Engine(String ENC) {
		ENCRYPTION_KEY = ENC;
		aes = new AES(ENCRYPTION_KEY);
	}

	/**
	 * Web Server Starter
	 */
	public void run() {
		try {
			/* Start Memory Monitor Thread */
			MemMonitor MM = new MemMonitor();
			MM.start();
			/* Read all databases */
			MIME.readFromFile("mime.db");
			users.readFromString(aes.decrypt(new String(IO.read(FilePaths.ConfigurationDirectory.getValue() + "users.db"))));
			docs.readFromString(aes.decrypt(new String(IO.read(FilePaths.ConfigurationDirectory.getValue() + "docs.db"))));
			WWWFiles = FileToAL.convert("WWWFiles.db");
			Metadata.readFromString(aes.decrypt(new String(IO.read(FilePaths.ConfigurationDirectory.getValue() + "metadata.db"))));
			/* TCP Settings */
			this.setMaximumRequestSizeInKB(10000); // 10MB
			this.setGZip(false);
			this.setBacklog(50000);
			/* Decrypt and Load WWW Files */
			for (String file : WWWFiles) {
				if (new File("./www/" + file).isFile()) WWWData.put("/" + file, aes.decrypt(IO.read("./www/" + file)));
			}
			/* Start Server */
			try (final DatagramSocket socket = new DatagramSocket()) {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				String ip = socket.getLocalAddress().getHostAddress();
				log.s("Server is running, Device local IP Address: " + ip);
			}
			this.HTTPSStart(443, FilePaths.ConfigurationDirectory.getValue() + "keystore.jks", SHA.gen(ENCRYPTION_KEY).substring(0, 10));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dynamic Engine Entry point
	 * @param aLm Request split by '\r\n'
	 * @param max_size Maximum request size in bytes.
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	HashMap<String, byte[]> main(List<byte[]> aLm, BufferedInputStream DIS, BufferedOutputStream DOS, int max_size)
			throws Exception {
		try {
			HashMap<String, String> headers = HeaderToHashmap.convert(new String(aLm.get(0))); // headers
			HashMap<String, byte[]> response = new HashMap<>(); // content, mime, code
			HashMap<String, Object> Elshanta = new HashMap<>(); // stuff to send to API class
			if (headers.get("path").startsWith("/api.")) {
				/**
				 * API request detected
				 */

				/* Retrieve API Endpoint */
				String ser = headers.get("path").replaceFirst("/api.", "");

				/* Verify a complete request body */
				Elshanta.put("body", PostRequestMerge.merge(aLm, DIS, headers, max_size));
				Elshanta.put("api", ser);
				Elshanta.put("encryption_key", ENCRYPTION_KEY);
				Elshanta.put("mime", MIME);
				Elshanta.put("aes", aes);

				/* Pass in 'Elshanta' the required arguments */
				if (Stream.of("login", "logout", "DataDoc", "table").anyMatch(ser::equals)) {
					Elshanta.put("session_ids", SESSION_IDS);
					Elshanta.put("users_db", users);
				}
				Elshanta.put("metadata", Metadata);
				if (ser.equals("generate") || ser.equals("doc")) Elshanta.put("session_ids", SESSION_IDS);
				if (Stream.of("about", "SearchDoc", "DownloadDoc", "generate", "VerifyDoc", "DataDoc").anyMatch(ser::equals))
					Elshanta.put("docs_db", docs);

				/* If custom specified headers exist, pass them on */
				if (headers.containsKey("code")) Elshanta.put("code", headers.get("code"));
				if (headers.containsKey("session_id")) Elshanta.put("session_id", headers.get("session_id"));
				if (headers.containsKey("Cookie")) Elshanta.put("Cookie", headers.get("Cookie"));
				if (headers.containsKey("extension")) Elshanta.put("extension", headers.get("extension"));

				/* Initialize Response */
				HashMap<String, Object> res;
				try {
					/* Get the response */
					res = new API().redirector(Elshanta); // Elshanta reply
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					res = new HashMap<>() {
						{
							put("body", ("Error. Please send the following text to the administrator: "
									+ aes.encrypt(sw.toString())).getBytes());
							put("code", HTTPCode.BAD_REQUEST);
							put("mime", "text/html".getBytes());
						}
					};
				}

				/* If there is an update for Session IDs, do it */
				if (res.containsKey("session_ids")) SESSION_IDS = (Map<String, String>) res.get("session_ids");
				/* If there is an update for document shard list, do it */
				boolean docsUpdated = false;
				if (res.containsKey("docs")) {
					docs = (SparkDB) res.get("docs");
					docsUpdated = true;
				}
				/* If there is an update for Metadata, do it */
				if(res.containsKey("metadata")) Metadata = (SparkDB) res.get("metadata");

				/* Retrieve Response */
				response.put("content", (byte[]) res.get("body"));
				response.put("code", HTTPCode.OK.getValue().getBytes());
				response.put("mime", (byte[]) res.get("mime"));

				/* Start writeSyncThread */
				writeSyncThread wST = new writeSyncThread(docsUpdated);
				wST.start();

				if (res.containsKey("extension")) response.put("extension", (byte[]) res.get("extension"));
			} else {
				/**
				 * Static file request detected
				 */
				String path = "";
				try {
					path = headers.get("path");
					/* If any of the specified Strings is equal to path */
					if (Stream.of("/login", "/support", "/tools", "/operation", "/index", "/submit", "/board", "/about")
							.anyMatch(path::equals))
						path += ".html";
					if (WWWData.containsKey(path)) {
						response.put("content", WWWData.get(path));
						response.put("code", HTTPCode.OK.getValue().getBytes());
						String[] pathSplit = path.split("\\.");
						/* Try to grab MIME from mime.db */
						try {
							response.put("mime",
									MIME.get(new HashMap<String, String>() {
										{
											put("extension", pathSplit[pathSplit.length - 1]);
										}
									}, "mime", 1).get(0).getBytes());
						} catch (Exception e) {
							/* If mime.db does not have the extension, default it to text/html */
							response.put("mime", "text/html".getBytes());
						}
					} else {
						/* If the user tried to access unspecified path */
						response.put("content", "Unauthorized".getBytes());
						response.put("code", HTTPCode.UNAUTHORIZED.getValue().getBytes());
						response.put("mime", "text/html".getBytes());
					}
				} catch (Exception e) {
					/* If error happened, print encrypted error message */
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					return new HashMap<>() {
						{
							put("content", ("Error. Please send the following text to the administrator: "
									+ aes.encrypt(sw.toString())).getBytes());
							put("code", HTTPCode.SERVICE_UNAVAILABLE.getValue().getBytes());
							put("mime", "text/html".getBytes());
						}
					};
				}
			}
			return response;
		} catch (Exception e) {
			/* If error happened, print encrypted error message */
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return new HashMap<>() {
				{
					put("content", ("Error. Please send the following text to the administrator: "
							+ aes.encrypt(sw.toString())).getBytes());
					put("code", HTTPCode.SERVICE_UNAVAILABLE.getValue().getBytes());
					put("mime", "text/html".getBytes());
				}
			};
		}
	}


	public static class writeSyncThread extends Thread {
		public boolean docsUpdated = false;

		public writeSyncThread(boolean docsUpdated) {
			this.docsUpdated = docsUpdated;
		}

		@Override
		public void run() {
			try {
			/* Update Metadata */
			IO.write(FilePaths.ConfigurationDirectory.getValue() + "metadata.db", aes.encrypt(Metadata.toString()), false);
			/* Update Docs DB */
			if(docsUpdated) IO.write(FilePaths.ConfigurationDirectory.getValue() + "docs.db", aes.encrypt(docs.toString()), false);
			}catch(Exception e) {
				// Shhhh....
			}
		}
	}
}
