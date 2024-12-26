package edu.tongji.vehicleroutingsim;

import edu.tongji.vehicleroutingsim.service.MapService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Description:
 * <p>
 * SpringBoot入口，启动类
 * 添加@EnableAspectJAutoProxy注解开启AOP
 * </p>
 *
 * @author KevinTung@MetaStudyline
 * @version 1.0
 * @since 2024/12/15 18:22
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class VehicleRoutingSimApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        ConfigurableApplicationContext context = SpringApplication.run(VehicleRoutingSimApplication.class, args);

        // 获取 Spring 容器中的 Bean
        MapService mapService = context.getBean(MapService.class);

        // 调用初始化方法
        if(!mapService.loadMapObject()){
            mapService.loadMapFile();
            mapService.saveMapObject();
        }
    }
}
