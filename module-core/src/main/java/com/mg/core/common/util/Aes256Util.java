package com.mg.core.common.util;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES256 CBC 모드를 사용하여 암호화 및 복호화를 수행하는 유틸리티 클래스입니다.
 */
@Component
public class Aes256Util {
    private static String privateKey_256;

    /**
     * 주어진 평문을 AES256 CBC 모드로 암호화합니다.
     *
     * @param plainText 암호화할 평문 문자열
     * @return 암호화된 문자열 (16진수 형식)
     * @throws Exception 암호화 과정에서 발생하는 예외
     */
    public static String aesCBCEncode(String plainText) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        byte[] encrpytionByte = c.doFinal(plainText.getBytes("UTF-8"));

        return Hex.encodeHexString(encrpytionByte);
    }

    /**
     * 암호화된 문자열을 평문으로 복호화합니다.
     *
     * @param encodeText 암호화된 문자열 (16진수 형식)
     * @return 복호화된 평문 문자열
     * @throws Exception 복호화 과정에서 발생하는 예외
     */
    public static String aesCBCDecode(String encodeText) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(privateKey_256.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey_256.substring(0, 16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.DECRYPT_MODE, secretKey, IV);

        byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());

        return new String(c.doFinal(decodeByte), "UTF-8");
    }

    /**
     * 시스템 속성에서 제공하는 AES256용 개인 키를 설정합니다.
     *
     * @param value 설정할 키 값
     */
    // @Value("#{sysProperties['aes256.private.key']}")
    // public void setKey(String value) {
    // privateKey_256 = value;
    // }
}
