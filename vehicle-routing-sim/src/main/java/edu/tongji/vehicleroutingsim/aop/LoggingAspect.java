package edu.tongji.vehicleroutingsim.aop;

import edu.tongji.vehicleroutingsim.service.CarService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description:
 * 日志切面类。
 * <p>
 * 该类使用 AOP（面向切面编程）实现对服务层方法执行时间的监控。
 * 通过拦截特定包中的方法调用，记录方法的执行时间并打印日志信息。
 * </p>
 *
 * 功能特点：
 * <ul>
 *     <li>拦截指定包中所有方法调用</li>
 *     <li>计算方法的执行时间</li>
 *     <li>以统一的日志格式输出执行时间和方法签名</li>
 * </ul>
 *
 * 使用场景：
 * <p>
 * 本类适用于需要监控方法性能的场景，例如：
 * <ul>
 *     <li>分析服务层方法的执行效率</li>
 *     <li>在日志中记录方法的运行时间以便于性能优化</li>
 * </ul>
 * </p>
 *
 * 日志示例：
 * <pre>
 * 方法 [public java.util.List edu.tongji.vehicleroutingsim.service.MapService.getMap()] 运行了 15 ms
 * </pre>
 *
 * 注意事项：
 * <ul>
 *     <li>本类的切面配置只拦截 `edu.tongji.vehicleroutingsim.service` 包及其子包中的方法。</li>
 *     <li>对于返回值较大的方法，尽量避免将整个返回值输出到日志中。</li>
 * </ul>
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/21 5:40
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * 定义切入点，匹配所有 service 包下的所有类的所有方法
     */
    @Around("execution(* edu.tongji.vehicleroutingsim.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 记录开始时间
        long startTime = System.nanoTime();

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 记录结束时间
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        double time = (double) executionTime /1000000;
        // 打印日志信息
        logger.info("方法 [{}] 运行了 {} ms", joinPoint.getSignature().toShortString(), time);

        return result;
    }
}