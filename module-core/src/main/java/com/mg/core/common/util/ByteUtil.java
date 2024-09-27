package com.mg.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 원시 데이터 타입을 바이트 배열로 변환하고, 그 반대의 변환도 수행하는 유틸리티 메서드들을 제공
 */
public class ByteUtil {

	private ByteUtil() {
	}

	/**
	 * short 값을 바이트 배열로 변환합니다.
	 * 바이트 배열은 빅 엔디언 방식으로 저장됩니다.
	 *
	 * @param s 변환할 short 값
	 * @return 변환된 바이트 배열
	 */
	public static final byte[] short2byte(short s) {
		byte dest[] = new byte[2];
		dest[1] = (byte) (s & 0xff);
		dest[0] = (byte) (s >>> 8 & 0xff);
		return dest;
	}

	/**
	 * 정수(int) 값을 바이트 배열로 변환합니다.
	 * 바이트 배열은 빅 엔디언 방식으로 저장됩니다.
	 *
	 * @param i 변환할 정수 값
	 * @return 변환된 바이트 배열
	 */
	public static final byte[] int2byte(int i) {
		byte dest[] = new byte[4];
		dest[3] = (byte) (i & 0xff);
		dest[2] = (byte) (i >>> 8 & 0xff);
		dest[1] = (byte) (i >>> 16 & 0xff);
		dest[0] = (byte) (i >>> 24 & 0xff);
		return dest;
	}

	/**
	 * 롱(long) 값을 바이트 배열로 변환합니다.
	 * 바이트 배열은 빅 엔디언 방식으로 저장됩니다.
	 *
	 * @param l 변환할 롱 값
	 * @return 변환된 바이트 배열
	 */
	public static final byte[] long2byte(long l) {
		byte dest[] = new byte[8];
		dest[7] = (byte) (l & 255L);
		dest[6] = (byte) (l >>> 8 & 255L);
		dest[5] = (byte) (l >>> 16 & 255L);
		dest[4] = (byte) (l >>> 24 & 255L);
		dest[3] = (byte) (l >>> 32 & 255L);
		dest[2] = (byte) (l >>> 40 & 255L);
		dest[1] = (byte) (l >>> 48 & 255L);
		dest[0] = (byte) (l >>> 56 & 255L);
		return dest;
	}

	/**
	 * 실수(float) 값을 바이트 배열로 변환합니다.
	 * 바이트 배열은 빅 엔디언 방식으로 저장됩니다.
	 *
	 * @param f 변환할 실수 값
	 * @return 변환된 바이트 배열
	 */
	public static final byte[] float2byte(float f) {
		byte dest[] = new byte[4];
		return setfloat(dest, 0, f);
	}

	/**
	 * double 값을 바이트 배열로 변환합니다.
	 *
	 * @param d 변환할 double 값
	 * @return 변환된 바이트 배열
	 */
	public static final byte[] double2byte(double d) {
		byte dest[] = new byte[8];
		return setdouble(dest, 0, d);
	}

	/**
	 * 바이트 배열에서 특정 위치의 바이트 값을 가져옵니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset 가져올 바이트의 오프셋
	 * @return 해당 위치의 바이트 값
	 */
	public static final byte getbyte(byte src[], int offset) {
		return src[offset];
	}

	/**
	 * 바이트 배열에서 특정 위치부터 지정된 길이만큼의 바이트 배열을 추출합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset 추출을 시작할 오프셋
	 * @param length 추출할 길이
	 * @return 추출된 바이트 배열
	 */
	public static final byte[] getbytes(byte src[], int offset, int length) {
		byte dest[] = new byte[length];
		System.arraycopy(src, offset, dest, 0, length);
		return dest;
	}

	/**
	 * 바이트 배열에서 특정 위치에 있는 short 값을 읽어 반환합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset short 값을 읽기 시작할 오프셋
	 * @return 읽은 short 값
	 */
	public static final short getshort(byte src[], int offset) {
		return (short) ((src[offset] & 0xff) << 8 | src[offset + 1] & 0xff);
	}

	/**
	 * 바이트 배열에서 특정 위치에 있는 int 값을 읽어 반환합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset int 값을 읽기 시작할 오프셋
	 * @return 읽은 int 값
	 */
	public static final int getint(byte src[], int offset) {
		return (src[offset] & 0xff) << 24 | (src[offset + 1] & 0xff) << 16 | (src[offset + 2] & 0xff) << 8
				| src[offset + 3] & 0xff;
	}

	/**
	 * 바이트 배열에서 특정 위치에 있는 long 값을 읽어 반환합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset long 값을 읽기 시작할 오프셋
	 * @return 읽은 long 값
	 */
	public static final long getlong(byte src[], int offset) {
		return (long) getint(src, offset) << 32 | (long) getint(src, offset + 4) & 0xffffffffL;
	}

	/**
	 * 바이트 배열에서 특정 위치에 있는 float 값을 읽어 반환합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset float 값을 읽기 시작할 오프셋
	 * @return 읽은 float 값
	 */
	public static final float getfloat(byte src[], int offset) {
		return Float.intBitsToFloat(getint(src, offset));
	}

	/**
	 * 바이트 배열에서 특정 위치에 있는 double 값을 읽어 반환합니다.
	 *
	 * @param src    소스 바이트 배열
	 * @param offset double 값을 읽기 시작할 오프셋
	 * @return 읽은 double 값
	 */
	public static final double getdouble(byte src[], int offset) {
		return Double.longBitsToDouble(getlong(src, offset));
	}

	/**
	 * 바이트 배열의 특정 위치에 바이트 값을 설정합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 바이트 값을 설정할 오프셋
	 * @param b      설정할 바이트 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setbyte(byte dest[], int offset, byte b) {
		dest[offset] = b;
		return dest;
	}

	/**
	 * 한 바이트 배열에서 다른 바이트 배열로 데이터를 복사합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 데이터를 복사할 시작 위치
	 * @param src    복사할 소스 바이트 배열
	 * @return 수정된 대상 바이트 배열
	 */
	public static final byte[] setbytes(byte dest[], int offset, byte src[]) {
		System.arraycopy(src, 0, dest, offset, src.length);
		return dest;
	}

	/**
	 * 한 바이트 배열에서 다른 바이트 배열로 지정된 길이만큼의 데이터를 복사합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 데이터를 복사할 시작 위치
	 * @param src    복사할 소스 바이트 배열
	 * @param len    복사할 데이터의 길이
	 * @return 수정된 대상 바이트 배열
	 */
	public static final byte[] setbytes(byte dest[], int offset, byte src[], int len) {
		System.arraycopy(src, 0, dest, offset, len);
		return dest;
	}

	/**
	 * 바이트 배열의 특정 위치에 숏(short) 값을 두 바이트로 나누어 저장합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 시작 오프셋
	 * @param s      저장할 숏(short) 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setshort(byte dest[], int offset, short s) {
		dest[offset] = (byte) (s >>> 8 & 0xff);
		dest[offset + 1] = (byte) (s & 0xff);
		return dest;
	}

	/**
	 * 바이트 배열의 특정 위치에 정수(int) 값을 네 바이트로 나누어 저장합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 시작 오프셋
	 * @param i      저장할 정수 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setint(byte dest[], int offset, int i) {
		dest[offset] = (byte) (i >>> 24 & 0xff);
		dest[offset + 1] = (byte) (i >>> 16 & 0xff);
		dest[offset + 2] = (byte) (i >>> 8 & 0xff);
		dest[offset + 3] = (byte) (i & 0xff);
		return dest;
	}

	/**
	 * 바이트 배열의 특정 위치에 롱(long) 값을 두 부분으로 나누어 저장합니다.
	 * 처음 4바이트에는 상위 32비트를, 다음 4바이트에는 하위 32비트를 저장합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 시작 오프셋
	 * @param l      저장할 롱(long) 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setlong(byte dest[], int offset, long l) {
		setint(dest, offset, (int) (l >>> 32));
		setint(dest, offset + 4, (int) (l & 0xffffffffL));
		return dest;
	}

	/**
	 * 바이트 배열의 특정 위치에 실수(float) 값을 정수(int) 형태로 변환하여 저장합니다.
	 * float 값을 int로 변환한 후, 그 값을 네 바이트로 나누어 저장합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 시작 오프셋
	 * @param f      저장할 실수(float) 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setfloat(byte dest[], int offset, float f) {
		return setint(dest, offset, Float.floatToIntBits(f));
	}

	/**
	 * 주어진 바이트 배열의 특정 위치에 double 값을 long 타입으로 변환하여 저장합니다.
	 *
	 * @param dest   대상 바이트 배열
	 * @param offset 시작 오프셋
	 * @param d      저장할 double 값
	 * @return 수정된 바이트 배열
	 */
	public static final byte[] setdouble(byte dest[], int offset, double d) {
		return setlong(dest, offset, Double.doubleToLongBits(d));
	}

	/**
	 * 바이트 배열과 문자열이 같은지 비교합니다.
	 *
	 * @param b 바이트 배열
	 * @param s 비교할 문자열
	 * @return 같으면 true, 다르면 false를 반환
	 */
	public static final boolean isEquals(byte b[], String s) {
		if (b == null || s == null)
			return false;
		int slen = s.length();
		if (b.length != slen)
			return false;
		for (int i = slen; i-- > 0;)
			if (b[i] != s.charAt(i))
				return false;

		return true;
	}

	/**
	 * 두 바이트 배열이 같은지 비교합니다.
	 *
	 * @param a 첫 번째 바이트 배열
	 * @param b 두 번째 바이트 배열
	 * @return 같으면 true, 다르면 false를 반환
	 */
	public static final boolean isEquals(byte a[], byte b[]) {
		if (a == null || b == null)
			return false;
		if (a.length != b.length)
			return false;
		for (int i = a.length; i-- > 0;)
			if (a[i] != b[i])
				return false;

		return true;
	}

	/**
	 * 16진수 문자열을 바이트 배열로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 변환된 바이트 배열
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	/**
	 * 바이트 배열을 16진수 문자열로 변환합니다.
	 *
	 * @param ba 변환할 바이트 배열
	 * @return 변환된 16진수 문자열
	 */
	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	/**
	 * 문자열을 16진수 문자열로 변환합니다. 각 문자는 공백으로 구분됩니다.
	 *
	 * @param s 변환할 문자열
	 * @return 16진수 형태의 문자열
	 */
	public static String stringToHex(String s) {
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			result += String.format("%02X ", (int) s.charAt(i));
		}

		return result;
	}

	/**
	 * 문자열을 16진수 문자열로 변환하며, 각 16진수 값 앞에 "0x"를 붙입니다. 각 값은 공백으로 구분됩니다.
	 *
	 * @param s 변환할 문자열
	 * @return "0x" 접두사가 붙은 16진수 형태의 문자열
	 */
	public static String stringToHex0x(String s) {
		String result = "";

		for (int i = 0; i < s.length(); i++) {
			result += String.format("0x%02X ", (int) s.charAt(i));
		}

		return result;
	}

	/**
	 * 입력 스트림을 바이트 배열로 변환합니다.
	 *
	 * @param is 변환할 입력 스트림
	 * @return 변환된 바이트 배열
	 */
	public static byte[] convertInputStreamToByteArray(InputStream is) {
		int len;
		int size = 1024;
		byte[] buf = null;

		try {
			if (is instanceof ByteArrayInputStream) {
				size = is.available();
				buf = new byte[size];
				len = is.read(buf, 0, size);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				buf = new byte[size];
				while ((len = is.read(buf, 0, size)) != -1)
					bos.write(buf, 0, len);
				buf = bos.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf;
	}

	/**
	 * 일반 텍스트 문자열을 16진수 문자열로 변환합니다.
	 *
	 * @param str 변환할 문자열
	 * @return 16진수로 변환된 문자열
	 */
	public static String convertStringToHex(String str) {

		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {

			String s = Integer.toHexString((int) chars[i]);
			if (s.length() < 2)
				s = "0" + s;
			hex.append(s);
		}

		return hex.toString();
	}

	/**
	 * 정수 값을 16진수 문자열로 변환합니다.
	 *
	 * @param str 변환할 정수 값
	 * @return 16진수 형태의 문자열 ("0x" 접두어 포함)
	 */
	public static String intToHex(int str) {
		return String.format("0x%02X", str);
	}

	/**
	 * 16진수 문자열을 일반 텍스트로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 일반 텍스트 문자열
	 */
	public static String convertHexToString(String hex) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
		}
		return sb.toString();
	}

	/**
	 * 16진수 문자열을 10진수 정수 배열로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 10진수 정수 배열
	 */
	public static int[] convertHexToDecimalArray(String hex) {
		int[] arrint = new int[(hex.length() / 2)];
		int k = 0;
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			arrint[k++] = Integer.parseInt(output, 16);
		}

		return arrint;
	}

	/**
	 * 16진수 문자열을 문자열 배열로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 문자열 배열로 변환된 결과
	 */
	public static String[] convertHexToStringArray(String hex) {
		String[] arrstr = new String[(hex.length() / 2)];
		int k = 0;
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			arrstr[k++] = String.valueOf((char) decimal);
		}
		return arrstr;
	}

	/**
	 * 16진수 문자열을 문자 배열로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 문자 배열로 변환된 결과
	 */
	public static char[] convertHexToCharArray(String hex) {

		char[] arrstr = new char[(hex.length() / 2)];

		int k = 0;
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			arrstr[k++] = (char) decimal;
		}

		return arrstr;
	}

	/**
	 * 16진수 문자열을 바이트 배열로 변환합니다.
	 *
	 * @param hex 변환할 16진수 문자열
	 * @return 바이트 배열로 변환된 결과
	 */
	public static byte[] convertHexToByteArray(String hex) {
		byte[] arrbyte = new byte[(hex.length() / 2)];
		int k = 0;
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			arrbyte[k++] = (byte) ((char) decimal);
		}

		return arrbyte;
	}

	/**
	 * 주어진 문자열에서 ASCII 바이트 배열을 생성합니다.
	 *
	 * @param buf 변환할 문자열
	 * @return ASCII 바이트 배열
	 */
	public static byte[] asciiGetBytes(String buf) {
		int size = buf.length();
		int i;

		byte[] bytebuf = new byte[size];
		for (i = 0; i < size; i++) {
			bytebuf[i] = (byte) buf.charAt(i);
		}
		return bytebuf;
	}

	/**
	 * 바이트 배열을 정수로 변환합니다. (Little Endian 방식 사용)
	 *
	 * @param bytes 변환할 바이트 배열
	 * @return 변환된 정수 값
	 */
	public static int byteArrayToInt(byte[] bytes) {
		final int size = Integer.SIZE / 8;
		ByteBuffer buff = ByteBuffer.allocate(size);
		final byte[] newBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i + bytes.length < size) {
				newBytes[i] = (byte) 0x00;
			} else {
				newBytes[i] = bytes[i + bytes.length - size];
			}
		}
		buff = ByteBuffer.wrap(newBytes);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		return buff.getInt();
	}

	/**
	 * 바이트 배열을 정수로 변환합니다. 바이트 순서(ByteOrder)를 지정할 수 있습니다.
	 *
	 * @param bytes 변환할 바이트 배열
	 * @param order 바이트 배열의 순서 (Big Endian 또는 Little Endian)
	 * @return 변환된 정수 값
	 */
	public static int byteArrayToInt(byte[] bytes, ByteOrder order) {
		final int size = Integer.SIZE / 8;
		ByteBuffer buff = ByteBuffer.allocate(size);
		final byte[] newBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i + bytes.length < size) {
				newBytes[i] = (byte) 0x00;
			} else {
				newBytes[i] = bytes[i + bytes.length - size];
			}
		}
		buff = ByteBuffer.wrap(newBytes);
		buff.order(order);
		return buff.getInt();
	}
}
