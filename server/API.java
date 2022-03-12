import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import lib.AES;
import lib.HTTPCode;
import lib.IO;
import lib.JSON;
import lib.RandomGenerator;
import lib.SparkDB;

public class API {
	/**
	 * API Processing class
	 */
	static String ENCRYPTION_KEY = "";
	static byte[] BODY = null; // HTTPS Body
	static HashMap<String, String> SESSION_IDS = new HashMap<>(); // id, full name
	static SparkDB users = new SparkDB();
	static String code = HTTPCode.OK;
	static String verify_code , session_id = "";
	static String extension = "";

	public static HashMap<String, byte[]> redirector(HashMap<String, Object> Elshanta_temp) {
		HashMap<String, byte[]> res = new HashMap<>();
		ENCRYPTION_KEY = (String) Elshanta_temp.get("encryption_key");
		if(Elshanta_temp.containsKey("body")) BODY = (byte[]) Elshanta_temp.get("body");
		SESSION_IDS = (HashMap<String, String>) Elshanta_temp.get(Elshanta_temp);
		if(Elshanta_temp.containsKey("users_db")) users = (SparkDB) Elshanta_temp.get("users_db");
		if(Elshanta_temp.containsKey("verify_code")) verify_code = (String) Elshanta_temp.get("verify_code");
		if(Elshanta_temp.containsKey("session_id")) session_id = (String) Elshanta_temp.get("session_id");
		if(Elshanta_temp.containsKey("extension")) extension = (String) Elshanta_temp.get("extension");
		IO.write("./conf/queries.txt", String.valueOf(Integer.parseInt(new String(IO.read("./conf/queries.txt"))) + 1), false); // increase query by one
		String in = (String) Elshanta_temp.get("api");
		if (in.equals("name")) {
			res.put("body", name().getBytes()); // school name
			res.put("code", code.getBytes());
		}
		else if (in.equals("about")) {
			res.put("body", about().getBytes()); // about
			res.put("code", code.getBytes());
		}
		else if(in.equals("login")) {
			res.put("body", login().getBytes()); // login
			res.put("code", code.getBytes());
		}
		else if(in.equals("sfad")) {
			res.put("body", sfad().getBytes()); // search for a document
			res.put("code", code.getBytes());
		}
		else if(in.equals("dac")) {
			res.put("body", dac()); // download a document
			res.put("code", code.getBytes());
		}
		else if(in.equals("generate")) {
			res.put("body", generate().getBytes()); // generate verification code
			res.put("code", code.getBytes());
		}
		else if(in.equals("vad")) {
			res.put("body", vad().getBytes()); // verify a document
			res.put("code", code.getBytes());
		}
		 else {
			res.put("body", JSON.HMQ(new HashMap<String, String>() {{
				put("status","failed");
				put("msg","API request isn't understood");
			}}).getBytes());
			code = HTTPCode.BAD_REQUEST;
			res.put("code", code.getBytes());
		}
		return res;
	}
	/**
	 * This function tells the name of the school
	 * req : GET Request
	 * res : {"name":"x"}
	 */
	static String name() {
		try {
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("name", AES.decrypt(new String(IO.read("./conf/info.txt")), ENCRYPTION_KEY));
				}
			});
		} catch (Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error";
		}
	}
	/**
	 * About data to /about
	 * req : GET Request
	 * res : {"document_num":"a","query_num":"b"}
	 */
	static String about() {
		try {
			String res = "";
			HashMap<String, String> data = new HashMap<>();
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			data.put("document_num", String.valueOf(db.num_queries));
			data.put("query_num", new String(IO.read("./conf/queries.txt")));

			res = JSON.HMQ(data);
			return res;
		} catch (Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error";
		}
	}
	/**
	 * Login functionality
	 * req : {"user":"a","pass":"b"}
	 * res0 : {"status":"failed","msg":"c"}
	 * res1 : {"session_id":"a","first_name":"b"}
	 */
	static String login() {
		try {
			String res = "";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (users.get("user", in.get("user"), "password").equals(in.get("pass"))) {
				String random = RandomGenerator.getSaltString();
				SESSION_IDS.put(random, users.get("user", in.get("user"), "full_name"));
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("session_id", random);
						put("first_name", users.get("user", in.get("user"), "full_name"));
					}
				});
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Username or password isn't correct");
					}
				});
			}
			return res;
		} catch (Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error";
		}
	}
	/**
	 * Search for a document
	 * req : {"public_code":"a"}
	 * res0 : {"status":"failed","msg":"b"}
	 * res1 : {"document_name":"c","verifier":"d","writer":"e","date_of_publication":"f"}
	 */
	static String sfad() {
		try {
			String res = "";
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if(db.Mapper.get("pub_code").contains(in.get("public_code"))) {
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("document_name", db.get("pub_code", in.get("public_code"), "doc_name"));
					put("verifier", db.get("pub_code", in.get("public_code"), "verifier"));
					put("writer", db.get("pub_code", in.get("public_code"), "writer"));
					put("date_of_publication", db.get("pub_code", in.get("public_code"), "date"));
				}});
			}else {
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("status","failed");
					put("msg","Public code isn't found");
				}});
			}
			return res;
		} catch (Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error";
		}
	}
	/**
	 * Download a document
	 * req : {"public_code":"a"}
	 * res0 : {"status":"failed","msg":"b"}
	 * res1 : File byte[]
	 */
	static byte[] dac() {
		try {
			byte[] res = null;
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if(db.Mapper.get("pub_code").contains(in.get("public_code"))) {
				res = IO.read(db.get("pub_code", in.get("public_code"), "path"));
			}else {
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("status","failed");
					put("msg","Public code isn't found");
				}}).getBytes();
			}
			return res;
		} catch (Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error".getBytes();
		}
	}
	/**
	 * Generate verification code
	 * req : {"doc_name":"a","date":"b","writer":"c","session_id":"d"}
	 * res0 : {"verify_code":"e"}
	 * res1 : {"status":"failed","msg":"f"}
	 */
	static String generate() {
		try {
			String res = "";
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if(SESSION_IDS.containsKey(in.get("session_id"))) {
			String ver_code = RandomGenerator.getSaltString();
			String pub_code = RandomGenerator.getSaltString();
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			db.add(new String[] {
					pub_code, // public_code
					ver_code, // verify code
					"", // path
					in.get("doc_name"), //  document name
					SESSION_IDS.get(in.get("session_id")), // verifier
					in.get("writer"), // writer
					timeStamp, // date
			});
			new JSON();
			res = JSON.HMQ(new HashMap<String, String>() {{
				put("verify_code", ver_code);
			}});
			}else {
				new JSON();
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("status","failed");
					put("msg","Session ID isn't correct");
				}});
			}
			return res;
		}catch(Exception e) {
			code = HTTPCode.INTERNAL_SERVER_ERROR;
			return "error";
		}
	}
	/**
	 * Verify a document using file upload and verify code in headers
	 * req : file as binary in body and verify code in header
	 * res : {"msg":"a"}
	 */
	static String vad() {
		String res = "";
		try {
			// BODY is post req body compared to path in db
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			byte[] original = AES.decrypt(IO.read(db.get("verify_code", verify_code, "path")), ENCRYPTION_KEY);
			// compare original to BODY
			boolean compare = Arrays.compare(original, BODY) == 0; // if zero, then identical
			String msg = compare? "The file is identical with the verify code":"The file isn't identical with the verify code";
			res = JSON.HMQ(new HashMap<String, String>() {{
				put("msg",msg);
			}});
		}catch(Exception e) {
			res = "error";
			code = HTTPCode.INTERNAL_SERVER_ERROR;
		}
		return res;
	}
	/**
	 * Submit a document using verify code and session id and file
	 * res0 : {"public_code":"a","verify_code":"b"}
	 * res1 : {"status":"failed","msg":"c"}
	 */
	static String doc() {
		String res = "";
		try {
			if(SESSION_IDS.containsValue(session_id)) {
				// BODY is file
				String path = "./docs/"+RandomGenerator.getSaltString() +"."+ extension; // ./docs/a7akmigjdfigjdo.pdf
				new File(path).createNewFile();
				IO.write(path, BODY, false);
				// then put path into db
				SparkDB db = new SparkDB();
				db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
				db.change("verify_code", verify_code, "path", path);
				// then construct the response
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("public_code", db.get("verify_code", verify_code, "pub_code"));
					put("verify_code",verify_code);
				}});
			}else {
				res = JSON.HMQ(new HashMap<String, String>() {{
					put("status","failed");
					put("msg","Session ID isn't found");
				}});
			}
		}catch(Exception e) {
			res = "error";
			code = HTTPCode.INTERNAL_SERVER_ERROR;
		}
		return res;
	}
}
