package edu.tongji.vehicleroutingsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Description:
 * <p>
 * SpringBoot入口，启动类
 * 添加@EnableAspectJAutoProxy注解开启AOP
 * </p>
 * @author KevinTung@MetaStudyline
 * @version 1.0
 * @since 2024/12/15 18:22
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class VehicleRoutingSimApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleRoutingSimApplication.class, args);
    }

}
