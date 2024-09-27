package com.mg.core.common.util;

/**
 * 문자열에서 원시 데이터 타입을 파싱하는 유틸리티 클래스입니다. {@code Integer.parseInt} 등의 메서드에 비해 객체 생성이
 * 없어
 * 성능 부하가 적습니다.
 */
public final class ParseUtil {
    /**
     * 제공된 문자열에서 시작 인덱스부터 끝 인덱스까지의 부분을 int 타입으로 변환합니다.
     * {@code Integer.parseInt}와 같은 기능이나, 부가적인 객체 생성 없이 처리하여 성능 부담을 줄입니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 정수값
     * @throws NumberFormatException 숫자 형식이 아닐 경우 예외 발생
     */
    public static int parseInt(String s, final int start, final int end)
            throws NumberFormatException {
        return parseSignedInt(s, start, end);
    }

    /**
     * 제공된 문자열에서 시작 인덱스부터 끝 인덱스까지의 부분을 부호 있는 정수(int)로 변환합니다.
     * 숫자 앞에 부호가 있을 경우 이를 고려하여 파싱합니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 부호 있는 정수값
     * @throws NumberFormatException 숫자가 아닌 문자를 만날 경우 또는 부적절한 위치에 부호가 있는 경우 예외 발생
     */
    public static int parseSignedInt(CharSequence s, final int start, final int end)
            throws NumberFormatException {
        if (s.charAt(start) == '-') {
            // 음수 처리
            return -parseUnsignedInt(s, start + 1, end);
        } else {
            return parseUnsignedInt(s, start, end);
        }
    }

    /**
     * 제공된 문자열에서 부호 없는 정수를 파싱합니다. {@code Integer.parseInt(s.substring(start, end))}와
     * 유사하지만
     * 객체 생성이나 가비지 컬렉션 부담이 없어 훨씬 효율적입니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 정수
     * @throws NumberFormatException 숫자가 아닌 문자를 만났을 경우 예외를 발생시킵니다.
     */
    public static int parseUnsignedInt(CharSequence s, final int start, final int end)
            throws NumberFormatException {
        int ret = 0;
        for (int i = start; i < end; i++) {
            final char c = s.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException(
                        "Not a valid base-10 digit: " + c + " in " + s.subSequence(start, end));
            }
            final int val = c - '0';
            ret = ret * 10 + val;
        }
        return ret;
    }

    /**
     * 제공된 문자열에서 부호 있는 긴 정수(long) 값을 파싱합니다.
     * {@code Long.parseLong(s.substring(start, end))}와 유사하지만
     * 객체 생성이나 가비지 컬렉션 부담이 없어 효율적입니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 긴 정수
     * @throws NumberFormatException 부적절한 문자를 만났을 경우 예외를 발생시킵니다.
     */
    public static long parseSignedLong(CharSequence s, final int start, final int end)
            throws NumberFormatException {
        if (s.charAt(start) == '-') {
            // negative!
            return -parseUnsignedLong(s, start + 1, end);
        } else {
            return parseUnsignedLong(s, start, end);
        }
    }

    /**
     * 제공된 문자열에서 부호 없는 긴 정수(long) 값을 파싱합니다.
     * {@code Long.parseLong(s.substring(start, end))}와 유사하지만
     * 객체 생성이나 가비지 컬렉션 부담이 없어 효율적입니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 긴 정수
     * @throws NumberFormatException 부적절한 문자를 만났을 경우 예외를 발생시킵니다.
     */
    public static long parseUnsignedLong(CharSequence s, final int start, final int end)
            throws NumberFormatException {
        long ret = 0;
        for (int i = start; i < end; i++) {
            final char c = s.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException(
                        "Not a valid base-10 digit: " + c + " in " + s.subSequence(start, end));
            }
            final int val = c - '0';
            ret = ret * 10 + val;
        }
        return ret;
    }

    private static final char infinityChars[] = { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };
    private static final char naNChars[] = { 'N', 'a', 'N' };

    /**
     * 제공된 문자열에서 부동 소수점 숫자를 파싱합니다. 이 메서드는 `Float.parseFloat(s.substring(start,
     * end))` 보다 특수화되어 있으며
     * 객체 생성이나 후속 가비지 컬렉션 부담 없이 작동합니다. "NaN", "Infinity", "-Infinity" 및
     * HexFloatingPointLiterals 문자열은 파싱하지 않습니다.
     *
     * @param s     파싱할 문자열
     * @param start 시작 인덱스
     * @param end   끝 인덱스
     * @return 파싱된 부동 소수점 숫자
     * @throws NumberFormatException 입력이 부동 소수점 숫자에 해당하지 않을 경우 예외를 발생시킵니다.
     */
    public static float parseFloat(String s, final int start, final int end)
            throws NumberFormatException {
        int i = start;
        final int sign;
        if (s.charAt(i) == '-') {
            // 음수 처리
            sign = -1;
            i++;
        } else {
            sign = 1;
        }

        // ret을 float으로 유지하면 문제가 발생할 수 있으므로 더 정확한 double을 사용합니다.
        double ret = 0;
        boolean decimalFound = false;
        double mult = 1;
        boolean isScientificNotation = false;

        for (; i < end; i++) {
            final char c = s.charAt(i);

            if (c == 'E' || c == 'e') {
                isScientificNotation = true;
                break;
            }

            if (c == 'N' || c == 'I') {
                final boolean isNanStart = (c == 'N');
                final char[] charsToMatch = c == 'N' ? naNChars : infinityChars;
                int j = 0;
                while (i < end && j < charsToMatch.length) {
                    if (s.charAt(i) == charsToMatch[j]) {
                        i++;
                        j++;
                    } else {
                        throw new NumberFormatException(
                                "지원하지 않는 형식, 입력 문자열을 파싱할 수 없습니다.");
                    }
                }

                if (i == end
                        && j == charsToMatch.length) { // NaN이나 Infinity를 의미합니다.
                    if (isNanStart) {
                        return Float.NaN;
                    } else if (sign == 1) {
                        return Float.POSITIVE_INFINITY;
                    } else {
                        return Float.NEGATIVE_INFINITY;
                    }
                } else {
                    throw new NumberFormatException(
                            "지원하지 않는 형식, 입력 문자열을 파싱할 수 없습니다.");
                }
            }

            if (decimalFound) {
                mult *= 10;
            }

            if (c >= '0' && c <= '9') {
                final int val = c - '0';
                ret = ret * 10 + val;
            } else if (c == '.') {
                decimalFound = true;
            } else {
                throw new NumberFormatException(
                        "Not a valid base-10 digit: " + c + " in " + s.substring(start, end));
            }
        }

        ret /= mult;

        if (isScientificNotation) {
            int exponent = parseSignedInt(s, ++i, end);
            ret *= Math.pow(10, exponent);
        }

        return (float) (ret * sign);
    }

    /**
     * 주어진 UID 문자열에서 로그 리포지토리용 타임스탬프 부분을 파싱합니다.
     *
     * @param s     분석할 문자열
     * @param start 시작 인덱스
     * @param end   종료 인덱스
     * @return 파싱된 타임스탬프 값
     * @throws IllegalArgumentException 유효하지 않은 UID가 주어진 경우
     */
    public static long parseTimestampFromUIDString(String s, final int start, final int end) {
        long ret = 0;
        for (int i = start; i < end && i < start + 9; i++) {
            ret <<= 5;
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ret |= c - '0';
            } else if (c >= 'a' && c <= 'v') {
                ret |= c - 'a' + 10;
            } else if (c >= 'A' && c <= 'V') {
                ret |= c - 'A' + 10;
            } else {
                throw new IllegalArgumentException(
                        s.substring(start, end) + " is not a valid UID!");
            }
        }
        return ret;
    }

    /**
     * 주어진 StringBuilder 객체에 있는 URL 인코딩된 문자열을 디코딩하여 원래 위치에 덮어씁니다.
     *
     * @param input 디코딩할 입력 StringBuilder 객체
     */
    public static void urlDecodeInplace(StringBuilder input) {
        urlDecodeInto(input, 0, input.length(), input, true);
    }

    /**
     * CharSequence로부터 URL 인코딩된 데이터를 디코딩하여 주어진 StringBuilder 객체에 결과를 저장합니다.
     *
     * @param input  입력 CharSequence
     * @param start  시작 인덱스
     * @param end    종료 인덱스
     * @param result 결과를 저장할 StringBuilder 객체
     */
    public static void urlDecodeInto(CharSequence input, int start, int end, StringBuilder result) {
        urlDecodeInto(input, start, end, result, false);
    }

    /**
     * 주어진 입력에서 URL 인코딩을 디코딩하여 결과를 주어진 StringBuilder 객체에 저장합니다. 현장에서 변경이 가능한 경우 입력을
     * 직접 수정합니다.
     *
     * @param input   입력 CharSequence
     * @param start   시작 인덱스
     * @param end     종료 인덱스
     * @param result  결과를 저장할 StringBuilder 객체
     * @param inplace 현장에서 변경 가능 여부
     */
    private static void urlDecodeInto(
            CharSequence input, int start, int end, StringBuilder result, boolean inplace) {
        int writeHead = start;
        for (int i = start; i < end; i++) {
            char c = input.charAt(i);
            if (c == '%' && i + 2 < end) {
                char val = decodeHexPair(input.charAt(i + 1), input.charAt(i + 2));
                if ((val & 0xE0) == 0xC0) {
                    if (i + 5 < end && input.charAt(i + 3) == '%') {
                        char val2 = decodeHexPair(input.charAt(i + 4), input.charAt(i + 5));
                        if (val2 != INVALID_HEX && (val2 & 0xC0) == 0x80) {
                            // zimmerm%C3%A4dchen
                            c = (char) (((val & 0x1F) << 6) | (val2 & 0x3F));
                            i += 5;
                        }
                    }
                } else if ((val & 0xF0) == 0xE0) {
                    if (i + 8 < end && input.charAt(i + 3) == '%' && input.charAt(i + 6) == '%') {
                        char val2 = decodeHexPair(input.charAt(i + 4), input.charAt(i + 5));
                        char val3 = decodeHexPair(input.charAt(i + 7), input.charAt(i + 8));
                        if (val2 != INVALID_HEX
                                && val3 != INVALID_HEX
                                && (val2 & 0xC0) == 0x80
                                && (val3 & 0xC0) == 0x80) {
                            // Technologist+%E2%80%93+Full+Time
                            c = (char) (((val & 0x0F) << 12)
                                    | ((val2 & 0x3F) << 6)
                                    | (val3 & 0x3F));
                            i += 8;
                        }
                    }
                } else if ((val & 0xF8) == 0xF0) {
                    // these are code points > 0XFFFF, they need a surrogate pair to represent them
                    if (i + 11 < end
                            && input.charAt(i + 3) == '%'
                            && input.charAt(i + 6) == '%'
                            && input.charAt(i + 9) == '%') {
                        char val2 = decodeHexPair(input.charAt(i + 4), input.charAt(i + 5));
                        char val3 = decodeHexPair(input.charAt(i + 7), input.charAt(i + 8));
                        char val4 = decodeHexPair(input.charAt(i + 10), input.charAt(i + 11));
                        if (val2 != INVALID_HEX
                                && val3 != INVALID_HEX
                                && val4 != INVALID_HEX
                                && (val2 & 0xC0) == 0x80
                                && (val3 & 0xC0) == 0x80
                                && (val4 & 0xC0) == 0x80) {
                            final int codePoint = (((val & 0x0F) << 15)
                                    | ((val2 & 0x3F) << 12)
                                    | ((val3 & 0x3F) << 6)
                                    | (val4 & 0x3F));
                            if (codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
                                c = (char) codePoint;
                            } else {
                                final int offset = codePoint - Character.MIN_SUPPLEMENTARY_CODE_POINT;
                                final char highChar = (char) ((offset >>> 10) + Character.MIN_HIGH_SURROGATE);
                                final char lowChar = (char) ((offset & 0x3ff) + Character.MIN_LOW_SURROGATE);
                                if (!inplace) {
                                    result.append(highChar);
                                } else {
                                    result.setCharAt(writeHead++, highChar);
                                }
                                c = lowChar; // let normal machinery take over here
                            }
                            i += 11;
                        }
                    }
                } else if (val != INVALID_HEX) {
                    c = val;
                    i += 2;
                }
            } else if (c == '+') {
                c = ' ';
            }

            if (!inplace) {
                result.append(c);
            } else {
                result.setCharAt(writeHead++, c);
            }
        }
        if (inplace) {
            result.delete(writeHead, end);
        }
    }

    public static final char INVALID_HEX = (char) 256; // 유효하지 않은 HEX 값을 나타내는 상수

    /**
     * 주어진 두 문자를 분석하여 해당하는 HEX 값을 디코딩합니다.
     * 예: decodeHexPair('3','A')는 ':'을 반환합니다.
     *
     * @param c1 첫 번째 HEX 문자
     * @param c2 두 번째 HEX 문자
     * @return 디코딩된 문자
     */
    public static char decodeHexPair(char c1, char c2) {
        char ret = 0;
        if (c1 >= '0' && c1 <= '9') {
            ret |= c1 - '0';
        } else if (c1 >= 'a' && c1 <= 'f') {
            ret |= c1 - 'a' + 10;
        } else if (c1 >= 'A' && c1 <= 'F') {
            ret |= c1 - 'A' + 10;
        } else {
            return INVALID_HEX;
        }
        ret <<= 4;
        if (c2 >= '0' && c2 <= '9') {
            ret |= c2 - '0';
        } else if (c2 >= 'a' && c2 <= 'f') {
            ret |= c2 - 'a' + 10;
        } else if (c2 >= 'A' && c2 <= 'F') {
            ret |= c2 - 'A' + 10;
        } else {
            return INVALID_HEX;
        }
        return ret;
    }
}
