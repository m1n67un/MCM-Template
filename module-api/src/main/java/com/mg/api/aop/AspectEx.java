package com.mg.api.aop;

import com.mg.api.aop.dto.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * AOP 구성(예시)
 * 참고 - https://adjh54.tistory.com/133
 */
@Aspect
@Component
@Slf4j
public class AspectEx {

    /**
     * execution 표현식의 구조
     *
     * execution(modifier-pattern? return-type-pattern
     * declaring-type-pattern?name-pattern(param-pattern))
     *
     * modifier-pattern: 메서드의 접근 제한자(public, private 등)를 지정합니다. 선택적으로 사용될 수 있습니다.
     * return-type-pattern: 메서드의 반환 타입 패턴을 지정합니다.
     * declaring-type-pattern: 메서드가 선언된 클래스 패턴을 지정합니다. 선택적으로 사용될 수 있습니다.
     * name-pattern: 메서드 이름 패턴을 지정합니다. 와일드카드(*)를 사용할 수 있습니다.
     * param-pattern: 메서드 인자 패턴을 지정합니다. 인자 타입, 개수 등을 지정할 수 있습니다.
     */

    /**
     * Before: 대상 “메서드”가 실행되기 전에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @Before("execution(* com.mg.api.*.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before: " + joinPoint.getSignature().getName());
    }

    /**
     * After : 대상 “메서드”가 실행된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     */
    @After("execution(* com.mg.api.*.controller.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After: " + joinPoint.getSignature().getName());
    }

    /**
     * AfterReturning: 대상 “메서드”가 정상적으로 실행되고 반환된 후에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.mg.api.*.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning: " + joinPoint.getSignature().getName() + " result: " + result);
    }

    /**
     * AfterThrowing: 대상 “메서드에서 예외가 발생”했을 때 Advice를 실행합니다.
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "execution(* com.mg.api.*.controller.*.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("AfterThrowing: " + joinPoint.getSignature().getName() + " exception: " + e.getMessage());
    }

    /**
     * Around : 대상 “메서드” 실행 전, 후 또는 예외 발생 시에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.mg.api.*.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Around before: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("Around after: " + joinPoint.getSignature().getName());
        return result;
    }

    /**
     * 일부 값들은 받을때부터 값을 전환함
     * Before: 대상 “메서드”가 실행되기 전에 Advice를 실행합니다.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.mg.api.*.controller.*.*(..))")
    public Object filterFields(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof TestDto) {
                TestDto dto = (TestDto) arg;

                // 특정 필드의 값을 변경하는 로직 추가
                // 성별의 경우 특정 값으로 넘어온 것은 치환하게 처리
                String gender = dto.getGender();
                String result = "";
                if ("/M/m/Male/male/남/남자/".indexOf(gender) > -1) {
                    result = "m";
                } else if ("/F/f/Female/female/여/여자/".indexOf(gender) > -1) {
                    result = "f";
                } else {
                    result = "none";
                }
                dto.setGender(result);

                // 전화번호의 경우 숫자를 제외하곤 모두 제거
                String phoneNo = dto.getPhoneNo();
                result = phoneNo.replaceAll("[^0-9]", "");
                // 중간 3/4자리를 치환할 경우 사용(하이픈의 경우 구분의 용이성을 위해 일부러 넣은 것)
                // result = result.replaceAll("(\\d{3})(\\d{3})?(\\d{4})", "$1-***-$3");
                // result = result.replaceAll("(\\d{3})(\\d{4})?(\\d{4})", "$1-****-$3");
                dto.setPhoneNo(result);

                // 주민번호의 경우 한국 기준 총 13자리('-' 포함시 14자리) 이므로 개인정보에 해당하는 뒷 부분에 *로 치환
                String regiNo = dto.getRegiNo();
                if (regiNo.length() == 14) {
                    result = regiNo.replaceAll("-(\\d{1})(\\d{6})", "-$1******");
                } else {
                    result = "wrong value";
                }
                dto.setRegiNo(result);
            }
        }
        return joinPoint.proceed(args);
    }

}
