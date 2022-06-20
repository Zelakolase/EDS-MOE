
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class API {
	private String IP = "";
	private String API_POINT = "";

	public API(String ip, String api) {
		this.IP = ip;
		this.API_POINT = api;
	}

	public byte[] send() {
		return send(new byte[] {});
	}

	public byte[] send(byte[] body) {
		return send(new HashMap<String, String>(), body);
	}

	public byte[] send(HashMap<String, String> Headers, byte[] body) {
		try {
			StringBuilder request = new StringBuilder("GET /").append(API_POINT).append("\r\n");
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket(IP, 443);
			socket.startHandshake();
			for (Entry<String, String> e : Headers.entrySet()) {
				request.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
			}
			request.append("\r\n");
			BufferedOutputStream BOS = new BufferedOutputStream(socket.getOutputStream(), 1024);
			BufferedInputStream BIS = new BufferedInputStream(socket.getInputStream(), 1024);
			BOS.write(request.toString().getBytes());
			BOS.write(body);
			BOS.flush();
			byte[] out = BIS.readAllBytes();
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
