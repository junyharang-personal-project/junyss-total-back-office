package com.giggalpeople.backoffice.common.aop;

import com.giggalpeople.backoffice.common.env.Environment;
import com.giggalpeople.backoffice.common.exception.TimeTraceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.API_PROCESSING_TAKES_LONG;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.API_PROCESSING_TAKES_TOO_LONG;

/**
 * <h2><b>각 Method 경과 시간을 확인하기 위한 AOP</b></h2>
 */

@Slf4j
//@Profile(value = "!prod")
@Aspect
@Component
public class TimeTraceAOP {

    /**
     * <b>@Around를 통해 지정된 Package 안에 있는 Method의 실행 경과 시간을 확인</b>
     * @param joinPoint Application 실행 흐름 기준 특정 Point (AOP 적용 가능한 지점) 즉, Advice가 적용될 수 있는 위치, Method 실행, 생성자 호출, 멤버 변수 값 접근, static Method 접근과 같은 프로그램 실행 중의 지점 의미
     * @return Object AOP가 적용된 Method가 반환하는 참조값을 Object Type으로 반환
     * @throws Throwable 예외 처리할 수 있는 최상위 Class로 Exception과 Error는 Throwable을 상속 받는다.
     */

    @Around("@annotation(com.giggalpeople.backoffice.common.annotaion.ExecutionTimeCheck)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        log.info("Method 동작 시간 확인 시작 : "
                + joinPoint.toString()
                + "의 동작 시간 확인합니다.");

        try {
            return joinPoint.proceed();

        } finally {
            int standardExecutionMsTime = 500;
            int dangerStandardExecutionMsTime = 5000;
            long finishTime = System.currentTimeMillis();
            long progressMethodMsTime = finishTime - startTime;
            String serverEnvironment = Environment.checkServerEnvironment();

            log.info("Method 동작 시간 검증 완료 : "
                    + joinPoint
                    + " Method 동작 시간은 "
                    + progressMethodMsTime
                    + "ms "
                    + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
                    + ")초 입니다.");

//            if (progressMethodMsTime >= standardExecutionMsTime && progressMethodMsTime < dangerStandardExecutionMsTime) {
//                if (serverEnvironment.equals("local") || serverEnvironment.equals("dev")) {
//                    longTimeExceptionHandler(joinPoint, progressMethodMsTime);
//
//                    log.warn("Method 동작 시간 검증 완료 : "
//                            + "Warning!! 기준치보다 동작 시간이 높게 측정 되고 있습니다!!"
//                            + joinPoint
//                            + " Method 동작 시간은 "
//                            + progressMethodMsTime
//                            + "ms "
//                            + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
//                            + ")초 입니다.");
//                } else {
//                    log.warn("Method 동작 시간 검증 완료 : "
//                            + "Warning!! 기준치보다 동작 시간이 높게 측정 되고 있습니다!!"
//                            + joinPoint
//                            + " Method 동작 시간은 "
//                            + progressMethodMsTime
//                            + "ms "
//                            + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
//                            + ")초 입니다.");
//                }
//
//            } else if (progressMethodMsTime >= dangerStandardExecutionMsTime) {
//                if (serverEnvironment.equals("local") || serverEnvironment.equals("dev")) {
//                    veryLongTimeExceptionHandler(joinPoint, progressMethodMsTime);
//
//                    log.error("Method 동작 시간 검증 완료 : "
//                            + "Danger!! 기준치보다 동작 시간이 너무 높게 측정 되고 있습니다!!"
//                            + joinPoint
//                            + " Method 동작 시간은 "
//                            + progressMethodMsTime
//                            + "ms "
//                            + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
//                            + ")초 입니다.");
//                } else {
//                    log.error("Method 동작 시간 검증 완료 : "
//                            + "Danger!! 기준치보다 동작 시간이 너무 높게 측정 되고 있습니다!!"
//                            + joinPoint
//                            + " Method 동작 시간은 "
//                            + progressMethodMsTime
//                            + "ms "
//                            + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
//                            + ")초 입니다.");
//                }
//
//            } else {
//                log.info("Method 동작 시간 검증 완료 : "
//                        + joinPoint
//                        + " Method 동작 시간은 "
//                        + progressMethodMsTime
//                        + "ms "
//                        + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
//                        + ")초 입니다.");
//            }
        }
    }

    /**
     * <b>Controller 호출 뒤 standardExecutionMsTime 변수 ms 이상 시간 소요 시 Exception 발생을 위한 Method</b>
     * @param joinPoint 특정 관심사가 처리 되는 지점을 뜻하는 것으로 Around advice(@Around)에서만 지원되는 JoinPoint이며, JoinPoint Interface는 호출 되는 대상 객체, Method, 매개 변수 목록에 접근할 수 있는 Method 제공
     * @param progressMethodMsTime 경고를 발생 시킬 기준 ms
     */

    private void longTimeExceptionHandler(ProceedingJoinPoint joinPoint, long progressMethodMsTime) {
        throw new TimeTraceException(API_PROCESSING_TAKES_LONG, "Method 동작 시간 검증 완료 : "
                + "Warning!! 기준치보다 동작 시간이 높게 측정 되고 있습니다!!"
                + joinPoint
                + " Method 동작 시간은 "
                + progressMethodMsTime
                + "ms "
                + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
                + ")초 입니다.");
    }

    /**
     * <b>Controller 호출 뒤 progressMethodMsTime 변수 ms 이상 시간 소요 시 Exception 발생을 위한 Method</b>
     * @param joinPoint 특정 관심사가 처리 되는 지점을 뜻하는 것으로 Around advice(@Around)에서만 지원되는 JoinPoint이며, JoinPoint Interface는 호출 되는 대상 객체, Method, 매개 변수 목록에 접근할 수 있는 Method 제공
     * @param progressMethodMsTime 위험 발생 시킬 기준 ms
     */
    private void veryLongTimeExceptionHandler(ProceedingJoinPoint joinPoint, long progressMethodMsTime) {
        throw new TimeTraceException(API_PROCESSING_TAKES_TOO_LONG, "Method 동작 시간 검증 완료 : "
                + "Danger!! 기준치보다 동작 시간이 너무 높게 측정 되고 있습니다!!"
                + joinPoint
                + " Method 동작 시간은 "
                + progressMethodMsTime
                + "ms "
                + "(" + String.format("%.3f", (double) progressMethodMsTime / 1000)
                + ")초 입니다.");
    }
}
