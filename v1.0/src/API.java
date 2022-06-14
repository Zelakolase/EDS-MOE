import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
	String ENCRYPTION_KEY = "";
	Map<String, String> SESSION_IDS; // id, full name
	SparkDB users = new SparkDB();
	SparkDB docs = new SparkDB();
	String mime = "application/json";
	SparkDB MIME = new SparkDB();


	public HashMap<String, Object> redirector(HashMap<String, Object> Elshanta_temp) throws Exception {
		String code = HTTPCode.OK;
		String verify_code = "", session_id = "";
		String extension = "";
		byte[] BODY = null; // HTTPS Body
		HashMap<String, Object> res = new HashMap<>();
		ENCRYPTION_KEY = (String) Elshanta_temp.get("encryption_key");
		MIME = (SparkDB) Elshanta_temp.get("mime");
		if (Elshanta_temp.containsKey("body"))
			BODY = (byte[]) Elshanta_temp.get("body");
		if (Elshanta_temp.containsKey("session_ids"))
			SESSION_IDS = (Map<String, String>) Elshanta_temp.get("session_ids");
		if (Elshanta_temp.containsKey("users_db"))
			users = (SparkDB) Elshanta_temp.get("users_db");
		if (Elshanta_temp.containsKey("verify_code"))
			verify_code = (String) Elshanta_temp.get("verify_code");
		if (Elshanta_temp.containsKey("session_id"))
			session_id = (String) Elshanta_temp.get("session_id");
		if (Elshanta_temp.containsKey("extension"))
			extension = (String) Elshanta_temp.get("extension");
		if (Elshanta_temp.containsKey("docs_db"))
			docs = (SparkDB) Elshanta_temp.get("docs_db");
		IO.write("./conf/queries.txt", String.valueOf(Integer.parseInt(new String(IO.read("./conf/queries.txt"))) + 1),
				false); // increase query by one
		String in = (String) Elshanta_temp.get("api");
		if (in.equals("name")) {
			res.put("body", name().getBytes()); // school name
			res.put("code", code.getBytes());
		} else if (in.equals("about")) {
			res.put("body", about().getBytes()); // about
			res.put("code", code.getBytes());
		} else if (in.equals("login")) {
			res.put("body", login(BODY).getBytes()); // login
			res.put("code", code.getBytes());
		} else if (in.equals("sfad")) {
			res.put("body", sfad(BODY).getBytes()); // search for a document
			res.put("code", code.getBytes());
		} else if (in.equals("dac")) {
			res.put("body", dac(BODY)); // download a document
			res.put("code", code.getBytes());
		} else if (in.equals("generate")) {
			res.put("body", generate(BODY).getBytes()); // generate verification code
			res.put("code", code.getBytes());
		} else if (in.equals("vad")) {
			res.put("body", vad(BODY, verify_code).getBytes()); // verify a document
			res.put("code", code.getBytes());
		} else if (in.equals("logout")) {
			res.put("body", logout(BODY).getBytes()); // verify a document
			res.put("code", code.getBytes());
		} else if (in.equals("doc")) {
			res.put("body", doc(BODY, session_id, verify_code, extension).getBytes()); // SAD S.2
			res.put("code", code.getBytes());
		} else {
			res.put("body", JSON.HMQ(new HashMap<String, String>() {
				{
					put("status", "failed");
					put("msg", "API request isn't understood");
				}
			}).getBytes());
			code = HTTPCode.BAD_REQUEST;
			res.put("code", code.getBytes());
		}
		if (res.get("body").equals("error"))
			code = HTTPCode.INTERNAL_SERVER_ERROR;
		if (Elshanta_temp.containsKey("session_ids"))
			res.put("session_ids", SESSION_IDS);
		if (Elshanta_temp.containsKey("docs_db"))
			res.put("docs", docs);
		if (Elshanta_temp.containsKey("users_db"))
			res.put("users", users);
		res.put("mime", mime);
		return res;
	}

	/**
	 * Log out req : {"session_id":"a","pass":"b"} res : {"status":"c"} Where 'c' is
	 * either failed or success
	 */
	String logout(byte[] BODY) throws Exception{
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (SESSION_IDS.containsKey(in.get("session_id"))) {
				String password = users.get("full_name", SESSION_IDS.get(in.get("session_id")), "password");
				if (password.equals(in.get("pass"))) {
					return JSON.HMQ(new HashMap<String, String>() {
						{
							put("status", "success");
						}
					});
				}
			}
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("status", "failed");
				}
			});
	}

	/**
	 * This function tells the name of the school req : GET Request res :
	 * {"name":"x"}
	 */
	String name() throws Exception{
			return JSON.HMQ(new HashMap<String, String>() {
				{
					put("name", AES.decrypt(new String(IO.read("./conf/info.txt")), ENCRYPTION_KEY));
				}
			});
	}

	/**
	 * About data to /about req : GET Request res :
	 * {"document_num":"a","query_num":"b"}
	 */
	String about() throws Exception{
			HashMap<String, String> data = new HashMap<>();
			SparkDB db = new SparkDB();
			db.readfromstring(AES.decrypt(new String(IO.read("./conf/docs.db")), ENCRYPTION_KEY));
			data.put("document_num", String.valueOf(db.num_queries));
			data.put("query_num", new String(IO.read("./conf/queries.txt")));

			return JSON.HMQ(data);
	}

	/**
	 * Login functionality req : {"user":"a","pass":"b"} res0 :
	 * {"status":"failed","msg":"c"} res1 : {"session_id":"a","first_name":"b"}
	 */
	String login(byte[] BODY) throws Exception {
			String res = "error";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (users.Mapper.get("user").contains(in.get("user"))
					&& users.get("user", in.get("user"), "password").equals(in.get("pass"))) {
				String random = RandomGenerator.getSaltString(50, 0); // Session ID
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
	}

	/**
	 * Search for a document req : {"public_code":"a"} res0 :
	 * {"status":"failed","msg":"b"} res1 :
	 * {"document_name":"c","verifier":"d","writer":"e","date_of_publication":"f"}
	 */
	String sfad(byte[] BODY) throws Exception {
			String res = "error";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (docs.Mapper.get("pub_code").contains(in.get("public_code"))) {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("document_name", docs.get("pub_code", in.get("public_code"), "doc_name"));
						put("verifier", docs.get("pub_code", in.get("public_code"), "verifier"));
						put("writer", docs.get("pub_code", in.get("public_code"), "writer"));
						put("date_of_publication", docs.get("pub_code", in.get("public_code"), "date"));
					}
				});
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Public code isn't found");
					}
				});
			}
			return res;
	}

	/**
	 * Download a document req : {"public_code":"a"} res0 :
	 * {"status":"failed","msg":"b"} res1 : File byte[]
	 */
	byte[] dac(byte[] BODY) throws Exception {
			byte[] res = {};
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (docs.Mapper.get("pub_code").contains(in.get("public_code"))) {
				res = AES.decrypt(IO.read(docs.get("pub_code", in.get("public_code"), "path")), ENCRYPTION_KEY);
				String[] pathSplit = docs.get("pub_code", in.get("public_code"), "path").split("\\.");
				//mime = MIME.get("extension", pathSplit[pathSplit.length - 1], "mime");
				mime = pathSplit[pathSplit.length - 1];
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Public code isn't found");
					}
				}).getBytes();
			}
			return res;
	}

	/**
	 * Generate verification code req :
	 * {"doc_name":"a","date":"b","writer":"c","session_id":"d"} res0 :
	 * {"verify_code":"e"} res1 : {"status":"failed","msg":"f"}
	 */
	String generate(byte[] BODY) throws Exception {
			String res = "";
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			if (SESSION_IDS.containsKey(in.get("session_id"))) {
				String ver_code = "";
				String pub_code = "";
				do {
					ver_code = RandomGenerator.getSaltString(4, 1) + "-" + RandomGenerator.getSaltString(4, 1) + "-"
							+ RandomGenerator.getSaltString(4, 1) + "-" + RandomGenerator.getSaltString(4, 1);
				} while (docs.Mapper.get("verify_code").contains(ver_code));
				do {
					pub_code = RandomGenerator.getSaltString(3, 2) + "-" + RandomGenerator.getSaltString(3, 2) + "-"
							+ RandomGenerator.getSaltString(3, 2);
				} while (docs.Mapper.get("pub_code").contains(pub_code));
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
				docs.add(new String[] {
						pub_code, // public_code
						ver_code, // verify code
						"0", // path
						in.get("doc_name"), // document name
						SESSION_IDS.get(in.get("session_id")), // verifier
						in.get("writer"), // writer
						timeStamp, // date
						"0" // sha
				});
				IO.write("./conf/docs.db", AES.encrypt(docs.print(), ENCRYPTION_KEY), false);
				new JSON();
				/*
				 * Bug Fix : Local variable ver_code defined in an enclosing scope must be final
				 * or effectively final.
				 */
				HashMap<String, String> temp = new HashMap<>();
				temp.put("verify_code", ver_code);
				res = JSON.HMQ(temp);
			} else {
				new JSON();
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Session ID isn't correct");
					}
				});
			}
			return res;
	}

	/**
	 * Verify a document using file upload and verify code in headers req : file as
	 * binary in body and verify code in header res : {"msg":"a"}
	 */
	String vad(byte[] BODY, String verify_code) throws Exception {
		String res = "error";
			// BODY is post req body compared to path in db docs
			// compare original sha to BODY sha
			if(docs.Mapper.get("verify_code").contains(verify_code)) {
			boolean compare = docs.get("verify_code", verify_code, "sha")
					.equals(
							Arrays.toString(MessageDigest.getInstance("SHA-256").digest(BODY))
							);
			String msg = compare ? "The file is identical with the verify code"
					: "The file isn't identical with the verify code";
			res = JSON.HMQ(new HashMap<String, String>() {
				{
					put("msg", msg);
				}
			});
			}else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("msg","Verify Code isn't found");
					}
				});
			}
		return res;
	}

	/**
	 * Submit a document using verify code and session id and file res0 :
	 * {"public_code":"a","verify_code":"b"} res1 : {"status":"failed","msg":"c"}
	 */
	String doc(byte[] BODY, String session_id, String verify_code, String extension) throws Exception {
		String res = "error";
			if (SESSION_IDS.containsKey(session_id)) {
				// BODY is file
				String path = "./docs/" + RandomGenerator.getSaltString(20, 0) + "." + extension; // ./docs/aakF5igjdfigjdo.pdf
				new File(path).createNewFile();
				IO.write(path, AES.encrypt(BODY, ENCRYPTION_KEY), false);
				docs.change("verify_code", verify_code, "path", path);
				docs.change("verify_code", verify_code, "sha",
						Arrays.toString(MessageDigest.getInstance("SHA-256").digest(BODY))
						);
				IO.write("./conf/docs.db", AES.encrypt(docs.print(), ENCRYPTION_KEY), false);
				// then construct the response
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("public_code", docs.get("verify_code", verify_code, "pub_code"));
						put("verify_code", verify_code);
					}
				});
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Session ID isn't found");
					}
				});
			}
		return res;
	}
}
