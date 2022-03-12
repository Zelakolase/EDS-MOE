import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PostRequestMerge {
	public static byte[] merge(List<byte[]> aLm) throws IOException {
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
		return whole.toByteArray();
	}
}
