package lib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
/**
 * Network IO Functions
 * @author Morad A.
 */
public class Network {
	public static String FixedHeaders = "X-XSS-Protection: 1; mode=block\r\n" + 
	"X-Frame-Options: DENY\r\n" + 
	"X-Content-Type-Options: nosniff\r\n" + 
	"Access-Control-Allow-Origin: *\r\n" + 
	"Access-Control-Allow-Methods: *\r\n" + 
	"Access-Control-Allow-Headers: *\r\n" + 
	"Access-Control-Allow-Credentials: true\r\n";

	/**
	 * GZIP Compression
	 *
	 * @param data data to compress in bytes
	 * @return compressed data in bytes
	 */
	public static byte[] compress(byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(data);
		gzip.close();
		byte[] compressed = bos.toByteArray();
		bos.close();
		return compressed;
	}

	/**
	 * HTTP Write Response Function
	 *
	 * @param dOS                  data stream to write to
	 * @param ResponseData         the body of the http response
	 * @param ContentType          the content type of the body
	 * @param ResponseCode         the response code (obv.)
	 * @param GZip                 weather to use GZip or not
	 * @param AddedResponseHeaders additional response headers to add like
	 *                             'X-XSS-Protection'
	 */
	public static void write(BufferedOutputStream dOS, byte[] ResponseData, String ContentType, String ResponseCode,
			boolean GZip, String AddedResponseHeaders, boolean Cache) {
		try {
			if (GZip)
				ResponseData = compress(ResponseData);
			dOS.write((ResponseCode + "\r\n").getBytes());
			dOS.write("Server: SWS 2.0\r\n".getBytes());
			if(Cache) dOS.write("Cache-Control: max-age=31536000\r\n".getBytes());
			dOS.write((AddedResponseHeaders).getBytes());
			dOS.write(("Connection: close\r\n").getBytes());
			if (GZip)
				dOS.write("Content-Encoding: gzip\r\n".getBytes());
			if (ContentType.equals("text/html")) {
				dOS.write(("Content-Type: " + ContentType + ";charset=UTF-8\r\n").getBytes());
			} else {
				dOS.write(("Content-Type: " + ContentType + "\r\n").getBytes());
			}
			dOS.write(FixedHeaders.getBytes());
			dOS.write(("Content-Length: " + ResponseData.length + "\r\n\r\n").getBytes());
			dOS.write(ResponseData);
			dOS.flush();
		} catch (Exception e) {
		}
	}

	/**
	 * Reads from socket into ArrayList
	 *
	 * @param MAX_REQ_SIZE the maximum kbytes to read
	 */
	public static ByteArrayOutputStream read(BufferedInputStream dIS, int MAX_REQ_SIZE) {
		MAX_REQ_SIZE = MAX_REQ_SIZE * 1000;
		ByteArrayOutputStream Reply = new ByteArrayOutputStream(1024);
		int counter = 0;
		try {
			ReadLoop: do {
				if (counter < MAX_REQ_SIZE) {
					Reply.write(dIS.readNBytes(1));
					counter++;
				} else {
					break ReadLoop;
				}
			} while (dIS.available() > 0);
		} catch (Exception e) {
		}
		return Reply;
	}

	/**
	 * Mandatory Read (used rarely)
	 */
	public static byte[] ManRead(BufferedInputStream dIS, int bytestoread) {
		try {
			return dIS.readNBytes(bytestoread);
		} catch (Exception e) {
		}
		return null;
	}
}
