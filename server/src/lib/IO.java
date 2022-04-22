package lib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	public static byte[] read(String filename) {
		try {
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(filename)),8192);
			return inputStream.readAllBytes();
		} catch (Exception e) {
			log.e(e, IO.class.getName(), "read");
			return null;
		}
	}

	/**
	 * Writes on file
	 *
	 * @param filename the name of the dest. file
	 * @param content  the content to write in String
	 * @param append   weather to append to existing value or not
	 */
	public static void write(String filename, String content, boolean append) {

		try {
			write(filename, content.getBytes(), append);
		} catch (Exception e) {
			log.e(e, IO.class.getName(), "write");
		}
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