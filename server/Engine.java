import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	static ArrayList<String> WWWFiles = new ArrayList<>();
	// Here goes the constant objects that never removed after request process

	Engine(String ENC) {
		ENCRYPTION_KEY = ENC;
	}

	public void run() {
		try {
		MemMonitor MM = new MemMonitor();
		MM.start();
		MIME.readfromfile("mime.db");
		users.readfromstring(AES.decrypt(new String(IO.read("./conf/users.db")), ENCRYPTION_KEY));
		docs.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
		WWWFiles = FileToAL.convert("WWWFiles.db");
		this.setMaximumConcurrentRequests(2500);
		this.setMaximumRequestSizeInKB(100000); // 100MB
		this.setGZip(false);
		this.setBacklog(this.MaxConcurrentRequests * 5);
		this.AddedResponseHeaders = "X-XSS-Protection: 1; mode=block\r\n" + "X-Frame-Options: DENY\r\n"
				+ "X-Content-Type-Options: nosniff\r\n";
		this.HTTPSStart(443, "./keystore.jks", "SWSTest");
		/**
		 * Get the local IP address of the preferred network interface, Google DNS reachability won't affect the function.
		 * From StackOverflow.
		 */
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  String ip = socket.getLocalAddress().getHostAddress();
			  log.s("Server is running, Device local IP Address: "+ip);
			}
		}catch(Exception e) {
			log.e(e, "Engine", "run()");
		}
	}

	@Override
	HashMap<String, byte[]> main(List<byte[]> aLm, DataInputStream DIS, DataOutputStream DOS) {
		try {
			HashMap<String, String> headers = HeaderToHashmap.convert(new String(aLm.get(0))); // headers
			HashMap<String, byte[]> response = new HashMap<>(); // content, mime, code
			HashMap<String, Object> Elshanta = new HashMap<>(); // stuff to send to API class
			if (headers.get("path").startsWith("/api.")) {
				/**
				 * API request detected
				 */
				String ser = headers.get("path").replaceFirst("/api.", "");
				Elshanta.put("body", PostRequestMerge.merge(aLm, DIS, headers));
				Elshanta.put("api", ser);
				Elshanta.put("encryption_key", ENCRYPTION_KEY);
				if(ser.equals("login") || ser.equals("logout")) {
					Elshanta.put("session_ids", SESSION_IDS);
					Elshanta.put("users_db", users);
				}
				if(ser.equals("generate") || ser.equals("doc")) Elshanta.put("session_ids", users);
				if(Stream.of("about","sfad","dac","generate","vad","doc").anyMatch(ser::equals)) Elshanta.put("docs_db", docs);
				if(headers.containsKey("verify_code")) Elshanta.put("verify_code", headers.get("verify_code"));
				if(headers.containsKey("session_id")) Elshanta.put("session_id", headers.get("session_id"));
				if(headers.containsKey("extension")) Elshanta.put("extension", headers.get("extension"));
				HashMap<String, Object> res = API.redirector(Elshanta); // Elshanta reply
				if(res.containsKey("session_ids")) SESSION_IDS = (ConcurrentHashMap<String, String>) res.get("session_ids");
				if(res.containsKey("users")) users = (SparkDB) res.get("users");
				if(res.containsKey("docs")) docs = (SparkDB) res.get("docs");
				response.put("content", (byte[]) res.get("body"));
				response.put("code", HTTPCode.OK.getBytes());
				response.put("mime", "application/json".getBytes());
			} else {
				/**
				 * Static file request detected
				 */
				String path = headers.get("path");
				if(path.equals("/login") || path.equals("/about") || path.equals("/support") || path.equals("/tools")) path += ".html";
				HashMap<String, byte[]> static_res = FileProcess.redirector(path.substring(1),WWWFiles);
				response.put("content", static_res.get("body"));
				response.put("code", static_res.get("code"));
				String[] pathSplit = path.split("\\.");
				response.put("mime", MIME.get("extension", pathSplit[pathSplit.length - 1], "mime").getBytes());
			}
			return response;
		} catch (Exception e) {
			log.e(e, "Engine", "main");
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
