import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import lib.AES;
import lib.FileToAL;
import lib.HTTPCode;
import lib.HeaderToHashmap;
import lib.IO;
import lib.MaxSizeHashMap;
import lib.MemMonitor;
import lib.PostRequestMerge;
import lib.SparkDB;
import lib.log;

public class Engine extends Server {
	/**
	 * Stage 3, HTTPS
	 */
	static String ENCRYPTION_KEY;
	static SparkDB MIME = new SparkDB();
	static Map<String, String> SESSION_IDS = Collections.synchronizedMap(new MaxSizeHashMap<String, String>(100)); // id, username
	static SparkDB users = new SparkDB();
	static SparkDB docs = new SparkDB();
	static Map<String, byte[]> WWWData = new HashMap<>();
	static ArrayList<String> WWWFiles = new ArrayList<>();
	// Here goes the constant objects that never removed after request process

	Engine(String ENC) {
		ENCRYPTION_KEY = ENC;
	}

	public void run() {
		try {
			MemMonitor MM = new MemMonitor();
			MM.start();
			MIME.readFromFile("mime.db");
			users.readFromString(AES.decrypt(new String(IO.read("./conf/users.db")), ENCRYPTION_KEY));
			docs.readFromString(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			WWWFiles = FileToAL.convert("WWWFiles.db");
			this.setMaximumConcurrentRequests(1000);
			this.setMaximumRequestSizeInKB(50000); // 50MB
			this.setGZip(false);
			this.setBacklog(10000);
			for(String file : WWWFiles) {
				if(new File("./www/"+file).isFile()) WWWData.put("/"+file, AES.decrypt(IO.read("./www/"+file), ENCRYPTION_KEY));
			}
			this.AddedResponseHeaders = "X-XSS-Protection: 1; mode=block\r\n" + "X-Frame-Options: DENY\r\n"
					+ "X-Content-Type-Options: nosniff\r\nAccess-Control-Allow-Origin: *\r\nAccess-Control-Allow-Methods: *\r\nAccess-Control-Allow-Headers: *\r\n";
					/**
					 * Get the local IP address of the preferred network interface, Google DNS
					 * reachability won't affect the function. From StackOverflow.
					 */
					try (final DatagramSocket socket = new DatagramSocket()) {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				String ip = socket.getLocalAddress().getHostAddress();
				log.s("Server is running, Device local IP Address: " + ip);
			}
			this.HTTPSStart(443, "./keystore.jks", "SWSTest");
		} catch (Exception e) {

		}
	}

	/**
	 * Dynamic Engine Entry point
	 */
	@Override
	HashMap<String, byte[]> main(List<byte[]> aLm, BufferedInputStream DIS, BufferedOutputStream DOS, int max_size) {
		try {
			HashMap<String, String> headers = HeaderToHashmap.convert(new String(aLm.get(0))); // headers
			HashMap<String, byte[]> response = new HashMap<>(); // content, mime, code
			HashMap<String, Object> Elshanta = new HashMap<>(); // stuff to send to API class
			if (headers.get("path").startsWith("/api.")) {
				/**
				 * API request detected
				 */
				String ser = headers.get("path").replaceFirst("/api.", "");
				Elshanta.put("body", PostRequestMerge.merge(aLm, DIS, headers, max_size));
				Elshanta.put("api", ser);
				Elshanta.put("encryption_key", ENCRYPTION_KEY);
				Elshanta.put("mime", MIME);
				if (ser.equals("login") || ser.equals("logout") || ser.equals("DataDoc")) {
					Elshanta.put("session_ids", SESSION_IDS);
					Elshanta.put("users_db", users);
				}
				if (ser.equals("generate") || ser.equals("doc")) Elshanta.put("session_ids", SESSION_IDS);
				if (Stream.of("about", "SearchDoc", "DownloadDoc", "generate", "VerifyDoc", "DataDoc").anyMatch(ser::equals)) Elshanta.put("docs_db", docs);
				if (headers.containsKey("code")) Elshanta.put("code", headers.get("code"));
				if (headers.containsKey("session_id")) Elshanta.put("session_id", headers.get("session_id"));
				if (headers.containsKey("extension")) Elshanta.put("extension", headers.get("extension"));
				HashMap<String, Object> res;
				try {
				res = new API().redirector(Elshanta); // Elshanta reply
				}catch(Exception e) {
					res = lib.ErrorWriter.wAPI(e, ENCRYPTION_KEY, ser);
				}
				if (res.containsKey("session_ids")) SESSION_IDS = (Map<String, String>) res.get("session_ids");
				if (res.containsKey("docs")) {
					docs = (SparkDB) res.get("docs");
					IO.write("./conf/docs.db", AES.encrypt(docs.toString(), ENCRYPTION_KEY), false);
				}
				response.put("content", (byte[]) res.get("body"));
				response.put("code", HTTPCode.OK.getBytes());
				response.put("mime", (byte[]) res.get("mime"));
			} else {
				/**
				 * Static file request detected
				 */
				String path = "";
				try {
				path = headers.get("path");
				if (path.equals("/login") || path.equals("/about") || path.equals("/support") || path.equals("/tools")
						|| path.equals("/operation"))
					path += ".html";
				if(WWWData.containsKey(path)) {
					response.put("content", WWWData.get(path));
					response.put("code", HTTPCode.OK.getBytes());
					String[] pathSplit = path.split("\\.");
					try {
						response.put("mime",
								MIME.get(new HashMap<String, String>() {{
									put("extension", pathSplit[pathSplit.length - 1]);
								}}, "mime", 1).get(0).getBytes()
								);
					}catch(Exception e) {
						response.put("mime", "text/html".getBytes());
					}
				}else {
					response.put("content","Unauthorized".getBytes());
					response.put("code", HTTPCode.UNAUTHORIZED.getBytes());
					response.put("mime", "text/html".getBytes());
				}
				}catch(Exception e) {
					response = lib.ErrorWriter.wnAPI(e, ENCRYPTION_KEY, path);
				}
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>() {
				{
					put("content", "Server error".getBytes());
					put("code", HTTPCode.SERVICE_UNAVAILABLE.getBytes());
					put("mime", "text/html".getBytes());
				}
			};

		}
	}
}
