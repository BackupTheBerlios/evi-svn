package org.schwering.evi.util;

/**
 * Base64 implementation. It can encode and decode Strings.<br>
 * <b>Important:</b> Base64 is <b>not secure</b>. It is even very insecure.
 * Base64 just clouds the encoded string so that it cannot be read by a human 
 * in seconds. However, a computer can decode it seconds!
 * <br>
 * <b>Note:</b> This implementation has <b>problems with non-ASCII 
 * characters</b>.
 * <br>
 * <b>Note:</b> I think there are several different implementations of 
 * Base64 which are not compatible with another. But I'm not sure.<br>
 * <br>
 * This class is based on my Base64 implementation in C available at 
 * http://www.schwering.org/~chs/sw/c_base64.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Base64 {
	private static final char[] table = new String(
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+
			"abcdefghijklmnopqrstuvwxyz"+
			"0123456789+/").toCharArray();
	
	/**
	 * Encodes a string.
	 * @param str The string.
	 * @return The encoded string.
	 */
	public static String encode(String str) {
		char[] src = str.toCharArray();
		int len = src.length;
		byte[] g = new byte[3];
		
		StringBuffer sb = new StringBuffer(len * 4 / 3 + 4);
		for (int i = 0, j = 0; i < len; i += 3, j += 4) {
			g[0] = (byte)src[i];
			g[1] = (i+1 < len) ? (byte)src[i+1] : 0;
			g[2] = (i+2 < len) ? (byte)src[i+2] : 0;
			sb.append(table[(g[0] >> 2) & 63]);
			sb.append(table[(g[0] << 4 | g[1] >> 4) & 63]);
			sb.append(table[(g[1] << 2 | g[2] >> 6) & 63]);
			sb.append(table[g[2] & 63]);
		}
		return sb.toString();
	}
	
	/**
	 * Decodes a string.
	 * @param str The string.
	 * @return The decoded string.
	 */
	public static String decode(String str) {
		char[] src = str.toCharArray();
		int len = src.length;
		byte[] g = new byte[4];
		
		StringBuffer sb = new StringBuffer(len * 3 / 4 + 5);
		for (int i = 0, j = 0; i < len; i += 4, j += 3) {
			g[0] = getTableIndex(src[i]);
			g[1] = (i+1 < len) ? getTableIndex(src[i+1]) : 0;
			g[2] = (i+2 < len) ? getTableIndex(src[i+2]) : 0;
			g[3] = (i+3 < len) ? getTableIndex(src[i+3]) : 0;
			sb.append((char)((g[0] << 2 | (g[1] >> 4 & 3)) & 127));
			sb.append((char)((g[1] << 4 | (g[2] >> 2 & 15)) & 127));
			sb.append((char)((g[2] << 6 | (g[3] & 63)) & 127));
		}
		return sb.toString();
	}
	
	/**
	 * Returns the index of a given char in the Base64 character table.
	 * @param c The seeked character.
	 * @return Its index as in the Base64 table (as byte).
	 */
	private static byte getTableIndex(char c) {
		int r;
		if (c >= 'A' && c <= 'Z') {
			r = c - 'A';
		} else if (c >= 'a' && c <= 'z') {
			r = c + 26 - 'a';
		} else if (c >= '0' && c <= '9') {
			r = c + 52 - '0';
		} else if (c == '+') {
			r = 62;
		} else if (c == '/') {
			r = 63;
		} else {
			r = 0;
		}
		return (byte)r;
	}
}