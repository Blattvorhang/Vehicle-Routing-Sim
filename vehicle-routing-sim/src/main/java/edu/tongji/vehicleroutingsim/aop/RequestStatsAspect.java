package edu.tongji.vehicleroutingsim.aop;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * Description:
 * <p>
 * 描述类的功能与作用（这里补充具体内容）。
 * </p>
 *
 * @author KevinTung@Studyline
 * @version 1.0
 * @since 2024/12/28 00:40
 */
@Aspect
@Component
public class RequestStatsAspect {

    private final AtomicInteger requestCount = new AtomicInteger(0);

    private final ExecutorService executorService;

    private static final Logger logger = LoggerFactory.getLogger(RequestStatsAspect.class);

    public RequestStatsAspect() {
        // 创建一个固定大小的线程池
        executorService = Executors.newSingleThreadScheduledExecutor();

        // 每秒钟清空请求计数器并输出当前请求数
        executorService.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);  // 每秒刷新一次
                    int count = requestCount.getAndSet(0);  // 获取当前请求数并重置
                    if(count > 0){
                        logger.info("每秒请求次数: " + count);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Before("execution(* edu.tongji.vehicleroutingsim.controller..*(..))")  // 指定你要统计的Controller
    public void countRequest(JoinPoint joinPoint) {
        requestCount.incrementAndGet();  // 增加请求计数
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

}