package com.xiye.sclibrary.utils;

public class TypeUtil {
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	// public static byte[] hexStringToBytes(String hexString) {
	// if (hexString == null || hexString.equals("")) {
	// return null;
	// }
	// hexString = hexString.toUpperCase();
	// int length = hexString.length() / 2;
	// char[] hexChars = hexString.toCharArray();
	// byte[] d = new byte[length];
	// for (int i = 0; i < length; i++) {
	// int pos = i * 2;
	// d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos +
	// 1]));
	//
	// }
	// return d;
	// }
	//
	// private static byte charToByte(char c) {
	// return (byte) "0123456789ABCDEF".indexOf(c);
	// }

	public static byte[] hexStringToByteArray(String s) {
		if (Tools.isStringEmpty(s)) {
			return null;
		}
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	// byte 与 int 的相互转换
	public static byte intToByte(int x) {
		return (byte) x;
	}

	public static int byteToInt(byte b) {// to 10进制
		// Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}

	public static int byteToIntHex(byte b) {
		int hex = Integer.parseInt(Integer.toHexString(byteToInt(b)), 16);
		return hex;
	}

	/**
	 * 字符串转换成十六进制字符串
	 */

	public static String str2HexStr(String str) {

		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 
	 * 十六进制转换字符串
	 */

	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}
}
