package com.mg.core.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 관련 다양한 유틸리티 메서드를 제공합니다.
 */
public class StringUtil {

	/**
	 * 전화번호를 특정 구분자를 사용하여 형식화합니다. 원본 문자열의 길이가 충분하지 않은 경우, 기본 문자열을 반환합니다.
	 *
	 * @param strOriginal 원본 전화번호 문자열
	 * @param strDivide   전화번호를 구분할 때 사용할 구분자
	 * @param strDefault  원본 문자열의 길이가 요구사항을 충족하지 않을 때 반환할 기본 문자열
	 * @return 형식화된 전화번호 또는 기본 문자열
	 */
	public static String parsePhoneNumber(String strOriginal, String strDivide,
			String strDefault) {
		String strRet = "";

		if (strOriginal == null || strOriginal.isEmpty() || strOriginal.length() < 10)
			return strDefault;

		strRet += strOriginal.substring(0, 3) + strDivide;

		// 0000000000 일 경우
		if (strOriginal.length() == 10) {
			strRet += strOriginal.substring(3, 6) + strDivide;
			strRet += strOriginal.substring(6, 10);
		} else if (strOriginal.length() == 11) { // 00000000000 일 경우
			strRet += strOriginal.substring(3, 7) + strDivide;
			strRet += strOriginal.substring(7, 11);
		}

		return strRet;
	}

	/**
	 * 주어진 문자열이 유효한 이메일 주소인지 검사합니다.
	 *
	 * @param emailAddress 검사할 이메일 주소
	 * @return 유효한 이메일 주소일 경우 true, 그렇지 않을 경우 false
	 */
	public static boolean isValidEmailAddress(String emailAddress) {
		String emailRegEx;
		Pattern pattern;
		emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
		pattern = Pattern.compile(emailRegEx);
		Matcher matcher = pattern.matcher(emailAddress);

		if (!matcher.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 문자열 양 끝의 이중 따옴표(")를 제거합니다.
	 * 문자열의 길이가 1보다 크고, 첫 번째와 마지막 문자가 따옴표일 때만 제거합니다.
	 *
	 * @param string 수정할 문자열
	 * @return 수정된 문자열
	 */
	public static String removeDoubleQuotes(String string) {
		int length = string.length();
		if ((length > 1) && (string.charAt(0) == '"')
				&& (string.charAt(length - 1) == '"')) {
			return string.substring(1, length - 1);
		}
		return string;
	}

	/**
	 * 문자열을 따옴표로 감싼 형태로 변환합니다.
	 *
	 * @param string 변환할 문자열
	 * @return 따옴표가 추가된 문자열
	 */
	public static String convertToQuotedString(String string) {
		return "\"" + string + "\"";
	}

	/**
	 * Java 문자열을 유니코드 바이트 배열로 변환하여 반환합니다.
	 * 각 문자는 두 바이트로 변환되며, 빅 엔디언 방식을 사용합니다.
	 *
	 * @param str 변환할 문자열
	 * @return 유니코드 바이트 배열
	 */
	public static byte[] getUnicodeBytes(String str) {
		if (str == null)
			return null;

		char charArry[] = str.toCharArray();
		byte byteArry[] = new byte[charArry.length * 2];
		int i = 0;
		for (char ch : charArry) {
			byteArry[i + 1] = (byte) ch;
			byteArry[i] = (byte) (ch >> 8);

			i += 2;
		}

		return byteArry;
	}

	/**
	 * 유니코드 바이트 배열을 Java 문자열로 변환하여 반환합니다.
	 * 배열의 각 연속된 두 바이트는 하나의 문자를 나타내며, 빅 엔디언 방식을 사용합니다.
	 *
	 * @param unicodes 유니코드 바이트 배열
	 * @return 변환된 문자열
	 */
	public static String getStringFromUnicodes(byte unicodes[]) {
		char charArry[] = new char[unicodes.length / 2];
		for (int i = 0; i < charArry.length; i++) {
			charArry[i] = (char) unicodes[i * 2];
			charArry[i] = (char) (charArry[i] << 8 | (0x00ff & unicodes[i * 2 + 1]));
		}

		return new String(charArry);
	}
}
