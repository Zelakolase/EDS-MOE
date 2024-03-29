package Endpoints;

import java.util.HashMap;
import java.util.Map;

import lib.AES;
import lib.FilePaths;
import lib.IO;
import lib.JSON;
import lib.SparkDB;

public class DownloadDoc {

	public Map<String, byte[]> run(byte[] BODY, String ENCRYPTION_KEY, AES aes) throws Exception {
		Map<String, byte[]> out = new HashMap<>();
			byte[] res = {};
			HashMap<String, String> in = JSON.QHM(new String(BODY));
			in.put("code", in.get("code").replaceAll("-", ""));
			SparkDB docs = new SparkDB();
			docs.readFromFile(FilePaths.ShardDirectory.getValue()+in.get("code").substring(0, 3)+".db", ENCRYPTION_KEY);
			if (docs.getColumn("code").contains(in.get("code"))) {
				String path = docs.get(new HashMap<String, String>() {{
					put("code", in.get("code"));
				}}, "path", 1).get(0);
				res = aes.decrypt(IO.read(path));
				String[] pathSplit = path.split("\\.");
				out.put("mime", pathSplit[pathSplit.length - 1].toLowerCase().getBytes());
			} else {
				res = JSON.HMQ(new HashMap<String, String>() {
					{
						put("status", "failed");
						put("msg", "Public code isn't found");
					}
				}).getBytes();
				out.put("mime", "application/json".getBytes());
			}
			out.put("body", res);
			return out;
	}
}
