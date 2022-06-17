package lib;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IO {
	/**
	 * Reads file as byte array
	 *
	 * @param filename the name of the dest. file
	 * @return file content in bytes
	 */
	public static byte[] read(String filename) throws Exception {
			RandomAccessFile f = new RandomAccessFile(filename, "r");
			byte[] b = new byte[(int)f.length()];
				f.readFully(b);
				f.close();
			return b;
	}

	/**
	 * Writes on file
	 *
	 * @param filename the name of the dest. file
	 * @param content  the content to write in String
	 * @param append   weather to append to existing value or not
	 */
	public static void write(String filename, String content, boolean append) throws Exception{
			write(filename, content.getBytes(), append);
	}

	/**
	 * Writes on file
	 *
	 * @param filename the name of the dest. file
	 * @param content  the content to write in bytes
	 * @param append   weather to append to existing value or not
	 */
	public static void write(String filename, byte[] content, boolean append) {
		StandardOpenOption set = append? StandardOpenOption.APPEND : StandardOpenOption.WRITE;
			(new Thread() {
				  @Override
				public void run() {
						try {
							Files.write(Paths.get(filename), content, set);
						} catch (IOException e) {
							log.e(e, IO.class.getName(), "write");
						}
				  }
				 }).start();
	}
}