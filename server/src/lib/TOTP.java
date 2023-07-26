package lib;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Time-based One Time Password Generation and Validation
 * @author Morad A.
 */
public final class TOTP {
	public static class Secret {
		public static class Base32 {
		private static final String base32Chars =
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
		private static final int[] base32Lookup =
		{ 0xFF,0xFF,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,
		  0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF,
		  0xFF,0x00,0x01,0x02,0x03,0x04,0x05,0x06,
		  0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,
		  0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x16,
		  0x17,0x18,0x19,0xFF,0xFF,0xFF,0xFF,0xFF,
		  0xFF,0x00,0x01,0x02,0x03,0x04,0x05,0x06,
		  0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,
		  0x0F,0x10,0x11,0x12,0x13,0x14,0x15,0x16,
		  0x17,0x18,0x19,0xFF,0xFF,0xFF,0xFF,0xFF
		};
	
		/**
		 * Encodes byte array to Base32 String.
		 *
		 * @param bytes Bytes to encode.
		 * @return Encoded byte array <code>bytes</code> as a String.
		 *
		 */
		static public String encode(final byte[] bytes) {
			int i = 0, index = 0, digit = 0;
			int currByte, nextByte;
			StringBuffer base32
			   = new StringBuffer((bytes.length + 7) * 8 / 5);
	
			while (i < bytes.length) {
				currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256);
	
				/* Is the current digit going to span a byte boundary? */
				if (index > 3) {
					if ((i + 1) < bytes.length) {
						nextByte = (bytes[i + 1] >= 0)
						   ? bytes[i + 1] : (bytes[i + 1] + 256);
					} else {
						nextByte = 0;
					}
	
					digit = currByte & (0xFF >> index);
					index = (index + 5) % 8;
					digit <<= index;
					digit |= nextByte >> (8 - index);
					i++;
				} else {
					digit = (currByte >> (8 - (index + 5))) & 0x1F;
					index = (index + 5) % 8;
					if (index == 0)
						i++;
				}
				base32.append(base32Chars.charAt(digit));
			}
	
			return base32.toString();
		}
	
		/**
		 * Decodes the given Base32 String to a raw byte array.
		 *
		 * @param base32
		 * @return Decoded <code>base32</code> String as a raw byte array.
		 */
		static public byte[] decode(final String base32) {
			int i, index, lookup, offset, digit;
			byte[] bytes = new byte[base32.length() * 5 / 8];
	
			for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
				lookup = base32.charAt(i) - '0';
	
				/* Skip chars outside the lookup table */
				if (lookup < 0 || lookup >= base32Lookup.length) {
					continue;
				}
	
				digit = base32Lookup[lookup];
	
				/* If this digit is not in the table, ignore it */
				if (digit == 0xFF) {
					continue;
				}
	
				if (index <= 3) {
					index = (index + 5) % 8;
					if (index == 0) {
						bytes[offset] |= digit;
						offset++;
						if (offset >= bytes.length)
							break;
					} else {
						bytes[offset] |= digit << (8 - index);
					}
				} else {
					index = (index + 5) % 8;
					bytes[offset] |= (digit >>> index);
					offset++;
	
					if (offset >= bytes.length) {
						break;
					}
					bytes[offset] |= digit << (8 - index);
				}
			}
			return bytes;
		}
	}
		private static final Random rand = new Random();
		
		public enum Size {
			DEFAULT(20),
			MEDIUM(32),
			LARGE(64);
			
			private int size;
			
			Size(int size) {
				this.size = size;
			}
			
			public int getSize() {
				return size;
			}
		}
		
		/**
		 * Generates random 20 bytes
		 * 
		 * @return generated secret
		 */
		public static final byte[] generate() {
			return generate(Size.DEFAULT);
		}
		
		/**
		 * Generates random bytes of given size
		 * 
		 * @return generated secret
		 */
		public static final byte[] generate(Size size) {
			byte[] b = new byte[size.getSize()];
			rand.nextBytes(b);
			return Arrays.copyOf(b, size.getSize());
		}
		
		/**
		 * Encodes TOTP Secret to Base32
		 * 
		 * @param secret the secret to use
		 * @return encoded secret
		 * @see Base32
		 */
		public static final String toBase32(byte[] secret) {
			return new String(Base32.encode(secret));
		}
		
		/**
		 * Decodes Base32 TOTP Secret to bytes
		 * 
		 * @param base32 the base32 to use
		 * @return decoded secret
		 * @see Base32
		 */
		public static final byte[] fromBase32(String base32) {
			return Base32.decode(base32);
		}
		
		public static final String toHex(byte[] secret) {
			return String.format("%x", new BigInteger(1, secret));
		}
		
		public static final byte[] fromHex(String hex) {
			// Adding one byte to get the right conversion
			// Values starting with "0" can be converted
			byte[] bArray = new BigInteger("10" + hex,16).toByteArray();
	
			// Copy all the REAL bytes, not the "first"
			byte[] ret = new byte[bArray.length - 1];
			for (int i = 0; i < ret.length; i++)
				ret[i] = bArray[i+1];
			return ret;
		}
		
	}	
	private static final int[] DIGITS_POWER
    // 0  1   2    3     4      5       6        7         8
    = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
	
	/**
	 * Default algorithm (Google Authenticator Compatible)
	 */
	public static final String DEFAULT_ALGORITHM = "HmacSHA1";
	
	/**
	 * Default time interval in seconds (Google Authenticator Compatible)
	 */
	public static final int DEFAULT_INTERVAL = 30;
	
	/**
	 * Default time interval steps to check into past for validity
	 */
	public static final int DEFAULT_STEPS = 1;
	
	/**
	 * Default code length (Google Authenticator Compatible)
	 */
	public static final int DEFAULT_LENGTH = 6;
	
	/**
	 * Default time 0 for the interval
	 */
	public static final int DEFAULT_T0 = 0;
	
	private final String algorithm;
	
	private final int interval;
	
	private final int length;
	
	private final int steps;
	
	private final int t0;
	
	/**
	 * Create default TOTP instance that is Google Authenticator compatible
	 */
	public TOTP() {
		this(DEFAULT_ALGORITHM, DEFAULT_INTERVAL, DEFAULT_LENGTH, DEFAULT_STEPS, DEFAULT_T0);
	}
	
	/**
	 * @param algorithm the algorithm to use; available HmacSHA1, HmacSHA256, HmacSHA512
	 * @param interval the time interval in seconds to use
	 */
	public TOTP(String algorithm, int interval) {
		this(algorithm, interval, DEFAULT_LENGTH, DEFAULT_STEPS, DEFAULT_T0);
	}
	
	/**
	 * Create new TOTP instance with own time interval
	 * 
	 * @param interval the time interval to use
	 */
	public TOTP(int interval) {
		this(DEFAULT_ALGORITHM, interval, DEFAULT_LENGTH, DEFAULT_STEPS, DEFAULT_T0);
	}

	/**
	 * @param interval the time interval in seconds to use
	 * @param length the code length to use; must be between 1 and 8
	 * @param steps the steps in history to validate code
	 */
	public TOTP(int interval, int length, int steps) {
		this(DEFAULT_ALGORITHM, interval, length, steps, DEFAULT_T0);
	}
	
	/**
	 * Create new TOTP instance with own configuration
	 * 
	 * @param algorithm the algorithm to use; available HmacSHA1, HmacSHA256, HmacSHA512
	 * @param interval the time interval in seconds to use
	 * @param length the code length to use; must be between 1 and 8
	 * @param steps the steps in history to validate code
	 * @param t0 the time 0 to be used for interval
	 */
	public TOTP(String algorithm, int interval, int length, int steps, int t0) {
		this.algorithm = algorithm;
		this.interval = Math.abs(interval);
		this.length = Math.abs(length);
		this.steps = Math.abs(steps);
		this.t0 = Math.abs(t0);
		
		if (length > DIGITS_POWER.length || length < 1) {
			throw new IllegalArgumentException("Length must be between 1 and 8");
		}
	}
	
	/**
	 * @return the algorithm being used
	 */
	public String getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * @return the interval being used
	 */
	public int getInterval() {
		return interval;
	}
	
	/**
	 * @return the length being used
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * @return the steps being used
	 */
	public int getSteps() {
		return steps;
	}
	
	/**
	 * @return the time 0 to be used for interval
	 */
	public int getT0() {
		return t0;
	}
	
	/**
	 * Generates TOTP code for current time interval
	 * 
	 * @param secret the secret to use
	 * @return generated code
	 * @see Secret#generate()
	 */
	public final String generate(byte[] secret) {
		return generateOTP(secret, getCurrentTimeInterval());
	}
	
	/**
	 * Generates TOTP code for given time
	 * 
	 * @param secret the secret to use
	 * @param time the time in milliseconds to generate code for
	 * @return generated code
	 * @see Secret#generate()
	 */
	public final String generate(byte[] secret, long time) {
		return generateOTP(secret, getTimeInterval(time));
	}
	
	/**
	 * Validates TOTP code for current time interval
	 * 
	 * @param secret the secret to use
	 * @param code the code to validate
	 * @return true if code is valid
	 * @see Secret#generate()
	 */
	public final boolean validate(byte[] secret, String code) {
		return validate(secret, code, System.currentTimeMillis());
	}
	
	/**
	 * Validates TOTP code for current time interval
	 * 
	 * @param secret the secret to use
	 * @param code the code to validate
	 * @param time the time in milliseconds to validate code against
	 * @return true if code is valid
	 * @see Secret#generate()
	 */
	public final boolean validate(byte[] secret, String code, long time) {
		int steps = getSteps();
		long itvl = getTimeInterval(time);
		
		for (int i = 0; i <= steps; i++) {
			boolean result = validateOTP(secret, itvl - i, code);
			if (result) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Generates TOTP code for give time interval
	 * 
	 * @param secret the secret to use
	 * @param itvl the time interval to use
	 * @return generated code
	 * @see Secret#generate()
	 * @see #getTimeInterval(long)
	 */
	final String generateOTP(byte[] secret, long itvl) {
		byte[] text = ByteBuffer.allocate(8).putLong(itvl).array();
		byte[] hash = getShaHash(secret, text);
		
		int off = hash[hash.length-1] & 0xf;
		int bin = ((hash[off] & 0x7f) << 24) | ((hash[off + 1] & 0xff) << 16) | ((hash[off + 2] & 0xff) << 8) | (hash[off + 3] & 0xff);

		int otp = bin % DIGITS_POWER[getLength()];
		String result = Integer.toString(otp);
		while (result.length() < getLength()) {
			result = "0" + result;
		}
		return result;
	}
	
	final boolean validateOTP(byte[] secret, long itvl, String code) {
		String hash = generateOTP(secret, itvl);
		return hash.equals(code);
	}
	
	private byte[] getShaHash(byte[] key, byte[] text) {
		try {
			Mac mac = Mac.getInstance(getAlgorithm());
			SecretKeySpec spec = new SecretKeySpec(key, "RAW");
			mac.init(spec);
			return mac.doFinal(text);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException(e);
		}
	}
	
	long getTimeInterval(long time) {
		return ((time / 1000) - getT0()) / getInterval();
	}
	
	long getCurrentTimeInterval() {
		return getTimeInterval(System.currentTimeMillis());
	}
	public static final String getQRUrl(String username, String host, String secret) {
		String format = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
		return String.format(format, username, host, secret);
	}
	
}