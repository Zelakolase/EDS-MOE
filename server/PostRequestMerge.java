import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import lib.Network;

public class PostRequestMerge {
	public static byte[] merge(List<byte[]> aLm, DataInputStream DIS, HashMap<String, String> headers) throws IOException {
		ByteArrayOutputStream whole = new ByteArrayOutputStream();
		whole.write(aLm.get(1));
		if (aLm.size() - 2 != 0) {
			// ALm(1) is the whole
			int x = aLm.size() - 2; // 1 element after ALm(1) if x = 1
			for (int i = 1; i <= x; i++) {
				byte[] CRLFCRLF = { 13, 10, 13, 10 };
				whole.write(CRLFCRLF);
				whole.write(aLm.get(i + 1));
			}
		}
		if(headers.containsKey("Content-Length")) {
			int num = Integer.parseInt(headers.get("Content-Length")); // num. of bytes in body only
			if(whole.size() < num) {
				// "We're in deep shit, na7n fe karb 3azeem" - Amr Diab or Adib idc
				whole.write(Network.ManRead(DIS, num - whole.size()));
			}else {
				// Phew, we got the whole request without TLS record shit
			}
		}
		return whole.toByteArray();
	}
}
