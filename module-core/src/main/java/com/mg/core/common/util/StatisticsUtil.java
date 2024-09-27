package com.mg.core.common.util;

import java.math.BigDecimal;

/**
 * 통계 관련 계산을 수행하는 유틸리티 클래스입니다.
 * 이 클래스는 반올림, 합계, 평균 등의 기본적인 수치 계산을 제공합니다.
 */
public class StatisticsUtil {

	/**
	 * 정수 두 개의 나눗셈 결과를 반올림합니다.
	 *
	 * @param dividend 피제수
	 * @param divisor  제수
	 * @param scale    소수점 이하 자릿수
	 * @return 반올림된 나눗셈 결과
	 */
	public static double divideRoundHalfUp(int dividend, int divisor, int scale) {
		BigDecimal bd_val = new BigDecimal(String.valueOf(dividend));
		BigDecimal bd_divisor = new BigDecimal(String.valueOf(divisor));
		BigDecimal result = bd_val.divide(bd_divisor, scale, BigDecimal.ROUND_HALF_UP);
		return result.doubleValue();
	}

	/**
	 * 주어진 double 값을 지정된 자릿수로 반올림합니다.
	 *
	 * @param dValue 반올림할 값
	 * @param sacle  소수점 이하 자릿수
	 * @return 반올림된 값
	 */
	public static double roundHalfUp(double dValue, int sacle) {
		return new BigDecimal(dValue).setScale(sacle, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 여러 개의 정수의 총합을 계산합니다.
	 *
	 * @param args 합산할 정수들
	 * @return 정수들의 총합
	 */
	public static int getTotalSum(int... args) {
		int r = 0;
		for (Integer i : args) {
			r += i;
		}

		return r;
	}

	/**
	 * 여러 개의 double 값의 총합을 계산합니다.
	 *
	 * @param args 합산할 double 값들
	 * @return double 값들의 총합
	 */
	public static double getDoubleTotalSum(double... args) {
		double r = 0;
		for (double i : args) {
			r += i;
		}

		return r;
	}

	/**
	 * 여러 개의 double 값의 평균을 계산합니다.
	 *
	 * @param args 평균을 계산할 double 값들
	 * @return 계산된 평균 값
	 */
	public static double getTotalAvg(double... args) {
		int divisor = 0;
		double dividend = 0;

		for (Double d : args) {
			divisor++;
			dividend += d;
		}

		return new BigDecimal(dividend).divide(new BigDecimal(String.valueOf(divisor))).doubleValue();
	}

}
