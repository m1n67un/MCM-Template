package com.mg.core.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 해시 관련 유틸리티 기능을 제공하는 클래스입니다.
 * 다양한 해시 알고리즘을 사용하여 문자열의 해시 값을 생성할 수 있습니다.
 */
public class HashUtil {

	/**
	 * 주어진 문자열에 대해 지정된 해시 알고리즘을 사용하여 해시 값을 계산합니다.
	 *
	 * @param type 해시 알고리즘 타입 (예: "MD5", "SHA-256")
	 * @param src  해시를 계산할 원본 문자열
	 * @return 계산된 해시 값의 16진수 문자열
	 */
	public static String hashData(String type, String src) {
		String hashData = "";
		try {
			MessageDigest md = MessageDigest.getInstance(type);
			md.update(src.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			hashData = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashData;
	}
}
