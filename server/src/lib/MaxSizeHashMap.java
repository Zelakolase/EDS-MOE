package lib;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * HashMap with Maximum size. When it reaches the maximum size, it deletes the eldest entry.
 * @author Morad A.
 * @param <K> Key
 * @param <V> Value
 */
public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
	private final int maxSize;

	public MaxSizeHashMap(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}
}
