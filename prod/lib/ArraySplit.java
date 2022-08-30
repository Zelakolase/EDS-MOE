package lib;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * See {@link #split(byte[], byte[])}
 * @author morad
 */
public class ArraySplit {
	/**
	 * Splits an array according to a byte sequence
	 * @param array The original array
	 * @param delimiter The byte sequence
	 * @return A list with the resulting arrays
	 */
	public static List<byte[]> split(byte[] array, byte[] delimiter) {
		List<byte[]> byteArrays = new LinkedList<>();
		int begin = 0;

		outer: for (int i = 0; i < array.length - delimiter.length + 1; i++) {
			for (int j = 0; j < delimiter.length; j++) {
				if (array[i + j] != delimiter[j]) {
					continue outer;
				}
			}
			byteArrays.add(Arrays.copyOfRange(array, begin, i));
			begin = i + delimiter.length;
		}
		byteArrays.add(Arrays.copyOfRange(array, begin, array.length));
		return byteArrays;
	}
}
